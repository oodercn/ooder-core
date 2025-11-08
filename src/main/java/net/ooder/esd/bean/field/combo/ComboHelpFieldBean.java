package net.ooder.esd.bean.field.combo;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.ComboHelpAnnotation;
import net.ooder.esd.annotation.field.ListAnnotation;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.bean.field.CustomListBean;
import net.ooder.esd.bean.field.base.ListFieldBean;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.custom.component.form.field.CustomHelpComponent;
import net.ooder.annotation.AnnotationType;
import net.ooder.esd.custom.component.form.field.combo.CustomComboListComponent;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
@CustomClass(clazz = CustomComboListComponent.class,
        viewType = CustomViewType.COMBOBOX,
        inputType = {ComboInputType.helpinput},
        componentType = ComponentType.COMBOINPUT
)
@AnnotationType(clazz = ComboHelpAnnotation.class)
public class ComboHelpFieldBean extends ComboxFieldBean<CustomHelpComponent> {

    public ComboHelpFieldBean(CustomHelpComponent comboInputComponent) {
        super(comboInputComponent);
        listBean = new ListFieldBean(comboInputComponent.getProperties());
    }



    public ComboHelpFieldBean() {

    }

    public ComboHelpFieldBean(ESDField esdField) {

    }

    public ComboHelpFieldBean(ComboInputType inputType) {
        super(inputType);
        AnnotationUtil.fillDefaultValue(ComboHelpAnnotation.class, this);
        listBean = AnnotationUtil.fillDefaultValue(ListAnnotation.class, new ListFieldBean());
    }

    public ComboHelpFieldBean(ESDField esdField, Set<Annotation> annotations, ComboInputType inputType) {
        super(esdField, annotations, inputType);
        if (inputType != null) {
            this.inputType = inputType;
        } else {
            this.inputType = ComboInputType.listbox;
        }
        listBean = new ListFieldBean(esdField, annotations);

    }

    @Override
    public ComboInputType getInputType() {
        return ComboInputType.helpinput;
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


    public ComboHelpFieldBean(ComboHelpAnnotation annotation) {
        fillData(annotation);
    }


    public ComboHelpFieldBean fillData(ComboHelpAnnotation annotation) {
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
        return getComboInputType(ComboHelpAnnotation.class);
    }


    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

}
