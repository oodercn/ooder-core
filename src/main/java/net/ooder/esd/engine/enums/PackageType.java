package net.ooder.esd.engine.enums;



import net.ooder.annotation.IconEnumstype;
import net.ooder.esd.annotation.ui.IconColorEnum;
import net.ooder.annotation.UserSpace;

public enum PackageType implements IconEnumstype {
    admin("本地服务", "esdadmin", UserSpace.SYS, "ri-server-line", IconColorEnum.BABYBLUE),
    custom("通用服务", "bpm", UserSpace.VIEW, "ri-node-tree", IconColorEnum.DARKBLUE),
    dsm("DSM建模", "DSMdsm", UserSpace.SYS, "ri-settings-3-line", IconColorEnum.CYAN),
    sys("系统服务", "sys", UserSpace.SYS, "ri-server-line", IconColorEnum.CYAN),
    tool("应用工具", "esdadmin", UserSpace.SYS, "ri-tools-line", IconColorEnum.PINK),
    esd("工具插件", "esdadmin", UserSpace.SYS, "ri-puzzle-line", IconColorEnum.BABYBLUE),
    cluster("集群服务", "cluster", UserSpace.SYS, "ri-network-line", IconColorEnum.GREEN),
    userdef("自定义服�?", null, UserSpace.VIEW, "ri-settings-3-line", IconColorEnum.YELLOW),
    view("视图", null, UserSpace.VIEW, "ri-eye-line", IconColorEnum.CYAN);
    
    private String name;
    public UserSpace catType;
    private IconColorEnum iconColorEnum;
    private String imageClass;
    private String defaultProjectName;

    PackageType(String name, String defaultProjectName, UserSpace catType, String imageClass, IconColorEnum iconColorEnum) {
        this.name = name;
        this.catType = catType;
        this.imageClass = imageClass;
        this.iconColorEnum = iconColorEnum;
        this.defaultProjectName = defaultProjectName;
    }


    public static PackageType fromDefaultProject(String defaultProjectName) {
        for (PackageType type : PackageType.values()) {
            if (type.getDefaultProjectName() != null && type.getDefaultProjectName().equals(defaultProjectName)) {
                return type;
            }
        }
        return null;
    }


    public UserSpace getCatType() {
        return catType;
    }

    public void setCatType(UserSpace catType) {
        this.catType = catType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IconColorEnum getIconColorEnum() {
        return iconColorEnum;
    }

    public String getDefaultProjectName() {
        return defaultProjectName;
    }

    public void setDefaultProjectName(String defaultProjectName) {
        this.defaultProjectName = defaultProjectName;
    }

    public void setIconColorEnum(IconColorEnum iconColorEnum) {
        this.iconColorEnum = iconColorEnum;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
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

    @Override
    public String getImageClass() {
        return imageClass;
    }
}
