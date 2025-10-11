package net.ooder.esd.custom.component;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.action.CustomGridAction;
import net.ooder.esd.annotation.event.CustomGalleryEvent;
import net.ooder.esd.annotation.event.GalleryEventEnum;
import net.ooder.esd.annotation.event.ModuleEventEnum;
import net.ooder.esd.annotation.event.PageEventEnum;
import net.ooder.esd.annotation.menu.CustomGalleryMenu;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.GalleryEventBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.RightContextMenuBean;
import net.ooder.esd.bean.data.CustomGalleryDataBean;
import net.ooder.esd.bean.grid.PageBarBean;
import net.ooder.esd.bean.view.CustomGalleryViewBean;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.custom.action.CustomAPICallAction;
import net.ooder.esd.custom.action.ShowPageAction;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.*;
import net.ooder.esd.tool.properties.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CustomGalleryComponent extends CustomModuleComponent<GalleryComponent> {


    @JSONField(serialize = false)
    private String editorPath;

    @JSONField(serialize = false)
    private String addPath;

    @JSONField(serialize = false)
    private String saveUrl;

    @JSONField(serialize = false)
    private String sortPath;

    @JSONField(serialize = false)
    private String delPath;

    @JSONField(serialize = false)
    public EUModule editorModule;

    @JSONField(serialize = false)
    public EUModule addModule;

    @JSONField(serialize = false)
    private String clickFlagPath;

    RightContextMenuBean contextMenuBean;


    @JSONField(serialize = false)
    private PageBarComponent<PageBarProperties, PageEventEnum, Integer> pageBarComponent;


    public CustomGalleryComponent() {
        super();
    }

    public CustomGalleryComponent(EUModule module, MethodConfig methodConfig, Map<String, Object> valueMap) throws ClassNotFoundException {
        super(module, methodConfig, valueMap);
    }

    public void addChildNav(GalleryComponent currComponent) {
        super.addChildLayoutNav(currComponent);
        this.setCurrComponent(currComponent);
        try {
            this.addChildren(this.genAPIComponent(methodAPIBean, this.getCtxBaseComponent()));
        } catch (JDSException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    protected void fillGalleryAction(CustomGalleryViewBean viewBean, Component currComponent) {

        PageBarBean pageBarBean = viewBean.getPageBar();
        if (pageBarBean != null) {
            this.pageBarComponent = new PageBarComponent(currComponent.getAlias() + "_" + ComponentType.PAGEBAR.name(), new PageBarProperties(pageBarBean));
            DivComponent divComponent = new DivComponent(pageBarComponent, pageBarBean.getDock());
            if (!pageBarBean.getHeight().equals("")) {
                divComponent.getProperties().setHeight(pageBarBean.getHeight());
            } else {
                divComponent.getProperties().setHeight("2.5em");
            }
            mainComponent.addChildren(divComponent);
        }

        Set<CustomGalleryEvent> customFormEvents = viewBean.getEvent();
        for (CustomGalleryEvent eventType : customFormEvents) {
            for (CustomAction actionType : eventType.getActions(false)) {
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
        List<CustomGalleryMenu> customFormMenus = viewBean.getCustomMenu();
        if (customFormMenus != null && customFormMenus.size() > 0) {
            this.getMenuBar(viewBean.getMenuBar()).addMenu(customFormMenus.toArray(new CustomGalleryMenu[]{}));
        }

        List<CustomGalleryMenu> customBottombar = viewBean.getBottombarMenu();
        if (customBottombar != null && customBottombar.size() > 0) {
            this.getBottomBar(viewBean.getBottomBar()).addMenu(customBottombar.toArray(new CustomGalleryMenu[]{}));
        }
        super.fillAction(viewBean);
        this.fillMenuAction(viewBean, currComponent);
        this.fillCustomAction(viewBean, currComponent);

    }

    protected void fillMenuAction(CustomGalleryViewBean viewBean, Component currComponent) {
        try {
            if (viewBean != null) {

                super.fillContextAction(viewBean, currComponent);
                super.fillToolBar(viewBean, currComponent);
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }

    protected void fillCustomAction(CustomGalleryViewBean view, Component currComponent) {
        Set<GalleryEventBean> extAPIEvent = view.getExtAPIEvent();
        for (GalleryEventBean eventEnum : extAPIEvent) {
            List<Action> actions = eventEnum.getActions();
            for (Action action : actions) {
                action.updateArgs("{page." + this.getAlias() + "}", 3);
                currComponent.addAction(action, true, eventEnum.getEventReturn());
            }
        }
    }


    protected void fillViewAction(MethodConfig methodConfig) {
        CustomGalleryDataBean dataBean = (CustomGalleryDataBean) methodConfig.getDataBean();

        if (dataBean != null) {
            if (dataBean.getDataUrl() != null && !dataBean.getDataUrl().equals("")) {
                this.dataUrl = dataBean.getDataUrl();
            }
            if (dataBean.getEditorPath() != null && !dataBean.getEditorPath().equals("")) {
                this.editorPath = dataBean.getEditorPath();
            }
            if (dataBean.getDelPath() != null && !dataBean.getDelPath().equals("")) {
                this.delPath = dataBean.getDelPath();
            }

            if (dataBean.getAddPath() != null && !dataBean.getAddPath().equals("")) {
                this.addPath = dataBean.getAddPath();
            }
            if (dataBean.getSortPath() != null && !dataBean.getSortPath().equals("")) {
                this.sortPath = dataBean.getSortPath();
            }
            if (dataUrl == null || dataUrl.equals("")) {
                MethodConfig methodAPIBean = methodConfig.getDataBean().getMethodEvent(CustomGalleryEvent.RELOAD);
                if (methodAPIBean != null) {
                    dataUrl = methodAPIBean.getUrl();
                }

            }
            if (addPath == null || addPath.equals("")) {
                MethodConfig methodAPIBean = methodConfig.getDataBean().getMethodByItem(CustomMenuItem.ADD);
                if (methodAPIBean != null) {
                    addPath = methodAPIBean.getUrl();
                }
            }

            if (editorPath == null || editorPath.equals("")) {
                MethodConfig methodAPIBean = methodConfig.getDataBean().getMethodByItem(CustomMenuItem.EDITOR);
                if (methodAPIBean != null) {
                    editorPath = methodAPIBean.getUrl();
                }
            }

            if (delPath == null || delPath.equals("")) {
                MethodConfig methodAPIBean = methodConfig.getDataBean().getMethodByItem(CustomMenuItem.DELETE);
                if (methodAPIBean != null) {
                    delPath = methodAPIBean.getUrl();
                }
            }
            if (dataUrl == null || dataUrl.equals("")) {
                this.dataUrl = methodConfig.getUrl();
            }
            if (clickFlagPath == null || clickFlagPath.equals("")) {
                MethodConfig methodAPIBean = methodConfig.getDataBean().getMethodEvent(CustomGalleryEvent.FLAGCLICK);
                if (methodAPIBean != null) {
                    clickFlagPath = methodAPIBean.getUrl();
                }
            }

        }


    }


    public PageBarComponent getPageBarComponent() {
        return pageBarComponent;
    }

    public void setPageBarComponent(PageBarComponent<PageBarProperties, PageEventEnum, Integer> pageBarComponent) {
        this.pageBarComponent = pageBarComponent;
    }

    //数据对象
    @JSONField(serialize = false)
    APICallerComponent[] genAPIComponent(MethodConfig methodConfig, Component ctxComponent) throws JDSException, ClassNotFoundException {
        GalleryComponent currComponent = this.getCurrComponent();
        List<APICallerComponent> apiCallerComponents = new ArrayList<APICallerComponent>();
        valueMap.put(methodConfig.getUrl(), this.getEuModule());
        if (editorPath != null && !editorPath.equals("")) {
            this.editorModule = CustomViewFactory.getInstance().getView(this.euModule, editorPath, valueMap);
            if (this.editorModule == null) {
                this.logger.warn("编辑视图加载失败[" + this.className + "===》" + editorPath);
            }
        }
        if (addPath != null && !addPath.equals("")) {
            this.addModule = CustomViewFactory.getInstance().getView(this.euModule, addPath, valueMap);
            if (this.addModule == null) {
                this.logger.warn("添加视图加载失败[" + this.className + "===》" + addPath);
            }
        }


        MethodConfig flagClickMethod = methodAPIBean.getDataBean().getMethodEvent(CustomGalleryEvent.FLAGCLICK);
        if (flagClickMethod != null) {

            APICallerComponent flagClickApi = new APICallerComponent(flagClickMethod);

            if (!flagClickMethod.isModule()) {
                APICallerProperties sortProperties = flagClickApi.getProperties();
                UrlPathData requestPathData = new UrlPathData(currComponent.getAlias(), RequestPathTypeEnum.OPINION, "");
                if (requestPathData != null) {
                    sortProperties.addRequestData(requestPathData);
                }
                UrlPathData ctxData = new UrlPathData(ctxComponent.getAlias(), RequestPathTypeEnum.FORM, "");
                sortProperties.addRequestData(ctxData);
                apiCallerComponents.add(flagClickApi);
                currComponent.addAction(new CustomAPICallAction(flagClickApi, GalleryEventEnum.onFlagClick));

            } else {
                ShowPageAction action = new ShowPageAction(GalleryEventEnum.onFlagClick);
                action.setDesc("点击" + flagClickApi.getAlias());
                action.updateArgs("{args[1]}", 5);
                action.setTarget(flagClickMethod.getEUClassName());
                action.setClassName(flagClickMethod.getEUClassName());
                action.setRedirection("page:" + flagClickMethod.getEUClassName() + ":show2");
                action.set_return(false);
                currComponent.addAction(action);
            }
        }

        if (delPath != null && !delPath.equals("")) {
            MethodConfig methodBean = getMethodBeanByItem(CustomMenuItem.DELETE);
            if (methodBean != null) {
                UrlPathData requestPathData = null;
                switch (currComponent.typeKey) {
                    case GALLERY:
                        requestPathData = new UrlPathData(currComponent.getAlias(), RequestPathTypeEnum.GALLERY, "");
                        break;
                    case TREEGRID:
                        requestPathData = new UrlPathData(currComponent.getAlias(), RequestPathTypeEnum.TREEGRID, "");
                        break;

                }
                APICallerComponent deleteAPI = new APICallerComponent(methodBean);
                deleteAPI.setAlias(CustomGridAction.DELETE.getTarget());
                APICallerProperties deleteProperties = deleteAPI.getProperties();
                if (requestPathData != null) {
                    deleteProperties.addRequestData(requestPathData);
                }

                UrlPathData ctxData = new UrlPathData(ctxComponent.getAlias(), RequestPathTypeEnum.FORM, "");
                deleteProperties.addRequestData(ctxData);
                apiCallerComponents.add(deleteAPI);
            } else {
                this.logger.error("【删除】绑定失败[" + this.className + "===》" + addPath);
            }
        }


//        if (sortPath != null && !sortPath.equals("")) {
//
//            MethodConfig methodBean = methodAPIBean.getDataBean().getMethodEvent(CustomGalleryEvent.PANMOVE);
//
//            if (methodBean != null) {
//                APICallerComponent sortAPI = new APICallerComponent(methodBean);
//                sortAPI.setAlias(CustomGridAction.SORTUP.getTarget());
//                APICallerProperties sortProperties = sortAPI.getProperties();
//                UrlPathData requestPathData = new UrlPathData(currComponent.getAlias(), RequestPathTypeEnum.GALLERY, "");
//
//                if (requestPathData != null) {
//                    sortProperties.addRequestData(requestPathData);
//                }
//                UrlPathData ctxData = new UrlPathData(ctxComponent.getAlias(), RequestPathTypeEnum.FORM, "");
//                sortProperties.addRequestData(ctxData);
//                apiCallerComponents.add(sortAPI);
//            } else {
//                this.logger.error("【排序】绑定失败[" + this.className + "===》" + sortPath);
//            }
//        }

        //装载保存事件
        if (saveUrl != null && !saveUrl.equals("")) {
            MethodConfig methodBean = getMethodBeanByItem(CustomMenuItem.SAVE);
            if (methodBean != null) {
                APICallerComponent saveAPI = new APICallerComponent(methodBean);
                saveAPI.setAlias(CustomGridAction.SAVE.getTarget());
                APICallerProperties saveProperties = saveAPI.getProperties();
                UrlPathData savepathData = new UrlPathData(this.getCurrComponent().getParent().getAlias(), RequestPathTypeEnum.FORM, "");
                saveProperties.addRequestData(savepathData);
                UrlPathData requestPathData = new UrlPathData(currComponent.getAlias(), RequestPathTypeEnum.GALLERY, "");

                if (requestPathData != null) {
                    saveProperties.addRequestData(requestPathData);
                }

                UrlPathData ctxData = new UrlPathData(this.getCtxBaseComponent().getAlias(), RequestPathTypeEnum.FORM, "");
                saveProperties.addRequestData(ctxData);

                CustomGalleryDataBean dataBean = (CustomGalleryDataBean) methodAPIBean.getDataBean();
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

        if (dataUrl != null && !dataUrl.equals("")) {
            MethodConfig methodBean = null;
            if (methodAPIBean != null) {
                methodBean = methodAPIBean;
            } else {
                methodBean = getMethodBeanByItem(CustomMenuItem.RELOAD);
            }

            if (methodBean != null) {
                UrlPathData responsePathData = new UrlPathData(currComponent.getAlias(), ResponsePathTypeEnum.GALLERY, "data");
                APICallerComponent loadApi = new APICallerComponent(methodBean);
                APICallerProperties apiProperties = loadApi.getProperties();
                loadApi.setAlias(CustomGridAction.RELOAD.getTarget());

                UrlPathData ctxData = new UrlPathData(ctxComponent.getAlias(), RequestPathTypeEnum.FORM, "");
                apiProperties.addRequestData(ctxData);


                if (responsePathData != null) {
                    apiProperties.addResponseData(responsePathData);
                }

                UrlPathData formCtxData = new UrlPathData(ctxComponent.getAlias(), ResponsePathTypeEnum.FORM, "data");
                apiProperties.addResponseData(formCtxData);

                if (pageBarComponent != null) {
                    UrlPathData pageData = new UrlPathData(pageBarComponent.getAlias(), RequestPathTypeEnum.PAGEBAR, "");
                    apiProperties.addRequestData(pageData);
                }


                if (pageBarComponent != null) {
                    UrlPathData pageBarPathData = new UrlPathData(pageBarComponent.getAlias(), ResponsePathTypeEnum.PAGEBAR, "size");
                    apiProperties.addResponseData(pageBarPathData);
                }
                apiProperties.setAutoRun(true);
                apiCallerComponents.add(loadApi);
            } else {
                this.logger.error("【数据加载】绑定失败[" + this.className + "===》" + dataUrl);
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


    public String getEditorPath() {
        return editorPath;
    }

    public void setEditorPath(String editorPath) {
        this.editorPath = editorPath;
    }

    public String getAddPath() {
        return addPath;
    }

    public void setAddPath(String addPath) {
        this.addPath = addPath;
    }

    public String getSortPath() {
        return sortPath;
    }

    public void setSortPath(String sortPath) {
        this.sortPath = sortPath;
    }

    public String getDelPath() {
        return delPath;
    }

    public void setDelPath(String delPath) {
        this.delPath = delPath;
    }


    public RightContextMenuBean getContextMenuBean() {
        return contextMenuBean;
    }

    public void setContextMenuBean(RightContextMenuBean contextMenuBean) {
        this.contextMenuBean = contextMenuBean;
    }

    public EUModule getEditorModule() {
        return editorModule;
    }

    public void setEditorModule(EUModule editorModule) {
        this.editorModule = editorModule;
    }

    public EUModule getAddModule() {
        return addModule;
    }

    public void setAddModule(EUModule addModule) {
        this.addModule = addModule;
    }

}
