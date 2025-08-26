package net.ooder.esd.dsm.view.context;

import net.ooder.esd.annotation.TabsAnnotation;
import net.ooder.esd.annotation.ViewType;
import net.ooder.esd.annotation.field.TabItem;
import net.ooder.esd.bean.view.StacksViewBean;
import net.ooder.esd.custom.properties.NavTabListItem;
import net.ooder.esd.dsm.aggregation.context.AggViewRoot;
import net.ooder.esd.tool.properties.item.TabListItem;


public class StacksViewRoot extends BaseTabsRoot<StacksViewBean, NavTabListItem> {


    private static final Class[] customClass = new Class[]{
            TabItem.class,
            TabListItem.class,
            TabsAnnotation.class,
    };


    public StacksViewRoot(AggViewRoot viewRoot, StacksViewBean viewBean, String moduleName, String className) {
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
