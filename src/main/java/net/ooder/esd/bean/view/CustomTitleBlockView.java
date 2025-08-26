package net.ooder.esd.bean.view;

import net.ooder.esd.annotation.event.CustomOpinionEvent;
import net.ooder.esd.annotation.menu.OpinionMenu;
import net.ooder.esd.bean.CustomView;
import net.ooder.esd.dsm.view.field.ESDFieldConfig;
import net.ooder.esd.tool.properties.item.TitleBlockItem;

import java.util.Set;

public interface CustomTitleBlockView<T extends ESDFieldConfig> extends CustomView<T,TitleBlockItem> {


    public Set<CustomOpinionEvent> getEvent();

    public Set<OpinionMenu> getCustomMenu();


}
