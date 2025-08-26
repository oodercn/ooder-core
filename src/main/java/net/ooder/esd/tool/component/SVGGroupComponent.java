package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.svg.SVGProperties;

public class SVGGroupComponent extends SVGBaseComponent<SVGProperties> {


    public SVGGroupComponent(String alias, SVGProperties properties) {
        super(alias, properties, ComponentType.SVGGROUP);
        this.setProperties(properties);
    }


    public SVGGroupComponent(String alias) {
        super(alias, ComponentType.SVGGROUP);
        super.setProperties(new SVGProperties());
    }

    public SVGGroupComponent() {
        super(ComponentType.SVGGROUP);
        super.setProperties(new SVGProperties());
    }
}
