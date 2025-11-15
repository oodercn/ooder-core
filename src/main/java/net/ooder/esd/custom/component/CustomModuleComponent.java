package net.ooder.esd.custom.component;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.esd.annotation.BottomBarMenu;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.CustomMenu;
import net.ooder.esd.annotation.event.*;
import net.ooder.esd.annotation.menu.CustomMenuType;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.*;
import net.ooder.esd.bean.bar.ContextMenuBar;
import net.ooder.esd.bean.bar.DynBar;
import net.ooder.esd.bean.bar.ToolsBar;
import net.ooder.esd.bean.view.CustomModuleBean;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.dsm.view.field.ESDFieldConfig;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.engine.enums.MenuBarBean;
import net.ooder.esd.manager.editor.PluginsFactory;
import net.ooder.esd.tool.component.*;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.DialogProperties;
import net.ooder.esd.tool.properties.item.UIItem;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;
import ognl.OgnlException;

import java.lang.reflect.Method;
import java.util.*;

public class CustomModuleComponent<M extends Component> extends ModuleComponent<M> {


    @JSONField(serialize = false)
    private ESDClass esdClass;
    @JSONField(serialize = false)
    protected ESDClass parentClass;

    @JSONField(serialize = false)
    protected List<ESDFieldConfig> fieldList;
    @JSONField(serialize = false)
    protected BlockComponent mainComponent;


    @JSONField(serialize = false)
    private CustomMenusBar menuBar;
    @JSONField(serialize = false)
    private CustomBottomBar bottomBar;

    @JSONField(serialize = false)
    protected String dataUrl;


    @JSONField(serialize = false)
    CustomToolsBar customToolsBar;

    @JSONField(serialize = false)
    CustomContextBar contextBar;

    @JSONField(serialize = false)
    public MethodConfig methodAPIBean;

    public CustomModuleComponent() {
        super();

    }

    public CustomModuleComponent(EUModule module, MethodConfig methodAPIBean, Map<String, Object> valueMap) {
        this.methodAPIBean = methodAPIBean;
        this.euModule = module;
        this.euModule.setComponent(this);
        this.valueMap = valueMap;
        this.dataUrl = methodAPIBean.getUrl();
        this.esdClass = methodAPIBean.getViewClass();
        this.parentClass = methodAPIBean.getSourceClass();
        this.mainComponent = this.getMainComponent();
        this.moduleBean = methodAPIBean.getModuleBean();
        if (moduleBean != null && moduleBean.getViewConfig() != null && moduleBean.getViewConfig().getViewStyles() != null) {
            ModuleStyleBean moduleStyleBean = moduleBean.getViewConfig().getViewStyles();
            if (moduleStyleBean.getBackgroundAttachment() != null) {
                this.mainComponent.getProperties().setPanelBgImgAttachment(moduleStyleBean.getBackgroundAttachment());
            }
            if (moduleStyleBean.getBackgroundColor() != null) {
                this.mainComponent.getProperties().setPanelBgClr(moduleStyleBean.getBackgroundColor());
            }

            if (moduleStyleBean.getBackgroundRepeat() != null) {
                this.mainComponent.getProperties().setPanelBgImgRepeat(moduleStyleBean.getBackgroundRepeat());
            }

            if (moduleStyleBean.getBackgroundPosition() != null) {
                this.mainComponent.getProperties().setPanelBgImgRepeat(moduleStyleBean.getBackgroundPosition());
            }
            if (moduleStyleBean.getBackgroundImage() != null) {
                this.mainComponent.getProperties().setPanelBgImg(moduleStyleBean.getBackgroundImage());
            }
        } else {
            this.mainComponent.getProperties().setPanelBgClr("transparent");
        }

        CustomViewBean<FieldFormConfig, UIItem, ? extends Component> viewBean = (CustomViewBean<FieldFormConfig, UIItem, ? extends Component>) methodAPIBean.getView();
        CustomData dataBean = methodAPIBean.getDataBean();

        String caption = methodAPIBean.getCaption();
        if (this.getProperties().getCaption() == null || this.getProperties().getCaption().equals("")) {
            getProperties().setCaption(caption);
        }
        this.desc = caption;
        this.moduleViewType = methodAPIBean.getModuleViewType();
        this.fieldList = new ArrayList<ESDFieldConfig>();


        List<ESDFieldConfig> hiddenFieldList = new ArrayList<ESDFieldConfig>();
        for (ESDFieldConfig field : viewBean.getAllFields()) {
            if (field.getSerialize() != null && field.getSerialize()) {
                if (field.getColHidden() != null && field.getColHidden()) {
                    hiddenFieldList.add(field);
                } else {
                    fieldList.add(field);
                }
                if ((field.getPid() != null && field.getPid()) || (field.getUid() != null && field.getUid())) {
                    hiddenFieldList.add(field);
                }
            }
        }


        for (ESDFieldConfig field : viewBean.getAllFields()) {
            if (field == null || (field.getSerialize() != null && field.getSerialize())) {
                if ((field.getUid() != null && field.getUid()) && field.getColHidden() != null && field.getColHidden() && hasField(fieldList, field.getFieldname())) {
                    hiddenFieldList.add(field);
                }
            }
        }


        this.getCtxBaseComponent().addParams(fillParams(methodAPIBean.getParamSet(), valueMap));
        if (hiddenFieldList != null && hiddenFieldList.size() > 0) {
            this.getCtxBaseComponent().addFields(hiddenFieldList, new HashMap());
        }

        this.initModuleEvent(moduleBean);
        if (methodAPIBean.getImageClass() != null && methodAPIBean.getImageClass().equals("")) {
            this.getProperties().setImageClass(methodAPIBean.getImageClass());
        }


        Method method = methodAPIBean.getMethod();
        if (method != null) {
            String serviceClass = method.getDeclaringClass().getName();
            Set<String> customServiceClass = dataBean.getCustomServiceClass();
            if (customServiceClass.isEmpty() || !customServiceClass.contains(serviceClass)) {
                customServiceClass.add(serviceClass);
            }
            ESDClass sourceClass = methodAPIBean.getSourceClass();
            if (!sourceClass.getSourceClass().getClassName().equals(sourceClass.getClassName())) {
                customServiceClass.remove(sourceClass.getSourceClass().getClassName());
            }
        }


    }


    private void initModuleEvent(CustomModuleBean moduleBean) {
        if (moduleBean != null) {
            this.addModuleAction(ModuleEventEnum.onReady, moduleBean.getOnReadyAction());
            this.addModuleAction(ModuleEventEnum.onDestroy, moduleBean.getOnDestroyAction());
            this.addModuleAction(ModuleEventEnum.onFragmentChanged, moduleBean.getOnFragmentChangedAction());
            this.addModuleAction(ModuleEventEnum.onMessage, moduleBean.getOnMessageAction());
            this.addModuleAction(ModuleEventEnum.beforeCreated, moduleBean.getBeforeCreatedAction());
            this.addModuleAction(ModuleEventEnum.afterShow, moduleBean.getAfterShowAction());
            this.addModuleAction(ModuleEventEnum.onLoadBaseClass, moduleBean.getOnLoadBaseClassAction());
            this.addModuleAction(ModuleEventEnum.onLoadRequiredClass, moduleBean.getOnLoadRequiredClassAction());
            this.addModuleAction(ModuleEventEnum.onIniResource, moduleBean.getOnIniResourceAction());
            this.addModuleAction(ModuleEventEnum.afterIniComponents, moduleBean.getAfterIniComponentsAction());
            this.addModuleAction(ModuleEventEnum.onModulePropChange, moduleBean.getOnModulePropChangeAction());
            this.addModuleAction(ModuleEventEnum.onRender, moduleBean.getOnRenderAction());


            this.addModuleEvent(moduleBean.getOnReady().toArray(new ModuleOnReadyEventEnum[]{}));
            this.addModuleEvent(moduleBean.getOnDestroy().toArray(new CustomOnDestroyEventEnum[]{}));
            this.addModuleEvent(moduleBean.getOnFragmentChanged().toArray(new CustomModuleEventEnum[]{}));
            this.addModuleEvent(moduleBean.getOnMessage().toArray(new ModuleOnMessageEventEnum[]{}));
            this.addModuleEvent(moduleBean.getBeforeCreated().toArray(new CustomModuleEventEnum[]{}));
            this.addModuleEvent(moduleBean.getAfterShow().toArray(new CustomModuleEventEnum[]{}));
            this.addModuleEvent(moduleBean.getOnLoadBaseClass().toArray(new CustomModuleEventEnum[]{}));
            this.addModuleEvent(moduleBean.getOnLoadRequiredClass().toArray(new CustomModuleEventEnum[]{}));
            this.addModuleEvent(moduleBean.getOnIniResource().toArray(new CustomModuleEventEnum[]{}));
            this.addModuleEvent(moduleBean.getAfterIniComponents().toArray(new CustomModuleEventEnum[]{}));
            this.addModuleEvent(moduleBean.getOnModulePropChange().toArray(new ModuleOnPropChangeEventEnum[]{}));
            this.addModuleEvent(moduleBean.getOnRender().toArray(new CustomModuleEventEnum[]{}));
        }


    }

    protected void fillContextAction(ContextMenuBar viewBean, Component currComponent) {
        if (viewBean != null) {
            RightContextMenuBean contextMenuBean = viewBean.getContextMenuBean();
            if (contextMenuBean != null && !contextMenuBean.getFristMenuClass().equals(Void.class)) {
                try {
                    Class contextMenu = contextMenuBean.getFristMenuClass();
                    contextBar = PluginsFactory.getInstance().initMenuClass(contextMenu, CustomContextBar.class);
                    if (contextBar != null) {
                        contextBar.bindModuleAction(currComponent, this, viewBean.getBindTypes(), null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void fillToolBar(ToolsBar view, Component currComponent) throws JDSException {
        ComponentType[] bindTypes = view.getBindTypes().toArray(new ComponentType[]{});
        ToolBarMenuBean toolBarBean = view.getToolBar();
        if (toolBarBean != null) {
            customToolsBar = new CustomToolsBar(toolBarBean);
            Class<DynBar>[] serviceObjs = toolBarBean.getMenuClasses();
            if (serviceObjs != null) {
                for (Class obj : serviceObjs) {
                    if (obj != null && !obj.equals(Void.class)) {
                        CustomToolsBar bar = PluginsFactory.getInstance().initMenuClass(obj, CustomToolsBar.class);
                        if (bar != null) {
                            this.customToolsBar = bar;
                            List<APICallerComponent> components = customToolsBar.getApis();
                            this.addApi(components);
                        } else {
                            this.addBindService(obj, customToolsBar, bindTypes);
                        }
                    }
                }

                PositionType positionType = toolBarBean.getPosition() == null ? PositionType.inner : toolBarBean.getPosition();
                switch (positionType) {
                    case inner:
                        TreeListItem group = customToolsBar.getProperties().getGroup();
                        if (group != null && group.getSub() != null && group.getSub().size() > 0) {
                            if (currComponent.getParent() != null) {
                                currComponent.getParent().addChildren(customToolsBar);
                            } else {
                                currComponent.addChildren(customToolsBar);
                            }
                        }
                        break;
                    case module:
                        mainComponent.addChildren(customToolsBar);
                        break;
                    case top:
                        if (this.getTopModule() != null) {
                            this.getTopModule().getComponent().getMainBoxComponent().addChildren(customToolsBar);
                        } else {
                            mainComponent.addChildren(customToolsBar);
                        }
                        break;
                    default:
                        currComponent.addChildren(customToolsBar);
                }
            }

        }
    }


    void addModuleEvent(ModuleEvent[] eventAnnotationEnums) {
        for (ModuleEvent apiEventAnnotationEnum : eventAnnotationEnums) {
            CustomAction[] actionTypes = apiEventAnnotationEnum.actions();
            List<Action> actions = new ArrayList<>();
            for (CustomAction actionType : actionTypes) {
                this.addAction(new Action(actionType, apiEventAnnotationEnum.event()));
            }
        }
    }

    void addModuleAction(ModuleEventEnum eventEnum, Set<Action> actions) {
        for (Action action : actions) {
            this.addAction(action);
        }
    }


    @JSONField(serialize = false)
    public MethodConfig getMethodAPIBean() {
        if (methodAPIBean == null) {
            methodAPIBean = super.getMethodAPIBean();
        }

        return methodAPIBean;
    }

    public void setMethodAPIBean(MethodConfig methodAPIBean) {
        this.methodAPIBean = methodAPIBean;
    }

    protected void addChildLayoutNav(Component component) {
        CustomViewBean<FieldFormConfig, UIItem, ? extends Component> viewBean = (CustomViewBean<FieldFormConfig, UIItem, ? extends Component>) methodAPIBean.getView();
        PanelType panelType = moduleBean.getPanelType();
        if (panelType != null) {
            switch (panelType) {
                case dialog:
                    if (component instanceof DialogComponent && component.getChildren() != null) {
                        for (Component childComponent : component.getChildren()) {
                            mainComponent.addChildren(childComponent);
                        }
                    } else {
                        mainComponent.addChildren(component);
                    }

                    DialogComponent<DialogProperties, DialogEventEnum> dialogComponent = this.getDialogComponent();
                    this.addChildren(dialogComponent);
                    dialogComponent.addChildren(mainComponent);
                    break;

                case panel:
                    PanelComponent modulePanelComponent = this.getModulePanelComponent();

                    if (component instanceof PanelComponent && component.getChildren() != null) {
                        for (Component childComponent : component.getChildren()) {
                            mainComponent.addChildren(childComponent);
                        }
                    } else {
                        mainComponent.addChildren(component);
                    }
                    this.addChildren(modulePanelComponent);
                    modulePanelComponent.addChildren(mainComponent);

                    break;
                case block:
                    BlockComponent blockPanelComponent = this.getBlockPanelComponent();
                    if (component instanceof BlockComponent && component.getChildren() != null) {
                        for (Component childComponent : component.getChildren()) {
                            mainComponent.addChildren(childComponent);
                        }
                    } else {
                        mainComponent.addChildren(component);
                    }
                    if (moduleBean != null && moduleBean.getBlockBean() != null && moduleBean.getBlockBean().getContainerBean() != null) {
                        mainComponent.getProperties().init(moduleBean.getBlockBean().getContainerBean());
                    }

                    mainComponent.setProperties(blockPanelComponent.getProperties());
                    this.addChildren(mainComponent);
                    break;
                case div:
                    DivComponent divComponent = this.getDivComponent();
                    if (component instanceof PanelComponent && component.getChildren() != null) {
                        for (Component childComponent : component.getChildren()) {
                            mainComponent.addChildren(childComponent);
                        }
                    } else {
                        mainComponent.addChildren(component);
                    }
                    this.addChildren(divComponent);
                    divComponent.addChildren(mainComponent);


            }
        }

    }


    public List<ESDFieldConfig> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<ESDFieldConfig> fieldList) {
        this.fieldList = fieldList;
    }

    boolean hasField(List<ESDFieldConfig> fieldList, String fieldName) {
        for (ESDFieldConfig field : fieldList) {
            if (field.getFieldname().equals(fieldName)) {
                return true;
            }
        }
        return false;
    }


    protected void fillAction(CustomViewBean view) {

        if (view != null) {
            try {
                ComponentType[] bindTypes = (ComponentType[]) view.getBindTypes().toArray(new ComponentType[]{});
                if (view.getBottomBar() != null) {
                    BottomBarMenuBean bottomBarMenuBean = view.getBottomBar();
                    bottomBarMenuBean.setSourceClassName(view.getSourceClassName());
                    bottomBarMenuBean.setDomainId(view.getDomainId());
                    bottomBarMenuBean.setMethodName(view.getMethodName());
                    CustomBottomBar bar = PluginsFactory.getInstance().getBottomBar(bottomBarMenuBean);
                    if (bar != null) {
                        this.bottomBar = bar;
                        List<APICallerComponent> callerComponents = bottomBar.getApis();
                        for (APICallerComponent component : callerComponents) {
                            this.addChildren(component);
                        }
                    } else {
                        CustomBottomBar statusButtons = this.getBottomBar(view.getBottomBar());
                        Class[] menuClass = view.getBottomBar().getMenuClasses();
                        if (menuClass != null) {
                            for (Class obj : menuClass) {
                                if (!obj.equals(Void.class)) {
                                    this.addBindService(obj, statusButtons, bindTypes);
                                }
                            }

                        }
                    }

                    Set<CustomMenu> bottomMenus = new HashSet<>();
                    try {
                        List menus = (List) view.getClass().getMethod("getBottombarMenu", null).invoke(view, null);
                        bottomMenus.addAll(menus);
                    } catch (Exception e) {

                    }

                    for (CustomMenu menu : bottomMenus) {
                        bottomBar.addMenu(menu);
                    }
                }


                if (view.getMenuBar() != null) {
                    MenuBarBean menuBarBean = view.getMenuBar();
                    CustomMenuType menuType = menuBarBean.getMenuType();
                    if (menuType == null) {
                        menuType = CustomMenuType.MENUBAR;
                        menuBarBean.setMenuType(menuType);
                    }

                    if (!menuType.equals(CustomMenuType.SUB)) {
                        menuBarBean.setSourceClassName(view.getSourceClassName());
                        menuBarBean.setDomainId(view.getDomainId());
                        menuBarBean.setMethodName(view.getMethodName());
                        DynBar bar = PluginsFactory.getInstance().getMenu(menuBarBean);
                        if (bar != null && bar instanceof CustomMenusBar) {
                            this.menuBar = (CustomMenusBar) bar;
                            List<APICallerComponent> callerComponents = menuBar.getApis();
                            for (APICallerComponent component : callerComponents) {
                                this.addChildren(component);
                            }
                        } else {
                            Class[] menuClass = view.getMenuBar().getMenuClasses();
                            if (menuClass != null) {
                                for (Class obj : menuClass) {
                                    if (!obj.equals(Void.class)) {
                                        this.addBindService(obj, this.getMenuBar(view.getMenuBar()), bindTypes);
                                    }
                                }
                            }
                        }

                        Set<CustomMenu> customMenus = new HashSet<>();
                        try {
                            List menus = (List) view.getClass().getMethod("getCustomMenu", null).invoke(view, null);
                            customMenus.addAll(menus);
                        } catch (Exception e) {

                        }

                        CustomMenu[] menus = view.getMenuBar().getMenus();
                        if (menus != null && menus.length > 0) {
                            for (CustomMenu menu : menus) {
                                customMenus.add(menu);
                            }
                        }

                        for (CustomMenu menu : customMenus) {
                            this.getMenuBar(view.getMenuBar()).addMenu(menu);
                        }
                    }
                }
                initBarLayout(view);
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }


    }

    private void initBarLayout(CustomViewBean viewBean) {
        //CustomViewBean<FieldFormConfig, UIItem, ? extends Component> viewBean = (CustomViewBean<FieldFormConfig, UIItem, ? extends Component>) methodAPIBean.getView();
        BottomBarMenuBean barMenuBean = viewBean.getBottomBar();
        if (barMenuBean != null) {
            bottomBar = this.getBottomBar(barMenuBean);
        } else {
            barMenuBean = AnnotationUtil.fillDefaultValue(BottomBarMenu.class, new BottomBarMenuBean());
        }
        BlockComponent blockComponent = new BlockComponent(Dock.center, barMenuBean.getAlias() + "Block");
        blockComponent.getProperties().setComboType(ComponentType.STATUSBUTTONS);
        blockComponent.getProperties().setBorderType(BorderType.none);
        blockComponent.getProperties().setOverflow(OverflowType.hidden);
        blockComponent.getProperties().setHeight((barMenuBean.getHeight() == null || barMenuBean.getHeight().equals("")) ? "3em" : barMenuBean.getHeight());
        blockComponent.getProperties().setDock((barMenuBean.getBarDock() == null || barMenuBean.getBarDock().equals(Dock.fill)) ? Dock.bottom : barMenuBean.getBarDock());
        blockComponent.getProperties().setBorderType(barMenuBean.getBarBorderType());

        if (bottomBar != null && bottomBar.getProperties().getItems() != null && bottomBar.getProperties().getItems().size() > 0) {
            blockComponent.addChildren(bottomBar);
            PositionType positionType = barMenuBean.getPosition() == null ? PositionType.inner : barMenuBean.getPosition();

            switch (positionType) {
                case inner:
                    this.getCurrComponent().addChildren(blockComponent);
                    break;
                case module:
                    mainComponent.addChildren(blockComponent);
                    break;
                case top:
                    if (this.getTopModule() != null) {
                        this.getTopModule().getComponent().getMainBoxComponent().addChildren(blockComponent);
                    } else {
                        mainComponent.addChildren(blockComponent);
                    }
                    break;
                default:
                    mainComponent.addChildren(blockComponent);
            }


        }


        MenuBarBean menuBean = viewBean.getMenuBar();
        if (menuBean != null) {
            menuBar = this.getMenuBar(menuBean);
        }

        if (menuBar != null && menuBar.getProperties().getItems() != null && menuBar.getProperties().getItems().size() > 0) {
            if (barMenuBean != null && barMenuBean.getBarDock() != null && barMenuBean.getBarDock().equals(Dock.top)) {
                BlockComponent menuBarComponent = new BlockComponent(Dock.right, barMenuBean.getAlias() + "Bar");
                menuBarComponent.getProperties().setBorderType(BorderType.none);
                net.ooder.esd.tool.properties.CS bcs = menuBar.getCS();
                if (bcs == null) {
                    bcs = new net.ooder.esd.tool.properties.CS();
                }
                Map<String, Object> bcMap = bcs.getITEMS();
                if (bcMap == null) {
                    bcMap = new HashMap<>();
                }
                bcMap.put("margin", "10px 0px 0px 0px");
                bcs.setITEMS(bcMap);
                menuBar.setCS(bcs);
                net.ooder.esd.tool.properties.CS cs = menuBarComponent.getCS();
                if (cs == null) {
                    cs = new net.ooder.esd.tool.properties.CS();
                }
                Map<String, Object> itemcMap = cs.getPANEL();
                if (itemcMap == null) {
                    itemcMap = new HashMap<>();
                }
                itemcMap.put("margin", "10px 0px 0px 0px");
                cs.setPANEL(itemcMap);
                menuBarComponent.setCS(cs);
                menuBarComponent.addChildren(menuBar);
                blockComponent.addChildren(menuBarComponent);
            } else {
                mainComponent.addChildren(menuBar);
            }
        }
    }

    @JSONField(serialize = false)
    public BlockComponent getMainComponent() {
        if (mainComponent == null) {
            mainComponent = (BlockComponent) this.getMainBoxComponent();
        }
        return mainComponent;
    }

    public void setMainComponent(BlockComponent mainComponent) {
        this.mainComponent = mainComponent;
    }

    @JSONField(serialize = false)
    public CustomMenusBar getMenuBar(MenuBarBean menuBarBean) {
        if (menuBar == null) {
            List<Component> componentList = this.findComponents(ComponentType.MENUBAR, null);
            if (componentList != null && componentList.size() > 0 && (componentList.get(0) instanceof CustomMenusBar)) {
                menuBar = (CustomMenusBar) componentList.get(0);
            } else {
                menuBar = new CustomMenusBar(menuBarBean);
            }
        }
        return menuBar;
    }

    @Override
    @JSONField(serialize = false)
    public Component getTopComponentBox() {
        return this.getMainComponent();
    }


    public CustomToolsBar getCustomToolsBar() {
        return customToolsBar;
    }

    public void setCustomToolsBar(CustomToolsBar customToolsBar) {
        this.customToolsBar = customToolsBar;
    }

    public CustomContextBar getContextBar() {
        return contextBar;
    }

    public void setContextBar(CustomContextBar contextBar) {
        this.contextBar = contextBar;
    }

    @JSONField(serialize = false)
    public CustomBottomBar getBottomBar(BottomBarMenuBean barMenuBean) {
        if (bottomBar == null) {
            List<Component> componentList = this.findComponents(ComponentType.STATUSBUTTONS, null);
            if (componentList != null && componentList.size() > 0 && (componentList.get(0) instanceof CustomBottomBar)) {
                bottomBar = (CustomBottomBar) componentList.get(0);
            } else {
                if (barMenuBean == null) {
                    barMenuBean = AnnotationUtil.fillDefaultValue(BottomBarMenu.class, new BottomBarMenuBean());
                }
                this.bottomBar = new CustomBottomBar(mainComponent.getAlias() + ComponentType.BUTTON.name(), barMenuBean);
            }
        }
        return bottomBar;
    }


    public ESDClass getEsdClass() {
        return esdClass;
    }

    public void setEsdClass(ESDClass esdClass) {
        this.esdClass = esdClass;
    }

    public ESDClass getParentClass() {
        return parentClass;
    }

    public void setParentClass(ESDClass parentClass) {
        this.parentClass = parentClass;
    }


    public String getDataUrl() {
        return dataUrl;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }

    public MethodConfig getMethodBeanByItem(CustomMenuItem menuItem) throws JDSException {
        MethodConfig methodBean = null;
        try {
            if (methodAPIBean != null) {
                methodBean = methodAPIBean.getDataBean().getMethodByItem(menuItem);
            }

            String url = (String) OgnlUtil.getValue(menuItem.getMethodName(), new HashMap(), this);
            if (methodBean == null) {
                methodBean = ESDFacrory.getAdminESDClient().getMethodAPIBean(url, projectName);
            }
            if (methodBean == null) {
                methodBean = ESDFacrory.getAdminESDClient().getMethodAPIBean(methodAPIBean.getSourceClassConfig().getUrl() + url, projectName);
            }

            if (methodBean == null && url != null) {
                methodBean = methodAPIBean.getDataBean().getMethodByName(url);
            }
            if (methodBean == null) {
                String apiUrl = methodAPIBean.getUrl().substring(0, methodAPIBean.getUrl().lastIndexOf("/") + 1);
                methodBean = ESDFacrory.getAdminESDClient().getMethodAPIBean(apiUrl + url, projectName);
            }


        } catch (OgnlException e) {
            methodBean = methodAPIBean.getDataBean().getMethodByItem(menuItem);
            //e.printStackTrace();
        }


        return methodBean;

    }


}
