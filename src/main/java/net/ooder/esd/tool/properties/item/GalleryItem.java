package net.ooder.esd.tool.properties.item;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.Enumstype;
import net.ooder.annotation.IconEnumstype;
import net.ooder.common.JDSException;
import net.ooder.common.util.StringUtility;
import net.ooder.context.JDSActionContext;


import net.ooder.esd.annotation.menu.CustomGalleryMenu;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.bean.gallery.GalleryItemBean;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.sf.cglib.beans.BeanMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GalleryItem extends TabListItem {
    @JSONField(serialize = false)
    public static final String ESDSearchPattern = "esdsearchpattern";

    public static String getESDSearchPattern() {
        return ESDSearchPattern;
    }

    public String comment;
    public String renderer;
    public String imagePos;
    public String imageBgSize;
    public String imageRepeat;
    public String iconFontSize;
    public String iconFontCode;
    public String iconStyle;
    public String flagText;
    public String flagClass;
    public String flagStyle;
    public String valueSeparator;
    public String bindClassName;
    List<CustomGalleryMenu> contextMenu;

    public GalleryItem() {
    }


    public GalleryItem(String id, String caption, String imageClass, String tips, Map<String, Object> params) {
        this.id = id;
        this.caption = caption;
        this.imageClass = imageClass;
        this.tagVar = params;
        this.tips = tips;
    }


    public GalleryItem(Enum enumType) {
        OgnlUtil.setProperties(BeanMap.create(enumType), this, false, false);
        if (enumType instanceof IconEnumstype) {
            if (id == null) {
                this.id = ((IconEnumstype) enumType).getType();
            }
            if (caption == null) {
                this.caption = ((IconEnumstype) enumType).getName();
            }
            this.imageClass = ((IconEnumstype) enumType).getImageClass();
        } else if (enumType instanceof Enumstype) {
            if (id == null) {
                this.id = ((Enumstype) enumType).getType();
            }
            if (caption == null) {
                this.caption = ((Enumstype) enumType).getName();
            }
        }
    }

    public GalleryItem(TreeListItem listItem) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(listItem), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);

    }

    public GalleryItem(GalleryItemBean galleryItemBean) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(galleryItemBean), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);

    }


    public GalleryItem(FieldModuleConfig itemConfig) throws JDSException {
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


    public GalleryItem(String id, String caption, String imageClass) {
        this.id = id;
        this.caption = caption;
        this.imageClass = imageClass;
    }

    public GalleryItem(String id, String caption) {
        this.id = id;
        this.caption = caption;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String getRenderer() {
        return renderer;
    }

    @Override
    public void setRenderer(String renderer) {
        this.renderer = renderer;
    }

    public String getImagePos() {
        return imagePos;
    }

    public void setImagePos(String imagePos) {
        this.imagePos = imagePos;
    }

    public String getImageBgSize() {
        return imageBgSize;
    }

    public void setImageBgSize(String imageBgSize) {
        this.imageBgSize = imageBgSize;
    }

    public String getImageRepeat() {
        return imageRepeat;
    }

    public void setImageRepeat(String imageRepeat) {
        this.imageRepeat = imageRepeat;
    }

    public String getIconFontSize() {
        return iconFontSize;
    }

    public void setIconFontSize(String iconFontSize) {
        this.iconFontSize = iconFontSize;
    }

    public String getIconFontCode() {
        return iconFontCode;
    }

    public void setIconFontCode(String iconFontCode) {
        this.iconFontCode = iconFontCode;
    }

    public String getIconStyle() {
        return iconStyle;
    }

    public void setIconStyle(String iconStyle) {
        this.iconStyle = iconStyle;
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

    public List<CustomGalleryMenu> getContextMenu() {
        return contextMenu;
    }

    public void setContextMenu(List<CustomGalleryMenu> contextMenu) {
        this.contextMenu = contextMenu;
    }

    public String getValueSeparator() {
        return valueSeparator;
    }

    public void setValueSeparator(String valueSeparator) {
        this.valueSeparator = valueSeparator;
    }


    public String getPattern() {
        String pattern = null;
        if (JDSActionContext.getActionContext().getParams(GalleryItem.ESDSearchPattern) != null) {
            pattern = JDSActionContext.getActionContext().getParams(GalleryItem.ESDSearchPattern).toString();
        }
        return pattern;
    }


}
