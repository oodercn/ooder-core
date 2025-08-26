package net.ooder.esd.tool.component;


import net.ooder.esd.annotation.event.NullEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.CSSBoxProperties;

public class CSSBoxComponent extends Component<CSSBoxProperties, NullEventEnum> {




    public CSSBoxComponent addAction(NullEventEnum eventKey, Action<NullEventEnum> action) {
        super.addAction( action);
        return this;
    }
    public CSSBoxComponent(String alias,CSSBoxProperties properties) {
        super(ComponentType.CSSBOX, alias);
        this.properties = properties;
    }

    public CSSBoxComponent() {
        super(ComponentType.CSSBOX);
        this.setProperties( new CSSBoxProperties());
    }
}
