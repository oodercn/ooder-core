package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.event.NullEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.form.HiddenInputProperties;

public class HiddenInputComponent extends Component<HiddenInputProperties, NullEventEnum> {

    public HiddenInputComponent(String alias) {
        super(ComponentType.HIDDENINPUT, alias);
        this.setProperties( new HiddenInputProperties());
    }

    public HiddenInputComponent(String alias, HiddenInputProperties properties) {
        super(ComponentType.HIDDENINPUT, alias);
        this.setProperties(properties);
    }

    public HiddenInputComponent(String alias, String name, Object value) {
        this(alias==null?alias:name, name, value, false);

    }


    public HiddenInputComponent(String alias, String name, Object value, boolean isPid) {
        super(ComponentType.HIDDENINPUT, alias==null?alias:name);
        this.setProperties( new HiddenInputProperties());
        this.getProperties().setFormField(true);
        this.getProperties().setName(name);
        this.getProperties().setPid(isPid);
        if (value != null) {
            this.getProperties().setValue(value);
        }

    }


    public HiddenInputComponent addAction(Action action) {
        super.addAction(action);
        return this;
    }

    public HiddenInputComponent() {
        super(ComponentType.HIDDENINPUT);
    }

}
