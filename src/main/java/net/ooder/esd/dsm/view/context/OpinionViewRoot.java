package net.ooder.esd.dsm.view.context;

import net.ooder.esd.annotation.*;
import net.ooder.esd.bean.view.CustomOpinionViewBean;
import net.ooder.esd.bean.gallery.IOpiniontem;
import net.ooder.esd.dsm.aggregation.context.AggViewRoot;
import net.ooder.esd.tool.properties.item.OpinionItem;


public class OpinionViewRoot extends BaseGalleryRoot<CustomOpinionViewBean> {

    private static final Class[] customClass = new Class[]{

            OpinionItem.class,
            IOpiniontem.class,
            GridAnnotation.class,
            GalleryAnnotation.class,
            OpinionAnnotation.class,
            GalleryItemAnnotation.class
    };


    public CustomOpinionViewBean viewBean;

    public OpinionViewRoot(AggViewRoot viewRoot, CustomOpinionViewBean viewBean, String moduleName, String className) {
        super(viewRoot, viewBean, moduleName, className);
    }


    @Override
    public ViewType getViewType() {
        return ViewType.OPINIONBLOCK;
    }


    public Class[] getCustomClass() {
        return customClass;
    }

}
