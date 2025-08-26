package net.ooder.esd.dsm.repository.config;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.common.util.StringUtility;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.CustomRefBean;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.util.OODUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.HashMap;
import java.util.Map;

@AnnotationType(clazz = CustomAnnotation.class)
public class FieldEntityConfig implements CustomBean {

    String id;
    String entityClassName;
    String rootClassName;
    String fieldname;
    String methodName;
    String caption;
    String expression;
    Boolean serialize;
    Boolean uid;
    Boolean pid;
    Boolean colHidden;
    Object value;
    CustomRefBean refBean;
    Boolean split;
    Boolean captionField;
    String simpleClassName;
    String enumClass;
    ComponentType componentType;
    Map tagVar = new HashMap<>();


    public FieldEntityConfig(ESDField info,String rootClassName,String methodName) {
        this.serialize = info.isSerialize();
        this.rootClassName=rootClassName;
        this.methodName=methodName;
        this.refBean = info.getRefBean();
        if (info.getESDClass() != null) {
            this.entityClassName = info.getESDClass().getName();
        }
        if (info.getGenericType() != null) {
            this.simpleClassName = AnnotationUtil.toType(info.getGenericType()).toString();
        } else {
            this.simpleClassName = AnnotationUtil.toType(info.getReturnType()).toString();
        }
        if (info.getReturnType().isEnum()) {
            enumClass = info.getReturnType().getName();
        }
        this.componentType = info.getComponentType();
        this.fieldname = OODUtil.formatJavaName(info.getName(), false);
        this.value = info.getValue();
        this.caption = info.getCaption() == null ? info.getName() : info.getCaption();
        this.uid = info.isUid();
        this.pid = info.isPid();
        this.colHidden = info.isHidden();
        this.expression = info.getExpression();
        this.id = info.getId();
        this.split = info.isSplit();
        this.captionField = info.isCaption();

    }

    @JSONField(serialize = false)
    public ESDField getEsdField() {
        ESDField esdField = null;
        try {
            esdField = BuildFactory.getInstance().getClassManager().getAggEntityByName(entityClassName,  false).getField(fieldname);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return esdField;
    }

    public String getRootClassName() {
        return rootClassName;
    }

    public void setRootClassName(String rootClassName) {
        this.rootClassName = rootClassName;
    }

    public CustomRefBean getRefBean() {
        return refBean;
    }

    public void setRefBean(CustomRefBean refBean) {
        this.refBean = refBean;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getEntityClassName() {
        return entityClassName;
    }

    public void setEntityClassName(String entityClassName) {
        this.entityClassName = entityClassName;
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


    public Boolean getSerialize() {
        return serialize;
    }

    public void setSerialize(Boolean serialize) {
        this.serialize = serialize;
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


    public Boolean getColHidden() {
        return colHidden;
    }

    public void setColHidden(Boolean colHidden) {
        this.colHidden = colHidden;
    }


    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
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

    public String getEnumClass() {
        return enumClass;
    }

    public void setEnumClass(String enumClass) {
        this.enumClass = enumClass;
    }

    public Map getTagVar() {
        return tagVar;
    }

    public void setTagVar(Map tagVar) {
        this.tagVar = tagVar;
    }

    public ComponentType getComponentType() {
        return componentType;
    }

    public void setComponentType(ComponentType componentType) {
        this.componentType = componentType;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

}
