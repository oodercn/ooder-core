package net.ooder.esd.bean.field;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.TimerAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.bean.ComponentBean;
import net.ooder.esd.custom.component.form.field.CustomTimerComponent;
import net.ooder.esd.tool.component.TimerComponent;
import net.ooder.esd.tool.properties.TimerProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.*;
@CustomClass(clazz = CustomTimerComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.TIMER
)
@AnnotationType(clazz = TimerAnnotation.class)
public class TimerFieldBean<T extends TimerProperties> implements ComponentBean {
    Boolean autoStart;
    Integer integer;
    Integer Interval;
    String xpath;

    public TimerFieldBean(TimerComponent timerComponent) {
        this.xpath = timerComponent.getPath();
        this.update((T) timerComponent.getProperties());
    }


    protected void update(T properties) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }

    public TimerFieldBean(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(TimerAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof TimerAnnotation) {
                fillData((TimerAnnotation) annotation);
            }
        }
    }

    public TimerFieldBean() {

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


    public TimerFieldBean(TimerAnnotation annotation) {
        fillData(annotation);
    }

    @Override
    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public Boolean getAutoStart() {
        return autoStart;
    }

    public void setAutoStart(Boolean autoStart) {
        this.autoStart = autoStart;
    }

    public Integer getInteger() {
        return integer;
    }

    public void setInteger(Integer integer) {
        this.integer = integer;
    }

    public Integer getInterval() {
        return Interval;
    }

    public void setInterval(Integer interval) {
        Interval = interval;
    }

    public TimerFieldBean fillData(TimerAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.TIMEPICKER;
    }
}
