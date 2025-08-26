package net.ooder.esd.tool.properties.echarts;

import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.view.CustomEChartViewBean;
import net.ooder.esd.bean.field.CustomEChartFieldBean;
import net.ooder.esd.tool.properties.ContainerUIProperties;

public class EChartProperties extends ContainerUIProperties {
    public String chartTheme;

    public String chartRenderer;

    public String bottom;


    public String chartCDN;

    public String chartCDNGL;


    public Integer chartDevicePixelRatio;

    public String display;

    public String xAxisDateFormatter;


    public Boolean chartResizeSilent;

    public ChartOption chartOption;

    public EChartProperties() {

    }


    public EChartProperties(CustomEChartViewBean chartViewBean, ContainerBean containerBean) {
        this.initChart(chartViewBean);
        if (containerBean != null) {
            this.init(containerBean);

        }

    }

    public EChartProperties(CustomEChartFieldBean fieldChartViewBean, ContainerBean containerBean) {
        CustomEChartViewBean chartViewBean = fieldChartViewBean.getViewBean();
        this.init(chartViewBean.getContainerBean());
        this.initChart(chartViewBean);
        if (containerBean != null) {
            this.init(containerBean);

        }


    }

    void initChart(CustomEChartViewBean chartViewBean) {


        this.id = chartViewBean.getId();
        this.name = chartViewBean.getName();


    }


    public ChartOption getChartOption() {
        return chartOption;
    }

    public void setChartOption(ChartOption chartOption) {
        this.chartOption = chartOption;
    }


    public Boolean getChartResizeSilent() {
        return chartResizeSilent;
    }

    public void setChartResizeSilent(Boolean chartResizeSilent) {
        this.chartResizeSilent = chartResizeSilent;
    }


    public String getChartTheme() {
        return chartTheme;
    }

    public void setChartTheme(String chartTheme) {
        this.chartTheme = chartTheme;
    }

    public String getChartRenderer() {
        return chartRenderer;
    }

    public void setChartRenderer(String chartRenderer) {
        this.chartRenderer = chartRenderer;
    }


    public String getBottom() {
        return bottom;
    }

    public void setBottom(String bottom) {
        this.bottom = bottom;
    }

    public String getChartCDN() {
        return chartCDN;
    }

    public void setChartCDN(String chartCDN) {
        this.chartCDN = chartCDN;
    }

    public String getChartCDNGL() {
        return chartCDNGL;
    }

    public void setChartCDNGL(String chartCDNGL) {
        this.chartCDNGL = chartCDNGL;
    }

    public Integer getChartDevicePixelRatio() {
        return chartDevicePixelRatio;
    }

    public void setChartDevicePixelRatio(Integer chartDevicePixelRatio) {
        this.chartDevicePixelRatio = chartDevicePixelRatio;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getxAxisDateFormatter() {
        return xAxisDateFormatter;
    }

    public void setxAxisDateFormatter(String xAxisDateFormatter) {
        this.xAxisDateFormatter = xAxisDateFormatter;
    }

}
