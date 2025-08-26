package net.ooder.esd.tool.properties.echarts;

import java.util.Map;

public class Grid3D {

    Integer boxWidth;

    Integer boxDepth;

    Map<String, Object> axisPointer;

    Light light;

    ViewControl viewControl;

    class ViewControl {
        Integer alpha;
        Integer beta;
        Integer minAlpha;
        Integer maxAlpha;
        Integer minBeta;
        Integer maxBeta;

        public Integer getAlpha() {
            return alpha;
        }

        public void setAlpha(Integer alpha) {
            this.alpha = alpha;
        }

        public Integer getBeta() {
            return beta;
        }

        public void setBeta(Integer beta) {
            this.beta = beta;
        }

        public Integer getMinAlpha() {
            return minAlpha;
        }

        public void setMinAlpha(Integer minAlpha) {
            this.minAlpha = minAlpha;
        }

        public Integer getMaxAlpha() {
            return maxAlpha;
        }

        public void setMaxAlpha(Integer maxAlpha) {
            this.maxAlpha = maxAlpha;
        }

        public Integer getMinBeta() {
            return minBeta;
        }

        public void setMinBeta(Integer minBeta) {
            this.minBeta = minBeta;
        }

        public Integer getMaxBeta() {
            return maxBeta;
        }

        public void setMaxBeta(Integer maxBeta) {
            this.maxBeta = maxBeta;
        }
    }

    class Light {

        Map<String, Object> main;
        Map<String, Object> ambient;

        public Map<String, Object> getMain() {
            return main;
        }

        public void setMain(Map<String, Object> main) {
            this.main = main;
        }

        public Map<String, Object> getAmbient() {
            return ambient;
        }

        public void setAmbient(Map<String, Object> ambient) {
            this.ambient = ambient;
        }

    }

    public Integer getBoxWidth() {
        return boxWidth;
    }

    public void setBoxWidth(Integer boxWidth) {
        this.boxWidth = boxWidth;
    }

    public Integer getBoxDepth() {
        return boxDepth;
    }

    public void setBoxDepth(Integer boxDepth) {
        this.boxDepth = boxDepth;
    }

    public Map<String, Object> getAxisPointer() {
        return axisPointer;
    }

    public void setAxisPointer(Map<String, Object> axisPointer) {
        this.axisPointer = axisPointer;
    }

    public Light getLight() {
        return light;
    }

    public void setLight(Light light) {
        this.light = light;
    }

    public ViewControl getViewControl() {
        return viewControl;
    }

    public void setViewControl(ViewControl viewControl) {
        this.viewControl = viewControl;
    }
}
