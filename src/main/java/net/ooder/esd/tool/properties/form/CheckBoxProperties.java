package net.ooder.esd.tool.properties.form;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.annotation.ui.IconPosType;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.field.base.CheckBoxFieldBean;
import net.ooder.esd.tool.properties.FieldProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.Map;

public class CheckBoxProperties extends FieldProperties {

    public CheckBoxProperties() {

    }

    public CheckBoxProperties(CheckBoxFieldBean fieldBean, ContainerBean containerBean) {
        OgnlUtil.setProperties( JSON.parseObject(JSON.toJSONString(fieldBean), Map.class),this,false,false);
        if (containerBean != null) {
            this.init(containerBean);

        }

    }

    public String iconFontCode;
    public IconPosType iconPos;
    public String VAlign;
    public String image;
    public String imagePos;
    public String imageBgSize;
    public String imageClass;
    public String fontColor;

    public String getIconFontCode() {
        return iconFontCode;
    }

    public void setIconFontCode(String iconFontCode) {
        this.iconFontCode = iconFontCode;
    }

    public IconPosType getIconPos() {
        return iconPos;
    }

    public void setIconPos(IconPosType iconPos) {
        this.iconPos = iconPos;
    }

    public String getVAlign() {
        return VAlign;
    }

    public void setVAlign(String VAlign) {
        this.VAlign = VAlign;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImagePos() {
        return imagePos;
    }

    public void setImagePos(String imagePos) {
        this.imagePos = imagePos;
    }

    public String getImageBgSize() {
        return imageBgSize;
    }

    public void setImageBgSize(String imageBgSize) {
        this.imageBgSize = imageBgSize;
    }

    @Override
    public String getImageClass() {
        return imageClass;
    }

    @Override
    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }
}
