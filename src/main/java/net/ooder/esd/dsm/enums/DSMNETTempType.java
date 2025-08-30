package net.ooder.esd.dsm.enums;


import net.ooder.annotation.Enumstype;

public enum DSMNETTempType implements Enumstype {
    custom("数据库应用", "fas fa-database"),
    weixin("小程序", "fas fa-mobile-alt"),
    process("流程模块", "fas fa-sitemap");


    private final String name;
    private final String imageClass;


    DSMNETTempType(String name, String imageClass) {
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
