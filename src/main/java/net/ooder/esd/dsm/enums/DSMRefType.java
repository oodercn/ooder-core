package net.ooder.esd.dsm.enums;


import net.ooder.annotation.IconEnumstype;

public enum DSMRefType implements IconEnumstype {
    call("调用", "ri-arrow-right-line"),
    source("源调用", "ri-arrow-up-right-line"),
    dic("弹出字典", "ri-book-line"),
    ref("引用", "ri-link-line"),
    enums("枚举字典", "ri-list-unordered-line");

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
