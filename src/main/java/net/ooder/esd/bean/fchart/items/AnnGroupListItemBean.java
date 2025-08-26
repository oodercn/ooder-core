package net.ooder.esd.bean.fchart.items;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.bean.bar.ContextMenuBar;
import net.ooder.esd.annotation.fchart.ChartAnnGroupAnnotation;
import net.ooder.esd.annotation.event.CustomFormEvent;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.RightContextMenuBean;
import net.ooder.esd.bean.view.CustomFChartViewBean;
import net.ooder.esd.bean.nav.TabItemBean;
import net.ooder.esd.dsm.view.field.FieldItemConfig;
import net.ooder.web.ConstructorBean;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.reflect.Constructor;
import java.util.LinkedHashSet;
import java.util.Set;

@AnnotationType(clazz = ChartAnnGroupAnnotation.class)
public class AnnGroupListItemBean<T extends FieldItemConfig> implements ContextMenuBar, Comparable<TabItemBean>, CustomBean {

    Integer index;
    Boolean lazyLoad;
    String id;
    String caption;

    Integer x;
    Integer y;
    Boolean showbelow;
    Boolean autoscale;
    Boolean constrainscale;
    Boolean scaletext;
    Boolean scaleimages;
    Integer xshift;
    Integer yshift;


    @JSONField(serialize = false)
    Set<ComponentType> bindTypes = new LinkedHashSet<>();

    RightContextMenuBean contextMenuBean;

    ConstructorBean constructorBean;

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

    public AnnGroupListItemBean() {

    }


    public AnnGroupListItemBean(MethodConfig methodConfig, CustomFChartViewBean chartDataBean) {
        this.methodConfig = methodConfig;
        this.chartDataBean = chartDataBean;
        Class[] paramClass = methodConfig.getMethod().getParameterTypes();
        if (paramClass.length > 0) {
            this.fristClass = paramClass[0];
        }
        ChartAnnGroupAnnotation rawDataItem = AnnotationUtil.getMethodAnnotation(methodConfig.getMethod(), ChartAnnGroupAnnotation.class);
        if (rawDataItem != null) {
            fillData(rawDataItem);
        } else {
            AnnotationUtil.fillDefaultValue(ChartAnnGroupAnnotation.class, this);
        }


    }

    public AnnGroupListItemBean(Constructor constructor, CustomFChartViewBean chartDataBean) {
        constructorBean = new ConstructorBean(constructor);

        Class[] paramClass = constructor.getParameterTypes();
        if (paramClass.length > 0) {
            this.fristClass = paramClass[0];
        }

        ChartAnnGroupAnnotation rawDataItem = AnnotationUtil.getConstructorAnnotation(constructor, ChartAnnGroupAnnotation.class);
        if (rawDataItem != null) {
            fillData(rawDataItem);
        } else {
            AnnotationUtil.fillDefaultValue(ChartAnnGroupAnnotation.class, this);
        }

    }

    public LineListItem getLineListItem() {
        return lineListItem;
    }

    public void setLineListItem(LineListItem lineListItem) {
        this.lineListItem = lineListItem;
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

    public Boolean getShowbelow() {
        return showbelow;
    }

    public void setShowbelow(Boolean showbelow) {
        this.showbelow = showbelow;
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

    public AnnGroupListItemBean<T> fillData(ChartAnnGroupAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    @Override
    public int compareTo(TabItemBean o) {
        return index - o.getIndex();
    }
}
