package net.ooder.esd.dsm.view.context;

import net.ooder.esd.annotation.TabsAnnotation;
import net.ooder.esd.annotation.ViewType;
import net.ooder.esd.annotation.field.TabItem;
import net.ooder.esd.bean.view.TabsViewBean;
import net.ooder.esd.custom.properties.NavTabListItem;
import net.ooder.esd.dsm.aggregation.context.AggViewRoot;
import net.ooder.esd.tool.properties.item.TabListItem;


public class TabsViewRoot extends BaseTabsRoot<TabsViewBean, NavTabListItem> {


    private static final Class[] customClass = new Class[]{
            TabItem.class,
            TabListItem.class,
            TabsAnnotation.class,
    };


    public TabsViewRoot(AggViewRoot viewRoot, TabsViewBean viewBean, String moduleName, String className) {
        super(viewRoot, viewBean, moduleName, className);
    }


    public Class[] getCustomClass() {
        return customClass;
    }

    @Override
    public ViewType getViewType() {
        return ViewType.NAVTABS;
    }
}
