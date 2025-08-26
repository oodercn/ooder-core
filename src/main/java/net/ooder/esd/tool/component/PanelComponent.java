package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.event.PanelEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.PanelProperties;

public class PanelComponent<T extends PanelProperties> extends Component<T, PanelEventEnum> {

    public PanelComponent(String alias, T properties) {
        super(ComponentType.PANEL, alias);
        this.setProperties(properties);
    }

    public PanelComponent addAction(Action<PanelEventEnum> action) {
        super.addAction( action);
        return this;
    }

    public PanelComponent(ComponentType typeKey, String alias) {
        super(typeKey, alias);
        this.setProperties((T) new PanelProperties());
    }

    public PanelComponent(ComponentType typeKey, String alias, T properties) {
        super(typeKey, alias);
        this.setProperties(properties);
    }

    public PanelComponent(ComponentType typeKey) {
        super(typeKey);
    }

    public PanelComponent() {
        super(ComponentType.PANEL);
        this.setProperties((T) new PanelProperties());
    }


    public PanelComponent(ComponentType typeKey, String alias, Dock dock) {
        super(typeKey, alias);
        this.setProperties((T) new PanelProperties(dock));

    }

}
