package net.ooder.esd.tool.component;


import net.ooder.esd.annotation.event.UIEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.FlashProperties;

public class FlashComponent extends FieldComponent<FlashProperties, UIEventEnum> {

    public FlashComponent addAction( Action<UIEventEnum> action) {
        super.addAction( action);
        return this;
    }

    public FlashComponent(String alias, FlashProperties properties) {
        super(ComponentType.FLASH, alias);
        this.setProperties(properties);

    }

    public FlashComponent(String alias) {
        super(ComponentType.FLASH, alias);
        this.setProperties(new FlashProperties());

    }

    public FlashComponent() {
        super(ComponentType.FLASH);
        this.setProperties(new FlashProperties());
    }
}
