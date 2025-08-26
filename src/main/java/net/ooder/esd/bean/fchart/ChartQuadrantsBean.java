package net.ooder.esd.bean.fchart;

import com.alibaba.fastjson.JSON;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.fchart.ChartQuadrantsAnnotation;
import net.ooder.esd.tool.properties.fchart.ChartData;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.Map;

@AnnotationType(clazz = ChartQuadrantsAnnotation.class)
public class ChartQuadrantsBean implements CustomBean {


    Boolean drawquadrant;
    Integer quadrantxval;
    Integer quadrantyval;
    String quadrantlinecolor;
    Integer quadrantlinethickness;
    Integer quadrantlinealpha;
    Integer quadrantlinedashed;
    Integer quadrantlinedashlen;
    Integer quadrantlinedashgap;
    String quadrantlabeltl;
    String quadrantlabeltr;
    String quadrantlabelbl;
    String quadrantlabelbr;
    Integer quadrantlabelpadding;

    public ChartQuadrantsBean() {

    }

    public ChartQuadrantsBean(ChartData chartData) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(chartData), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);

    }

    public ChartQuadrantsBean(ChartQuadrantsAnnotation annotation) {

        this.fillData(annotation);
    }

    public Boolean getDrawquadrant() {
        return drawquadrant;
    }

    public void setDrawquadrant(Boolean drawquadrant) {
        this.drawquadrant = drawquadrant;
    }

    public Integer getQuadrantxval() {
        return quadrantxval;
    }

    public void setQuadrantxval(Integer quadrantxval) {
        this.quadrantxval = quadrantxval;
    }

    public Integer getQuadrantyval() {
        return quadrantyval;
    }

    public void setQuadrantyval(Integer quadrantyval) {
        this.quadrantyval = quadrantyval;
    }

    public String getQuadrantlinecolor() {
        return quadrantlinecolor;
    }

    public void setQuadrantlinecolor(String quadrantlinecolor) {
        this.quadrantlinecolor = quadrantlinecolor;
    }

    public Integer getQuadrantlinethickness() {
        return quadrantlinethickness;
    }

    public void setQuadrantlinethickness(Integer quadrantlinethickness) {
        this.quadrantlinethickness = quadrantlinethickness;
    }

    public Integer getQuadrantlinealpha() {
        return quadrantlinealpha;
    }

    public void setQuadrantlinealpha(Integer quadrantlinealpha) {
        this.quadrantlinealpha = quadrantlinealpha;
    }

    public Integer getQuadrantlinedashed() {
        return quadrantlinedashed;
    }

    public void setQuadrantlinedashed(Integer quadrantlinedashed) {
        this.quadrantlinedashed = quadrantlinedashed;
    }

    public Integer getQuadrantlinedashlen() {
        return quadrantlinedashlen;
    }

    public void setQuadrantlinedashlen(Integer quadrantlinedashlen) {
        this.quadrantlinedashlen = quadrantlinedashlen;
    }

    public Integer getQuadrantlinedashgap() {
        return quadrantlinedashgap;
    }

    public void setQuadrantlinedashgap(Integer quadrantlinedashgap) {
        this.quadrantlinedashgap = quadrantlinedashgap;
    }

    public String getQuadrantlabeltl() {
        return quadrantlabeltl;
    }

    public void setQuadrantlabeltl(String quadrantlabeltl) {
        this.quadrantlabeltl = quadrantlabeltl;
    }

    public String getQuadrantlabeltr() {
        return quadrantlabeltr;
    }

    public void setQuadrantlabeltr(String quadrantlabeltr) {
        this.quadrantlabeltr = quadrantlabeltr;
    }

    public String getQuadrantlabelbl() {
        return quadrantlabelbl;
    }

    public void setQuadrantlabelbl(String quadrantlabelbl) {
        this.quadrantlabelbl = quadrantlabelbl;
    }

    public String getQuadrantlabelbr() {
        return quadrantlabelbr;
    }

    public void setQuadrantlabelbr(String quadrantlabelbr) {
        this.quadrantlabelbr = quadrantlabelbr;
    }

    public Integer getQuadrantlabelpadding() {
        return quadrantlabelpadding;
    }

    public void setQuadrantlabelpadding(Integer quadrantlabelpadding) {
        this.quadrantlabelpadding = quadrantlabelpadding;
    }




    public ChartQuadrantsBean fillData(ChartQuadrantsAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
