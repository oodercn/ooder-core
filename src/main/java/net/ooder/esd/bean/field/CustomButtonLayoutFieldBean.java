package net.ooder.esd.bean.field;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.ButtonViewsFieldAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.view.ButtonLayoutViewAnnotation;
import net.ooder.esd.bean.BaseWidgetBean;
import net.ooder.esd.bean.nav.TabItemBean;
import net.ooder.esd.bean.view.CustomButtonLayoutViewBean;
import net.ooder.esd.custom.component.form.field.CustomFieldButtonLayoutComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.ButtonLayoutComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.item.ButtonLayoutItem;
import net.ooder.esd.tool.properties.list.AbsListProperties;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

@CustomClass(clazz = CustomFieldButtonLayoutComponent.class,
        viewType = CustomViewType.COMPONENT,
        moduleType = ModuleViewType.NAVBUTTONVIEWSCONFIG,
        componentType = ComponentType.BUTTONVIEWS
)
@AnnotationType(clazz = ButtonLayoutViewAnnotation.class)
public class CustomButtonLayoutFieldBean extends BaseWidgetBean<CustomButtonLayoutViewBean, ButtonLayoutComponent> {

    public CustomButtonLayoutFieldBean() {

    }

    public CustomButtonLayoutFieldBean(ModuleComponent parentModuleComponent, ButtonLayoutComponent buttonLayoutComponent) {

        update(parentModuleComponent, buttonLayoutComponent);
    }


    @Override
    public void update(ModuleComponent parentModuleComponent, ButtonLayoutComponent component) {
        this.initWidget(parentModuleComponent, component);
        AbsListProperties listProperties = component.getProperties();
        if (component.getChildren() != null && component.getChildren().size() > 0) {
           super.update(parentModuleComponent, component);
        } else if (listProperties.getItems().size() > 0) {
          super.update(parentModuleComponent, component);
            this.viewBean = genViewBean();
        }

    }


    @Override
    public CustomButtonLayoutViewBean createViewBean(ModuleComponent currModuleComponent, ButtonLayoutComponent buttonLayoutComponent) {
        List<TabItemBean> itemBeans = new ArrayList<>();
        List<ButtonLayoutItem> buttonViewsListItems = buttonLayoutComponent.getProperties().getItems();
        for (ButtonLayoutItem buttonViewsListItem : buttonViewsListItems) {
            TabItemBean itemBean = new TabItemBean(buttonViewsListItem);
            itemBeans.add(itemBean);
        }
        viewBean = new CustomButtonLayoutViewBean(currModuleComponent);
        if (viewBean == null) {
            viewBean = new CustomButtonLayoutViewBean(currModuleComponent);
            viewBean.setTabItems(buttonViewsListItems);
        } else {
            viewBean.setTabItems(buttonViewsListItems);
            viewBean.updateModule(currModuleComponent);
        }
        viewBean.updateContainerBean(buttonLayoutComponent);
        return viewBean;

    }


    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        return new HashSet<>();
    }


    public CustomButtonLayoutFieldBean(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(ButtonViewsFieldAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof ButtonViewsFieldAnnotation) {
                fillData((ButtonViewsFieldAnnotation) annotation);
            }
        }
    }

    public CustomButtonLayoutFieldBean(ButtonViewsFieldAnnotation annotation) {
        fillData(annotation);
    }

    public CustomButtonLayoutFieldBean fillData(ButtonViewsFieldAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.BUTTONVIEWS;
    }

}
