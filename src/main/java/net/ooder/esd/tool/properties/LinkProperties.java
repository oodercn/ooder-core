package net.ooder.esd.tool.properties;

public class LinkProperties extends AbsUIProperties {

    String caption;
    String href;
    String target;

    public LinkProperties() {

    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
