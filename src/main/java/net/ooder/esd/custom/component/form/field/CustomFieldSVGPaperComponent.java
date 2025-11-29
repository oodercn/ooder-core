package net.ooder.esd.custom.component.form.field;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.util.TypeUtils;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.PositionType;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.field.CustomSVGPaperFieldBean;
import net.ooder.esd.bean.field.combo.ComboBoxBean;
import net.ooder.esd.bean.field.combo.ComboInputFieldBean;
import net.ooder.esd.bean.view.CustomSVGPaperViewBean;
import net.ooder.esd.custom.CustomViewConfigFactory;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.SVGPaperComponent;
import net.ooder.esd.tool.properties.svg.SVGPaperProperties;
import ognl.OgnlException;

import java.lang.reflect.Constructor;
import java.util.*;

public class CustomFieldSVGPaperComponent extends SVGPaperComponent {
    @JSONField(serialize = false)
    Map<String, Object> valueMap = new HashMap<>();
    @JSONField(serialize = false)
    EUModule euModule;


    public CustomFieldSVGPaperComponent(EUModule euModule, FieldFormConfig<CustomSVGPaperFieldBean, ?> field, String target, Object value, Map<String, Object> valueMap) {
        CustomSVGPaperViewBean customComponentViewBean = (CustomSVGPaperViewBean) field.getMethodConfig().getView();

        this.setAlias(field.getFieldname());
        init(euModule, customComponentViewBean, valueMap);
        this.setTarget(target);
        Component component = field.getWidgetConfig().getComponent();
        if (component != null) {
            if (component.getCS() != null) {
                this.CS = component.getCS();
            }
            this.events = component.getEvents();
        }

    }

    public CustomFieldSVGPaperComponent(EUModule euModule, MethodConfig methodConfig, Map dbMap) {
        CustomSVGPaperViewBean customComponentViewBean = (CustomSVGPaperViewBean) methodConfig.getView();
        this.setAlias(methodConfig.getFieldName());
        init(euModule, customComponentViewBean, dbMap);
    }


    void init(EUModule euModule, CustomSVGPaperViewBean customComponentViewBean, Map dbMap) {
        List<FieldFormConfig> formFieldList = customComponentViewBean.getAllFields();
        SVGPaperProperties blockProperties = new SVGPaperProperties(customComponentViewBean.getSvgPaperFieldBean(), customComponentViewBean.getContainerBean());
        this.setProperties(blockProperties);
        this.euModule = euModule;
        List<FieldFormConfig> fieldList = new ArrayList<>();
        for (FieldFormConfig fieldFormConfig : formFieldList) {
            if ((fieldFormConfig.getColHidden() == null || !fieldFormConfig.getColHidden()) && !fieldFormConfig.getComponentType().equals(ComponentType.HIDDENINPUT)) {
                fieldList.add(fieldFormConfig);
            }
        }
        if (dbMap != null) {
            this.valueMap = dbMap;
        }

        for (FieldFormConfig fieldInfo : fieldList) {
            if (fieldInfo.getEsdField() != null) {
                Class fieldClass = fieldInfo.getEsdField().getReturnType();
                Component svgComponent = null;
                Object value = null;
                if (fieldInfo.getMethodConfig() != null
                        && fieldInfo.getMethodConfig().getRequestMethodBean() != null
                        && fieldInfo.getMethodConfig().getApi() == null) {

                    if (fieldInfo.getMethodConfig() != null && fieldInfo.getMethodConfig().getRequestMethodBean() != null && fieldInfo.getMethodConfig().getApi() == null) {
                        try {
                            value = fieldInfo.getMethodConfig().getRequestMethodBean().invok(JDSActionContext.getActionContext().getOgnlContext(), JDSActionContext.getActionContext().getContext());
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (OgnlException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (value != null && (value.getClass().isArray() || Collection.class.isAssignableFrom(value.getClass()))) {
                    ArrayList<Object> objs = TypeUtils.castToJavaBean(value, ArrayList.class);
                    for (Object obj : objs) {
                        svgComponent = createComponent(fieldInfo, obj);
                        PositionType positionType = fieldInfo.getFieldBean().getInnerPosition();
                        if (positionType == null || positionType.equals(PositionType.inner)) {
                            this.addChildren(svgComponent);
                        } else {
                            this.euModule.getComponent().getMainBoxComponent().addChildren(svgComponent);
                        }
                    }

                } else {
                    if (Component.class.isAssignableFrom(fieldClass)) {
                        svgComponent = (Component) value;
                    } else {
                        svgComponent = createComponent(fieldInfo, value);
                    }
                    if (svgComponent.getKey().equals(ComponentType.MQTT.getClassName())) {
                        this.euModule.getComponent().addChildren(svgComponent);
                    } else if (svgComponent.getKey().equals(ComponentType.APICALLER.getClassName())) {
                        this.euModule.getComponent().addChildren(svgComponent);
                    } else {
                        PositionType positionType = fieldInfo.getFieldBean().getInnerPosition();
                        if (positionType == null || positionType.equals(PositionType.inner)) {
                            this.addChildren(svgComponent);
                        } else {
                            this.euModule.getComponent().getMainBoxComponent().addChildren(svgComponent);
                        }
                    }
                }
            }

        }
    }

    private Component createComponent(FieldFormConfig fieldInfo, Object value) {
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
                    inputComponent = new CustomFieldInputComponent(euModule, fieldInfo, null, value, valueMap);
                }
        }
        return inputComponent;
    }

}
