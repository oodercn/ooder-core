package net.ooder.esd.bean.svg;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.svg.SVGTextAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.bean.field.FieldComponentBean;
import net.ooder.esd.custom.component.form.field.svg.CustomSVGTextComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.SVGTextComponent;
import net.ooder.esd.tool.properties.svg.SVGProperties;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CustomClass(clazz = CustomSVGTextComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.SVGTEXT)

@AnnotationType(clazz = SVGTextAnnotation.class)
public class SVGTextBean implements FieldComponentBean<SVGTextComponent> {


    SVGBean svgBean;

    String xpath;

    String path;

    public SVGTextBean() {

    }

    public SVGTextBean(SVGTextComponent svgTextComponent) {
        xpath = svgTextComponent.getPath();
        svgBean = new SVGBean(svgTextComponent.getProperties());
    }

    @Override
    public List<JavaSrcBean> getJavaSrcBeans() {
        return new ArrayList<>();
    }

    @Override
    public List<JavaSrcBean> update(ModuleComponent moduleComponent, SVGTextComponent component) {
        return new ArrayList<>();
    }

    public SVGTextBean(SVGProperties properties) {
        svgBean = new SVGBean(properties);
    }

    public SVGTextBean(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(SVGTextAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof SVGTextAnnotation) {
                fillData((SVGTextAnnotation) annotation);
            }
        }
        svgBean = new SVGBean(annotations);

    }


    @Override
    public ComponentType getComponentType() {
        return ComponentType.SVGTEXT;
    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        annotationBeans = new ArrayList<>();
        if (svgBean != null && !AnnotationUtil.getAnnotationMap(svgBean).isEmpty()) {
            annotationBeans.add(svgBean);
        }

        annotationBeans.add(this);
        return annotationBeans;
    }

    public SVGTextBean(SVGTextAnnotation annotation) {
        fillData(annotation);
    }

    public SVGTextBean fillData(SVGTextAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public SVGTextBean clone() {
        SVGTextBean circleBean = new SVGTextBean();
        if (svgBean != null) {
            circleBean.setSvgBean(svgBean.clone());
        }
        return circleBean;
    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        return new HashSet<>();
    }

    public SVGBean getSvgBean() {
        return svgBean;
    }

    public void setSvgBean(SVGBean svgBean) {
        this.svgBean = svgBean;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    @Override
    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
