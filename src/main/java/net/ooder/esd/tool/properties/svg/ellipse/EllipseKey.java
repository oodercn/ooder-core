package net.ooder.esd.tool.properties.svg.ellipse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.svg.RectKeyAnnotation;
import net.ooder.esd.tool.properties.svg.comb.Key;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;

@AnnotationType(clazz = RectKeyAnnotation.class)
public class EllipseKey extends Key {

    Integer width = 80;
    Integer height = 40;

    @JSONField(name = "stroke-miterlimit")
    Integer strokeMiterlimit;

    @JSONField(name = "stroke-opacity")
    Integer strokeOpacity;

    @JSONField(name = "stroke-width")
    Integer strokeWidth;

    List<String> transform;


    public EllipseKey() {

    }

    public EllipseKey(EllipseProperties properties) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }

    public EllipseKey(Set<Annotation> annotations) {
        super(annotations);
        AnnotationUtil.fillDefaultValue(RectKeyAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof RectKeyAnnotation) {
                fillData((RectKeyAnnotation) annotation);
            }
        }

    }

    public EllipseKey(RectKeyAnnotation annotation) {
        fillData(annotation);
    }

    public EllipseKey fillData(RectKeyAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public Integer getStrokeMiterlimit() {
        return strokeMiterlimit;
    }

    public void setStrokeMiterlimit(Integer strokeMiterlimit) {
        this.strokeMiterlimit = strokeMiterlimit;
    }

    public Integer getStrokeOpacity() {
        return strokeOpacity;
    }

    public void setStrokeOpacity(Integer strokeOpacity) {
        this.strokeOpacity = strokeOpacity;
    }

    public Integer getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(Integer strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public List<String> getTransform() {
        return transform;
    }

    public void setTransform(    List<String> transform) {
        this.transform = transform;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public EllipseKey clone() {
        EllipseKey text = new EllipseKey();
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(this), Map.class), text, false, false);
        return text;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
