package net.ooder.esd.dsm.enums;


import net.ooder.annotation.IconEnumstype;

public enum DSMRefType implements IconEnumstype {
    call("调用", "spafont spa-icon-function"),
    source("源调用", "spafont spa-icon-function"),
    dic("弹出字典", "spafont spa-icon-shukongjian"),
    ref("引用", "spafont spa-icon-c-databinder"),
    enums("枚举字典", "spafont spa-icon-c-menu");

    private final String imageClass;
    private final String name;

    DSMRefType(String name, String imageClass) {
        this.name = name;
        this.imageClass = imageClass;
    }

    public String getImageClass() {
        return imageClass;
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
