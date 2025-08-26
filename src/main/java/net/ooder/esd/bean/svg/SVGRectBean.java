package net.ooder.esd.bean.svg;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.svg.SVGRectAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.custom.component.form.field.svg.CustomSVGRectComponent;
import net.ooder.esd.tool.component.SVGRectCombComponent;
import net.ooder.esd.tool.component.SVGRectComponent;
import net.ooder.esd.tool.properties.svg.comb.rect.RectKey;
import net.ooder.esd.tool.properties.svg.comb.rect.RectProperties;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@CustomClass(clazz = CustomSVGRectComponent.class, viewType = CustomViewType.COMPONENT, componentType = ComponentType.SVGRECT)
@AnnotationType(clazz = SVGRectAnnotation.class)
public class SVGRectBean<T extends RectKey> extends SVGFieldBaseBean<T,SVGRectComponent> {

    public String svgTag;

    public T svgKey;



    public SVGRectBean() {

    }


    public SVGRectBean(RectProperties properties) {
        svgBean = new SVGBean(properties);
        svgText = properties.getAttr().getTEXT();
        svgBG = properties.getAttr().getBG();
        svgTag = properties.getSvgTag();
        svgKey = (T) properties.getAttr().getKEY();
    }


    public SVGRectBean(Set<Annotation> annotations) {
        super(annotations);
        AnnotationUtil.fillDefaultValue(SVGRectAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof SVGRectAnnotation) {
                fillData((SVGRectAnnotation) annotation);
            }
        }

        svgKey = (T) new RectKey(annotations);

    }


    @Override
    public ComponentType getComponentType() {
        return ComponentType.SVGRECT;
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
    public Set<Class> getOtherClass() {
        Set<Class> classSet=new HashSet<>();

        return classSet;
    }

    public SVGRectBean clone() {
        SVGRectBean circleBean = new SVGRectBean();
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

    public SVGRectBean(SVGRectAnnotation annotation) {
        fillData(annotation);
    }

    public SVGRectBean fillData(SVGRectAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String getSvgTag() {
        return svgTag;
    }

    public void setSvgTag(String svgTag) {
        this.svgTag = svgTag;
    }

    @Override
    public T getSvgKey() {
        return svgKey;
    }


    @Override
    public void setSvgKey(T svgKey) {
        this.svgKey = svgKey;
    }

    @Override
    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
