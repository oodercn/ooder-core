package net.ooder.esd.bean.fchart;

import com.alibaba.fastjson.JSON;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.fchart.ChartLabelAnnotation;
import net.ooder.esd.tool.properties.fchart.ChartData;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.Map;

@AnnotationType(clazz = ChartLabelAnnotation.class)
public class ChartLabelBean implements CustomBean {


    String labelfont;

    String labelfontcolor;

    Integer labelfontSize;

    Boolean labelfontbold;

    Boolean labelfontitalic;

    String labelbgcolor;

    String labelbordercolor;

    Integer labelalpha;

    Integer labelBgalpha;

    Integer labelborderalpha;

    Integer labelborderpadding;

    Integer labelborderradius;

    Integer labelborderthickness;

    Boolean labelborderdashed;

    Boolean labelborderdashlen;

    Integer labelborderdashgap;

    String labellink;


    public ChartLabelBean() {

    }

    public ChartLabelBean( ChartData chartData) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(chartData), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);

    }


    public ChartLabelBean(ChartLabelAnnotation annotation) {

        this.fillData(annotation);
    }

    public String getLabelfont() {
        return labelfont;
    }

    public void setLabelfont(String labelfont) {
        this.labelfont = labelfont;
    }

    public String getLabelfontcolor() {
        return labelfontcolor;
    }

    public void setLabelfontcolor(String labelfontcolor) {
        this.labelfontcolor = labelfontcolor;
    }

    public Integer getLabelfontSize() {
        return labelfontSize;
    }

    public void setLabelfontSize(Integer labelfontSize) {
        this.labelfontSize = labelfontSize;
    }

    public Boolean getLabelfontbold() {
        return labelfontbold;
    }

    public void setLabelfontbold(Boolean labelfontbold) {
        this.labelfontbold = labelfontbold;
    }

    public Boolean getLabelfontitalic() {
        return labelfontitalic;
    }

    public void setLabelfontitalic(Boolean labelfontitalic) {
        this.labelfontitalic = labelfontitalic;
    }

    public String getLabelbgcolor() {
        return labelbgcolor;
    }

    public void setLabelbgcolor(String labelbgcolor) {
        this.labelbgcolor = labelbgcolor;
    }

    public String getLabelbordercolor() {
        return labelbordercolor;
    }

    public void setLabelbordercolor(String labelbordercolor) {
        this.labelbordercolor = labelbordercolor;
    }

    public Integer getLabelalpha() {
        return labelalpha;
    }

    public void setLabelalpha(Integer labelalpha) {
        this.labelalpha = labelalpha;
    }

    public Integer getLabelBgalpha() {
        return labelBgalpha;
    }

    public void setLabelBgalpha(Integer labelBgalpha) {
        this.labelBgalpha = labelBgalpha;
    }

    public Integer getLabelborderalpha() {
        return labelborderalpha;
    }

    public void setLabelborderalpha(Integer labelborderalpha) {
        this.labelborderalpha = labelborderalpha;
    }

    public Integer getLabelborderpadding() {
        return labelborderpadding;
    }

    public void setLabelborderpadding(Integer labelborderpadding) {
        this.labelborderpadding = labelborderpadding;
    }

    public Integer getLabelborderradius() {
        return labelborderradius;
    }

    public void setLabelborderradius(Integer labelborderradius) {
        this.labelborderradius = labelborderradius;
    }

    public Integer getLabelborderthickness() {
        return labelborderthickness;
    }

    public void setLabelborderthickness(Integer labelborderthickness) {
        this.labelborderthickness = labelborderthickness;
    }

    public Boolean getLabelborderdashed() {
        return labelborderdashed;
    }

    public void setLabelborderdashed(Boolean labelborderdashed) {
        this.labelborderdashed = labelborderdashed;
    }

    public Boolean getLabelborderdashlen() {
        return labelborderdashlen;
    }

    public void setLabelborderdashlen(Boolean labelborderdashlen) {
        this.labelborderdashlen = labelborderdashlen;
    }

    public Integer getLabelborderdashgap() {
        return labelborderdashgap;
    }

    public void setLabelborderdashgap(Integer labelborderdashgap) {
        this.labelborderdashgap = labelborderdashgap;
    }

    public String getLabellink() {
        return labellink;
    }

    public void setLabellink(String labellink) {
        this.labellink = labellink;
    }

    public ChartLabelBean fillData(ChartLabelAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
