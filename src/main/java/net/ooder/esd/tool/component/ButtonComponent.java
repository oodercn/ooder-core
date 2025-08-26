package net.ooder.esd.tool.component;


import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.form.ButtonProperties;

public class ButtonComponent extends FieldComponent<ButtonProperties, FieldEventEnum> {


    public ButtonComponent(String alias) {
        super(ComponentType.BUTTON, alias);
        this.setProperties( new ButtonProperties());
    }

    public ButtonComponent() {
        super(ComponentType.BUTTON);
        this.setProperties( new ButtonProperties());

    }

    public ButtonComponent(String alias, ButtonProperties properties) {
        super(ComponentType.BUTTON, alias);
        this.setProperties(properties);

    }

    public ButtonComponent(ButtonProperties properties) {
        super(ComponentType.BUTTON);
        this.setProperties(properties);

    }

    public ButtonComponent addAction(Action<FieldEventEnum> action) {
        super.addAction( action);
        return this;
    }
}
