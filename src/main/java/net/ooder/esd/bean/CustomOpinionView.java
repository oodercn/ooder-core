package net.ooder.esd.bean;

import net.ooder.esd.annotation.event.CustomOpinionEvent;
import net.ooder.esd.annotation.menu.OpinionMenu;
import net.ooder.esd.bean.CustomView;
import net.ooder.esd.dsm.view.field.ESDFieldConfig;
import net.ooder.esd.tool.properties.item.OpinionItem;

import java.util.Set;

public interface CustomOpinionView<T extends ESDFieldConfig> extends CustomView<T,OpinionItem> {


    public Set<CustomOpinionEvent> getEvent();

    public Set<OpinionMenu> getCustomMenu();


}
