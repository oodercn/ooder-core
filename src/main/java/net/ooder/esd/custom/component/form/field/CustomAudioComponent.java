package net.ooder.esd.custom.component.form.field;

import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.AudioComponent;
import net.ooder.esd.tool.properties.AudioProperties;

import java.util.Map;

public class CustomAudioComponent extends AudioComponent {


    public CustomAudioComponent(EUModule euModule, FieldFormConfig field, String target, Object value, Map valueMap) {
        super(field.getId());
        AudioProperties properties = new AudioProperties();
        properties.setId(field.getId());
        properties.setName(field.getFieldname());
        properties.setDesc(field.getAggConfig().getCaption());
        properties.setDock(Dock.fill);
        this.setTarget(target);
        this.setProperties(properties);

    }


}
