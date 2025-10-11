package net.ooder.esd.custom.component;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.action.CustomGridAction;
import net.ooder.esd.annotation.event.*;
import net.ooder.esd.annotation.menu.ContentBlockMenu;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.data.CustomContentBlockDataBean;
import net.ooder.esd.bean.view.CustomContentBlockViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.RightContextMenuBean;
import net.ooder.esd.bean.grid.PageBarBean;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.custom.action.CustomAPICallAction;
import net.ooder.esd.custom.action.MShowPageAction;
import net.ooder.esd.custom.action.ShowPageAction;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.*;
import net.ooder.esd.tool.properties.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CustomContentBlockComponent extends CustomModuleComponent<ContentBlockComponent> {


    @JSONField(serialize = false)
    private String editorPath;

    @JSONField(serialize = false)
    private String addPath;

    @JSONField(serialize = false)
    private String clickFlagPath;

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


    RightContextMenuBean contextMenuBean;


    @JSONField(serialize = false)
    private PageBarComponent<PageBarProperties, PageEventEnum, Integer> pageBarComponent;


    public CustomContentBlockComponent() {
        super();
    }

    public CustomContentBlockComponent(EUModule module, MethodConfig methodConfig, Map<String, Object> valueMap) {
        super(module, methodConfig, valueMap);

    }

    public void addChildNav(ContentBlockComponent currComponent) {
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

    protected void fillContentBlockAction(CustomContentBlockViewBean viewBean, Component currComponent) {

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

        Set<CustomContentBlockEvent> customFormEvents = viewBean.getEvent();
        for (CustomContentBlockEvent eventType : customFormEvents) {
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
                        action = new MShowPageAction(methodConfig, eventType.getEventEnum());
                        action.updateArgs("{args[1]}", 6);
                        action.updateArgs("{args[1].tagVar}", 5);
                    }
                }
                currComponent.addAction(action, false);
            }
        }
        List<ContentBlockMenu> customFormMenus = viewBean.getCustomMenu();
        if (customFormMenus != null && customFormMenus.size() > 0) {
            this.getMenuBar(viewBean.getMenuBar()).addMenu(customFormMenus.toArray(new ContentBlockMenu[]{}));
        }

        List<ContentBlockMenu> customBottombar = viewBean.getBottombarMenu();
        if (customBottombar != null && customBottombar.size() > 0) {
            this.getBottomBar(viewBean.getBottomBar()).addMenu(customBottombar.toArray(new ContentBlockMenu[]{}));
        }
        super.fillAction(viewBean);
        this.fillMenuAction(viewBean, currComponent);
    }

    protected void fillMenuAction(CustomContentBlockViewBean viewBean, Component currComponent) {
        try {
            if (viewBean != null) {
                super.fillContextAction(viewBean, currComponent);
                super.fillToolBar(viewBean, currComponent);
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }


    }


    protected void fillViewAction(MethodConfig methodConfig) {
        CustomContentBlockDataBean dataBean = (CustomContentBlockDataBean) methodConfig.getDataBean();

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
            if (clickFlagPath == null || clickFlagPath.equals("")) {
                MethodConfig methodAPIBean = methodConfig.getDataBean().getMethodEvent(CustomGalleryEvent.FLAGCLICK);
                if (methodAPIBean != null) {
                    clickFlagPath = methodAPIBean.getUrl();
                }
            }
//            if (sortPath == null || sortPath.equals("")) {
//                MethodConfig methodAPIBean = methodConfig.getDataBean().getMethodEvent(CustomGalleryEvent.PANMOVE);
//                if (methodAPIBean != null) {
//                    sortPath = methodAPIBean.getUrl();
//                }
//            }

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


    public PageBarComponent getPageBarComponent() {
        return pageBarComponent;
    }

    public void setPageBarComponent(PageBarComponent<PageBarProperties, PageEventEnum, Integer> pageBarComponent) {
        this.pageBarComponent = pageBarComponent;
    }

    //数据对象
    @JSONField(serialize = false)
    APICallerComponent[] genAPIComponent(MethodConfig methodConfig, Component ctxComponent) throws JDSException, ClassNotFoundException {
        ContentBlockComponent currComponent = this.getCurrComponent();
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

        MethodConfig deleteMethodBean = null;
        if (delPath != null && !delPath.equals("")) {
            deleteMethodBean = ESDFacrory.getAdminESDClient().getMethodAPIBean(delPath, projectName);
        } else {
            deleteMethodBean = getMethodBeanByItem(CustomMenuItem.DELETE);
        }


        if (deleteMethodBean != null) {
            UrlPathData requestPathData = new UrlPathData(currComponent.getAlias(), RequestPathTypeEnum.CONTENTBLOCK, "");
            APICallerComponent deleteAPI = new APICallerComponent(deleteMethodBean);
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

        MethodConfig sortMethodBean = null;
        if (sortPath != null && !sortPath.equals("")) {
            sortMethodBean = ESDFacrory.getAdminESDClient().getMethodAPIBean(sortPath, projectName);
        }
        if (sortMethodBean != null) {
            APICallerComponent sortAPI = new APICallerComponent(sortMethodBean);
            sortAPI.setAlias(CustomGridAction.SORTUP.getTarget());
            APICallerProperties sortProperties = sortAPI.getProperties();
            UrlPathData requestPathData = new UrlPathData(currComponent.getAlias(), RequestPathTypeEnum.CONTENTBLOCK, "");
            if (requestPathData != null) {
                sortProperties.addRequestData(requestPathData);
            }
            UrlPathData ctxData = new UrlPathData(ctxComponent.getAlias(), RequestPathTypeEnum.FORM, "");
            sortProperties.addRequestData(ctxData);
            apiCallerComponents.add(sortAPI);
        } else {
            this.logger.error("【排序】绑定失败[" + this.className + "===》" + sortPath);
        }


        MethodConfig clickFlagMethodBean = null;
        if (clickFlagPath != null && !clickFlagPath.equals("")) {
            clickFlagMethodBean = ESDFacrory.getAdminESDClient().getMethodAPIBean(clickFlagPath, projectName);
        } else {
            clickFlagMethodBean = methodConfig.getDataBean().getMethodEvent(CustomContentBlockEvent.ONMORECLICK);
        }


        if (clickFlagMethodBean != null) {
            APICallerComponent flagClickApi = new APICallerComponent(clickFlagMethodBean);
            if (!clickFlagMethodBean.isModule()) {
                APICallerProperties sortProperties = flagClickApi.getProperties();
                UrlPathData requestPathData = new UrlPathData(currComponent.getAlias(), RequestPathTypeEnum.CONTENTBLOCK, "");
                if (requestPathData != null) {
                    sortProperties.addRequestData(requestPathData);
                }
                UrlPathData ctxData = new UrlPathData(ctxComponent.getAlias(), RequestPathTypeEnum.FORM, "");
                sortProperties.addRequestData(ctxData);
                apiCallerComponents.add(flagClickApi);
                currComponent.addAction(new CustomAPICallAction(flagClickApi, ContentBlockEventEnum.onMoreClick));

            } else {
                ShowPageAction action = new ShowPageAction(ContentBlockEventEnum.onMoreClick);
                action.setDesc("点击" + flagClickApi.getAlias());
                action.updateArgs("{args[1]}", 5);
                action.setTarget(clickFlagMethodBean.getEUClassName());
                action.setClassName(clickFlagMethodBean.getEUClassName());
                action.setRedirection("page:" + clickFlagMethodBean.getEUClassName() + ":show2");
                action.set_return(false);
                currComponent.addAction(action);
            }
        } else {
            this.logger.error("【clickFlagPath】绑定失败[" + this.className + "===》" + sortPath);
        }


        MethodConfig saveMethodBean = null;
        //装载保存事件
        if (saveUrl != null && !saveUrl.equals("")) {
            saveMethodBean = ESDFacrory.getAdminESDClient().getMethodAPIBean(dataUrl, projectName);
        } else {
            saveMethodBean = getMethodBeanByItem(CustomMenuItem.SAVE);
        }

        if (saveMethodBean != null) {
            APICallerComponent saveAPI = new APICallerComponent(saveMethodBean);
            saveAPI.setAlias(CustomGridAction.SAVE.getTarget());
            APICallerProperties saveProperties = saveAPI.getProperties();
            UrlPathData savepathData = new UrlPathData(this.getCurrComponent().getParent().getAlias(), RequestPathTypeEnum.FORM, "");
            saveProperties.addRequestData(savepathData);
            UrlPathData requestPathData = new UrlPathData(currComponent.getAlias(), RequestPathTypeEnum.CONTENTBLOCK, "");

            if (requestPathData != null) {
                saveProperties.addRequestData(requestPathData);
            }

            UrlPathData ctxData = new UrlPathData(this.getCtxBaseComponent().getAlias(), RequestPathTypeEnum.FORM, "");
            saveProperties.addRequestData(ctxData);
            CustomContentBlockDataBean dataBean = (CustomContentBlockDataBean) methodConfig.getDataBean();
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

        MethodConfig methodBean = null;
        if (dataUrl != null && !dataUrl.equals("")) {
            methodBean = ESDFacrory.getAdminESDClient().getMethodAPIBean(dataUrl, projectName);
        } else {
            if (methodAPIBean != null) {
                methodBean = methodAPIBean;
            } else {
                methodBean = getMethodBeanByItem(CustomMenuItem.RELOAD);
            }
        }


        if (methodBean != null) {
            UrlPathData responsePathData = new UrlPathData(currComponent.getAlias(), ResponsePathTypeEnum.CONTENTBLOCK, "data");
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

        return apiCallerComponents.toArray(new APICallerComponent[]{});
    }

    public String getClickFlagPath() {
        return clickFlagPath;
    }

    public void setClickFlagPath(String clickFlagPath) {
        this.clickFlagPath = clickFlagPath;
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
