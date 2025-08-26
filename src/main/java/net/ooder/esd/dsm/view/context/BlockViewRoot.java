package net.ooder.esd.dsm.view.context;

import net.ooder.esd.annotation.BlockAnnotation;
import net.ooder.esd.annotation.ViewType;
import net.ooder.esd.annotation.event.TabsEvent;
import net.ooder.esd.annotation.event.TabsEventEnum;
import net.ooder.esd.annotation.view.BlockViewAnnotation;
import net.ooder.esd.bean.view.CustomBlockFormViewBean;
import net.ooder.esd.dsm.aggregation.context.AggViewRoot;


public class BlockViewRoot extends BaseFormViewRoot<CustomBlockFormViewBean> {

    private static final Class[] customClass = new Class[]{
            TabsEvent.class,
            BlockViewAnnotation.class,
            BlockAnnotation.class,
            TabsEventEnum.class
    };


    public BlockViewRoot(AggViewRoot aggViewRoot, CustomBlockFormViewBean viewBean, String moduleName, String className) {
        super(aggViewRoot, viewBean, moduleName, className);
    }

    public Class[] getCustomClass() {
        return customClass;
    }

    @Override
    public ViewType getViewType() {
        return ViewType.BLOCK;
    }


}
