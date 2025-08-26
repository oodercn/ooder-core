package net.ooder.esd.tool.component;


import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.svg.comb.circle.CircleProperties;

public class SVGCircleComponent extends SVGBaseComponent<CircleProperties> {

    public SVGCircleComponent(String alias) {
        super(alias, ComponentType.SVGCIRCLE);
        super.setProperties( new CircleProperties());
    }

    public SVGCircleComponent(String alias, CircleProperties properties) {
        super(alias, properties, ComponentType.SVGCIRCLE);
        this.setProperties(properties);
    }

    public SVGCircleComponent() {
        super(ComponentType.SVGCIRCLE);
        super.setProperties(new CircleProperties());

    }
}
