package net.ooder.esd.custom.component;

import net.ooder.esd.annotation.event.MQTTEventEnum;
import net.ooder.esd.bean.MQTTBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.MQTTComponent;
import net.ooder.esd.tool.properties.MQTTProperties;

import java.util.Map;

public class CustomMQTTComponent<T extends MQTTProperties, K extends MQTTEventEnum> extends MQTTComponent<T, K> {


    public CustomMQTTComponent(EUModule euModule, FieldFormConfig<MQTTBean, ?> field, String target, Object value, Map valueMap) {
        super(field.getFieldname());
        MQTTBean mqttBean = field.getWidgetConfig();
        MQTTProperties properties = new MQTTProperties(mqttBean);
        this.setProperties(properties);
    }
}
