package net.ooder.esd.tool.component;


import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.form.ProgressBarProperties;

public class ProgressBarComponent extends FieldComponent<ProgressBarProperties, FieldEventEnum> {

    public ProgressBarComponent(String alias) {
        super(ComponentType.PROGRESSBAR, alias);
        this.setProperties( new ProgressBarProperties());
    }

    public ProgressBarComponent(String alias, ProgressBarProperties properties) {
        super(ComponentType.PROGRESSBAR, alias);
        this.setProperties(properties);
    }

    public ProgressBarComponent addAction(Action<FieldEventEnum> action) {
        super.addAction( action);
        return this;
    }

    public ProgressBarComponent() {
        super(ComponentType.PROGRESSBAR);
        this.setProperties(new ProgressBarProperties());
    }

}
