package net.ooder.esd.bean;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.DivAnnotation;
import net.ooder.esd.annotation.Widget;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.OverflowType;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.properties.CustomWidgetBean;
import net.ooder.esd.tool.properties.DivProperties;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//@CustomClass(viewType = CustomViewType.COMPONENT,clazz =DivComponent.class, componentType = ComponentType.DIV)
@AnnotationType(clazz = DivAnnotation.class)
public class CustomDivBean implements ComponentBean {
    public String iframeAutoLoad;
    public String ajaxAutoLoad;
    public String width;
    public String height;
    public String html;
    String xpath;
    public OverflowType overflow;
    public ContainerBean containerBean;
    CustomWidgetBean widgetBean;


    public CustomDivBean(Component bean) {
        if (containerBean == null) {
            containerBean = new ContainerBean(bean);
        } else {
            containerBean.update(bean);
        }
        DivProperties properties = (DivProperties) bean.getProperties();
        this.init(properties);

    }

    public void update(Component bean) {
        if (containerBean == null) {
            containerBean = new ContainerBean(bean);
        } else {
            containerBean.update(bean);
        }
        this.xpath = bean.getPath();
        DivProperties properties = (DivProperties) bean.getProperties();
        this.init(properties);
    }

    public void update(CustomDivBean bean) {
        this.iframeAutoLoad = bean.getIframeAutoLoad();
        this.ajaxAutoLoad = bean.getAjaxAutoLoad();
        this.width = bean.getWidth();
        this.height = bean.getHeight();
        this.overflow = bean.getOverflow();
        if (bean.getContainerBean() != null) {
            containerBean = bean.getContainerBean();
        }
    }

    public void init(DivProperties bean) {
        this.iframeAutoLoad = bean.getIframeAutoLoad();
        this.ajaxAutoLoad = bean.getAjaxAutoLoad();
        this.width = bean.getWidth();
        this.height = bean.getHeight();
        this.overflow = bean.getOverflow();
    }
//
//    public CustomDivBean(DivProperties bean) {
//        containerBean = new ContainerBean(bean);
//        this.iframeAutoLoad = bean.getIframeAutoLoad();
//        this.ajaxAutoLoad = bean.getAjaxAutoLoad();
//        this.width = bean.getWidth();
//        this.height = bean.getHeight();
//        this.overflow = bean.getOverflow();
//    }


    public CustomDivBean() {

    }


    public CustomDivBean(DivAnnotation annotation) {
        fillData(annotation);
    }

    public CustomDivBean fillData(DivAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public CustomDivBean(Set<Annotation> annotations) {
        initAnnotations(annotations.toArray(new Annotation[]{}));
    }

    public CustomDivBean(Annotation[] annotations) {
        initAnnotations(annotations);
    }


    void initAnnotations(Annotation[] annotations) {
        AnnotationUtil.fillDefaultValue(DivAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof DivAnnotation) {
                fillData((DivAnnotation) annotation);
            }
            if (annotation instanceof Widget) {
                widgetBean = new CustomWidgetBean((Widget) annotation);
            }
        }
        containerBean = new ContainerBean(annotations);
    }


    @Override
    public ComponentType getComponentType() {
        return ComponentType.DIV;
    }


    @JSONField(serialize = false)
    public List<CustomBean> getAllAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        if (!AnnotationUtil.getAnnotationMap(this).isEmpty()) {
            annotationBeans.add(this);
        }
        if (containerBean != null) {
            annotationBeans.addAll(containerBean.getAnnotationBeans());
        }
        return annotationBeans;
    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        if (!AnnotationUtil.getAnnotationMap(this).isEmpty()) {
            annotationBeans.add(this);
        }
        return annotationBeans;
    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classes = new HashSet<>();
        return classes;
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

    @Override
    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public String getIframeAutoLoad() {
        return iframeAutoLoad;
    }

    public void setIframeAutoLoad(String iframeAutoLoad) {
        this.iframeAutoLoad = iframeAutoLoad;
    }

    public String getAjaxAutoLoad() {
        return ajaxAutoLoad;
    }

    public void setAjaxAutoLoad(String ajaxAutoLoad) {
        this.ajaxAutoLoad = ajaxAutoLoad;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public OverflowType getOverflow() {
        return overflow;
    }

    public void setOverflow(OverflowType overflow) {
        this.overflow = overflow;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
