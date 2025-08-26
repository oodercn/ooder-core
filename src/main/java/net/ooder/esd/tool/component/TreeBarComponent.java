package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.event.TreeViewEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.TreeViewProperties;

public class TreeBarComponent extends Component<TreeViewProperties<TreeListItem>, TreeViewEventEnum> {


    public TreeBarComponent addAction(Action<TreeViewEventEnum> action) {
        super.addAction( action);
        return this;
    }


    public TreeBarComponent(String alias, TreeViewProperties<TreeListItem> properties) {
        super(ComponentType.LAYOUT, alias);
        this.setProperties(properties);

    }


    public TreeBarComponent() {
        super(ComponentType.LAYOUT);
        this.setProperties( new TreeViewProperties());
    }

}
