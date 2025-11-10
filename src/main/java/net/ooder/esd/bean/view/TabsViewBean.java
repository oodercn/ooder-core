package net.ooder.esd.bean.view;

import com.alibaba.fastjson.JSON;
import net.ooder.annotation.AnnotationType;
import net.ooder.common.JDSException;
import net.ooder.common.util.CaselessStringKeyHashMap;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.TabsAnnotation;
import net.ooder.esd.annotation.event.CustomTabsEvent;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.ui.PosType;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.nav.TabItemBean;
import net.ooder.esd.custom.properties.NavTabListItem;
import net.ooder.esd.custom.properties.NavTabsProperties;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.gen.view.GenTabsChildModule;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.*;
import net.ooder.esd.tool.properties.item.TabListItem;
import net.ooder.esd.util.ESDEnumsUtil;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.RemoteConnectionManager;
import net.ooder.web.util.AnnotationUtil;
import net.ooder.web.util.JSONGenUtil;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@AnnotationType(clazz = TabsAnnotation.class)
public class TabsViewBean<U extends NavTabListItem> extends BaseTabsViewBean<CustomTabsEvent, U> {

    ModuleViewType moduleViewType = ModuleViewType.NAVTABSCONFIG;

    Set<CustomTabsEvent> event = new HashSet<>();

    public TabsViewBean() {
        super();

    }

    public TabsViewBean(Class clazz, CustomTreeViewBean parentItem) {
        this.domainId = parentItem.getDomainId();
        init(clazz);
    }

    public TabsViewBean(Class<? extends TabListItem> clazz) {
        init(clazz);
    }

    public TabsViewBean(MethodConfig methodAPIBean) {
        super(methodAPIBean);
        Class clazz = JSONGenUtil.getInnerReturnType(methodAPIBean.getMethod());
        this.init(clazz);
    }

    public TabsViewBean(Class clazz, TabsViewBean parentItem) {
        this.domainId = parentItem.getDomainId();
        this.viewClassName = parentItem.getViewClassName();
        this.sourceClassName = parentItem.getSourceClassName();
        init(clazz);
    }


    void init(Class clazz) {
        TabsAnnotation tabsAnnotation = AnnotationUtil.getClassAnnotation(clazz, TabsAnnotation.class);
        if (tabsAnnotation != null) {
            fillData(tabsAnnotation);
        } else {
            AnnotationUtil.fillDefaultValue(TabsAnnotation.class, this);
        }
        this.initBaseTabViews(clazz);

    }

    public TabsViewBean(ModuleComponent moduleComponent) {
        super();
        AnnotationUtil.fillDefaultValue(TabsAnnotation.class, this);
        this.updateModule(moduleComponent);
    }


    public List<JavaSrcBean> updateModule(ModuleComponent moduleComponent) {
        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
        super.updateBaseModule(moduleComponent);
        List<CustomModuleBean> navModuleBeans = new ArrayList<>();
        Component currComponent = moduleComponent.getCurrComponent();
        TabsComponent tabsComponent = null;
        if (currComponent instanceof LayoutComponent) {
            LayoutComponent layoutComponent = (LayoutComponent) moduleComponent.getCurrComponent();
            for (Component component : layoutComponent.getChildren()) {
                if (component.getTarget() != null && component.getTarget().equals(PosType.main.name())) {
                    if (component instanceof TabsComponent) {
                        tabsComponent = (TabsComponent) component;
                    } else {
                        tabsComponent = new TabsComponent();
                        tabsComponent.addChildComponet(component);
                    }
                }
            }
            if (tabsComponent == null) {
                tabsComponent = new TabsComponent();
                tabsComponent.setTarget(PosType.main.name());
                currComponent.addChildren(tabsComponent);
            }


        } else if (currComponent instanceof TabsComponent) {
            tabsComponent = (TabsComponent) currComponent;
        } else {
            for (Component component : currComponent.getChildrenRecursivelyList()) {
                if (component instanceof TabsComponent) {
                    tabsComponent = (TabsComponent) component;
                }
            }
        }

        NavTabsProperties tabsProperties = (NavTabsProperties) tabsComponent.getProperties();
        this.initProperties(tabsProperties);
        List<Component> components = cloneComponentList(tabsComponent.getChildren());
        if (tabItems == null || tabItems.isEmpty()) {
            tabItems = (List<U>) tabsProperties.getItems();
        }

        List<GenTabsChildModule> tasks = new ArrayList<GenTabsChildModule>();
        if (components != null && components.size() > 0) {
            for (Component childComponent : components) {
                ModuleViewType comModuleViewType = ModuleViewType.getModuleViewByCom(ComponentType.fromType(childComponent.getKey()));
                if (!(childComponent instanceof ModulePlaceHolder) && !comModuleViewType.equals(ModuleViewType.NONE)) {
                    GenTabsChildModule genChildModule = new GenTabsChildModule(moduleComponent, childComponent, this);
                    tasks.add(genChildModule);
                }
            }
            try {
                ExecutorService service = RemoteConnectionManager.createConntctionService(this.getXpath());
                List<Future<CustomModuleBean>> futures = service.invokeAll(tasks);
                for (Future<CustomModuleBean> resultFuture : futures) {
                    try {
                        CustomModuleBean cModuleBean = resultFuture.get();
                        if (navModuleBeans != null && !navModuleBeans.contains(cModuleBean)) {
                            navModuleBeans.add(cModuleBean);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                service.shutdownNow();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        this.setModuleBeans(navModuleBeans);
        this.setTabItems(tabItems);
        tabsProperties.setItems(tabItems);
        this.initProperties(tabsProperties);
        try {
            DSMFactory.getInstance().saveCustomViewBean(this);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return javaSrcBeans;
    }


    public <N extends NavTabsProperties> void initProperties(N tabsProperties) {
        Map valueMap = AnnotationUtil.getDefaultAnnMap(TabsAnnotation.class);
        valueMap.putAll(JSON.parseObject(JSON.toJSONString(tabsProperties), Map.class));
        OgnlUtil.setProperties(valueMap, this, false, false);
        this.name = tabsProperties.getName();
        itemConfigMap = new CaselessStringKeyHashMap<>();
        itemNames = new LinkedHashSet<String>();
        tabItems = (List<U>) tabsProperties.getItems();
        moduleBeans = this.getModuleBeans();
        try {
            String projectName = DSMFactory.getInstance().getDefaultProjectName();
            List<NavTabListItem> layoutItems = tabsProperties.getItems();
            for (TabListItem layoutItem : layoutItems) {
                initTabItems(layoutItem, projectName);
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }
        itemBeans = this.getItemBeans();
        tabItems = (List<U>) tabsProperties.getItems();
        for (TabListItem tabListItem : tabItems) {
            TabItemBean itemBean = this.getTabItemBeanById(tabListItem.getId());
            if (itemBean == null) {
                itemBean = new TabItemBean(tabListItem);
                itemBeans.add(itemBean);
            } else {
                itemBean.update(tabListItem);
            }
        }
    }

    @Override
    public List<U> getTabItems() {
        if (tabItems == null || tabItems.isEmpty()) {
            Class<? extends Enum> enumClass = getEnumClass();
            Class<? extends Enum> viewClass = null;
            String viewClassName = getViewClassName();
            if (viewClassName != null) {
                Class clazz = null;
                try {
                    clazz = ClassUtility.loadClass(viewClassName);
                    if (clazz.isEnum()) {
                        viewClass = clazz;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            if (viewClass != null) {
                tabItems = (List<U>) ESDEnumsUtil.getEnumItems(viewClass, NavTabListItem.class);
            } else if (enumClass != null) {
                tabItems = (List<U>) ESDEnumsUtil.getEnumItems(enumClass, NavTabListItem.class);
            }
            for (TabListItem tabListItem : tabItems) {
                MethodConfig editorMethod = findEditorMethod(tabListItem.getBindClass());
                if (editorMethod != null) {
                    tabListItem.setEuClassName(editorMethod.getEUClassName());
                }
            }
        }
        return tabItems;
    }


    @Override
    public Set<CustomTabsEvent> getEvent() {
        return event;
    }

    public void setEvent(Set<CustomTabsEvent> event) {
        this.event = event;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.TABS;
    }


    public TabsViewBean fillData(TabsAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    @Override
    public ModuleViewType getModuleViewType() {
        return moduleViewType;
    }

    public void setModuleViewType(ModuleViewType moduleViewType) {
        this.moduleViewType = moduleViewType;
    }

}