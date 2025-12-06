package net.ooder.esd.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.annotation.SimpleCustomBean;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.MQTTAnnotation;
import net.ooder.esd.annotation.event.CustomModuleEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.bean.field.FieldComponentBean;
import net.ooder.esd.custom.component.CustomMQTTComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.MQTTComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.PanelComponent;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.Annotation;
import java.util.*;
@CustomClass(viewType = CustomViewType.COMPONENT,clazz =CustomMQTTComponent.class, componentType = ComponentType.MQTT)
@AnnotationType(clazz = MQTTAnnotation.class)
public class MQTTBean implements FieldComponentBean<MQTTComponent> {

    public String dataBinder;
    public String dataField;
    public String libCDN;
    public Boolean autoConn;
    public Boolean autoSub;
    public Set<String> subscribers;

    public String server;
    public String port;
    public String path;
    public String xpath;
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

    List<CustomModuleEventEnum> onMsgArrived;

    List<CustomAction> onMsgArrivedAction;


    public MQTTBean(MQTTComponent mqttComponent) {
        this.xpath = mqttComponent.getPath();
        Map valueMap = JSON.parseObject(JSON.toJSONString(mqttComponent.getProperties()), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);

    }

    public MQTTBean() {

    }

    public MQTTBean(Set<Annotation> annotations) {

        AnnotationUtil.fillDefaultValue(MQTTAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof MQTTAnnotation) {
                fillData((MQTTAnnotation) annotation);
            }
        }

    }

    @Override
    public List<JavaSrcBean> getJavaSrcBeans() {
        return new ArrayList<>();
    }

    @Override
    public void update(ModuleComponent moduleComponent, MQTTComponent component) {
        this.xpath = component.getPath();
        Map valueMap = JSON.parseObject(JSON.toJSONString(component.getProperties()), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);


    }

    public List<CustomModuleEventEnum> getOnMsgArrived() {
        return onMsgArrived;
    }

    public void setOnMsgArrived(List<CustomModuleEventEnum> onMsgArrived) {
        this.onMsgArrived = onMsgArrived;
    }

    public List<CustomAction> getOnMsgArrivedAction() {
        return onMsgArrivedAction;
    }

    public void setOnMsgArrivedAction(List<CustomAction> onMsgArrivedAction) {
        this.onMsgArrivedAction = onMsgArrivedAction;
    }

    @Override
    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
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

    public Boolean getAutoConn() {
        return autoConn;
    }

    public void setAutoConn(Boolean autoConn) {
        this.autoConn = autoConn;
    }

    public Boolean getAutoSub() {
        return autoSub;
    }

    public void setAutoSub(Boolean autoSub) {
        this.autoSub = autoSub;
    }

    public Set<String> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Set<String> subscribers) {
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

    public Boolean getCleanSession() {
        return cleanSession;
    }

    public void setCleanSession(Boolean cleanSession) {
        this.cleanSession = cleanSession;
    }

    public Boolean getUseSSL() {
        return useSSL;
    }

    public void setUseSSL(Boolean useSSL) {
        this.useSSL = useSSL;
    }

    public Boolean getReconnect() {
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

    public Boolean getWillRetained() {
        return willRetained;
    }

    public void setWillRetained(Boolean willRetained) {
        this.willRetained = willRetained;
    }

    public MQTTBean(MQTTAnnotation annotation) {
        fillData(annotation);
    }

    public MQTTBean fillData(MQTTAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }


    @Override
    public ComponentType getComponentType() {
        return ComponentType.MQTT;
    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        annotationBeans.add(new SimpleCustomBean(ResponseBody.class));
        annotationBeans.add(this);
        return annotationBeans;
    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet = new HashSet<>();

        return classSet;
    }


    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
