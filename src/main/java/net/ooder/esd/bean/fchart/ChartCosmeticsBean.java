package net.ooder.esd.bean.fchart;

import com.alibaba.fastjson.JSON;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.fchart.ChartCosmeticsAnnotation;
import net.ooder.esd.annotation.fchart.FChartDisplayMode;
import net.ooder.esd.annotation.fchart.FChartLogoPosition;
import net.ooder.esd.annotation.ui.HAlignType;
import net.ooder.esd.annotation.ui.VAlignType;
import net.ooder.esd.tool.properties.fchart.ChartData;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.Map;

@AnnotationType(clazz = ChartCosmeticsAnnotation.class)
public class ChartCosmeticsBean implements CustomBean {



    String bgcolor;

    Integer bgalpha;

    Integer bgratio;

    Integer bgangle;

    String bgimage;

    Integer bgimagealpha;

    FChartDisplayMode bgimagedisplaymode;

    VAlignType bgimagevalign;

    HAlignType bgimagehalign;

    Integer bgimagescale;

    String canvasbgcolor;

    Integer canvasbgalpha;

    Integer canvasbgratio;

    Integer canvasbgangle;

    String xaxislinecolor;

    String canvasbordercolor;

    Integer canvasborderthickness;

    Integer canvasborderalpha;

    Boolean showborder;

    String bordercolor;

    Integer borderthickness;

    Integer borderalpha;

    Boolean showvlinelabelborder;

    String logourl;

    FChartLogoPosition logoposition;

    Integer logoalpha;

    Integer logoscale;

    String logolink;


    public ChartCosmeticsBean( ChartData chartData) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(chartData), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);

    }



    public ChartCosmeticsBean() {

    }

    public ChartCosmeticsBean(ChartCosmeticsAnnotation annotation) {

        this.fillData(annotation);
    }

    public String getBgcolor() {
        return bgcolor;
    }

    public void setBgcolor(String bgcolor) {
        this.bgcolor = bgcolor;
    }

    public Integer getBgalpha() {
        return bgalpha;
    }

    public void setBgalpha(Integer bgalpha) {
        this.bgalpha = bgalpha;
    }

    public Integer getBgratio() {
        return bgratio;
    }

    public void setBgratio(Integer bgratio) {
        this.bgratio = bgratio;
    }

    public Integer getBgangle() {
        return bgangle;
    }

    public void setBgangle(Integer bgangle) {
        this.bgangle = bgangle;
    }

    public String getBgimage() {
        return bgimage;
    }

    public void setBgimage(String bgimage) {
        this.bgimage = bgimage;
    }

    public Integer getBgimagealpha() {
        return bgimagealpha;
    }

    public void setBgimagealpha(Integer bgimagealpha) {
        this.bgimagealpha = bgimagealpha;
    }

    public FChartDisplayMode getBgimagedisplaymode() {
        return bgimagedisplaymode;
    }

    public void setBgimagedisplaymode(FChartDisplayMode bgimagedisplaymode) {
        this.bgimagedisplaymode = bgimagedisplaymode;
    }

    public VAlignType getBgimagevalign() {
        return bgimagevalign;
    }

    public void setBgimagevalign(VAlignType bgimagevalign) {
        this.bgimagevalign = bgimagevalign;
    }

    public HAlignType getBgimagehalign() {
        return bgimagehalign;
    }

    public void setBgimagehalign(HAlignType bgimagehalign) {
        this.bgimagehalign = bgimagehalign;
    }

    public Integer getBgimagescale() {
        return bgimagescale;
    }

    public void setBgimagescale(Integer bgimagescale) {
        this.bgimagescale = bgimagescale;
    }

    public String getCanvasbgcolor() {
        return canvasbgcolor;
    }

    public void setCanvasbgcolor(String canvasbgcolor) {
        this.canvasbgcolor = canvasbgcolor;
    }

    public Integer getCanvasbgalpha() {
        return canvasbgalpha;
    }

    public void setCanvasbgalpha(Integer canvasbgalpha) {
        this.canvasbgalpha = canvasbgalpha;
    }

    public Integer getCanvasbgratio() {
        return canvasbgratio;
    }

    public void setCanvasbgratio(Integer canvasbgratio) {
        this.canvasbgratio = canvasbgratio;
    }

    public Integer getCanvasbgangle() {
        return canvasbgangle;
    }

    public void setCanvasbgangle(Integer canvasbgangle) {
        this.canvasbgangle = canvasbgangle;
    }

    public String getXaxislinecolor() {
        return xaxislinecolor;
    }

    public void setXaxislinecolor(String xaxislinecolor) {
        this.xaxislinecolor = xaxislinecolor;
    }

    public String getCanvasbordercolor() {
        return canvasbordercolor;
    }

    public void setCanvasbordercolor(String canvasbordercolor) {
        this.canvasbordercolor = canvasbordercolor;
    }

    public Integer getCanvasborderthickness() {
        return canvasborderthickness;
    }

    public void setCanvasborderthickness(Integer canvasborderthickness) {
        this.canvasborderthickness = canvasborderthickness;
    }

    public Integer getCanvasborderalpha() {
        return canvasborderalpha;
    }

    public void setCanvasborderalpha(Integer canvasborderalpha) {
        this.canvasborderalpha = canvasborderalpha;
    }

    public Boolean getShowborder() {
        return showborder;
    }

    public void setShowborder(Boolean showborder) {
        this.showborder = showborder;
    }

    public String getBordercolor() {
        return bordercolor;
    }

    public void setBordercolor(String bordercolor) {
        this.bordercolor = bordercolor;
    }

    public Integer getBorderthickness() {
        return borderthickness;
    }

    public void setBorderthickness(Integer borderthickness) {
        this.borderthickness = borderthickness;
    }

    public Integer getBorderalpha() {
        return borderalpha;
    }

    public void setBorderalpha(Integer borderalpha) {
        this.borderalpha = borderalpha;
    }

    public Boolean getShowvlinelabelborder() {
        return showvlinelabelborder;
    }

    public void setShowvlinelabelborder(Boolean showvlinelabelborder) {
        this.showvlinelabelborder = showvlinelabelborder;
    }

    public String getLogourl() {
        return logourl;
    }

    public void setLogourl(String logourl) {
        this.logourl = logourl;
    }

    public FChartLogoPosition getLogoposition() {
        return logoposition;
    }

    public void setLogoposition(FChartLogoPosition logoposition) {
        this.logoposition = logoposition;
    }

    public Integer getLogoalpha() {
        return logoalpha;
    }

    public void setLogoalpha(Integer logoalpha) {
        this.logoalpha = logoalpha;
    }

    public Integer getLogoscale() {
        return logoscale;
    }

    public void setLogoscale(Integer logoscale) {
        this.logoscale = logoscale;
    }

    public String getLogolink() {
        return logolink;
    }

    public void setLogolink(String logolink) {
        this.logolink = logolink;
    }

    public ChartCosmeticsBean fillData(ChartCosmeticsAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
