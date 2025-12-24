package net.ooder.esd.bean.field.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.Input;
import net.ooder.esd.annotation.field.InputAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.InputType;
import net.ooder.esd.bean.field.FieldBaseBean;
import net.ooder.esd.bean.field.InputBean;
import net.ooder.esd.custom.component.form.field.CustomFieldInputComponent;
import net.ooder.esd.tool.component.InputComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.form.InputProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@CustomClass(clazz = CustomFieldInputComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.INPUT
)
@AnnotationType(clazz = InputAnnotation.class)
public class InputFieldBean extends FieldBaseBean<InputComponent> {

    public InputBean inputBean;

    public InputType texttype;

    public Integer maxlength;

    public Boolean multiLines;


    public Boolean hiddenBorder;

    public Integer autoexpand;

    public InputFieldBean(ModuleComponent moduleComponent, InputComponent component) {
        super(moduleComponent, component);
    }


    public void updateProperties(InputProperties properties) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }

    @Override
    public void update(ModuleComponent moduleComponent, InputComponent component) {
        this.updateProperties(component.getProperties());
        super.update(moduleComponent, component);
        inputBean = new InputBean(component);
    }



    public InputFieldBean() {

    }

    public InputFieldBean(Set<Annotation> annotations) {
        super(annotations);
        AnnotationUtil.fillDefaultValue(InputAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof Input) {
                inputBean = new InputBean((Input) annotation);
            }
            if (annotation instanceof InputAnnotation) {
                fillData((InputAnnotation) annotation);
            }
        }

    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = super.getAnnotationBeans();
        if (inputBean != null && !AnnotationUtil.getAnnotationMap(inputBean).isEmpty()) {
            annotationBeans.addAll(inputBean.getAnnotationBeans());
        }
        annotationBeans.add(this);
        return annotationBeans;
    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classes = new HashSet<>();

        return classes;
    }

    public Boolean getHiddenBorder() {
        return hiddenBorder;
    }

    public void setHiddenBorder(Boolean hiddenBorder) {
        this.hiddenBorder = hiddenBorder;
    }

    public InputType getTexttype() {
        return texttype;
    }

    public void setTexttype(InputType texttype) {
        this.texttype = texttype;
    }

    public Integer getMaxlength() {
        return maxlength;
    }

    public void setMaxlength(Integer maxlength) {
        this.maxlength = maxlength;
    }

    public Boolean getMultiLines() {
        return multiLines;
    }

    public void setMultiLines(Boolean multiLines) {
        this.multiLines = multiLines;
    }

    public Integer getAutoexpand() {
        return autoexpand;
    }

    public void setAutoexpand(Integer autoexpand) {
        this.autoexpand = autoexpand;
    }

    public InputBean getInputBean() {
        return inputBean;
    }

    public void setInputBean(InputBean inputBean) {
        this.inputBean = inputBean;
    }

    public InputFieldBean(InputAnnotation annotation) {
        fillData(annotation);
    }

    public InputFieldBean fillData(InputAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.INPUT;
    }

}
