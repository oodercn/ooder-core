package net.ooder.esd.tool.properties.form;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.ui.HAlignType;
import net.ooder.esd.annotation.ui.ImagePos;
import net.ooder.esd.annotation.ui.VAlignType;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.field.LabelFieldBean;
import net.ooder.esd.tool.properties.FieldProperties;

public class LabelProperties extends FieldProperties {

    Boolean selectable;
    String clock;
    String image;

    ImagePos imagePos;
    String imageBgSize;
    String imageClass;
    String iconFontCode;
    String iconFontColor;
    @JSONField(name = "hAlign")
    HAlignType hAlign;
    @JSONField(name = "vAlign")
    VAlignType vAlign;
    String fontColor;
    String fontSize;
    String fontWeight;
    String fontFamily;
    String position;
    String excelCellFormula;


    public LabelProperties() {

    }

    public LabelProperties(LabelFieldBean labelFieldBean, ContainerBean containerBean) {

        if (containerBean != null) {
            this.init(containerBean);

        }

        if (labelFieldBean != null) {
            this.clock = labelFieldBean.getClock();
            this.image = labelFieldBean.getImage();
            this.imagePos = labelFieldBean.getImagePos();
            this.imageBgSize = labelFieldBean.getImageBgSize();
            this.iconFontCode = labelFieldBean.getIconFontCode();
            this.iconFontColor = labelFieldBean.getIconFontCode();
            this.hAlign = labelFieldBean.gethAlign();
            this.vAlign = labelFieldBean.getvAlign();
            this.fontColor = labelFieldBean.getFontColor();
            this.fontSize = labelFieldBean.getFontSize();
            this.fontWeight = labelFieldBean.getFontWeight();
            this.fontFamily = labelFieldBean.getFontFamily();
            if (labelFieldBean.getImageClass() != null) {
                this.imageClass = labelFieldBean.getImageClass();
            }

            if (labelFieldBean.getSelectable() != null) {
                this.selectable = labelFieldBean.getSelectable();
            }
            if (labelFieldBean.getPosition() != null) {
                this.position = labelFieldBean.getPosition();
            }
            this.excelCellFormula = labelFieldBean.getExcelCellFormula();
        }


    }


    public String getIconFontColor() {
        return iconFontColor;
    }

    public void setIconFontColor(String iconFontColor) {
        this.iconFontColor = iconFontColor;
    }

    public Boolean getSelectable() {
        return selectable;
    }

    public void setSelectable(Boolean selectable) {
        this.selectable = selectable;
    }

    public String getClock() {
        return clock;
    }

    public void setClock(String clock) {
        this.clock = clock;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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

    public HAlignType gethAlign() {
        return hAlign;
    }

    public void sethAlign(HAlignType hAlign) {
        this.hAlign = hAlign;
    }

    public VAlignType getvAlign() {
        return vAlign;
    }

    public void setvAlign(VAlignType vAlign) {
        this.vAlign = vAlign;
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


    public String getExcelCellFormula() {
        return excelCellFormula;
    }

    public void setExcelCellFormula(String excelCellFormula) {
        this.excelCellFormula = excelCellFormula;
    }
}
