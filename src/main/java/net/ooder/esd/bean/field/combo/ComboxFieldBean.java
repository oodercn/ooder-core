package net.ooder.esd.bean.field.combo;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.ComboBoxAnnotation;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.bean.field.base.ListFieldBean;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.custom.component.form.field.combo.CustomComboBoxComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.ComboInputComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.form.ComboInputProperties;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CustomClass(clazz = CustomComboBoxComponent.class,
        viewType = CustomViewType.COMBOBOX,
        inputType = {ComboInputType.combobox},
        componentType = ComponentType.COMBOINPUT
)
@AnnotationType(clazz = ComboBoxAnnotation.class)
public class ComboxFieldBean<M extends ComboInputComponent> extends ComboInputFieldBean<M> {


    String listKey;

    String dropImageClass;

    Integer dropListWidth;

    Integer dropListHeight;

    ListFieldBean listBean;


    public ComboxFieldBean(M comboInputComponent) {
        super(comboInputComponent);
        update(comboInputComponent.getProperties());
    }


    protected void update(ComboInputProperties properties) {
        if (listBean == null) {
            this.listBean = new ListFieldBean(properties);
        } else {
            listBean.update(properties);
        }
    }


    @Override
    public List<JavaSrcBean> update(ModuleComponent moduleComponent, M component) {
        this.update(component.getProperties());
        return  listBean.update(moduleComponent,component);
    }



    public ComboxFieldBean() {

    }

    public ComboxFieldBean(ComboInputType inputType) {
        super(inputType);
        AnnotationUtil.fillDefaultValue(ComboBoxAnnotation.class, this);
    }

    public ComboxFieldBean(ESDField esdField, Set<Annotation> annotations, ComboInputType inputType) {
        super(esdField, annotations);
        if (inputType != null) {
            this.inputType = inputType;
        } else {
            this.inputType = ComboInputType.combobox;
        }
        AnnotationUtil.fillDefaultValue(ComboBoxAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof ComboBoxAnnotation) {
                fillData((ComboBoxAnnotation) annotation);
            }
        }
        listBean = new ListFieldBean(esdField, annotations);

    }


    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = super.getAnnotationBeans();
        if (listBean != null) {
            annotationBeans.addAll(listBean.getAnnotationBeans());
        }
        if (!annotationBeans.contains(this) && !AnnotationUtil.getAnnotationMap(this).isEmpty()) {
            annotationBeans.add(this);
        }
        return annotationBeans;
    }

    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet = new HashSet<>();
        if (listBean != null) {
            classSet.addAll(listBean.getOtherClass());
        }

        return ClassUtility.checkBase(classSet);
    }


    public String getListKey() {
        return listKey;
    }


    public void setListKey(String listKey) {
        this.listKey = listKey;
    }


    public String getDropImageClass() {
        return dropImageClass;
    }


    public void setDropImageClass(String dropImageClass) {
        this.dropImageClass = dropImageClass;
    }

    public Integer getDropListWidth() {
        return dropListWidth;
    }

    public void setDropListWidth(Integer dropListWidth) {
        this.dropListWidth = dropListWidth;
    }

    public Integer getDropListHeight() {
        return dropListHeight;
    }

    public void setDropListHeight(Integer dropListHeight) {
        this.dropListHeight = dropListHeight;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ListFieldBean getListBean() {
        return listBean;
    }

    public void setListBean(ListFieldBean listBean) {
        this.listBean = listBean;
    }

    public ComboxFieldBean(ComboBoxAnnotation annotation) {
        fillData(annotation);
    }

    @Override
    public ComboInputType getInputType() {
        return inputType;
    }

    public ComboxFieldBean fillData(ComboBoxAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }


    @Override
    public ComboInputType[] getComboInputType() {
        return getComboInputType(ComboBoxAnnotation.class);
    }


    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

}
