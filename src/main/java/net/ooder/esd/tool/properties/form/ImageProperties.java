package net.ooder.esd.tool.properties.form;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.annotation.ui.CursorType;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.field.ImageFieldBean;
import net.ooder.esd.tool.properties.FieldProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.List;
import java.util.Map;


public class ImageProperties extends FieldProperties {
    Integer maxWidth;
    Integer maxHeight;
    String src;
    String alt;
    List<ImageProperties> items;
    String dragKey;
    String activeItem;
    CursorType cursor;
    String dataBinder;
    String dataField;


    public ImageProperties() {

    }


    public ImageProperties(ImageFieldBean fieldBean, ContainerBean containerBean) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(fieldBean), Map.class), this, false, false);
        if (containerBean != null) {
            this.init(containerBean);
        }

    }




    public String getDataBinder() {
        return dataBinder;
    }

    public void setDataBinder(String dataBinder) {
        this.dataBinder = dataBinder;
    }

    public String getDataField() {
        return dataField;
    }

    public void setDataField(String dataField) {
        this.dataField = dataField;
    }

    public String getDragKey() {
        return dragKey;
    }

    public void setDragKey(String dragKey) {
        this.dragKey = dragKey;
    }


    public Integer getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(Integer maxWidth) {
        this.maxWidth = maxWidth;
    }

    public Integer getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(Integer maxHeight) {
        this.maxHeight = maxHeight;
    }


    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public List<ImageProperties> getItems() {
        return items;
    }

    public void setItems(List<ImageProperties> items) {
        this.items = items;
    }

    public String getActiveItem() {
        return activeItem;
    }

    public void setActiveItem(String activeItem) {
        this.activeItem = activeItem;
    }

    public CursorType getCursor() {
        return cursor;
    }

    public void setCursor(CursorType cursor) {
        this.cursor = cursor;
    }


}
