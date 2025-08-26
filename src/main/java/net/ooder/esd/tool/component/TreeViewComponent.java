package net.ooder.esd.tool.component;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.event.TreeViewEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.view.CustomTreeViewBean;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.custom.DataComponent;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.TreeViewProperties;

import java.util.List;

public class TreeViewComponent extends Component<TreeViewProperties, TreeViewEventEnum> implements DataComponent<List<TreeListItem>> {

    public TreeViewComponent(CustomTreeViewBean viewBean) {
        super(ComponentType.TREEVIEW, viewBean.getName());
        this.setProperties(new TreeViewProperties(viewBean));
    }

    public TreeViewComponent addAction(Action<TreeViewEventEnum> action) {
        super.addAction(action);
        return this;
    }

    public TreeViewComponent(String alias, TreeViewProperties properties) {
        super(ComponentType.TREEVIEW, alias);
        this.setProperties(properties);
    }


    public TreeViewComponent() {
        super(ComponentType.TREEVIEW);
        this.setProperties( new TreeViewProperties());
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
