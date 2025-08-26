package net.ooder.esd.dsm.enums;


import net.ooder.annotation.Enumstype;
import net.ooder.esd.dsm.domain.enums.DomainType;

public enum TempType implements Enumstype {

    FRAME("框架", "spafont spa-icon-c-databinder", DomainType.VIEW),
    NAVVIEW("导航", "spafont spa-icon-shukongjian", DomainType.VIEW),
    LAYOUT("布局", "spafont spa-icon-c-buttonviews", DomainType.VIEW),
    GRID("列表", "spafont spa-icon-c-grid", DomainType.VIEW),
    GALLERY("画廊", "spafont spa-icon-c-iconslist", DomainType.VIEW),
    FORM("表单", "spafont spa-icon-c-richeditor", DomainType.VIEW),
    TREE("树形", "spafont spafont spa-icon-c-treeview", DomainType.VIEW),
    CHARTS("统计图表", "ood-uicmd-location", DomainType.VIEW),
    SVG("SVG绘图", "spafont spa-icon-editpath", DomainType.VIEW),
    MODULE("内嵌模块", "spafont spa-icon-conf", DomainType.VIEW),
    DIC("字典辅助", "spafont spa-icon-function", DomainType.VIEW),

    ROUTE("流转域", "spafont spa-icon-c-buttonviews", DomainType.BPM),
    WORKLIST("查询域", "spafont spa-icon-shukongjian", DomainType.BPM),
    BPMFORM("表单域", "spafont spa-icon-c-grid", DomainType.BPM),
    ADMIN("管理域", "spafont spa-icon-c-grid", DomainType.BPM),
    STATISTICS("统计域", "spafont spa-icon-shukongjian", DomainType.BPM),
    BPMRIGHT("权限域", "spafont spa-icon-c-iconslist", DomainType.BPM),
    CUSTOM("通用输入域", "spafont spa-icon-conf", DomainType.BPM),
    EVENT("事件", "spafont spafont spa-icon-c-treeview", DomainType.BPM),
    OTHER("其他", "spafont spa-icon-empty", DomainType.BPM),

    MQTT("Mqtt消息", "spafont spa-icon-c-buttonviews", DomainType.MSG),
    APICALL("接口", "spafont spa-icon-shukongjian", DomainType.MSG),
    COMMAND("命令", "spafont spa-icon-c-grid", DomainType.MSG),


    NAVMODULE("模块", "spafont spa-icon-c-buttonviews", DomainType.NAV),
    MENU("菜单", "spafont spa-icon-shukongjian", DomainType.NAV),
    NAV("导航布局", "spafont spa-icon-c-grid", DomainType.BPM),


    ORG("组织机构", "spafont spa-icon-c-buttonviews", DomainType.NAV),
    ROLE("角色", "spafont spa-icon-shukongjian", DomainType.NAV),
    PERSON("成员", "spafont spa-icon-c-grid", DomainType.NAV),
    RIGHT("权限", "spafont spa-icon-shukongjian", DomainType.NAV),

    FILE("文件", "spafont spa-icon-c-buttonviews", DomainType.NAV),
    FOLDER("文件夹", "spafont spa-icon-shukongjian", DomainType.NAV),
    COMPONENT("组件", "spafont spa-icon-c-grid", DomainType.NAV),


    VO("实体对象", "spafont spa-icon-c-webapi", DomainType.VO),
    DO("库表映射", "spafont spa-icon-paste", DomainType.DO),
    REPOSITORY("仓储接口", "spafont spa-icon-c-webapi", DomainType.REPOSITORY),
    REPOSITORYIMPL("仓储实现", "spafont spa-icon-tools", DomainType.REPOSITORYIMPL),


    ALL("所有", "spafont spa-icon-empty", DomainType.ALL);


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
