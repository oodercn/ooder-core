package net.ooder.esd.bean.grid;

import net.ooder.esd.annotation.event.CustomGridEvent;
import net.ooder.esd.annotation.menu.GridMenu;
import net.ooder.esd.bean.CustomView;
import net.ooder.esd.dsm.view.field.ESDFieldConfig;
import net.ooder.esd.tool.properties.item.GridItem;

import java.util.Set;

public interface CustomGridView<T extends ESDFieldConfig> extends CustomView<T,GridItem> {


    public Set<CustomGridEvent> getEvent();

    public Set<GridMenu> getCustomMenu();


}
