package net.ooder.esd.tool.component;


import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.form.CheckBoxProperties;

public class CheckBoxComponent extends FieldComponent<CheckBoxProperties, FieldEventEnum> {

    public CheckBoxComponent(String alias) {
        super(ComponentType.CHECKBOX, alias);
        this.setProperties(new CheckBoxProperties());
    }

    public CheckBoxComponent(String alias, CheckBoxProperties properties) {
        super(ComponentType.CHECKBOX, alias);
        this.setProperties(properties);
    }


    public CheckBoxComponent addAction(Action<FieldEventEnum> action) {
        super.addAction( action);
        return this;
    }


    public CheckBoxComponent() {
        super(ComponentType.CHECKBOX);
        this.setProperties(new CheckBoxProperties());
    }


}
