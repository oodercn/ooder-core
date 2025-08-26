package net.ooder.esd.bean;

import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.ui.CmdButtonType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.TagCmdsAlign;
import net.ooder.web.util.AnnotationUtil;

import java.util.LinkedHashSet;
import java.util.Set;

public class RowCmdBean implements CustomBean {

    public TagCmdsAlign tagCmdsAlign;

    CmdButtonType buttonType;

    String id;

    String caption;

    Boolean showCaption = false;

    String itemStyle;

    String tips;

    Boolean disabled;

    Boolean lazy;

    Boolean dynLoad;

    Class[] menuClass = new Class[]{};

    public Set<ComponentType> bindTypes = new LinkedHashSet<>();

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

    public Boolean getShowCaption() {
        return showCaption;
    }

    public void setShowCaption(Boolean showCaption) {
        this.showCaption = showCaption;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getItemStyle() {
        return itemStyle;
    }

    public void setItemStyle(String itemStyle) {
        this.itemStyle = itemStyle;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }


    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
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

    public Class[] getMenuClass() {
        return menuClass;
    }

    public void setMenuClass(Class[] menuClass) {
        this.menuClass = menuClass;
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
