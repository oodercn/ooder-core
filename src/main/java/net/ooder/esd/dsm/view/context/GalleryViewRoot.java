package net.ooder.esd.dsm.view.context;

import net.ooder.esd.annotation.GalleryAnnotation;
import net.ooder.esd.annotation.GalleryItemAnnotation;
import net.ooder.esd.annotation.ViewType;
import net.ooder.esd.bean.view.CustomGalleryViewBean;
import net.ooder.esd.bean.gallery.IGalleryItem;
import net.ooder.esd.dsm.aggregation.context.AggViewRoot;
import net.ooder.esd.tool.properties.item.GalleryItem;


public class GalleryViewRoot  extends BaseGalleryRoot<CustomGalleryViewBean> {


    private static final Class[] customClass = new Class[]{
            GalleryItem.class,
            IGalleryItem.class,
            GalleryAnnotation.class,
            GalleryItemAnnotation.class
    };


    public GalleryViewRoot(AggViewRoot viewRoot, CustomGalleryViewBean viewBean, String moduleName, String className) {
        super(viewRoot, viewBean, moduleName, className);
    }

    public  Class[] getCustomClass() {
        return customClass;
    }

    @Override
    public ViewType getViewType() {
        return ViewType.GALLERY;
    }


}
