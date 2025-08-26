package net.ooder.esd.bean.fchart;

import com.alibaba.fastjson.JSON;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.fchart.ChartFontAnnotation;
import net.ooder.esd.tool.properties.fchart.ChartData;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.Map;

@AnnotationType(clazz = ChartFontAnnotation.class)
public class ChartFontBean implements CustomBean {


    String basefont;

    Integer basefontsize;

    String basefontcolor;

    String outcnvbasefont;

    Integer outcnvbasefontsize;

    String outcnvbasefontcolor;

    public ChartFontBean() {

    }

    public ChartFontBean( ChartData chartData) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(chartData), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);

    }

    public ChartFontBean(ChartFontAnnotation annotation) {

        this.fillData(annotation);
    }

    public Integer getBasefontsize() {
        return basefontsize;
    }

    public void setBasefontsize(Integer basefontsize) {
        this.basefontsize = basefontsize;
    }

    public Integer getOutcnvbasefontsize() {
        return outcnvbasefontsize;
    }

    public void setOutcnvbasefontsize(Integer outcnvbasefontsize) {
        this.outcnvbasefontsize = outcnvbasefontsize;
    }

    public String getBasefont() {
        return basefont;
    }

    public void setBasefont(String basefont) {
        this.basefont = basefont;
    }


    public String getOutcnvbasefont() {
        return outcnvbasefont;
    }

    public void setOutcnvbasefont(String outcnvbasefont) {
        this.outcnvbasefont = outcnvbasefont;
    }

    public String getBasefontcolor() {
        return basefontcolor;
    }

    public void setBasefontcolor(String basefontcolor) {
        this.basefontcolor = basefontcolor;
    }

    public String getOutcnvbasefontcolor() {
        return outcnvbasefontcolor;
    }

    public void setOutcnvbasefontcolor(String outcnvbasefontcolor) {
        this.outcnvbasefontcolor = outcnvbasefontcolor;
    }

    public ChartFontBean fillData(ChartFontAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
