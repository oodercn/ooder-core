package net.ooder.esd.bean.fchart;

import com.alibaba.fastjson.JSON;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.fchart.ChartXAnnotation;
import net.ooder.esd.tool.properties.fchart.ChartData;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.Map;

@AnnotationType(clazz = ChartXAnnotation.class)
public class ChartXBean implements CustomBean {


    String xaxisnamefontcolor;

    Integer xaxisnamefontsize;

    Boolean xaxisnamefontbold;

    Boolean axisnamefontitalic;

    String xaxisnamebgcolor;

    String xaxisnamebordercolor;

    Integer xaxisnamealpha;

    Integer xaxisnamefontalpha;

    Integer xaxisnamebgalpha;

    Integer xaxisnameborderalpha;

    Integer xaxisnameborderpadding;

    Integer xaxisnameborderradius;

    Integer xaxisnameborderthickness;

    Boolean xaxisnameborderdashed;

    Integer xaxisnameborderdashLen;

    Integer xaxisnameborderdashgap;

    public ChartXBean() {

    }

    public ChartXBean( ChartData chartData) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(chartData), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);

    }


    public ChartXBean(ChartXAnnotation annotation) {
        this.fillData(annotation);
    }

    public ChartXBean fillData(ChartXAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String getxaxisnamefontcolor() {
        return xaxisnamefontcolor;
    }

    public void setxaxisnamefontcolor(String xaxisnamefontcolor) {
        this.xaxisnamefontcolor = xaxisnamefontcolor;
    }

    public String getxaxisnamebgcolor() {
        return xaxisnamebgcolor;
    }

    public void setxaxisnamebgcolor(String xaxisnamebgcolor) {
        this.xaxisnamebgcolor = xaxisnamebgcolor;
    }

    public String getxaxisnamebordercolor() {
        return xaxisnamebordercolor;
    }

    public void setxaxisnamebordercolor(String xaxisnamebordercolor) {
        this.xaxisnamebordercolor = xaxisnamebordercolor;
    }

    public Integer getxaxisnamealpha() {
        return xaxisnamealpha;
    }

    public void setxaxisnamealpha(Integer xaxisnamealpha) {
        this.xaxisnamealpha = xaxisnamealpha;
    }

    public Integer getxaxisnamefontalpha() {
        return xaxisnamefontalpha;
    }

    public void setxaxisnamefontalpha(Integer xaxisnamefontalpha) {
        this.xaxisnamefontalpha = xaxisnamefontalpha;
    }

    public Integer getxaxisnamebgalpha() {
        return xaxisnamebgalpha;
    }

    public void setxaxisnamebgalpha(Integer xaxisnamebgalpha) {
        this.xaxisnamebgalpha = xaxisnamebgalpha;
    }

    public Integer getxaxisnameborderalpha() {
        return xaxisnameborderalpha;
    }

    public void setxaxisnameborderalpha(Integer xaxisnameborderalpha) {
        this.xaxisnameborderalpha = xaxisnameborderalpha;
    }

    public String getXaxisnamefontcolor() {
        return xaxisnamefontcolor;
    }

    public void setXaxisnamefontcolor(String xaxisnamefontcolor) {
        this.xaxisnamefontcolor = xaxisnamefontcolor;
    }

    public Integer getXaxisnamefontsize() {
        return xaxisnamefontsize;
    }

    public void setXaxisnamefontsize(Integer xaxisnamefontsize) {
        this.xaxisnamefontsize = xaxisnamefontsize;
    }

    public Boolean getXaxisnamefontbold() {
        return xaxisnamefontbold;
    }

    public void setXaxisnamefontbold(Boolean xaxisnamefontbold) {
        this.xaxisnamefontbold = xaxisnamefontbold;
    }

    public Boolean getAxisnamefontitalic() {
        return axisnamefontitalic;
    }

    public void setAxisnamefontitalic(Boolean axisnamefontitalic) {
        this.axisnamefontitalic = axisnamefontitalic;
    }

    public String getXaxisnamebgcolor() {
        return xaxisnamebgcolor;
    }

    public void setXaxisnamebgcolor(String xaxisnamebgcolor) {
        this.xaxisnamebgcolor = xaxisnamebgcolor;
    }

    public String getXaxisnamebordercolor() {
        return xaxisnamebordercolor;
    }

    public void setXaxisnamebordercolor(String xaxisnamebordercolor) {
        this.xaxisnamebordercolor = xaxisnamebordercolor;
    }

    public Integer getXaxisnamealpha() {
        return xaxisnamealpha;
    }

    public void setXaxisnamealpha(Integer xaxisnamealpha) {
        this.xaxisnamealpha = xaxisnamealpha;
    }

    public Integer getXaxisnamefontalpha() {
        return xaxisnamefontalpha;
    }

    public void setXaxisnamefontalpha(Integer xaxisnamefontalpha) {
        this.xaxisnamefontalpha = xaxisnamefontalpha;
    }

    public Integer getXaxisnamebgalpha() {
        return xaxisnamebgalpha;
    }

    public void setXaxisnamebgalpha(Integer xaxisnamebgalpha) {
        this.xaxisnamebgalpha = xaxisnamebgalpha;
    }

    public Integer getXaxisnameborderalpha() {
        return xaxisnameborderalpha;
    }

    public void setXaxisnameborderalpha(Integer xaxisnameborderalpha) {
        this.xaxisnameborderalpha = xaxisnameborderalpha;
    }

    public Integer getXaxisnameborderpadding() {
        return xaxisnameborderpadding;
    }

    public void setXaxisnameborderpadding(Integer xaxisnameborderpadding) {
        this.xaxisnameborderpadding = xaxisnameborderpadding;
    }

    public Integer getXaxisnameborderradius() {
        return xaxisnameborderradius;
    }

    public void setXaxisnameborderradius(Integer xaxisnameborderradius) {
        this.xaxisnameborderradius = xaxisnameborderradius;
    }

    public Integer getXaxisnameborderthickness() {
        return xaxisnameborderthickness;
    }

    public void setXaxisnameborderthickness(Integer xaxisnameborderthickness) {
        this.xaxisnameborderthickness = xaxisnameborderthickness;
    }

    public Boolean getXaxisnameborderdashed() {
        return xaxisnameborderdashed;
    }

    public void setXaxisnameborderdashed(Boolean xaxisnameborderdashed) {
        this.xaxisnameborderdashed = xaxisnameborderdashed;
    }

    public Integer getXaxisnameborderdashLen() {
        return xaxisnameborderdashLen;
    }

    public void setXaxisnameborderdashLen(Integer xaxisnameborderdashLen) {
        this.xaxisnameborderdashLen = xaxisnameborderdashLen;
    }

    public Integer getXaxisnameborderdashgap() {
        return xaxisnameborderdashgap;
    }

    public void setXaxisnameborderdashgap(Integer xaxisnameborderdashgap) {
        this.xaxisnameborderdashgap = xaxisnameborderdashgap;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
