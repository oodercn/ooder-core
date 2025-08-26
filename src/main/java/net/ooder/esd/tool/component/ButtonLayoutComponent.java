package net.ooder.esd.tool.component;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.event.GalleryEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.custom.DataComponent;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.ButtonLayoutProperties;
import net.ooder.esd.tool.properties.item.ButtonLayoutItem;

import java.util.List;


public class ButtonLayoutComponent extends Component<ButtonLayoutProperties, GalleryEventEnum> implements DataComponent<List<ButtonLayoutItem>> {


    public ButtonLayoutComponent addAction(Action<GalleryEventEnum> action) {
        super.addAction(action);
        return this;
    }

    public ButtonLayoutComponent(String alias) {
        super(ComponentType.BUTTONLAYOUT, alias);
        this.setProperties( new ButtonLayoutProperties());

    }

    public ButtonLayoutComponent(String alias, ButtonLayoutProperties properties) {
        super(ComponentType.BUTTONLAYOUT, alias);
        this.setProperties(properties);

    }

    public ButtonLayoutComponent(ComponentType typeKey, ButtonLayoutProperties properties) {
        super(ComponentType.BUTTONLAYOUT);
        this.setProperties(properties);

    }

    public ButtonLayoutComponent() {
        super(ComponentType.BUTTONLAYOUT);
        this.setProperties( new ButtonLayoutProperties());
    }

    @Override
    @JSONField(serialize = false)
    public List<ButtonLayoutItem> getData() {
        return  this.getProperties().getItems();
    }

    @Override
    public void setData(List<ButtonLayoutItem> data) {
        this.getProperties().setItems(data);
    }


}
