package net.ooder.esd.bean.fchart;

import com.alibaba.fastjson.JSON;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.fchart.ChartLegentAnnotation;
import net.ooder.esd.annotation.fchart.LegendPosition;
import net.ooder.esd.tool.properties.fchart.ChartData;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.Map;

@AnnotationType(clazz = ChartLegentAnnotation.class)
public class ChartLegentBean implements CustomBean {


    Boolean showlegend;

    Boolean legenditemfontbold;

    String legenditemfont;

    Integer legenditemfontsize;

    String legenditemfontcolor;

    String legenditemhoverfontcolor;

    Boolean legendcaptionalignment;

    Boolean legendcaptionbold;

    String legendcaptionfont;

    Integer legendcaptionfontsize;

    Boolean legendiconscale;

    String legenditemhiddencolor;

    LegendPosition legendposition;

    String legendbgcolor;

    Integer legendbgalpha;

    String legendbordercolor;

    Integer legendborderthickness;

    Integer legendborderalpha;

    Boolean legendshadow;

    Boolean legendallowdrag;

    String legendscrollbgcolor;

    Boolean reverselegend;

    Boolean interactivelegend;

    Integer legendnumcolumns;

    Boolean minimisewrappinginlegend;

    public ChartLegentBean() {

    }

    public ChartLegentBean( ChartData chartData) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(chartData), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);

    }

    public ChartLegentBean(ChartLegentAnnotation annotation) {

        this.fillData(annotation);
    }

    public ChartLegentBean fillData(ChartLegentAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }


    public String getlegenditemfont() {
        return legenditemfont;
    }

    public void setlegenditemfont(String legenditemfont) {
        this.legenditemfont = legenditemfont;
    }

    public String getlegenditemfontcolor() {
        return legenditemfontcolor;
    }

    public void setlegenditemfontcolor(String legenditemfontcolor) {
        this.legenditemfontcolor = legenditemfontcolor;
    }


    public Boolean getShowlegend() {
        return showlegend;
    }

    public void setShowlegend(Boolean showlegend) {
        this.showlegend = showlegend;
    }


    public Integer getlegenditemfontsize() {
        return legenditemfontsize;
    }

    public void setlegenditemfontsize(Integer legenditemfontsize) {
        this.legenditemfontsize = legenditemfontsize;
    }


    public String getlegendcaptionfont() {
        return legendcaptionfont;
    }

    public void setlegendcaptionfont(String legendcaptionfont) {
        this.legendcaptionfont = legendcaptionfont;
    }

    public Integer getlegendcaptionfontsize() {
        return legendcaptionfontsize;
    }

    public void setlegendcaptionfontsize(Integer legendcaptionfontsize) {
        this.legendcaptionfontsize = legendcaptionfontsize;
    }


    public String getlegendbgcolor() {
        return legendbgcolor;
    }

    public Boolean getLegendiconscale() {
        return legendiconscale;
    }

    public void setLegendiconscale(Boolean legendiconscale) {
        this.legendiconscale = legendiconscale;
    }

    public Boolean getLegendallowdrag() {
        return legendallowdrag;
    }

    public void setLegendallowdrag(Boolean legendallowdrag) {
        this.legendallowdrag = legendallowdrag;
    }

    public void setlegendbgcolor(String legendbgcolor) {
        this.legendbgcolor = legendbgcolor;
    }


    public String getlegendbordercolor() {
        return legendbordercolor;
    }

    public void setlegendbordercolor(String legendbordercolor) {
        this.legendbordercolor = legendbordercolor;
    }


    public Boolean getReverselegend() {
        return reverselegend;
    }

    public void setReverselegend(Boolean reverselegend) {
        this.reverselegend = reverselegend;
    }

    public Boolean getInteractivelegend() {
        return interactivelegend;
    }

    public Boolean getLegenditemfontbold() {
        return legenditemfontbold;
    }

    public void setLegenditemfontbold(Boolean legenditemfontbold) {
        this.legenditemfontbold = legenditemfontbold;
    }

    public String getLegenditemfont() {
        return legenditemfont;
    }

    public void setLegenditemfont(String legenditemfont) {
        this.legenditemfont = legenditemfont;
    }

    public Integer getLegenditemfontsize() {
        return legenditemfontsize;
    }

    public void setLegenditemfontsize(Integer legenditemfontsize) {
        this.legenditemfontsize = legenditemfontsize;
    }

    public String getLegenditemfontcolor() {
        return legenditemfontcolor;
    }

    public void setLegenditemfontcolor(String legenditemfontcolor) {
        this.legenditemfontcolor = legenditemfontcolor;
    }

    public String getLegenditemhoverfontcolor() {
        return legenditemhoverfontcolor;
    }

    public void setLegenditemhoverfontcolor(String legenditemhoverfontcolor) {
        this.legenditemhoverfontcolor = legenditemhoverfontcolor;
    }

    public Boolean getLegendcaptionalignment() {
        return legendcaptionalignment;
    }

    public void setLegendcaptionalignment(Boolean legendcaptionalignment) {
        this.legendcaptionalignment = legendcaptionalignment;
    }

    public Boolean getLegendcaptionbold() {
        return legendcaptionbold;
    }

    public void setLegendcaptionbold(Boolean legendcaptionbold) {
        this.legendcaptionbold = legendcaptionbold;
    }

    public String getLegendcaptionfont() {
        return legendcaptionfont;
    }

    public void setLegendcaptionfont(String legendcaptionfont) {
        this.legendcaptionfont = legendcaptionfont;
    }

    public Integer getLegendcaptionfontsize() {
        return legendcaptionfontsize;
    }

    public void setLegendcaptionfontsize(Integer legendcaptionfontsize) {
        this.legendcaptionfontsize = legendcaptionfontsize;
    }


    public String getLegenditemhiddencolor() {
        return legenditemhiddencolor;
    }

    public void setLegenditemhiddencolor(String legenditemhiddencolor) {
        this.legenditemhiddencolor = legenditemhiddencolor;
    }

    public LegendPosition getLegendposition() {
        return legendposition;
    }

    public void setLegendposition(LegendPosition legendposition) {
        this.legendposition = legendposition;
    }

    public String getLegendbgcolor() {
        return legendbgcolor;
    }

    public void setLegendbgcolor(String legendbgcolor) {
        this.legendbgcolor = legendbgcolor;
    }

    public Integer getLegendbgalpha() {
        return legendbgalpha;
    }

    public void setLegendbgalpha(Integer legendbgalpha) {
        this.legendbgalpha = legendbgalpha;
    }

    public String getLegendbordercolor() {
        return legendbordercolor;
    }

    public void setLegendbordercolor(String legendbordercolor) {
        this.legendbordercolor = legendbordercolor;
    }

    public Integer getLegendborderthickness() {
        return legendborderthickness;
    }

    public void setLegendborderthickness(Integer legendborderthickness) {
        this.legendborderthickness = legendborderthickness;
    }

    public Integer getLegendborderalpha() {
        return legendborderalpha;
    }

    public void setLegendborderalpha(Integer legendborderalpha) {
        this.legendborderalpha = legendborderalpha;
    }

    public Boolean getLegendshadow() {
        return legendshadow;
    }

    public void setLegendshadow(Boolean legendshadow) {
        this.legendshadow = legendshadow;
    }


    public String getLegendscrollbgcolor() {
        return legendscrollbgcolor;
    }

    public void setLegendscrollbgcolor(String legendscrollbgcolor) {
        this.legendscrollbgcolor = legendscrollbgcolor;
    }

    public Integer getLegendnumcolumns() {
        return legendnumcolumns;
    }

    public void setLegendnumcolumns(Integer legendnumcolumns) {
        this.legendnumcolumns = legendnumcolumns;
    }

    public Boolean getMinimisewrappinginlegend() {
        return minimisewrappinginlegend;
    }

    public void setMinimisewrappinginlegend(Boolean minimisewrappinginlegend) {
        this.minimisewrappinginlegend = minimisewrappinginlegend;
    }

    public void setInteractivelegend(Boolean interactivelegend) {
        this.interactivelegend = interactivelegend;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
