package net.ooder.esd.custom.component;

import com.alibaba.fastjson.annotation.JSONField;
import javassist.NotFoundException;
import net.ooder.common.JDSException;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.action.CustomAPIMethod;
import net.ooder.esd.annotation.action.CustomFormAction;
import net.ooder.esd.annotation.action.CustomTreeAction;
import net.ooder.esd.annotation.event.ActionTypeEnum;
import net.ooder.esd.annotation.event.CustomTreeEvent;
import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.annotation.event.TreeViewEventEnum;
import net.ooder.esd.annotation.menu.ContextMenu;
import net.ooder.esd.annotation.menu.TreeMenu;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.*;
import net.ooder.esd.bean.view.ChildTreeViewBean;
import net.ooder.esd.bean.view.CustomTreeViewBean;
import net.ooder.esd.custom.action.CustomAPICallAction;
import net.ooder.esd.custom.action.ShowPageAction;
import net.ooder.esd.custom.properties.CustomCmdBar;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.manager.editor.PluginsFactory;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.component.ComboInputComponent;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.TreeViewComponent;
import net.ooder.esd.tool.properties.APICallerProperties;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.Condition;
import net.ooder.esd.tool.properties.UrlPathData;
import net.ooder.esd.tool.properties.form.ComboInputProperties;

import java.util.*;

public class CustomTreeComponent<M extends TreeViewComponent> extends CustomModuleComponent<M> {

    public String saveUrl;
    public String editorPath;
    public String saveRowPath;
    public ResponsePathTypeEnum itemType;

    @JSONField(serialize = false)
    public String loadChildUrl;

    @JSONField(serialize = false)
    CustomCmdBar cmdBar;

    public CustomTreeComponent() {
        super();
    }

    public CustomTreeComponent(EUModule module, MethodConfig methodConfig, Map valueMap) {
        super(module, methodConfig, valueMap);

    }


    //填充默认绑定动作
    protected void fillMenuAction(CustomTreeViewBean viewBean, Component currComponent) {
        try {
            if (viewBean != null) {
                this.fillRowCmd(viewBean, currComponent);
                this.fillContextAction(viewBean, currComponent);
                this.fillLazyLoadAction(viewBean);
                super.fillToolBar(viewBean, currComponent);
                this.fillHelpBar(viewBean, currComponent);
                this.fillCustomAction(viewBean, currComponent);

            }
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }

    void fillContextAction(CustomTreeViewBean viewBean, Component currComponent) {
        Set<ChildTreeViewBean> childTreeViewBeans = viewBean.getChildTreeViewBeans();
        if (!childTreeViewBeans.isEmpty()) {
            for (ChildTreeViewBean childTreeViewBean : childTreeViewBeans) {
                boolean showOpt = viewBean.getOptBtn() != null && !viewBean.getOptBtn().equals("");
                try {
                    fillTreeContextAction(childTreeViewBean, currComponent, showOpt);
                } catch (NotFoundException e) {
                    e.printStackTrace();
                } catch (JDSException e) {
                    e.printStackTrace();
                }
            }
        }
        super.fillContextAction(viewBean, currComponent);
    }


    protected void fillTreeContextAction(ChildTreeViewBean childTreeViewBean, Component currComponent, boolean showOpt) throws NotFoundException, JDSException {
        if (childTreeViewBean != null) {
            RightContextMenuBean contextMenuBean = childTreeViewBean.getContextMenuBean();
            if (contextMenuBean != null) {

                CustomContextBar contextBar = new CustomContextBar(contextMenuBean);
                if (!contextMenuBean.getFristMenuClass().equals(Void.class)) {
                    Class contextMenu = contextMenuBean.getFristMenuClass();
                    contextBar = PluginsFactory.getInstance().initMenuClass(contextMenu, CustomContextBar.class);

                } else if (contextMenuBean.getContextMenu().size() > 0) {
                    Set<ContextMenu> menus = contextMenuBean.getContextMenu();
                    for (ContextMenu menu : menus) {
                        if (menu != null) {
                            contextBar.addMenu(menu);
                        }
                    }

                }
                if (contextBar != null) {
                    Condition condition = new Condition("{args[3].groupName}", SymbolType.equal, childTreeViewBean.getGroupName());
                    contextBar.bindModuleAction(currComponent, this, childTreeViewBean.getBindTypes(), Arrays.asList(condition));
                    if (showOpt) {
                        condition = new Condition("{args[1].groupName}", SymbolType.equal, childTreeViewBean.getGroupName());
                        contextBar.bindOptAction(currComponent, this, childTreeViewBean.getBindTypes(), Arrays.asList(condition));
                    }
                }
            }


        }
    }


    public void fillRowCmd(CustomTreeViewBean viewBean, Component currComponent) {
        TreeRowCmdBean rowCmdBean = viewBean.getRowCmdBean();
        if (rowCmdBean != null) {
            if (rowCmdBean.getRowMenu().size() > 0 || rowCmdBean.getMenuClass().length > 0) {
                cmdBar = new CustomCmdBar(rowCmdBean, currComponent, this);
            }
        }
    }


    //数据对象
    @JSONField(serialize = false)
    public List<APICallerComponent> genAPIComponent(CustomTreeViewBean viewBean) throws JDSException {
        List<APICallerComponent> apiCallerComponents = new ArrayList<APICallerComponent>();
        APICallerProperties apiCallerProperties = methodAPIBean.getApi().getApiCallerProperties();
        if ((methodAPIBean.getModuleBean().getDynLoad() != null && this.methodAPIBean.getModuleBean().getDynLoad()) || viewBean.getLazyLoad() || (apiCallerProperties != null && apiCallerProperties.getAutoRun() != null && apiCallerProperties.getAutoRun())) {
            MethodConfig methodBean = null;
            if (dataUrl != null && !dataUrl.equals("")) {
                methodBean = ESDFacrory.getAdminESDClient().getMethodAPIBean(dataUrl, projectName);
            }
            if (methodBean == null) {
                methodBean = getMethodBeanByItem(CustomMenuItem.RELOAD);
            }
            if (methodBean == null) {
                methodBean = methodAPIBean;
            }
            APICallerComponent reloadAPI = new APICallerComponent(methodBean);
            reloadAPI.setAlias(CustomTreeAction.RELOAD.getTarget());
            APICallerProperties reloadProperties = reloadAPI.getProperties();
            UrlPathData requestCtxData = new UrlPathData(this.getCtxBaseComponent().getAlias(), RequestPathTypeEnum.FORM, "");
            reloadProperties.addRequestData(requestCtxData);
            UrlPathData requestFormData = new UrlPathData(this.getCurrComponent().getParent().getAlias(), RequestPathTypeEnum.FORM, "");
            reloadProperties.addRequestData(requestFormData);
            UrlPathData formData = new UrlPathData(this.getCurrComponent().getAlias(), itemType, "data");
            reloadProperties.addResponseData(formData);
            UrlPathData formCtxData = new UrlPathData(this.getCtxBaseComponent().getAlias(), ResponsePathTypeEnum.FORM, "data");
            reloadProperties.addResponseData(formCtxData);
            reloadProperties.setAutoRun(true);
            apiCallerComponents.add(reloadAPI);
        }
        return apiCallerComponents;
    }


    void fillChildCustomAction(ChildTreeViewBean childTreeViewBean, Component currComponent) {
        String groupName = childTreeViewBean.getGroupName();
        Condition condition = new Condition("{args[1].groupName}", SymbolType.equal, groupName);
        Set<TreeEventBean> extAPIEvent = childTreeViewBean.getExtAPIEvent();
        MethodConfig methodConfig = childTreeViewBean.findMethod(CustomTreeEvent.TREENODEEDITOR);

        for (TreeEventBean eventEnum : extAPIEvent) {
            List<Action> actions = eventEnum.getActions();
            for (Action action : actions) {
                if (!action.getConditions().contains(condition)) {
                    action.getConditions().add(condition);
                }
                if (methodConfig != null) {
                    APICallerComponent apiCallerComponent = (APICallerComponent) this.findComponentByAlias(methodConfig.getMethodName());
                    if (apiCallerComponent == null) {
                        apiCallerComponent = new APICallerComponent(methodConfig);
                        this.addChildren(apiCallerComponent);
                    }
                    action.updateArgs("{page." + apiCallerComponent.getAlias() + "}", 3);
                }
                currComponent.addAction(action, true, eventEnum.getEventReturn());
            }
        }
    }


    void addReloadChildAPI(MethodConfig childMethod, String groupName) {
        Condition condition = new Condition("{args[1].groupName}", SymbolType.equal, groupName);
        APICallerComponent reloadChild = new APICallerComponent(childMethod);
        reloadChild.setAlias(groupName);
        APICallerProperties reloadProperties = reloadChild.getProperties();
        UrlPathData requestCtxData = new UrlPathData(this.getCtxBaseComponent().getAlias(), RequestPathTypeEnum.FORM, "");
        UrlPathData requestFormData = new UrlPathData(this.getCurrComponent().getParent().getAlias(), RequestPathTypeEnum.FORM, "");
        reloadProperties.addRequestData(requestCtxData);
        reloadProperties.addRequestData(requestFormData);

        Action setParamsIdAction = new Action(TreeViewEventEnum.onGetContent);
        if (condition != null) {
            setParamsIdAction.addCondition(condition);
        }

        setParamsIdAction.setArgs(Arrays.asList(new String[]{"{page." + reloadChild.getAlias() + ".setQueryData()}", null, null, "{args[1].id}", "id"}));
        setParamsIdAction.setType(ActionTypeEnum.control);
        setParamsIdAction.setDesc("添加当前节点ID参数");
        setParamsIdAction.setTarget(reloadChild.getAlias());
        setParamsIdAction.setMethod("setQueryData");
        setParamsIdAction.setRedirection("other:callback:call");
        setParamsIdAction.set_return(true);
        Action setParamsAction = new Action(TreeViewEventEnum.onGetContent);
        if (condition != null) {
            setParamsAction.addCondition(condition);
        } else {
            condition = new Condition("{args[1].groupName}", SymbolType.equal, "{}");
        }

        setParamsAction.setArgs(Arrays.asList(new String[]{"{page." + reloadChild.getAlias() + ".setQueryData()}", null, null, "{args[1].tagVar}", ""}));
        setParamsAction.setType(ActionTypeEnum.control);
        setParamsAction.setDesc("设置扩展参数");
        setParamsAction.setTarget(reloadChild.getAlias());
        setParamsAction.setMethod("setQueryData");
        setParamsAction.setRedirection("other:callback:call");
        setParamsAction.set_return(true);
        this.getCurrComponent().addAction(setParamsAction);

        Action callAction = new CustomAPICallAction(reloadChild, TreeViewEventEnum.onGetContent);
        if (condition != null) {
            callAction.addCondition(condition);
        }
        callAction.setArgs(Arrays.asList(new String[]{"{page." + reloadChild.getAlias() + ".invoke()}", "temp", null, "{args[2]}"}));
        callAction.setType(ActionTypeEnum.control);
        callAction.setTarget(reloadChild.getAlias());
        callAction.setDesc("子节点装载");
        callAction.setMethod(CustomAPIMethod.invoke.getType());
        callAction.setRedirection("other:callback:call");
        callAction.set_return(true);
        this.getCurrComponent().addAction(callAction);
        this.addChildren(reloadChild);
    }


    public void addTreeAction(CustomTreeViewBean treeViewBean) {
        try {
            fillLazyLoadAction(treeViewBean);
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }


    void fillLazyLoadAction(CustomTreeViewBean customTreeViewBean) throws JDSException {
        Set<ChildTreeViewBean> childTreeViewBeans = customTreeViewBean.getChildTreeViewBeans();
        for (ChildTreeViewBean childTreeViewBean : childTreeViewBeans) {
            if (childTreeViewBean.getLazyLoad() != null && childTreeViewBean.getLazyLoad()) {
                MethodConfig childMethod = customTreeViewBean.findMethodByEvent(CustomTreeEvent.RELOADCHILD, childTreeViewBean.getBindClass());
                if (childMethod != null) {
                    this.addReloadChildAPI(childMethod, childTreeViewBean.getGroupName());
                }
            }
        }
        for (CustomTreeViewBean childTree : customTreeViewBean.getAllChild()) {
            if (childTree.getLazyLoad() != null && childTree.getLazyLoad()) {
                MethodConfig childMethod = customTreeViewBean.findMethodByEvent(CustomTreeEvent.RELOADCHILD, customTreeViewBean.getBindService());
                if (childMethod != null) {
                    this.addReloadChildAPI(childMethod, childMethod.getEUClassName());
                }
            }
        }


        if (customTreeViewBean.getLazyLoad() != null && customTreeViewBean.getLazyLoad()) {
            if (!childTreeViewBeans.isEmpty()) {
                MethodConfig childMethod = customTreeViewBean.findMethodByEvent(CustomTreeEvent.RELOADCHILD, customTreeViewBean.getBindService());
                if (childMethod != null) {
                    this.addReloadChildAPI(childMethod, customTreeViewBean.getGroupName());
                }
            } else {
                MethodConfig methodBean = null;
                if (customTreeViewBean.getBindService() != null) {
                    methodBean = customTreeViewBean.findMethodByEvent(CustomTreeEvent.RELOADCHILD, customTreeViewBean.getBindService());
                }

                if (methodBean == null) {
                    methodBean = methodAPIBean.getDataBean().getMethodEvent(CustomTreeEvent.RELOADCHILD);
                }

                if (loadChildUrl == null || loadChildUrl.equals("")) {
                    loadChildUrl = dataUrl;
                }


                if (loadChildUrl != null && !loadChildUrl.equals("")) {
                    methodBean = ESDFacrory.getAdminESDClient().getMethodAPIBean(loadChildUrl, projectName);
                }


                if (methodBean == null) {
                    methodBean = methodAPIBean;
                }

                APICallerComponent reloadChild = new APICallerComponent(methodBean);
                reloadChild.setAlias(CustomTreeAction.RELOADCHILD.getTarget());


                APICallerProperties reloadProperties = reloadChild.getProperties();
                UrlPathData requestCtxData = new UrlPathData(this.getCtxBaseComponent().getAlias(), RequestPathTypeEnum.FORM, "");
                UrlPathData requestFormData = new UrlPathData(this.getCurrComponent().getParent().getAlias(), RequestPathTypeEnum.FORM, "");
                reloadProperties.addRequestData(requestCtxData);
                reloadProperties.addRequestData(requestFormData);

                Action setParamsIdAction = new Action(TreeViewEventEnum.onGetContent);
                setParamsIdAction.setArgs(Arrays.asList(new String[]{"{page." + reloadChild.getAlias() + ".setQueryData()}", null, null, "{args[1].id}", "id"}));
                setParamsIdAction.setType(ActionTypeEnum.control);
                setParamsIdAction.setDesc("添加当前节点ID参数");
                setParamsIdAction.setTarget(reloadChild.getAlias());
                setParamsIdAction.setMethod("setQueryData");
                setParamsIdAction.setRedirection("other:callback:call");
                setParamsIdAction.set_return(false);

                Action setParamsAction = new Action(TreeViewEventEnum.onGetContent);
                setParamsAction.setArgs(Arrays.asList(new String[]{"{page." + reloadChild.getAlias() + ".setQueryData()}", null, null, "{args[1].tagVar}", ""}));
                setParamsAction.setType(ActionTypeEnum.control);
                setParamsAction.setDesc("设置扩展参数");
                setParamsAction.setTarget(reloadChild.getAlias());
                setParamsAction.setMethod("setQueryData");
                setParamsAction.setRedirection("other:callback:call");
                setParamsAction.set_return(true);

                this.getCurrComponent().addAction(setParamsAction);
                Action callAction = new CustomAPICallAction(reloadChild, TreeViewEventEnum.onGetContent);
                callAction.setArgs(Arrays.asList(new String[]{"{page." + reloadChild.getAlias() + ".invoke()}", "temp", null, "{args[2]}"}));
                callAction.setType(ActionTypeEnum.control);
                callAction.setTarget(reloadChild.getAlias());
                callAction.setDesc("子节点装载");
                callAction.setMethod(CustomAPIMethod.invoke.getType());
                callAction.setRedirection("other:callback:call");
                callAction.set_return(true);
                this.getCurrComponent().addAction(callAction);
                this.addChildren(reloadChild);
            }
        }
    }


    protected void fillCustomAction(CustomTreeViewBean view, Component currComponent) {
        Set<TreeEventBean> extAPIEvent = view.getExtAPIEvent();
        for (TreeEventBean eventEnum : extAPIEvent) {
            List<Action> actions = eventEnum.getActions();
            for (Action action : actions) {
                action.updateArgs("{page." + this.getAlias() + "}", 3);
                currComponent.addAction(action, true, eventEnum.getEventReturn());
            }
        }
    }


    protected void fillTreeAction(CustomTreeViewBean view, Component currComponent) {
        Set<CustomTreeEvent> customFormEvents = view.getEvent();
        for (CustomTreeEvent eventType : customFormEvents) {
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
        List<TreeMenu> customFormMenus = view.getCustomMenu();
        if (customFormMenus != null && customFormMenus.size() > 0) {
            this.getMenuBar(view.getMenuBar()).addMenu(customFormMenus.toArray(new TreeMenu[]{}));
        }


        List<TreeMenu> customBottombar = view.getBottombarMenu();
        if (customBottombar != null && customBottombar.size() > 0) {
            this.getBottomBar(view.getBottomBar()).addMenu(customBottombar.toArray(new TreeMenu[]{}));
        }


        Set<ChildTreeViewBean> childTreeViewBeans = view.getChildTreeViewBeans();
        for (ChildTreeViewBean childTreeViewBean : childTreeViewBeans) {
            this.fillChildCustomAction(childTreeViewBean, currComponent);
            Set<CustomTreeEvent> childFormEvents = childTreeViewBean.getEvent();
            for (CustomTreeEvent eventType : childFormEvents) {
                MethodConfig methodConfig = view.findMethodByEvent(eventType, childTreeViewBean.getBindClass());
                if (methodConfig != null && methodConfig.getMethod() != null) {
                    Class serviceClass = methodConfig.getMethod().getDeclaringClass();
                    for (CustomAction actionType : eventType.getActions(false)) {
                        Action action = new Action(actionType, eventType.getEventEnum());
                        Condition condition = new Condition("{args[1].serviceClass}", SymbolType.equal, serviceClass.getSimpleName());
                        action.addCondition(condition);
                        currComponent.addAction(action);
                    }
                }
            }
            Set<TreeMenu> childFormMenus = childTreeViewBean.getToolBarMenu();
            if (customFormMenus != null && customFormMenus.size() > 0) {
                for (TreeMenu customFormMenu : childFormMenus) {
                    this.getMenuBar(view.getMenuBar()).addMenu(customFormMenu);
                }
            }
        }
        super.fillAction(view);
    }


    void fillHelpBar(CustomTreeViewBean customAnnotation, Component currComponent) {
        if (customAnnotation.getHeplBar() != null && customAnnotation.getHeplBar()) {
            ComboInputComponent helpBar;
            helpBar = new ComboInputComponent();
            helpBar.setAlias(TreeListItem.ESDSearchPattern);
            ComboInputProperties comboInputProperties = helpBar.getProperties();
            comboInputProperties.setDock(Dock.top);
            comboInputProperties.setName(TreeListItem.ESDSearchPattern);
            comboInputProperties.setLabelCaption("查询:");
            comboInputProperties.setType(ComboInputType.helpinput);
            comboInputProperties.setLeft("3.5em");
            comboInputProperties.setTop("2em");
            comboInputProperties.setLabelSize("4em");
            comboInputProperties.setImageClass("fa-solid fa-code");
            helpBar.addAction(new Action(CustomFormAction.RELOAD, FieldEventEnum.onChange));
            currComponent.getParent().addChildren(helpBar);
        }

    }


    public String getSaveRowPath() {
        return saveRowPath;
    }

    public void setSaveRowPath(String saveRowPath) {
        this.saveRowPath = saveRowPath;
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

    public CustomCmdBar getCmdBar() {
        return cmdBar;
    }

    public void setCmdBar(CustomCmdBar cmdBar) {
        this.cmdBar = cmdBar;
    }

    public String getLoadChildUrl() {
        return loadChildUrl;
    }

    public void setLoadChildUrl(String loadChildUrl) {
        this.loadChildUrl = loadChildUrl;
    }


    public ResponsePathTypeEnum getItemType() {
        return itemType;
    }

    public void setItemType(ResponsePathTypeEnum itemType) {
        this.itemType = itemType;
    }

}
