package net.ooder.esd.tool.component;


import net.ooder.esd.annotation.event.ResizerEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.ResizerProperties;

public class ResizerComponent<T extends ResizerProperties, K extends ResizerEventEnum> extends Component<T, K> {

    public ResizerComponent addAction(Action action) {
        super.addAction( action);
        return this;
    }

    public ResizerComponent( String alias,T properties) {
        super(ComponentType.RESIZER, alias);
       this.setProperties(properties);
    }

    public ResizerComponent() {
        super(ComponentType.RESIZER);
        this.setProperties((T) new ResizerProperties());
    }
}
