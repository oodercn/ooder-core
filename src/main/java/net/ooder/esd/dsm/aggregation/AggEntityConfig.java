package net.ooder.esd.dsm.aggregation;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.common.database.bpm.DefaultColEnum;
import net.ooder.common.util.CaselessStringKeyHashMap;
import net.ooder.common.util.StringUtility;
import net.ooder.annotation.CustomBean;
import net.ooder.annotation.SimpleCustomBean;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.annotation.event.CustomEvent;
import net.ooder.esd.annotation.event.CustomFieldEvent;
import net.ooder.esd.annotation.event.CustomFormEvent;
import net.ooder.esd.annotation.event.CustomHotKeyEvent;
import net.ooder.esd.annotation.event.CustomContentBlockEvent;
import net.ooder.esd.annotation.event.CustomGalleryEvent;
import net.ooder.esd.annotation.event.CustomTitleBlockEvent;
import net.ooder.esd.annotation.event.CustomGridEvent;
import net.ooder.esd.annotation.event.CustomTabsEvent;
import net.ooder.esd.annotation.event.CustomTreeEvent;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.*;
import net.ooder.esd.custom.CustomMethodInfo;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.ref.AggEntityRef;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;
import net.ooder.esd.engine.enums.MenuBarBean;
import net.ooder.web.AggregationBean;
import net.ooder.web.EntityBean;
import net.ooder.web.RequestMappingBean;
import net.ooder.web.ViewBean;
import net.ooder.web.util.MethodUtil;
import org.springframework.stereotype.Controller;

import java.util.*;


public class AggEntityConfig {

    String domainId;

    Long lastUpdateTime;

    String sourceClassName;

    String currClassName;

    String entityClassName;

    String url;

    List<String> moduleMethodNames = new ArrayList<>();

    List<String> fieldNames = new ArrayList<>();

    List<String> allMethodNames = new ArrayList<>();

    List<String> otherMethodNames = new ArrayList<>();

    Set<String> javaTempIds = new LinkedHashSet<>();

    Set<String> otherClassNames = new LinkedHashSet<>();

    Set<AggEntityRef> refs = new LinkedHashSet<>();

    @JSONField(serialize = false)
    List<AggEntityConfig> serviceClasses = new ArrayList<>();

    CaselessStringKeyHashMap<String, FieldAggConfig> allFieldMap = new CaselessStringKeyHashMap<>();

    Map<String, MethodConfig> allMethodMap = new LinkedHashMap<>();

    @JSONField(serialize = false)
    Map<String, MethodConfig> otherMethodMap = new LinkedHashMap<>();

    private MenuBarBean menuBarBean;

    private MethodChinaBean methodChinaBean;

    private RepositoryBean repositoryBean;

    private AggregationBean aggregationBean;

    private EntityBean entityBean;

    private RequestMappingBean requestMappingBean;

    private ViewBean viewBean;

    public AggEntityConfig() {

    }


    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        if (entityBean != null) {
            annotationBeans.add(entityBean);
        }
        if (methodChinaBean != null) {
            annotationBeans.add(methodChinaBean);
        }
        if (aggregationBean != null) {
            annotationBeans.add(aggregationBean);
        }
        if (viewBean != null) {
            annotationBeans.add(viewBean);
        }
        if (menuBarBean != null) {
            annotationBeans.add(menuBarBean);
        }
        if (repositoryBean != null) {
            annotationBeans.add(repositoryBean);
        }
        if (requestMappingBean != null) {
            annotationBeans.add(requestMappingBean);
            annotationBeans.add(new SimpleCustomBean(Controller.class));
        }
        return annotationBeans;
    }

    @JSONField(serialize = false)
    public List<CustomBean> getAPIAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        if (methodChinaBean != null) {
            annotationBeans.add(methodChinaBean);
        }
        if (aggregationBean != null) {
            annotationBeans.add(aggregationBean);
        }

        if (requestMappingBean != null) {
            annotationBeans.add(requestMappingBean);
            annotationBeans.add(new SimpleCustomBean(Controller.class));
        }
        return annotationBeans;
    }

    public AggEntityConfig(ESDClass currClass, String domainId) {
        initClass(currClass);
        this.domainId = domainId;
    }

    public AggEntityConfig(ESDClass currClass) {
        initClass(currClass);
    }


    @JSONField(serialize = false)
    public List<AggEntityConfig> getServiceClasses() {
        List<FieldModuleConfig> moduleConfigs = this.getModuleMethods();
        for (FieldModuleConfig moduleConfig : moduleConfigs) {
            if (moduleConfig.getMethodConfig().getViewClass() != null) {
                Set<ESDClass> childClasses = moduleConfig.getMethodConfig().getViewClass().getServiceClass();
                for (ESDClass esdClass : childClasses) {
                    try {
                        if (!esdClass.getClassName().equals(this.getCurrClassName())) {
                            AggEntityConfig entityConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(esdClass.getClassName(), false);
                            if (!serviceClasses.contains(entityConfig)) {
                                serviceClasses.add(entityConfig);
                            }
                        }

                    } catch (JDSException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        return serviceClasses;
    }

    ;

    @JSONField(serialize = false)
    public List<MethodConfig> getAllBindEventMethod() {
        List<MethodConfig> allAPIBeans = this.getAllMethods();
        List<MethodConfig> methodAPIBeans = new ArrayList<>();
        for (MethodConfig methodAPIBean : allAPIBeans) {
            if (methodAPIBean.getApi() != null) {
                if (methodAPIBean.getApi().getBindGridEvent().size() > 0) {
                    methodAPIBeans.add(methodAPIBean);
                } else if (methodAPIBean.getApi().getBindTabsEvent().size() > 0) {
                    methodAPIBeans.add(methodAPIBean);
                } else if (methodAPIBean.getApi().getBindTreeEvent().size() > 0) {
                    methodAPIBeans.add(methodAPIBean);
                } else if (methodAPIBean.getApi().getBindFormEvent().size() > 0) {
                    methodAPIBeans.add(methodAPIBean);
                } else if (methodAPIBean.getApi().getBindGalleryEvent().size() > 0) {
                    methodAPIBeans.add(methodAPIBean);
                } else if (methodAPIBean.getApi().getBindFieldEvent().size() > 0) {
                    methodAPIBeans.add(methodAPIBean);
                } else if (methodAPIBean.getApi().getBindTitleBlockEvent().size() > 0) {
                    methodAPIBeans.add(methodAPIBean);
                } else if (methodAPIBean.getApi().getBindContentBlockEvent().size() > 0) {
                    methodAPIBeans.add(methodAPIBean);
                } else if (methodAPIBean.getApi().getBindHotKeyEvent().size() > 0) {
                    methodAPIBeans.add(methodAPIBean);
                }
            }

        }
        return methodAPIBeans;
    }


    @JSONField(serialize = false)
    public List<AggEntityConfig> getAllServiceClasses() {
        List<AggEntityConfig> allServiceConfig = new ArrayList<>();
        List<AggEntityConfig> moduleConfigs = this.getServiceClasses();
        for (AggEntityConfig serviceConfig : moduleConfigs) {
            if (!allServiceConfig.contains(serviceConfig)) {
                allServiceConfig.add(serviceConfig);
                List<AggEntityConfig> childServiceList = serviceConfig.getAllServiceClasses();
                for (AggEntityConfig childService : childServiceList) {
                    if (!allServiceConfig.contains(childService)) {
                        allServiceConfig.add(childService);
                    }
                }
            }
        }
        return allServiceConfig;
    }


    @JSONField(serialize = false)
    public ESDClass getSourceClass() {
        ESDClass esdClass = null;
        if (sourceClassName != null) {
            try {
                esdClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(sourceClassName, false);
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        return esdClass;
    }

    @JSONField(serialize = false)
    public MethodConfig getTitleBlockEvent(CustomTitleBlockEvent eventEnum) {
        MethodConfig methodAPICallBean = null;
        List<MethodConfig> methodAPIBeans = this.getAllMethods();
        for (MethodConfig methodAPIBean : methodAPIBeans) {
            if (methodAPIBean.getApi().getBindTitleBlockEvent().contains(eventEnum)) {
                methodAPICallBean = methodAPIBean;
                continue;
            }
        }
        return methodAPICallBean;
    }


    @JSONField(serialize = false)
    public MethodConfig getContentBlockEvent(CustomContentBlockEvent eventEnum) {
        MethodConfig methodAPICallBean = null;
        List<MethodConfig> methodAPIBeans = this.getAllMethods();
        for (MethodConfig methodAPIBean : methodAPIBeans) {
            if (methodAPIBean.getApi().getBindContentBlockEvent().contains(eventEnum)) {
                methodAPICallBean = methodAPIBean;
                continue;
            }
        }
        return methodAPICallBean;
    }


    @JSONField(serialize = false)
    public ESDClass getRootClass() {
        ESDClass rootClass = null;
        ESDClass esdClass = this.getESDClass();
        AggregationBean aggregationBean = esdClass.getAggregationBean();
        try {
            if (aggregationBean != null && aggregationBean.getRootClass() != null) {
                String rootClassName = aggregationBean.getRootClass().getName();
                rootClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(rootClassName, false);
            } else {
                rootClass = esdClass;
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }

        return rootClass;
    }

    void  initClass(ESDClass esdClass) {
        List<CustomMethodInfo> esdMethods = new ArrayList<>();
        esdMethods.addAll(esdClass.getMethodsList());
        // esdMethods.addAll(esdClass.getOtherMethodsList());
        Collections.sort(esdMethods);
        this.currClassName = esdClass.getClassName();
        this.entityBean = esdClass.getEntityBean();
        this.menuBarBean = esdClass.getMenuBarBean();
        this.entityClassName = esdClass.getEntityClassName();
        this.methodChinaBean = esdClass.getMethodChinaBean();
        this.aggregationBean = esdClass.getAggregationBean();
        if (aggregationBean != null) {
            if (aggregationBean.getSourceClass() != null) {
                this.sourceClassName = aggregationBean.getSourceClass().getName();
            } else {
                this.sourceClassName = esdClass.getCtClass().getName();
            }
            if (aggregationBean.getRootClass() == null) {
                aggregationBean.setRootClass(esdClass.getCtClass());
            }
            if (aggregationBean.getEntityClass() != null) {
                this.entityClassName = aggregationBean.getEntityClass().getName();
            }

        } else {
            this.sourceClassName = esdClass.getCtClass().getName();
        }

        this.domainId = esdClass.getDomainId();
        this.viewBean = esdClass.getViewBean();
        this.requestMappingBean = esdClass.getRequestMappingBean();
        this.repositoryBean = esdClass.getRepositoryBean();
        try {
            DomainInst domainInst = DSMFactory.getInstance().getAggregationManager().getDomainInstById(domainId, null);
            String baseUrl = domainInst.getProjectVersionName() + "/" + domainInst.getSpace() + "/";
            if (requestMappingBean == null) {
                String beanPath = esdClass.getEntityClass().getName().toLowerCase();
                if (viewBean != null) {
                    beanPath = StringUtility.formatUrl(beanPath);
                }

                if (esdClass.isProxy()) {
                    requestMappingBean = new RequestMappingBean(baseUrl + "/" + beanPath, "");
                } else {
                    requestMappingBean = new RequestMappingBean(beanPath, "");
                }

            } else {
                url = requestMappingBean.getFristUrl();
                if (url.startsWith("/")) {
                    url = url.substring(1);
                }
                if (esdClass.isProxy()) {
                    if (!url.startsWith(baseUrl)) {
                        requestMappingBean = new RequestMappingBean(baseUrl + "/" + url, "");
                    }
                }
            }
            url = requestMappingBean.getFristUrl();
        } catch (JDSException e) {
            e.printStackTrace();
        }


        for (ESDField esdField : esdClass.getFieldList()) {
            allFieldMap.put(esdField.getName(), new FieldAggConfig(esdField, domainId));
            fieldNames.add(esdField.getName());
        }

        for (ESDField esdField : esdClass.getDisableFieldList()) {
            allFieldMap.put(esdField.getName(), new FieldAggConfig(esdField, domainId));
            if (!fieldNames.contains(esdField.getName())) {
                fieldNames.add(esdField.getName());
            }
        }

        for (CustomMethodInfo field : esdClass.getOtherMethodsList()) {
            MethodConfig methodAPIBean = new MethodConfig(field, this);
            if (field.getComponentType().equals(ComponentType.MODULE)) {
                if (!moduleMethodNames.contains(methodAPIBean.getMethodName())) {
                    moduleMethodNames.add(methodAPIBean.getMethodName());
                }
            } else {
                if (!otherMethodNames.contains(methodAPIBean.getMethodName())) {
                    otherMethodNames.add(methodAPIBean.getMethodName());
                }
            }
            otherMethodMap.put(methodAPIBean.getMethodName(), methodAPIBean);
        }

        for (ESDField field : esdClass.getDisableFieldList()) {
            if (field instanceof CustomMethodInfo) {
                CustomMethodInfo methodField = (CustomMethodInfo) field;
                MethodConfig methodAPIBean = new MethodConfig(methodField, this);
                if (field.getComponentType().equals(ComponentType.MODULE)) {
                    if (!moduleMethodNames.contains(methodAPIBean.getMethodName())) {
                        moduleMethodNames.add(methodAPIBean.getMethodName());
                    }
                } else {
                    if (!otherMethodNames.contains(methodAPIBean.getMethodName())) {
                        otherMethodNames.add(methodAPIBean.getMethodName());
                    }
                }
                otherMethodMap.put(methodAPIBean.getMethodName(), methodAPIBean);
                if (!allMethodNames.contains(methodAPIBean.getMethodName())) {
                    allMethodNames.add(methodAPIBean.getMethodName());
                }
            }
        }

        for (CustomMethodInfo field : esdMethods) {
            MethodConfig methodAPIBean = new MethodConfig(field, this);
            if (field.getComponentType().equals(ComponentType.MODULE)) {
                if (!moduleMethodNames.contains(methodAPIBean.getMethodName())) {
                    moduleMethodNames.add(methodAPIBean.getMethodName());
                }
            } else if (MethodUtil.isGetMethod(methodAPIBean.getMethod()) || methodAPIBean.isModule()) {
                if (!fieldNames.contains(methodAPIBean.getFieldName())) {
                    fieldNames.add(methodAPIBean.getFieldName());
                }
                allFieldMap.put(methodAPIBean.getFieldName(), new FieldAggConfig(field, domainId));
            }
            allMethodMap.put(methodAPIBean.getMethodName(), methodAPIBean);
            allMethodNames.remove(methodAPIBean.getMethodName());
            allMethodNames.add(methodAPIBean.getMethodName());

        }

        for (ESDField refField : esdClass.getRefFields()) {
            if (refField instanceof CustomMethodInfo) {
                AggEntityRef aggEntityRef = new AggEntityRef((CustomMethodInfo) refField, domainId);
                this.refs.add(aggEntityRef);
            }
        }
    }


    public FieldAggConfig createField(String fieldname, Class returntype, ComponentType componentType) throws JDSException {
        FieldAggConfig fieldAgg = new FieldAggConfig(this, fieldname, returntype, componentType, fieldNames.size());
        if (componentType == null || componentType.equals(ComponentType.HIDDENINPUT)) {
            fieldAgg.setColHidden(true);
        }
        allFieldMap.put(fieldname, fieldAgg);
        fieldNames.add(fieldname);
        DSMFactory.getInstance().getAggregationManager().updateAggEntityConfig(this);
        return fieldAgg;
    }

    @JSONField(serialize = false)
    public FieldAggConfig getUidField() {

        for (FieldAggConfig esdField : this.getFieldList()) {
            if (esdField.getUid() != null && esdField.getUid()) {
                return esdField;
            }
        }

        return null;
    }

    @JSONField(serialize = false)
    public FieldAggConfig getCaptionField() {
        for (FieldAggConfig esdField : this.getFieldList()) {
            if (esdField.getCaptionField() != null && esdField.getCaptionField()) {
                return esdField;
            }
        }

        return null;
    }

    @JSONField(serialize = false)
    public List<FieldAggConfig> getFieldList() {
        List<FieldAggConfig> fields = new ArrayList<>();
        for (String fieldName : fieldNames) {
            FieldAggConfig fieldAggConfig = this.getFieldByName(fieldName);
            if (fieldAggConfig != null) {
                fields.add(fieldAggConfig);
            }
        }
        Collections.sort(fields);
        return fields;
    }

    @JSONField(serialize = false)
    public List<FieldAggConfig> getCustomFieldList() {
        List<FieldAggConfig> fields = new ArrayList<>();
        for (String fieldName : fieldNames) {
            FieldAggConfig fieldAggConfig = this.getFieldByName(fieldName);
            if (fieldAggConfig != null && !fieldAggConfig.getComponentType().equals(ComponentType.MODULE)) {
                fields.add(fieldAggConfig);
            }
        }
        Collections.sort(fields);
        return fields;
    }

    @JSONField(serialize = false)
    public List<FieldAggConfig> getSimpleFieldList() {
        List<FieldAggConfig> fields = new ArrayList<>();
        for (String fieldName : fieldNames) {
            FieldAggConfig fieldAggConfig = this.getFieldByName(fieldName);
            if (fieldAggConfig != null && (fieldAggConfig.getEnumClass() == null || fieldAggConfig.getEnumClass().equals("")) && !fieldAggConfig.getComponentType().equals(ComponentType.MODULE)) {
                fields.add(fieldAggConfig);
            }
        }
        return fields;
    }

    @JSONField(serialize = false)
    public List<FieldAggConfig> getHiddenFieldList() {
        List<FieldAggConfig> fields = new ArrayList<>();
        DefaultColEnum[] values = DefaultColEnum.values();
        for (String fieldName : fieldNames) {
            FieldAggConfig fieldAggConfig = this.getFieldByName(fieldName);
            if (fieldAggConfig != null) {
                if (fieldAggConfig.getColHidden()) {
                    fields.add(fieldAggConfig);
                } else {
                    for (DefaultColEnum colEnum : values) {
                        if (colEnum.name.toUpperCase().equals(fieldName.toUpperCase())) {
                            fields.add(fieldAggConfig);
                        }
                    }
                }

            }

        }
        return fields;
    }

    @JSONField(serialize = false)
    public List<FieldAggConfig> getEnumsFieldList() {
        List<FieldAggConfig> fields = new ArrayList<>();
        for (String fieldName : fieldNames) {
            FieldAggConfig fieldAggConfig = this.getFieldByName(fieldName);
            if (fieldAggConfig != null && fieldAggConfig.getEnumClass() != null && !fieldAggConfig.getEnumClass().equals("")) {
                fields.add(fieldAggConfig);
            }
        }
        return fields;
    }

    @JSONField(serialize = false)
    public List<MethodConfig> getOtherMethodsList() {
        List<MethodConfig> methodConfigs = new ArrayList<>();
        for (String methodName : otherMethodNames) {
            MethodConfig methodConfig = otherMethodMap.get(methodName);
            if (methodConfig == null) {
                methodConfig = allMethodMap.get(methodName);
            }
            if (methodConfig != null && !fieldNames.contains(methodConfig.getFieldName())) {
                methodConfigs.add(methodConfig);
            }
        }
        return methodConfigs;
    }

    @JSONField(serialize = false)
    public List<MethodConfig> getAllMethods() {
        List<MethodConfig> methodConfigs = new ArrayList<>();
        for (String methodName : allMethodNames) {
            MethodConfig methodConfig = allMethodMap.get(methodName);
            if (methodConfig == null) {
                methodConfig = otherMethodMap.get(methodName);
            }
            if (methodConfig != null) {
                if (methodConfig.getRequestMapping().getValue().isEmpty()) {
                    methodConfig.getRequestMapping().getValue().add(methodConfig.getDefaultMenuItem().getDefaultMethodName());
                }
                methodConfigs.add(methodConfig);
            }
        }
        return methodConfigs;

    }

    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet = new HashSet<>();
        List<MethodConfig> allMethods = getAllMethods();
        for (MethodConfig methodConfig : allMethods) {
            Set<Class> methodClassSet = methodConfig.getOtherClass();
            for (Class clazz : methodClassSet) {
                if (clazz != null) {
                    classSet.add(clazz);
                }
            }
        }
        return classSet;
    }

    @JSONField(serialize = false)
    public MethodConfig getMethodByEvent(CustomEvent eventEnum) {
        MethodConfig methodAPICallBean = null;
        if (eventEnum instanceof CustomGridEvent) {
            methodAPICallBean = this.getGridEvent((CustomGridEvent) eventEnum);
        } else if (eventEnum instanceof CustomTreeEvent) {
            methodAPICallBean = this.getTreeEvent((CustomTreeEvent) eventEnum);
        } else if (eventEnum instanceof CustomTabsEvent) {
            methodAPICallBean = this.getTabsEvent((CustomTabsEvent) eventEnum);
        } else if (eventEnum instanceof CustomFormEvent) {
            methodAPICallBean = this.getFormEvent((CustomFormEvent) eventEnum);
        } else if (eventEnum instanceof CustomFieldEvent) {
            methodAPICallBean = this.getFieldEvent((CustomFieldEvent) eventEnum);
        } else if (eventEnum instanceof CustomGalleryEvent) {
            methodAPICallBean = this.getGalleryEvent((CustomGalleryEvent) eventEnum);
        } else if (eventEnum instanceof CustomTitleBlockEvent) {
            methodAPICallBean = this.getTitleBlockEvent((CustomTitleBlockEvent) eventEnum);
        } else if (eventEnum instanceof CustomContentBlockEvent) {
            methodAPICallBean = this.getContentBlockEvent((CustomContentBlockEvent) eventEnum);
        } else if (eventEnum instanceof CustomHotKeyEvent) {
            methodAPICallBean = this.getHotKeyEvent((CustomHotKeyEvent) eventEnum);
        }
        return methodAPICallBean;
    }


    @JSONField(serialize = false)
    public MethodConfig getFieldEvent(CustomFieldEvent eventEnum) {
        MethodConfig methodAPICallBean = null;
        List<MethodConfig> methodAPIBeans = this.getAllMethods();
        for (MethodConfig methodAPIBean : methodAPIBeans) {
            if (methodAPIBean.getApi() != null && methodAPIBean.getApi().getBindFieldEvent().contains(eventEnum)) {
                methodAPICallBean = methodAPIBean;
                continue;
            }
        }
        return methodAPICallBean;
    }


    @JSONField(serialize = false)
    public MethodConfig getGalleryEvent(CustomGalleryEvent eventEnum) {
        MethodConfig methodAPICallBean = null;
        List<MethodConfig> methodAPIBeans = this.getAllMethods();
        for (MethodConfig methodAPIBean : methodAPIBeans) {
            if (methodAPIBean.getApi().getBindGalleryEvent().contains(eventEnum)) {
                methodAPICallBean = methodAPIBean;
                continue;
            }
        }
        return methodAPICallBean;
    }

    @JSONField(serialize = false)
    public MethodConfig getHotKeyEvent(CustomHotKeyEvent eventEnum) {
        MethodConfig methodAPICallBean = null;
        List<MethodConfig> methodAPIBeans = this.getAllMethods();
        for (MethodConfig methodAPIBean : methodAPIBeans) {
            if (methodAPIBean.getApi().getBindHotKeyEvent().contains(eventEnum)) {
                methodAPICallBean = methodAPIBean;
                continue;
            }
        }
        return methodAPICallBean;
    }

    @JSONField(serialize = false)
    private MethodConfig getGridEvent(CustomGridEvent eventEnum) {
        MethodConfig methodAPICallBean = null;
        List<MethodConfig> methodAPIBeans = this.getAllMethods();
        for (MethodConfig methodAPIBean : methodAPIBeans) {
            if (methodAPIBean.getApi() != null && methodAPIBean.getApi().getBindGridEvent().contains(eventEnum)) {
                methodAPICallBean = methodAPIBean;
                continue;
            }
        }
        return methodAPICallBean;
    }


    @JSONField(serialize = false)
    private MethodConfig getTabsEvent(CustomTabsEvent eventEnum) {
        MethodConfig methodAPICallBean = null;
        List<MethodConfig> methodAPIBeans = this.getAllMethods();
        for (MethodConfig methodAPIBean : methodAPIBeans) {
            if (methodAPIBean.getApi() != null && methodAPIBean.getApi().getBindTabsEvent().contains(eventEnum)) {
                methodAPICallBean = methodAPIBean;
                continue;
            }
        }
        return methodAPICallBean;
    }

    @JSONField(serialize = false)
    private MethodConfig getFormEvent(CustomFormEvent eventEnum) {
        MethodConfig methodAPICallBean = null;
        List<MethodConfig> methodAPIBeans = this.getAllMethods();
        for (MethodConfig methodAPIBean : methodAPIBeans) {
            if (methodAPIBean.getApi() != null && methodAPIBean.getApi().getBindFormEvent().contains(eventEnum)) {
                methodAPICallBean = methodAPIBean;
                continue;
            }
        }
        return methodAPICallBean;
    }


    @JSONField(serialize = false)
    private MethodConfig getTreeEvent(CustomTreeEvent eventEnum) {
        MethodConfig methodAPICallBean = null;
        List<MethodConfig> methodAPIBeans = this.getAllMethods();
        for (MethodConfig methodAPIBean : methodAPIBeans) {
            if (methodAPIBean.getApi() != null && methodAPIBean.getApi().getBindTreeEvent().contains(eventEnum)) {
                methodAPICallBean = methodAPIBean;
                continue;
            }
        }
        return methodAPICallBean;
    }

    @JSONField(serialize = false)
    public MethodConfig getMethodByItem(CustomMenuItem item) {
        MethodConfig methodAPICallBean = this.getMethodByName(item.getMethodName());
        if (methodAPICallBean == null) {
            List<MethodConfig> methodAPIBeans = this.getAllMethods();
            for (MethodConfig methodAPIBean : methodAPIBeans) {
                if (methodAPIBean.getBindMenus().contains(item)) {
                    methodAPICallBean = methodAPIBean;
                    continue;
                }
            }
        }
        return methodAPICallBean;
    }

    @JSONField(serialize = false)
    public List<MethodConfig> getProxyMethods() {
        List<MethodConfig> proxyMethods = new ArrayList<>();
        List<MethodConfig> methodConfigs = getAllMethods();
        for (MethodConfig methodConfig : methodConfigs) {
            if (methodConfig.getPublicMethod() && !fieldNames.contains(methodConfig.getFieldName())) {
                proxyMethods.add(methodConfig);
            }
        }
        return proxyMethods;

    }


    @JSONField(serialize = false)
    public List<MethodConfig> getServiceMethods() {
        List<MethodConfig> proxyMethods = new ArrayList<>();
        List<MethodConfig> methodConfigs = getOtherMethodsList();
        for (MethodConfig methodConfig : methodConfigs) {
            if (methodConfig.getApi() != null && !methodConfig.isModule() && methodConfig.getPublicMethod() && !methodConfig.getCustomMethodInfo().isSplit()) {
                proxyMethods.add(methodConfig);
            }
        }
        return proxyMethods;

    }


    @JSONField(serialize = false)
    public List<FieldAggConfig> getFieldMethods() {
        List<FieldAggConfig> fieldAggConfigs = new ArrayList<>();
        for (String methodName : fieldNames) {
            FieldAggConfig fieldAggConfig = allFieldMap.get(methodName.toLowerCase());
            fieldAggConfigs.add(fieldAggConfig);
        }
        return fieldAggConfigs;

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
        if (currClassName != null) {
            try {
                esdClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(currClassName, false);
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        if (esdClass == null) {
            try {
                esdClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(sourceClassName, false);
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        return esdClass;
    }

    @JSONField(serialize = false)
    public ESDClass getEntityClass() {
        ESDClass entityClass = null;
        if (entityClassName != null) {
            try {
                entityClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(entityClassName, false);
            } catch (JDSException e) {
                e.printStackTrace();
            }
        } else {
            entityClass = this.getESDClass().getEntityClass();
        }

        return entityClass;
    }


    public MethodConfig getMethodByName(String methodName) {
        MethodConfig methodAPICallBean = null;
        if (methodName != null) {
            methodAPICallBean = allMethodMap.get(methodName);
            if (methodAPICallBean == null) {
                if (this.getESDClass().getMethodInfo(methodName) != null) {
                    CustomMethodInfo methodInfo = this.getESDClass().getMethodInfo(methodName);
                    methodAPICallBean = new MethodConfig(methodInfo, this);
                    allMethodMap.put(methodName, methodAPICallBean);
                } else if (this.getESDClass().getField(methodName) != null) {
                    ESDField methodInfo = this.getESDClass().getField(methodName);
                    if (methodInfo instanceof CustomMethodInfo) {
                        methodAPICallBean = new MethodConfig((CustomMethodInfo) methodInfo, this);
                        allMethodMap.put(methodName, methodAPICallBean);
                    }
                }
            }
        }
        return methodAPICallBean;
    }


    public FieldAggConfig getFieldByName(String fieldName) {
        FieldAggConfig fieldAggConfig = allFieldMap.get(fieldName.toLowerCase());
        if (fieldAggConfig == null) {
            for (String methodName : allMethodNames) {
                MethodConfig methodConfig = allMethodMap.get(methodName);
                if (methodConfig != null && methodConfig.getMethod() != null && (methodConfig.getMethodName().equals(fieldName) || methodConfig.getFieldName().equals(fieldName)) && MethodUtil.isGetMethod(methodConfig.getMethod())) {
                    fieldAggConfig = new FieldAggConfig(methodConfig.getCustomMethodInfo(), domainId);
                    allFieldMap.put(fieldAggConfig.getFieldname(), fieldAggConfig);
                    allFieldMap.put(methodConfig.getMethodName(), fieldAggConfig);
                }
            }

        }
        if (fieldAggConfig != null) {
            fieldAggConfig.setEntityClassName(this.getESDClass().getCtClass().getName());
        }
        return fieldAggConfig;
    }

    public Long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getEntityClassName() {
        return entityClassName;
    }

    public void setEntityClassName(String entityClassName) {
        this.entityClassName = entityClassName;
    }

    public void setServiceClasses(List<AggEntityConfig> serviceClasses) {
        this.serviceClasses = serviceClasses;
    }

    public Map<String, MethodConfig> getOtherMethodMap() {
        return otherMethodMap;
    }

    public void setOtherMethodMap(Map<String, MethodConfig> otherMethodMap) {
        this.otherMethodMap = otherMethodMap;
    }

    public MethodChinaBean getMethodChinaBean() {
        return methodChinaBean;
    }

    public void setMethodChinaBean(MethodChinaBean methodChinaBean) {
        this.methodChinaBean = methodChinaBean;
    }

    public RepositoryBean getRepositoryBean() {
        return repositoryBean;
    }

    public void setRepositoryBean(RepositoryBean repositoryBean) {
        this.repositoryBean = repositoryBean;
    }

    public AggregationBean getAggregationBean() {
        return aggregationBean;
    }

    public void setAggregationBean(AggregationBean aggregationBean) {
        this.aggregationBean = aggregationBean;
    }

    public EntityBean getEntityBean() {
        return entityBean;
    }

    public void setEntityBean(EntityBean entityBean) {
        this.entityBean = entityBean;
    }

    public RequestMappingBean getRequestMappingBean() {
        return requestMappingBean;
    }

    public void setRequestMappingBean(RequestMappingBean requestMappingBean) {
        this.requestMappingBean = requestMappingBean;
    }

    public ViewBean getViewBean() {
        return viewBean;
    }

    public void setViewBean(ViewBean viewBean) {
        this.viewBean = viewBean;
    }

    public CaselessStringKeyHashMap<String, FieldAggConfig> getAllFieldMap() {
        return allFieldMap;
    }

    public void setAllFieldMap(CaselessStringKeyHashMap<String, FieldAggConfig> allFieldMap) {
        this.allFieldMap = allFieldMap;
    }

    public Set<AggEntityRef> getRefs() {
        return refs;
    }

    public void setRefs(Set<AggEntityRef> refs) {
        this.refs = refs;
    }

    public List<String> getModuleMethodNames() {
        return moduleMethodNames;
    }

    public void setModuleMethodNames(List<String> moduleMethodNames) {
        this.moduleMethodNames = moduleMethodNames;
    }

    public List<String> getFieldNames() {
        return fieldNames;
    }

    public void setFieldNames(List<String> fieldNames) {
        this.fieldNames = fieldNames;
    }

    public List<String> getAllMethodNames() {
        return allMethodNames;
    }

    public void setAllMethodNames(List<String> allMethodNames) {
        this.allMethodNames = allMethodNames;
    }

    public List<String> getOtherMethodNames() {
        return otherMethodNames;
    }

    public void setOtherMethodNames(List<String> otherMethodNames) {
        this.otherMethodNames = otherMethodNames;
    }

    public MenuBarBean getMenuBarBean() {
        return menuBarBean;
    }

    public void setMenuBarBean(MenuBarBean menuBarBean) {
        this.menuBarBean = menuBarBean;
    }

    public Map<String, MethodConfig> getAllMethodMap() {
        return allMethodMap;
    }

    public void setAllMethodMap(Map<String, MethodConfig> allMethodMap) {
        this.allMethodMap = allMethodMap;
    }


    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getSourceClassName() {
        return sourceClassName;
    }

    public void setSourceClassName(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }

    public Set<String> getJavaTempIds() {
        return javaTempIds;
    }

    public void setJavaTempIds(Set<String> javaTempIds) {
        this.javaTempIds = javaTempIds;
    }

    public String getUrl() {
        return url;
    }

    public Set<String> getOtherClassNames() {
        return otherClassNames;
    }

    public void setOtherClassNames(Set<String> otherClassNames) {
        this.otherClassNames = otherClassNames;
    }

    public void setUrl(String url) {
        this.url = url;
        if (!url.endsWith("/")) {
            url = url + "/";
        }
        if (!url.startsWith("/")) {
            url = "/" + url;
        }
        if (requestMappingBean == null) {
            requestMappingBean = new RequestMappingBean(url, "");
        } else {
            requestMappingBean.reSetUrl(url);
        }
    }

    public String getCurrClassName() {
        return currClassName;
    }

    public void setCurrClassName(String currClassName) {
        this.currClassName = currClassName;
    }
}
