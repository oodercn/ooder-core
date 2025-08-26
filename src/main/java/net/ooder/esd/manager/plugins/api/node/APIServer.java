package net.ooder.esd.manager.plugins.api.node;

import net.ooder.cluster.ServerNode;
import net.ooder.esd.manager.plugins.api.node.APIPaths;

import java.util.List;

public class APIServer {
    String id;
    String name;
    String desc;
    String url;
    List<APIPaths> apiPaths;
    List<ServerNode> serverNodeList;

    public APIServer(ServerNode node) {
        this.name = node.getId();
        this.id = node.getId();
        this.url = node.getUrl();
        this.desc = node.getDesc()==null ?node.getName(): node.getDesc();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public List<APIPaths> getApiPaths() {
        return apiPaths;
    }

    public void setApiPaths(List<APIPaths> apiPaths) {
        this.apiPaths = apiPaths;
    }

    public List<ServerNode> getServerNodeList() {
        return serverNodeList;
    }

    public void setServerNodeList(List<ServerNode> serverNodeList) {
        this.serverNodeList = serverNodeList;
    }
}
