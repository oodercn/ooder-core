package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.event.ListEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.list.ListFieldProperties;

public class ListComponent extends FieldComponent<ListFieldProperties,ListEventEnum> {

    public ListComponent addAction(Action<ListEventEnum> action) {
        super.addAction( action);
        return this;
    }

    public ListComponent(String alias, ListFieldProperties properties) {
        super(ComponentType.LIST, alias);
        this.setProperties(properties);

    }

    public ListComponent(String alias) {
        super(ComponentType.LIST, alias);
        this.setProperties( new ListFieldProperties());
    }

    public ListComponent() {
        super(ComponentType.LIST);
        this.setProperties( new ListFieldProperties());
    }


}
