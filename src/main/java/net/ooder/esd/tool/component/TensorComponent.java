package net.ooder.esd.tool.component;


import net.ooder.esd.annotation.event.TensorEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.form.TensorProperties;

public class TensorComponent extends FieldComponent<TensorProperties, TensorEventEnum> {

    public TensorComponent addAction(Action<TensorEventEnum> action) {
        super.addAction(action);
        return this;
    }

    public TensorComponent(String alias) {
        super(ComponentType.TENSOR, alias);
       this.setProperties( new TensorProperties());
    }

    public TensorComponent(String alias, TensorProperties properties) {
        super(ComponentType.TENSOR, alias);
        this.setProperties(properties);
    }

    public TensorComponent() {
        super(ComponentType.TENSOR);
        this.setProperties( new TensorProperties());
    }
}
