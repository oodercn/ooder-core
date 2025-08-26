package net.ooder.esd.bean.svg.comb;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.svg.SVGRectCombAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.bean.svg.SVGBean;
import net.ooder.esd.bean.svg.SVGFieldBaseBean;
import net.ooder.esd.custom.component.form.field.svg.comb.CustomSVGImageCombComponent;
import net.ooder.esd.custom.component.form.field.svg.comb.CustomSVGRectCombComponent;
import net.ooder.esd.tool.component.SVGRectCombComponent;
import net.ooder.esd.tool.properties.svg.comb.rect.RectKey;
import net.ooder.esd.tool.properties.svg.comb.rect.RectProperties;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@CustomClass(clazz = CustomSVGRectCombComponent.class, viewType = CustomViewType.COMPONENT, componentType = ComponentType.SVGRECTCOMB)
@AnnotationType(clazz = SVGRectCombAnnotation.class)
public class SVGRectCombBean<T extends RectKey> extends SVGFieldBaseBean<T,SVGRectCombComponent> {

    public String svgTag;

    public T svgKey;

    public SVGRectCombBean() {

    }

    public SVGRectCombBean(RectProperties properties) {
        svgBean = new SVGBean(properties);
        svgText = properties.getAttr().getTEXT();
        svgBG = properties.getAttr().getBG();
        svgTag = properties.getSvgTag();
        svgKey = (T) properties.getAttr().getKEY();
    }


    public SVGRectCombBean(Set<Annotation> annotations) {
        super(annotations);
        AnnotationUtil.fillDefaultValue(SVGRectCombAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof SVGRectCombAnnotation) {
                fillData((SVGRectCombAnnotation) annotation);
            }
        }
        svgKey = (T) new RectKey(annotations);
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


    public SVGRectCombBean(SVGRectCombAnnotation annotation) {
        fillData(annotation);
    }

    public SVGRectCombBean fillData(SVGRectCombAnnotation annotation) {
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

    public SVGRectCombBean clone() {
        SVGRectCombBean circleBean = new SVGRectCombBean();
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

    @Override
    public void setSvgKey(T svgKey) {
        this.svgKey = svgKey;
    }

    @Override
    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
