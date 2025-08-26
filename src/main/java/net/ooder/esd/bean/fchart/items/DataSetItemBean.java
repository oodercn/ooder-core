package net.ooder.esd.bean.fchart.items;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.RightContextMenu;
import net.ooder.esd.annotation.event.CustomFormEvent;
import net.ooder.esd.annotation.fchart.RawDataItemAnnotation;
import net.ooder.esd.annotation.fchart.ValuePosition;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.RightContextMenuBean;
import net.ooder.esd.bean.bar.ContextMenuBar;
import net.ooder.esd.bean.view.CustomFChartViewBean;
import net.ooder.esd.dsm.view.field.FieldItemConfig;
import net.ooder.esd.tool.properties.fchart.RawData;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.ConstructorBean;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.reflect.Constructor;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@AnnotationType(clazz = RawDataItemAnnotation.class)
public class DataSetItemBean<T extends FieldItemConfig> implements ContextMenuBar, Comparable<DataSetItemBean>, CustomBean {


    String seriesname;

    String color;

    Integer alpha;

    ValuePosition valueposition;

    Boolean showvalues;

    Integer dashed;

    Boolean includeinlegend;

    Boolean drawanchors;

    Integer anchorradius;

    String anchorbordercolor;

    Integer anchorborderthickness;

    String anchorbgcolor;

    Integer anchoralpha;

    Integer anchorbgalpha;

    Integer linethickness;

    Integer linedashlen;

    Integer linedashgap;

    Integer plotborderalpha;

    Integer plotborderthickness;

    String plotborderbolor;

    Boolean showplotborder;

    Boolean showregressionline;

    Boolean showyonx;

    String regressionlinecolor;

    Integer regressionlinethickness;

    Integer regressionlinealpha;

    Integer index;
    Boolean lazyLoad;
    String id;
    String caption;


    List<RawData> data;

    @JSONField(serialize = false)
    Set<ComponentType> bindTypes = new LinkedHashSet<>();

    RightContextMenuBean contextMenuBean;

    ConstructorBean constructorBean;

    Class bindService;

    DataSetListItem dataSetListItem;

    Set<CustomFormEvent> event = new LinkedHashSet<>();


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

    public DataSetItemBean() {

    }

    public DataSetItemBean(RawData rawData) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(rawData), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }


    public DataSetItemBean(MethodConfig methodConfig, CustomFChartViewBean chartDataBean) {
        this.methodConfig = methodConfig;
        this.chartDataBean = chartDataBean;
        Class[] paramClass = methodConfig.getMethod().getParameterTypes();
        if (paramClass.length > 0) {
            this.fristClass = paramClass[0];
        }
        RawDataItemAnnotation rawDataItem = AnnotationUtil.getMethodAnnotation(methodConfig.getMethod(), RawDataItemAnnotation.class);
        if (rawDataItem != null) {
            fillData(rawDataItem);
        } else {
            AnnotationUtil.fillDefaultValue(RawDataItemAnnotation.class, this);
        }

        RightContextMenu annotation = AnnotationUtil.getMethodAnnotation(methodConfig.getMethod(), RightContextMenu.class);
        if (annotation != null) {
            contextMenuBean = new RightContextMenuBean(this.getId(), annotation);
        }

    }

    public DataSetItemBean(Constructor constructor, CustomFChartViewBean chartDataBean) {
        constructorBean = new ConstructorBean(constructor);

        Class[] paramClass = constructor.getParameterTypes();
        if (paramClass.length > 0) {
            this.fristClass = paramClass[0];
        }

        RawDataItemAnnotation rawDataItem = AnnotationUtil.getConstructorAnnotation(constructor, RawDataItemAnnotation.class);
        if (rawDataItem != null) {
            fillData(rawDataItem);
        } else {
            AnnotationUtil.fillDefaultValue(RawDataItemAnnotation.class, this);
        }

        RightContextMenu annotation = AnnotationUtil.getConstructorAnnotation(constructor, RightContextMenu.class);
        if (annotation != null) {
            contextMenuBean = new RightContextMenuBean(this.getId(), annotation);
        }
    }

    public Class getBindService() {
        return bindService;
    }

    public void setBindService(Class bindService) {
        this.bindService = bindService;
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

    public DataSetItemBean<T> fillData(RawDataItemAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String getSeriesname() {
        return seriesname;
    }

    public void setSeriesname(String seriesname) {
        this.seriesname = seriesname;
    }

    public ValuePosition getValueposition() {
        return valueposition;
    }

    public void setValueposition(ValuePosition valueposition) {
        this.valueposition = valueposition;
    }

    public Boolean getShowvalues() {
        return showvalues;
    }

    public void setShowvalues(Boolean showvalues) {
        this.showvalues = showvalues;
    }

    public Integer getDashed() {
        return dashed;
    }

    public void setDashed(Integer dashed) {
        this.dashed = dashed;
    }

    public Boolean getIncludeinlegend() {
        return includeinlegend;
    }

    public void setIncludeinlegend(Boolean includeinlegend) {
        this.includeinlegend = includeinlegend;
    }

    public Boolean getDrawanchors() {
        return drawanchors;
    }

    public void setDrawanchors(Boolean drawanchors) {
        this.drawanchors = drawanchors;
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

    public Integer getLinethickness() {
        return linethickness;
    }

    public void setLinethickness(Integer linethickness) {
        this.linethickness = linethickness;
    }

    public Integer getLinedashlen() {
        return linedashlen;
    }

    public void setLinedashlen(Integer linedashlen) {
        this.linedashlen = linedashlen;
    }

    public Integer getLinedashgap() {
        return linedashgap;
    }

    public void setLinedashgap(Integer linedashgap) {
        this.linedashgap = linedashgap;
    }

    public Integer getPlotborderalpha() {
        return plotborderalpha;
    }

    public void setPlotborderalpha(Integer plotborderalpha) {
        this.plotborderalpha = plotborderalpha;
    }

    public Integer getPlotborderthickness() {
        return plotborderthickness;
    }

    public void setPlotborderthickness(Integer plotborderthickness) {
        this.plotborderthickness = plotborderthickness;
    }

    public String getPlotborderbolor() {
        return plotborderbolor;
    }

    public void setPlotborderbolor(String plotborderbolor) {
        this.plotborderbolor = plotborderbolor;
    }

    public Boolean getShowplotborder() {
        return showplotborder;
    }

    public void setShowplotborder(Boolean showplotborder) {
        this.showplotborder = showplotborder;
    }

    public Boolean getShowregressionline() {
        return showregressionline;
    }

    public void setShowregressionline(Boolean showregressionline) {
        this.showregressionline = showregressionline;
    }

    public Boolean getShowyonx() {
        return showyonx;
    }

    public void setShowyonx(Boolean showyonx) {
        this.showyonx = showyonx;
    }

    public String getRegressionlinecolor() {
        return regressionlinecolor;
    }

    public void setRegressionlinecolor(String regressionlinecolor) {
        this.regressionlinecolor = regressionlinecolor;
    }

    public Integer getRegressionlinethickness() {
        return regressionlinethickness;
    }

    public void setRegressionlinethickness(Integer regressionlinethickness) {
        this.regressionlinethickness = regressionlinethickness;
    }

    public Integer getRegressionlinealpha() {
        return regressionlinealpha;
    }

    public void setRegressionlinealpha(Integer regressionlinealpha) {
        this.regressionlinealpha = regressionlinealpha;
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

    public List<RawData> getData() {
        return data;
    }

    public void setData(List<RawData> data) {
        this.data = data;
    }

    public DataSetListItem getDataSetListItem() {
        return dataSetListItem;
    }

    public void setDataSetListItem(DataSetListItem dataSetListItem) {
        this.dataSetListItem = dataSetListItem;
    }

    @Override
    public int compareTo(DataSetItemBean o) {
        if (index == null) {
            return -1;
        }

        if (index != null && o.getIndex() != null) {
            return this.index - o.getIndex();
        }

        return 1;
    }
}
