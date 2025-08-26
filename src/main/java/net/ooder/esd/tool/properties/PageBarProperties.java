package net.ooder.esd.tool.properties;

import net.ooder.esd.bean.grid.PageBarBean;
import net.ooder.esd.tool.properties.item.UIItem;

public class PageBarProperties extends UIItem {

    public Integer pageCount;

    public Boolean showMoreBtns;

    public String pageCaption;

    public Boolean hiddenBar;

    public String prevMark;

    public String parentID;

    public Boolean autoTips;

    public String nextMark;

    public String uriTpl;

    public String textTpl;

    public String value;

    public String height;


    public PageBarProperties() {

    }


    public PageBarProperties(PageBarBean viewBean) {
        this.showMoreBtns = viewBean.getShowMoreBtns();
        this.pageCount = viewBean.getPageCount();
        this.value = viewBean.getValue();
        if (viewBean.getUriTpl() != null && !viewBean.getUriTpl().equals("")) {
            this.uriTpl = viewBean.getUriTpl();
        }
        if (viewBean.getTextTpl() != null && !viewBean.getTextTpl().equals("")) {
            this.textTpl = viewBean.getTextTpl();
        }
        this.value = viewBean.getValue();
        this.readonly = viewBean.getReadonly();
        this.disabled = viewBean.getDisabled();

        this.prevMark = viewBean.getPrevMark();
        this.nextMark = viewBean.getNextMark();
        this.caption = viewBean.getPageCaption();
        this.hiddenBar = viewBean.getHiddenBar();
    }

    public Boolean getHiddenBar() {
        return hiddenBar;
    }

    public void setHiddenBar(Boolean hiddenBar) {
        this.hiddenBar = hiddenBar;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Boolean getShowMoreBtns() {
        return showMoreBtns;
    }

    public void setShowMoreBtns(Boolean showMoreBtns) {
        this.showMoreBtns = showMoreBtns;
    }

    public String getPageCaption() {
        return pageCaption;
    }

    public void setPageCaption(String pageCaption) {
        this.pageCaption = pageCaption;
    }

    public String getPrevMark() {
        return prevMark;
    }

    public void setPrevMark(String prevMark) {
        this.prevMark = prevMark;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public Boolean getAutoTips() {
        return autoTips;
    }

    public void setAutoTips(Boolean autoTips) {
        this.autoTips = autoTips;
    }

    public String getNextMark() {
        return nextMark;
    }

    public void setNextMark(String nextMark) {
        this.nextMark = nextMark;
    }

    public String getUriTpl() {
        return uriTpl;
    }

    public void setUriTpl(String uriTpl) {
        this.uriTpl = uriTpl;
    }

    public String getTextTpl() {
        return textTpl;
    }

    public void setTextTpl(String textTpl) {
        this.textTpl = textTpl;
    }

    @Override
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
