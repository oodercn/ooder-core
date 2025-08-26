package net.ooder.esd.custom.component.form.field;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.bean.view.CustomEChartViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.field.CustomEChartFieldBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.EChartComponent;
import net.ooder.esd.tool.properties.echarts.EChartProperties;

import java.util.HashMap;
import java.util.Map;

public class CustomFieldEChartComponent extends EChartComponent {
    @JSONField(serialize = false)
    Map<String, Object> valueMap = new HashMap<>();
    @JSONField(serialize = false)
    EUModule euModule;


    public CustomFieldEChartComponent(EUModule euModule, FieldFormConfig<CustomEChartFieldBean, ?> field, String target, Object value, Map<String, Object> valueMap) {
        super(field.getFieldname());
        CustomEChartFieldBean customComponentViewBean = field.getWidgetConfig();
        Component component = customComponentViewBean.getComponent();
        if (component != null) {
            if (component.getCS() != null) {
                this.CS = component.getCS();
            }
            this.events = component.getEvents();
        }

        init(euModule, customComponentViewBean, valueMap);

    }

    public CustomFieldEChartComponent(EUModule euModule, FieldFormConfig<CustomEChartFieldBean, ?> field, Map valueMap) {
        super(field.getFieldname());
        CustomEChartFieldBean customComponentViewBean = field.getWidgetConfig();

        init(euModule, customComponentViewBean, valueMap);

    }

    public CustomFieldEChartComponent(EUModule euModule, MethodConfig methodConfig, Map valueMap) {
        super(methodConfig.getFieldName());
        CustomEChartViewBean customComponentViewBean = (CustomEChartViewBean) methodConfig.getView();
        init(euModule, customComponentViewBean, valueMap);

    }

    void init(EUModule euModule, CustomEChartViewBean customComponentViewBean, Map dbMap) {
        EChartProperties divProperties = new EChartProperties(customComponentViewBean, customComponentViewBean.getContainerBean());
        this.setProperties(divProperties);
    }

    void init(EUModule euModule, CustomEChartFieldBean customComponentViewBean, Map dbMap) {
        EChartProperties divProperties = new EChartProperties(customComponentViewBean, customComponentViewBean.getContainerBean());
        this.setProperties(divProperties);
    }


}
