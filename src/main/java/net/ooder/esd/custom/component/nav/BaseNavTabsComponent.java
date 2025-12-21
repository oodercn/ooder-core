package net.ooder.esd.custom.component.nav;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.CustomMenu;
import net.ooder.esd.annotation.action.CustomFormAction;
import net.ooder.esd.annotation.action.CustomLoadClassAction;
import net.ooder.esd.annotation.event.*;
import net.ooder.esd.annotation.menu.CustomFormMenu;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.annotation.ui.RequestPathTypeEnum;
import net.ooder.esd.annotation.ui.ResponsePathTypeEnum;
import net.ooder.esd.annotation.ui.SymbolType;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.data.TabsDataBean;
import net.ooder.esd.bean.view.TabsEventBean;
import net.ooder.esd.bean.view.TabsViewBean;
import net.ooder.esd.custom.action.CustomAPICallAction;
import net.ooder.esd.custom.action.ShowPageAction;
import net.ooder.esd.custom.component.CustomModuleComponent;
import net.ooder.esd.custom.component.CustomTabsComponent;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.properties.*;

import java.util.*;

public class BaseNavTabsComponent extends CustomModuleComponent<CustomTabsComponent> {

    ResponsePathTypeEnum itemType;

    String saveUrl;

    String reSetUrl;

    public BaseNavTabsComponent() {
        super();
    }

    public BaseNavTabsComponent(EUModule module, MethodConfig methodAPIBean, Map<String, Object> valueMap) {
        super(module, methodAPIBean, valueMap);
    }


    public void addChildNav(CustomTabsComponent currComponent) {
        this.addChildLayoutNav(currComponent);
        this.setCurrComponent(currComponent);
        Action showAction = new Action(CustomLoadClassAction.tabShow, TabsEventEnum.onItemSelected);
        showAction.updateArgs(currComponent.getAlias(), 4);
        TabsViewBean viewBean = (TabsViewBean) methodAPIBean.getView();
        if (viewBean != null && viewBean.getAutoReload()) {
            currComponent.addAction(showAction, false);
        } else {
            currComponent.addAction(showAction, false);
        }

        TabsProperties properties = currComponent.getProperties();
        if (properties.getItems() != null && properties.getItems().size() > 0) {
            Action clickItemAction = new Action(TabsEventEnum.onRender);
            clickItemAction.setType(ActionTypeEnum.control);
            clickItemAction.setTarget(currComponent.getAlias());
            clickItemAction.setDesc("初始化");
            clickItemAction.setMethod("fireItemClickEvent");
            clickItemAction.setArgs(Arrays.asList(new String[]{properties.getFristId()}));
            currComponent.addAction( clickItemAction,false);
        }
        try {
            this.addChildren(genAPIComponent(methodAPIBean));
        } catch (JDSException e) {
            e.printStackTrace();
        }
        this.fillCustomAction(viewBean, currComponent);
    }


    protected void fillCustomAction(TabsViewBean view, Component currComponent) {
        Set<TabsEventBean> extAPIEvent = view.getExtAPIEvent();
        for (TabsEventBean eventEnum : extAPIEvent) {
            List<Action> actions = eventEnum.getActions();
            for (Action action : actions) {
                if (action.getScript() != null && !action.getScript().equals("")) {
                    action.updateArgs("{page." + this.getAlias() + "}", 3);
                }
                currComponent.addAction(action, true, eventEnum.getEventReturn());
            }
        }
    }


    protected void fillTabsAction(TabsViewBean view, Component currComponent) {
        Set<CustomTabsEvent> customFormEvents = view.getEvent();
        for (CustomTabsEvent eventType : customFormEvents) {
            for (CustomAction actionType : eventType.getActions(false)) {
                currComponent.addAction(new Action(actionType, eventType.getEventEnum()), false);
                Action action = new Action(actionType, eventType.getEventEnum());
                MethodConfig methodConfig = methodAPIBean.getDataBean().getMethodEvent(eventType);
                if (methodConfig != null) {
                    if (!methodConfig.isModule()) {
                        if (this.findComponentByAlias(actionType.target()) == null) {
                            APICallerComponent apiCallerComponent = new APICallerComponent(methodConfig);
                            apiCallerComponent.setAlias(actionType.target());
                            this.addChildren(apiCallerComponent);
                        }
                    } else {
                        action = new ShowPageAction(methodConfig, eventType.getEventEnum());
                        action.updateArgs("{args[1]}", 6);
                        action.updateArgs("{args[2]}", 5);
                    }
                }
                currComponent.addAction(action, false);
            }
        }
        List<CustomFormMenu> customFormMenus = view.getCustomMenu();
        if (customFormMenus != null && customFormMenus.size() > 0) {
            this.getMenuBar(view.getMenuBar()).addMenu(customFormMenus.toArray(new CustomMenu[]{}));
        }


        List<CustomFormMenu> customBottombar = view.getBottombarMenu();
        if (customBottombar != null && customBottombar.size() > 0) {
            this.getBottomBar(view.getBottomBar()).addMenu(customBottombar.toArray(new CustomFormMenu[]{}));
        }
        super.fillAction(view);

    }

    protected void fillViewAction(TabsDataBean dataBean) {
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

    public String getSaveUrl() {
        return saveUrl;
    }

    public void setSaveUrl(String saveUrl) {
        this.saveUrl = saveUrl;
    }
}
