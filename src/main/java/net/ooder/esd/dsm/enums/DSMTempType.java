package net.ooder.esd.dsm.enums;


import net.ooder.annotation.IconEnumstype;

public enum DSMTempType implements IconEnumstype {
    dao("通用数据库模板", "fas fa-database"),
    custom("通用视图模板", "fas fa-layer-group"),
    process("业务流程模板", "fas fa-sitemap"),
    statistics("统计分析模板", "fas fa-chart-line"),
    cms("门户网站模板", "fas fa-globe"),
    mobile("移动应用模板", "fas fa-mobile-alt");


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
