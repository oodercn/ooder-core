package net.ooder.esd.manager.plugins.font.node;

import com.alibaba.fastjson.annotation.JSONField;

public class ALFont {
    String name;
    @JSONField(name = "icon_id")
    String iconid;
    String unicode;
    @JSONField(name = "font_class")
    String unicode_decimal;
    @JSONField(name = "font_class")
    String fontclass;

    protected ALFont clone() {
        ALFont ialFont = new ALFont();
        ialFont.setFontclass(this.fontclass);
        ialFont.setUnicode(this.unicode);
        ialFont.setName(this.name);
        ialFont.setIconid(this.iconid);
        ialFont.setUnicode_decimal(this.unicode_decimal);
        return ialFont;
    }


    public String getUnicode() {
        return unicode;
    }

    public void setUnicode(String unicode) {
        this.unicode = unicode;
    }

    public String getUnicode_decimal() {
        return unicode_decimal;
    }

    public void setUnicode_decimal(String unicode_decimal) {
        this.unicode_decimal = unicode_decimal;
    }


    public String getIconid() {
        return iconid;
    }

    public void setIconid(String iconid) {
        this.iconid = iconid;
    }

    public String getFontclass() {
        return fontclass;
    }

    public void setFontclass(String fontclass) {
        this.fontclass = fontclass;
    }

    public ALFont() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}