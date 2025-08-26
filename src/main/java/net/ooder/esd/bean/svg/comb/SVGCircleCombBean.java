package net.ooder.esd.bean.svg.comb;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.svg.SVGCircleCombAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.bean.svg.SVGBean;
import net.ooder.esd.bean.svg.SVGFieldBaseBean;
import net.ooder.esd.custom.component.form.field.svg.comb.CustomSVGCircleCombComponent;
import net.ooder.esd.tool.component.SVGCircleCombComponent;
import net.ooder.esd.tool.properties.svg.comb.circle.CircleKey;
import net.ooder.esd.tool.properties.svg.comb.circle.CircleProperties;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@CustomClass(clazz = CustomSVGCircleCombComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.SVGCIRCLECOMB
)
@AnnotationType(clazz = SVGCircleCombAnnotation.class)
public class SVGCircleCombBean<T extends CircleKey> extends SVGFieldBaseBean<T,SVGCircleCombComponent> {

    public String svgTag;


    public String xpath;

    public String path;

    public T svgKey;



    public SVGCircleCombBean() {

    }

    public SVGCircleCombBean(SVGCircleCombComponent combComponent) {
        this.xpath=combComponent.getPath();
        this.update(combComponent.getProperties());

    }

    protected void update(CircleProperties properties) {
        this.update(properties);
    }

    public SVGCircleCombBean(CircleProperties properties) {
        svgBean = new SVGBean(properties);
        svgText = properties.getAttr().getTEXT();
        svgBG = properties.getAttr().getBG();
        svgTag = properties.getSvgTag();
        svgKey = (T) properties.getAttr().getKEY();
    }

    public SVGCircleCombBean(Set<Annotation> annotations) {
        super(annotations);
        AnnotationUtil.fillDefaultValue(SVGCircleCombAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof SVGCircleCombAnnotation) {
                fillData((SVGCircleCombAnnotation) annotation);
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

    public SVGCircleCombBean(SVGCircleCombAnnotation annotation) {
        fillData(annotation);
    }

    public SVGCircleCombBean fillData(SVGCircleCombAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }
    @Override
    public String getXpath() {
        return xpath;
    }

    @Override
    public void setXpath(String xpath) {
        this.xpath = xpath;
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

    public SVGCircleCombBean clone() {
        SVGCircleCombBean circleBean = new SVGCircleCombBean();
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
