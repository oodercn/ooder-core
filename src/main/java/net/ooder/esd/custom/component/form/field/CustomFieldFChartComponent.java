package net.ooder.esd.custom.component.form.field;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.CustomFChartViewBean;
import net.ooder.esd.bean.field.CustomFChartFieldBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.FChartComponent;
import net.ooder.esd.tool.properties.fchart.FChartProperties;

import java.util.HashMap;
import java.util.Map;

public class CustomFieldFChartComponent extends FChartComponent {
    @JSONField(serialize = false)
    Map<String, Object> valueMap = new HashMap<>();
    @JSONField(serialize = false)
    EUModule euModule;


    public CustomFieldFChartComponent(EUModule euModule, FieldFormConfig<CustomFChartFieldBean, ?> field, String target, Object value, Map<String, Object> valueMap) {
        super(field.getFieldname());
        CustomFChartFieldBean customComponentViewBean = field.getWidgetConfig();
        Component component = customComponentViewBean.getComponent();
        if (component != null) {
            if (component.getCS() != null) {
                this.CS = component.getCS();
            }
            this.events = component.getEvents();
        }
        init(euModule, customComponentViewBean, valueMap);

    }

    public CustomFieldFChartComponent(EUModule euModule, FieldFormConfig<CustomFChartFieldBean, ?> field, Map valueMap) {
        super(field.getFieldname());
        CustomFChartFieldBean customComponentViewBean = field.getWidgetConfig();
        init(euModule, customComponentViewBean, valueMap);

    }

    public CustomFieldFChartComponent(EUModule euModule, MethodConfig methodConfig, Map valueMap) {
        super(methodConfig.getFieldName());
        CustomFChartViewBean customComponentViewBean = (CustomFChartViewBean) methodConfig.getView();
        init(euModule, customComponentViewBean, valueMap);

    }

    void init(EUModule euModule, CustomFChartFieldBean customComponentViewBean, Map dbMap) {
        FChartProperties divProperties = new FChartProperties(customComponentViewBean, customComponentViewBean.getContainerBean());
        this.setProperties(divProperties);
    }

    void init(EUModule euModule, CustomFChartViewBean customComponentViewBean, Map dbMap) {
        try {
            if (customComponentViewBean.getBindService() != null) {
                euModule.getComponent().addBindService(customComponentViewBean.getBindService());
            }
            for (Class serviceClass : customComponentViewBean.getCustomService()) {
                euModule.getComponent().addBindService(serviceClass);
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }
        FChartProperties divProperties = new FChartProperties(customComponentViewBean, customComponentViewBean.getContainerBean());
        this.setProperties(divProperties);
    }


}
