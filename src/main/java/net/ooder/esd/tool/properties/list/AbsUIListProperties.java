package net.ooder.esd.tool.properties.list;

import net.ooder.esd.annotation.ui.BorderType;
import net.ooder.esd.annotation.ui.SelModeType;
import net.ooder.esd.tool.properties.AbsUIProperties;
import net.ooder.esd.tool.properties.ContainerUIProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AbsUIListProperties<T extends AbsUIProperties> extends ContainerUIProperties {

    public List<T> items;
    public List<String> listKey;
    public Boolean dragSortable;
    public String valueSeparator;
    public SelModeType selMode;
    public BorderType borderType;
    public Boolean activeLast;


    public AbsUIListProperties() {

    }


    public List<T> addItem(T item) {
        if (items == null) {
            items = new ArrayList<T>();
        }
        for (T oitem : items) {
            if (item.getId().equals(oitem.getId())) {
                return items;
            }
        }
        items.add(item);
        return items;
    }


    public void setActiveLast(Boolean activeLast) {

        this.activeLast = activeLast;
    }

    public List<String> getListKey() {
        return listKey;
    }

    public void setListKey(List<String> listKey) {
        this.listKey = listKey;
    }

    public List<T> getItems() {

        if (items != null) {
            Arrays.sort(items.toArray());
        } else {
            items = new ArrayList<>();
        }
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public Boolean getDragSortable() {
        return dragSortable;
    }

    public void setDragSortable(Boolean dragSortable) {
        this.dragSortable = dragSortable;
    }

    public String getValueSeparator() {
        return valueSeparator;
    }

    public void setValueSeparator(String valueSeparator) {
        this.valueSeparator = valueSeparator;
    }

    public SelModeType getSelMode() {
        return selMode;
    }

    public void setSelMode(SelModeType selMode) {
        this.selMode = selMode;
    }

    public BorderType getBorderType() {
        return borderType;
    }

    public void setBorderType(BorderType borderType) {
        this.borderType = borderType;
    }

    public Boolean getActiveLast() {
        return activeLast;
    }
}
