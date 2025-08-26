package net.ooder.esd.dsm.view.context;

import net.ooder.esd.annotation.ViewType;
import net.ooder.esd.annotation.fchart.ICategorie;
import net.ooder.esd.annotation.fchart.ILineList;
import net.ooder.esd.annotation.fchart.IRawData;
import net.ooder.esd.annotation.field.ToolBarMenu;
import net.ooder.esd.bean.view.CustomFChartViewBean;
import net.ooder.esd.bean.fchart.items.CategorieListItemBean;
import net.ooder.esd.bean.fchart.items.LineListItemBean;
import net.ooder.esd.bean.fchart.items.RawDataItemBean;
import net.ooder.esd.dsm.aggregation.context.AggViewRoot;
import net.ooder.esd.tool.properties.fchart.FColor;
import net.ooder.esd.tool.properties.fchart.FPoint;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;


public class FChartRoot extends BaseViewRoot<CustomFChartViewBean> {


    public String baseUrl;

    public String fullUrl;

    public String space;


    public AggViewRoot aggViewRoot;

    List<RawDataItemBean> rawDataBeans = new ArrayList<>();

    List<LineListItemBean> lineListItemBeans = new ArrayList<>();

    List<CategorieListItemBean> categorieListItemBeans = new ArrayList<>();

    List<FPoint> fPoints = new ArrayList<>();

    List<FColor> fColors = new ArrayList<>();

    private static final Class[] customClass = new Class[]{
            IRawData.class,
            ILineList.class,
            ICategorie.class,
            ToolBarMenu.class,
            ResponseBody.class};


    public FChartRoot(AggViewRoot aggViewRoot, CustomFChartViewBean viewBean, String moduleName, String className) {
        super(aggViewRoot, viewBean, moduleName, className);
        this.aggViewRoot = aggViewRoot;
        this.viewBean = viewBean;

        this.space = dsmBean.getSpace();
        this.rawDataBeans = viewBean.getRawDataBeans();
        this.lineListItemBeans = viewBean.getLineListItemBeans();
        this.categorieListItemBeans = viewBean.getCategorieListItemBeans();
        if (viewBean.getTrendpoints() != null) {
            fPoints = viewBean.getTrendpoints().getPoint();
        }
        if (viewBean.getColorrange() != null) {
            fColors = viewBean.getColorrange().getColor();
        }


    }

    @Override
    public String getBaseUrl() {
        return baseUrl;
    }

    @Override
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    @Override
    public String getSpace() {
        return space;
    }

    @Override
    public void setSpace(String space) {
        this.space = space;
    }

    public AggViewRoot getAggViewRoot() {
        return aggViewRoot;
    }

    public void setAggViewRoot(AggViewRoot aggViewRoot) {
        this.aggViewRoot = aggViewRoot;
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

    public List<FPoint> getfPoints() {
        return fPoints;
    }

    public void setfPoints(List<FPoint> fPoints) {
        this.fPoints = fPoints;
    }

    public List<FColor> getfColors() {
        return fColors;
    }

    public void setfColors(List<FColor> fColors) {
        this.fColors = fColors;
    }

    public Class[] getCustomClass() {
        return customClass;
    }

    @Override
    public ViewType getViewType() {
        return ViewType.FCHARTS;
    }
}
