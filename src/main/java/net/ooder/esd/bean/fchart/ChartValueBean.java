package net.ooder.esd.bean.fchart;


import com.alibaba.fastjson.JSON;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.fchart.ChartValueAnnotation;
import net.ooder.esd.tool.properties.fchart.ChartData;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.Map;

@AnnotationType(clazz = ChartValueAnnotation.class)
public class ChartValueBean implements CustomBean {


    String valuefont;

    String valuefontcolor;

    Integer valuefontsize;

    Boolean valuefontbold;

    Boolean valuefontitalic;

    String valuebgcolor;

    String valuebordercolor;

    Integer valuealpha;

    Integer valuefontalpha;

    Integer valuebgalpha;

    Integer valueborderalpha;

    Integer valueborderthickness;

    Integer valueborderradius;

    Boolean valueborderdashed;

    Integer valueborderdashgap;

    Integer valueborderdashlen;

    Integer valuehoveralpha;

    Integer valuefontgoveralpha;

    Integer valuebggoveralpha;

    Integer valuebordergoveralpha;

    Boolean showvaluesongover;

    public ChartValueBean() {

    }

    public ChartValueBean( ChartData chartData) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(chartData), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);

    }


    public ChartValueBean(ChartValueAnnotation annotation) {
        this.fillData(annotation);
    }

    public ChartValueBean fillData(ChartValueAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String getValuefont() {
        return valuefont;
    }

    public void setValuefont(String valuefont) {
        this.valuefont = valuefont;
    }

    public String getValuefontcolor() {
        return valuefontcolor;
    }

    public void setValuefontcolor(String valuefontcolor) {
        this.valuefontcolor = valuefontcolor;
    }


    public String getValuebgcolor() {
        return valuebgcolor;
    }

    public void setValuebgcolor(String valuebgcolor) {
        this.valuebgcolor = valuebgcolor;
    }

    public String getValuebordercolor() {
        return valuebordercolor;
    }

    public void setValuebordercolor(String valuebordercolor) {
        this.valuebordercolor = valuebordercolor;
    }


    public Boolean getValuefontbold() {
        return valuefontbold;
    }

    public void setValuefontbold(Boolean valuefontbold) {
        this.valuefontbold = valuefontbold;
    }


    public Integer getValuealpha() {
        return valuealpha;
    }

    public void setValuealpha(Integer valuealpha) {
        this.valuealpha = valuealpha;
    }

    public Integer getValuefontalpha() {
        return valuefontalpha;
    }

    public void setValuefontalpha(Integer valuefontalpha) {
        this.valuefontalpha = valuefontalpha;
    }

    public Integer getValuebgalpha() {
        return valuebgalpha;
    }

    public void setValuebgalpha(Integer valuebgalpha) {
        this.valuebgalpha = valuebgalpha;
    }

    public Integer getValueborderalpha() {
        return valueborderalpha;
    }

    public void setValueborderalpha(Integer valueborderalpha) {
        this.valueborderalpha = valueborderalpha;
    }

    public Integer getValuefontsize() {
        return valuefontsize;
    }

    public void setValuefontsize(Integer valuefontsize) {
        this.valuefontsize = valuefontsize;
    }

    public Boolean getValuefontitalic() {
        return valuefontitalic;
    }

    public void setValuefontitalic(Boolean valuefontitalic) {
        this.valuefontitalic = valuefontitalic;
    }

    public Integer getValueborderthickness() {
        return valueborderthickness;
    }

    public void setValueborderthickness(Integer valueborderthickness) {
        this.valueborderthickness = valueborderthickness;
    }

    public Integer getValueborderradius() {
        return valueborderradius;
    }

    public void setValueborderradius(Integer valueborderradius) {
        this.valueborderradius = valueborderradius;
    }

    public Boolean getValueborderdashed() {
        return valueborderdashed;
    }

    public void setValueborderdashed(Boolean valueborderdashed) {
        this.valueborderdashed = valueborderdashed;
    }

    public Integer getValueborderdashgap() {
        return valueborderdashgap;
    }

    public void setValueborderdashgap(Integer valueborderdashgap) {
        this.valueborderdashgap = valueborderdashgap;
    }

    public Integer getValueborderdashlen() {
        return valueborderdashlen;
    }

    public void setValueborderdashlen(Integer valueborderdashlen) {
        this.valueborderdashlen = valueborderdashlen;
    }

    public Integer getValuehoveralpha() {
        return valuehoveralpha;
    }

    public void setValuehoveralpha(Integer valuehoveralpha) {
        this.valuehoveralpha = valuehoveralpha;
    }

    public Integer getValuefontgoveralpha() {
        return valuefontgoveralpha;
    }

    public void setValuefontgoveralpha(Integer valuefontgoveralpha) {
        this.valuefontgoveralpha = valuefontgoveralpha;
    }

    public Integer getValuebggoveralpha() {
        return valuebggoveralpha;
    }

    public void setValuebggoveralpha(Integer valuebggoveralpha) {
        this.valuebggoveralpha = valuebggoveralpha;
    }

    public Integer getValuebordergoveralpha() {
        return valuebordergoveralpha;
    }

    public void setValuebordergoveralpha(Integer valuebordergoveralpha) {
        this.valuebordergoveralpha = valuebordergoveralpha;
    }

    public Boolean getShowvaluesongover() {
        return showvaluesongover;
    }

    public void setShowvaluesongover(Boolean showvaluesongover) {
        this.showvaluesongover = showvaluesongover;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
