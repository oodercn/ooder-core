package net.ooder.esd.bean.field.combo;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.ComboImageAnnotation;
import net.ooder.esd.annotation.field.ImageAnnotation;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.ImagePos;
import net.ooder.esd.bean.field.ImageFieldBean;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.custom.component.form.field.combo.CustomComboInputComponent;
import net.ooder.esd.tool.properties.form.ComboInputProperties;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@CustomClass(clazz = CustomComboInputComponent.class,
        viewType = CustomViewType.COMBOBOX,
        inputType = {ComboInputType.image},
        componentType = ComponentType.COMBOINPUT
)
@AnnotationType(clazz = ComboImageAnnotation.class)
public class ComboImageFieldBean extends ComboInputFieldBean<CustomComboInputComponent> {

    String image;


    ImagePos imagePos;

    ImageFieldBean imageFieldBean;

    public ComboImageFieldBean(CustomComboInputComponent imageComponent){
        super(imageComponent);
        this.update(imageComponent.getProperties());

    }
    protected void update(ComboInputProperties properties) {

        imageFieldBean = new ImageFieldBean(properties);
        this.image=properties.getImage();
        this.imagePos=properties.getImagePos();


    }


    public ComboImageFieldBean(){

    }

    public ComboImageFieldBean(ComboInputType inputType) {
        super(inputType);
        AnnotationUtil.fillDefaultValue(ComboImageAnnotation.class, this);
        imageFieldBean = AnnotationUtil.fillDefaultValue(ImageAnnotation.class, new ImageFieldBean());
    }

    public ComboImageFieldBean(ESDField esdField, Set<Annotation> annotations, ComboInputType inputType) {
        super(esdField, annotations);
        if (inputType != null) {
            this.inputType = inputType;
        }
        AnnotationUtil.fillDefaultValue(ComboImageAnnotation.class, this);
        imageFieldBean = AnnotationUtil.fillDefaultValue(ImageAnnotation.class, new ImageFieldBean());

        for (Annotation annotation : annotations) {
            if (annotation instanceof ComboImageAnnotation) {
                fillData((ComboImageAnnotation) annotation);
            }
        }

        for (Annotation annotation : annotations) {
            if (annotation instanceof ImageAnnotation) {
                imageFieldBean = new ImageFieldBean((ImageAnnotation) annotation);
            }
        }

    }

    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet=new HashSet<>();
        if (imageFieldBean!=null){
            classSet.addAll(imageFieldBean.getOtherClass());
        }

        return classSet;
    }
    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans =super.getAnnotationBeans();
        if (imageFieldBean!=null && !AnnotationUtil.getAnnotationMap(imageFieldBean).isEmpty()){
            annotationBeans.add(imageFieldBean);
        }
        if (!annotationBeans.contains(this)) {
            annotationBeans.add(this);
        }
        return annotationBeans;
    }

    public String getImage() {
        return image;
    }


    public void setImage(String image) {
        this.image = image;
    }


    public ImagePos getImagePos() {
        return imagePos;
    }


    public void setImagePos(ImagePos imagePos) {
        this.imagePos = imagePos;
    }

    public ImageFieldBean getImageFieldBean() {
        return imageFieldBean;
    }

    public void setImageFieldBean(ImageFieldBean imageFieldBean) {
        this.imageFieldBean = imageFieldBean;
    }

    public ComboImageFieldBean(ComboImageAnnotation annotation) {
        fillData(annotation);
    }

    @Override
    public ComboInputType[] getComboInputType() {
        return getComboInputType(ComboImageAnnotation.class);    }

    public ComboImageFieldBean fillData(ComboImageAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
