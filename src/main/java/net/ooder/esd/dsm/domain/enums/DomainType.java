package net.ooder.esd.dsm.domain.enums;


import net.ooder.annotation.Enumstype;
import net.ooder.esd.dsm.enums.DSMType;

public enum DomainType implements Enumstype {
    AGGREGATIONROOT("聚合根", "fa-solid fa-project-diagram", DSMType.AGGREGATION),
    AGGREGATIONSET("聚合集合", "fa-solid fa-layer-group", DSMType.AGGREGATION),
    VIEW("视图", "fa-solid fa-eye", DSMType.AGGREGATION),
    AGGREGATIONENTITY("聚合实体", "fa-solid fa-object-group", DSMType.AGGREGATION),

    BPM("流程", "fa-solid fa-sitemap", DSMType.CUSTOMDOMAIN),
    MSG("消息", "fa-solid fa-comment-alt", DSMType.CUSTOMDOMAIN),
    NAV("应用菜单", "fa-solid fa-bars", DSMType.CUSTOMDOMAIN),
    VFS("文件应用", "fa-solid fa-folder", DSMType.CUSTOMDOMAIN),
    ORG("组织权限", "fa-solid fa-users", DSMType.CUSTOMDOMAIN),

    VO("实体对象", "fa-solid fa-code-branch", DSMType.REPOSITORY),
    DO("库表映射", "fa-solid fa-database", DSMType.REPOSITORY),
    REPOSITORY("仓储接口", "fa-solid fa-warehouse", DSMType.REPOSITORY),
    REPOSITORYIMPL("仓储实现", "fa-solid fa-tools", DSMType.REPOSITORY),
    ALL("所有", "fa-solid fa-check-circle", DSMType.REPOSITORY);
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
