package net.ooder.esd.dsm.view.field;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.esd.annotation.ComponentAnnotation;
import net.ooder.esd.bean.CustomLayoutItemBean;
import net.ooder.esd.bean.CustomRefBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.field.CustomFieldBean;
import net.ooder.esd.bean.field.combo.CustomModuleRefFieldBean;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.engine.ESDClient;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.engine.EUModule;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@AnnotationType(clazz = ComponentAnnotation.class)
public class FieldComponentConfig implements ESDFieldConfig, Comparable<FieldComponentConfig> {


    String id;
    String viewClassName;
    Set<String> serviceClassNames;
    String sourceClassName;
    String euClassName;
    String fieldname;
    String methodName;
    String imageClass;
    String caption;
    String expression;
    Integer index = 0;
    Integer size;
    Boolean captionField;
    Boolean serialize;
    Boolean uid;
    Boolean pid;
    Boolean colHidden;
    String domainId;
    String projectVersionName;
    String url;
    CustomFieldBean customBean;
    CustomRefBean refBean;
    @JSONField(serialize = false)
    MethodConfig methodConfig;


    CustomLayoutItemBean layoutItemBean;

    Map tagVar = new HashMap<>();

    public FieldComponentConfig() {

    }

    public FieldComponentConfig(MethodConfig methodConfig) {

        init(methodConfig);
    }

    public FieldComponentConfig(FieldFormConfig esdField) {
        MethodConfig fieldMethodConfig = null;
        try {
            ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(esdField.getViewClassName());
            MethodConfig methodConfig = apiClassConfig.getMethodByName(esdField.getFieldname());
            if (esdField != null && esdField.getWidgetConfig() instanceof CustomModuleRefFieldBean) {
                CustomModuleRefFieldBean fieldBean = (CustomModuleRefFieldBean) esdField.getWidgetConfig();
                if (fieldBean.getSrc() != null && !fieldBean.getSrc().equals("")) {
                    fieldMethodConfig = apiClassConfig.getMethodByName(fieldBean.getSrc());
                } else if (fieldBean.getBindClass() != null && !fieldBean.getBindClass().equals(Void.class) && !fieldBean.getBindClass().equals(Enum.class)) {
                    ApiClassConfig bindConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(fieldBean.getBindClass().getName());
                    fieldMethodConfig = bindConfig.findEditorMethod();
                } else {
                    ApiClassConfig bindConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(esdField.getViewClassName());
                    fieldMethodConfig = bindConfig.getMethodByName(esdField.getFieldname());
                }
            } else {
                fieldMethodConfig = methodConfig;
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }

        init(fieldMethodConfig);
    }

    private void init(MethodConfig methodConfig) {
        this.methodConfig = methodConfig;
        this.uid = false;
        this.pid = false;
        this.serialize = true;
        this.colHidden = false;
        this.customBean = methodConfig.getFieldBean();
        this.layoutItemBean = methodConfig.getLayoutItem();
        this.serviceClassNames = methodConfig.getServiceClassNames();
        this.sourceClassName = methodConfig.getSourceClassName();
        if (methodConfig.getViewClass() != null) {
            this.viewClassName = methodConfig.getViewClass().getName();
        } else {
            this.viewClassName = methodConfig.getViewClassName();
        }


        this.fieldname = methodConfig.getName();
        this.methodName = methodConfig.getMethodName();
        this.caption = methodConfig.getCaption();
        this.index = methodConfig.getIndex() == null ? 0 : methodConfig.getIndex();
        this.expression = methodConfig.getExpression();
        this.id = methodConfig.getSourceClass().getName() + "_" + methodConfig.getId();
        this.imageClass = methodConfig.getImageClass();
        this.domainId = methodConfig.getDomainId();
        this.url = methodConfig.getUrl();
        this.tagVar = methodConfig.getTagVar();
        this.refBean = methodConfig.getRefBean();
        this.captionField = false;
    }

    public EUModule getModule(Map valueMap) throws JDSException {
        ESDClient client = ESDFacrory.getAdminESDClient();
        String projectName = DSMFactory.getInstance().getDefaultProjectName();
        EUModule module = null;
        if (euClassName != null && !euClassName.equals("")) {
            module = client.getModule(euClassName, projectName);
            module.getComponent().setAlias(this.id + "Panel");
        } else {
            module = client.getCustomModule(this.getUrl(), projectName, valueMap);
        }
        if (module != null) {
            module.getComponent().setTarget(this.getId());
        }
        return module;

    }

    @JSONField(serialize = false)
    public MethodConfig getMethodConfig() {
        if (methodConfig == null) {
            try {
                ApiClassConfig esdClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(this.getViewClassName());
                if (esdClassConfig != null) {
                    methodConfig = esdClassConfig.getMethodByName(methodName == null ? fieldname : methodName);
                }
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        return methodConfig;
    }


    @Override
    public String getSourceClassName() {
        return sourceClassName;
    }

    public void setSourceClassName(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }

    public void setColHidden(Boolean colHidden) {
        this.colHidden = colHidden;
    }

    public String getEuClassName() {
        return euClassName;
    }

    public void setEuClassName(String euClassName) {
        this.euClassName = euClassName;
    }

    @JSONField(serialize = false)
    public ESDField getEsdField() {
        ESDField esdField = null;
        try {
            esdField = BuildFactory.getInstance().getClassManager().getAggEntityByName(this.getViewClassName(), false).getField(fieldname);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return esdField;
    }

    @Override
    public Boolean getCaptionField() {
        return captionField;
    }

    public void setCaptionFiled(Boolean captionField) {
        this.captionField = captionField;
    }

    public Set<String> getServiceClassNames() {
        return serviceClassNames;
    }

    public void setServiceClassNames(Set<String> serviceClassNames) {
        this.serviceClassNames = serviceClassNames;
    }

    public CustomLayoutItemBean getLayoutItemBean() {
        return layoutItemBean;
    }

    public void setLayoutItemBean(CustomLayoutItemBean layoutItemBean) {
        this.layoutItemBean = layoutItemBean;
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

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
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

    public void setCaptionField(Boolean captionField) {
        this.captionField = captionField;
    }

    @Override
    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getProjectVersionName() {
        return projectVersionName;
    }

    public void setProjectVersionName(String projectVersionName) {
        this.projectVersionName = projectVersionName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map getTagVar() {
        return tagVar;
    }

    public void setTagVar(Map tagVar) {
        this.tagVar = tagVar;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

//
//    @Override
//    public String getServiceClassName() {
//        if (serviceClassNames.size() > 0) {
//            return serviceClassNames.iterator().toString();
//        }
//        return null;
//    }

    @JSONField(serialize = false)
    public MethodConfig getSourceMethodConfig() {
        MethodConfig methodConfig = null;
        try {
            ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(sourceClassName);
            methodConfig = apiClassConfig.getMethodByName(methodName);
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

    public void setMethodConfig(MethodConfig methodConfig) {
        this.methodConfig = methodConfig;
    }

    @Override
    public Class<? extends ESDFieldConfig> getClazz() {
        return this.getClass();
    }

    @Override
    public int compareTo(FieldComponentConfig o) {
        if (index == null) {
            return -1;
        }

        if (index != null && o.getIndex() != null) {
            return this.index - o.getIndex();
        }

        return 1;
    }

}
