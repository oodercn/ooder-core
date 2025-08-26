package net.ooder.esd.tool.properties;

import net.ooder.esd.annotation.event.PageEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.PageBarComponent;

public class PageBarLayoutComponent<T extends BlockProperties, K extends PageBarProperties> extends Component<T, PageEventEnum> {

    public PageBarLayoutComponent(T properties, K pageproperties, String alias) {
        super(ComponentType.DIV, alias + "div");
        this.addChildren(new PageBarComponent(alias, pageproperties));
    }

}
