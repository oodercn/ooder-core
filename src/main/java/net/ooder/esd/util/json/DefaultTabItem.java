package net.ooder.esd.util.json;

import net.ooder.esd.annotation.field.TabItem;
import net.ooder.esd.tool.properties.item.TabListItem;

public class DefaultTabItem implements TabItem {
    String name;
    String id;
    String type;
    String imageClass;
    Class[] bindClass;
    boolean iniFold = false;
    boolean dynDestory = true;
    boolean dynLoad = false;

    String caption;

    String tips;

    public DefaultTabItem() {

    }

    public DefaultTabItem(TabItem tabItem) {
        init(tabItem);
    }

    public DefaultTabItem(TabListItem tabListItem) {
        if (tabListItem.getTabItem() != null) {
            init(tabListItem.getTabItem());
        } else {
            this.name = tabListItem.getName();
            this.type = tabListItem.getType() != null ? tabListItem.getType().name() : tabListItem.getName();
            this.imageClass = tabListItem.getImageClass();
            this.bindClass = tabListItem.getBindClass();
            this.caption=tabListItem.getCaption();
            this.tips=tabListItem.getTips();
        }


    }

    void init(TabItem tabItem) {
        this.type = tabItem.getType();
        this.name = tabItem.getName();
        this.caption=tabItem.getCaption();
        this.tips=tabItem.getTips();
        this.imageClass = tabItem.getImageClass();
        this.bindClass = tabItem.getBindClass();
        this.iniFold = tabItem.isIniFold();
        this.dynDestory = tabItem.isDynDestory();
        this.dynLoad = tabItem.isDynLoad();
        if (tabItem.getClass().isEnum()) {
            this.id = ((Enum) tabItem).name();
        }else{
            this.id=type;
        }


    }

    @Override
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean isDynDestory() {
        return dynDestory;
    }

    public void setDynDestory(boolean dynDestory) {
        this.dynDestory = dynDestory;
    }

    @Override
    public boolean isDynLoad() {
        return dynLoad;
    }

    public void setDynLoad(boolean dynLoad) {
        this.dynLoad = dynLoad;
    }
}
