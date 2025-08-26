package net.ooder.esd.dsm.domain.enums;


import net.ooder.annotation.IconEnumstype;

public enum VfsDomainType implements IconEnumstype {
    file("文件", "spafont spa-icon-c-buttonviews"),
    folder("文件夹", "spafont spa-icon-shukongjian"),
    component("组件", "spafont spa-icon-c-grid"),
    all("所有", "spafont spa-icon-empty");


    private final String name;
    private final String imageClass;


    VfsDomainType(String name, String imageClass) {
        this.name = name;
        this.imageClass = imageClass;
    }

    public static VfsDomainType fromType(String type) {
        VfsDomainType defaultViewType = all;
        if (type != null) {
            for (VfsDomainType viewType : VfsDomainType.values()) {
                if (viewType.getType().equals(type)) {
                    defaultViewType = viewType;
                }
            }
        }

        return defaultViewType;
    }

    public String getImageClass() {
        return imageClass;
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public String getType() {
        return name();
    }

    @Override
    public String getName() {
        return name;
    }
}
