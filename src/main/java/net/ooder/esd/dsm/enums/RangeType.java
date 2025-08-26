package net.ooder.esd.dsm.enums;


import net.ooder.annotation.Enumstype;

public enum RangeType implements Enumstype {

    TABLE("表单内", "spafont spa-icon-previewpage"),
    ENTITY("独立实体", "spafont spa-icon-previewpage"),
    ENTITYREF("关联实体", "spafont spa-icon-alignwh"),
    REF("关联表内", "spafont spa-icon-alignwh"),
    MODULE("模块内", "spafont spa-icon-explore"),
    PROJECT("工程内", "spafont spa-icon-app"),
    USERSPACE("全局空间", "spafont spa-icon-aligncell"),
    MODULEVIEW("逆向视图", "spafont spa-icon-designview"),
    NONE("无", "spafont spa-icon-empty");


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
