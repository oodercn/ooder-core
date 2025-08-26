package net.ooder.esd.bean.view;

import net.ooder.esd.annotation.event.CustomOpinionEvent;
import net.ooder.esd.annotation.menu.OpinionMenu;
import net.ooder.esd.bean.CustomView;
import net.ooder.esd.dsm.view.field.ESDFieldConfig;
import net.ooder.esd.tool.properties.item.ButtonLayoutItem;

import java.util.Set;

public interface CustomButtonLayoutView<T extends ESDFieldConfig> extends CustomView<T,ButtonLayoutItem> {


    public Set<CustomOpinionEvent> getEvent();

    public Set<OpinionMenu> getCustomMenu();


}
