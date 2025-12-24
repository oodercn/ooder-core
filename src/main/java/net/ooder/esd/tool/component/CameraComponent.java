package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.event.CameraEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.CameraProperties;
import org.checkerframework.checker.units.qual.K;

public class CameraComponent extends FieldComponent<CameraProperties, CameraEventEnum> {


    public CameraComponent addAction(Action<CameraEventEnum> action) {
        super.addAction( action);
        return this;
    }

    public CameraComponent(String alias, CameraProperties properties) {
        super(ComponentType.CAMERA, alias);
        this.setProperties(properties);

    }

    public CameraComponent() {
        super(ComponentType.CAMERA);
        this.setProperties( new CameraProperties());
    }
}
