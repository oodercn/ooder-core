package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.event.ListEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.list.ListFieldProperties;


public class FoldingListComponent extends FieldComponent<ListFieldProperties, ListEventEnum> {

    public FoldingListComponent addAction(Action<ListEventEnum> action) {
        super.addAction(action);
        return this;
    }

    public FoldingListComponent(String alias, ListFieldProperties properties) {
        super(ComponentType.FOLDINGLIST, alias);
        this.setProperties(properties);

    }

    public FoldingListComponent() {
        super(ComponentType.FOLDINGLIST);
        this.setProperties(new ListFieldProperties());
    }


}
