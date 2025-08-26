package net.ooder.esd.tool.component;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.event.ContentBlockEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.custom.DataComponent;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.ContentBlockProperties;
import net.ooder.esd.tool.properties.item.ContentBlockItem;

import java.util.List;


public class ContentBlockComponent extends Component<ContentBlockProperties, ContentBlockEventEnum> implements DataComponent<List<ContentBlockItem>> {


    public ContentBlockComponent addAction(Action<ContentBlockEventEnum> action) {
        super.addAction(action);
        return this;
    }

    public ContentBlockComponent(String alias) {
        super(ComponentType.CONTENTBLOCK, alias);
        this.setProperties( new ContentBlockProperties());

    }

    public ContentBlockComponent(String alias, ContentBlockProperties properties) {
        super(ComponentType.CONTENTBLOCK, alias);
        this.setProperties(properties);

    }

    public ContentBlockComponent(ComponentType typeKey, ContentBlockProperties properties) {
        super(typeKey);
        this.setProperties(properties);

    }

    public ContentBlockComponent() {
        super(ComponentType.CONTENTBLOCK);
        this.setProperties( new ContentBlockProperties());
    }

    @Override
    @JSONField(serialize = false)
    public List<ContentBlockItem> getData() {
        return  this.getProperties().getItems();
    }

    @Override
    public void setData(List<ContentBlockItem> data) {
        this.getProperties().setItems(data);
    }


}
