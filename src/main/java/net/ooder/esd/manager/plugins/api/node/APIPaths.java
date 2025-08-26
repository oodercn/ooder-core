package net.ooder.esd.manager.plugins.api.node;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.esd.manager.plugins.api.enums.APIPathType;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.ProjectVersion;
import net.ooder.esd.tool.properties.APICallerProperties;
import net.ooder.web.APIConfig;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class APIPaths implements Comparable<APIPaths> {

    String name;
    String desc;
    String path;
    String pattern;
    String systemId;
    String imageClass;
    @JSONField(serialize = false)
    Object source;
    @JSONField(serialize = false)
    Set<APIConfig> apiConfigs = new LinkedHashSet<>();
    List<String> childIds = new ArrayList<String>();
    List<String> apiIds = new ArrayList<String>();


    public APIPaths(ProjectVersion projectVersion) {
        this.source = projectVersion;
        this.name = projectVersion.getProject().getProjectName();
        this.path = projectVersion.getProject().getProjectName() + ":/";
        this.systemId = projectVersion.getVersionName();
        this.imageClass = "fa fa-institution";
        this.desc = projectVersion.getDesc();
    }

    public APIPaths(APIServer server) {
        this.source = server;
        this.name = server.getName();
        this.path = server.getId() + ":/";
        this.systemId = server.getId();
        this.imageClass = "fa fa-institution";
        this.desc = server.getDesc();
    }

    public APIPaths(String systemId, APIConfig apiConfig) {
        this.apiConfigs.add(apiConfig);
        this.name = apiConfig.getName();
        this.path = apiConfig.getUrl();
        if (!path.startsWith(systemId)) {
            path = systemId + ":" + path;
        }
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        if (apiConfig.getImageClass() != null && !apiConfig.getImageClass().equals("")) {
            this.imageClass = apiConfig.getImageClass();
        } else {
            this.imageClass = "spafont spa-icon-c-iconslist";
        }
        this.desc = this.path + "(" + apiConfig.getDesc() + ")";
        this.source = apiConfig;
        this.systemId = systemId;


    }

    public APIPaths(String systemId, APICallerProperties apiCallerProperties) {
        this.name = apiCallerProperties.getName();
        this.path = apiCallerProperties.getQueryURL();
        this.imageClass = "spafont spa-icon-c-webapi";
        if (!path.startsWith(systemId)) {
            path = systemId + ":" + path;
        }
        this.source = apiCallerProperties;
        this.desc = name + "(" + apiCallerProperties.getDesc() + ")";
    }


    public APIPaths(String systemId, String name, String path, String desc) {
        this.name = name;
        this.path = path;
        this.systemId = systemId;
        this.desc = desc;
        this.imageClass = "fa fa-cubes";
    }


    public APIPaths(APIPathType type) {
        this.name = type.getSystemCode();
        this.path = type.getPattern();
        this.systemId = type.getSystemCode();
        this.desc = type.getDesc();
        this.imageClass = type.getImageClass();

    }


    public APIPaths(String path) {
        this.path = path;
        this.name = path;
        if (name.endsWith("/")) {
            name = name.substring(0, name.length() - 1);
        }
        if (name.indexOf("/") > -1) {
            name = name.substring(name.lastIndexOf("/"), name.length());
        }
        this.desc = name + "(" + this.path + ")";
    }

    @JSONField(serialize = false)
    public APIPaths getParent() {
        APIPaths ppath = null;
        try {
            ppath = ESDFacrory.getAdminESDClient().getAPIPaths(this.getParentPath());
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return ppath;
    }

    @JSONField(serialize = false)
    public List<APIPaths> getChildren() {
        List<APIPaths> apiPaths = new ArrayList<APIPaths>();
        for (String path : childIds) {
            try {
                APIPaths cpath = ESDFacrory.getAdminESDClient().getAPIPaths(path);
                apiPaths.add(cpath);
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        return apiPaths;

    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }

    public void addChild(String apiPath) {
        if (!childIds.contains(apiPath) && !apiPath.equals(this.getPath())) {
            childIds.add(apiPath);
        }
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    public String getParentPath() {
        String parentPath = this.getPath();
        while (parentPath.endsWith("/")) {
            parentPath = parentPath.substring(0, parentPath.length() - 1);
        }
        if (parentPath.indexOf("/") > -1) {
            parentPath = parentPath.substring(0, parentPath.lastIndexOf("/") + 1);
        } else {
            parentPath = this.systemId;
        }

        return parentPath;
    }


    public List<String> getChildIds() {
        return childIds;
    }

    public void setChildIds(List<String> childIds) {
        this.childIds = childIds;
    }

    public List<String> getApiIds() {
        return apiIds;
    }

    public void setApiIds(List<String> apiIds) {
        this.apiIds = apiIds;
    }

    public Set<APIConfig> getApiConfigs() {
        return apiConfigs;
    }

    public void setApiConfigs(Set<APIConfig> apiConfigs) {
        this.apiConfigs = apiConfigs;
    }

    @Override
    public String toString() {
        return this.getDesc();
    }

    @Override
    public int compareTo(APIPaths o) {
        if (o.getPath() == null || this.getPath() == null) {
            return -1;
        }
        return o.getPath().compareTo(this.getPath());
    }
}
