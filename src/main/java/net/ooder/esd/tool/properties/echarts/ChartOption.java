package net.ooder.esd.tool.properties.echarts;

import java.util.List;
import java.util.Map;

public class ChartOption {

    public AxisData xAxis;

    public AxisData yAxis;

    public AxisData xAxis3D;

    public AxisData yAxis3D;

    public AxisData zAxis3D;

    public Grid3D grid3D;

    public Boolean calculable;

    public Map<String, Object> title;

    public Map<String, Object> radar;

    public Map<String, Object> legend;


    public Map<String, Object> tooltip;

    public Map<String, Object> visualMap;

    public String backgroundColor;

    public ToolBox toolbox;

    public List<SeriesData> series;

    public List<String> color;

    public Map<String, Object> getRadar() {
        return radar;
    }

    public void setRadar(Map<String, Object> radar) {
        this.radar = radar;
    }

    public Map<String, Object> getLegend() {
        return legend;
    }

    public void setLegend(Map<String, Object> legend) {
        this.legend = legend;
    }

    public AxisData getxAxis3D() {
        return xAxis3D;
    }

    public void setxAxis3D(AxisData xAxis3D) {
        this.xAxis3D = xAxis3D;
    }

    public AxisData getyAxis3D() {
        return yAxis3D;
    }

    public void setyAxis3D(AxisData yAxis3D) {
        this.yAxis3D = yAxis3D;
    }

    public AxisData getzAxis3D() {
        return zAxis3D;
    }

    public void setzAxis3D(AxisData zAxis3D) {
        this.zAxis3D = zAxis3D;
    }

    public Grid3D getGrid3D() {
        return grid3D;
    }

    public void setGrid3D(Grid3D grid3D) {
        this.grid3D = grid3D;
    }

    public Map<String, Object> getVisualMap() {
        return visualMap;
    }

    public void setVisualMap(Map<String, Object> visualMap) {
        this.visualMap = visualMap;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public List<String> getColor() {
        return color;
    }

    public void setColor(List<String> color) {
        this.color = color;
    }

    public Boolean getCalculable() {
        return calculable;
    }

    public void setCalculable(Boolean calculable) {
        this.calculable = calculable;
    }

    public Map<String, Object> getTitle() {
        return title;
    }

    public void setTitle(Map<String, Object> title) {
        this.title = title;
    }

    public Map<String, Object> getTooltip() {
        return tooltip;
    }

    public void setTooltip(Map<String, Object> tooltip) {
        this.tooltip = tooltip;
    }

    public ToolBox getToolbox() {
        return toolbox;
    }

    public void setToolbox(ToolBox toolbox) {
        this.toolbox = toolbox;
    }

    public AxisData getxAxis() {
        return xAxis;
    }

    public void setxAxis(AxisData xAxis) {
        this.xAxis = xAxis;
    }

    public AxisData getyAxis() {
        return yAxis;
    }

    public void setyAxis(AxisData yAxis) {
        this.yAxis = yAxis;
    }

    public List<SeriesData> getSeries() {
        return series;
    }

    public void setSeries(List<SeriesData> series) {
        this.series = series;
    }
}
