package net.ooder.esd.tool.properties.fchart;

import net.ooder.esd.annotation.fchart.ValuePosition;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.sf.cglib.beans.BeanMap;

public class FSet {

    String label;

    String value;

    String displayvalue;

    String color;

    String link;

    String tooltext;

    Boolean showlabel;

    Boolean showvalue;

    Integer dashed;

    Integer alpha;

    String labelfont;

    String labelfontcolor;

    Integer labelfontsize;

    Boolean labelfontbold;

    Boolean labelfontitalic;

    String labelbgcolor;

    Integer labelalpha;

    Integer labelbgalpha;

    Integer labelborderalpha;

    Integer labelborderpadding;

    Integer labelborderradius;

    Integer labelborderthickness;

    Boolean labelborderdashed;

    Integer labelborderdashlen;

    Integer labelborderdashgap;

    String labellink;

    String anchorsides;

    Integer anchorradius;

    String anchorbordercolor;

    Integer anchorborderthickness;

    String anchorbgcolor;

    Integer anchoralpha;

    Integer anchorbgalpha;

    ValuePosition valuePosition;

    public FSet() {

    }
    public FSet(Enum linenum) {
        OgnlUtil.setProperties(BeanMap.create(linenum), this, false, false);
    }
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDisplayvalue() {
        return displayvalue;
    }

    public void setDisplayvalue(String displayvalue) {
        this.displayvalue = displayvalue;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTooltext() {
        return tooltext;
    }

    public void setTooltext(String tooltext) {
        this.tooltext = tooltext;
    }

    public Boolean getShowlabel() {
        return showlabel;
    }

    public void setShowlabel(Boolean showlabel) {
        this.showlabel = showlabel;
    }

    public Boolean getShowvalue() {
        return showvalue;
    }

    public void setShowvalue(Boolean showvalue) {
        this.showvalue = showvalue;
    }

    public Integer getDashed() {
        return dashed;
    }

    public void setDashed(Integer dashed) {
        this.dashed = dashed;
    }

    public Integer getAlpha() {
        return alpha;
    }

    public void setAlpha(Integer alpha) {
        this.alpha = alpha;
    }

    public String getLabelfont() {
        return labelfont;
    }

    public void setLabelfont(String labelfont) {
        this.labelfont = labelfont;
    }

    public String getLabelfontcolor() {
        return labelfontcolor;
    }

    public void setLabelfontcolor(String labelfontcolor) {
        this.labelfontcolor = labelfontcolor;
    }

    public Integer getLabelfontsize() {
        return labelfontsize;
    }

    public void setLabelfontsize(Integer labelfontsize) {
        this.labelfontsize = labelfontsize;
    }

    public Boolean getLabelfontbold() {
        return labelfontbold;
    }

    public void setLabelfontbold(Boolean labelfontbold) {
        this.labelfontbold = labelfontbold;
    }

    public Boolean getLabelfontitalic() {
        return labelfontitalic;
    }

    public void setLabelfontitalic(Boolean labelfontitalic) {
        this.labelfontitalic = labelfontitalic;
    }

    public String getLabelbgcolor() {
        return labelbgcolor;
    }

    public void setLabelbgcolor(String labelbgcolor) {
        this.labelbgcolor = labelbgcolor;
    }

    public Integer getLabelalpha() {
        return labelalpha;
    }

    public void setLabelalpha(Integer labelalpha) {
        this.labelalpha = labelalpha;
    }

    public Integer getLabelbgalpha() {
        return labelbgalpha;
    }

    public void setLabelbgalpha(Integer labelbgalpha) {
        this.labelbgalpha = labelbgalpha;
    }

    public Integer getLabelborderalpha() {
        return labelborderalpha;
    }

    public void setLabelborderalpha(Integer labelborderalpha) {
        this.labelborderalpha = labelborderalpha;
    }

    public Integer getLabelborderpadding() {
        return labelborderpadding;
    }

    public void setLabelborderpadding(Integer labelborderpadding) {
        this.labelborderpadding = labelborderpadding;
    }

    public Integer getLabelborderradius() {
        return labelborderradius;
    }

    public void setLabelborderradius(Integer labelborderradius) {
        this.labelborderradius = labelborderradius;
    }

    public Integer getLabelborderthickness() {
        return labelborderthickness;
    }

    public void setLabelborderthickness(Integer labelborderthickness) {
        this.labelborderthickness = labelborderthickness;
    }

    public Boolean getLabelborderdashed() {
        return labelborderdashed;
    }

    public void setLabelborderdashed(Boolean labelborderdashed) {
        this.labelborderdashed = labelborderdashed;
    }

    public Integer getLabelborderdashlen() {
        return labelborderdashlen;
    }

    public void setLabelborderdashlen(Integer labelborderdashlen) {
        this.labelborderdashlen = labelborderdashlen;
    }

    public Integer getLabelborderdashgap() {
        return labelborderdashgap;
    }

    public void setLabelborderdashgap(Integer labelborderdashgap) {
        this.labelborderdashgap = labelborderdashgap;
    }

    public String getLabellink() {
        return labellink;
    }

    public void setLabellink(String labellink) {
        this.labellink = labellink;
    }

    public String getAnchorsides() {
        return anchorsides;
    }

    public void setAnchorsides(String anchorsides) {
        this.anchorsides = anchorsides;
    }

    public Integer getAnchorradius() {
        return anchorradius;
    }

    public void setAnchorradius(Integer anchorradius) {
        this.anchorradius = anchorradius;
    }

    public String getAnchorbordercolor() {
        return anchorbordercolor;
    }

    public void setAnchorbordercolor(String anchorbordercolor) {
        this.anchorbordercolor = anchorbordercolor;
    }

    public Integer getAnchorborderthickness() {
        return anchorborderthickness;
    }

    public void setAnchorborderthickness(Integer anchorborderthickness) {
        this.anchorborderthickness = anchorborderthickness;
    }

    public String getAnchorbgcolor() {
        return anchorbgcolor;
    }

    public void setAnchorbgcolor(String anchorbgcolor) {
        this.anchorbgcolor = anchorbgcolor;
    }

    public Integer getAnchoralpha() {
        return anchoralpha;
    }

    public void setAnchoralpha(Integer anchoralpha) {
        this.anchoralpha = anchoralpha;
    }

    public Integer getAnchorbgalpha() {
        return anchorbgalpha;
    }

    public void setAnchorbgalpha(Integer anchorbgalpha) {
        this.anchorbgalpha = anchorbgalpha;
    }

    public ValuePosition getValuePosition() {
        return valuePosition;
    }

    public void setValuePosition(ValuePosition valuePosition) {
        this.valuePosition = valuePosition;
    }
}
