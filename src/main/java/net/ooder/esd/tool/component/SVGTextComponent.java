package net.ooder.esd.tool.component;


import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.svg.text.SVGTextProperties;

public class SVGTextComponent extends SVGBaseComponent<SVGTextProperties> {


    public SVGTextComponent(String alias, SVGTextProperties properties) {
        super(alias, properties, ComponentType.SVGTEXT);
        this.setProperties(properties);
    }

    public SVGTextComponent(String alias) {
        super(alias, ComponentType.SVGTEXT);
        super.setProperties(new SVGTextProperties());

    }

    public SVGTextComponent() {
        super(ComponentType.SVGTEXT);
        super.setProperties(new SVGTextProperties());

    }
}
