package net.ooder.esd.tool.properties.list;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.Enumstype;
import net.ooder.annotation.IconEnumstype;
import net.ooder.esd.annotation.ui.BorderType;
import net.ooder.esd.annotation.ui.SelModeType;
import net.ooder.esd.tool.properties.AbsUIProperties;
import net.ooder.esd.tool.properties.ContainerProperties;
import net.ooder.esd.tool.properties.FieldProperties;
import net.ooder.esd.util.json.EnumsClassDeserializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AbsUIListProperties<T extends AbsUIProperties> extends FieldProperties {

    public List<T> items;
    public List<String> listKey;
    public Boolean dragSortable;
    public String valueSeparator;
    public SelModeType selMode;
    public BorderType borderType;
    public Boolean activeLast;
    @JSONField(deserializeUsing = EnumsClassDeserializer.class)
    public Class<? extends Enum> enumClass;


    public AbsUIListProperties() {

    }


    public AbsUIListProperties(Enum enumType) {
        this.setId(enumType.name());
        if (enumType instanceof IconEnumstype) {
            this.id = ((IconEnumstype) enumType).getType();
            this.caption = ((IconEnumstype) enumType).getName();
            this.imageClass = ((IconEnumstype) enumType).getImageClass();
        } else if (enumType instanceof Enumstype) {
            this.id = ((Enumstype) enumType).getType();
            this.caption = ((Enumstype) enumType).getName();
        }
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

    public void setEnumClass(Class<? extends Enum> enumClass) {
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
