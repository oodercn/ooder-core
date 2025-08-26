package net.ooder.esd.bean.field;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.IconAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.ImagePos;
import net.ooder.esd.custom.component.form.field.CustomIconComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.IconComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@CustomClass(clazz = CustomIconComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.ICON
)
@AnnotationType(clazz = IconAnnotation.class)
public class IconFieldBean extends FieldBaseBean<IconComponent> {


    String html;

    String attributes;

    String renderer;

    Boolean defaultFocus;

    Integer tabindex;

    String image;

    ImagePos imagePos;

    String imageBgSize;

    String imageClass;

    String iconFontCode;

    String iconFontSize;

    String iconColor;

    @Override
    public List<JavaSrcBean> update(ModuleComponent moduleComponent, IconComponent component) {

        return new ArrayList<>();
    }

    public IconFieldBean(IconComponent component) {
        super(component);
    }


    public IconFieldBean(Set<Annotation> annotations) {
        super(annotations);
        AnnotationUtil.fillDefaultValue(IconAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof IconAnnotation) {
                fillData((IconAnnotation) annotation);
            }
        }
    }


    public IconFieldBean() {

    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        return new HashSet<>();
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public String getRenderer() {
        return renderer;
    }

    public void setRenderer(String renderer) {
        this.renderer = renderer;
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

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }


    public Integer getTabindex() {
        return tabindex;
    }

    public void setTabindex(Integer tabindex) {
        this.tabindex = tabindex;
    }


    public IconFieldBean(IconAnnotation annotation) {
        fillData(annotation);
    }

    public IconFieldBean fillData(IconAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.ICON;
    }
}
