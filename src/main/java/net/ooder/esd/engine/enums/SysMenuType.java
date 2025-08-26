package net.ooder.esd.engine.enums;


import net.ooder.annotation.Enumstype;
import net.ooder.esd.bean.bar.DynBar;

public enum SysMenuType implements Enumstype {

    RADTopMenu("调试菜单"),
    RADTopToolsBar("调试工具条"),
    CustomMenusBar("菜单栏"),
    CustomToolsBar("工具栏"),
    CustomListBar("弹出菜单"),
    CustomContextBar("右键菜单"),
    CustomSubBar("子菜单"),
    CustomBottomBar("底部栏"),
    RouteInstBottomBar("流程控制栏"),
    RouteInstToolBar("流程控制栏");

    private String name;

    <T extends DynBar> SysMenuType(String name) {
        this.name = name;
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
