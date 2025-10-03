package net.ooder.esd.tool.properties;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.ui.BarLocationType;
import net.ooder.esd.annotation.ui.BorderType;
import net.ooder.esd.annotation.ui.SideBarStatusType;
import net.ooder.esd.annotation.ui.TagCmdsAlign;
import net.ooder.esd.tool.properties.item.TabListItem;
import net.ooder.esd.tool.properties.list.AbsUIListProperties;

import java.util.List;

public class TabsProperties<T extends TabListItem> extends AbsUIListProperties<T> {

    public Boolean lazyAppend;
    public String message;
    public String itemWidth;
    public String itemAlig;
    public String dropKeysPanel;
    public Boolean noPanel;
    @JSONField(name = "hAlign")
    public String hAlign;
    @JSONField(name = "vAlign")
    public String vAlign;
    public Boolean noHandler;
    public Boolean reload;
    @JSONField(serialize = false)
    public String fristId;

    public Integer maxHeight;
    public Boolean closeBtn;
    public Boolean popBtn;
    public Boolean iniFold;
    public Boolean autoReload;
    public Boolean animCollapse;
    public Boolean dynDestory;
    public Boolean togglePlaceholder;
    public TagCmdsAlign tagCmdsAlign;
    public Boolean group;
    public Boolean autoSave;
    public String optBtn;
    public Boolean singleOpen;
    public BarLocationType barLocation;
    public String barSize;
    public String sideBarSize;
    public SideBarStatusType sideBarStatus;
    Class<? extends Enum> enumClass;

    Boolean autoIconColor;
    Boolean autoItemColor;
    Boolean autoFontColor;

    public TabsProperties() {

    }


    public BorderType getBorderType() {
        return borderType;
    }

    public void setBorderType(BorderType borderType) {
        this.borderType = borderType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    private String getFristId(TabListItem itemInfo) {
        if (itemInfo.getEuClassName() != null && !itemInfo.getEuClassName().equals("")) {
            return itemInfo.getId();
        }
        return null;
    }


    public String getFristId() {
        if (fristId == null) {
            if (this.getItems().size() > 0) {
                fristId = this.getFristId(this.getItems().get(0));
                if (fristId == null) {
                    fristId = this.getItems().get(0).getId();
                }
            }
        }
        return fristId;
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

    public Boolean getLazyAppend() {
        return lazyAppend;
    }

    public void setLazyAppend(Boolean lazyAppend) {
        this.lazyAppend = lazyAppend;
    }

    public String getItemWidth() {
        return itemWidth;
    }

    public void setItemWidth(String itemWidth) {
        this.itemWidth = itemWidth;
    }

    public String getItemAlig() {
        return itemAlig;
    }

    public void setItemAlig(String itemAlig) {
        this.itemAlig = itemAlig;
    }

    public String getDropKeysPanel() {
        return dropKeysPanel;
    }

    public void setDropKeysPanel(String dropKeysPanel) {
        this.dropKeysPanel = dropKeysPanel;
    }

    public Boolean getNoPanel() {
        return noPanel;
    }

    public void setNoPanel(Boolean noPanel) {
        this.noPanel = noPanel;
    }

    public String gethAlign() {
        return hAlign;
    }

    public void sethAlign(String hAlign) {
        this.hAlign = hAlign;
    }

    public String getvAlign() {
        return vAlign;
    }

    public void setvAlign(String vAlign) {
        this.vAlign = vAlign;
    }

    public Boolean getNoHandler() {
        return noHandler;
    }

    public void setNoHandler(Boolean noHandler) {
        this.noHandler = noHandler;
    }

    public Boolean getReload() {
        return reload;
    }

    public void setReload(Boolean reload) {
        this.reload = reload;
    }

    public void setFristId(String fristId) {
        this.fristId = fristId;
    }


    public Integer getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(Integer maxHeight) {
        this.maxHeight = maxHeight;
    }

    public Boolean getCloseBtn() {
        return closeBtn;
    }

    public void setCloseBtn(Boolean closeBtn) {
        this.closeBtn = closeBtn;
    }

    public Boolean getPopBtn() {
        return popBtn;
    }

    public void setPopBtn(Boolean popBtn) {
        this.popBtn = popBtn;
    }


    public Boolean getIniFold() {
        return iniFold;
    }

    public void setIniFold(Boolean iniFold) {
        this.iniFold = iniFold;
    }

    public Boolean getAutoReload() {
        return autoReload;
    }

    public void setAutoReload(Boolean autoReload) {
        this.autoReload = autoReload;
    }

    public Boolean getAnimCollapse() {
        return animCollapse;
    }

    public void setAnimCollapse(Boolean animCollapse) {
        this.animCollapse = animCollapse;
    }

    public Boolean getDynDestory() {
        return dynDestory;
    }

    public void setDynDestory(Boolean dynDestory) {
        this.dynDestory = dynDestory;
    }

    public Boolean getTogglePlaceholder() {
        return togglePlaceholder;
    }

    public void setTogglePlaceholder(Boolean togglePlaceholder) {
        this.togglePlaceholder = togglePlaceholder;
    }

    public TagCmdsAlign getTagCmdsAlign() {
        return tagCmdsAlign;
    }

    public void setTagCmdsAlign(TagCmdsAlign tagCmdsAlign) {
        this.tagCmdsAlign = tagCmdsAlign;
    }

    public Boolean getGroup() {
        return group;
    }

    public void setGroup(Boolean group) {
        this.group = group;
    }

    public Boolean getAutoSave() {
        return autoSave;
    }

    public void setAutoSave(Boolean autoSave) {
        this.autoSave = autoSave;
    }

    public String getOptBtn() {
        return optBtn;
    }

    public void setOptBtn(String optBtn) {
        this.optBtn = optBtn;
    }

    public Boolean getSingleOpen() {
        return singleOpen;
    }

    public void setSingleOpen(Boolean singleOpen) {
        this.singleOpen = singleOpen;
    }

    public BarLocationType getBarLocation() {
        return barLocation;
    }

    public void setBarLocation(BarLocationType barLocation) {
        this.barLocation = barLocation;
    }

    public String getBarSize() {
        return barSize;
    }

    public void setBarSize(String barSize) {
        this.barSize = barSize;
    }

    public String getSideBarSize() {
        return sideBarSize;
    }

    public void setSideBarSize(String sideBarSize) {
        this.sideBarSize = sideBarSize;
    }

    public SideBarStatusType getSideBarStatus() {
        return sideBarStatus;
    }

    public void setSideBarStatus(SideBarStatusType sideBarStatus) {
        this.sideBarStatus = sideBarStatus;
    }

    public Class<? extends Enum> getEnumClass() {
        return enumClass;
    }

    public void setEnumClass(Class<? extends Enum> enumClass) {
        this.enumClass = enumClass;
    }
}
