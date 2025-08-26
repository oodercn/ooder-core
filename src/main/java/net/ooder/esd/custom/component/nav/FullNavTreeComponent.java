package net.ooder.esd.custom.component.nav;

import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.CustomMenu;
import net.ooder.esd.annotation.event.CustomTreeEvent;
import net.ooder.esd.annotation.menu.CustomFormMenu;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.NavTreeComboViewBean;
import net.ooder.esd.custom.action.ShowPageAction;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.component.LayoutComponent;
import net.ooder.esd.tool.properties.Action;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class FullNavTreeComponent<M extends LayoutComponent> extends CustomNavTreeComponent<M> {
    public FullNavTreeComponent() {
        super();
    }

    public FullNavTreeComponent(EUModule module, MethodConfig methodConfig, Map<String, Object> valueMap) {
        super(module, methodConfig, valueMap);
        NavTreeComboViewBean bean = (NavTreeComboViewBean) methodConfig.getView();
        //this.fillAction(bean);
        this.fillPopTreeAction(bean);
        this.addChildNav(bean);
        this.fillMenuAction(bean.getTreeViewBean(), getTreeComponent());
    }


    protected void fillPopTreeAction(NavTreeComboViewBean view) {
        super.fillTreeAction(view.getTreeViewBean(), treeComponent);
        Set<CustomTreeEvent> customFormEvents = view.getTreeViewBean().getEvent();
        for (CustomTreeEvent eventType : customFormEvents) {
            for (CustomAction actionType : eventType.getActions(false)) {
                treeComponent.addAction(new Action(actionType, eventType.getEventEnum()));
                Action action = new Action(actionType, eventType.getEventEnum());
                MethodConfig methodConfig = view.getTreeViewBean().getMethodConfig().getDataBean().getMethodEvent(eventType);
                if (methodConfig != null) {
                    if (!methodConfig.isModule()) {
                        if (this.findComponentByAlias(actionType.target()) == null) {
                            APICallerComponent apiCallerComponent = new APICallerComponent(methodConfig);
                            apiCallerComponent.setAlias(actionType.target());
                            this.addChildren(apiCallerComponent);
                        }
                    } else {
                        action = new ShowPageAction(methodConfig, eventType.getEventEnum());
                        action.updateArgs("{args[1]}", 6);
                        action.updateArgs("{args[2]}", 5);
                    }
                }
                treeComponent.addAction(action);

            }
        }
        List<CustomFormMenu> customFormMenus = view.getCustomMenu();
        if (customFormMenus != null && customFormMenus.size() > 0) {
            this.getMenuBar(view.getMenuBar()).addMenu(customFormMenus.toArray(new CustomMenu[]{}));
        }

        List<CustomFormMenu> customBottombar = view.getBottombarMenu();
        if (customBottombar != null && customBottombar.size() > 0) {
            this.getBottomBar(view.getBottomBar()).addMenu(customBottombar.toArray(new CustomFormMenu[]{}));
        }

    }


}
