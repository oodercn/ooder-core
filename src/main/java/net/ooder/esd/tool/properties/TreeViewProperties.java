package net.ooder.esd.tool.properties;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.bean.view.CustomTreeViewBean;
import net.ooder.esd.bean.TreeListItem;

import java.util.List;

public class TreeViewProperties<T extends TreeListItem> extends TreeBarProperties<T> {


    @JSONField(serialize = false)
    public String fristId;


    public TreeViewProperties() {
        super();
    }

    public TreeViewProperties(CustomTreeViewBean customTreeViewBean) {
        super(customTreeViewBean);
    }

    private String getFristId(T itemInfo) {
        if (itemInfo.getEuClassName() != null && !itemInfo.getEuClassName().equals("")) {
            return itemInfo.getId();
        } else {
            List<T> sub = itemInfo.getSub();
            if (sub != null && sub.size() > 0) {
                for (T item : sub) {
                    String id = getFristId(item);
                    if (id != null) {
                        return id;
                    }
                }
            }
        }
        return null;
    }



    public String getFristId() {
        if (fristId == null) {
            List<T> items = this.getItems();
            if (items.size() > 0) {
                T itemProperties = items.get(0);
                fristId = getFristId(itemProperties);

            } else {
                fristId = this.getId();
            }
        }

        return fristId;
    }

    public void setFristId(String fristId) {
        this.fristId = fristId;
    }
}
