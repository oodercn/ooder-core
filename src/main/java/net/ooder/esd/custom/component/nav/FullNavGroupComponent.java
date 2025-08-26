package net.ooder.esd.custom.component.nav;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.esd.annotation.BottomBarMenu;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.CustomMenu;
import net.ooder.esd.annotation.action.CustomFormAction;
import net.ooder.esd.annotation.event.CustomFormEvent;
import net.ooder.esd.annotation.event.ModuleEventEnum;
import net.ooder.esd.annotation.menu.CustomFormMenu;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.BottomBarMenuBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.nav.GroupItemBean;
import net.ooder.esd.bean.data.NavGroupDataBean;
import net.ooder.esd.bean.view.NavGroupViewBean;
import net.ooder.esd.custom.component.CustomModuleComponent;
import net.ooder.esd.custom.action.CustomAPICallAction;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.component.BlockComponent;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.properties.APICallerProperties;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.Condition;
import net.ooder.esd.tool.properties.UrlPathData;
import net.ooder.web.util.AnnotationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FullNavGroupComponent extends CustomModuleComponent {

    private String saveUrl;

    private String reSetUrl;

    private String searchUrl;

    @JSONField(serialize = false)
    private String formload;


    public FullNavGroupComponent() {
        super();
    }


    public FullNavGroupComponent(EUModule module, MethodConfig methodConfig, Map<String, Object> valueMap) {
        super(module, methodConfig, valueMap);
        BlockComponent groupComponent = new BlockComponent(Dock.fill, module.getName() + ComponentType.GROUP.name());
        addChildLayoutNav(groupComponent);
        setCurrComponent(groupComponent);

        NavGroupViewBean navGroupViewBean = (NavGroupViewBean) methodConfig.getView();
        List<GroupItemBean> itemModules = navGroupViewBean.getItemBeans();
        for (GroupItemBean groupItemBean : itemModules) {
            try {
                groupComponent.addChildren(new ClassNavGroupComponent(module, groupItemBean, valueMap));
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        fillGroupAction(navGroupViewBean, groupComponent);
        fillViewAction(methodAPIBean);
        try {
            this.addChildren(this.genAPIComponent());
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


    protected void fillViewAction(MethodConfig methodConfig) {
        NavGroupDataBean dataBean = (NavGroupDataBean) methodConfig.getDataBean();
        if (dataBean != null) {
            if (dataBean.getDataUrl() != null && !dataBean.getDataUrl().equals("")) {
                this.dataUrl = dataBean.getDataUrl();
            }
            if (dataBean.getSearchUrl() != null && !dataBean.getSearchUrl().equals("")) {
                this.reSetUrl = dataBean.getSearchUrl();
            }
            if (dataBean.getSaveUrl() != null && !dataBean.getSaveUrl().equals("")) {
                this.saveUrl = dataBean.getSaveUrl();
            }

            if (dataUrl == null || dataUrl.equals("")) {
                MethodConfig methodAPIBean = methodConfig.getDataBean().getMethodByItem(CustomMenuItem.RELOAD);
                if (methodAPIBean != null) {
                    dataUrl = methodAPIBean.getUrl();
                }
            }

            if (saveUrl == null || saveUrl.equals("")) {
                MethodConfig methodAPIBean = methodConfig.getDataBean().getMethodByItem(CustomMenuItem.SAVE);
                if (methodAPIBean != null) {
                    saveUrl = methodAPIBean.getUrl();
                }
            }

            if (reSetUrl == null || reSetUrl.equals("")) {
                MethodConfig methodAPIBean = methodConfig.getDataBean().getMethodEvent(CustomFormEvent.RESET);
                if (methodAPIBean != null) {
                    reSetUrl = methodAPIBean.getUrl();
                }
            }


            if (dataUrl == null || dataUrl.equals("")) {
                dataUrl = methodConfig.getUrl();
            }


        }

    }


    protected void fillGroupAction(NavGroupViewBean view, Component currComponent) {

        NavGroupViewBean formView = (NavGroupViewBean) view;
        Set<CustomFormEvent> customFormEvents = formView.getEvent();

        for (CustomFormEvent eventType : customFormEvents) {
            for (CustomAction actionType : eventType.getActions(false)) {
                currComponent.addAction(new Action(actionType,eventType.getEventEnum()));
            }
        }
        List<CustomFormMenu> customFormMenus = formView.getCustomMenu();
        if (customFormMenus != null && customFormMenus.size() > 0) {
            this.getMenuBar(view.getMenuBar()).addMenu(customFormMenus.toArray(new CustomMenu[]{}));
        }


        List<CustomFormMenu> customBottombar = formView.getBottombarMenu();
        if (customBottombar != null && customBottombar.size() > 0) {
            BottomBarMenuBean barMenuBean = view.getBottomBar();
            if (barMenuBean == null) {
                barMenuBean = AnnotationUtil.fillDefaultValue(BottomBarMenu.class, new BottomBarMenuBean());
            }
            this.getBottomBar(barMenuBean).addMenu(customBottombar.toArray(new CustomFormMenu[]{}));
        }
        super.fillAction(view);

    }

    //数据对象
    @JSONField(serialize = false)
    APICallerComponent[] genAPIComponent() throws JDSException {
        List<APICallerComponent> apiCallerComponents = new ArrayList<APICallerComponent>();
        if (dataUrl != null && !dataUrl.equals("")) {
            MethodConfig methodBean = getMethodBeanByItem(CustomMenuItem.RELOAD);
            if (methodBean == null) {
                methodBean = this.getMethodAPIBean();
            }
            if (methodBean == null) {
                APICallerComponent reloadAPI = new APICallerComponent(methodBean);
                reloadAPI.setAlias(CustomFormAction.RELOAD.getTarget());
                //刷新调用
                APICallerProperties reloadProperties = reloadAPI.getProperties();
                UrlPathData patternData = new UrlPathData(this.getMainComponent().getAlias(), RequestPathTypeEnum.FORM, "");
                reloadProperties.addRequestData(patternData);
                UrlPathData formCtxData = new UrlPathData(this.getCtxBaseComponent().getAlias(), RequestPathTypeEnum.FORM, "data");
                reloadProperties.addResponseData(formCtxData);
                apiCallerComponents.add(reloadAPI);
            }
        }

        //装载保存事件
        if (reSetUrl != null && !reSetUrl.equals("")) {
            MethodConfig methodBean = methodAPIBean.getDataBean().getMethodEvent(CustomFormEvent.RESET);
            if (methodBean != null) {
                APICallerComponent resetAPI = new APICallerComponent(methodBean);
                resetAPI.setAlias(CustomFormAction.RESET.getTarget());
                APICallerProperties saveProperties = resetAPI.getProperties();
                UrlPathData treepathData = new UrlPathData(this.getMainComponent().getAlias(), RequestPathTypeEnum.FORM, "");
                saveProperties.addRequestData(treepathData);
                UrlPathData ctxData = new UrlPathData(getCtxBaseComponent().getAlias(), RequestPathTypeEnum.FORM, "");
                saveProperties.addRequestData(ctxData);
                apiCallerComponents.add(resetAPI);
            } else {
                this.logger.error("【重置】绑定失败[" + this.className + "===》" + reSetUrl);
            }
        }

        //装载保存事件
        if (saveUrl != null && !saveUrl.equals("")) {
            MethodConfig methodBean = getMethodBeanByItem(CustomMenuItem.SAVE);
            if (methodBean != null) {
                APICallerComponent saveAPI = new APICallerComponent(methodBean);
                // saveAPI.getProperties().setIsAllform(true);
                saveAPI.setAlias(CustomFormAction.SAVE.getTarget());
                APICallerProperties saveProperties = saveAPI.getProperties();
                UrlPathData savepathData = new UrlPathData(this.getMainComponent().getAlias(), RequestPathTypeEnum.FORM, "");
                saveProperties.addRequestData(savepathData);
                UrlPathData ctxData = new UrlPathData(this.getCtxBaseComponent().getAlias(), RequestPathTypeEnum.FORM, "");
                saveProperties.addRequestData(ctxData);
                NavGroupDataBean dataBean = (NavGroupDataBean) methodAPIBean.getDataBean();
                if (dataBean.getAutoSave()) {
                    CustomAPICallAction customAPICallAction = new CustomAPICallAction(saveAPI,ModuleEventEnum.onDestroy);
                    Condition condition = new Condition("{page." + this.getCurrComponent().getAlias() + ".isDirtied()}", SymbolType.equal, "{true}");
                    customAPICallAction.addCondition(condition);
                    this.addAction( customAPICallAction);
                }
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

    public String getSearchUrl() {
        return searchUrl;
    }

    public void setSearchUrl(String searchUrl) {
        this.searchUrl = searchUrl;
    }

    public String getFormload() {
        return formload;
    }

    public void setFormload(String formload) {
        this.formload = formload;
    }

}
