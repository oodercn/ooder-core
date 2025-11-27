package net.ooder.esd.tool.properties.list;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.ui.BorderType;
import net.ooder.esd.annotation.ui.SelModeType;
import net.ooder.esd.util.json.EnumsClassDeserializer;
import net.ooder.esd.tool.properties.AbsUIProperties;
import net.ooder.esd.tool.properties.FieldProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AbsListProperties<T extends AbsUIProperties> extends FieldProperties {


    public List<String> listKey;
    public List<T> items;
    public Boolean dragSortable;
    public String valueSeparator;
    @JSONField(deserializeUsing = EnumsClassDeserializer.class)
    public Class<? extends Enum>  enumClass;
    public SelModeType selMode;
    public BorderType borderType;
    public Boolean activeLast;




    public AbsListProperties() {

    }

    public List<T> addItem(T item) {
        if (items == null) {
            items = new ArrayList<T>();
        }
        T ooitem = null;
        for (T oitem : items) {
            if (item.getId().equals(oitem.getId())) {
                ooitem = oitem;
            }
        }
        if (ooitem != null) {
            int k = items.indexOf(ooitem);
            items.remove(ooitem);
            items.add(k, item);
        } else {
            items.add(item);
        }
        return items;
    }

    public Class<? extends Enum> getEnumClass() {
        return enumClass;
    }

    public void setEnumClass(Class<? extends Enum>  enumClass) {
        this.enumClass = enumClass;
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
