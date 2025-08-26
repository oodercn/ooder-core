package net.ooder.esd.custom.component.form.field;

import net.ooder.esd.annotation.event.DatePickerEventEnum;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.DatePickerComponent;
import net.ooder.esd.tool.properties.form.DatePickerProperties;

import java.util.Map;

public class CustomDatePickerComponent<T extends DatePickerProperties, K extends DatePickerEventEnum> extends DatePickerComponent<T, K> {

    public CustomDatePickerComponent(EUModule euModule, FieldFormConfig field, String target, Object value, Map valueMap) {
        super(field.getId());
        this.getProperties().setId(field.getId());
        this.getProperties().setName(field.getFieldname());
        this.getProperties().setDesc(field.getAggConfig().getCaption());
        this.getProperties().setCaption(field.getAggConfig().getCaption());
        this.setTarget(target);

    }

}
