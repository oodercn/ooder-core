package net.ooder.esd.bean;

import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.ComponentAnnotation;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

@AnnotationType(clazz = ComponentAnnotation.class)
public class CustomComponentBean implements CustomBean {

    String id;

    String expression;

    Integer index = 0;

    String caption;

    Boolean readonly;

    Boolean disabled;

    Boolean hidden;

    Boolean uid;

    Boolean pid;

    String[] enums;

    Class<? extends Enum> enumClass;

    String imageClass;

    String tips;

    Boolean captionField;


    public CustomComponentBean() {

    }

    public Boolean getCaptionField() {
        return captionField;
    }

    public void setCaptionField(Boolean captionField) {
        this.captionField = captionField;
    }

    public String[] getEnums() {
        return enums;
    }

    public void setEnums(String[] enums) {
        this.enums = enums;
    }

    public Class<? extends Enum> getEnumClass() {
        return enumClass;
    }

    public void setEnumClass(Class<? extends Enum> enumClass) {
        this.enumClass = enumClass;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }


    public Boolean getReadonly() {
        return readonly;
    }

    public void setReadonly(Boolean readonly) {
        this.readonly = readonly;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }


    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public Boolean getUid() {
        return uid;
    }

    public void setUid(Boolean uid) {
        this.uid = uid;
    }

    public Boolean getPid() {
        return pid;
    }

    public void setPid(Boolean pid) {
        this.pid = pid;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }


    public CustomComponentBean(ComponentAnnotation annotation) {
        fillData(annotation);
    }

    public CustomComponentBean fillData(ComponentAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
