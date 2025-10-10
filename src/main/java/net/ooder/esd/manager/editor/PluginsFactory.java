package net.ooder.esd.manager.editor;

import net.ooder.common.JDSConstants;
import net.ooder.common.JDSException;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.esb.config.manager.EsbBeanFactory;
import net.ooder.esb.util.EsbFactory;
import net.ooder.esd.annotation.BottomBarMenu;
import net.ooder.esd.annotation.MenuBarMenu;
import net.ooder.esd.annotation.event.CustomFieldEvent;
import net.ooder.esd.annotation.field.ToolBarMenu;
import net.ooder.esd.annotation.menu.CustomMenuType;
import net.ooder.esd.annotation.ui.CustomImageType;
import net.ooder.esd.annotation.ui.ResponsePathTypeEnum;
import net.ooder.esd.bean.BottomBarMenuBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.ToolBarMenuBean;
import net.ooder.esd.bean.bar.DynBar;
import net.ooder.esd.bean.bar.MenuDynBar;
import net.ooder.esd.bean.bar.PopDynBar;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.custom.CustomMethodInfo;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.custom.component.CustomBottomBar;
import net.ooder.esd.custom.component.CustomToolsBar;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.MySpace;
import net.ooder.esd.engine.enums.MenuBarBean;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.properties.APICallerProperties;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.UrlPathData;
import net.ooder.web.APIConfig;
import net.ooder.web.APIConfigFactory;
import net.ooder.web.RequestMethodBean;
import net.ooder.web.util.AnnotationUtil;
import net.ooder.web.util.JSONGenUtil;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PluginsFactory {

    private static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, PluginsFactory.class);

    public static final String THREAD_LOCK = "Thread Lock";

    static Map<String, PluginsFactory> managerMap = new HashMap<String, PluginsFactory>();

    private List<MenuBarBean> topMenus = new ArrayList<MenuBarBean>();

    private List<ToolBarMenuBean> toolBarMenus = new ArrayList<ToolBarMenuBean>();

    private Map<String, DynBar> viewBarMap = new HashMap();

    private Map<String, MenuBarBean> menuBarBeanMap = new HashMap();

    MySpace space;

    public static PluginsFactory getInstance() throws JDSException {
        MySpace space = ESDFacrory.getAdminESDClient().getSpace();
        return getInstance(space);
    }


    public static PluginsFactory getInstance(MySpace space) {
        String path = space.getPath();
        PluginsFactory manager = managerMap.get(path);
        if (manager == null) {
            synchronized (THREAD_LOCK) {
                if (manager == null) {
                    manager = new PluginsFactory(space);
                    managerMap.put(path, manager);
                }
            }
        }
        return manager;
    }

    public void reload() {
        this.menuBarBeanMap.clear();
        viewBarMap.clear();
        toolBarMenus.clear();
        topMenus.clear();
        Map<String, Class<?>> allClass = new ConcurrentHashMap<>();
        allClass.putAll(EsbBeanFactory.getInstance().getAllClass());
        Set<Map.Entry<String, Class<?>>> allClassSet = allClass.entrySet();
        for (Map.Entry<String, Class<?>> clazzEntry : allClassSet) {
            Class clazz = clazzEntry.getValue();
            if (!clazz.isInterface()) {
                try {
                    checkMenuBar(clazz);
                } catch (Throwable e) {
                    e.printStackTrace();
                    logger.error(e);
                }

            }
        }

    }

    public synchronized List<MenuBarBean> getAllTopMenu(CustomMenuType menuType, String domainId) {
        List<MenuBarBean> topMenuBeans = new ArrayList<>();
        for (MenuBarBean topMenuBean : topMenus) {
            if ((topMenuBean.getDomainId() == null || topMenuBean.getDomainId().equals(domainId)) && topMenuBean.getMenuType().equals(menuType) && topMenuBean.getParentId() == null) {
                topMenuBeans.add(topMenuBean);
            }
        }
        Collections.sort(topMenuBeans);
        return topMenuBeans;
    }

    public synchronized List<DynBar> getAllViewBar(CustomMenuType menuType, String domainId) {
        List<DynBar> viewBars = new ArrayList<>();
        List<MenuBarBean> topMenuBeans = getAllTopMenu(menuType, domainId);
        for (MenuBarBean topMenuBean : topMenuBeans) {
            Class clazz = EsbFactory.guessType("$" + menuType.getSysMenuType().name());
            DynBar viewBar = getViewBarById(topMenuBean.getId(), clazz, false);
            if (viewBar != null) {
                viewBars.add(viewBar);
            }
        }
        Collections.sort(viewBars);
        return viewBars;
    }


    public <T extends DynBar> T getViewBarById(String barId, Class<T> viewType, boolean reload) {
        T bar = (T) viewBarMap.get(barId);
        MenuBarBean menuBean = this.getViewBarBeanById(barId);
        if (menuBean != null) {
            if (bar == null || menuBean.getDynLoad()) {
                try {
                    fillBar(menuBean, null, reload);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error(e);
                }
            }
            bar = (T) viewBarMap.get(barId);
        }
        return bar;
    }

    public CustomToolsBar getToolBarById(String barId, boolean reload) {
        CustomToolsBar bar = (CustomToolsBar) viewBarMap.get(barId);
        ToolBarMenuBean menuBean = this.getToolBarBeanById(barId);
        if (menuBean != null) {
            if (bar == null || menuBean.getDynLoad()) {
                try {
                    fillToolBar(menuBean, reload);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error(e);
                }
            }
            bar = (CustomToolsBar) viewBarMap.get(barId);
        }
        return bar;
    }

    private <T extends DynBar> T fillDynToolbar(ToolBarMenuBean menuBean, boolean buildMenu) throws Exception {
        T viewBar = null;
        CustomMenuType menuType = CustomMenuType.TOOLBAR;
        Class menuTypeClass = EsbFactory.guessType("$" + menuType.getSysMenuType().name());
        if (!menuTypeClass.equals(Object.class)) {
            Constructor<T> viewBarConstructor = menuTypeClass.getConstructor(new Class[]{ToolBarMenuBean.class});
            viewBar = viewBarConstructor.newInstance(new Object[]{menuBean});
        }

        if (menuBean.getLazy() != null && menuBean.getLazy() && !buildMenu) {
            Class serviceClass = menuBean.getServiceClass();
            MethodConfig methodConfig = null;
            if (serviceClass != null) {
                ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(serviceClass.getName());
                methodConfig = apiClassConfig.getFieldEvent(CustomFieldEvent.DYNRELOAD);
            }


            if (methodConfig == null) {
                serviceClass = (Class) AnnotationUtil.getDefaultValue(ToolBarMenu.class, "serviceClass");
                ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(serviceClass.getName());
                methodConfig = apiClassConfig.getFieldEvent(CustomFieldEvent.DYNRELOAD);
            }

            APICallerComponent apiCallerComponent = new APICallerComponent(methodConfig);
            apiCallerComponent.setAlias(menuBean.getId() + "_" + CustomFieldEvent.DYNRELOAD);
            apiCallerComponent.getProperties().addQuaryArgs("viewBarId", viewBar.getId());
            apiCallerComponent.getProperties().addQuaryArgs("alias", viewBar.getId());
            apiCallerComponent.getProperties().addQuaryArgs("sourceClassName", menuBean.getSourceClassName());
            apiCallerComponent.getProperties().addQuaryArgs("methodName", menuBean.getMethodName());
            apiCallerComponent.getProperties().addQuaryArgs("domainId", menuBean.getDomainId());
            UrlPathData formData = new UrlPathData(viewBar.getId(), ResponsePathTypeEnum.MENUBAR, "");
            apiCallerComponent.getProperties().addResponseData(formData);
            viewBar.addMenu(apiCallerComponent);

        } else if (menuBean.getMenuClasses() != null) {
            for (Class clazz : menuBean.getMenuClasses()) {
                try {
                    if (viewBar.initMenuClass(clazz)) {
                        ESDClass menuClass = BuildFactory.getInstance().getClassManager().loadAggregation(clazz.getName());
                        List<CustomMethodInfo> esdMethods = new ArrayList<>();
                        esdMethods.addAll(menuClass.getMethodsList());
                        esdMethods.addAll(menuClass.getOtherMethodsList());
                        Collections.sort(esdMethods);
                        APIConfig apiConfig = APIConfigFactory.getInstance().getAPIConfig(clazz.getName());
                        for (CustomMethodInfo field : esdMethods) {
                            CustomMethodInfo methodField = field;
                            if (methodField.isSplit()) {
//                                if (viewBar == null || viewBar instanceof PopDynBar) {
//                                    viewBar.addSplit(methodField.getId());
//                                }
                                if (viewBar != null && viewBar instanceof PopDynBar) {
                                    viewBar.addSplit(methodField.getId());
                                }

                            } else {
                                RequestMethodBean methodBean = apiConfig.getMethodByName(methodField.getInnerMethod().getName());
                                if (methodBean != null) {
                                    ApiClassConfig classConfig = null;
                                    try {
                                        classConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(menuClass.getClassName(), false);
                                        MethodConfig methodAPIBean = classConfig.getMethodByName(methodBean.getMethodName());
                                        if (methodAPIBean == null) {
                                            methodAPIBean = new MethodConfig(field, classConfig);
                                        }
                                        APICallerComponent component = new APICallerComponent(methodAPIBean);
                                        if (!component.getAlias().startsWith(menuBean.getId())) {
                                            component.setAlias(menuBean.getId() + "_" + component.getAlias());
                                            Set<Action> actions = component.getActions();
                                            for (Action action : actions) {
                                                action.updateArgs("{page.+" + component.getAlias() + "}", 3);
                                            }
                                        }
                                        APICallerProperties properties = component.getProperties();
                                        properties.setImageClass(methodField.getImageClass());
                                        if (methodAPIBean != null && methodAPIBean.isModule()) {
                                            viewBar.addMenu(methodAPIBean.getEUClassName(), component);
                                        } else {
                                            viewBar.addMenu(component);
                                        }
                                    } catch (JDSException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }
                    }


                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (JDSException e) {
                    e.printStackTrace();
                }
            }
        }
        if (viewBar != null && viewBar.getId() != null) {
            viewBarMap.put(viewBar.getId(), viewBar);
        }
        return viewBar;
    }

    private <T extends DynBar> List<T> fillToolBar(ToolBarMenuBean menuBean, boolean reload) throws Exception {
        List<T> dynBars = new ArrayList<>();
        T viewBar = fillDynToolbar(menuBean, reload);
        dynBars.add(viewBar);
        return dynBars;
    }

    public <T extends DynBar> T getMenu(MenuBarBean menuBean) {
        T viewBar = null;
        try {
            viewBar = fillDynBar(menuBean, null, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return viewBar;
    }


    public <T extends CustomBottomBar> T getBottomBar(BottomBarMenuBean menuBean) {
        T viewBar = null;
        try {
            viewBar = fillDynBottom(menuBean, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return viewBar;
    }

    public <T extends CustomBottomBar> T fillDynBottomBar(BottomBarMenuBean menuBean) throws Exception {
        T viewBar = fillDynBottom(menuBean, true);
        return viewBar;
    }

    public <T extends DynBar> T getToolbar(ToolBarMenuBean menuBean) {
        T viewBar = null;
        try {
            viewBar = fillDynToolbar(menuBean, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return viewBar;
    }

    public <T extends DynBar> T fillDynToolBar(ToolBarMenuBean menuBean) throws Exception {
        T viewBar = fillDynToolbar(menuBean, true);
        return viewBar;
    }

    public <T extends DynBar> T fillDynMenu(MenuBarBean menuBean) throws Exception {
        T viewBar = fillDynBar(menuBean, null, true);
        return viewBar;
    }

    private <T extends DynBar> T fillDynBar(MenuBarBean menuBean, MenuBarBean parentMenuBean, boolean buildMenu) throws
            Exception {
        T viewBar = null;
        CustomMenuType menuType = menuBean.getMenuType();

        if (parentMenuBean != null) {
            menuBean.setParentId(parentMenuBean.getId());
            menuBean.setDomainId(parentMenuBean.getDomainId());
            menuType = parentMenuBean.getMenuType();
        }


        Class menuTypeClass = EsbFactory.guessType("$" + menuType.getSysMenuType().name());
        if (!menuTypeClass.equals(Object.class)) {
            Constructor<T> viewBarConstructor = menuTypeClass.getConstructor(new Class[]{MenuBarBean.class});
            viewBar = viewBarConstructor.newInstance(new Object[]{menuBean});
        }

        if (menuBean.getLazy() != null && menuBean.getLazy() && !buildMenu) {

            Class serviceClass = menuBean.getServiceClass();
            MethodConfig methodConfig = null;
            if (serviceClass != null) {
                ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(serviceClass.getName());
                methodConfig = apiClassConfig.getFieldEvent(CustomFieldEvent.DYNRELOAD);
            }

            if (methodConfig == null) {
                serviceClass = (Class) AnnotationUtil.getDefaultValue(MenuBarMenu.class, "serviceClass");
                ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(serviceClass.getName());
                methodConfig = apiClassConfig.getFieldEvent(CustomFieldEvent.DYNRELOAD);
            }

            if (methodConfig != null) {
                APICallerComponent apiCallerComponent = new APICallerComponent(methodConfig);
                apiCallerComponent.setAlias(menuBean.getId() + "_" + CustomFieldEvent.DYNRELOAD);
                apiCallerComponent.getProperties().addQuaryArgs("viewBarId", viewBar.getId());
                apiCallerComponent.getProperties().addQuaryArgs("alias", viewBar.getId());
                apiCallerComponent.getProperties().addQuaryArgs("sourceClassName", menuBean.getSourceClassName());
                apiCallerComponent.getProperties().addQuaryArgs("methodName", menuBean.getMethodName());
                apiCallerComponent.getProperties().addQuaryArgs("domainId", menuBean.getDomainId());
                apiCallerComponent.getProperties().setImageClass(CustomImageType.refresh.getImageClass());
                apiCallerComponent.getProperties().setCaption("装载菜单");
                UrlPathData formData = new UrlPathData(viewBar.getId(), ResponsePathTypeEnum.MENUBAR, "");
                apiCallerComponent.getProperties().addResponseData(formData);
                viewBar.addMenu(apiCallerComponent);
            }
        } else if (menuBean.getMenuClasses() != null) {
            for (Class clazz : menuBean.getMenuClasses()) {
                try {
                    if (viewBar.initMenuClass(clazz)) {
                        if (clazz.isEnum()) {
                            viewBar.addMenu();
                        } else {
                            ESDClass menuClass = BuildFactory.getInstance().getClassManager().loadAggregation(clazz.getName());
                            List<CustomMethodInfo> esdMethods = new ArrayList<>();
                            esdMethods.addAll(menuClass.getMethodsList());
                            esdMethods.addAll(menuClass.getOtherMethodsList());
                            Collections.sort(esdMethods);
                            APIConfig apiConfig = APIConfigFactory.getInstance().getAPIConfig(clazz.getName());
                            for (CustomMethodInfo field : esdMethods) {
                                CustomMethodInfo methodField = field;
                                if (methodField.isSplit()) {

                                    if (viewBar != null && viewBar instanceof PopDynBar) {
                                        viewBar.addSplit(methodField.getId());
                                    }
                                } else {
                                    RequestMethodBean methodBean = apiConfig.getMethodByName(methodField.getInnerMethod().getName());
                                    if (methodBean != null) {
                                        MenuBarMenu childMenu = AnnotationUtil.getMethodAnnotation(methodField.getInnerMethod(), MenuBarMenu.class);
                                        if (childMenu != null) {
                                            Class innerReturnType = JSONGenUtil.getInnerReturnType(methodField.getInnerMethod());
                                            MenuBarBean bean = this.fillMenuBar(innerReturnType, childMenu);
                                            bean.setParentId(menuBean.getId());
                                            List<T> bars = fillBar(bean, menuBean, buildMenu);
                                            for (T bar : bars) {
                                                ((MenuDynBar) viewBar).addChild((MenuDynBar) bar);
                                            }
                                        } else {
                                            ApiClassConfig classConfig = null;
                                            try {
                                                classConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(menuClass.getClassName(), false);
                                                MethodConfig methodAPIBean = classConfig.getMethodByName(methodBean.getMethodName());
                                                if (methodAPIBean == null) {
                                                    methodAPIBean = new MethodConfig(field, classConfig);
                                                }
                                                APICallerComponent component = new APICallerComponent(methodAPIBean);

                                                if (!component.getAlias().startsWith(menuBean.getId())) {
                                                    component.setAlias(menuBean.getId() + "_" + component.getAlias());
                                                    Set<Action> actions = component.getActions();
                                                    for (Action action : actions) {
                                                        action.updateArgs("{page.+" + component.getAlias() + "}", 3);
                                                    }
                                                }

                                                APICallerProperties properties = component.getProperties();
                                                properties.setImageClass(methodField.getImageClass());
                                                if (methodAPIBean != null && methodAPIBean.isModule()) {
                                                    viewBar.addMenu(methodAPIBean.getEUClassName(), component);
                                                } else {
                                                    viewBar.addMenu(component);
                                                }
                                            } catch (JDSException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }


                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (JDSException e) {
                    e.printStackTrace();
                }
            }
        }
        if (viewBar != null && viewBar.getId() != null) {
            viewBarMap.put(viewBar.getId(), viewBar);
        }
        return viewBar;
    }


    private <T extends CustomBottomBar> T fillDynBottom(BottomBarMenuBean menuBean, boolean buildMenu) throws
            Exception {
        T viewBar = null;
        CustomMenuType menuType = menuBean.getMenuType();
        if (menuType == null) {
            menuType = CustomMenuType.BOTTOMBAR;
        }
        Class menuTypeClass = EsbFactory.guessType("$" + menuType.getSysMenuType().name());
        if (!menuTypeClass.equals(Object.class)) {
            Constructor<T> viewBarConstructor = menuTypeClass.getConstructor(new Class[]{BottomBarMenuBean.class});
            viewBar = viewBarConstructor.newInstance(new Object[]{menuBean});
        }
        if (menuBean.getLazy() != null && menuBean.getLazy() && !buildMenu) {


            Class serviceClass = menuBean.getServiceClass();
            MethodConfig methodConfig = null;
            if (serviceClass != null) {
                ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(serviceClass.getName());
                methodConfig = apiClassConfig.getFieldEvent(CustomFieldEvent.DYNRELOAD);
            }

            if (methodConfig == null) {
                serviceClass = (Class) AnnotationUtil.getDefaultValue(BottomBarMenu.class, "serviceClass");
                ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(serviceClass.getName());
                methodConfig = apiClassConfig.getFieldEvent(CustomFieldEvent.DYNRELOAD);
            }


            if (methodConfig != null) {
                APICallerComponent apiCallerComponent = new APICallerComponent(methodConfig);
                apiCallerComponent.setAlias(menuBean.getAlias() + "_" + CustomFieldEvent.DYNRELOAD);
                apiCallerComponent.getProperties().addQuaryArgs("viewBarId", viewBar.getId());
                apiCallerComponent.getProperties().addQuaryArgs("alias", viewBar.getId());
                apiCallerComponent.getProperties().addQuaryArgs("sourceClassName", menuBean.getSourceClassName());
                apiCallerComponent.getProperties().addQuaryArgs("methodName", menuBean.getMethodName());
                apiCallerComponent.getProperties().addQuaryArgs("domainId", menuBean.getDomainId());
                UrlPathData formData = new UrlPathData(viewBar.getId(), ResponsePathTypeEnum.MENUBAR, "");
                apiCallerComponent.getProperties().addResponseData(formData);
                viewBar.addMenu(apiCallerComponent);
            }
        } else if (menuBean.getMenuClasses() != null) {
            for (Class clazz : menuBean.getMenuClasses()) {
                try {
                    if (!clazz.equals(Void.class) && viewBar.initMenuClass(clazz)) {
                        ESDClass menuClass = BuildFactory.getInstance().getClassManager().loadAggregation(clazz.getName());
                        if (menuTypeClass != null && menuClass != null) {

                            List<CustomMethodInfo> esdMethods = new ArrayList<>();
                            esdMethods.addAll(menuClass.getMethodsList());
                            esdMethods.addAll(menuClass.getOtherMethodsList());
                            Collections.sort(esdMethods);
                            APIConfig apiConfig = APIConfigFactory.getInstance().getAPIConfig(clazz.getName());
                            for (CustomMethodInfo field : esdMethods) {
                                CustomMethodInfo methodField = field;
                                if (methodField.isSplit()) {
                                    if (viewBar != null && viewBar instanceof PopDynBar) {
                                        viewBar.addSplit(methodField.getId());
                                    }
                                } else {
                                    RequestMethodBean methodBean = apiConfig.getMethodByName(methodField.getInnerMethod().getName());
                                    if (methodBean != null) {
                                        ApiClassConfig classConfig = null;
                                        try {
                                            classConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(menuClass.getClassName(), false);
                                            MethodConfig methodAPIBean = classConfig.getMethodByName(methodBean.getMethodName());
                                            if (methodAPIBean == null) {
                                                methodAPIBean = new MethodConfig(field, classConfig);
                                            }
                                            APICallerComponent component = new APICallerComponent(methodAPIBean);

                                            if (!component.getAlias().startsWith(menuBean.getAlias())) {
                                                component.setAlias(menuBean.getAlias() + "_" + component.getAlias());
                                                Set<Action> actions = component.getActions();
                                                for (Action action : actions) {
                                                    action.updateArgs("{page.+" + component.getAlias() + "}", 3);
                                                }
                                            }
                                            APICallerProperties properties = component.getProperties();
                                            properties.setImageClass(methodField.getImageClass());
                                            if (methodAPIBean != null && methodAPIBean.isModule()) {
                                                viewBar.addMenu(methodAPIBean.getEUClassName(), component);
                                            } else {
                                                viewBar.addMenu(component);
                                            }
                                        } catch (JDSException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }

                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (JDSException e) {
                    e.printStackTrace();
                }
            }
        }
        if (viewBar != null && viewBar.getId() != null) {
            viewBarMap.put(viewBar.getId(), viewBar);
        }
        return viewBar;
    }

    private <T extends DynBar> List<T> fillBar(MenuBarBean menuBean, MenuBarBean parentMenuBean, boolean reload) throws
            Exception {
        List<T> dynBars = new ArrayList<>();
        T viewBar = fillDynBar(menuBean, parentMenuBean, reload);
        dynBars.add(viewBar);
        return dynBars;
    }

    public ToolBarMenuBean checkToolMenuBar(Class clazz) {
        ToolBarMenu topMenu = AnnotationUtil.getClassAnnotation(clazz, ToolBarMenu.class);
        return fillToolBar(clazz, topMenu);
    }

    public ToolBarMenuBean fillToolBar(Class clazz, ToolBarMenu toolBarMenu) {
        ToolBarMenuBean menuBean = null;
        try {
            ESDClass menuEsdClass = BuildFactory.getInstance().getClassManager().loadAggregation(clazz.getName());
            if (menuEsdClass != null) {
                ToolBarMenuBean menuClassBean = new ToolBarMenuBean(clazz, toolBarMenu);
                menuBean = this.getToolBarBeanById(menuClassBean.getId());
                if (menuBean == null) {
                    menuBean = menuClassBean;
                    toolBarMenus.add(menuClassBean);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (JDSException e) {
            e.printStackTrace();
        }

        return menuBean;
    }

    ;


    public MenuBarBean checkMenuBar(Class clazz) {
        MenuBarBean menuBarBean = menuBarBeanMap.get(clazz.getName());
        if (menuBarBean == null) {
            MenuBarMenu topMenu = AnnotationUtil.getClassAnnotation(clazz, MenuBarMenu.class);
            menuBarBean = fillMenuBar(clazz, topMenu);
        }
        return menuBarBean;

    }

    public MenuBarBean fillMenuBar(Class clazz, MenuBarMenu topMenu) {
        MenuBarBean menuBean = null;
        try {
            ESDClass menuEsdClass = BuildFactory.getInstance().getClassManager().loadAggregation(clazz.getName());
            if (topMenu != null && menuEsdClass != null) {
                MenuBarBean menuClassBean = new MenuBarBean(clazz, topMenu, null);
                menuBean = this.getViewBarBeanById(menuClassBean.getId());
                if (menuBean != null) {
                    menuBean.addMenuClass(clazz);
                } else {
                    topMenus.add(menuClassBean);
                    menuBean = menuClassBean;
                }

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (JDSException e) {
            e.printStackTrace();
        }

        return menuBean;
    }

    ;

    //
    public <T extends DynBar> T initMenuClass(Class clazz, Class<T> tClass) {
        T viewBar = null;
        MenuBarBean menuBarBean = checkMenuBar(clazz);
        try {
            if (tClass == null) {
                ESDClass menuEsdClass = BuildFactory.getInstance().getClassManager().loadAggregation(clazz.getName());
                if (menuBarBean != null && menuEsdClass != null) {
                    tClass = EsbFactory.guessType("$" + menuBarBean.getMenuType().getSysMenuType().name());
                }
            }
            if (menuBarBean != null) {
                viewBar = this.getViewBarById(menuBarBean.getId(), tClass, false);

            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return viewBar;
    }

    public CustomToolsBar initToolClass(Class clazz) {
        CustomToolsBar viewBar = null;
        ToolBarMenuBean toolBarMenuBean = checkToolMenuBar(clazz);
        try {
            ESDClass menuEsdClass = BuildFactory.getInstance().getClassManager().loadAggregation(clazz.getName());
            String menuId = toolBarMenuBean.getId();
            if (menuEsdClass != null) {
                viewBar = this.getToolBarById(menuId, false);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (JDSException e) {
            e.printStackTrace();
        }

        return viewBar;
    }


    PluginsFactory(MySpace space) {
        this.space = space;
        this.reload();
    }

    public MenuBarBean getViewBarBeanById(String barId) {
        for (MenuBarBean bean : topMenus) {
            if (bean.getId().equals(barId)) {
                return bean;
            }
        }
        return null;
    }

    public ToolBarMenuBean getToolBarBeanById(String barId) {
        for (ToolBarMenuBean bean : toolBarMenus) {
            if (bean.getId().equals(barId)) {
                return bean;
            }
        }
        return null;
    }


}


