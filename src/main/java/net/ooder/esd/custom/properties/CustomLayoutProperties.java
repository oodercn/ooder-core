package net.ooder.esd.custom.properties;


import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;
import net.ooder.esd.tool.properties.LayoutProperties;
import net.ooder.esd.tool.properties.item.LayoutListItem;

import java.util.List;


public class CustomLayoutProperties extends LayoutProperties {

    public CustomLayoutProperties(MethodConfig methodConfig) {
        this.name = methodConfig.getName() + ComponentType.LAYOUT.name();
        List<FieldModuleConfig> items = methodConfig.getView().getNavItems();
        this.caption = methodConfig.getCaption();
        this.imageClass = methodConfig.getImageClass();
        for (FieldModuleConfig itemInfo : items) {
            LayoutListItem navItemProperties = new LayoutListItem(itemInfo.getLayoutItemBean());
            this.addItem(navItemProperties);
        }
    }

}
