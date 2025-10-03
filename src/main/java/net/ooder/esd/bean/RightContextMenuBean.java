package net.ooder.esd.bean;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.RightContextMenu;
import net.ooder.esd.annotation.menu.ContextMenu;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@AnnotationType(clazz = RightContextMenu.class)
public class RightContextMenuBean implements CustomBean {

    public Boolean hideAfterClick;

    public List<String> listKey;

    public Boolean autoHide;

    public String parentID;

    public Boolean handler;

    public Boolean formField;

    public Class bindService;

    public String id;

    public String iconFontSize;

    public String itemClass;

    public String itemStyle;

    public Boolean lazy;

    public Boolean dynLoad;

    public Class serviceClass;

    public Class[] menuClass;

    public Set<ContextMenu> contextMenu = new HashSet<>();


    Boolean autoIconColor = true;

    Boolean autoItemColor = false;

    Boolean autoFontColor = false;


    public Set<ComponentType> bindTypes = new LinkedHashSet<>();

    public RightContextMenuBean() {

    }

    public RightContextMenuBean(String id) {
        this.id = id;
    }

    public RightContextMenuBean(String id,RightContextMenu annotation) {
        fillData(annotation);
    }

    public RightContextMenuBean fillData(RightContextMenu annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }


    public Class getFristMenuClass() {
        Class fristMenuClass = Void.class;
        if (menuClass != null && menuClass.length > 0) {
            fristMenuClass = menuClass[0];
        }
        return fristMenuClass;
    }

    public Set<ContextMenu> getContextMenu() {
        if (contextMenu == null) {
            contextMenu = new HashSet<>();
        }
        return contextMenu;
    }

    public void setContextMenu(Set<ContextMenu> contextMenu) {
        this.contextMenu = contextMenu;
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

    public Boolean getHideAfterClick() {
        return hideAfterClick;
    }

    public void setHideAfterClick(Boolean hideAfterClick) {
        this.hideAfterClick = hideAfterClick;
    }

    public List<String> getListKey() {
        return listKey;
    }

    public void setListKey(List<String> listKey) {
        this.listKey = listKey;
    }

    public Boolean getAutoHide() {
        return autoHide;
    }

    public void setAutoHide(Boolean autoHide) {
        this.autoHide = autoHide;
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

    public Boolean getFormField() {
        return formField;
    }

    public void setFormField(Boolean formField) {
        this.formField = formField;
    }

    public String getIconFontSize() {
        return iconFontSize;
    }

    public void setIconFontSize(String iconFontSize) {
        this.iconFontSize = iconFontSize;
    }

    public Class getBindService() {
        return bindService;
    }

    public void setBindService(Class bindService) {
        this.bindService = bindService;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemClass() {
        return itemClass;
    }

    public void setItemClass(String itemClass) {
        this.itemClass = itemClass;
    }

    public String getItemStyle() {
        return itemStyle;
    }

    public void setItemStyle(String itemStyle) {
        this.itemStyle = itemStyle;
    }

    public Boolean getLazy() {
        return lazy;
    }

    public void setLazy(Boolean lazy) {
        this.lazy = lazy;
    }

    public Boolean getDynLoad() {
        return dynLoad;
    }

    public void setDynLoad(Boolean dynLoad) {
        this.dynLoad = dynLoad;
    }

    public Class getServiceClass() {
        return serviceClass;
    }

    public Class[] getMenuClass() {
        return menuClass;
    }

    public void setMenuClass(Class[] menuClass) {
        this.menuClass = menuClass;
    }

    public void setServiceClass(Class serviceClass) {
        this.serviceClass = serviceClass;
    }

    public Set<ComponentType> getBindTypes() {
        return bindTypes;
    }

    public void setBindTypes(Set<ComponentType> bindTypes) {
        this.bindTypes = bindTypes;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
