package net.ooder.esd.tool.properties.fchart;

import net.ooder.esd.annotation.ui.HAlignType;
import net.ooder.esd.annotation.ui.VAlignType;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.sf.cglib.beans.BeanMap;

public class VLineData {

    Integer lineposition;
    String color;
    Integer thickness;
    Boolean showlabelborder;
    String label;
    Integer labelposition;
    Integer alpha;
    Integer dashed;
    Integer dashlen;
    Integer dashgap;
    HAlignType labelhalign;
    VAlignType labelvalign;
    Integer startindex;
    Integer endindex;
    String displayvalue;
    Boolean displayalways;
    Integer displaywhencount;
    Boolean showontop;
    Boolean valueontop;

    public VLineData() {

    }

    public VLineData(Enum linenum) {
        OgnlUtil.setProperties(BeanMap.create(linenum), this, false, false);
    }

    public Integer getLineposition() {
        return lineposition;
    }

    public void setLineposition(Integer lineposition) {
        this.lineposition = lineposition;
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

    public Boolean getShowlabelborder() {
        return showlabelborder;
    }

    public void setShowlabelborder(Boolean showlabelborder) {
        this.showlabelborder = showlabelborder;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getLabelposition() {
        return labelposition;
    }

    public void setLabelposition(Integer labelposition) {
        this.labelposition = labelposition;
    }

    public Integer getAlpha() {
        return alpha;
    }

    public void setAlpha(Integer alpha) {
        this.alpha = alpha;
    }

    public Integer getDashed() {
        return dashed;
    }

    public void setDashed(Integer dashed) {
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

    public HAlignType getLabelhalign() {
        return labelhalign;
    }

    public void setLabelhalign(HAlignType labelhalign) {
        this.labelhalign = labelhalign;
    }

    public VAlignType getLabelvalign() {
        return labelvalign;
    }

    public void setLabelvalign(VAlignType labelvalign) {
        this.labelvalign = labelvalign;
    }

    public Integer getStartindex() {
        return startindex;
    }

    public void setStartindex(Integer startindex) {
        this.startindex = startindex;
    }

    public Integer getEndindex() {
        return endindex;
    }

    public void setEndindex(Integer endindex) {
        this.endindex = endindex;
    }

    public String getDisplayvalue() {
        return displayvalue;
    }

    public void setDisplayvalue(String displayvalue) {
        this.displayvalue = displayvalue;
    }

    public Boolean getDisplayalways() {
        return displayalways;
    }

    public void setDisplayalways(Boolean displayalways) {
        this.displayalways = displayalways;
    }

    public Integer getDisplaywhencount() {
        return displaywhencount;
    }

    public void setDisplaywhencount(Integer displaywhencount) {
        this.displaywhencount = displaywhencount;
    }

    public Boolean getShowontop() {
        return showontop;
    }

    public void setShowontop(Boolean showontop) {
        this.showontop = showontop;
    }

    public Boolean getValueontop() {
        return valueontop;
    }

    public void setValueontop(Boolean valueontop) {
        this.valueontop = valueontop;
    }
}

