package net.ooder.esd.bean.field;

import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.EChartFieldAnnotation;
import net.ooder.esd.annotation.Widget;
import net.ooder.esd.annotation.event.CustomFieldEvent;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.BaseWidgetBean;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.view.CustomEChartViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.component.form.field.CustomFieldEChartComponent;
import net.ooder.esd.tool.component.EChartComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.CustomWidgetBean;
import net.ooder.esd.tool.properties.echarts.EChartProperties;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@CustomClass(clazz = CustomFieldEChartComponent.class,
        viewType = CustomViewType.COMPONENT,
        moduleType = ModuleViewType.ECHARTCONFIG,
        componentType = ComponentType.ECHARTS
)
@AnnotationType(clazz = EChartFieldAnnotation.class)
public class CustomEChartFieldBean extends BaseWidgetBean<CustomEChartViewBean, EChartComponent> {

    public Boolean canBuildModule = true;

    String renderer;

    Boolean selectable;

    Set<CustomFieldEvent> event = new LinkedHashSet<>();


    public CustomEChartFieldBean() {

    }

    public CustomEChartFieldBean(MethodConfig methodAPIBean) {
        this.viewBean = (CustomEChartViewBean) methodAPIBean.getView();

    }

    public CustomEChartFieldBean(EChartComponent chartComponent) {
        AnnotationUtil.fillDefaultValue(EChartFieldAnnotation.class, this);
        this.update(null, chartComponent);

    }

    public CustomEChartFieldBean(ModuleComponent parentModuleComponent, EChartComponent chartComponent) {
        AnnotationUtil.fillDefaultValue(EChartFieldAnnotation.class, this);
      this.update(parentModuleComponent, chartComponent);

    }


    @Override
    public CustomEChartViewBean createViewBean(ModuleComponent currModuleComponent, EChartComponent component) {
        if (viewBean == null) {
            viewBean = new CustomEChartViewBean(currModuleComponent);
        } else {
            viewBean.updateModule(currModuleComponent);
        }
        viewBean.updateContainerBean(component);
        EChartProperties chartProperties = component.getProperties();
        this.renderer = chartProperties.getRenderer();
        this.selectable = chartProperties.getSelectable();
        return viewBean;
    }

    public CustomEChartFieldBean(Set<Annotation> annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof EChartFieldAnnotation) {
                this.fillData((EChartFieldAnnotation) annotation);
            } else if (annotation instanceof Widget) {
                widgetBean = new CustomWidgetBean((Widget) annotation);
            }
        }
        containerBean = new ContainerBean(annotations);
    }


    public CustomEChartFieldBean(EChartFieldAnnotation annotation) {
        this.fillData(annotation);
    }


    public Boolean getSelectable() {
        return selectable;
    }

    public void setSelectable(Boolean selectable) {
        this.selectable = selectable;
    }

    public String getRenderer() {
        return renderer;
    }

    public void setRenderer(String renderer) {
        this.renderer = renderer;
    }

    public CustomEChartFieldBean fillData(EChartFieldAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }


    public ContainerBean getContainerBean() {
        return containerBean;
    }

    public void setContainerBean(ContainerBean containerBean) {
        this.containerBean = containerBean;
    }

    public Set<CustomFieldEvent> getEvent() {
        return event;
    }

    public void setEvent(Set<CustomFieldEvent> event) {
        this.event = event;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    @Override
    public CustomEChartViewBean getViewBean() {
        return viewBean;
    }


    @Override
    public ComponentType getComponentType() {
        return ComponentType.FCHART;
    }


    @Override
    public Set<Class> getOtherClass() {
        Set<Class> classSet = new HashSet<>();
        if (viewBean != null) {
            classSet.addAll(viewBean.getOtherClass());
        }
        return ClassUtility.checkBase(classSet);
    }

}
