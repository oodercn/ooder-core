package net.ooder.esd.tool.properties;

import com.alibaba.fastjson.JSON;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.view.CustomButtonLayoutViewBean;
import net.ooder.esd.bean.field.CustomButtonLayoutFieldBean;
import net.ooder.esd.tool.properties.item.ButtonLayoutItem;
import net.ooder.esd.tool.properties.item.CmdItem;
import net.ooder.esd.util.ESDEnumsUtil;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.List;
import java.util.Map;

public class ButtonLayoutProperties extends TabsProperties<ButtonLayoutItem> {
    public List<CmdItem> tagCmds;
    public String caption;
    public String imageClass;
    public String position;
    public String bgimg;
    public String iotStatus;
    public Boolean autoImgSize;
    public Boolean autoItemSize;
    public Boolean iconOnly;
    public String itemMargin;
    public String itemPadding;
    public String itemWidth;
    public String itemHeight;
    public String imgWidth;
    public String imgHeight;
    public Integer columns;
    public Integer rows;
    public String iconFontSize;
    public String width;
    public String height;
    public Boolean showDirtyMark;

    public ButtonLayoutProperties() {

    }

    public ButtonLayoutProperties(CustomButtonLayoutViewBean galleryViewBean) {
        init(galleryViewBean);
    }

    public ButtonLayoutProperties(CustomButtonLayoutViewBean galleryViewBean, ContainerBean containerBean) {
        init(galleryViewBean);
        if (containerBean != null) {
            this.init(containerBean);
        }


    }


    public ButtonLayoutProperties(CustomButtonLayoutFieldBean galleryViewBean) {
        this.init(galleryViewBean.getContainerBean().getUiBean());
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(galleryViewBean), Map.class), this, false, false);
    }


    public void init(CustomButtonLayoutViewBean buttonLayoutViewBean) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(buttonLayoutViewBean), Map.class), this, false, false);
        this.init(buttonLayoutViewBean.getContainerBean());
        List<ButtonLayoutItem> buttonLayoutItems = buttonLayoutViewBean.getButtonLayoutItemBeans();

        if (buttonLayoutItems != null) {
            for (ButtonLayoutItem buttonLayoutItem : buttonLayoutItems) {
                this.addItem(buttonLayoutItem);
            }
        }

        if (this.getItems() == null || this.getItems().isEmpty()) {
            Class<? extends Enum> enumClass = buttonLayoutViewBean.getEnumClass();
            Class<? extends Enum> viewClass = null;
            String viewClassName = buttonLayoutViewBean.getViewClassName();
            if (viewClassName != null) {
                Class clazz = null;
                try {
                    clazz = ClassUtility.loadClass(viewClassName);
                    if (clazz.isEnum()) {
                        viewClass = clazz;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            if (viewClass != null) {
                this.items = ESDEnumsUtil.getEnumItems(viewClass, ButtonLayoutItem.class);
            } else if (enumClass != null) {
                this.items = ESDEnumsUtil.getEnumItems(enumClass, ButtonLayoutItem.class);
            }
        }


    }

    public Boolean getShowDirtyMark() {
        return showDirtyMark;
    }

    public void setShowDirtyMark(Boolean showDirtyMark) {
        this.showDirtyMark = showDirtyMark;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getIconFontSize() {
        return iconFontSize;
    }

    public void setIconFontSize(String iconFontSize) {
        this.iconFontSize = iconFontSize;
    }

    public List<CmdItem> getTagCmds() {
        return tagCmds;
    }


    public void setTagCmds(List<CmdItem> tagCmds) {
        this.tagCmds = tagCmds;
    }

    public String getBgimg() {
        return bgimg;
    }

    public void setBgimg(String bgimg) {
        this.bgimg = bgimg;
    }

    public String getIotStatus() {
        return iotStatus;
    }

    public void setIotStatus(String iotStatus) {
        this.iotStatus = iotStatus;
    }


    public Boolean getAutoImgSize() {
        return autoImgSize;
    }

    public void setAutoImgSize(Boolean autoImgSize) {
        this.autoImgSize = autoImgSize;
    }

    public Boolean getAutoItemSize() {
        return autoItemSize;
    }

    public void setAutoItemSize(Boolean autoItemSize) {
        this.autoItemSize = autoItemSize;
    }

    public Boolean getIconOnly() {
        return iconOnly;
    }

    public void setIconOnly(Boolean iconOnly) {
        this.iconOnly = iconOnly;
    }

    public String getItemMargin() {
        return itemMargin;
    }

    public void setItemMargin(String itemMargin) {
        this.itemMargin = itemMargin;
    }

    public String getItemPadding() {
        return itemPadding;
    }

    public void setItemPadding(String itemPadding) {
        this.itemPadding = itemPadding;
    }

    public String getItemWidth() {
        return itemWidth;
    }

    public void setItemWidth(String itemWidth) {
        this.itemWidth = itemWidth;
    }

    public String getItemHeight() {
        return itemHeight;
    }

    public void setItemHeight(String itemHeight) {
        this.itemHeight = itemHeight;
    }

    public String getImgWidth() {
        return imgWidth;
    }

    public void setImgWidth(String imgWidth) {
        this.imgWidth = imgWidth;
    }

    public String getImgHeight() {
        return imgHeight;
    }

    public void setImgHeight(String imgHeight) {
        this.imgHeight = imgHeight;
    }

    public Integer getColumns() {
        return columns;
    }

    public void setColumns(Integer columns) {
        this.columns = columns;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
