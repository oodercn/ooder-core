package net.ooder.esd.engine.config;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.engine.enums.ProjectResourceType;
import net.ooder.esd.engine.enums.ProjectRoleType;

import java.util.*;

public class ProjectConfig {


    private List<DataBaseConfig> dbConfigs = new ArrayList<DataBaseConfig>();

    private String usrPackage;

    private String projectName;

    private String publicPath;

    private String workSpace;

    private ProjectResourceType resourceType;

    private List<String> apiFilter = new ArrayList<String>();

    private List<String> customModuleFilter = new ArrayList<String>();

    private String index = "App.index";


    private String publicServerUrl;

    private List<String> fonts = new ArrayList<String>();

    private List<String> imgs = new ArrayList<String>();

    private List<String> styles = new ArrayList<String>();

    public List<String> getExtcoms() {
        return extcoms;
    }

    public void setExtcoms(List<String> extcoms) {
        this.extcoms = extcoms;
    }

    public List<String> getExtmodules() {
        return extmodules;
    }

    public void setExtmodules(List<String> extmodules) {
        this.extmodules = extmodules;
    }

    private List<String> extcoms = new ArrayList<String>();

    private List<String> extmodules = new ArrayList<String>();


    public Map<ProjectRoleType, Set<String>> devPersons = new HashMap<ProjectRoleType, Set<String>>();

    public ProjectConfig() {

    }


    public Map<ProjectRoleType, Set<String>> getDevPersons() {
        return devPersons;
    }

    public void setDevPersons(Map<ProjectRoleType, Set<String>> devPersons) {
        this.devPersons = devPersons;
    }


    public void removeDbConfigBykey(String configKey) {
        DataBaseConfig deldataBaseConfig = getDataBaseConfigBykey(configKey);
        if (deldataBaseConfig != null) {
            dbConfigs.remove(deldataBaseConfig);
        }
    }

    public DataBaseConfig getDataBaseConfigBykey(String configKey) {
        for (DataBaseConfig dataBaseConfig : dbConfigs) {
            if (dataBaseConfig.getConfigKey() != null && dataBaseConfig.getConfigKey().equals(configKey)) {
                return dataBaseConfig;
            }
        }
        return null;
    }


    @JSONField(serialize = false)
    public Set<String> getDevPersons(ProjectRoleType type) {
        Set<String> persons = new HashSet<String>();
        if (type.equals(ProjectRoleType.all)) {
            Set<ProjectRoleType> keys = devPersons.keySet();
            for (ProjectRoleType key : keys) {

                Set<String> allpersons = devPersons.get(key);
                if (allpersons != null) {
                    persons.addAll(allpersons);
                }
            }

        } else {
            persons = devPersons.get(type);
            if (persons == null) {
                persons = new HashSet<>();
                devPersons.put(type, persons);
            }
        }


        return persons;
    }


    public void addDevPersons(ProjectRoleType type, String personId) {
        Set<String> persons = getDevPersons(type);
        if (!persons.contains(personId)) {
            persons.add(personId);
        }
    }

    public void clearDevPersons() {
        devPersons.clear();
    }

    public void removeDevPersons(ProjectRoleType type, String personId) {
        Set<String> persons = getDevPersons(type);
        if (persons != null && persons.contains(personId)) {
            persons.remove(personId);
        }
    }

    public String getUsrPackage() {
        return usrPackage;
    }

    public void setUsrPackage(String usrPackage) {
        this.usrPackage = usrPackage;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }


    public List<String> getFonts() {
        return fonts;
    }

    public void setFonts(List<String> fonts) {
        this.fonts = fonts;
    }


    public String getPublicPath() {
        return publicPath;
    }

    public void setPublicPath(String publicPath) {
        this.publicPath = publicPath;
    }

    public String getWorkSpace() {
        return workSpace;
    }

    public void setWorkSpace(String workSpace) {
        this.workSpace = workSpace;
    }


    public void setApiFilter(List<String> apiFilter) {
        this.apiFilter = apiFilter;
    }

    public List<String> getApiFilter() {
        return apiFilter;
    }

    public String getPublicServerUrl() {
        return publicServerUrl;
    }

    public void setPublicServerUrl(String publicServerUrl) {
        this.publicServerUrl = publicServerUrl;
    }

    public ProjectResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ProjectResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public void addDbConfig(DataBaseConfig dataBaseConfig) {
        dbConfigs.add(dataBaseConfig);
    }

    public List<String> getCustomModuleFilter() {
        return customModuleFilter;
    }

    public void setCustomModuleFilter(List<String> customModuleFilter) {
        this.customModuleFilter = customModuleFilter;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<DataBaseConfig> getDbConfigs() {
        return dbConfigs;
    }

    public void setDbConfigs(List<DataBaseConfig> dbConfigs) {
        this.dbConfigs = dbConfigs;
    }

    public List<String> getStyles() {
        return styles;
    }

    public void setStyles(List<String> styles) {
        this.styles = styles;
    }

}
