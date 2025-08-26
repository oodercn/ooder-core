package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.svg.comb.rect.RectProperties;

public class SVGRectComponent extends SVGBaseComponent<RectProperties> {


    public SVGRectComponent(String alias, RectProperties properties) {
        super(alias, properties, ComponentType.SVGRECT);
        super.setProperties(properties);
    }

    public SVGRectComponent(String alias) {
        super(alias, ComponentType.SVGRECT);
        super.setProperties( new RectProperties());
    }

    public SVGRectComponent() {
        super(ComponentType.SVGRECT);
        super.setProperties(new RectProperties());
    }
}
