package net.ooder.esd.tool.properties.fchart;

import net.ooder.jds.core.esb.util.OgnlUtil;
import net.sf.cglib.beans.BeanMap;

import java.util.List;

public class AnnotationsGroup {

    String id;
    Long x;
    Long y;
    Boolean showbelow;
    Boolean autoscale;
    Boolean constrainscale;
    Boolean scaletext;
    Boolean scaleimages;
    Long xshift;
    Long yshift;
    List<AnnotationsBlock> items;

    public AnnotationsGroup() {

    }

    public AnnotationsGroup(Enum linenum) {
        OgnlUtil.setProperties(BeanMap.create(linenum), this, false, false);
    }




    public List<AnnotationsBlock> getItems() {
        return items;
    }

    public void setItems(List<AnnotationsBlock> items) {
        this.items = items;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getX() {
        return x;
    }

    public void setX(Long x) {
        this.x = x;
    }

    public Long getY() {
        return y;
    }

    public void setY(Long y) {
        this.y = y;
    }

    public Boolean getShowbelow() {
        return showbelow;
    }

    public void setShowbelow(Boolean showbelow) {
        this.showbelow = showbelow;
    }

    public Boolean getAutoscale() {
        return autoscale;
    }

    public void setAutoscale(Boolean autoscale) {
        this.autoscale = autoscale;
    }

    public Boolean getConstrainscale() {
        return constrainscale;
    }

    public void setConstrainscale(Boolean constrainscale) {
        this.constrainscale = constrainscale;
    }

    public Boolean getScaletext() {
        return scaletext;
    }

    public void setScaletext(Boolean scaletext) {
        this.scaletext = scaletext;
    }

    public Boolean getScaleimages() {
        return scaleimages;
    }

    public void setScaleimages(Boolean scaleimages) {
        this.scaleimages = scaleimages;
    }

    public Long getXshift() {
        return xshift;
    }

    public void setXshift(Long xshift) {
        this.xshift = xshift;
    }

    public Long getYshift() {
        return yshift;
    }

    public void setYshift(Long yshift) {
        this.yshift = yshift;
    }
}
