package net.ooder.esd.tool.component;


import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.form.SliderProperties;

public class SliderComponent<T extends SliderProperties, K extends FieldEventEnum> extends FieldComponent<T, K> {


    public SliderComponent(String alias) {
        super(ComponentType.SLIDER, alias);
        this.setProperties((T) new SliderProperties());
    }

    public SliderComponent(String alias, T properties) {
        super(ComponentType.SLIDER, alias);
        this.setProperties(properties);
    }


    public SliderComponent addAction(Action<K> action) {
        super.addAction(action);
        return this;
    }

    public SliderComponent() {
        super(ComponentType.SLIDER);
        this.setProperties((T) new SliderProperties());
    }

}
