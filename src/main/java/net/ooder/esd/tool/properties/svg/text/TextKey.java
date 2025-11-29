package net.ooder.esd.tool.properties.svg.text;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.NotNull;
import net.ooder.esd.annotation.svg.RectKeyAnnotation;
import net.ooder.esd.annotation.svg.SVGTextAnnotation;
import net.ooder.esd.annotation.ui.CursorType;
import net.ooder.esd.tool.properties.svg.comb.Key;
import net.ooder.esd.tool.properties.svg.ellipse.EllipseProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;

@AnnotationType(clazz = SVGTextAnnotation.class)
public class TextKey extends Key {


    @JSONField(name = "stroke-miterlimit")
    Integer strokeMiterlimit;

    @JSONField(name = "stroke-opacity")
    Integer strokeOpacity;

    @JSONField(name = "stroke-width")
    Integer strokeWidth;

    List<String> transform;

    @NotNull
    String fontSize;

    String fontWight;

    String fontStyle;

    CursorType cursor;


    public TextKey() {

    }

    public TextKey(EllipseProperties properties) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }

    public TextKey(Set<Annotation> annotations) {
        super(annotations);
        AnnotationUtil.fillDefaultValue(RectKeyAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof RectKeyAnnotation) {
                fillData((RectKeyAnnotation) annotation);
            }
        }

    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public String getFontWight() {
        return fontWight;
    }

    public void setFontWight(String fontWight) {
        this.fontWight = fontWight;
    }

    public String getFontStyle() {
        return fontStyle;
    }

    public void setFontStyle(String fontStyle) {
        this.fontStyle = fontStyle;
    }

    public CursorType getCursor() {
        return cursor;
    }

    public void setCursor(CursorType cursor) {
        this.cursor = cursor;
    }

    public TextKey(RectKeyAnnotation annotation) {
        fillData(annotation);
    }

    public TextKey fillData(RectKeyAnnotation annotation) {
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

    public void setTransform(List<String> transform) {
        this.transform = transform;
    }


    public TextKey clone() {
        TextKey text = new TextKey();
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(this), Map.class), text, false, false);
        return text;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
