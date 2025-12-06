package net.ooder.esd.bean.field;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.HTMLButtonAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.custom.component.form.field.CustomHTMLButtonComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.HTMLButtonComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@CustomClass(clazz = CustomHTMLButtonComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.HTMLBUTTON
)
@AnnotationType(clazz = HTMLButtonAnnotation.class)
public class HTMLButtonFieldBean extends FieldBaseBean<HTMLButtonComponent> {


    String nodeName;

    Integer tabindex;

    String width;

    String height;

    Boolean shadow;


    String html;

    String caption;

    String iconFontCode;

    String fontColor;

    String fontSize;

    String fontWeight;

    String fontFamily;


    @Override
    public void update(ModuleComponent moduleComponent, HTMLButtonComponent component) {

    }

    public HTMLButtonFieldBean(HTMLButtonComponent component) {
        super(component);
    }

    public HTMLButtonFieldBean( Set<Annotation> annotations) {
        super(annotations);
        AnnotationUtil.fillDefaultValue(HTMLButtonAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof HTMLButtonAnnotation) {
                fillData((HTMLButtonAnnotation) annotation);
            }
        }

    }

    public HTMLButtonFieldBean() {

    }
    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        return new HashSet<>();
    }
    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public Integer getTabindex() {
        return tabindex;
    }

    public void setTabindex(Integer tabindex) {
        this.tabindex = tabindex;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public Boolean getShadow() {
        return shadow;
    }

    public void setShadow(Boolean shadow) {
        this.shadow = shadow;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getIconFontCode() {
        return iconFontCode;
    }

    public void setIconFontCode(String iconFontCode) {
        this.iconFontCode = iconFontCode;
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

    public HTMLButtonFieldBean(HTMLButtonAnnotation annotation) {
        fillData(annotation);
    }

    public HTMLButtonFieldBean fillData(HTMLButtonAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.HTMLBUTTON;
    }

}
