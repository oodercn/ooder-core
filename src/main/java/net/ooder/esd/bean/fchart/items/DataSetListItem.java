package net.ooder.esd.bean.fchart.items;

import net.ooder.esd.annotation.fchart.ValuePosition;
import net.ooder.esd.tool.properties.item.UIItem;

import java.util.HashMap;
import java.util.Map;

public class DataSetListItem extends UIItem {


    String seriesname;

    String color;

    Integer alpha;

    ValuePosition valueposition;

    Boolean showvalues;

    Integer dashed;

    Boolean includeinlegend;

    Boolean drawanchors;

    Integer anchorradius;

    String anchorbordercolor;

    Integer anchorborderthickness;

    String anchorbgcolor;

    Integer anchoralpha;

    Integer anchorbgalpha;

    Integer linethickness;

    Integer linedashlen;

    Integer linedashgap;

    Integer plotborderalpha;

    Integer plotborderthickness;

    String plotborderbolor;

    Boolean showplotborder;

    Boolean showregressionline;

    Boolean showyonx;

    String regressionlinecolor;

    Integer regressionlinethickness;

    Integer regressionlinealpha;



    public DataSetListItem() {

    }

    public Map<String, Object> addTagVar(String name, Object value) {
        if (tagVar == null) {
            tagVar = new HashMap<>();
        }
        tagVar.put(name, value);
        return tagVar;
    }

    public String getSeriesname() {
        return seriesname;
    }

    public void setSeriesname(String seriesname) {
        this.seriesname = seriesname;
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

    public ValuePosition getValueposition() {
        return valueposition;
    }

    public void setValueposition(ValuePosition valueposition) {
        this.valueposition = valueposition;
    }

    public Boolean getShowvalues() {
        return showvalues;
    }

    public void setShowvalues(Boolean showvalues) {
        this.showvalues = showvalues;
    }

    public Integer getDashed() {
        return dashed;
    }

    public void setDashed(Integer dashed) {
        this.dashed = dashed;
    }

    public Boolean getIncludeinlegend() {
        return includeinlegend;
    }

    public void setIncludeinlegend(Boolean includeinlegend) {
        this.includeinlegend = includeinlegend;
    }

    public Boolean getDrawanchors() {
        return drawanchors;
    }

    public void setDrawanchors(Boolean drawanchors) {
        this.drawanchors = drawanchors;
    }

    public Integer getAnchorradius() {
        return anchorradius;
    }

    public void setAnchorradius(Integer anchorradius) {
        this.anchorradius = anchorradius;
    }

    public String getAnchorbordercolor() {
        return anchorbordercolor;
    }

    public void setAnchorbordercolor(String anchorbordercolor) {
        this.anchorbordercolor = anchorbordercolor;
    }

    public Integer getAnchorborderthickness() {
        return anchorborderthickness;
    }

    public void setAnchorborderthickness(Integer anchorborderthickness) {
        this.anchorborderthickness = anchorborderthickness;
    }

    public String getAnchorbgcolor() {
        return anchorbgcolor;
    }

    public void setAnchorbgcolor(String anchorbgcolor) {
        this.anchorbgcolor = anchorbgcolor;
    }

    public Integer getAnchoralpha() {
        return anchoralpha;
    }

    public void setAnchoralpha(Integer anchoralpha) {
        this.anchoralpha = anchoralpha;
    }

    public Integer getAnchorbgalpha() {
        return anchorbgalpha;
    }

    public void setAnchorbgalpha(Integer anchorbgalpha) {
        this.anchorbgalpha = anchorbgalpha;
    }

    public Integer getLinethickness() {
        return linethickness;
    }

    public void setLinethickness(Integer linethickness) {
        this.linethickness = linethickness;
    }

    public Integer getLinedashlen() {
        return linedashlen;
    }

    public void setLinedashlen(Integer linedashlen) {
        this.linedashlen = linedashlen;
    }

    public Integer getLinedashgap() {
        return linedashgap;
    }

    public void setLinedashgap(Integer linedashgap) {
        this.linedashgap = linedashgap;
    }

    public Integer getPlotborderalpha() {
        return plotborderalpha;
    }

    public void setPlotborderalpha(Integer plotborderalpha) {
        this.plotborderalpha = plotborderalpha;
    }

    public Integer getPlotborderthickness() {
        return plotborderthickness;
    }

    public void setPlotborderthickness(Integer plotborderthickness) {
        this.plotborderthickness = plotborderthickness;
    }

    public String getPlotborderbolor() {
        return plotborderbolor;
    }

    public void setPlotborderbolor(String plotborderbolor) {
        this.plotborderbolor = plotborderbolor;
    }

    public Boolean getShowplotborder() {
        return showplotborder;
    }

    public void setShowplotborder(Boolean showplotborder) {
        this.showplotborder = showplotborder;
    }

    public Boolean getShowregressionline() {
        return showregressionline;
    }

    public void setShowregressionline(Boolean showregressionline) {
        this.showregressionline = showregressionline;
    }

    public Boolean getShowyonx() {
        return showyonx;
    }

    public void setShowyonx(Boolean showyonx) {
        this.showyonx = showyonx;
    }

    public String getRegressionlinecolor() {
        return regressionlinecolor;
    }

    public void setRegressionlinecolor(String regressionlinecolor) {
        this.regressionlinecolor = regressionlinecolor;
    }

    public Integer getRegressionlinethickness() {
        return regressionlinethickness;
    }

    public void setRegressionlinethickness(Integer regressionlinethickness) {
        this.regressionlinethickness = regressionlinethickness;
    }

    public Integer getRegressionlinealpha() {
        return regressionlinealpha;
    }

    public void setRegressionlinealpha(Integer regressionlinealpha) {
        this.regressionlinealpha = regressionlinealpha;
    }
}
