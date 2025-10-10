package net.ooder.esd.bean.view;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.common.JDSException;
import net.ooder.common.util.ClassUtility;
import net.ooder.config.TreeListResultModel;
import net.ooder.esd.annotation.BottomBarMenu;
import net.ooder.esd.annotation.EnumsClass;
import net.ooder.esd.annotation.MenuBarMenu;
import net.ooder.esd.annotation.TabItemAnnotation;
import net.ooder.esd.annotation.event.*;
import net.ooder.esd.annotation.field.TabItem;
import net.ooder.esd.annotation.field.ToolBarMenu;
import net.ooder.esd.annotation.menu.CustomFormMenu;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.*;
import net.ooder.esd.bean.nav.TabItemBean;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.custom.properties.NavTabListItem;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.engine.enums.MenuBarBean;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.TabsComponent;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.item.TabListItem;
import net.ooder.esd.util.ESDEnumsUtil;
import net.ooder.esd.util.OODUtil;
import net.ooder.esd.util.json.EMSerializer;
import net.ooder.util.EnumsUtil;
import net.ooder.web.util.AnnotationUtil;

import java.lang.reflect.Constructor;
import java.util.*;

public abstract class BaseTabsViewBean<E extends CustomEvent, U extends TabListItem> extends CustomViewBean<FieldFormConfig, U, TabsComponent> implements ComponentBean<TabsComponent> {

    @JSONField(serialize = false)
    public List<CustomModuleBean> moduleBeans = new ArrayList<>();

    public List<TabItemBean> itemBeans = new ArrayList<>();


    EnumsClassBean enumsClassBean;
    Boolean closeBtn;
    Boolean popBtn;
    Boolean autoReload;
    Boolean iniFold;
    Boolean activeLast;
    String message;
    Integer maxHeight;
    SelModeType selMode;
    Boolean lazyAppend;
    Boolean formField;
    Boolean noHandler;
    Boolean animCollapse;
    Boolean dynDestory;
    Boolean togglePlaceholder;
    TagCmdsAlign tagCmdsAlign;
    BorderType borderType;
    Boolean group;
    Boolean autoSave;
    String optBtn;
    String valueSeparator;
    Boolean singleOpen = true;
    BarLocationType barLocation;
    HAlignType barHAlign;
    VAlignType barVAlign;
    @JSONField(serializeUsing = EMSerializer.class)
    String barSize;
    @JSONField(serializeUsing = EMSerializer.class)
    String sideBarSize;

    Boolean autoIconColor;
    Boolean autoItemColor;
    Boolean autoFontColor;

    SideBarStatusType sideBarStatus;
    ToolBarMenuBean toolBarMenuBean;
    List<CustomFormMenu> toolBarMenu = new ArrayList<>();
    List<CustomFormMenu> bottombarMenu = new ArrayList<>();
    List<CustomFormMenu> customMenu = new ArrayList<>();

    public LinkedHashSet<TabsEventBean> extAPIEvent = new LinkedHashSet<>();
    public BaseTabsViewBean() {
        super();
    }

    public BaseTabsViewBean(MethodConfig methodConfig) {
        super(methodConfig);
    }

    @Override
    public ComponentBean findComByAlias(String alias) {
        alias = OODUtil.formatJavaName(alias, false);
        if (alias != null) {
            alias = alias.toLowerCase();
        }
        if (OODUtil.formatJavaName(this.getId(), false).equals(alias)) {
            return this;
        } else {
            List<CustomModuleBean> moduleBeans = this.getModuleBeans();
            for (CustomModuleBean moduleBean : moduleBeans) {
                MethodConfig methodConfig = moduleBean.getMethodConfig();
                if (methodConfig != null) {
                    CustomViewBean customViewBean = methodConfig.getView();
                    if (customViewBean != null) {
                        ComponentBean componentBean = customViewBean.findComByAlias(alias);
                        if (componentBean != null) {
                            return componentBean;
                        }
                    }
                }
            }

            itemBeans = this.getItemBeans();
            for (TabItemBean itemBean : itemBeans) {
                List<MethodConfig> methodConfigs = itemBean.getMethodConfigList();
                for (MethodConfig methodConfig : methodConfigs) {
                    if (methodConfig != null) {
                        CustomViewBean customViewBean = methodConfig.getView();
                        if (customViewBean != null) {
                            ComponentBean componentBean = customViewBean.findComByAlias(alias);
                            if (componentBean != null) {
                                return componentBean;
                            }
                        }
                    }
                }
            }

            List<U> tabItemBeans = this.getTabItems();
            for (U itemBean : tabItemBeans) {
                MethodConfig editorMethod = this.findEditorMethod(itemBean.getBindClass());
                if (editorMethod != null) {
                    CustomViewBean customViewBean = editorMethod.getView();
                    if (customViewBean != null) {
                        ComponentBean componentBean = customViewBean.findComByAlias(alias);
                        if (componentBean != null) {
                            return componentBean;
                        }
                    }
                }
            }

        }
        return null;
    }


    @Override
    public ComponentBean findComByPath(String path) {
        path = OODUtil.formatJavaName(path, false);
        if (path != null) {
            path = path.toLowerCase();
        }
        if (OODUtil.formatJavaName(this.getXpath(), false).equals(path)) {
            return this;
        } else {
            List<CustomModuleBean> moduleBeans = this.getModuleBeans();
            for (CustomModuleBean moduleBean : moduleBeans) {
                MethodConfig methodConfig = moduleBean.getMethodConfig();
                if (methodConfig != null) {
                    CustomViewBean customViewBean = methodConfig.getView();
                    if (customViewBean != null) {
                        String itemPath = getXpath();
                        if (!itemPath.endsWith("." + moduleBean.getAlias())) {
                            itemPath = itemPath + "." + moduleBean.getAlias();
                        }
                        customViewBean.setXpath(itemPath);
                        ComponentBean componentBean = customViewBean.findComByPath(path);
                        if (componentBean != null) {
                            return componentBean;
                        }

                    }
                }
            }

            itemBeans = this.getItemBeans();
            for (TabItemBean itemBean : itemBeans) {
                List<MethodConfig> methodConfigs = itemBean.getMethodConfigList();
                for (MethodConfig methodConfig : methodConfigs) {
                    if (methodConfig != null && !methodConfig.equals(this.getMethodConfig())) {
                        CustomViewBean customViewBean = methodConfig.getView();
                        if (customViewBean != null) {
                            if (customViewBean.getXpath().equals(path)) {
                                return customViewBean;
                            } else {
                                String itemPath = getXpath();
                                if (!itemPath.endsWith("." + methodConfig.getFieldName())) {
                                    itemPath = itemPath + "." + methodConfig.getFieldName();
                                }
                                customViewBean.setXpath(itemPath);
                                ComponentBean componentBean = customViewBean.findComByPath(path);
                                if (componentBean != null) {
                                    return componentBean;
                                }
                            }
                        }
                    }
                }

            }

            List<U> tabItemBeans = this.getTabItems();
            for (U itemBean : tabItemBeans) {
                MethodConfig editorMethod = this.findEditorMethod(itemBean.getBindClass());
                if (editorMethod != null) {
                    CustomViewBean customViewBean = editorMethod.getView();
                    if (customViewBean != null) {
                        if (customViewBean.getXpath().equals(path)) {
                            return customViewBean;
                        } else {
                            String itemPath = getXpath();
                            if (!itemPath.endsWith("." + editorMethod.getFieldName())) {
                                itemPath = itemPath + "." + editorMethod.getFieldName();
                            }
                            customViewBean.setXpath(itemPath);
                            ComponentBean componentBean = customViewBean.findComByPath(path);
                            if (componentBean != null) {
                                return componentBean;
                            }
                        }
                    }
                }
            }

        }
        return null;
    }

    public List<MethodConfig> findMethodByTabId(String tabId) {
        List<MethodConfig> methodConfigList = new ArrayList<>();
        TabItemBean itemBean = this.getChildTabBean(tabId);
        methodConfigList.addAll(itemBean.getMethodConfigList());
        if (methodConfigList.isEmpty()) {
            U uitemBean = this.findTabItem(tabId);
            Class[] bindClass = uitemBean.getBindClass();
            if (bindClass != null) {
                for (Class clazz : bindClass) {
                    MethodConfig methodConfig = this.findEditorMethod(clazz);
                    if (methodConfig != null) {
                        methodConfigList.add(methodConfig);
                    }
                }
            }
        }
        return methodConfigList;
    }


    protected void initTabItems(TabListItem layoutItem, String projectName) throws JDSException {
        String euClassName = layoutItem.getEuClassName();
        if (euClassName != null) {
            EUModule euModule = ESDFacrory.getAdminESDClient().getModule(euClassName, projectName);
            if (euModule == null) {
                euModule = CustomViewFactory.getInstance().getView(euClassName, projectName);
            }
            if (euModule != null) {
                MethodConfig methodConfig = euModule.getComponent().getMethodAPIBean();
                CustomModuleBean customModuleBean = new CustomModuleBean(euModule.getComponent());
                customModuleBean.reBindMethod(methodConfig);
                if (!moduleBeans.contains(customModuleBean)) {
                    moduleBeans.add(customModuleBean);
                }
                FieldModuleConfig config = new FieldModuleConfig(customModuleBean);
                itemConfigMap.put(config.getId(), config);
                itemNames.add(config.getId());
            }
        }
    }


    public TabItemBean getTabItemBeanById(String itemId) {
        if (itemBeans != null) {
            for (TabItemBean itemBean : itemBeans) {
                if (itemBean.getId().equals(itemId)) {
                    return itemBean;
                }
            }
        }
        return null;
    }

    public List<CustomModuleBean> getModuleBeans() {
        if (moduleBeans == null) {
            moduleBeans = new ArrayList<>();
        }
        return moduleBeans;
    }

    public void setModuleBeans(List<CustomModuleBean> moduleBeans) {
        this.moduleBeans = moduleBeans;
    }

    protected List<NavTabListItem> cloneItems(List<? extends TabListItem> tabListItemList) {
        List<NavTabListItem> navTabItems = new ArrayList<>();
        for (TabListItem tabListItem : tabListItemList) {
            NavTabListItem navTabListItem = null;
            if (tabListItem instanceof NavTabListItem) {
                navTabListItem = (NavTabListItem) tabListItem;
            } else {
                navTabListItem = new NavTabListItem(tabListItem);
            }
            navTabItems.add(navTabListItem);
        }
        return navTabItems;
    }


    public List<TabItemBean> getItemBeans() {
        if (itemBeans == null) {
            itemBeans = new ArrayList<>();
        } else {
            Arrays.sort(itemBeans.toArray());
        }
        return itemBeans;
    }


    public void setItemBeans(List<TabItemBean> itemBeans) {
        this.itemBeans = itemBeans;
    }

    public CustomViewBean getItemViewBean(U currListItem, String projectName) throws JDSException {
        CustomViewBean customViewBean = null;
        if (currListItem.getEuClassName() != null) {
            EUModule euModule = ESDFacrory.getAdminESDClient().getModule(currListItem.getEuClassName(), projectName);
            if (euModule != null) {
                ModuleComponent cmoduleComponent = euModule.getComponent();
                if (cmoduleComponent.getMethodAPIBean() != null) {
                    customViewBean = cmoduleComponent.getMethodAPIBean().getView();
                }
            }
        }
        return customViewBean;
    }

    protected void initBaseTabViews(Class clazz) {
        if (clazz.isEnum()) {
            this.setEnumClass((Class<? extends Enum>) clazz);
        }
        EnumsClass enums = AnnotationUtil.getClassAnnotation(clazz, EnumsClass.class);
        if (enums != null && enums.clazz() != null) {
            this.setEnumClass(enums.clazz());
            this.enumsClassBean = new EnumsClassBean(enums.clazz());
        }


        TabsEvent tabsEvent = AnnotationUtil.getClassAnnotation(clazz, TabsEvent.class);
        if (tabsEvent != null) {
            TabsEventBean customTabsEvent= new TabsEventBean(tabsEvent);
            this.extAPIEvent.add(customTabsEvent);
        }

        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            TabItemAnnotation tabItemAnnotation = AnnotationUtil.getConstructorAnnotation(constructor, TabItemAnnotation.class);
            if (tabItemAnnotation != null) {
                Class[] bindClass = tabItemAnnotation.bindClass();

                if (!tabItemAnnotation.customItems().equals(TabItem.class)) {
                    TabItem[] tabItems = EnumsUtil.getEnums(tabItemAnnotation.customItems());
                    int k = 0;
                    for (TabItem tabItem : tabItems) {
                        TabItemBean oitemBean = new TabItemBean(constructor, this, tabItem, k);
                        TabItemBean itemBean = this.getTabItemBeanById(oitemBean.getId());
                        if (itemBean == null) {
                            itemBeans.add(oitemBean);
                        } else {
                            itemBean.update(oitemBean);
                        }
                        k = k + 1;
                    }
                } else if (tabItemAnnotation.bindClass().length > 0) {
                    try {
                        MethodConfig editorMethod = this.findMethodByEvent(CustomTabsEvent.TABEDITOR, bindClass);

                        if (editorMethod != null) {
                            TabItemBean oitemBean = new TabItemBean(constructor, this);
                            TabItemBean itemBean = this.getTabItemBeanById(oitemBean.getId());
                            if (itemBean == null) {
                                itemBeans.add(oitemBean);
                            } else {
                                itemBean.update(oitemBean);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    TabItemBean oitemBean = new TabItemBean(constructor, this);
                    TabItemBean itemBean = this.getTabItemBeanById(oitemBean.getId());
                    if (itemBean == null) {
                        itemBeans.add(oitemBean);
                    } else {
                        itemBean.update(oitemBean);
                    }
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


        for (FieldModuleConfig fieldModuleConfig : fieldModuleConfigs) {
            MethodConfig methodConfig = fieldModuleConfig.getMethodConfig();
            TabItemAnnotation tabItemAnnotation = AnnotationUtil.getMethodAnnotation(methodConfig.getMethod(), TabItemAnnotation.class);
            if (tabItemAnnotation != null) {
                Class[] bindClass = tabItemAnnotation.bindClass();
                if (!tabItemAnnotation.customItems().equals(TabItem.class)) {
                    TabItem[] tabItems = EnumsUtil.getEnums(tabItemAnnotation.customItems());
                    int k = 0;
                    for (TabItem tabItem : tabItems) {
                        TabItemBean oitemBean = new TabItemBean(methodConfig, this, tabItem, k);
                        TabItemBean itemBean = this.getTabItemBeanById(oitemBean.getId());
                        if (itemBean == null) {
                            itemBeans.add(oitemBean);
                        } else {
                            itemBean.update(oitemBean);
                        }
                        k = k + 1;

                    }
                } else if (bindClass.length > 0) {
                    try {
                        MethodConfig editorMethod = this.findMethodByEvent(CustomTabsEvent.TABEDITOR, bindClass);
                        if (editorMethod != null) {
                            TabItemBean oitemBean = new TabItemBean(methodConfig, this);
                            TabItemBean itemBean = this.getTabItemBeanById(oitemBean.getId());
                            if (itemBean == null) {
                                itemBeans.add(oitemBean);
                            } else {
                                itemBean.update(oitemBean);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    TabItemBean oitemBean = new TabItemBean(methodConfig, this);
                    TabItemBean itemBean = this.getTabItemBeanById(oitemBean.getId());
                    if (itemBean == null) {
                        itemBeans.add(oitemBean);
                    } else {
                        itemBean.update(oitemBean);
                    }
                }

            } else if (methodConfig.isModule()) {
                TabItemBean oitemBean = new TabItemBean(methodConfig, this);
                TabItemBean itemBean = this.getTabItemBeanById(oitemBean.getId());
                if (itemBean == null) {
                    itemBeans.add(oitemBean);
                } else {
                    itemBean.update(oitemBean);
                }
            }
        }

        if (itemBeans == null || itemBeans.isEmpty()) {
            itemBeans = new ArrayList<>();
            Class<? extends Enum> enumClass = this.getEnumClass();
            Class<? extends Enum> viewClass = null;
            String viewClassName = this.getViewClassName();
            if (viewClassName != null) {
                try {
                    viewClass = ClassUtility.loadClass(viewClassName);
                    if (enumClass.isEnum()) {
                        viewClass = enumClass;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            List<TabListItem> tabListItems = ESDEnumsUtil.getEnumItems(viewClass, TabListItem.class);
            for (TabListItem listItem : tabListItems) {
                itemBeans.add(new TabItemBean(listItem));
            }
        }


        try {
            this.initHiddenField(clazz.getName());
        } catch (JDSException e) {
            e.printStackTrace();
        }

    }

    protected U findTabItem(String target) {
        return this.findTabItem(target, tabItems);
    }

    public U findTabItem(String target, List<U> navTabItems) {
        if (target == null) {
            return navTabItems.get(0);
        } else {
            for (U tabItem : navTabItems) {
                if (target.equals(tabItem.getId())) {
                    return tabItem;
                }
            }
        }
        return null;
    }

    public LinkedHashSet<TabsEventBean> getExtAPIEvent() {
        return extAPIEvent;
    }

    public void setExtAPIEvent(LinkedHashSet<TabsEventBean> extAPIEvent) {
        this.extAPIEvent = extAPIEvent;
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

        if (fields.isEmpty() && this.methodConfig != null) {
            try {
                Object object = invokMethod(this.methodConfig.getRequestMethodBean(), tagMap);
                if (object != null) {
                    if (object instanceof TreeListResultModel) {
                        TreeListResultModel<List<TreeListItem>> resultModel = (TreeListResultModel) object;
                        List<TreeListItem> items = resultModel.getData();
                        if (items != null) {
                            for (TreeListItem item : items) {
                                FieldModuleConfig fieldFormConfig = new FieldModuleConfig();
                                fieldFormConfig.setId(item.getId());
                                fieldFormConfig.setSourceClassName(sourceClassName);
                                fieldFormConfig.setMethodName(methodName);
                                fieldFormConfig.setViewClassName(viewClassName);
                                fieldFormConfig.setDomainId(domainId);
                                fieldFormConfig.setFieldname(item.getId());
                                fieldFormConfig.setCaption(item.getCaption());
                                fieldFormConfig.setImageClass(item.getImageClass());
                                fieldFormConfig.setEuClassName(item.getEuClassName());
                                fieldFormConfig.setTagVar(item.getTagVar());
                                fields.add(fieldFormConfig);
                            }
                        }

                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fields;
    }


    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet = super.getOtherClass();
        List<TabItemBean> itemBeans = this.getItemBeans();
        for (TabItemBean itemBean : itemBeans) {
            List<MethodConfig> methodConfigs = itemBean.getMethodConfigList();
            for (MethodConfig methodConfig : methodConfigs) {
                if (methodConfig != null) {
                    classSet.addAll(methodConfig.getOtherClass());
                }
            }
            if (itemBean.getBindClass() != null) {
                for (Class clazz : itemBean.getBindClass()) {
                    classSet.add(clazz);
                }
            }
        }
        return classSet;
    }


    @JSONField(serialize = false)
    public TabItemBean getChildTabBean(Constructor constructor) {
        List<TabItemBean> childTabViewBeans = this.getItemBeans();
        for (TabItemBean childTabViewBean : childTabViewBeans) {
            try {
                Constructor sourceConstructor = childTabViewBean.getConstructorBean().getSourceConstructor();
                if (sourceConstructor != null && constructor.equals(sourceConstructor)) {
                    return childTabViewBean;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @JSONField(serialize = false)
    public TabItemBean getChildTabBean(TabItem tabItem) {
        List<TabItemBean> childTabViewBeans = this.getItemBeans();
        for (TabItemBean childTabViewBean : childTabViewBeans) {
            if (childTabViewBean.getTabItem() != null && childTabViewBean.getTabItem().getType().equals(tabItem.getType())) {
                return childTabViewBean;

            }
        }
        return null;
    }

    @JSONField(serialize = false)
    public TabItemBean getChildTabBean(String id) {
        List<TabItemBean> childTabViewBeans = this.getItemBeans();
        for (TabItemBean childTabViewBean : childTabViewBeans) {
            if (childTabViewBean.getId() != null && childTabViewBean.getId().equals(id)) {
                return childTabViewBean;

            }
        }
        return null;
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

    public Boolean getActiveLast() {
        return activeLast;
    }

    public void setActiveLast(Boolean activeLast) {
        this.activeLast = activeLast;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(Integer maxHeight) {
        this.maxHeight = maxHeight;
    }

    public SelModeType getSelMode() {
        return selMode;
    }

    public void setSelMode(SelModeType selMode) {
        this.selMode = selMode;
    }

    public Boolean getLazyAppend() {
        return lazyAppend;
    }

    public void setLazyAppend(Boolean lazyAppend) {
        this.lazyAppend = lazyAppend;
    }

    public Boolean getFormField() {
        return formField;
    }

    public void setFormField(Boolean formField) {
        this.formField = formField;
    }

    public Boolean getNoHandler() {
        return noHandler;
    }

    public void setNoHandler(Boolean noHandler) {
        this.noHandler = noHandler;
    }

    public Boolean getAnimCollapse() {
        return animCollapse;
    }

    public void setAnimCollapse(Boolean animCollapse) {
        this.animCollapse = animCollapse;
    }

    public Boolean getDynDestory() {
        return dynDestory;
    }

    public void setDynDestory(Boolean dynDestory) {
        this.dynDestory = dynDestory;
    }

    public Boolean getTogglePlaceholder() {
        return togglePlaceholder;
    }

    public void setTogglePlaceholder(Boolean togglePlaceholder) {
        this.togglePlaceholder = togglePlaceholder;
    }

    public TagCmdsAlign getTagCmdsAlign() {
        return tagCmdsAlign;
    }

    public void setTagCmdsAlign(TagCmdsAlign tagCmdsAlign) {
        this.tagCmdsAlign = tagCmdsAlign;
    }

    public BorderType getBorderType() {
        return borderType;
    }

    public void setBorderType(BorderType borderType) {
        this.borderType = borderType;
    }

    public Boolean getGroup() {
        return group;
    }

    public void setGroup(Boolean group) {
        this.group = group;
    }

    public Boolean getAutoSave() {
        if (autoSave == null) {
            autoSave = false;
        }
        return autoSave;
    }

    public void setAutoSave(Boolean autoSave) {
        this.autoSave = autoSave;
    }

    public String getOptBtn() {
        return optBtn;
    }

    public void setOptBtn(String optBtn) {
        this.optBtn = optBtn;
    }

    public String getValueSeparator() {
        return valueSeparator;
    }

    public void setValueSeparator(String valueSeparator) {
        this.valueSeparator = valueSeparator;
    }

    public Boolean getSingleOpen() {
        return singleOpen;
    }

    public void setSingleOpen(Boolean singleOpen) {
        this.singleOpen = singleOpen;
    }

    public BarLocationType getBarLocation() {
        return barLocation;
    }

    public void setBarLocation(BarLocationType barLocation) {
        this.barLocation = barLocation;
    }

    public HAlignType getBarHAlign() {
        return barHAlign;
    }

    public void setBarHAlign(HAlignType barHAlign) {
        this.barHAlign = barHAlign;
    }

    public VAlignType getBarVAlign() {
        return barVAlign;
    }

    public void setBarVAlign(VAlignType barVAlign) {
        this.barVAlign = barVAlign;
    }

    public String getBarSize() {
        return barSize;
    }

    public void setBarSize(String barSize) {
        this.barSize = barSize;
    }

    public String getSideBarSize() {
        return sideBarSize;
    }

    public void setSideBarSize(String sideBarSize) {
        this.sideBarSize = sideBarSize;
    }


    public Boolean getAutoIconColor() {
        return autoIconColor;
    }

    public void setAutoIconColor(Boolean autoIconColor) {
        this.autoIconColor = autoIconColor;
    }

    public Boolean getAutoItemColor() {
        return autoItemColor;
    }

    public void setAutoItemColor(Boolean autoItemColor) {
        this.autoItemColor = autoItemColor;
    }

    public Boolean getAutoFontColor() {
        return autoFontColor;
    }

    public void setAutoFontColor(Boolean autoFontColor) {
        this.autoFontColor = autoFontColor;
    }

    public SideBarStatusType getSideBarStatus() {
        return sideBarStatus;
    }

    public void setSideBarStatus(SideBarStatusType sideBarStatus) {
        this.sideBarStatus = sideBarStatus;
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

    public List<CustomFormMenu> getBottombarMenu() {
        return bottombarMenu;
    }

    public void setBottombarMenu(List<CustomFormMenu> bottombarMenu) {
        this.bottombarMenu = bottombarMenu;
    }

    public List<CustomFormMenu> getCustomMenu() {
        return customMenu;
    }

    public void setCustomMenu(List<CustomFormMenu> customMenu) {
        this.customMenu = customMenu;
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

    public abstract Set<E> getEvent();


    @Override
    public BottomBarMenuBean getBottomBar() {
        if (bottomBar == null) {
            if (bottombarMenu != null && bottombarMenu.size() > 0) {
                this.bottomBar = AnnotationUtil.fillDefaultValue(BottomBarMenu.class, new BottomBarMenuBean());
            }
        }
        return bottomBar;
    }


    public EnumsClassBean getEnumsClassBean() {
        return enumsClassBean;
    }

    public void setEnumsClassBean(EnumsClassBean enumsClassBean) {
        this.enumsClassBean = enumsClassBean;
    }

    public Boolean getCloseBtn() {
        return closeBtn;
    }

    public void setCloseBtn(Boolean closeBtn) {
        this.closeBtn = closeBtn;
    }

    public Boolean getPopBtn() {
        return popBtn;
    }

    public void setPopBtn(Boolean popBtn) {
        this.popBtn = popBtn;
    }

    public Boolean getAutoReload() {
        return autoReload;
    }

    public void setAutoReload(Boolean autoReload) {
        this.autoReload = autoReload;
    }

    public Boolean getIniFold() {
        return iniFold;
    }

    public void setIniFold(Boolean iniFold) {
        this.iniFold = iniFold;
    }
}
