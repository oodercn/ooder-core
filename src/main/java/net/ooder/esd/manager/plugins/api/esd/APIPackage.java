package net.ooder.esd.manager.plugins.api.esd;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.common.util.StringUtility;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.manager.plugins.api.node.APIPaths;
import net.ooder.web.APIConfig;
import net.ooder.web.APIConfigFactory;

import java.util.ArrayList;
import java.util.List;

public class APIPackage implements Comparable<APIPackage> {

    String name;
    String desc;
    String path;
    String packageName;
    String pattern;
    String systemId;
    String imageClass;
    List<APIConfig> apiConfigs = new ArrayList<>();

    APIPaths apiPaths;


    public APIPackage(APIPaths apiPaths) {
        this.apiPaths = apiPaths;
        this.name = apiPaths.getName();
        this.path = apiPaths.getPath();
        this.systemId = apiPaths.getSystemId();
        this.desc = apiPaths.getDesc();
        this.imageClass = "fa fa-cubes";
        if (path.endsWith("/")) {
            packageName = packageName.substring(0, packageName.length() - 1);
        }
        this.packageName = StringUtility.replace(apiPaths.getPath(), "/", ".");
    }


    public APIPackage(String path) {
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
    public List<APIPackage> getChildren() {
        List<APIPackage> apiPathsList = new ArrayList<APIPackage>();
        for (String path : apiPaths.getChildIds()) {
            try {
                APIPaths cpath = ESDFacrory.getAdminESDClient().getAPIPaths(path);
                apiPathsList.add(new APIPackage(cpath));
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        return apiPathsList;
    }

    @JSONField(serialize = false)
    public List<APIConfig> getAPIList() {
        List<APIConfig> apiConfigList = new ArrayList<APIConfig>();
        for (String apiId : apiPaths.getApiIds()) {
            List<APIConfig> apiConfigs = APIConfigFactory.getInstance().getAPIConfigs(path);
            apiConfigList.addAll(apiConfigs);
        }
        return apiConfigList;
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


    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<APIConfig> getApiConfigs() {
        return apiConfigs;
    }

    public void setApiConfigs(List<APIConfig> apiConfigs) {
        this.apiConfigs = apiConfigs;
    }

    @Override
    public String toString() {
        return this.getDesc();
    }

    @Override
    public int compareTo(APIPackage o) {
        if (o.getPath() == null || this.getPath() == null) {
            return -1;
        }
        return o.getPath().compareTo(this.getPath());
    }
}
