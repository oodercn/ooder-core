package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.svg.comb.circle.CircleProperties;

public class SVGCircleCombComponent extends SVGBaseComponent<CircleProperties> {


    public SVGCircleCombComponent(String alias) {
        super(alias, ComponentType.SVGCIRCLECOMB);
        this.setProperties(new CircleProperties());
    }


    public SVGCircleCombComponent(String alias, CircleProperties properties) {
        super(alias, properties, ComponentType.SVGCIRCLECOMB);
        this.setProperties(properties);
    }

    public SVGCircleCombComponent() {
        super(ComponentType.SVGCIRCLECOMB);
        this.setProperties( new CircleProperties());
    }
}
