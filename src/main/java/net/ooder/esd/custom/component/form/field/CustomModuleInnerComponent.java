package net.ooder.esd.custom.component.form.field;

import net.ooder.common.JDSConstants;
import net.ooder.common.JDSException;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.field.CustomBlockFieldBean;
import net.ooder.esd.bean.field.combo.CustomModuleRefFieldBean;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.engine.ProjectVersion;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.BlockComponent;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.properties.BlockProperties;
import net.ooder.esd.tool.properties.ContainerProperties;
import net.ooder.esd.tool.properties.ModuleProperties;

import java.util.List;
import java.util.Map;

public class CustomModuleInnerComponent extends BlockComponent {
    private static final Log log = LogFactory.getLog(JDSConstants.CONFIG_KEY, CustomModuleInnerComponent.class);

    public CustomModuleInnerComponent(EUModule module, FieldFormConfig<CustomModuleRefFieldBean, ?> field, String target, Object value, Map<String, ?> valueMap) {
        super(Dock.none, field.getFieldname() + CustomBlockFieldBean.skipStr);
        CustomModuleRefFieldBean moduleRefFieldBean = field.getWidgetConfig();
        AppendType appendType = moduleRefFieldBean.getAppend();
        String projectName = module.getProjectVersion().getVersionName();
        BlockProperties properties = this.getProperties();
        properties.init(field.getContainerBean());
        properties.setDock(moduleRefFieldBean.getDock());
        properties.setId(field.getId());
        properties.setOverflow(OverflowType.hidden);
        properties.setName(field.getFieldname());
        properties.setDesc(field.getAggConfig().getCaption());
        MethodConfig fieldMethodConfig = null;
        try {
            MethodConfig sourceMethodConfig = module.getComponent().getMethodAPIBean();
            ProjectVersion version = ESDFacrory.getAdminESDClient().getProjectVersionByName(projectName);
            ApiClassConfig classConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(sourceMethodConfig.getSourceClassName());
            if (moduleRefFieldBean.getBindClass() != null && !moduleRefFieldBean.getBindClass().equals(Void.class)) {
                ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(moduleRefFieldBean.getBindClass().getName());
                fieldMethodConfig = apiClassConfig.findEditorMethod();
            } else if (moduleRefFieldBean.getSrc() != null && !moduleRefFieldBean.getSrc().equals("")) {
                fieldMethodConfig = classConfig.getMethodByName(moduleRefFieldBean.getSrc());
                if (fieldMethodConfig == null) {
                    fieldMethodConfig = ESDFacrory.getAdminESDClient().getMethodAPIBean(moduleRefFieldBean.getSrc(), version.getVersionName());
                }
            } else {
                ApiClassConfig bindConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(field.getViewClassName());
                fieldMethodConfig = bindConfig.getMethodByName(field.getFieldname());
            }
            EUModule newmodule = null;

            if (fieldMethodConfig != null && fieldMethodConfig.getUrl() != null) {
                if (fieldMethodConfig.getView() != null && !fieldMethodConfig.getView().getModuleViewType().equals(ModuleViewType.DYNCONFIG)) {
                    newmodule = ESDFacrory.getAdminESDClient().getCustomModule(fieldMethodConfig, projectName, valueMap);
                } else if (fieldMethodConfig.getEUClassName() != null) {
                    newmodule = ESDFacrory.getAdminESDClient().getModule(fieldMethodConfig.getEUClassName(), projectName);
                }
            }

            if (newmodule != null) {
                switch (appendType) {
                    case ref:
                        ModuleComponent moduleComponent = new ModuleComponent();
                        ModuleProperties moduleProperties = moduleComponent.getProperties();
                        properties.setComboType(ComponentType.MODULE);
                        moduleProperties.setName(field.getFieldname());
                        moduleComponent.setAlias(field.getFieldname());
                        moduleComponent.setClassName(newmodule.getClassName());
                        this.addChildren(moduleComponent);
                        break;
                    case append:
                        Component component = newmodule.getComponent().getCurrComponent();
                        this.addChildren(component);
                        Dock dock = this.getProperties().getDock();
                        if (dock != null && !dock.equals(Dock.fill) && (component.getProperties() instanceof ContainerProperties)) {
                            ((ContainerProperties) component.getProperties()).setDock(Dock.fill);
                        }
                        if (this.getProperties().getBackground() == null) {
                            this.getProperties().setBackground("transparent");
                        }

                        List<Component> apiComponents = newmodule.getComponent().findComponents(ComponentType.APICALLER, null);
                        for (Component apiCom : apiComponents) {
                            module.getComponent().addChildren(apiCom);
                        }
                        break;
                }
            }

        } catch (JDSException e) {
            e.printStackTrace();
        }
        this.setProperties(properties);
        this.setTarget(target);
    }


}
