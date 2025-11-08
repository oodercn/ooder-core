package net.ooder.esd.bean.field.combo;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.ComboListBoxAnnotation;
import net.ooder.esd.annotation.field.ListAnnotation;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.bean.field.CustomListBean;
import net.ooder.esd.bean.field.base.ListFieldBean;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.custom.component.form.field.combo.CustomComboListComponent;
import net.ooder.esd.tool.component.ComboInputComponent;
import net.ooder.esd.tool.properties.form.ComboInputProperties;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
@CustomClass(clazz = CustomComboListComponent.class,
        viewType = CustomViewType.COMBOBOX,
        inputType = {ComboInputType.listbox},
        componentType = ComponentType.COMBOINPUT
)
@AnnotationType(clazz = ComboListBoxAnnotation.class)
public class ComboListBoxFieldBean<T extends ComboInputProperties> extends ComboxFieldBean {



    public ComboListBoxFieldBean(ComboInputComponent comboInputComponent) {
        super(comboInputComponent);
        listBean = new ListFieldBean(comboInputComponent.getProperties());
    }


    public ComboListBoxFieldBean() {

    }


    @Override
    public ComboInputType getInputType() {
        return ComboInputType.listbox;
    }


    public ComboListBoxFieldBean(ComboInputType inputType) {
        super(inputType);
        AnnotationUtil.fillDefaultValue(ComboListBoxAnnotation.class, this);
        listBean = AnnotationUtil.fillDefaultValue(ListAnnotation.class, new ListFieldBean());
    }

    public ComboListBoxFieldBean(ESDField esdField, Set<Annotation> annotations, ComboInputType inputType) {
        super(esdField, annotations, inputType);
        if (inputType != null) {
            this.inputType = inputType;
        } else {
            this.inputType = ComboInputType.listbox;
        }
        listBean = new ListFieldBean(esdField, annotations);

    }


    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = super.getAnnotationBeans();
        if (listBean != null) {
            List<CustomBean> customBeans = listBean.getAnnotationBeans();
            for (CustomBean customBean : customBeans) {
                if (!(customBean instanceof CustomListBean)) {
                    annotationBeans.add(customBean);
                }

            }
        }
        return annotationBeans;
    }

    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classes = super.getOtherClass();
        if (listBean != null) {
            classes.addAll(listBean.getOtherClass());
        }
        return ClassUtility.checkBase(classes);
    }


    public ComboListBoxFieldBean(ComboListBoxAnnotation annotation) {
        fillData(annotation);
    }


    public ComboListBoxFieldBean fillData(ComboListBoxAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public ListFieldBean getListBean() {
        return listBean;
    }

    public void setListBean(ListFieldBean listBean) {
        this.listBean = listBean;
    }

    @Override
    public ComboInputType[] getComboInputType() {
        return getComboInputType(ComboListBoxAnnotation.class);
    }


    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

}
