package net.ooder.esd.tool.properties.fchart;

import net.ooder.jds.core.esb.util.OgnlUtil;
import net.sf.cglib.beans.BeanMap;

public class FPoint {

    Integer startvalue;
    Integer endvalue;
    String displayvalue;
    Boolean valueinside;
    String color;
    Integer alpha;
    Integer thickness;
    Boolean showborder;
    String bordercolor;
    Integer radius;
    Integer innerradius;
    Boolean dashed;
    Integer dashlen;
    Integer dashgap;
    Boolean usemarker;
    String markercolor;
    String markerbordercolor;
    Integer markerradius;
    String markertooltext;

    public FPoint() {

    }
    public FPoint(Enum linenum) {
        OgnlUtil.setProperties(BeanMap.create(linenum), this, false, false);
    }

    public Integer getAlpha() {
        return alpha;
    }

    public void setAlpha(Integer alpha) {
        this.alpha = alpha;
    }


    public String getBordercolor() {
        return bordercolor;
    }

    public void setBordercolor(String bordercolor) {
        this.bordercolor = bordercolor;
    }

    public Integer getStartvalue() {
        return startvalue;
    }

    public void setStartvalue(Integer startvalue) {
        this.startvalue = startvalue;
    }

    public Integer getEndvalue() {
        return endvalue;
    }

    public void setEndvalue(Integer endvalue) {
        this.endvalue = endvalue;
    }

    public String getDisplayvalue() {
        return displayvalue;
    }

    public void setDisplayvalue(String displayvalue) {
        this.displayvalue = displayvalue;
    }

    public Boolean getValueinside() {
        return valueinside;
    }

    public void setValueinside(Boolean valueinside) {
        this.valueinside = valueinside;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getThickness() {
        return thickness;
    }

    public void setThickness(Integer thickness) {
        this.thickness = thickness;
    }

    public Boolean getShowborder() {
        return showborder;
    }

    public void setShowborder(Boolean showborder) {
        this.showborder = showborder;
    }

    public Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    public Integer getInnerradius() {
        return innerradius;
    }

    public void setInnerradius(Integer innerradius) {
        this.innerradius = innerradius;
    }

    public Boolean getDashed() {
        return dashed;
    }

    public void setDashed(Boolean dashed) {
        this.dashed = dashed;
    }

    public Integer getDashlen() {
        return dashlen;
    }

    public void setDashlen(Integer dashlen) {
        this.dashlen = dashlen;
    }

    public Integer getDashgap() {
        return dashgap;
    }

    public void setDashgap(Integer dashgap) {
        this.dashgap = dashgap;
    }

    public Boolean getUsemarker() {
        return usemarker;
    }

    public void setUsemarker(Boolean usemarker) {
        this.usemarker = usemarker;
    }

    public String getMarkercolor() {
        return markercolor;
    }

    public void setMarkercolor(String markercolor) {
        this.markercolor = markercolor;
    }

    public String getMarkerbordercolor() {
        return markerbordercolor;
    }

    public void setMarkerbordercolor(String markerbordercolor) {
        this.markerbordercolor = markerbordercolor;
    }

    public Integer getMarkerradius() {
        return markerradius;
    }

    public void setMarkerradius(Integer markerradius) {
        this.markerradius = markerradius;
    }

    public String getMarkertooltext() {
        return markertooltext;
    }

    public void setMarkertooltext(String markertooltext) {
        this.markertooltext = markertooltext;
    }
}
