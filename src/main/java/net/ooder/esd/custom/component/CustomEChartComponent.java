package net.ooder.esd.custom.component;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.bean.view.CustomEChartViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.field.CustomEChartFieldBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.EChartComponent;
import net.ooder.esd.tool.properties.echarts.EChartProperties;
import net.ooder.esd.tool.properties.fchart.Categories;
import net.ooder.esd.tool.properties.fchart.DataSet;
import net.ooder.esd.tool.properties.fchart.RawData;
import net.ooder.esd.tool.properties.fchart.TrendLines;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomEChartComponent extends EChartComponent {
    @JSONField(serialize = false)
    Map<String, Object> valueMap = new HashMap<>();
    @JSONField(serialize = false)
    EUModule euModule;

    List<Categories> categories;

    List<DataSet> dataset;

    List<RawData> data;

    List<TrendLines> trendlines;


    public CustomEChartComponent(EUModule euModule, FieldFormConfig<CustomEChartFieldBean, ?> field, String target, Object value, Map<String, Object> valueMap) {
        super(field.getFieldname());
        CustomEChartFieldBean fieldChartViewBean = field.getWidgetConfig();
        CustomEChartViewBean chartViewBean = fieldChartViewBean.getViewBean();
        init(euModule, chartViewBean, valueMap);

    }

    public CustomEChartComponent(EUModule euModule, FieldFormConfig<CustomEChartFieldBean, ?> field, Map valueMap) {
        super(field.getFieldname());
        CustomEChartFieldBean fieldChartViewBean = field.getWidgetConfig();
        CustomEChartViewBean chartViewBean = (CustomEChartViewBean) fieldChartViewBean.getViewBean();
        init(euModule, chartViewBean, valueMap);

    }

    public CustomEChartComponent(EUModule euModule, MethodConfig methodConfig, Map valueMap) {
        super(methodConfig.getFieldName());
        CustomEChartViewBean chartViewBean = (CustomEChartViewBean) methodConfig.getView();
        init(euModule, chartViewBean, valueMap);

    }

    void init(EUModule euModule, CustomEChartViewBean customComponentViewBean, Map dbMap) {
        EChartProperties properties = new EChartProperties(customComponentViewBean, customComponentViewBean.getContainerBean());
        this.setProperties(properties);
    }

    public Map<String, Object> getValueMap() {
        return valueMap;
    }

    public void setValueMap(Map<String, Object> valueMap) {
        this.valueMap = valueMap;
    }

    public EUModule getEuModule() {
        return euModule;
    }

    public void setEuModule(EUModule euModule) {
        this.euModule = euModule;
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
}
