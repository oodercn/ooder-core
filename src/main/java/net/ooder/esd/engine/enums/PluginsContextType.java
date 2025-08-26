package net.ooder.esd.engine.enums;


import net.ooder.annotation.Enumstype;

public enum PluginsContextType implements Enumstype {

    all("所有"), xuiPackage("文件夹"), xuiModule("模块"), api("服务调用"), action("动作"), innerModule("内部模块"), component("组件");

    private final String name;


    PluginsContextType(String name) {
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
