package net.ooder.esd.bean.nav;


import net.ooder.esd.annotation.field.TabItem;

public enum TabItemEnums implements TabItem {
    a("page1", "ri-grid-line-large", false, false, false, null),
    b("page2", "ri-history-line", false, false, false, null),
    c("page3", "ri-notification-line", false, false, false, null),
    d("page4", "ri-database-line", false, false, false, null);

    private String name;
    private String imageClass;
    private final Class[] bindClass;
    private final boolean iniFold;
    private final boolean dynDestory;
    private final boolean dynLoad;
    private final String caption;
    private final String tips;

    TabItemEnums(String name, String imageClass, boolean iniFold, boolean dynLoad, boolean dynDestory, Class... bindClass) {
        this.name = name;
        this.imageClass = imageClass;
        this.bindClass = bindClass;
        this.iniFold = iniFold;
        this.dynLoad = dynLoad;
        this.dynDestory = dynDestory;
        this.caption = name;
        this.tips = name;

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }

    @Override
    public String getCaption() {
        return caption;
    }

    @Override
    public String getTips() {
        return tips;
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


}
