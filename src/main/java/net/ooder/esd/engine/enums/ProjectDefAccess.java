package net.ooder.esd.engine.enums;


import net.ooder.annotation.Enumstype;

public enum ProjectDefAccess implements Enumstype {

    Public("Public", "独立启动", "userspace", "", PackageType.userdef, PackageType.custom),

    DSM("Dsm", "DSM建模", "dsm", "dsm", PackageType.dsm),

    sys("sys", "系统服务", "sys", "sys", PackageType.sys, PackageType.cluster),

    admin("admin", "管理端服务", "admin", "admin", PackageType.admin, PackageType.esd, PackageType.tool),

    ExtCom("ExtCom", "扩展组件工程", "extcomspace", "ExtCom"),

    Component("Component", "通用组件库工程", "componentspace", "COMPONENT"),

    Private("Private", "子工程", "Private", "PRIVATE"),

    Resource("Resource", "资源目录", "resoucespace", "RESOURCE"),

    Templat("Templat", "模板工程", "tempspace", "TEMP"),

    Module("Module", "嵌入式模块", "modulespacce", "MODULE");

    private final String path;
    private final String tag;
    private String type;
    private String name;
    private PackageType[] packageTypes;

    ProjectDefAccess(String type, String name, String path, String tag, PackageType... packageTypes) {
        this.type = type;
        this.name = name;
        this.path = path;
        this.tag = tag;
        this.packageTypes = packageTypes;

    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }


    public String getPath() {
        return path;
    }

    public String getTag() {
        return tag;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return type;
    }

    public static ProjectDefAccess fromType(String typeName) {

        for (ProjectDefAccess type : ProjectDefAccess.values()) {
            if (type.getType().equals(typeName)) {
                return type;
            }
        }
        return Public;
    }

    public static ProjectDefAccess fromPath(String path) {

        for (ProjectDefAccess type : ProjectDefAccess.values()) {
            if (type.getPath().equals(path)) {
                return type;
            }
        }
        return Public;
    }

    public PackageType[] getPackageTypes() {
        return packageTypes;
    }

    public void setPackageTypes(PackageType[] packageTypes) {
        this.packageTypes = packageTypes;
    }
}
