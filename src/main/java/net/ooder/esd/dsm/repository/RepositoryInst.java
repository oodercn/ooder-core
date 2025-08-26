package net.ooder.esd.dsm.repository;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMInst;
import net.ooder.esd.dsm.enums.DSMType;
import net.ooder.esd.dsm.java.JavaPackage;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.repository.database.ref.TableRef;
import net.ooder.esd.dsm.repository.entity.EntityRef;
import net.ooder.esd.util.json.CaseEnumsSerializer;
import net.ooder.annotation.Aggregation;
import net.ooder.annotation.AggregationType;
import net.ooder.annotation.ESDEntity;
import net.ooder.annotation.UserSpace;

import java.util.*;

public class RepositoryInst extends DSMInst implements Comparable<RepositoryInst> {
    public String schema = "fdt";
    public String serverUrl = "http://api.radev.cn";
    @JSONField(deserializeUsing = CaseEnumsSerializer.class)
    public DSMType dsmType = DSMType.REPOSITORY;
    public Map<String, TableRef> tableRefMap = new HashMap<>();
    public Map<String, EntityRef> entityRefMap = new HashMap<>();
    public Set<String> entityNames = new LinkedHashSet<>();
    public Set<String> bpmEntityNames = new LinkedHashSet<>();
    public Set<String> userEntityNames = new LinkedHashSet<>();
    public Set<String> viewEntityNames = new LinkedHashSet<>();
    public Set<String> urlEntityNames = new LinkedHashSet<>();
    public Set<String> tableNames = new LinkedHashSet<>();
    public Set<String> bpmTables = new LinkedHashSet<>();
    public Set<String> userTables = new LinkedHashSet<>();
    public Set<String> viewTables = new LinkedHashSet<>();
    public Set<String> urlNames = new LinkedHashSet<>();
    public Set<String> refIds = new LinkedHashSet<>();


    public RepositoryInst() {

    }

    public RepositoryInst(String projectVersionName) {
        this.projectVersionName = projectVersionName;
        this.name = projectVersionName;
        this.packageName = "net.ooder." + projectVersionName + ".repository";
        this.space = projectVersionName;
    }

    public JavaPackage getRootPackage() {
        if (rootPackage == null || rootPackage.listAllChildren().isEmpty()) {
            rootPackage = getPackageByName(packageName);
            if (rootPackage == null) {
                rootPackage = getProjectRoot().createChildPackage(packageName.replace(".", "/"));
            }
        }
        return rootPackage;
    }


    @JSONField(serialize = false)
    public List<ESDClass> getEntityList() {
        List<JavaSrcBean> repositoryList = this.getRepositoryInst().getJavaEntities();
        List<ESDClass> entityList = new ArrayList<>();
        for (JavaSrcBean srcBean : repositoryList) {
            String className = srcBean.getClassName();
            try {
                Class clazz = ClassUtility.loadClass(className);
                ESDEntity entity = (ESDEntity) clazz.getAnnotation(ESDEntity.class);
                if (entity != null) {
                    ESDClass esdClass = BuildFactory.getInstance().getClassManager().getRepositoryClass(className, true);
                    if (!entityList.contains(esdClass)){
                        entityList.add(esdClass);
                    }

                }
            } catch (ClassNotFoundException e) {

            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        return entityList;

    }

    @JSONField(serialize = false)
    public List<JavaSrcBean> getJavaSrcListByMethod(String sourceClassName, String methodName) {
        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();

        for (JavaSrcBean srcBean : this.getJavaSrcBeans()) {
            if (srcBean.getSourceClassName() != null && srcBean.getSourceClassName().equals(sourceClassName) && srcBean.getMethodName() != null && srcBean.getMethodName().equals(methodName)) {
                javaSrcBeans.add(srcBean);
            }
        }
        return javaSrcBeans;
    }


    public Set<String> getBpmEntityNames() {
        return bpmEntityNames;
    }

    public void setBpmEntityNames(Set<String> bpmEntityNames) {
        this.bpmEntityNames = bpmEntityNames;
    }

    public Set<String> getUserEntityNames() {
        return userEntityNames;
    }

    public void setUserEntityNames(Set<String> userEntityNames) {
        this.userEntityNames = userEntityNames;
    }

    public Set<String> getViewEntityNames() {
        return viewEntityNames;
    }

    public void setViewEntityNames(Set<String> viewEntityNames) {
        this.viewEntityNames = viewEntityNames;
    }

    public Set<String> getUrlEntityNames() {
        return urlEntityNames;
    }

    public void setUrlEntityNames(Set<String> urlEntityNames) {
        this.urlEntityNames = urlEntityNames;
    }

    public Set<String> getBpmTables() {
        return bpmTables;
    }

    public void setBpmTables(Set<String> bpmTables) {
        this.bpmTables = bpmTables;
    }

    public Set<String> getUserTables() {
        return userTables;
    }

    public void setUserTables(Set<String> userTables) {
        this.userTables = userTables;
    }

    public Set<String> getViewTables() {
        return viewTables;
    }

    public void setViewTables(Set<String> viewTables) {
        this.viewTables = viewTables;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public Set<String> getUrlNames() {
        return urlNames;
    }

    public void setUrlNames(Set<String> urlNames) {
        this.urlNames = urlNames;
    }

    @Override
    public String getDsmId() {
        return projectVersionName;
    }

    @Override
    public List<JavaSrcBean> getJavaSrcBeans() {
        return getRootPackage().listAllFile();
    }


    @JSONField(serialize = false)
    public List<ESDClass> getAggBeans(UserSpace userSpace, AggregationType aggregationType) {
        List<JavaSrcBean> repositoryList = this.getRepositoryInst().getJavaEntities();
        List<ESDClass> entityList = new ArrayList<>();
        for (JavaSrcBean srcBean : repositoryList) {
            String className = srcBean.getClassName();
            try {
                Class clazz = ClassUtility.loadClass(className);
                Aggregation aggregation = (Aggregation) clazz.getAnnotation(Aggregation.class);
                if (aggregation != null && aggregation.rootClass() != null && (aggregationType == null || aggregation.type().equals(aggregationType))) {
                    if (aggregation.userSpace().length == 0 || userSpace == null || Arrays.asList(aggregation.userSpace()).contains(userSpace)) {
                        ESDClass esdClass = BuildFactory.getInstance().getClassManager().getRepositoryClass(className, true);
                        if (!entityList.contains(esdClass)) {
                            entityList.add(esdClass);
                        }
                    }
                }
            } catch (ClassNotFoundException e) {

            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        return entityList;

    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public DSMType getDsmType() {
        return dsmType;
    }

    public void setDsmType(DSMType dsmType) {
        this.dsmType = dsmType;
    }

    public Map<String, TableRef> getTableRefMap() {
        return tableRefMap;
    }

    public void setTableRefMap(Map<String, TableRef> tableRefMap) {
        this.tableRefMap = tableRefMap;
    }

    public Map<String, EntityRef> getEntityRefMap() {
        return entityRefMap;
    }

    public void setEntityRefMap(Map<String, EntityRef> entityRefMap) {
        this.entityRefMap = entityRefMap;
    }

    public Set<String> getEntityNames() {
        return entityNames;
    }

    public void setEntityNames(Set<String> entityNames) {
        this.entityNames = entityNames;
    }


    public Set<String> getTableNames() {
        return tableNames;
    }

    public void setTableNames(Set<String> tableNames) {
        this.tableNames = tableNames;
    }

    public Set<String> getRefIds() {
        return refIds;
    }

    public void setRefIds(Set<String> refIds) {
        this.refIds = refIds;
    }

    public void setRootPackage(JavaPackage rootPackage) {
        this.rootPackage = rootPackage;
    }

    @Override
    public int compareTo(RepositoryInst o) {
        return o.getCreateTime().compareTo(this.getCreateTime());
    }
}
