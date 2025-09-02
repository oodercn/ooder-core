package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.event.DatePickerEventEnum;
import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.form.ButtonProperties;
import net.ooder.esd.tool.properties.form.DatePickerProperties;

public class DatePickerComponent<T extends DatePickerProperties, K extends DatePickerEventEnum> extends FieldComponent<T, K> {
    public DatePickerProperties properties;

    public DatePickerComponent(String alias) {
        super(ComponentType.DATEPICKER, alias);
        this.setProperties((T) new DatePickerProperties());
    }
    public DatePickerComponent(String alias,T properties) {
        super(ComponentType.DATEPICKER, alias);
        this.setProperties(properties);
    }


    public DatePickerComponent addAction(Action<K> action) {
        super.addAction( action);
        return this;
    }

    public DatePickerComponent() {
        super(ComponentType.DATEPICKER);
        this.setProperties((T) new DatePickerProperties());
    }


}
