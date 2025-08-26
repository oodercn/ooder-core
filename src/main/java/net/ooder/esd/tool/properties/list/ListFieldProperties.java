package net.ooder.esd.tool.properties.list;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.annotation.ui.ItemRow;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.ListMenuBean;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.bean.field.CustomListBean;
import net.ooder.esd.bean.field.base.ListFieldBean;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListFieldProperties extends AbsListProperties<TreeListItem> {


    public ItemRow itemRow;
    public Boolean read;
    public ComboInputType type;


    public ListFieldProperties() {

    }

    public ListFieldProperties(ListMenuBean listMenuBean) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(listMenuBean), Map.class), this, false, false);
    }


    public ListFieldProperties(ListFieldBean fieldBean, ContainerBean containerBean) {
        super();
        CustomListBean customListBean = fieldBean.getCustomListBean();
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(fieldBean), Map.class), this, false, false);
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(customListBean), Map.class), this, false, false);
        if (containerBean != null) {
            this.init(containerBean);
        }
        this.setItemRow(fieldBean.getItemRow());
        this.setDock(fieldBean.getDock());
        this.setDynLoad(customListBean.getDynLoad());
        items = customListBean.getItems();

    }


    public List<TreeListItem> addItem(TreeListItem item) {
        if (items == null) {
            items = new ArrayList<TreeListItem>();
        }
        if (item != null) {
            for (TreeListItem oitem : items) {
                if (item.getId().equals(oitem.getId())) {
                    return items;
                }
            }
            items.add(item);
        }
        return items;
    }


    public ItemRow getItemRow() {
        return itemRow;
    }

    public void setItemRow(ItemRow itemRow) {
        this.itemRow = itemRow;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public ComboInputType getType() {
        return type;
    }

    public void setType(ComboInputType type) {
        this.type = type;
    }
}
