package net.ooder.esd.custom.component.form.field.combo;

import com.alibaba.fastjson.JSON;
import net.ooder.common.JDSConstants;
import net.ooder.common.JDSException;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.annotation.ui.RequestPathTypeEnum;
import net.ooder.esd.annotation.ui.ResponsePathTypeEnum;
import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.annotation.event.CustomFieldEvent;
import net.ooder.esd.annotation.ui.CommandBtnType;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.field.CustomListBean;
import net.ooder.esd.bean.field.combo.ComboInputFieldBean;
import net.ooder.esd.bean.field.combo.ComboListBoxFieldBean;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.custom.action.PopPageAction;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.component.ComboInputComponent;
import net.ooder.esd.tool.properties.UrlPathData;
import net.ooder.esd.tool.properties.form.ComboInputProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import org.mvel2.templates.TemplateRuntime;

import java.util.Map;

public class CustomComboListComponent extends ComboInputComponent {

    public static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, CustomComboListComponent.class);

    public CustomComboListComponent(EUModule euModule, FieldFormConfig<ComboInputFieldBean, ComboListBoxFieldBean> field, String target, Object value, Map<String, Object> valueMap) {
        super(field);
        ComboInputProperties comboInputProperties = this.getProperties();
        ComboListBoxFieldBean<ComboInputProperties> comboListBoxFieldBean = field.getComboConfig();
        ComboInputFieldBean inputFieldBean = field.getWidgetConfig();

        if (value != null) {
            if (value instanceof ComboInputFieldBean) {
                String json = JSON.toJSONString(value);
                String obj = (String) TemplateRuntime.eval(json, value);
                OgnlUtil.setProperties(JSON.parseObject(obj, Map.class), inputFieldBean, false, false);
            } else if (value instanceof ComboListBoxFieldBean) {
                String json = JSON.toJSONString(value);
                String obj = (String) TemplateRuntime.eval(json, value);
                OgnlUtil.setProperties(JSON.parseObject(obj, Map.class), comboListBoxFieldBean, false, false);
            }
        }
        if (value != null && (value instanceof String) && !value.equals("")) {
            comboInputProperties.setValue(value);
        } else if (valueMap != null) {
            value = valueMap.get(field.getFieldname());
            if (value != null && !value.equals("")) {
                comboInputProperties.setValue(value);
            }
        }

        CustomListBean customListBean = comboListBoxFieldBean.getListBean().getCustomListBean();
        MethodConfig sourceMethodConfig = euModule.getComponent().getMethodAPIBean();
        try {
            MethodConfig fieldMethodConfig = null;
            if (customListBean.getDynLoad() != null && customListBean.getDynLoad() && customListBean.getBindClass() != null && !customListBean.getBindClass().equals(Enum.class)) {
                lazyLoad(euModule, field, this, customListBean.getBindClass());
            }

            if (customListBean.getBindClass() != null && !customListBean.getBindClass().equals(Void.class) && !customListBean.getBindClass().equals(Enum.class)) {
                ApiClassConfig bindConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(customListBean.getBindClass().getName());
                fieldMethodConfig = bindConfig.getFieldEvent(CustomFieldEvent.POPEDITOR);
                if (fieldMethodConfig == null) {
                    fieldMethodConfig = bindConfig.getMethodByItem(CustomMenuItem.INDEX);
                }
            } else if (field.getViewClassName() != null) {
                ApiClassConfig bindConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(field.getViewClassName());
                if (bindConfig != null) {
                    fieldMethodConfig = bindConfig.getMethodByName(field.getFieldname());
                }
            }


            if (fieldMethodConfig != null && fieldMethodConfig.getView() != null) {
                Map<String, Object> tagVar = fieldMethodConfig.getTagVar();
                tagVar.put(fieldName, field.getFieldname());
                tagVar.put(fieldCaption, field.getFieldname());
                String btnType = inputFieldBean.getCommandBtn();
                if (btnType == null || btnType.equals("")) {
                    btnType = CommandBtnType.select.getType();
                }
                PopPageAction action = new PopPageAction(fieldMethodConfig, FieldEventEnum.onCommand);
                this.getProperties().setCommandBtn(btnType);
                this.addAction(action);
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }

        if (comboInputProperties.getCaption() != null
                && comboInputProperties.getLabelCaption() != null
                && comboInputProperties.getLabelCaption().equals(comboInputProperties.getCaption())) {
            comboInputProperties.setCaption(null);
        }


        initEvent(euModule, field);
        initContextMenu(euModule, field, FieldEventEnum.onContextmenu);
        initEditor(euModule, field, FieldEventEnum.onChange);
        initFilter(euModule, field, customListBean);
        this.setTarget(target);

    }


    private void lazyLoad(EUModule euModule, FieldFormConfig field, ComboInputComponent listComponent, Class bindClass) {
        try {
            ApiClassConfig config = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(bindClass.getName());
            MethodConfig loadMethod = config.getMethodByItem(CustomMenuItem.RELOAD);
            if (loadMethod == null) {
                loadMethod = config.getFieldEvent(CustomFieldEvent.LOADITEMS);
            }
            if (loadMethod != null) {
                APICallerComponent apiCallerComponent = new APICallerComponent(loadMethod);
                UrlPathData ctxData = new UrlPathData(euModule.getComponent().getTopComponentBox().getAlias(), RequestPathTypeEnum.FORM, "");
                apiCallerComponent.getProperties().addRequestData(ctxData);
                apiCallerComponent.getProperties().setAutoRun(true);
                UrlPathData pageBarPathData = new UrlPathData(listComponent.getAlias(), ResponsePathTypeEnum.LIST, "data");
                apiCallerComponent.getProperties().addResponseData(pageBarPathData);
                euModule.getComponent().addChildren(apiCallerComponent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
