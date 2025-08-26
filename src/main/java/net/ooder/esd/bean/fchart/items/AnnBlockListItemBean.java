package net.ooder.esd.bean.fchart.items;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.fchart.ChartAnnBlockAnnotation;
import net.ooder.esd.annotation.fchart.FChartAnnType;
import net.ooder.esd.annotation.event.CustomFormEvent;
import net.ooder.esd.annotation.ui.AlignType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.VAlignType;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.RightContextMenuBean;
import net.ooder.esd.bean.bar.ContextMenuBar;
import net.ooder.esd.bean.view.CustomFChartViewBean;
import net.ooder.esd.bean.nav.TabItemBean;
import net.ooder.esd.dsm.view.field.FieldItemConfig;
import net.ooder.web.ConstructorBean;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.reflect.Constructor;
import java.util.LinkedHashSet;
import java.util.Set;

@AnnotationType(clazz = ChartAnnBlockAnnotation.class)
public class AnnBlockListItemBean<T extends FieldItemConfig> implements ContextMenuBar, Comparable<TabItemBean>, CustomBean {

    Integer index;
    Boolean lazyLoad;
    String id;
    String caption;
    FChartAnnType type;
    Integer x;
    Integer y;
    Integer tox;
    Integer toy;
    String fillcolor;
    Integer fillalpha;
    Integer fillratio;
    Integer fillngle;
    String fillpattern;
    Boolean showborder;
    String bordercolor;
    Integer borderalpha;
    Integer borderthickness;
    Integer dashed;
    Integer dashlen;
    Integer dashgap;
    String tooltext;
    String link;
    Boolean showshadow;
    String label;
    AlignType align;
    VAlignType valign;
    String font;
    Integer fontsize;
    String fontcolor;
    Boolean bold;
    Integer italic;
    Integer leftmargin;
    String bgcolor;
    String rotatetext;
    Integer wrapwidth;
    Integer wrapheight;
    Boolean wrap;
    Integer radius;
    Integer yradius;
    Integer startangle;
    Integer endangle;
    Integer thickness;
    Boolean showbelow;
    Boolean autoscale;
    Boolean constrainscale;
    Boolean scaletext;
    Boolean scaleimages;
    Integer xshift;
    Integer yshift;
    Integer grpxshift;
    Integer grpyshift;
    String color;
    Integer alpha;
    Boolean visible;

    AlignType textalign;
    VAlignType textvalign;

    Boolean wraptext;
    String path;

    Integer origw;
    Integer righ;

    RightContextMenuBean contextMenuBean;

    ConstructorBean constructorBean;

    Class bindService;

    LineListItem lineListItem;

    Set<CustomFormEvent> event = new LinkedHashSet<>();


    @JSONField(serialize = false)
    Set<ComponentType> bindTypes = new LinkedHashSet<>();


    public String sourceClassName;

    public String methodName;

    public String rootClassName;

    public String rootMethodName;

    public String viewClassName;


    @JSONField(serialize = false)
    Class fristClass;

    @JSONField(serialize = false)
    MethodConfig methodConfig;

    @JSONField(serialize = false)
    CustomFChartViewBean chartDataBean;

    public AnnBlockListItemBean() {

    }


    public AnnBlockListItemBean(MethodConfig methodConfig, CustomFChartViewBean chartDataBean) {
        this.methodConfig = methodConfig;
        this.chartDataBean = chartDataBean;
        Class[] paramClass = methodConfig.getMethod().getParameterTypes();
        if (paramClass.length > 0) {
            this.fristClass = paramClass[0];
        }
        ChartAnnBlockAnnotation rawDataItem = AnnotationUtil.getMethodAnnotation(methodConfig.getMethod(), ChartAnnBlockAnnotation.class);
        if (rawDataItem != null) {
            fillData(rawDataItem);
        } else {
            AnnotationUtil.fillDefaultValue(ChartAnnBlockAnnotation.class, this);
        }


    }

    public AnnBlockListItemBean(Constructor constructor, CustomFChartViewBean chartDataBean) {
        constructorBean = new ConstructorBean(constructor);

        Class[] paramClass = constructor.getParameterTypes();
        if (paramClass.length > 0) {
            this.fristClass = paramClass[0];
        }

        ChartAnnBlockAnnotation rawDataItem = AnnotationUtil.getConstructorAnnotation(constructor, ChartAnnBlockAnnotation.class);
        if (rawDataItem != null) {
            fillData(rawDataItem);
        } else {
            AnnotationUtil.fillDefaultValue(ChartAnnBlockAnnotation.class, this);
        }

    }


    public Integer getTox() {
        return tox;
    }

    public void setTox(Integer tox) {
        this.tox = tox;
    }

    public Integer getToy() {
        return toy;
    }

    public void setToy(Integer toy) {
        this.toy = toy;
    }

    public Integer getFillratio() {
        return fillratio;
    }

    public void setFillratio(Integer fillratio) {
        this.fillratio = fillratio;
    }

    public Integer getFillngle() {
        return fillngle;
    }

    public void setFillngle(Integer fillngle) {
        this.fillngle = fillngle;
    }

    public String getFillpattern() {
        return fillpattern;
    }

    public void setFillpattern(String fillpattern) {
        this.fillpattern = fillpattern;
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

    public Integer getBorderalpha() {
        return borderalpha;
    }

    public void setBorderalpha(Integer borderalpha) {
        this.borderalpha = borderalpha;
    }

    public Integer getBorderthickness() {
        return borderthickness;
    }

    public void setBorderthickness(Integer borderthickness) {
        this.borderthickness = borderthickness;
    }

    public Integer getDashlen() {
        return dashlen;
    }

    public void setDashlen(Integer dashlen) {
        this.dashlen = dashlen;
    }

    public Integer getDashgap() {
        return dashgap;
    }

    public void setDashgap(Integer dashgap) {
        this.dashgap = dashgap;
    }

    public VAlignType getValign() {
        return valign;
    }

    public void setValign(VAlignType valign) {
        this.valign = valign;
    }

    public String getFontcolor() {
        return fontcolor;
    }

    public void setFontcolor(String fontcolor) {
        this.fontcolor = fontcolor;
    }

    public Integer getLeftmargin() {
        return leftmargin;
    }

    public void setLeftmargin(Integer leftmargin) {
        this.leftmargin = leftmargin;
    }

    public String getBgcolor() {
        return bgcolor;
    }

    public void setBgcolor(String bgcolor) {
        this.bgcolor = bgcolor;
    }

    public Integer getWrapwidth() {
        return wrapwidth;
    }

    public void setWrapwidth(Integer wrapwidth) {
        this.wrapwidth = wrapwidth;
    }

    public Integer getWrapheight() {
        return wrapheight;
    }

    public void setWrapheight(Integer wrapheight) {
        this.wrapheight = wrapheight;
    }

    public Integer getYradius() {
        return yradius;
    }

    public void setYradius(Integer yradius) {
        this.yradius = yradius;
    }

    public Integer getStartangle() {
        return startangle;
    }

    public void setStartangle(Integer startangle) {
        this.startangle = startangle;
    }

    public Integer getEndangle() {
        return endangle;
    }

    public void setEndangle(Integer endangle) {
        this.endangle = endangle;
    }

    public Integer getXshift() {
        return xshift;
    }

    public void setXshift(Integer xshift) {
        this.xshift = xshift;
    }

    public Integer getYshift() {
        return yshift;
    }

    public void setYshift(Integer yshift) {
        this.yshift = yshift;
    }

    public Integer getGrpxshift() {
        return grpxshift;
    }

    public void setGrpxshift(Integer grpxshift) {
        this.grpxshift = grpxshift;
    }

    public Integer getGrpyshift() {
        return grpyshift;
    }

    public void setGrpyshift(Integer grpyshift) {
        this.grpyshift = grpyshift;
    }

    public Boolean getWraptext() {
        return wraptext;
    }

    public void setWraptext(Boolean wraptext) {
        this.wraptext = wraptext;
    }

    public Integer getOrigw() {
        return origw;
    }

    public void setOrigw(Integer origw) {
        this.origw = origw;
    }

    public Integer getRigh() {
        return righ;
    }

    public void setRigh(Integer righ) {
        this.righ = righ;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Boolean getLazyLoad() {
        return lazyLoad;
    }

    public void setLazyLoad(Boolean lazyLoad) {
        this.lazyLoad = lazyLoad;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public FChartAnnType getType() {
        return type;
    }

    public void setType(FChartAnnType type) {
        this.type = type;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }


    public String getFillcolor() {
        return fillcolor;
    }

    public void setFillcolor(String fillcolor) {
        this.fillcolor = fillcolor;
    }

    public Integer getFillalpha() {
        return fillalpha;
    }

    public void setFillalpha(Integer fillalpha) {
        this.fillalpha = fillalpha;
    }


    public Integer getDashed() {
        return dashed;
    }

    public void setDashed(Integer dashed) {
        this.dashed = dashed;
    }


    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public AlignType getAlign() {
        return align;
    }

    public void setAlign(AlignType align) {
        this.align = align;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }


    public Boolean getBold() {
        return bold;
    }

    public void setBold(Boolean bold) {
        this.bold = bold;
    }

    public Integer getItalic() {
        return italic;
    }

    public void setItalic(Integer italic) {
        this.italic = italic;
    }

    public Boolean getWrap() {
        return wrap;
    }

    public void setWrap(Boolean wrap) {
        this.wrap = wrap;
    }

    public Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    public Integer getThickness() {
        return thickness;
    }

    public void setThickness(Integer thickness) {
        this.thickness = thickness;
    }

    public Boolean getShowbelow() {
        return showbelow;
    }

    public void setShowbelow(Boolean showbelow) {
        this.showbelow = showbelow;
    }


    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getAlpha() {
        return alpha;
    }

    public void setAlpha(Integer alpha) {
        this.alpha = alpha;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Boolean getShowshadow() {
        return showshadow;
    }

    public void setShowshadow(Boolean showshadow) {
        this.showshadow = showshadow;
    }

    public String getTooltext() {
        return tooltext;
    }

    public void setTooltext(String tooltext) {
        this.tooltext = tooltext;
    }

    public Integer getFontsize() {
        return fontsize;
    }

    public void setFontsize(Integer fontsize) {
        this.fontsize = fontsize;
    }

    public AlignType getTextalign() {
        return textalign;
    }

    public void setTextalign(AlignType textalign) {
        this.textalign = textalign;
    }

    public VAlignType getTextvalign() {
        return textvalign;
    }

    public void setTextvalign(VAlignType textvalign) {
        this.textvalign = textvalign;
    }

    public String getRotatetext() {
        return rotatetext;
    }

    public void setRotatetext(String rotatetext) {
        this.rotatetext = rotatetext;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public LineListItem getLineListItem() {
        return lineListItem;
    }

    public void setLineListItem(LineListItem lineListItem) {
        this.lineListItem = lineListItem;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Boolean getAutoscale() {
        return autoscale;
    }

    public void setAutoscale(Boolean autoscale) {
        this.autoscale = autoscale;
    }

    public Boolean getConstrainscale() {
        return constrainscale;
    }

    public void setConstrainscale(Boolean constrainscale) {
        this.constrainscale = constrainscale;
    }

    public Boolean getScaletext() {
        return scaletext;
    }

    public void setScaletext(Boolean scaletext) {
        this.scaletext = scaletext;
    }

    public Boolean getScaleimages() {
        return scaleimages;
    }

    public void setScaleimages(Boolean scaleimages) {
        this.scaleimages = scaleimages;
    }

    public Class getBindService() {
        return bindService;
    }

    public void setBindService(Class bindService) {
        this.bindService = bindService;
    }

    public Class getFristClass() {
        return fristClass;
    }

    public void setFristClass(Class fristClass) {
        this.fristClass = fristClass;
    }


    @Override
    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    public Set<CustomFormEvent> getEvent() {
        return event;
    }

    public void setEvent(Set<CustomFormEvent> event) {
        this.event = event;
    }

    public String getSourceClassName() {
        return sourceClassName;
    }

    public void setSourceClassName(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getRootClassName() {
        return rootClassName;
    }

    public void setRootClassName(String rootClassName) {
        this.rootClassName = rootClassName;
    }

    public String getRootMethodName() {
        return rootMethodName;
    }

    public void setRootMethodName(String rootMethodName) {
        this.rootMethodName = rootMethodName;
    }

    public String getViewClassName() {
        return viewClassName;
    }

    public void setViewClassName(String viewClassName) {
        this.viewClassName = viewClassName;
    }

    public CustomFChartViewBean getChartDataBean() {
        return chartDataBean;
    }

    public void setChartDataBean(CustomFChartViewBean chartDataBean) {
        this.chartDataBean = chartDataBean;
    }

    @Override
    public Set<ComponentType> getBindTypes() {
        return bindTypes;
    }

    public void setBindTypes(Set<ComponentType> bindTypes) {
        this.bindTypes = bindTypes;
    }

    @Override
    public RightContextMenuBean getContextMenuBean() {
        return contextMenuBean;
    }

    public void setContextMenuBean(RightContextMenuBean contextMenuBean) {
        this.contextMenuBean = contextMenuBean;
    }

    public ConstructorBean getConstructorBean() {
        return constructorBean;
    }

    public void setConstructorBean(ConstructorBean constructorBean) {
        this.constructorBean = constructorBean;
    }

    public MethodConfig getMethodConfig() {
        return methodConfig;
    }

    public void setMethodConfig(MethodConfig methodConfig) {
        this.methodConfig = methodConfig;
    }

    public AnnBlockListItemBean<T> fillData(ChartAnnBlockAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    @Override
    public int compareTo(TabItemBean o) {
        if (index == null) {
            return -1;
        }

        if (index != null && o.getIndex() != null) {
            return this.index - o.getIndex();
        }

        return 1;
    }
}
