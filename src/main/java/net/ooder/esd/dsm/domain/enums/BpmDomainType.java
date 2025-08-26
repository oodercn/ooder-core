package net.ooder.esd.dsm.domain.enums;


import net.ooder.annotation.IconEnumstype;

public enum BpmDomainType implements IconEnumstype {
    ROUTE("流转域", "spafont spa-icon-c-buttonviews"),
    WORKLIST("查询域", "spafont spa-icon-shukongjian"),
    FORM("表单域", "spafont spa-icon-c-grid"),
    ADMIN("管理域", "spafont spa-icon-c-grid"),
    STATISTICS("统计域", "spafont spa-icon-shukongjian"),
    BPMRIGHT("权限域", "spafont spa-icon-c-iconslist"),
    CUSTOM("通用输入域", "spafont spa-icon-conf"),
    EVENT("事件", "spafont spafont spa-icon-c-treeview");


    private final String name;
    private final String imageClass;


    BpmDomainType(String name, String imageClass) {
        this.name = name;
        this.imageClass = imageClass;

    }

    public static BpmDomainType fromType(String type) {
        BpmDomainType defaultViewType = EVENT;
        if (type != null) {
            for (BpmDomainType viewType : BpmDomainType.values()) {
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
