package net.ooder.esd.bean.svg;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.svg.SVGConnectorAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.custom.component.form.field.svg.CustomSVGConnectorComponent;
import net.ooder.esd.tool.component.SVGConnectorComponent;
import net.ooder.esd.tool.properties.svg.comb.connector.ConnectorKey;
import net.ooder.esd.tool.properties.svg.comb.connector.ConnectorProperties;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@CustomClass(clazz = CustomSVGConnectorComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.SVGCONNECTOR
)
@AnnotationType(clazz = SVGConnectorAnnotation.class)
public class SVGConnectorBean<T extends ConnectorKey> extends SVGFieldBaseBean<T,SVGConnectorComponent> {

    String svgTag;
    T svgKey;


    public SVGConnectorBean() {

    }

    public SVGConnectorBean(ConnectorProperties properties) {
        svgBean = new SVGBean(properties);
        svgText = properties.getAttr().getTEXT();
        svgBG = properties.getAttr().getBG();
        svgTag = properties.getSvgTag();
        svgKey = (T) properties.getAttr().getKEY();

    }
    public SVGConnectorBean(Set<Annotation> annotations) {
        super(annotations);
        AnnotationUtil.fillDefaultValue(SVGConnectorAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof SVGConnectorAnnotation) {
                fillData((SVGConnectorAnnotation) annotation);
            }
        }
        svgKey = (T) new ConnectorKey(annotations);
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

    public SVGConnectorBean(SVGConnectorAnnotation annotation) {
        fillData(annotation);
    }

    public SVGConnectorBean fillData(SVGConnectorAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public SVGConnectorBean clone() {
        SVGConnectorBean circleBean = new SVGConnectorBean();
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

    @Override
    public void setSvgKey(T svgKey) {
        this.svgKey = svgKey;
    }

    @Override
    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
