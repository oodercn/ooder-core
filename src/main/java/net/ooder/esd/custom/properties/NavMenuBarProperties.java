package net.ooder.esd.custom.properties;

import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.data.NavMenuBarDataBean;
import net.ooder.esd.bean.view.NavMenuBarViewBean;
import net.ooder.esd.tool.properties.MenuBarProperties;

import java.util.Map;


public class NavMenuBarProperties extends MenuBarProperties {

    public NavMenuBarProperties(MethodConfig<NavMenuBarViewBean, NavMenuBarDataBean> methodConfig, Map valueMap) {
        NavMenuBarViewBean menuBarViewBean = methodConfig.getView();
        this.name = methodConfig.getName();
        this.caption = methodConfig.getCaption();
        this.imageClass = methodConfig.getImageClass();
        NavMenuListItem navItemProperties = new NavMenuListItem(menuBarViewBean);
        this.addItem(navItemProperties);
    }

    public NavMenuBarProperties(NavMenuBarViewBean menuBarViewBean, Map valueMap) {
        this.name = menuBarViewBean.getName();
        this.caption = menuBarViewBean.getCaption();
        this.imageClass = menuBarViewBean.getImageClass();
        NavMenuListItem navItemProperties = new NavMenuListItem(menuBarViewBean);
        this.addItem(navItemProperties);
    }

}
