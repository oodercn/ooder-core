package net.ooder.esd.tool.component;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.event.TabsEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.custom.DataComponent;
import net.ooder.esd.custom.properties.ButtonViewsListItem;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.ButtonViewsProperties;

import java.util.List;

public class MButtonViewsComponent extends Component<ButtonViewsProperties, TabsEventEnum> implements DataComponent<List<ButtonViewsListItem>> {

    public MButtonViewsComponent addAction(Action<TabsEventEnum> action) {
        super.addAction( action);
        return this;
    }


    public MButtonViewsComponent(String alias, ButtonViewsProperties properties) {
        super(ComponentType.MBUTTONVIEWS, alias);
        this.setProperties(properties);
    }


    public MButtonViewsComponent(ComponentType type) {
        super(type);
    }

    public MButtonViewsComponent() {
        super(ComponentType.MBUTTONVIEWS);
    }

    @Override
    @JSONField(serialize = false)
    public List<ButtonViewsListItem> getData() {
        return  this.getProperties().getItems();
    }

    @Override
    public void setData(List<ButtonViewsListItem> data) {
        this.getProperties().setItems(data);
    }
}