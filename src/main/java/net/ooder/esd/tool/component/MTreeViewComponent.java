package net.ooder.esd.tool.component;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.event.TreeViewEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.view.CustomMTreeViewBean;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.custom.DataComponent;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.TreeViewProperties;

import java.util.List;

public class MTreeViewComponent extends Component<TreeViewProperties, TreeViewEventEnum> implements DataComponent<List<TreeListItem>> {

    public MTreeViewComponent(CustomMTreeViewBean viewBean) {
        super(ComponentType.MTREEVIEW, viewBean.getName());
        this.setProperties(new TreeViewProperties(viewBean));
    }

    public MTreeViewComponent addAction(Action<TreeViewEventEnum> action) {
        super.addAction(action);
        return this;
    }

    public MTreeViewComponent(String alias, TreeViewProperties properties) {
        super(ComponentType.MTREEVIEW, alias);
        this.setProperties(properties);
    }


    public MTreeViewComponent() {
        super(ComponentType.MTREEVIEW);
        this.setProperties(new TreeViewProperties());
    }

    @Override
    @JSONField(serialize = false)
    public List<TreeListItem> getData() {
        return (List<TreeListItem>) this.getProperties().getItems();
    }

    @Override
    public void setData(List data) {
        this.getProperties().setItems(data);
    }
}
