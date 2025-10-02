package net.ooder.esd.custom.component.nav;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.esd.annotation.action.CustomFormAction;
import net.ooder.esd.annotation.action.CustomLoadClassAction;
import net.ooder.esd.annotation.event.CustomFormEvent;
import net.ooder.esd.annotation.event.ModuleEventEnum;
import net.ooder.esd.annotation.event.TabsEventEnum;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.annotation.ui.RequestPathTypeEnum;
import net.ooder.esd.annotation.ui.ResponsePathTypeEnum;
import net.ooder.esd.annotation.ui.SymbolType;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.data.NavFoldingTabsDataBean;
import net.ooder.esd.bean.data.TabsDataBean;
import net.ooder.esd.bean.view.NavFoldingTabsViewBean;
import net.ooder.esd.custom.action.CustomAPICallAction;
import net.ooder.esd.custom.component.CustomModuleComponent;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.properties.APICallerProperties;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.Condition;
import net.ooder.esd.tool.properties.UrlPathData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FullNavFoldingTabsComponent extends CustomModuleComponent {


    ResponsePathTypeEnum itemType;

    String saveUrl;

    String reSetUrl;


    public FullNavFoldingTabsComponent() {
        super();
    }

    public FullNavFoldingTabsComponent(EUModule module, MethodConfig methodConfig, Map<String, Object> valueMap) throws ClassNotFoundException {
        super(module, methodConfig, valueMap);

        NavFoldingTabsComponent tabcomponent = new NavFoldingTabsComponent(euModule, methodConfig, valueMap);
        this.addChildLayoutNav(tabcomponent);
        this.setCurrComponent(tabcomponent);
        Action showAction = new Action(CustomLoadClassAction.tabShow, TabsEventEnum.onItemSelected);
        showAction.updateArgs(tabcomponent.getAlias(), 4);
        NavFoldingTabsViewBean viewBean = (NavFoldingTabsViewBean) methodConfig.getView();
        this.fillAction(viewBean);
        this.fillViewAction((NavFoldingTabsDataBean) methodConfig.getDataBean());
        try {
            this.addChildren(this.genAPIComponent(methodConfig));
        } catch (JDSException e) {
            e.printStackTrace();
        }


    }

    protected void fillViewAction(NavFoldingTabsDataBean dataBean) {
        if (dataBean != null) {
            if (dataBean.getDataUrl() != null && !dataBean.getDataUrl().equals("")) {
                this.dataUrl = dataBean.getDataUrl();
            } else {
                MethodConfig methodAPIBean = null;
                try {
                    methodAPIBean = this.getMethodBeanByItem(CustomMenuItem.RELOAD);
                } catch (JDSException e) {
                    e.printStackTrace();
                }
                if (methodAPIBean != null) {
                    dataUrl = methodAPIBean.getUrl();
                }
            }
            if (dataBean.getItemType() != null) {
                this.itemType = dataBean.getItemType();
            }
            if (dataBean.getSaveUrl() != null && !dataBean.getSaveUrl().equals("")) {
                this.saveUrl = dataBean.getSaveUrl();
            } else {
                MethodConfig methodAPIBean = null;
                try {
                    methodAPIBean = this.getMethodBeanByItem(CustomMenuItem.SAVE);
                } catch (JDSException e) {
                    e.printStackTrace();
                }
                if (methodAPIBean != null) {
                    saveUrl = methodAPIBean.getUrl();
                }
            }
            if (dataBean.getReSetUrl() != null && !dataBean.getReSetUrl().equals("")) {
                this.reSetUrl = dataBean.getReSetUrl();
            } else {
                MethodConfig methodConfig = methodAPIBean.getDataBean().getMethodEvent(CustomFormEvent.RESET);
                if (methodConfig != null) {
                    reSetUrl = methodConfig.getUrl();
                }

            }

        }

    }

    //数据对象
    @JSONField(serialize = false)
    APICallerComponent[] genAPIComponent(MethodConfig methodAPIBean) throws JDSException {
        List<APICallerComponent> apiCallerComponents = new ArrayList<APICallerComponent>();
        if (dataUrl != null && !dataUrl.equals("") && methodAPIBean.getModuleBean().getDynLoad() != null && methodAPIBean.getModuleBean().getDynLoad()) {
            MethodConfig methodBean = getMethodBeanByItem(CustomMenuItem.RELOAD);
            if (methodBean == null) {
                methodBean = methodAPIBean;
            }
            APICallerComponent reloadAPI = new APICallerComponent(methodBean);
            reloadAPI.setAlias(CustomFormAction.RELOAD.getTarget());
            //刷新调用
            APICallerProperties reloadProperties = reloadAPI.getProperties();
            UrlPathData treepathData = new UrlPathData(this.getCtxBaseComponent().getAlias(), RequestPathTypeEnum.FORM, "");
            reloadProperties.addRequestData(treepathData);
            UrlPathData formData = new UrlPathData(this.getCurrComponent().getAlias(), itemType, "data");
            reloadProperties.addResponseData(formData);
            UrlPathData formCtxData = new UrlPathData(this.getCtxBaseComponent().getAlias(), ResponsePathTypeEnum.FORM, "data");
            reloadProperties.addResponseData(formCtxData);
            reloadProperties.setAutoRun(true);
            apiCallerComponents.add(reloadAPI);
        }

        //装载保存事件
        if (reSetUrl != null && !reSetUrl.equals("")) {
            MethodConfig methodBean = methodAPIBean.getDataBean().getMethodEvent(CustomFormEvent.RESET);
            if (methodBean != null) {
                APICallerComponent saveAPI = new APICallerComponent(methodBean);
                saveAPI.setAlias(CustomFormAction.RESET.getTarget());
                APICallerProperties saveProperties = saveAPI.getProperties();
                UrlPathData treepathData = new UrlPathData(mainComponent.getAlias(), RequestPathTypeEnum.FORM, "");
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
            MethodConfig methodBean = getMethodBeanByItem(CustomMenuItem.SAVE);
            if (methodBean != null) {
                APICallerComponent saveAPI = new APICallerComponent(methodBean);
                saveAPI.setAlias(CustomFormAction.SAVE.getTarget());
                APICallerProperties saveProperties = saveAPI.getProperties();
                UrlPathData savepathData = new UrlPathData(this.getCurrComponent().getAlias(), RequestPathTypeEnum.FORM, "");
                saveProperties.addRequestData(savepathData);
                UrlPathData ctxData = new UrlPathData(this.getCtxBaseComponent().getAlias(), RequestPathTypeEnum.FORM, "");
                saveProperties.addRequestData(ctxData);
                TabsDataBean dataBean = (TabsDataBean) methodAPIBean.getDataBean();
                if (dataBean.getAutoSave()) {
                    CustomAPICallAction customAPICallAction = new CustomAPICallAction(saveAPI, ModuleEventEnum.onDestroy);
                    Condition condition = new Condition("{page." + this.getCurrComponent().getAlias() + ".isDirtied()}", SymbolType.equal, "{true}");
                    customAPICallAction.addCondition(condition);
                    this.addAction(customAPICallAction, false);
                }
                apiCallerComponents.add(saveAPI);
            }
        }


        return apiCallerComponents.toArray(new APICallerComponent[]{});
    }

    public ResponsePathTypeEnum getItemType() {
        return itemType;
    }

    public void setItemType(ResponsePathTypeEnum itemType) {
        this.itemType = itemType;
    }

    public String getSaveUrl() {
        return saveUrl;
    }

    public void setSaveUrl(String saveUrl) {
        this.saveUrl = saveUrl;
    }

    public String getReSetUrl() {
        return reSetUrl;
    }

    public void setReSetUrl(String reSetUrl) {
        this.reSetUrl = reSetUrl;
    }
}
