package net.ooder.esd.bean.fchart;

import com.alibaba.fastjson.JSON;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.fchart.ChartCustomAnnotation;
import net.ooder.esd.tool.properties.fchart.ChartData;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.Map;

@AnnotationType(clazz = ChartCustomAnnotation.class)
public class ChartCustomBean implements CustomBean {


    Boolean animation;

    Integer palette;

    String palettecolors;

    Boolean showlabels;

    Integer maxlabelheight;

    Boolean labeldisplay;

    Boolean useellipseswhenoverflow;

    Boolean rotatelabels;

    Boolean slantlabels;

    Integer labelstep;

    Integer staggerlines;

    Boolean showvalues;

    Boolean rotatevalues;

    Boolean placevaluesinside;

    Boolean showyaxisvalues;

    Boolean showlimits;

    Boolean showdivlinevalues;

    Integer yaxisvaluesstep;

    Boolean showShadow;

    Boolean adjustdiv;

    Boolean rotateyaxisname;

    Integer yaxisnamewidth;

    String clickurl;

    Integer yaxisminvalue;

    Integer yaxismaxvalue;

    Integer setadaptiveymin;

    Boolean usedataplotcolorforlabels;

    public ChartCustomBean( ChartData chartData) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(chartData), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);

    }


    public ChartCustomBean() {

    }

    public ChartCustomBean(ChartCustomAnnotation annotation) {
        fillData(annotation);
    }

    public String getPalettecolors() {
        return palettecolors;
    }

    public void setPalettecolors(String palettecolors) {
        this.palettecolors = palettecolors;
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

    public Boolean getShowlabels() {
        return showlabels;
    }

    public void setShowlabels(Boolean showlabels) {
        this.showlabels = showlabels;
    }

    public Integer getmaxlabelheight() {
        return maxlabelheight;
    }

    public void setmaxlabelheight(Integer maxlabelheight) {
        this.maxlabelheight = maxlabelheight;
    }

    public Boolean getlabeldisplay() {
        return labeldisplay;
    }

    public void setlabeldisplay(Boolean labeldisplay) {
        this.labeldisplay = labeldisplay;
    }

    public Boolean getRotatelabels() {
        return rotatelabels;
    }

    public void setRotatelabels(Boolean rotatelabels) {
        this.rotatelabels = rotatelabels;
    }

    public Boolean getSlantlabels() {
        return slantlabels;
    }

    public void setSlantlabels(Boolean slantlabels) {
        this.slantlabels = slantlabels;
    }

    public Integer getlabelstep() {
        return labelstep;
    }

    public void setlabelstep(Integer labelstep) {
        this.labelstep = labelstep;
    }

    public Integer getStaggerlines() {
        return staggerlines;
    }

    public void setStaggerlines(Integer staggerlines) {
        this.staggerlines = staggerlines;
    }

    public Boolean getShowvalues() {
        return showvalues;
    }

    public void setShowvalues(Boolean showvalues) {
        this.showvalues = showvalues;
    }

    public Boolean getRotatevalues() {
        return rotatevalues;
    }

    public void setRotatevalues(Boolean rotatevalues) {
        this.rotatevalues = rotatevalues;
    }


    public Boolean getShowyaxisvalues() {
        return showyaxisvalues;
    }

    public void setShowyaxisvalues(Boolean showyaxisvalues) {
        this.showyaxisvalues = showyaxisvalues;
    }

    public Integer getMaxlabelheight() {
        return maxlabelheight;
    }

    public void setMaxlabelheight(Integer maxlabelheight) {
        this.maxlabelheight = maxlabelheight;
    }

    public Boolean getLabeldisplay() {
        return labeldisplay;
    }

    public void setLabeldisplay(Boolean labeldisplay) {
        this.labeldisplay = labeldisplay;
    }

    public Boolean getUseellipseswhenoverflow() {
        return useellipseswhenoverflow;
    }

    public void setUseellipseswhenoverflow(Boolean useellipseswhenoverflow) {
        this.useellipseswhenoverflow = useellipseswhenoverflow;
    }

    public Integer getLabelstep() {
        return labelstep;
    }

    public void setLabelstep(Integer labelstep) {
        this.labelstep = labelstep;
    }

    public Boolean getPlacevaluesinside() {
        return placevaluesinside;
    }

    public void setPlacevaluesinside(Boolean placevaluesinside) {
        this.placevaluesinside = placevaluesinside;
    }

    public Boolean getShowlimits() {
        return showlimits;
    }

    public void setShowlimits(Boolean showlimits) {
        this.showlimits = showlimits;
    }

    public Integer getYaxisvaluesstep() {
        return yaxisvaluesstep;
    }

    public void setYaxisvaluesstep(Integer yaxisvaluesstep) {
        this.yaxisvaluesstep = yaxisvaluesstep;
    }

    public Integer getYaxisnamewidth() {
        return yaxisnamewidth;
    }

    public void setYaxisnamewidth(Integer yaxisnamewidth) {
        this.yaxisnamewidth = yaxisnamewidth;
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

    public Boolean getUsedataplotcolorforlabels() {
        return usedataplotcolorforlabels;
    }

    public void setUsedataplotcolorforlabels(Boolean usedataplotcolorforlabels) {
        this.usedataplotcolorforlabels = usedataplotcolorforlabels;
    }

    public Boolean getShowdivlinevalues() {
        return showdivlinevalues;
    }

    public void setShowdivlinevalues(Boolean showdivlinevalues) {
        this.showdivlinevalues = showdivlinevalues;
    }

    public Integer getyaxisvaluesstep() {
        return yaxisvaluesstep;
    }

    public void setyaxisvaluesstep(Integer yaxisvaluesstep) {
        this.yaxisvaluesstep = yaxisvaluesstep;
    }

    public Boolean getShowShadow() {
        return showShadow;
    }

    public void setShowShadow(Boolean showShadow) {
        this.showShadow = showShadow;
    }

    public Boolean getAdjustdiv() {
        return adjustdiv;
    }

    public void setAdjustdiv(Boolean adjustdiv) {
        this.adjustdiv = adjustdiv;
    }

    public Boolean getRotateyaxisname() {
        return rotateyaxisname;
    }

    public void setRotateyaxisname(Boolean rotateyaxisname) {
        this.rotateyaxisname = rotateyaxisname;
    }

    public Integer getyaxisnamewidth() {
        return yaxisnamewidth;
    }

    public void setyaxisnamewidth(Integer yaxisnamewidth) {
        this.yaxisnamewidth = yaxisnamewidth;
    }

    public Integer getyaxisminvalue() {
        return yaxisminvalue;
    }

    public void setyaxisminvalue(Integer yaxisminvalue) {
        this.yaxisminvalue = yaxisminvalue;
    }

    public Integer getyaxismaxvalue() {
        return yaxismaxvalue;
    }

    public void setyaxismaxvalue(Integer yaxismaxvalue) {
        this.yaxismaxvalue = yaxismaxvalue;
    }

    public ChartCustomBean fillData(ChartCustomAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
