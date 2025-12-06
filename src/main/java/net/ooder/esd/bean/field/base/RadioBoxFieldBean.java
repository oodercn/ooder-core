package net.ooder.esd.bean.field.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.RadioBoxAnnotation;
import net.ooder.esd.annotation.ui.BorderType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.bean.field.CustomListBean;
import net.ooder.esd.bean.field.FieldBaseBean;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.custom.component.form.field.CustomRadioBoxComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.ListComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.RadioBoxComponent;
import net.ooder.esd.tool.properties.form.RadioBoxProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.*;

@CustomClass(clazz = CustomRadioBoxComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.RADIOBOX
)
@AnnotationType(clazz = RadioBoxAnnotation.class)
public class RadioBoxFieldBean extends FieldBaseBean<RadioBoxComponent> {


    String id;

    Dock dock;

    Boolean checkBox;

    BorderType borderType;

    String tagCmds;

    CustomListBean<RadioBoxProperties> listFieldBean;

    ListFieldBean<RadioBoxProperties, ListComponent> listBean;

    public RadioBoxFieldBean(ModuleComponent moduleComponent, RadioBoxComponent component) {
        update(moduleComponent, component);
    }


    @Override
    public void update(ModuleComponent parentModuleComponent, RadioBoxComponent component) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(component.getProperties()), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);

        if (listFieldBean == null) {
            listFieldBean = new CustomListBean();
        }
        listFieldBean.update(parentModuleComponent, component);
        listBean = new ListFieldBean(component.getProperties());

    }


    public RadioBoxFieldBean(ESDField esdField, Set<Annotation> annotations) {
        super(annotations);
        AnnotationUtil.fillDefaultValue(RadioBoxAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof RadioBoxAnnotation) {
                fillData((RadioBoxAnnotation) annotation);
            }
        }
        this.listFieldBean = new CustomListBean(esdField, annotations);
        this.listBean = new ListFieldBean(esdField, annotations);

    }


    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        annotationBeans.addAll(super.getAnnotationBeans());
        if (listFieldBean != null) {
            annotationBeans.addAll(listFieldBean.getAnnotationBeans());
        }
        if (listBean != null) {
            annotationBeans.addAll(listBean.getAnnotationBeans());
        }
        annotationBeans.add(this);
        return annotationBeans;
    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet = new HashSet<>();

        if (listFieldBean != null) {
            classSet.addAll(listFieldBean.getOtherClass());
        }
        if (listBean != null) {
            classSet.addAll(listBean.getOtherClass());
        }

        return ClassUtility.checkBase(classSet);
    }


    public RadioBoxFieldBean() {

    }

    public ListFieldBean getListBean() {
        return listBean;
    }

    public void setListBean(ListFieldBean listBean) {
        this.listBean = listBean;
    }

    public Dock getDock() {
        return dock;
    }

    public void setDock(Dock dock) {
        this.dock = dock;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(Boolean checkBox) {
        this.checkBox = checkBox;
    }

    public BorderType getBorderType() {
        return borderType;
    }

    public void setBorderType(BorderType borderType) {
        this.borderType = borderType;
    }

    public String getTagCmds() {
        return tagCmds;
    }

    public void setTagCmds(String tagCmds) {
        this.tagCmds = tagCmds;
    }

    public RadioBoxFieldBean(RadioBoxAnnotation annotation) {
        fillData(annotation);
    }

    public RadioBoxFieldBean fillData(RadioBoxAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public CustomListBean getListFieldBean() {
        return listFieldBean;
    }

    public void setListFieldBean(CustomListBean listFieldBean) {
        this.listFieldBean = listFieldBean;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.RADIOBOX;
    }
}
