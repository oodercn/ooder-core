package net.ooder.esd.tool.properties.fchart;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.annotation.fchart.ValuePosition;
import net.ooder.esd.annotation.ui.HAlignType;
import net.ooder.esd.annotation.ui.VAlignType;
import net.ooder.esd.bean.fchart.items.LineListItemBean;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.sf.cglib.beans.BeanMap;

import java.util.Map;

public class LineData {

    ValuePosition parentyaxis;
    Integer startvalue;
    Integer endvalue;
    String displayvalue;
    String color;
    Boolean istrendzone;
    Boolean showontop;
    Integer thickness;
    Integer alpha;
    Integer dashed;
    Integer dashlen;
    Integer dashgap;
    Boolean valueonright;
    String tooltext;

    Integer lineposition;
    Boolean showlabelborder;
    String label;
    Integer labelposition;
    HAlignType labelhalign;
    VAlignType labelvalign;
    Integer startindex;
    Integer endindex;
    Boolean displayalways;
    Integer displaywhencount;
    Boolean valueontop;


    public LineData() {

    }




    public LineData(Enum linenum) {
        OgnlUtil.setProperties(BeanMap.create(linenum), this, false, false);
    }

    public LineData(LineListItemBean lineListItemBean) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(lineListItemBean), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }


    public Integer getLineposition() {
        return lineposition;
    }

    public void setLineposition(Integer lineposition) {
        this.lineposition = lineposition;
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

    public Boolean getValueontop() {
        return valueontop;
    }

    public void setValueontop(Boolean valueontop) {
        this.valueontop = valueontop;
    }

    public ValuePosition getParentyaxis() {
        return parentyaxis;
    }

    public void setParentyaxis(ValuePosition parentyaxis) {
        this.parentyaxis = parentyaxis;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Boolean getIstrendzone() {
        return istrendzone;
    }

    public void setIstrendzone(Boolean istrendzone) {
        this.istrendzone = istrendzone;
    }

    public Boolean getShowontop() {
        return showontop;
    }

    public void setShowontop(Boolean showontop) {
        this.showontop = showontop;
    }

    public Integer getThickness() {
        return thickness;
    }

    public void setThickness(Integer thickness) {
        this.thickness = thickness;
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

    public Boolean getValueonright() {
        return valueonright;
    }

    public void setValueonright(Boolean valueonright) {
        this.valueonright = valueonright;
    }

    public String getTooltext() {
        return tooltext;
    }

    public void setTooltext(String tooltext) {
        this.tooltext = tooltext;
    }
}

