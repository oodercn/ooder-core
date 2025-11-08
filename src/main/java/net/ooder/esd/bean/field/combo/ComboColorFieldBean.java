package net.ooder.esd.bean.field.combo;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.ColorPickerAnnotation;
import net.ooder.esd.annotation.field.ComboColorAnnotation;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.ShowModeType;
import net.ooder.esd.bean.field.base.ColorPickerFieldBean;
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
        inputType = {ComboInputType.color},
        componentType = ComponentType.COMBOINPUT
)
@AnnotationType(clazz = ComboColorAnnotation.class)
public class ComboColorFieldBean extends ComboInputFieldBean {

    ShowModeType showMode;

    ColorPickerFieldBean colorPickBean;

    public ComboColorFieldBean(){

    }

    public ComboColorFieldBean(ComboInputComponent comboInputComponent) {
         super(comboInputComponent);
         update(comboInputComponent.getProperties());
    }

    protected  void update (ComboInputProperties properties) {
        this.colorPickBean = new ColorPickerFieldBean(properties);
    }


    public ComboColorFieldBean(ComboInputType inputType) {
        super(inputType);
        AnnotationUtil.fillDefaultValue(ComboColorAnnotation.class, this);
        colorPickBean = AnnotationUtil.fillDefaultValue(ColorPickerAnnotation.class, new ColorPickerFieldBean());
    }

    public ComboColorFieldBean(ESDField esdField, Set<Annotation> annotations, ComboInputType inputType) {
        super(esdField, annotations);
        if (inputType != null) {
            this.inputType = inputType;
        }
        AnnotationUtil.fillDefaultValue(ComboColorAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof ComboColorAnnotation) {
                fillData((ComboColorAnnotation) annotation);
            }
        }

        colorPickBean = AnnotationUtil.fillDefaultValue(ColorPickerAnnotation.class, new ColorPickerFieldBean());
        for (Annotation annotation : annotations) {
            if (annotation instanceof ColorPickerAnnotation) {
                colorPickBean = new ColorPickerFieldBean((ColorPickerAnnotation) annotation);
            }
        }

    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = super.getAnnotationBeans();
        if (colorPickBean != null && !AnnotationUtil.getAnnotationMap(colorPickBean).isEmpty()) {
            annotationBeans.add(colorPickBean);
        }
        if (!annotationBeans.contains(this)) {
            annotationBeans.add(this);
        }
        return annotationBeans;
    }

    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet=new HashSet<>();
        if (colorPickBean!=null){
            classSet.addAll(colorPickBean.getOtherClass());
        }

        return ClassUtility.checkBase(classSet);
    }


    public ShowModeType getShowMode() {
        return showMode;
    }


    public void setShowMode(ShowModeType showMode) {
        this.showMode = showMode;
    }

    public ColorPickerFieldBean getColorPickBean() {
        return colorPickBean;
    }

    public void setColorPickBean(ColorPickerFieldBean colorPickBean) {
        this.colorPickBean = colorPickBean;
    }

    public ComboColorFieldBean(ComboColorAnnotation annotation) {
        fillData(annotation);
    }

    @Override
    public ComboInputType[] getComboInputType() {
        return getComboInputType(ComboColorAnnotation.class);
    }

    public ComboColorFieldBean fillData(ComboColorAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
