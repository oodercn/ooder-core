package net.ooder.esd.dsm.view.context;


import net.ooder.annotation.IconEnumstype;
import net.ooder.esd.annotation.NavFoldingTabsAnnotation;
import net.ooder.esd.annotation.TabsAnnotation;
import net.ooder.esd.annotation.ViewType;
import net.ooder.esd.annotation.field.TabItem;
import net.ooder.esd.bean.view.NavFoldingTabsViewBean;
import net.ooder.esd.custom.properties.NavFoldingTabsListItem;
import net.ooder.esd.custom.properties.NavTabListItem;
import net.ooder.esd.dsm.aggregation.context.AggViewRoot;


public class FoldingTabsViewRoot extends BaseTabsRoot<NavFoldingTabsViewBean, NavTabListItem> {


    public String space;

    public String baseUrl;


    private static final Class[] customClass = new Class[]{

            NavFoldingTabsListItem.class,
            TabItem.class,
            IconEnumstype.class,
            NavFoldingTabsAnnotation.class,
            TabsAnnotation.class
    };


    public FoldingTabsViewRoot(AggViewRoot viewRoot, NavFoldingTabsViewBean viewBean, String moduleName, String className) {
        super(viewRoot, viewBean, moduleName, className);
        this.dsmBean = viewRoot.getDsmBean();
        this.space = dsmBean.getSpace();


    }

    @Override
    public String getSpace() {
        return space;
    }

    @Override
    public void setSpace(String space) {
        this.space = space;
    }

    @Override
    public String getBaseUrl() {
        return baseUrl;
    }

    @Override
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Class[] getCustomClass() {
        return customClass;
    }

    @Override
    public ViewType getViewType() {
        return ViewType.NAVFOLDINGTABS;
    }


}
