package net.ooder.esd.custom.properties;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.ui.HAlignType;
import net.ooder.esd.annotation.ui.VAlignType;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.tool.properties.list.AbsListProperties;

import java.util.ArrayList;
import java.util.List;

public class BarProperties<T extends TreeListItem> extends AbsListProperties<T> {

    public Boolean handler;
    @JSONField(name = "HAlign")
    public HAlignType hAlign;
    @JSONField(name = "VAlign")
    public VAlignType vAlign;
    public Boolean lazy;
    public Boolean dynLoad = false;

    public BarProperties() {

    }

    public List<T> addItem(T item) {
        if (items == null) {
            items = new ArrayList<T>();
        }
        if (!items.contains(item)){
            items.add(item);
        }

        return items;
    }



    public Boolean getDynLoad() {
        return dynLoad;
    }

    public void setDynLoad(Boolean dynLoad) {
        this.dynLoad = dynLoad;
    }


    public Boolean getLazy() {
        return lazy;
    }

    public void setLazy(Boolean lazy) {
        this.lazy = lazy;
    }

    public HAlignType gethAlign() {
        return hAlign;
    }

    public void sethAlign(HAlignType hAlign) {
        this.hAlign = hAlign;
    }

    public VAlignType getvAlign() {
        return vAlign;
    }

    public void setvAlign(VAlignType vAlign) {
        this.vAlign = vAlign;
    }

    public Boolean getHandler() {
        return handler;
    }

    public void setHandler(Boolean handler) {
        this.handler = handler;
    }

}
