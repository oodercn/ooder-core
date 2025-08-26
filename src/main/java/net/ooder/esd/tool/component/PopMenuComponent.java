package net.ooder.esd.tool.component;


import net.ooder.esd.annotation.event.PopMenuEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.PopMenuProperties;

public class PopMenuComponent<T extends PopMenuProperties, K extends PopMenuEventEnum> extends Component<T, K> {


    public PopMenuComponent( String alias,T properties) {
        super(ComponentType.POPMENU, alias);
        this.setProperties( properties);
    }


    public PopMenuComponent addAction(Action action) {
        super.addAction(action);
        return this;
    }
    public PopMenuComponent() {
        super(ComponentType.POPMENU);
    }
}
