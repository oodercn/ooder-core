package net.ooder.esd.custom.component;


import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.TabsViewBean;
import net.ooder.esd.custom.properties.NavTabsProperties;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.TabsComponent;

import java.util.Map;

public class FullTabsComponent extends CustomModuleComponent<TabsComponent<NavTabsProperties>> {


    public FullTabsComponent() {
        super();
    }

    public FullTabsComponent(EUModule module, MethodConfig methodConfig, Map<String, Object> valueMap) {
        super(module, methodConfig, valueMap);
        TabsViewBean viewBean = (TabsViewBean) methodConfig.getView();
        TabsComponent currComponent = new CustomTabsComponent(viewBean, valueMap);
        this.addChildLayoutNav(currComponent);
        this.setCurrComponent(currComponent);
        this.fillAction(viewBean);
    }


}
