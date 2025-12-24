package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.event.IconEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.IconProperties;
import org.checkerframework.checker.units.qual.K;

public class IconComponent extends FieldComponent<IconProperties, IconEventEnum> {


    public IconComponent addAction( Action<IconEventEnum> action) {
        super.addAction( action);
        return this;
    }

    public IconComponent(String alias, IconProperties properties) {
        super(ComponentType.ICON, alias);
        this.setProperties(properties);

    }

    public IconComponent(String alias) {
        super(ComponentType.ICON, alias);
        this.setProperties( new IconProperties());

    }

    public IconComponent() {
        super(ComponentType.ICON);
        this.setProperties( new IconProperties());
    }
}
