package net.ooder.esd.tool.properties;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.annotation.ui.ImagePos;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.field.IconFieldBean;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.Map;

public class IconProperties extends FieldProperties {


    public String html;
    public Map<String, Object> attributes;
    public Boolean defaultFocus;
    public String image;
    public ImagePos imagePos;
    public String imageBgSize;
    public String imageClass;
    public String iconFontCode;
    public String iconFontSize;
    public String iconColor;


    public IconProperties() {

    }

    public IconProperties(IconFieldBean fieldBean, ContainerBean containerBean) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(fieldBean), Map.class), this, false, false);
        if (containerBean != null) {
            this.init(containerBean);
        }

        this.defaultFocus = fieldBean.getDefaultFocus();
        this.image = fieldBean.getImage();
        this.imagePos = fieldBean.getImagePos();
        this.imageBgSize = fieldBean.getImageBgSize();
        this.imageClass = fieldBean.getImageClass();
        this.iconFontCode = fieldBean.getIconFontCode();
        this.iconFontSize = fieldBean.getIconFontSize();
        this.iconColor = fieldBean.getIconColor();
        this.html = fieldBean.getHtml();
    }



    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Boolean getDefaultFocus() {
        return defaultFocus;
    }

    public void setDefaultFocus(Boolean defaultFocus) {
        this.defaultFocus = defaultFocus;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ImagePos getImagePos() {
        return imagePos;
    }

    public void setImagePos(ImagePos imagePos) {
        this.imagePos = imagePos;
    }

    public String getImageBgSize() {
        return imageBgSize;
    }

    public void setImageBgSize(String imageBgSize) {
        this.imageBgSize = imageBgSize;
    }

    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }

    public String getIconFontCode() {
        return iconFontCode;
    }

    public void setIconFontCode(String iconFontCode) {
        this.iconFontCode = iconFontCode;
    }

    public String getIconFontSize() {
        return iconFontSize;
    }

    public void setIconFontSize(String iconFontSize) {
        this.iconFontSize = iconFontSize;
    }

    public String getIconColor() {
        return iconColor;
    }

    public void setIconColor(String iconColor) {
        this.iconColor = iconColor;
    }
}
