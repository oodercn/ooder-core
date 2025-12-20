package net.ooder.esd.dsm.enums;


import net.ooder.annotation.Enumstype;

public enum RangeType implements Enumstype {

    TABLE("表单内", "ri-file-line"),
    ENTITY("独立实体", "ri-database-line"),
    ENTITYREF("关联实体", "ri-link-line"),
    REF("关联表内", "ri-link-line"),
    MODULE("模块内", "ri-cube-line"),
    PROJECT("工程内", "ri-node-tree"),
    USERSPACE("全局空间", "ri-earth-line"),
    MODULEVIEW("逆向视图", "ri-node-tree"),
    NONE("无", "ri-close-circle-line");


    private final String name;

    private final String imageClass;


    RangeType(String name, String imageClass) {
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
}
