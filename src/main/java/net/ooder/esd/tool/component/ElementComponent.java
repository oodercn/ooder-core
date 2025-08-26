package net.ooder.esd.tool.component;


import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.ElementProperties;

public class ElementComponent extends FieldComponent<ElementProperties, FieldEventEnum> {

    public ElementComponent addAction( Action<FieldEventEnum> action) {
        super.addAction( action);
        return this;
    }
    public ElementComponent( String alias) {
        super(ComponentType.ELEMENT, alias);
        this.setProperties(new ElementProperties());
    }

    public ElementComponent( String alias,ElementProperties properties) {
        super(ComponentType.ELEMENT, alias);
        this.setProperties(properties);

    }

    public ElementComponent() {
        super(ComponentType.ELEMENT);
        this.setProperties( new ElementProperties());
    }
}
