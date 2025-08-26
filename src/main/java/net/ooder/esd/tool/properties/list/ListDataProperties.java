package net.ooder.esd.tool.properties.list;

import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.TreeListItem;

import java.util.ArrayList;
import java.util.List;

public class ListDataProperties<T extends TreeListItem> extends DataProperties {



    public String listKey;
    public List<T> items;
    public Boolean dragSortable;
    public String valueSeparator;
    public ItemRow itemRow;
    public SelModeType selMode;
    public BorderType borderType;
    public Boolean activeLast;
    public String labelSize;
    public String labelCaption;
    public String labelVAlign;
    public String labelHAlign;
    public LabelPos labelPos;
    public Boolean read;
    public ComboInputType type;


    public ListDataProperties() {

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

    public String getListKey() {
        return listKey;
    }

    public void setListKey(String listKey) {
        this.listKey = listKey;
    }

    public List<T> getItems() {
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

    public ItemRow getItemRow() {
        return itemRow;
    }

    public void setItemRow(ItemRow itemRow) {
        this.itemRow = itemRow;
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

    public void setActiveLast(Boolean activeLast) {
        this.activeLast = activeLast;
    }

    public String getLabelSize() {
        return labelSize;
    }

    public void setLabelSize(String labelSize) {
        this.labelSize = labelSize;
    }

    public String getLabelCaption() {
        return labelCaption;
    }

    public void setLabelCaption(String labelCaption) {
        this.labelCaption = labelCaption;
    }

    public String getLabelVAlign() {
        return labelVAlign;
    }

    public void setLabelVAlign(String labelVAlign) {
        this.labelVAlign = labelVAlign;
    }

    public String getLabelHAlign() {
        return labelHAlign;
    }

    public void setLabelHAlign(String labelHAlign) {
        this.labelHAlign = labelHAlign;
    }

    public LabelPos getLabelPos() {
        return labelPos;
    }

    public void setLabelPos(LabelPos labelPos) {
        this.labelPos = labelPos;
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
