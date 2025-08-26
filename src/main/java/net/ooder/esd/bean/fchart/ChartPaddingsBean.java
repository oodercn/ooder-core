package net.ooder.esd.bean.fchart;

import com.alibaba.fastjson.JSON;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.fchart.ChartPaddingsAnnotation;
import net.ooder.esd.tool.properties.fchart.ChartData;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.Map;

@AnnotationType(clazz = ChartPaddingsAnnotation.class)
public class ChartPaddingsBean implements CustomBean {


    Integer captionpadding;

    Integer xaxisnamepadding;

    Integer yaxisnamepadding;

    Integer yaxisvaluespadding;

    Integer labelpadding;

    Integer valuepadding;

    Integer plotspacepercent;

    Integer chartleftmargin;

    Integer chartrightmargin;

    Integer charttopmargin;

    Integer chartbottommargin;

    Integer canvasleftmargin;

    Integer canvasrightmargin;

    Integer canvastopmargin;

    Integer canvasbottommargin;

    public ChartPaddingsBean() {

    }

    public ChartPaddingsBean(ChartData chartData) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(chartData), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);

    }

    public ChartPaddingsBean(ChartPaddingsAnnotation annotation) {

        this.fillData(annotation);
    }

    public ChartPaddingsBean fillData(ChartPaddingsAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public Integer getCaptionpadding() {
        return captionpadding;
    }

    public void setCaptionpadding(Integer captionpadding) {
        this.captionpadding = captionpadding;
    }

    public Integer getxaxisnamepadding() {
        return xaxisnamepadding;
    }

    public void setxaxisnamepadding(Integer xaxisnamepadding) {
        this.xaxisnamepadding = xaxisnamepadding;
    }

    public Integer getyaxisnamepadding() {
        return yaxisnamepadding;
    }

    public void setyaxisnamepadding(Integer yaxisnamepadding) {
        this.yaxisnamepadding = yaxisnamepadding;
    }


    public Integer getLabelpadding() {
        return labelpadding;
    }

    public void setLabelpadding(Integer labelpadding) {
        this.labelpadding = labelpadding;
    }

    public Integer getValuepadding() {
        return valuepadding;
    }

    public void setValuepadding(Integer valuepadding) {
        this.valuepadding = valuepadding;
    }

    public Integer getXaxisnamepadding() {
        return xaxisnamepadding;
    }

    public void setXaxisnamepadding(Integer xaxisnamepadding) {
        this.xaxisnamepadding = xaxisnamepadding;
    }

    public Integer getYaxisnamepadding() {
        return yaxisnamepadding;
    }

    public void setYaxisnamepadding(Integer yaxisnamepadding) {
        this.yaxisnamepadding = yaxisnamepadding;
    }

    public Integer getYaxisvaluespadding() {
        return yaxisvaluespadding;
    }

    public void setYaxisvaluespadding(Integer yaxisvaluespadding) {
        this.yaxisvaluespadding = yaxisvaluespadding;
    }

    public Integer getPlotspacepercent() {
        return plotspacepercent;
    }

    public void setPlotspacepercent(Integer plotspacepercent) {
        this.plotspacepercent = plotspacepercent;
    }

    public Integer getChartleftmargin() {
        return chartleftmargin;
    }

    public void setChartleftmargin(Integer chartleftmargin) {
        this.chartleftmargin = chartleftmargin;
    }

    public Integer getChartrightmargin() {
        return chartrightmargin;
    }

    public void setChartrightmargin(Integer chartrightmargin) {
        this.chartrightmargin = chartrightmargin;
    }

    public Integer getCharttopmargin() {
        return charttopmargin;
    }

    public void setCharttopmargin(Integer charttopmargin) {
        this.charttopmargin = charttopmargin;
    }

    public Integer getChartbottommargin() {
        return chartbottommargin;
    }

    public void setChartbottommargin(Integer chartbottommargin) {
        this.chartbottommargin = chartbottommargin;
    }

    public Integer getCanvasleftmargin() {
        return canvasleftmargin;
    }

    public void setCanvasleftmargin(Integer canvasleftmargin) {
        this.canvasleftmargin = canvasleftmargin;
    }

    public Integer getCanvasrightmargin() {
        return canvasrightmargin;
    }

    public void setCanvasrightmargin(Integer canvasrightmargin) {
        this.canvasrightmargin = canvasrightmargin;
    }

    public Integer getCanvastopmargin() {
        return canvastopmargin;
    }

    public void setCanvastopmargin(Integer canvastopmargin) {
        this.canvastopmargin = canvastopmargin;
    }

    public Integer getCanvasbottommargin() {
        return canvasbottommargin;
    }

    public void setCanvasbottommargin(Integer canvasbottommargin) {
        this.canvasbottommargin = canvasbottommargin;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
