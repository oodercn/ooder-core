package net.ooder.esd.tool;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.properties.Event;
import net.ooder.esd.util.json.ComponentsMapDeserializer;

import java.util.List;
import java.util.Map;

public class ModuleJSON {

    List<String> dependencies;
    // 需要的模块
    List<String> required;
    // 初始化属性
    Map<String, Object> properties;

    @JSONField(deserializeUsing = ComponentsMapDeserializer.class)
    Map<String, Component> components;

    public Map<String, Event> events;

    @JSONField(name = "Static")
    public ModuleViewConfig staticConfig;

    public Map<String, Event> getEvents() {
        return events;
    }

    public void setEvents(Map<String, Event> events) {
        this.events = events;
    }

    public ModuleJSON() {

    }

    public ModuleViewConfig getStaticConfig() {
        return staticConfig;
    }

    public void setStaticConfig(ModuleViewConfig staticConfig) {
        this.staticConfig = staticConfig;
    }

    public List<String> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<String> dependencies) {
        this.dependencies = dependencies;
    }

    public List<String> getRequired() {
        return required;
    }

    public void setRequired(List<String> required) {
        this.required = required;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public Map<String, Component> getComponents() {
        return components;
    }

    public void setComponents(Map<String, Component> components) {
        this.components = components;
    }
}
