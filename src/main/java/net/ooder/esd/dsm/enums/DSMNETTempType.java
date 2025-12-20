package net.ooder.esd.dsm.enums;


import net.ooder.annotation.Enumstype;

public enum DSMNETTempType implements Enumstype {
    custom("数据库应�?", "ri-database-line"),
    weixin("小程�?", "ri-smartphone-line"),
    process("流程模块", "ri-node-tree");


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
