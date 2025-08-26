package net.ooder.esd.tool.component;


import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.svg.comb.connector.ConnectorProperties;

public class SVGConnectorComponent extends SVGBaseComponent<ConnectorProperties> {


    public SVGConnectorComponent(SVGBaseComponent start, SVGBaseComponent end) {
        super(ComponentType.SVGCONNECTOR);
        super.setProperties( new ConnectorProperties(start, end));
    }

    public SVGConnectorComponent(String alias) {
        super(alias, ComponentType.SVGCONNECTOR);
        super.setProperties(new ConnectorProperties());
    }

    public SVGConnectorComponent(String alias, ConnectorProperties properties) {
        super(alias, properties, ComponentType.SVGCONNECTOR);
        super.setProperties(properties);
    }

    public SVGConnectorComponent() {
        super(ComponentType.SVGCONNECTOR);
        super.setProperties( new ConnectorProperties());
    }
}
