package net.ooder.esd.bean.fchart;

import com.alibaba.fastjson.JSON;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.fchart.ChartEffectAnnotation;
import net.ooder.esd.tool.properties.fchart.ChartData;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.Map;

@AnnotationType(clazz = ChartEffectAnnotation.class)
public class ChartEffectBean implements CustomBean {

    Boolean showhovereffect;

    Boolean plothovereffect;

    String plotfillhovercolor;

    Integer plotfillhoveralpha;


    public ChartEffectBean() {

    }

    public ChartEffectBean( ChartData chartData) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(chartData), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);

    }


    public ChartEffectBean(ChartEffectAnnotation annotation) {

        this.fillData(annotation);
    }

    public Boolean getShowhovereffect() {
        return showhovereffect;
    }

    public void setShowhovereffect(Boolean showhovereffect) {
        this.showhovereffect = showhovereffect;
    }

    public Boolean getPlothovereffect() {
        return plothovereffect;
    }

    public void setPlothovereffect(Boolean plothovereffect) {
        this.plothovereffect = plothovereffect;
    }

    public String getPlotfillhovercolor() {
        return plotfillhovercolor;
    }

    public void setPlotfillhovercolor(String plotfillhovercolor) {
        this.plotfillhovercolor = plotfillhovercolor;
    }

    public Integer getPlotfillhoveralpha() {
        return plotfillhoveralpha;
    }

    public void setPlotfillhoveralpha(Integer plotfillhoveralpha) {
        this.plotfillhoveralpha = plotfillhoveralpha;
    }

    public ChartEffectBean fillData(ChartEffectAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
