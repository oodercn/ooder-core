package net.ooder.esd.custom.component.form.field;


import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.bean.field.base.SliderFieldBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.SliderComponent;
import net.ooder.esd.tool.properties.form.SliderProperties;

import java.util.Map;

public class CustomSliderComponent<T extends SliderProperties, K extends FieldEventEnum> extends SliderComponent<T, K> {


    public CustomSliderComponent(EUModule euModule, FieldFormConfig field, String target, Object value, Map valueMap) {
        super(field.getId());
        SliderProperties sliderProperties = new SliderProperties((SliderFieldBean) field.getWidgetConfig(),field.getContainerBean());
        sliderProperties.setId(field.getId());
        sliderProperties.setName(field.getFieldname());
        sliderProperties.setDesc(field.getAggConfig().getCaption());
        sliderProperties.setCaption(field.getAggConfig().getCaption());
        sliderProperties.setValue(value);
        sliderProperties.setReadonly(field.getAggConfig().getReadonly());
        sliderProperties.setDisabled(field.getAggConfig().getDisabled());
        this.setProperties((T) sliderProperties);
        this.setTarget(target);
        initEditor(euModule, field, (K) FieldEventEnum.onChange);
    }
}
