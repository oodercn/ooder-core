package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.field.combo.ComboInputFieldBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.form.ComboInputProperties;

public class ComboInputComponent extends FieldComponent<ComboInputProperties, FieldEventEnum> {
    public static final String fieldName = "fieldName";

    public static final String fieldCaption = "fieldCaption";


    public ComboInputComponent(FieldFormConfig<ComboInputFieldBean, ?> field) {
        super(ComponentType.COMBOINPUT, field.getFieldname());
        this.properties = new ComboInputProperties(field);
    }

    public ComboInputComponent(String alias) {
        super(ComponentType.COMBOINPUT, alias);
        this.setProperties(new ComboInputProperties());
    }

    public ComboInputComponent(String alias, ComboInputProperties properties) {
        super(ComponentType.COMBOINPUT, alias);
        this.setProperties(properties);
    }

    public ComboInputComponent addAction(Action<FieldEventEnum> action) {
        super.addAction(action);
        return this;
    }

    public ComboInputComponent() {
        super(ComponentType.COMBOINPUT);
        this.setProperties(new ComboInputProperties());
    }

}
