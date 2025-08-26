package net.ooder.esd.custom.component.form.field.combo;

import net.ooder.common.JDSConstants;
import net.ooder.common.JDSException;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.annotation.ui.RequestPathTypeEnum;
import net.ooder.esd.annotation.ui.ResponsePathTypeEnum;
import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.annotation.event.CustomFieldEvent;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.annotation.ui.CommandBtnType;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.field.combo.ComboInputFieldBean;
import net.ooder.esd.bean.field.combo.ComboPopFieldBean;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.custom.action.PopPageAction;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.properties.UrlPathData;
import net.ooder.esd.tool.properties.item.UIItem;

import java.util.Map;

public class CustomComboPopComponent extends CustomComboInputComponent {

    public static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, CustomComboPopComponent.class);


    public CustomComboPopComponent(EUModule euModule, FieldFormConfig<ComboInputFieldBean, ComboPopFieldBean> field, String target, Object value, Map<String, Object> valueMap) {
        super(euModule, field, target, value, valueMap);
        ComboPopFieldBean fieldBean = field.getComboConfig();
        try {
            MethodConfig sourceMethodConfig = euModule.getComponent().getMethodAPIBean();
            ApiClassConfig classConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(sourceMethodConfig.getSourceClassName());
            MethodConfig fieldMethodConfig = null;
            MethodConfig loadChildMethod = null;
            MethodConfig popMethod = null;
            if (fieldBean.getSrc() != null && !fieldBean.getSrc().equals("")) {
                fieldMethodConfig = classConfig.getMethodByName(fieldBean.getSrc());
            } else if (fieldBean.getBindClass() != null && !fieldBean.getBindClass().equals(Void.class) && !fieldBean.getBindClass().equals(Enum.class)) {
                ApiClassConfig bindConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(fieldBean.getBindClass().getName());
                fieldMethodConfig = bindConfig.getFieldEvent(CustomFieldEvent.POPEDITOR);
                if (fieldMethodConfig == null) {
                    fieldMethodConfig = bindConfig.getMethodByItem(CustomMenuItem.INDEX);
                }

                loadChildMethod = bindConfig.getFieldEvent(CustomFieldEvent.LOADITEMS);
                if (loadChildMethod != null && UIItem.class.isAssignableFrom(loadChildMethod.getInnerReturnType())) {
                    lazyLoad(euModule, loadChildMethod);
                }


            } else {
                AggEntityConfig bindConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(field.getViewClassName(), false);
                popMethod = bindConfig.getMethodByName(field.getFieldname());
            }

            if (popMethod != null) {
                this.properties.setHoverPop(popMethod.getFieldName());
            } else if (fieldMethodConfig != null) {
                this.properties.setHoverPop(null);
                Map<String, Object> tagVar = fieldMethodConfig.getTagVar();
                tagVar.put(fieldName, field.getFieldname());
                tagVar.put(fieldCaption, field.getFieldname());
                String btnType = fieldBean.getCommandBtn();
                if (btnType == null || btnType.equals("")) {
                    btnType = CommandBtnType.pop.getType();
                }
                this.properties.setCommandBtn(btnType);
                this.properties.setType(ComboInputType.listbox);
                PopPageAction action = new PopPageAction(fieldMethodConfig, FieldEventEnum.onCommand);
                this.addAction(action);
            }

        } catch (JDSException e) {
            e.printStackTrace();
        }


    }

    private void lazyLoad(EUModule euModule,  MethodConfig loadMethod) {
        try {
            if (loadMethod != null) {
                APICallerComponent apiCallerComponent = new APICallerComponent(loadMethod);
                UrlPathData ctxData = new UrlPathData(euModule.getComponent().getTopComponentBox().getAlias(), RequestPathTypeEnum.FORM, "");
                apiCallerComponent.getProperties().addRequestData(ctxData);
                apiCallerComponent.getProperties().setAutoRun(true);
                UrlPathData pageBarPathData = new UrlPathData(this.getAlias(), ResponsePathTypeEnum.LIST, "data");
                apiCallerComponent.getProperties().addResponseData(pageBarPathData);
                euModule.getComponent().addChildren(apiCallerComponent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
