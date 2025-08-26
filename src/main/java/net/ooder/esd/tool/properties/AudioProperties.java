package net.ooder.esd.tool.properties;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.annotation.ui.PreloadType;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.field.AudioFieldBean;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.Map;

public class AudioProperties extends FieldProperties {


    String src;
    Boolean cover;
    Boolean controls;
    PreloadType preload;
    Boolean loop;
    Boolean muted;
    Integer volume;
    Boolean autoplay;

    public AudioProperties() {

    }

    public AudioProperties(AudioFieldBean bean, ContainerBean containerBean) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(bean), Map.class), this, false, false);
        if (containerBean != null) {
            this.init(containerBean);

        }

    }

    public Boolean getCover() {
        return cover;
    }

    public Boolean getControls() {
        return controls;
    }

    public Boolean getLoop() {
        return loop;
    }

    public Boolean getMuted() {
        return muted;
    }

    public Boolean getAutoplay() {
        return autoplay;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public Boolean isCover() {
        return cover;
    }

    public void setCover(Boolean cover) {
        this.cover = cover;
    }

    public Boolean isControls() {
        return controls;
    }

    public void setControls(Boolean controls) {
        this.controls = controls;
    }

    public PreloadType getPreload() {
        return preload;
    }

    public void setPreload(PreloadType preload) {
        this.preload = preload;
    }

    public Boolean isLoop() {
        return loop;
    }

    public void setLoop(Boolean loop) {
        this.loop = loop;
    }

    public Boolean isMuted() {
        return muted;
    }

    public void setMuted(Boolean muted) {
        this.muted = muted;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Boolean isAutoplay() {
        return autoplay;
    }

    public void setAutoplay(Boolean autoplay) {
        this.autoplay = autoplay;
    }
}
