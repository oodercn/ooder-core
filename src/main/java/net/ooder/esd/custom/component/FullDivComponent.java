package net.ooder.esd.custom.component;


import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.component.form.field.CustomFieldDivComponent;
import net.ooder.esd.engine.EUModule;

import java.util.Map;

public class FullDivComponent extends CustomModuleComponent<CustomFieldDivComponent> {

    public FullDivComponent() {
        super();
    }


    public FullDivComponent(EUModule module, MethodConfig methodConfig, Map<String, Object> valueMap) throws ClassNotFoundException {
        super(module, methodConfig, valueMap);
        CustomFieldDivComponent topBarComponent = new CustomFieldDivComponent(module, methodConfig, valueMap);
        this.setCurrComponent(topBarComponent);
        this.getModuleComponent().addChildren(topBarComponent);

    }


}
