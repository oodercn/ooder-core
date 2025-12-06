package net.ooder.esd.bean.svg;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.svg.SVGImageAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.bean.field.FieldComponentBean;
import net.ooder.esd.custom.component.form.field.svg.CustomSVGImageComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.SVGImageComponent;
import net.ooder.esd.tool.properties.svg.SVGProperties;
import net.ooder.annotation.AnnotationType;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.*;

@CustomClass(clazz = CustomSVGImageComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.SVGIMAGE
)
@AnnotationType(clazz = SVGImageAnnotation.class)
public class SVGImageBean implements FieldComponentBean<SVGImageComponent> {


    SVGBean svgBean;
    String xpath;
    public SVGImageBean() {

    }

    public SVGImageBean(SVGImageComponent svgImageComponent) {
        this.xpath=svgImageComponent.getPath();
        svgBean = new SVGBean(svgImageComponent.getProperties());
    }
    public SVGImageBean(SVGProperties properties) {
        svgBean = new SVGBean(properties);
    }


    @Override
    public List<JavaSrcBean> getJavaSrcBeans() {
        return new ArrayList<>();
    }

    @Override
    public void update(ModuleComponent moduleComponent, SVGImageComponent component) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(component.getProperties()), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);

    }

    public SVGImageBean(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(SVGImageAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof SVGImageAnnotation) {
                fillData((SVGImageAnnotation) annotation);
            }
        }
        svgBean = new SVGBean(annotations);

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

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet=new HashSet<>();

        return classSet;
    }

    public SVGImageBean(SVGImageAnnotation annotation) {
        fillData(annotation);
    }

    public SVGImageBean fillData(SVGImageAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public SVGImageBean clone() {
        SVGImageBean circleBean = new SVGImageBean();
        if (svgBean != null) {
            circleBean.setSvgBean(svgBean.clone());
        }
        return circleBean;
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
