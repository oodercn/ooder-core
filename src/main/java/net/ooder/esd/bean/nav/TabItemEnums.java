package net.ooder.esd.bean.nav;


import net.ooder.esd.annotation.field.TabItem;

public enum TabItemEnums implements TabItem {
    a("page1", "fa-solid fa-th-large", false, false, false, null),
    b("page2", "fa-solid fa-history", false, false, false, null),
    c("page3", "fa-solid fa-bell", false, false, false, null),
    d("page4", "fa-solid fa-database", false, false, false, null);

    private String name;
    private String imageClass;
    private final Class[] bindClass;
    private final boolean iniFold;
    private final boolean dynDestory;
    private final boolean dynLoad;


    TabItemEnums(String name, String imageClass, boolean iniFold, boolean dynLoad, boolean dynDestory, Class... bindClass) {
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


}
