package net.ooder.esd.bean.fchart;


import com.alibaba.fastjson.JSON;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.fchart.ChartPlotAnnotation;
import net.ooder.esd.tool.properties.fchart.ChartData;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.Map;

@AnnotationType(clazz = ChartPlotAnnotation.class)
public class ChartPlotBean implements CustomBean {

    Boolean showplotborder;

    Boolean useroundedges;

    String plotbordercolor;

    Integer plotborderthickness;

    Integer plotborderalpha;

    Boolean plotborderdashed;

    Integer plotborderdashlen;

    Integer plotborderdashgap;

    Integer plotfillangle;

    Integer plotfillratio;

    Integer plotfillalpha;

    String plotgradientcolor;

    public ChartPlotBean() {

    }

    public ChartPlotBean( ChartData chartData) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(chartData), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);

    }

    public ChartPlotBean(ChartPlotAnnotation annotation) {

        this.fillData(annotation);
    }

    public ChartPlotBean fillData(ChartPlotAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public Boolean getShowplotborder() {
        return showplotborder;
    }

    public void setShowplotborder(Boolean showplotborder) {
        this.showplotborder = showplotborder;
    }

    public Boolean getUseroundedges() {
        return useroundedges;
    }

    public void setUseroundedges(Boolean useroundedges) {
        this.useroundedges = useroundedges;
    }

    public String getPlotbordercolor() {
        return plotbordercolor;
    }

    public void setPlotbordercolor(String plotbordercolor) {
        this.plotbordercolor = plotbordercolor;
    }

    public Integer getPlotborderthickness() {
        return plotborderthickness;
    }

    public void setPlotborderthickness(Integer plotborderthickness) {
        this.plotborderthickness = plotborderthickness;
    }

    public Integer getPlotborderalpha() {
        return plotborderalpha;
    }

    public void setPlotborderalpha(Integer plotborderalpha) {
        this.plotborderalpha = plotborderalpha;
    }

    public Boolean getPlotborderdashed() {
        return plotborderdashed;
    }

    public void setPlotborderdashed(Boolean plotborderdashed) {
        this.plotborderdashed = plotborderdashed;
    }

    public Integer getPlotborderdashlen() {
        return plotborderdashlen;
    }

    public void setPlotborderdashlen(Integer plotborderdashlen) {
        this.plotborderdashlen = plotborderdashlen;
    }

    public Integer getPlotborderdashgap() {
        return plotborderdashgap;
    }

    public void setPlotborderdashgap(Integer plotborderdashgap) {
        this.plotborderdashgap = plotborderdashgap;
    }

    public Integer getPlotfillangle() {
        return plotfillangle;
    }

    public void setPlotfillangle(Integer plotfillangle) {
        this.plotfillangle = plotfillangle;
    }

    public Integer getPlotfillratio() {
        return plotfillratio;
    }

    public void setPlotfillratio(Integer plotfillratio) {
        this.plotfillratio = plotfillratio;
    }

    public Integer getPlotfillalpha() {
        return plotfillalpha;
    }

    public void setPlotfillalpha(Integer plotfillalpha) {
        this.plotfillalpha = plotfillalpha;
    }

    public String getPlotgradientcolor() {
        return plotgradientcolor;
    }

    public void setPlotgradientcolor(String plotgradientcolor) {
        this.plotgradientcolor = plotgradientcolor;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
