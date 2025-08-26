package net.ooder.esd.bean.fchart.items;


import net.ooder.esd.tool.properties.item.UIItem;

import java.util.HashMap;
import java.util.Map;

public class AnnGroupListItem extends UIItem {

    public String id;
    public Integer x;
    public Integer y;
    public Boolean showbelow;
    public Boolean autoscale;
    public Boolean constrainscale;
    public Boolean scaletext;
    public Boolean scaleimages;
    public Integer xshift;
    public Integer yshift;


    public AnnGroupListItem() {

    }


    public Map<String, Object> addTagVar(String name, Object value) {
        if (tagVar == null) {
            tagVar = new HashMap<>();
        }
        tagVar.put(name, value);
        return tagVar;
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

    public Integer getXshift() {
        return xshift;
    }

    public void setXshift(Integer xshift) {
        this.xshift = xshift;
    }

    public Integer getYshift() {
        return yshift;
    }

    public void setYshift(Integer yshift) {
        this.yshift = yshift;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Boolean getShowbelow() {
        return showbelow;
    }

    public void setShowbelow(Boolean showbelow) {
        this.showbelow = showbelow;
    }
}
