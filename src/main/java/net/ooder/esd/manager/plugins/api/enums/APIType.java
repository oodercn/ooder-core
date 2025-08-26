package net.ooder.esd.manager.plugins.api.enums;


import net.ooder.annotation.Enumstype;

public enum APIType implements Enumstype {
    local("本地API"), system("系统API"), otherapi("其他API"), cluster("远程调用"), userdef("用户自定义"), all("所有API");


    private final String name;


    APIType(String name) {
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
