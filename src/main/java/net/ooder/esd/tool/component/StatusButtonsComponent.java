package net.ooder.esd.tool.component;


import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.form.StatusButtonsProperties;

public class StatusButtonsComponent extends FieldComponent<StatusButtonsProperties, FieldEventEnum> {


    public StatusButtonsComponent(String alias, StatusButtonsProperties properties) {
        super(ComponentType.STATUSBUTTONS, alias);
        this.setProperties(properties);

    }

    public StatusButtonsComponent(String alias) {
        super(ComponentType.STATUSBUTTONS, alias);
        this.setProperties( new StatusButtonsProperties());

    }


    public StatusButtonsComponent addAction(Action<FieldEventEnum> action) {
        super.addAction( action);
        return this;
    }

    public StatusButtonsComponent() {
        super(ComponentType.STATUSBUTTONS);
        this.setProperties(new StatusButtonsProperties());
    }


}
