package net.ooder.esd.bean.field;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.ButtonAnnotation;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.custom.component.form.field.CustomButtonComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.ButtonComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@CustomClass(clazz = CustomButtonComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.BUTTON
)
@AnnotationType(clazz = ButtonAnnotation.class)
public class ButtonFieldBean extends FieldBaseBean<ButtonComponent> {


    String html;

    String image;

    String value;

    ImagePos imagePos;

    String imageBgSize;

    String iconFontCode;

    HAlignType hAlign;

    VAlignType vAlign;

    String fontColor;

    String fontSize;

    String fontWeight;

    String fontFamily;

    ButtonType buttonType;


    @Override
    public void update(ModuleComponent moduleComponent, ButtonComponent component) {

    }

    public ButtonFieldBean(ButtonComponent component) {
        super(component);
    }


    public ButtonFieldBean(Set<Annotation> annotations) {
        super(annotations);
        AnnotationUtil.fillDefaultValue(ButtonAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof ButtonAnnotation) {
                fillData((ButtonAnnotation) annotation);
            }
        }

    }

    public ButtonFieldBean() {

    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ButtonType getButtonType() {
        return buttonType;
    }

    public void setButtonType(ButtonType buttonType) {
        this.buttonType = buttonType;
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


    public String getIconFontCode() {
        return iconFontCode;
    }

    public void setIconFontCode(String iconFontCode) {
        this.iconFontCode = iconFontCode;
    }


    public ButtonFieldBean(ButtonAnnotation annotation) {
        fillData(annotation);
    }

    public ButtonFieldBean fillData(ButtonAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.BUTTON;
    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet = new HashSet<>();

        return classSet;
    }

}
