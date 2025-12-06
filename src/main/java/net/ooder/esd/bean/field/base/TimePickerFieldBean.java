package net.ooder.esd.bean.field.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.TimePickerAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.bean.ComponentBean;
import net.ooder.esd.bean.field.FieldBaseBean;
import net.ooder.esd.bean.field.FieldComponentBean;
import net.ooder.esd.custom.component.form.field.CustomTimePickerComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.ButtonComponent;
import net.ooder.esd.tool.component.ComboInputComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.SVGPathComponent;
import net.ooder.esd.tool.properties.form.ComboInputProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.*;
@CustomClass(clazz = CustomTimePickerComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.TIMEPICKER
)
@AnnotationType(clazz = TimePickerAnnotation.class)
public class TimePickerFieldBean<T extends ComboInputProperties>  extends FieldBaseBean<ComboInputComponent> {

    String id;

    String value;

    Boolean closeBtn;

    String xpath;
    public TimePickerFieldBean(ComboInputComponent comboInputComponent) {
        this.xpath=comboInputComponent.getPath();
        update((T) comboInputComponent.getProperties());
    }

    void update (T properties) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }

    public TimePickerFieldBean(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(TimePickerAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof TimePickerAnnotation) {
                fillData((TimePickerAnnotation) annotation);
            }
        }
    }

    public TimePickerFieldBean() {

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

    @Override
    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public TimePickerFieldBean(TimePickerAnnotation annotation) {
        fillData(annotation);
    }

    public TimePickerFieldBean fillData(TimePickerAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.TIMEPICKER;
    }

    @Override
    public void update(ModuleComponent moduleComponent, ComboInputComponent component) {

    }
}
