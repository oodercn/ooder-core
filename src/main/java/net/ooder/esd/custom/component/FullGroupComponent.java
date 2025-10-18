package net.ooder.esd.custom.component;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.CustomMenu;
import net.ooder.esd.annotation.action.CustomFormAction;
import net.ooder.esd.annotation.event.CustomFormEvent;
import net.ooder.esd.annotation.event.ModuleEventEnum;
import net.ooder.esd.annotation.menu.CustomFormMenu;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.ToolBarMenuBean;
import net.ooder.esd.bean.bar.DynBar;
import net.ooder.esd.bean.data.CustomGroupDataBean;
import net.ooder.esd.bean.view.CustomGroupFormViewBean;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.custom.action.CustomAPICallAction;
import net.ooder.esd.custom.component.form.field.CustomFieldGroupComponent;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.engine.ProjectVersion;
import net.ooder.esd.manager.editor.PluginsFactory;
import net.ooder.esd.tool.component.*;
import net.ooder.esd.tool.properties.APICallerProperties;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.Condition;
import net.ooder.esd.tool.properties.UrlPathData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FullGroupComponent<M extends CustomFieldGroupComponent> extends CustomModuleComponent<M> {

    private String saveUrl;

    private String reSetUrl;

    private String searchUrl;

    @JSONField(serialize = false)
    private String formLoad;

    @JSONField(serialize = false)
    CustomToolsBar customToolsBar;

    public FullGroupComponent() {
        super();
    }

    public FullGroupComponent(EUModule module, MethodConfig methodConfig, Map<String, Object> valueMap) {
        super(module, methodConfig, valueMap);
        try {
            init(methodConfig);
        } catch (JDSException e) {
            e.printStackTrace();
        }

    }


    void init(MethodConfig methodConfig) throws JDSException {
        CustomGroupFormViewBean viewBean = (CustomGroupFormViewBean) methodConfig.getView();
        CustomGroupDataBean dataBean = (CustomGroupDataBean) methodConfig.getDataBean();

        if (dataBean != null) {
            this.dataUrl = dataBean.getDataUrl();
            this.saveUrl = dataBean.getSaveUrl();
            this.searchUrl = dataBean.getSearchUrl();
            this.reSetUrl = dataBean.getReSetUrl();

            if (dataUrl == null || dataUrl.equals("")) {
                MethodConfig methodAPIBean = methodConfig.getDataBean().getMethodByItem(CustomMenuItem.RELOAD);
                if (methodAPIBean != null) {
                    dataUrl = methodAPIBean.getUrl();
                }
            }


            if (saveUrl == null || saveUrl.equals("")) {
                MethodConfig methodAPIBean = methodConfig.getDataBean().getMethodEvent(CustomFormEvent.SAVE);
                if (methodAPIBean != null) {
                    saveUrl = methodAPIBean.getUrl();
                }
            }

            if (saveUrl == null || saveUrl.equals("")) {
                MethodConfig methodAPIBean = methodConfig.getDataBean().getMethodByItem(CustomMenuItem.SAVE);
                if (methodAPIBean != null) {
                    saveUrl = methodAPIBean.getUrl();
                }
            }


            if (searchUrl == null || searchUrl.equals("")) {
                MethodConfig methodAPIBean = methodConfig.getDataBean().getMethodByItem(CustomMenuItem.SEARCH);
                if (methodAPIBean != null) {
                    searchUrl = methodAPIBean.getUrl();
                }
            }

            if (reSetUrl == null || reSetUrl.equals("")) {
                MethodConfig methodAPIBean = methodConfig.getDataBean().getMethodEvent(CustomFormEvent.RESET);
                if (methodAPIBean != null) {
                    reSetUrl = methodAPIBean.getUrl();
                }
            }

            if (dataUrl == null || dataUrl.equals("")) {
                this.dataUrl = methodConfig.getUrl();
            }

            if (dataUrl != null && !dataUrl.equals("")) {
                formLoad = dataUrl;
            }

        }
        CustomFieldGroupComponent currComponent = new CustomFieldGroupComponent(euModule, methodConfig, valueMap);
        this.addChildLayoutNav(currComponent);
        this.setCurrComponent((M) currComponent);
        if (viewBean != null) {
            fillFromAction(viewBean, currComponent);
        }
        APICallerComponent[] apiCallerComponents = this.genAPIComponent(getCtxBaseComponent(), mainComponent);
        this.addChildren(apiCallerComponents);

        this.fillToolBar(viewBean, currComponent);
    }

    protected void fillFromAction(CustomGroupFormViewBean view, Component currComponent) {
        Set<CustomFormEvent> customFormEvents = view.getEvent();
        for (CustomFormEvent eventType : customFormEvents) {
            for (CustomAction actionType : eventType.getActions(false)) {
                currComponent.addAction(new Action(actionType, eventType.getEventEnum()));
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

    void fillToolBar(CustomGroupFormViewBean view, Component currComponent) throws JDSException {
        ComponentType[] bindTypes = (ComponentType[]) view.getBindTypes().toArray(new ComponentType[]{});
        ToolBarMenuBean toolBarBean = view.getToolBar();
        if (toolBarBean != null) {
            Class<DynBar>[] serviceObjs = toolBarBean.getMenuClasses();
            String groupId = currComponent.getAlias() + "Bar";
            if (toolBarBean.getGroupId() != null && !toolBarBean.getGroupId().equals("")) {
                groupId = toolBarBean.getGroupId();
            }
            customToolsBar = new CustomToolsBar(groupId);
            if (serviceObjs != null) {
                for (Class obj : serviceObjs) {
                    if (!obj.equals(Void.class)) {
                        CustomToolsBar bar = PluginsFactory.getInstance().initMenuClass(obj, CustomToolsBar.class);
                        if (customToolsBar == null) {
                            this.customToolsBar = bar;
                            List<APICallerComponent> components = customToolsBar.getApis();
                            this.addApi(components);
                        } else {
                            this.addBindService(obj, customToolsBar, bindTypes);
                        }
                    }
                }
            }

            if (customToolsBar != null && customToolsBar.getProperties().getGroup() != null && customToolsBar.getProperties().getGroup().getSub() != null && customToolsBar.getProperties().getGroup().getSub().size() > 0) {
                customToolsBar.getProperties().setDock(Dock.top);
                currComponent.getParent().addChildren(customToolsBar);
            }

        }
    }

    //数据对象
    @JSONField(serialize = false)
    APICallerComponent[] genAPIComponent(Component ctxComponent, BlockComponent mainComponent) throws JDSException {
        List<APICallerComponent> apiCallerComponents = new ArrayList<APICallerComponent>();
        //装载保存事件
        if (saveUrl != null && !saveUrl.equals("")) {
            MethodConfig methodBean = getMethodBeanByItem(CustomMenuItem.SAVE);
            if (methodBean != null) {
                APICallerComponent saveAPI = new APICallerComponent(methodBean);
                saveAPI.setAlias(CustomFormAction.SAVE.getTarget());
                APICallerProperties saveProperties = saveAPI.getProperties();
                UrlPathData treepathData = new UrlPathData(mainComponent.getAlias(), RequestPathTypeEnum.FORM, "");
                saveProperties.addRequestData(treepathData);
                UrlPathData ctxData = new UrlPathData(ctxComponent.getAlias(), RequestPathTypeEnum.FORM, "");
                saveProperties.addRequestData(ctxData);

                CustomGroupDataBean dataBean = (CustomGroupDataBean) methodAPIBean.getDataBean();
                if (dataBean.getAutoSave()) {
                    CustomAPICallAction customAPICallAction = new CustomAPICallAction(saveAPI, ModuleEventEnum.onDestroy);
                    Condition condition = new Condition("{page." + this.getCurrComponent().getAlias() + ".isDirtied()}", SymbolType.equal, "{true}");
                    customAPICallAction.addCondition(condition);
                    this.addAction(customAPICallAction);
                }
                apiCallerComponents.add(saveAPI);
            } else {
                this.logger.error("【保存】绑定失败[" + this.className + "===》" + saveUrl);
            }
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
                UrlPathData ctxData = new UrlPathData(ctxComponent.getAlias(), RequestPathTypeEnum.FORM, "");
                saveProperties.addRequestData(ctxData);
                apiCallerComponents.add(saveAPI);
            } else {
                this.logger.error("【重置】绑定失败[" + this.className + "===》" + reSetUrl);
            }
        }


        //装载保存事件
        if (searchUrl != null && !searchUrl.equals("")) {
            String fullUrl = searchUrl;
            MethodConfig methodBean = getMethodBeanByItem(CustomMenuItem.SEARCH);
            if (methodBean != null) {
                APICallerComponent searchAPI = new APICallerComponent(methodBean);
                searchAPI.setAlias(CustomFormAction.SEARCH.getTarget());
                APICallerProperties searchProperties = searchAPI.getProperties();
                UrlPathData treepathData = new UrlPathData(mainComponent.getAlias(), RequestPathTypeEnum.FORM, "");
                searchProperties.addRequestData(treepathData);
                searchProperties.setAutoRun(true);
                UrlPathData ctxData = new UrlPathData(ctxComponent.getAlias(), RequestPathTypeEnum.FORM, "");
                searchProperties.addRequestData(ctxData);
                ProjectVersion version = ESDFacrory.getAdminESDClient().getProjectVersionByName(this.euModule.getProjectVersion().getVersionName());
                //放入当前环境便面循环调用
                valueMap.put(this.getClassName(), this.getEuModule());
                EUModule newmodule = CustomViewFactory.getInstance().getViewByMethod(methodBean, version.getVersionName(), valueMap);
                if (newmodule == null) {
                    newmodule = ESDFacrory.getAdminESDClient().getCustomModule(euModule.getPackageName() + "." + searchUrl, version.getVersionName(), valueMap);
                }

                if (newmodule != null) {
                    TreeGridComponent gridComponent = (TreeGridComponent) newmodule.getComponent().getFristComponentByType(TreeGridComponent.class);
                    if (gridComponent != null) {
                        UrlPathData gredpathData = new UrlPathData(gridComponent.getAlias(), ResponsePathTypeEnum.TREEGRID, "data");
                        searchProperties.addResponseData(gredpathData);
                    }

                    PageBarComponent pageBarComponent = (PageBarComponent) newmodule.getComponent().getFristComponentByType(PageBarComponent.class);
                    if (pageBarComponent != null) {
                        UrlPathData patepathData = new UrlPathData(pageBarComponent.getAlias(), ResponsePathTypeEnum.PAGEBAR, "size");
                        searchProperties.addResponseData(patepathData);
                    }
                    apiCallerComponents.add(searchAPI);
                }

            } else {
                this.logger.error("【查询】绑定失败[" + this.className + "===》" + searchUrl);
            }
        }

        if (formLoad != null && !formLoad.equals("")) {
            MethodConfig methodBean = null;
            if (methodAPIBean != null) {
                methodBean = methodAPIBean;
            } else {
                methodBean = getMethodBeanByItem(CustomMenuItem.RELOAD);
            }
            APICallerComponent reloadAPI = new APICallerComponent(methodBean);
            if (reloadAPI != null) {
                reloadAPI.setAlias(CustomFormAction.RELOAD.getTarget());
                //刷新调用
                APICallerProperties reloadProperties = reloadAPI.getProperties();
                UrlPathData treepathData = new UrlPathData(mainComponent.getAlias(), RequestPathTypeEnum.FORM, "");
                reloadProperties.addRequestData(treepathData);

                UrlPathData formData = new UrlPathData(mainComponent.getAlias(), ResponsePathTypeEnum.FORM, "data");
                reloadProperties.addResponseData(formData);

                UrlPathData formCtxData = new UrlPathData(ctxComponent.getAlias(), ResponsePathTypeEnum.FORM, "data");
                reloadProperties.addResponseData(formCtxData);


                UrlPathData ctxData = new UrlPathData(ctxComponent.getAlias(), RequestPathTypeEnum.FORM, "");
                reloadProperties.addRequestData(ctxData);
                apiCallerComponents.add(reloadAPI);
            } else {
                this.logger.error("【装载】绑定失败[" + this.className + "===》" + formLoad);
            }

        }

        return apiCallerComponents.toArray(new APICallerComponent[]{});
    }


    public String getSaveUrl() {
        return saveUrl;
    }

    public void setSaveUrl(String saveUrl) {
        this.saveUrl = saveUrl;
    }

    public String getSearchUrl() {
        return searchUrl;
    }

    public void setSearchUrl(String searchUrl) {
        this.searchUrl = searchUrl;
    }

    public String getReSetUrl() {
        return reSetUrl;
    }

    public void setReSetUrl(String reSetUrl) {
        this.reSetUrl = reSetUrl;
    }

    public String getFormLoad() {
        return formLoad;
    }

    public void setFormLoad(String formLoad) {
        this.formLoad = formLoad;
    }

}
