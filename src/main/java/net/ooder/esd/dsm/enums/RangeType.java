package net.ooder.esd.dsm.enums;


import net.ooder.annotation.Enumstype;

public enum RangeType implements Enumstype {

    TABLE("表单内", "fas fa-file-alt"),
    ENTITY("独立实体", "fas fa-database"),
    ENTITYREF("关联实体", "fas fa-link"),
    REF("关联表内", "fas fa-link"),
    MODULE("模块内", "fas fa-cubes"),
    PROJECT("工程内", "fas fa-project-diagram"),
    USERSPACE("全局空间", "fas fa-globe"),
    MODULEVIEW("逆向视图", "fas fa-sitemap"),
    NONE("无", "fas fa-ban");


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
