package net.ooder.esd.tool.properties;


import net.ooder.esd.bean.RightContextMenuBean;

public class PopMenuProperties extends MenuBarProperties {

    public Boolean autoHide;
    public Boolean hideAfterClick;

    public PopMenuProperties() {

    }

    public PopMenuProperties(RightContextMenuBean contextMenuBean) {
        this.id = contextMenuBean.getId();
        this.hideAfterClick = contextMenuBean.getHideAfterClick();
        this.autoHide = contextMenuBean.getAutoHide();
        this.listKey = contextMenuBean.getListKey();
        this.parentID = contextMenuBean.getParentID();
        this.handler = contextMenuBean.getHandler();
        this.formField = contextMenuBean.getFormField();
        this.lazy = contextMenuBean.getLazy();
        this.dynLoad = contextMenuBean.getDynLoad();
    }


    public Boolean getHideAfterClick() {
        return hideAfterClick;
    }

    public void setHideAfterClick(Boolean hideAfterClick) {
        this.hideAfterClick = hideAfterClick;
    }

    public Boolean getAutoHide() {
        return autoHide;
    }

    public void setAutoHide(Boolean autoHide) {
        this.autoHide = autoHide;
    }
}
