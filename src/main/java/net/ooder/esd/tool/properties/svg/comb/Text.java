package net.ooder.esd.tool.properties.svg.comb;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.svg.SVGText;
import net.ooder.esd.annotation.ui.CursorType;
import net.ooder.esd.tool.properties.svg.SVGProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

@AnnotationType(clazz = SVGText.class)
public class Text implements CustomBean {
    String text;
    @JSONField(name = "font-size")
    String fontSize;

    String fill;
    @JSONField(name = "font-weight")
    String fontWeight;

    @JSONField(name = "font-family")
    String fontFamily;

    @JSONField(name = "font-style")
    String fontStyle;

    @JSONField(name = "strok-width")
    Integer strokeWidth;

    String stroke;

    String title;

    CursorType cursor;


    public Text() {

    }

    public Text(SVGProperties properties) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);

    }


    public Text(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(SVGText.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof SVGText) {
                fillData((SVGText) annotation);
            }
        }

    }

    public Text(SVGText annotation) {
        fillData(annotation);
    }

    public Text fillData(SVGText annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }


    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStroke() {
        return stroke;
    }

    public void setStroke(String stroke) {
        this.stroke = stroke;
    }

    public Integer getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(Integer strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public String getFill() {
        return fill;
    }

    public void setFill(String fill) {
        this.fill = fill;
    }

    public String getFontWeight() {
        return fontWeight;
    }

    public void setFontWeight(String fontWeight) {
        this.fontWeight = fontWeight;
    }

    public Text clone() {
        Text text = new Text();
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(this), Map.class), text, false, false);
        return text;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
