package net.ooder.esd.dsm.view.field;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.bean.CustomRefBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.field.CustomFieldBean;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.util.json.BindClassArrDeserializer;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.dsm.aggregation.FieldAggConfig;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.util.json.EnumsClassDeserializer;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.*;

@AnnotationType(clazz = CustomAnnotation.class)
public class FieldTreeConfig<T> implements ESDFieldConfig {

    String id;
    String className;
    String fieldname;
    String methodName;
    String sourceMethodName;
    String sourceClassName;
    String serviceClassName;
    String viewClassName;
    String entityClassName;
    String caption;
    String expression;
    Boolean serialize = true;
    Boolean uid;
    Boolean pid;
    Boolean colHidden;
    String domainId;
    Object value;
    Boolean split;
    String simpleClassName;

    @JSONField(deserializeUsing = EnumsClassDeserializer.class)
    Class<? extends Enum> enumClass;
    Boolean captionField;
    CustomRefBean refBean;
    CustomFieldBean customBean;
    @JSONField(deserializeUsing = BindClassArrDeserializer.class)
    public Class[] bindClass;
    @JSONField(serialize = false)
    FieldAggConfig aggConfig;


    public FieldTreeConfig() {

    }

    public FieldTreeConfig(TreeListItem listItem) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(listItem), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);

    }

    public FieldTreeConfig(ESDField info, String sourceClassName, String sourceMethodName) {
        this.className = info.getESDClass().getName();
        this.fieldname = info.getName();
        this.customBean = info.getCustomBean();
        this.viewClassName = info.getESDClass().getClassName();
        this.sourceMethodName = sourceMethodName;
        this.sourceClassName = sourceClassName;
        this.value = info.getValue();
        this.caption = info.getCaption();
        this.serialize = info.isSerialize();
        this.colHidden = info.isHidden();
        this.pid = info.isPid();
        this.refBean = info.getRefBean();
        this.simpleClassName = info.getReturnType().getSimpleName();
        if (info.getReturnType().isEnum()) {
            enumClass = info.getReturnType();
        }
        this.uid = info.isUid();
        this.expression = info.getExpression();
        this.id = info.getId();

        this.domainId = info.getDomainId();
        this.split = info.isSplit();
        this.captionField = info.isCaption();
        this.init(this.getAggConfig());


    }


    void init(FieldAggConfig aggConfig) {

        this.methodName = aggConfig.getMethodName();
        this.domainId = aggConfig.getDomainId();
        this.fieldname = aggConfig.getFieldname();
        this.id = aggConfig.getId();
        this.className = aggConfig.getEntityClassName();
        this.fieldname = aggConfig.getFieldname();
        this.value = aggConfig.getValue();
        this.caption = aggConfig.getCaption();
        this.serialize = aggConfig.getSerialize();
        this.colHidden = aggConfig.getColHidden();
        this.pid = aggConfig.getPid();
        this.refBean = aggConfig.getRefBean();
        this.simpleClassName = aggConfig.getSimpleClassName();
        this.uid = aggConfig.getUid();
        //  this.expression = aggConfig.getExpression();
        this.captionField = aggConfig.getCaptionField();
    }


    @JSONField(serialize = false)
    public MethodConfig getMethodConfig() {
        MethodConfig methodConfig = null;
        try {
            ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(this.getViewClassName());
            if (apiClassConfig != null) {
                methodConfig = apiClassConfig.getMethodByName(methodName == null ? fieldname : methodName);
            }
        } catch (JDSException e) {
            e.printStackTrace();
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


    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet = new HashSet<>();
        if (this.getEnumClass() != null) {
            classSet.add(this.getEnumClass());
        }
        return classSet;
    }

    @Override
    public CustomRefBean getRefBean() {
        return refBean;
    }

    public void setRefBean(CustomRefBean refBean) {
        this.refBean = refBean;
    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        annotationBeans = new ArrayList<>();
        if (refBean != null) {
            annotationBeans.add(refBean);
        }
        annotationBeans.add(this);
        return annotationBeans;
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
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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
        if (colHidden == null) {
            colHidden = false;
        }
        return colHidden;
    }

    public void setColHidden(Boolean colHidden) {
        this.colHidden = colHidden;
    }

    @Override
    public String getDomainId() {
        return domainId;
    }

    @Override
    public void setDomainId(String domainId) {
        this.domainId = domainId;
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

    @Override
    public Boolean getCaptionField() {
        return captionField;
    }

    public void setCaptionField(Boolean captionField) {
        this.captionField = captionField;
    }

    public void setServiceClassName(String serviceClassName) {
        this.serviceClassName = serviceClassName;
    }


    public String getSourceMethodName() {
        return sourceMethodName;
    }

    public void setSourceMethodName(String sourceMethodName) {
        this.sourceMethodName = sourceMethodName;
    }

    public String getEntityClassName() {
        return entityClassName;
    }

    public void setEntityClassName(String entityClassName) {
        this.entityClassName = entityClassName;
    }

    @Override
    public String getViewClassName() {
        if (viewClassName == null && this.getSourceMethodConfig().getViewClass() != null && this.getSourceMethodConfig() != null) {
            viewClassName = this.getSourceMethodConfig().getViewClass().getClassName();
        }
        return viewClassName;
    }


    public void setViewClassName(String viewClassName) {
        this.viewClassName = viewClassName;
    }


    public String getServiceClassName() {
        return serviceClassName;
    }


    @Override
    public String getSourceClassName() {
        return sourceClassName;
    }


    @JSONField(serialize = false)
    public FieldAggConfig getAggConfig() {
        if (aggConfig == null) {
            try {
                if (entityClassName == null) {
                    if (sourceMethodName != null && !sourceMethodName.equals("") && getMethodConfig() != null) {
                        String topSourceClsss = getMethodConfig().getEsdClass().getTopSourceClass().getClassName();
                        AggEntityConfig sourceEntityConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(topSourceClsss, false);
                        MethodConfig entityMethod = sourceEntityConfig.getMethodByName(sourceMethodName);
                        if (entityMethod != null) {
                            AggEntityConfig aggEntityConfig = entityMethod.getAggEntityConfig();
                            this.entityClassName = aggEntityConfig.getCurrClassName();
                        } else {
                            entityClassName = this.getViewClassName();
                        }
                    } else {
                        entityClassName = this.getViewClassName();
                    }

                }
                aggConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(entityClassName, false).getFieldByName(this.fieldname);
                if (aggConfig == null) {
                    aggConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(getViewClassName(), false).getFieldByName(this.fieldname);
                }
                if (aggConfig == null) {
                    aggConfig = new FieldAggConfig(this.getEsdField(), domainId);
                    DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(getViewClassName(), false).getAllFieldMap().put(fieldname, aggConfig);
                }
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        return aggConfig;
    }

    public Class[] getBindClass() {
        return bindClass;
    }

    public void setBindClass(Class[] bindClass) {
        this.bindClass = bindClass;
    }

    public void setAggConfig(FieldAggConfig aggConfig) {
        this.aggConfig = aggConfig;
    }

    public void setSourceClassName(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }

    @Override
    public Class<? extends ESDFieldConfig> getClazz() {
        return this.getClass();
    }

    public String toEnumsStr() {
        StringBuffer enumBuffer = new StringBuffer();
        if (bindClass != null && bindClass.length > 0) {
            for (Class clazz : bindClass) {
                if (clazz != null) {
                    enumBuffer.append(clazz.getSimpleName() + ".class,");
                }
            }
        }

        if (enumBuffer.toString().equals("")) {
            enumBuffer.append("null");
        }

        String enumStr = enumBuffer.toString();
        if (enumStr.endsWith(",")) {
            enumStr = enumStr.substring(0, enumStr.length() - 1);
        }

        return enumStr;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

}
