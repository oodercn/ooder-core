package net.ooder.esd.bean.fchart;

import com.alibaba.fastjson.JSON;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.fchart.ChartDivisionalAnnotation;
import net.ooder.esd.tool.properties.fchart.ChartData;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.Map;

@AnnotationType(clazz = ChartDivisionalAnnotation.class)
public class ChartDivisionalBean implements CustomBean {


    Integer numdivlines;
    String divlinecolor;
    Integer divlinethickness;
    Integer divlinealpha;
    Integer divlinedashed;
    Integer divLinedashlen;
    Integer divLinedashgap;
    String zeroplanecolor;
    Integer zeroplanethickness;
    Integer zeroplanealpha;
    Boolean showzeroplanevalue;
    Boolean showalternatevgridcolor;
    String alternatevgridcolor;
    Integer alternatevgridalpha;

    public ChartDivisionalBean() {

    }

    public ChartDivisionalBean(ChartData chartData) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(chartData), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);

    }

    public ChartDivisionalBean(ChartDivisionalAnnotation annotation) {

        this.fillData(annotation);
    }

    public Integer getNumdivlines() {
        return numdivlines;
    }

    public void setNumdivlines(Integer numdivlines) {
        this.numdivlines = numdivlines;
    }

    public String getDivlinecolor() {
        return divlinecolor;
    }

    public void setDivlinecolor(String divlinecolor) {
        this.divlinecolor = divlinecolor;
    }

    public Integer getDivlinethickness() {
        return divlinethickness;
    }

    public void setDivlinethickness(Integer divlinethickness) {
        this.divlinethickness = divlinethickness;
    }

    public Integer getDivlinealpha() {
        return divlinealpha;
    }

    public void setDivlinealpha(Integer divlinealpha) {
        this.divlinealpha = divlinealpha;
    }

    public Integer getDivlinedashed() {
        return divlinedashed;
    }

    public void setDivlinedashed(Integer divlinedashed) {
        this.divlinedashed = divlinedashed;
    }

    public Integer getDivLinedashlen() {
        return divLinedashlen;
    }

    public void setDivLinedashlen(Integer divLinedashlen) {
        this.divLinedashlen = divLinedashlen;
    }

    public Integer getDivLinedashgap() {
        return divLinedashgap;
    }

    public void setDivLinedashgap(Integer divLinedashgap) {
        this.divLinedashgap = divLinedashgap;
    }

    public String getZeroplanecolor() {
        return zeroplanecolor;
    }

    public void setZeroplanecolor(String zeroplanecolor) {
        this.zeroplanecolor = zeroplanecolor;
    }

    public Integer getZeroplanethickness() {
        return zeroplanethickness;
    }

    public void setZeroplanethickness(Integer zeroplanethickness) {
        this.zeroplanethickness = zeroplanethickness;
    }

    public Integer getZeroplanealpha() {
        return zeroplanealpha;
    }

    public void setZeroplanealpha(Integer zeroplanealpha) {
        this.zeroplanealpha = zeroplanealpha;
    }

    public Boolean getShowzeroplanevalue() {
        return showzeroplanevalue;
    }

    public void setShowzeroplanevalue(Boolean showzeroplanevalue) {
        this.showzeroplanevalue = showzeroplanevalue;
    }

    public Boolean getShowalternatevgridcolor() {
        return showalternatevgridcolor;
    }

    public void setShowalternatevgridcolor(Boolean showalternatevgridcolor) {
        this.showalternatevgridcolor = showalternatevgridcolor;
    }

    public String getAlternatevgridcolor() {
        return alternatevgridcolor;
    }

    public void setAlternatevgridcolor(String alternatevgridcolor) {
        this.alternatevgridcolor = alternatevgridcolor;
    }

    public Integer getAlternatevgridalpha() {
        return alternatevgridalpha;
    }

    public void setAlternatevgridalpha(Integer alternatevgridalpha) {
        this.alternatevgridalpha = alternatevgridalpha;
    }

    public ChartDivisionalBean fillData(ChartDivisionalAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
