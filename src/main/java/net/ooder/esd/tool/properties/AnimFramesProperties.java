package net.ooder.esd.tool.properties;

import java.util.Properties;

public class AnimFramesProperties extends Properties {
    AnimBinderStatus status;
    String type;
    Integer times;
    Integer duration;
    Boolean restore;

    public AnimBinderStatus getStatus() {
        return status;
    }

    public void setStatus(AnimBinderStatus status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Boolean getRestore() {
        return restore;
    }

    public void setRestore(Boolean restore) {
        this.restore = restore;
    }
}
