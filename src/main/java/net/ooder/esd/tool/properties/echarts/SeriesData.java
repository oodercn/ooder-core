package net.ooder.esd.tool.properties.echarts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SeriesData {


    List<Object> data = new ArrayList<Object>();

    Map<String, Object> label;

    Map<String, Object> lineStyle;

    Map<String, Object> detail;

    Map<String, Object> equation;

    Map<String, Object> wireframe;

    Map<String, Object> emphasis;

    Boolean hoverAnimation;

    Boolean clipOverflow;

    Boolean step;

    String smoothMonotone;

    String symbol;

    Integer symbolSize;

    String symbolRotate;

    Boolean showSymbol;

    String showAllSymbol;

    Boolean connectNulls;

    String sampling;

    String animationEasing;

    Integer progressive;

    String hoverLayerThreshold;
    String stack;
    String shading;
    String type;
    Boolean smooth;
    Integer zlevel;
    Integer z;
    String coordinateSystem;
    Boolean legendHoverLink;

    public Map<String, Object> getDetail() {
        return detail;
    }

    public void setDetail(Map<String, Object> detail) {
        this.detail = detail;
    }

    public Map<String, Object> getEquation() {
        return equation;
    }

    public void setEquation(Map<String, Object> equation) {
        this.equation = equation;
    }

    public Map<String, Object> getWireframe() {
        return wireframe;
    }

    public void setWireframe(Map<String, Object> wireframe) {
        this.wireframe = wireframe;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public String getShading() {
        return shading;
    }

    public void setShading(String shading) {
        this.shading = shading;
    }


    public Map<String, Object> getLabel() {
        return label;
    }

    public void setLabel(Map<String, Object> label) {
        this.label = label;
    }

    public Map<String, Object> getLineStyle() {
        return lineStyle;
    }

    public void setLineStyle(Map<String, Object> lineStyle) {
        this.lineStyle = lineStyle;
    }

    public Map<String, Object> getEmphasis() {
        return emphasis;
    }

    public void setEmphasis(Map<String, Object> emphasis) {
        this.emphasis = emphasis;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getSmooth() {
        return smooth;
    }

    public void setSmooth(Boolean smooth) {
        this.smooth = smooth;
    }

    public Integer getZlevel() {
        return zlevel;
    }

    public void setZlevel(Integer zlevel) {
        this.zlevel = zlevel;
    }

    public Integer getZ() {
        return z;
    }

    public void setZ(Integer z) {
        this.z = z;
    }

    public String getCoordinateSystem() {
        return coordinateSystem;
    }

    public void setCoordinateSystem(String coordinateSystem) {
        this.coordinateSystem = coordinateSystem;
    }

    public Boolean getLegendHoverLink() {
        return legendHoverLink;
    }

    public void setLegendHoverLink(Boolean legendHoverLink) {
        this.legendHoverLink = legendHoverLink;
    }

    public Boolean getHoverAnimation() {
        return hoverAnimation;
    }

    public void setHoverAnimation(Boolean hoverAnimation) {
        this.hoverAnimation = hoverAnimation;
    }

    public Boolean getClipOverflow() {
        return clipOverflow;
    }

    public void setClipOverflow(Boolean clipOverflow) {
        this.clipOverflow = clipOverflow;
    }

    public Boolean getStep() {
        return step;
    }

    public void setStep(Boolean step) {
        this.step = step;
    }

    public String getSmoothMonotone() {
        return smoothMonotone;
    }

    public void setSmoothMonotone(String smoothMonotone) {
        this.smoothMonotone = smoothMonotone;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Integer getSymbolSize() {
        return symbolSize;
    }

    public void setSymbolSize(Integer symbolSize) {
        this.symbolSize = symbolSize;
    }

    public String getSymbolRotate() {
        return symbolRotate;
    }

    public void setSymbolRotate(String symbolRotate) {
        this.symbolRotate = symbolRotate;
    }

    public Boolean getShowSymbol() {
        return showSymbol;
    }

    public void setShowSymbol(Boolean showSymbol) {
        this.showSymbol = showSymbol;
    }

    public String getShowAllSymbol() {
        return showAllSymbol;
    }

    public void setShowAllSymbol(String showAllSymbol) {
        this.showAllSymbol = showAllSymbol;
    }

    public Boolean getConnectNulls() {
        return connectNulls;
    }

    public void setConnectNulls(Boolean connectNulls) {
        this.connectNulls = connectNulls;
    }

    public String getSampling() {
        return sampling;
    }

    public void setSampling(String sampling) {
        this.sampling = sampling;
    }

    public String getAnimationEasing() {
        return animationEasing;
    }

    public void setAnimationEasing(String animationEasing) {
        this.animationEasing = animationEasing;
    }

    public Integer getProgressive() {
        return progressive;
    }

    public void setProgressive(Integer progressive) {
        this.progressive = progressive;
    }

    public String getHoverLayerThreshold() {
        return hoverLayerThreshold;
    }

    public void setHoverLayerThreshold(String hoverLayerThreshold) {
        this.hoverLayerThreshold = hoverLayerThreshold;
    }


}
