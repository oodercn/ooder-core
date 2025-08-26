package net.ooder.esd.bean.field.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.ColorPickerAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.bean.ComponentBean;
import net.ooder.esd.custom.component.form.field.CustomColorPickerComponent;
import net.ooder.esd.tool.component.ColorPickerComponent;
import net.ooder.esd.tool.properties.Properties;
import net.ooder.esd.tool.properties.form.ColorPickerProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.*;
@CustomClass(clazz = CustomColorPickerComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.COLORPICKER
)
@AnnotationType(clazz = ColorPickerAnnotation.class)
public class ColorPickerFieldBean implements ComponentBean {

    String id;

    String height;

    String width;

    String value;

    Boolean closeBtn;

    Boolean barDisplay;

    Boolean advance;

    String xpath;


    public ColorPickerFieldBean(ColorPickerComponent component) {
        ColorPickerProperties properties = component.getProperties();
        this.xpath=component.getPath();
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }


    public ColorPickerFieldBean(Properties properties) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }



    public ColorPickerFieldBean(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(ColorPickerAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof ColorPickerAnnotation) {
                fillData((ColorPickerAnnotation) annotation);
            }
        }
    }

    public ColorPickerFieldBean() {

    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        annotationBeans.add(this);
        return annotationBeans;
    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        return new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getCloseBtn() {
        return closeBtn;
    }

    public void setCloseBtn(Boolean closeBtn) {
        this.closeBtn = closeBtn;
    }

    public Boolean getBarDisplay() {
        return barDisplay;
    }

    public void setBarDisplay(Boolean barDisplay) {
        this.barDisplay = barDisplay;
    }

    public Boolean getAdvance() {
        return advance;
    }

    public void setAdvance(Boolean advance) {
        this.advance = advance;
    }

    public ColorPickerFieldBean(ColorPickerAnnotation annotation) {
        fillData(annotation);
    }

    public ColorPickerFieldBean fillData(ColorPickerAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    @Override
    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.COLORPICKER;
    }
}
