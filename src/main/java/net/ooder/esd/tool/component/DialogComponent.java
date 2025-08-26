package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.event.DialogEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.DialogProperties;

public class DialogComponent<T extends DialogProperties, K extends DialogEventEnum> extends Component<T, K> {

    public DialogComponent() {
        super(ComponentType.DIALOG);
        this.setProperties((T) new DialogProperties());
    }
    public DialogComponent(ComponentType componentType) {
        super(componentType);
        this.setProperties((T) new DialogProperties());
    }

    public DialogComponent addAction(Action<K> action) {
        super.addAction( action);
        return this;
    }

    public DialogComponent(String alias, T properties) {
        super(ComponentType.DIALOG, alias);
        this.setProperties(properties);

    }


}
;
