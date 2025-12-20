package net.ooder.esd.dsm.domain.enums;


import net.ooder.annotation.Enumstype;
import net.ooder.esd.dsm.enums.DSMType;

public enum DomainType implements Enumstype {
    AGGREGATIONROOT("聚合�?", "ri-node-tree", DSMType.AGGREGATION),
    AGGREGATIONSET("聚合集合", "ri-stack-line", DSMType.AGGREGATION),
    VIEW("视图", "ri-eye-line", DSMType.AGGREGATION),
    AGGREGATIONENTITY("聚合实体", "ri-object-group-line", DSMType.AGGREGATION),

    BPM("流程", "ri-node-tree", DSMType.CUSTOMDOMAIN),
    MSG("消息", "ri-chat-1-line", DSMType.CUSTOMDOMAIN),
    NAV("应用菜单", "ri-menu-line", DSMType.CUSTOMDOMAIN),
    VFS("文件应用", "ri-folder-line", DSMType.CUSTOMDOMAIN),
    ORG("组织权限", "ri-user-group-line", DSMType.CUSTOMDOMAIN),

    VO("实体对象", "ri-git-branch-line", DSMType.REPOSITORY),
    DO("库表映射", "ri-database-line", DSMType.REPOSITORY),
    REPOSITORY("仓储接口", "ri-warehouse-line", DSMType.REPOSITORY),
    REPOSITORYIMPL("仓储实现", "ri-tools-line", DSMType.REPOSITORY),
    ALL("全部", "ri-checkbox-circle-line", DSMType.REPOSITORY);
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
