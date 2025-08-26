package net.ooder.esd.custom.properties;



import net.ooder.esd.annotation.ui.BorderType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.SelModeType;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;
import net.ooder.esd.tool.properties.GalleryProperties;
import net.ooder.esd.tool.properties.item.GalleryItem;
import net.ooder.esd.tool.properties.item.GalleryNavItem;

import java.util.List;


public class NavGalleryClassesProperties extends GalleryProperties {

    public NavGalleryClassesProperties(String name, MethodConfig[] configs) {
        for (MethodConfig navItemClass : configs) {
            List<FieldModuleConfig> items = navItemClass.getView().getNavItems();
            for (FieldModuleConfig itemInfo : items) {
                GalleryItem navItemProperties = null;
                navItemProperties = new GalleryNavItem(itemInfo);
                navItemProperties.setComment(navItemProperties.getCaption());
                navItemProperties.setCaption("");
                this.addItem(navItemProperties);
            }
        }
        int size = items.size();
        this.name = name + ComponentType.GALLERY.name();
        this.desc = name;
        this.value = items.get(0).getId();
        this.columns = size;
        this.autoImgSize = false;
        this.autoItemSize = false;
        this.iconOnly = false;
        this.autoImgSize = false;
        this.iconOnly = false;
        this.position = "static";
        this.selMode = SelModeType.single;
        this.borderType = BorderType.none;
        this.iconFontSize = "4em";
        this.rows = 1;
        this.itemWidth = "4em";

        if (size > 0) {
            this.setWidth((8 * size) + "em");
            this.setValue(this.getItems().get(0).getId());
            this.setHeight("7em");
        }

        ;

    }

}
