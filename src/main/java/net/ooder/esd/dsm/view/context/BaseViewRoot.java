package net.ooder.esd.dsm.view.context;

import net.ooder.annotation.*;
import net.ooder.common.JDSException;
import net.ooder.common.util.ClassUtility;
import net.ooder.common.util.StringUtility;
import net.ooder.config.ListResultModel;
import net.ooder.config.ResultModel;
import net.ooder.esd.annotation.GridAnnotation;
import net.ooder.esd.annotation.ModuleAnnotation;
import net.ooder.esd.annotation.ViewType;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.field.ToolBarMenu;
import net.ooder.esd.annotation.view.GridViewAnnotation;
import net.ooder.esd.bean.CustomViewBean;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.DSMInst;
import net.ooder.esd.dsm.JavaRoot;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.aggregation.context.AggViewRoot;
import net.ooder.esd.dsm.repository.RepositoryInst;
import net.ooder.esd.dsm.view.ViewInst;
import net.ooder.esd.util.ESDEnumsUtil;
import net.ooder.esd.util.OODUtil;
import net.ooder.web.AggregationBean;
import net.ooder.web.util.MethodUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.*;

public abstract class BaseViewRoot<T extends CustomViewBean> implements JavaRoot {


    private final Class[] baseClass = new Class[]{
            Controller.class,
            ModuleAnnotation.class,
            ToolBarMenu.class,
            ResultModel.class,
            GridViewAnnotation.class,
            ListResultModel.class,
            JDSException.class,
            APIEventAnnotation.class,
            EsbBeanAnnotation.class,
            ResponseBody.class,
            Date.class,
            LocalDate.class,
            ESDEnumsUtil.class,
            IconEnumstype.class,
            JDSException.class,
            MethodChinaName.class,
            GridAnnotation.class
    };


    private String moduleName;

    private AggViewRoot viewRoot;

    public String packageName;

    public String className;

    public Class serviceClass;

    public String serviceClassName;

    public String cnName;

    public String uidFiled;

    public DSMInst dsmBean;

    public String space;

    public String baseUrl;

    public String simClassName;

    public T viewBean;

    public Set<String> imports = new LinkedHashSet<>();

    RepositoryInst repositoryInst;

    AggEntityConfig aggEntityConfig;

    public BaseViewRoot() {

    }


    public BaseViewRoot(AggViewRoot viewRoot, T viewBean, String moduleName, String fullClassName) {
        super();
        this.viewRoot = viewRoot;
        this.viewBean = viewBean;
        this.dsmBean = viewRoot.getDsmBean();
        this.repositoryInst = viewRoot.getDsmBean().getRepositoryInst();


        this.moduleName = moduleName;
        this.cnName = dsmBean.getDesc();
        this.space = dsmBean.getSpace();
        if (viewBean.getUidField() != null) {
            this.uidFiled = viewBean.getUidField().getFieldname();
        }

        if (fullClassName.indexOf(".") == -1) {
            fullClassName = dsmBean.getRootPackage().getPackageName() + "." + fullClassName;
        }
        this.packageName = fullClassName.substring(0, fullClassName.lastIndexOf("."));
        this.simClassName = OODUtil.formatJavaName(fullClassName.substring(fullClassName.lastIndexOf(".") + 1), true);
        this.className = simClassName;
        List<ESDClass> esdClasses = repositoryInst.getAggBeans(UserSpace.VIEW, AggregationType.ENTITY);
        for (ESDClass esdClass : esdClasses) {
            Class entityClass = esdClass.getAggregationBean().getEntityClass();
            if (entityClass != null && entityClass.getSimpleName().equals(simClassName)) {
                serviceClass = esdClass.getCtClass();
                imports.add(serviceClass.getPackage().getName() + ".*");
                serviceClassName = serviceClass.getSimpleName();
            }
        }

        this.baseUrl = StringUtility.replace(packageName, ".", "/");
        try {
            aggEntityConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(viewBean.getSourceClassName(), true);
            if (aggEntityConfig != null) {
                AggregationBean aggregationBean = aggEntityConfig.getAggregationBean();
                if (aggregationBean == null) {
                    aggregationBean = new AggregationBean();
                    aggregationBean.setType(AggregationType.ENTITY);
                    aggregationBean.setModuleName(this.moduleName);
                    aggEntityConfig.setAggregationBean(aggregationBean);
                } else {
                    if (aggregationBean.getRootClass() != null && !aggregationBean.getRootClass().equals(Void.class)) {
                        imports.add(aggregationBean.getRootClass().getName());
                    }
                    if (aggregationBean.getSourceClass() != null && !aggregationBean.getSourceClass().equals(Void.class)) {
                        imports.add(aggregationBean.getSourceClass().getName());
                    }
                    if (aggregationBean.getEntityClass() != null && !aggregationBean.getEntityClass().equals(Void.class)) {
                        imports.add(aggregationBean.getEntityClass().getName());
                    }
                }
                aggEntityConfig.getRequestMappingBean().reSetUrl(this.baseUrl);
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }

        Set<Class> classes = new HashSet<>();

        if (this.getCustomClass() != null && this.getCustomClass().length > 0) {
            classes.addAll(Arrays.asList(this.getCustomClass()));
        }
        Set<Class> annClassSet = viewBean.getOtherClass();
        classes.addAll(annClassSet);
        classes.addAll(Arrays.asList(baseClass));
        for (Class clazz : annClassSet) {
            if (clazz != null) {
                try {
                    AnnotationType annotationType = (AnnotationType) clazz.getAnnotation(AnnotationType.class);
                    if (annotationType != null && annotationType.clazz() != null && !annotationType.clazz().equals(Void.class) && !annotationType.clazz().equals(Enum.class)) {
                        classes.add(annotationType.clazz());
                    }
                } catch (Throwable e) {

                }
            }
        }
        this.imports.addAll(getPackageClassImpls());
        this.imports.addAll(getCustomClassImpls(classes));
        this.imports.addAll(getRepositoryImpls());
        this.imports.remove(packageName + ".*");

    }


    public abstract Class[] getCustomClass();

    public abstract ViewType getViewType();

    protected Set<String> getCustomClassImpls(Set<Class> classes) {
        Set<String> imports = new HashSet<>();
        if (classes != null) {
            for (Class clazz : classes) {
                if (clazz != null) {
                    try {
                        imports.add(clazz.getPackage().getName() + ".*");
                        if (clazz.isAnnotation()) {
                            imports = MethodUtil.getAllImports(clazz, imports);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return imports;
    }

    public String getUidFiled() {
        return uidFiled;
    }

    public void setUidFiled(String uidFiled) {
        this.uidFiled = uidFiled;
    }

    protected Set<String> getPackageClassImpls() {
        Set<String> imports = new HashSet<>();
        String basePackage = dsmBean.getPackageName();
        for (Package pack : Package.getPackages()) {
            for (String packName : innerPacks) {
                if (pack.getName().startsWith(basePackage + "." + packName)) {
                    imports.add(pack.getName() + ".*");
                }
            }
        }
        return imports;
    }


    protected Set<String> getRepositoryImpls() {
        Set<String> imports = new HashSet<>();
        Set<String> javaTempIds = repositoryInst.getJavaTempIds();
        String repositoryPackageName = repositoryInst.getPackageName();
        DomainInst domainInst = null;
        if (dsmBean instanceof DomainInst) {
            domainInst = (DomainInst) dsmBean;
        } else if (dsmBean instanceof ViewInst) {
            domainInst = ((ViewInst) dsmBean).getDomainInst();
        }

        if (domainInst != null) {
            String basePackage = domainInst.getEuPackage() + ".";
            if (moduleName.startsWith(basePackage)) {
                moduleName = moduleName.substring(basePackage.length()).toLowerCase();
            }
        }


        if (moduleName.equals("")) {
            moduleName = simClassName.toLowerCase();
        }

        if (!moduleName.equals(simClassName.toLowerCase()) && !moduleName.endsWith("." + simClassName.toLowerCase()) && !simClassName.toLowerCase().endsWith(moduleName.toLowerCase())) {
            repositoryPackageName = repositoryPackageName + "." + moduleName.toLowerCase() + "." + simClassName.toLowerCase();
        } else {
            repositoryPackageName = repositoryPackageName + "." + moduleName.toLowerCase();
        }

        for (String innerPack : innerPacks) {
            String rInnerPackageName = repositoryPackageName + "." + innerPack;
            if (Package.getPackage(rInnerPackageName) != null) {
                imports.add(rInnerPackageName + ".*");
            }

            for (Package pack : ClassUtility.getAllDynPacks()) {
                if (pack.getName().startsWith(repositoryPackageName + "." + innerPack)) {
                    imports.add(pack.getName() + ".*");
                }
            }
        }
        return imports;
    }

    public String getServiceClassName() {
        return serviceClassName;
    }

    public void setServiceClassName(String serviceClassName) {
        this.serviceClassName = serviceClassName;
    }

    public Class getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(Class serviceClass) {
        this.serviceClass = serviceClass;
    }

    public AggEntityConfig getAggEntityConfig() {
        return aggEntityConfig;
    }

    public void setAggEntityConfig(AggEntityConfig aggEntityConfig) {
        this.aggEntityConfig = aggEntityConfig;
    }

    public AggViewRoot getViewRoot() {
        return viewRoot;
    }

    public RepositoryInst getRepositoryInst() {
        return repositoryInst;
    }

    public void setRepositoryInst(RepositoryInst repositoryInst) {
        this.repositoryInst = repositoryInst;
    }

    public String getSimClassName() {
        return simClassName;
    }

    public void setSimClassName(String simClassName) {
        this.simClassName = simClassName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public T getViewBean() {
        return viewBean;
    }

    public void setViewBean(T viewBean) {
        this.viewBean = viewBean;
    }

    public Set<String> getImports() {
        return imports;
    }

    public void setImports(Set<String> imports) {
        this.imports = imports;
    }


    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public DSMInst getDsmBean() {
        return dsmBean;
    }

    public void setDsmBean(DSMInst dsmBean) {
        this.dsmBean = dsmBean;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
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

    @Override
    public String getBasepath() {
        return packageName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

}
