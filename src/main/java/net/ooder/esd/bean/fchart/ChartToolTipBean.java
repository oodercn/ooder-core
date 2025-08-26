package net.ooder.esd.bean.fchart;

import com.alibaba.fastjson.JSON;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.fchart.ChartToolTipAnnotation;
import net.ooder.esd.tool.properties.fchart.ChartData;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.Map;

@AnnotationType(clazz = ChartToolTipAnnotation.class)
public class ChartToolTipBean implements CustomBean {


    Boolean showtooltip;

    String tooltipcolor;

    String tooltipbgcolor;

    String tooltipbordercolor;

    String tooltipsepchar;

    Boolean seriesnameintooltip;

    Boolean showtooltipshadow;

    public ChartToolTipBean() {

    }

    public ChartToolTipBean( ChartData chartData) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(chartData), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);

    }


    public ChartToolTipBean(ChartToolTipAnnotation annotation) {

        this.fillData(annotation);
    }

    public ChartToolTipBean fillData(ChartToolTipAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public Boolean getShowtooltip() {
        return showtooltip;
    }

    public void setShowtooltip(Boolean showtooltip) {
        this.showtooltip = showtooltip;
    }

    public Boolean getSeriesnameintooltip() {
        return seriesnameintooltip;
    }

    public void setSeriesnameintooltip(Boolean seriesnameintooltip) {
        this.seriesnameintooltip = seriesnameintooltip;
    }

    public Boolean getShowtooltipshadow() {
        return showtooltipshadow;
    }

    public void setShowtooltipshadow(Boolean showtooltipshadow) {
        this.showtooltipshadow = showtooltipshadow;
    }

    public String getTooltipcolor() {
        return tooltipcolor;
    }

    public void setTooltipcolor(String tooltipcolor) {
        this.tooltipcolor = tooltipcolor;
    }

    public String getTooltipbgcolor() {
        return tooltipbgcolor;
    }

    public void setTooltipbgcolor(String tooltipbgcolor) {
        this.tooltipbgcolor = tooltipbgcolor;
    }

    public String getTooltipbordercolor() {
        return tooltipbordercolor;
    }

    public void setTooltipbordercolor(String tooltipbordercolor) {
        this.tooltipbordercolor = tooltipbordercolor;
    }

    public String getTooltipsepchar() {
        return tooltipsepchar;
    }

    public void setTooltipsepchar(String tooltipsepchar) {
        this.tooltipsepchar = tooltipsepchar;
    }


    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
