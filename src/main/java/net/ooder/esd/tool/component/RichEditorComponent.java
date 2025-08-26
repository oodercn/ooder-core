package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.form.RichEditorProperties;

public class RichEditorComponent extends FieldComponent<RichEditorProperties, FieldEventEnum> {

    public RichEditorComponent(String alias) {
        super(ComponentType.RICHEDITOR, alias);
        this.setProperties( new RichEditorProperties());
    }

    public RichEditorComponent(String alias, RichEditorProperties properties) {
        super(ComponentType.RICHEDITOR, alias);
        this.setProperties(properties);
    }

    public RichEditorComponent addAction(Action<FieldEventEnum> action) {
        super.addAction( action);
        return this;
    }

    public RichEditorComponent() {
        super(ComponentType.RICHEDITOR);
        this.setProperties(new RichEditorProperties());
    }

}
