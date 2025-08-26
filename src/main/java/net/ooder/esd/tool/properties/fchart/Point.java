package net.ooder.esd.tool.properties.fchart;

import net.ooder.jds.core.esb.util.OgnlUtil;
import net.sf.cglib.beans.BeanMap;

public class Point {

    Integer startValue;
    Integer endValue;
    Boolean displayValue;
    Boolean valueInside;
    String color;
    Integer alpha;
    Integer thickness;
    Boolean showBorder;
    String borderColor;
    Integer radius;
    Integer innerRadius;
    Boolean dashed;
    Integer dashLen;
    Integer dashGap;
    Boolean useMarker;
    String markerColor;
    String markerBorderColor;
    Integer markerRadius;
    String markerTooltext;


    public Point() {

    }

    public Point(Enum linenum) {
        OgnlUtil.setProperties(BeanMap.create(linenum), this, false, false);
    }


    public Integer getStartValue() {
        return startValue;
    }

    public void setStartValue(Integer startValue) {
        this.startValue = startValue;
    }

    public Integer getEndValue() {
        return endValue;
    }

    public void setEndValue(Integer endValue) {
        this.endValue = endValue;
    }

    public Boolean getDisplayValue() {
        return displayValue;
    }

    public void setDisplayValue(Boolean displayValue) {
        this.displayValue = displayValue;
    }

    public Boolean getValueInside() {
        return valueInside;
    }

    public void setValueInside(Boolean valueInside) {
        this.valueInside = valueInside;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getAlpha() {
        return alpha;
    }

    public void setAlpha(Integer alpha) {
        this.alpha = alpha;
    }

    public Integer getThickness() {
        return thickness;
    }

    public void setThickness(Integer thickness) {
        this.thickness = thickness;
    }

    public Boolean getShowBorder() {
        return showBorder;
    }

    public void setShowBorder(Boolean showBorder) {
        this.showBorder = showBorder;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    public Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    public Integer getInnerRadius() {
        return innerRadius;
    }

    public void setInnerRadius(Integer innerRadius) {
        this.innerRadius = innerRadius;
    }

    public Boolean getDashed() {
        return dashed;
    }

    public void setDashed(Boolean dashed) {
        this.dashed = dashed;
    }

    public Integer getDashLen() {
        return dashLen;
    }

    public void setDashLen(Integer dashLen) {
        this.dashLen = dashLen;
    }

    public Integer getDashGap() {
        return dashGap;
    }

    public void setDashGap(Integer dashGap) {
        this.dashGap = dashGap;
    }

    public Boolean getUseMarker() {
        return useMarker;
    }

    public void setUseMarker(Boolean useMarker) {
        this.useMarker = useMarker;
    }

    public String getMarkerColor() {
        return markerColor;
    }

    public void setMarkerColor(String markerColor) {
        this.markerColor = markerColor;
    }

    public String getMarkerBorderColor() {
        return markerBorderColor;
    }

    public void setMarkerBorderColor(String markerBorderColor) {
        this.markerBorderColor = markerBorderColor;
    }

    public Integer getMarkerRadius() {
        return markerRadius;
    }

    public void setMarkerRadius(Integer markerRadius) {
        this.markerRadius = markerRadius;
    }

    public String getMarkerTooltext() {
        return markerTooltext;
    }

    public void setMarkerTooltext(String markerTooltext) {
        this.markerTooltext = markerTooltext;
    }
}
