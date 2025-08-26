package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.event.AudioEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.VideoProperties;

public class VideoComponent<T extends VideoProperties, K extends AudioEventEnum> extends FieldComponent<T, K> {


    public VideoComponent addAction( Action<K> action) {
        super.addAction(action);
        return this;
    }

    public VideoComponent(String alias, T properties) {
        super(ComponentType.VIDEO, alias);
        this.setProperties(properties);

    }

    public VideoComponent() {
        super(ComponentType.VIDEO);
        this.setProperties((T) new VideoProperties());
    }
}
