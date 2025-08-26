package net.ooder.esd.manager.esdserver;

import net.ooder.esd.engine.Project;
import net.ooder.esd.engine.config.LocalServer;

public class ESDServerExe {

    String projectName;
    String applUrl = "";
    String path = "";
    String proxyUrl = "";
    String indexPage = "App.Index";
    String pid;
    String proxyPort;


       public ESDServerExe(Project project, LocalServer localServer) {
        this.proxyPort = localServer.getProxyPort().toString();
        this.projectName = project.getProjectName();
        this.applUrl = project.getPublicServerUrl();
        this.proxyUrl = localServer.getProxyUrl();
        this.indexPage = project.getConfig().getIndex();
        this.path = localServer.getPath();
    }


    public ESDServerExe(String projectName, String applUrl, String proxyUrl, String indexPage, String path, String pid, String proxyPort) {
        this.proxyPort = proxyPort;
        this.projectName = projectName;
        this.applUrl = applUrl;
        this.proxyUrl = proxyUrl;
        this.indexPage = indexPage;
        this.pid = pid;
        this.path = path;

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

    public String getApplUrl() {
        return applUrl;
    }

    public void setApplUrl(String applUrl) {
        this.applUrl = applUrl;
    }

    public String getProxyUrl() {
        return proxyUrl;
    }

    public void setProxyUrl(String proxyUrl) {
        this.proxyUrl = proxyUrl;
    }

    public String getIndexPage() {
        return indexPage;
    }

    public void setIndexPage(String indexPage) {
        this.indexPage = indexPage;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
    }
}

