package net.ooder.esd.bean.svg;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.svg.SVGCircleAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.custom.component.form.field.svg.CustomSVGCircleComponent;
import net.ooder.esd.tool.component.SVGCircleComponent;
import net.ooder.esd.tool.properties.svg.comb.circle.CircleKey;
import net.ooder.esd.tool.properties.svg.comb.circle.CircleProperties;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@CustomClass(clazz = CustomSVGCircleComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.SVGCIRCLE
)
@AnnotationType(clazz = SVGCircleAnnotation.class)
public class SVGCircleBean<T extends CircleKey> extends SVGFieldBaseBean<T,SVGCircleComponent> {

    public String svgTag;

    public T svgKey;

    public String xpath;


    public SVGCircleBean() {

    }

    public SVGCircleBean(SVGCircleComponent component) {
        this.xpath=component.getPath();
        this.update(component.getProperties());
    }

    void update(CircleProperties properties) {
        svgBean = new SVGBean(properties);
        svgText = properties.getAttr().getTEXT();
        svgBG = properties.getAttr().getBG();
        svgTag = properties.getSvgTag();
        svgKey = (T) properties.getAttr().getKEY();

    }


    public SVGCircleBean(Set<Annotation> annotations) {
        super(annotations);
        AnnotationUtil.fillDefaultValue(SVGCircleAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof SVGCircleAnnotation) {
                fillData((SVGCircleAnnotation) annotation);
            }
        }
        svgKey = (T) new CircleKey(annotations);

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
        Set<Class> classSet=new HashSet<>();

        return classSet;
    }

    public SVGCircleBean(SVGCircleAnnotation annotation) {
        fillData(annotation);
    }

    public SVGCircleBean fillData(SVGCircleAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String getSvgTag() {
        return svgTag;
    }

    public void setSvgTag(String svgTag) {
        this.svgTag = svgTag;
    }

    @Override
    public String getXpath() {
        return xpath;
    }

    @Override
    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    @Override
    public T getSvgKey() {
        return svgKey;
    }

    @Override
    public void setSvgKey(T svgKey) {
        this.svgKey = svgKey;
    }

    public SVGCircleBean clone() {
        SVGCircleBean circleBean = new SVGCircleBean();
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

    ;


    @Override
    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
