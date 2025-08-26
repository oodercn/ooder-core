package net.ooder.esd.dsm.enums;


import net.ooder.annotation.Enumstype;

public enum TempPackageType implements Enumstype {
    base("基础模型"), local("本地实现"), remote("远程实现"), view("自定义视图");


    private final String name;


    TempPackageType(String name) {
        this.name = name;
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
