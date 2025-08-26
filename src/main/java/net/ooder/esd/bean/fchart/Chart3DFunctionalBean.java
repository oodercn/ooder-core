package net.ooder.esd.bean.fchart;

import com.alibaba.fastjson.JSON;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.fchart.Chart3DFunctionalAnnotation;
import net.ooder.esd.tool.properties.fchart.ChartData;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.Map;

@AnnotationType(clazz = Chart3DFunctionalAnnotation.class)
public class Chart3DFunctionalBean implements CustomBean {

    Boolean is2d;
    Boolean clustered;
    String chartorder;
    Boolean chartontop;
    Boolean autoscaling;
    Boolean allowscaling;

    Integer startangx;
    Integer startangy;
    Integer endangx;
    Integer endangy;
    Integer cameraangx;
    Integer cameraangy;
    Integer lightangx;
    Integer lightangy;
    Integer intensity;
    Boolean dynamicshading;
    Boolean bright2d;
    Boolean allowrotation;
    Boolean constrainverticalrotation;
    Integer minverticalrotangle;
    Integer maxverticalrotangle;
    Boolean constrainhorizontalrotation;
    Integer minhorizontalrotangle;
    Integer maxhorizontalrotangle;

    Boolean showplotborder;
    Integer zdepth;
    Integer zgapplot;

    Integer yzwalldepth;
    Integer zxwalldepth;
    Integer xywalldepth;


    public Chart3DFunctionalBean(Chart3DFunctionalAnnotation annotation) {
        this.fillData(annotation);
    }

    public Chart3DFunctionalBean(ChartData chartData) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(chartData), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);

    }

    public Chart3DFunctionalBean() {

    }

    public Boolean getIs2d() {
        return is2d;
    }

    public void setIs2d(Boolean is2d) {
        this.is2d = is2d;
    }

    public Boolean getClustered() {
        return clustered;
    }

    public void setClustered(Boolean clustered) {
        this.clustered = clustered;
    }

    public String getChartorder() {
        return chartorder;
    }

    public void setChartorder(String chartorder) {
        this.chartorder = chartorder;
    }

    public Boolean getChartontop() {
        return chartontop;
    }

    public void setChartontop(Boolean chartontop) {
        this.chartontop = chartontop;
    }

    public Boolean getAutoscaling() {
        return autoscaling;
    }

    public void setAutoscaling(Boolean autoscaling) {
        this.autoscaling = autoscaling;
    }

    public Boolean getAllowscaling() {
        return allowscaling;
    }

    public void setAllowscaling(Boolean allowscaling) {
        this.allowscaling = allowscaling;
    }

    public Integer getStartangx() {
        return startangx;
    }

    public void setStartangx(Integer startangx) {
        this.startangx = startangx;
    }

    public Integer getStartangy() {
        return startangy;
    }

    public void setStartangy(Integer startangy) {
        this.startangy = startangy;
    }

    public Integer getEndangx() {
        return endangx;
    }

    public void setEndangx(Integer endangx) {
        this.endangx = endangx;
    }

    public Integer getEndangy() {
        return endangy;
    }

    public void setEndangy(Integer endangy) {
        this.endangy = endangy;
    }

    public Integer getCameraangx() {
        return cameraangx;
    }

    public void setCameraangx(Integer cameraangx) {
        this.cameraangx = cameraangx;
    }

    public Integer getCameraangy() {
        return cameraangy;
    }

    public void setCameraangy(Integer cameraangy) {
        this.cameraangy = cameraangy;
    }

    public Integer getLightangx() {
        return lightangx;
    }

    public void setLightangx(Integer lightangx) {
        this.lightangx = lightangx;
    }

    public Integer getLightangy() {
        return lightangy;
    }

    public void setLightangy(Integer lightangy) {
        this.lightangy = lightangy;
    }

    public Integer getIntensity() {
        return intensity;
    }

    public void setIntensity(Integer intensity) {
        this.intensity = intensity;
    }

    public Boolean getDynamicshading() {
        return dynamicshading;
    }

    public void setDynamicshading(Boolean dynamicshading) {
        this.dynamicshading = dynamicshading;
    }

    public Boolean getBright2d() {
        return bright2d;
    }

    public void setBright2d(Boolean bright2d) {
        this.bright2d = bright2d;
    }

    public Boolean getAllowrotation() {
        return allowrotation;
    }

    public void setAllowrotation(Boolean allowrotation) {
        this.allowrotation = allowrotation;
    }

    public Boolean getConstrainverticalrotation() {
        return constrainverticalrotation;
    }

    public void setConstrainverticalrotation(Boolean constrainverticalrotation) {
        this.constrainverticalrotation = constrainverticalrotation;
    }

    public Integer getMinverticalrotangle() {
        return minverticalrotangle;
    }

    public void setMinverticalrotangle(Integer minverticalrotangle) {
        this.minverticalrotangle = minverticalrotangle;
    }

    public Integer getMaxverticalrotangle() {
        return maxverticalrotangle;
    }

    public void setMaxverticalrotangle(Integer maxverticalrotangle) {
        this.maxverticalrotangle = maxverticalrotangle;
    }

    public Boolean getConstrainhorizontalrotation() {
        return constrainhorizontalrotation;
    }

    public void setConstrainhorizontalrotation(Boolean constrainhorizontalrotation) {
        this.constrainhorizontalrotation = constrainhorizontalrotation;
    }

    public Integer getMinhorizontalrotangle() {
        return minhorizontalrotangle;
    }

    public void setMinhorizontalrotangle(Integer minhorizontalrotangle) {
        this.minhorizontalrotangle = minhorizontalrotangle;
    }

    public Integer getMaxhorizontalrotangle() {
        return maxhorizontalrotangle;
    }

    public void setMaxhorizontalrotangle(Integer maxhorizontalrotangle) {
        this.maxhorizontalrotangle = maxhorizontalrotangle;
    }

    public Boolean getShowplotborder() {
        return showplotborder;
    }

    public void setShowplotborder(Boolean showplotborder) {
        this.showplotborder = showplotborder;
    }

    public Integer getZdepth() {
        return zdepth;
    }

    public void setZdepth(Integer zdepth) {
        this.zdepth = zdepth;
    }

    public Integer getZgapplot() {
        return zgapplot;
    }

    public void setZgapplot(Integer zgapplot) {
        this.zgapplot = zgapplot;
    }

    public Integer getYzwalldepth() {
        return yzwalldepth;
    }

    public void setYzwalldepth(Integer yzwalldepth) {
        this.yzwalldepth = yzwalldepth;
    }

    public Integer getZxwalldepth() {
        return zxwalldepth;
    }

    public void setZxwalldepth(Integer zxwalldepth) {
        this.zxwalldepth = zxwalldepth;
    }

    public Integer getXywalldepth() {
        return xywalldepth;
    }

    public void setXywalldepth(Integer xywalldepth) {
        this.xywalldepth = xywalldepth;
    }

    public Chart3DFunctionalBean fillData(Chart3DFunctionalAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
