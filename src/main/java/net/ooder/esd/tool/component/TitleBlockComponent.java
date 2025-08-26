package net.ooder.esd.tool.component;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.event.TitleBlockEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.custom.DataComponent;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.TitleBlockProperties;
import net.ooder.esd.tool.properties.item.TitleBlockItem;

import java.util.List;


public class TitleBlockComponent extends Component<TitleBlockProperties, TitleBlockEventEnum> implements DataComponent<List<TitleBlockItem>> {


    public TitleBlockComponent addAction(Action<TitleBlockEventEnum> action) {
        super.addAction( action);
        return this;
    }

    public TitleBlockComponent(String alias) {
        super(ComponentType.TITLEBLOCK, alias);
        this.setProperties( new TitleBlockProperties());

    }

    public TitleBlockComponent(String alias, TitleBlockProperties properties) {
        super(ComponentType.TITLEBLOCK, alias);
        this.setProperties(properties);

    }

    public TitleBlockComponent(ComponentType typeKey, TitleBlockProperties properties) {
        super(ComponentType.TITLEBLOCK);
        this.setProperties(properties);

    }

    public TitleBlockComponent() {
        super(ComponentType.TITLEBLOCK);
        this.setProperties( new TitleBlockProperties());
    }

    @Override
    @JSONField(serialize = false)
    public List<TitleBlockItem> getData() {
        return  this.getProperties().getItems();
    }

    @Override
    public void setData(List<TitleBlockItem> data) {
        this.getProperties().setItems(data);
    }


}
