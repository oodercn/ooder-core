package net.ooder.esd.dsm.domain.enums;


import net.ooder.annotation.IconEnumstype;

public enum BpmDomainType implements IconEnumstype {
    ROUTE("流转�?", "ri-exchange-line"),
    WORKLIST("查询�?", "ri-search-line"),
    FORM("表单�?", "ri-file-line"),
    ADMIN("管理�?", "ri-settings-3-line"),
    STATISTICS("统计�?", "ri-line-chart-line"),
    BPMRIGHT("权限�?", "ri-key-line"),
    CUSTOM("通用输入�?", "ri-key-lineboard"),
    EVENT("事件", "ri-notification-line");

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
