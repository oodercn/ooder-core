package net.ooder.esd.custom.component.nav;

import net.ooder.common.JDSException;
import net.ooder.esd.annotation.CustomMenu;
import net.ooder.esd.annotation.action.CustomLoadClassAction;
import net.ooder.esd.annotation.event.TabsEventEnum;
import net.ooder.esd.annotation.menu.CustomFormMenu;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.StacksViewBean;
import net.ooder.esd.custom.component.CustomModuleComponent;
import net.ooder.esd.custom.component.CustomStacksComponent;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.properties.Action;

import java.util.List;
import java.util.Map;

public class FullStacksComponent extends CustomModuleComponent<CustomStacksComponent> {
    public FullStacksComponent() {
        super();
    }

    public FullStacksComponent(EUModule module, MethodConfig methodConfig, Map<String, Object> valueMap) throws JDSException {
        super(module, methodConfig, valueMap);

        CustomStacksComponent stacksComponent = new CustomStacksComponent(euModule, methodConfig, valueMap);
        Action showAction = new Action(CustomLoadClassAction.tabShow,TabsEventEnum.onItemSelected);
        showAction.updateArgs(stacksComponent.getAlias(), 4);
        StacksViewBean viewBean = (StacksViewBean) methodConfig.getView();
        if (viewBean != null && viewBean.getAutoReload()) {
            stacksComponent.addAction(showAction,false);
        } else {
            showAction.setEventKey(TabsEventEnum.onIniPanelView);
            stacksComponent.addAction( showAction,false);
        }
        this.fillTabsAction(viewBean, stacksComponent);


    }

    protected void fillTabsAction(StacksViewBean view, Component currComponent) {


//        Set<CustomFormEvent> customFormEvents = view.getEvent();
//
//        for (CustomEvent eventType : customFormEvents) {
//            for (CustomAction actionType : eventType.getActions(false)) {
//                currComponent.addEvent(eventType.getEventEnum(), new Action(actionType));
//            }
//        }
        List<CustomFormMenu> customFormMenus = view.getCustomMenu();
        if (customFormMenus != null && customFormMenus.size() > 0) {
            this.getMenuBar(view.getMenuBar()).addMenu(customFormMenus.toArray(new CustomMenu[]{}));
        }


        List<CustomFormMenu> customBottombar = view.getBottombarMenu();
        if (customBottombar != null && customBottombar.size() > 0) {
            this.getBottomBar(view.getBottomBar()).addMenu(customBottombar.toArray(new CustomFormMenu[]{}));
        }
        super.fillAction(view);

    }
}
