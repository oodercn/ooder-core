package net.ooder.esd.bean;


import net.ooder.annotation.Enumstype;

public enum CustomPanelMethod implements Enumstype {

    setChildren("setChildren", "动态装载"),
    destroy("destroy", "销毁模块"),
    setData("setData", "填充数据");

    private final String type;
    private final String name;

    CustomPanelMethod(String type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }



}
