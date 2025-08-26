package net.ooder.esd.tool.properties.item;

import com.alibaba.fastjson.annotation.JSONField;


import net.ooder.annotation.Enumstype;
import net.ooder.annotation.IconEnumstype;
import net.ooder.esd.annotation.CustomMenu;
import net.ooder.esd.annotation.ui.FontColorEnum;
import net.ooder.esd.annotation.ui.IconColorEnum;
import net.ooder.esd.annotation.ui.ItemColorEnum;
import net.ooder.esd.annotation.ui.CmdButtonType;
import net.ooder.esd.annotation.ui.CmdTPosType;
import net.ooder.esd.annotation.ui.StatusItemType;
import net.ooder.esd.annotation.ui.TagCmdsAlign;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.util.json.BindClassArrDeserializer;
import net.ooder.esd.util.json.EnumsSerializer;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.sf.cglib.beans.BeanMap;

public class CmdItem<T extends Enum> extends UIItem<T> {

    public TagCmdsAlign tagCmdsAlign;
    public CmdButtonType buttonType;
    public CmdTPosType pos;
    public StatusItemType itemType;
    @JSONField(serializeUsing = EnumsSerializer.class, deserializeUsing = EnumsSerializer.class)
    public IconColorEnum iconColor;
    @JSONField(serializeUsing = EnumsSerializer.class, deserializeUsing = EnumsSerializer.class)
    public ItemColorEnum itemColor;
    @JSONField(serializeUsing = EnumsSerializer.class, deserializeUsing = EnumsSerializer.class)
    public FontColorEnum fontColor;

    @JSONField(deserializeUsing = BindClassArrDeserializer.class)
    public Class[] bindClass;


    public Integer index = 0;

    public CmdItem() {
        super();
    }


    public CmdItem(TreeListItem treeListItem) {
        super();
        this.id = treeListItem.getId();
        this.caption = treeListItem.getCaption();
        this.imageClass = treeListItem.getImageClass();
        this.iconColor = treeListItem.getIconColor();
        this.itemColor = treeListItem.getItemColor();
        this.fontColor = treeListItem.getFontColor();
        this.tagVar = treeListItem.getTagVar();

    }


    public CmdItem(T enumType) {
        super(enumType);
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



    public CmdItem(CmdButtonType inputType, String id) {
        super();
        this.id = id;
        this.buttonType = inputType;
    }

    public CmdItem(StatusItemType itemType, String id) {
        super();
        this.id = id;
        this.itemType = itemType;
    }


    public CmdItem(CustomMenu menu) {
        super(menu);
        this.iconColor = menu.iconColor();
        this.itemColor = menu.itemColor();
        this.fontColor = menu.fontColor();
    }


    public CmdItem(String id, String caption, String imageClass, IconColorEnum iconColorEnum, ItemColorEnum itemColor, FontColorEnum fontColor) {
        super();
        this.id = id;
        this.caption = caption;
        this.imageClass = imageClass;
        this.iconColor = iconColorEnum;
        this.itemColor = itemColor;
        this.fontColor = fontColor;
    }

    //    public CmdItem(String id, String caption, String imageClass, IconColorEnum iconColorEnum, ItemColorEnum itemColor) {
//        super();
//        this.id = id;
//        this.caption = caption;
//        this.imageClass = imageClass;
//        this.iconColor = iconColorEnum;
//        this.itemColor = itemColor;
//    }
//
//    public CmdItem(String id, String caption, String imageClass, IconColorEnum iconColorEnum) {
//        super();
//        this.id = id;
//        this.caption = caption;
//        this.imageClass = imageClass;
//        this.iconColor = iconColorEnum;
//    }
//


    public CmdItem(String id, String caption, String imageClass) {
        super();
        this.id = id;
        this.caption = caption;
        this.imageClass = imageClass;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public IconColorEnum getIconColor() {
        return iconColor;
    }

    public void setIconColor(IconColorEnum iconColor) {
        this.iconColor = iconColor;
    }

    public ItemColorEnum getItemColor() {
        return itemColor;
    }

    public void setItemColor(ItemColorEnum itemColor) {
        this.itemColor = itemColor;
    }

    public FontColorEnum getFontColor() {
        return fontColor;
    }

    public void setFontColor(FontColorEnum fontColor) {
        this.fontColor = fontColor;
    }

    public StatusItemType getItemType() {
        return itemType;
    }

    public void setItemType(StatusItemType itemType) {
        this.itemType = itemType;
    }

    public TagCmdsAlign getTagCmdsAlign() {
        return tagCmdsAlign;
    }

    public void setTagCmdsAlign(TagCmdsAlign tagCmdsAlign) {
        this.tagCmdsAlign = tagCmdsAlign;
    }


    public CmdButtonType getButtonType() {
        return buttonType;
    }

    public void setButtonType(CmdButtonType buttonType) {
        this.buttonType = buttonType;
    }

    public CmdTPosType getPos() {
        return pos;
    }

    public void setPos(CmdTPosType pos) {
        this.pos = pos;
    }


}

