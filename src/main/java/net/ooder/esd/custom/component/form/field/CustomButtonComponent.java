package net.ooder.esd.custom.component.form.field;


import net.ooder.esd.bean.field.ButtonFieldBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.ButtonComponent;
import net.ooder.esd.tool.properties.form.ButtonProperties;

import java.util.Map;

public class CustomButtonComponent extends ButtonComponent {


    public CustomButtonComponent(EUModule euModule, FieldFormConfig<ButtonFieldBean, ?> field, String target, Object value, Map valueMap) {
        super(field.getId());
        ButtonProperties buttonProperties = new ButtonProperties(field);
        buttonProperties.setId(field.getId());
        buttonProperties.setName(field.getFieldname());
        buttonProperties.setDesc(field.getAggConfig().getCaption());
        buttonProperties.setValue(value);
        buttonProperties.setReadonly(field.getAggConfig().getReadonly());
        buttonProperties.setDisabled(field.getAggConfig().getDisabled());
        this.setProperties( buttonProperties);
        this.setTarget(target);
        this.initEvent(euModule, field);

    }


}
