package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.svg.ellipse.EllipseProperties;

public class SVGEllipseCombComponent extends SVGBaseComponent<EllipseProperties> {


    public SVGEllipseCombComponent(String alias, EllipseProperties properties) {
        super(alias, properties, ComponentType.SVGELLIPSECOMB);
        this.setProperties( properties);
    }

    public SVGEllipseCombComponent(String alias) {
        super(alias, ComponentType.SVGELLIPSECOMB);
        this.setProperties( new EllipseProperties());
    }

    public SVGEllipseCombComponent() {
        super(ComponentType.SVGELLIPSECOMB);
        super.setProperties( new EllipseProperties());
    }
}
