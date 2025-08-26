package net.ooder.esd.bean.fchart.items;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.bean.bar.ContextMenuBar;
import net.ooder.esd.annotation.fchart.ChartCategorieAnnotation;
import net.ooder.esd.annotation.event.CustomFormEvent;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.RightContextMenuBean;
import net.ooder.esd.bean.view.CustomFChartViewBean;
import net.ooder.esd.bean.nav.TabItemBean;
import net.ooder.esd.dsm.view.field.FieldItemConfig;
import net.ooder.esd.tool.properties.fchart.Categories;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.ConstructorBean;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.reflect.Constructor;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@AnnotationType(clazz = ChartCategorieAnnotation.class)
public class CategorieListItemBean<T extends FieldItemConfig> implements ContextMenuBar, Comparable<TabItemBean>, CustomBean {

    Integer index;

    Boolean lazyLoad;

    String id;

    String caption;

    String label;

    Boolean showlabel;

    String tooltext;

    String font;

    String fontcolor;

    Boolean fontbold;

    Boolean fontitalic;

    String bgcolor;

    String bordercolor;

    Integer alpha;

    Integer bgalpha;

    Integer borderalpha;

    Integer borderpadding;

    Integer borderradius;

    Integer borderthickness;

    Boolean borderdashed;

    Integer borderdashLen;

    Integer borderdashgap;

    String link;


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

    public CategorieListItemBean(Categories categories) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(categories), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }

    public CategorieListItemBean() {

    }


    public CategorieListItemBean(MethodConfig methodConfig, CustomFChartViewBean chartDataBean) {
        this.methodConfig = methodConfig;
        this.chartDataBean = chartDataBean;
        Class[] paramClass = methodConfig.getMethod().getParameterTypes();
        if (paramClass.length > 0) {
            this.fristClass = paramClass[0];
        }
        ChartCategorieAnnotation rawDataItem = AnnotationUtil.getMethodAnnotation(methodConfig.getMethod(), ChartCategorieAnnotation.class);
        if (rawDataItem != null) {
            fillData(rawDataItem);
        } else {
            AnnotationUtil.fillDefaultValue(ChartCategorieAnnotation.class, this);
        }


    }

    public CategorieListItemBean(Constructor constructor, CustomFChartViewBean chartDataBean) {
        constructorBean = new ConstructorBean(constructor);

        Class[] paramClass = constructor.getParameterTypes();
        if (paramClass.length > 0) {
            this.fristClass = paramClass[0];
        }

        ChartCategorieAnnotation rawDataItem = AnnotationUtil.getConstructorAnnotation(constructor, ChartCategorieAnnotation.class);
        if (rawDataItem != null) {
            fillData(rawDataItem);
        } else {
            AnnotationUtil.fillDefaultValue(ChartCategorieAnnotation.class, this);
        }

    }

    public Boolean getShowlabel() {
        return showlabel;
    }

    public void setShowlabel(Boolean showlabel) {
        this.showlabel = showlabel;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String getFontcolor() {
        return fontcolor;
    }

    public void setFontcolor(String fontcolor) {
        this.fontcolor = fontcolor;
    }

    public Boolean getFontbold() {
        return fontbold;
    }

    public void setFontbold(Boolean fontbold) {
        this.fontbold = fontbold;
    }

    public Boolean getFontitalic() {
        return fontitalic;
    }

    public void setFontitalic(Boolean fontitalic) {
        this.fontitalic = fontitalic;
    }

    public String getBgcolor() {
        return bgcolor;
    }

    public void setBgcolor(String bgcolor) {
        this.bgcolor = bgcolor;
    }

    public String getBordercolor() {
        return bordercolor;
    }

    public void setBordercolor(String bordercolor) {
        this.bordercolor = bordercolor;
    }

    public Integer getBgalpha() {
        return bgalpha;
    }

    public void setBgalpha(Integer bgalpha) {
        this.bgalpha = bgalpha;
    }

    public Integer getBorderalpha() {
        return borderalpha;
    }

    public void setBorderalpha(Integer borderalpha) {
        this.borderalpha = borderalpha;
    }

    public Integer getBorderpadding() {
        return borderpadding;
    }

    public void setBorderpadding(Integer borderpadding) {
        this.borderpadding = borderpadding;
    }

    public Integer getBorderradius() {
        return borderradius;
    }

    public void setBorderradius(Integer borderradius) {
        this.borderradius = borderradius;
    }

    public Integer getBorderthickness() {
        return borderthickness;
    }

    public void setBorderthickness(Integer borderthickness) {
        this.borderthickness = borderthickness;
    }

    public Boolean getBorderdashed() {
        return borderdashed;
    }

    public void setBorderdashed(Boolean borderdashed) {
        this.borderdashed = borderdashed;
    }

    public Integer getBorderdashLen() {
        return borderdashLen;
    }

    public void setBorderdashLen(Integer borderdashLen) {
        this.borderdashLen = borderdashLen;
    }

    public Integer getBorderdashgap() {
        return borderdashgap;
    }

    public void setBorderdashgap(Integer borderdashgap) {
        this.borderdashgap = borderdashgap;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public LineListItem getLineListItem() {
        return lineListItem;
    }

    public void setLineListItem(LineListItem lineListItem) {
        this.lineListItem = lineListItem;
    }

    public Boolean getLazyLoad() {
        return lazyLoad;
    }

    public void setLazyLoad(Boolean lazyLoad) {
        this.lazyLoad = lazyLoad;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }


    public Integer getAlpha() {
        return alpha;
    }

    public void setAlpha(Integer alpha) {
        this.alpha = alpha;
    }


    public String getTooltext() {
        return tooltext;
    }

    public void setTooltext(String tooltext) {
        this.tooltext = tooltext;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
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

    public CategorieListItemBean<T> fillData(ChartCategorieAnnotation annotation) {
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
