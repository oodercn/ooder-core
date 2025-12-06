package net.ooder.esd.bean.field;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.ImageAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CursorType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.custom.component.form.field.CustomImageComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.ImageComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.form.ComboInputProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.*;

@CustomClass(clazz = CustomImageComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.IMAGE
)
@AnnotationType(clazz = ImageAnnotation.class)
public class ImageFieldBean extends FieldBaseBean<ImageComponent> {

    Integer maxWidth;

    Integer maxHeight;

    String width;

    String height;

    CursorType cursor;

    String src;

    String alt;


    public ImageFieldBean(Set<Annotation> annotations) {
        super(annotations);
        AnnotationUtil.fillDefaultValue(ImageAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof ImageAnnotation) {
                fillData((ImageAnnotation) annotation);
            }
        }
    }

    @Override
    public void update(ModuleComponent moduleComponent, ImageComponent component) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(component.getProperties()), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }

    public ImageFieldBean(ImageComponent component) {
        super(component);
    }

    public ImageFieldBean(ComboInputProperties properties ) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }
    public ImageFieldBean() {

    }


    public CursorType getCursor() {
        return cursor;
    }

    public void setCursor(CursorType cursor) {
        this.cursor = cursor;
    }

    public Integer getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(Integer maxWidth) {
        this.maxWidth = maxWidth;
    }

    public Integer getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(Integer maxHeight) {
        this.maxHeight = maxHeight;
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

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public ImageFieldBean(ImageAnnotation annotation) {
        fillData(annotation);
    }

    public ImageFieldBean fillData(ImageAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.IMAGE;
    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet=new HashSet<>();

        return classSet;
    }
}
