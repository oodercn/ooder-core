package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.event.PageEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.custom.DataComponent;
import net.ooder.esd.tool.properties.PageBarProperties;

public class PageBarComponent<T extends PageBarProperties, K extends PageEventEnum, V extends Integer> extends Component<T, K> implements DataComponent<V> {


    public PageBarComponent(String alias) {
        super(ComponentType.PAGEBAR, alias);
        this.setProperties((T) new PageBarProperties());

    }

    public PageBarComponent(String alias, T properties) {
        super(ComponentType.PAGEBAR, alias);
        this.setProperties(properties);
    }

    public PageBarComponent() {
        super(ComponentType.PAGEBAR);
        this.setProperties((T) new PageBarProperties());
    }

    @Override
    public V getData() {
        return (V) this.getProperties().getPageCount();
    }

    @Override
    public void setData(V data) {
        this.getProperties().setPageCount(data);
    }
}
