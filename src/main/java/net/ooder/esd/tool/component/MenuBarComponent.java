package net.ooder.esd.tool.component;


import net.ooder.esd.annotation.event.MenuEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.MenuBarProperties;

public class MenuBarComponent extends Component<MenuBarProperties, MenuEventEnum> {


    public MenuBarComponent addAction(Action<MenuEventEnum> action) {
        super.addAction( action);
        return this;
    }


    public MenuBarComponent(String alias, MenuBarProperties properties) {
        super(ComponentType.MENUBAR, alias);
        this.setProperties(  properties);
    }

    public MenuBarComponent() {
        super(ComponentType.MENUBAR);
    }
}
