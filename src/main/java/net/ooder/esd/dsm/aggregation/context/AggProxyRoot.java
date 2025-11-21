package net.ooder.esd.dsm.aggregation.context;

import com.alibaba.fastjson.JSONObject;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.Ref;
import net.ooder.common.util.ClassUtility;
import net.ooder.config.ListResultModel;
import net.ooder.config.ResultModel;
import net.ooder.config.TreeListResultModel;
import net.ooder.annotation.CustomBean;
import net.ooder.annotation.MethodChinaName;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.ModuleAnnotation;
import net.ooder.esd.annotation.View;
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
import net.ooder.esd.annotation.view.GridViewAnnotation;
import net.ooder.esd.bean.CustomData;
import net.ooder.esd.bean.data.CustomDataBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.custom.CustomMethodInfo;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.JavaRoot;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.aggregation.FieldAggConfig;
import net.ooder.esd.dsm.aggregation.ref.AggEntityRefProxy;
import net.ooder.esd.dsm.java.JavaPackage;
import net.ooder.esd.tool.component.Component;

import net.ooder.jds.core.esb.EsbUtil;
import net.ooder.web.AggregationBean;
import net.ooder.web.util.MethodUtil;
import net.ooder.web.util.PageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;


public class AggProxyRoot<K extends CustomData> implements JavaRoot {

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
            GridViewAnnotation.class,
            MethodChinaName.class,
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

    public ApiClassConfig apiClassConfig;

    public K data;


    public AggProxyRoot(DomainInst domainInst, AggEntityConfig appEntity) {
        this.domainInst = domainInst;
        try {
            this.apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(appEntity.getCurrClassName());
            this.esdClass = appEntity.getEntityClass();
            this.url = apiClassConfig.getUrl();
            List<MethodConfig> allMethods = apiClassConfig.getAllMethods();
            config = JSONObject.parseObject(JSONObject.toJSONString(appEntity), AggEntityConfig.class);
            config.getAllMethodMap().putAll(apiClassConfig.getAllMethodMap());
            config.getAggregationBean().setRootClass(config.getRootClass().getCtClass());
            config.setUrl(url);

            this.moduleName = config.getAggregationBean().getModuleName();
            this.space = domainInst.getSpace();
            this.allRefs = new ArrayList<>();
            this.allFields = config.getFieldList();
            this.basepath = domainInst.getPackageName();
            this.className = esdClass.getCtClass().getName();
            this.packageName = this.getEuPackageName();
            this.allMethods = esdClass.getMethodsList();

            for (MethodConfig methodConfig : config.getAllMethods()) {
                if (methodConfig.isModule()) {
                    methodConfig.setParentEuPackage(this.getEuPackageName());
                }
            }

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


            AggregationBean aggregationBean = esdClass.getAggregationBean();
            if (aggregationBean != null) {
                if (aggregationBean.getRootClass() != null) {
                    imports.add(aggregationBean.getRootClass().getPackage().getName() + ".*");
                }
                if (aggregationBean.getSourceClass() != null) {
                    imports.add(aggregationBean.getSourceClass().getPackage().getName() + ".*");
                }
                if (aggregationBean.getEntityClass() != null) {
                    imports.add(aggregationBean.getEntityClass().getPackage().getName() + ".*");
                }
            }

            imports = MethodUtil.getAllImports(esdClass.getCtClass(), imports);
            for (Class clazz : customClass) {
                imports.add(clazz.getPackage().getName() + ".*");
                if (clazz.isAnnotation()) {
                    imports = MethodUtil.getAllImports(clazz, imports);
                }
            }

            String viewPackage = domainInst.getSpace();
            String domainPackage = domainInst.getPackageName();
            String repositoryPackage = domainInst.getRepositoryInst().getPackageName();
            if (moduleName == null || moduleName.equals("")) {
                if (packageName.startsWith(viewPackage.length() + ".")) {
                    moduleName = packageName.substring(viewPackage.length() + 1);
                }
            }

            if (moduleName != null && !moduleName.equals("")) {
                viewPackage =  domainInst.getEuPackage()  + "." + moduleName.toLowerCase();
                domainPackage = domainInst.getPackageName() + "." + moduleName.toLowerCase();
                repositoryPackage = domainInst.getRepositoryInst().getPackageName() + "." + moduleName.toLowerCase();
            }

            Map<String, Class> classMap = new HashMap<>();
            classMap.putAll(ClassUtility.getDynClassMap());
            classMap.putAll(ClassUtility.getFileClassMap());

            String[] basePacks = new String[]{viewPackage, domainPackage, repositoryPackage};

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

            String euPackageName = this.getEuPackageName();
            JavaPackage javaPackage = domainInst.getPackageByName(euPackageName);
            if (javaPackage != null && javaPackage.listAllFile().size() > 0) {
                this.imports.add(euPackageName + ".*");
            }

            if (Package.getPackage(euPackageName) != null) {
                this.imports.add(euPackageName + ".*");
            } else {
                for (Package pack : ClassUtility.getAllDynPacks()) {
                    if (pack.getName().startsWith(euPackageName)) {
                        this.imports.add(pack.getName() + ".*");
                    }
                }
            }

            if (imports.contains(Component.class.getPackage().getName() + ".*")
                    && imports.contains("org.springframework.stereotype.*")) {
                imports.add(Component.class.getName());
            }

            imports.remove(this.getPackageName() + ".*");
            imports.remove(viewPackage + ".*");
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
            euPackageName = url;
            euPackageName = euPackageName.replace("/", ".");
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
