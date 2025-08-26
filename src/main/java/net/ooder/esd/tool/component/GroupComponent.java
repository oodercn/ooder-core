package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.event.PanelEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.tool.properties.GroupProperties;

public class GroupComponent extends Component<GroupProperties, PanelEventEnum> {


    public GroupComponent(String alias, GroupProperties properties) {
        super(ComponentType.GROUP, alias);
        this.setProperties(properties);

    }

    public GroupComponent(Dock dock, String alias) {
        super(ComponentType.GROUP, alias);
        this.setProperties(new GroupProperties(dock));
    }

    public GroupComponent(Component child, Dock dock) {
        super(ComponentType.GROUP, child.getAlias() + ComponentType.GROUP.name());
        this.setProperties(new GroupProperties(dock));
        this.addChildren(child);
    }

    public GroupComponent() {
        super(ComponentType.GROUP);
        this.setProperties(new GroupProperties());
    }
}
