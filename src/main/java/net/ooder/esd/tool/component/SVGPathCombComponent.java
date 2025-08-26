package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.svg.comb.path.PathProperties;

public class SVGPathCombComponent extends SVGBaseComponent<PathProperties> {

    public SVGPathCombComponent(String alias, PathProperties properties) {
        super(alias, properties, ComponentType.SVGPATHCOMB);
        this.setProperties(properties);
    }
    public SVGPathCombComponent(String alias) {
        super(alias,ComponentType.SVGPATHCOMB);
        super.setProperties(new PathProperties());

    }
    public SVGPathCombComponent() {
        super(ComponentType.SVGPATHCOMB);
        super.setProperties(new PathProperties());

    }
}
