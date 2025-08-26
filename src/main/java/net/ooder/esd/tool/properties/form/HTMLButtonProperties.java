package net.ooder.esd.tool.properties.form;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.field.HTMLButtonFieldBean;
import net.ooder.esd.tool.properties.ElementProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.Map;

public class HTMLButtonProperties extends ElementProperties {

    public Boolean readonly;
    public String caption;
    public Boolean disabled;
    public Boolean shadow;
    public String fontColor;
    public String fontSize;
    public String fontWeight;
    public String fontFamily;

    public HTMLButtonProperties() {

    }

    public HTMLButtonProperties(HTMLButtonFieldBean fieldBean, ContainerBean containerBean) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(fieldBean), Map.class), this, false, false);
        if (containerBean != null) {
            this.init(containerBean);

        }

    }


    public Boolean getReadonly() {
        return readonly;
    }

    public void setReadonly(Boolean readonly) {
        this.readonly = readonly;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Boolean getShadow() {
        return shadow;
    }

    public void setShadow(Boolean shadow) {
        this.shadow = shadow;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public String getFontWeight() {
        return fontWeight;
    }

    public void setFontWeight(String fontWeight) {
        this.fontWeight = fontWeight;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }
}
