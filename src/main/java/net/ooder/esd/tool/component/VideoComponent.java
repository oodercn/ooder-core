package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.event.AudioEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.VideoProperties;
import org.checkerframework.checker.units.qual.K;

public class VideoComponent extends FieldComponent<VideoProperties, AudioEventEnum> {


    public VideoComponent addAction( Action<AudioEventEnum> action) {
        super.addAction(action);
        return this;
    }

    public VideoComponent(String alias, VideoProperties properties) {
        super(ComponentType.VIDEO, alias);
        this.setProperties(properties);

    }

    public VideoComponent() {
        super(ComponentType.VIDEO);
        this.setProperties( new VideoProperties());
    }
}
