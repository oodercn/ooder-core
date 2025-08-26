package net.ooder.esd.tool.properties;


import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.ui.BorderType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.annotation.ui.HAlignType;
import net.ooder.esd.annotation.ui.VAlignType;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.bean.view.NavComboBaseViewBean;
import net.ooder.esd.bean.view.TabsViewBean;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;
import net.ooder.esd.engine.enums.MenuBarBean;
import net.ooder.esd.tool.properties.list.ListFieldProperties;

import java.util.List;

public class MenuBarProperties extends ListFieldProperties {

    public String parentID;
    public Boolean handler;
    @JSONField(name = "hAlign")
    public HAlignType hAlign;
    @JSONField(name = "vAlign")
    public VAlignType vAlign;
    public String parentId;
    public Integer index = 100;
    public Boolean showCaption = true;
    public Boolean lazy;
    public Integer autoShowTime;
    public String iconFontSize;


    @JSONField(serialize = false)
    List<String> iconColors;
    @JSONField(serialize = false)
    List<String> itemColors;
    @JSONField(serialize = false)
    List<String> fontColors;

    Boolean autoIconColor = true;

    Boolean autoItemColor = false;

    Boolean autoFontColor = false;

    public MenuBarProperties() {

    }

    public MenuBarProperties(MenuBarBean menuBarBean) {
        super();
        init(menuBarBean);
    }

    void init(MenuBarBean menuBarBean) {
        this.dock = menuBarBean.getDock() == null ? Dock.top : menuBarBean.getDock();
        this.handler = menuBarBean.getHandler();
        this.hAlign = menuBarBean.gethAlign();
        this.vAlign = menuBarBean.getvAlign();
        this.showCaption = menuBarBean.getShowCaption();
        this.lazy = menuBarBean.getLazy();
        this.parentID = menuBarBean.getParentId();
        this.index = menuBarBean.getIndex();
        this.autoShowTime = menuBarBean.getAutoShowTime();
        this.iconColors = menuBarBean.getIconColors();
        this.itemColors = menuBarBean.getItemColors();
        this.fontColors = menuBarBean.getFontColors();
        this.autoFontColor = menuBarBean.getAutoFontColor();
        this.autoIconColor = menuBarBean.getAutoIconColor();
        this.autoItemColor = menuBarBean.getAutoItemColor();
    }

    public MenuBarProperties(NavComboBaseViewBean indexViewBean) {
        super();
        init(indexViewBean.getMenuBar());
        this.setHandler(false);
        this.setPosition("relative");
        this.setBorderType(BorderType.none);
        this.setDockOrder(1);
        this.setAutoShowTime(0);
        TabsViewBean tabsViewBean = indexViewBean.getTabsViewBean();
        this.iconColors = tabsViewBean.getIconColors();
        this.itemColors = tabsViewBean.getItemColors();
        this.fontColors = tabsViewBean.getFontColors();
        this.autoFontColor = tabsViewBean.getAutoFontColor();
        this.autoIconColor = tabsViewBean.getAutoIconColor();
        this.autoItemColor = tabsViewBean.getAutoItemColor();

        List<FieldModuleConfig> moduleItems = indexViewBean.getNavItems();
        for (FieldModuleConfig itemInfo : moduleItems) {
            TreeListItem navItemProperties = new TreeListItem(itemInfo);
            navItemProperties.setImageClass("");
            this.addItem(navItemProperties);
        }
    }

    public List<String> getIconColors() {
        return iconColors;
    }

    public void setIconColors(List<String> iconColors) {
        this.iconColors = iconColors;
    }

    public List<String> getItemColors() {
        return itemColors;
    }

    public void setItemColors(List<String> itemColors) {
        this.itemColors = itemColors;
    }

    public List<String> getFontColors() {
        return fontColors;
    }

    public void setFontColors(List<String> fontColors) {
        this.fontColors = fontColors;
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

    public Integer getAutoShowTime() {
        return autoShowTime;
    }

    public void setAutoShowTime(Integer autoShowTime) {
        this.autoShowTime = autoShowTime;
    }

    public Integer getDockOrder() {
        return dockOrder;
    }

    public void setDockOrder(Integer dockOrder) {
        this.dockOrder = dockOrder;
    }

    public String getIconFontSize() {
        return iconFontSize;
    }

    public void setIconFontSize(String iconFontSize) {
        this.iconFontSize = iconFontSize;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public Boolean getHandler() {
        return handler;
    }

    public void setHandler(Boolean handler) {
        this.handler = handler;
    }

    public HAlignType gethAlign() {
        return hAlign;
    }

    public void sethAlign(HAlignType hAlign) {
        this.hAlign = hAlign;
    }

    public VAlignType getvAlign() {
        return vAlign;
    }

    public void setvAlign(VAlignType vAlign) {
        this.vAlign = vAlign;
    }


    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Boolean getShowCaption() {
        return showCaption;
    }

    public void setShowCaption(Boolean showCaption) {
        this.showCaption = showCaption;
    }


    public Boolean getLazy() {
        return lazy;
    }

    public void setLazy(Boolean lazy) {
        this.lazy = lazy;
    }




}
