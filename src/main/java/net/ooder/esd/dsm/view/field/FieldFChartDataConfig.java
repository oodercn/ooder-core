package net.ooder.esd.dsm.view.field;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.bean.CustomRefBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.field.CustomFieldBean;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.HashMap;
import java.util.Map;

@AnnotationType(clazz = CustomAnnotation.class)
public class FieldFChartDataConfig implements ESDFieldConfig {

    String id;
    String viewClassName;
    String sourceClassName;
    String serviceClassName;
    String fieldname;
    String methodName;
    String sourceMethodName;
    String imageClass;
    String caption;
    String expression;
    Boolean serialize;
    Boolean uid;
    Boolean pid;
    Boolean colHidden;
    String domainId;
    Object value;
    Boolean split;
    Boolean captionField;
    CustomRefBean refBean;
    String simpleClassName;
    String enumClass;
    CustomFieldBean customBean;
    @JSONField(serialize = false)
    MethodConfig methodConfig;
    Map tagVar = new HashMap<>();

    public FieldFChartDataConfig() {

    }

    public FieldFChartDataConfig(ESDField info, String sourceClassName) {
        this.serialize = info.isSerialize();
        this.colHidden = info.isHidden();
        this.pid = info.isPid();
        this.uid = info.isUid();
        this.viewClassName = info.getESDClass().getName();
        this.sourceClassName = sourceClassName;
        this.customBean = info.getCustomBean();
        this.simpleClassName = info.getReturnType().getSimpleName();
        if (info.getFieldBean() != null) {
            if (info.getFieldBean().getServiceClass() != null) {
                this.serviceClassName = info.getFieldBean().getServiceClass().getName();
            }
        }
        if (info.getReturnType().isEnum()) {
            enumClass = info.getReturnType().getName();
        }
        this.refBean = info.getRefBean();
        this.fieldname = info.getName();
        this.value = info.getValue();
        this.caption = info.getCaption();
        this.uid = info.isUid();
        this.pid = info.isPid();
        this.serialize = info.isSerialize();
        this.colHidden = info.isHidden();
        this.expression = info.getExpression();
        this.id = info.getId();
        this.imageClass = info.getImageClass();

        this.domainId = info.getDomainId();
        this.split = info.isSplit();

        this.captionField = info.isCaption();

    }


    @Override
    public String getDomainId() {
        return domainId;
    }

    @Override
    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    @JSONField(serialize = false)
    public ESDField getEsdField() {
        ESDField esdField = null;
        try {
            esdField = BuildFactory.getInstance().getClassManager().getAggEntityByName(getViewClassName(), false).getField(fieldname);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return esdField;
    }

    @Override
    public CustomRefBean getRefBean() {
        return refBean;
    }

    public void setRefBean(CustomRefBean refBean) {
        this.refBean = refBean;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @JSONField(serialize = false)
    public MethodConfig getMethodConfig() {
        if (methodConfig == null) {
            try {
                ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(this.getViewClassName());
                if (apiClassConfig != null) {
                    methodConfig = apiClassConfig.getMethodByName(methodName == null ? fieldname : methodName);
                }
                if (methodConfig == null) {
                    AggEntityConfig aggEntityConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(this.getViewClassName(), false);
                    if (aggEntityConfig != null) {
                        methodConfig = aggEntityConfig.getMethodByName(methodName == null ? fieldname : methodName);
                    }
                }
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        return methodConfig;
    }

    @JSONField(serialize = false)
    public MethodConfig getSourceMethodConfig() {
        MethodConfig methodConfig = null;
        try {
            ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(sourceClassName);
            methodConfig = apiClassConfig.getMethodByName(sourceMethodName);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return methodConfig;
    }

    @Override
    public CustomFieldBean getCustomBean() {
        return customBean;
    }

    @Override
    public void setCustomBean(CustomFieldBean customBean) {
        this.customBean = customBean;
    }

    @Override
    public String getViewClassName() {
        if (viewClassName == null && this.getSourceMethodConfig() != null) {
            viewClassName = this.getSourceMethodConfig().getViewClass().getClassName();
        }
        return viewClassName;
    }

    public void setViewClassName(String viewClassName) {
        this.viewClassName = viewClassName;
    }

    @Override
    public String getSourceClassName() {
        return sourceClassName;
    }


    public String getServiceClassName() {
        return serviceClassName;
    }

    public void setServiceClassName(String serviceClassName) {
        this.serviceClassName = serviceClassName;
    }

    public void setSourceClassName(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }

    @Override
    public String getFieldname() {
        return fieldname;
    }

    public void setFieldname(String fieldname) {
        this.fieldname = fieldname;
    }

    @Override
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

    @Override
    public Boolean getSerialize() {
        return serialize;
    }

    public void setSerialize(Boolean serialize) {
        this.serialize = serialize;
    }

    @Override
    public Boolean getUid() {
        return uid;
    }

    public void setUid(Boolean uid) {
        this.uid = uid;
    }

    @Override
    public Boolean getPid() {
        return pid;
    }

    public void setPid(Boolean pid) {
        this.pid = pid;
    }

    @Override
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

    @Override
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

    @Override
    public Class<? extends ESDFieldConfig> getClazz() {
        return this.getClass();
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

}
