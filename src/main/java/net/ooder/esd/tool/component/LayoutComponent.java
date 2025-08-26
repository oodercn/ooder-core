package net.ooder.esd.tool.component;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.event.PanelEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.annotation.ui.PosType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.LayoutProperties;

import java.util.Arrays;

public class LayoutComponent extends Component<LayoutProperties, PanelEventEnum> {
    ComponentType[] navTypes = new ComponentType[]{ComponentType.TREEVIEW, ComponentType.GALLERY, ComponentType.TREEBAR, ComponentType.FOLDINGTABS, ComponentType.TITLEBLOCK};

    public LayoutComponent(String alias, LayoutProperties properties) {
        super(ComponentType.LAYOUT, alias);
        this.setProperties(properties);

    }

    public LayoutComponent addAction(Action<PanelEventEnum> action) {
        super.addAction(action);
        return this;
    }

    public LayoutComponent(Dock dock, String alias) {
        super(ComponentType.LAYOUT, alias);
        this.setProperties(new LayoutProperties(dock));
    }

    public LayoutComponent(Component child, Dock dock) {
        super(ComponentType.LAYOUT, child.getAlias() + "Layout");
        this.setProperties(new LayoutProperties(dock));
        this.addChildren(child);
    }

    public LayoutComponent() {
        super(ComponentType.LAYOUT);
        this.setProperties(new LayoutProperties());
    }

    @JSONField(serialize = false)
    public Component getNavComponent() {
        ComponentList componentList = this.getChildren();
        for (Component component : componentList) {
            if (component.getTarget() != null && component.getTarget().equals(PosType.before.name())) {
                ComponentType componentType = ComponentType.fromType(component.getKey());
                if (Arrays.asList(navTypes).contains(componentType)) {
                    return component;
                }
                return findChild(component);
            }
        }


        return null;
    }


    private Component findChild(Component component) {
        ComponentList componentList = component.getChildrenRecursivelyList();
        for (Component childComponent : componentList) {
            ComponentType componentType = ComponentType.fromType(childComponent.getKey());
            if (Arrays.asList(navTypes).contains(componentType)) {
                return childComponent;
            }
        }
        return null;
    }


}
;
