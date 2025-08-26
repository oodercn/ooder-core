package net.ooder.esd.custom.component;

import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.event.CustomTreeEvent;
import net.ooder.esd.annotation.menu.TreeMenu;
import net.ooder.esd.bean.view.CustomTreeViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.TreeViewComponent;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.TreeViewProperties;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class FullPopTreeComponent<M extends TreeViewComponent> extends CustomPopTreeComponent<M> {
    public FullPopTreeComponent() {
        super();
    }

    public FullPopTreeComponent(EUModule module, MethodConfig methodConfig, Map<String, Object> valueMap) throws ClassNotFoundException {
        super(module, methodConfig, valueMap);
        CustomTreeViewBean customTreeViewBean = (CustomTreeViewBean) methodConfig.getView();
        TreeViewProperties treeViewProperties = new TreeViewProperties(customTreeViewBean);
        M currComponent = (M) new TreeViewComponent(euModule.getName(), treeViewProperties);
        this.addChildNav(currComponent);
        this.fillTreeAction(customTreeViewBean, currComponent);
        this.fillPopTreeAction(customTreeViewBean, currComponent);
        this.fillButtonAction(customTreeViewBean);
        this.fillMenuAction(customTreeViewBean, currComponent);

    }


    protected void fillButtonAction(CustomTreeViewBean view) {
        List<TreeMenu> customFormMenus = view.getCustomMenu();
        if (customFormMenus != null && customFormMenus.size() > 0) {
            this.getMenuBar(view.getMenuBar()).addMenu(customFormMenus.toArray(new TreeMenu[]{}));
        }

        List<TreeMenu> customBottombar = view.getBottombarMenu();
        if (customBottombar != null && customBottombar.size() > 0) {
            this.getBottomBar(view.getBottomBar()).addMenu(customBottombar.toArray(new TreeMenu[]{}));
        }
    }


    protected void fillPopTreeAction(CustomTreeViewBean view, Component currComponent) {
        Set<CustomTreeEvent> customFormEvents = view.getEvent();
        for (CustomTreeEvent eventType : customFormEvents) {
            for (CustomAction actionType : eventType.getActions(false)) {
                currComponent.addAction(new Action(actionType,eventType.getEventEnum()));
            }
        }
    }


}
