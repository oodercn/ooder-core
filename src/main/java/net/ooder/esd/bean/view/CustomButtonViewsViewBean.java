package net.ooder.esd.bean.view;


import com.alibaba.fastjson.JSON;
import net.ooder.annotation.AnnotationType;
import net.ooder.common.JDSException;
import net.ooder.common.util.CaselessStringKeyHashMap;
import net.ooder.esd.annotation.ButtonViewsAnnotation;
import net.ooder.esd.annotation.event.CustomTabsEvent;
import net.ooder.esd.annotation.field.TabItem;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.CustomViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.nav.TabItemBean;
import net.ooder.esd.custom.properties.ButtonViewsListItem;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.gen.view.GenTabsChildModule;
import net.ooder.esd.dsm.java.JavaGenSource;
import net.ooder.esd.tool.DSMProperties;
import net.ooder.esd.tool.component.ButtonViewsComponent;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.ModulePlaceHolder;
import net.ooder.esd.tool.properties.ButtonViewsProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;
import net.ooder.web.util.JSONGenUtil;

import java.util.*;
import java.util.concurrent.Callable;

@AnnotationType(clazz = ButtonViewsAnnotation.class)
public class CustomButtonViewsViewBean extends BaseTabsViewBean<CustomTabsEvent, ButtonViewsListItem> {

    ModuleViewType moduleViewType = ModuleViewType.NAVBUTTONVIEWSCONFIG;

    Boolean noFoldBar;

    Set<CustomTabsEvent> event = new HashSet<>();


    public List<ButtonViewsListItem> tabItems = new ArrayList<>();


    public CustomButtonViewsViewBean() {
        super();
    }

    public CustomButtonViewsViewBean(ModuleComponent<ButtonViewsComponent> moduleComponent) {
        AnnotationUtil.fillDefaultValue(ButtonViewsAnnotation.class, this);
        childModules = this.updateModule(moduleComponent);
    }


    public CustomButtonViewsViewBean(MethodConfig methodAPIBean) {
        super(methodAPIBean);
        Class clazz = JSONGenUtil.getInnerReturnType(methodAPIBean.getMethod());
        if (methodAPIBean.getViewClass() != null) {
            clazz = methodAPIBean.getViewClass().getCtClass();
        }
        this.init(clazz);
    }


    public List<JavaGenSource> buildAll() {
        List<JavaGenSource> allSourceList = new ArrayList<>();
        List<Callable<List<JavaGenSource>>> callableList = new ArrayList<>();
        for (Callable childModule : childModules) {
            GenTabsChildModule genFormChildModule = (GenTabsChildModule) childModule;
            callableList.add(childModule);
            CustomViewBean customViewBean = genFormChildModule.getCustomViewBean();
            allSourceList.addAll(customViewBean.buildAll());
        }
        List<JavaGenSource> sourceList = build(callableList);
        allSourceList.addAll(sourceList);
        return allSourceList;

    }

    @Override
    public List<Callable<List<JavaGenSource>>> updateModule(ModuleComponent moduleComponent) {
        super.updateBaseModule(moduleComponent);
        List<Callable<List<JavaGenSource>>> tasks = new ArrayList();
        List<CustomModuleBean> navModuleBeans = new ArrayList<>();
        ButtonViewsComponent buttonViewsComponent = (ButtonViewsComponent) moduleComponent.getCurrComponent();
        List<Component> components = this.cloneComponentList(buttonViewsComponent.getChildren());
        DSMProperties dsmProperties = moduleComponent.getProperties().getDsmProperties();
        if (dsmProperties != null) {
            this.domainId = dsmProperties.getDomainId();
        }
        ButtonViewsProperties buttonViewsProperties = buttonViewsComponent.getProperties();
        this.init(buttonViewsProperties);
        if (components != null && components.size() > 0) {
            for (Component childComponent : components) {
                ModuleViewType comModuleViewType = ModuleViewType.getModuleViewByCom(ComponentType.fromType(childComponent.getKey()));
                if (!(childComponent instanceof ModulePlaceHolder) && !comModuleViewType.equals(ModuleViewType.NONE)) {
                    GenTabsChildModule genChildModule = new GenTabsChildModule(moduleComponent, childComponent, this);
                    tasks.add(genChildModule);
                    navModuleBeans.add(genChildModule.getCmoduleBean());
                }
            }

            this.setTabItems(tabItems);
            buttonViewsProperties.setItems(tabItems);
        }
        childModules = tasks;
        this.setModuleBeans(navModuleBeans);
        return tasks;
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

    }

    public Boolean getNoFoldBar() {
        return noFoldBar;
    }

    public void setNoFoldBar(Boolean noFoldBar) {
        this.noFoldBar = noFoldBar;
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
