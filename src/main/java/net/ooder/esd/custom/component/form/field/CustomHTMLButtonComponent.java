package net.ooder.esd.custom.component.form.field;


import net.ooder.esd.annotation.event.BottonEventEnum;
import net.ooder.esd.bean.field.ButtonFieldBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.HTMLButtonComponent;
import net.ooder.esd.tool.properties.form.HTMLButtonProperties;

import java.util.Map;

public class CustomHTMLButtonComponent<T extends HTMLButtonProperties, K extends BottonEventEnum> extends HTMLButtonComponent<T, K> {


    public CustomHTMLButtonComponent(EUModule euModule, FieldFormConfig<ButtonFieldBean, ?> field, String target, Object value, Map valueMap) {
        super(field.getId());
        this.getProperties().setId(field.getId());
        this.getProperties().setName(field.getFieldname());
        this.getProperties().setDesc(field.getAggConfig().getCaption());
        this.getProperties().setReadonly(field.getAggConfig().getReadonly());
        this.getProperties().setDisabled(field.getAggConfig().getDisabled());
        this.setTarget(target);

    }


}
