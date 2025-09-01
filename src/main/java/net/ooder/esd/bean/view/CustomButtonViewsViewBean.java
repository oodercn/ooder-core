package net.ooder.esd.bean.view;


import com.alibaba.fastjson.JSON;
import net.ooder.annotation.AnnotationType;
import net.ooder.common.JDSException;
import net.ooder.common.util.CaselessStringKeyHashMap;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.ButtonViewsAnnotation;
import net.ooder.esd.annotation.event.CustomTabsEvent;
import net.ooder.esd.annotation.field.TabItem;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.nav.TabItemBean;
import net.ooder.esd.tool.component.ModulePlaceHolder;
import net.ooder.esd.custom.properties.ButtonViewsListItem;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.gen.view.GenTabsChildModule;
import net.ooder.esd.dsm.java.AggRootBuild;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.DSMProperties;
import net.ooder.esd.tool.component.ButtonViewsComponent;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.ButtonViewsProperties;
import net.ooder.esd.util.ESDEnumsUtil;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.RemoteConnectionManager;
import net.ooder.web.util.AnnotationUtil;
import net.ooder.web.util.JSONGenUtil;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


@AnnotationType(clazz = ButtonViewsAnnotation.class)
public class CustomButtonViewsViewBean extends BaseTabsViewBean<CustomTabsEvent, ButtonViewsListItem> {

    ModuleViewType moduleViewType = ModuleViewType.NAVBUTTONVIEWSCONFIG;

    Set<CustomTabsEvent> event = new HashSet<>();

    public CustomButtonViewsViewBean() {
        super();
    }

    public CustomButtonViewsViewBean(ModuleComponent<ButtonViewsComponent> moduleComponent) {
        AnnotationUtil.fillDefaultValue(ButtonViewsAnnotation.class, this);
        updateModule(moduleComponent);
    }


    public CustomButtonViewsViewBean(MethodConfig methodAPIBean) {
        super(methodAPIBean);
        Class clazz = JSONGenUtil.getInnerReturnType(methodAPIBean.getMethod());
        this.init(clazz);
        ButtonViewsAnnotation formAnnotation = null;
        if (methodAPIBean.getViewClass() != null) {
            formAnnotation = AnnotationUtil.getClassAnnotation(methodAPIBean.getViewClass().getCtClass(), ButtonViewsAnnotation.class);
        }

        if (formAnnotation == null) {
            AnnotationUtil.fillDefaultValue(ButtonViewsAnnotation.class, this);
        } else {
            AnnotationUtil.fillBean(formAnnotation, this);
        }
    }


    @Override
    public List<JavaSrcBean> updateModule(ModuleComponent moduleComponent) {
        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
        super.updateBaseModule(moduleComponent);
        List<CustomModuleBean> navModuleBeans = new ArrayList<>();
        ButtonViewsComponent buttonViewsComponent = (ButtonViewsComponent) moduleComponent.getCurrComponent();
        List<Component> components = this.cloneComponentList(buttonViewsComponent.getChildren());
        DSMProperties dsmProperties = moduleComponent.getProperties().getDsmProperties();
        if (dsmProperties != null) {
            this.domainId = dsmProperties.getDomainId();
        }
        ButtonViewsProperties buttonViewsProperties = buttonViewsComponent.getProperties();
        this.init(buttonViewsProperties);
        List<GenTabsChildModule> tasks = new ArrayList<GenTabsChildModule>();
        if (components != null && components.size() > 0) {
            for (Component childComponent : components) {
                if (!(childComponent instanceof ModulePlaceHolder)) {
                    GenTabsChildModule genChildModule = new GenTabsChildModule(moduleComponent, childComponent, this);
                    tasks.add(genChildModule);
                }
            }
            try {
                List<Future<CustomModuleBean>> futures = RemoteConnectionManager.getStaticConntction(this.getXpath()).invokeAll(tasks);
                for (Future<CustomModuleBean> resultFuture : futures) {
                    try {
                        CustomModuleBean cModuleBean = resultFuture.get();
                        if (navModuleBeans != null && !navModuleBeans.contains(cModuleBean)) {
                            navModuleBeans.add(cModuleBean);
                            AggRootBuild aggRootBuild = BuildFactory.getInstance().getAggRootBuild(cModuleBean.getViewBean(), cModuleBean.getEuClassName(), cModuleBean.getPackageName());
                            if (aggRootBuild != null) {
                                javaSrcBeans.addAll(aggRootBuild.getAllSrcBean());
                            }
                        }
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            RemoteConnectionManager.getStaticConntction(this.getXpath()).shutdown();
            this.setTabItems(tabItems);
            buttonViewsProperties.setItems(tabItems);
        }
        this.setModuleBeans(navModuleBeans);
        addChildJavaSrc(javaSrcBeans);
        try {
            DSMFactory.getInstance().saveCustomViewBean(this);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return javaSrcBeans;
    }


    private void init(ButtonViewsProperties buttonViewsProperties) {
        this.name = buttonViewsProperties.getName();
        itemConfigMap = new CaselessStringKeyHashMap<>();
        itemNames = new LinkedHashSet<String>();
        if (tabItems == null || tabItems.isEmpty()) {
            tabItems = buttonViewsProperties.getItems();
        }
        try {
            String projectName = DSMFactory.getInstance().getDefaultProjectName();
            for (ButtonViewsListItem layoutItem : buttonViewsProperties.getItems()) {
                initTabItems(layoutItem, projectName);
            }
            OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(buttonViewsProperties), Map.class), this, false, false);
            tabItems = buttonViewsProperties.getItems();
            for (ButtonViewsListItem buttonViewsListItem : tabItems) {
                TabItemBean itemBean = new TabItemBean(buttonViewsListItem);
                if (!itemBeans.contains(itemBean)) {
                    itemBeans.add(itemBean);
                }
            }
            initHiddenField(this.getViewClassName());
        } catch (JDSException e) {
            e.printStackTrace();
        }


    }


    void init(Class<? extends TabItem> clazz) {
        ButtonViewsAnnotation tabsAnnotation = AnnotationUtil.getClassAnnotation(clazz, ButtonViewsAnnotation.class);
        if (tabsAnnotation != null) {
            fillData(tabsAnnotation);
        } else {
            AnnotationUtil.fillDefaultValue(ButtonViewsAnnotation.class, this);
        }
        this.initBaseTabViews(clazz);
        if (itemBeans == null || itemBeans.isEmpty()) {
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

            List<ButtonViewsListItem> items = new ArrayList<>();
            if (viewClass != null) {
                items = ESDEnumsUtil.getEnumItems(viewClass, ButtonViewsListItem.class);
            } else if (enumClass != null) {
                items = ESDEnumsUtil.getEnumItems(enumClass, ButtonViewsListItem.class);
            }

            for (ButtonViewsListItem buttonViewsListItem : items) {
                TabItemBean itemBean = this.getTabItemBeanById(buttonViewsListItem.getId());
                if (itemBean == null) {
                    itemBean = new TabItemBean(buttonViewsListItem);
                    itemBeans.add(itemBean);
                } else {
                    itemBean.update(buttonViewsListItem);
                }
            }

        }
    }


    @Override
    public ComponentType getComponentType() {
        return ComponentType.BUTTONVIEWS;
    }


    public CustomButtonViewsViewBean fillData(ButtonViewsAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    @Override
    public Set<CustomTabsEvent> getEvent() {
        return event;
    }

    public void setEvent(Set<CustomTabsEvent> event) {
        this.event = event;
    }

    @Override
    public ModuleViewType getModuleViewType() {
        return moduleViewType;
    }

    public void setModuleViewType(ModuleViewType moduleViewType) {
        this.moduleViewType = moduleViewType;
    }


    public List<ButtonViewsListItem> getTabItems() {
        return tabItems;
    }

    public void setTabItems(List<ButtonViewsListItem> tabItems) {
        this.tabItems = tabItems;
    }

}
