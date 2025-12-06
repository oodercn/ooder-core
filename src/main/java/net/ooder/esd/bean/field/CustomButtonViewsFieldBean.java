package net.ooder.esd.bean.field;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.ButtonViewsFieldAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.BaseWidgetBean;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.view.CustomButtonViewsViewBean;
import net.ooder.esd.bean.nav.TabItemBean;
import net.ooder.esd.custom.component.form.field.CustomFieldButtonViewsComponent;
import net.ooder.esd.custom.properties.ButtonViewsListItem;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.ButtonLayoutComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.ButtonViewsComponent;
import net.ooder.annotation.AnnotationType;
import net.ooder.esd.tool.properties.ContentBlockProperties;
import net.ooder.esd.tool.properties.list.AbsUIListProperties;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@CustomClass(clazz = CustomFieldButtonViewsComponent.class,
        viewType = CustomViewType.COMPONENT,
        moduleType = ModuleViewType.NAVBUTTONVIEWSCONFIG,
        componentType = ComponentType.BUTTONVIEWS
)
@AnnotationType(clazz = ButtonViewsFieldAnnotation.class)
public class CustomButtonViewsFieldBean extends BaseWidgetBean<CustomButtonViewsViewBean, ButtonViewsComponent> {


    public CustomButtonViewsFieldBean() {

    }

    public CustomButtonViewsFieldBean(ModuleComponent parentModuleComponent, ButtonViewsComponent buttonViewsComponent) {

        update(parentModuleComponent, buttonViewsComponent);
    }


    @Override
    public CustomButtonViewsViewBean createViewBean(ModuleComponent currModuleComponent, ButtonViewsComponent buttonViewsComponent) {
        List<TabItemBean> itemBeans = new ArrayList<>();
        List<ButtonViewsListItem> buttonViewsListItems = buttonViewsComponent.getProperties().getItems();
        for (ButtonViewsListItem buttonViewsListItem : buttonViewsListItems) {
            TabItemBean itemBean = new TabItemBean(buttonViewsListItem);
            itemBeans.add(itemBean);
        }
        if (viewBean == null) {
            viewBean = new CustomButtonViewsViewBean(currModuleComponent);
            viewBean.setItemBeans(itemBeans);
            viewBean.setTabItems(buttonViewsListItems);
        } else {
            viewBean.setItemBeans(itemBeans);
            viewBean.setTabItems(buttonViewsListItems);
            viewBean.updateModule(currModuleComponent);
        }
        viewBean.updateContainerBean(buttonViewsComponent);
        return viewBean;

    }

    @Override
    public void update(ModuleComponent parentModuleComponent, ButtonViewsComponent component) {
        this.initWidget(parentModuleComponent,component);
        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
        if (component.getChildren() != null && component.getChildren().size() > 0) {
            super.update(parentModuleComponent, component);
        }else if (component.getProperties().getItems().size()>0){
            this.viewBean=genViewBean();
        }


    }


    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        return new HashSet<>();
    }


    public CustomButtonViewsFieldBean(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(ButtonViewsFieldAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof ButtonViewsFieldAnnotation) {
                fillData((ButtonViewsFieldAnnotation) annotation);
            }
        }
    }

    public CustomButtonViewsFieldBean(ButtonViewsFieldAnnotation annotation) {
        fillData(annotation);
    }

    public CustomButtonViewsFieldBean fillData(ButtonViewsFieldAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.BUTTONVIEWS;
    }

}
