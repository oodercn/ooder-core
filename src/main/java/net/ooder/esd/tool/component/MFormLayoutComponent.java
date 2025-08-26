package net.ooder.esd.tool.component;


import net.ooder.esd.annotation.event.PanelEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.form.FormLayoutProperties;

public class MFormLayoutComponent extends PanelComponent<FormLayoutProperties> {


    public MFormLayoutComponent() {
        super(ComponentType.MFORMLAYOUT);
        this.setProperties( new FormLayoutProperties());
    }

    public MFormLayoutComponent(Dock dock) {
        super(ComponentType.MFORMLAYOUT);
        this.setProperties(new FormLayoutProperties(dock));
    }


    public MFormLayoutComponent addAction(Action<PanelEventEnum> action) {
        super.addAction( action);
        return this;
    }

    public MFormLayoutComponent(String alias, FormLayoutProperties properties) {
        super(ComponentType.MFORMLAYOUT);
        this.alias = alias;
        this.setProperties(properties);
    }

    public MFormLayoutComponent(Dock dock, String arias) {
        super(ComponentType.MFORMLAYOUT);
        this.alias = arias;
        this.setProperties(new FormLayoutProperties(dock));

    }


}
