package net.ooder.esd.manager;

import net.ooder.esd.engine.EUModule;

public class OODModuleFile implements Comparable<OODModuleFile> {

    String name;
    String id;
    String location;
    String className;
    String imageClass;
    String projectName;
    String caption;
    String path;
    String type;

    Boolean sub;
    public Boolean iniFold;

    public OODModuleFile(EUModule module) {
        this.name = module.getName() + ".cls";
        this.sub = true;
        this.iniFold = true;
        this.location = module.getPath();
        String curProjectPath = module.getProjectVersion().getPath();
        if (curProjectPath != null && !curProjectPath.equals("") && location.startsWith(curProjectPath)) {
            location = location.substring(curProjectPath.length());
        }
        this.className = module.getClassName();
        this.id = this.getClassName();
        this.imageClass = "spafont spa-icon-page";
        this.caption = name;
        this.projectName = module.getProjectVersion().getProjectName();
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public Boolean getIniFold() {
        return iniFold;
    }

    public void setIniFold(Boolean iniFold) {
        this.iniFold = iniFold;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }

    public Boolean getSub() {
        return sub;
    }

    public void setSub(Boolean sub) {
        this.sub = sub;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int compareTo(OODModuleFile o) {
        if (className != null && o.getClassName() != null) {
            return className.compareTo(o.getClassName());
        } else if (caption != null && o.getCaption() != null) {
            return caption.compareTo(o.getCaption());
        } else if (location != null && o.getLocation() != null) {
            return location.compareTo(o.getLocation());
        }

        return id.compareTo(o.getId());
    }
}