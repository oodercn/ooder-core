package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.event.DataBinderEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.DataBinderProperties;

public class DataBinderComponent<T extends DataBinderProperties, K extends DataBinderEventEnum> extends Component<T, K> {
    public DataBinderProperties properties;
    public DataBinderComponent( String arieas,DataBinderProperties properties) {
        super(ComponentType.DATABINDER, arieas);
        this.properties = properties;
    }

    public DataBinderComponent() {
        super(ComponentType.DATABINDER);
    }

    @Override
    public T getProperties() {
        return (T) properties;
    }

    public void setProperties(DataBinderProperties properties) {
        this.properties = properties;
    }

}
