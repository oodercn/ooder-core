package net.ooder.esd.tool.properties.form;


import net.ooder.esd.tool.properties.FieldProperties;

public class ProgressBarProperties extends FieldProperties {

    public String captionTpl;
    public String type;
    public String fillBG;

    public String getCaptionTpl() {
        return captionTpl;
    }

    public void setCaptionTpl(String captionTpl) {
        this.captionTpl = captionTpl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFillBG() {
        return fillBG;
    }

    public void setFillBG(String fillBG) {
        this.fillBG = fillBG;
    }
}
