package net.ooder.esd.dsm.repository.config;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.RepositoryBean;
import net.ooder.esd.custom.CustomMethodInfo;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;
import net.ooder.web.EntityBean;

import java.util.*;

public class EntityConfig {


    String rootClassName;

    String methodName;

    String projectVersionName;

    Set<String> moduleMethodNames = new HashSet<>();

    Set<String> fieldNames = new HashSet<>();

    Set<String> allMethodNames = new HashSet<>();

    Set<String> otherMethodNames = new HashSet<>();

    Set<String> javaTempIds = new LinkedHashSet<>();

    Map<String, FieldEntityConfig> allFieldMap = new LinkedHashMap<>();

    Map<String, MethodConfig> allMethodMap = new LinkedHashMap<>();

    RepositoryBean repositoryBean;

    EntityBean entityBean;


    public EntityConfig() {

    }
    public EntityConfig(ESDClass esdClass) {
        initClass(esdClass);
        this.rootClassName=esdClass.getTopSourceClass().getClassName();

    }


    public EntityConfig(ESDClass esdClass, String rootClassName, String methodName) {
        this.rootClassName=rootClassName;
        this.methodName=methodName;
        initClass(esdClass);
    }


    @JSONField(serialize = false)
    public ESDClass getRootClass() {
        ESDClass esdClass = null;
        if (rootClassName != null) {
            try {
                esdClass = BuildFactory.getInstance().getClassManager().getRepositoryClass(rootClassName, true);
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        return esdClass;
    }

    void initClass(ESDClass esdClass) {
        List<CustomMethodInfo> esdMethods = new ArrayList<>();
        esdMethods.addAll(esdClass.getMethodsList());
        Collections.sort(esdMethods);


        repositoryBean = new RepositoryBean();
        entityBean = new EntityBean();

        for (ESDField esdField : esdClass.getFieldList()) {
            allFieldMap.put(esdField.getName(), new FieldEntityConfig(esdField, rootClassName, methodName));
            fieldNames.add(esdField.getName());
        }

        for (ESDField esdField : esdClass.getDisableFieldList()) {
            allFieldMap.put(esdField.getName(), new FieldEntityConfig(esdField, rootClassName, methodName));
            // fieldNames.add(esdField.getName());
        }

        for (CustomMethodInfo field : esdClass.getOtherMethodsList()) {
            MethodConfig methodAPIBean = new MethodConfig(field);
            if (field.getComponentType().equals(ComponentType.MODULE)) {
                moduleMethodNames.add(methodAPIBean.getMethodName());
            } else {
                otherMethodNames.add(methodAPIBean.getMethodName());
            }
            allMethodMap.put(methodAPIBean.getMethodName(), methodAPIBean);
            otherMethodNames.add(methodAPIBean.getMethodName());

        }

        for (ESDField field : esdClass.getDisableFieldList()) {
            if (field instanceof CustomMethodInfo) {
                CustomMethodInfo methodField = (CustomMethodInfo) field;
                MethodConfig methodAPIBean = new MethodConfig(methodField);
                if (field.getComponentType().equals(ComponentType.MODULE)) {
                    moduleMethodNames.add(methodAPIBean.getMethodName());
                } else {
                    otherMethodNames.add(methodAPIBean.getMethodName());
                }
                allMethodMap.put(methodAPIBean.getMethodName(), methodAPIBean);
                otherMethodNames.add(methodAPIBean.getMethodName());
            }
        }


        for (CustomMethodInfo field : esdMethods) {
            MethodConfig methodAPIBean = new MethodConfig(field);
            if (field.getComponentType().equals(ComponentType.MODULE)) {
                moduleMethodNames.add(methodAPIBean.getMethodName());
            } else {
                fieldNames.add(methodAPIBean.getFieldName());
            }
            allMethodMap.put(methodAPIBean.getMethodName(), methodAPIBean);
            allMethodNames.add(methodAPIBean.getMethodName());


        }
    }


    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        if (repositoryBean != null) {
            annotationBeans.add(repositoryBean);
        }
        if (entityBean != null) {
            annotationBeans.add(entityBean);
        }
        return annotationBeans;
    }

    public Map<String, FieldEntityConfig> getAllFieldMap() {
        return allFieldMap;
    }

    public void setAllFieldMap(Map<String, FieldEntityConfig> allFieldMap) {
        this.allFieldMap = allFieldMap;
    }

    public RepositoryBean getRepositoryBean() {
        return repositoryBean;
    }

    public void setRepositoryBean(RepositoryBean repositoryBean) {
        this.repositoryBean = repositoryBean;
    }

    public EntityBean getEntityBean() {
        return entityBean;
    }

    public void setEntityBean(EntityBean entityBean) {
        this.entityBean = entityBean;
    }

    public List<FieldEntityConfig> getFieldList() {
        List<FieldEntityConfig> fields = new ArrayList<>();
        for (String methodName : fieldNames) {
            FieldEntityConfig fieldRepositoryConfig = allFieldMap.get(methodName);
            if (fieldRepositoryConfig != null) {
                fields.add(fieldRepositoryConfig);
            }
        }
        return fields;
    }

    public Set<String> getOtherMethodNames() {
        return otherMethodNames;
    }

    public void setOtherMethodNames(Set<String> otherMethodNames) {
        this.otherMethodNames = otherMethodNames;
    }

    @JSONField(serialize = false)
    public List<MethodConfig> getOtherMethodsList() {
        List<MethodConfig> methodConfigs = new ArrayList<>();
        for (String methodName : otherMethodNames) {
            MethodConfig methodConfig = allMethodMap.get(methodName);
            if (!fieldNames.contains(methodConfig.getFieldName())) {
                methodConfigs.add(methodConfig);
            }
        }
        return methodConfigs;
    }

    @JSONField(serialize = false)
    public List<MethodConfig> getAllMethods() {
        List<MethodConfig> methodConfigs = new ArrayList<>();
        for (String methodName : allMethodNames) {
            methodConfigs.add(allMethodMap.get(methodName));
        }
        return methodConfigs;

    }

    @JSONField(serialize = false)
    public List<FieldEntityConfig> getFieldMethods() {
        List<FieldEntityConfig> fieldConfigs = new ArrayList<>();
        for (String methodName : fieldNames) {
            FieldEntityConfig fieldEntityConfig = allFieldMap.get(methodName);
            fieldConfigs.add(fieldEntityConfig);
        }
        return fieldConfigs;

    }

    @JSONField(serialize = false)
    public List<FieldModuleConfig> getModuleMethods() {
        List<FieldModuleConfig> methodConfigs = new ArrayList<>();
        for (String methodName : moduleMethodNames) {
            MethodConfig methodConfig = allMethodMap.get(methodName);
            if (methodConfig != null) {
                methodConfigs.add(new FieldModuleConfig(methodConfig));
            }
        }
        return methodConfigs;

    }

    @JSONField(serialize = false)
    public ESDClass getESDClass() {
        ESDClass esdClass = null;
        if (rootClassName != null) {
            try {
                esdClass = BuildFactory.getInstance().getClassManager().getRepositoryClass(rootClassName, true);
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        return esdClass;
    }

    public MethodConfig getMethodByName(String methodName) {
        MethodConfig methodAPICallBean = allMethodMap.get(methodName);
        return methodAPICallBean;
    }

    public FieldEntityConfig getFieldByName(String fieldName) {
        FieldEntityConfig fieldRepositoryConfig = allFieldMap.get(fieldName);
        return fieldRepositoryConfig;
    }

    public String getProjectVersionName() {
        return projectVersionName;
    }

    public void setProjectVersionName(String projectVersionName) {
        this.projectVersionName = projectVersionName;
    }

    public Set<String> getModuleMethodNames() {
        return moduleMethodNames;
    }

    public void setModuleMethodNames(Set<String> moduleMethodNames) {
        this.moduleMethodNames = moduleMethodNames;
    }

    public Set<String> getFieldNames() {
        return fieldNames;
    }

    public void setFieldNames(Set<String> fieldNames) {
        this.fieldNames = fieldNames;
    }

    public Set<String> getAllMethodNames() {
        return allMethodNames;
    }

    public void setAllMethodNames(Set<String> allMethodNames) {
        this.allMethodNames = allMethodNames;
    }

    public Map<String, MethodConfig> getAllMethodMap() {
        return allMethodMap;
    }

    public void setAllMethodMap(Map<String, MethodConfig> allMethodMap) {
        this.allMethodMap = allMethodMap;
    }

    public String getRootClassName() {
        return rootClassName;
    }

    public void setRootClassName(String rootClassName) {
        this.rootClassName = rootClassName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Set<String> getJavaTempIds() {
        return javaTempIds;
    }

    public void setJavaTempIds(Set<String> javaTempIds) {
        this.javaTempIds = javaTempIds;
    }


}
