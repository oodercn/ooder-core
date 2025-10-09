package net.ooder.esd.tool.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.EventKey;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.ViewGroupType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.properties.AbsUIProperties;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.Event;
import net.ooder.esd.tool.properties.Properties;
import net.ooder.esd.util.json.ComponentsDeserializer;
import net.ooder.web.util.JSONGenUtil;
import org.mvel2.templates.TemplateRuntime;

import java.lang.reflect.Type;
import java.util.*;

public class Component<T extends Properties, K extends EventKey> {

    protected ComponentType[] skipComponents = new ComponentType[]{ComponentType.BLOCK, ComponentType.DIV, ComponentType.DIALOG};

    public String alias;

    public String host;

    @JSONField(serialize = false)
    public ComponentType typeKey;

    @JSONField(serialize = false)
    public Component parent;

    @JSONField(deserialize = false)
    public T properties;

    @JSONField(deserializeUsing = ComponentsDeserializer.class)
    public ComponentList children;

    public Map<K, Event> events = new HashMap<K, Event>();

    @JSONField(name = "CS")
    public net.ooder.esd.tool.properties.CS CS;

    public String target;

    public Component() {

    }

    public Component(ComponentType typeKey, String alias) {
        this.typeKey = typeKey;
        this.alias = alias;
        ;
    }

    @JSONField(serialize = false)
    public List<Action> getAllAction() {
        return this.getModuleComponent().getComponentAction(this.alias);
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }


    public Map<K, Event> getEvents() {
        return events;
    }

    public void setEvents(Map<K, Event> events) {
        this.events = events;
    }

    public Component(ComponentType typeKey) {
        this.typeKey = typeKey;
    }

    public void append(EUModule parentModule, ModuleComponent component, String target) {
        Component fristComponent = component.getTopComponentBox();
        List<APICallerComponent> apis = component.findComponents(ComponentType.APICALLER.name(), null);
        if (fristComponent == null) {
            fristComponent = component.getLastBoxComponent();
        }
        if (target == null) {
            target = component.getTarget();
        }

        for (APICallerComponent apiCallerComponent : apis) {
            apiCallerComponent.setAlias(component.getAlias() + "." + apiCallerComponent.getAlias());
            parentModule.getComponent().addChildren(apiCallerComponent);
        }

        if (fristComponent != null) {
            fristComponent.setTarget(target);
            this.addChildren(fristComponent);
        }
    }

    public Component addAction(Action<K> action) {
        return addAction(action, true);
    }

    public Component addAction(Action<K> action, boolean par) {
        return addAction(action, par, null);
    }

    ;

    public Component addAction(Action<K> action, boolean par, String _return) {
        K eventKey = action.getEventKey();
        if (events == null) {
            events = new HashMap<K, Event>();
        }
        Event event = events.get(eventKey);

        if (event == null) {
            event = new Event(eventKey);
        }

        if (_return != null) {
            event.setEventReturn(_return);
        }

        List<Action> actions = event.getActions();
        if (actions == null) {
            actions = new ArrayList<Action>();
        }
        if (!actions.contains(action)) {

            if (par) {
                String json = JSONObject.toJSONString(action, false);
                Map context = JDSActionContext.getActionContext().getContext();
                if (this.getModuleComponent() != null) {
                    context.put(CustomViewFactory.CurrModuleKey, this.getModuleComponent().getEuModule());
                }
                String objStr = (String) TemplateRuntime.eval(json, context);
                action = JSONObject.parseObject(objStr, new TypeReference<Action<K>>() {
                });
                action.setEventKey(eventKey);
            }

            actions.add(action);
        }


        event.setActions(actions);
        events.put(eventKey, event);
        return this;
    }

    public void removeChildren(Component... components) {
        if (children == null) {
            children = new ComponentList();
        }
        for (Component component : components) {
            if (component.getAlias() == null || component.getAlias().equals("")) {
                component.setAlias(component.typeKey.name() + components.length);
            }
            Component oldComponent = this.getComponentByAlias(component.getAlias());
            if (oldComponent != null) {
                children.remove(oldComponent);
            }
        }
    }

    public Component setChildren(Component... components) {
        if (children == null) {
            children = new ComponentList();
        }
        removeChildren(components);
        this.addChildren(components);

        return this;
    }

    public void replaceChild(Component... components) {
        this.removeChildren(components);
        this.addChildren(components);
    }

    public Component addChildren(Component... components) {
        if (components != null) {
            if (children == null) {
                children = new ComponentList();
            }
            for (Component component : components) {
                if (component.getAlias() == null || component.getAlias().equals("")) {
                    component.setAlias(component.typeKey.name() + components.length);
                }
                Component oldComponent = this.getComponentByAlias(component.getAlias());

                if (oldComponent == null && this.getModuleComponent() != null) {
                    oldComponent = this.getModuleComponent().findComponentByAlias(component.getAlias());
                }

                if (oldComponent != null && oldComponent.getParent() != null) {
                    oldComponent.getParent().removeChildren(oldComponent);
                }

                children.add(component);
                component.setParent(this);

                if (this.getModuleComponent() != null) {
                    ComponentList componentList = component.getChildrenRecursivelyList();
                    for (Component childComponent : componentList) {
                        this.getModuleComponent().getComponents().put(childComponent.getAlias(), childComponent);
                    }
                    this.getModuleComponent().getComponents().put(component.getAlias(), component);
                }

            }
        }
        return this;
    }


    @JSONField(serialize = false)
    public List<Component> getListGroupChild(ViewGroupType... groupTypes) {

        List<Component> viewComponents = new ArrayList<>();
        List<Component> components = this.getChildrenRecursivelyList();
        Set<ComponentType> customViewTypes = new HashSet<>();
        for (ViewGroupType groupType : groupTypes) {
            List<ModuleViewType> moduleViewTypes = ModuleViewType.getModuleViewByGroup(groupType);
            for (ModuleViewType moduleViewType : moduleViewTypes) {
                customViewTypes.addAll(Arrays.asList(moduleViewType.getComponentTypes()));
            }
        }

        for (Component component : components) {
            ComponentType type = ComponentType.fromType(component.getKey());
            if (customViewTypes.contains(type)) {
                if (Arrays.asList(skipComponents).contains(type)) {
                    List<Component> childComponent = component.getListGroupChild(groupTypes);
                    if (childComponent.size() > 0 || (component.getChildren() != null && component.getChildren().size() > 1)) {
                        viewComponents.add(component);
                    }
                } else {
                    viewComponents.add(component);
                }
            }
        }
        return viewComponents;
    }


    private ComponentList addChild(ComponentList allChildList) {
        ComponentList childs = this.getChildren();
        if (childs != null) {
            for (Component childcomponent : childs) {
                allChildList.addComponent(childcomponent);
                ComponentList childList = childcomponent.getChildren();
                if (childList != null && childList.size() > 0 && !childList.contains(this)) {
                    allChildList.addAll(childcomponent.getChildrenRecursivelyList());
                }
            }
        }
        return allChildList;
    }

    @JSONField(serialize = false)
    public ComponentList getChildrenRecursivelyList() {
        ComponentList allChildList = new ComponentList();
        allChildList = addChild(allChildList);
        return allChildList;
    }

    @JSONField(serialize = false)
    public LinkedList<Component> getAllParentList() {
        LinkedList<Component> result = new LinkedList<Component>();
        Component parent = this.getParent();
        if (parent != null) {
            result.addLast(parent);
        }

        while (parent != null) {
            Component p = parent.getParent();
            if (p == null) {
                break;
            }
            if (p != null) {
                result.addFirst(p);
                if (p.getParent() == null || p.getParent().equals("this")) {
                    break;
                }
                parent = p;
            }
        }
        return result;
    }

    ;

    public Component getParent() {
        return parent;
    }

    public String getPath() {
        String path = this.getAlias();
        if (this.getKey().equals(ComponentType.MODULE.getClassName())) {
            return this.getModuleComponent().getClassName();
        } else {
            if (parent != null) {
                path = parent.getPath() + "." + path;
            }
        }
        return path;
    }


    @JSONField(serialize = false)
    public ModuleComponent getModuleComponent() {
        if (this.getKey().equals(ComponentType.MODULE.getClassName())) {
            return (ModuleComponent) this;
        } else {
            Component parent = this.getParent();
            while (parent != null && !parent.getKey().equals(ComponentType.MODULE.getClassName())) {
                parent = parent.getParent();
            }
            return (ModuleComponent) parent;
        }
    }

    @Override
    public Component clone() {
        Class<Component> clazz = ComponentType.fromType(this.getKey()).getClazz();
        Component component = JSONObject.parseObject(JSON.toJSONString(this), clazz);
        Type realType = JSONGenUtil.getRealType(clazz, Properties.class);
        if (realType != null) {
            Properties properties = JSONObject.parseObject(JSON.toJSONString(this.getProperties()), realType);
            component.setProperties(properties);
        }
        return component;
    }

    public void setParent(Component parent) {
        this.parent = parent;
    }


    public net.ooder.esd.tool.properties.CS getCS() {
        return CS;
    }

    public void setCS(net.ooder.esd.tool.properties.CS CS) {
        this.CS = CS;
    }


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getKey() {
        return typeKey.getClassName();
    }

    public void setKey(String key) {
        this.typeKey = ComponentType.fromType(key);
    }

    public T getProperties() {
        return properties;
    }

    public void setProperties(T properties) {
        this.properties = properties;
    }


    public void merge(Component<T, K> component) {
        this.setProperties(component.getProperties());
        this.setEvents(component.getEvents());
        this.setCS(component.getCS());
        this.setTarget(component.getTarget());
        ComponentList components = component.getChildren();
        if (components != null) {
            for (Component sub : components) {
                this.addChildren(sub);
            }
        }
    }

    private Component getComponentByAlias(String alias) {
        for (Component sub : children) {
            if (sub.getAlias() != null && sub.getAlias().equals(alias)) {
                return sub;
            }
        }
        return null;
    }

    public ComponentList getChildren() {
        return children;
    }

    public Component guessCurrComponent() {
        List<Component> childs = this.getChildren();
        if (childs != null) {
            for (Component component : childs) {
                if (component.getKey().equals(ComponentType.BLOCK.getType())) {
                    return component;
                }
            }
            for (Component component : childs) {
                if (component.guessCurrComponent() != null) {
                    return component;
                }
            }
        }
        return null;
    }

    //for json
    public void setChildren(ComponentList childrens) {
        for (Component component : childrens) {
            this.addChildren(component);
        }
        // this.children = childrens;
    }

    public String toJson() {
        return JSONObject.toJSONString(this, true);
    }


    @Override
    public String toString() {
        Properties properties = this.getProperties();
        String desc = "";
        if (properties != null) {
            desc = properties.getDesc();
            if (desc == null) {

                if (properties instanceof AbsUIProperties) {
                    desc = ((AbsUIProperties) properties).getCaption();
                } else {
                    desc = typeKey.getName();
                }
            }

            if (desc == null) {
                desc = typeKey.getName();
            }

            if (desc != null) {
                desc = "(" + desc + ")";
            }
        }
        return getAlias() + desc;
    }

}
