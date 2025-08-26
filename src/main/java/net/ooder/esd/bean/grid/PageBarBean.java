package net.ooder.esd.bean.grid;

import com.alibaba.fastjson.JSON;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.MPageBar;
import net.ooder.esd.annotation.PageBar;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.tool.properties.PageBarProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.Map;

@AnnotationType(clazz = PageBar.class)
public class PageBarBean implements CustomBean {


    Integer pageCount;

    Boolean showMoreBtns;

    String pageCaption;

    String prevMark;

    String parentID;

    Boolean disabled;

    Boolean readonly;

    Boolean autoTips;

    String nextMark;

    String uriTpl;

    String textTpl;

    String value;

    String height;

    Dock dock;

    Boolean hiddenBar;

    public void update(PageBarProperties pageBarProperties) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(pageBarProperties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }

    public PageBarBean(PageBarProperties pageBarProperties) {
        update(pageBarProperties);
    }

    public PageBarBean() {
        AnnotationUtil.fillDefaultValue(PageBar.class, this);
    }

    public PageBarBean(MPageBar annotation) {
        AnnotationUtil.fillBean(annotation, this);
    }

    public PageBarBean(PageBar annotation) {
        AnnotationUtil.fillBean(annotation, this);
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Boolean getReadonly() {
        return readonly;
    }

    public void setReadonly(Boolean readonly) {
        this.readonly = readonly;
    }

    public Boolean getAutoTips() {
        return autoTips;
    }

    public void setAutoTips(Boolean autoTips) {
        this.autoTips = autoTips;
    }

    public String getTextTpl() {
        return textTpl;
    }

    public void setTextTpl(String textTpl) {
        this.textTpl = textTpl;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Dock getDock() {
        return dock;
    }

    public void setDock(Dock dock) {
        this.dock = dock;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public String getUriTpl() {
        return uriTpl;
    }

    public void setUriTpl(String uriTpl) {
        this.uriTpl = uriTpl;
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

    public String getNextMark() {
        return nextMark;
    }

    public void setNextMark(String nextMark) {
        this.nextMark = nextMark;
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


    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }


}
