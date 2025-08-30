package net.ooder.esd.dsm.enums;


import net.ooder.annotation.Enumstype;

public enum RangeType implements Enumstype {

    TABLE("表单内", "fa-solid fa-file-alt"),
    ENTITY("独立实体", "fa-solid fa-database"),
    ENTITYREF("关联实体", "fa-solid fa-link"),
    REF("关联表内", "fa-solid fa-link"),
    MODULE("模块内", "fa-solid fa-cubes"),
    PROJECT("工程内", "fa-solid fa-project-diagram"),
    USERSPACE("全局空间", "fa-solid fa-globe"),
    MODULEVIEW("逆向视图", "fa-solid fa-sitemap"),
    NONE("无", "fa-solid fa-ban");


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
