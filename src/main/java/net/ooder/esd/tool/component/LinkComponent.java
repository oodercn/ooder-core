package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.event.NullEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.LinkProperties;

public class LinkComponent<T extends LinkProperties, K extends NullEventEnum> extends Component<T, K> {


    public LinkComponent addAction( Action<K> action) {
        super.addAction(action);
        return this;
    }

    public LinkComponent( String alias,T properties) {
        super(ComponentType.LINK, alias);
        this.setProperties(properties);

    }

    public LinkComponent() {
        super(ComponentType.LINK);
        this.setProperties((T) new LinkProperties());
    }
}
