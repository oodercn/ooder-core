package net.ooder.esd.dsm.domain.enums;


import net.ooder.annotation.Enumstype;
import net.ooder.esd.dsm.enums.DSMType;

public enum DomainType implements Enumstype {
    AGGREGATIONROOT("聚合根", "spafont spa-icon-shukongjian", DSMType.AGGREGATION),
    AGGREGATIONSET("聚合集合", "spafont spa-icon-c-grid", DSMType.AGGREGATION),
    VIEW("视图", "spafont spa-icon-c-grid", DSMType.AGGREGATION),
    AGGREGATIONENTITY("聚合实体", "spafont spa-icon-function", DSMType.AGGREGATION),

    BPM("流程", "spafont spa-icon-shukongjian", DSMType.CUSTOMDOMAIN),
    MSG("消息", "spafont spa-icon-c-grid", DSMType.CUSTOMDOMAIN),
    NAV("应用菜单", "spafont spa-icon-c-grid", DSMType.CUSTOMDOMAIN),
    VFS("文件应用", "spafont spa-icon-c-grid", DSMType.CUSTOMDOMAIN),
    ORG("组织权限", "spafont spa-icon-function", DSMType.CUSTOMDOMAIN),

    VO("实体对象", "spafont spa-icon-c-webapi", DSMType.REPOSITORY),
    DO("库表映射", "spafont spa-icon-paste", DSMType.REPOSITORY),
    REPOSITORY("仓储接口", "spafont spa-icon-c-webapi", DSMType.REPOSITORY),
    REPOSITORYIMPL("仓储实现", "spafont spa-icon-tools", DSMType.REPOSITORY),
    ALL("所有", "spafont spa-icon-empty", DSMType.REPOSITORY);
    private final String name;
    private final String imageClass;
    private final DSMType dsmType;


    DomainType(String name, String imageClass, DSMType dsmType) {
        this.name = name;
        this.imageClass = imageClass;
        this.dsmType = dsmType;
    }

    public static DomainType fromType(String type) {
        DomainType defaultViewType = AGGREGATIONROOT;
        if (type != null) {
            for (DomainType viewType : DomainType.values()) {
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
