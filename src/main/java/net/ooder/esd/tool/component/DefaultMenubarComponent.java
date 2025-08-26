package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.ui.UIButtonType;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.tool.properties.MenuBarProperties;

public class DefaultMenubarComponent extends MenuBarComponent {
    public DefaultMenubarComponent(String alias) {
        super(alias,  new MenuBarProperties());
        MenuBarProperties properties = this.getProperties();
        properties.addItem(new TreeListItem(UIButtonType.add));
        properties.addItem(new TreeListItem(UIButtonType.delete));
        properties.addItem(new TreeListItem(UIButtonType.edit));

    }

}
