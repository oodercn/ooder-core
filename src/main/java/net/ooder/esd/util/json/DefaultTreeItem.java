package net.ooder.esd.util.json;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.TreeItem;
import net.ooder.esd.bean.TreeListItem;

public class DefaultTreeItem implements TreeItem {
    String name;
    String type;
    String imageClass;
    @JSONField(deserializeUsing = BindClassArrDeserializer.class)
    Class[] bindClass;
    boolean iniFold;
    boolean dynDestory;
    boolean lazyLoad;

    public DefaultTreeItem() {

    }

    public DefaultTreeItem(String name, String type, String imageClass) {
        this.name = name;
        this.type = type;
        this.imageClass = imageClass;
    }


    public DefaultTreeItem(TreeListItem tabItem) {
        this.name = tabItem.getCaption();
        this.type = tabItem.getEnumName();
        this.imageClass = tabItem.getImageClass();
        this.bindClass = tabItem.getBindClass();
        if (tabItem.getIniFold() != null) {
            this.iniFold = tabItem.getIniFold();
        }
        if (tabItem.getDynDestory() != null) {
            this.dynDestory = tabItem.getDynDestory();
        }
        if (tabItem.getDynLoad() != null) {
            this.lazyLoad = tabItem.getDynLoad();
        }
    }


    public DefaultTreeItem(TreeItem tabItem) {
        this.name = tabItem.getName();
        this.type = tabItem.getType();
        this.imageClass = tabItem.getImageClass();
        this.bindClass = tabItem.getBindClass();
        this.iniFold = tabItem.isIniFold();
        this.dynDestory = tabItem.isDynDestory();
        this.lazyLoad = tabItem.isLazyLoad();
    }


    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }

    @Override
    public Class[] getBindClass() {
        return bindClass;
    }

    public void setBindClass(Class[] bindClass) {
        this.bindClass = bindClass;
    }

    @Override
    public boolean isIniFold() {
        return iniFold;
    }

    public void setIniFold(boolean iniFold) {
        this.iniFold = iniFold;
    }

    @Override
    public boolean isDynDestory() {
        return dynDestory;
    }

    public void setDynDestory(boolean dynDestory) {
        this.dynDestory = dynDestory;
    }

    @Override
    public boolean isLazyLoad() {
        return lazyLoad;
    }

    public void setLazyLoad(boolean lazyLoad) {
        this.lazyLoad = lazyLoad;
    }
}
