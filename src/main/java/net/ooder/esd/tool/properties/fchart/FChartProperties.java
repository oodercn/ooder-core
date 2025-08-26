package net.ooder.esd.tool.properties.fchart;

import net.ooder.esd.annotation.fchart.FChartType;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.view.CustomFChartViewBean;
import net.ooder.esd.bean.field.CustomFChartFieldBean;
import net.ooder.esd.tool.properties.ContainerDivProperties;

import java.util.Map;

public class FChartProperties extends ContainerDivProperties {


    FChartType chartType;
    net.ooder.esd.tool.properties.fchart.JSONData JSONData;
    String renderer;
    Boolean selectable;
    String chartCDN;
    String XMLUrl;
    String JSONUrl;
    Map<String, Object> XMLData;
    Map<String, Object> plotData;
    Map<String, Object> feedData;

    public FChartProperties() {

    }

    public FChartProperties(CustomFChartViewBean chartViewBean, ContainerBean containerBean) {
        this.initChart(chartViewBean);
        if (containerBean != null) {
            this.init(containerBean);

        }

    }

    public FChartProperties(CustomFChartFieldBean fieldChartViewBean, ContainerBean containerBean) {
        CustomFChartViewBean chartViewBean = fieldChartViewBean.getViewBean();
        this.init(chartViewBean.getContainerBean());
        this.initChart(chartViewBean);
        if (containerBean != null) {
            this.init(containerBean);

        }


    }

    void initChart(CustomFChartViewBean chartViewBean){

        this.id = chartViewBean.getId();
        this.name = chartViewBean.getName();
        this.selectable = chartViewBean.getSelectable();
        this.renderer = chartViewBean.getRenderer();
        this.chartCDN = chartViewBean.getChartCDN();
        this.chartType = chartViewBean.getChartType();
        this.XMLUrl = chartViewBean.getXMLUrl();
        this.XMLData = chartViewBean.getXMLData();
        this.JSONUrl = chartViewBean.getJSONUrl();
        this.plotData = chartViewBean.getPlotData();
        this.feedData = chartViewBean.getFeedData();
        this.JSONData = new JSONData(chartViewBean);

    }


    public String getRenderer() {
        return renderer;
    }


    public void setRenderer(String renderer) {
        this.renderer = renderer;
    }

    public Boolean getSelectable() {
        return selectable;
    }

    public void setSelectable(Boolean selectable) {
        this.selectable = selectable;
    }

    public String getChartCDN() {
        return chartCDN;
    }

    public void setChartCDN(String chartCDN) {
        this.chartCDN = chartCDN;
    }

    public FChartType getChartType() {
        return chartType;
    }

    public void setChartType(FChartType chartType) {
        this.chartType = chartType;
    }

    public String getXMLUrl() {
        return XMLUrl;
    }

    public void setXMLUrl(String XMLUrl) {
        this.XMLUrl = XMLUrl;
    }

    public Map<String, Object> getXMLData() {
        return XMLData;
    }

    public void setXMLData(Map<String, Object> XMLData) {
        this.XMLData = XMLData;
    }

    public String getJSONUrl() {
        return JSONUrl;
    }

    public void setJSONUrl(String JSONUrl) {
        this.JSONUrl = JSONUrl;
    }

    public Map<String, Object> getPlotData() {
        return plotData;
    }

    public void setPlotData(Map<String, Object> plotData) {
        this.plotData = plotData;
    }

    public Map<String, Object> getFeedData() {
        return feedData;
    }

    public void setFeedData(Map<String, Object> feedData) {
        this.feedData = feedData;
    }

    public net.ooder.esd.tool.properties.fchart.JSONData getJSONData() {
        return JSONData;
    }

    public void setJSONData(net.ooder.esd.tool.properties.fchart.JSONData JSONData) {
        this.JSONData = JSONData;
    }

}
