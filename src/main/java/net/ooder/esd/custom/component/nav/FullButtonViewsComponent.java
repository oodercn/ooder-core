package net.ooder.esd.custom.component.nav;

import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.CustomMenu;
import net.ooder.esd.annotation.event.CustomTabsEvent;
import net.ooder.esd.annotation.menu.CustomFormMenu;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.CustomButtonViewsViewBean;
import net.ooder.esd.custom.component.CustomModuleComponent;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.ButtonViewsComponent;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.ButtonViewsProperties;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class FullButtonViewsComponent extends CustomModuleComponent<ButtonViewsComponent> {

    public FullButtonViewsComponent() {
        super();
    }

    public FullButtonViewsComponent(EUModule module, MethodConfig methodConfig, Map<String, Object> valueMap) {
        super(module, methodConfig, valueMap);
        CustomButtonViewsViewBean viewBean = (CustomButtonViewsViewBean) methodConfig.getView();
        String alias = viewBean.getMethodName() + ComponentType.BUTTONVIEWS.name();
        ButtonViewsProperties viewsProperties = new ButtonViewsProperties(viewBean, valueMap);
        ButtonViewsComponent currComponent = new ButtonViewsComponent(alias, viewsProperties);
        this.addChildLayoutNav(currComponent);
        this.setCurrComponent(currComponent);
        this.fillTabsAction(viewBean, currComponent);

    }

    protected void fillTabsAction(CustomButtonViewsViewBean view, Component currComponent) {


        Set<CustomTabsEvent> customFormEvents = view.getEvent();

        for (CustomTabsEvent eventType : customFormEvents) {
            for (CustomAction actionType : eventType.getActions(false)) {
                currComponent.addAction( new Action(actionType,eventType.getEventEnum()));
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
