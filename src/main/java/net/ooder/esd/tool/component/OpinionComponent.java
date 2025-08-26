package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.event.GalleryEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.custom.properties.OpinionProperties;
import net.ooder.esd.tool.component.Component;

public class OpinionComponent extends Component<OpinionProperties, GalleryEventEnum> {

    public OpinionProperties properties;

    @Override
    public OpinionProperties getProperties() {
        return properties;
    }

    public void setProperties(OpinionProperties properties) {
        this.properties = properties;
    }


    public OpinionComponent(String alias) {
        super(ComponentType.OPINION, alias);
        this.setProperties( new OpinionProperties());

    }


    public OpinionComponent(String alias, OpinionProperties properties) {
        super(ComponentType.OPINION, alias);
        this.properties = properties;

    }

    public OpinionComponent() {
        super(ComponentType.OPINION);
    }


}
