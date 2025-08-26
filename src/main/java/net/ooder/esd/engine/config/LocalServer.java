package net.ooder.esd.engine.config;


import net.ooder.common.SystemStatus;

public class LocalServer {

    private String name;

    private String configFile;

    private String serverId;

    private String startScript;

    private String path;

    public SystemStatus status = SystemStatus.OFFLINE;

    private String projectName;

    private String proxyUrl = "";

    private Integer proxyPort = 8090;


    public LocalServer() {

    }

    public SystemStatus getStatus() {
        return status;
    }

    public void setStatus(SystemStatus status) {
        this.status = status;
    }

    public String getConfigFile() {
        return configFile;
    }

    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getStartScript() {
        return startScript;
    }

    public void setStartScript(String startScript) {
        this.startScript = startScript;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProxyUrl() {
        return proxyUrl;
    }

    public void setProxyUrl(String proxyUrl) {
        this.proxyUrl = proxyUrl;
    }

    public Integer getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(Integer proxyPort) {
        this.proxyPort = proxyPort;
    }
}
