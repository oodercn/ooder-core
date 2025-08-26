package net.ooder.esd.dsm.view.context;


import net.ooder.annotation.IconEnumstype;
import net.ooder.esd.annotation.TitleBlockAnnotation;
import net.ooder.esd.annotation.ViewType;
import net.ooder.esd.bean.view.CustomTitleBlockViewBean;
import net.ooder.esd.bean.gallery.ITitleBlockItem;
import net.ooder.esd.dsm.aggregation.context.AggViewRoot;
import net.ooder.esd.tool.properties.item.TitleBlockItem;
import java.util.*;


public class TitleBlockViewRoot extends BaseGalleryRoot<CustomTitleBlockViewBean> {


    public CustomTitleBlockViewBean viewBean;

    private static final Class[] customClass = new Class[]{
            TitleBlockItem.class,
            IconEnumstype.class,
            ITitleBlockItem.class,
            TitleBlockAnnotation.class
    };

    List<TitleBlockItem> titleBlockItems;

    public TitleBlockViewRoot(AggViewRoot viewRoot, CustomTitleBlockViewBean viewBean, String moduleName, String className) {
        super(viewRoot, viewBean, moduleName, className);
        this.titleBlockItems = viewBean.getTitleBlockItems();
    }


    public Class[] getCustomClass() {
        return customClass;
    }

    @Override
    public ViewType getViewType() {
        return ViewType.TITLEBLOCK;
    }


    public List<TitleBlockItem> getTitleBlockItems() {
        return titleBlockItems;
    }

    public void setTitleBlockItems(List<TitleBlockItem> titleBlockItems) {
        this.titleBlockItems = titleBlockItems;
    }
}
