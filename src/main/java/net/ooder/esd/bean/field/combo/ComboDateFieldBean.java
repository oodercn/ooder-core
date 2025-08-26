package net.ooder.esd.bean.field.combo;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.ComboDateAnnotation;
import net.ooder.esd.annotation.field.DatePickerAnnotation;
import net.ooder.esd.annotation.field.TimePickerAnnotation;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.bean.field.base.DatePickerFieldBean;
import net.ooder.esd.bean.field.base.TimePickerFieldBean;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.custom.component.form.field.combo.CustomComboInputComponent;
import net.ooder.esd.tool.component.ComboInputComponent;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@CustomClass(clazz = CustomComboInputComponent.class,
        viewType = CustomViewType.COMBOBOX,
        inputType = {ComboInputType.date, ComboInputType.datetime, ComboInputType.time},
        componentType = ComponentType.COMBOINPUT
)
@AnnotationType(clazz = ComboDateAnnotation.class)
public class ComboDateFieldBean extends ComboInputFieldBean {

    String dateEditorTpl;

    DatePickerFieldBean dataPickBean;

    TimePickerFieldBean tiemPickBean;

    public ComboDateFieldBean(){

    }

    public ComboDateFieldBean(ComboInputComponent comboInputComponent) {
        super(comboInputComponent);
        this.update(comboInputComponent.getProperties());
        dataPickBean = new DatePickerFieldBean(comboInputComponent);
        tiemPickBean = new TimePickerFieldBean(comboInputComponent);
    }





    public ComboDateFieldBean(ComboInputType inputType) {
        super(inputType);
        AnnotationUtil.fillDefaultValue(ComboDateAnnotation.class, this);
        dataPickBean = AnnotationUtil.fillDefaultValue(DatePickerAnnotation.class, new DatePickerFieldBean());
        tiemPickBean = AnnotationUtil.fillDefaultValue(TimePickerAnnotation.class, new TimePickerFieldBean());
    }
    public ComboDateFieldBean(ESDField esdField, Set<Annotation> annotations, ComboInputType inputType) {
        super(esdField, annotations);
        if (inputType != null) {
            this.inputType = inputType;
        }

        AnnotationUtil.fillDefaultValue(ComboDateAnnotation.class, this);
        dataPickBean = AnnotationUtil.fillDefaultValue(DatePickerAnnotation.class, new DatePickerFieldBean());
        tiemPickBean = AnnotationUtil.fillDefaultValue(TimePickerAnnotation.class, new TimePickerFieldBean());
        for (Annotation annotation : annotations) {
            if (annotation instanceof ComboDateAnnotation) {
                fillData((ComboDateAnnotation) annotation);
            }
        }

        for (Annotation annotation : annotations) {
            if (annotation instanceof DatePickerAnnotation) {
                dataPickBean = new DatePickerFieldBean((DatePickerAnnotation) annotation);
            }
        }
        for (Annotation annotation : annotations) {
            if (annotation instanceof TimePickerAnnotation) {
                tiemPickBean = new TimePickerFieldBean((TimePickerAnnotation) annotation);
            }
        }
    }
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet=new HashSet<>();
        if (dataPickBean!=null){
            classSet.addAll(dataPickBean.getOtherClass());
        }
        if (tiemPickBean!=null){
            classSet.addAll(tiemPickBean.getOtherClass());
        }
        return classSet;
    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans =super.getAnnotationBeans();
        if (dataPickBean!=null && !AnnotationUtil.getAnnotationMap(dataPickBean).isEmpty()){
            annotationBeans.add(dataPickBean);
        }
        if (tiemPickBean!=null && !AnnotationUtil.getAnnotationMap(tiemPickBean).isEmpty()){
            annotationBeans.add(tiemPickBean);
        }
        if (!annotationBeans.contains(this)) {
            annotationBeans.add(this);
        }
        return annotationBeans;
    }



    public TimePickerFieldBean getTiemPickBean() {
        return tiemPickBean;
    }

    public void setTiemPickBean(TimePickerFieldBean tiemPickBean) {
        this.tiemPickBean = tiemPickBean;
    }

    public DatePickerFieldBean getDataPickBean() {
        return dataPickBean;
    }

    public void setDataPickBean(DatePickerFieldBean dataPickBean) {
        this.dataPickBean = dataPickBean;
    }

    public String getDateEditorTpl() {
        return dateEditorTpl;
    }

    public void setDateEditorTpl(String dateEditorTpl) {
        this.dateEditorTpl = dateEditorTpl;
    }

    public ComboDateFieldBean(ComboDateAnnotation annotation) {
        fillData(annotation);
    }

    @Override
    public ComboInputType[] getComboInputType() {
        return getComboInputType(ComboDateAnnotation.class);
    }

    public ComboDateFieldBean fillData(ComboDateAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
