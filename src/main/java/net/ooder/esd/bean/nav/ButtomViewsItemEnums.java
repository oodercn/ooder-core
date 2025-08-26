package net.ooder.esd.bean.nav;


import net.ooder.esd.annotation.ui.FontColorEnum;
import net.ooder.esd.annotation.ui.IconColorEnum;
import net.ooder.esd.annotation.ui.ItemColorEnum;

public enum ButtomViewsItemEnums implements ButtonViewsItem {
    a("page1", "xui-icon-xui", false, false, false, Void.class),
    b("page2", "dsmfont dsm-huitiaojilu", false, false, false, Void.class),
    c("page3", "iotfont iotmenling", false, false, false, Void.class),
    d("page4", "dsmfont dsm-shujuyuan", false, false, false, Void.class);


    private String name;
    private String imageClass;
    private final Class[] bindClass;
    private final boolean iniFold;
    private final boolean dynDestory;
    private final boolean dynLoad;

    public boolean closeBtn;
    public boolean popBtn;
    public boolean activeLast;
    public IconColorEnum iconColor;
    public ItemColorEnum itemColor;
    public FontColorEnum fontColor;

    ButtomViewsItemEnums(String name, String imageClass, boolean iniFold, boolean dynLoad, boolean dynDestory, boolean closeBtn, boolean popBtn, boolean activeLast, Class... bindClass) {
        this.name = name;
        this.imageClass = imageClass;
        this.bindClass = bindClass;
        this.iniFold = iniFold;
        this.dynLoad = dynLoad;
        this.dynDestory = dynDestory;
        this.closeBtn = closeBtn;
        this.popBtn = popBtn;
        this.activeLast = activeLast;

    }


    ButtomViewsItemEnums(String name, String imageClass, boolean iniFold, boolean dynLoad, boolean dynDestory, Class... bindClass) {
        this.name = name;
        this.imageClass = imageClass;
        this.bindClass = bindClass;
        this.iniFold = iniFold;
        this.dynLoad = dynLoad;
        this.dynDestory = dynDestory;

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }


    @Override
    public String getType() {
        return name();
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean isIniFold() {
        return iniFold;
    }

    @Override
    public boolean isDynDestory() {
        return dynDestory;
    }

    @Override
    public boolean isDynLoad() {
        return dynLoad;
    }

    public Class[] getBindClass() {
        return bindClass;
    }

    public String getImageClass() {
        return imageClass;
    }

    @Override
    public boolean isCloseBtn() {
        return closeBtn;
    }

    public void setCloseBtn(boolean closeBtn) {
        this.closeBtn = closeBtn;
    }

    @Override
    public boolean isPopBtn() {
        return popBtn;
    }

    public void setPopBtn(boolean popBtn) {
        this.popBtn = popBtn;
    }

    @Override
    public boolean isActiveLast() {
        return activeLast;
    }

    public void setActiveLast(boolean activeLast) {
        this.activeLast = activeLast;
    }

    @Override
    public IconColorEnum getIconColor() {
        return iconColor;
    }

    public void setIconColor(IconColorEnum iconColor) {
        this.iconColor = iconColor;
    }

    @Override
    public ItemColorEnum getItemColor() {
        return itemColor;
    }

    public void setItemColor(ItemColorEnum itemColor) {
        this.itemColor = itemColor;
    }

    @Override
    public FontColorEnum getFontColor() {
        return fontColor;
    }

    public void setFontColor(FontColorEnum fontColor) {
        this.fontColor = fontColor;
    }
}
