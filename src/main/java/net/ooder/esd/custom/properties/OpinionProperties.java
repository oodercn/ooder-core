package net.ooder.esd.custom.properties;

import com.alibaba.fastjson.JSON;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.OpinionAnnotation;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.field.CustomOpinionFieldBean;
import net.ooder.esd.bean.view.CustomOpinionViewBean;
import net.ooder.esd.tool.properties.item.OpinionItem;
import net.ooder.esd.tool.properties.list.AbsListProperties;
import net.ooder.esd.util.ESDEnumsUtil;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;

import java.util.List;
import java.util.Map;

public class OpinionProperties extends AbsListProperties<OpinionItem> {

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
    String flagText;
    String flagClass;
    String flagStyle;


    public OpinionProperties() {

    }

    public OpinionProperties(CustomOpinionViewBean opinionViewBean) {

        init(opinionViewBean);
    }

    public OpinionProperties(CustomOpinionViewBean opinionViewBean, ContainerBean containerBean) {
        init(opinionViewBean);
        if (containerBean != null) {
            this.init(containerBean.getUiBean());
        }

    }


    public OpinionProperties(CustomOpinionFieldBean opinionViewBean) {
        this.init(opinionViewBean.getContainerBean());
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(opinionViewBean), Map.class), this, false, false);
    }


    public void init(CustomOpinionViewBean opinionViewBean) {
        this.init(opinionViewBean.getContainerBean());
        Class<? extends Enum> enumClass = opinionViewBean.getEnumClass();
        if (enumClass == null || enumClass.equals(AnnotationUtil.getDefaultValue(OpinionAnnotation.class, "enumClass"))) {
            String viewClassName = opinionViewBean.getViewClassName();
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
            List<OpinionItem> listItems = ESDEnumsUtil.getEnumItems(enumClass, OpinionItem.class);
            for (OpinionItem listItem : listItems) {
                this.addItem(listItem);
            }
        }

        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(opinionViewBean), Map.class), this, false, false);
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
}
