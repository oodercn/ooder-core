package net.ooder.esd.custom.component.nav;


import net.ooder.esd.custom.properties.IndexGalleryProperties;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.GalleryComponent;

public class IndxeGalleryComponent extends GalleryComponent {
    public IndxeGalleryComponent(EUModule module, EUModule[] modules) {
        super();
        this.setProperties(new IndexGalleryProperties(module.getName(), modules));
        this.setAlias(module.getName());
    }
}
