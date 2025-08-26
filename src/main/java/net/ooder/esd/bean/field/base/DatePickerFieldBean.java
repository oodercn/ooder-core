package net.ooder.esd.bean.field.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.DatePickerAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.bean.ComponentBean;
import net.ooder.esd.custom.component.form.field.CustomDatePickerComponent;
import net.ooder.esd.tool.component.ComboInputComponent;
import net.ooder.esd.tool.properties.form.ComboInputProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.*;
@CustomClass(clazz = CustomDatePickerComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.DATEPICKER
)
@AnnotationType(clazz = DatePickerAnnotation.class)
public class DatePickerFieldBean implements ComponentBean {

    String id;

    public String xpath;

    Boolean timeInput;

    String value;

    Boolean closeBtn;

    Integer firstDayOfWeek;

    String offDays;

    Boolean hideWeekLabels;

    String dateInputFormat;


    public DatePickerFieldBean(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(DatePickerAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof DatePickerAnnotation) {
                fillData((DatePickerAnnotation) annotation);
            }
        }
    }

    public DatePickerFieldBean() {

    }
    public DatePickerFieldBean(ComboInputComponent component) {
        this.xpath=component.getPath();
        this.update((ComboInputProperties) component.getProperties());

    }

  public   void update(ComboInputProperties properties) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
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

    public Boolean getTimeInput() {
        return timeInput;
    }

    public void setTimeInput(Boolean timeInput) {
        this.timeInput = timeInput;
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

    public Integer getFirstDayOfWeek() {
        return firstDayOfWeek;
    }

    public void setFirstDayOfWeek(Integer firstDayOfWeek) {
        this.firstDayOfWeek = firstDayOfWeek;
    }

    public String getOffDays() {
        return offDays;
    }

    public void setOffDays(String offDays) {
        this.offDays = offDays;
    }

    public Boolean getHideWeekLabels() {
        return hideWeekLabels;
    }

    public void setHideWeekLabels(Boolean hideWeekLabels) {
        this.hideWeekLabels = hideWeekLabels;
    }

    public String getDateInputFormat() {
        return dateInputFormat;
    }

    public void setDateInputFormat(String dateInputFormat) {
        this.dateInputFormat = dateInputFormat;
    }

    public DatePickerFieldBean(DatePickerAnnotation annotation) {
        fillData(annotation);
    }

    public DatePickerFieldBean fillData(DatePickerAnnotation annotation) {
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
        return ComponentType.DATEPICKER;
    }
}
