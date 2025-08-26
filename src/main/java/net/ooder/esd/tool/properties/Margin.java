package net.ooder.esd.tool.properties;

import net.ooder.annotation.CustomBean;
import net.ooder.web.util.AnnotationUtil;

public class Margin implements CustomBean {

    Integer left;

    Integer top;

    Integer right;

    Integer bottom;

    public Margin() {

    }

    public Integer getLeft() {
        return left;
    }

    public void setLeft(Integer left) {
        this.left = left;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

    public Integer getRight() {
        return right;
    }

    public void setRight(Integer right) {
        this.right = right;
    }

    public Integer getBottom() {
        return bottom;
    }

    public void setBottom(Integer bottom) {
        this.bottom = bottom;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

}
