package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.event.FoldingListEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.list.ListFieldProperties;


public class FoldingListComponent<T extends ListFieldProperties, K extends FoldingListEventEnum> extends Component<T, K> {



    public FoldingListComponent v(Action<K> action) {
        super.addAction(action);
        return this;
    }

    public FoldingListComponent(String alias, T properties) {
        super(ComponentType.LIST, alias);
        this.setProperties(properties);

    }

    public FoldingListComponent() {
        super(ComponentType.LIST);
        this.setProperties((T) new ListFieldProperties());
    }


}
