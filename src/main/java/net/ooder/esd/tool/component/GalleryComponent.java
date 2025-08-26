package net.ooder.esd.tool.component;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.event.GalleryEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.view.CustomGalleryViewBean;
import net.ooder.esd.custom.DataComponent;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.GalleryProperties;
import net.ooder.esd.tool.properties.item.GalleryItem;

import java.util.List;


public class GalleryComponent extends Component<GalleryProperties, GalleryEventEnum> implements DataComponent<List<GalleryItem>> {



    public GalleryComponent(CustomGalleryViewBean viewBean) {
        super(ComponentType.GALLERY, viewBean.getName());
        this.setProperties(new GalleryProperties(viewBean));
    }

    public GalleryComponent addAction(Action<GalleryEventEnum> action) {
        super.addAction( action);
        return this;
    }

    public GalleryComponent(String alias) {
        super(ComponentType.GALLERY, alias);
        this.setProperties(new GalleryProperties());

    }

    public GalleryComponent(String alias, GalleryProperties properties) {
        super(ComponentType.GALLERY, alias);
        this.setProperties(properties);

    }

    public GalleryComponent(ComponentType typeKey, GalleryProperties properties) {
        super(ComponentType.GALLERY);
        this.setProperties(properties);

    }

    public GalleryComponent() {
        super(ComponentType.GALLERY);
        this.setProperties( new GalleryProperties());
    }

    @Override
    @JSONField(serialize = false)
    public List<GalleryItem> getData() {
        return  this.getProperties().getItems();
    }

    @Override
    public void setData(List<GalleryItem> data) {
        this.getProperties().setItems(data);
    }


}
