package net.ooder.esd.tool.component;


import net.ooder.esd.annotation.event.PanelEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.form.FormLayoutProperties;

public class FormLayoutComponent extends PanelComponent<FormLayoutProperties> {


    public FormLayoutComponent() {
        super(ComponentType.FORMLAYOUT);
        this.setProperties( new FormLayoutProperties());
    }

    public FormLayoutComponent(Dock dock) {
        super(ComponentType.FORMLAYOUT);
        this.setProperties( new FormLayoutProperties(dock));
    }


    public FormLayoutComponent addAction( Action<PanelEventEnum> action) {
        super.addAction(action);
        return this;
    }

    public FormLayoutComponent(String alias, FormLayoutProperties properties) {
        super(ComponentType.FORMLAYOUT);
        this.alias = alias;
        this.setProperties(properties);
    }

    public FormLayoutComponent(Dock dock, String arias) {
        super(ComponentType.FORMLAYOUT);
        this.alias = arias;
        this.setProperties(new FormLayoutProperties(dock));

    }


}
