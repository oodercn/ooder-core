package net.ooder.esd.dsm.aggregation.context;

import com.alibaba.fastjson.JSONObject;
import net.ooder.common.JDSException;
import net.ooder.common.util.ClassUtility;
import net.ooder.config.ListResultModel;
import net.ooder.config.ResultModel;
import net.ooder.config.TreeListResultModel;
import net.ooder.annotation.CustomBean;
import net.ooder.annotation.MethodChinaName;
import net.ooder.esd.annotation.*;
import net.ooder.esd.annotation.event.CustomCallBack;
import net.ooder.esd.annotation.event.CustomTabsEvent;
import net.ooder.esd.annotation.event.CustomTreeEvent;
import net.ooder.esd.annotation.fchart.ICategorie;
import net.ooder.esd.annotation.fchart.ILineList;
import net.ooder.esd.annotation.fchart.IRawData;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.field.ToolBarMenu;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.annotation.ui.UIType;
import net.ooder.esd.annotation.view.*;
import net.ooder.esd.bean.CustomData;
import net.ooder.esd.bean.data.CustomDataBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.custom.CustomMethodInfo;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.JavaRoot;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.aggregation.FieldAggConfig;
import net.ooder.esd.dsm.aggregation.ref.AggEntityRef;
import net.ooder.esd.dsm.aggregation.ref.AggEntityRefProxy;
import net.ooder.esd.dsm.temp.JavaTemp;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.properties.item.LayoutListItem;

import net.ooder.jds.core.esb.EsbUtil;
import net.ooder.web.AggregationBean;
import net.ooder.web.RequestMappingBean;
import net.ooder.annotation.*;
import net.ooder.web.util.MethodUtil;
import net.ooder.web.util.PageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;


public class AggEntityRoot<K extends CustomData> implements JavaRoot {

    public String euPackageName;

    public String packageName;

    public String className;

    public String moduleName;

    public String rootId = "00000000-0000-0000-0000-000000000000";

    public String basepath;

    public String url;

    public String space;

    public String cnName;

    public DomainInst domainInst;

    public ESDClass esdClass;


    public Set<String> imports = new LinkedHashSet<>();

    public List<AggEntityRefProxy> allRefs = new ArrayList<>();

    public List<AggEntityRefProxy> refs = new ArrayList<>();

    public List<AggEntityRefProxy> f2fs = new ArrayList<>();

    public List<AggEntityRefProxy> o2os = new ArrayList<>();

    public List<AggEntityRefProxy> dics = new ArrayList<>();

    public List<AggEntityRefProxy> o2ms = new ArrayList<>();

    public List<AggEntityRefProxy> m2os = new ArrayList<>();

    public List<AggEntityRefProxy> m2ms = new ArrayList<>();

    private List<FieldAggConfig> allFields = new ArrayList<>();

    private List<FieldAggConfig> moduleFields = new ArrayList<>();

    private List<CustomMethodInfo> allMethods = new ArrayList<>();

    private Set<FieldAggConfig> customFields = new LinkedHashSet<FieldAggConfig>();

    private Set<FieldAggConfig> hiddenFields = new LinkedHashSet<FieldAggConfig>();

    private Set<FieldAggConfig> refFields = new LinkedHashSet<FieldAggConfig>();

    private Set<FieldAggConfig> pkFields = new LinkedHashSet<FieldAggConfig>();

    private Set<FieldAggConfig> fkFields = new LinkedHashSet<FieldAggConfig>();

    private static final Class[] customClass = new Class[]{
            PageUtil.class,
            EsbUtil.class,
            List.class,
            ResultModel.class,
            ListResultModel.class,
            TreeListResultModel.class,
            CustomMenuItem.class,
            CustomCallBack.class,
            UIType.class,
            IRawData.class,
            ILineList.class,
            ICategorie.class,
            View.class,
            LayoutViewAnnotation.class,
            LayoutListItem.class,
            ButtonViewsViewAnnotation.class,
            GridViewAnnotation.class,
            MethodChinaName.class,
            FormAnnotation.class,
            GridAnnotation.class,
            TreeAnnotation.class,
            TabsViewAnnotation.class,
            NavTreeViewAnnotation.class,
            FormViewAnnotation.class,
            TreeViewAnnotation.class,
            GalleryViewAnnotation.class,
            Controller.class,
            CustomTabsEvent.class,
            CustomTreeEvent.class,
            ModuleAnnotation.class,
            APIEventAnnotation.class,
            Ref.class,
            ToolBarMenu.class,
            CustomAnnotation.class,
            RequestMapping.class,
            ResponseBody.class};

    public AggEntityConfig config;

    //  public ApiClassConfig apiClassConfig;

    public K data;


    public AggEntityRoot(DomainInst domainInst, AggEntityConfig appEntity, List<AggEntityRef> dsmRefs) {
        try {
            this.domainInst = domainInst;
            config = JSONObject.parseObject(JSONObject.toJSONString(appEntity), AggEntityConfig.class);
            this.esdClass = appEntity.getEntityClass();
            this.url = appEntity.getUrl();

            if (appEntity.getESDClass().isProxy()) {
                ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(appEntity.getCurrClassName(), true);
                this.url = apiClassConfig.getUrl();
                config.getAllMethodMap().putAll(apiClassConfig.getAllMethodMap());
                List<MethodConfig> allMethods = apiClassConfig.getAllMethods();
                for (MethodConfig methodConfig : allMethods) {
                    CustomDataBean dataBean = methodConfig.getDataBean();
                    if (dataBean != null) {
                        List<CustomBean> annotationBeans = dataBean.getAnnotationBeans();
                        for (CustomBean annotationBean : annotationBeans) {
                            AnnotationType annotationType = annotationBean.getClass().getAnnotation(AnnotationType.class);
                            if (annotationType != null && annotationType.clazz() != null && !annotationType.clazz().equals(Void.class) && !annotationType.clazz().equals(Enum.class)) {
                                imports.add(annotationType.clazz().getName());
                            }
                        }
                    }
                }
            }

            if (url.startsWith("/")) {
                url = url.substring(1);
            }
            String euClassName = url.replace("/", ".");
            config.getAggregationBean().setRootClass(config.getRootClass().getCtClass());
            config.getAggregationBean().setEntityClass(esdClass.getCtClass());
            config.getAggregationBean().setDomainId(domainInst.getDomainId());
            config.getAggregationBean().getUserSpace().clear();
            config.getAggregationBean().addUserSpace(domainInst.getUserSpace());
            String realModuleName = config.getAggregationBean().getModuleName();
            if (realModuleName != null && !realModuleName.toLowerCase().equals(esdClass.getName().toLowerCase())) {
                this.moduleName = realModuleName;
            }
            if (moduleName == null || moduleName.equals("")) {
                if (!euClassName.equals(domainInst.getEuPackage())) {
                    if (euClassName.indexOf(".") > -1) {
                        moduleName = euClassName.substring(0, euClassName.lastIndexOf(".")).toLowerCase();
                        while (moduleName.startsWith(domainInst.getEuPackage() + ".")) {
                            moduleName = moduleName.substring(domainInst.getEuPackage().length() + 1);
                        }
                    } else {
                        moduleName = euClassName.toLowerCase();
                    }
                }
                appEntity.getAggregationBean().setModuleName(moduleName.toLowerCase());
                config.getAggregationBean().setModuleName(moduleName.toLowerCase());
            } else {
                moduleName = moduleName.toLowerCase();
            }

            ESDClass soruceClass = config.getSourceClass().getEntityClass();
            String viewPackage = domainInst.getEuPackage();
            String domainPackage = domainInst.getPackageName();
            String repositoryPackage = domainInst.getRepositoryInst().getPackageName();
            String entityName = soruceClass.getName().toLowerCase();
            RequestMappingBean requestMappingBean = config.getRequestMappingBean();
            String value = requestMappingBean.getFristUrl();
            space = domainInst.getSpace();
            if (space.startsWith("/")) {
                space = space.substring(1);
            }

            String baseUrl = domainInst.getProjectVersionName() + "/" + space;
            if (!value.startsWith("/" + baseUrl + "/") && !value.equals("/" + baseUrl + "/")) {
                if (value.startsWith("/")) {
                    value = value.substring(1);
                }
                if (value.endsWith("/")) {
                    value = value.substring(value.length() - 1);
                }
                url = "/" + baseUrl;
                if (!value.equals(baseUrl)) {
                    url = url + "/" + value;
                }
            }


            if (moduleName != null && !moduleName.equals("")) {
                String basePackage = domainInst.getProjectVersionName() + "." + space;
                if (moduleName.startsWith(basePackage + ".")) {
                    moduleName = moduleName.substring((basePackage + ".").length()).toLowerCase();
                }
                if (!moduleName.equals(basePackage)) {
                    viewPackage = basePackage + "." + moduleName.toLowerCase();
                    domainPackage = domainInst.getPackageName() + "." + moduleName.toLowerCase();
                    repositoryPackage = domainInst.getRepositoryInst().getPackageName() + "." + moduleName.toLowerCase();
                    String moduleUrl = moduleName.toLowerCase();
                    if (appEntity.getEntityClass().isProxy()) {
                        if (moduleUrl.indexOf(".") > -1) {
                            moduleUrl = moduleUrl.substring(0, moduleUrl.indexOf("."));
                        }
                    }

                    url = basePackage + "/" + moduleUrl;
                    url = url.replace(".", "/");
                    if (!url.endsWith("/")) {
                        url = url + "/";
                    }
                    if (!url.startsWith("/")) {
                        url = "/" + url;
                    }
                }

                if (!moduleName.equals(entityName.toLowerCase()) && !moduleName.endsWith("." + entityName.toLowerCase())) {
                    viewPackage = viewPackage + "." + entityName.toLowerCase();
                    domainPackage = domainPackage + "." + entityName.toLowerCase();
                    repositoryPackage = repositoryPackage + "." + entityName.toLowerCase();
                }
            }

            config.setUrl(url);

            for (MethodConfig methodConfig : config.getAllMethods()) {
                if (methodConfig.isModule()) {
                    methodConfig.setParentEuPackage(this.getEuPackageName());
                }
            }


            this.space = domainInst.getSpace();
            this.allRefs = new ArrayList<>();
            this.allFields = config.getFieldList();
            this.basepath = domainInst.getPackageName();

            this.className = esdClass.getCtClass().getName();
            this.allMethods = esdClass.getMethodsList();
            AggregationBean aggregationBean = esdClass.getAggregationBean();
            if (aggregationBean != null) {
                if (aggregationBean.getRootClass() != null) {
                    imports.add(aggregationBean.getRootClass().getName());
                }
                if (aggregationBean.getSourceClass() != null) {
                    imports.add(aggregationBean.getSourceClass().getName());
                }
                if (aggregationBean.getEntityClass() != null) {
                    imports.add(aggregationBean.getEntityClass().getName());
                }
            }

            imports = MethodUtil.getAllImports(esdClass.getCtClass(), imports);
            for (Class clazz : customClass) {
                imports.add(clazz.getName());
                if (clazz.isAnnotation()) {
                    imports = MethodUtil.getAllImports(clazz, imports);
                }
            }


            Map<String, Class> classMap = new HashMap<>();
            classMap.putAll(ClassUtility.getDynClassMap());
            classMap.putAll(ClassUtility.getFileClassMap());
            String[] basePacks = new String[]{domainPackage, repositoryPackage};

            for (String basePackage : basePacks) {
                if (Package.getPackage(basePackage) != null) {
                    this.imports.add(basePackage + ".*");
                }

                for (String innerPack : innerPacks) {
                    String innerPackageName = basePackage + "." + innerPack;
                    if (Package.getPackage(innerPackageName) != null) {
                        this.imports.add(innerPackageName + ".*");
                    }

                    for (Package pack : ClassUtility.getAllDynPacks()) {
                        if (pack.getName().startsWith(basePackage + "." + innerPack)) {
                            this.imports.add(pack.getName() + ".*");
                        }
                    }
                }

                Set<Map.Entry<String, Class>> dynClassSet = classMap.entrySet();
                for (Map.Entry<String, Class> classEntry : dynClassSet) {
                    Class dynClass = classEntry.getValue();
                    if (dynClass.getPackage().getName().startsWith(basePackage)) {
                        this.imports.add(dynClass.getPackage().getName() + ".*");
                    }
                }
            }


            try {
                boolean hasRepository = false;
                for (String pName : this.imports) {
                    if (pName.startsWith(repositoryPackage)) {
                        hasRepository = true;
                    }
                }
                if (hasRepository) {
                    Set<String> javaTempIds = domainInst.getJavaTempIds();
                    for (String javaTempId : javaTempIds) {
                        JavaTemp javaTemp = BuildFactory.getInstance().getTempManager().getJavaTempById(javaTempId);
                        if (javaTemp != null && javaTemp.getAggregationType() != null &&
                                javaTemp.getAggregationType().equals(AggregationType.ENTITY)) {
                            if (javaTemp.getPackagePostfix() == null || javaTemp.getPackagePostfix().equals("..")) {
                                this.imports.add(domainPackage + ".*");
                            } else {
                                this.imports.add(domainPackage + "." + javaTemp.getPackagePostfix() + ".*");
                            }
                        }
                    }
                }

                if (imports.contains(Component.class.getPackage().getName() + ".*")
                        && imports.contains("org.springframework.stereotype.*")) {
                    imports.add(Component.class.getName());
                }
            } catch (JDSException e) {
                e.printStackTrace();
            }

            imports.remove(this.getEuPackageName() + ".*");
            imports.remove(viewPackage + ".*");

            if (dsmRefs == null) {
                dsmRefs = new ArrayList<>();
            }


            if (dsmRefs.isEmpty()) {
                dsmRefs.addAll(config.getRefs());
            }


            for (AggEntityRef dsmRef : dsmRefs) {
                AggEntityRefProxy dsmRefProxy = new AggEntityRefProxy(dsmRef, domainInst.getDomainId());
                if (dsmRefProxy.getPk() != null) {
                    allRefs.add(dsmRefProxy);
                    if (dsmRefProxy.getField() != null) {
                        refFields.add(dsmRefProxy.getField());
                    }
                    FieldAggConfig fieldProxy = config.getFieldByName(dsmRefProxy.getMethodConfig().getFieldName());
                    if (fieldProxy != null) {
                        switch (dsmRef.getRefBean().getRef()) {
                            case F2F:
                                this.f2fs.add(dsmRefProxy);
                                addFkField(fieldProxy);
                                break;
                            case REF:
                                this.refs.add(dsmRefProxy);
                                break;
                            case M2M:
                                this.m2ms.add(dsmRefProxy);
                                this.moduleFields.add(fieldProxy);
                                break;
                            case O2M:
                                o2ms.add(dsmRefProxy);
                                this.moduleFields.add(fieldProxy);
                                break;
                            case O2O:
                                this.o2os.add(dsmRefProxy);
                                this.moduleFields.add(fieldProxy);
                                break;
                            case M2O:
                                addFkField(fieldProxy);
                                this.m2os.add(dsmRefProxy);
                                break;
                        }
                    }

                }

            }


            for (FieldAggConfig field : config.getFieldList()) {
                if (esdClass.getUid() != null) {
                    if (field.getFieldname().equals(esdClass.getUid())) {
                        this.pkFields.add(field);
                    }
                }
            }

            for (FieldAggConfig colProxy : config.getFieldList()) {
                if (!hasCols(colProxy, fkFields)
                        && !hasCols(colProxy, hiddenFields)
                        && !hasCols(colProxy, pkFields)
                        && !hasCols(colProxy, refFields)
                        ) {
                    this.customFields.add(colProxy);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    void addFkField(FieldAggConfig colProxy) {
        fkFields.add(colProxy);
    }

    boolean hasCols(FieldAggConfig colProxy, Set<FieldAggConfig> cols) {
        for (FieldAggConfig customCol : cols) {
            if (customCol.getFieldname().toLowerCase().equals(colProxy.getFieldname().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public DomainInst getDomainInst() {
        return domainInst;
    }

    public void setDomainInst(DomainInst domainInst) {
        this.domainInst = domainInst;
    }

    public AggEntityConfig getConfig() {
        return config;
    }

    public void setConfig(AggEntityConfig config) {
        this.config = config;
    }

    public Set<String> getImports() {
        return imports;
    }

    public void setImports(Set<String> imports) {
        this.imports = imports;
    }

    public String getRootId() {
        return rootId;
    }

    public void setRootId(String rootId) {
        this.rootId = rootId;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public String getBasepath() {
        return basepath;
    }

    public void setBasepath(String basepath) {
        this.basepath = basepath;
    }

    public List<AggEntityRefProxy> getAllRefs() {
        return allRefs;
    }

    public void setAllRefs(List<AggEntityRefProxy> allRefs) {
        this.allRefs = allRefs;
    }

    public List<AggEntityRefProxy> getDics() {
        return dics;
    }

    public void setDics(List<AggEntityRefProxy> dics) {
        this.dics = dics;
    }

    public List<FieldAggConfig> getModuleFields() {
        return moduleFields;
    }

    public void setModuleFields(List<FieldAggConfig> moduleFields) {
        this.moduleFields = moduleFields;
    }

    public List<CustomMethodInfo> getAllMethods() {
        return allMethods;
    }

    public void setAllMethods(List<CustomMethodInfo> allMethods) {
        this.allMethods = allMethods;
    }


    public String getEuPackageName() {
        if (euPackageName == null && this.url != null) {
            euPackageName = url.replace("/", ".");
            if (euPackageName.startsWith(".")) {
                euPackageName = euPackageName.substring(1);
            }
            if (euPackageName.endsWith(".")) {
                euPackageName = euPackageName.substring(0, euPackageName.length() - 1);
            }
        }
        return euPackageName;
    }

    public void setEuPackageName(String euPackageName) {
        this.euPackageName = euPackageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {

        return packageName;
    }


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public List<AggEntityRefProxy> getRefs() {
        return refs;
    }

    public void setRefs(List<AggEntityRefProxy> refs) {
        this.refs = refs;
    }

    public List<AggEntityRefProxy> getF2fs() {
        return f2fs;
    }

    public void setF2fs(List<AggEntityRefProxy> f2fs) {
        this.f2fs = f2fs;
    }

    public List<AggEntityRefProxy> getO2os() {
        return o2os;
    }

    public void setO2os(List<AggEntityRefProxy> o2os) {
        this.o2os = o2os;
    }

    public List<AggEntityRefProxy> getO2ms() {
        return o2ms;
    }

    public void setO2ms(List<AggEntityRefProxy> o2ms) {
        this.o2ms = o2ms;
    }

    public List<AggEntityRefProxy> getM2os() {
        return m2os;
    }

    public void setM2os(List<AggEntityRefProxy> m2os) {
        this.m2os = m2os;
    }

    public List<AggEntityRefProxy> getM2ms() {
        return m2ms;
    }

    public void setM2ms(List<AggEntityRefProxy> m2ms) {
        this.m2ms = m2ms;
    }

    public ESDClass getEsdClass() {
        return esdClass;
    }

    public void setEsdClass(ESDClass esdClass) {
        this.esdClass = esdClass;
    }

    public List<FieldAggConfig> getAllFields() {
        return allFields;
    }

    public void setAllFields(List<FieldAggConfig> allFields) {
        this.allFields = allFields;
    }

    public Set<FieldAggConfig> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(Set<FieldAggConfig> customFields) {
        this.customFields = customFields;
    }

    public Set<FieldAggConfig> getHiddenFields() {
        return hiddenFields;
    }

    public void setHiddenFields(Set<FieldAggConfig> hiddenFields) {
        this.hiddenFields = hiddenFields;
    }

    public Set<FieldAggConfig> getRefFields() {
        return refFields;
    }

    public void setRefFields(Set<FieldAggConfig> refFields) {
        this.refFields = refFields;
    }

    public Set<FieldAggConfig> getPkFields() {
        return pkFields;
    }

    public void setPkFields(Set<FieldAggConfig> pkFields) {
        this.pkFields = pkFields;
    }

    public Set<FieldAggConfig> getFkFields() {
        return fkFields;
    }

    public void setFkFields(Set<FieldAggConfig> fkFields) {
        this.fkFields = fkFields;
    }

    public K getData() {
        return data;
    }

    public void setData(K data) {
        this.data = data;
    }
}
