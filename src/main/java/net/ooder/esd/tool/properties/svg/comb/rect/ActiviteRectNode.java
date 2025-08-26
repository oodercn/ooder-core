package net.ooder.esd.tool.properties.svg.comb.rect;

import net.ooder.esd.tool.component.SVGRectCombComponent;

public class ActiviteRectNode extends SVGRectCombComponent {
    ActivityRectProperties properties = new ActivityRectProperties();

    @Override
    public ActivityRectProperties getProperties() {
        return  properties;
    }

    public void setProperties(ActivityRectProperties properties) {
        this.properties = properties;
    }

    public ActiviteRectNode(String alias) {
        super(alias);
        this.setProperties(new ActivityRectProperties());

    }

    public ActiviteRectNode(String alias, ActivityRectProperties properties) {
        super(alias);
        this.setProperties(properties);
    }

    public ActiviteRectNode() {
        super();
        this.setProperties(new ActivityRectProperties());
    }


}
