package net.ooder.esd.engine.enums;


import net.ooder.annotation.Enumstype;

public enum ProjectTempType implements Enumstype {


    OA("OA", "协同办公", "oa", "Project"),

    Custom("Custom", "通用模板", "custom", "Project"),

    IOT("IOT", "物联网工程", "iot", "Project"),

    Clone("Clone", "克隆已有工程", "clone", "Project");


    private final String path;

    private final String tag;
    private String type;


    private String name;


    ProjectTempType(String type, String name, String path, String tag) {
        this.type = type;
        this.name = name;
        this.path = path;
        this.tag = tag;

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

    public static ProjectTempType fromType(String typeName) {

        for (ProjectTempType type : ProjectTempType.values()) {
            if (type.getType().equals(typeName)) {
                return type;
            }
        }
        return Custom;
    }

    public static ProjectTempType fromPath(String path) {

        for (ProjectTempType type : ProjectTempType.values()) {
            if (type.getPath().equals(path)) {
                return type;
            }
        }
        return Custom;
    }


}
