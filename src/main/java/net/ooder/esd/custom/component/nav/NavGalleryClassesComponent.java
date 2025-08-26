package net.ooder.esd.custom.component.nav;


import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.properties.NavGalleryClassesProperties;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.GalleryComponent;

import java.util.Map;

public class NavGalleryClassesComponent extends GalleryComponent {
    public NavGalleryClassesComponent(EUModule module, MethodConfig[] methodConfigs, Map valueMap) {
        super();
        this.setProperties( new NavGalleryClassesProperties(module.getName(), methodConfigs));
        this.setAlias(module.getName() );
    }
}
