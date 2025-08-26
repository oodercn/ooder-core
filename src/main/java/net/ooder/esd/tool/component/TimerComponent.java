package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.event.NullEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.TimerProperties;

public class TimerComponent extends Component<TimerProperties, NullEventEnum> {
    public TimerProperties properties;


    public TimerComponent(String arieas, TimerProperties properties) {
        super(ComponentType.TIMER, arieas);
        this.properties = properties;
    }

    public TimerComponent() {
        super(ComponentType.TIMER);
    }

    public TimerComponent addAction(Action<NullEventEnum> action) {
        super.addAction( action);
        return this;
    }

    @Override
    public TimerProperties getProperties() {
        return properties;
    }

    public void setProperties(TimerProperties properties) {
        this.properties = properties;
    }

}
