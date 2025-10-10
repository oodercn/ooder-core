package net.ooder.esd.tool.properties.item;

import net.ooder.esd.annotation.CustomMenu;
import net.ooder.esd.annotation.ui.AlignType;
import net.ooder.esd.tool.properties.list.DataProperties;

import java.util.Map;

public class UIItem<T extends Enum> extends DataProperties<T> {

    public String itemClass;
    public String itemStyle;
    public String tips;
    public String title;
    public AlignType location;
    public String className;
    public Boolean hidden;
    public Integer index;


    public UIItem() {

    }

    public UIItem(CustomMenu menu) {
        if (menu.getClass().isEnum()) {
            init((T) menu);
        }
        this.id = menu.type();
        this.caption = menu.caption();
        this.imageClass = menu.imageClass();
    }


    public UIItem(T enumType) {
        this.init(enumType);
    }


    public UIItem(String id, String caption) {
        this.id = id;
        this.caption = caption;
        this.tips = caption;
        this.title = caption;
    }

    public UIItem(String id, String caption, String imageClass, String tips, Map<String, Object> params) {
        this.id = id;
        this.caption = caption;
        this.tips = tips;
        this.title = tips;
        this.imageClass = imageClass;
        this.tagVar = params;
    }

    public UIItem(String id, String caption, String imageClass) {
        this.id = id;
        this.caption = caption;
        this.tips = caption;
        this.title = tips;
        this.imageClass = imageClass;
    }


    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }


    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getTips() {
        if (tips == null || tips.equals("")) {
            tips = this.getTitle();
        }
        if (tips == null || tips.equals("")) {
            tips = this.getId();
        }
        return tips;
    }

    public String getTitle() {
        if (title == null || title.equals("")) {
            title = this.getCaption();
        }
        if (title == null || title.equals("")) {
            title = this.getDesc();
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getItemClass() {
        return itemClass;
    }

    public void setItemClass(String itemClass) {
        this.itemClass = itemClass;
    }

    public String getItemStyle() {
        return itemStyle;
    }

    public void setItemStyle(String itemStyle) {
        this.itemStyle = itemStyle;
    }

    public AlignType getLocation() {
        return location;
    }

    public void setLocation(AlignType location) {
        this.location = location;
    }
}
