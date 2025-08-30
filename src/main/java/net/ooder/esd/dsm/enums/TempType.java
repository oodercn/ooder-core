package net.ooder.esd.dsm.enums;


import net.ooder.annotation.Enumstype;
import net.ooder.esd.dsm.domain.enums.DomainType;

public enum TempType implements Enumstype {

    FRAME("框架", "fas fa-layer-group", DomainType.VIEW),
    NAVVIEW("导航", "fas fa-bars", DomainType.VIEW),
    LAYOUT("布局", "fas fa-th", DomainType.VIEW),
    GRID("列表", "fas fa-table", DomainType.VIEW),
    GALLERY("画廊", "fas fa-images", DomainType.VIEW),
    FORM("表单", "fas fa-file-alt", DomainType.VIEW),
    TREE("树形", "fas fa-sitemap", DomainType.VIEW),
    CHARTS("统计图表", "fas fa-chart-bar", DomainType.VIEW),
    SVG("SVG绘图", "fas fa-drafting-compass", DomainType.VIEW),
    MODULE("内嵌模块", "fas fa-cubes", DomainType.VIEW),
    DIC("字典辅助", "fas fa-book", DomainType.VIEW),

    ROUTE("流转域", "fas fa-exchange-alt", DomainType.BPM),
    WORKLIST("查询域", "fas fa-search", DomainType.BPM),
    BPMFORM("表单域", "fas fa-file-alt", DomainType.BPM),
    ADMIN("管理域", "fas fa-tools", DomainType.BPM),
    STATISTICS("统计域", "fas fa-chart-line", DomainType.BPM),
    BPMRIGHT("权限域", "fas fa-key", DomainType.BPM),
    CUSTOM("通用输入域", "fas fa-cogs", DomainType.BPM),
    EVENT("事件", "fas fa-bell", DomainType.BPM),
    OTHER("其他", "fas fa-question-circle", DomainType.BPM),

    MQTT("Mqtt消息", "fas fa-comment-alt", DomainType.MSG),
    APICALL("接口", "fas fa-plug", DomainType.MSG),
    COMMAND("命令", "fas fa-terminal", DomainType.MSG),


    NAVMODULE("模块", "fas fa-cubes", DomainType.NAV),
    MENU("菜单", "fas fa-bars", DomainType.NAV),
    NAV("导航布局", "fas fa-th-large", DomainType.BPM),


    ORG("组织机构", "fas fa-users", DomainType.NAV),
    ROLE("角色", "fas fa-user-tag", DomainType.NAV),
    PERSON("成员", "fas fa-user", DomainType.NAV),
    RIGHT("权限", "fas fa-key", DomainType.NAV),

    FILE("文件", "fas fa-file", DomainType.NAV),
    FOLDER("文件夹", "fas fa-folder", DomainType.NAV),
    COMPONENT("组件", "fas fa-puzzle-piece", DomainType.NAV),


    VO("实体对象", "fas fa-object-group", DomainType.VO),
    DO("库表映射", "fas fa-database", DomainType.DO),
    REPOSITORY("仓储接口", "fas fa-warehouse", DomainType.REPOSITORY),
    REPOSITORYIMPL("仓储实现", "fas fa-tools", DomainType.REPOSITORYIMPL),


    ALL("所有", "fas fa-check-circle", DomainType.ALL);


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
