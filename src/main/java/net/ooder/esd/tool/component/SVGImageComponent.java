package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.svg.SVGProperties;

public class SVGImageComponent extends SVGBaseComponent<SVGProperties> {


    public SVGImageComponent(String alias, SVGProperties properties) {
        super(alias, properties, ComponentType.SVGIMAGE);
        this.setProperties(properties);
    }

    public SVGImageComponent(String alias) {
        super(alias,ComponentType.SVGIMAGE);
        super.setProperties( new SVGProperties());
    }

    public SVGImageComponent() {
        super(ComponentType.SVGIMAGE);
        super.setProperties(new SVGProperties());
    }
}
