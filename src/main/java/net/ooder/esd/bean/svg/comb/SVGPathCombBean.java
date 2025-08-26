package net.ooder.esd.bean.svg.comb;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.svg.SVGPathCombAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.bean.svg.SVGBean;
import net.ooder.esd.bean.svg.SVGFieldBaseBean;
import net.ooder.esd.custom.component.form.field.svg.comb.CustomSVGPathCombComponent;
import net.ooder.esd.tool.component.SVGPathComponent;
import net.ooder.esd.tool.properties.svg.comb.path.PathKey;
import net.ooder.esd.tool.properties.svg.comb.path.PathProperties;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@CustomClass(clazz = CustomSVGPathCombComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.SVGPATHCOMB
)
@AnnotationType(clazz = SVGPathCombAnnotation.class)
public class SVGPathCombBean<T extends PathKey> extends SVGFieldBaseBean<T,SVGPathComponent> {

    public String svgTag;
    public T svgKey;


    public SVGPathCombBean() {

    }

    public SVGPathCombBean(PathProperties properties) {
        svgBean = new SVGBean(properties);
        svgText = properties.getAttr().getTEXT();
        svgBG = properties.getAttr().getBG();
        svgTag = properties.getSvgTag();
        svgKey = (T) properties.getAttr().getKEY();
    }


    public SVGPathCombBean(Set<Annotation> annotations) {
        super(annotations);
        AnnotationUtil.fillDefaultValue(SVGPathCombAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof SVGPathCombAnnotation) {
                fillData((SVGPathCombAnnotation) annotation);
            }
        }
        svgKey = (T) new PathKey(annotations);
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


    public SVGPathCombBean(SVGPathCombAnnotation annotation) {
        fillData(annotation);
    }

    public SVGPathCombBean fillData(SVGPathCombAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public SVGPathCombBean clone() {
        SVGPathCombBean circleBean = new SVGPathCombBean();
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
    public T getSvgKey() {
        return svgKey;
    }

    public void setSvgKey(T svgKey) {
        this.svgKey = svgKey;
    }

    @Override
    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
