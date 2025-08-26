package net.ooder.esd.tool.properties.fchart;

import com.alibaba.fastjson.JSONArray;
import net.ooder.common.JDSException;
import net.ooder.common.util.ClassUtility;
import net.ooder.config.ListResultModel;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.fchart.ChartDataBean;
import net.ooder.esd.bean.fchart.items.CategorieListItemBean;
import net.ooder.esd.bean.fchart.items.LineListItemBean;
import net.ooder.esd.bean.fchart.items.RawDataItemBean;
import net.ooder.esd.bean.field.CustomFChartFieldBean;
import net.ooder.esd.bean.view.CustomFChartViewBean;
import net.ooder.esd.util.ESDEnumsUtil;
import ognl.OgnlException;
import ognl.OgnlRuntime;

import java.util.ArrayList;
import java.util.List;

public class JSONData {

    ChartData chart;

    List<Categories> categories;

    List<DataSet> dataset;

    List<RawData> data;

    List<TrendLines> trendlines;

    AnnotationsData annotations;

    Dials dials;

    Colorrange colorrange;

    Trendpoints trendpoints;


    public JSONData() {

    }


    private <T> List<T> getEnumsData(CustomFChartViewBean chartViewBean, CustomMenuItem menuItem, Class<T> dataClass) {
        List<T> dataList = new ArrayList();
        try {
            if (chartViewBean.getMethodConfig() != null && chartViewBean.getMethodConfig().getDataBean() != null) {
                MethodConfig methodConfig = chartViewBean.getMethodConfig().getDataBean().getMethodByItem(menuItem);
                if (methodConfig != null) {

                    Object object = methodConfig.getRequestMethodBean().invok(JDSActionContext.getActionContext().getOgnlContext(), JDSActionContext.getActionContext().getContext());

                    if (object instanceof ListResultModel) {
                        ListResultModel<List> resultModel = (ListResultModel) object;
                        String json = JSONArray.toJSONString(resultModel.get());
                        dataList = JSONArray.parseArray(json, dataClass);
                    } else if (object instanceof List) {
                        for (Object obj : ((List) object)) {
                            dataList.add((T) OgnlRuntime.callConstructor(JDSActionContext.getActionContext().getOgnlContext(), dataClass.getName(), new Object[]{obj}));
                        }

                    }
                }
            }

        } catch (JDSException e) {
            e.printStackTrace();
        } catch (OgnlException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return dataList;
    }


    public JSONData(CustomFChartViewBean chartViewBean) {
        this.init(chartViewBean);
    }

    public JSONData(CustomFChartFieldBean chartViewBean) {
        this.init(chartViewBean.getViewBean());
    }

    void init(CustomFChartViewBean chartViewBean) {
        ChartDataBean chartDataBean = chartViewBean.getChartDataBean();
        if (chartDataBean != null) {
            chart = new ChartData(chartDataBean);
        }

        if (trendlines == null || trendlines.isEmpty()) {
            List<TrendLines> trendLines = new ArrayList<>();

            List<LineData> lineDatas = new ArrayList<>();
            List<LineListItemBean> lines = chartViewBean.getLineListItemBeans();
            if (lines != null) {
                for (LineListItemBean trendLine : lines) {
                    lineDatas.add(new LineData(trendLine));
                }
            }
            if (lineDatas == null || lineDatas.isEmpty()) {
                lineDatas = getEnumsData(chartViewBean, CustomMenuItem.TRENDLINESURL, LineData.class);
            }

            TrendLines trendLine = new TrendLines();
            trendLine.setLine(lineDatas);
            trendLines.add(trendLine);
            this.setTrendlines(trendLines);
        }


        if (data == null || data.isEmpty()) {
            List<RawData> rawDatas = new ArrayList<>();
            List<RawDataItemBean> rawDataBeans = chartViewBean.getRawDataBeans();
            if (rawDataBeans == null || rawDataBeans.isEmpty()) {
                rawDatas = getEnumsData(chartViewBean, CustomMenuItem.DATAURL, RawData.class);


            } else {
                for (RawDataItemBean rawDataItemBean : rawDataBeans) {
                    RawData rawData = new RawData(rawDataItemBean);
                    rawDatas.add(rawData);
                }
            }
            if (rawDataBeans == null || rawDataBeans.isEmpty()) {
                Class<? extends Enum> enumClass = chartViewBean.getEnumClass();
                Class<? extends Enum> viewClass = null;
                String viewClassName = chartViewBean.getViewClassName();
                if (viewClassName != null) {
                    Class clazz = null;
                    try {
                        clazz = ClassUtility.loadClass(viewClassName);
                        if (clazz.isEnum()) {
                            viewClass = clazz;
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                if (viewClass != null) {
                    rawDatas = ESDEnumsUtil.getEnumItems(viewClass, RawData.class);
                } else if (enumClass != null) {
                    rawDatas = ESDEnumsUtil.getEnumItems(enumClass, RawData.class);
                }

            }


            this.setData(rawDatas);
        }


        if (categories == null || categories.isEmpty()) {
            List<Categories> categoriesList = new ArrayList<>();
            Categories categories = new Categories();
            List<Categorie> categorieList = new ArrayList<>();
            List<CategorieListItemBean> categorieListItemBeans = chartViewBean.getCategorieListItemBeans();
            if (categorieListItemBeans != null) {
                for (CategorieListItemBean categorieListItemBean : categorieListItemBeans) {
                    Categorie categorie = new Categorie(categorieListItemBean);
                    categorieList.add(categorie);
                }
            }

            if (categorieList == null || categorieList.isEmpty()) {
                categorieList = getEnumsData(chartViewBean, CustomMenuItem.CATEGORIESURL, Categorie.class);
            }

            categories.setCategory(categorieList);
            categoriesList.add(categories);
            this.setCategories(categoriesList);
        }


        this.dataset = chartViewBean.getDataset();
        this.annotations = chartViewBean.getAnnotations();
        this.dials = chartViewBean.getDials();
        this.colorrange = chartViewBean.getColorrange();
        this.trendpoints = chartViewBean.getTrendpoints();
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

    public List<RawData> getData() {
        return data;
    }

    public void setData(List<RawData> data) {
        this.data = data;
    }

    public ChartData getChart() {
        return chart;
    }

    public void setChart(ChartData chart) {
        this.chart = chart;
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


    public List<TrendLines> getTrendlines() {
        return trendlines;
    }

    public void setTrendlines(List<TrendLines> trendlines) {
        this.trendlines = trendlines;
    }

}
