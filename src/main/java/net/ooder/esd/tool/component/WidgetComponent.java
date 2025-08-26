package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.event.UIEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.WidgetProperties;

public class WidgetComponent<T extends WidgetProperties, K extends UIEventEnum> extends Component<T, K> {


    public WidgetComponent(ComponentType typeKey, String alias, T properties) {
        super(typeKey, alias);
        this.setProperties(properties);
    }

    public WidgetComponent(ComponentType typeKey, String alias, Dock dock) {
        super(typeKey, alias);
        this.setProperties( (T) new WidgetProperties(dock));

    }

    public WidgetComponent addAction(Action<K> action) {
        super.addAction( action);
        return this;
    }
    public WidgetComponent(ComponentType typeKey) {
        super(typeKey);
        this.setProperties( (T) new WidgetProperties());
    }


    public WidgetComponent(String alias, T properties) {
        super(ComponentType.WIDGET, alias);
        this.setProperties(properties);
    }

    public WidgetComponent(Component child, Dock dock) {
        super(ComponentType.WIDGET, child.getAlias() + "Div");
        this.setProperties( (T) new WidgetProperties(dock));
        this.addChildren(child);
    }

    public WidgetComponent() {
        super(ComponentType.WIDGET);
    }
}
