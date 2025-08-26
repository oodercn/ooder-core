package net.ooder.esd.dsm.view.context;

import net.ooder.esd.annotation.ContentBlockAnnotation;
import net.ooder.esd.annotation.ViewType;
import net.ooder.esd.bean.view.CustomContentBlockViewBean;
import net.ooder.esd.bean.gallery.ContentBlockItemEnum;
import net.ooder.esd.bean.gallery.IContentBlockItem;
import net.ooder.esd.dsm.aggregation.context.AggViewRoot;
import net.ooder.esd.tool.properties.item.ContentBlockItem;

import java.util.*;


public class ContentBlockViewRoot extends BaseGalleryRoot<CustomContentBlockViewBean> {

    public CustomContentBlockViewBean viewBean;

    private List<ContentBlockItem> contentBlockItems = new ArrayList<>();

    private static final Class[] customClass = new Class[]{
            ContentBlockItem.class,
            ContentBlockItemEnum.class,
            IContentBlockItem.class,
            ContentBlockAnnotation.class
    };

    public ContentBlockViewRoot() {
        super();
    }

    public ContentBlockViewRoot(AggViewRoot viewRoot, CustomContentBlockViewBean viewBean, String moduleName, String className) {
        super(viewRoot, viewBean, moduleName, className);
        this.contentBlockItems = viewBean.getContentBlockItems();
    }


    @Override
    public ViewType getViewType() {
        return ViewType.CONTENTBLOCK;
    }

    public List<ContentBlockItem> getContentBlockItems() {
        return contentBlockItems;
    }

    public void setContentBlockItems(List<ContentBlockItem> contentBlockItems) {
        this.contentBlockItems = contentBlockItems;
    }

    public Class[] getCustomClass() {
        return customClass;
    }


}
