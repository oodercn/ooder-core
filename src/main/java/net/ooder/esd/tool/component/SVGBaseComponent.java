package net.ooder.esd.tool.component;


import net.ooder.esd.annotation.event.SVGEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.svg.SVGProperties;

public class SVGBaseComponent<M extends SVGProperties> extends Component<M, SVGEventEnum> {



    public SVGBaseComponent(String alias, ComponentType typeKey) {
        super(typeKey, alias);
        super.setProperties((M) new SVGProperties());
    }

    public SVGBaseComponent(ComponentType typeKey) {
        super(typeKey);
        super.setProperties((M) new SVGProperties());
    }

    public SVGBaseComponent(String alias, SVGProperties properties, ComponentType typeKey) {
        super(typeKey, alias);
        this.setProperties((M) properties);
    }


}
