package net.ooder.esd.tool.component;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.event.TabsEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.custom.DataComponent;
import net.ooder.esd.custom.properties.NavTabListItem;
import net.ooder.esd.custom.properties.NavTabsProperties;
import net.ooder.esd.tool.properties.AbsUIProperties;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.PanelProperties;
import net.ooder.esd.tool.properties.item.TabListItem;

import java.util.List;

public class TabsComponent<T extends NavTabsProperties> extends Component<T, TabsEventEnum> implements DataComponent<List<NavTabListItem>> {

    public TabsComponent addAction(Action<TabsEventEnum> action) {
        super.addAction( action);
        return this;
    }

    public TabsComponent(String alias, T properties) {
        super(ComponentType.TABS, alias);
        this.setProperties(properties);
    }

    public TabsComponent(String alias) {
        super(ComponentType.TABS, alias);
        this.setProperties((T) new NavTabsProperties());
    }

    public void addChildComponet(Component... components) {
        for (Component component : components) {
            NavTabListItem item = null;
            if (component instanceof ModuleComponent) {
                ModuleComponent moduleComponent = (ModuleComponent) component;
                item = new NavTabListItem(component.getAlias() + "Item", moduleComponent.getTitle(), moduleComponent.getImageClass());
            } else if (component instanceof PanelComponent) {
                PanelComponent<PanelProperties> panelComponent = (PanelComponent) component;
                item = new NavTabListItem(component.getAlias() + "Item", panelComponent.getProperties().getCaption(), panelComponent.getProperties().getImageClass());
            } else {
                AbsUIProperties uiProperties = (AbsUIProperties) component.getProperties();
                item = new NavTabListItem(component.getAlias() + "Item", uiProperties.getCaption(), null);
            }

            this.getProperties().addItem(item);
            this.getProperties().setValue(((TabListItem) this.getProperties().getItems().get(0)).getId());
            component.setTarget(item.getId());
            this.addChildren(component);
        }
    }


    public TabsComponent(ComponentType type) {
        super(type);
    }

    public TabsComponent() {
        super(ComponentType.TABS);
        this.setProperties((T) new NavTabsProperties());
    }

    @Override
    @JSONField(serialize = false)
    public List<NavTabListItem> getData() {
        return this.getProperties().getItems();
    }

    @Override
    public void setData(List<NavTabListItem> data) {
        this.getProperties().setItems(data);
    }

}