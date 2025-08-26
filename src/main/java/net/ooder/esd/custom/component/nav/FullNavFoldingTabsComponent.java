package net.ooder.esd.custom.component.nav;

import net.ooder.esd.annotation.action.CustomLoadClassAction;
import net.ooder.esd.annotation.event.TabsEventEnum;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.NavFoldingTabsViewBean;
import net.ooder.esd.custom.component.CustomModuleComponent;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.properties.Action;

import java.util.Map;

public class FullNavFoldingTabsComponent extends CustomModuleComponent {
    public FullNavFoldingTabsComponent() {
        super();
    }

    public FullNavFoldingTabsComponent(EUModule module, MethodConfig methodConfig, Map<String, Object> valueMap) throws ClassNotFoundException {
        super(module, methodConfig, valueMap);

        NavFoldingTabsComponent tabcomponent = new NavFoldingTabsComponent(euModule, methodConfig, valueMap);
        this.addChildLayoutNav(tabcomponent);
        this.setCurrComponent(tabcomponent);
        Action showAction = new Action(CustomLoadClassAction.tabShow,TabsEventEnum.onItemSelected);
        showAction.updateArgs(tabcomponent.getAlias(), 4);
        NavFoldingTabsViewBean viewBean = (NavFoldingTabsViewBean) methodConfig.getView();
        this.fillAction(viewBean);



    }


}
