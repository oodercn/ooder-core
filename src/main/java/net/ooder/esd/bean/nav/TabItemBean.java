package net.ooder.esd.bean.nav;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.common.JDSException;
import net.ooder.esd.annotation.LayoutItemAnnotation;
import net.ooder.esd.annotation.RightContextMenu;
import net.ooder.esd.annotation.TabItemAnnotation;
import net.ooder.esd.annotation.event.CustomEvent;
import net.ooder.esd.annotation.event.CustomFormEvent;
import net.ooder.esd.annotation.event.CustomTabsEvent;
import net.ooder.esd.annotation.event.TabsEvent;
import net.ooder.esd.annotation.field.TabItem;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.CustomLayoutItemBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.RightContextMenuBean;
import net.ooder.esd.bean.bar.ContextMenuBar;
import net.ooder.esd.bean.view.BaseTabsViewBean;
import net.ooder.esd.bean.view.TabsEventBean;
import net.ooder.esd.bean.view.TabsViewBean;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.dsm.view.field.FieldItemConfig;
import net.ooder.esd.tool.properties.item.TabListItem;
import net.ooder.esd.util.json.BindClassArrDeserializer;
import net.ooder.esd.util.json.DefaultTabItem;
import net.ooder.esd.util.json.TabItemDeserializer;
import net.ooder.esd.util.json.TabItemSerializer;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.ConstructorBean;
import net.ooder.web.util.AnnotationUtil;

import java.lang.reflect.Constructor;
import java.util.*;

@AnnotationType(clazz = TabItemAnnotation.class)
public class TabItemBean<T extends FieldItemConfig> implements ContextMenuBar, Comparable<TabItemBean>, CustomBean {

    String id;

    String imageClass;

    Boolean closeBtn;

    Boolean popBtn;

    String caption;

    Boolean iniFold;

    Boolean lazyAppend;

    Boolean autoReload;

    Boolean dynDestory;

    Boolean lazyLoad;


    String className;

    Integer index = 1;

    String groupName;


    IconColorEnum iconColor;

    ItemColorEnum itemColor;

    FontColorEnum fontColor;

    Set<CustomFormEvent> event = new LinkedHashSet<>();

    RightContextMenuBean contextMenuBean;

    ConstructorBean constructorBean;

    CustomLayoutItemBean layoutItemBean;

    @JSONField(serialize = false)
    List<MethodConfig> methodConfigList = new ArrayList<>();

    @JSONField(serialize = false)
    TabsViewBean viewBean;

    @JSONField(serialize = false)
    public Set<ComponentType> bindTypes = new LinkedHashSet<>();

    @JSONField(serialize = false)
    Class fristClass;

    @JSONField(deserializeUsing = BindClassArrDeserializer.class)
    Class[] bindClass;

    public String sourceClassName;

    public String methodName;

    public String rootClassName;

    public String rootMethodName;

    public String viewClassName;

    public String domainId;


    @JSONField(deserializeUsing = TabItemDeserializer.class, serializeUsing = TabItemSerializer.class)
    public TabItem tabItem;

    public TabItemBean() {

    }


    public TabItemBean(TabListItem tabListItem) {
        update(tabListItem);
    }

    public TabItemBean(MethodConfig methodConfig, BaseTabsViewBean viewBean, TabItem tabItem, Integer index) {
        this.rootClassName = viewBean.getSourceClassName();
        this.rootMethodName = viewBean.getMethodName();
        this.methodConfigList.add(methodConfig);
        //this.methodConfig = methodConfig;
        this.domainId = viewBean.getDomainId();
        this.methodName = methodConfig.getMethodName();
        this.sourceClassName = methodConfig.getSourceClassName();
        this.viewClassName = methodConfig.getViewClassName();
        this.tabItem = new DefaultTabItem(tabItem);
        this.index = index;
        Class[] paramClass = methodConfig.getMethod().getParameterTypes();
        if (paramClass.length > 0) {
            this.fristClass = paramClass[0];
        }
        TabItemAnnotation tabViewAnnotation = AnnotationUtil.getMethodAnnotation(methodConfig.getMethod(), TabItemAnnotation.class);
        if (tabViewAnnotation != null) {
            fillData(tabViewAnnotation);
        } else {
            AnnotationUtil.fillDefaultValue(TabItemAnnotation.class, this);
        }

        TabsEvent tabsEvent = AnnotationUtil.getMethodAnnotation(methodConfig.getMethod(), TabsEvent.class);
        if (tabsEvent != null) {
            TabsEventBean customTabsEvent = new TabsEventBean(tabsEvent);
            viewBean.getExtAPIEvent().add(customTabsEvent);
        }

        LayoutItemAnnotation tabItemAnnotation = AnnotationUtil.getMethodAnnotation(methodConfig.getMethod(), LayoutItemAnnotation.class);
        if (tabItemAnnotation != null) {
            layoutItemBean = new CustomLayoutItemBean(tabItemAnnotation);
        }

        RightContextMenu annotation = AnnotationUtil.getMethodAnnotation(methodConfig.getMethod(), RightContextMenu.class);
        if (annotation != null) {
            contextMenuBean = new RightContextMenuBean(this.getId(), annotation);
        }

        if (tabItem != null) {
            this.caption = tabItem.getName();
            this.imageClass = tabItem.getImageClass();
            this.bindClass = tabItem.getBindClass();

            this.dynDestory = tabItem.isDynDestory();
            this.lazyLoad = tabItem.isDynLoad();
            this.iniFold = tabItem.isIniFold();
        }
    }


    public TabItemBean(Constructor constructor, BaseTabsViewBean viewBean, TabItem tabItem, Integer index) {
        constructorBean = new ConstructorBean(constructor);
        this.rootClassName = viewBean.getSourceClassName();
        this.rootMethodName = viewBean.getMethodName();
        this.sourceClassName = viewBean.getSourceClassName();
        this.viewClassName = viewBean.getViewClassName();
        this.methodName = viewBean.getMethodName();
        this.domainId = viewBean.getDomainId();
        this.index = index;
        this.tabItem = tabItem;
        Class[] paramClass = constructor.getParameterTypes();
        if (paramClass.length > 0) {
            this.fristClass = paramClass[0];
        }
        TabItemAnnotation tabViewAnnotation = AnnotationUtil.getConstructorAnnotation(constructor, TabItemAnnotation.class);
        if (tabViewAnnotation != null) {
            fillData(tabViewAnnotation);
        } else {
            AnnotationUtil.fillDefaultValue(TabItemAnnotation.class, this);
        }
        RightContextMenu annotation = AnnotationUtil.getConstructorAnnotation(constructor, RightContextMenu.class);
        if (annotation != null) {
            contextMenuBean = new RightContextMenuBean(this.getId(), annotation);
        }
        if (tabItem != null) {
            this.caption = tabItem.getName();
            this.imageClass = tabItem.getImageClass();
            this.bindClass = tabItem.getBindClass();
            this.dynDestory = tabItem.isDynDestory();
            this.lazyLoad = tabItem.isDynLoad();
            this.iniFold = tabItem.isIniFold();
        }


    }


    public TabItemBean(MethodConfig methodConfig, BaseTabsViewBean viewBean) {
        this.methodConfigList.add(methodConfig);
        this.methodName = methodConfig.getMethodName();
        this.id = methodName;
        this.rootClassName = viewBean.getSourceClassName();
        this.rootMethodName = viewBean.getMethodName();
        this.sourceClassName = methodConfig.getSourceClassName();
        this.viewClassName = methodConfig.getViewClassName();
        this.index = methodConfig.getIndex();
        this.domainId = viewBean.getDomainId();
        if (caption == null || !caption.equals("")) {
            this.caption = methodConfig.getCaption();
        }
        if (imageClass == null || !imageClass.equals("")) {
            this.imageClass = methodConfig.getImageClass();
        }

        TabItemAnnotation treeViewAnnotation = AnnotationUtil.getMethodAnnotation(methodConfig.getMethod(), TabItemAnnotation.class);
        if (treeViewAnnotation != null) {
            fillData(treeViewAnnotation);
        } else {
            AnnotationUtil.fillDefaultValue(TabItemAnnotation.class, this);
            this.setCloseBtn(viewBean.getCloseBtn());
            this.setIniFold(viewBean.getIniFold());
            this.setAutoReload(viewBean.getAutoReload());
            this.setPopBtn(viewBean.getPopBtn());
        }

        RightContextMenu annotation = AnnotationUtil.getMethodAnnotation(methodConfig.getMethod(), RightContextMenu.class);
        if (annotation != null) {
            contextMenuBean = new RightContextMenuBean(this.getId(), annotation);
        }

    }


    @JSONField(serialize = false)
    public MethodConfig getMethodConfig() {
        MethodConfig methodConfig = null;
        if (this.getMethodConfigList() != null && this.getMethodConfigList().size() > 0) {
            methodConfig = this.getMethodConfigList().get(0);
        }
        return methodConfig;
    }


    public TabItemBean(Constructor constructor, BaseTabsViewBean viewBean) {
        constructorBean = new ConstructorBean(constructor);
        this.rootClassName = viewBean.getSourceClassName();
        this.rootMethodName = viewBean.getMethodName();
        this.sourceClassName = viewBean.getSourceClassName();
        this.viewClassName = viewBean.getViewClassName();
        this.methodName = viewBean.getMethodName();
        this.domainId = viewBean.getDomainId();
        Class[] paramClass = constructor.getParameterTypes();
        if (paramClass.length > 0) {
            this.fristClass = paramClass[0];
        }
        TabItemAnnotation treeViewAnnotation = AnnotationUtil.getConstructorAnnotation(constructor, TabItemAnnotation.class);
        if (treeViewAnnotation != null) {
            fillData(treeViewAnnotation);
        } else {
            AnnotationUtil.fillDefaultValue(TabItemAnnotation.class, this);
        }
        RightContextMenu annotation = AnnotationUtil.getConstructorAnnotation(constructor, RightContextMenu.class);
        if (annotation != null) {
            contextMenuBean = new RightContextMenuBean(this.getId(), annotation);
        }
    }


    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        if (contextMenuBean != null && contextMenuBean.getMenuClass().length > 0) {
            annotationBeans.add(contextMenuBean);
        }
        annotationBeans.add(this);

        if (this.getMethodConfig() != null) {
            annotationBeans.addAll(this.getMethodConfig().getAnnotationBeans());
        }
        return annotationBeans;
    }

    @JSONField(serialize = false)
    public List<MethodConfig> getMethodConfigList() {
        if (methodConfigList == null || methodConfigList.isEmpty()) {
            try {
                if (bindClass != null && bindClass.length > 0) {
                    for (Class clazz : bindClass) {
                        if (clazz != null) {
                            ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(clazz.getName());
                            MethodConfig methodConfig = apiClassConfig.findEditorMethod();
                            methodConfigList.add(methodConfig);
                        }
                    }
                } else if (sourceClassName != null && !sourceClassName.equals("") && domainId != null) {
                    ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(sourceClassName);
                    if (apiClassConfig != null) {
                        MethodConfig methodConfig = apiClassConfig.getMethodByName(this.methodName);
                        methodConfigList.add(methodConfig);
                    }
                }
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        return methodConfigList;
    }


    public void update(TabListItem tabListItem) {
        this.id = tabListItem.getId();
        this.index = tabListItem.getIndex() == null ? 0 : tabListItem.getIndex();
        this.imageClass = tabListItem.getImageClass();
        this.className = tabListItem.getEuClassName();
        this.caption = tabListItem.getCaption();
        this.lazyAppend = tabListItem.getDynLoad();
        this.lazyLoad = tabListItem.getDynLoad();
        this.tabItem = new DefaultTabItem(tabListItem);
        this.popBtn = tabListItem.getPopBtn();
        this.closeBtn = tabListItem.getCloseBtn();
        this.className = tabListItem.getEuClassName();
        this.bindClass = tabListItem.getBindClass();

    }


    public void update(TabItemBean tabListItem) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(tabListItem), Map.class), this, false, false);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof TabItemBean) {
            return ((TabItemBean) obj).getId().equals(this.getId());
        }
        return super.equals(obj);
    }

    public IconColorEnum getIconColor() {
        return iconColor;
    }

    public void setIconColor(IconColorEnum iconColor) {
        this.iconColor = iconColor;
    }

    public ItemColorEnum getItemColor() {
        return itemColor;
    }

    public void setItemColor(ItemColorEnum itemColor) {
        this.itemColor = itemColor;
    }

    public FontColorEnum getFontColor() {
        return fontColor;
    }

    public void setFontColor(FontColorEnum fontColor) {
        this.fontColor = fontColor;
    }

    public String getRootClassName() {
        return rootClassName;
    }

    public void setRootClassName(String rootClassName) {
        this.rootClassName = rootClassName;
    }

    public String getRootMethodName() {
        return rootMethodName;
    }

    public void setRootMethodName(String rootMethodName) {
        this.rootMethodName = rootMethodName;
    }

    public TabsViewBean getViewBean() {
        return viewBean;
    }

    public void setViewBean(TabsViewBean viewBean) {
        this.viewBean = viewBean;
    }

    public TabItemBean fillData(TabItemAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String getSourceClassName() {
        return sourceClassName;
    }

    public void setSourceClassName(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }


    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Boolean getLazyAppend() {
        return lazyAppend;
    }

    public void setLazyAppend(Boolean lazyAppend) {
        this.lazyAppend = lazyAppend;
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

    public Boolean getIniFold() {
        return iniFold;
    }

    public void setIniFold(Boolean iniFold) {
        this.iniFold = iniFold;
    }

    public Boolean getDynDestory() {
        return dynDestory;
    }

    public void setDynDestory(Boolean dynDestory) {
        this.dynDestory = dynDestory;
    }

    public String getViewClassName() {
        return viewClassName;
    }

    public void setViewClassName(String viewClassName) {
        this.viewClassName = viewClassName;
    }

    public Boolean getAutoReload() {
        return autoReload;
    }

    public void setAutoReload(Boolean autoReload) {
        this.autoReload = autoReload;
    }


    public Set<CustomFormEvent> getEvent() {
        return event;
    }

    public void setEvent(Set<CustomFormEvent> event) {
        this.event = event;
    }

    @Override
    public RightContextMenuBean getContextMenuBean() {
        return contextMenuBean;
    }

    public void setContextMenuBean(RightContextMenuBean contextMenuBean) {
        this.contextMenuBean = contextMenuBean;
    }

    public ConstructorBean getConstructorBean() {
        return constructorBean;
    }

    public void setConstructorBean(ConstructorBean constructorBean) {
        this.constructorBean = constructorBean;
    }

    @Override
    public Set<ComponentType> getBindTypes() {
        return bindTypes;
    }

    public void setBindTypes(Set<ComponentType> bindTypes) {
        this.bindTypes = bindTypes;
    }

    public Class getFristClass() {
        return fristClass;
    }

    public void setFristClass(Class fristClass) {
        this.fristClass = fristClass;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }


    public Boolean getLazyLoad() {
        return lazyLoad;
    }

    public void setLazyLoad(Boolean lazyLoad) {
        this.lazyLoad = lazyLoad;
    }


    public MethodConfig findMethodByEvent(CustomEvent eventEnum, Class... bindClasses) {
        if (bindClasses != null) {
            Set<String> bindClassSet = new HashSet<>();
            for (Class bindClass : bindClasses) {
                if (bindClass != null && !bindClass.equals(Void.class)) {
                    bindClassSet.add(bindClass.getName());
                }
            }
            MethodConfig methodConfig = findMethodByEvent(eventEnum, bindClassSet.toArray(new String[]{}));
            if (methodConfig != null) {
                return methodConfig;
            }
        }
        return null;
    }


    public MethodConfig findMethodByEvent(CustomEvent eventEnum, String... bindClassNames) {
        MethodConfig methodConfig = null;
        if (bindClassNames != null) {
            for (String bindClassName : bindClassNames) {
                if (bindClassName != null && !bindClassName.equals("") && !bindClassName.equals(Void.class.getName()) && !bindClassName.equals(Enum.class.getName())) {
                    try {
                        ApiClassConfig config = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(bindClassName);
                        if (config != null) {
                            methodConfig = config.getMethodByEvent(eventEnum);
                        } else {
                            AggEntityConfig entityConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(bindClassName, false);
                            methodConfig = entityConfig.getMethodByEvent(eventEnum);
                        }
                        if (methodConfig != null) {
                            return methodConfig;
                        }
                    } catch (JDSException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return methodConfig;
    }


    public MethodConfig findMethodByMenuItem(CustomMenuItem menuItem, Class... bindClassList) {
        MethodConfig methodConfig = null;
        if (bindClassList != null) {
            for (Class bindClass : bindClassList) {
                if (!bindClass.equals(Void.class)) {
                    String bindClassName = bindClass.getName();
                    try {
                        ApiClassConfig config = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(bindClassName);
                        if (config != null) {
                            methodConfig = config.getMethodByItem(menuItem);
                        } else {
                            AggEntityConfig entityConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(bindClassName, false);
                            methodConfig = entityConfig.getMethodByItem(menuItem);
                        }
                        if (methodConfig != null) {
                            return methodConfig;
                        }
                    } catch (JDSException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return methodConfig;
    }

    public MethodConfig findMethodByMenuItem(CustomMenuItem menuItem, String... bindClassNames) {
        MethodConfig methodConfig = null;
        if (bindClassNames != null) {
            for (String bindClassName : bindClassNames) {
                if (bindClassName != null && !bindClassName.equals("") && !bindClassName.equals(Void.class.getName()) && !bindClassName.equals(Enum.class.getName())) {
                    try {
                        ApiClassConfig config = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(bindClassName);
                        if (config != null) {
                            methodConfig = config.getMethodByItem(menuItem);
                        } else {
                            AggEntityConfig entityConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(bindClassName, false);
                            methodConfig = entityConfig.getMethodByItem(menuItem);
                        }
                        if (methodConfig != null) {
                            return methodConfig;
                        }
                    } catch (JDSException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return methodConfig;
    }


    public void reBindService(Class... bindService) {
        this.setBindClass(bindService);
    }

    public TabItem getTabItem() {
        return tabItem;
    }

    public void setTabItem(TabItem tabItem) {
        this.tabItem = tabItem;
    }

    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getId() {
        if (id == null || id.equals("")) {
            if (this.getTabItem() != null) {
                id = this.getTabItem().getType();
            } else {
                id = this.getGroupName();
            }
        }
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }

    @JSONField(serialize = false)
    public String getGroupName() {
        if (groupName == null || groupName.equals("")) {
            Class[] bindClass = this.getBindClass();
            if (bindClass != null && bindClass.length > 0) {
                MethodConfig editorMethod = findMethodByEvent(CustomTabsEvent.TABEDITOR, bindClass);
                MethodConfig loadChildMethod = findMethodByEvent(CustomTabsEvent.TABCHILD, bindClass);
                if (editorMethod != null) {
                    groupName = editorMethod.getEUClassName();
                } else if (loadChildMethod != null) {
                    groupName = loadChildMethod.getEUClassName();
                } else {
                    if (fristClass != null) {
                        groupName = fristClass.getName();
                    }
                }

            } else if (fristClass != null) {
                groupName = fristClass.getName();
            }
        }

        return groupName;
    }

    public Class[] getBindClass() {
        return bindClass;
    }

    public void setBindClass(Class[] bindClass) {
        this.bindClass = bindClass;
    }

    public CustomLayoutItemBean getLayoutItemBean() {
        return layoutItemBean;
    }

    public void setLayoutItemBean(CustomLayoutItemBean layoutItemBean) {
        this.layoutItemBean = layoutItemBean;
    }

    public int compareTo(TabItemBean o) {
        return index - o.getIndex();
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @Override
    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
