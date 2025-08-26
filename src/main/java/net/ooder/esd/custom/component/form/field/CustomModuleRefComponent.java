package net.ooder.esd.custom.component.form.field;

import net.ooder.common.JDSException;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.field.FieldBean;
import net.ooder.esd.bean.field.combo.CustomModuleRefFieldBean;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.engine.ProjectVersion;
import net.ooder.esd.tool.component.ModuleComponent;

import java.util.Map;

public class CustomModuleRefComponent extends ModuleComponent {
    public CustomModuleRefComponent(EUModule module, FieldFormConfig<CustomModuleRefFieldBean, ?> field, String target, Object value, Map<String, ?> valueMap) {
        CustomModuleRefFieldBean moduleRefFieldBean = field.getWidgetConfig();
        this.getProperties().setId(field.getId());
        this.getProperties().setName(field.getFieldname());
        this.getProperties().setDesc(field.getAggConfig().getCaption());
        this.setAlias(field.getFieldname());

        FieldBean fieldBean = field.getFieldBean();

        MethodConfig sourceMethodConfig = module.getComponent().getMethodAPIBean();
        try {
            ProjectVersion version = ESDFacrory.getAdminESDClient().getProjectVersionByName(module.getProjectVersion().getVersionName());
            ApiClassConfig classConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(sourceMethodConfig.getSourceClassName());
            MethodConfig fieldMethodConfig = null;
            if (moduleRefFieldBean.getBindClass() != null && !moduleRefFieldBean.getBindClass().equals(Void.class) && !moduleRefFieldBean.getBindClass().equals(Enum.class)) {
                ApiClassConfig bindConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(moduleRefFieldBean.getBindClass().getName());
                fieldMethodConfig = bindConfig.getMethodByItem(CustomMenuItem.INDEX);

            } else if (moduleRefFieldBean.getSrc() != null && !moduleRefFieldBean.getSrc().equals("")) {
                fieldMethodConfig = classConfig.getMethodByName(moduleRefFieldBean.getSrc());
                if (fieldMethodConfig == null) {
                    fieldMethodConfig = ESDFacrory.getAdminESDClient().getMethodAPIBean(moduleRefFieldBean.getSrc(), projectName);
                }
            } else {
                ApiClassConfig bindConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(field.getViewClassName());
                fieldMethodConfig = bindConfig.getMethodByName(field.getFieldname());
            }
            if (fieldMethodConfig != null && fieldMethodConfig.getUrl() != null) {

                if (fieldMethodConfig.getView() != null && !fieldMethodConfig.getView().getModuleViewType().equals(ModuleViewType.DYNCONFIG)) {
                    EUModule newmodule = ESDFacrory.getAdminESDClient().getCustomModule(fieldMethodConfig, version.getVersionName(), valueMap);
                    if (newmodule != null) {
                        this.setClassName(newmodule.getClassName());
                    }
                } else if (fieldMethodConfig.getEUClassName() != null) {
                    this.setClassName(fieldMethodConfig.getEUClassName());
                }
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }

        this.setTarget(target);
    }

}
