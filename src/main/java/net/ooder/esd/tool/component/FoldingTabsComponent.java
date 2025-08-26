package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.event.TabsEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.custom.properties.NavFoldingTabsProperties;
import net.ooder.esd.tool.properties.Action;

public class FoldingTabsComponent extends TabsComponent<NavFoldingTabsProperties> {

    public void FoldingTabsComponent(String alias, NavFoldingTabsProperties properties) {
        this.alias = alias;
        this.setProperties( properties);
    }

    public FoldingTabsComponent addAction(Action<TabsEventEnum> action) {
        super.addAction( action);
        return this;
    }

    public FoldingTabsComponent() {
        super(ComponentType.FOLDINGTABS);
        this.setProperties(new NavFoldingTabsProperties());
    }
}
