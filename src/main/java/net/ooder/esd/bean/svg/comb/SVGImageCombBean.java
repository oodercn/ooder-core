package net.ooder.esd.bean.svg.comb;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.svg.SVGImageCombAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.bean.field.FieldComponentBean;
import net.ooder.esd.bean.svg.SVGBean;
import net.ooder.esd.custom.component.form.field.svg.comb.CustomSVGImageCombComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.SVGImageCombComponent;
import net.ooder.esd.tool.properties.svg.SVGProperties;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@CustomClass(clazz = CustomSVGImageCombComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.SVGIMAGECOMB
)
@AnnotationType(clazz = SVGImageCombAnnotation.class)
public class SVGImageCombBean implements FieldComponentBean<SVGImageCombComponent> {

    SVGBean svgBean;

    String xpath;

    String path;

    public SVGImageCombBean() {

    }


    public SVGImageCombBean(SVGProperties properties) {
        svgBean = new SVGBean(properties);
    }

    public SVGImageCombBean(SVGImageCombComponent svgImageCombComponent) {

        this.xpath=svgImageCombComponent.getPath();
        svgBean = new SVGBean(svgImageCombComponent.getProperties());
    }



    public SVGImageCombBean(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(SVGImageCombAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof SVGImageCombAnnotation) {
                fillData((SVGImageCombAnnotation) annotation);
            }
        }
        svgBean = new SVGBean(annotations);

    }
    @Override
    public List<JavaSrcBean> getJavaSrcBeans() {
        return new ArrayList<>();
    }

    @Override
    public void update(ModuleComponent moduleComponent, SVGImageCombComponent component) {

    }


    @Override
    public ComponentType getComponentType() {
        return ComponentType.SVGRECTCOMB;
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



    public SVGImageCombBean(SVGImageCombAnnotation annotation) {
        fillData(annotation);
    }

    public SVGImageCombBean fillData(SVGImageCombAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        return new HashSet<>();
    }
    public SVGImageCombBean clone() {
        SVGImageCombBean circleBean = new SVGImageCombBean();
        if (svgBean != null) {
            circleBean.setSvgBean(svgBean.clone());
        }
        return circleBean;
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

    public SVGBean getSvgBean() {
        return svgBean;
    }

    public void setSvgBean(SVGBean svgBean) {
        this.svgBean = svgBean;
    }

    @Override
    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
