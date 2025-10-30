package net.ooder.esd.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.BottomBarMenu;
import net.ooder.esd.annotation.CustomMenu;
import net.ooder.esd.annotation.menu.CustomMenuType;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.tool.component.StatusButtonsComponent;
import net.ooder.esd.tool.properties.form.StatusButtonsProperties;
import net.ooder.esd.tool.properties.item.CmdItem;
import net.ooder.esd.util.json.CaseEnumsSerializer;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AnnotationType(clazz = BottomBarMenu.class)
public class BottomBarMenuBean<T extends Enum> implements CustomBean {

    String domainId;

    String sourceClassName;

    String methodName;

    String itemPadding;

    String itemMargin;

    String itemWidth;

    String width;

    String height;

    String barheight;

    Dock dock;

    Dock barDock;

    String itemHeight;

    AlignType itemAlign;

    StatusItemType itemType;

    PositionType position;

    Boolean connected;

    BorderType borderType;

    BorderType barBorderType;

    TagCmdsAlign tagCmdsAlign;

    Class[] menuClasses;

    String id;
    @JSONField(deserializeUsing = CaseEnumsSerializer.class)
    CustomMenuType menuType;

    Boolean dynLoad = false;

    Boolean showCaption = true;

    String imageClass;

    Boolean lazy;

    String alias;

    Boolean autoIconColor;

    Boolean autoItemColor;

    Boolean autoFontColor;


    @JSONField(serialize = false)
    CustomMenu[] bottombar = new CustomMenu[]{};

    public Class serviceClass;

    @JSONField(serialize = false)
    public List<T> enumItems;


    public BottomBarMenuBean() {

    }

    public void update(StatusButtonsComponent statusButtonsComponent) {
        AnnotationUtil.fillDefaultValue(BottomBarMenu.class, this);
        StatusButtonsProperties statusButtonsProperties = statusButtonsComponent.getProperties();
        List<CmdItem> cmdItems = statusButtonsProperties.getItems();
        enumItems = new ArrayList<>();

        if (cmdItems != null) {
            for (CmdItem cmdItem : cmdItems) {
                if (cmdItem.getEnumItem() != null) {
                    enumItems.add((T) cmdItem.getEnumItem());
                }

            }
        }
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(statusButtonsProperties), Map.class), this, false, false);
    }

    public BottomBarMenuBean(StatusButtonsComponent statusButtonsComponent) {
        this.update(statusButtonsComponent);
    }

    public CustomMenuType getMenuType() {
        if (menuType == null) {
            menuType = CustomMenuType.BOTTOMBAR;
        }
        return menuType;
    }

    public void setMenuType(CustomMenuType menuType) {
        this.menuType = menuType;
    }


    public String getAlias() {
        if (alias == null || alias.equals("")) {
            if (methodName != null) {
                alias = methodName + "Bottom";
            } else if (menuClasses != null && menuClasses.length > 0) {
                Class clazz = menuClasses[0];
                alias = clazz.getSimpleName();
            } else if (bottombar.length > 0) {
                CustomMenu menu = bottombar[0];
                alias = menu.type() + "Bottom";
            } else if (id != null) {
                alias = id;
            } else {
                alias = "StatusBottom";
            }
        }
        return alias;
    }

    public String getId() {

        if (id == null || id.equals("")) {
            id = this.getAlias();
        }
        return id;
    }


    public void setAlias(String alias) {
        this.alias = alias;
    }

    public PositionType getPosition() {
        return position;
    }

    public void setPosition(PositionType position) {
        this.position = position;
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

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getDynLoad() {
        return dynLoad;
    }

    public void setDynLoad(Boolean dynLoad) {
        this.dynLoad = dynLoad;
    }

    public Boolean getShowCaption() {
        return showCaption;
    }

    public void setShowCaption(Boolean showCaption) {
        this.showCaption = showCaption;
    }

    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }

    public Boolean getLazy() {
        return lazy;
    }

    public void setLazy(Boolean lazy) {
        this.lazy = lazy;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getSourceClassName() {
        return sourceClassName;
    }

    public void setSourceClassName(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getBarheight() {
        return barheight;
    }

    public void setBarheight(String barheight) {
        this.barheight = barheight;
    }

    public BorderType getBarBorderType() {
        return barBorderType;
    }

    public void setBarBorderType(BorderType barBorderType) {
        this.barBorderType = barBorderType;
    }

    public BottomBarMenuBean(BottomBarMenu annotation) {
        fillData(annotation);
    }

    public BottomBarMenuBean fillData(BottomBarMenu annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }


    public BorderType getBorderType() {
        return borderType;
    }

    public void setBorderType(BorderType borderType) {
        this.borderType = borderType;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public Dock getDock() {
        return dock;
    }

    public void setDock(Dock dock) {
        this.dock = dock;
    }

    public Class[] getMenuClasses() {
        return menuClasses;
    }

    public void setMenuClasses(Class[] menuClasses) {
        this.menuClasses = menuClasses;
    }

    public String getItemPadding() {
        return itemPadding;
    }

    public void setItemPadding(String itemPadding) {
        this.itemPadding = itemPadding;
    }

    public String getItemMargin() {
        return itemMargin;
    }

    public void setItemMargin(String itemMargin) {
        this.itemMargin = itemMargin;
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

    public Dock getBarDock() {
        return barDock;
    }

    public void setBarDock(Dock barDock) {
        this.barDock = barDock;
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

    public Boolean getConnected() {
        return connected;
    }

    public void setConnected(Boolean connected) {
        this.connected = connected;
    }

    public TagCmdsAlign getTagCmdsAlign() {
        return tagCmdsAlign;
    }

    public void setTagCmdsAlign(TagCmdsAlign tagCmdsAlign) {
        this.tagCmdsAlign = tagCmdsAlign;
    }

    public List<T> getEnumItems() {
        return enumItems;
    }

    public void setEnumItems(List<T> enumItems) {
        this.enumItems = enumItems;
    }

    public CustomMenu[] getBottombar() {
        return bottombar;
    }

    public void setBottombar(CustomMenu[] bottombar) {
        this.bottombar = bottombar;
    }

    public Class getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(Class serviceClass) {
        this.serviceClass = serviceClass;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
