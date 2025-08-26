package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.event.CameraEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.CameraProperties;

public class CameraComponent<T extends CameraProperties, K extends CameraEventEnum> extends FieldComponent<T, K> {


    public CameraComponent addAction(Action<K> action) {
        super.addAction( action);
        return this;
    }

    public CameraComponent(String alias, T properties) {
        super(ComponentType.CAMERA, alias);
        this.setProperties(properties);

    }

    public CameraComponent() {
        super(ComponentType.CAMERA);
        this.setProperties((T) new CameraProperties());
    }
}
