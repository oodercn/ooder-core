package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.event.IconEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.IconProperties;

public class IconComponent<T extends IconProperties, K extends IconEventEnum> extends FieldComponent<T, K> {


    public IconComponent addAction( Action<K> action) {
        super.addAction( action);
        return this;
    }

    public IconComponent(String alias, T properties) {
        super(ComponentType.ICON, alias);
        this.setProperties(properties);

    }

    public IconComponent(String alias) {
        super(ComponentType.ICON, alias);
        this.setProperties((T) new IconProperties());

    }

    public IconComponent() {
        super(ComponentType.ICON);
        this.setProperties((T) new IconProperties());
    }
}
