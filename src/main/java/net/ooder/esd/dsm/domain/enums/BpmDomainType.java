package net.ooder.esd.dsm.domain.enums;


import net.ooder.annotation.IconEnumstype;

public enum BpmDomainType implements IconEnumstype {
    ROUTE("流转域", "ri-exchange-line"),
    WORKLIST("查询域", "ri-search-line"),
    FORM("表单域", "ri-file-line"),
    ADMIN("管理域", "ri-settings-3-line"),
    STATISTICS("统计域", "ri-line-chart-line"),
    BPMRIGHT("权限域", "ri-key-line"),
    CUSTOM("通用输入域", "ri-keyboard-line"),
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
