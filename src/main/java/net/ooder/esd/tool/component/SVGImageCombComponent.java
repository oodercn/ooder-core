package net.ooder.esd.tool.component;


import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.svg.SVGProperties;

public class SVGImageCombComponent extends SVGBaseComponent<SVGProperties> {


    public SVGImageCombComponent(String alias, SVGProperties properties) {
        super(alias, properties, ComponentType.SVGIMAGECOMB);
        this.setProperties(properties);
    }

    public SVGImageCombComponent(String alias) {
        super(alias, ComponentType.SVGIMAGECOMB);
        super.setProperties( new SVGProperties());
    }

    public SVGImageCombComponent() {
        super(ComponentType.SVGIMAGECOMB);
        super.setProperties( new SVGProperties());
    }
}
