package net.ooder.esd.custom.component.form.field;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.field.CustomGroupFieldBean;
import net.ooder.esd.bean.field.combo.ComboBoxBean;
import net.ooder.esd.bean.field.combo.ComboInputFieldBean;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.engine.ProjectVersion;
import net.ooder.esd.bean.view.CustomGroupFormViewBean;
import net.ooder.esd.custom.CustomViewConfigFactory;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.GroupComponent;
import net.ooder.esd.tool.properties.ContainerProperties;
import net.ooder.esd.tool.properties.GroupProperties;
import net.ooder.esd.tool.properties.ModuleProperties;
import net.ooder.server.httpproxy.core.AbstractHandler;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomFieldGroupComponent extends GroupComponent {
    @JSONField(serialize = false)
    Map<String, Object> valueMap = new HashMap<>();
    @JSONField(serialize = false)
    EUModule euModule;


    public CustomFieldGroupComponent(EUModule module, FieldFormConfig<CustomGroupFieldBean, ?> field, String target, Object value, Map<String, Object> valueMap) {

        this.setAlias(field.getFieldname());
        this.euModule = module;
        //存在集合属性
        CustomGroupFieldBean groupFieldBean = field.getWidgetConfig();
        AppendType appendType = groupFieldBean.getAppend();
        String projectName = module.getProjectVersion().getVersionName();
        GroupProperties properties = this.getProperties();
        properties.init(field.getContainerBean());
        properties.setId(field.getId());
        properties.setDock(groupFieldBean.getDock());
        properties.setOverflow(OverflowType.hidden);
        properties.setName(field.getFieldname());
        properties.setCaption(groupFieldBean.getCaption());
        properties.setDesc(groupFieldBean.getCaption());
        MethodConfig fieldMethodConfig = null;
        try {
            MethodConfig sourceMethodConfig = module.getComponent().getMethodAPIBean();
            ProjectVersion version = ESDFacrory.getAdminESDClient().getProjectVersionByName(projectName);
            ApiClassConfig classConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(sourceMethodConfig.getSourceClassName());
            if (groupFieldBean.getBindClass() != null && !groupFieldBean.getBindClass().equals(Void.class)) {
                ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(groupFieldBean.getBindClass().getName());
                fieldMethodConfig = apiClassConfig.findEditorMethod();
            } else if (groupFieldBean.getSrc() != null && !groupFieldBean.getSrc().equals("")) {
                fieldMethodConfig = classConfig.getMethodByName(groupFieldBean.getSrc());
                if (fieldMethodConfig == null) {
                    fieldMethodConfig = ESDFacrory.getAdminESDClient().getMethodAPIBean(groupFieldBean.getSrc(), version.getVersionName());
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
                properties.setEuClassName(newmodule.getClassName());
                switch (appendType) {
                    case ref:
                        ModuleComponent moduleComponent = new ModuleComponent();
                        ModuleProperties moduleProperties = moduleComponent.getProperties();
                        properties.setComboType(ComponentType.MODULE);
                        String moduleAlias = field.getFieldname() + "Module";
                        moduleProperties.setName(newmodule.getSimpleClassName());
                        moduleComponent.setAlias(moduleAlias);
                        moduleComponent.setClassName(newmodule.getClassName());
                        this.addChildren(moduleComponent);
                        break;
                    case append:
                        Component currComponent = newmodule.getComponent().getCurrComponent();
                        this.addChildren(currComponent);
                        Dock dock = this.getProperties().getDock();
                        if (dock != null && !dock.equals(Dock.fill) && (currComponent.getProperties() instanceof ContainerProperties)) {
                            ((ContainerProperties) currComponent.getProperties()).setDock(Dock.fill);
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


    public CustomFieldGroupComponent(EUModule euModule, MethodConfig methodConfig, Map dbMap) {
        CustomGroupFormViewBean customComponentViewBean = (CustomGroupFormViewBean) methodConfig.getView();
        GroupProperties groupProperties = new GroupProperties(customComponentViewBean.getContainerBean());
        this.setProperties(groupProperties);
        this.setAlias(methodConfig.getFieldName());
        this.euModule = euModule;
        if (dbMap != null) {
            this.valueMap = dbMap;
        }

        List<FieldFormConfig> formFieldList = customComponentViewBean.getAllFields();
        List<FieldFormConfig> fieldList = new ArrayList<>();
        for (FieldFormConfig fieldFormConfig : formFieldList) {
            if ((fieldFormConfig.getColHidden() == null || !fieldFormConfig.getColHidden()) && !fieldFormConfig.getComponentType().equals(ComponentType.HIDDENINPUT)) {
                fieldList.add(fieldFormConfig);
            }
        }

        for (FieldFormConfig fieldInfo : fieldList) {
            Component inputComponent = null;
            if (fieldInfo.getEsdField() != null && Component.class.isAssignableFrom(fieldInfo.getEsdField().getReturnType())) {
                Object handle = JDSActionContext.getActionContext().getHandle();
                if (handle != null && handle instanceof AbstractHandler) {
                    AbstractHandler abstractHandler = (AbstractHandler) handle;
                    inputComponent = (Component) abstractHandler.invokMethod(fieldInfo.getMethodConfig().getRequestMethodBean());
                }
            } else {
                ComponentType componentType = fieldInfo.getComponentType();
                if (fieldInfo.getWidgetConfig() != null && fieldInfo.getWidgetConfig().getComponentType() != null) {
                    componentType = fieldInfo.getWidgetConfig().getComponentType();
                }

                if (fieldInfo.getComboConfig() != null && (fieldInfo.getWidgetConfig() instanceof ComboInputFieldBean) && !componentType.equals(ComponentType.MODULE)) {
                    componentType = ComponentType.COMBOINPUT;
                }
                Object value = null;
                if (valueMap != null) {
                    value = valueMap.get(fieldInfo.getFieldname());
                    valueMap.put("parent", this);
                }

                switch (componentType) {
                    case COMBOINPUT:
                        ComboBoxBean comboBoxBean = fieldInfo.getComboConfig();
                        Class<Component> defaultFieldClass = CustomViewConfigFactory.getInstance().getComboBoxComponent(comboBoxBean.getInputType());
                        try {
                            if (defaultFieldClass != null) {
                                Constructor<Component> constructor = defaultFieldClass.getConstructor(new Class[]{EUModule.class, FieldFormConfig.class, String.class, Object.class, Map.class});
                                inputComponent = constructor.newInstance(euModule, fieldInfo, null, value, valueMap);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    default:
                        defaultFieldClass = CustomViewConfigFactory.getInstance().getComponent(componentType);
                        if (defaultFieldClass != null) {
                            try {
                                if (defaultFieldClass != null) {
                                    Constructor<Component> constructor = defaultFieldClass.getConstructor(new Class[]{EUModule.class, FieldFormConfig.class, String.class, Object.class, Map.class});
                                    inputComponent = constructor.newInstance(euModule, fieldInfo, null, value, valueMap);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            inputComponent = new CustomHiddenInputComponent(euModule, fieldInfo, null, value, valueMap);
                        }
                }
            }
            if (inputComponent.getKey().equals(ComponentType.MQTT.getClassName())) {
                this.euModule.getComponent().addChildren(inputComponent);
            } else if (inputComponent.getKey().equals(ComponentType.APICALLER.getClassName())) {
                this.euModule.getComponent().addChildren(inputComponent);
            } else {
                PositionType positionType = fieldInfo.getFieldBean().getInnerPosition();
                if (positionType == null || positionType.equals(PositionType.inner)) {
                    this.addChildren(inputComponent);
                } else {
                    this.euModule.getComponent().addChildren(inputComponent);
                }
            }
        }
    }
}
