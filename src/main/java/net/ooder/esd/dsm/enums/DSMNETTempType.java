package net.ooder.esd.dsm.enums;


import net.ooder.annotation.Enumstype;

public enum DSMNETTempType implements Enumstype {
    custom("数据库应用", "spsafont spa-icon-module"),
    weixin("小程序", "spafont spa-icon-links"),
    process("流程模块", "spafont spa-icon-shukongjian");


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
