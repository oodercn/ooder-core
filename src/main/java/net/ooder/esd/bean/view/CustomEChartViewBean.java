package net.ooder.esd.bean.view;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.BottomBarMenu;
import net.ooder.esd.annotation.MenuBarMenu;
import net.ooder.esd.annotation.RightContextMenu;
import net.ooder.esd.annotation.field.ToolBarMenu;
import net.ooder.esd.annotation.view.EChartAnnotation;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.event.CustomFieldEvent;
import net.ooder.esd.annotation.menu.CustomFormMenu;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.*;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.dsm.aggregation.FieldAggConfig;
import net.ooder.esd.dsm.gen.view.GenTabsChildModule;
import net.ooder.esd.dsm.java.JavaGenSource;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.enums.MenuBarBean;
import net.ooder.esd.tool.component.EChartComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.echarts.*;
import net.ooder.esd.tool.properties.item.UIItem;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;
import net.ooder.web.util.JSONGenUtil;

import java.util.*;
import java.util.concurrent.Callable;

@AnnotationType(clazz = EChartAnnotation.class)
public class CustomEChartViewBean extends CustomViewBean<FieldFormConfig, UIItem, EChartComponent> implements CustomBean {

    ModuleViewType moduleViewType = ModuleViewType.ECHARTCONFIG;

    public String chartTheme;

    public String chartRenderer;

    public String bottom;


    public String chartCDN;

    public String chartCDNGL;


    public Integer chartDevicePixelRatio;

    public String display;

    public String xAxisDateFormatter;

    public Boolean chartResizeSilent;


    public AxisData xAxis;

    public AxisData yAxis;

    public AxisData xAxis3D;

    public AxisData yAxis3D;

    public AxisData zAxis3D;

    public Grid3D grid3D;

    public Boolean calculable;

    public Map<String, Object> title;

    public Map<String, Object> radar;

    public Map<String, Object> legend;


    public Map<String, Object> tooltip;

    public Map<String, Object> visualMap;

    public String backgroundColor;

    public ToolBox toolbox;

    public List<SeriesData> series;

    public List<String> color;


    public ChartOption chartOption;


    RightContextMenuBean contextMenuBean;

    List<CustomFormMenu> toolBarMenu = new ArrayList<>();

    List<CustomFormMenu> customMenu = new ArrayList<>();

    List<CustomFormMenu> bottombarMenu = new ArrayList<>();


    Set<CustomFieldEvent> event = new LinkedHashSet<>();


    public CustomEChartViewBean() {

    }

    public CustomEChartViewBean(EChartProperties chartProperties) {
        init(chartProperties);
    }

    public void init(EChartProperties chartProperties) {
        this.chartTheme = chartProperties.getChartTheme();
        this.chartRenderer = chartProperties.getChartRenderer();
        this.bottom = chartProperties.getBottom();
        this.chartCDN = chartProperties.getChartCDN();
        this.chartCDNGL = chartProperties.getChartCDNGL();
        this.chartDevicePixelRatio = chartProperties.getChartDevicePixelRatio();
        this.display = chartProperties.getDisplay();
        this.xAxisDateFormatter = chartProperties.getxAxisDateFormatter();
        this.chartOption = chartProperties.getChartOption();


    }

    public CustomEChartViewBean(ModuleComponent<EChartComponent> moduleComponent) {
        super();
        AnnotationUtil.fillDefaultValue(EChartAnnotation.class, this);
        this.updateModule(moduleComponent);

    }

    @Override
    public List<Callable<List<JavaGenSource>>> updateModule(ModuleComponent moduleComponent) {
        updateBaseModule(moduleComponent);
        List<Callable<List<JavaGenSource>>> javaSrcBeans = new ArrayList<>();
        EChartComponent chartComponent = (EChartComponent) moduleComponent.getCurrComponent();
        EChartProperties chartProperties = chartComponent.getProperties();
        this.init(chartProperties);
        return javaSrcBeans;
    }


    public List<JavaGenSource> buildAll() {

        return build(childModules);
    }

    public CustomEChartViewBean(EChartAnnotation annotation) {
        this.fillData(annotation);
    }

    public CustomEChartViewBean(MethodConfig methodAPIBean) {
        super(methodAPIBean);
        Class clazz = JSONGenUtil.getInnerReturnType(methodAPIBean.getMethod());
        this.methodName = methodAPIBean.getMethodName();
        EChartAnnotation formAnnotation = AnnotationUtil.getClassAnnotation(clazz, EChartAnnotation.class);
        if (formAnnotation == null) {
            AnnotationUtil.fillDefaultValue(EChartAnnotation.class, this);
        } else {
            AnnotationUtil.fillBean(formAnnotation, this);
        }


        RightContextMenu contextMenu = AnnotationUtil.getClassAnnotation(clazz, RightContextMenu.class);
        if (contextMenu != null) {
            contextMenuBean = new RightContextMenuBean(this.getId(),contextMenu);
        }
        if (toolBarMenu != null && toolBarMenu.size() > 0) {
            if (this.toolBar == null) {
                this.toolBar = AnnotationUtil.fillDefaultValue(ToolBarMenu.class, new ToolBarMenuBean());
            }
        }

        if (customMenu != null && customMenu.size() > 0) {
            if (this.menuBar == null) {
                this.menuBar = AnnotationUtil.fillDefaultValue(MenuBarMenu.class, new MenuBarBean());
            }
        }

        if (bottombarMenu != null && bottombarMenu.size() > 0) {
            if (this.bottomBar == null) {
                this.bottomBar = AnnotationUtil.fillDefaultValue(BottomBarMenu.class, new BottomBarMenuBean());
            }
        }
        try {
            AggEntityConfig esdClassConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(this.getViewClassName(), false);
            List<FieldAggConfig> cols = esdClassConfig.getFieldList();
            for (FieldAggConfig esdField : cols) {
                FieldFormConfig config = new FieldFormConfig(esdField, methodAPIBean.getSourceClassName(), methodAPIBean.getMethodName());
                fieldConfigMap.put(esdField.getFieldname(), config);
                fieldNames.add(esdField.getFieldname());
            }

        } catch (JDSException e) {
            e.printStackTrace();
        }
    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet = super.getOtherClass();
        return classSet;
    }


    public CustomEChartViewBean fillData(EChartAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.FCHART;
    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = super.getAnnotationBeans();

        if (menuBar == null) {
            if (customMenu != null && customMenu.size() > 0) {
                this.menuBar = AnnotationUtil.fillDefaultValue(MenuBarMenu.class, new MenuBarBean());
            }
        }
        if (bottomBar == null) {
            if (bottombarMenu != null && bottombarMenu.size() > 0) {
                this.bottomBar = AnnotationUtil.fillDefaultValue(BottomBarMenu.class, new BottomBarMenuBean());
            }
        }

        if (toolBar == null) {
            if (toolBarMenu != null && toolBarMenu.size() > 0) {
                this.toolBar = AnnotationUtil.fillDefaultValue(ToolBarMenu.class, new ToolBarMenuBean());
            }
        }


        if (menuBar != null) {
            annotationBeans.add(menuBar);
        }

        if (bottomBar != null) {
            annotationBeans.add(bottomBar);
        }

        if (toolBar != null) {
            annotationBeans.add(toolBar);
        }
        if (contextMenuBean != null) {
            annotationBeans.add(contextMenuBean);
        }

        annotationBeans.add(this);

        return annotationBeans;
    }


    @Override
    public ToolBarMenuBean getToolBar() {
        if (toolBar == null) {
            if (toolBarMenu != null && toolBarMenu.size() > 0) {
                this.toolBar = AnnotationUtil.fillDefaultValue(ToolBarMenu.class, new ToolBarMenuBean());
                this.toolBar.setMenus(toolBarMenu.toArray(new CustomFormMenu[]{}));
            }
        }
        return toolBar;
    }

    @Override
    public MenuBarBean getMenuBar() {
        if (menuBar == null) {
            if (customMenu != null && customMenu.size() > 0) {
                this.menuBar = AnnotationUtil.fillDefaultValue(MenuBarMenu.class, new MenuBarBean());
                this.menuBar.setMenus(customMenu.toArray(new CustomFormMenu[]{}));
            }
        }
        return menuBar;
    }


    @Override
    public BottomBarMenuBean getBottomBar() {
        if (bottomBar == null) {
            if (bottombarMenu != null && bottombarMenu.size() > 0) {
                this.bottomBar = AnnotationUtil.fillDefaultValue(BottomBarMenu.class, new BottomBarMenuBean());
                this.bottomBar.setBottombar(bottombarMenu.toArray(new CustomFormMenu[]{}));
            }
        }
        return bottomBar;
    }

    public void setModuleViewType(ModuleViewType moduleViewType) {
        this.moduleViewType = moduleViewType;
    }

    public String getChartTheme() {
        return chartTheme;
    }

    public void setChartTheme(String chartTheme) {
        this.chartTheme = chartTheme;
    }

    public String getChartRenderer() {
        return chartRenderer;
    }

    public void setChartRenderer(String chartRenderer) {
        this.chartRenderer = chartRenderer;
    }

    public String getBottom() {
        return bottom;
    }

    public void setBottom(String bottom) {
        this.bottom = bottom;
    }

    public String getChartCDN() {
        return chartCDN;
    }

    public void setChartCDN(String chartCDN) {
        this.chartCDN = chartCDN;
    }

    public String getChartCDNGL() {
        return chartCDNGL;
    }

    public void setChartCDNGL(String chartCDNGL) {
        this.chartCDNGL = chartCDNGL;
    }

    public Integer getChartDevicePixelRatio() {
        return chartDevicePixelRatio;
    }

    public void setChartDevicePixelRatio(Integer chartDevicePixelRatio) {
        this.chartDevicePixelRatio = chartDevicePixelRatio;
    }

    public AxisData getxAxis() {
        return xAxis;
    }

    public void setxAxis(AxisData xAxis) {
        this.xAxis = xAxis;
    }

    public AxisData getyAxis() {
        return yAxis;
    }

    public void setyAxis(AxisData yAxis) {
        this.yAxis = yAxis;
    }

    public AxisData getxAxis3D() {
        return xAxis3D;
    }

    public void setxAxis3D(AxisData xAxis3D) {
        this.xAxis3D = xAxis3D;
    }

    public AxisData getyAxis3D() {
        return yAxis3D;
    }

    public void setyAxis3D(AxisData yAxis3D) {
        this.yAxis3D = yAxis3D;
    }

    public AxisData getzAxis3D() {
        return zAxis3D;
    }

    public void setzAxis3D(AxisData zAxis3D) {
        this.zAxis3D = zAxis3D;
    }

    public Grid3D getGrid3D() {
        return grid3D;
    }

    public void setGrid3D(Grid3D grid3D) {
        this.grid3D = grid3D;
    }

    public Boolean getCalculable() {
        return calculable;
    }

    public void setCalculable(Boolean calculable) {
        this.calculable = calculable;
    }

    public Map<String, Object> getTitle() {
        return title;
    }

    public void setTitle(Map<String, Object> title) {
        this.title = title;
    }

    public Map<String, Object> getRadar() {
        return radar;
    }

    public void setRadar(Map<String, Object> radar) {
        this.radar = radar;
    }

    public Map<String, Object> getLegend() {
        return legend;
    }

    public void setLegend(Map<String, Object> legend) {
        this.legend = legend;
    }

    public Map<String, Object> getTooltip() {
        return tooltip;
    }

    public void setTooltip(Map<String, Object> tooltip) {
        this.tooltip = tooltip;
    }

    public Map<String, Object> getVisualMap() {
        return visualMap;
    }

    public void setVisualMap(Map<String, Object> visualMap) {
        this.visualMap = visualMap;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public ToolBox getToolbox() {
        return toolbox;
    }

    public void setToolbox(ToolBox toolbox) {
        this.toolbox = toolbox;
    }

    public List<SeriesData> getSeries() {
        return series;
    }

    public void setSeries(List<SeriesData> series) {
        this.series = series;
    }

    public List<String> getColor() {
        return color;
    }

    public void setColor(List<String> color) {
        this.color = color;
    }

    public RightContextMenuBean getContextMenuBean() {
        return contextMenuBean;
    }

    public void setContextMenuBean(RightContextMenuBean contextMenuBean) {
        this.contextMenuBean = contextMenuBean;
    }

    public List<CustomFormMenu> getToolBarMenu() {
        return toolBarMenu;
    }

    public void setToolBarMenu(List<CustomFormMenu> toolBarMenu) {
        this.toolBarMenu = toolBarMenu;
    }

    public List<CustomFormMenu> getCustomMenu() {
        return customMenu;
    }

    public void setCustomMenu(List<CustomFormMenu> customMenu) {
        this.customMenu = customMenu;
    }

    public List<CustomFormMenu> getBottombarMenu() {
        return bottombarMenu;
    }

    public void setBottombarMenu(List<CustomFormMenu> bottombarMenu) {
        this.bottombarMenu = bottombarMenu;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getxAxisDateFormatter() {
        return xAxisDateFormatter;
    }

    public void setxAxisDateFormatter(String xAxisDateFormatter) {
        this.xAxisDateFormatter = xAxisDateFormatter;
    }

    public Boolean getChartResizeSilent() {
        return chartResizeSilent;
    }

    public void setChartResizeSilent(Boolean chartResizeSilent) {
        this.chartResizeSilent = chartResizeSilent;
    }

    public ChartOption getChartOption() {
        return chartOption;
    }

    public void setChartOption(ChartOption chartOption) {
        this.chartOption = chartOption;
    }

    public Set<CustomFieldEvent> getEvent() {
        return event;
    }

    public void setEvent(Set<CustomFieldEvent> event) {
        this.event = event;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    @Override
    public ModuleViewType getModuleViewType() {
        return moduleViewType;
    }
}
