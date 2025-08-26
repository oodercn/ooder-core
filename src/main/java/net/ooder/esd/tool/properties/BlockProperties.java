package net.ooder.esd.tool.properties;

import net.ooder.esd.annotation.ui.BorderType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.annotation.ui.SideBarStatusType;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.CustomBlockBean;
import net.ooder.esd.bean.field.CustomBlockFieldBean;

import java.util.Map;

public class BlockProperties extends WidgetProperties {

    BorderType borderType;
    Boolean resizer;
    Map<String, Object> resizerProp;
    String sideBarCaption;
    String sideBarType;
    SideBarStatusType sideBarStatus;
    ComponentType comboType;
    String sideBarSize;
    String background;


    public BlockProperties() {

    }

    public BlockProperties(ComponentType comboType) {
        this.comboType = comboType;
    }


    public BlockProperties(CustomBlockBean blockBean) {
        init(blockBean);
    }


    public BlockProperties(CustomBlockFieldBean blockFieldBean) {
        this.init(blockFieldBean);
    }

    public void init(CustomBlockFieldBean blockBean) {
        if (blockBean != null) {
            initWidget(blockBean.getWidgetBean());
            if (blockBean.getDock() != null && !blockBean.getDock().equals(Dock.fill)) {
                this.dock = blockBean.getDock();
            }
            if (blockBean.getBorderType() != null && !blockBean.getBorderType().equals(BorderType.none)) {
                this.borderType = blockBean.getBorderType();
            }
            if (blockBean.getBackground() != null) {
                this.background = blockBean.getBackground();
            }
            this.resizer = blockBean.getResizer();
            this.resizerProp = blockBean.getResizerProp();
            this.sideBarCaption = blockBean.getSideBarCaption();
            this.sideBarType = blockBean.getSideBarType();
            this.sideBarStatus = blockBean.getSideBarStatus();
            this.sideBarSize = blockBean.getSideBarSize();
            if (blockBean.getContainerBean() != null) {
                this.init(blockBean.getContainerBean());
            }
        }
    }


    void init(CustomBlockBean blockBean) {

        if (blockBean != null) {
            ContainerBean containerBean = blockBean.getContainerBean();
            if (containerBean != null) {
                this.init(containerBean);
            }
            initWidget(blockBean.getWidgetBean());
            if (blockBean.getDock() != null) {
                this.dock = blockBean.getDock();
            }
            if (blockBean.getBorderType() != null) {
                this.borderType = blockBean.getBorderType();
            }
            if (blockBean.getBackground() != null) {
                this.background = blockBean.getBackground();
            }
            this.resizer = blockBean.getResizer();
            this.resizerProp = blockBean.getResizerProp();
            this.sideBarCaption = blockBean.getSideBarCaption();
            this.sideBarType = blockBean.getSideBarType();
            this.sideBarStatus = blockBean.getSideBarStatus();
            this.sideBarSize = blockBean.getSideBarSize();
        }

    }

    public BlockProperties(Dock dock) {
        super(dock);
        this.dock = dock;

    }

    public ComponentType getComboType() {
        return comboType;
    }

    public void setComboType(ComponentType comboType) {
        this.comboType = comboType;
    }

    public BorderType getBorderType() {
        return borderType;
    }

    public void setBorderType(BorderType borderType) {
        this.borderType = borderType;
    }

    public Boolean getResizer() {
        return resizer;
    }

    public void setResizer(Boolean resizer) {
        this.resizer = resizer;
    }

    public Map<String, Object> getResizerProp() {
        return resizerProp;
    }

    public void setResizerProp(Map<String, Object> resizerProp) {
        this.resizerProp = resizerProp;
    }

    public String getSideBarCaption() {
        return sideBarCaption;
    }

    public void setSideBarCaption(String sideBarCaption) {
        this.sideBarCaption = sideBarCaption;
    }

    public String getSideBarType() {
        return sideBarType;
    }

    public void setSideBarType(String sideBarType) {
        this.sideBarType = sideBarType;
    }

    public SideBarStatusType getSideBarStatus() {
        return sideBarStatus;
    }

    public void setSideBarStatus(SideBarStatusType sideBarStatus) {
        this.sideBarStatus = sideBarStatus;
    }

    public String getSideBarSize() {
        return sideBarSize;
    }

    public void setSideBarSize(String sideBarSize) {
        this.sideBarSize = sideBarSize;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }
}
