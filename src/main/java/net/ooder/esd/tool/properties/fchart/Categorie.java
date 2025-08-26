package net.ooder.esd.tool.properties.fchart;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.bean.fchart.items.CategorieListItemBean;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.sf.cglib.beans.BeanMap;

import java.util.Map;

public class Categorie {

    String label;

    Boolean showlabel;

    String tooltext;

    String font;

    String fontcolor;

    Boolean fontbold;

    Boolean fontitalic;

    String bgcolor;

    String bordercolor;

    Integer alpha;

    Integer bgalpha;

    Integer borderalpha;

    Integer borderpadding;

    Integer borderradius;

    Integer borderthickness;

    Boolean borderdashed;

    Integer borderdashLen;

    Integer borderdashgap;

    String link;


    public Categorie() {

    }

    public Categorie(Enum linenum) {
        OgnlUtil.setProperties(BeanMap.create(linenum), this, false, false);
    }

    public Categorie(CategorieListItemBean itemBean) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(itemBean), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public Integer getAlpha() {
        return alpha;
    }

    public void setAlpha(Integer alpha) {
        this.alpha = alpha;
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
