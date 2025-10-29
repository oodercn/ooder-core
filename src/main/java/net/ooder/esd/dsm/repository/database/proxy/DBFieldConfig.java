package net.ooder.esd.dsm.repository.database.proxy;

import net.ooder.common.util.StringUtility;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.Set;

@AnnotationType(clazz = CustomAnnotation.class)
public class DBFieldConfig implements CustomBean {

    String id;
    String fieldname;
    String methodName;
    String caption;
    String expression;
    Boolean uid;
    Boolean pid;
    Boolean hidden;
    String projectVersionName;
    Integer index;

    Boolean split;
    Boolean captionField;
    String simpleClassName;
    Set<String> enums;
    Class<? extends Enum> enumClass;


    public DBFieldConfig(ESDField info) {

        if (info.getGenericType() != null) {
            this.simpleClassName = AnnotationUtil.toType(info.getGenericType()).toString();
        } else {
            this.simpleClassName = AnnotationUtil.toType(info.getReturnType()).toString();
        }
        this.enums = info.getEnums();
        this.enumClass = info.getEnumClass();
        if (info.getReturnType().isEnum()) {
            enumClass = info.getReturnType();
        }
        this.index = info.getCustomBean().getIndex();
        this.fieldname = StringUtility.formatJavaName(info.getName(), false);
        this.caption = info.getCaption();
        this.uid = info.isUid();
        this.pid = info.isPid();
        this.hidden = info.isHidden();
        this.expression = info.getExpression();
        this.id = info.getId();
        this.projectVersionName = info.getProjectVersionName();
        this.split = info.isSplit();
        this.captionField = info.isCaption();

    }


    public DBFieldConfig(FieldFormConfig info) {
        this.fieldname = info.getFieldname();
        this.uid = info.getUid();
        this.pid = info.getPid();
        this.id = info.getId();
        this.captionField = info.getCaptionField();
        if (info.getLabelBean() != null) {
            this.caption = info.getLabelBean().getLabelCaption();
        }

        if (info.getCustomBean() != null) {
            this.enumClass = info.getCustomBean().getEnumClass();
            this.enums = info.getCustomBean().getEnums();
            this.index = info.getCustomBean().getIndex();
        }
        if (info.getAggConfig() != null) {
            this.simpleClassName = info.getAggConfig().getSimpleClassName();
        }

    }


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getFieldname() {
        return fieldname;
    }

    public void setFieldname(String fieldname) {
        this.fieldname = fieldname;
    }


    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
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


    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public String getProjectVersionName() {
        return projectVersionName;
    }

    public void setProjectVersionName(String projectVersionName) {
        this.projectVersionName = projectVersionName;
    }


    public Boolean getSplit() {
        return split;
    }

    public void setSplit(Boolean split) {
        this.split = split;
    }

    public Boolean getCaptionField() {
        return captionField;
    }

    public void setCaptionField(Boolean captionField) {
        this.captionField = captionField;
    }

    public String getSimpleClassName() {
        return simpleClassName;
    }

    public void setSimpleClassName(String simpleClassName) {
        this.simpleClassName = simpleClassName;
    }

    public Class<? extends Enum> getEnumClass() {
        return enumClass;
    }

    public void setEnumClass(Class<? extends Enum> enumClass) {
        this.enumClass = enumClass;
    }

    public Set<String> getEnums() {
        return enums;
    }

    public void setEnums(Set<String> enums) {
        this.enums = enums;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }


    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
