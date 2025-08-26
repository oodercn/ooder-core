package net.ooder.esd.bean.nav;


import net.ooder.esd.annotation.field.TabItem;
import net.ooder.esd.annotation.ui.FontColorEnum;
import net.ooder.esd.annotation.ui.IconColorEnum;
import net.ooder.esd.annotation.ui.ItemColorEnum;

public interface ButtonViewsItem extends TabItem {

    public boolean isCloseBtn();

    public boolean isPopBtn();

    public boolean isActiveLast();

    public IconColorEnum getIconColor();

    public ItemColorEnum getItemColor();

    public FontColorEnum getFontColor();
}
