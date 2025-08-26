package net.ooder.esd.bean.svg;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.svg.SVGEllipseAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.tool.component.SVGEllipseComponent;
import net.ooder.esd.tool.properties.svg.SVGProperties;
import net.ooder.esd.tool.properties.svg.ellipse.EllipseKey;
import net.ooder.esd.tool.properties.svg.ellipse.EllipseProperties;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@CustomClass(clazz = SVGEllipseComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.SVGELLIPSE
)
@AnnotationType(clazz = SVGEllipseAnnotation.class)
public class SVGEllipseBean extends SVGFieldBaseBean<EllipseKey,SVGEllipseComponent> {

   public String svgTag;
    public   EllipseKey svgKey;


    public SVGEllipseBean() {

    }

    public SVGEllipseBean(SVGEllipseComponent component) {
        super(component);
        this.update((SVGProperties) component.getProperties());
    }
    protected void update(EllipseProperties properties) {
        svgBean = new SVGBean(properties);
        svgText = properties.getAttr().getTEXT();
        svgBG = properties.getAttr().getBG();
        svgTag = properties.getSvgTag();
        svgKey = properties.getAttr().getKEY();

    }




    public SVGEllipseBean(Set<Annotation> annotations) {
        super(annotations);
        AnnotationUtil.fillDefaultValue(SVGEllipseAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof SVGEllipseAnnotation) {
                fillData((SVGEllipseAnnotation) annotation);
            }
        }

        svgKey =  new EllipseKey(annotations);

    }


    @Override
    public ComponentType getComponentType() {
        return ComponentType.SVGRECTCOMB;
    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = super.getAnnotationBeans();

        if (svgKey != null && !AnnotationUtil.getAnnotationMap(svgKey).isEmpty()) {
            annotationBeans.add(svgKey);
        }
        annotationBeans.add(this);
        return annotationBeans;
    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        return new HashSet<>();
    }

    public SVGEllipseBean(SVGEllipseAnnotation annotation) {
        fillData(annotation);
    }

    public SVGEllipseBean fillData(SVGEllipseAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public SVGEllipseBean clone() {
        SVGEllipseBean circleBean = new SVGEllipseBean();
        circleBean.setSvgTag(this.getSvgTag());
        if (svgKey != null) {
            circleBean.setSvgKey(svgKey.clone());
        }
        if (svgBean != null) {
            circleBean.setSvgBean(svgBean.clone());
        }
        if (svgBG != null) {
            circleBean.setSvgBG(svgBG.clone());
        }
        if (svgText != null) {
            circleBean.setSvgText(svgText.clone());
        }
        return circleBean;
    }

    public String getSvgTag() {
        return svgTag;
    }

    public void setSvgTag(String svgTag) {
        this.svgTag = svgTag;
    }

    @Override
    public EllipseKey getSvgKey() {
        return svgKey;
    }

    @Override
    public void setSvgKey(EllipseKey svgKey) {
        this.svgKey = svgKey;
    }

    @Override
    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
