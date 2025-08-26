package net.ooder.esd.tool.properties.item;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.Enumstype;
import net.ooder.annotation.IconEnumstype;
import net.ooder.common.util.StringUtility;
import net.ooder.context.JDSActionContext;


import net.ooder.esd.annotation.ui.BorderType;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;

import java.util.HashMap;
import java.util.Map;

public class GalleryNavItem extends GalleryItem {
    @JSONField(serialize = false)
    public static final String ESDSearchPattern = "esdsearchpattern";
    public BorderType borderType;
    public Boolean activeLast;

    public GalleryNavItem() {
    }


    public GalleryNavItem(String id, String caption, String imageClass, String tips, Map<String, Object> params) {
        this.id = id;
        this.caption = caption;
        this.imageClass = imageClass;
        this.tagVar = params;
    }

    public GalleryNavItem(FieldModuleConfig itemConfig) {
        this.caption = itemConfig.getCaption();
        this.imageClass = itemConfig.getImageClass();
        String url = itemConfig.getUrl();
        if (url.startsWith("/")) {
            url = url.substring(1);
        }
        euClassName = StringUtility.replace(url, "/", ".");
        this.id = itemConfig.getId();
        if (tagVar == null) {
            this.tagVar = new HashMap<>();
        }

    }

    public GalleryNavItem(Enum enumType) {
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

    public GalleryNavItem(String id, String caption, String imageClass) {
        this.id = id;
        this.caption = caption;
        this.imageClass = imageClass;
    }


    @JSONField(serialize = false)
    private GalleryNavItem getFristId(GalleryNavItem itemInfo) {
        if (itemInfo.getEuClassName() != null && !itemInfo.getEuClassName().equals("")) {
            return itemInfo;
        }
        return null;
    }


    @JSONField(serialize = false)
    public GalleryNavItem getFristClassItem(GalleryNavItem item) {
        GalleryNavItem fristItem = getFristId(item);
        if (fristItem == null) {
            fristItem = item;
        }
        return fristItem;
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

    public String getPattern() {
        String pattern = null;
        if (JDSActionContext.getActionContext().getParams(GalleryNavItem.ESDSearchPattern) != null) {
            pattern = JDSActionContext.getActionContext().getParams(GalleryNavItem.ESDSearchPattern).toString();
        }
        return pattern;
    }


}
