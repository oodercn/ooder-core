package net.ooder.esd.tool.component;


import net.ooder.esd.annotation.event.MQTTEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.MQTTProperties;

public class MQTTComponent<T extends MQTTProperties, K extends MQTTEventEnum> extends Component<T, K> {
    public MQTTProperties properties;
    public MQTTComponent(String arieas,MQTTProperties properties) {
        super(ComponentType.MQTT, arieas);
        this.properties=properties;
    }

    public MQTTComponent(String arieas) {
        super(ComponentType.MQTT, arieas);
        this.properties=new MQTTProperties();
    }

    public MQTTComponent addAction(Action<K> action) {
        super.addAction( action);
        return this;
    }

    public MQTTComponent() {
        super(ComponentType.MQTT);
    }

    @Override
    public T getProperties() {
        return (T) properties;
    }

    public void setProperties(MQTTProperties properties) {
        this.properties = properties;
    }

}
