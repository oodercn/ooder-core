package net.ooder.esd.custom.properties;

import net.ooder.common.util.StringUtility;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.view.NavGalleryComboViewBean;
import net.ooder.esd.tool.properties.GalleryProperties;
import net.ooder.esd.tool.properties.item.GalleryNavItem;

import java.util.List;


public class NavGalleryProperties extends GalleryProperties {


    public NavGalleryProperties(NavGalleryComboViewBean galleryViewBean) {
        super.init(galleryViewBean.getGalleryViewBean());
        initNav(galleryViewBean);
    }


    public void initNav(NavGalleryComboViewBean galleryViewBean) {
        this.name = galleryViewBean.getMethodName() + ComponentType.GALLERY.name();
        this.caption = galleryViewBean.getCaption();
        this.imageClass = galleryViewBean.getImageClass();
        List<GalleryNavItem> moduleItems = galleryViewBean.getTabItems();
        for (GalleryNavItem navItem : moduleItems) {
                this.addItem(navItem);
        }

//        List<FieldModuleConfig> moduleItems = galleryViewBean.getNavItems();
//
//        for (FieldModuleConfig itemInfo : moduleItems) {
//            GalleryNavItem navItemProperties = null;
//            try {
//                navItemProperties = new GalleryNavItem(itemInfo);
//                navItemProperties.setComment(navItemProperties.getCaption());
//                navItemProperties.setCaption("");
//                this.addItem((T) navItemProperties);
//            } catch (JDSException e) {
//                e.printStackTrace();
//            }
//        }

        int size = moduleItems.size();

        if (size > 0) {
            this.setValue(moduleItems.get(0).getId());
            this.setColumns(moduleItems.size());
            String iconSize = galleryViewBean.getGalleryViewBean().getIconFontSize();
            String width = galleryViewBean.getGalleryViewBean().getItemWidth();
            String height = galleryViewBean.getGalleryViewBean().getItemHeight();

            if (iconSize != null) {
                if (iconSize.endsWith("em")) {
                    iconSize = StringUtility.replace(iconSize, "em", "");
                    this.setWidth((Integer.valueOf(iconSize) * 2 * size) + "em");
                    this.setHeight((Integer.valueOf(iconSize) * 2 * size) + "em");
                }

            } else {
                if (width != null) {
                    if (width.endsWith("em")) {
                        width = StringUtility.replace(width, "em", "");
                        this.setWidth((Integer.valueOf(width) * size) + "em");
                    } else if (width.endsWith("px")) {
                        width = StringUtility.replace(width, "px", "");
                        this.setWidth((Integer.valueOf(width) * size) + "em");
                    } else {
                        this.setWidth((Integer.valueOf(width) * size) + "");
                    }
                    if (height != null) {
                        if (height.endsWith("em")) {
                            height = StringUtility.replace(height, "em", "");
                            this.setHeight((Integer.valueOf(height) * size) + "em");
                        } else if (height.endsWith("px")) {
                            height = StringUtility.replace(height, "px", "");
                            this.setHeight((Integer.valueOf(height) * galleryViewBean.getGalleryViewBean().getRows()) + "em");
                        } else {
                            this.setHeight((Integer.valueOf(height) * galleryViewBean.getGalleryViewBean().getRows()) + "");
                        }
                    }
                }
            }
        }
    }

}
