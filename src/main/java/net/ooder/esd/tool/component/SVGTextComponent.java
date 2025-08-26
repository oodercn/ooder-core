package net.ooder.esd.tool.component;


import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.svg.SVGProperties;

public class SVGTextComponent extends SVGBaseComponent<SVGProperties> {


    public SVGTextComponent(String alias, SVGProperties properties) {
        super(alias, properties, ComponentType.SVGTEXT);
        this.setProperties(properties);
    }

    public SVGTextComponent(String alias) {
        super(alias, ComponentType.SVGTEXT);
        super.setProperties( new SVGProperties());

    }

    public SVGTextComponent() {
        super(ComponentType.SVGTEXT);
        super.setProperties(new SVGProperties());

    }
}
