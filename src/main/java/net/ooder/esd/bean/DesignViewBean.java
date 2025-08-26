package net.ooder.esd.bean;

import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.DesignViewAnnotation;
import net.ooder.esd.tool.DesignViewConf;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

@AnnotationType(clazz = DesignViewAnnotation.class)
public class DesignViewBean implements CustomBean {

    Integer width;
    Integer height;
    Boolean mobileFrame = false;


    public void update(DesignViewConf viewConf) {
        this.width = viewConf.getWidth();
        this.height = viewConf.getHeight();
        this.mobileFrame = viewConf.getTouchDevice();
    }

    public DesignViewBean(DesignViewConf viewConf) {
        update(viewConf);
    }


    public DesignViewBean() {
        AnnotationUtil.fillDefaultValue(DesignViewAnnotation.class, this);
    }

    public DesignViewBean(DesignViewAnnotation annotation) {
        this.fillData(annotation);

    }

    public DesignViewBean fillData(DesignViewAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
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

    public boolean getMobileFrame() {
        return mobileFrame;
    }

    public void setMobileFrame(boolean mobileFrame) {
        this.mobileFrame = mobileFrame;
    }


    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
