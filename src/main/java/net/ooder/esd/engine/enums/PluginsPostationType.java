package net.ooder.esd.engine.enums;


import net.ooder.annotation.Enumstype;

public enum PluginsPostationType implements Enumstype {

    topBar("工具栏"), topMenu("菜单栏"), reightPanelConfig("右侧配置栏"), rightPopMenu("右键弹出菜单");

    private final String name;



    PluginsPostationType(String name) {
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
