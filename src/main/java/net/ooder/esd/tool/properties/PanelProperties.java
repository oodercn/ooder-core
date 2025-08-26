package net.ooder.esd.tool.properties;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.CustomPanelBean;
import net.ooder.esd.bean.field.CustomPanelFieldBean;
import net.ooder.esd.bean.nav.BtnBean;
import net.ooder.esd.tool.properties.item.CmdItem;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.List;
import java.util.Map;


public class PanelProperties extends DivProperties {


    public BorderType borderType;
    public List<CmdItem> tagCmds;
    public Boolean toggle;
    public ToggleIconType toggleIcon;
    public Boolean infoBtn;
    public Boolean optBtn;
    public Boolean closeBtn;
    public Boolean refreshBtn;
    public Boolean popBtn;
    public String image;
    public ImagePos imagePos;
    public String imageBgSize;
    public String iconFontCode;
    public Boolean noFrame;
    public HAlignType hAlign;
    public Boolean toggleBtn;


    public PanelProperties(CustomPanelFieldBean panelFieldBean) {
        init(panelFieldBean);
    }


    public PanelProperties(CustomPanelBean bean) {
        init(bean);
    }

    protected void init(CustomPanelBean bean) {
        if (bean != null) {
            BtnBean btnBean = bean.getBtnBean();
            if (btnBean != null) {
                OgnlUtil.setProperties((Map) JSON.toJSON(btnBean), this, false, false);
            }
            OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(bean), Map.class), this, false, false);
            super.init(bean.getDivBean());
        }
    }


    public void init(CustomPanelFieldBean panelFieldBean) {
        if (panelFieldBean != null) {
            if (panelFieldBean.getDock() != null && !panelFieldBean.getDock().equals(Dock.fill)) {
                this.dock = panelFieldBean.getDock();
            }
            if (panelFieldBean.getBorderType() != null && !panelFieldBean.getBorderType().equals(BorderType.none)) {
                this.borderType = panelFieldBean.getBorderType();
            }
            OgnlUtil.setProperties((Map) JSON.toJSON(panelFieldBean), this, false, false);
            if (panelFieldBean.getContainerBean() != null) {
                this.init(panelFieldBean.getContainerBean());
            }
        }
    }


    public BorderType getBorderType() {
        return borderType;
    }

    public void setBorderType(BorderType borderType) {
        this.borderType = borderType;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }


    public ToggleIconType getToggleIcon() {
        return toggleIcon;
    }

    public void setToggleIcon(ToggleIconType toggleIcon) {
        this.toggleIcon = toggleIcon;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ImagePos getImagePos() {
        return imagePos;
    }

    public void setImagePos(ImagePos imagePos) {
        this.imagePos = imagePos;
    }

    public String getImageBgSize() {
        return imageBgSize;
    }

    public void setImageBgSize(String imageBgSize) {
        this.imageBgSize = imageBgSize;
    }

    public String getIconFontCode() {
        return iconFontCode;
    }

    public void setIconFontCode(String iconFontCode) {
        this.iconFontCode = iconFontCode;
    }

    public Boolean getNoFrame() {
        return noFrame;
    }

    public void setNoFrame(Boolean noFrame) {
        this.noFrame = noFrame;
    }

    public HAlignType gethAlign() {
        return hAlign;
    }

    public void sethAlign(HAlignType hAlign) {
        this.hAlign = hAlign;
    }

    public PanelProperties() {

    }

    public PanelProperties(Dock dock) {
        this.dock = dock;
    }

    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }


    public List<CmdItem> getTagCmds() {
        return tagCmds;
    }

    public void setTagCmds(List<CmdItem> tagCmds) {
        this.tagCmds = tagCmds;
    }

    public Boolean getToggle() {
        return toggle;
    }

    public void setToggle(Boolean toggle) {
        this.toggle = toggle;
    }

    public Boolean getInfoBtn() {
        return infoBtn;
    }

    public void setInfoBtn(Boolean infoBtn) {
        this.infoBtn = infoBtn;
    }

    public Boolean getOptBtn() {
        return optBtn;
    }

    public void setOptBtn(Boolean optBtn) {
        this.optBtn = optBtn;
    }

    public Boolean getCloseBtn() {
        return closeBtn;
    }

    public void setCloseBtn(Boolean closeBtn) {
        this.closeBtn = closeBtn;
    }

    public Boolean getRefreshBtn() {
        return refreshBtn;
    }

    public void setRefreshBtn(Boolean refreshBtn) {
        this.refreshBtn = refreshBtn;
    }

    public Boolean getPopBtn() {
        return popBtn;
    }

    public void setPopBtn(Boolean popBtn) {
        this.popBtn = popBtn;
    }

    public Boolean getToggleBtn() {
        return toggleBtn;
    }

    public void setToggleBtn(Boolean toggleBtn) {
        this.toggleBtn = toggleBtn;
    }
}
