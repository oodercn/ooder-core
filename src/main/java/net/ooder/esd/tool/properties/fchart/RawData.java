package net.ooder.esd.tool.properties.fchart;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.bean.fchart.items.RawDataItemBean;
import net.ooder.esd.tool.properties.item.UIItem;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.sf.cglib.beans.BeanMap;

import java.util.Map;

public class RawData extends UIItem {

    String id;
    String label;
    String value;
    String displayvalue;
    String color;
    String link;
    String tooltext;
    Boolean showlabel;
    Boolean showvalue;
    Boolean dashed;
    Integer alpha;
    String labelfont;
    String labelfontcolor;
    Integer labelfontsize;
    Boolean labelfontbold;
    String labelfontitalic;
    String labelbgcolor;
    String labelbordercolor;
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

    public RawData() {

    }


    public RawData(String lable, String value) {
        this.label = lable;
        this.value = value;
    }


    public RawData(Enum enumType) {
        OgnlUtil.setProperties(BeanMap.create(enumType), this, false, false);
    }

    public RawData(RawDataItemBean rawDataItemBean) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(rawDataItemBean), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getDashed() {
        return dashed;
    }

    public void setDashed(Boolean dashed) {
        this.dashed = dashed;
    }

    public Integer getAlpha() {
        return alpha;
    }

    public void setAlpha(Integer alpha) {
        this.alpha = alpha;
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


    public String getLabelfontitalic() {
        return labelfontitalic;
    }

    public void setLabelfontitalic(String labelfontitalic) {
        this.labelfontitalic = labelfontitalic;
    }

    public String getLabelbgcolor() {
        return labelbgcolor;
    }

    public void setLabelbgcolor(String labelbgcolor) {
        this.labelbgcolor = labelbgcolor;
    }

    public String getLabelbordercolor() {
        return labelbordercolor;
    }

    public void setLabelbordercolor(String labelbordercolor) {
        this.labelbordercolor = labelbordercolor;
    }

    public String getLabellink() {
        return labellink;
    }

    public void setLabellink(String labellink) {
        this.labellink = labellink;
    }
}
