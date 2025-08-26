package net.ooder.esd.custom.component.form.field;


import net.ooder.esd.annotation.event.UIEventEnum;
import net.ooder.esd.bean.field.base.TimePickerFieldBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.TimePickerComponent;
import net.ooder.esd.tool.properties.form.TimePickerProperties;

import java.util.Map;

public class CustomTimePickerComponent<T extends TimePickerProperties, K extends UIEventEnum> extends TimePickerComponent<T, K> {

    public CustomTimePickerComponent(EUModule euModule, FieldFormConfig field, String target, Object value, Map valueMap)  {
        super(field.getId());

        TimePickerProperties timePickerProperties=new TimePickerProperties((TimePickerFieldBean) field.getWidgetConfig(),field.getContainerBean());
       this.setProperties((T) timePickerProperties);

        this.getProperties().setId(field.getId());
        this.getProperties().setName(field.getFieldname());
        this.getProperties().setDesc(field.getAggConfig().getCaption());
        this.getProperties().setCaption(field.getAggConfig().getCaption());
        this.setTarget(target);

    }


}
