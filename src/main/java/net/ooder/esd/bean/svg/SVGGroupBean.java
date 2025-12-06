package net.ooder.esd.bean.svg;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.svg.SVGGroupAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.bean.field.FieldComponentBean;
import net.ooder.esd.custom.component.form.field.svg.CustomSVGGroupComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.SVGGroupComponent;
import net.ooder.esd.tool.properties.svg.SVGProperties;
import net.ooder.annotation.AnnotationType;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.*;

@CustomClass(clazz = CustomSVGGroupComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.SVGGROUP
)
@AnnotationType(clazz = SVGGroupAnnotation.class)
public class SVGGroupBean implements FieldComponentBean<SVGGroupComponent> {


    SVGBean svgBean;

    String xpath;

    String path;

    public SVGGroupBean() {

    }

    public SVGGroupBean(SVGGroupComponent svgGroupComponent) {
        this.xpath = svgGroupComponent.getPath();
        this.update(svgGroupComponent.getProperties());
    }

    @Override
    public List<JavaSrcBean> getJavaSrcBeans() {
        return new ArrayList<>();
    }

    @Override
    public void update(ModuleComponent moduleComponent, SVGGroupComponent component) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(component.getProperties()), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);

    }

    public void update(SVGProperties properties) {
        svgBean = new SVGBean(properties);
    }

    public SVGGroupBean(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(SVGGroupAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof SVGGroupAnnotation) {
                fillData((SVGGroupAnnotation) annotation);
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public SVGGroupBean clone() {
        SVGGroupBean circleBean = new SVGGroupBean();
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

    public SVGGroupBean(SVGGroupAnnotation annotation) {
        fillData(annotation);
    }

    public SVGGroupBean fillData(SVGGroupAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
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
