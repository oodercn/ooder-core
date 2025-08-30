package net.ooder.esd.dsm.domain.enums;


import net.ooder.annotation.Enumstype;
import net.ooder.esd.dsm.enums.DSMType;

public enum DomainType implements Enumstype {
    AGGREGATIONROOT("聚合根", "fas fa-project-diagram", DSMType.AGGREGATION),
    AGGREGATIONSET("聚合集合", "fas fa-layer-group", DSMType.AGGREGATION),
    VIEW("视图", "fas fa-eye", DSMType.AGGREGATION),
    AGGREGATIONENTITY("聚合实体", "fas fa-object-group", DSMType.AGGREGATION),

    BPM("流程", "fas fa-sitemap", DSMType.CUSTOMDOMAIN),
    MSG("消息", "fas fa-comment-alt", DSMType.CUSTOMDOMAIN),
    NAV("应用菜单", "fas fa-bars", DSMType.CUSTOMDOMAIN),
    VFS("文件应用", "fas fa-folder", DSMType.CUSTOMDOMAIN),
    ORG("组织权限", "fas fa-users", DSMType.CUSTOMDOMAIN),

    VO("实体对象", "fas fa-code-branch", DSMType.REPOSITORY),
    DO("库表映射", "fas fa-database", DSMType.REPOSITORY),
    REPOSITORY("仓储接口", "fas fa-warehouse", DSMType.REPOSITORY),
    REPOSITORYIMPL("仓储实现", "fas fa-tools", DSMType.REPOSITORY),
    ALL("所有", "fas fa-check-circle", DSMType.REPOSITORY);
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
