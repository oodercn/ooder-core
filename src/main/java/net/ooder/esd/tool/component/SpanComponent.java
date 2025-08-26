package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.event.SpanEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.SpanProperties;

public class SpanComponent extends FieldComponent<SpanProperties,SpanEventEnum> {

    public SpanComponent addAction(Action<SpanEventEnum> action) {
        super.addAction( action);
        return this;
    }

    public SpanComponent(String alias, SpanProperties properties) {
        super(ComponentType.SPAN, alias);
        this.setProperties(properties);

    }
    public SpanComponent(String alias) {
        super(ComponentType.SPAN, alias);
        this.setProperties( new SpanProperties());
    }
    public SpanComponent() {
        super(ComponentType.SPAN);
        this.setProperties( new SpanProperties());
    }
}
