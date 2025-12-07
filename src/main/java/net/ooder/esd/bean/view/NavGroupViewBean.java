package net.ooder.esd.bean.view;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.common.JDSException;
import net.ooder.common.util.CaselessStringKeyHashMap;
import net.ooder.common.util.ClassUtility;
import net.ooder.config.TreeListResultModel;
import net.ooder.esd.annotation.*;
import net.ooder.esd.annotation.event.CustomFormEvent;
import net.ooder.esd.annotation.event.CustomTabsEvent;
import net.ooder.esd.annotation.field.TabItem;
import net.ooder.esd.annotation.field.ToolBarMenu;
import net.ooder.esd.annotation.menu.CustomFormMenu;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.*;
import net.ooder.esd.bean.nav.GroupItemBean;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.context.MethodRoot;
import net.ooder.esd.dsm.java.JavaGenSource;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;
import net.ooder.esd.engine.enums.MenuBarBean;
import net.ooder.esd.tool.DSMProperties;
import net.ooder.esd.tool.component.BlockComponent;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.GroupComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.Properties;
import net.ooder.esd.tool.properties.item.GalleryItem;
import net.ooder.esd.tool.properties.item.TabListItem;
import net.ooder.esd.util.OODUtil;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.util.EnumsUtil;
import net.ooder.web.util.AnnotationUtil;
import net.ooder.web.util.JSONGenUtil;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.Callable;

@AnnotationType(clazz = NavGroupAnnotation.class)
public class NavGroupViewBean extends NavBaseViewBean<GroupItemBean, GalleryItem, BlockComponent> {

    ModuleViewType moduleViewType = ModuleViewType.NAVGROUPCONFIG;

    CustomBlockBean blockBean;

    Dock dock;

    Boolean noFrame;

    Boolean toggleBtn;

    ToolBarMenuBean toolBarMenuBean;

    List<CustomFormMenu> toolBarMenu = new ArrayList<>();

    List<CustomFormMenu> bottombarMenu = new ArrayList<>();

    List<CustomFormMenu> customMenu = new ArrayList<>();

    Set<CustomFormEvent> event = new LinkedHashSet<>();

    List<GroupItemBean> itemBeans = new ArrayList<>();

    @JSONField(serialize = false)
    MethodConfig methodAPIBean;

    @JSONField(serialize = false)
    List<CustomModuleBean> moduleBeans = new ArrayList<>();

    public NavGroupViewBean() {

    }

    public NavGroupViewBean(ModuleComponent<BlockComponent> moduleComponent) {
        super();
        AnnotationUtil.fillDefaultValue(NavGroupAnnotation.class, this);
        this.updateModule(moduleComponent);
    }


    public List<Callable> updateModule(ModuleComponent moduleComponent) {
        List<Callable> javaSrcBeans = new ArrayList<>();
        super.updateBaseModule(moduleComponent);
        itemBeans = new ArrayList<>();
        BlockComponent currComponent = (BlockComponent) moduleComponent.getCurrComponent();
        String realPath = currComponent.getPath();
        DSMProperties dsmProperties = moduleComponent.getProperties().getDsmProperties();
        if (dsmProperties != null && dsmProperties.getRealPath() != null) {
            realPath = dsmProperties.getRealPath();
        }
        this.setXpath(realPath);
        Properties blockProperties = currComponent.getParent().getProperties();
        List<Component> subModules = moduleComponent.findComponents(ComponentType.MODULE, null);
        List<GroupComponent> groupComponents = moduleComponent.findComponents(ComponentType.GROUP, null);
        for (GroupComponent groupComponent : groupComponents) {
            String groupId = groupComponent.getProperties().getEuClassName();
            GroupItemBean itemBean = this.getItemBean(groupId);
            if (itemBean == null) {
                itemBean = new GroupItemBean(this, groupComponent);
                this.itemBeans.add(itemBean);
            } else {
                itemBean.update(groupComponent);
            }
            CustomModuleBean customModuleBean = new CustomModuleBean(groupComponent);

            if (itemBean.getMethodConfig() != null) {
                customModuleBean.reBindMethod(itemBean.getMethodConfig());
            }
            if (!moduleBeans.contains(customModuleBean)) {
                moduleBeans.add(customModuleBean);
            }
            itemBean.setMethodRoot(new MethodRoot(customModuleBean));
        }

        this.name = blockProperties.getName();
        itemConfigMap = new CaselessStringKeyHashMap<>();
        itemNames = new LinkedHashSet<String>();
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(blockProperties), Map.class), this, false, false);
        return javaSrcBeans;
    }


    public NavGroupViewBean(MethodConfig methodAPIBean) {
        super(methodAPIBean);
        Class clazz = JSONGenUtil.getInnerReturnType(methodAPIBean.getMethod());
        this.init(clazz);
        NavGroupAnnotation groupViewAnnotation = AnnotationUtil.getClassAnnotation(clazz, NavGroupAnnotation.class);
        if (groupViewAnnotation != null) {
            fillData(groupViewAnnotation);
        } else {
            AnnotationUtil.fillDefaultValue(NavGroupAnnotation.class, this);
        }

        BlockAnnotation panelAnnotation = AnnotationUtil.getClassAnnotation(clazz, BlockAnnotation.class);
        if (panelAnnotation != null) {
            blockBean = new CustomBlockBean(panelAnnotation);
        } else {
            AnnotationUtil.fillDefaultValue(BlockAnnotation.class, new CustomBlockBean());
        }


        try {
            this.initHiddenField(this.getViewClassName());
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }


    public NavGroupViewBean(Class<? extends TabListItem> clazz) {
        init(clazz);
    }


    public NavGroupViewBean(Class<? extends TabListItem> clazz, TabsViewBean parentItem) {
        this.domainId = parentItem.getDomainId();
        this.viewClassName = parentItem.getViewClassName();
        this.sourceClassName = parentItem.getSourceClassName();
        init(clazz);
    }

    void init(Class<? extends TabListItem> clazz) {
        NavGroupAnnotation groupAnnotation = AnnotationUtil.getClassAnnotation(clazz, NavGroupAnnotation.class);
        if (groupAnnotation != null) {
            fillData(groupAnnotation);
        } else {
            AnnotationUtil.fillDefaultValue(NavGroupAnnotation.class, this);
        }
        if (customMenu != null && customMenu.size() > 0) {
            if (this.menuBar == null) {
                this.menuBar = AnnotationUtil.fillDefaultValue(MenuBarMenu.class, new MenuBarBean());
            }
        }

        if (toolBarMenu != null && toolBarMenu.size() > 0) {
            if (this.toolBar == null) {
                this.toolBar = AnnotationUtil.fillDefaultValue(ToolBarMenu.class, new ToolBarMenuBean());
            }
        }

        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            GroupItemAnnotation tabItemAnnotation = AnnotationUtil.getConstructorAnnotation(constructor, GroupItemAnnotation.class);
            if (tabItemAnnotation != null) {
                if (!tabItemAnnotation.customItems().equals(TabItem.class)) {
                    TabItem[] tabItems = EnumsUtil.getEnums(tabItemAnnotation.customItems());
                    for (TabItem tabItem : tabItems) {
                        GroupItemBean tabItemBean = new GroupItemBean(constructor, this, tabItem);
                        itemBeans.add(tabItemBean);
                    }
                } else if (tabItemAnnotation.bindService() != null && !tabItemAnnotation.bindService().equals(Void.class) && !tabItemAnnotation.bindService().equals(Enum.class)) {
                    try {
                        ApiClassConfig entityConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(tabItemAnnotation.bindService().getName());
                        MethodConfig editorMethod = entityConfig.getTabsEvent(CustomTabsEvent.TABEDITOR);
                        if (editorMethod != null) {
                            GroupItemBean tabItemBean = new GroupItemBean(constructor, this);
                            itemBeans.add(tabItemBean);
                        }
                        MethodConfig loadChildMethod = entityConfig.getTabsEvent(CustomTabsEvent.TABCHILD);
                        if (loadChildMethod != null && loadChildMethod.getViewClass() != null) {
                            String childTreeClassName = loadChildMethod.getViewClass().getClassName();
                            if (!childTreeClassName.equals(clazz.getName())) {
                                Class treeClass = ClassUtility.loadClass(childTreeClassName);
                                NavGroupViewBean viewBean = new NavGroupViewBean(treeClass, this);
                                itemBeans.addAll(viewBean.getItemBeans());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    GroupItemBean childTreeViewBean = new GroupItemBean(constructor, this);
                    itemBeans.add(childTreeViewBean);
                }
            }
        }


        Set<String> fieldNames = this.getItemNames();
        Map<String, Object> tagMap = new HashMap<>();
        List<FieldModuleConfig> fieldModuleConfigs = new ArrayList<>();
        for (String fieldName : fieldNames) {
            FieldModuleConfig fieldFormConfig = this.getItemConfigMap().get(fieldName);
            if (fieldFormConfig != null) {
                fieldModuleConfigs.add(fieldFormConfig);
            }
        }
        try {

            List<MethodConfig> methodConfigs = new ArrayList<>();
            for (FieldModuleConfig fieldModuleConfig : fieldModuleConfigs) {
                methodConfigs.add(fieldModuleConfig.getMethodConfig());
            }
            ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(this.viewClassName);
            for (MethodConfig methodConfig : apiClassConfig.getAllMethods()) {
                if (!methodConfigs.contains(methodConfig)) {
                    methodConfigs.add(methodConfig);
                }
            }
            for (MethodConfig methodConfig : methodConfigs) {
                GroupItemAnnotation tabItemAnnotation = AnnotationUtil.getMethodAnnotation(methodConfig.getMethod(), GroupItemAnnotation.class);
                if (tabItemAnnotation != null) {
                    if (!tabItemAnnotation.customItems().equals(TabItem.class)) {
                        TabItem[] tabItems = EnumsUtil.getEnums(tabItemAnnotation.customItems());
                        for (TabItem tabItem : tabItems) {
                            GroupItemBean itemBean = new GroupItemBean(methodConfig, this, tabItem);
                            itemBeans.add(itemBean);
                        }
                    } else if (tabItemAnnotation.bindService() != null && !tabItemAnnotation.bindService().equals(Void.class) && !tabItemAnnotation.bindService().equals(Enum.class)) {
                        try {
                            ApiClassConfig entityConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(tabItemAnnotation.bindService().getName());
                            MethodConfig editorMethod = entityConfig.getTabsEvent(CustomTabsEvent.TABEDITOR);
                            if (editorMethod != null) {
                                GroupItemBean itemBean = new GroupItemBean(methodConfig, this);
                                itemBeans.add(itemBean);
                            }
                            MethodConfig loadChildMethod = entityConfig.getTabsEvent(CustomTabsEvent.TABCHILD);
                            if (loadChildMethod != null && loadChildMethod.getViewClass() != null) {
                                String childTreeClassName = loadChildMethod.getViewClass().getClassName();
                                if (!childTreeClassName.equals(clazz.getName())) {
                                    Class treeClass = ClassUtility.loadClass(childTreeClassName);
                                    NavGroupViewBean viewBean = new NavGroupViewBean(treeClass, this);
                                    itemBeans.addAll(viewBean.getItemBeans());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        GroupItemBean itemBean = new GroupItemBean(methodConfig, this);
                        itemBeans.add(itemBean);
                    }

                } else if (methodConfig.isModule()) {
                    GroupItemBean itemBean = new GroupItemBean(methodConfig, this);
                    itemBeans.add(itemBean);
                }
            }


            if (bottombarMenu != null && bottombarMenu.size() > 0) {
                if (this.bottomBar == null) {
                    this.bottomBar = AnnotationUtil.fillDefaultValue(BottomBarMenu.class, new BottomBarMenuBean());
                }
            }

            this.initHiddenField(clazz.getName());
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }

    public List<JavaGenSource> buildAll() {

        return build(childModules);
    }


    @Override
    public ComponentBean findComByPath(String path) {
        path = OODUtil.formatJavaName(path, false);
        if (path != null) {
            path = path.toLowerCase();
        }
        List<CustomModuleBean> moduleBeans = this.getModuleBeans();
        for (CustomModuleBean moduleBean : moduleBeans) {
            CustomViewBean customViewBean = moduleBean.getMethodConfig().getView();
            if (customViewBean != null) {
                ComponentBean componentBean = customViewBean.findComByPath(path);
                if (componentBean != null) {
                    return componentBean;
                }

            }
        }
        List<GroupItemBean> itemBeans = this.getItemBeans();
        for (GroupItemBean itemBean : itemBeans) {
            MethodConfig methodConfig = itemBean.getMethodConfig();
            if (methodConfig != null) {
                CustomViewBean customViewBean = methodConfig.getView();
                if (customViewBean != null) {
                    ComponentBean componentBean = customViewBean.findComByPath(path);
                    if (componentBean != null) {
                        return componentBean;
                    }
                }
            }
        }
        return null;
    }

    public List<CustomModuleBean> getModuleBeans() {
        return moduleBeans;
    }

    public void setModuleBeans(List<CustomModuleBean> moduleBeans) {
        this.moduleBeans = moduleBeans;
    }

    public NavGroupViewBean(Class<? extends TabListItem> clazz, NavGroupViewBean parentItem) {
        this.domainId = parentItem.getDomainId();
        this.viewClassName = parentItem.getViewClassName();
        this.sourceClassName = parentItem.getSourceClassName();
        init(clazz);
    }

    public Dock getDock() {
        return dock;
    }

    public void setDock(Dock dock) {
        this.dock = dock;
    }

    public Boolean getNoFrame() {
        return noFrame;
    }

    public void setNoFrame(Boolean noFrame) {
        this.noFrame = noFrame;
    }

    public Boolean getToggleBtn() {
        return toggleBtn;
    }

    public void setToggleBtn(Boolean toggleBtn) {
        this.toggleBtn = toggleBtn;
    }

    @Override
    public List<CustomFormMenu> getBottombarMenu() {
        return bottombarMenu;
    }

    @Override
    public void setBottombarMenu(List<CustomFormMenu> bottombarMenu) {
        this.bottombarMenu = bottombarMenu;
    }

    @Override
    public List<CustomFormMenu> getCustomMenu() {
        return customMenu;
    }

    @Override
    public void setCustomMenu(List<CustomFormMenu> customMenu) {
        this.customMenu = customMenu;
    }

    @Override
    public Set<CustomFormEvent> getEvent() {
        return event;
    }

    @Override
    public void setEvent(Set<CustomFormEvent> event) {
        this.event = event;
    }

    @Override
    public List<GroupItemBean> getItemBeans() {
        return itemBeans;
    }

    public void setItemBeans(List<GroupItemBean> itemBeans) {
        this.itemBeans = itemBeans;
    }

    public MethodConfig getMethodAPIBean() {
        return methodAPIBean;
    }

    public void setMethodAPIBean(MethodConfig methodAPIBean) {
        this.methodAPIBean = methodAPIBean;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.GROUP;
    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = super.getAnnotationBeans();
        if (toolBar != null) {
            annotationBeans.add(toolBar);
        }
        if (menuBar != null &&
                ((menuBar.getMenus() != null && menuBar.getMenus().length > 0)
                        || (menuBar.getMenuClasses() != null && menuBar.getMenuClasses().length > 0))) {
            annotationBeans.add(menuBar);
        }
        if (bottomBar != null) {
            annotationBeans.add(bottomBar);
        }

        annotationBeans.add(this);

        return annotationBeans;
    }


    @Override
    @JSONField(serialize = false)
    public List<FieldModuleConfig> getNavItems() {
        Set<String> fieldNames = this.getItemNames();
        Map<String, Object> tagMap = new HashMap<>();
        List<FieldModuleConfig> fields = new ArrayList<>();
        for (String fieldName : fieldNames) {
            FieldModuleConfig fieldFormConfig = this.getItemConfigMap().get(fieldName);
            if (fieldFormConfig != null) {
                fields.add(fieldFormConfig);
            }
        }

        if (fields.isEmpty() && methodAPIBean != null) {
            try {
                Object object = invokMethod(this.methodAPIBean.getRequestMethodBean(), tagMap);
                if (object instanceof TreeListResultModel) {
                    TreeListResultModel<List<TreeListItem>> resultModel = (TreeListResultModel) object;
                    List<TreeListItem> items = resultModel.getData();
                    if (items != null) {
                        for (TreeListItem item : items) {
                            FieldModuleConfig fieldFormConfig = new FieldModuleConfig();
                            fieldFormConfig.setId(item.getId());
                            fieldFormConfig.setSourceClassName(sourceClassName);
                            fieldFormConfig.setViewClassName(viewClassName);
                            fieldFormConfig.setDomainId(domainId);
                            fieldFormConfig.setMethodName(item.getMethodName());
                            fieldFormConfig.setCaption(item.getCaption());
                            fieldFormConfig.setImageClass(item.getImageClass());
                            fieldFormConfig.setEuClassName(item.getEuClassName());
                            fieldFormConfig.setTagVar(item.getTagVar());
                            fields.add(fieldFormConfig);
                        }
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fields;
    }

    public ToolBarMenuBean getToolBarMenuBean() {
        return toolBarMenuBean;
    }

    public void setToolBarMenuBean(ToolBarMenuBean toolBarMenuBean) {
        this.toolBarMenuBean = toolBarMenuBean;
    }

    public List<CustomFormMenu> getToolBarMenu() {
        return toolBarMenu;
    }

    public void setToolBarMenu(List<CustomFormMenu> toolBarMenu) {
        this.toolBarMenu = toolBarMenu;
    }

    public List<GroupItemBean> getItemBean(Constructor constructor) {
        List<GroupItemBean> childTabViewBeans = this.getItemBeans();
        List<GroupItemBean> treeViewBeans = new ArrayList<>();
        for (GroupItemBean childTabViewBean : childTabViewBeans) {
            try {
                Constructor sourceConstructor = childTabViewBean.getConstructorBean().getSourceConstructor();
                if (sourceConstructor != null && constructor.equals(sourceConstructor)) {
                    treeViewBeans.add(childTabViewBean);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return treeViewBeans;
    }

    public GroupItemBean getItemBean(TabItem tabItem) {
        List<GroupItemBean> childTabViewBeans = this.getItemBeans();
        for (GroupItemBean childTabViewBean : childTabViewBeans) {
            if (childTabViewBean.getTabItem() != null && childTabViewBean.getTabItem().equals(tabItem)) {
                return childTabViewBean;

            }
        }
        return null;
    }


    public NavGroupViewBean(Class<? extends TreeListItem> clazz, CustomTreeViewBean parentItem) {
        this.domainId = parentItem.getDomainId();
        init(clazz);
    }


    public CustomBlockBean getBlockBean() {
        return blockBean;
    }


    public void setBlockBean(CustomBlockBean blockBean) {
        this.blockBean = blockBean;
    }

    public NavGroupViewBean fillData(NavGroupAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet = super.getOtherClass();
        List<GroupItemBean> itemBeans = this.getItemBeans();

        for (GroupItemBean itemBean : itemBeans) {
            if (itemBean.getMethodConfig() != null) {
                classSet.addAll(itemBean.getMethodConfig().getOtherClass());
            } else if (itemBean.getBindService() != null && !itemBean.getBindService().equals(Void.class) && !itemBean.getBindService().equals(Enum.class)) {
                classSet.add(itemBean.getBindService());
            }
        }
        return ClassUtility.checkBase(classSet);
    }

    @Override
    public ToolBarMenuBean getToolBar() {
        if (toolBar == null) {
            if (toolBarMenu != null && toolBarMenu.size() > 0) {
                this.toolBar = AnnotationUtil.fillDefaultValue(ToolBarMenu.class, new ToolBarMenuBean());
                this.toolBar.setMenus(toolBarMenu.toArray(new CustomFormMenu[]{}));
            }
        }
        return toolBar;
    }

    @Override
    public MenuBarBean getMenuBar() {
        if (menuBar == null) {
            if (customMenu != null && customMenu.size() > 0) {
                this.menuBar = AnnotationUtil.fillDefaultValue(MenuBarMenu.class, new MenuBarBean());
                this.menuBar.setMenus(customMenu.toArray(new CustomFormMenu[]{}));
            }
        }
        return menuBar;
    }


    @Override
    public BottomBarMenuBean getBottomBar() {
        if (bottomBar == null) {
            if (bottombarMenu != null && bottombarMenu.size() > 0) {
                this.bottomBar = AnnotationUtil.fillDefaultValue(BottomBarMenu.class, new BottomBarMenuBean());
                this.bottomBar.setBottombar(bottombarMenu.toArray(new CustomFormMenu[]{}));
            }
        }
        return bottomBar;
    }

    @Override
    public ModuleViewType getModuleViewType() {
        return moduleViewType;
    }

    public void setModuleViewType(ModuleViewType moduleViewType) {
        this.moduleViewType = moduleViewType;
    }


}

