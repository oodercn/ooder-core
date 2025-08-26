package net.ooder.esd.tool.component;


import net.ooder.esd.annotation.event.UIEventEnum;
import net.ooder.esd.annotation.ui.BorderType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.tool.properties.ModuleProperties;
import net.ooder.esd.tool.properties.Action;

public class ModulePlaceHolder extends Component<ModuleProperties, UIEventEnum> {

    public ModulePlaceHolder addAction(Action<UIEventEnum> action) {
        super.addAction(action);
        return this;
    }

    public ModulePlaceHolder() {
        super(ComponentType.MODLUEPLACEHOLDER);
        this.setProperties(new ModuleProperties(Dock.fill));
        this.getProperties().setBorderType(BorderType.none);
    }

}
