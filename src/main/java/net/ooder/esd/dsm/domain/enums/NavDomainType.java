package net.ooder.esd.dsm.domain.enums;


import net.ooder.annotation.IconEnumstype;

public enum NavDomainType implements IconEnumstype {
    MODULE("模块", "ri-puzzle-line"),
    MENU("菜单", "ri-menu-line"),
    NAV("导航布局", "ri-node-tree");


    private final String name;
    private final String imageClass;


    NavDomainType(String name, String imageClass) {
        this.name = name;
        this.imageClass = imageClass;
    }

    public static NavDomainType fromType(String type) {
        NavDomainType defaultViewType = MODULE;
        if (type != null) {
            for (NavDomainType viewType : NavDomainType.values()) {
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
