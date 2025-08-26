package net.ooder.esd.bean.view;

import net.ooder.esd.annotation.event.CustomGalleryEvent;
import net.ooder.esd.annotation.menu.CustomGalleryMenu;
import net.ooder.esd.bean.CustomView;
import net.ooder.esd.dsm.view.field.ESDFieldConfig;
import net.ooder.esd.tool.properties.item.GalleryItem;

import java.util.Set;

public interface CustomGalleryView<T extends ESDFieldConfig> extends CustomView<T,GalleryItem> {


    public Set<CustomGalleryEvent> getEvent();

    public Set<CustomGalleryMenu> getCustomMenu();


}
