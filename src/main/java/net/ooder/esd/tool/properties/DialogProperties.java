package net.ooder.esd.tool.properties;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.annotation.ui.DiaStatusType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.bean.DialogBean;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.Map;

public class DialogProperties extends PanelProperties {

    Boolean movable;
    Boolean minBtn;
    Boolean maxBtn;
    Boolean resizer;
    Boolean pinBtn;
    Boolean landBtn;
    String minWidth;
    String minHeight;
    String fromRegion;
    Boolean displayBar;
    Boolean modal;
    String initPos;
    DiaStatusType status;
    public DialogProperties() {
        this.dock = Dock.none;
    }

    public DialogProperties(DialogBean dialogBean) {
        this.init(dialogBean.getContainerBean());
        if (dialogBean.getDialogBtnBean() != null) {
            OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(dialogBean.getDialogBtnBean()), Map.class), this, false, false);
        }
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(dialogBean), Map.class), this, false, false);
    }

    public Boolean getDisplayBar() {
        return displayBar;
    }

    public void setDisplayBar(Boolean displayBar) {
        this.displayBar = displayBar;
    }

    public Boolean getMaxBtn() {
        return maxBtn;
    }

    public void setMaxBtn(Boolean maxBtn) {
        this.maxBtn = maxBtn;
    }

    public Boolean getResizer() {
        return resizer;
    }

    public void setResizer(Boolean resizer) {
        this.resizer = resizer;
    }


    public Boolean getMovable() {
        return movable;
    }

    public void setMovable(Boolean movable) {
        this.movable = movable;
    }

    public Boolean getMinBtn() {
        return minBtn;
    }

    public void setMinBtn(Boolean minBtn) {
        this.minBtn = minBtn;
    }


    public Boolean getPinBtn() {
        return pinBtn;
    }

    public void setPinBtn(Boolean pinBtn) {
        this.pinBtn = pinBtn;
    }

    public Boolean getLandBtn() {
        return landBtn;
    }

    public void setLandBtn(Boolean landBtn) {
        this.landBtn = landBtn;
    }

    public String getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(String minWidth) {
        this.minWidth = minWidth;
    }

    public String getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(String minHeight) {
        this.minHeight = minHeight;
    }

    public String getFromRegion() {
        return fromRegion;
    }

    public void setFromRegion(String fromRegion) {
        this.fromRegion = fromRegion;
    }

    public Boolean getModal() {
        return modal;
    }

    public void setModal(Boolean modal) {
        this.modal = modal;
    }

    public String getInitPos() {
        return initPos;
    }

    public void setInitPos(String initPos) {
        this.initPos = initPos;
    }

    public DiaStatusType getStatus() {
        return status;
    }

    public void setStatus(DiaStatusType status) {
        this.status = status;
    }
}
