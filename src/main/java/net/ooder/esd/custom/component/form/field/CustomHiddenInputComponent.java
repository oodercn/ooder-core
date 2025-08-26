package net.ooder.esd.custom.component.form.field;

import net.ooder.esd.custom.ESDField;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.HiddenInputComponent;

import java.util.Map;

public class CustomHiddenInputComponent extends HiddenInputComponent {


    public CustomHiddenInputComponent(EUModule euModule, ESDField field, String target, Object value) {
        super(field.getName());
        this.getProperties().setId(field.getId());
        this.getProperties().setName(field.getName());
        this.getProperties().setDesc(field.getCaption());
        this.getProperties().setValue(value);
        this.setTarget(target);

    }

    public CustomHiddenInputComponent(EUModule euModule, FieldFormConfig field, String target, Object value,Map valueMap) {
        super(field.getFieldname());
        this.getProperties().setId(field.getId());
        this.getProperties().setName(field.getFieldname());
        this.getProperties().setDesc(field.getAggConfig().getCaption());
        this.getProperties().setValue(value);
        this.setTarget(target);

    }
}
