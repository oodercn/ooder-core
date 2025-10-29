package net.ooder.esd.dsm.aggregation;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.common.util.StringUtility;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.CustomRefBean;
import net.ooder.esd.bean.field.CustomFieldBean;
import net.ooder.esd.custom.BaseFieldInfo;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.tool.properties.Properties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.*;

@AnnotationType(clazz = CustomAnnotation.class)
public class FieldAggConfig implements CustomBean, Comparable<FieldAggConfig> {

    String id;
    String entityClassName;
   // String serviceClassName;
    String fieldname;
    String methodName;
    CustomRefBean refBean;
    String imageClass;
    String domainId;
    String caption;
   // String expression;
    Boolean serialize;
    Boolean uid;
    Boolean pid;
    String target;

    Boolean hidden;
    Boolean colHidden;
    Boolean disabled;
    Boolean readonly;
    Object value;
    Boolean split;
    Boolean captionField;
    String simpleClassName;
    Class<? extends Enum>  enumClass;
    Set<String> enums;
    ComponentType componentType;
    Map tagVar = new HashMap<>();
    Integer index = 0;
    String tips;


    public FieldAggConfig() {

    }

    public FieldAggConfig(Properties properties) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }

    public FieldAggConfig(String fieldname, Class returntype, ComponentType componentType, String domainId, String entityClassName, int index) {
        AnnotationUtil.fillDefaultValue(CustomAnnotation.class, this);
        this.index = index;
        this.entityClassName = entityClassName;
        this.simpleClassName = AnnotationUtil.toType(returntype).toString();
        this.domainId = domainId;
        this.id = fieldname;
        this.fieldname = fieldname;
        if (componentType == null) {
            componentType = ComponentType.HIDDENINPUT;
        }
        this.componentType = componentType;
        if (returntype.isEnum()) {
            enumClass = returntype;
        }
        if (returntype.equals(boolean.class)) {
            methodName = StringUtility.getIsMethodName(fieldname);
        } else {
            methodName = StringUtility.getGetMethodName(fieldname);
        }
    }

    public FieldAggConfig(AggEntityConfig aggEntityConfig, String fieldname, Class returntype, ComponentType componentType, int index) {
        AnnotationUtil.fillDefaultValue(CustomAnnotation.class, this);
        this.index = index;
        this.entityClassName = aggEntityConfig.getCurrClassName();
        this.simpleClassName = AnnotationUtil.toType(returntype).toString();
        this.domainId = aggEntityConfig.getDomainId();
        this.id = fieldname;
        this.fieldname = fieldname;
        if (componentType == null) {
            componentType = ComponentType.HIDDENINPUT;
        }
        this.componentType = componentType;
        if (returntype.isEnum()) {
            enumClass = returntype;
        }
        if (returntype.equals(boolean.class)) {
            methodName = StringUtility.getIsMethodName(fieldname);
        } else {
            methodName = StringUtility.getGetMethodName(fieldname);
        }
    }

    public FieldAggConfig(ESDField info, String domainId) {
        this.serialize = info.isSerialize();
        this.pid = info.isPid();
        this.uid = info.isUid();

        this.refBean = info.getRefBean();
        this.readonly = info.isReadonly();
        this.disabled = info.isDisabled();
        this.domainId = domainId;
        this.imageClass = info.getImageClass();
        this.enums = info.getEnums();

        if (info instanceof BaseFieldInfo) {
            this.index = ((BaseFieldInfo) info).getIndex();
        }

//        if (info.getFieldBean() != null) {
//            if (info.getFieldBean().getServiceClass() != null) {
//                this.serviceClassName = info.getFieldBean().getServiceClass().getName();
//            }
//        }

        if (info.getESDClass() != null) {
            this.entityClassName = info.getESDClass().getCtClass().getName();
        }

        if (info.getGenericType() != null) {
            this.simpleClassName = AnnotationUtil.toType(info.getGenericType()).toString();
        } else {
            this.simpleClassName = AnnotationUtil.toType(info.getReturnType()).toString();
        }
        if (info.getReturnType().isEnum()) {
            enumClass = info.getReturnType();
        }
        if (info.getReturnType().equals(boolean.class)) {
            methodName = StringUtility.getIsMethodName(info.getName());
        } else {
            methodName = StringUtility.getGetMethodName(info.getName());
        }
        this.componentType = info.getComponentType();
        this.fieldname = info.getFieldName();
        this.value = info.getValue();
        this.caption = info.getCaption();
        this.colHidden = info.isHidden();
   //     this.expression = info.getExpression();
        this.id = info.getId();
        this.domainId = info.getDomainId();
        this.split = info.isSplit();
        this.captionField = info.isCaption();


    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }


    public Boolean getReadonly() {
        return readonly;
    }

    public void setReadonly(Boolean readonly) {
        this.readonly = readonly;
    }


    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }


    @JSONField(serialize = false)
    public ESDField getEsdField() {
        ESDField esdField = null;
        try {
            if (entityClassName != null) {
                ESDClass esdClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(entityClassName,  false);
                if (esdClass != null) {
                    esdField = esdClass.getField(fieldname);
                }

            }
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return esdField;
    }

    public Set<String> getEnums() {
        return enums;
    }

    public void setEnums(Set<String> enums) {
        this.enums = enums;
    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        annotationBeans = new ArrayList<>();
        if (refBean != null && !AnnotationUtil.getAnnotationMap(refBean).isEmpty()) {
            annotationBeans.add(refBean);
        }
        annotationBeans.add(this);
        return annotationBeans;
    }


    public ComponentType getComponentType() {
        return componentType;
    }

    public void setComponentType(ComponentType componentType) {
        this.componentType = componentType;
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

    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }

    public CustomRefBean getRefBean() {
        return refBean;
    }

    public void setRefBean(CustomRefBean refBean) {
        this.refBean = refBean;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
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
        if (colHidden == null) {
            if (componentType != null && componentType.equals(ComponentType.HIDDENINPUT)) {
                colHidden = true;
            } else {
                colHidden = false;
            }
        }
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
        if (captionField == null && this.getEsdField() != null) {
            captionField = this.getEsdField().isCaption();
        }
        return captionField;
    }

    public void setCaptionField(Boolean captionField) {
        this.captionField = captionField;
    }

    public String getSimpleClassName() {
        if (simpleClassName == null) {
            ESDField esdField = this.getEsdField();
            if (esdField != null) {
                if (esdField.getGenericType() != null) {
                    this.simpleClassName = AnnotationUtil.toType(esdField.getGenericType()).toString();
                } else {
                    this.simpleClassName = AnnotationUtil.toType(esdField.getReturnType()).toString();
                }
            }

        }

        return simpleClassName;
    }

    public void setSimpleClassName(String simpleClassName) {
        this.simpleClassName = simpleClassName;
    }

    public Class<? extends Enum>  getEnumClass() {
        return enumClass;
    }

    public void setEnumClass(Class<? extends Enum>  enumClass) {
        this.enumClass = enumClass;
    }


    public Map getTagVar() {
        return tagVar;
    }

    public void setTagVar(Map tagVar) {
        this.tagVar = tagVar;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    @Override
    public String toAnnotationStr() {
        CustomFieldBean fieldBean = new CustomFieldBean();
        fieldBean.setHidden(this.getColHidden());
        Map valueMap = JSON.parseObject(JSON.toJSONString(this), Map.class);
        OgnlUtil.setProperties(valueMap, fieldBean, false, false);
        return AnnotationUtil.toAnnotationStr(fieldBean);
    }

    @Override
    public int compareTo(FieldAggConfig o) {
        if (index == null) {
            return -1;
        }

        if (index != null && o.getIndex() != null) {
            return this.index - o.getIndex();
        }

        return 1;
    }


}
