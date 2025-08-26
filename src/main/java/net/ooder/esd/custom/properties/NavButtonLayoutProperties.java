package net.ooder.esd.custom.properties;


import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.view.CustomButtonLayoutViewBean;
import net.ooder.esd.bean.view.NavButtonLayoutComboViewBean;
import net.ooder.esd.tool.properties.ButtonLayoutProperties;
import net.ooder.esd.tool.properties.item.ButtonLayoutItem;

import java.util.List;


public class NavButtonLayoutProperties extends ButtonLayoutProperties {


    public NavButtonLayoutProperties(NavButtonLayoutComboViewBean buttonLayoutViewBean) {
        super.init(buttonLayoutViewBean.getButtonLayoutViewBean());
        initNav(buttonLayoutViewBean.getButtonLayoutViewBean());
    }


    public void initNav(CustomButtonLayoutViewBean buttonLayoutViewBean) {
        this.name = buttonLayoutViewBean.getMethodName() + ComponentType.BUTTONLAYOUT.name();
        this.caption = buttonLayoutViewBean.getCaption();
        this.imageClass = buttonLayoutViewBean.getImageClass();
        List<ButtonLayoutItem> moduleItems = buttonLayoutViewBean.getTabItems();
        this.setItems(moduleItems);
        this.setColumns(moduleItems.size());
    }

}
