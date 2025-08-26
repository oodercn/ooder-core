package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.event.AnimBinderEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.AnimBinderProperties;

public class AnimBinderComponent extends FieldComponent<AnimBinderProperties,AnimBinderEventEnum> {
    public AnimBinderProperties properties;


    public AnimBinderComponent(String alias,AnimBinderProperties properties) {
        super(ComponentType.ANIMBINDER, alias);
        this.properties = properties;
    }

    public AnimBinderComponent addAction(Action<AnimBinderEventEnum> action) {
        super.addAction(action);
        return this;
    }

    public AnimBinderComponent() {
        super(ComponentType.ANIMBINDER);
    }

    @Override
    public AnimBinderProperties getProperties() {
        return  properties;
    }

    public void setProperties(AnimBinderProperties properties) {
        this.properties = properties;
    }


}
