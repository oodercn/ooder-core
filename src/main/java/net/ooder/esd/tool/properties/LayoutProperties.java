package net.ooder.esd.tool.properties;


import net.ooder.esd.annotation.LayoutAnnotation;
import net.ooder.esd.annotation.LayoutItemAnnotation;
import net.ooder.esd.annotation.ui.BorderType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.annotation.ui.LayoutType;
import net.ooder.esd.annotation.ui.NavComboType;
import net.ooder.esd.bean.CustomLayoutItemBean;
import net.ooder.esd.bean.view.CustomLayoutViewBean;
import net.ooder.esd.tool.properties.item.LayoutListItem;
import net.ooder.esd.tool.properties.list.AbsUIListProperties;

import java.util.Arrays;
import java.util.List;

public class LayoutProperties extends AbsUIListProperties<LayoutListItem> {


    public LayoutType type;
    public Boolean dragSortable;

    public List<String> listKey;
    public Boolean flexSize;
    public Boolean transparent;
    public Integer conLayoutColumns;
    public NavComboType navComboType;

    public String top;
    public BorderType borderType;
    public String left;


    public LayoutProperties() {

    }

    public LayoutProperties(Dock dock) {
        this.dock = dock;
    }

    public LayoutProperties(CustomLayoutViewBean layoutViewBean) {
        this.type = layoutViewBean.getType();
        this.left = layoutViewBean.getLeft();
        this.borderType = layoutViewBean.getBorderType();
        this.top = layoutViewBean.getTop();
        this.transparent = layoutViewBean.getTransparent();
        this.dragSortable = layoutViewBean.getDragSortable();
        this.flexSize = layoutViewBean.getFlexSize();
        this.conLayoutColumns = layoutViewBean.getConLayoutColumns();
        if (layoutViewBean.getListKey() != null && layoutViewBean.getListKey().size() > 0) {
            listKey = layoutViewBean.getListKey();
        }
        List<CustomLayoutItemBean> itemBeanList = layoutViewBean.getLayoutItems();
        for (CustomLayoutItemBean layoutItemBean : itemBeanList) {
            this.getItems().add(new LayoutListItem(layoutItemBean));
        }
    }

    public LayoutProperties(LayoutAnnotation layoutAnnotation) {
        this.type = layoutAnnotation.type();
        this.left = layoutAnnotation.left();
        this.borderType = layoutAnnotation.borderType();
        this.top = layoutAnnotation.top();
        this.transparent = layoutAnnotation.transparent();
        this.dragSortable = layoutAnnotation.dragSortable();
        this.flexSize = layoutAnnotation.flexSize();
        if (layoutAnnotation.conLayoutColumns() != -1) {
            conLayoutColumns = layoutAnnotation.conLayoutColumns();
        }
        if (layoutAnnotation.listKey().length > 0) {
            listKey = Arrays.asList(layoutAnnotation.listKey());
        }
        for (LayoutItemAnnotation itemAnnotation : layoutAnnotation.layoutItems()) {
            this.getItems().add(new LayoutListItem(itemAnnotation));
        }
    }

    public NavComboType getNavComboType() {
        return navComboType;
    }

    public void setNavComboType(NavComboType navComboType) {
        this.navComboType = navComboType;
    }

    @Override
    public Integer getConLayoutColumns() {
        return conLayoutColumns;
    }

    @Override
    public void setConLayoutColumns(Integer conLayoutColumns) {
        this.conLayoutColumns = conLayoutColumns;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    @Override
    public BorderType getBorderType() {
        return borderType;
    }

    @Override
    public void setBorderType(BorderType borderType) {
        this.borderType = borderType;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public LayoutType getType() {
        return type;
    }

    public void setType(LayoutType type) {
        this.type = type;
    }

    public Boolean getDragSortable() {
        return dragSortable;
    }

    public void setDragSortable(Boolean dragSortable) {
        this.dragSortable = dragSortable;
    }


    public List<String> getListKey() {
        return listKey;
    }

    public void setListKey(List<String> listKey) {
        this.listKey = listKey;
    }

    public Boolean getFlexSize() {
        return flexSize;
    }

    public void setFlexSize(Boolean flexSize) {
        this.flexSize = flexSize;
    }

    public Boolean getTransparent() {
        return transparent;
    }

    public void setTransparent(Boolean transparent) {
        this.transparent = transparent;
    }

}
;
