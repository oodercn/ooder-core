package net.ooder.esd.tool.properties;

import java.util.Map;

public class CSSBoxProperties extends SpanProperties {
    String className;
    String sandbox;
    String customCss;
    Map<String, Object> normalStatus;
    Map<String, Object> hoverStatus;
    Map<String, Object> activeStatus;
    Map<String, Object> focusStatus;


    public CSSBoxProperties() {

    }


    public String getCustomCss() {
        return customCss;
    }

    public void setCustomCss(String customCss) {
        this.customCss = customCss;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSandbox() {
        return sandbox;
    }

    public void setSandbox(String sandbox) {
        this.sandbox = sandbox;
    }

    public Map<String, Object> getNormalStatus() {
        return normalStatus;
    }

    public void setNormalStatus(Map<String, Object> normalStatus) {
        this.normalStatus = normalStatus;
    }

    public Map<String, Object> getHoverStatus() {
        return hoverStatus;
    }

    public void setHoverStatus(Map<String, Object> hoverStatus) {
        this.hoverStatus = hoverStatus;
    }

    public Map<String, Object> getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(Map<String, Object> activeStatus) {
        this.activeStatus = activeStatus;
    }

    public Map<String, Object> getFocusStatus() {
        return focusStatus;
    }

    public void setFocusStatus(Map<String, Object> focusStatus) {
        this.focusStatus = focusStatus;
    }


}
