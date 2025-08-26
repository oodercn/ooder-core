package net.ooder.esd.tool.component;


import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.form.LabelProperties;

public class LabelComponent extends FieldComponent<LabelProperties, FieldEventEnum> {


    public LabelComponent addAction(Action<FieldEventEnum> action) {
        super.addAction( action);
        return this;
    }

    public LabelComponent(String alias) {
        super(ComponentType.LABEL, alias);
        this.setProperties( new LabelProperties());
    }



    public LabelComponent(String alias, LabelProperties properties) {
        super(ComponentType.LABEL, alias);
        this.setProperties(properties);
    }

    public LabelComponent() {
        super(ComponentType.LABEL);
        this.setProperties( new LabelProperties());
    }
}
