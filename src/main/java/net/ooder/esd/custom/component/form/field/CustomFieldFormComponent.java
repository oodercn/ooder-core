package net.ooder.esd.custom.component.form.field;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.PositionType;
import net.ooder.esd.bean.field.CustomFormLayoutFieldBean;
import net.ooder.esd.bean.view.CustomFormViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.field.combo.ComboBoxBean;
import net.ooder.esd.bean.field.combo.ComboInputFieldBean;
import net.ooder.esd.custom.CustomViewConfigFactory;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.FormLayoutComponent;
import net.ooder.esd.tool.properties.form.FormLayoutProperties;
import ognl.OgnlException;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomFieldFormComponent extends FormLayoutComponent {
    @JSONField(serialize = false)
    Map<String, Object> valueMap = new HashMap<>();
    @JSONField(serialize = false)
    EUModule euModule;


    public CustomFieldFormComponent(EUModule euModule, FieldFormConfig<CustomFormLayoutFieldBean, ?> field, String target, Object value, Map valueMap) {
        this.setAlias(field.getFieldname());
        //存在集合属性
        if (field.getMethodConfig() != null && field.getMethodConfig().getView() != null && field.getMethodConfig().getView() instanceof CustomFormViewBean) {
            CustomFormViewBean customComponentViewBean = (CustomFormViewBean) field.getMethodConfig().getView();
            init(euModule, customComponentViewBean, valueMap);
            FormLayoutProperties blockProperties = new FormLayoutProperties(customComponentViewBean,field.getWidgetConfig());
            this.setProperties(blockProperties);
            this.setTarget(target);

        }


        Component component = field.getWidgetConfig().getComponent();
        if (component != null) {
            if (component.getCS() != null) {
                this.CS = component.getCS();
            }
            this.events = component.getEvents();
        }


    }


    public CustomFieldFormComponent(EUModule euModule, MethodConfig methodConfig, Map dbMap) {

        CustomFormViewBean customComponentViewBean = (CustomFormViewBean) methodConfig.getView();
        FormLayoutProperties blockProperties = new FormLayoutProperties();
        this.setProperties(blockProperties);
        this.setAlias(methodConfig.getFieldName());
        init(euModule, customComponentViewBean, dbMap);
    }


    void init(EUModule euModule, CustomFormViewBean customComponentViewBean, Map dbMap) {
        List<FieldFormConfig> formFieldList = customComponentViewBean.getAllFields();
        this.euModule = euModule;
        List<FieldFormConfig> fieldList = new ArrayList<>();
        for (FieldFormConfig fieldFormConfig : formFieldList) {
            if ((fieldFormConfig.getColHidden() == null || !fieldFormConfig.getColHidden())  && !fieldFormConfig.getComponentType().equals(ComponentType.HIDDENINPUT)) {
                fieldList.add(fieldFormConfig);
            }
        }
        if (dbMap != null) {
            this.valueMap = dbMap;
        }
        for (FieldFormConfig fieldInfo : fieldList) {

            Component inputComponent = null;

            Class fieldClass = fieldInfo.getEsdField().getReturnType();
            if (Component.class.isAssignableFrom(fieldClass)) {

                if (fieldInfo.getMethodConfig()!=null && fieldInfo.getMethodConfig().getRequestMethodBean()!=null){
                    try {
                        inputComponent = (Component)fieldInfo.getMethodConfig().getRequestMethodBean().invok(JDSActionContext.getActionContext().getOgnlContext(), JDSActionContext.getActionContext().getContext());
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (OgnlException e) {
                        e.printStackTrace();
                    }
                }


            } else {
                ComponentType componentType = fieldInfo.getComponentType();
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

    private Component createComponent(FieldFormConfig fieldInfo, String target, Object value, Map valueMap) {
        Component inputComponent = null;
        ComponentType componentType = fieldInfo.getComponentType();
        if (fieldInfo.getComboConfig() != null && (fieldInfo.getWidgetConfig() instanceof ComboInputFieldBean) && !componentType.equals(ComponentType.MODULE)) {
            componentType = ComponentType.COMBOINPUT;
        }
        if (fieldInfo.getMethodConfig() != null && fieldInfo.getMethodConfig().getApi() != null) {
            componentType = ComponentType.APICALLER;
        }

        if (value == null && valueMap != null) {
            value = valueMap.get(fieldInfo.getFieldname());
        }

        switch (componentType) {
            case COMBOINPUT:
                ComboBoxBean comboBoxBean = fieldInfo.getComboConfig();
                Class<Component> defaultFieldClass = CustomViewConfigFactory.getInstance().getComboBoxComponent(comboBoxBean.getInputType());
                try {
                    if (defaultFieldClass != null) {
                        Constructor<Component> constructor = defaultFieldClass.getConstructor(new Class[]{EUModule.class, FieldFormConfig.class, String.class, Object.class, Map.class});
                        inputComponent = constructor.newInstance(euModule, fieldInfo, target, value, valueMap);
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
                            inputComponent = constructor.newInstance(euModule, fieldInfo, target, value, valueMap);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    inputComponent = new CustomHiddenInputComponent(euModule, fieldInfo, target, value, valueMap);
                }
        }
        return inputComponent;
    }

    ;

}
