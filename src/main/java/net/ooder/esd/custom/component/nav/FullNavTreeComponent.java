package net.ooder.esd.custom.component.nav;

import net.ooder.esd.annotation.action.CustomFormAction;
import net.ooder.esd.annotation.event.ModuleEventEnum;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.NavTreeComboViewBean;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.component.LayoutComponent;
import net.ooder.esd.tool.properties.Action;

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
        APICallerComponent component = (APICallerComponent) this.findComponentByAlias("RELOAD");
        if (component != null && (component.getProperties().getAutoRun() == null || !component.getProperties().getAutoRun())) {
            Action action = new Action(CustomFormAction.RELOAD, ModuleEventEnum.afterShow);
            this.addAction(action);
        }
    }

    protected void fillPopTreeAction(NavTreeComboViewBean view) {
        super.fillTreeAction(view.getTreeViewBean(), treeComponent);
    }
}
