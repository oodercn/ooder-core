package net.ooder.esd.bean.field;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.FormFieldAnnotation;
import net.ooder.esd.annotation.Widget;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.BaseWidgetBean;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.view.CustomFormViewBean;
import net.ooder.esd.custom.component.form.field.CustomFieldFormComponent;
import net.ooder.esd.tool.component.FormLayoutComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.CustomWidgetBean;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
@CustomClass(clazz = CustomFieldFormComponent.class,
        viewType = CustomViewType.COMPONENT,
        moduleType = ModuleViewType.FORMCONFIG,
        componentType = ComponentType.MENUBAR
)
@AnnotationType(clazz = FormFieldAnnotation.class)
public class CustomFormFieldBean extends BaseWidgetBean<CustomFormViewBean, FormLayoutComponent> {

    public CustomFormFieldBean() {

    }


    public CustomFormFieldBean(FormLayoutComponent formLayoutComponent) {
        update(null, formLayoutComponent);

    }

    public CustomFormFieldBean(ModuleComponent parentModuleComponent, FormLayoutComponent formLayoutComponent) {
        update(parentModuleComponent, formLayoutComponent);

    }


    public CustomFormFieldBean fillData(FormFieldAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }


    public CustomFormFieldBean(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(FormFieldAnnotation.class, this);
        init(annotations);
    }


    void init(Set<Annotation> annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof FormFieldAnnotation) {
                fillData((FormFieldAnnotation) annotation);
            }
            if (annotation instanceof Widget) {
                widgetBean = new CustomWidgetBean((Widget) annotation);
            }
        }
        containerBean = new ContainerBean(annotations);
    }

    @Override
    public CustomFormViewBean createViewBean(ModuleComponent currModuleComponent, FormLayoutComponent component) {
        if (viewBean == null) {
            viewBean = new CustomFormViewBean(currModuleComponent);
        } else {
            viewBean.updateModule(currModuleComponent);
        }
        viewBean.updateContainerBean(component);
        return viewBean;
    }


    @Override
    public ComponentType getComponentType() {
        return ComponentType.FORMLAYOUT;
    }


    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classes = new HashSet<>();
        if (viewBean != null) {
            classes.addAll(viewBean.getOtherClass());
        }

        return classes;
    }

    public ContainerBean getContainerBean() {
        return containerBean;
    }

    public void setContainerBean(ContainerBean containerBean) {
        this.containerBean = containerBean;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }


}
