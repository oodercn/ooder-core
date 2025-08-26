package net.ooder.esd.bean.fchart;

import com.alibaba.fastjson.JSON;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.fchart.ChartCanvasCosmeticsAnnotation;
import net.ooder.esd.tool.properties.fchart.ChartData;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.Map;

@AnnotationType(clazz = ChartCanvasCosmeticsAnnotation.class)
public class ChartCanvasCosmeticsBean implements CustomBean {


    String canvasbgcolor;
    Integer canvasbgalpha;
    Integer canvasbgratio;
    Integer canvasbgangle;
    String canvasbordercolor;
    Integer canvasborderthickness;
    Integer canvasborderalpha;


    public ChartCanvasCosmeticsBean() {

    }

    public ChartCanvasCosmeticsBean(ChartData chartData) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(chartData), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);

    }

    public ChartCanvasCosmeticsBean(ChartCanvasCosmeticsAnnotation annotation) {

        this.fillData(annotation);
    }

    public String getCanvasbgcolor() {
        return canvasbgcolor;
    }

    public void setCanvasbgcolor(String canvasbgcolor) {
        this.canvasbgcolor = canvasbgcolor;
    }

    public Integer getCanvasbgalpha() {
        return canvasbgalpha;
    }

    public void setCanvasbgalpha(Integer canvasbgalpha) {
        this.canvasbgalpha = canvasbgalpha;
    }

    public Integer getCanvasbgratio() {
        return canvasbgratio;
    }

    public void setCanvasbgratio(Integer canvasbgratio) {
        this.canvasbgratio = canvasbgratio;
    }

    public Integer getCanvasbgangle() {
        return canvasbgangle;
    }

    public void setCanvasbgangle(Integer canvasbgangle) {
        this.canvasbgangle = canvasbgangle;
    }

    public String getCanvasbordercolor() {
        return canvasbordercolor;
    }

    public void setCanvasbordercolor(String canvasbordercolor) {
        this.canvasbordercolor = canvasbordercolor;
    }

    public Integer getCanvasborderthickness() {
        return canvasborderthickness;
    }

    public void setCanvasborderthickness(Integer canvasborderthickness) {
        this.canvasborderthickness = canvasborderthickness;
    }

    public Integer getCanvasborderalpha() {
        return canvasborderalpha;
    }

    public void setCanvasborderalpha(Integer canvasborderalpha) {
        this.canvasborderalpha = canvasborderalpha;
    }

    public ChartCanvasCosmeticsBean fillData(ChartCanvasCosmeticsAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
