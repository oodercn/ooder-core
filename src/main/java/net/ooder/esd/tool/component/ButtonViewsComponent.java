package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.event.TabsEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.ButtonViewsProperties;

public class ButtonViewsComponent extends TabsComponent<ButtonViewsProperties> {

    public ButtonViewsComponent addAction(Action<TabsEventEnum> action) {
        super.addAction(action);
        return this;
    }

    public ButtonViewsComponent(String alias, ButtonViewsProperties properties) {
        super(ComponentType.BUTTONVIEWS, alias);
        this.setProperties(properties);
    }


    public ButtonViewsComponent(ComponentType type) {
        super(type);
    }

    public ButtonViewsComponent() {
        super(ComponentType.BUTTONVIEWS);
    }


}