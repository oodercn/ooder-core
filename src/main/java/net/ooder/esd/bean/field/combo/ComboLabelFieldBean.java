package net.ooder.esd.bean.field.combo;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.ComboLabelAnnotation;
import net.ooder.esd.annotation.field.LabelAnnotation;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.bean.field.LabelFieldBean;
import net.ooder.esd.custom.ESDField;
import net.ooder.annotation.AnnotationType;
import net.ooder.esd.custom.component.form.field.combo.CustomComboLabelComponent;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@CustomClass(clazz = CustomComboLabelComponent.class,
        viewType = CustomViewType.COMBOBOX,
        inputType = {ComboInputType.label, ComboInputType.button, ComboInputType.split, ComboInputType.none, ComboInputType.text},
        componentType = ComponentType.COMBOINPUT
)
@AnnotationType(clazz = ComboLabelAnnotation.class)
public class ComboLabelFieldBean extends ComboInputFieldBean {

    String dateEditorTpl;

    LabelFieldBean labelFieldBean;

    public ComboLabelFieldBean() {

    }




    public ComboLabelFieldBean(ComboInputType inputType) {
        super(inputType);
        AnnotationUtil.fillDefaultValue(ComboLabelAnnotation.class, this);
        labelFieldBean = AnnotationUtil.fillDefaultValue(LabelAnnotation.class, new LabelFieldBean());
    }

    public ComboLabelFieldBean(ESDField esdField, Set<Annotation> annotations, ComboInputType inputType) {
        super(esdField, annotations);
        if (inputType != null) {
            this.inputType = inputType;
        }
        AnnotationUtil.fillDefaultValue(ComboLabelAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof ComboLabelAnnotation) {
                fillData((ComboLabelAnnotation) annotation);
            }
        }
        labelFieldBean = AnnotationUtil.fillDefaultValue(LabelAnnotation.class, new LabelFieldBean());
        for (Annotation annotation : annotations) {
            if (annotation instanceof LabelAnnotation) {
                labelFieldBean = new LabelFieldBean((LabelAnnotation) annotation);
            }
        }

    }


    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = super.getAnnotationBeans();
        if (labelFieldBean != null && !AnnotationUtil.getAnnotationMap(labelFieldBean).isEmpty()) {
            annotationBeans.add(labelFieldBean);
        }
        if (!annotationBeans.contains(this)) {
            annotationBeans.add(this);
        }
        return annotationBeans;
    }

    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet=new HashSet<>();
        if (labelFieldBean!=null){
            classSet.addAll(labelFieldBean.getOtherClass());
        }

        return ClassUtility.checkBase(classSet);
    }

    public LabelFieldBean getLabelFieldBean() {
        return labelFieldBean;
    }

    public void setLabelFieldBean(LabelFieldBean labelFieldBean) {
        this.labelFieldBean = labelFieldBean;
    }

    public String getDateEditorTpl() {
        return dateEditorTpl;
    }

    public void setDateEditorTpl(String dateEditorTpl) {
        this.dateEditorTpl = dateEditorTpl;
    }

    public ComboLabelFieldBean(ComboLabelAnnotation annotation) {
        fillData(annotation);
    }

    @Override
    public ComboInputType[] getComboInputType() {
        return getComboInputType(ComboLabelAnnotation.class);
    }

    public ComboLabelFieldBean fillData(ComboLabelAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
