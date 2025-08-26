package net.ooder.esd.tool.properties.list;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.bean.TreeListItem;

import java.util.ArrayList;
import java.util.List;

public class TreeListProperties<T extends TreeListItem> extends AbsListProperties<T> {

    public Boolean lazyLoad;
    public String optBtn;
    public Boolean singleOpen;
    public String dropKeys;
    public String dragKey;


    public TreeListProperties() {
    }


    @JSONField(serialize = false)
    public List<T> getChildrenRecursivelyList() {
        List<T> allChildList = new ArrayList<>();
        for (T item : items) {
            allChildList.add(item);
            allChildList.addAll(item.getChildrenRecursivelyList());
        }
        return allChildList;
    }


    public Boolean getLazyLoad() {
        return lazyLoad;
    }

    public void setLazyLoad(Boolean lazyLoad) {
        this.lazyLoad = lazyLoad;
    }


    public String getOptBtn() {
        return optBtn;
    }

    public void setOptBtn(String optBtn) {
        this.optBtn = optBtn;
    }

    public Boolean getSingleOpen() {
        return singleOpen;
    }

    public void setSingleOpen(Boolean singleOpen) {
        this.singleOpen = singleOpen;
    }

    public String getDropKeys() {
        return dropKeys;
    }

    public void setDropKeys(String dropKeys) {
        this.dropKeys = dropKeys;
    }

    public String getDragKey() {
        return dragKey;
    }

    public void setDragKey(String dragKey) {
        this.dragKey = dragKey;
    }


}
