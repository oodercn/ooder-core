package net.ooder.esd.bean;

import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.AnimFrameAnnotation;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

@AnnotationType(clazz = AnimFrameAnnotation.class)
public class AnimFramesPropBean implements CustomBean {
    String type;
    Integer times;
    Integer duration;
    Boolean restore;

    AnimBindStatusBean status;

    public AnimFramesPropBean() {

    }

    public AnimFramesPropBean(AnimFrameAnnotation annotation) {
        fillData((AnimFrameAnnotation) annotation);
    }

    public AnimFramesPropBean fillData(AnimFrameAnnotation annotation) {
        this.status = new AnimBindStatusBean(annotation.status());
        return AnnotationUtil.fillBean(annotation, this);
    }

    public AnimBindStatusBean getStatus() {
        return status;
    }

    public void setStatus(AnimBindStatusBean status) {
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

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
