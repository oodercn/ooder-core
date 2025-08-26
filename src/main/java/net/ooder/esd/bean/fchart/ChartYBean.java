package net.ooder.esd.bean.fchart;


import com.alibaba.fastjson.JSON;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.fchart.ChartYAnnotation;
import net.ooder.esd.tool.properties.fchart.ChartData;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.Map;

@AnnotationType(clazz = ChartYAnnotation.class)
public class ChartYBean implements CustomBean {

    String yaxisname;

    Integer yaxisvaluesstep;

    Integer yaxismaxvalue;

    String yaxisnamefont;

    String yaxisnamefontcolor;

    Integer yaxisnamefontsize;

    Boolean yaxisnamefontbold;

    Boolean yaxisnamefontitalic;

    String yaxisnamebgcolor;

    String yaxisnamebordercolor;

    public ChartYBean() {

    }

    public ChartYBean(ChartData chartData) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(chartData), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);

    }

    public ChartYBean(ChartYAnnotation annotation) {
        this.fillData(annotation);
    }

    public Integer getYaxisvaluesstep() {
        return yaxisvaluesstep;
    }

    public void setYaxisvaluesstep(Integer yaxisvaluesstep) {
        this.yaxisvaluesstep = yaxisvaluesstep;
    }

    public Integer getYaxismaxvalue() {
        return yaxismaxvalue;
    }

    public void setYaxismaxvalue(Integer yaxismaxvalue) {
        this.yaxismaxvalue = yaxismaxvalue;
    }

    public Integer getYaxisnamefontsize() {
        return yaxisnamefontsize;
    }

    public void setYaxisnamefontsize(Integer yaxisnamefontsize) {
        this.yaxisnamefontsize = yaxisnamefontsize;
    }

    public Boolean getYaxisnamefontbold() {
        return yaxisnamefontbold;
    }

    public void setYaxisnamefontbold(Boolean yaxisnamefontbold) {
        this.yaxisnamefontbold = yaxisnamefontbold;
    }

    public Boolean getYaxisnamefontitalic() {
        return yaxisnamefontitalic;
    }

    public void setYaxisnamefontitalic(Boolean yaxisnamefontitalic) {
        this.yaxisnamefontitalic = yaxisnamefontitalic;
    }


    public ChartYBean fillData(ChartYAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String getYaxisname() {
        return yaxisname;
    }

    public void setYaxisname(String yaxisname) {
        this.yaxisname = yaxisname;
    }


    public String getYaxisnamefont() {
        return yaxisnamefont;
    }

    public void setYaxisnamefont(String yaxisnamefont) {
        this.yaxisnamefont = yaxisnamefont;
    }

    public String getYaxisnamefontcolor() {
        return yaxisnamefontcolor;
    }

    public void setYaxisnamefontcolor(String yaxisnamefontcolor) {
        this.yaxisnamefontcolor = yaxisnamefontcolor;
    }

    public String getYaxisnamebgcolor() {
        return yaxisnamebgcolor;
    }

    public void setYaxisnamebgcolor(String yaxisnamebgcolor) {
        this.yaxisnamebgcolor = yaxisnamebgcolor;
    }

    public String getYaxisnamebordercolor() {
        return yaxisnamebordercolor;
    }

    public void setYaxisnamebordercolor(String yaxisnamebordercolor) {
        this.yaxisnamebordercolor = yaxisnamebordercolor;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
