package net.ooder.esd.custom.component.nav;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.action.CustomAPIMethod;
import net.ooder.esd.annotation.action.CustomFormAction;
import net.ooder.esd.annotation.action.CustomLoadClassAction;
import net.ooder.esd.annotation.action.CustomTreeAction;
import net.ooder.esd.annotation.event.*;
import net.ooder.esd.annotation.menu.TreeMenu;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.*;
import net.ooder.esd.bean.data.NavTreeDataBean;
import net.ooder.esd.bean.data.TreeDataBaseBean;
import net.ooder.esd.bean.view.*;
import net.ooder.esd.custom.action.CustomAPICallAction;
import net.ooder.esd.custom.action.ShowPageAction;
import net.ooder.esd.custom.component.CustomContextBar;
import net.ooder.esd.custom.component.CustomModuleComponent;
import net.ooder.esd.custom.component.CustomTabsComponent;
import net.ooder.esd.custom.properties.CustomCmdBar;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.manager.editor.PluginsFactory;
import net.ooder.esd.tool.component.*;
import net.ooder.esd.tool.properties.*;
import net.ooder.esd.tool.properties.form.ComboInputProperties;
import net.ooder.esd.tool.properties.item.LayoutListItem;

import java.util.*;

public class CustomNavTreeComponent<M extends LayoutComponent> extends CustomModuleComponent<M> {

    public String saveUrl;
    public String editorPath;
    public String saveRowPath;
    public ResponsePathTypeEnum itemType;
    @JSONField(serialize = false)
    public String loadChildUrl;
    @JSONField(serialize = false)
    CustomCmdBar cmdBar;
    @JSONField(serialize = false)
    public TreeViewComponent treeComponent;
    @JSONField(serialize = false)
    public CustomTabsComponent tabsComponent;
    @JSONField(serialize = false)
    public LayoutComponent layoutComponent;

    @JSONField(serialize = false)
    public BlockComponent treeBlockComponent;


    public CustomNavTreeComponent() {
        super();
    }


    public CustomNavTreeComponent(EUModule module, MethodConfig methodConfig, Map<String, Object> valueMap) {
        super(module, methodConfig, valueMap);
        NavTreeComboViewBean navTreeViewBean = (NavTreeComboViewBean) methodConfig.getView();
        this.layoutComponent = getLayoutComponent(navTreeViewBean.getLayoutViewBean());
        this.tabsComponent = createTabsComponent(navTreeViewBean.getTabsViewBean(), valueMap);
        treeBlockComponent = createNavComponent(navTreeViewBean.getTreeViewBean(), valueMap);
        treeBlockComponent.setTarget(PosType.before.name());
        layoutComponent.addChildren(treeBlockComponent);
        tabsComponent.setTarget(PosType.main.name());
        layoutComponent.addChildren(tabsComponent);
    }

    private CustomTabsComponent createTabsComponent(TabsViewBean tabsViewBean, Map valueMap) {
        CustomTabsComponent tabComponent = new CustomTabsComponent(tabsViewBean, valueMap);
        tabComponent.getProperties().getItems().clear();
        Action showAction = new Action(CustomLoadClassAction.tabShow, TabsEventEnum.onIniPanelView);
        showAction.updateArgs(tabComponent.getAlias(), 4);
        tabComponent.addAction(showAction);
        return tabComponent;
    }

    private BlockComponent createNavComponent(CustomTreeViewBean treeViewBean, Map valueMap) {
        this.treeComponent = new TreeViewComponent(treeViewBean);
        BlockComponent treeBlockComponent = new BlockComponent(Dock.fill, treeComponent.getAlias() + "Panel");
        treeBlockComponent.addChildren(treeComponent);
        return treeBlockComponent;
    }

    public void addChildNav(NavTreeComboViewBean navTreeViewBean) {
        super.addChildLayoutNav(layoutComponent);
        Condition condition = new Condition("{args[1].euClassName}", SymbolType.nonempty, "");
        if (navTreeViewBean.getTabsViewBean().getAutoSave() != null && navTreeViewBean.getTabsViewBean().getAutoSave()) {
            Action saveAction = new Action(TreeViewEventEnum.onItemSelected);
            saveAction.setDesc("保存表单");
            saveAction.setMethod("autoSave");
            saveAction.setType(ActionTypeEnum.control);
            saveAction.addCondition(condition);
            saveAction.setTarget(tabsComponent.getAlias());
            treeComponent.addAction(saveAction);
        }
        if (!navTreeViewBean.getTabsViewBean().getSingleOpen()) {
            Action removeAction = new Action(TreeViewEventEnum.onItemSelected);
            removeAction.setDesc("删除存在页");
            removeAction.setMethod("removeItems");
            removeAction.setType(ActionTypeEnum.control);
            removeAction.addCondition(condition);
            removeAction.setTarget(tabsComponent.getAlias());
            removeAction.setArgs(Arrays.asList(new String[]{"{args[1].id}"}));
            treeComponent.addAction(removeAction);
        } else {
            Action clearAction = new Action(TreeViewEventEnum.onItemSelected);
            clearAction.setDesc("清空");
            clearAction.setMethod("clearItems");
            clearAction.setType(ActionTypeEnum.control);
            clearAction.addCondition(condition);
            clearAction.setTarget(tabsComponent.getAlias());
            treeComponent.addAction(clearAction);
        }


        Action action = new Action(TreeViewEventEnum.onItemSelected);
        action.setDesc("添加TAB页");
        action.setMethod("insertItems2");
        action.setType(ActionTypeEnum.control);
        action.addCondition(condition);
        action.setTarget(tabsComponent.getAlias());
        action.setArgs(Arrays.asList(new String[]{"{args[1]}"}));
        treeComponent.addAction(action);
        Action tabclickItemAction = new Action(TreeViewEventEnum.onItemSelected);
        tabclickItemAction.setDesc("添加点击事件");
        tabclickItemAction.addCondition(condition);
        tabclickItemAction.setType(ActionTypeEnum.control);
        tabclickItemAction.setTarget(tabsComponent.getAlias());
        tabclickItemAction.setMethod("fireItemClickEvent");
        tabclickItemAction.setArgs(Arrays.asList(new String[]{"{args[1].id}"}));

        treeComponent.addAction(tabclickItemAction);

        TreeViewProperties properties = treeComponent.getProperties();
        if (properties.getItems() != null && properties.getItems().size() > 0) {
            Action clickItemAction = new Action(TreeViewEventEnum.onRender);
            clickItemAction.setType(ActionTypeEnum.control);
            clickItemAction.setTarget(treeComponent.getAlias());
            clickItemAction.setDesc("初始化");
            clickItemAction.setMethod("fireItemClickEvent");
            clickItemAction.setArgs(Arrays.asList(new String[]{properties.getFristId()}));
            treeComponent.addAction(clickItemAction);
        }


        try {
            fillViewAction(methodAPIBean);
            this.addChildren(genAPIComponent(navTreeViewBean.getTreeViewBean()).toArray(new APICallerComponent[]{}));
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }


    //数据对象
    @JSONField(serialize = false)
    public List<APICallerComponent> genAPIComponent(CustomTreeViewBean customTreeViewBean) throws JDSException {
        List<APICallerComponent> apiCallerComponents = new ArrayList<>();
        APICallerProperties apiCallerProperties = customTreeViewBean.getMethodConfig().getApi().getApiCallerProperties();
        if ((customTreeViewBean.getMethodConfig().getModuleBean().getDynLoad() != null && customTreeViewBean.getMethodConfig().getModuleBean().getDynLoad()) || customTreeViewBean.getLazyLoad() || (apiCallerProperties != null && apiCallerProperties.getAutoRun() != null && apiCallerProperties.getAutoRun())) {
            MethodConfig methodBean = null;
            if (dataUrl != null && !dataUrl.equals("")) {
                methodBean = ESDFacrory.getAdminESDClient().getMethodAPIBean(dataUrl, projectName);
            }
            if (methodBean == null) {
                methodBean = getMethodBeanByItem(CustomMenuItem.RELOAD);
            }
            if (methodBean == null) {
                methodBean = customTreeViewBean.getMethodConfig();
            }
            APICallerComponent reloadAPI = new APICallerComponent(methodBean);
            reloadAPI.setAlias(CustomTreeAction.RELOAD.getTarget());
            APICallerProperties reloadProperties = reloadAPI.getProperties();
            UrlPathData requestCtxData = new UrlPathData(this.getCtxBaseComponent().getAlias(), RequestPathTypeEnum.FORM, "");
            reloadProperties.addRequestData(requestCtxData);
            UrlPathData requestFormData = new UrlPathData(treeBlockComponent.getAlias(), RequestPathTypeEnum.FORM, "");
            reloadProperties.addRequestData(requestFormData);
            UrlPathData formData = new UrlPathData(this.getTreeComponent().getAlias(), itemType, "data");
            reloadProperties.addResponseData(formData);
            UrlPathData formCtxData = new UrlPathData(this.getCtxBaseComponent().getAlias(), ResponsePathTypeEnum.FORM, "data");
            reloadProperties.addResponseData(formCtxData);
            //reloadProperties.setAutoRun(true);
            apiCallerComponents.add(reloadAPI);
        }

        //装载保存事件
        MethodConfig methodBean = getMethodBeanByItem(CustomMenuItem.TREESAVE);
        if (methodBean != null) {
            APICallerComponent saveAPI = new APICallerComponent(methodBean);
            saveAPI.getProperties().setIsAllform(true);
            saveAPI.setAlias(CustomFormAction.SAVE.getTarget());
            APICallerProperties saveProperties = saveAPI.getProperties();
            saveProperties.setIsAllform(true);
            UrlPathData currPath = new UrlPathData(this.getTreeComponent().getAlias(), RequestPathTypeEnum.FORM, "");
            saveProperties.addRequestData(currPath);
            UrlPathData savepathData = new UrlPathData(this.getTreeComponent().getAlias(), RequestPathTypeEnum.FORM, "");
            saveProperties.addRequestData(savepathData);
            UrlPathData ctxData = new UrlPathData(this.getCtxBaseComponent().getAlias(), RequestPathTypeEnum.FORM, "");
            saveProperties.addRequestData(ctxData);

            TreeDataBaseBean dataBean = (TreeDataBaseBean) customTreeViewBean.getMethodConfig().getDataBean();
            if (dataBean.getAutoSave() != null && dataBean.getAutoSave()) {
                CustomAPICallAction customAPICallAction = new CustomAPICallAction(saveAPI, ModuleEventEnum.onDestroy);
                this.addAction(customAPICallAction);
            }

            apiCallerComponents.add(saveAPI);
        }


        return apiCallerComponents;
    }


    protected void fillViewAction(MethodConfig methodConfig) {
        NavTreeDataBean dataBean = (NavTreeDataBean) methodConfig.getDataBean();

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
            if (dataBean.getLoadChildUrl() != null && !dataBean.getLoadChildUrl().equals("")) {
                this.loadChildUrl = dataBean.getLoadChildUrl();
            }

            if (dataUrl == null || dataUrl.equals("")) {
                MethodConfig methodAPIBean = methodConfig.getDataBean().getMethodEvent(CustomTreeEvent.TREERELOAD);
                if (methodAPIBean != null) {
                    dataUrl = methodAPIBean.getUrl();
                }

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

            if (loadChildUrl == null || loadChildUrl.equals("")) {
                MethodConfig methodAPIBean = methodConfig.getDataBean().getMethodEvent(CustomTreeEvent.RELOADCHILD);
                if (methodAPIBean != null) {
                    loadChildUrl = methodAPIBean.getUrl();
                }
            }

            if (editorPath == null || editorPath.equals("")) {
                MethodConfig methodAPIBean = methodConfig.getDataBean().getMethodEvent(CustomTreeEvent.TREENODEEDITOR);
                if (methodAPIBean != null) {
                    editorPath = methodAPIBean.getUrl();
                }
            }

            if (dataUrl == null || dataUrl.equals("")) {
                dataUrl = methodConfig.getUrl();
            }

        }


    }


    public LayoutComponent getLayoutComponent(CustomLayoutViewBean layoutViewBean) {
        LayoutComponent layoutComponent = new LayoutComponent(Dock.fill, layoutViewBean.getName() + ComponentType.LAYOUT.name());
        LayoutProperties layoutProperties = layoutComponent.getProperties();
        LayoutListItem beforItem = new LayoutListItem(PosType.before);
        LayoutListItem mainItem = new LayoutListItem(PosType.main);

        List<CustomLayoutItemBean> itemBeanList = layoutViewBean.getLayoutItems();
        for (CustomLayoutItemBean layoutItemBean : itemBeanList) {
            if (layoutItemBean.getPos().equals(PosType.before)) {
                beforItem = new LayoutListItem(layoutItemBean);
            } else if (layoutItemBean.getPos().equals(PosType.main)) {
                mainItem = new LayoutListItem(layoutItemBean);
            }
        }


        if (layoutViewBean != null) {
            layoutProperties = new LayoutProperties(layoutViewBean);
            layoutProperties.getItems().clear();
            layoutProperties.addItem(beforItem);
            layoutProperties.addItem(mainItem);
        } else {
            layoutProperties.setBorderType(BorderType.none);
            layoutProperties.setType(LayoutType.horizontal);
            layoutProperties.addItem(beforItem);
            layoutProperties.addItem(mainItem);
        }
        layoutComponent.setProperties(layoutProperties);
        return layoutComponent;

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

            }
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }

    void fillDynLoad(ChildTreeViewBean childTreeViewBean, CustomTreeViewBean viewBean) {
        if (childTreeViewBean.getLazyLoad() != null && childTreeViewBean.getLazyLoad()) {
            MethodConfig childMethod = viewBean.findMethodByEvent(CustomTreeEvent.RELOADCHILD, childTreeViewBean.getBindClass());
            if (childMethod != null) {
                this.addAPIConfig(childMethod, childTreeViewBean.getGroupName());
            }
        }
    }

    void fillLazyLoadAction(CustomTreeViewBean customTreeViewBean) throws JDSException {
        Set<ChildTreeViewBean> childTreeViewBeans = customTreeViewBean.getChildTreeViewBeans();
        for (ChildTreeViewBean childTreeViewBean : childTreeViewBeans) {
            fillDynLoad(childTreeViewBean, customTreeViewBean);
        }
        for (CustomTreeViewBean childTree : customTreeViewBean.getAllChild()) {
            if (childTree.getLazyLoad() != null && childTree.getLazyLoad()) {
                MethodConfig childMethod = customTreeViewBean.findMethodByEvent(CustomTreeEvent.RELOADCHILD, customTreeViewBean.getBindService());
                if (childMethod != null) {
                    this.addAPIConfig(childMethod, childMethod.getEUClassName());
                }
            }
        }


        if (customTreeViewBean.getLazyLoad() != null && customTreeViewBean.getLazyLoad()) {
            if (!childTreeViewBeans.isEmpty()) {
                MethodConfig childMethod = customTreeViewBean.findMethodByEvent(CustomTreeEvent.RELOADCHILD, customTreeViewBean.getBindService());
                if (childMethod != null) {
                    this.addAPIConfig(childMethod, customTreeViewBean.getGroupName());
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
                    methodBean = customTreeViewBean.getMethodConfig();
                }

                APICallerComponent reloadChild = new APICallerComponent(methodBean);
                reloadChild.setAlias(CustomTreeAction.RELOADCHILD.getTarget());


                APICallerProperties reloadProperties = reloadChild.getProperties();
                UrlPathData requestCtxData = new UrlPathData(this.getCtxBaseComponent().getAlias(), RequestPathTypeEnum.FORM, "");
                UrlPathData requestFormData = new UrlPathData(this.getTreeComponent().getAlias(), RequestPathTypeEnum.FORM, "");
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

                this.getTreeComponent().addAction(setParamsAction);
                Action callAction = new CustomAPICallAction(reloadChild, TreeViewEventEnum.onGetContent);
                callAction.setArgs(Arrays.asList(new String[]{"{page." + reloadChild.getAlias() + ".invoke()}", "temp", null, "{args[2]}"}));
                callAction.setType(ActionTypeEnum.control);
                callAction.setTarget(reloadChild.getAlias());
                callAction.setDesc("子节点装载");
                callAction.setMethod(CustomAPIMethod.invoke.getType());
                callAction.setRedirection("other:callback:call");
                callAction.set_return(true);
                this.getTreeComponent().addAction(callAction);
                this.addChildren(reloadChild);
            }
        }
    }


    void addAPIConfig(MethodConfig childMethod, String groupName) {
        Condition condition = new Condition("{args[1].groupName}", SymbolType.equal, groupName);
        APICallerComponent reloadChild = new APICallerComponent(childMethod);
        // String alias = StringUtility.replace(childMethod.getUrl(), "/", "_");
        reloadChild.setAlias(groupName);
        APICallerProperties reloadProperties = reloadChild.getProperties();
        UrlPathData requestCtxData = new UrlPathData(this.getCtxBaseComponent().getAlias(), RequestPathTypeEnum.FORM, "");
        UrlPathData requestFormData = new UrlPathData(this.getTreeComponent().getAlias(), RequestPathTypeEnum.FORM, "");
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
        this.getTreeComponent().addAction(setParamsAction);

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
        this.getTreeComponent().addAction(callAction);
        this.addChildren(reloadChild);


    }

    void fillContextAction(CustomTreeViewBean viewBean, Component currComponent) {
        Set<ChildTreeViewBean> childTreeViewBeans = viewBean.getChildTreeViewBeans();
        if (!childTreeViewBeans.isEmpty()) {
            for (ChildTreeViewBean childTreeViewBean : childTreeViewBeans) {
                boolean showOpt = viewBean.getOptBtn() != null && !viewBean.getOptBtn().equals("");
                fillTreeContextAction(childTreeViewBean, currComponent, showOpt);
            }
        }
        super.fillContextAction(viewBean, currComponent);
    }


    protected void fillTreeContextAction(ChildTreeViewBean childTreeViewBean, Component currComponent, boolean showOpt) {
        if (childTreeViewBean != null) {
            RightContextMenuBean contextMenuBean = childTreeViewBean.getContextMenuBean();
            if (contextMenuBean != null) {
                if (!contextMenuBean.getFristMenuClass().equals(Void.class)) {
                    try {
                        Class contextMenu = contextMenuBean.getFristMenuClass();
                        CustomContextBar contextBar = PluginsFactory.getInstance().initMenuClass(contextMenu, CustomContextBar.class);
                        if (contextBar != null) {
                            Condition condition = new Condition("{args[3].groupName}", SymbolType.equal, childTreeViewBean.getGroupName());
                            contextBar.bindModuleAction(currComponent, this, childTreeViewBean.getBindTypes(), Arrays.asList(condition));
                            if (showOpt) {
                                condition = new Condition("{args[1].groupName}", SymbolType.equal, childTreeViewBean.getGroupName());
                                contextBar.bindOptAction(currComponent, this, childTreeViewBean.getBindTypes(), Arrays.asList(condition));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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

    protected void fillTreeAction(CustomTreeViewBean customTreeViewBean, Component currComponent) {

        Set<CustomTreeEvent> customFormEvents = customTreeViewBean.getEvent();
        for (CustomTreeEvent eventType : customFormEvents) {
            for (CustomAction actionType : eventType.getActions(false)) {
                Action action = new Action(actionType, eventType.getEventEnum());
                MethodConfig methodConfig = customTreeViewBean.getMethodConfig().getDataBean().getMethodEvent(eventType);
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
        List<TreeMenu> customFormMenus = customTreeViewBean.getCustomMenu();
        if (customFormMenus != null && customFormMenus.size() > 0) {
            this.getMenuBar(customTreeViewBean.getMenuBar()).addMenu(customFormMenus.toArray(new TreeMenu[]{}));
        }


        List<TreeMenu> customBottombar = customTreeViewBean.getBottombarMenu();
        if (customBottombar != null && customBottombar.size() > 0) {
            this.getBottomBar(customTreeViewBean.getBottomBar()).addMenu(customBottombar.toArray(new TreeMenu[]{}));
        }


        Set<ChildTreeViewBean> childTreeViewBeans = customTreeViewBean.getChildTreeViewBeans();
        for (ChildTreeViewBean childTreeViewBean : childTreeViewBeans) {
            Set<CustomTreeEvent> childFormEvents = childTreeViewBean.getEvent();
            for (CustomTreeEvent eventType : childFormEvents) {
                MethodConfig methodConfig = customTreeViewBean.findMethodByEvent(CustomTreeEvent.RELOADCHILD, childTreeViewBean.getBindClass());
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
                    this.getMenuBar(customTreeViewBean.getMenuBar()).addMenu(customFormMenu);
                }
            }
        }
        super.fillAction(customTreeViewBean);

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

    public String getSaveRowPath() {
        return saveRowPath;
    }

    public void setSaveRowPath(String saveRowPath) {
        this.saveRowPath = saveRowPath;
    }

    public ResponsePathTypeEnum getItemType() {
        return itemType;
    }

    public void setItemType(ResponsePathTypeEnum itemType) {
        this.itemType = itemType;
    }

    public String getLoadChildUrl() {
        return loadChildUrl;
    }

    public void setLoadChildUrl(String loadChildUrl) {
        this.loadChildUrl = loadChildUrl;
    }

    public CustomCmdBar getCmdBar() {
        return cmdBar;
    }

    public void setCmdBar(CustomCmdBar cmdBar) {
        this.cmdBar = cmdBar;
    }

    public TreeViewComponent getTreeComponent() {
        return treeComponent;
    }

    public void setTreeComponent(TreeViewComponent treeComponent) {
        this.treeComponent = treeComponent;
    }

    public CustomTabsComponent getTabsComponent() {
        return tabsComponent;
    }

    public void setTabsComponent(CustomTabsComponent tabsComponent) {
        this.tabsComponent = tabsComponent;
    }

    public LayoutComponent getLayoutComponent() {
        return layoutComponent;
    }

    public void setLayoutComponent(LayoutComponent layoutComponent) {
        this.layoutComponent = layoutComponent;
    }
}
