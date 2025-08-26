package net.ooder.esd.manager.temp;

import net.ooder.esd.engine.enums.ProjectTempType;

public class ProjectTemp {

    private String path;

    private String tag;

    private String type;

    private String name;

    private String caption;

    public ProjectTemp(ProjectTempType type) {
        this.path = type.getPath();
        this.tag = type.getTag();
        this.type = type.getType();
        this.name = type.getName();
        this.caption = type.getName();
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
