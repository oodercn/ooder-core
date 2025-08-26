package net.ooder.esd.tool.properties.svg.comb.connector;

import net.ooder.esd.tool.component.SVGBaseComponent;
import net.ooder.esd.tool.component.SVGConnectorComponent;
import net.ooder.esd.tool.properties.svg.comb.BGText;


public class ConnectorNode extends SVGConnectorComponent {
    ConnectorProperties properties;
    String alias;
    String key = "xui.svg.connector";


    public ConnectorNode() {
        this.properties = new ConnectorProperties();
    }

    public ConnectorNode(SVGBaseComponent start, SVGBaseComponent end, String alias, String title) {
        this.setAlias(alias);
        this.properties = new ConnectorProperties(start, end);
        ConnectorAttr attr = properties.getAttr();
        ConnectorKey key = attr.getKEY();
        key.setFill("none");
        key.setStroke("#004A7F");
        key.setTitle(title);
        BGText text = attr.getBG();
        text.setStrokeWidth(4);
        text.setFill("none");

    }

    @Override
    public ConnectorProperties getProperties() {
        return properties;
    }

    public void setProperties(ConnectorProperties properties) {
        this.properties = properties;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


}
