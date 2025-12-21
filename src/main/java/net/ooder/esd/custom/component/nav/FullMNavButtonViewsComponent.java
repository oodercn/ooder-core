package net.ooder.esd.custom.component.nav;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.esd.annotation.action.CustomFormAction;
import net.ooder.esd.annotation.action.CustomLoadClassAction;
import net.ooder.esd.annotation.event.CustomFormEvent;
import net.ooder.esd.annotation.event.TabsEventEnum;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.annotation.ui.RequestPathTypeEnum;
import net.ooder.esd.annotation.ui.ResponsePathTypeEnum;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.data.ButtonViewsDataBean;
import net.ooder.esd.bean.view.CustomButtonViewsViewBean;
import net.ooder.esd.custom.component.CustomMButtonViewsComponent;
import net.ooder.esd.custom.component.CustomModuleComponent;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.properties.APICallerProperties;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.UrlPathData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FullMNavButtonViewsComponent extends CustomModuleComponent<CustomMButtonViewsComponent> {


    String saveUrl;

    String reSetUrl;

    ResponsePathTypeEnum itemType;

    public FullMNavButtonViewsComponent() {
        super();
    }

    public FullMNavButtonViewsComponent(EUModule module, MethodConfig methodConfig, Map<String, Object> valueMap) {
        super(module, methodConfig, valueMap);
        this.euModule = module;
        try {
            CustomMButtonViewsComponent currComponent =  new CustomMButtonViewsComponent(euModule, methodConfig, valueMap);
            this.addChildLayoutNav(currComponent);
            this.setCurrComponent(currComponent);
            Action showAction = new Action(CustomLoadClassAction.tabShow,TabsEventEnum.onItemSelected);
            showAction.updateArgs(currComponent.getAlias(), 4);
            ButtonViewsDataBean dataBean = (ButtonViewsDataBean) methodConfig.getDataBean();
            CustomButtonViewsViewBean viewBean = (CustomButtonViewsViewBean) methodConfig.getView();
            if (viewBean != null && viewBean.getAutoReload()) {
                currComponent.addAction(showAction,false);
            } else {
                showAction.setEventKey(TabsEventEnum.onIniPanelView);
                currComponent.addAction(showAction,false);
            }
            this.fillAction(viewBean);
            this.fillViewAction(dataBean);
            this.addChildren(this.genAPIComponent(methodConfig));

        } catch (JDSException e) {
            e.printStackTrace();
        }
    }

    public String getSaveUrl() {
        return saveUrl;
    }

    public void setSaveUrl(String saveUrl) {
        this.saveUrl = saveUrl;
    }

    protected void fillViewAction(ButtonViewsDataBean dataBean) {

        if (dataBean != null) {
            if (dataBean.getDataUrl() != null && !dataBean.getDataUrl().equals("")) {
                this.dataUrl = dataBean.getDataUrl();
            }
            if (dataBean.getItemType() != null) {
                this.itemType = dataBean.getItemType();
            }
            if (dataBean.getSaveUrl() != null && !dataBean.getSaveUrl().equals("")) {
                this.saveUrl = dataBean.getSaveUrl();
            }
            if (dataBean.getReSetUrl() != null && !dataBean.getReSetUrl().equals("")) {
                this.reSetUrl = dataBean.getReSetUrl();
            }

        }


    }

    //数据对象
    @JSONField(serialize = false)
    APICallerComponent[] genAPIComponent(MethodConfig methodAPIBean) throws JDSException {
        List<APICallerComponent> apiCallerComponents = new ArrayList<APICallerComponent>();

        if (dataUrl != null && !dataUrl.equals("") && methodAPIBean.getModuleBean().getDynLoad() != null&& methodAPIBean.getModuleBean().getDynLoad()) {

            MethodConfig methodBean = this.getMethodBeanByItem(CustomMenuItem.RELOAD);

            if (methodBean != null) {
                APICallerComponent reloadAPI = new APICallerComponent(methodBean);
                reloadAPI.setAlias(CustomFormAction.RELOAD.getTarget());
                //刷新调用
                APICallerProperties reloadProperties = reloadAPI.getProperties();

                UrlPathData patternData = new UrlPathData(this.getCurrComponent().getAlias(), RequestPathTypeEnum.FORM, "");
                reloadProperties.addRequestData(patternData);

                UrlPathData requestCtxData = new UrlPathData(this.getCtxBaseComponent().getAlias(), RequestPathTypeEnum.FORM, "");
                reloadProperties.addRequestData(requestCtxData);

                UrlPathData formData = new UrlPathData(this.getCurrComponent().getAlias(), ResponsePathTypeEnum.TABS, "data");
                reloadProperties.addResponseData(formData);

                UrlPathData formCtxData = new UrlPathData(this.getCtxBaseComponent().getAlias(), ResponsePathTypeEnum.FORM, "data");
                reloadProperties.addResponseData(formCtxData);
                reloadProperties.setAutoRun(true);
                apiCallerComponents.add(reloadAPI);
            } else {
                this.logger.error("【数据加载】绑定失败[" + this.className + "===》" + dataUrl);
            }

        }

        //装载保存事件
        if (reSetUrl != null && !reSetUrl.equals("")) {
            MethodConfig methodBean = methodAPIBean.getDataBean().getMethodEvent(CustomFormEvent.RESET);
            if (methodBean != null) {
                APICallerComponent saveAPI = new APICallerComponent(methodBean);
                saveAPI.setAlias(CustomFormAction.RESET.getTarget());
                APICallerProperties saveProperties = saveAPI.getProperties();
                UrlPathData treepathData = new UrlPathData(getMainComponent().getAlias(), RequestPathTypeEnum.FORM, "");
                saveProperties.addRequestData(treepathData);
                UrlPathData ctxData = new UrlPathData(getCtxBaseComponent().getAlias(), RequestPathTypeEnum.FORM, "");
                saveProperties.addRequestData(ctxData);
                apiCallerComponents.add(saveAPI);
            } else {
                this.logger.error("【重置】绑定失败[" + this.className + "===》" + reSetUrl);
            }
        }

        //装载保存事件
        if (saveUrl != null && !saveUrl.equals("")) {
            MethodConfig methodBean = this.getMethodBeanByItem(CustomMenuItem.SAVE);

            if (methodBean != null) {
                APICallerComponent saveAPI = new APICallerComponent(methodBean);
                saveAPI.setAlias(CustomFormAction.SAVE.getTarget());
                APICallerProperties saveProperties = saveAPI.getProperties();
                UrlPathData savepathData = new UrlPathData(this.getCurrComponent().getAlias(), RequestPathTypeEnum.FORM, "");
                saveProperties.addRequestData(savepathData);
                UrlPathData ctxData = new UrlPathData(this.getCtxBaseComponent().getAlias(), RequestPathTypeEnum.FORM, "");
                saveProperties.addRequestData(ctxData);
                apiCallerComponents.add(saveAPI);
            } else {
                this.logger.error("【保存】绑定失败[" + this.className + "===》" + saveUrl);
            }
        }


        return apiCallerComponents.toArray(new APICallerComponent[]{});
    }

    public String getReSetUrl() {
        return reSetUrl;
    }

    public void setReSetUrl(String reSetUrl) {
        this.reSetUrl = reSetUrl;
    }

    public ResponsePathTypeEnum getItemType() {
        return itemType;
    }

    public void setItemType(ResponsePathTypeEnum itemType) {
        this.itemType = itemType;
    }
}
