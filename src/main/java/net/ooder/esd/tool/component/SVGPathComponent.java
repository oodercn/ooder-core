package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.svg.comb.path.PathProperties;

public class SVGPathComponent extends SVGBaseComponent<PathProperties> {


    public SVGPathComponent(String alias, PathProperties properties) {
        super(alias, properties, ComponentType.SVGPATH);
        this.setProperties(properties);
    }

    public SVGPathComponent(String alias) {
        super(alias, ComponentType.SVGPATH);
        super.setProperties(new PathProperties());
    }

    public SVGPathComponent() {
        super(ComponentType.SVGPATH);
        super.setProperties( new PathProperties());
    }
}
