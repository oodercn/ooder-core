package net.ooder.esd.bean.field;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.TabsFieldAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.BaseWidgetBean;
import net.ooder.esd.bean.nav.TabItemBean;
import net.ooder.esd.bean.view.TabsViewBean;
import net.ooder.esd.custom.component.form.field.CustomFieldTabsComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.ButtonViewsComponent;
import net.ooder.esd.tool.component.TabsComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.TabsProperties;
import net.ooder.esd.tool.properties.item.TabListItem;
import net.ooder.annotation.AnnotationType;
import net.ooder.esd.tool.properties.list.AbsListProperties;
import net.ooder.esd.tool.properties.list.AbsUIListProperties;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@CustomClass(clazz = CustomFieldTabsComponent.class,
        viewType = CustomViewType.COMPONENT,
        moduleType = ModuleViewType.NAVTABSCONFIG,
        componentType = ComponentType.TABS
)
@AnnotationType(clazz = TabsFieldAnnotation.class)
public class CustomTabsFieldBean extends BaseWidgetBean<TabsViewBean, TabsComponent> {

    public CustomTabsFieldBean() {

    }


    public CustomTabsFieldBean(TabsComponent tabsComponent) {
        update(null, tabsComponent);
    }


    public CustomTabsFieldBean(ModuleComponent parentModuleComponent, TabsComponent tabsComponent) {
        update(parentModuleComponent, tabsComponent);
    }


    public CustomTabsFieldBean(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(TabsFieldAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof TabsFieldAnnotation) {
                fillData((TabsFieldAnnotation) annotation);
            }
        }
    }
    @Override
    public void update(ModuleComponent parentModuleComponent, TabsComponent component) {
        this.initWidget(parentModuleComponent,component);
        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();

        AbsListProperties listProperties= (AbsListProperties) component.getProperties();
        if (component.getChildren() != null && component.getChildren().size() > 0) {
         super.update(parentModuleComponent, component);
        } else if (listProperties.getItems().size()>0){
            this.viewBean=genViewBean();
        }


    }

    @Override
    public TabsViewBean createViewBean(ModuleComponent currModuleComponent, TabsComponent tabsComponent) {
        TabsProperties tabsProperties = (TabsProperties) tabsComponent.getProperties();
        List<TabListItem> tabsListItems = tabsProperties.getItems();
        List<TabItemBean> itemBeans = new ArrayList<>();
        for (TabListItem tabListItem : tabsListItems) {
            TabItemBean itemBean = new TabItemBean(tabListItem);
            itemBeans.add(itemBean);
        }

        if (viewBean == null) {
            viewBean = new TabsViewBean(currModuleComponent);
            viewBean.setItemBeans(itemBeans);
            viewBean.setTabItems(tabsListItems);
        } else {
            viewBean.setItemBeans(itemBeans);
            viewBean.setTabItems(tabsListItems);
            viewBean.updateModule(currModuleComponent);
        }
        viewBean.updateContainerBean(tabsComponent);
        return viewBean;

    }


    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        return new HashSet<>();
    }

    public CustomTabsFieldBean(TabsFieldAnnotation annotation) {
        fillData(annotation);
    }

    public CustomTabsFieldBean fillData(TabsFieldAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.TABS;
    }


}
