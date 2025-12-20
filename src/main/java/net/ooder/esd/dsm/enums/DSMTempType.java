package net.ooder.esd.dsm.enums;


import net.ooder.annotation.IconEnumstype;

public enum DSMTempType implements IconEnumstype {
    dao("通用数据库模板", "ri-database-line"),
    custom("通用视图模板", "ri-stack-line"),
    process("业务流程模板", "ri-node-tree"),
    statistics("统计分析模板", "ri-line-chart-line"),
    cms("门户网站模板", "ri-earth-line"),
    mobile("移动应用模板", "ri-smartphone-line");


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
