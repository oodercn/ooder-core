package net.ooder.esd.tool.component;


import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.tool.properties.svg.comb.rect.RectProperties;

public class SVGRectCombComponent extends SVGBaseComponent<RectProperties> {

    public SVGRectCombComponent(String alias) {
        super(alias, ComponentType.SVGRECTCOMB);
        super.setProperties( new RectProperties());
        ;
    }

    public SVGRectCombComponent(String alias,RectProperties properties) {
        super(alias, properties, ComponentType.SVGRECTCOMB);
        this.setProperties(properties);
    }

    public SVGRectCombComponent() {
        super(ComponentType.SVGRECTCOMB);
        super.setProperties( new RectProperties());
        ;
    }

}
