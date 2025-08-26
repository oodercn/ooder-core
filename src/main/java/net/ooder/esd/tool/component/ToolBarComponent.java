package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.CustomFieldAnnClass;
import net.ooder.esd.annotation.event.ToolBarEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.ToolBarMenuBean;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.ToolBarProperties;

public class ToolBarComponent extends FieldComponent<ToolBarProperties, ToolBarEventEnum> {


    public ToolBarComponent(String alias, ToolBarProperties properties) {
        super(ComponentType.TOOLBAR, alias);
        this.setProperties(properties);
    }

    public ToolBarComponent(String alias) {
        super(ComponentType.TOOLBAR, alias);
    }


    public ToolBarComponent addAction(Action action) {
        super.addAction(action);
        return this;
    }


    public ToolBarComponent() {
        super(ComponentType.TOOLBAR);
        this.setProperties(new ToolBarProperties("toolbar"));
    }
}
