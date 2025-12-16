package net.ooder.esd.custom.component.nav;

import net.ooder.common.JDSException;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.BlockComponent;
import net.ooder.web.RequestMethodBean;

import java.util.Map;

public class FullMainComponent extends ModuleComponent {


    public FullMainComponent(EUModule module, RequestMethodBean methodBean, Map<String, Object> valueMap) {
        this.euModule = module;
        euModule.setComponent(this);
        BlockComponent mainComponent = new BlockComponent(Dock.fill, euModule.getName() + DefaultTopBoxfix);
        String packageName = (String) valueMap.get("packageName");

    }


}
