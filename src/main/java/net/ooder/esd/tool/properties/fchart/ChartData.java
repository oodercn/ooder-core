package net.ooder.esd.tool.properties.fchart;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.annotation.fchart.FChartDisplayMode;
import net.ooder.esd.annotation.fchart.FChartLogoPosition;
import net.ooder.esd.annotation.fchart.LegendPosition;
import net.ooder.esd.annotation.ui.AlignType;
import net.ooder.esd.annotation.ui.HAlignType;
import net.ooder.esd.annotation.ui.VAlignType;
import net.ooder.esd.bean.fchart.ChartDataBean;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.Map;

public class ChartData {

    AlignType captionalignment;

    Boolean captionontop;

    Integer captionfontsize;

    Integer subcaptionfontsize;

    String captionfont;

    String subcaptionfont;

    String captionfontcolor;

    String subcaptionfontcolor;

    Boolean captionfontbold;

    Boolean subcaptionfontbold;

    Boolean aligncaptionwithcanvas;

    Integer captionhorizontalpadding;


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


    Boolean animation;

    Integer palette;

    String palettecolors;

    Boolean showlabels;

    Integer maxlabelheight;

    Boolean labeldisplay;

    Boolean useellipseswhenoverflow;

    Boolean rotatelabels;

    Boolean slantlabels;

    Integer labelstep;

    Integer staggerlines;

    Boolean showvalues;

    Boolean rotatevalues;

    Boolean placevaluesinside;

    Boolean showyaxisvalues;

    Boolean showlimits;

    Boolean showdivlinevalues;

    Integer yaxisvaluesstep;

    Boolean showshadow;

    Boolean adjustdiv;

    Boolean rotateyaxisname;

    Integer yaxisnamewidth;

    String clickurl;

    Integer yaxisminvalue;

    Integer yaxismaxvalue;

    Integer setadaptiveyMin;

    Boolean usedataplotcolorforlabels;


    String caption;

    String subcaption;

    String xaxisname;

    String yaxisname;


    Boolean showhovereffect;

    Boolean plothovereffect;

    String plotfillhovercolor;

    Integer plotfillhoveralpha;


    String basefont;

    Integer basefontsize;

    String basefontcolor;

    String outcnvbasefont;

    Integer outcnvbasefontsize;

    String outcnvbasefontcolor;


    String labelfont;

    String labelfontcolor;

    Integer labelfontsize;

    Boolean labelfontbold;

    Boolean labelfontitalic;

    String labelbgcolor;

    String labelbordercolor;

    Integer labelalpha;

    Integer labelbgalpha;

    Integer labelborderalpha;

    Integer labelborderpadding;

    Integer labelborderradius;

    Integer labelborderthickness;

    Boolean labelborderdashed;

    Boolean labelborderdashLen;

    Integer labelborderdashgap;

    String labellink;


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


    Boolean formatnumber;

    Boolean formatnumberscale;

    String defaultnumberscale;

    String numberscaleunit;

    String numberscalevalue;

    Boolean scalerecursively;

    Integer maxscalerecursion;

    String scaleseparator;

    String numberprefix;

    String numbersuffix;

    String decimalseparator;

    String thousandseparator;

    Integer thousandseparatorposition;

    String indecimalseparator;

    String inthousandseparator;

    Integer decimals;

    Boolean forcedecimals;

    Boolean forceyaxisValueDecimals;

    Integer yaxisvaluedecimals;


    Integer captionpadding;

    Integer xaxisnamepadding;

    Integer yaxisnamepadding;

    Integer yaxivaluespadding;

    Integer labelpadding;

    Integer valuepadding;

    Integer plotspacepercent;

    Integer chartleftmargin;

    Integer chartrightmargin;

    Integer charttopmargin;

    Integer chartbottommargin;

    Integer canvasleftmargin;

    Integer canvasrightmargin;

    Integer canvastopmargin;

    Integer canvasbottommargin;


    Boolean showplotborder;

    Boolean useroundedges;

    String plotbordercolor;

    Integer plotborderthickness;

    Integer plotborderalpha;

    Boolean plotborderdashed;

    Integer plotborderdashLen;

    Integer plotborderdashgap;

    Integer plotfillangle;

    Integer plotfillratio;

    Integer plotfillalpha;

    String plotgradientcolor;


    Boolean showtooltip;

    String tooltipcolor;

    String tooltipbgcolor;

    String tooltipbordercolor;

    String tooltipsepchar;

    Boolean seriesnameintooltip;

    Boolean showtooltipshadow;


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

    Integer valuebordedashlen;

    Integer valuehoveralpha;

    Integer valuefonthoveralpha;

    Integer valuebghoveralpha;

    Integer valueborderhoveralpha;

    Boolean showvaluesonhover;


    String xaxisnamefontcolor;

    Integer xaxisnamefontsize;

    Boolean xaxisnamefontbold;

    Boolean axisnamefontitalic;

    String xaxisbamebgcolor;

    String xaxisnamebordercolor;

    Integer xaxisnamealpha;

    Integer xaxisnamefontalpha;

    Integer xaxisbamebgalpha;

    Integer xaxisnameborderalpha;

    Integer xaxisborderborderpadding;

    Integer xaxisnameborderRadius;

    Integer xaxisnameborderThickness;

    Boolean xaxisnameborderDashed;

    Integer xaxisnameborderDashLen;

    Integer xaxisnameborderDashGap;


    String yaxisnamefont;

    String yaxisnamefontcolor;

    Integer yaxisnamefontsize;

    Boolean yaxisnamefontbold;

    Boolean yaxisnamefontitalic;

    String yaxisbamebgcolor;

    String yaxisnamebordercolor;


    Boolean is2d;
    Boolean clustered;
    String chartorder;
    Boolean chartontop;
    Boolean autoscaling;
    Boolean allowscaling;

    Integer startangx;
    Integer startangy;
    Integer endangx;
    Integer endangy;
    Integer cameraangx;
    Integer cameraangy;
    Integer lightangx;
    Integer lightangy;
    Integer intensity;
    Boolean dynamicshading;
    Boolean bright2d;
    Boolean allowrotation;
    Boolean constrainverticalrotation;
    Integer minVerticalrotangle;
    Integer maxVerticalrotangle;
    Boolean constrainhorizontalrotation;
    Integer minhorizontalrotangle;
    Integer maxhorizontalrotangle;

    Integer zdepth;
    Integer zgapplot;

    Integer yzwalldepth;
    Integer zxwalldepth;
    Integer xywalldepth;


    Boolean drawanchors;
    Integer anchorsides;
    Integer anchorradius;
    String anchorbordercolor;
    Integer anchorborderthickness;
    String anchorbgcolor;
    Integer anchoralpha;
    Integer anchorbgalpha;



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


    Boolean animate3d;
    Integer exetime;

    Boolean connectnulldata;
    Integer labeldtep;
    Integer yaxisbaluesstep;
    Integer setadaptiveymin;
    String xaxistickcolor;
    Integer xaxistickalpha;
    Integer xaxistickthickness;


    Boolean drawquadrant;
    Integer quadrantxval;
    Integer quadrantyval;
    String quadrantlinecolor;
    Integer quadrantlinethickness;
    Integer quadrantlinealpha;
    Integer quadrantlinedashed;
    Integer quadrantlinedashlen;
    Integer quadrantlinedashgap;
    String quadrantlabeltl;
    String quadrantlabeltr;
    String quadrantlabelbl;
    String quadrantlabelbr;
    Integer quadrantlabelpadding;


    String scrollcolor;
    Integer scrollheight;
    Integer scrollpadding;
    Integer crollbtnwidth;
    Integer scrollbtnpadding;


    public ChartData() {

    }

    public ChartData(ChartDataBean chartDataBean) {

        this.caption = chartDataBean.getCaption();

        this.subcaption = chartDataBean.getSubcaption();

        this.xaxisname = chartDataBean.getXaxisname();

        this.yaxisname = chartDataBean.getYaxisname();


        if (chartDataBean.getCaptionBean() != null) {
            OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(chartDataBean.getCaptionBean()), Map.class), this, false, false);

        }
        if (chartDataBean.getCosmeticsBean() != null) {
            OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(chartDataBean.getCosmeticsBean()), Map.class), this, false, false);
        }
        if (chartDataBean.getCustomBean() != null) {
            OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(chartDataBean.getCustomBean()), Map.class), this, false, false);
        }
        if (chartDataBean.getEffectBean() != null) {
            OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(chartDataBean.getEffectBean()), Map.class), this, false, false);
        }
        if (chartDataBean.getFontBean() != null) {
            OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(chartDataBean.getFontBean()), Map.class), this, false, false);
        }
        if (chartDataBean.getLabelBean() != null) {
            OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(chartDataBean.getLabelBean()), Map.class), this, false, false);
        }
        if (chartDataBean.getLegentBean() != null) {
            OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(chartDataBean.getLegentBean()), Map.class), this, false, false);
        }
        if (chartDataBean.getNumberBean() != null) {
            OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(chartDataBean.getNumberBean()), Map.class), this, false, false);
        }
        if (chartDataBean.getPlotBean() != null) {
            OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(chartDataBean.getPlotBean()), Map.class), this, false, false);
        }
        if (chartDataBean.getToolTipBean() != null) {
            OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(chartDataBean.getToolTipBean()), Map.class), this, false, false);
        }
        if (chartDataBean.getValueBean() != null) {
            OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(chartDataBean.getValueBean()), Map.class), this, false, false);
        }


        if (chartDataBean.getPaddingsBean() != null) {
            OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(chartDataBean.getPaddingsBean()), Map.class), this, false, false);
        }
        if (chartDataBean.getFunctionalBean() != null) {
            OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(chartDataBean.getFunctionalBean()), Map.class), this, false, false);
        }
        if (chartDataBean.getFunctional3D() != null) {
            OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(chartDataBean.getFunctional3D()), Map.class), this, false, false);
        }
        if (chartDataBean.getAnchorsBean() != null) {
            OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(chartDataBean.getAnchorsBean()), Map.class), this, false, false);
        }

        if (chartDataBean.getCanvasCosmeticsBean() != null) {
            OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(chartDataBean.getCanvasCosmeticsBean()), Map.class), this, false, false);
        }
        if (chartDataBean.getQuadrantsBean() != null) {
            OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(chartDataBean.getQuadrantsBean()), Map.class), this, false, false);
        }
        if (chartDataBean.getScrollBarBean() != null) {
            OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(chartDataBean.getScrollBarBean()), Map.class), this, false, false);
        }
        if (chartDataBean.getDivisionalBean() != null) {
            OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(chartDataBean.getDivisionalBean()), Map.class), this, false, false);
        }


    }


    public Boolean getIs2d() {
        return is2d;
    }

    public void setIs2d(Boolean is2d) {
        this.is2d = is2d;
    }

    public Boolean getClustered() {
        return clustered;
    }

    public void setClustered(Boolean clustered) {
        this.clustered = clustered;
    }

    public String getChartorder() {
        return chartorder;
    }

    public void setChartorder(String chartorder) {
        this.chartorder = chartorder;
    }

    public Boolean getChartontop() {
        return chartontop;
    }

    public void setChartontop(Boolean chartontop) {
        this.chartontop = chartontop;
    }

    public Boolean getAutoscaling() {
        return autoscaling;
    }

    public void setAutoscaling(Boolean autoscaling) {
        this.autoscaling = autoscaling;
    }

    public Boolean getAllowscaling() {
        return allowscaling;
    }

    public void setAllowscaling(Boolean allowscaling) {
        this.allowscaling = allowscaling;
    }

    public Integer getStartangx() {
        return startangx;
    }

    public void setStartangx(Integer startangx) {
        this.startangx = startangx;
    }

    public Integer getStartangy() {
        return startangy;
    }

    public void setStartangy(Integer startangy) {
        this.startangy = startangy;
    }

    public Integer getEndangx() {
        return endangx;
    }

    public void setEndangx(Integer endangx) {
        this.endangx = endangx;
    }

    public Integer getEndangy() {
        return endangy;
    }

    public void setEndangy(Integer endangy) {
        this.endangy = endangy;
    }

    public Integer getCameraangx() {
        return cameraangx;
    }

    public void setCameraangx(Integer cameraangx) {
        this.cameraangx = cameraangx;
    }

    public Integer getCameraangy() {
        return cameraangy;
    }

    public void setCameraangy(Integer cameraangy) {
        this.cameraangy = cameraangy;
    }

    public Integer getLightangx() {
        return lightangx;
    }

    public void setLightangx(Integer lightangx) {
        this.lightangx = lightangx;
    }

    public Integer getLightangy() {
        return lightangy;
    }

    public void setLightangy(Integer lightangy) {
        this.lightangy = lightangy;
    }

    public Integer getIntensity() {
        return intensity;
    }

    public void setIntensity(Integer intensity) {
        this.intensity = intensity;
    }

    public Boolean getDynamicshading() {
        return dynamicshading;
    }

    public void setDynamicshading(Boolean dynamicshading) {
        this.dynamicshading = dynamicshading;
    }

    public Boolean getBright2d() {
        return bright2d;
    }

    public void setBright2d(Boolean bright2d) {
        this.bright2d = bright2d;
    }

    public Boolean getAllowrotation() {
        return allowrotation;
    }

    public void setAllowrotation(Boolean allowrotation) {
        this.allowrotation = allowrotation;
    }

    public Boolean getConstrainverticalrotation() {
        return constrainverticalrotation;
    }

    public void setConstrainverticalrotation(Boolean constrainverticalrotation) {
        this.constrainverticalrotation = constrainverticalrotation;
    }

    public Integer getMinVerticalrotangle() {
        return minVerticalrotangle;
    }

    public void setMinVerticalrotangle(Integer minVerticalrotangle) {
        this.minVerticalrotangle = minVerticalrotangle;
    }

    public Integer getMaxVerticalrotangle() {
        return maxVerticalrotangle;
    }

    public void setMaxVerticalrotangle(Integer maxVerticalrotangle) {
        this.maxVerticalrotangle = maxVerticalrotangle;
    }

    public Boolean getConstrainhorizontalrotation() {
        return constrainhorizontalrotation;
    }

    public void setConstrainhorizontalrotation(Boolean constrainhorizontalrotation) {
        this.constrainhorizontalrotation = constrainhorizontalrotation;
    }

    public Integer getMinhorizontalrotangle() {
        return minhorizontalrotangle;
    }

    public void setMinhorizontalrotangle(Integer minhorizontalrotangle) {
        this.minhorizontalrotangle = minhorizontalrotangle;
    }

    public Integer getMaxhorizontalrotangle() {
        return maxhorizontalrotangle;
    }

    public void setMaxhorizontalrotangle(Integer maxhorizontalrotangle) {
        this.maxhorizontalrotangle = maxhorizontalrotangle;
    }

    public Boolean getShowplotborder() {
        return showplotborder;
    }

    public void setShowplotborder(Boolean showplotborder) {
        this.showplotborder = showplotborder;
    }

    public Integer getZdepth() {
        return zdepth;
    }

    public void setZdepth(Integer zdepth) {
        this.zdepth = zdepth;
    }

    public Integer getZgapplot() {
        return zgapplot;
    }

    public void setZgapplot(Integer zgapplot) {
        this.zgapplot = zgapplot;
    }

    public Integer getYzwalldepth() {
        return yzwalldepth;
    }

    public void setYzwalldepth(Integer yzwalldepth) {
        this.yzwalldepth = yzwalldepth;
    }

    public Integer getZxwalldepth() {
        return zxwalldepth;
    }

    public void setZxwalldepth(Integer zxwalldepth) {
        this.zxwalldepth = zxwalldepth;
    }

    public Integer getXywalldepth() {
        return xywalldepth;
    }

    public void setXywalldepth(Integer xywalldepth) {
        this.xywalldepth = xywalldepth;
    }

    public Boolean getDrawanchors() {
        return drawanchors;
    }

    public void setDrawanchors(Boolean drawanchors) {
        this.drawanchors = drawanchors;
    }

    public Integer getAnchorsides() {
        return anchorsides;
    }

    public void setAnchorsides(Integer anchorsides) {
        this.anchorsides = anchorsides;
    }

    public Integer getAnchorradius() {
        return anchorradius;
    }

    public void setAnchorradius(Integer anchorradius) {
        this.anchorradius = anchorradius;
    }

    public String getAnchorbordercolor() {
        return anchorbordercolor;
    }

    public void setAnchorbordercolor(String anchorbordercolor) {
        this.anchorbordercolor = anchorbordercolor;
    }

    public Integer getAnchorborderthickness() {
        return anchorborderthickness;
    }

    public void setAnchorborderthickness(Integer anchorborderthickness) {
        this.anchorborderthickness = anchorborderthickness;
    }

    public String getAnchorbgcolor() {
        return anchorbgcolor;
    }

    public void setAnchorbgcolor(String anchorbgcolor) {
        this.anchorbgcolor = anchorbgcolor;
    }

    public Integer getAnchoralpha() {
        return anchoralpha;
    }

    public void setAnchoralpha(Integer anchoralpha) {
        this.anchoralpha = anchoralpha;
    }

    public Integer getAnchorbgalpha() {
        return anchorbgalpha;
    }

    public void setAnchorbgalpha(Integer anchorbgalpha) {
        this.anchorbgalpha = anchorbgalpha;
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

    public AlignType getCaptionalignment() {
        return captionalignment;
    }

    public void setCaptionalignment(AlignType captionalignment) {
        this.captionalignment = captionalignment;
    }

    public Boolean getCaptionontop() {
        return captionontop;
    }

    public void setCaptionontop(Boolean captionontop) {
        this.captionontop = captionontop;
    }

    public Integer getCaptionfontsize() {
        return captionfontsize;
    }

    public void setCaptionfontsize(Integer captionfontsize) {
        this.captionfontsize = captionfontsize;
    }

    public Integer getSubcaptionfontsize() {
        return subcaptionfontsize;
    }

    public void setSubcaptionfontsize(Integer subcaptionfontsize) {
        this.subcaptionfontsize = subcaptionfontsize;
    }

    public String getCaptionfont() {
        return captionfont;
    }

    public void setCaptionfont(String captionfont) {
        this.captionfont = captionfont;
    }

    public String getSubcaptionfont() {
        return subcaptionfont;
    }

    public void setSubcaptionfont(String subcaptionfont) {
        this.subcaptionfont = subcaptionfont;
    }

    public String getCaptionfontcolor() {
        return captionfontcolor;
    }

    public void setCaptionfontcolor(String captionfontcolor) {
        this.captionfontcolor = captionfontcolor;
    }

    public String getSubcaptionfontcolor() {
        return subcaptionfontcolor;
    }

    public void setSubcaptionfontcolor(String subcaptionfontcolor) {
        this.subcaptionfontcolor = subcaptionfontcolor;
    }

    public Boolean getCaptionfontbold() {
        return captionfontbold;
    }

    public void setCaptionfontbold(Boolean captionfontbold) {
        this.captionfontbold = captionfontbold;
    }

    public Boolean getSubcaptionfontbold() {
        return subcaptionfontbold;
    }

    public void setSubcaptionfontbold(Boolean subcaptionfontbold) {
        this.subcaptionfontbold = subcaptionfontbold;
    }

    public Boolean getAligncaptionwithcanvas() {
        return aligncaptionwithcanvas;
    }

    public void setAligncaptionwithcanvas(Boolean aligncaptionwithcanvas) {
        this.aligncaptionwithcanvas = aligncaptionwithcanvas;
    }

    public Integer getCaptionhorizontalpadding() {
        return captionhorizontalpadding;
    }

    public void setCaptionhorizontalpadding(Integer captionhorizontalpadding) {
        this.captionhorizontalpadding = captionhorizontalpadding;
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

    public Boolean getAnimation() {
        return animation;
    }

    public void setAnimation(Boolean animation) {
        this.animation = animation;
    }

    public Integer getPalette() {
        return palette;
    }

    public void setPalette(Integer palette) {
        this.palette = palette;
    }

    public String getPalettecolors() {
        return palettecolors;
    }

    public void setPalettecolors(String palettecolors) {
        this.palettecolors = palettecolors;
    }

    public Boolean getShowlabels() {
        return showlabels;
    }

    public void setShowlabels(Boolean showlabels) {
        this.showlabels = showlabels;
    }

    public Integer getMaxlabelheight() {
        return maxlabelheight;
    }

    public void setMaxlabelheight(Integer maxlabelheight) {
        this.maxlabelheight = maxlabelheight;
    }

    public Boolean getLabeldisplay() {
        return labeldisplay;
    }

    public void setLabeldisplay(Boolean labeldisplay) {
        this.labeldisplay = labeldisplay;
    }

    public Boolean getUseellipseswhenoverflow() {
        return useellipseswhenoverflow;
    }

    public void setUseellipseswhenoverflow(Boolean useellipseswhenoverflow) {
        this.useellipseswhenoverflow = useellipseswhenoverflow;
    }

    public Boolean getRotatelabels() {
        return rotatelabels;
    }

    public void setRotatelabels(Boolean rotatelabels) {
        this.rotatelabels = rotatelabels;
    }

    public Boolean getSlantlabels() {
        return slantlabels;
    }

    public void setSlantlabels(Boolean slantlabels) {
        this.slantlabels = slantlabels;
    }

    public Integer getLabelstep() {
        return labelstep;
    }

    public void setLabelstep(Integer labelstep) {
        this.labelstep = labelstep;
    }

    public Integer getStaggerlines() {
        return staggerlines;
    }

    public void setStaggerlines(Integer staggerlines) {
        this.staggerlines = staggerlines;
    }

    public Boolean getShowvalues() {
        return showvalues;
    }

    public void setShowvalues(Boolean showvalues) {
        this.showvalues = showvalues;
    }

    public Boolean getRotatevalues() {
        return rotatevalues;
    }

    public void setRotatevalues(Boolean rotatevalues) {
        this.rotatevalues = rotatevalues;
    }

    public Boolean getPlacevaluesinside() {
        return placevaluesinside;
    }

    public void setPlacevaluesinside(Boolean placevaluesinside) {
        this.placevaluesinside = placevaluesinside;
    }

    public Boolean getShowyaxisvalues() {
        return showyaxisvalues;
    }

    public void setShowyaxisvalues(Boolean showyaxisvalues) {
        this.showyaxisvalues = showyaxisvalues;
    }

    public Boolean getShowlimits() {
        return showlimits;
    }

    public void setShowlimits(Boolean showlimits) {
        this.showlimits = showlimits;
    }

    public Boolean getShowdivlinevalues() {
        return showdivlinevalues;
    }

    public void setShowdivlinevalues(Boolean showdivlinevalues) {
        this.showdivlinevalues = showdivlinevalues;
    }

    public Integer getYaxisvaluesstep() {
        return yaxisvaluesstep;
    }

    public void setYaxisvaluesstep(Integer yaxisvaluesstep) {
        this.yaxisvaluesstep = yaxisvaluesstep;
    }

    public Boolean getShowshadow() {
        return showshadow;
    }

    public void setShowshadow(Boolean showshadow) {
        this.showshadow = showshadow;
    }

    public Boolean getAdjustdiv() {
        return adjustdiv;
    }

    public void setAdjustdiv(Boolean adjustdiv) {
        this.adjustdiv = adjustdiv;
    }

    public Boolean getRotateyaxisname() {
        return rotateyaxisname;
    }

    public void setRotateyaxisname(Boolean rotateyaxisname) {
        this.rotateyaxisname = rotateyaxisname;
    }

    public Integer getYaxisnamewidth() {
        return yaxisnamewidth;
    }

    public void setYaxisnamewidth(Integer yaxisnamewidth) {
        this.yaxisnamewidth = yaxisnamewidth;
    }

    public String getClickurl() {
        return clickurl;
    }

    public void setClickurl(String clickurl) {
        this.clickurl = clickurl;
    }

    public Integer getYaxisminvalue() {
        return yaxisminvalue;
    }

    public void setYaxisminvalue(Integer yaxisminvalue) {
        this.yaxisminvalue = yaxisminvalue;
    }

    public Integer getYaxismaxvalue() {
        return yaxismaxvalue;
    }

    public void setYaxismaxvalue(Integer yaxismaxvalue) {
        this.yaxismaxvalue = yaxismaxvalue;
    }

    public Integer getSetadaptiveyMin() {
        return setadaptiveyMin;
    }

    public void setSetadaptiveyMin(Integer setadaptiveyMin) {
        this.setadaptiveyMin = setadaptiveyMin;
    }

    public Boolean getUsedataplotcolorforlabels() {
        return usedataplotcolorforlabels;
    }

    public void setUsedataplotcolorforlabels(Boolean usedataplotcolorforlabels) {
        this.usedataplotcolorforlabels = usedataplotcolorforlabels;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getSubcaption() {
        return subcaption;
    }

    public void setSubcaption(String subcaption) {
        this.subcaption = subcaption;
    }

    public String getXaxisname() {
        return xaxisname;
    }

    public void setXaxisname(String xaxisname) {
        this.xaxisname = xaxisname;
    }

    public String getYaxisname() {
        return yaxisname;
    }

    public void setYaxisname(String yaxisname) {
        this.yaxisname = yaxisname;
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

    public String getBasefont() {
        return basefont;
    }

    public void setBasefont(String basefont) {
        this.basefont = basefont;
    }

    public Integer getBasefontsize() {
        return basefontsize;
    }

    public void setBasefontsize(Integer basefontsize) {
        this.basefontsize = basefontsize;
    }

    public String getBasefontcolor() {
        return basefontcolor;
    }

    public void setBasefontcolor(String basefontcolor) {
        this.basefontcolor = basefontcolor;
    }

    public String getOutcnvbasefont() {
        return outcnvbasefont;
    }

    public void setOutcnvbasefont(String outcnvbasefont) {
        this.outcnvbasefont = outcnvbasefont;
    }

    public Integer getOutcnvbasefontsize() {
        return outcnvbasefontsize;
    }

    public void setOutcnvbasefontsize(Integer outcnvbasefontsize) {
        this.outcnvbasefontsize = outcnvbasefontsize;
    }

    public String getOutcnvbasefontcolor() {
        return outcnvbasefontcolor;
    }

    public void setOutcnvbasefontcolor(String outcnvbasefontcolor) {
        this.outcnvbasefontcolor = outcnvbasefontcolor;
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

    public Integer getLabelfontsize() {
        return labelfontsize;
    }

    public void setLabelfontsize(Integer labelfontsize) {
        this.labelfontsize = labelfontsize;
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

    public Integer getLabelbgalpha() {
        return labelbgalpha;
    }

    public void setLabelbgalpha(Integer labelbgalpha) {
        this.labelbgalpha = labelbgalpha;
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

    public Boolean getLabelborderdashLen() {
        return labelborderdashLen;
    }

    public void setLabelborderdashLen(Boolean labelborderdashLen) {
        this.labelborderdashLen = labelborderdashLen;
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

    public Boolean getShowlegend() {
        return showlegend;
    }

    public void setShowlegend(Boolean showlegend) {
        this.showlegend = showlegend;
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

    public Boolean getLegendiconscale() {
        return legendiconscale;
    }

    public void setLegendiconscale(Boolean legendiconscale) {
        this.legendiconscale = legendiconscale;
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

    public Boolean getLegendallowdrag() {
        return legendallowdrag;
    }

    public void setLegendallowdrag(Boolean legendallowdrag) {
        this.legendallowdrag = legendallowdrag;
    }

    public String getLegendscrollbgcolor() {
        return legendscrollbgcolor;
    }

    public void setLegendscrollbgcolor(String legendscrollbgcolor) {
        this.legendscrollbgcolor = legendscrollbgcolor;
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

    public void setInteractivelegend(Boolean interactivelegend) {
        this.interactivelegend = interactivelegend;
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

    public Boolean getFormatnumber() {
        return formatnumber;
    }

    public void setFormatnumber(Boolean formatnumber) {
        this.formatnumber = formatnumber;
    }

    public Boolean getFormatnumberscale() {
        return formatnumberscale;
    }

    public void setFormatnumberscale(Boolean formatnumberscale) {
        this.formatnumberscale = formatnumberscale;
    }

    public String getDefaultnumberscale() {
        return defaultnumberscale;
    }

    public void setDefaultnumberscale(String defaultnumberscale) {
        this.defaultnumberscale = defaultnumberscale;
    }

    public String getNumberscaleunit() {
        return numberscaleunit;
    }

    public void setNumberscaleunit(String numberscaleunit) {
        this.numberscaleunit = numberscaleunit;
    }

    public String getNumberscalevalue() {
        return numberscalevalue;
    }

    public void setNumberscalevalue(String numberscalevalue) {
        this.numberscalevalue = numberscalevalue;
    }

    public Boolean getScalerecursively() {
        return scalerecursively;
    }

    public void setScalerecursively(Boolean scalerecursively) {
        this.scalerecursively = scalerecursively;
    }

    public Integer getMaxscalerecursion() {
        return maxscalerecursion;
    }

    public void setMaxscalerecursion(Integer maxscalerecursion) {
        this.maxscalerecursion = maxscalerecursion;
    }

    public String getScaleseparator() {
        return scaleseparator;
    }

    public void setScaleseparator(String scaleseparator) {
        this.scaleseparator = scaleseparator;
    }

    public String getNumberprefix() {
        return numberprefix;
    }

    public void setNumberprefix(String numberprefix) {
        this.numberprefix = numberprefix;
    }

    public String getNumbersuffix() {
        return numbersuffix;
    }

    public void setNumbersuffix(String numbersuffix) {
        this.numbersuffix = numbersuffix;
    }

    public String getDecimalseparator() {
        return decimalseparator;
    }

    public void setDecimalseparator(String decimalseparator) {
        this.decimalseparator = decimalseparator;
    }

    public String getThousandseparator() {
        return thousandseparator;
    }

    public void setThousandseparator(String thousandseparator) {
        this.thousandseparator = thousandseparator;
    }

    public Integer getThousandseparatorposition() {
        return thousandseparatorposition;
    }

    public void setThousandseparatorposition(Integer thousandseparatorposition) {
        this.thousandseparatorposition = thousandseparatorposition;
    }

    public String getIndecimalseparator() {
        return indecimalseparator;
    }

    public void setIndecimalseparator(String indecimalseparator) {
        this.indecimalseparator = indecimalseparator;
    }

    public String getInthousandseparator() {
        return inthousandseparator;
    }

    public void setInthousandseparator(String inthousandseparator) {
        this.inthousandseparator = inthousandseparator;
    }

    public Integer getDecimals() {
        return decimals;
    }

    public void setDecimals(Integer decimals) {
        this.decimals = decimals;
    }

    public Boolean getForcedecimals() {
        return forcedecimals;
    }

    public void setForcedecimals(Boolean forcedecimals) {
        this.forcedecimals = forcedecimals;
    }

    public Boolean getForceyaxisValueDecimals() {
        return forceyaxisValueDecimals;
    }

    public void setForceyaxisValueDecimals(Boolean forceyaxisValueDecimals) {
        this.forceyaxisValueDecimals = forceyaxisValueDecimals;
    }

    public Integer getYaxisvaluedecimals() {
        return yaxisvaluedecimals;
    }

    public void setYaxisvaluedecimals(Integer yaxisvaluedecimals) {
        this.yaxisvaluedecimals = yaxisvaluedecimals;
    }

    public Integer getCaptionpadding() {
        return captionpadding;
    }

    public void setCaptionpadding(Integer captionpadding) {
        this.captionpadding = captionpadding;
    }

    public Integer getXaxisnamepadding() {
        return xaxisnamepadding;
    }

    public void setXaxisnamepadding(Integer xaxisnamepadding) {
        this.xaxisnamepadding = xaxisnamepadding;
    }

    public Integer getYaxisnamepadding() {
        return yaxisnamepadding;
    }

    public void setYaxisnamepadding(Integer yaxisnamepadding) {
        this.yaxisnamepadding = yaxisnamepadding;
    }

    public Integer getYaxivaluespadding() {
        return yaxivaluespadding;
    }

    public void setYaxivaluespadding(Integer yaxivaluespadding) {
        this.yaxivaluespadding = yaxivaluespadding;
    }

    public Integer getLabelpadding() {
        return labelpadding;
    }

    public void setLabelpadding(Integer labelpadding) {
        this.labelpadding = labelpadding;
    }

    public Integer getValuepadding() {
        return valuepadding;
    }

    public void setValuepadding(Integer valuepadding) {
        this.valuepadding = valuepadding;
    }

    public Integer getPlotspacepercent() {
        return plotspacepercent;
    }

    public void setPlotspacepercent(Integer plotspacepercent) {
        this.plotspacepercent = plotspacepercent;
    }

    public Integer getChartleftmargin() {
        return chartleftmargin;
    }

    public void setChartleftmargin(Integer chartleftmargin) {
        this.chartleftmargin = chartleftmargin;
    }

    public Integer getChartrightmargin() {
        return chartrightmargin;
    }

    public void setChartrightmargin(Integer chartrightmargin) {
        this.chartrightmargin = chartrightmargin;
    }

    public Integer getCharttopmargin() {
        return charttopmargin;
    }

    public void setCharttopmargin(Integer charttopmargin) {
        this.charttopmargin = charttopmargin;
    }

    public Integer getChartbottommargin() {
        return chartbottommargin;
    }

    public void setChartbottommargin(Integer chartbottommargin) {
        this.chartbottommargin = chartbottommargin;
    }

    public Integer getCanvasleftmargin() {
        return canvasleftmargin;
    }

    public void setCanvasleftmargin(Integer canvasleftmargin) {
        this.canvasleftmargin = canvasleftmargin;
    }

    public Integer getCanvasrightmargin() {
        return canvasrightmargin;
    }

    public void setCanvasrightmargin(Integer canvasrightmargin) {
        this.canvasrightmargin = canvasrightmargin;
    }

    public Integer getCanvastopmargin() {
        return canvastopmargin;
    }

    public void setCanvastopmargin(Integer canvastopmargin) {
        this.canvastopmargin = canvastopmargin;
    }

    public Integer getCanvasbottommargin() {
        return canvasbottommargin;
    }

    public void setCanvasbottommargin(Integer canvasbottommargin) {
        this.canvasbottommargin = canvasbottommargin;
    }

    public Boolean getUseroundedges() {
        return useroundedges;
    }

    public void setUseroundedges(Boolean useroundedges) {
        this.useroundedges = useroundedges;
    }

    public String getPlotbordercolor() {
        return plotbordercolor;
    }

    public void setPlotbordercolor(String plotbordercolor) {
        this.plotbordercolor = plotbordercolor;
    }

    public Integer getPlotborderthickness() {
        return plotborderthickness;
    }

    public void setPlotborderthickness(Integer plotborderthickness) {
        this.plotborderthickness = plotborderthickness;
    }

    public Integer getPlotborderalpha() {
        return plotborderalpha;
    }

    public void setPlotborderalpha(Integer plotborderalpha) {
        this.plotborderalpha = plotborderalpha;
    }

    public Boolean getPlotborderdashed() {
        return plotborderdashed;
    }

    public void setPlotborderdashed(Boolean plotborderdashed) {
        this.plotborderdashed = plotborderdashed;
    }

    public Integer getPlotborderdashLen() {
        return plotborderdashLen;
    }

    public void setPlotborderdashLen(Integer plotborderdashLen) {
        this.plotborderdashLen = plotborderdashLen;
    }

    public Integer getPlotborderdashgap() {
        return plotborderdashgap;
    }

    public void setPlotborderdashgap(Integer plotborderdashgap) {
        this.plotborderdashgap = plotborderdashgap;
    }

    public Integer getPlotfillangle() {
        return plotfillangle;
    }

    public void setPlotfillangle(Integer plotfillangle) {
        this.plotfillangle = plotfillangle;
    }

    public Integer getPlotfillratio() {
        return plotfillratio;
    }

    public void setPlotfillratio(Integer plotfillratio) {
        this.plotfillratio = plotfillratio;
    }

    public Integer getPlotfillalpha() {
        return plotfillalpha;
    }

    public void setPlotfillalpha(Integer plotfillalpha) {
        this.plotfillalpha = plotfillalpha;
    }

    public String getPlotgradientcolor() {
        return plotgradientcolor;
    }

    public void setPlotgradientcolor(String plotgradientcolor) {
        this.plotgradientcolor = plotgradientcolor;
    }

    public Boolean getShowtooltip() {
        return showtooltip;
    }

    public void setShowtooltip(Boolean showtooltip) {
        this.showtooltip = showtooltip;
    }

    public String getTooltipcolor() {
        return tooltipcolor;
    }

    public void setTooltipcolor(String tooltipcolor) {
        this.tooltipcolor = tooltipcolor;
    }

    public String getTooltipbgcolor() {
        return tooltipbgcolor;
    }

    public void setTooltipbgcolor(String tooltipbgcolor) {
        this.tooltipbgcolor = tooltipbgcolor;
    }

    public String getTooltipbordercolor() {
        return tooltipbordercolor;
    }

    public void setTooltipbordercolor(String tooltipbordercolor) {
        this.tooltipbordercolor = tooltipbordercolor;
    }

    public String getTooltipsepchar() {
        return tooltipsepchar;
    }

    public void setTooltipsepchar(String tooltipsepchar) {
        this.tooltipsepchar = tooltipsepchar;
    }

    public Boolean getSeriesnameintooltip() {
        return seriesnameintooltip;
    }

    public void setSeriesnameintooltip(Boolean seriesnameintooltip) {
        this.seriesnameintooltip = seriesnameintooltip;
    }

    public Boolean getShowtooltipshadow() {
        return showtooltipshadow;
    }

    public void setShowtooltipshadow(Boolean showtooltipshadow) {
        this.showtooltipshadow = showtooltipshadow;
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

    public Integer getValuefontsize() {
        return valuefontsize;
    }

    public void setValuefontsize(Integer valuefontsize) {
        this.valuefontsize = valuefontsize;
    }

    public Boolean getValuefontbold() {
        return valuefontbold;
    }

    public void setValuefontbold(Boolean valuefontbold) {
        this.valuefontbold = valuefontbold;
    }

    public Boolean getValuefontitalic() {
        return valuefontitalic;
    }

    public void setValuefontitalic(Boolean valuefontitalic) {
        this.valuefontitalic = valuefontitalic;
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

    public Integer getValuebordedashlen() {
        return valuebordedashlen;
    }

    public void setValuebordedashlen(Integer valuebordedashlen) {
        this.valuebordedashlen = valuebordedashlen;
    }

    public Integer getValuehoveralpha() {
        return valuehoveralpha;
    }

    public void setValuehoveralpha(Integer valuehoveralpha) {
        this.valuehoveralpha = valuehoveralpha;
    }

    public Integer getValuefonthoveralpha() {
        return valuefonthoveralpha;
    }

    public void setValuefonthoveralpha(Integer valuefonthoveralpha) {
        this.valuefonthoveralpha = valuefonthoveralpha;
    }

    public Integer getValuebghoveralpha() {
        return valuebghoveralpha;
    }

    public void setValuebghoveralpha(Integer valuebghoveralpha) {
        this.valuebghoveralpha = valuebghoveralpha;
    }

    public Integer getValueborderhoveralpha() {
        return valueborderhoveralpha;
    }

    public void setValueborderhoveralpha(Integer valueborderhoveralpha) {
        this.valueborderhoveralpha = valueborderhoveralpha;
    }

    public Boolean getShowvaluesonhover() {
        return showvaluesonhover;
    }

    public void setShowvaluesonhover(Boolean showvaluesonhover) {
        this.showvaluesonhover = showvaluesonhover;
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

    public String getXaxisbamebgcolor() {
        return xaxisbamebgcolor;
    }

    public void setXaxisbamebgcolor(String xaxisbamebgcolor) {
        this.xaxisbamebgcolor = xaxisbamebgcolor;
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

    public Integer getXaxisbamebgalpha() {
        return xaxisbamebgalpha;
    }

    public void setXaxisbamebgalpha(Integer xaxisbamebgalpha) {
        this.xaxisbamebgalpha = xaxisbamebgalpha;
    }

    public Integer getXaxisnameborderalpha() {
        return xaxisnameborderalpha;
    }

    public void setXaxisnameborderalpha(Integer xaxisnameborderalpha) {
        this.xaxisnameborderalpha = xaxisnameborderalpha;
    }

    public Integer getXaxisborderborderpadding() {
        return xaxisborderborderpadding;
    }

    public void setXaxisborderborderpadding(Integer xaxisborderborderpadding) {
        this.xaxisborderborderpadding = xaxisborderborderpadding;
    }

    public Integer getXaxisnameborderRadius() {
        return xaxisnameborderRadius;
    }

    public void setXaxisnameborderRadius(Integer xaxisnameborderRadius) {
        this.xaxisnameborderRadius = xaxisnameborderRadius;
    }

    public Integer getXaxisnameborderThickness() {
        return xaxisnameborderThickness;
    }

    public void setXaxisnameborderThickness(Integer xaxisnameborderThickness) {
        this.xaxisnameborderThickness = xaxisnameborderThickness;
    }

    public Boolean getXaxisnameborderDashed() {
        return xaxisnameborderDashed;
    }

    public void setXaxisnameborderDashed(Boolean xaxisnameborderDashed) {
        this.xaxisnameborderDashed = xaxisnameborderDashed;
    }

    public Integer getXaxisnameborderDashLen() {
        return xaxisnameborderDashLen;
    }

    public void setXaxisnameborderDashLen(Integer xaxisnameborderDashLen) {
        this.xaxisnameborderDashLen = xaxisnameborderDashLen;
    }

    public Integer getXaxisnameborderDashGap() {
        return xaxisnameborderDashGap;
    }

    public void setXaxisnameborderDashGap(Integer xaxisnameborderDashGap) {
        this.xaxisnameborderDashGap = xaxisnameborderDashGap;
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

    public String getYaxisbamebgcolor() {
        return yaxisbamebgcolor;
    }

    public void setYaxisbamebgcolor(String yaxisbamebgcolor) {
        this.yaxisbamebgcolor = yaxisbamebgcolor;
    }

    public String getYaxisnamebordercolor() {
        return yaxisnamebordercolor;
    }

    public void setYaxisnamebordercolor(String yaxisnamebordercolor) {
        this.yaxisnamebordercolor = yaxisnamebordercolor;
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

    public Boolean getAnimate3d() {
        return animate3d;
    }

    public void setAnimate3d(Boolean animate3d) {
        this.animate3d = animate3d;
    }

    public Integer getExetime() {
        return exetime;
    }

    public void setExetime(Integer exetime) {
        this.exetime = exetime;
    }

    public Boolean getConnectnulldata() {
        return connectnulldata;
    }

    public void setConnectnulldata(Boolean connectnulldata) {
        this.connectnulldata = connectnulldata;
    }

    public Integer getLabeldtep() {
        return labeldtep;
    }

    public void setLabeldtep(Integer labeldtep) {
        this.labeldtep = labeldtep;
    }

    public Integer getYaxisbaluesstep() {
        return yaxisbaluesstep;
    }

    public void setYaxisbaluesstep(Integer yaxisbaluesstep) {
        this.yaxisbaluesstep = yaxisbaluesstep;
    }

    public Integer getSetadaptiveymin() {
        return setadaptiveymin;
    }

    public void setSetadaptiveymin(Integer setadaptiveymin) {
        this.setadaptiveymin = setadaptiveymin;
    }

    public String getXaxistickcolor() {
        return xaxistickcolor;
    }

    public void setXaxistickcolor(String xaxistickcolor) {
        this.xaxistickcolor = xaxistickcolor;
    }

    public Integer getXaxistickalpha() {
        return xaxistickalpha;
    }

    public void setXaxistickalpha(Integer xaxistickalpha) {
        this.xaxistickalpha = xaxistickalpha;
    }

    public Integer getXaxistickthickness() {
        return xaxistickthickness;
    }

    public void setXaxistickthickness(Integer xaxistickthickness) {
        this.xaxistickthickness = xaxistickthickness;
    }

    public Boolean getDrawquadrant() {
        return drawquadrant;
    }

    public void setDrawquadrant(Boolean drawquadrant) {
        this.drawquadrant = drawquadrant;
    }

    public Integer getQuadrantxval() {
        return quadrantxval;
    }

    public void setQuadrantxval(Integer quadrantxval) {
        this.quadrantxval = quadrantxval;
    }

    public Integer getQuadrantyval() {
        return quadrantyval;
    }

    public void setQuadrantyval(Integer quadrantyval) {
        this.quadrantyval = quadrantyval;
    }

    public String getQuadrantlinecolor() {
        return quadrantlinecolor;
    }

    public void setQuadrantlinecolor(String quadrantlinecolor) {
        this.quadrantlinecolor = quadrantlinecolor;
    }

    public Integer getQuadrantlinethickness() {
        return quadrantlinethickness;
    }

    public void setQuadrantlinethickness(Integer quadrantlinethickness) {
        this.quadrantlinethickness = quadrantlinethickness;
    }

    public Integer getQuadrantlinealpha() {
        return quadrantlinealpha;
    }

    public void setQuadrantlinealpha(Integer quadrantlinealpha) {
        this.quadrantlinealpha = quadrantlinealpha;
    }

    public Integer getQuadrantlinedashed() {
        return quadrantlinedashed;
    }

    public void setQuadrantlinedashed(Integer quadrantlinedashed) {
        this.quadrantlinedashed = quadrantlinedashed;
    }

    public Integer getQuadrantlinedashlen() {
        return quadrantlinedashlen;
    }

    public void setQuadrantlinedashlen(Integer quadrantlinedashlen) {
        this.quadrantlinedashlen = quadrantlinedashlen;
    }

    public Integer getQuadrantlinedashgap() {
        return quadrantlinedashgap;
    }

    public void setQuadrantlinedashgap(Integer quadrantlinedashgap) {
        this.quadrantlinedashgap = quadrantlinedashgap;
    }

    public String getQuadrantlabeltl() {
        return quadrantlabeltl;
    }

    public void setQuadrantlabeltl(String quadrantlabeltl) {
        this.quadrantlabeltl = quadrantlabeltl;
    }

    public String getQuadrantlabeltr() {
        return quadrantlabeltr;
    }

    public void setQuadrantlabeltr(String quadrantlabeltr) {
        this.quadrantlabeltr = quadrantlabeltr;
    }

    public String getQuadrantlabelbl() {
        return quadrantlabelbl;
    }

    public void setQuadrantlabelbl(String quadrantlabelbl) {
        this.quadrantlabelbl = quadrantlabelbl;
    }

    public String getQuadrantlabelbr() {
        return quadrantlabelbr;
    }

    public void setQuadrantlabelbr(String quadrantlabelbr) {
        this.quadrantlabelbr = quadrantlabelbr;
    }

    public Integer getQuadrantlabelpadding() {
        return quadrantlabelpadding;
    }

    public void setQuadrantlabelpadding(Integer quadrantlabelpadding) {
        this.quadrantlabelpadding = quadrantlabelpadding;
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
}
