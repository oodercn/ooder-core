package net.ooder.esd.bean.field.combo;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.field.ComboInputAnnotation;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.ComboInputComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SimpleComboBean implements ComboBoxBean<ComboInputComponent> {


    ComboInputType inputType;


    public SimpleComboBean() {


    }



    public SimpleComboBean(ComboInputType inputType) {

        this.inputType = inputType;

    }


    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();

        return annotationBeans;
    }

    @Override
    public Set<Class> getOtherClass() {
        return new HashSet<>();
    }

    public ComboInputType getInputType() {
        return inputType;
    }

    public void setInputType(ComboInputType inputType) {
        this.inputType = inputType;
    }


    public SimpleComboBean(ComboInputAnnotation annotation) {
        fillData(annotation);
    }

    public SimpleComboBean fillData(ComboInputAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    @Override
    public List<JavaSrcBean> getJavaSrcBeans() {
        return new ArrayList<>();
    }



    @Override
    public ComboInputType[] getComboInputType() {
        return getComboInputType(ComboInputAnnotation.class);
    }


    //
    protected ComboInputType[] getComboInputType(Class<? extends Annotation> annotationClass) {
        ComboInputType[] comboInputTypes = new ComboInputType[]{};
        CustomClass customClass = annotationClass.getAnnotation(CustomClass.class);
        if (customClass != null) {
            comboInputTypes = customClass.inputType();
        }
        return comboInputTypes;
    }

    // @Override
    public ComponentType getComponentType() {
        return ComponentType.COMBOINPUT;
    }
}
