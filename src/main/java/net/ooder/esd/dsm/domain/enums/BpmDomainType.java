package net.ooder.esd.dsm.domain.enums;


import net.ooder.annotation.IconEnumstype;

public enum BpmDomainType implements IconEnumstype {
    ROUTE("流转域", "fas fa-exchange-alt"),
    WORKLIST("查询域", "fas fa-search"),
    FORM("表单域", "fas fa-file-alt"),
    ADMIN("管理域", "fas fa-cogs"),
    STATISTICS("统计域", "fas fa-chart-line"),
    BPMRIGHT("权限域", "fas fa-key"),
    CUSTOM("通用输入域", "fas fa-keyboard"),
    EVENT("事件", "fas fa-bell");

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
