package net.ooder.esd.custom.component.nav;

import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.NavTreeComboViewBean;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.LayoutComponent;

import java.util.Map;

public class FullNavTreeComponent<M extends LayoutComponent> extends CustomNavTreeComponent<M> {
    public FullNavTreeComponent() {
        super();
    }

    public FullNavTreeComponent(EUModule module, MethodConfig methodConfig, Map<String, Object> valueMap) {
        super(module, methodConfig, valueMap);
        NavTreeComboViewBean bean = (NavTreeComboViewBean) methodConfig.getView();
        this.fillPopTreeAction(bean);
        this.addChildNav(bean);
        this.fillMenuAction(bean.getTreeViewBean(), getTreeComponent());

    }


    protected void fillPopTreeAction(NavTreeComboViewBean view) {
        super.fillTreeAction(view.getTreeViewBean(), treeComponent);
    }
}
