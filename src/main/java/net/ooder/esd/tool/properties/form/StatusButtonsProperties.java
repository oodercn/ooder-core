package net.ooder.esd.tool.properties.form;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.menu.CustomMenuType;
import net.ooder.esd.annotation.ui.AlignType;
import net.ooder.esd.annotation.ui.StatusItemType;
import net.ooder.esd.annotation.ui.TagCmdsAlign;
import net.ooder.esd.bean.BottomBarMenuBean;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.field.StatusButtonsFieldBean;
import net.ooder.esd.util.json.CaseEnumsSerializer;
import net.ooder.esd.tool.properties.item.CmdItem;
import net.ooder.esd.tool.properties.list.AbsListProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.List;
import java.util.Map;

public class StatusButtonsProperties extends AbsListProperties<CmdItem> {
    public List<CmdItem> tagCmds;
    public String itemMargin;
    public String itemPadding;
    public String itemWidth;
    public Boolean showCaption;
    public AlignType itemAlign;
    public StatusItemType itemType;
    public Boolean connected;
    public Boolean dirtyMark;
    @JSONField(deserializeUsing = CaseEnumsSerializer.class)
    CustomMenuType menuType;
    public TagCmdsAlign tagCmdsAlign;
    List<String> iconColors;
    List<String> fontColors;
    List<String> itemColors;
    Boolean autoIconColor;
    Boolean autoItemColor;
    Boolean autoFontColor;


    public StatusButtonsProperties(BottomBarMenuBean bean) {
        this.reload(bean);
    }

    public StatusButtonsProperties(StatusButtonsFieldBean bean, ContainerBean containerBean) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(bean), Map.class), this, false, false);
        if (containerBean != null) {
            this.init(containerBean);
        }

    }


    public StatusButtonsProperties(StatusButtonsFieldBean bean) {

        itemMargin = bean.getItemMargin();
        itemPadding = bean.getItemPadding();
        this.dock = bean.getDock();
        this.width = bean.getWidth();
        this.itemWidth = bean.getItemWidth();
        this.itemAlign = bean.getAlign();
        this.borderType = bean.getBorderType();
        this.itemType = bean.getItemType();
        this.connected = bean.getConnected();
        this.tagCmdsAlign = bean.getTagCmdsAlign();
        this.iconColors = bean.getIconColors();
        this.fontColors = bean.getFontColors();
        this.itemColors = bean.getIconColors();
        this.autoFontColor = bean.getAutoFontColor();
        this.autoIconColor = bean.getAutoIconColor();
        this.autoItemColor = bean.getAutoItemColor();


    }


    public void reload(BottomBarMenuBean bean) {
        this.showCaption = bean.getShowCaption();
        this.menuType = bean.getMenuType();
        itemMargin = bean.getItemMargin();
        itemPadding = bean.getItemPadding();
        this.dock = bean.getDock();
        this.width = bean.getWidth();
        this.itemWidth = bean.getItemWidth();
        this.itemAlign = bean.getItemAlign();
        this.borderType = bean.getBorderType();
        this.itemType = bean.getItemType();
        this.connected = bean.getConnected();
        this.tagCmdsAlign = bean.getTagCmdsAlign();
        this.iconColors = bean.getIconColors();
        this.fontColors = bean.getFontColors();
        this.itemColors = bean.getIconColors();
        this.autoFontColor = bean.getAutoFontColor();
        this.autoIconColor = bean.getAutoIconColor();
        this.autoItemColor = bean.getAutoItemColor();
    }

    public StatusButtonsProperties() {

    }

    public CustomMenuType getMenuType() {
        return menuType;
    }

    public void setMenuType(CustomMenuType menuType) {
        this.menuType = menuType;
    }

    public Boolean getShowCaption() {
        return showCaption;
    }

    public void setShowCaption(Boolean showCaption) {
        this.showCaption = showCaption;
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


    public Boolean getConnected() {
        return connected;
    }

    public TagCmdsAlign getTagCmdsAlign() {
        return tagCmdsAlign;
    }

    public void setTagCmdsAlign(TagCmdsAlign tagCmdsAlign) {
        this.tagCmdsAlign = tagCmdsAlign;
    }

    public List<CmdItem> getTagCmds() {
        return tagCmds;
    }

    public void setTagCmds(List<CmdItem> tagCmds) {
        this.tagCmds = tagCmds;
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

    public AlignType getItemAlign() {
        return itemAlign;
    }

    public void setItemAlign(AlignType itemAlign) {
        this.itemAlign = itemAlign;
    }

    public StatusItemType getItemType() {
        return itemType;
    }

    public void setItemType(StatusItemType itemType) {
        this.itemType = itemType;
    }

    public Boolean isConnected() {
        return connected;
    }

    public void setConnected(Boolean connected) {
        this.connected = connected;
    }

    public Boolean getDirtyMark() {
        return dirtyMark;
    }

    public void setDirtyMark(Boolean dirtyMark) {
        this.dirtyMark = dirtyMark;
    }
}
