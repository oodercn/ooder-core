package net.ooder.esd.bean.fchart;

import com.alibaba.fastjson.JSON;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.fchart.ChartScrollBarAnnotation;
import net.ooder.esd.tool.properties.fchart.ChartData;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.Map;

@AnnotationType(clazz = ChartScrollBarAnnotation.class)
public class ChartScrollBarBean implements CustomBean {


    String scrollcolor;
    Integer scrollheight;
    Integer scrollpadding;
    Integer crollbtnwidth;
    Integer scrollbtnpadding;

    public ChartScrollBarBean() {

    }

    public ChartScrollBarBean(ChartScrollBarAnnotation annotation) {

        this.fillData(annotation);
    }

    public ChartScrollBarBean(ChartData chartData) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(chartData), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);

    }


    public String getScrollcolor() {
        return scrollcolor;
    }

    public void setScrollcolor(String scrollcolor) {
        this.scrollcolor = scrollcolor;
    }

    public Integer getScrollheight() {
        return scrollheight;
    }

    public void setScrollheight(Integer scrollheight) {
        this.scrollheight = scrollheight;
    }

    public Integer getScrollpadding() {
        return scrollpadding;
    }

    public void setScrollpadding(Integer scrollpadding) {
        this.scrollpadding = scrollpadding;
    }

    public Integer getCrollbtnwidth() {
        return crollbtnwidth;
    }

    public void setCrollbtnwidth(Integer crollbtnwidth) {
        this.crollbtnwidth = crollbtnwidth;
    }

    public Integer getScrollbtnpadding() {
        return scrollbtnpadding;
    }

    public void setScrollbtnpadding(Integer scrollbtnpadding) {
        this.scrollbtnpadding = scrollbtnpadding;
    }



    public ChartScrollBarBean fillData(ChartScrollBarAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
