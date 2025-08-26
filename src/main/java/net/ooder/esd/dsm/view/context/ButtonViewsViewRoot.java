package net.ooder.esd.dsm.view.context;

import net.ooder.esd.annotation.ButtonViewsAnnotation;
import net.ooder.esd.annotation.ViewType;
import net.ooder.esd.bean.nav.ButtonViewsItem;
import net.ooder.esd.bean.view.CustomButtonViewsViewBean;
import net.ooder.esd.bean.nav.TabItemEnums;
import net.ooder.esd.custom.properties.ButtonViewsListItem;
import net.ooder.esd.dsm.aggregation.context.AggViewRoot;


public class ButtonViewsViewRoot extends BaseTabsRoot<CustomButtonViewsViewBean, ButtonViewsListItem> {


    private static final Class[] customClass = new Class[]{
            ButtonViewsItem.class,
            ButtonViewsListItem.class,
            TabItemEnums.class,
            ButtonViewsAnnotation.class
    };

    public ButtonViewsViewRoot(AggViewRoot viewRoot, CustomButtonViewsViewBean viewBean, String moduleName, String className) {
        super(viewRoot, viewBean, moduleName, className);
    }


    public Class[] getCustomClass() {
        return customClass;
    }

    @Override
    public ViewType getViewType() {
        return ViewType.NAVBUTTONVIEWS;
    }


}
