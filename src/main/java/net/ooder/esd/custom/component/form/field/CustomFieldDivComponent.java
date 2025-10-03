package net.ooder.esd.custom.component.form.field;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.view.CustomDivFormViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.field.CustomDivFieldBean;
import net.ooder.esd.bean.field.combo.ComboBoxBean;
import net.ooder.esd.bean.field.combo.ComboInputFieldBean;
import net.ooder.esd.custom.CustomViewConfigFactory;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.DivComponent;
import net.ooder.esd.tool.properties.DivProperties;
import net.ooder.server.httpproxy.core.AbstractHandler;
import ognl.OgnlException;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomFieldDivComponent extends DivComponent {
    @JSONField(serialize = false)
    Map<String, Object> valueMap = new HashMap<>();
    @JSONField(serialize = false)
    EUModule euModule;


    public CustomFieldDivComponent(EUModule euModule, FieldFormConfig<CustomDivFieldBean, ?> field, String target, Object value, Map<String, Object> valueMap) {
        super(field.getFieldname());
        CustomDivFormViewBean divViewBean = (CustomDivFormViewBean) field.getMethodConfig().getView();
        DivProperties divProperties = this.getProperties();
        Component component = null;
        CustomDivFieldBean fieldBean = field.getWidgetConfig();
        if (fieldBean != null) {
            divProperties.init(fieldBean);
            component = fieldBean.getComponent();
        }
        if (divViewBean != null) {
            init(euModule, divViewBean, valueMap);
            divProperties.initCustomBean(divViewBean);
            component = divViewBean.getComponent();
        }


        if (component != null) {
            if (component.getCS() != null) {
                this.CS = component.getCS();
            }
            this.events = component.getEvents();
        }

    }

    public CustomFieldDivComponent(EUModule euModule, FieldFormConfig<CustomDivFieldBean, ?> field, Map valueMap) {
        super(field.getFieldname());
        CustomDivFormViewBean divViewBean = (CustomDivFormViewBean) field.getMethodConfig().getView();
        DivProperties divProperties = this.getProperties();
        Component component = null;
        CustomDivFieldBean fieldBean = field.getWidgetConfig();
        if (fieldBean != null) {
            divProperties.init(fieldBean);
            component = fieldBean.getComponent();
        }
        if (divViewBean != null) {
            init(euModule, divViewBean, valueMap);
            divProperties.initCustomBean(divViewBean);
            component = divViewBean.getComponent();
        }


        if (component != null) {
            if (component.getCS() != null) {
                this.CS = component.getCS();
            }
            this.events = component.getEvents();
        }
    }

    public CustomFieldDivComponent(EUModule euModule, MethodConfig methodConfig, Map valueMap) {
        super(methodConfig.getFieldName());
        CustomDivFormViewBean customComponentViewBean = (CustomDivFormViewBean) methodConfig.getView();
        init(euModule, customComponentViewBean, valueMap);

    }

    void init(EUModule euModule, CustomDivFormViewBean divViewBean, Map dbMap) {
        this.euModule = euModule;
        List<FieldFormConfig> formFieldList = divViewBean.getAllFields();
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
            Component inputComponent = null;
            if (fieldInfo.getEsdField() != null) {
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
                                inputComponent = new CustomFieldInputComponent(euModule, fieldInfo, null, value, valueMap);
                            }
                    }
                }
            }


            this.addChildren(inputComponent);
        }
    }


}
