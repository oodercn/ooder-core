package net.ooder.esd.custom.properties;


import net.ooder.esd.bean.view.CustomTreeViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.tool.properties.TreeViewProperties;

import java.util.Map;


public class ClassTreeProperties extends TreeViewProperties<NavTreeListItem> {
    public ClassTreeProperties(MethodConfig methodConfig, Map valueMap) {
        CustomTreeViewBean bean = new CustomTreeViewBean(methodConfig);
        this.init(bean);
        this.name = methodConfig.getName();
        this.caption = methodConfig.getCaption();
        NavTreeListItem navItemProperties = new NavTreeListItem(bean);
        this.addItem(navItemProperties);
    }


}
