package net.ooder.esd.tool.properties;

import com.alibaba.fastjson.JSON;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.bean.view.CustomGalleryViewBean;
import net.ooder.esd.bean.gallery.GalleryItemBean;
import net.ooder.esd.tool.properties.item.CmdItem;
import net.ooder.esd.tool.properties.item.GalleryItem;
import net.ooder.esd.tool.properties.list.AbsListProperties;
import net.ooder.esd.util.ESDEnumsUtil;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.List;
import java.util.Map;

public class GalleryProperties extends AbsListProperties<GalleryItem> {
    public List<CmdItem> tagCmds;
    public String bgimg;
    public String iotStatus;
    public String backgroundColor;
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


    List<String> iconColors;
    List<String> fontColors;
    List<String> itemColors;
    Boolean autoIconColor;
    Boolean autoItemColor;
    Boolean autoFontColor;

    public GalleryProperties() {

    }

    public GalleryProperties(CustomGalleryViewBean galleryViewBean) {
        init(galleryViewBean);
    }


    public void init(CustomGalleryViewBean galleryViewBean) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(galleryViewBean), Map.class), this, false, false);
        this.init(galleryViewBean.getContainerBean());
        List<GalleryItemBean> galleryItemBeans = galleryViewBean.getGalleryItemBeans();
        if (galleryItemBeans != null) {
            for (GalleryItemBean galleryItemBean : galleryItemBeans) {
                this.addItem(new GalleryItem(galleryItemBean));
            }
        }

        if (this.getItems() == null || this.getItems().isEmpty()) {
            Class<? extends Enum> enumClass = galleryViewBean.getEnumClass();
            Class<? extends Enum> viewClass = null;
            String viewClassName = galleryViewBean.getViewClassName();
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
                this.items = ESDEnumsUtil.getEnumItems(viewClass, GalleryItem.class);
            } else if (enumClass != null) {
                this.items = ESDEnumsUtil.getEnumItems(enumClass, GalleryItem.class);
            }
        }


    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
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

    public String getIconFontSize() {
        return iconFontSize;
    }

    public void setIconFontSize(String iconFontSize) {
        this.iconFontSize = iconFontSize;
    }

    public List<String> getIconColors() {
        return iconColors;
    }

    public void setIconColors(List<String> iconColors) {
        this.iconColors = iconColors;
    }

    public List<String> getFontColors() {
        return fontColors;
    }

    public void setFontColors(List<String> fontColors) {
        this.fontColors = fontColors;
    }

    public List<String> getItemColors() {
        return itemColors;
    }

    public void setItemColors(List<String> itemColors) {
        this.itemColors = itemColors;
    }

    public Boolean getAutoIconColor() {
        return autoIconColor;
    }

    public void setAutoIconColor(Boolean autoIconColor) {
        this.autoIconColor = autoIconColor;
    }

    public Boolean getAutoItemColor() {
        return autoItemColor;
    }

    public void setAutoItemColor(Boolean autoItemColor) {
        this.autoItemColor = autoItemColor;
    }

    public Boolean getAutoFontColor() {
        return autoFontColor;
    }

    public void setAutoFontColor(Boolean autoFontColor) {
        this.autoFontColor = autoFontColor;
    }
}
