package net.ooder.esd.tool.component;


import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.form.RadioBoxProperties;

public class RadioBoxComponent extends FieldComponent<RadioBoxProperties, FieldEventEnum> {

    public RadioBoxComponent(String alias) {
        super(ComponentType.RADIOBOX, alias);
        this.setProperties(new RadioBoxProperties());
    }

    public RadioBoxComponent(String alias, RadioBoxProperties properties) {
        super(ComponentType.RADIOBOX, alias);
        this.setProperties(properties);
    }

    public RadioBoxComponent addAction(Action<FieldEventEnum> action) {
        super.addAction( action);
        return this;
    }

    public RadioBoxComponent() {
        super(ComponentType.RADIOBOX);
        this.setProperties(new RadioBoxProperties());
    }

}
