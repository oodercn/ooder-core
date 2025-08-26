package net.ooder.esd.tool.properties;

import net.ooder.annotation.CustomBean;
import net.ooder.web.util.AnnotationUtil;

public class Spacing implements CustomBean {
    Integer width;
    Integer height;

    public Spacing() {

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

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
