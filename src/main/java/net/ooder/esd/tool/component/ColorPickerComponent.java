package net.ooder.esd.tool.component;


import net.ooder.esd.annotation.event.UIEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.form.ColorPickerProperties;

public class ColorPickerComponent extends FieldComponent<ColorPickerProperties, UIEventEnum> {

    public ColorPickerComponent(String alias) {
        super(ComponentType.COLORPICKER, alias);
        this.setProperties(new ColorPickerProperties());
    }

    public ColorPickerComponent(String alias,ColorPickerProperties properties) {
        super(ComponentType.COLORPICKER, alias);
        this.setProperties(properties);
    }


    public ColorPickerComponent addAction(Action<UIEventEnum> action) {
        super.addAction( action);
        return this;
    }

    public ColorPickerComponent() {
        super(ComponentType.COLORPICKER);
        this.setProperties( new ColorPickerProperties());
    }

}
