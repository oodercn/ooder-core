package net.ooder.esd.custom.component.form.field;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSConstants;
import net.ooder.common.JDSException;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.CustomMenu;
import net.ooder.esd.annotation.event.GridEventEnum;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.annotation.ui.RequestPathTypeEnum;
import net.ooder.esd.annotation.ui.ResponsePathTypeEnum;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.field.CustomGridFieldBean;
import net.ooder.esd.bean.view.CustomGridViewBean;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.custom.action.CustomConditionAction;
import net.ooder.esd.custom.component.CustomGalleryComponent;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.TreeGridComponent;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.ClassGridProperties;
import net.ooder.esd.tool.properties.UrlPathData;
import net.ooder.esd.util.json.APICallSerialize;
import net.ooder.jds.core.esb.EsbUtil;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomFieldGridComponent extends TreeGridComponent {
    public static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, CustomGalleryComponent.class);

    public String id;

    @JSONField(serializeUsing = APICallSerialize.class)
    List<APICallerComponent> apis = new ArrayList<>();





    public CustomFieldGridComponent(EUModule euModule, FieldFormConfig<CustomGridFieldBean, ?> field, String target, Object value, Map valueMap) {
        super(field.getFieldname());
        MethodConfig methodConfig = field.getMethodConfig();
        if (methodConfig.getRequestMethodBean() == null) {
            String euClassName = methodConfig.getEUClassName();
            EUModule currModule = null;
            try {
                currModule = ESDFacrory.getUserESDClient().getModule(euClassName, euModule.getProjectVersion().getProjectName());
            } catch (JDSException e) {
                e.printStackTrace();
            }
            methodConfig = currModule.getComponent().getMethodAPIBean();
        }

        Component component = field.getWidgetConfig().getComponent();
        if (component != null) {
            if (component.getCS() != null) {
                this.CS = component.getCS();
            }
            this.events = component.getEvents();
        }

        CustomGridViewBean viewBean = (CustomGridViewBean) methodConfig.getView();
        this.setProperties(new ClassGridProperties(viewBean));
        this.setAlias(viewBean.getName());
        this.setTarget(target);

    }


    private void lazyLoad(EUModule euModule, FieldFormConfig field, CustomFieldGridComponent listComponent, Class bindClass) {
        try {
            ApiClassConfig config = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(bindClass.getName());
            MethodConfig loadMethod = config.getMethodByItem(CustomMenuItem.RELOAD);
            if (loadMethod != null) {
                APICallerComponent apiCallerComponent = new APICallerComponent(loadMethod);
                UrlPathData ctxData = new UrlPathData(euModule.getComponent().getTopComponentBox().getAlias(), RequestPathTypeEnum.FORM, "");
                apiCallerComponent.getProperties().addRequestData(ctxData);
                UrlPathData galleryData = new UrlPathData(listComponent.getAlias(), ResponsePathTypeEnum.TREEGRID, "data");
                apiCallerComponent.getProperties().addResponseData(galleryData);
                apiCallerComponent.getProperties().setAutoRun(true);
                euModule.getComponent().addChildren(apiCallerComponent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    boolean parExpression(String expression) {
        boolean result = false;
        try {
            result = EsbUtil.parExpression(expression, Boolean.class);
        } catch (Throwable e) {
            //  e.printStackTrace();
            logger.error("expression[" + expression + "] par err[" + e.getMessage() + "]");
        }
        return result;

    }

    public List<Action> fillActions(CustomMenu type) {
        List<Action> actions = new ArrayList<>();
        CustomAction[] actionTypes = type.actions();
        for (CustomAction actionType : actionTypes) {
            String exprossion = actionType.expression();
            try {
                if (exprossion != null && !exprossion.equals("") && EsbUtil.parExpression(exprossion, Boolean.class)) {
                    CustomConditionAction action = new CustomConditionAction(actionType, type,GridEventEnum.onDblclickRow);
                    this.addAction( action);
                    actions.add(action);
                }
            } catch (Throwable e) {
                // e.printStackTrace();
                logger.error("expression[" + exprossion + "] par err[" + e.getMessage() + "]");
                // log.error("expression err[" + actionType.getExpression() + "]");
            }
        }
        return actions;
    }


    public List<APICallerComponent> getApis() {
        return apis;
    }

    public void setApis(List<APICallerComponent> apis) {
        this.apis = apis;
    }

}
