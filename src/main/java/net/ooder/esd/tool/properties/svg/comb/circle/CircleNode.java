package net.ooder.esd.tool.properties.svg.comb.circle;

import net.ooder.esd.tool.component.SVGCircleCombComponent;

public class CircleNode extends SVGCircleCombComponent {

    CircleProperties properties = new CircleProperties();


    public CircleNode() {

    }


    public CircleNode(String alias) {
        super(alias);
        this.alias = alias;
    }

    public CircleNode(String alias, CircleProperties properties) {
        super(alias, properties);
        this.setProperties(properties);
    }

    @Override
    public CircleProperties getProperties() {
        return  properties;
    }

    public void setProperties(CircleProperties properties) {
        this.properties = properties;
    }
}
