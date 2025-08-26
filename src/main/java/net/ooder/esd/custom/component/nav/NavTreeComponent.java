package net.ooder.esd.custom.component.nav;

import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.NavTreeComboViewBean;
import net.ooder.esd.custom.properties.NavTreeProperties;
import net.ooder.esd.tool.component.TreeViewComponent;

import java.util.Map;

public class NavTreeComponent extends TreeViewComponent {


    public NavTreeComponent(MethodConfig methodConfig, Map valueMap) {
        super();
        NavTreeComboViewBean viewBean = (NavTreeComboViewBean) methodConfig.getView();
        this.setProperties(new NavTreeProperties(viewBean));
        this.setAlias(methodConfig.getName());
    }


}
