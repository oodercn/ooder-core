package net.ooder.esd.bean.field;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.HiddenInputAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.custom.component.form.field.CustomFieldInputComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.HiddenInputComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.form.HiddenInputProperties;
import net.ooder.annotation.AnnotationType;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.*;

@CustomClass(clazz = CustomFieldInputComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.HIDDENINPUT
)
@AnnotationType(clazz = HiddenInputAnnotation.class)
public class HiddenInputFieldBean implements FieldComponentBean {

    String value;
    String xpath;

    public HiddenInputFieldBean(HiddenInputComponent component) {
        this.xpath = component.getPath();
        this.value = (String) component.getProperties().getValue();
    }


    public HiddenInputFieldBean(HiddenInputProperties properties) {
        this.value = (String) properties.getValue();
    }

    public HiddenInputFieldBean(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(HiddenInputAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof HiddenInputAnnotation) {
                fillData((HiddenInputAnnotation) annotation);
            }

        }

    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        // annotationBeans.add(this);
        return annotationBeans;
    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        return new HashSet<>();
    }


    public HiddenInputFieldBean() {

    }

    public HiddenInputFieldBean(HiddenInputAnnotation annotation) {
        fillData(annotation);
    }

    public HiddenInputFieldBean fillData(HiddenInputAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
        return ComponentType.HIDDENINPUT;
    }

    @Override
    public List<JavaSrcBean> getJavaSrcBeans() {
        return new ArrayList<>();
    }

    @Override
    public void update(ModuleComponent moduleComponent, Component component) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(component.getProperties()), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);

    }
}
