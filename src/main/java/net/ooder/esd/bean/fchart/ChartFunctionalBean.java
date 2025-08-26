package net.ooder.esd.bean.fchart;


import com.alibaba.fastjson.JSON;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.fchart.ChartFunctionalAnnotation;
import net.ooder.esd.tool.properties.fchart.ChartData;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.Map;

@AnnotationType(clazz = ChartFunctionalAnnotation.class)
public class ChartFunctionalBean implements CustomBean {

    Boolean animation;
    Boolean animate3d;
    Integer exetime;
    Integer palette;
    String palettecolors;
    Boolean connectnulldata;
    Boolean showlabels;
    Integer maxlabelheight;
    Boolean showvalues;
    Integer labeldtep;
    Integer yaxisbaluesstep;
    Boolean showyaxisvalues;
    Boolean showlimits;
    Boolean showdivlinevalues;
    Boolean adjustdiv;
    String clickurl;
    Integer yaxisminvalue;
    Integer yaxismaxvalue;
    Integer setadaptiveymin;
    String xaxistickcolor;
    Integer xaxistickalpha;
    Integer xaxistickthickness;


    public ChartFunctionalBean(ChartFunctionalAnnotation annotation) {
        this.fillData(annotation);
    }

    public ChartFunctionalBean(ChartData chartData) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(chartData), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);

    }

    public ChartFunctionalBean() {

    }

    public Boolean getAnimate3d() {
        return animate3d;
    }

    public void setAnimate3d(Boolean animate3d) {
        this.animate3d = animate3d;
    }

    public Integer getExetime() {
        return exetime;
    }

    public void setExetime(Integer exetime) {
        this.exetime = exetime;
    }

    public String getPalettecolors() {
        return palettecolors;
    }

    public void setPalettecolors(String palettecolors) {
        this.palettecolors = palettecolors;
    }

    public Boolean getConnectnulldata() {
        return connectnulldata;
    }

    public void setConnectnulldata(Boolean connectnulldata) {
        this.connectnulldata = connectnulldata;
    }

    public Boolean getShowlabels() {
        return showlabels;
    }

    public void setShowlabels(Boolean showlabels) {
        this.showlabels = showlabels;
    }

    public Integer getMaxlabelheight() {
        return maxlabelheight;
    }

    public void setMaxlabelheight(Integer maxlabelheight) {
        this.maxlabelheight = maxlabelheight;
    }

    public Boolean getShowvalues() {
        return showvalues;
    }

    public void setShowvalues(Boolean showvalues) {
        this.showvalues = showvalues;
    }

    public Integer getLabeldtep() {
        return labeldtep;
    }

    public void setLabeldtep(Integer labeldtep) {
        this.labeldtep = labeldtep;
    }

    public Integer getYaxisbaluesstep() {
        return yaxisbaluesstep;
    }

    public void setYaxisbaluesstep(Integer yaxisbaluesstep) {
        this.yaxisbaluesstep = yaxisbaluesstep;
    }

    public Boolean getShowyaxisvalues() {
        return showyaxisvalues;
    }

    public void setShowyaxisvalues(Boolean showyaxisvalues) {
        this.showyaxisvalues = showyaxisvalues;
    }

    public Boolean getShowlimits() {
        return showlimits;
    }

    public void setShowlimits(Boolean showlimits) {
        this.showlimits = showlimits;
    }

    public Boolean getShowdivlinevalues() {
        return showdivlinevalues;
    }

    public void setShowdivlinevalues(Boolean showdivlinevalues) {
        this.showdivlinevalues = showdivlinevalues;
    }

    public Boolean getAdjustdiv() {
        return adjustdiv;
    }

    public void setAdjustdiv(Boolean adjustdiv) {
        this.adjustdiv = adjustdiv;
    }

    public String getClickurl() {
        return clickurl;
    }

    public void setClickurl(String clickurl) {
        this.clickurl = clickurl;
    }

    public Integer getYaxisminvalue() {
        return yaxisminvalue;
    }

    public void setYaxisminvalue(Integer yaxisminvalue) {
        this.yaxisminvalue = yaxisminvalue;
    }

    public Integer getYaxismaxvalue() {
        return yaxismaxvalue;
    }

    public void setYaxismaxvalue(Integer yaxismaxvalue) {
        this.yaxismaxvalue = yaxismaxvalue;
    }

    public Integer getSetadaptiveymin() {
        return setadaptiveymin;
    }

    public void setSetadaptiveymin(Integer setadaptiveymin) {
        this.setadaptiveymin = setadaptiveymin;
    }

    public String getXaxistickcolor() {
        return xaxistickcolor;
    }

    public void setXaxistickcolor(String xaxistickcolor) {
        this.xaxistickcolor = xaxistickcolor;
    }

    public Integer getXaxistickalpha() {
        return xaxistickalpha;
    }

    public void setXaxistickalpha(Integer xaxistickalpha) {
        this.xaxistickalpha = xaxistickalpha;
    }

    public Integer getXaxistickthickness() {
        return xaxistickthickness;
    }

    public void setXaxistickthickness(Integer xaxistickthickness) {
        this.xaxistickthickness = xaxistickthickness;
    }

    public Boolean getAnimation() {
        return animation;
    }

    public void setAnimation(Boolean animation) {
        this.animation = animation;
    }


    public Integer getPalette() {
        return palette;
    }

    public void setPalette(Integer palette) {
        this.palette = palette;
    }


    public ChartFunctionalBean fillData(ChartFunctionalAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
