package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.event.UIEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.form.TimePickerProperties;

public class TimePickerComponent<T extends TimePickerProperties, K extends UIEventEnum> extends Component<T, K> {


    public TimePickerComponent(String alias) {
        super(ComponentType.TIMEPICKER, alias);
        this.setProperties((T) new TimePickerProperties());
    }

    public TimePickerComponent(String alias,T properties) {
        super(ComponentType.TIMEPICKER, alias);
        this.setProperties(properties);
    }



    public TimePickerComponent addAction(Action action) {
        super.addAction( action);
        return this;
    }


    public TimePickerComponent() {
        super(ComponentType.TIMEPICKER);
        this.setProperties((T) new TimePickerProperties());
    }


}
