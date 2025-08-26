package net.ooder.esd.tool.properties.form;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.ui.ImagePos;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.field.ButtonFieldBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.Map;

public class ButtonProperties extends HTMLButtonProperties implements FormField {

    public String image;
    public ImagePos imagePos;
    public String imageBgSize;
    public String imageClass;
    public String iconFontCode;
    @JSONField(name = "hAlign")
    public String hAlign;
    public Object value;
    public String type;
    public String tag;
    public Boolean FormField = false;

    public ButtonProperties(FieldFormConfig<ButtonFieldBean, ?> fieldFormConfig) {
        this.init(fieldFormConfig.getContainerBean());
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(fieldFormConfig.getAggConfig()), Map.class), this, false, false);
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(fieldFormConfig.getWidgetConfig()), Map.class), this, false, false);
    }

    public ButtonProperties(ButtonFieldBean buttonFieldBean, ContainerBean containerBean) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(buttonFieldBean), Map.class), this, false, false);
        if (containerBean != null) {
            this.init(containerBean);

        }

    }


    public ButtonProperties() {

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

    public String gethAlign() {
        return hAlign;
    }

    public void sethAlign(String hAlign) {
        this.hAlign = hAlign;
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public void setTag(String tag) {
        this.tag = tag;
    }


    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public Boolean getFormField() {
        return FormField;
    }

    public void setFormField(Boolean formField) {
        FormField = formField;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
