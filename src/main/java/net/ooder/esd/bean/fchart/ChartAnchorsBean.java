package net.ooder.esd.bean.fchart;


import com.alibaba.fastjson.JSON;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.fchart.ChartAnchorsAnnotation;
import net.ooder.esd.tool.properties.fchart.ChartData;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.Map;

@AnnotationType(clazz = ChartAnchorsAnnotation.class)
public class ChartAnchorsBean implements CustomBean {


    Boolean drawanchors;
    Integer anchorsides;
    Integer anchorradius;
    String anchorbordercolor;
    Integer anchorborderthickness;
    String anchorbgcolor;
    Integer anchoralpha;
    Integer anchorbgalpha;

    public ChartAnchorsBean() {

    }

    public ChartAnchorsBean(ChartData chartData) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(chartData), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);

    }


    public ChartAnchorsBean(ChartAnchorsAnnotation annotation) {

        this.fillData(annotation);
    }

    public Boolean getDrawanchors() {
        return drawanchors;
    }

    public void setDrawanchors(Boolean drawanchors) {
        this.drawanchors = drawanchors;
    }

    public Integer getAnchorsides() {
        return anchorsides;
    }

    public void setAnchorsides(Integer anchorsides) {
        this.anchorsides = anchorsides;
    }

    public Integer getAnchorradius() {
        return anchorradius;
    }

    public void setAnchorradius(Integer anchorradius) {
        this.anchorradius = anchorradius;
    }

    public String getAnchorbordercolor() {
        return anchorbordercolor;
    }

    public void setAnchorbordercolor(String anchorbordercolor) {
        this.anchorbordercolor = anchorbordercolor;
    }

    public Integer getAnchorborderthickness() {
        return anchorborderthickness;
    }

    public void setAnchorborderthickness(Integer anchorborderthickness) {
        this.anchorborderthickness = anchorborderthickness;
    }

    public String getAnchorbgcolor() {
        return anchorbgcolor;
    }

    public void setAnchorbgcolor(String anchorbgcolor) {
        this.anchorbgcolor = anchorbgcolor;
    }

    public Integer getAnchoralpha() {
        return anchoralpha;
    }

    public void setAnchoralpha(Integer anchoralpha) {
        this.anchoralpha = anchoralpha;
    }

    public Integer getAnchorbgalpha() {
        return anchorbgalpha;
    }

    public void setAnchorbgalpha(Integer anchorbgalpha) {
        this.anchorbgalpha = anchorbgalpha;
    }

    public ChartAnchorsBean fillData(ChartAnchorsAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
