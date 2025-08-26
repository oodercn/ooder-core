package net.ooder.esd.custom.component.form.field;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.view.CustomSVGPaperViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.field.CustomSVGPaperFieldBean;
import net.ooder.esd.bean.field.combo.ComboBoxBean;
import net.ooder.esd.bean.field.combo.ComboInputFieldBean;
import net.ooder.esd.custom.CustomViewConfigFactory;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.SVGPaperComponent;
import net.ooder.esd.tool.properties.svg.SVGPaperProperties;
import net.ooder.server.httpproxy.core.AbstractHandler;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomSVGPaperComponent extends SVGPaperComponent {
    @JSONField(serialize = false)
    Map<String, Object> valueMap = new HashMap<>();
    @JSONField(serialize = false)
    EUModule euModule;


    public CustomSVGPaperComponent(EUModule euModule, FieldFormConfig<CustomSVGPaperFieldBean, ?> field, String target, Object value, Map<String, Object> valueMap) {
        super(field.getFieldname());
        CustomSVGPaperViewBean customComponentViewBean = (CustomSVGPaperViewBean) field.getMethodConfig().getView();
        init(euModule, customComponentViewBean, valueMap);

    }

    public CustomSVGPaperComponent(EUModule euModule, FieldFormConfig<CustomSVGPaperFieldBean, ?> field, Map valueMap) {
        super(field.getFieldname());
        CustomSVGPaperViewBean customComponentViewBean = (CustomSVGPaperViewBean) field.getMethodConfig().getView();
        this.setAlias(field.getFieldname());
        init(euModule, customComponentViewBean, valueMap);

    }

    public CustomSVGPaperComponent(EUModule euModule, MethodConfig methodConfig, Map valueMap) {
        super(methodConfig.getFieldName());
        CustomSVGPaperViewBean customComponentViewBean = (CustomSVGPaperViewBean) methodConfig.getView();
        init(euModule, customComponentViewBean, valueMap);

    }


    void init(EUModule euModule, CustomSVGPaperViewBean customComponentViewBean, Map dbMap) {

        SVGPaperProperties divProperties = new SVGPaperProperties(customComponentViewBean.getSvgPaperFieldBean(),customComponentViewBean.getContainerBean());
        this.setProperties( divProperties);
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
                Object handle = JDSActionContext.getActionContext().getHandle();
                if (handle != null && handle instanceof AbstractHandler) {
                    AbstractHandler abstractHandler = (AbstractHandler) handle;
                    inputComponent = (Component) abstractHandler.invokMethod(fieldInfo.getMethodConfig().getRequestMethodBean());
                }
            } else {
                ComponentType componentType = fieldInfo.getComponentType();
                if (fieldInfo.getComboConfig() != null && (fieldInfo.getWidgetConfig() instanceof ComboInputFieldBean) && !componentType.equals(ComponentType.MODULE)) {
                    componentType = ComponentType.COMBOINPUT;
                }
                Object value = null;
                if (valueMap != null) {
                    value = valueMap.get(fieldInfo.getFieldname());
                    valueMap.put("parent",this);
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
            }


            this.addChildren(inputComponent);
        }
    }


}
