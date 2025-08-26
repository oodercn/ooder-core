package net.ooder.esd.tool.properties.fchart;

import net.ooder.esd.annotation.fchart.FChartAnnType;
import net.ooder.esd.annotation.ui.AlignType;
import net.ooder.esd.annotation.ui.VAlignType;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.sf.cglib.beans.BeanMap;

public class AnnotationsBlock {
    FChartAnnType type;
    String id;
    Integer x;
    Integer y;
    Integer tox;
    Integer toy;
    String fillcolor;
    Integer fillalpha;
    Integer fillratio;
    Integer fillangle;
    String fillpattern;
    Boolean showborder;
    String bordercolor;
    Integer borderalpha;
    Integer borderthickness;
    Integer dashed;
    Integer dashlen;
    Integer dashgap;
    String tooltext;
    String link;
    Boolean showshadow;
    String label;
    AlignType align;
    VAlignType valign;
    String font;
    Integer fontsize;
    String fontcolor;
    Boolean bold;
    Integer italic;
    Integer leftnargin;
    String bgcolor;
    String rotatetext;
    Integer wrapwidth;
    Integer wrapheight;
    Boolean wrap;
    Integer radius;
    Integer yradius;
    Integer startangle;
    Integer endangle;
    Integer thickness;
    Boolean showbelow;
    Boolean autoscale;
    Boolean constrainscale;
    String scaletext;
    String scaleimages;
    Integer xshift;
    Integer yshift;
    Integer grpxshift;
    Integer grpyshift;
    String color;
    Integer alpha;
    Boolean visible;

    AlignType textalign;
    VAlignType textvalign;

    String wraptext;
    String path;

    String origw;
    String righ;

    public AnnotationsBlock() {

    }

    public AnnotationsBlock(Enum linenum) {
        OgnlUtil.setProperties(BeanMap.create(linenum), this, false, false);
    }

    public FChartAnnType getType() {
        return type;
    }

    public void setType(FChartAnnType type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

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

    public String getFillcolor() {
        return fillcolor;
    }

    public Integer getTox() {
        return tox;
    }

    public void setTox(Integer tox) {
        this.tox = tox;
    }

    public Integer getToy() {
        return toy;
    }

    public void setToy(Integer toy) {
        this.toy = toy;
    }

    public void setFillcolor(String fillcolor) {
        this.fillcolor = fillcolor;
    }

    public Integer getFillalpha() {
        return fillalpha;
    }

    public void setFillalpha(Integer fillalpha) {
        this.fillalpha = fillalpha;
    }

    public Integer getFillratio() {
        return fillratio;
    }

    public void setFillratio(Integer fillratio) {
        this.fillratio = fillratio;
    }

    public Integer getFillangle() {
        return fillangle;
    }

    public void setFillangle(Integer fillangle) {
        this.fillangle = fillangle;
    }

    public String getFillpattern() {
        return fillpattern;
    }

    public void setFillpattern(String fillpattern) {
        this.fillpattern = fillpattern;
    }

    public Boolean getShowborder() {
        return showborder;
    }

    public void setShowborder(Boolean showborder) {
        this.showborder = showborder;
    }

    public String getBordercolor() {
        return bordercolor;
    }

    public void setBordercolor(String bordercolor) {
        this.bordercolor = bordercolor;
    }

    public Integer getBorderalpha() {
        return borderalpha;
    }

    public void setBorderalpha(Integer borderalpha) {
        this.borderalpha = borderalpha;
    }

    public Integer getBorderthickness() {
        return borderthickness;
    }

    public void setBorderthickness(Integer borderthickness) {
        this.borderthickness = borderthickness;
    }

    public Integer getDashed() {
        return dashed;
    }

    public void setDashed(Integer dashed) {
        this.dashed = dashed;
    }

    public Integer getDashlen() {
        return dashlen;
    }

    public void setDashlen(Integer dashlen) {
        this.dashlen = dashlen;
    }

    public Integer getDashgap() {
        return dashgap;
    }

    public void setDashgap(Integer dashgap) {
        this.dashgap = dashgap;
    }

    public String getTooltext() {
        return tooltext;
    }

    public void setTooltext(String tooltext) {
        this.tooltext = tooltext;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Boolean getShowshadow() {
        return showshadow;
    }

    public void setShowshadow(Boolean showshadow) {
        this.showshadow = showshadow;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public AlignType getAlign() {
        return align;
    }

    public void setAlign(AlignType align) {
        this.align = align;
    }

    public VAlignType getValign() {
        return valign;
    }

    public void setValign(VAlignType valign) {
        this.valign = valign;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public Integer getFontsize() {
        return fontsize;
    }

    public void setFontsize(Integer fontsize) {
        this.fontsize = fontsize;
    }

    public String getFontcolor() {
        return fontcolor;
    }

    public void setFontcolor(String fontcolor) {
        this.fontcolor = fontcolor;
    }

    public Boolean getBold() {
        return bold;
    }

    public void setBold(Boolean bold) {
        this.bold = bold;
    }

    public Integer getItalic() {
        return italic;
    }

    public void setItalic(Integer italic) {
        this.italic = italic;
    }

    public Integer getLeftnargin() {
        return leftnargin;
    }

    public void setLeftnargin(Integer leftnargin) {
        this.leftnargin = leftnargin;
    }

    public String getBgcolor() {
        return bgcolor;
    }

    public void setBgcolor(String bgcolor) {
        this.bgcolor = bgcolor;
    }

    public String getRotatetext() {
        return rotatetext;
    }

    public void setRotatetext(String rotatetext) {
        this.rotatetext = rotatetext;
    }

    public Integer getWrapwidth() {
        return wrapwidth;
    }

    public void setWrapwidth(Integer wrapwidth) {
        this.wrapwidth = wrapwidth;
    }

    public Integer getWrapheight() {
        return wrapheight;
    }

    public void setWrapheight(Integer wrapheight) {
        this.wrapheight = wrapheight;
    }

    public Boolean getWrap() {
        return wrap;
    }

    public void setWrap(Boolean wrap) {
        this.wrap = wrap;
    }

    public Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    public Integer getYradius() {
        return yradius;
    }

    public void setYradius(Integer yradius) {
        this.yradius = yradius;
    }

    public Integer getStartangle() {
        return startangle;
    }

    public void setStartangle(Integer startangle) {
        this.startangle = startangle;
    }

    public Integer getEndangle() {
        return endangle;
    }

    public void setEndangle(Integer endangle) {
        this.endangle = endangle;
    }

    public Integer getThickness() {
        return thickness;
    }

    public void setThickness(Integer thickness) {
        this.thickness = thickness;
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

    public String getScaletext() {
        return scaletext;
    }

    public void setScaletext(String scaletext) {
        this.scaletext = scaletext;
    }

    public String getScaleimages() {
        return scaleimages;
    }

    public void setScaleimages(String scaleimages) {
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

    public Integer getGrpxshift() {
        return grpxshift;
    }

    public void setGrpxshift(Integer grpxshift) {
        this.grpxshift = grpxshift;
    }

    public Integer getGrpyshift() {
        return grpyshift;
    }

    public void setGrpyshift(Integer grpyshift) {
        this.grpyshift = grpyshift;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getAlpha() {
        return alpha;
    }

    public void setAlpha(Integer alpha) {
        this.alpha = alpha;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public AlignType getTextalign() {
        return textalign;
    }

    public void setTextalign(AlignType textalign) {
        this.textalign = textalign;
    }

    public VAlignType getTextvalign() {
        return textvalign;
    }

    public void setTextvalign(VAlignType textvalign) {
        this.textvalign = textvalign;
    }

    public String getWraptext() {
        return wraptext;
    }

    public void setWraptext(String wraptext) {
        this.wraptext = wraptext;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getOrigw() {
        return origw;
    }

    public void setOrigw(String origw) {
        this.origw = origw;
    }

    public String getRigh() {
        return righ;
    }

    public void setRigh(String righ) {
        this.righ = righ;
    }
}
