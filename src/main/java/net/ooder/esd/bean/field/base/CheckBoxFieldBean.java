package net.ooder.esd.bean.field.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.field.CheckBoxAnnotation;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.field.FieldBaseBean;
import net.ooder.esd.custom.component.form.field.CustomCheckBoxComponent;
import net.ooder.esd.tool.component.CheckBoxComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.form.CheckBoxProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.*;

@CustomClass(clazz = CustomCheckBoxComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.CHECKBOX
)
@AnnotationType(clazz = CheckBoxAnnotation.class)
public class CheckBoxFieldBean extends FieldBaseBean<CheckBoxComponent> {

    String id;

    HAlignType hAlign;

    VAlignType vAlign;

    ImagePos iconPos;

    String image;

    ImagePos imagePos;

    String imageBgSize;

    String imageClass;


    String iconFontCode;

    String caption;

    public CheckBoxFieldBean(ModuleComponent moduleComponent, CheckBoxComponent component) {
        super(moduleComponent, component);
    }


    public void updateProperties(CheckBoxProperties checkBoxProperties) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(checkBoxProperties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }

    @Override
    public void update(ModuleComponent moduleComponent, CheckBoxComponent component) {
        this.updateProperties(component.getProperties());
        super.update(moduleComponent, component);

    }

    public CheckBoxFieldBean(Set<Annotation> annotations) {
        super(annotations);
        AnnotationUtil.fillDefaultValue(CheckBoxAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof CheckBoxAnnotation) {
                fillData((CheckBoxAnnotation) annotation);
            }
        }


    }


    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        annotationBeans.addAll(super.getAnnotationBeans());
        annotationBeans.add(this);
        return annotationBeans;
    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet = new HashSet<>();

        return classSet;
    }


    public CheckBoxFieldBean() {

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HAlignType gethAlign() {
        return hAlign;
    }

    public void sethAlign(HAlignType hAlign) {
        this.hAlign = hAlign;
    }

    public VAlignType getvAlign() {
        return vAlign;
    }

    public void setvAlign(VAlignType vAlign) {
        this.vAlign = vAlign;
    }

    public ImagePos getIconPos() {
        return iconPos;
    }

    public void setIconPos(ImagePos iconPos) {
        this.iconPos = iconPos;
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

    public String getImageBgSize() {
        return imageBgSize;
    }

    public void setImageBgSize(String imageBgSize) {
        this.imageBgSize = imageBgSize;
    }

    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }

    public String getIconFontCode() {
        return iconFontCode;
    }

    public void setIconFontCode(String iconFontCode) {
        this.iconFontCode = iconFontCode;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public CheckBoxFieldBean(CheckBoxAnnotation annotation) {
        fillData(annotation);
    }

    public CheckBoxFieldBean fillData(CheckBoxAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.CHECKBOX;
    }

}
