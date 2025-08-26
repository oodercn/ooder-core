package net.ooder.esd.bean.fchart.items;

import net.ooder.esd.tool.properties.item.UIItem;

import java.util.HashMap;
import java.util.Map;

public class AnnListItem extends UIItem {

    public Boolean autoscale;
    public Boolean constrainscale;
    public Boolean scaletext;
    public Boolean scaleimages;
    public Integer xshift;
    public Integer yshift;
    public Integer grpyshift;
    public Integer origw;
    public Integer origh;
    public Integer grpxshift;


    public AnnListItem() {

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

    public Integer getGrpyshift() {
        return grpyshift;
    }

    public void setGrpyshift(Integer grpyshift) {
        this.grpyshift = grpyshift;
    }

    public Integer getOrigw() {
        return origw;
    }

    public void setOrigw(Integer origw) {
        this.origw = origw;
    }

    public Integer getOrigh() {
        return origh;
    }

    public void setOrigh(Integer origh) {
        this.origh = origh;
    }

    public Integer getGrpxshift() {
        return grpxshift;
    }

    public void setGrpxshift(Integer grpxshift) {
        this.grpxshift = grpxshift;
    }
}
