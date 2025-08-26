package net.ooder.esd.custom.component.form.field;

import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.ProgressBarComponent;
import net.ooder.esd.tool.properties.form.ProgressBarProperties;

import java.util.Map;

public class CustomProgressBarComponent extends ProgressBarComponent {


    public CustomProgressBarComponent(EUModule euModule, FieldFormConfig field, String target, Object value, Map valueMap)  {
        super(field.getId());
        ProgressBarProperties progressBarProperties = new ProgressBarProperties();
        progressBarProperties.setId(field.getId());
        progressBarProperties.setName(field.getFieldname());
        progressBarProperties.setDesc(field.getAggConfig().getCaption());
        progressBarProperties.setCaption(field.getAggConfig().getCaption());
        progressBarProperties.setValue(value);
        progressBarProperties.setDock(Dock.middle);
        progressBarProperties.setReadonly(field.getAggConfig().getReadonly());
        progressBarProperties.setDisabled(field.getAggConfig().getDisabled());
        this.setProperties(progressBarProperties);
        initEditor(euModule, field,  FieldEventEnum.onChange);
        this.setTarget(target);

    }
}
