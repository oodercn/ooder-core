package net.ooder.esd.tool.properties.fchart;

import net.ooder.jds.core.esb.util.OgnlUtil;
import net.sf.cglib.beans.BeanMap;

import java.util.ArrayList;
import java.util.List;

public class AnnotationsData {


    Boolean autoscale;
    Boolean constrainscale;
    Boolean scaletext;
    Boolean scaleimages;
    Integer xshift;
    Integer yshift;
    Integer grpyshift;
    Integer origw;
    Integer origh;
    Integer grpxshift;

    List<AnnotationsGroup> groups = new ArrayList<>();

    public AnnotationsData() {

    }

    public AnnotationsData(Enum linenum) {
        OgnlUtil.setProperties(BeanMap.create(linenum), this, false, false);
    }

    public List<AnnotationsGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<AnnotationsGroup> groups) {
        this.groups = groups;
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
