package net.ooder.esd.bean.fchart;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.fchart.*;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.tool.properties.fchart.ChartData;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;
import net.ooder.web.util.JSONGenUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AnnotationType(clazz = ChartAnnotation.class)
public class ChartDataBean implements CustomBean {
    String caption;

    String subcaption;

    String xaxisname;

    String yaxisname;

    ChartCustomBean customBean;

    ChartCaptionBean captionBean;

    ChartCosmeticsBean cosmeticsBean;

    ChartEffectBean effectBean;

    ChartFontBean fontBean;

    ChartLabelBean labelBean;

    ChartLegentBean legentBean;

    ChartNumberBean numberBean;

    ChartPlotBean plotBean;

    ChartToolTipBean toolTipBean;

    ChartValueBean valueBean;

    ChartXBean xBean;

    ChartYBean yBean;

    ChartPaddingsBean paddingsBean;

    ChartFunctionalBean functionalBean;

    Chart3DFunctionalBean functional3D;

    ChartAnchorsBean anchorsBean;

    ChartCanvasCosmeticsBean canvasCosmeticsBean;

    ChartQuadrantsBean quadrantsBean;

    ChartScrollBarBean scrollBarBean;

    ChartDivisionalBean divisionalBean;

    public ChartDataBean() {


    }

    public ChartDataBean(ChartData chartData) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(chartData), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);


        this.customBean = new ChartCustomBean(chartData);

        this.captionBean = new ChartCaptionBean(chartData);

        this.cosmeticsBean = new ChartCosmeticsBean(chartData);

        this.effectBean = new ChartEffectBean(chartData);

        this.fontBean = new ChartFontBean(chartData);

        this.labelBean = new ChartLabelBean(chartData);

        this.legentBean = new ChartLegentBean(chartData);

        this.numberBean = new ChartNumberBean(chartData);

        this.plotBean = new ChartPlotBean(chartData);

        this.toolTipBean = new ChartToolTipBean(chartData);

        this.valueBean = new ChartValueBean(chartData);

        this.xBean = new ChartXBean(chartData);

        this.yBean = new ChartYBean(chartData);

        this.paddingsBean = new ChartPaddingsBean(chartData);

        this.functionalBean = new ChartFunctionalBean(chartData);

        this.functional3D = new Chart3DFunctionalBean(chartData);

        this.anchorsBean = new ChartAnchorsBean(chartData);

        this.canvasCosmeticsBean = new ChartCanvasCosmeticsBean(chartData);

        this.quadrantsBean = new ChartQuadrantsBean(chartData);

        this.scrollBarBean = new ChartScrollBarBean(chartData);

        this.divisionalBean = new ChartDivisionalBean(chartData);

    }


    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        CustomBean[] customBeans = new CustomBean[]{
                customBean, captionBean, cosmeticsBean, effectBean, fontBean, labelBean, legentBean,
                numberBean, plotBean, toolTipBean, valueBean, xBean, yBean, paddingsBean,
                functionalBean, functional3D, anchorsBean, canvasCosmeticsBean,
                quadrantsBean, scrollBarBean, divisionalBean

        };

        for (CustomBean customBean : customBeans) {
            if (customBean != null && !AnnotationUtil.getAnnotationMap(customBean).isEmpty()) {
                annotationBeans.add(customBean);
            }
        }

        annotationBeans.add(this);
        return annotationBeans;

    }

    ;


    public ChartDataBean(MethodConfig methodAPIBean) {
        Class clazz = JSONGenUtil.getInnerReturnType(methodAPIBean.getMethod());

        ChartAnnotation chartAnnotation = AnnotationUtil.getClassAnnotation(clazz, ChartAnnotation.class);
        if (chartAnnotation != null) {
            this.fillData(chartAnnotation);
        }

        ChartCustomAnnotation customAnnotation = AnnotationUtil.getClassAnnotation(clazz, ChartCustomAnnotation.class);
        if (customAnnotation != null) {
            customBean = new ChartCustomBean(customAnnotation);
        }
        ChartCaptionAnnotation captionAnnotation = AnnotationUtil.getClassAnnotation(clazz, ChartCaptionAnnotation.class);
        if (captionAnnotation != null) {
            captionBean = new ChartCaptionBean(captionAnnotation);
        }
        ChartCosmeticsAnnotation cosmeticsAnnotation = AnnotationUtil.getClassAnnotation(clazz, ChartCosmeticsAnnotation.class);
        if (cosmeticsAnnotation != null) {
            cosmeticsBean = new ChartCosmeticsBean(cosmeticsAnnotation);
        }
        ChartEffectAnnotation effectAnnotation = AnnotationUtil.getClassAnnotation(clazz, ChartEffectAnnotation.class);
        if (effectAnnotation != null) {
            effectBean = new ChartEffectBean(effectAnnotation);
        }
        ChartFontAnnotation fontAnnotation = AnnotationUtil.getClassAnnotation(clazz, ChartFontAnnotation.class);
        if (fontAnnotation != null) {
            fontBean = new ChartFontBean(fontAnnotation);
        }
        ChartLabelAnnotation labelAnnotation = AnnotationUtil.getClassAnnotation(clazz, ChartLabelAnnotation.class);
        if (labelAnnotation != null) {
            labelBean = new ChartLabelBean(labelAnnotation);
        }
        ChartLegentAnnotation legentAnnotation = AnnotationUtil.getClassAnnotation(clazz, ChartLegentAnnotation.class);
        if (legentAnnotation != null) {
            legentBean = new ChartLegentBean(legentAnnotation);
        }
        ChartNumberAnnotation numberAnnotation = AnnotationUtil.getClassAnnotation(clazz, ChartNumberAnnotation.class);
        if (numberAnnotation != null) {
            numberBean = new ChartNumberBean(numberAnnotation);
        }
        ChartPlotAnnotation plotAnnotation = AnnotationUtil.getClassAnnotation(clazz, ChartPlotAnnotation.class);
        if (plotAnnotation != null) {
            plotBean = new ChartPlotBean(plotAnnotation);
        }
        ChartToolTipAnnotation toolTipAnnotation = AnnotationUtil.getClassAnnotation(clazz, ChartToolTipAnnotation.class);
        if (toolTipAnnotation != null) {
            toolTipBean = new ChartToolTipBean(toolTipAnnotation);
        }

        ChartValueAnnotation valueAnnotation = AnnotationUtil.getClassAnnotation(clazz, ChartValueAnnotation.class);
        if (valueAnnotation != null) {
            valueBean = new ChartValueBean(valueAnnotation);
        }
        ChartXAnnotation xAnnotation = AnnotationUtil.getClassAnnotation(clazz, ChartXAnnotation.class);
        if (xAnnotation != null) {
            xBean = new ChartXBean(xAnnotation);
        }
        ChartYAnnotation yAnnotation = AnnotationUtil.getClassAnnotation(clazz, ChartYAnnotation.class);
        if (yAnnotation != null) {
            yBean = new ChartYBean(yAnnotation);
        }


        ChartPaddingsAnnotation paddingsAnnotation = AnnotationUtil.getClassAnnotation(clazz, ChartPaddingsAnnotation.class);
        if (paddingsAnnotation != null) {
            paddingsBean = new ChartPaddingsBean(paddingsAnnotation);
        }
        ChartFunctionalAnnotation functionalAnnotation = AnnotationUtil.getClassAnnotation(clazz, ChartFunctionalAnnotation.class);
        if (functionalAnnotation != null) {
            functionalBean = new ChartFunctionalBean(functionalAnnotation);
        }
        Chart3DFunctionalAnnotation chart3DFunctionalAnnotation = AnnotationUtil.getClassAnnotation(clazz, Chart3DFunctionalAnnotation.class);
        if (chart3DFunctionalAnnotation != null) {
            functional3D = new Chart3DFunctionalBean(chart3DFunctionalAnnotation);
        }


        ChartAnchorsAnnotation chartAnchorsAnnotation = AnnotationUtil.getClassAnnotation(clazz, ChartAnchorsAnnotation.class);
        if (chartAnchorsAnnotation != null) {
            anchorsBean = new ChartAnchorsBean(chartAnchorsAnnotation);
        }
        ChartCanvasCosmeticsAnnotation chartCanvasCosmeticsAnnotation = AnnotationUtil.getClassAnnotation(clazz, ChartCanvasCosmeticsAnnotation.class);
        if (chartCanvasCosmeticsAnnotation != null) {
            canvasCosmeticsBean = new ChartCanvasCosmeticsBean(chartCanvasCosmeticsAnnotation);
        }
        ChartQuadrantsAnnotation chartQuadrantsAnnotation = AnnotationUtil.getClassAnnotation(clazz, ChartQuadrantsAnnotation.class);
        if (chartQuadrantsAnnotation != null) {
            quadrantsBean = new ChartQuadrantsBean(chartQuadrantsAnnotation);
        }

        ChartScrollBarAnnotation chartScrollBarAnnotation = AnnotationUtil.getClassAnnotation(clazz, ChartScrollBarAnnotation.class);
        if (chartScrollBarAnnotation != null) {
            scrollBarBean = new ChartScrollBarBean(chartScrollBarAnnotation);
        }
        ChartDivisionalAnnotation divisionalAnnotation = AnnotationUtil.getClassAnnotation(clazz, ChartDivisionalAnnotation.class);
        if (divisionalAnnotation != null) {
            divisionalBean = new ChartDivisionalBean(divisionalAnnotation);
        }


    }

    public ChartDataBean(ChartAnnotation annotation) {
        this.fillData(annotation);
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

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public ChartPaddingsBean getPaddingsBean() {
        return paddingsBean;
    }

    public void setPaddingsBean(ChartPaddingsBean paddingsBean) {
        this.paddingsBean = paddingsBean;
    }

    public ChartFunctionalBean getFunctionalBean() {
        return functionalBean;
    }

    public void setFunctionalBean(ChartFunctionalBean functionalBean) {
        this.functionalBean = functionalBean;
    }

    public Chart3DFunctionalBean getFunctional3D() {
        return functional3D;
    }

    public void setFunctional3D(Chart3DFunctionalBean functional3D) {
        this.functional3D = functional3D;
    }

    public ChartAnchorsBean getAnchorsBean() {
        return anchorsBean;
    }

    public void setAnchorsBean(ChartAnchorsBean anchorsBean) {
        this.anchorsBean = anchorsBean;
    }

    public ChartCanvasCosmeticsBean getCanvasCosmeticsBean() {
        return canvasCosmeticsBean;
    }

    public void setCanvasCosmeticsBean(ChartCanvasCosmeticsBean canvasCosmeticsBean) {
        this.canvasCosmeticsBean = canvasCosmeticsBean;
    }

    public ChartQuadrantsBean getQuadrantsBean() {
        return quadrantsBean;
    }

    public void setQuadrantsBean(ChartQuadrantsBean quadrantsBean) {
        this.quadrantsBean = quadrantsBean;
    }

    public ChartScrollBarBean getScrollBarBean() {
        return scrollBarBean;
    }

    public void setScrollBarBean(ChartScrollBarBean scrollBarBean) {
        this.scrollBarBean = scrollBarBean;
    }

    public ChartDivisionalBean getDivisionalBean() {
        return divisionalBean;
    }

    public void setDivisionalBean(ChartDivisionalBean divisionalBean) {
        this.divisionalBean = divisionalBean;
    }

    public ChartCustomBean getCustomBean() {
        return customBean;
    }

    public void setCustomBean(ChartCustomBean customBean) {
        this.customBean = customBean;
    }

    public ChartCaptionBean getCaptionBean() {
        return captionBean;
    }

    public void setCaptionBean(ChartCaptionBean captionBean) {
        this.captionBean = captionBean;
    }

    public ChartCosmeticsBean getCosmeticsBean() {
        return cosmeticsBean;
    }

    public void setCosmeticsBean(ChartCosmeticsBean cosmeticsBean) {
        this.cosmeticsBean = cosmeticsBean;
    }

    public ChartEffectBean getEffectBean() {
        return effectBean;
    }

    public void setEffectBean(ChartEffectBean effectBean) {
        this.effectBean = effectBean;
    }

    public ChartFontBean getFontBean() {
        return fontBean;
    }

    public void setFontBean(ChartFontBean fontBean) {
        this.fontBean = fontBean;
    }

    public ChartLabelBean getLabelBean() {
        return labelBean;
    }

    public void setLabelBean(ChartLabelBean labelBean) {
        this.labelBean = labelBean;
    }

    public ChartLegentBean getLegentBean() {
        return legentBean;
    }

    public void setLegentBean(ChartLegentBean legentBean) {
        this.legentBean = legentBean;
    }

    public ChartNumberBean getNumberBean() {
        return numberBean;
    }

    public void setNumberBean(ChartNumberBean numberBean) {
        this.numberBean = numberBean;
    }

    public ChartPlotBean getPlotBean() {
        return plotBean;
    }

    public void setPlotBean(ChartPlotBean plotBean) {
        this.plotBean = plotBean;
    }

    public ChartToolTipBean getToolTipBean() {
        return toolTipBean;
    }

    public void setToolTipBean(ChartToolTipBean toolTipBean) {
        this.toolTipBean = toolTipBean;
    }

    public ChartValueBean getValueBean() {
        return valueBean;
    }

    public void setValueBean(ChartValueBean valueBean) {
        this.valueBean = valueBean;
    }

    public ChartXBean getxBean() {
        return xBean;
    }

    public void setxBean(ChartXBean xBean) {
        this.xBean = xBean;
    }

    public ChartYBean getyBean() {
        return yBean;
    }

    public void setyBean(ChartYBean yBean) {
        this.yBean = yBean;
    }

    public ChartDataBean fillData(ChartAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}

