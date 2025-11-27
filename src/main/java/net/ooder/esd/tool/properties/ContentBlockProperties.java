package net.ooder.esd.tool.properties;

import com.alibaba.fastjson.JSON;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.ContentBlockAnnotation;
import net.ooder.esd.bean.ContentBlockItemBean;
import net.ooder.esd.bean.field.CustomGalleryFieldBean;
import net.ooder.esd.bean.view.CustomContentBlockViewBean;
import net.ooder.esd.tool.properties.item.ContentBlockItem;
import net.ooder.esd.tool.properties.list.AbsListProperties;
import net.ooder.esd.tool.properties.list.AbsUIListProperties;
import net.ooder.esd.util.ESDEnumsUtil;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;

import java.util.List;
import java.util.Map;

public class ContentBlockProperties extends AbsListProperties<ContentBlockItem> {

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


    List<String> moreColors;
    List<String> iconColors;
    List<String> fontColors;
    List<String> itemColors;
    Boolean autoIconColor;
    Boolean autoItemColor;
    Boolean autoFontColor;
    String flagText;
    String flagClass;
    String flagStyle;


    public ContentBlockProperties() {

    }

    public ContentBlockProperties(CustomContentBlockViewBean contentBlockViewBean) {
        init(contentBlockViewBean);

    }


    public ContentBlockProperties(CustomGalleryFieldBean contentBlockViewBean) {
        this.init(contentBlockViewBean.getContainerBean());
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(contentBlockViewBean), Map.class), this, false, false);
    }


    public void init(CustomContentBlockViewBean contentBlockViewBean) {
        this.init(contentBlockViewBean.getContainerBean());

        Class<? extends Enum> enumClass = contentBlockViewBean.getEnumClass();
        if (enumClass == null || enumClass.equals(AnnotationUtil.getDefaultValue(ContentBlockAnnotation.class, "enumClass"))) {
            String viewClassName = contentBlockViewBean.getViewClassName();
            if (viewClassName != null) {
                Class clazz = null;
                try {
                    clazz = ClassUtility.loadClass(viewClassName);
                    if (clazz.isEnum()) {
                        enumClass = clazz;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        if (enumClass != null) {
            List<ContentBlockItem> listItems = ESDEnumsUtil.getEnumItems(enumClass, ContentBlockItem.class);
            for (ContentBlockItem listItem : listItems) {
                this.addItem(listItem);
            }
        }

        if (this.getItems() != null && this.getItems().isEmpty()) {
            for (ContentBlockItemBean itemBean : contentBlockViewBean.getContentBlockItemBeans()) {
                ContentBlockItem contentBlockItem = itemBean.getContentBlockItem();
                this.addItem(contentBlockItem);
            }
        }


        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(contentBlockViewBean), Map.class), this, false, false);
    }

    public String getFlagText() {
        return flagText;
    }

    public void setFlagText(String flagText) {
        this.flagText = flagText;
    }

    public String getFlagClass() {
        return flagClass;
    }

    public void setFlagClass(String flagClass) {
        this.flagClass = flagClass;
    }

    public String getFlagStyle() {
        return flagStyle;
    }

    public void setFlagStyle(String flagStyle) {
        this.flagStyle = flagStyle;
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

    public String getIconFontSize() {
        return iconFontSize;
    }

    public void setIconFontSize(String iconFontSize) {
        this.iconFontSize = iconFontSize;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public List<String> getMoreColors() {
        return moreColors;
    }

    public void setMoreColors(List<String> moreColors) {
        this.moreColors = moreColors;
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
