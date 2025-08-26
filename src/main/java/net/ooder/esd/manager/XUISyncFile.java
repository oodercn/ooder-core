package net.ooder.esd.manager;

import net.ooder.common.JDSException;
import net.ooder.common.util.StringUtility;
import net.ooder.esd.engine.ProjectVersion;
import net.ooder.esd.engine.enums.PackagePathType;
import net.ooder.esd.manager.plugins.api.APIFactory;
import net.ooder.esd.manager.plugins.api.node.APIPaths;
import net.ooder.vfs.Folder;
import net.ooder.web.APIConfig;

import java.util.ArrayList;
import java.util.List;

public class XUISyncFile implements Comparable<XUISyncFile> {

    String name;
    String id;
    Integer type;
    String location;
    String className;
    String imageClass;
    String projectName;
    String caption;
    String path;
    List<XUIModuleFile> sub = new ArrayList<XUIModuleFile>();
    public Boolean iniFold;

    public XUISyncFile(Folder folder, ProjectVersion version) {
        iniFold = true;
        this.name = folder.getDescrition() == null ? folder.getName() : folder.getDescrition();
        this.location = folder.getPath();
        this.id = location;
        String subpath = StringUtility.replace(folder.getPath(), version.getPath(), "/");
        try {
            APIPaths apiPaths = APIFactory.getInstance().getAPIPaths(subpath);
            if (apiPaths != null) {
                for (APIConfig config : apiPaths.getApiConfigs()) {
                    if (config.getChinaName() != null) {
                        this.name = config.getPackageName() + "(" + config.getDesc() + ")";
                        this.imageClass = apiPaths.getImageClass();
                    }
                }
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }

        PackagePathType packagePathType = PackagePathType.equalsPath(subpath);

        if (packagePathType != null) {
            this.name = packagePathType.getDesc();
            this.imageClass = packagePathType.getImageClass();
        }

        this.className = StringUtility.replace(location, version.getPath(), "");
        this.className = StringUtility.replace(this.className, "/", ".");
        if (className.endsWith(".")) {
            className = className.substring(0, className.length() - 1);
        }
        if (location.endsWith(".")) {
            className = className.substring(1, className.length());
        }
        path = location;

        this.type = 0;
        this.caption = this.name;
        //模板文件没有版本
        if (version != null) {
            String curProjectPath = version.getPath();
            if (curProjectPath != null && !curProjectPath.equals("") && location.startsWith(curProjectPath)) {
                location = location.substring(curProjectPath.length());
            }
            this.projectName = version.getVersionName();
        }

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public List<XUIModuleFile> getSub() {
        return sub;
    }

    public void setSub(List<XUIModuleFile> sub) {
        this.sub = sub;
    }

    @Override
    public int compareTo(XUISyncFile o) {
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