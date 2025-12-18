package net.ooder.esd.custom.component.form.field;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.action.CustomFormAction;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.RequestPathTypeEnum;
import net.ooder.esd.annotation.ui.ResponsePathTypeEnum;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.field.CustomFormLayoutFieldBean;
import net.ooder.esd.bean.field.CustomLayoutFieldBean;
import net.ooder.esd.bean.field.combo.ComboBoxBean;
import net.ooder.esd.bean.field.combo.ComboInputFieldBean;
import net.ooder.esd.bean.view.CustomFormViewBean;
import net.ooder.esd.custom.CustomViewConfigFactory;
import net.ooder.esd.custom.component.form.FormLayoutModule;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.FormLayoutComponent;
import net.ooder.esd.tool.properties.APICallerProperties;
import net.ooder.esd.tool.properties.UrlPathData;
import net.ooder.esd.tool.properties.form.FormLayoutProperties;

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

            FormLayoutProperties blockProperties = new FormLayoutProperties(customComponentViewBean, field.getWidgetConfig());
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
        FormLayoutModule layoutModule = new FormLayoutModule(euModule, this, customComponentViewBean.getAllFields(), dbMap, customComponentViewBean);
        MethodConfig methodConfig = euModule.getComponent().getMethodAPIBean();
        if (methodConfig != null) {
            genAPIComponent(euModule.getComponent().getCtxBaseComponent(), methodConfig, this);
        }
    }


    //数据对象
    @JSONField(serialize = false)
    APICallerComponent[] genAPIComponent(Component ctxComponent, MethodConfig methodConfig, FormLayoutComponent mainComponent) {
        List<APICallerComponent> apiCallerComponents = new ArrayList<APICallerComponent>();
        APICallerComponent reloadAPI = new APICallerComponent(methodConfig);
        if (reloadAPI != null) {
            reloadAPI.setAlias(CustomFormAction.RELOAD.getTarget());
            //刷新调用
            APICallerProperties reloadProperties = reloadAPI.getProperties();
            UrlPathData treepathData = new UrlPathData(mainComponent.getAlias(), RequestPathTypeEnum.FORM, "");
            reloadProperties.addRequestData(treepathData);

            UrlPathData ctxData = new UrlPathData(ctxComponent.getAlias(), RequestPathTypeEnum.FORM, "");
            reloadProperties.addRequestData(ctxData);

            UrlPathData formData = new UrlPathData(mainComponent.getAlias(), ResponsePathTypeEnum.FORM, "data");
            reloadProperties.addResponseData(formData);

            UrlPathData formCtxData = new UrlPathData(ctxComponent.getAlias(), ResponsePathTypeEnum.FORM, "data");
            reloadProperties.addResponseData(formCtxData);
            apiCallerComponents.add(reloadAPI);
        }

        return apiCallerComponents.toArray(new APICallerComponent[]{});
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
