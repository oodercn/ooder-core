package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.field.ImageFieldBean;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.form.ImageProperties;

public class ImageComponent extends FieldComponent<ImageProperties, FieldEventEnum> {


    public ImageComponent addAction(Action<FieldEventEnum> action) {
        super.addAction( action);
        return this;
    }
    public ImageComponent(String alias) {
        super(ComponentType.IMAGE, alias);
        this.setProperties(properties);
    }

    public ImageComponent(String alias, ImageFieldBean imageFieldBean, ContainerBean containerBean) {
        super(ComponentType.IMAGE, alias);
        this.properties =  new ImageProperties(imageFieldBean,containerBean);
        this.setProperties(properties);
    }


    public ImageComponent(String alias, ImageProperties properties) {
        super(ComponentType.IMAGE, alias);
        this.setProperties(properties);
    }

    public ImageComponent() {
        super(ComponentType.IMAGE);
        this.setProperties( new ImageProperties());
    }
}
