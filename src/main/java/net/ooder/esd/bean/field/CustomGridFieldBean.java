package net.ooder.esd.bean.field;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.GridFieldAnnotation;
import net.ooder.esd.annotation.Widget;
import net.ooder.esd.annotation.ui.BorderType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.BaseWidgetBean;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.CustomGridViewBean;
import net.ooder.esd.custom.component.form.field.CustomFieldGridComponent;
import net.ooder.esd.tool.component.TreeGridComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.CustomWidgetBean;
import net.ooder.esd.tool.properties.GridProperties;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
@CustomClass(clazz = CustomFieldGridComponent.class,
        viewType = CustomViewType.COMPONENT,
        moduleType = ModuleViewType.GRIDCONFIG,
        componentType = ComponentType.TREEGRID
)
@AnnotationType(clazz = GridFieldAnnotation.class)
public class CustomGridFieldBean extends BaseWidgetBean<CustomGridViewBean, TreeGridComponent> {

    BorderType borderType;

    public CustomGridFieldBean() {

    }

    public CustomGridFieldBean(TreeGridComponent component) {
        AnnotationUtil.fillDefaultValue(GridFieldAnnotation.class, this);
        update(null, component);
    }


    public CustomGridFieldBean(ModuleComponent parentModuleComponent, TreeGridComponent component) {
        AnnotationUtil.fillDefaultValue(GridFieldAnnotation.class, this);
        update(parentModuleComponent, component);
    }

    public CustomGridFieldBean(MethodConfig methodConfig) {
        viewBean = (CustomGridViewBean) methodConfig.getView();
        init(AnnotationUtil.getAllAnnotations(methodConfig.getMethod(),true));
    }



    @Override
    public CustomGridViewBean createViewBean(ModuleComponent currModuleComponent, TreeGridComponent component) {
        if (viewBean == null) {
            viewBean = new CustomGridViewBean(currModuleComponent);
        } else {
            viewBean.updateModule(currModuleComponent);
        }
        viewBean.updateContainerBean(component);
        this.initProperties(component);
        this.borderType = component.getProperties().getBorderType();
        return viewBean;
    }


    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        if (viewBean != null) {
            return viewBean.getOtherClass();
        }
        return new HashSet<>();
    }

    public CustomGridFieldBean(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(GridFieldAnnotation.class, this);
        init(annotations);
    }

    void init(Set<Annotation> annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof GridFieldAnnotation) {
                fillData((GridFieldAnnotation) annotation);
            }
            if (annotation instanceof Widget) {
                widgetBean = new CustomWidgetBean((Widget) annotation);
            }
        }
        containerBean = new ContainerBean(annotations);
    }


    public CustomGridFieldBean(GridProperties galleryProperties) {
        viewBean.init(galleryProperties);


    }

    public BorderType getBorderType() {
        return borderType;
    }

    public void setBorderType(BorderType borderType) {
        this.borderType = borderType;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.TREEGRID;
    }


    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }


    public CustomGridFieldBean fillData(GridFieldAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }



}
