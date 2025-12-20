package net.ooder.esd.dsm.domain.enums;


import net.ooder.annotation.IconEnumstype;

public enum CustomDomainType implements IconEnumstype {
    BPM("流程", "ri-node-tree"),
    MSG("消息", "ri-table-line"),
    NAV("应用菜单", "ri-table-line"),
    VFS("文件应用", "ri-table-line"),
    ORG("组织权限", "ri-settings-3-line");


    private final String name;
    private final String imageClass;


    CustomDomainType(String name, String imageClass) {
        this.name = name;
        this.imageClass = imageClass;
    }

    public static CustomDomainType fromType(String type) {
        CustomDomainType defaultViewType = MSG;
        if (type != null) {
            for (CustomDomainType viewType : CustomDomainType.values()) {
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
