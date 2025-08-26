package net.ooder.esd.bean.fchart.items;


import net.ooder.esd.tool.properties.item.UIItem;

import java.util.HashMap;
import java.util.Map;

public class CategorieListItem extends UIItem {

    public String label;

    public Boolean showlabel;

    public String tooltext;

    public String font;

    public String fontcolor;

    public Boolean fontbold;

    public Boolean fontitalic;

    public String bgcolor;

    public String bordercolor;

    public Integer alpha;

    public Integer bgalpha;

    public Integer borderalpha;

    public Integer borderpadding;

    public Integer borderradius;

    public Integer borderthickness;

    public Boolean borderdashed;

    public Integer borderdashLen;

    public Integer borderdashgap;

    public String link;

    public CategorieListItem() {

    }


    public Map<String, Object> addTagVar(String name, Object value) {
        if (tagVar == null) {
            tagVar = new HashMap<>();
        }
        tagVar.put(name, value);
        return tagVar;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Boolean getShowlabel() {
        return showlabel;
    }

    public void setShowlabel(Boolean showlabel) {
        this.showlabel = showlabel;
    }

    public String getTooltext() {
        return tooltext;
    }

    public void setTooltext(String tooltext) {
        this.tooltext = tooltext;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String getFontcolor() {
        return fontcolor;
    }

    public void setFontcolor(String fontcolor) {
        this.fontcolor = fontcolor;
    }

    public Boolean getFontbold() {
        return fontbold;
    }

    public void setFontbold(Boolean fontbold) {
        this.fontbold = fontbold;
    }

    public Boolean getFontitalic() {
        return fontitalic;
    }

    public void setFontitalic(Boolean fontitalic) {
        this.fontitalic = fontitalic;
    }

    public String getBgcolor() {
        return bgcolor;
    }

    public void setBgcolor(String bgcolor) {
        this.bgcolor = bgcolor;
    }

    public String getBordercolor() {
        return bordercolor;
    }

    public void setBordercolor(String bordercolor) {
        this.bordercolor = bordercolor;
    }

    public Integer getAlpha() {
        return alpha;
    }

    public void setAlpha(Integer alpha) {
        this.alpha = alpha;
    }

    public Integer getBgalpha() {
        return bgalpha;
    }

    public void setBgalpha(Integer bgalpha) {
        this.bgalpha = bgalpha;
    }

    public Integer getBorderalpha() {
        return borderalpha;
    }

    public void setBorderalpha(Integer borderalpha) {
        this.borderalpha = borderalpha;
    }

    public Integer getBorderpadding() {
        return borderpadding;
    }

    public void setBorderpadding(Integer borderpadding) {
        this.borderpadding = borderpadding;
    }

    public Integer getBorderradius() {
        return borderradius;
    }

    public void setBorderradius(Integer borderradius) {
        this.borderradius = borderradius;
    }

    public Integer getBorderthickness() {
        return borderthickness;
    }

    public void setBorderthickness(Integer borderthickness) {
        this.borderthickness = borderthickness;
    }

    public Boolean getBorderdashed() {
        return borderdashed;
    }

    public void setBorderdashed(Boolean borderdashed) {
        this.borderdashed = borderdashed;
    }

    public Integer getBorderdashLen() {
        return borderdashLen;
    }

    public void setBorderdashLen(Integer borderdashLen) {
        this.borderdashLen = borderdashLen;
    }

    public Integer getBorderdashgap() {
        return borderdashgap;
    }

    public void setBorderdashgap(Integer borderdashgap) {
        this.borderdashgap = borderdashgap;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
