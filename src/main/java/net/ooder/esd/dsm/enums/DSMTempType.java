package net.ooder.esd.dsm.enums;


import net.ooder.annotation.IconEnumstype;

public enum DSMTempType implements IconEnumstype {
    dao("通用数据库模板", "iconfont iconchucun"),
    custom("通用视图模板", "spafont spa-icon-module"),
    process("业务流程模板", "spafont spa-icon-shukongjian"),
    statistics("统计分析模板", "spafont spa-icon-values"),
    cms("门户网站模板", "spafont spa-icon-links"),
    mobile("移动应用模板", "ood-icon-mobile");


    private final String name;
    private final String imageClass;


    DSMTempType(String name, String imageClass) {
        this.name = name;
        this.imageClass = imageClass;

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

    public String getImageClass() {
        return imageClass;
    }

}
