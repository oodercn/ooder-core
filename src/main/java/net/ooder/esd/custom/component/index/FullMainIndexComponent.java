package net.ooder.esd.custom.component.index;

import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.component.CustomModuleComponent;
import net.ooder.esd.engine.EUModule;

import java.util.Map;

public class FullMainIndexComponent extends CustomModuleComponent<IndexMainComponent> {


    public FullMainIndexComponent() {
        super();
    }

    public FullMainIndexComponent(EUModule module, MethodConfig methodConfig, Map valueMap) throws ClassNotFoundException {
        super(module, methodConfig, valueMap);
        IndexMainComponent topBarComponent = new IndexMainComponent(module, methodConfig, valueMap);
        this.setCurrComponent( topBarComponent);
        this.getModuleComponent().addChildren(topBarComponent);

    }


}
