package net.ooder.esd.bean.svg;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.svg.SVGPathAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.bean.field.FieldComponentBean;
import net.ooder.esd.custom.component.form.field.svg.CustomSVGPathComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.SVGPathComponent;
import net.ooder.esd.tool.properties.svg.comb.path.PathKey;
import net.ooder.esd.tool.properties.svg.comb.path.PathProperties;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CustomClass(clazz = CustomSVGPathComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.SVGPATH
)
@AnnotationType(clazz = SVGPathAnnotation.class)
public class SVGPathBean implements FieldComponentBean<SVGPathComponent> {

    public String svgTag;
    public PathKey svgKey;
    public String xpath;
    public String path;

    public SVGBean svgBean;

    public SVGPathBean() {

    }

    public SVGPathBean(PathProperties properties) {
        init(properties);

    }

    @Override
    public List<JavaSrcBean> getJavaSrcBeans() {
        return new ArrayList<>();
    }

    @Override
    public List<JavaSrcBean> update(ModuleComponent moduleComponent, SVGPathComponent component) {
        init(component.getProperties());
        return new ArrayList<>();
    }

    void init(PathProperties properties) {
        svgTag = properties.getSvgTag();
        path = properties.getAttr().getPath();
        svgBean = new SVGBean(properties);
        svgKey = new PathKey(properties);
    }

    public SVGPathBean(Set<Annotation> annotations) {

        AnnotationUtil.fillDefaultValue(SVGPathAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof SVGPathAnnotation) {
                fillData((SVGPathAnnotation) annotation);
            }
        }
        svgBean = new SVGBean();
        svgKey = new PathKey(annotations);
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

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = annotationBeans = new ArrayList<>();
        if (svgKey != null && !AnnotationUtil.getAnnotationMap(svgKey).isEmpty()) {
            annotationBeans.add(svgKey);
        }
        if (svgBean != null && !AnnotationUtil.getAnnotationMap(svgBean).isEmpty()
                && !AnnotationUtil.getAnnotationMap(svgBean).equals(AnnotationUtil.getAnnotationMap(this))
                ) {
            annotationBeans.add(svgBean);
        }

        annotationBeans.add(this);
        return annotationBeans;
    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet = new HashSet<>();
        return classSet;
    }

    public SVGPathBean clone() {
        SVGPathBean pathBean = new SVGPathBean();
        pathBean.setSvgTag(this.getSvgTag());
        if (svgKey != null) {
            pathBean.setSvgKey(svgKey.clone());
        }
        if (svgBean != null) {
            pathBean.setSvgBean(svgBean.clone());
        }

        return pathBean;
    }

    public SVGPathBean(SVGPathAnnotation annotation) {
        fillData(annotation);
    }

    public SVGPathBean fillData(SVGPathAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String getSvgTag() {
        return svgTag;
    }

    public void setSvgTag(String svgTag) {
        this.svgTag = svgTag;
    }

    public PathKey getSvgKey() {
        return svgKey;
    }

    public void setSvgKey(PathKey svgKey) {
        this.svgKey = svgKey;
    }

    public ComponentType getComponentType() {
        return ComponentType.SVGPATH;
    }

    @Override
    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
