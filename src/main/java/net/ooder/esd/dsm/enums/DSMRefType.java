package net.ooder.esd.dsm.enums;


import net.ooder.annotation.IconEnumstype;

public enum DSMRefType implements IconEnumstype {
    call("调用", "fa-solid fa-arrow-right"),
    source("源调用", "fa-solid fa-arrow-up-right"),
    dic("弹出字典", "fa-solid fa-book"),
    ref("引用", "fa-solid fa-link"),
    enums("枚举字典", "fa-solid fa-list-ul");

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
