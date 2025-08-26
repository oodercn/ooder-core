package net.ooder.esd.tool.component;

import net.ooder.common.JDSConstants;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.form.InputProperties;

public class InputComponent extends FieldComponent<InputProperties, FieldEventEnum> {
    public static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, InputComponent.class);

    public InputComponent(String alias) {
        super(ComponentType.INPUT, alias);
        this.setProperties( new InputProperties());
    }

    public InputComponent(String alias, InputProperties properties) {
        super(ComponentType.INPUT, alias);
        this.setProperties(properties);
    }

    public InputComponent addAction(Action<FieldEventEnum> action) {
        super.addAction( action);
        return this;
    }

    public InputComponent() {
        super(ComponentType.INPUT);
        this.setProperties(new InputProperties());
    }


}
