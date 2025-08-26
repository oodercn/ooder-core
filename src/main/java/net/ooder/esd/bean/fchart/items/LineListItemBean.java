package net.ooder.esd.bean.fchart.items;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.bean.bar.ContextMenuBar;
import net.ooder.esd.annotation.fchart.LineListItemAnnotation;
import net.ooder.esd.annotation.fchart.ValuePosition;
import net.ooder.esd.annotation.event.CustomFormEvent;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.HAlignType;
import net.ooder.esd.annotation.ui.VAlignType;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.RightContextMenuBean;
import net.ooder.esd.bean.view.CustomFChartViewBean;
import net.ooder.esd.bean.nav.TabItemBean;
import net.ooder.esd.dsm.view.field.FieldItemConfig;
import net.ooder.esd.tool.properties.fchart.LineData;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.ConstructorBean;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.reflect.Constructor;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@AnnotationType(clazz = LineListItemAnnotation.class)
public class LineListItemBean<T extends FieldItemConfig> implements ContextMenuBar, Comparable<TabItemBean>, CustomBean {

    Integer startvalue;
    String color;
    String displayvalue;
    Boolean showontop;
    Integer index;
    String id;
    String caption;


    ValuePosition parentyaxis;
    Integer endvalue;
    Boolean istrendzone;
    Integer thickness;
    Integer alpha;
    Integer dashed;
    Integer dashlen;
    Integer dashgap;
    Boolean valueonright;
    String tooltext;


    //VLine
    Integer lineposition;
    Boolean showlabelborder;
    String label;
    Integer labelposition;
    HAlignType labelhalign;
    VAlignType labelvalign;
    Integer startindex;
    Integer endindex;
    Boolean displayalways;
    Integer displaywhencount;
    Boolean valueontop;


    @JSONField(serialize = false)
    Set<ComponentType> bindTypes = new LinkedHashSet<>();

    RightContextMenuBean contextMenuBean;

    ConstructorBean constructorBean;

    Boolean lazyLoad;

    Class bindService;

    LineListItem lineListItem;

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

    public LineListItemBean(LineData lineData) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(lineData), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);

    }

    public LineListItemBean() {

    }

    public LineListItemBean(MethodConfig methodConfig, CustomFChartViewBean chartDataBean) {
        this.methodConfig = methodConfig;
        this.chartDataBean = chartDataBean;
        Class[] paramClass = methodConfig.getMethod().getParameterTypes();
        if (paramClass.length > 0) {
            this.fristClass = paramClass[0];
        }
        LineListItemAnnotation rawDataItem = AnnotationUtil.getMethodAnnotation(methodConfig.getMethod(), LineListItemAnnotation.class);
        if (rawDataItem != null) {
            fillData(rawDataItem);
        } else {
            AnnotationUtil.fillDefaultValue(LineListItemAnnotation.class, this);
        }


    }

    public LineListItemBean(Constructor constructor, CustomFChartViewBean chartDataBean) {
        constructorBean = new ConstructorBean(constructor);
        this.chartDataBean = chartDataBean;

        Class[] paramClass = constructor.getParameterTypes();
        if (paramClass.length > 0) {
            this.fristClass = paramClass[0];
        }

        LineListItemAnnotation rawDataItem = AnnotationUtil.getConstructorAnnotation(constructor, LineListItemAnnotation.class);
        if (rawDataItem != null) {
            fillData(rawDataItem);
        } else {
            AnnotationUtil.fillDefaultValue(LineListItemAnnotation.class, this);
        }

    }

    public Integer getStartvalue() {
        return startvalue;
    }

    public void setStartvalue(Integer startvalue) {
        this.startvalue = startvalue;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDisplayvalue() {
        return displayvalue;
    }

    public void setDisplayvalue(String displayvalue) {
        this.displayvalue = displayvalue;
    }

    public Boolean getShowontop() {
        return showontop;
    }

    public void setShowontop(Boolean showontop) {
        this.showontop = showontop;
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


    public ValuePosition getParentyaxis() {
        return parentyaxis;
    }

    public void setParentyaxis(ValuePosition parentyaxis) {
        this.parentyaxis = parentyaxis;
    }

    public Integer getEndvalue() {
        return endvalue;
    }

    public void setEndvalue(Integer endvalue) {
        this.endvalue = endvalue;
    }

    public Boolean getIstrendzone() {
        return istrendzone;
    }

    public void setIstrendzone(Boolean istrendzone) {
        this.istrendzone = istrendzone;
    }

    public Integer getThickness() {
        return thickness;
    }

    public void setThickness(Integer thickness) {
        this.thickness = thickness;
    }

    public Integer getAlpha() {
        return alpha;
    }

    public void setAlpha(Integer alpha) {
        this.alpha = alpha;
    }

    public Integer getDashed() {
        return dashed;
    }

    public void setDashed(Integer dashed) {
        this.dashed = dashed;
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

    public Boolean getValueonright() {
        return valueonright;
    }

    public void setValueonright(Boolean valueonright) {
        this.valueonright = valueonright;
    }

    public String getTooltext() {
        return tooltext;
    }

    public void setTooltext(String tooltext) {
        this.tooltext = tooltext;
    }

    public Integer getLineposition() {
        return lineposition;
    }

    public void setLineposition(Integer lineposition) {
        this.lineposition = lineposition;
    }

    public Boolean getShowlabelborder() {
        return showlabelborder;
    }

    public void setShowlabelborder(Boolean showlabelborder) {
        this.showlabelborder = showlabelborder;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getLabelposition() {
        return labelposition;
    }

    public void setLabelposition(Integer labelposition) {
        this.labelposition = labelposition;
    }

    public HAlignType getLabelhalign() {
        return labelhalign;
    }

    public void setLabelhalign(HAlignType labelhalign) {
        this.labelhalign = labelhalign;
    }

    public VAlignType getLabelvalign() {
        return labelvalign;
    }

    public void setLabelvalign(VAlignType labelvalign) {
        this.labelvalign = labelvalign;
    }

    public Integer getStartindex() {
        return startindex;
    }

    public void setStartindex(Integer startindex) {
        this.startindex = startindex;
    }

    public Integer getEndindex() {
        return endindex;
    }

    public void setEndindex(Integer endindex) {
        this.endindex = endindex;
    }

    public Boolean getDisplayalways() {
        return displayalways;
    }

    public void setDisplayalways(Boolean displayalways) {
        this.displayalways = displayalways;
    }

    public Integer getDisplaywhencount() {
        return displaywhencount;
    }

    public void setDisplaywhencount(Integer displaywhencount) {
        this.displaywhencount = displaywhencount;
    }

    public Boolean getValueontop() {
        return valueontop;
    }

    public void setValueontop(Boolean valueontop) {
        this.valueontop = valueontop;
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

    public LineListItemBean<T> fillData(LineListItemAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    @Override
    public int compareTo(TabItemBean o) {
        return index - o.getIndex();
    }
}
