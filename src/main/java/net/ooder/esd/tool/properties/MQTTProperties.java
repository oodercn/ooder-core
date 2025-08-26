package net.ooder.esd.tool.properties;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.bean.MQTTBean;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.List;
import java.util.Map;

public class MQTTProperties extends Properties {
    public String dataBinder;
    public String dataField;
    public String libCDN;
    public Boolean autoConn;
    public Boolean autoSub;
    public List<String> subscribers;

    public String server;
    public String port;
    public String path;
    public String clientId;

    public Integer timeout;
    public String userName;
    public String password;
    public Integer keepAliveInterval;
    public Boolean cleanSession;
    public Boolean useSSL;
    public Boolean reconnect;

    public String willTopic;
    public String willMessage;
    public Integer willQos;
    public Boolean willRetained;

    public MQTTProperties() {

    }

    public MQTTProperties(MQTTBean mqttBean) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(mqttBean), Map.class), this, false, false);
    }

    public String getDataBinder() {
        return dataBinder;
    }

    public void setDataBinder(String dataBinder) {
        this.dataBinder = dataBinder;
    }

    public String getDataField() {
        return dataField;
    }

    public void setDataField(String dataField) {
        this.dataField = dataField;
    }

    public String getLibCDN() {
        return libCDN;
    }

    public void setLibCDN(String libCDN) {
        this.libCDN = libCDN;
    }

    public Boolean isAutoConn() {
        return autoConn;
    }

    public void setAutoConn(Boolean autoConn) {
        this.autoConn = autoConn;
    }

    public Boolean isAutoSub() {
        return autoSub;
    }

    public void setAutoSub(Boolean autoSub) {
        this.autoSub = autoSub;
    }

    public List<String> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(List<String> subscribers) {
        this.subscribers = subscribers;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getKeepAliveInterval() {
        return keepAliveInterval;
    }

    public void setKeepAliveInterval(Integer keepAliveInterval) {
        this.keepAliveInterval = keepAliveInterval;
    }

    public Boolean isCleanSession() {
        return cleanSession;
    }

    public void setCleanSession(Boolean cleanSession) {
        this.cleanSession = cleanSession;
    }

    public Boolean isUseSSL() {
        return useSSL;
    }

    public void setUseSSL(Boolean useSSL) {
        this.useSSL = useSSL;
    }

    public Boolean isReconnect() {
        return reconnect;
    }

    public void setReconnect(Boolean reconnect) {
        this.reconnect = reconnect;
    }

    public String getWillTopic() {
        return willTopic;
    }

    public void setWillTopic(String willTopic) {
        this.willTopic = willTopic;
    }

    public String getWillMessage() {
        return willMessage;
    }

    public void setWillMessage(String willMessage) {
        this.willMessage = willMessage;
    }

    public Integer getWillQos() {
        return willQos;
    }

    public void setWillQos(Integer willQos) {
        this.willQos = willQos;
    }

    public Boolean isWillRetained() {
        return willRetained;
    }

    public void setWillRetained(Boolean willRetained) {
        this.willRetained = willRetained;
    }
}
