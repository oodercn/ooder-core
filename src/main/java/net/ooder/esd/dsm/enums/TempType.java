package net.ooder.esd.dsm.enums;


import net.ooder.annotation.Enumstype;
import net.ooder.esd.dsm.domain.enums.DomainType;

public enum TempType implements Enumstype {

    FRAME("框架", "fa-solid fa-layer-group", DomainType.VIEW),
    NAVVIEW("导航", "fa-solid fa-bars", DomainType.VIEW),
    LAYOUT("布局", "fa-solid fa-th", DomainType.VIEW),
    GRID("列表", "fa-solid fa-table", DomainType.VIEW),
    GALLERY("画廊", "fa-solid fa-images", DomainType.VIEW),
    FORM("表单", "fa-solid fa-file-alt", DomainType.VIEW),
    TREE("树形", "fa-solid fa-sitemap", DomainType.VIEW),
    CHARTS("统计图表", "fa-solid fa-chart-bar", DomainType.VIEW),
    SVG("SVG绘图", "fa-solid fa-drafting-compass", DomainType.VIEW),
    MODULE("内嵌模块", "fa-solid fa-cubes", DomainType.VIEW),
    DIC("字典辅助", "fa-solid fa-book", DomainType.VIEW),

    ROUTE("流转域", "fa-solid fa-exchange-alt", DomainType.BPM),
    WORKLIST("查询域", "fa-solid fa-search", DomainType.BPM),
    BPMFORM("表单域", "fa-solid fa-file-alt", DomainType.BPM),
    ADMIN("管理域", "fa-solid fa-tools", DomainType.BPM),
    STATISTICS("统计域", "fa-solid fa-chart-line", DomainType.BPM),
    BPMRIGHT("权限域", "fa-solid fa-key", DomainType.BPM),
    CUSTOM("通用输入域", "fa-solid fa-cogs", DomainType.BPM),
    EVENT("事件", "fa-solid fa-bell", DomainType.BPM),
    OTHER("其他", "fa-solid fa-question-circle", DomainType.BPM),

    MQTT("Mqtt消息", "fa-solid fa-comment-alt", DomainType.MSG),
    APICALL("接口", "fa-solid fa-plug", DomainType.MSG),
    COMMAND("命令", "fa-solid fa-terminal", DomainType.MSG),


    NAVMODULE("模块", "fa-solid fa-cubes", DomainType.NAV),
    MENU("菜单", "fa-solid fa-bars", DomainType.NAV),
    NAV("导航布局", "fa-solid fa-th-large", DomainType.BPM),


    ORG("组织机构", "fa-solid fa-users", DomainType.NAV),
    ROLE("角色", "fa-solid fa-user-tag", DomainType.NAV),
    PERSON("成员", "fa-solid fa-user", DomainType.NAV),
    RIGHT("权限", "fa-solid fa-key", DomainType.NAV),

    FILE("文件", "fa-solid fa-file", DomainType.NAV),
    FOLDER("文件夹", "fa-solid fa-folder", DomainType.NAV),
    COMPONENT("组件", "fa-solid fa-puzzle-piece", DomainType.NAV),


    VO("实体对象", "fa-solid fa-object-group", DomainType.VO),
    DO("库表映射", "fa-solid fa-database", DomainType.DO),
    REPOSITORY("仓储接口", "fa-solid fa-warehouse", DomainType.REPOSITORY),
    REPOSITORYIMPL("仓储实现", "fa-solid fa-tools", DomainType.REPOSITORYIMPL),


    ALL("所有", "fa-solid fa-check-circle", DomainType.ALL);


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
