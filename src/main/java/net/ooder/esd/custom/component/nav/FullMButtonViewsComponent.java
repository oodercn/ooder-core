package net.ooder.esd.custom.component.nav;

import net.ooder.common.JDSException;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.CustomMenu;
import net.ooder.esd.annotation.event.CustomTabsEvent;
import net.ooder.esd.annotation.menu.CustomFormMenu;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.MButtonViewsViewBean;
import net.ooder.esd.custom.component.CustomMButtonViewsComponent;
import net.ooder.esd.custom.component.CustomModuleComponent;
import net.ooder.esd.custom.action.ShowPageAction;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.MButtonViewsComponent;
import net.ooder.esd.tool.properties.Action;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class FullMButtonViewsComponent extends CustomModuleComponent<MButtonViewsComponent> {

    public FullMButtonViewsComponent() {
        super();
    }

    public FullMButtonViewsComponent(EUModule module, MethodConfig methodConfig, Map<String, Object> valueMap) throws JDSException {
        super(module, methodConfig, valueMap);
        MButtonViewsViewBean viewBean = (MButtonViewsViewBean) methodConfig.getView();
        CustomMButtonViewsComponent currComponent = new CustomMButtonViewsComponent(viewBean, valueMap);
        this.addChildLayoutNav(currComponent);
        this.setCurrComponent(currComponent);
        this.fillTabsAction(viewBean, currComponent);

    }

    protected void fillTabsAction(MButtonViewsViewBean view, Component currComponent) {


        Set<CustomTabsEvent> customFormEvents = view.getEvent();

        for (CustomTabsEvent eventType : customFormEvents) {
            for (CustomAction actionType : eventType.getActions(false)) {
                currComponent.addAction(new Action(actionType,eventType.getEventEnum()));
                Action action = new Action(actionType,eventType.getEventEnum());
                MethodConfig methodConfig = methodAPIBean.getDataBean().getMethodEvent(eventType);
                if (methodConfig != null) {
                    if (!methodConfig.isModule()) {
                        if (this.findComponentByAlias(actionType.target()) == null) {
                            APICallerComponent apiCallerComponent = new APICallerComponent(methodConfig);
                            apiCallerComponent.setAlias(actionType.target());
                            this.addChildren(apiCallerComponent);
                        }
                    } else {
                        action = new ShowPageAction(methodConfig,eventType.getEventEnum());
                        action.updateArgs("{args[1]}", 6);
                        action.updateArgs("{args[2]}", 5);
                    }
                }
                currComponent.addAction( action);


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

        super.fillAction(view);
    }
}
