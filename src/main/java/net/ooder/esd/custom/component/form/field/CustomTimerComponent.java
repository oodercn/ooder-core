package net.ooder.esd.custom.component.form.field;


import net.ooder.esd.annotation.event.UIEventEnum;
import net.ooder.esd.bean.field.base.TimePickerFieldBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.properties.TimerProperties;

import java.util.Map;

public class CustomTimerComponent<T extends TimerProperties, K extends UIEventEnum> extends Component<T, K> {

    public CustomTimerComponent(EUModule euModule, FieldFormConfig field, String target, Object value, Map valueMap)  {

        TimerProperties timerProperties=new TimerProperties((TimePickerFieldBean) field.getWidgetConfig(),field.getContainerBean());
        this.setProperties((T) timerProperties);


        this.getProperties().setId(field.getId());
        this.getProperties().setName(field.getFieldname());
        this.getProperties().setDesc(field.getAggConfig().getCaption());
        this.getProperties().setCaption(field.getAggConfig().getCaption());
        this.setTarget(target);

    }


}
