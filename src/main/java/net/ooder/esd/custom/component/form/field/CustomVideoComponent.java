package net.ooder.esd.custom.component.form.field;

import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.DivComponent;
import net.ooder.esd.tool.component.VideoComponent;
import net.ooder.esd.tool.properties.VideoProperties;

import java.util.Map;

public class CustomVideoComponent extends DivComponent {


    public CustomVideoComponent(EUModule euModule, FieldFormConfig field, String target, Object value, Map valueMap) {
        super(ComponentType.DIV, field.getId() + "div", Dock.fill);
        VideoProperties properties = new VideoProperties();
        properties.setId(field.getId());
        properties.setName(field.getFieldname());
        properties.setDesc(field.getAggConfig().getCaption());
        properties.setDock(Dock.fill);
        VideoComponent audioComponent = new VideoComponent(field.getFieldname(), properties);
        this.addChildren(audioComponent);
        this.setTarget(target);

    }


}
