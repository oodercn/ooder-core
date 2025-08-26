package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.DivProperties;

public class DivComponent extends FieldComponent<DivProperties, FieldEventEnum> {


    public DivComponent(ComponentType typeKey, String alias, DivProperties properties) {
        super(typeKey, alias);
        this.setProperties(properties);
    }

    public DivComponent(ComponentType typeKey, String alias, Dock dock) {
        super(typeKey, alias);
        this.setProperties( new DivProperties(dock));

    }

    public DivComponent addAction(Action<FieldEventEnum> action) {
        super.addAction(action);
        return this;
    }

    public DivComponent(ComponentType typeKey) {
        super(typeKey);
        this.setProperties((DivProperties) new DivProperties());
    }


    public DivComponent(String alias, DivProperties properties) {
        super(ComponentType.DIV, alias);
        this.setProperties(properties);
    }

    public DivComponent(Component child, Dock dock) {
        super(ComponentType.DIV, child.getAlias() + "Div");
        this.setProperties(new DivProperties(dock));
        this.addChildren(child);
    }

    public DivComponent(String alias) {
        super(ComponentType.DIV, alias);
        this.setProperties( new DivProperties(Dock.none));
    }

    public DivComponent() {
        super(ComponentType.DIV);
        this.setProperties( new DivProperties(Dock.none));
    }
}
