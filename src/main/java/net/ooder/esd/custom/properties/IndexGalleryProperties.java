package net.ooder.esd.custom.properties;


import net.ooder.esd.annotation.ui.BorderType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.SelModeType;
import net.ooder.esd.annotation.ui.UIPositionType;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.properties.GalleryProperties;

import java.util.ArrayList;
import java.util.List;


public class IndexGalleryProperties extends GalleryProperties {

    public IndexGalleryProperties(String name, EUModule[] euModules) {
        int k = 0;
        List<String> classNames = new ArrayList<>();
        if (euModules != null && euModules.length > 0) {
            for (EUModule childModule : euModules) {
                ModuleItemProperties navItemProperties = new ModuleItemProperties(childModule, k);
                navItemProperties.setComment(navItemProperties.getCaption());
                navItemProperties.setCaption("");
                if (!classNames.contains(navItemProperties.getEuClassName())) {
                    classNames.add(navItemProperties.getEuClassName());
                    if (k > 9) {
                        k = 0;
                    }
                    k = k + 1;
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
            this.position = UIPositionType.STATIC;
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

        }

    }

}
