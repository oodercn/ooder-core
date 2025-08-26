package net.ooder.esd.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.SVGPaperAnnotation;
import net.ooder.esd.annotation.Widget;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.OverflowType;
import net.ooder.esd.tool.component.SVGPaperComponent;
import net.ooder.esd.tool.properties.CustomWidgetBean;
import net.ooder.esd.tool.properties.svg.SVGPaperProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.*;

@AnnotationType(clazz = SVGPaperAnnotation.class)
public class SVGPaperBean implements ComponentBean {


    String iframeAutoLoad;

    String html;

    String width;

    String xpath;

    String height;

    OverflowType overflow;

    Boolean scaleChildren;

    Integer graphicZIndex;

    CustomWidgetBean widgetBean;

    ContainerBean containerBean;


    public SVGPaperBean(SVGPaperComponent component) {
        if (containerBean == null) {
            containerBean = new ContainerBean(component);
        } else {
            containerBean.update(component);
        }
        if (widgetBean == null) {
            widgetBean = new CustomWidgetBean(component.getProperties());
        }
        this.xpath = component.getPath();
        this.init(component.getProperties());
    }

    private void init(SVGPaperProperties properties) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);

    }

    public SVGPaperBean() {

    }

    public SVGPaperBean(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(SVGPaperAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof SVGPaperAnnotation) {
                fillData((SVGPaperAnnotation) annotation);
            }
            if (annotation instanceof Widget) {
                widgetBean = new CustomWidgetBean((Widget) annotation);
            }
        }
        containerBean = new ContainerBean(annotations);

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

    public String getIframeAutoLoad() {
        return iframeAutoLoad;
    }

    public void setIframeAutoLoad(String iframeAutoLoad) {
        this.iframeAutoLoad = iframeAutoLoad;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
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

    public OverflowType getOverflow() {
        return overflow;
    }

    public void setOverflow(OverflowType overflow) {
        this.overflow = overflow;
    }

    public Boolean getScaleChildren() {
        return scaleChildren;
    }

    public void setScaleChildren(Boolean scaleChildren) {
        this.scaleChildren = scaleChildren;
    }

    public Integer getGraphicZIndex() {
        return graphicZIndex;
    }

    public void setGraphicZIndex(Integer graphicZIndex) {
        this.graphicZIndex = graphicZIndex;
    }

    public SVGPaperBean(SVGPaperAnnotation annotation) {
        fillData(annotation);
    }

    public SVGPaperBean fillData(SVGPaperAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.SVGPAPER;
    }

    @Override
    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    @Override
    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        annotationBeans.add(this);
        return annotationBeans;
    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet = new HashSet<>();

        return classSet;
    }
}
