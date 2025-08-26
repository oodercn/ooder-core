package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.event.BottonEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.form.HTMLButtonProperties;

public class HTMLButtonComponent<T extends HTMLButtonProperties, K extends BottonEventEnum> extends FieldComponent<T, K> {

    public HTMLButtonComponent(String alias) {
        super(ComponentType.BUTTON, alias);
        this.setProperties((T) new HTMLButtonProperties());
    }

    public HTMLButtonComponent(String alias, T properties) {
        super(ComponentType.BUTTON, alias);
        this.setProperties(properties);
    }


    public HTMLButtonComponent addAction(Action<K> action) {
        super.addAction( action);
        return this;
    }

    public HTMLButtonComponent() {
        super(ComponentType.BUTTON);
        this.setProperties((T) new HTMLButtonProperties());
    }



}
