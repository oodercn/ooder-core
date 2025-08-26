package net.ooder.esd.bean.fchart;

import com.alibaba.fastjson.JSON;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.fchart.ChartCaptionAnnotation;
import net.ooder.esd.annotation.ui.AlignType;
import net.ooder.esd.tool.properties.fchart.ChartData;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.Map;

@AnnotationType(clazz = ChartCaptionAnnotation.class)
public class ChartCaptionBean implements CustomBean {


    AlignType captionalignment;

    Boolean captionontop;

    Integer captionfontsize;

    Integer subCaptionfontsize;

    String captionfont;

    String subcaptionfont;

    String captionfontcolor;

    String subcaptionfontcolor;

    Boolean captionfontbold;

    Boolean subcaptionfontbold;

    Boolean aligncaptionwithcanvas;

    Integer captionhorizontalpadding;


    public ChartCaptionBean( ChartData chartData) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(chartData), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);

    }



    public ChartCaptionBean(ChartCaptionAnnotation annotation) {
        this.fillData(annotation);
    }

    public ChartCaptionBean() {

    }

    public AlignType getCaptionalignment() {
        return captionalignment;
    }

    public void setCaptionalignment(AlignType captionalignment) {
        this.captionalignment = captionalignment;
    }

    public Boolean getCaptionontop() {
        return captionontop;
    }

    public void setCaptionontop(Boolean captionontop) {
        this.captionontop = captionontop;
    }

    public Integer getCaptionfontsize() {
        return captionfontsize;
    }

    public void setCaptionfontsize(Integer captionfontsize) {
        this.captionfontsize = captionfontsize;
    }

    public Integer getSubCaptionfontsize() {
        return subCaptionfontsize;
    }

    public void setSubCaptionfontsize(Integer subCaptionfontsize) {
        this.subCaptionfontsize = subCaptionfontsize;
    }

    public String getCaptionfont() {
        return captionfont;
    }

    public void setCaptionfont(String captionfont) {
        this.captionfont = captionfont;
    }

    public String getSubcaptionfont() {
        return subcaptionfont;
    }

    public void setSubcaptionfont(String subcaptionfont) {
        this.subcaptionfont = subcaptionfont;
    }

    public String getCaptionfontcolor() {
        return captionfontcolor;
    }

    public void setCaptionfontcolor(String captionfontcolor) {
        this.captionfontcolor = captionfontcolor;
    }

    public String getSubcaptionfontcolor() {
        return subcaptionfontcolor;
    }

    public void setSubcaptionfontcolor(String subcaptionfontcolor) {
        this.subcaptionfontcolor = subcaptionfontcolor;
    }

    public Boolean getCaptionfontbold() {
        return captionfontbold;
    }

    public void setCaptionfontbold(Boolean captionfontbold) {
        this.captionfontbold = captionfontbold;
    }

    public Boolean getSubcaptionfontbold() {
        return subcaptionfontbold;
    }

    public void setSubcaptionfontbold(Boolean subcaptionfontbold) {
        this.subcaptionfontbold = subcaptionfontbold;
    }

    public Boolean getAligncaptionwithcanvas() {
        return aligncaptionwithcanvas;
    }

    public void setAligncaptionwithcanvas(Boolean aligncaptionwithcanvas) {
        this.aligncaptionwithcanvas = aligncaptionwithcanvas;
    }

    public Integer getCaptionhorizontalpadding() {
        return captionhorizontalpadding;
    }

    public void setCaptionhorizontalpadding(Integer captionhorizontalpadding) {
        this.captionhorizontalpadding = captionhorizontalpadding;
    }

    ;


    public ChartCaptionBean fillData(ChartCaptionAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
