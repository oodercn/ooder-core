package net.ooder.esd.tool.properties;


import net.ooder.esd.annotation.ui.OverflowType;

public class SpanProperties extends FieldProperties {

    public String nodeName;

    public String html;


    public Boolean selectable;

    public Integer tabindex;

    public OverflowType overflow;


    public SpanProperties() {

    }


    public Boolean getSelectable() {
        return selectable;
    }

    public void setSelectable(Boolean selectable) {
        this.selectable = selectable;
    }

    public Integer getTabindex() {
        return tabindex;
    }

    public void setTabindex(Integer tabindex) {
        this.tabindex = tabindex;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public OverflowType getOverflow() {
        return overflow;
    }

    public void setOverflow(OverflowType overflow) {
        this.overflow = overflow;
    }
}
