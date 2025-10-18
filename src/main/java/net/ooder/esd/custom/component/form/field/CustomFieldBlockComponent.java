package net.ooder.esd.custom.component.form.field;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.Label;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.CustomBlockBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.field.CustomBlockFieldBean;
import net.ooder.esd.bean.field.LabelBean;
import net.ooder.esd.bean.field.combo.ComboBoxBean;
import net.ooder.esd.bean.field.combo.ComboInputFieldBean;
import net.ooder.esd.bean.view.CustomBlockFormViewBean;
import net.ooder.esd.bean.view.CustomModuleBean;
import net.ooder.esd.custom.CustomViewConfigFactory;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.BlockComponent;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.properties.BlockProperties;
import net.ooder.web.util.AnnotationUtil;
import ognl.OgnlException;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomFieldBlockComponent extends BlockComponent {
    @JSONField(serialize = false)
    Map<String, Object> valueMap = new HashMap<>();
    @JSONField(serialize = false)
    EUModule euModule;


    public CustomFieldBlockComponent(EUModule euModule, FieldFormConfig<CustomBlockFieldBean, ?> field, String target, Object value, Map valueMap) {
        this.setAlias(field.getFieldname());
        //存在集合属性
        CustomBlockFieldBean blockFieldBean = field.getWidgetConfig();
        if (field.getMethodConfig() != null && field.getMethodConfig().getView() != null && field.getMethodConfig().getView() instanceof CustomBlockFormViewBean) {
            CustomBlockFormViewBean customComponentViewBean = (CustomBlockFormViewBean) field.getMethodConfig().getView();
            CustomModuleBean customModuleBean = customComponentViewBean.getModuleBean();
            if (customModuleBean == null && customComponentViewBean.getMethodConfig() != null) {
                customModuleBean = customComponentViewBean.getMethodConfig().getModuleBean();
            }
            Component component = blockFieldBean.getComponent();
            if (component != null) {
                if (component.getCS() != null) {
                    this.CS = component.getCS();
                }
                this.events = component.getEvents();
            }

            CustomBlockBean blockBean = customModuleBean.getBlockBean();
            BlockProperties blockProperties = new BlockProperties(blockBean);
            blockProperties.init(blockFieldBean);
            this.setProperties(blockProperties);
            init(euModule, customComponentViewBean, valueMap);
        } else {
            blockFieldBean.setContainerBean(field.getContainerBean());
            BlockProperties blockProperties = new BlockProperties(blockFieldBean);
            this.setProperties(blockProperties);
        }
        this.setTarget(target);
    }


    public CustomFieldBlockComponent(EUModule euModule, MethodConfig methodConfig, Map dbMap) {
        CustomBlockFormViewBean customComponentViewBean = (CustomBlockFormViewBean) methodConfig.getView();
        if (methodConfig.getModuleBean() != null && methodConfig.getModuleBean().getBlockBean() != null) {
            CustomBlockBean blockBean = methodConfig.getModuleBean().getBlockBean();
            if (customComponentViewBean.getContainerBean() != null) {
                blockBean.setContainerBean(customComponentViewBean.getContainerBean());
            }
            BlockProperties blockProperties = new BlockProperties(blockBean);
            this.setProperties(blockProperties);
        }
        this.setAlias(methodConfig.getFieldName());
        init(euModule, customComponentViewBean, dbMap);
    }


    void init(EUModule euModule, CustomBlockFormViewBean customComponentViewBean, Map dbMap) {
        List<FieldFormConfig> formFieldList = customComponentViewBean.getAllFields();
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
            Component inputComponent = null;
            Integer con = customComponentViewBean.getCol();
            if (con != null && con > 0) {
                Integer labelHeight = 24;
                fieldInfo.getContainerBean().getUiBean().setPosition("relative");

                if (fieldInfo.getFieldBean().getManualHeight() == null) {

                    String caption = fieldInfo.getCustomBean().getCaption();
                    if (fieldInfo.getFieldBean().getCaption() != null) {
                        caption = fieldInfo.getFieldBean().getCaption();
                    }
                    LabelBean labelBean = fieldInfo.getLabelBean();

                    if (labelBean == null) {
                        labelBean = new LabelBean();
                        AnnotationUtil.fillDefaultValue(Label.class, labelBean);
                        fieldInfo.setLabelBean(labelBean);
                    }
                    fieldInfo.getLabelBean().setLabelCaption(caption);
                    if (fieldInfo.getLabelBean().getLabelPos().equals(LabelPos.top)) {
                        fieldInfo.getLabelBean().setLabelSize(labelHeight + "px");
                        fieldInfo.getLabelBean().setLabelHAlign(HAlignType.left);
                        fieldInfo.getLabelBean().setLabelVAlign(VAlignType.middle);
                        fieldInfo.getFieldBean().setManualHeight(customComponentViewBean.getDefaultRowHeight() + labelHeight);
                        fieldInfo.getContainerBean().getUiBean().setHeight((customComponentViewBean.getDefaultRowHeight() + labelHeight) + "px");
                    }

                }
            }


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

//                if (fieldInfo.getComboConfig() != null && (fieldInfo.getWidgetConfig() instanceof ComboInputFieldBean) && !componentType.equals(ComponentType.LAYOUT)) {
//                    ModuleViewType moduleViewType = fieldInfo.getMethodConfig().getModuleViewType();
//                    if (moduleViewType != null) {
//                        switch (moduleViewType) {
//                            case NAVMENUBARCONFIG:
//                                componentType = ComponentType.MENUBAR;
//                                break;
//                            case NAVGALLERYCONFIG:
//                                componentType = ComponentType.GALLERY;
//                                break;
//                            case NAVTREECONFIG:
//                                componentType = ComponentType.TREEVIEW;
//                                break;
//
//                            case NAVBUTTONLAYOUTCONFIG:
//                                componentType = ComponentType.BUTTONLAYOUT;
//                                break;
//
//                            case NAVFOLDINGTREECONFIG:
//                                componentType = ComponentType.FOLDINGTABS;
//                                break;
//                        }
//                    }
//
//                }

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

            if (inputComponent != null) {

                if (inputComponent.getKey().equals(ComponentType.MQTT.getClassName())) {
                    this.euModule.getComponent().addChildren(inputComponent);
                } else if (inputComponent.getKey().equals(ComponentType.APICALLER.getClassName())) {
                    this.euModule.getComponent().getMainBoxComponent().addChildren(inputComponent);
                } else {
                    PositionType positionType = fieldInfo.getFieldBean().getInnerPosition();
                    if (positionType == null || positionType.equals(PositionType.inner)) {
                        this.addChildren(inputComponent);
                    } else {
                        this.euModule.getComponent().getMainBoxComponent().addChildren(inputComponent);
                    }
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
