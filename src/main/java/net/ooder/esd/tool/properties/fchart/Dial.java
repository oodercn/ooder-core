package net.ooder.esd.tool.properties.fchart;

import net.ooder.jds.core.esb.util.OgnlUtil;
import net.sf.cglib.beans.BeanMap;

public class Dial {

    String id;
    String value;
    Boolean showalue;
    Integer valuex;
    Integer valuey;
    Boolean editmode;
    String bordercolor;
    Integer borderthickness;
    Integer borderalpha;
    String bgcolor;
    Integer radius;
    Integer basewidth;
    Integer baseradius;
    Integer topwidth;
    Integer rearextension;
    String link;
    String tooltext;


    public Dial() {

    }

    public Dial(Enum linenum) {
        OgnlUtil.setProperties(BeanMap.create(linenum), this, false, false);
    }
    public Boolean getShowalue() {
        return showalue;
    }

    public void setShowalue(Boolean showalue) {
        this.showalue = showalue;
    }

    public Integer getValuex() {
        return valuex;
    }

    public void setValuex(Integer valuex) {
        this.valuex = valuex;
    }

    public Integer getValuey() {
        return valuey;
    }

    public void setValuey(Integer valuey) {
        this.valuey = valuey;
    }

    public Boolean getEditmode() {
        return editmode;
    }

    public void setEditmode(Boolean editmode) {
        this.editmode = editmode;
    }

    public String getBordercolor() {
        return bordercolor;
    }

    public void setBordercolor(String bordercolor) {
        this.bordercolor = bordercolor;
    }

    public Integer getBorderthickness() {
        return borderthickness;
    }

    public void setBorderthickness(Integer borderthickness) {
        this.borderthickness = borderthickness;
    }

    public Integer getBorderalpha() {
        return borderalpha;
    }

    public void setBorderalpha(Integer borderalpha) {
        this.borderalpha = borderalpha;
    }

    public String getBgcolor() {
        return bgcolor;
    }

    public void setBgcolor(String bgcolor) {
        this.bgcolor = bgcolor;
    }

    public Integer getBasewidth() {
        return basewidth;
    }

    public void setBasewidth(Integer basewidth) {
        this.basewidth = basewidth;
    }

    public Integer getBaseradius() {
        return baseradius;
    }

    public void setBaseradius(Integer baseradius) {
        this.baseradius = baseradius;
    }

    public Integer getTopwidth() {
        return topwidth;
    }

    public void setTopwidth(Integer topwidth) {
        this.topwidth = topwidth;
    }

    public Integer getRearextension() {
        return rearextension;
    }

    public void setRearextension(Integer rearextension) {
        this.rearextension = rearextension;
    }

    public String getTooltext() {
        return tooltext;
    }

    public void setTooltext(String tooltext) {
        this.tooltext = tooltext;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }



    public Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }


    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
