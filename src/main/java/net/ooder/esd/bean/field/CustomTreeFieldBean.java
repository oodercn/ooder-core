package net.ooder.esd.bean.field;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.FormFieldAnnotation;
import net.ooder.esd.annotation.field.TreeFieldAnnotation;
import net.ooder.esd.annotation.Widget;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.BaseWidgetBean;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.view.CustomTreeViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.component.form.field.CustomFieldTreeComponent;
import net.ooder.esd.tool.component.TreeViewComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.CustomWidgetBean;
import net.ooder.esd.tool.properties.TreeViewProperties;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
@CustomClass(clazz = CustomFieldTreeComponent.class,
        viewType = CustomViewType.COMPONENT,
        moduleType = ModuleViewType.TREECONFIG,
        componentType = ComponentType.TREEVIEW
)
@AnnotationType(clazz = TreeFieldAnnotation.class)
public class CustomTreeFieldBean extends BaseWidgetBean<CustomTreeViewBean, TreeViewComponent> {


    public CustomTreeFieldBean() {

    }

    public CustomTreeFieldBean(TreeViewComponent component) {
        update(null, component);
    }

    public CustomTreeFieldBean(ModuleComponent parentModuleComponent, TreeViewComponent component) {
        update(parentModuleComponent, component);
    }


    public CustomTreeFieldBean fillData(FormFieldAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }


    public CustomTreeFieldBean(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(FormFieldAnnotation.class, this);
        init(annotations);
    }

    public CustomTreeFieldBean(MethodConfig methodAPIBean) {
        this.viewBean = (CustomTreeViewBean) methodAPIBean.getView();
    }




    @Override
    public CustomTreeViewBean createViewBean(ModuleComponent currModuleComponent, TreeViewComponent component) {
        if (viewBean == null) {
            viewBean = new CustomTreeViewBean(currModuleComponent);
        } else {
            viewBean.updateModule(currModuleComponent);
        }
        viewBean.updateContainerBean(component);
        return viewBean;
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


    public CustomTreeFieldBean(TreeViewProperties galleryProperties) {
        viewBean.initProperties(galleryProperties);
    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        if (viewBean != null) {
            return viewBean.getOtherClass();
        }
        return new HashSet<>();
    }




    @Override
    public ComponentType getComponentType() {
        return ComponentType.TREEVIEW;
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
