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
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.dsm.aggregation.FieldAggConfig;
import net.ooder.esd.util.json.EnumsClassDeserializer;
import net.ooder.esd.tool.properties.item.GalleryItem;
import net.ooder.esd.tool.properties.item.TitleBlockItem;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AnnotationType(clazz = CustomAnnotation.class)
public class FieldGalleryConfig implements ESDFieldConfig {


    public String comment;
    public String renderer;
    public String imagePos;
    public String imageBgSize;
    public String imageRepeat;
    public String iconFontSize;
    public String iconFontCode;
    public String iconStyle;
    public String flagText;
    public String flagClass;
    public String flagStyle;
    CustomFieldBean customBean;
    public String valueSeparator;
    public String bindClassName;


    String id;
    String className;
    String fieldname;
    String imageClass;
    String methodName;
    String caption;
    String expression;
    Boolean serialize;
    Boolean uid;
    Boolean pid;
    Boolean colHidden;
    String domainId;
    Object value;
    Boolean split;
    CustomRefBean refBean;
    Boolean captionField;
    String simpleClassName;
    @JSONField(deserializeUsing = EnumsClassDeserializer.class)
    Class<? extends Enum> enumClass;
    String serviceClassName;
    String sourceClassName;
    String sourceMethodName;
    String viewClassName;
    String entityClassName;

    public String msgnum;
    public String more;
    public String title;


    @JSONField(serialize = false)
    FieldAggConfig aggConfig;


    public FieldGalleryConfig(TitleBlockItem item) {
        simpleClassName = String.class.getSimpleName();
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(item), Map.class), this, false, false);
    }

    public FieldGalleryConfig(GalleryItem item) {
        simpleClassName = String.class.getSimpleName();
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(item), Map.class), this, false, false);
    }

    public FieldGalleryConfig() {


    }


    public FieldGalleryConfig(ESDField info, String sourceClassName, String sourceMethodName) {
        this.className = info.getESDClass().getName();
        this.viewClassName = info.getESDClass().getClassName();
        this.methodName = info.getFieldName();
        this.fieldname = info.getName();
        this.customBean = info.getCustomBean();
        this.sourceMethodName = sourceMethodName;
        this.value = info.getValue();
        this.caption = info.getCaption();
        this.serialize = info.isSerialize();
        this.colHidden = info.isHidden();
        this.pid = info.isPid();
        this.uid = info.isUid();
        this.refBean = info.getRefBean();
        if (info.getFieldBean() != null) {
            if (info.getFieldBean().getServiceClass() != null) {
                this.serviceClassName = info.getFieldBean().getServiceClass().getName();
            }
        }

        this.simpleClassName = info.getReturnType().getSimpleName();
        if (info.getReturnType().isEnum()) {
            enumClass = info.getReturnType();
        }
        this.sourceClassName = sourceClassName;
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
        this.serialize = aggConfig.getSerialize();
        this.colHidden = aggConfig.getColHidden();
        this.pid = aggConfig.getPid();
        this.uid = aggConfig.getUid();
        this.refBean = aggConfig.getRefBean();
        this.fieldname = aggConfig.getId();
        this.captionField = aggConfig.getCaptionField();
        this.id = aggConfig.getId();
        // this.serviceClassName = aggConfig.getServiceClassName();

        if (this.simpleClassName == null || this.simpleClassName.equals("")) {
            simpleClassName = aggConfig.getSimpleClassName();
        }
        this.caption = aggConfig.getCaption();
        this.domainId = aggConfig.getDomainId();
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
                aggConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(entityClassName, true).getFieldByName(this.fieldname);
                if (aggConfig == null) {
                    aggConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(getViewClassName(), true).getFieldByName(this.fieldname);
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


    @JSONField(serialize = false)
    public MethodConfig getMethodConfig() {
        MethodConfig methodConfig = null;
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
    @JSONField(serialize = false)
    public String getViewClassName() {
        if (viewClassName == null && this.getSourceMethodConfig() != null) {
            viewClassName = this.getSourceMethodConfig().getViewClass().getClassName();
        }
        return viewClassName;
    }


    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        annotationBeans.add(this);
        return annotationBeans;
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

    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }

    public String getMsgnum() {
        return msgnum;
    }

    public void setMsgnum(String msgnum) {
        this.msgnum = msgnum;
    }

    public String getMore() {
        return more;
    }

    public void setMore(String more) {
        this.more = more;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRenderer() {
        return renderer;
    }

    public void setRenderer(String renderer) {
        this.renderer = renderer;
    }

    public String getImagePos() {
        return imagePos;
    }

    public void setImagePos(String imagePos) {
        this.imagePos = imagePos;
    }

    public String getImageBgSize() {
        return imageBgSize;
    }

    public void setImageBgSize(String imageBgSize) {
        this.imageBgSize = imageBgSize;
    }

    public String getImageRepeat() {
        return imageRepeat;
    }

    public void setImageRepeat(String imageRepeat) {
        this.imageRepeat = imageRepeat;
    }

    public String getIconFontSize() {
        return iconFontSize;
    }

    public void setIconFontSize(String iconFontSize) {
        this.iconFontSize = iconFontSize;
    }

    public String getIconFontCode() {
        return iconFontCode;
    }

    public void setIconFontCode(String iconFontCode) {
        this.iconFontCode = iconFontCode;
    }

    public String getIconStyle() {
        return iconStyle;
    }

    public void setIconStyle(String iconStyle) {
        this.iconStyle = iconStyle;
    }

    public String getFlagText() {
        return flagText;
    }

    public void setFlagText(String flagText) {
        this.flagText = flagText;
    }

    public String getFlagClass() {
        return flagClass;
    }

    public void setFlagClass(String flagClass) {
        this.flagClass = flagClass;
    }

    public String getFlagStyle() {
        return flagStyle;
    }

    public void setFlagStyle(String flagStyle) {
        this.flagStyle = flagStyle;
    }

    public String getValueSeparator() {
        return valueSeparator;
    }

    public void setValueSeparator(String valueSeparator) {
        this.valueSeparator = valueSeparator;
    }

    public String getBindClassName() {
        return bindClassName;
    }

    public void setBindClassName(String bindClassName) {
        this.bindClassName = bindClassName;
    }

    public String getEntityClassName() {
        return entityClassName;
    }

    public void setEntityClassName(String entityClassName) {
        this.entityClassName = entityClassName;
    }

    public void setAggConfig(FieldAggConfig aggConfig) {
        this.aggConfig = aggConfig;
    }

    public String getSourceMethodName() {
        return sourceMethodName;
    }

    public void setSourceMethodName(String sourceMethodName) {
        this.sourceMethodName = sourceMethodName;
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


    public String getServiceClassName() {
        return serviceClassName;
    }

    public void setServiceClassName(String serviceClassName) {
        this.serviceClassName = serviceClassName;
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

    public Class<? extends Enum> getEnumClass() {
        return enumClass;
    }

    public void setEnumClass(Class<? extends Enum> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public String getSourceClassName() {
        return sourceClassName;
    }


    public void setSourceClassName(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }


    public void setViewClassName(String viewClassName) {
        this.viewClassName = viewClassName;
    }

    @Override
    public Class<? extends ESDFieldConfig> getClazz() {
        return this.getClass();
    }

    @Override
    public String toAnnotationStr() {
        CustomFieldBean fieldBean = new CustomFieldBean(fieldname);
        fieldBean.setHidden(this.getColHidden());
        Map valueMap = JSON.parseObject(JSON.toJSONString(this), Map.class);
        OgnlUtil.setProperties(valueMap, fieldBean, false, false);
        return AnnotationUtil.toAnnotationStr(fieldBean);
    }


}
