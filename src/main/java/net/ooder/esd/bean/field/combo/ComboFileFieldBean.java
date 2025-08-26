package net.ooder.esd.bean.field.combo;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.ComboFileAnnotation;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.custom.component.form.field.combo.CustomComboInputComponent;
import net.ooder.esd.tool.component.ComboInputComponent;
import net.ooder.esd.tool.properties.form.ComboInputProperties;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@CustomClass(clazz = CustomComboInputComponent.class,
        viewType = CustomViewType.COMBOBOX,
        inputType = {ComboInputType.file},
        componentType = ComponentType.COMBOINPUT
)
@AnnotationType(clazz = ComboFileAnnotation.class)
public class ComboFileFieldBean extends ComboInputFieldBean {

    String fileAccept;

    Boolean fileMultiple;

    public ComboFileFieldBean() {

    }

    public ComboFileFieldBean(ComboInputComponent comboInputComponent) {
        super(comboInputComponent);
        update(comboInputComponent.getProperties());
    }

    protected void update(ComboInputProperties labelProperties) {

        fileAccept = labelProperties.getFileAccept();
        fileMultiple = labelProperties.getFileMultiple();
    }

    public ComboFileFieldBean(ComboInputType inputType) {
        super(inputType);
        AnnotationUtil.fillDefaultValue(ComboFileAnnotation.class, this);
    }

    public ComboFileFieldBean(ESDField esdField, Set<Annotation> annotations, ComboInputType inputType) {
        super(esdField, annotations);
        if (inputType != null) {
            this.inputType = inputType;
        }
        AnnotationUtil.fillDefaultValue(ComboFileAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof ComboFileAnnotation) {
                fillData((ComboFileAnnotation) annotation);
            }
        }

    }

    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet=new HashSet<>();

        return classSet;
    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = super.getAnnotationBeans();
        if (!annotationBeans.contains(this)) {
            annotationBeans.add(this);
        }
        return annotationBeans;
    }

    public String getFileAccept() {
        return fileAccept;
    }


    public void setFileAccept(String fileAccept) {
        this.fileAccept = fileAccept;
    }


    public Boolean getFileMultiple() {
        return fileMultiple;
    }


    public void setFileMultiple(Boolean fileMultiple) {
        this.fileMultiple = fileMultiple;
    }

    public ComboFileFieldBean(ComboFileAnnotation annotation) {
        fillData(annotation);
    }

    @Override
    public ComboInputType[] getComboInputType() {
        return getComboInputType(ComboFileAnnotation.class);
    }

    public ComboFileFieldBean fillData(ComboFileAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
