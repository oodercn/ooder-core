package net.ooder.esd.tool.component;


import net.ooder.esd.annotation.event.TabsEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.custom.properties.StacksProperties;
import net.ooder.esd.tool.properties.Action;

public class StacksComponent extends TabsComponent<StacksProperties> {

    public StacksComponent addAction(Action<TabsEventEnum> action) {
        super.addAction( action);
        return this;
    }

    public StacksComponent() {
        super(ComponentType.STACKS);
        this.setProperties(new StacksProperties());
    }
}
