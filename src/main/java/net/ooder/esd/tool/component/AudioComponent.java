package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.AudioProperties;

public class AudioComponent extends FieldComponent<AudioProperties, FieldEventEnum> {

    public AudioComponent addAction( Action<FieldEventEnum> action) {
        super.addAction( action);
        return this;
    }

    public AudioComponent( String alias,AudioProperties properties) {
        super(ComponentType.AUDIO, alias);
        this.setProperties(properties);

    }
    public AudioComponent(String alias) {
        super(ComponentType.AUDIO, alias);
        this.setProperties( new AudioProperties());
    }
    public AudioComponent() {
        super(ComponentType.AUDIO);
        this.setProperties( new AudioProperties());
    }
}
