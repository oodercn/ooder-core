package net.ooder.esd.tool.properties;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.CustomUIBean;
import net.ooder.esd.bean.DisabledBean;
import net.ooder.esd.bean.DockBean;
import net.ooder.esd.tool.properties.list.DataProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.List;
import java.util.Map;


public class ContainerProperties extends DataProperties {

    public String panelBgClr;
    public String panelBgImg;
    public String panelBgImgPos;
    public String panelBgImgAttachment;
    public Integer conLayoutColumns;
    public Boolean conDockRelative;

    public ThemesType sandboxTheme;
    public String formMethod;
    public String formTarget;
    public String formDataPath;
    public String formAction;
    public String formEnctype;
    public String className;
    public String dropKeys;
    public String iframeAutoLoad;
    public String ajaxAutoLoad;
    public String html;
    public String dragKey;
    public OverflowType overflow;
    public String panelBgImgRepeat;
    public String set;
    public Margin conDockPadding;
    public Spacing conDockSpacing;
    public DockFlexType conDockFlexFill;
    public String conDockStretch;
    public String autoTips;
    public Boolean disableClickEffect;
    public Boolean disableHoverEffect;
    public Boolean disableTips;
    public SpaceUnitType spaceUnit;
    public Boolean defaultFocus;
    public String hoverPopType;

    public String hoverPop;

    public Boolean dockIgnore;
    public Boolean dockFloat;
    public Integer dockOrder;
    public String showEffects;
    public String hideEffects;
    public Margin dockMargin;

    public String dockMinW;
    public String dockMinH;
    public String dockMaxW;
    public String dockMaxH;
    public Boolean dockIgnoreFlexFill;
    public String dockStretch;
    public String tips;
    public Integer rotate;
    public String activeAnim;


    public ContainerProperties() {

    }


    public void init(ContainerBean containerBean) {
        if (containerBean != null) {
            OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(containerBean), Map.class), this, false, false);
            if (containerBean.getDisabledBean()!=null){
                OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(containerBean.getDisabledBean()), Map.class), this, false, false);
            }
            if (containerBean.getDockBean()!=null){
                OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(containerBean.getDockBean()), Map.class), this, false, false);
            }

            super.init(containerBean.getUiBean());
        }

    }


    public ContainerProperties(Dock dock) {
        this.dock = dock;
    }

    public String getPanelBgClr() {
        return panelBgClr;
    }

    public void setPanelBgClr(String panelBgClr) {
        this.panelBgClr = panelBgClr;
    }

    public String getPanelBgImg() {
        return panelBgImg;
    }

    public void setPanelBgImg(String panelBgImg) {
        this.panelBgImg = panelBgImg;
    }

    public String getPanelBgImgPos() {
        return panelBgImgPos;
    }

    public void setPanelBgImgPos(String panelBgImgPos) {
        this.panelBgImgPos = panelBgImgPos;
    }

    public String getPanelBgImgAttachment() {
        return panelBgImgAttachment;
    }

    public void setPanelBgImgAttachment(String panelBgImgAttachment) {
        this.panelBgImgAttachment = panelBgImgAttachment;
    }

    public Integer getConLayoutColumns() {
        return conLayoutColumns;
    }

    public void setConLayoutColumns(Integer conLayoutColumns) {
        this.conLayoutColumns = conLayoutColumns;
    }

    public ThemesType getSandboxTheme() {
        return sandboxTheme;
    }

    public void setSandboxTheme(ThemesType sandboxTheme) {
        this.sandboxTheme = sandboxTheme;
    }

    public String getFormMethod() {
        return formMethod;
    }

    public void setFormMethod(String formMethod) {
        this.formMethod = formMethod;
    }

    public String getFormTarget() {
        return formTarget;
    }

    public void setFormTarget(String formTarget) {
        this.formTarget = formTarget;
    }

    public String getFormDataPath() {
        return formDataPath;
    }

    public void setFormDataPath(String formDataPath) {
        this.formDataPath = formDataPath;
    }

    public String getFormAction() {
        return formAction;
    }

    public void setFormAction(String formAction) {
        this.formAction = formAction;
    }

    public String getFormEnctype() {
        return formEnctype;
    }

    public void setFormEnctype(String formEnctype) {
        this.formEnctype = formEnctype;
    }

    public String getDropKeys() {
        return dropKeys;
    }

    public void setDropKeys(String dropKeys) {
        this.dropKeys = dropKeys;
    }

    public String getIframeAutoLoad() {
        return iframeAutoLoad;
    }

    public void setIframeAutoLoad(String iframeAutoLoad) {
        this.iframeAutoLoad = iframeAutoLoad;
    }

    public String getAjaxAutoLoad() {
        return ajaxAutoLoad;
    }

    public void setAjaxAutoLoad(String ajaxAutoLoad) {
        this.ajaxAutoLoad = ajaxAutoLoad;
    }

    public Boolean getSelectable() {
        return selectable;
    }

    public void setSelectable(Boolean selectable) {
        this.selectable = selectable;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public OverflowType getOverflow() {
        return overflow;
    }

    public void setOverflow(OverflowType overflow) {
        this.overflow = overflow;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDragKey() {
        return dragKey;
    }

    public void setDragKey(String dragKey) {
        this.dragKey = dragKey;
    }

    public String getPanelBgImgRepeat() {
        return panelBgImgRepeat;
    }

    public void setPanelBgImgRepeat(String panelBgImgRepeat) {
        this.panelBgImgRepeat = panelBgImgRepeat;
    }

    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        this.set = set;
    }

    public Spacing getConDockSpacing() {
        return conDockSpacing;
    }

    public void setConDockSpacing(Spacing conDockSpacing) {
        this.conDockSpacing = conDockSpacing;
    }

    public DockFlexType getConDockFlexFill() {
        return conDockFlexFill;
    }

    public void setConDockFlexFill(DockFlexType conDockFlexFill) {
        this.conDockFlexFill = conDockFlexFill;
    }

    public String getConDockStretch() {
        return conDockStretch;
    }

    public void setConDockStretch(String conDockStretch) {
        this.conDockStretch = conDockStretch;
    }


    public String getAutoTips() {
        return autoTips;
    }

    public void setAutoTips(String autoTips) {
        this.autoTips = autoTips;
    }

    public Boolean getDisableClickEffect() {
        return disableClickEffect;
    }

    public void setDisableClickEffect(Boolean disableClickEffect) {
        this.disableClickEffect = disableClickEffect;
    }

    public SpaceUnitType getSpaceUnit() {
        return spaceUnit;
    }

    public void setSpaceUnit(SpaceUnitType spaceUnit) {
        this.spaceUnit = spaceUnit;
    }

    public Boolean getDefaultFocus() {
        return defaultFocus;
    }

    public void setDefaultFocus(Boolean defaultFocus) {
        this.defaultFocus = defaultFocus;
    }

    public String getHoverPopType() {
        return hoverPopType;
    }

    public void setHoverPopType(String hoverPopType) {
        this.hoverPopType = hoverPopType;
    }

    public Dock getDock() {
        return dock;
    }

    public void setDock(Dock dock) {
        this.dock = dock;
    }

    public String getDockStretch() {
        return dockStretch;
    }

    public void setDockStretch(String dockStretch) {
        this.dockStretch = dockStretch;
    }

    public Boolean getConDockRelative() {
        return conDockRelative;
    }

    public void setConDockRelative(Boolean conDockRelative) {
        this.conDockRelative = conDockRelative;
    }

    public Boolean getDisableHoverEffect() {
        return disableHoverEffect;
    }

    public void setDisableHoverEffect(Boolean disableHoverEffect) {
        this.disableHoverEffect = disableHoverEffect;
    }

    public Boolean getDisableTips() {
        return disableTips;
    }

    public void setDisableTips(Boolean disableTips) {
        this.disableTips = disableTips;
    }

    public String getHoverPop() {
        return hoverPop;
    }

    public void setHoverPop(String hoverPop) {
        this.hoverPop = hoverPop;
    }


    public Boolean getDockIgnore() {
        return dockIgnore;
    }

    public void setDockIgnore(Boolean dockIgnore) {
        this.dockIgnore = dockIgnore;
    }

    public Boolean getDockFloat() {
        return dockFloat;
    }

    public void setDockFloat(Boolean dockFloat) {
        this.dockFloat = dockFloat;
    }

    public Integer getDockOrder() {
        return dockOrder;
    }

    public void setDockOrder(Integer dockOrder) {
        this.dockOrder = dockOrder;
    }

    public String getHideEffects() {
        return hideEffects;
    }

    public void setHideEffects(String hideEffects) {
        this.hideEffects = hideEffects;
    }

    public Boolean getDockIgnoreFlexFill() {
        return dockIgnoreFlexFill;
    }

    public void setDockIgnoreFlexFill(Boolean dockIgnoreFlexFill) {
        this.dockIgnoreFlexFill = dockIgnoreFlexFill;
    }

    public String getShowEffects() {
        return showEffects;
    }

    public void setShowEffects(String showEffects) {
        this.showEffects = showEffects;
    }

    public String getDockMinW() {
        return dockMinW;
    }

    public void setDockMinW(String dockMinW) {
        this.dockMinW = dockMinW;
    }

    public String getDockMinH() {
        return dockMinH;
    }

    public void setDockMinH(String dockMinH) {
        this.dockMinH = dockMinH;
    }

    public String getDockMaxW() {
        return dockMaxW;
    }

    public void setDockMaxW(String dockMaxW) {
        this.dockMaxW = dockMaxW;
    }

    public String getDockMaxH() {
        return dockMaxH;
    }

    public void setDockMaxH(String dockMaxH) {
        this.dockMaxH = dockMaxH;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public Integer getRotate() {
        return rotate;
    }

    public void setRotate(Integer rotate) {
        this.rotate = rotate;
    }

    public String getActiveAnim() {
        return activeAnim;
    }

    public void setActiveAnim(String activeAnim) {
        this.activeAnim = activeAnim;
    }

    public Margin getConDockPadding() {
        return conDockPadding;
    }

    public void setConDockPadding(Margin conDockPadding) {
        this.conDockPadding = conDockPadding;
    }

    public Margin getDockMargin() {
        return dockMargin;
    }

    public void setDockMargin(Margin dockMargin) {
        this.dockMargin = dockMargin;
    }


}


