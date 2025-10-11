package net.ooder.esd.custom.component.grid;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.CustomMenu;
import net.ooder.esd.annotation.action.CustomGridAction;
import net.ooder.esd.annotation.event.CustomGridEvent;
import net.ooder.esd.annotation.event.PageEventEnum;
import net.ooder.esd.annotation.menu.GridMenu;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.annotation.ui.RequestPathTypeEnum;
import net.ooder.esd.annotation.ui.ResponsePathTypeEnum;
import net.ooder.esd.bean.GridEventBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.data.CustomGridDataBean;
import net.ooder.esd.bean.grid.GridRowCmdBean;
import net.ooder.esd.bean.grid.PageBarBean;
import net.ooder.esd.bean.view.CustomGridViewBean;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.custom.action.ShowPageAction;
import net.ooder.esd.custom.component.CustomModuleComponent;
import net.ooder.esd.custom.properties.CustomCmdBar;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.*;
import net.ooder.esd.tool.properties.APICallerProperties;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.PageBarProperties;
import net.ooder.esd.tool.properties.UrlPathData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CustomGridComponent<M extends TreeGridComponent> extends CustomModuleComponent<M> {


    @JSONField(serialize = false)
    private String editorPath;

    @JSONField(serialize = false)
    private String saveRowPath;

    @JSONField(serialize = false)
    private String saveAllRowPath;

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
    public CustomCmdBar cmdBar;


    @JSONField(serialize = false)
    private PageBarComponent<PageBarProperties, PageEventEnum, Integer> pageBarComponent;

    public CustomGridComponent() {
        super();
    }

    public CustomGridComponent(EUModule module, MethodConfig methodConfig, Map<String, Object> valueMap) {
        super(module, methodConfig, valueMap);
    }


    //填充默认绑定动作
    protected void fillMenuAction(CustomGridViewBean viewBean, Component currComponent) {
        try {
            if (viewBean != null) {
                this.fillRowCmd(viewBean, currComponent);
                super.fillContextAction(viewBean, currComponent);
                super.fillToolBar(viewBean, currComponent);

            }
        } catch (JDSException e) {
            e.printStackTrace();
        }


    }


    public void addChildNav(Component currComponent) {
        super.addChildLayoutNav(currComponent);
        this.setCurrComponent((M) currComponent);
        try {
            this.addChildren(this.genAPIComponent(methodAPIBean, this.getCtxBaseComponent()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void fillGridAction(CustomGridViewBean viewBean, Component currComponent) {
        PageBarBean pageBarBean = viewBean.getPageBar();
        if (pageBarBean != null) {
            this.pageBarComponent = new PageBarComponent(currComponent.getAlias() + "_" + ComponentType.PAGEBAR.name(), new PageBarProperties(pageBarBean));
            DivComponent divComponent = new DivComponent(pageBarComponent, pageBarBean.getDock());
            if (pageBarBean.getHeight() != null && !pageBarBean.getHeight().equals("")) {
                divComponent.getProperties().setHeight(pageBarBean.getHeight());
            } else {
                divComponent.getProperties().setHeight("2.5em");
            }
            mainComponent.addChildren(divComponent);
        }

        Set<CustomGridEvent> customFormEvents = viewBean.getEvent();
        for (CustomGridEvent eventType : customFormEvents) {
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
        List<GridMenu> customFormMenus = viewBean.getCustomMenu();
        if (customFormMenus != null && customFormMenus.size() > 0) {
            this.getMenuBar(viewBean.getMenuBar()).addMenu(customFormMenus.toArray(new CustomMenu[]{}));
        }

        List<GridMenu> customBottombar = viewBean.getBottombarMenu();
        if (customBottombar != null && customBottombar.size() > 0) {
            this.getBottomBar(viewBean.getBottomBar()).addMenu(customBottombar.toArray(new GridMenu[]{}));
        }
        this.fillContextAction(viewBean, currComponent);
        try {
            this.fillToolBar(viewBean, currComponent);
        } catch (JDSException e) {
            e.printStackTrace();
        }

        super.fillAction(viewBean);

        this.fillCustomAction(viewBean, currComponent);
    }


    protected void fillCustomAction(CustomGridViewBean view, Component currComponent) {
        Set<GridEventBean> extAPIEvent = view.getExtAPIEvent();
        for (GridEventBean eventEnum : extAPIEvent) {
            List<Action> actions = eventEnum.getActions();
            for (Action action : actions) {
                currComponent.addAction(action, true, eventEnum.getEventReturn());
            }
        }

        List<GridEventBean> fieldAPIEvents = view.getFieldAPIEvent();
        for (GridEventBean eventEnum : fieldAPIEvents) {
            List<Action> actions = eventEnum.getActions();
            for (Action action : actions) {
                currComponent.addAction(action, true, eventEnum.getEventReturn());
            }
        }
    }


    public void fillRowCmd(CustomGridViewBean viewBean, Component currComponent) {
        GridRowCmdBean rowCmdBean = viewBean.getRowCmdBean();
        if (rowCmdBean != null) {
            if (rowCmdBean.getRowMenu().size() > 0 || rowCmdBean.getMenuClass().length > 0) {
                cmdBar = new CustomCmdBar(rowCmdBean, currComponent, this);
            }
        }
    }


    public PageBarComponent getPageBarComponent() {
        return pageBarComponent;
    }

    public void setPageBarComponent(PageBarComponent pageBarComponent) {
        this.pageBarComponent = pageBarComponent;
    }

    //数据对象
    @JSONField(serialize = false)
    APICallerComponent[] genAPIComponent(MethodConfig methodConfig, Component ctxComponent) throws JDSException, ClassNotFoundException {
        fillViewAction(methodConfig);
        M currComponent = this.getCurrComponent();
        List<APICallerComponent> apiCallerComponents = new ArrayList<APICallerComponent>();
        valueMap.put(methodConfig.getUrl(), this.getEuModule());
        if (editorPath != null && !editorPath.equals("")) {
            this.editorModule = CustomViewFactory.getInstance().getView(this.euModule, editorPath, valueMap);
            if (this.editorModule == null) {
                MethodConfig methodBean = getMethodBeanByItem(CustomMenuItem.EDITOR);
                if (methodBean != null) {
                    this.editorModule = methodBean.getModule(valueMap, projectName);
                }
            }
            if (this.editorModule == null) {
                this.logger.warn("编辑视图加载失败[" + this.className + "===》" + editorPath);
            }
        }

        if (addPath != null && !addPath.equals("")) {
            this.addModule = CustomViewFactory.getInstance().getView(this.euModule, addPath, valueMap);
            if (this.addModule == null) {
                MethodConfig methodBean = getMethodBeanByItem(CustomMenuItem.ADD);
                if (methodBean != null) {
                    this.addModule = methodBean.getModule(valueMap, projectName);
                }
            }
            if (this.addModule == null) {
                this.logger.warn("添加视图加载失败[" + this.className + "===》" + addPath);
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

        if (saveRowPath != null && !saveRowPath.equals("")) {
            MethodConfig methodBean = getMethodBeanByItem(CustomMenuItem.SAVEROW);
            if (methodBean != null) {
                UrlPathData requestPathData = null;
                switch (currComponent.typeKey) {
                    case GALLERY:
                        requestPathData = new UrlPathData(currComponent.getAlias(), RequestPathTypeEnum.GALLERY, "");
                        break;
                    case TREEGRID:
                        requestPathData = new UrlPathData(currComponent.getAlias(), RequestPathTypeEnum.TREEGRIDROWVALUE, "");
                        break;

                }
                APICallerComponent saveRowAPI = new APICallerComponent(methodBean);
                saveRowAPI.setAlias(CustomGridAction.SAVEROW.getTarget());
                APICallerProperties saveRowAPIProperties = saveRowAPI.getProperties();
                if (requestPathData != null) {
                    saveRowAPIProperties.addRequestData(requestPathData);
                }

                UrlPathData ctxData = new UrlPathData(ctxComponent.getAlias(), RequestPathTypeEnum.FORM, "");
                saveRowAPIProperties.addRequestData(ctxData);
                apiCallerComponents.add(saveRowAPI);
            } else {
                this.logger.error("【删除】绑定失败[" + this.className + "===》" + saveRowPath);
            }
        }
        if (saveAllRowPath != null && !saveAllRowPath.equals("")) {
            MethodConfig methodBean = getMethodBeanByItem(CustomMenuItem.SAVEALLROW);
            if (methodBean != null) {
                UrlPathData requestPathData = null;
                switch (currComponent.typeKey) {
                    case GALLERY:
                        requestPathData = new UrlPathData(currComponent.getAlias(), RequestPathTypeEnum.GALLERY, "");
                        break;
                    case TREEGRID:
                        requestPathData = new UrlPathData(currComponent.getAlias(), RequestPathTypeEnum.TREEGRIDALLVALUE, "");
                        break;

                }
                APICallerComponent saveAllRowAPI = new APICallerComponent(methodBean);
                saveAllRowAPI.setAlias(CustomGridAction.SAVEALLROW.getTarget());
                APICallerProperties saveAllRowAPIProperties = saveAllRowAPI.getProperties();
                if (requestPathData != null) {
                    saveAllRowAPIProperties.addRequestData(requestPathData);
                }

                UrlPathData ctxData = new UrlPathData(ctxComponent.getAlias(), RequestPathTypeEnum.FORM, "");
                saveAllRowAPIProperties.addRequestData(ctxData);
                apiCallerComponents.add(saveAllRowAPI);
            } else {
                this.logger.error("【删除】绑定失败[" + this.className + "===》" + saveAllRowPath);
            }
        }
        if (sortPath != null && !sortPath.equals("")) {
            MethodConfig methodBean = methodConfig.getDataBean().getMethodEvent(CustomGridEvent.CLICKSORTDOWN);
            if (methodBean != null) {
                APICallerComponent sortAPI = new APICallerComponent(methodBean);
                sortAPI.setAlias(CustomGridAction.SORTUP.getTarget());
                APICallerProperties sortProperties = sortAPI.getProperties();
                UrlPathData requestPathData = null;
                switch (currComponent.typeKey) {
                    case GALLERY:
                        requestPathData = new UrlPathData(currComponent.getAlias(), RequestPathTypeEnum.GALLERY, "");
                        break;
                    case TREEGRID:
                        requestPathData = new UrlPathData(currComponent.getAlias(), RequestPathTypeEnum.TREEGRIDROW, "");
                        break;
                }
                if (requestPathData != null) {
                    sortProperties.addRequestData(requestPathData);
                }
                UrlPathData ctxData = new UrlPathData(ctxComponent.getAlias(), RequestPathTypeEnum.FORM, "");
                sortProperties.addRequestData(ctxData);
                apiCallerComponents.add(sortAPI);
            } else {
                this.logger.error("【排序】绑定失败[" + this.className + "===》" + sortPath);
            }
        }

        //装载保存事件
        if (saveUrl != null && !saveUrl.equals("")) {
            MethodConfig methodBean = getMethodBeanByItem(CustomMenuItem.SAVE);
            if (methodBean != null) {
                APICallerComponent saveAPI = new APICallerComponent(methodBean);
                saveAPI.setAlias(CustomGridAction.SAVE.getTarget());
                APICallerProperties saveProperties = saveAPI.getProperties();
                UrlPathData savepathData = new UrlPathData(this.getCurrComponent().getParent().getAlias(), RequestPathTypeEnum.FORM, "");
                saveProperties.addRequestData(savepathData);
                UrlPathData requestPathData = null;
                switch (currComponent.typeKey) {
                    case GALLERY:
                        requestPathData = new UrlPathData(currComponent.getAlias(), RequestPathTypeEnum.GALLERY, "");
                        break;
                    case TREEGRID:
                        requestPathData = new UrlPathData(currComponent.getAlias(), RequestPathTypeEnum.TREEGRIDROW, "");
                        break;
                }

                if (requestPathData != null) {
                    saveProperties.addRequestData(requestPathData);
                }
                UrlPathData ctxData = new UrlPathData(this.getCtxBaseComponent().getAlias(), RequestPathTypeEnum.FORM, "");
                saveProperties.addRequestData(ctxData);
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

            UrlPathData responsePathData = null;
            switch (currComponent.typeKey) {
                case GALLERY:
                    responsePathData = new UrlPathData(currComponent.getAlias(), ResponsePathTypeEnum.GALLERY, "data");
                    break;
                case TREEGRID:
                    responsePathData = new UrlPathData(currComponent.getAlias(), ResponsePathTypeEnum.TREEGRID, "data");
                    break;
            }
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
                UrlPathData pageBarPathData = new UrlPathData(pageBarComponent.getAlias(), ResponsePathTypeEnum.PAGEBAR, "size");
                apiProperties.addResponseData(pageBarPathData);
            }

            apiProperties.setAutoRun(true);
            apiCallerComponents.add(loadApi);
        } else {
            this.logger.error("【数据加载】绑定失败[" + this.className + "===》" + dataUrl);
        }

        return apiCallerComponents.toArray(new APICallerComponent[]{});
    }


    protected void fillViewAction(MethodConfig methodConfig) {
        CustomGridDataBean dataBean = (CustomGridDataBean) methodConfig.getDataBean();

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
                MethodConfig methodAPIBean = methodConfig.getDataBean().getMethodByItem(CustomMenuItem.RELOAD);
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

            if (sortPath == null || sortPath.equals("")) {
                MethodConfig methodAPIBean = methodConfig.getDataBean().getMethodEvent(CustomGridEvent.CLICKSORTDOWN);
                if (methodAPIBean != null) {
                    sortPath = methodAPIBean.getUrl();
                }
            }

            if (saveRowPath == null || saveRowPath.equals("")) {
                MethodConfig methodAPIBean = methodConfig.getDataBean().getMethodByItem(CustomMenuItem.SAVEROW);
                if (methodAPIBean != null) {
                    saveRowPath = methodAPIBean.getUrl();
                }
            }
            if (saveAllRowPath == null || saveAllRowPath.equals("")) {
                MethodConfig methodAPIBean = methodConfig.getDataBean().getMethodByItem(CustomMenuItem.SAVEALLROW);
                if (methodAPIBean != null) {
                    saveAllRowPath = methodAPIBean.getUrl();
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

        }
    }

    public String getSaveUrl() {
        return saveUrl;
    }

    public void setSaveUrl(String saveUrl) {
        this.saveUrl = saveUrl;
    }

    public String getSaveRowPath() {
        return saveRowPath;
    }

    public void setSaveRowPath(String saveRowPath) {
        this.saveRowPath = saveRowPath;
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

    public CustomCmdBar getCmdBar() {
        return cmdBar;
    }

    public void setCmdBar(CustomCmdBar cmdBar) {
        this.cmdBar = cmdBar;
    }
}
