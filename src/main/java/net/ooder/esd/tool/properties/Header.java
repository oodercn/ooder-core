package net.ooder.esd.tool.properties;

import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.tool.properties.item.UIItem;

import java.util.ArrayList;
import java.util.List;

public class Header extends UIItem {

    public String headerStyle;
    public Boolean colResizer;
    public Boolean editable;
    public Boolean flexSize;
    public ComboInputType type;
    public Class<? extends Enum> enumClass;
    public List<TreeListItem> editorListItems;

    public Header() {

    }

    public Header(String id, String caption) {
        super(id, caption);
    }


    public ComboInputType getType() {
        return type;
    }

    public void setType(ComboInputType type) {
        this.type = type;
    }


    public List<TreeListItem> getEditorListItems() {
        return editorListItems;
    }

    public void setEditorListItems(List<TreeListItem> editorListItems) {
        this.editorListItems = editorListItems;
    }


    public String getHeaderStyle() {
        return headerStyle;
    }

    public void setHeaderStyle(String headerStyle) {
        this.headerStyle = headerStyle;
    }

    public boolean isColResizer() {
        return colResizer;
    }

    public void setColResizer(boolean colResizer) {
        this.colResizer = colResizer;
    }


    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isFlexSize() {
        return flexSize;
    }

    public void setFlexSize(boolean flexSize) {
        this.flexSize = flexSize;
    }


    public boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    public Header addSelectItem(TreeListItem... items) {
        if (editorListItems == null) {
            editorListItems = new ArrayList<TreeListItem>();
        }

        for (TreeListItem item : items) {
            TreeListItem colItem = new TreeListItem(item.getId(), item.getCaption());
            editorListItems.add(colItem);
        }

        return this;
    }

    public Class<? extends Enum> getEnumClass() {
        return enumClass;
    }

    public void setEnumClass(Class<? extends Enum> enumClass) {
        this.enumClass = enumClass;
    }

    public Boolean getColResizer() {
        return colResizer;
    }

    public void setColResizer(Boolean colResizer) {
        this.colResizer = colResizer;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public Boolean getFlexSize() {
        return flexSize;
    }

    public void setFlexSize(Boolean flexSize) {
        this.flexSize = flexSize;
    }

}
