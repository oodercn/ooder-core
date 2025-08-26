package net.ooder.esd.tool.component;


import net.ooder.esd.annotation.event.UIEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.AbsUIProperties;
import net.ooder.esd.tool.properties.Action;

public class UIComponent<T extends AbsUIProperties, K extends UIEventEnum> extends Component<T, K> {

    public UIComponent addAction(Action action) {
        super.addAction( action);
        return this;
    }

    public UIComponent(String alias, T properties) {
        super(ComponentType.UI, alias);
       this.setProperties(properties);
    }

    public UIComponent() {
        super(ComponentType.UI);
        this.setProperties((T) new AbsUIProperties());
    }
}
