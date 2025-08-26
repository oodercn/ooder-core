package net.ooder.esd.custom.component;


import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.component.nav.NavTreeComponent;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.TreeViewComponent;

import java.util.Map;

public class ClassTreeComponent extends CustomTreeComponent<TreeViewComponent> {


    public ClassTreeComponent() {
        super();
    }


    public ClassTreeComponent(EUModule module, MethodConfig customMethodAPIBean , Map<String, Object> valueMap) {
        super(module, customMethodAPIBean, valueMap);
        TreeViewComponent currComponent =  new NavTreeComponent( customMethodAPIBean,valueMap);
        this.addChildLayoutNav(currComponent);
        this.setCurrComponent(currComponent);

    }


}
