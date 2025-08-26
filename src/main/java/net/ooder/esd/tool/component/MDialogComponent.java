package net.ooder.esd.tool.component;


import net.ooder.esd.annotation.event.DialogEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.DialogProperties;

public class MDialogComponent<T extends DialogProperties, K extends DialogEventEnum> extends DialogComponent<T, K> {

    public MDialogComponent() {
        super(ComponentType.MDIALOG);
        this.setProperties((T) new DialogProperties());
    }

    public MDialogComponent addAction(Action<K> action) {
        super.addAction( action);
        return this;
    }

    public MDialogComponent(String alias, T properties) {
        super(ComponentType.MDIALOG);
        this.setAlias(alias);
        this.setProperties(properties);


    }


}
;
