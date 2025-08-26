package net.ooder.esd.custom.component;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.CustomFChartViewBean;
import net.ooder.esd.bean.field.CustomFChartFieldBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.FChartComponent;
import net.ooder.esd.tool.properties.fchart.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomFChartComponent extends FChartComponent {
    @JSONField(serialize = false)
    Map<String, Object> valueMap = new HashMap<>();
    @JSONField(serialize = false)
    EUModule euModule;

    List<Categories> categories;

    List<DataSet> dataset;

    List<RawData> data;

    List<TrendLines> trendlines;


    public CustomFChartComponent(EUModule euModule, FieldFormConfig<CustomFChartFieldBean, ?> field, String target, Object value, Map<String, Object> valueMap) {
        super(field.getFieldname());
        CustomFChartFieldBean fieldChartViewBean = field.getWidgetConfig();
        CustomFChartViewBean chartViewBean= (CustomFChartViewBean) fieldChartViewBean.getViewBean();
        init(euModule, chartViewBean, valueMap);

    }

    public CustomFChartComponent(EUModule euModule, FieldFormConfig<CustomFChartFieldBean, ?> field, Map valueMap) {
        super(field.getFieldname());
        CustomFChartFieldBean fieldChartViewBean = field.getWidgetConfig();
        CustomFChartViewBean chartViewBean= fieldChartViewBean.getViewBean();
        init(euModule, chartViewBean, valueMap);

    }

    public CustomFChartComponent(EUModule euModule, MethodConfig methodConfig, Map valueMap) {
        super(methodConfig.getFieldName());
        CustomFChartViewBean chartViewBean = (CustomFChartViewBean) methodConfig.getView();
        init(euModule, chartViewBean, valueMap);

    }

    void init(EUModule euModule, CustomFChartViewBean customComponentViewBean, Map dbMap) {
        FChartProperties properties = new FChartProperties(customComponentViewBean,customComponentViewBean.getContainerBean());
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
