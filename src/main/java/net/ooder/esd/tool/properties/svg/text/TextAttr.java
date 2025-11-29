package net.ooder.esd.tool.properties.svg.text;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.ui.CursorType;
import net.ooder.esd.bean.svg.SVGTextBean;
import net.ooder.esd.tool.properties.svg.SVGAttr;
import net.ooder.esd.tool.properties.svg.comb.Text;


public class TextAttr extends SVGAttr {
    @JSONField(name = "KEY")
    TextKey KEY;

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



    public TextAttr(SVGTextBean textBean) {
        this.KEY = textBean.getKEY();
    }

    public TextAttr() {
        KEY = new TextKey();
        TEXT = new Text();
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    @Override
    public String getFill() {
        return fill;
    }

    @Override
    public void setFill(String fill) {
        this.fill = fill;
    }

    public String getFontWeight() {
        return fontWeight;
    }

    public void setFontWeight(String fontWeight) {
        this.fontWeight = fontWeight;
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

    public Integer getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(Integer strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    @Override
    public String getStroke() {
        return stroke;
    }

    @Override
    public void setStroke(String stroke) {
        this.stroke = stroke;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    public CursorType getCursor() {
        return cursor;
    }

    public void setCursor(CursorType cursor) {
        this.cursor = cursor;
    }

    public TextKey getKEY() {
        return KEY;
    }

    public void setKEY(TextKey KEY) {
        this.KEY = KEY;
    }


}
