package net.ooder.esd.dsm.aggregation;


import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.Aggregation;
import net.ooder.annotation.AggregationType;
import net.ooder.annotation.UserSpace;
import net.ooder.common.JDSConstants;
import net.ooder.common.JDSException;
import net.ooder.common.database.metadata.TableInfo;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.common.util.ClassUtility;
import net.ooder.config.JDSConfig;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.DSMInst;
import net.ooder.esd.dsm.aggregation.ref.AggEntityRef;
import net.ooder.esd.dsm.enums.DSMType;
import net.ooder.esd.dsm.java.JavaPackage;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.view.ViewInst;
import net.ooder.esd.util.json.CaseEnumsSerializer;

import java.util.*;

public class DomainInst extends DSMInst implements Comparable<DomainInst> {
    private static final Log log = LogFactory.getLog(JDSConstants.CONFIG_KEY, DomainInst.class);
    public String domainId;
    @JSONField(deserializeUsing = CaseEnumsSerializer.class)
    public UserSpace userSpace = UserSpace.FORM;
    @JSONField(deserializeUsing = CaseEnumsSerializer.class)
    public DSMType dsmType = DSMType.AGGREGATION;
    public Set<String> dsmRefIds = new LinkedHashSet<>();
    public Map<String, AggEntityRef> aggEntityRefMap = new HashMap<>();

    public DomainInst() {
        super();
    }



    public DomainInst(String projectVersionName, UserSpace userSpace) {
        super();
        this.userSpace = userSpace;
        this.projectVersionName = projectVersionName;
        this.name = projectVersionName + "_" + userSpace.name();
        this.space = userSpace.name().toLowerCase();
        this.packageName = "net.ooder." + projectVersionName + ".domain";

        if (userSpace.equals(UserSpace.USER)) {
            this.euPackage = projectVersionName;
        } else {
            this.euPackage = projectVersionName + "." + space;
        }
//        if (userSpace.equals(UserSpace.User) || userSpace.equals(UserSpace.SYS)) {
//            this.packageName = "net.ooder." + projectVersionName;
//        } else {
//            this.packageName = "net.ooder." + projectVersionName + "." + userSpace.name().toLowerCase();
//        }

        this.packageName = "net.ooder." + projectVersionName + "." + userSpace.name().toLowerCase();
    }


    @JSONField(serialize = false)
    public ViewInst getViewInst() {
        ViewInst viewInst = null;
        try {
            viewInst = DSMFactory.getInstance().getViewManager().createDefaultView(this);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return viewInst;
    }


    @JSONField(serialize = false)
    public List<JavaSrcBean> getJavaSrcListByMethod(String sourceClassName, String methodName) {
        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
        List<JavaSrcBean> allSrcBeans = new ArrayList<>();
        allSrcBeans.addAll(this.getJavaSrcBeans());

        allSrcBeans.addAll(this.getViewInst().getJavaSrcBeans());
        for (JavaSrcBean srcBean : allSrcBeans) {

            if (srcBean.getSourceClassName() != null && srcBean.getSourceClassName().equals(sourceClassName) && srcBean.getMethodName() != null && srcBean.getMethodName().equals(methodName)) {
                javaSrcBeans.add(srcBean);
            }
        }
        return javaSrcBeans;
    }


    @JSONField(serialize = false)
    public List<ESDClass> getViewEntityList() {
        List<ESDClass> srcBeanList = getEntityList(AggregationType.ENTITY, true);
        return srcBeanList;

    }

    @JSONField(serialize = false)
    public List<ESDClass> getAggModules() {
        List<ESDClass> roots = this.getEntityList(AggregationType.MODULE, true);
        return roots;
    }



    @JSONField(serialize = false)
    public List<ESDClass> getAggMenus() {
        List<ESDClass> roots = this.getEntityList(AggregationType.MENU, true);
        return roots;
    }


    @JSONField(serialize = false)
    public List<ESDClass> getAggDomains() {
        List<ESDClass> roots = this.getEntityList(AggregationType.DOMAIN, true);
        return roots;
    }

    @JSONField(serialize = false)
    public List<ESDClass> getAggViews() {
        List<ESDClass> roots = this.getEntityList(AggregationType.VIEW, true);
        return roots;
    }


    @JSONField(serialize = false)
    public List<ESDClass> getAggAPIs() {
        List<ESDClass> roots = this.getEntityList(AggregationType.API, true);
        return roots;
    }


    @JSONField(serialize = false)
    public List<ESDClass> getAllServiceClass() {
        List<ESDClass> esdClasses = new ArrayList<>();
        for (String className : getAllClassNames()) {
            ESDClass esdClass = null;
            try {
                esdClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(className, false);
            } catch (Throwable e) {
                log.warn(e);
            }

            if (esdClass != null && !esdClass.isProxy()) {
                esdClasses.add(esdClass);
            }
        }
        return esdClasses;
    }

    @JSONField(serialize = false)
    public List<ESDClass> getAllProxyClass() {
        List<ESDClass> esdClasses = new ArrayList<>();
        for (String className : getAllClassNames()) {
            ESDClass esdClass = null;
            try {
                esdClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(className, false);
            } catch (Throwable e) {
                log.warn(e);
            }
            if (esdClass != null && esdClass.isProxy()) {
                esdClasses.add(esdClass);
            }
        }
        return esdClasses;
    }

    @Override
    @JSONField(serialize = false)
    public JavaPackage getRootPackage() {
        if (rootPackage == null || rootPackage.listAllChildren().isEmpty()) {
            rootPackage = getPackageByName(packageName);
            if (rootPackage == null) {
                rootPackage = getProjectRoot().createChildPackage(packageName.replace(".", "/"));
            }
        }
        return rootPackage;
    }

    public void setRootPackage(JavaPackage rootPackage) {
        this.rootPackage = rootPackage;
    }


    @JSONField(serialize = false)
    @Override
    public List<JavaSrcBean> getJavaSrcBeans() {
        return this.getRootPackage().listAllFile();
    }


    public List<ESDClass> getEntityList(AggregationType aggregationType, boolean isProxy) {
        List<ESDClass> serviceBeans = new ArrayList<>();
        List<ESDClass> aggBeans = new ArrayList<>();
        List<JavaSrcBean> domainBeanList = this.getJavaSrcBeans();
        List<ESDClass> repositoryList = this.getRepositoryInst().getAggBeans(userSpace, aggregationType);
        aggBeans.addAll(repositoryList);

        for (JavaSrcBean srcBean : domainBeanList) {
            Class clazz = null;
            String className = srcBean.getClassName();
            try {
                clazz = ClassUtility.loadClass(className);
                if (clazz != null) {
                    Aggregation aggregation = (Aggregation) clazz.getAnnotation(Aggregation.class);
                    if (aggregation != null && aggregation.rootClass() != null && aggregation.type().equals(aggregationType)) {
                        ESDClass serviceClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(className, true);
                        aggBeans.add(serviceClass);
                    }
                }
            } catch (Throwable e) {
                log.warn(e);
            }
        }

        for (ESDClass esdClass : aggBeans) {
            if (isProxy) {
                if (esdClass.isProxy()) {
                    serviceBeans.add(esdClass);
                }
            } else if (!esdClass.isProxy()) {
                serviceBeans.add(esdClass);
            }
        }
        return serviceBeans;
    }


    @JSONField(serialize = false)
    public ESDClass getDAOService(String tableName) {
        List<ESDClass> daoServiceClasses = this.getEntityList(AggregationType.REPOSITORY, false);
        for (ESDClass esdClass : daoServiceClasses) {
            try {
                if (esdClass.getEntityClass() != null) {
                    TableInfo tableInfo = DSMFactory.getInstance().getRepositoryManager().loadTableInfo(esdClass.getEntityClass().getClassName());
                    if (tableInfo != null && tableInfo.getName().toUpperCase().equals(tableName.toUpperCase())) {
                        return esdClass;
                    }
                }
            } catch (Throwable e) {
                log.warn(e);
            }
        }
        return null;
    }

    public UserSpace getUserSpace() {
        return userSpace;
    }

    public void setUserSpace(UserSpace userSpace) {
        this.userSpace = userSpace;
    }

    @JSONField(serialize = false)
    public ESDClass getDAOBean(String tableName) {
        ESDClass esdClass = getDAOService(tableName);
        if (esdClass != null) {
            esdClass.getEntityClass();
        }
        return null;
    }

    @JSONField(serialize = false)
    public Set<String> getAllClassNames() {
        Set<String> allEntityNames = new LinkedHashSet<>();
        List<JavaSrcBean> esdClassList = this.getJavaSrcBeans();
        for (JavaSrcBean javaSrcBean : esdClassList) {
            allEntityNames.add(javaSrcBean.getClassName());
        }
        return allEntityNames;
    }

    public DSMType getDsmType() {
        return dsmType;
    }

    public void setDsmType(DSMType dsmType) {
        this.dsmType = dsmType;
    }

    public Map<String, AggEntityRef> getAggEntityRefMap() {
        return aggEntityRefMap;
    }

    public void setAggEntityRefMap(Map<String, AggEntityRef> aggEntityRefMap) {
        this.aggEntityRefMap = aggEntityRefMap;
    }

    @Override
    public String getDsmId() {
        return domainId;
    }


    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }


    public Set<String> getDsmRefIds() {
        return dsmRefIds;
    }

    public void setDsmRefIds(Set<String> dsmRefIds) {
        this.dsmRefIds = dsmRefIds;
    }


    @Override
    public int compareTo(DomainInst o) {
        return o.getCreateTime().compareTo(this.getCreateTime());
    }
}
