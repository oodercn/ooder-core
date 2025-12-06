package net.ooder.esd.bean.view;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.BottomBarMenu;
import net.ooder.esd.annotation.MenuBarMenu;
import net.ooder.esd.annotation.RightContextMenu;
import net.ooder.esd.annotation.field.FChartAnnotation;
import net.ooder.esd.annotation.field.TabItem;
import net.ooder.esd.annotation.field.ToolBarMenu;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.fchart.FChartType;
import net.ooder.esd.annotation.event.CustomFieldEvent;
import net.ooder.esd.annotation.menu.CustomFormMenu;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.*;
import net.ooder.esd.bean.fchart.ChartDataBean;
import net.ooder.esd.bean.fchart.items.CategorieListItemBean;
import net.ooder.esd.bean.fchart.items.LineListItemBean;
import net.ooder.esd.bean.fchart.items.RawDataItemBean;
import net.ooder.esd.custom.component.FullFChartComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.enums.MenuBarBean;
import net.ooder.esd.tool.component.FChartComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.fchart.*;
import net.ooder.esd.tool.properties.item.UIItem;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;
import net.ooder.web.util.JSONGenUtil;

import java.util.*;
import java.util.concurrent.Callable;

@AnnotationType(clazz = FChartAnnotation.class)
public class CustomFChartViewBean extends CustomViewBean<FieldFormConfig, UIItem,FullFChartComponent> implements CustomBean {

    ModuleViewType moduleViewType = ModuleViewType.CHARTCONFIG;

    String renderer;

    Boolean selectable;

    String chartCDN;

    String JSONUrl;

    String XMLUrl;

    FChartType chartType;

    Map<String, Object> XMLData;

    Map<String, Object> plotData;

    Map<String, Object> feedData;

    ChartDataBean chartDataBean;

    RightContextMenuBean contextMenuBean;

    List<CustomFormMenu> toolBarMenu = new ArrayList<>();

    List<CustomFormMenu> customMenu = new ArrayList<>();

    List<CustomFormMenu> bottombarMenu = new ArrayList<>();

    List<RawDataItemBean> rawDataBeans = new ArrayList<>();

    List<LineListItemBean> lineListItemBeans = new ArrayList<>();

    List<CategorieListItemBean> categorieListItemBeans = new ArrayList<>();

    List<Categories> categories;

    List<DataSet> dataset;

    List<RawData> data;

    List<TrendLines> trendlines;

    AnnotationsData annotations;

    Dials dials;

    Colorrange colorrange;

    Trendpoints trendpoints;

    Set<CustomFieldEvent> event = new LinkedHashSet<>();


    public CustomFChartViewBean() {

    }

    public CustomFChartViewBean(ModuleComponent moduleComponent) {
        AnnotationUtil.fillDefaultValue(FChartAnnotation.class, this);
        updateModule(moduleComponent);
    }

    public  List<Callable> updateModule(ModuleComponent moduleComponent) {
        List<Callable> tasks = new ArrayList<>();
        super.updateBaseModule(moduleComponent);
        FChartComponent chartComponent = (FChartComponent) moduleComponent.getCurrComponent();
        FChartProperties chartProperties = chartComponent.getProperties();
        List<TrendLines> lines = chartProperties.getJSONData().getTrendlines();

        if (lines != null) {
            for (TrendLines trendLine : lines) {
                lineListItemBeans = new ArrayList<>();
                List<LineData> lineData = trendLine.getLine();
                if (lineData.size() > 0) {
                    LineListItemBean lineListItemBean = new LineListItemBean(lineData.get(0));
                    lineListItemBeans.add(lineListItemBean);
                }
            }
        }

        List<RawData> rawDatas = chartProperties.getJSONData().getData();
        if (rawDatas != null) {
            rawDataBeans = new ArrayList<>();
            for (RawData rawData : rawDatas) {
                RawDataItemBean rawDataItemBean = new RawDataItemBean(rawData);
                rawDataBeans.add(rawDataItemBean);

            }
        }


        List<Categories> categoriesList = chartProperties.getJSONData().getCategories();
        if (categoriesList != null) {
            categorieListItemBeans = new ArrayList<>();
            for (Categories categories : categoriesList) {
                CategorieListItemBean rawDataItemBean = new CategorieListItemBean(categories);
                categorieListItemBeans.add(rawDataItemBean);
            }
        }
        this.init(chartProperties);
        return tasks;
    }


    public void init(FChartProperties chartProperties) {
        JSONData jsonData = chartProperties.getJSONData();
        ChartData chartData = chartProperties.getJSONData().getChart();
        this.chartDataBean = new ChartDataBean(chartData);
        this.categories = jsonData.getCategories();
        this.dataset = jsonData.getDataset();
        this.data = jsonData.getData();
        this.trendlines = jsonData.getTrendlines();
        this.annotations = jsonData.getAnnotations();
        this.dials = jsonData.getDials();
        this.colorrange = jsonData.getColorrange();
        this.trendpoints = jsonData.getTrendpoints();
        this.XMLData = chartProperties.getXMLData();
        this.plotData = chartProperties.getPlotData();
        this.feedData = chartProperties.getFeedData();
        this.renderer = chartProperties.getRenderer();
        this.selectable = chartProperties.getSelectable();
        this.chartCDN = chartProperties.getChartCDN();
        this.JSONUrl = chartProperties.getJSONUrl();
        this.XMLUrl = chartProperties.getXMLUrl();
        this.chartType = chartProperties.getChartType();


    }

    public CustomFChartViewBean(FChartAnnotation annotation) {
        this.fillData(annotation);
    }

    public CustomFChartViewBean(MethodConfig methodAPIBean) {
        super(methodAPIBean);
        this.chartDataBean = new ChartDataBean(methodAPIBean);
        Class clazz = JSONGenUtil.getInnerReturnType(methodAPIBean.getMethod());
        this.methodName = methodAPIBean.getMethodName();
        FChartAnnotation formAnnotation = AnnotationUtil.getClassAnnotation(clazz, FChartAnnotation.class);
        if (formAnnotation == null) {
            AnnotationUtil.fillDefaultValue(FChartAnnotation.class, this);
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
            this.initHiddenField(this.getViewClassName());
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet =super.getOtherClass();
        return classSet;
    }

    public List<Categories> getCategories() {
        return categories;
    }

    public void setCategories(List<Categories> categories) {
        this.categories = categories;
    }

    public List<DataSet> getDataset() {
        return dataset;
    }

    public void setDataset(List<DataSet> dataset) {
        this.dataset = dataset;
    }

    public List<RawData> getData() {
        return data;
    }

    public void setData(List<RawData> data) {
        this.data = data;
    }

    public List<TrendLines> getTrendlines() {
        return trendlines;
    }

    public void setTrendlines(List<TrendLines> trendlines) {
        this.trendlines = trendlines;
    }

    public AnnotationsData getAnnotations() {
        return annotations;
    }

    public void setAnnotations(AnnotationsData annotations) {
        this.annotations = annotations;
    }

    public Dials getDials() {
        return dials;
    }

    public void setDials(Dials dials) {
        this.dials = dials;
    }

    public Colorrange getColorrange() {
        return colorrange;
    }

    public void setColorrange(Colorrange colorrange) {
        this.colorrange = colorrange;
    }

    public Trendpoints getTrendpoints() {
        return trendpoints;
    }

    public void setTrendpoints(Trendpoints trendpoints) {
        this.trendpoints = trendpoints;
    }

    public CustomFChartViewBean fillData(FChartAnnotation annotation) {
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

        if (chartDataBean != null) {
            annotationBeans.addAll(chartDataBean.getAnnotationBeans());
        }


        annotationBeans.add(this);

        return annotationBeans;
    }

    public LineListItemBean getLineListItemBean(TabItem tabItem) {
        List<LineListItemBean> lineListItemBeans = this.getLineListItemBeans();
        for (LineListItemBean lineListItemBean : lineListItemBeans) {
            if (lineListItemBean.getLineListItem() != null && lineListItemBean.getLineListItem().equals(tabItem)) {
                return lineListItemBean;

            }
        }
        return null;
    }


    public RawDataItemBean getRawDataItemBean(TabItem tabItem) {
        List<RawDataItemBean> childTabViewBeans = this.getRawDataBeans();
        for (RawDataItemBean childTabViewBean : childTabViewBeans) {
            if (childTabViewBean.getDataListItem() != null && childTabViewBean.getDataListItem().equals(tabItem)) {
                return childTabViewBean;

            }
        }
        return null;
    }


    public RawDataItemBean getChildItemBean(String id) {
        List<RawDataItemBean> childItemViewBeans = this.getRawDataBeans();
        for (RawDataItemBean rawDataItemBean : childItemViewBeans) {
            if (rawDataItemBean.getId() != null && rawDataItemBean.getId().equals(id)) {
                return rawDataItemBean;

            }
        }
        return null;
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

    public List<RawDataItemBean> getRawDataBeans() {
        return rawDataBeans;
    }

    public void setRawDataBeans(List<RawDataItemBean> rawDataBeans) {
        this.rawDataBeans = rawDataBeans;
    }

    public List<LineListItemBean> getLineListItemBeans() {
        return lineListItemBeans;
    }

    public void setLineListItemBeans(List<LineListItemBean> lineListItemBeans) {
        this.lineListItemBeans = lineListItemBeans;
    }

    public List<CategorieListItemBean> getCategorieListItemBeans() {
        return categorieListItemBeans;
    }

    public void setCategorieListItemBeans(List<CategorieListItemBean> categorieListItemBeans) {
        this.categorieListItemBeans = categorieListItemBeans;
    }

    public void setModuleViewType(ModuleViewType moduleViewType) {
        this.moduleViewType = moduleViewType;
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

    public Set<CustomFieldEvent> getEvent() {
        return event;
    }

    public void setEvent(Set<CustomFieldEvent> event) {
        this.event = event;
    }

    public String getRenderer() {
        return renderer;
    }

    public void setRenderer(String renderer) {
        this.renderer = renderer;
    }

    public Boolean getSelectable() {
        return selectable;
    }

    public void setSelectable(Boolean selectable) {
        this.selectable = selectable;
    }

    public String getChartCDN() {
        return chartCDN;
    }

    public void setChartCDN(String chartCDN) {
        this.chartCDN = chartCDN;
    }

    public String getJSONUrl() {
        return JSONUrl;
    }

    public void setJSONUrl(String JSONUrl) {
        this.JSONUrl = JSONUrl;
    }

    public String getXMLUrl() {
        return XMLUrl;
    }


    public void setXMLUrl(String XMLUrl) {
        this.XMLUrl = XMLUrl;
    }

    public FChartType getChartType() {
        return chartType;
    }

    public void setChartType(FChartType chartType) {
        this.chartType = chartType;
    }


    public Map<String, Object> getXMLData() {
        return XMLData;
    }

    public void setXMLData(Map<String, Object> XMLData) {
        this.XMLData = XMLData;
    }

    public Map<String, Object> getPlotData() {
        return plotData;
    }

    public void setPlotData(Map<String, Object> plotData) {
        this.plotData = plotData;
    }

    public Map<String, Object> getFeedData() {
        return feedData;
    }

    public void setFeedData(Map<String, Object> feedData) {
        this.feedData = feedData;
    }

    public ChartDataBean getChartDataBean() {
        return chartDataBean;
    }

    public void setChartDataBean(ChartDataBean chartDataBean) {
        this.chartDataBean = chartDataBean;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    @Override
    public ModuleViewType getModuleViewType() {
        return moduleViewType;
    }
}
