package net.ooder.esd.custom;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSConstants;
import net.ooder.common.JDSException;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.common.util.ClassUtility;
import net.ooder.annotation.MethodChinaName;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.MenuBarMenu;
import net.ooder.esd.annotation.View;
import net.ooder.esd.annotation.view.DynLoadAnnotation;
import net.ooder.esd.bean.*;
import net.ooder.esd.engine.enums.MenuBarBean;
import net.ooder.esd.util.DSMAnnotationUtil;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.domain.CustomDomain;
import net.ooder.esd.dsm.repository.database.proxy.DSMColProxy;
import net.ooder.esd.dsm.repository.database.proxy.DSMTableProxy;
import net.ooder.esd.util.OODUtil;
import net.ooder.web.AggregationBean;
import net.ooder.web.EntityBean;
import net.ooder.web.RequestMappingBean;
import net.ooder.annotation.*;
import net.ooder.web.ViewBean;
import net.ooder.web.util.AnnotationUtil;
import net.ooder.web.util.MethodUtil;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class ESDClass {
    protected Log log = LogFactory.getLog(JDSConstants.CONFIG_KEY, ESDClass.class);
    private Class ctClass;

    private String sourceClassName;

    private String rootClassName;

    private String viewServiceClassName;
    //
    //Repostitory Entity
    private String entityClassName;

    //View Entity
    private String viewClassName;

    private String className;

    private String caption;

    private String imageClass;

    private String packageName;

    public String name;

    public String uid;

    public String projectVersionName;

    public String desc;

    public ESDField captionField;

    public ESDField uidField;


    public List<ESDField> hiddenFieldList = new ArrayList<>();

    public List<CustomMethodInfo> methodsList = new ArrayList<>();

    public List<CustomMethodInfo> otherMethodsList = new ArrayList<>();

    public List<String> fieldNameList = new ArrayList<>();

    public Map<String, ESDField> esdFieldMap = new HashMap<>();

    public Map<String, CustomMethodInfo> methodMap = new HashMap<>();

    public Map<String, ESDField> allFieldMap = new HashMap<>();

    public List<ESDField> disableFieldList = new ArrayList<>();

    public CustomDomain customDomain;

    private ArrayList<Field> allCtFields;

    private ArrayList<Method> allCtMethods;

    private Set<String> serviceClassNames;
    @JSONField(serialize = false)
    private MethodChinaBean methodChinaBean;
    @JSONField(serialize = false)
    private RepositoryBean repositoryBean;
    @JSONField(serialize = false)
    private AggregationBean aggregationBean;
    @JSONField(serialize = false)
    private EntityBean entityBean;
    @JSONField(serialize = false)
    private RequestMappingBean requestMappingBean;
    @JSONField(serialize = false)
    private ViewBean viewBean;
    @JSONField(serialize = false)
    private MenuBarBean menuBarBean;

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    private static final String[] customClassName = new String[]{"toString", "compareTo", "equals", "getCachedSize", "setCachedSize"};

    public ESDClass(DSMTableProxy tableProxy) {
        this.projectVersionName = tableProxy.getProjectVersionName();
        this.uid = tableProxy.getPkFieldName();
        List<String> hiddenList = new ArrayList<>();
        for (DSMColProxy hiddenField : tableProxy.getHiddenFields()) {
            hiddenList.add(hiddenField.getTableFieldInfo().getFieldName().toLowerCase());
            hiddenFieldList.add(hiddenField.getTableFieldInfo());
        }
        for (DSMColProxy hiddenField : tableProxy.getPkFields()) {
            hiddenList.add(hiddenField.getTableFieldInfo().getFieldName().toLowerCase());
            hiddenFieldList.add(hiddenField.getTableFieldInfo());
        }
        for (DSMColProxy colInfo : tableProxy.getDSMColList()) {
            if (!hiddenList.contains(colInfo.getTableFieldInfo().getFieldName().toLowerCase())) {
                esdFieldMap.put(colInfo.getTableFieldInfo().getFieldName(), colInfo.getTableFieldInfo());
            }
        }
        this.captionField = esdFieldMap.get(tableProxy.getCaptionField().getTableFieldInfo().getFieldName());
    }


    ESDClass(Class ctClass) {
        init(ctClass);
    }


    void init(Class ctClass) {
        long start = System.currentTimeMillis();
        this.ctClass = ctClass;
        this.allCtFields = new ArrayList<>();
        for (Field field : ctClass.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) {
                allCtFields.add(field);
            }
        }

        for (Field field : ctClass.getFields()) {
            if (field.getDeclaringClass().equals(ctClass) && !field.getDeclaringClass().equals(Object.class)) {
                allCtFields.add(field);
            }
        }

        this.allCtMethods = new ArrayList<>();
        List<String> methodNames = new ArrayList<>();
        for (Method method : ctClass.getDeclaredMethods()) {
            if (!Modifier.isStatic(method.getModifiers())) {
                allCtMethods.add(method);
                methodNames.add(method.getName());
            }
        }

        for (Method method : ctClass.getMethods()) {
            if (!Modifier.isStatic(method.getModifiers())
                    && !methodNames.contains(method.getName())
                    && !method.getDeclaringClass().equals(ctClass)
                    && !method.getDeclaringClass().equals(Enum.class)
                    && !method.getDeclaringClass().equals(Object.class)) {
                allCtMethods.add(method);
            }
        }


        MethodChinaName chinaName = AnnotationUtil.getClassAnnotation(ctClass, MethodChinaName.class);
        if (chinaName != null) {
            this.methodChinaBean = new MethodChinaBean(chinaName);
        }


        RepositoryAnnotation repository = AnnotationUtil.getClassAnnotation(ctClass, RepositoryAnnotation.class);
        if (repository != null) {
            repositoryBean = new RepositoryBean(repository);
        }

        Aggregation aggregationClass = AnnotationUtil.getClassAnnotation(ctClass, Aggregation.class);
        if (aggregationClass != null) {
            aggregationBean = new AggregationBean(aggregationClass);
        }

        ESDEntity dsmEntityClass = AnnotationUtil.getClassAnnotation(ctClass, ESDEntity.class);
        if (dsmEntityClass != null) {
            entityBean = new EntityBean(dsmEntityClass);
        }

        MenuBarMenu menuBarMenu = AnnotationUtil.getClassAnnotation(ctClass, MenuBarMenu.class);
        if (menuBarMenu != null) {
            menuBarBean = new MenuBarBean(menuBarMenu);
        }

        DBTable dbTable = AnnotationUtil.getClassAnnotation(ctClass, DBTable.class);

        RequestMapping requestMapping = AnnotationUtil.getClassAnnotation(ctClass, RequestMapping.class);
        if (requestMapping != null) {
            requestMappingBean = new RequestMappingBean(requestMapping);
        }

        View view = AnnotationUtil.getClassAnnotation(ctClass, View.class);
        if (view != null) {
            viewBean = new ViewBean(view);
        }
        this.name = ctClass.getSimpleName();
        this.className = ctClass.getName();
        this.packageName = ctClass.getPackage().getName();

        if (chinaName != null) {
            desc = chinaName.cname();
            this.imageClass = chinaName.imageClass();
        } else if (dbTable != null) {
            this.desc = dbTable.cname();
        } else {
            desc = this.getName();
        }


        //中文注解最后覆盖
        if (caption == null || caption.equals("")) {
            if (chinaName != null) {
                caption = chinaName.cname();
            } else {
                this.caption = name;
            }
        }

        if (repository != null) {
            if (!repository.imageClass().equals("")) {
                this.imageClass = repository.imageClass();
            }
            if (repository.entityClass() != null && !repository.entityClass().equals(Void.class) && !repository.entityClass().equals(Enum.class)) {
                this.entityClassName = repository.entityClass().getName();
            }
        }

        if (aggregationClass != null) {
            if (!aggregationClass.sourceClass().equals(Void.class)) {
                this.sourceClassName = aggregationClass.sourceClass().getName();
            }
            if (!aggregationClass.rootClass().equals(Void.class)) {
                this.rootClassName = aggregationClass.rootClass().getName();
            }
            if (aggregationClass.type().equals(AggregationType.DOMAIN)) {
                customDomain = new CustomDomain(ctClass);
            }

        }

        if (dsmEntityClass != null) {
            if (!dsmEntityClass.sourceClass().equals(Void.class)) {
                this.sourceClassName = dsmEntityClass.sourceClass().getName();
            }

        }
        //initField();


        //  log.info("end new ESDClass---end= " + className + "[" + className + "] times=" + (System.currentTimeMillis() - start));
    }


    public void initField() {
        if (esdFieldMap == null || esdFieldMap.isEmpty()) {
            long start = System.currentTimeMillis();
            Map<String, ESDField> fieldMap = new LinkedHashMap<String, ESDField>();
            Map<String, ESDField> disableFieldMap = new LinkedHashMap<String, ESDField>();
            Map<String, CustomAnnotation> customAnnotationMap = new LinkedHashMap<String, CustomAnnotation>();
            int index = 0;
            for (Field field : allCtFields) {
                CustomAnnotation methodmapping = field.getAnnotation(CustomAnnotation.class);
                Uid uid = field.getAnnotation(Uid.class);
                Pid pid = field.getAnnotation(Pid.class);
                Caption caption = field.getAnnotation(Caption.class);
                CustomFieldInfo fieldInfo = new CustomFieldInfo(field, index, this);
                if (methodmapping != null || uid != null || pid != null || caption != null) {
                    fieldInfo.setDefault(false);
                }
                if (fieldInfo.isSerialize()) {
                    fieldMap.put(field.getName().toLowerCase(), fieldInfo);
                    esdFieldMap.put(fieldInfo.getFieldName(), fieldInfo);
                    fieldNameList.add(fieldInfo.getFieldName());
                    index = index + 1;
                } else {
                    disableFieldMap.put(field.getName().toLowerCase(), fieldInfo);
                    disableFieldList.add(fieldInfo);
                }
                if (methodmapping != null) {
                    if (methodmapping.captionField()) {
                        captionField = fieldInfo;
                    }
                    customAnnotationMap.put(field.getName().toLowerCase(), methodmapping);
                    if (methodmapping.uid()) {
                        this.uid = field.getName();
                    }
                }
                if (uid != null) {
                    this.uid = field.getName();
                }

                if (caption != null) {
                    this.captionField = fieldInfo;
                }

            }


            //   log.info("end new ESDClass---fillAnnotation= " + className + "[" + className + "] times=" + (System.currentTimeMillis() - start));

            for (Method method : allCtMethods) {
                if (!Arrays.asList(customClassName).contains(method.getName())) {
                    CustomMethodInfo methodInfo = new CustomMethodInfo(method, this);
                    if (methodInfo.isSerialize()) {
                        if (MethodUtil.isGetMethod(methodInfo.getInnerMethod()) || methodInfo.isModule()) {
                            String fieldName = MethodUtil.getFieldName(method);
                            methodInfo.setFieldName(fieldName);
                            CustomAnnotation methodmapping = AnnotationUtil.getMethodAnnotation(method, CustomAnnotation.class);
                            DynLoadAnnotation dynLoadAnnotation = AnnotationUtil.getMethodAnnotation(method, DynLoadAnnotation.class);
                            CustomAnnotation allmapping = customAnnotationMap.get(fieldName);
                            Caption caption = AnnotationUtil.getMethodAnnotation(method, Caption.class);
                            Uid uid = AnnotationUtil.getMethodAnnotation(method, Uid.class);
                            //字段必须可见
                            if (methodInfo.isSerialize() && !disableFieldMap.containsKey(fieldName)) {
                                //如果字段未定义
                                ESDField field = fieldMap.get(fieldName.toLowerCase());
                                if (field == null
                                        || !(field instanceof CustomFieldInfo)
                                        || ((CustomFieldInfo) fieldMap.get(fieldName.toLowerCase())).isDefault()) {
                                    fieldMap.put(fieldName.toLowerCase(), methodInfo);
                                    esdFieldMap.put(methodInfo.getFieldName(), methodInfo);

                                    if (!fieldNameList.contains(methodInfo.getFieldName())) {
                                        fieldNameList.add(methodInfo.getFieldName());
                                    }

                                } else {
                                    if (allmapping == null && (methodmapping != null || dynLoadAnnotation != null)) {
                                        //优先使用字段注解
                                        fieldMap.put(fieldName.toLowerCase(), methodInfo);
                                    }
                                }
                            }
                            if (methodmapping != null && methodmapping.uid()) {
                                this.uid = fieldName;
                            }
                            if (uid != null) {
                                this.uid = fieldName;
                            }
                            if (methodmapping != null && methodmapping.captionField()) {
                                this.captionField = methodInfo;
                            }
                            if (caption != null) {
                                this.captionField = methodInfo;
                            }

                        } else if (!MethodUtil.isSetMethod(method)) {
                            otherMethodsList.add(methodInfo);
                        }
                        methodsList.add(methodInfo);
                    } else if (!disableFieldMap.containsKey(methodInfo.getName())) {
                        disableFieldMap.put(methodInfo.getName().toLowerCase(), methodInfo);
                        disableFieldList.add(methodInfo);
                    }

                }
            }
            log.info("end new ESDClass---fillallCtMethods= " + className + "[" + className + "] times=" + (System.currentTimeMillis() - start));
            Set<String> keySet = fieldMap.keySet();
            for (String fieldName : keySet) {
                ESDField field = fieldMap.get(fieldName);
                if (field != null) {
                    allFieldMap.put(field.getFieldName(), field);
                    if (field.isSerialize()) {
                        if (field.isHidden() || field.isPid()) {
                            if (!hiddenFieldList.contains(field)) {
                                hiddenFieldList.add(field);
                            }
                        }
                    }
                }
            }
        }

    }


    public List<ESDField> getRefFields() {
        List<ESDField> esdFields = new ArrayList<>();

        for (ESDField field : this.getMethodsList()) {
            RefType refType = null;
            CustomRefBean refBean = field.getRefBean();
            if (refBean != null) {
                refType = refBean.getRef();
            }
            if (refType != null && !refType.equals(RefType.NONE)) {
                esdFields.add(field);
            }
        }
        return esdFields;
    }

    public boolean isProxy() {
        if (this.aggregationBean != null && this.aggregationBean.getRootClass() != null) {
            Class rootClass = this.aggregationBean.getRootClass();
            if (rootClass.equals(this.getCtClass()) && rootClass.getName().endsWith("API")) {
                return true;
            }
            if (!rootClass.equals(this.getCtClass()) && !ClassUtility.isAssignableFrom(rootClass, this.getCtClass())) {
                return true;
            }
        }

        return false;
    }

    public CustomMethodInfo getMethodInfo(String name) {
        if (methodMap == null || methodMap.isEmpty()) {
            this.initField();
        }
        CustomMethodInfo esdField = methodMap.get(name);
        if (esdField == null) {
            for (CustomMethodInfo customMethodInfo : this.getMethodsList()) {
                if (customMethodInfo != null && customMethodInfo.getName().equals(name)) {
                    methodMap.put(name, customMethodInfo);
                    return customMethodInfo;
                }
            }

            for (Method method : this.getAllCtMethods()) {
                if (method != null && method.getName().equals(name)) {
                    CustomMethodInfo methodInfo = new CustomMethodInfo(method, this);
                    methodMap.put(name, methodInfo);
                    return methodInfo;
                }
            }
        }


        return esdField;
    }

    public ESDField getField(String name) {
        ESDField esdField = this.getEsdFieldMap().get(name);
        if (esdField == null) {
            name = OODUtil.formatJavaName(name, false);
            esdField = this.getEsdFieldMap().get(name);
        }
        if (esdField == null) {
            for (Method method : this.getAllCtMethods()) {
                CustomMethodInfo customMethodInfo = getMethodInfo(method.getName());
                if (customMethodInfo.getName().equals(name) && customMethodInfo.getInnerMethod() != null && (MethodUtil.isGetMethod(customMethodInfo.getInnerMethod()) || customMethodInfo.isModule())) {
                    esdFieldMap.put(name, customMethodInfo);
                    return customMethodInfo;
                }
            }
        }
        return esdField;
    }

    public ESDClass getTopSourceClass() {
        ESDClass sourceClass = getRootClass();
        if (getRootClass() == null) {
            sourceClass = getSourceClass();
            while (!sourceClass.getSourceClass().equals(sourceClass)) {
                sourceClass = sourceClass.getSourceClass();
            }
        }
        return sourceClass;
    }


    public ESDClass getRootClass() {
        ESDClass rootClass = null;
        if (rootClassName == null) {
            if (aggregationBean != null && aggregationBean.getRootClass() != null && !aggregationBean.getRootClass().equals(Void.class)) {
                rootClassName = aggregationBean.getRootClass().getName();
            }
        }

        if (rootClassName != null && !rootClassName.equals("void")) {
            try {
                if (aggregationBean != null) {
                    rootClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(rootClassName, false);
                }
            } catch (JDSException e) {
                e.printStackTrace();
            }
        } else {
            rootClass = this;
        }
        return rootClass;
    }

    public ESDClass getSourceClass() {
        ESDClass sourceClass = null;
        try {
            if (aggregationBean != null && aggregationBean.getSourceClass() != null) {
                sourceClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(aggregationBean.getSourceClass().getName(), false);
            } else if (repositoryBean != null && repositoryBean.getSourceClass() != null) {
                sourceClass = BuildFactory.getInstance().getClassManager().getRepositoryClass(repositoryBean.getSourceClass().getName(), false);
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }
        if (sourceClass == null) {
            sourceClass = this;
        }

        if (sourceClassName == null) {
            sourceClassName = sourceClass.getClassName();
        }


        return sourceClass;
    }


    public ESDClass getViewServiceClass() {
        ESDClass viewServiceClass = null;
        if (viewServiceClassName == null && viewBean != null && viewBean.getAggClass() != null && !viewBean.getAggClass().equals(Void.class)) {
            this.viewServiceClassName = viewBean.getAggClass().getName();
        }

        if (viewServiceClassName != null && !viewServiceClassName.equals("void")) {
            try {
                viewServiceClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(viewServiceClassName, false);
            } catch (JDSException e) {
                e.printStackTrace();
            }
        } else {
            viewServiceClass = this.getSourceClass();
        }
        return viewServiceClass;
    }

    @JSONField(serialize = false)
    public Set<String> getServiceClassNames() {
        if (serviceClassNames == null) {
            serviceClassNames = new LinkedHashSet<>();
            List<Annotation> viewAnnotaions = DSMAnnotationUtil.getServiceAnnotation(ctClass);
            if (viewAnnotaions != null) {
                for (Annotation annotation : viewAnnotaions) {
                    Class[] serviceClasses = (Class[]) AnnotationUtil.getValues(annotation, "customService");
                    if (serviceClasses != null && serviceClasses.length > 0) {
                        for (Class eClass : serviceClasses) {
                            if (!eClass.equals(Void.class)) {
                                serviceClassNames.add(eClass.getName());
                            }
                        }
                    }
                }
            }
            if (entityBean != null && entityBean.getServiceClass() != null && !entityBean.getServiceClass().equals(Void.class) && !entityBean.getServiceClass().equals(Enum.class)) {
                serviceClassNames.add(entityBean.getServiceClass().getName());
            }
        }
        return serviceClassNames;
    }

    @JSONField(serialize = false)
    public Set<ESDClass> getServiceClass() {
        Set<ESDClass> serviceClassSet = new HashSet<>();
        Set<String> serviceClassNames = this.getServiceClassNames();
        for (String serviceClassName : serviceClassNames) {
            try {
                ESDClass serviceClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(serviceClassName, true);
                if (serviceClass != null) {
                    serviceClassSet.add(serviceClass);
                }
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        serviceClassSet.add(this);
        return serviceClassSet;
    }

    public ESDClass getEntityClass() {
        ESDClass entityClass = null;
        //aggregation
        AggregationBean aggregation = getAggregationBean();
        if (aggregation != null) {
            if (aggregation.getEntityClass() != null && !aggregation.getEntityClass().equals(Void.class)) {
                this.entityClassName = aggregation.getEntityClass().getName();
            } else if (aggregation.getRootClass() != null && !aggregation.getRootClass().equals(Void.class)) {
                this.entityClassName = aggregation.getRootClass().getName();
            }
        }

        if (entityClassName == null) {
            EntityBean entity = this.getRootClass().getEntityBean();
            if (entity != null && entity.getRootClass() != null && !entity.getRootClass().equals(Void.class)) {
                this.entityClassName = entity.getRootClass().getName();
            }
            if (this.getViewBean() != null) {
                ViewBean view = this.getSourceClass().getViewBean();
                if (view != null && view.getEntityClass() != null && !view.getEntityClass().equals(Void.class)) {
                    this.entityClassName = entity.getRootClass().getName();
                }
            }
            if (entityClassName == null && repositoryBean != null) {
                if (repositoryBean.getEntityClass() != null && !repositoryBean.getEntityClass().equals(Void.class)) {
                    this.entityClassName = repositoryBean.getEntityClass().getName();
                }
            }

        }


        if (entityClassName != null && !entityClassName.equals("void")) {
            try {
                entityClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(entityClassName, false);
                this.uid = entityClass.getUid();
            } catch (JDSException e) {
                e.printStackTrace();
            }
        } else {
            entityClass = this;
        }

        return entityClass;
    }

    public ESDField getUidField() {
        if (uidField == null) {
            for (ESDField esdField : this.getAllFieldList()) {
                if (esdField.isUid()) {
                    uidField = esdField;
                }
            }
        }
        if (uidField == null) {
            for (ESDField esdField : this.getMethodsList()) {
                if (esdField.isUid()) {
                    uidField = esdField;
                }
            }
        }
        return uidField;
    }

    public ESDField getCaptionField() {
        if (captionField == null) {
            for (ESDField esdField : this.getAllFieldList()) {
                if (esdField.isCaption()) {
                    captionField = esdField;
                }
            }
        }
        if (captionField == null) {
            for (ESDField esdField : this.getMethodsList()) {
                if (esdField.isCaption()) {
                    captionField = esdField;
                }
            }
        }
        return captionField;
    }


    public List<ESDField> getCustomFieldList() {
        List<ESDField> esdFields = new ArrayList<>();
        for (ESDField esdField : this.getAllFieldList()) {
            if (esdField.getRefBean() == null || esdField.getRefBean().getRef() == null) {
                esdFields.add(esdField);
            }
        }
        return esdFields;
    }

    public void setHiddenFieldList(List<ESDField> hiddenFieldList) {
        this.hiddenFieldList = hiddenFieldList;
    }

    public CustomDomain getCustomDomain() {
        return customDomain;
    }

    public void setCustomDomain(CustomDomain customDomain) {
        this.customDomain = customDomain;
    }

    public List<ESDField> getDisableFieldList() {

        if (disableFieldList == null) {
            this.initField();
        }
        return disableFieldList;
    }

    public void setDisableFieldList(List<ESDField> disableFieldList) {
        this.disableFieldList = disableFieldList;
    }

    public List<ESDField> getHiddenFieldList() {
        if (hiddenFieldList == null) {
            this.initField();
        }
        return hiddenFieldList;
    }

    public List<CustomMethodInfo> getOtherMethodsList() {
        return otherMethodsList;
    }

    public void setOtherMethodsList(List<CustomMethodInfo> otherMethodsList) {
        this.otherMethodsList = otherMethodsList;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getSourceClassName() {
        return sourceClassName;
    }

    public void setSourceClassName(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }

    public String getViewServiceClassName() {
        return viewServiceClassName;
    }

    public void setViewServiceClassName(String viewServiceClassName) {
        this.viewServiceClassName = viewServiceClassName;
    }


    public String getDomainId() {
        String domainId = null;
        try {
            String projectName = DSMFactory.getInstance().getDefaultProjectName();
            DomainInst domainInst = DSMFactory.getInstance().getDefaultDomain(projectName, UserSpace.VIEW);
            domainId = domainInst.getDomainId();
            if (aggregationBean != null) {
                if (aggregationBean.getDomainId() != null) {
                    domainId = aggregationBean.getDomainId();
                } else if (aggregationBean.getUserSpace() != null && aggregationBean.getUserSpace().size() > 0) {
                    UserSpace userSpace = aggregationBean.getUserSpace().iterator().next();
                    domainInst = DSMFactory.getInstance().getDefaultDomain(projectName, userSpace);
                    domainId = domainInst.getDomainId();
                }
            }

        } catch (JDSException e) {
            e.printStackTrace();
        }

        return domainId;
    }


    public static String[] getCustomClassName() {
        return customClassName;
    }

    public Class getCtClass() {
        return ctClass;
    }

    public void setCtClass(Class ctClass) {
        this.ctClass = ctClass;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<ESDField> getFieldList() {
        List<ESDField> esdFields = new ArrayList<>();
        for (String fieldName : getFieldNameList()) {
            ESDField esdField = this.getField(fieldName);
            if (esdField != null) {
                esdFields.add(esdField);
            }
        }
        return esdFields;

    }


    public List<CustomMethodInfo> getMethodsList() {
        if (methodsList.isEmpty()) {
            this.initField();
        }

        return methodsList;
    }

    public CustomMethodInfo getCustomMethodByName(String methodName) {
        CustomMethodInfo customMethodInfo = null;
        for (CustomMethodInfo methodInfo : getMethodsList()) {
            if (methodInfo.getMethodName().equals(methodName)) {
                customMethodInfo = methodInfo;
            }
        }
        return customMethodInfo;
    }

    public List<String> getFieldNameList() {
        if (fieldNameList == null || fieldNameList.isEmpty()) {
            this.initField();
        }
        return fieldNameList;
    }

    public void setFieldNameList(List<String> fieldNameList) {
        this.fieldNameList = fieldNameList;
    }

    public void setMethodsList(List<CustomMethodInfo> methodsList) {
        this.methodsList = methodsList;
    }

    public Map<String, ESDField> getAllFieldMap() {
        if (allFieldMap == null || allFieldMap.isEmpty()) {
            this.initField();
        }
        return allFieldMap;
    }

    public void setAllFieldMap(Map<String, ESDField> allFieldMap) {
        this.allFieldMap = allFieldMap;
    }

    private List<ESDField> getAllFieldList() {
        List<ESDField> esdFields = new ArrayList<>();
        for (Map.Entry<String, ESDField> entry : this.getAllFieldMap().entrySet()) {
            esdFields.add(entry.getValue());
        }
        return esdFields;

    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public ArrayList<Field> getAllCtFields() {
        return allCtFields;
    }

    public void setAllCtFields(ArrayList<Field> allCtFields) {
        this.allCtFields = allCtFields;
    }

    public ArrayList<Method> getAllCtMethods() {
        return allCtMethods;
    }

    public void setAllCtMethods(ArrayList<Method> allCtMethods) {
        this.allCtMethods = allCtMethods;
    }

    public void setCaptionField(ESDField captionField) {
        this.captionField = captionField;
    }

    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }


    public RequestMappingBean getRequestMappingBean() {
        return requestMappingBean;
    }

    public void setRequestMappingBean(RequestMappingBean requestMappingBean) {
        this.requestMappingBean = requestMappingBean;
    }

    public void setServiceClassNames(Set<String> serviceClassNames) {
        this.serviceClassNames = serviceClassNames;
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

    public ViewBean getViewBean() {
        return viewBean;
    }

    public void setViewBean(ViewBean viewBean) {
        this.viewBean = viewBean;
    }

    public String getEntityClassName() {
        return entityClassName;
    }

    public void setEntityClassName(String entityClassName) {
        this.entityClassName = entityClassName;
    }

    public MenuBarBean getMenuBarBean() {
        return menuBarBean;
    }

    public void setMenuBarBean(MenuBarBean menuBarBean) {
        this.menuBarBean = menuBarBean;
    }

    public Map<String, ESDField> getEsdFieldMap() {
        if (esdFieldMap == null || esdFieldMap.isEmpty()) {
            this.initField();
        }
        return esdFieldMap;
    }

    public void setEsdFieldMap(Map<String, ESDField> esdFieldMap) {
        this.esdFieldMap = esdFieldMap;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof ESDClass) {
            if (((ESDClass) obj).getClassName() != null && this.getClassName() != null) {
                return ((ESDClass) obj).getClassName().equals(this.getClassName());
            }
        }

        return super.equals(obj);
    }
}
