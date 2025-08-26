package net.ooder.esd.custom.properties;


import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.SelModeType;
import net.ooder.esd.bean.view.CustomTreeViewBean;
import net.ooder.esd.bean.view.NavTreeComboViewBean;
import net.ooder.esd.tool.properties.TreeViewProperties;

public class NavTreeProperties extends TreeViewProperties<NavTreeListItem> {
    public NavTreeProperties(NavTreeComboViewBean treeViewBean) {
        CustomTreeViewBean customTreeViewBean = treeViewBean.getTreeViewBean();
        this.init(customTreeViewBean);
        this.name = treeViewBean.getName() + ComponentType.TREEGRID.name();
        this.caption = treeViewBean.getCaption();
        NavTreeListItem navItemProperties = new NavTreeListItem(customTreeViewBean);
        this.addItem(navItemProperties);
        this.setSelMode(SelModeType.single);
        treeViewBean.getTabsViewBean().setNoHandler(true);

    }

}
