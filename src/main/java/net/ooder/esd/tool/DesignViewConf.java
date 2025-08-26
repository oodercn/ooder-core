package net.ooder.esd.tool;


import net.ooder.esd.bean.DesignViewBean;

public class DesignViewConf {

    Integer width;
    Integer height;
    Boolean touchDevice = false;

    public DesignViewConf() {

    }

    public DesignViewConf(DesignViewBean viewBean) {
        this.width = viewBean.getWidth();
        this.height = viewBean.getHeight();
        this.touchDevice = viewBean.getMobileFrame();
    }


    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Boolean getTouchDevice() {
        return touchDevice;
    }

    public void setTouchDevice(Boolean touchDevice) {
        this.touchDevice = touchDevice;
    }
}
