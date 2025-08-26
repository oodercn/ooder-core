package net.ooder.esd.bean.svg.comb;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.svg.SVGEllipseCombAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.bean.svg.SVGEllipseBean;
import net.ooder.esd.custom.component.form.field.svg.comb.CustomSVGEllipseCombComponent;
import net.ooder.esd.tool.component.SVGEllipseComponent;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
@CustomClass(clazz = CustomSVGEllipseCombComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.SVGELLIPSECOMB
)
@AnnotationType(clazz = SVGEllipseCombAnnotation.class)
public class SVGEllipseCombBean extends SVGEllipseBean {




    public SVGEllipseCombBean() {

    }

    public SVGEllipseCombBean(SVGEllipseComponent component){
        super(component);


    }



    public SVGEllipseCombBean(Set<Annotation> annotations) {
        super(annotations);
        AnnotationUtil.fillDefaultValue(SVGEllipseCombAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof SVGEllipseCombAnnotation) {
                fillData((SVGEllipseCombAnnotation) annotation);
            }
        }

    }


    @Override
    public ComponentType getComponentType() {
        return ComponentType.SVGRECTCOMB;
    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = super.getAnnotationBeans();
        annotationBeans.add(this);
        return annotationBeans;
    }

    public SVGEllipseCombBean(SVGEllipseCombAnnotation annotation) {
        fillData(annotation);
    }

    public SVGEllipseCombBean fillData(SVGEllipseCombAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public SVGEllipseCombBean clone() {

        SVGEllipseCombBean circleBean = new SVGEllipseCombBean();
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
    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
