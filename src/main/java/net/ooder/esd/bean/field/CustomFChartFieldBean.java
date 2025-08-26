package net.ooder.esd.bean.field;

import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.FChartFieldAnnotation;
import net.ooder.esd.annotation.Widget;
import net.ooder.esd.annotation.fchart.FChartType;
import net.ooder.esd.annotation.event.CustomFieldEvent;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.BaseWidgetBean;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.CustomFChartViewBean;
import net.ooder.esd.custom.component.form.field.CustomFieldFChartComponent;
import net.ooder.esd.tool.component.FChartComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.CustomWidgetBean;
import net.ooder.esd.tool.properties.fchart.FChartProperties;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@CustomClass(clazz = CustomFieldFChartComponent.class,
        viewType = CustomViewType.COMPONENT,
        moduleType = ModuleViewType.CHARTCONFIG,
        componentType = ComponentType.FCHART
)

@AnnotationType(clazz = FChartFieldAnnotation.class)
public class CustomFChartFieldBean extends BaseWidgetBean<CustomFChartViewBean, FChartComponent> {


    Set<CustomFieldEvent> event = new LinkedHashSet<>();

    Class<? extends Enum> enumClass;

    String renderer;

    Boolean selectable;

    FChartType chartType;

    public String path;


    public CustomFChartFieldBean() {

    }


    @Override
    public CustomFChartViewBean createViewBean(ModuleComponent currModuleComponent, FChartComponent component) {
        if (viewBean == null) {
            viewBean = new CustomFChartViewBean(currModuleComponent);
        } else {
            viewBean.updateModule(currModuleComponent);
        }
        viewBean.updateContainerBean(component);
        FChartProperties chartProperties = component.getProperties();
        this.chartType = chartProperties.getChartType();
        this.renderer = chartProperties.getRenderer();
        this.selectable = chartProperties.getSelectable();
        return viewBean;
    }


    public CustomFChartFieldBean(FChartComponent chartComponent) {
        update(null, chartComponent);
    }

    public CustomFChartFieldBean(ModuleComponent parentModuleComponent, FChartComponent chartComponent) {
        update(parentModuleComponent, chartComponent);
    }


    public CustomFChartFieldBean(MethodConfig methodAPIBean) {
        this.viewBean = (CustomFChartViewBean) methodAPIBean.getView();
        chartType = viewBean.getChartType();

    }


    private void init(FChartProperties chartProperties) {

        this.chartType = chartProperties.getChartType();
        this.renderer = chartProperties.getRenderer();
        this.selectable = chartProperties.getSelectable();
    }


    public CustomFChartFieldBean(Set<Annotation> annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof FChartFieldAnnotation) {
                this.fillData((FChartFieldAnnotation) annotation);
            } else if (annotation instanceof Widget) {
                widgetBean = new CustomWidgetBean((Widget) annotation);
            }
        }
        containerBean = new ContainerBean(annotations);
    }


    public CustomFChartFieldBean(FChartFieldAnnotation annotation) {
        this.fillData(annotation);
    }

    public Class<? extends Enum> getEnumClass() {
        return enumClass;
    }

    public void setEnumClass(Class<? extends Enum> enumClass) {
        this.enumClass = enumClass;
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

    public FChartType getChartType() {
        return chartType;
    }

    public void setChartType(FChartType chartType) {
        this.chartType = chartType;
    }


    public CustomFChartFieldBean fillData(FChartFieldAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public CustomWidgetBean getWidgetBean() {
        return widgetBean;
    }

    public void setWidgetBean(CustomWidgetBean widgetBean) {
        this.widgetBean = widgetBean;
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
    public CustomFChartViewBean getViewBean() {
        return viewBean;
    }

    public void setViewBean(CustomFChartViewBean viewBean) {
        this.viewBean = viewBean;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.FCHART;
    }


    @Override
    public Set<Class> getOtherClass() {
        Set<Class> classSet = new HashSet<>();
        try {
            if (euClassName != null) {
                Class euClass = ClassUtility.loadClass(euClassName);
                classSet.add(euClass);
            }

        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
        }

        if (bindService != null && !bindService.equals(Void.class)) {
            classSet.add(bindService);
        }
        if (viewBean != null) {
            classSet.addAll(viewBean.getOtherClass());
        }
        return classSet;
    }


}
