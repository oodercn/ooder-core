package net.ooder.esd.bean.fchart.items;


import net.ooder.esd.annotation.fchart.ValuePosition;
import net.ooder.esd.annotation.ui.HAlignType;
import net.ooder.esd.annotation.ui.VAlignType;
import net.ooder.esd.tool.properties.item.UIItem;

import java.util.HashMap;
import java.util.Map;

public class LineListItem extends UIItem {
    public Integer startvalue;
    public String color;
    public Integer displayvalue;
    public Boolean showontop;
    public ValuePosition parentyaxis;
    public Integer endvalue;
    public Boolean istrendzone;
    public Integer thickness;
    public Integer alpha;
    public Boolean dashed;
    public Integer dashlen;
    public Integer dashgap;
    public Boolean valueonright;
    public String tooltext;
    //VLine
    public Integer lineposition;
    public Boolean showlabelborder;
    public String label;
    public Integer labelposition;
    public HAlignType labelhalign;
    public VAlignType labelvalign;
    public Integer startindex;
    public Integer endindex;
    public Boolean displayalways;
    public Integer displaywhencount;
    public Boolean valueontop;
    public String id;
    public String caption;
    public String euClassName;
    public String bindClassName;


    public LineListItem() {

    }


    public Map<String, Object> addTagVar(String name, Object value) {
        if (tagVar == null) {
            tagVar = new HashMap<>();
        }
        tagVar.put(name, value);
        return tagVar;
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

    public Integer getEndvalue() {
        return endvalue;
    }

    public void setEndvalue(Integer endvalue) {
        this.endvalue = endvalue;
    }

    public Boolean getIstrendzone() {
        return istrendzone;
    }

    public void setIstrendzone(Boolean istrendzone) {
        this.istrendzone = istrendzone;
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

    public Integer getStartvalue() {
        return startvalue;
    }

    public void setStartvalue(Integer startvalue) {
        this.startvalue = startvalue;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getDisplayvalue() {
        return displayvalue;
    }

    public void setDisplayvalue(Integer displayvalue) {
        this.displayvalue = displayvalue;
    }

    public Boolean getShowontop() {
        return showontop;
    }

    public void setShowontop(Boolean showontop) {
        this.showontop = showontop;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getCaption() {
        return caption;
    }

    @Override
    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getEuClassName() {
        return euClassName;
    }

    public void setEuClassName(String euClassName) {
        this.euClassName = euClassName;
    }

    public String getBindClassName() {
        return bindClassName;
    }

    public void setBindClassName(String bindClassName) {
        this.bindClassName = bindClassName;
    }
}
