package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.event.BottonEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.form.HTMLButtonProperties;

public class HTMLButtonComponent extends FieldComponent<HTMLButtonProperties, BottonEventEnum> {

    public HTMLButtonComponent(String alias) {
        super(ComponentType.BUTTON, alias);
        this.setProperties(new HTMLButtonProperties());
    }

    public HTMLButtonComponent(String alias, HTMLButtonProperties properties) {
        super(ComponentType.BUTTON, alias);
        this.setProperties(properties);
    }


    public HTMLButtonComponent addAction(Action<BottonEventEnum> action) {
        super.addAction(action);
        return this;
    }

    public HTMLButtonComponent() {
        super(ComponentType.BUTTON);
        this.setProperties(new HTMLButtonProperties());
    }


}
