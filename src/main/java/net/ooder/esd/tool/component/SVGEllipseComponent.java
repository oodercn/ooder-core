package net.ooder.esd.tool.component;


import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.svg.ellipse.EllipseProperties;

public class SVGEllipseComponent<M extends EllipseProperties> extends SVGBaseComponent<M> {


    public SVGEllipseComponent(String alias, M properties) {
        super(alias, properties, ComponentType.SVGELLIPSE);
        this.setProperties(properties);
    }

    public SVGEllipseComponent(String alias) {
        super(alias, ComponentType.SVGELLIPSE);
        super.setProperties((M) new EllipseProperties());
    }

    public SVGEllipseComponent() {
        super(ComponentType.SVGELLIPSE);
        super.setProperties((M) new EllipseProperties());
    }
}
