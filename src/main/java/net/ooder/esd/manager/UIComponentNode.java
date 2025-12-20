package net.ooder.esd.manager;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.ComponentList;
import net.ooder.esd.tool.properties.CS;
import net.ooder.esd.tool.properties.Properties;
import net.ooder.esd.tool.properties.form.FormField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UIComponentNode {


    private String className;


    public String id;

    public String path;

    public String alias;

    public boolean disabled = false;

    public boolean group = false;

    public boolean draggable = true;

    public String key;

    public String cls = "ood.UI.ComboInput";

    public String imageClass = "ri-code-box-line";

    public String caption;


    public boolean iniFold = false;

    public List<UIComponentNode> sub;


    public String target;


    public Map<String, String> tagVar = new HashMap<String, String>();

    public Properties iniProp;

    @JSONField(name = "CS")
    public CS cs;


    public UIComponentNode(Component component, boolean draggable) {
        Properties properties = component.getProperties();
        this.id =
                component.getPath();


        String desc = "";
        if (properties != null) {
            desc = properties.getDesc();
            if (desc == null) {
                desc = component.typeKey.getName();
            }
            if (desc != null) {
                desc = "(" + desc + ")";
            }
        }


        component.getProperties().setEvents(component.getEvents());
        this.caption = component.getAlias() + desc;
        this.key = component.getKey();
        if (properties != null && properties instanceof FormField && properties.getName() != null) {
            this.caption = properties.getName() + desc;
        }

        this.cls = component.getKey();
        this.path = component.getPath();
        this.alias = component.getAlias();
        this.imageClass = component.typeKey.getImageClass();


        ComponentList childs = component.getChildren();
        if (childs != null && childs.size() > 0) {
            sub = new ArrayList<UIComponentNode>();
            for (Component childcomponent : childs) {
                if (childcomponent != null && childcomponent.getProperties() != null) {
                    sub.add(new UIComponentNode(childcomponent, draggable));
                }

            }
        }


        if (component.getModuleComponent() != null) {
            this.className = component.getModuleComponent().getClassName();
        }


        if (draggable) {
            if (component.typeKey.equals(ComponentType.MODULE)) {
                this.caption = ((ModuleComponent) component).getClassName();
                this.iniFold = true;
            }
            this.iniProp = component.getProperties();

            this.cs = component.getCS();
            this.target = component.getTarget();
        } else {
            if (component.typeKey.equals(ComponentType.MODULE)) {
                this.caption = ((ModuleComponent) component).getEuModule().getName();
                this.iniFold = true;
            }
        }


    }

    public List<UIComponentNode> getSub() {
        return sub;
    }

    public void setSub(List<UIComponentNode> sub) {
        this.sub = sub;
    }


    public CS getCs() {
        return cs;
    }

    public void setCs(CS cs) {
        this.cs = cs;
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

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
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

    public String getClassName() {
        return className;
    }
}
