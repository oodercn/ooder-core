package net.ooder.esd.custom.component.nav;


import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.properties.NavMenuBarProperties;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.MenuBarComponent;

import java.util.Map;

public class NavMenuBarComponent extends MenuBarComponent {
    public NavMenuBarComponent(EUModule module, MethodConfig methodConfig, Map<String, Object> valueMap) {
        super();
        this.setProperties( new NavMenuBarProperties(methodConfig,valueMap));
        this.setAlias(methodConfig.getName());
    }
}
