package net.ooder.esd.engine.config;


import net.ooder.server.httpproxy.core.ProxyHost;

import java.util.ArrayList;
import java.util.List;

public class DevUserConfig {


    private String userId;

    private String serverHome;


    private List<ProxyHost> hosts = new ArrayList<ProxyHost>();

    private List<LocalServer> servers = new ArrayList<LocalServer>();

    private List<RemoteServer> remoteServers = new ArrayList<RemoteServer>();


    public DevUserConfig() {

    }

    public List<RemoteServer> getRemoteServers() {
        return remoteServers;
    }

    public void setRemoteServers(List<RemoteServer> remoteServers) {
        this.remoteServers = remoteServers;
    }

    public String getServerHome() {
        return serverHome;
    }

    public void setServerHome(String serverHome) {
        this.serverHome = serverHome;
    }

    public List<ProxyHost> getProxyHostByUrl(String host) {
        List<ProxyHost> ohosts = new ArrayList<ProxyHost>();
        for (ProxyHost proxyHost : hosts) {
            if (proxyHost.getHost() != null && proxyHost.getHost().equals(host)) {
                ohosts.add(proxyHost);
            }
        }
        return ohosts;
    }

    public ProxyHost getProxyHostById(String proxyId) {
        for (ProxyHost proxyHost : hosts) {
            if (proxyHost.getProxyId().equals(proxyId)) {
                return proxyHost;
            }
        }
        return null;
    }

    public LocalServer getServerById(String serverId) {
        if (serverId != null && !serverId.equals("")) {
            for (LocalServer server : servers) {
                if (server.getServerId().equals(serverId)) {
                    return server;
                }
            }
        }
        return null;
    }


    public List<LocalServer> getLocalServerByProjectName(String projectName) {
        List<LocalServer> servers = new ArrayList<>();
        for (LocalServer server : servers) {
            if (server.getProjectName() != null && server.getProjectName().equals(projectName)) {
                servers.add(server);
            }
        }
        return servers;
    }


    public RemoteServer getRemoteServerById(String serverId) {
        if (serverId != null && !serverId.equals("")) {
            for (RemoteServer server : remoteServers) {
                if (server.getServerId().equals(serverId)) {
                    return server;
                }
            }
        }
        return null;
    }


    public List<RemoteServer> getRemoteServerByProjectName(String projectName) {
        List<RemoteServer> servers = new ArrayList<>();
        for (RemoteServer server : remoteServers) {
            if (server.getProjectName() != null && server.getProjectName().equals(projectName)) {
                servers.add(server);
            }
        }
        return servers;
    }


    public List<LocalServer> getServers() {
        return servers;
    }

    public void setServers(List<LocalServer> servers) {
        this.servers = servers;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<ProxyHost> getHosts() {
        return hosts;
    }

    public void setHosts(List<ProxyHost> hosts) {
        this.hosts = hosts;
    }

    public DevUserConfig(String userId) {
        this.userId = userId;
    }


}
