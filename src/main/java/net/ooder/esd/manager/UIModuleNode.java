package net.ooder.esd.manager;

import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.properties.Properties;

import java.util.HashMap;
import java.util.Map;

public class UIModuleNode {


    public String id;

    public String path;

    String alias;

    public boolean disabled = false;

    public boolean group = false;

    public boolean draggable = true;

    public String key;

    public String cls;

    public String imageClass = "fa-solid fa-code";

    public String caption;


    public boolean iniFold = false;


    public Map<String, String> tagVar = new HashMap<String, String>();

    public Properties iniProp;


    public UIModuleNode(EUModule module) {
        this.id = module.getClassName();
        this.caption = module.getName();
        this.cls = module.getComponent().getKey();
        this.iniProp = module.getComponent().getProperties();


    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Properties getIniProp() {
        return iniProp;
    }

    public void setIniProp(Properties iniProp) {
        this.iniProp = iniProp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }


    public boolean isIniFold() {
        return iniFold;
    }

    public void setIniFold(boolean iniFold) {
        this.iniFold = iniFold;
    }

    public Map<String, String> getTagVar() {
        return tagVar;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isGroup() {
        return group;
    }

    public void setGroup(boolean group) {
        this.group = group;
    }

    public boolean isDraggable() {
        return draggable;
    }

    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public void setTagVar(Map<String, String> tagVar) {
        this.tagVar = tagVar;
    }

}
