package net.ooder.esd.dsm.enums;


import net.ooder.annotation.Enumstype;
import net.ooder.esd.dsm.domain.enums.DomainType;

public enum TempType implements Enumstype {

    FRAME("框架", "ri-stack-line", DomainType.VIEW),
    NAVVIEW("导航", "ri-menu-line", DomainType.VIEW),
    LAYOUT("布局", "ri-grid-line", DomainType.VIEW),
    GRID("列表", "ri-table-line", DomainType.VIEW),
    GALLERY("画廊", "ri-image-line", DomainType.VIEW),
    FORM("表单", "ri-file-line", DomainType.VIEW),
    TREE("树形", "ri-node-tree", DomainType.VIEW),
    CHARTS("统计图表", "ri-bar-chart-line", DomainType.VIEW),
    SVG("SVG绘图", "ri-compass-line", DomainType.VIEW),
    MODULE("内嵌模块", "ri-cube-line", DomainType.VIEW),
    DIC("字典辅助", "ri-book-line", DomainType.VIEW),

    ROUTE("流转域", "ri-exchange-line", DomainType.BPM),
    WORKLIST("查询域", "ri-search-line", DomainType.BPM),
    BPMFORM("表单域", "ri-file-line", DomainType.BPM),
    ADMIN("管理域", "ri-tools-line", DomainType.BPM),
    STATISTICS("统计域", "ri-line-chart-line", DomainType.BPM),
    BPMRIGHT("权限域", "ri-key-line", DomainType.BPM),
    CUSTOM("通用输入域", "ri-settings-3-line", DomainType.BPM),
    EVENT("事件", "ri-notification-line", DomainType.BPM),
    OTHER("其他", "ri-question-mark-circle-line", DomainType.BPM),

    MQTT("Mqtt消息", "ri-chat-1-line", DomainType.MSG),
    APICALL("接口", "ri-plug-line", DomainType.MSG),
    COMMAND("命令", "ri-terminal-box-line", DomainType.MSG),


    NAVMODULE("模块", "ri-cube-line", DomainType.NAV),
    MENU("菜单", "ri-menu-line", DomainType.NAV),
    NAV("导航布局", "ri-grid-large-line", DomainType.BPM),


    ORG("组织机构", "ri-user-group-line", DomainType.NAV),
    ROLE("角色", "ri-user-tag-line", DomainType.NAV),
    PERSON("成员", "ri-user-line", DomainType.NAV),
    RIGHT("权限", "ri-key-line", DomainType.NAV),

    FILE("文件", "ri-file-line", DomainType.NAV),
    FOLDER("文件夹", "ri-folder-line", DomainType.NAV),
    COMPONENT("组件", "ri-puzzle-line", DomainType.NAV),


    VO("实体对象", "ri-object-group-line", DomainType.VO),
    DO("库表映射", "ri-database-line", DomainType.DO),
    REPOSITORY("仓储接口", "ri-warehouse-line", DomainType.REPOSITORY),
    REPOSITORYIMPL("仓储实现", "ri-tools-line", DomainType.REPOSITORYIMPL),


    ALL("所有", "ri-checkbox-circle-line", DomainType.ALL);


    private final String name;
    private final String imageClass;
    private final DomainType domainType;


    TempType(String name, String imageClass, DomainType domainType) {
        this.name = name;
        this.imageClass = imageClass;
        this.domainType = domainType;
    }

    public static TempType fromType(String type) {
        TempType defaultTempType = ALL;
        if (type != null) {
            for (TempType tempType : TempType.values()) {
                if (tempType.getType().equals(type)) {
                    defaultTempType = tempType;
                }
            }
        }

        return defaultTempType;
    }

    public DomainType getDomainType() {
        return domainType;
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
