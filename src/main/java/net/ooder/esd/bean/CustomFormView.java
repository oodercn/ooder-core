package net.ooder.esd.bean;


import net.ooder.esd.annotation.event.CustomFormEvent;
import net.ooder.esd.annotation.menu.CustomFormMenu;
import net.ooder.esd.dsm.view.field.ESDFieldConfig;
import net.ooder.esd.tool.properties.item.UIItem;

import java.util.Set;

public interface CustomFormView<T extends ESDFieldConfig> extends CustomView<T,UIItem> {


    public Set<CustomFormEvent> getEvent();

    public Set<CustomFormMenu> getCustomMenu();

    public Set<CustomFormMenu> getCustomBottombar();


}
