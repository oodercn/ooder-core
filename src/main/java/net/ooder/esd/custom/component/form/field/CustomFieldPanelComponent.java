package net.ooder.esd.custom.component.form.field;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.PositionType;
import net.ooder.esd.bean.CustomDivBean;
import net.ooder.esd.bean.CustomPanelBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.field.CustomBlockFieldBean;
import net.ooder.esd.bean.field.CustomPanelFieldBean;
import net.ooder.esd.bean.field.combo.ComboBoxBean;
import net.ooder.esd.bean.field.combo.ComboInputFieldBean;
import net.ooder.esd.bean.view.CustomBlockFormViewBean;
import net.ooder.esd.bean.view.CustomModuleBean;
import net.ooder.esd.bean.view.CustomPanelFormViewBean;
import net.ooder.esd.custom.CustomViewConfigFactory;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.PanelComponent;
import net.ooder.esd.tool.properties.PanelProperties;
import ognl.OgnlException;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomFieldPanelComponent extends PanelComponent<PanelProperties> {
    @JSONField(serialize = false)
    Map<String, Object> valueMap = new HashMap<>();
    @JSONField(serialize = false)
    EUModule euModule;


    public CustomFieldPanelComponent(EUModule euModule, FieldFormConfig<CustomPanelFieldBean, ?> field, String target, Object value, Map<String, Object> valueMap) {
        CustomPanelFormViewBean customComponentViewBean = (CustomPanelFormViewBean) field.getMethodConfig().getView();
        this.setAlias(field.getFieldname());
        //存在集合属性
        CustomPanelFieldBean panelFieldBean = field.getWidgetConfig();
        Component component = field.getWidgetConfig().getComponent();
        if (component != null) {
            if (component.getCS() != null) {
                this.CS = component.getCS();
            }
            this.events = component.getEvents();
        }


        if (field.getMethodConfig() != null && field.getMethodConfig().getView() != null && field.getMethodConfig().getView() instanceof CustomBlockFormViewBean) {
            CustomModuleBean customModuleBean = customComponentViewBean.getModuleBean();
            if (customModuleBean == null && customComponentViewBean.getMethodConfig() != null) {
                customModuleBean = customComponentViewBean.getMethodConfig().getModuleBean();
            }
            CustomPanelBean panelBean = customModuleBean.getPanelBean();
            PanelProperties panelProperties = new PanelProperties(panelBean);
            panelProperties.init(panelFieldBean);
            this.setProperties(panelProperties);
            init(euModule, customComponentViewBean, valueMap);
        } else {
            panelFieldBean.setContainerBean(field.getContainerBean());
            PanelProperties blockProperties = new PanelProperties(panelFieldBean);
            this.setProperties(blockProperties);
        }
        this.setTarget(target);
    }


    public CustomFieldPanelComponent(EUModule euModule, MethodConfig methodConfig, Map dbMap) {
        CustomPanelFormViewBean customComponentViewBean = (CustomPanelFormViewBean) methodConfig.getView();
        if (methodConfig.getModuleBean() != null && methodConfig.getModuleBean().getBlockBean() != null) {
            CustomPanelBean panelBean = methodConfig.getModuleBean().getPanelBean();
            if (customComponentViewBean.getContainerBean() != null && panelBean != null) {
                if (panelBean.getDivBean() == null) {
                    panelBean.setDivBean(new CustomDivBean());
                }
                panelBean.getDivBean().setContainerBean(customComponentViewBean.getContainerBean());
            }
            PanelProperties panelProperties = new PanelProperties(panelBean);
            this.setProperties(panelProperties);
        }
        this.setAlias(methodConfig.getFieldName());
        init(euModule, customComponentViewBean, dbMap);
    }


    void init(EUModule euModule, CustomPanelFormViewBean customComponentViewBean, Map dbMap) {
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
                if (fieldInfo.getMethodConfig() != null && fieldInfo.getMethodConfig().getRequestMethodBean() != null) {
                    try {
                        inputComponent = (Component) fieldInfo.getMethodConfig().getRequestMethodBean().invok(JDSActionContext.getActionContext().getOgnlContext(), JDSActionContext.getActionContext().getContext());
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (OgnlException e) {
                        e.printStackTrace();
                    }
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
