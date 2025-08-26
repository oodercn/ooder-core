package net.ooder.esd.dsm.view.context;

import net.ooder.esd.annotation.ViewType;
import net.ooder.esd.annotation.event.TabsEvent;
import net.ooder.esd.annotation.event.TabsEventEnum;
import net.ooder.esd.bean.view.CustomDivFormViewBean;
import net.ooder.esd.dsm.aggregation.context.AggViewRoot;


public class DivViewRoot  extends BaseFormViewRoot<CustomDivFormViewBean> {
    private static final Class[] customClass = new Class[]{
            TabsEvent.class,
            TabsEventEnum.class
    };


    public DivViewRoot(AggViewRoot aggViewRoot, CustomDivFormViewBean viewBean, String moduleName, String className) {
        super(aggViewRoot, viewBean, moduleName, className);
    }

    public Class[] getCustomClass() {
        return customClass;
    }

    @Override
    public ViewType getViewType() {
        return ViewType.DIV;
    }


}
