package net.ooder.esd.dsm.repository;

import com.alibaba.fastjson.JSON;
import net.ooder.annotation.DBTable;
import net.ooder.annotation.RefType;
import net.ooder.annotation.UserSpace;
import net.ooder.common.JDSConstants;
import net.ooder.common.JDSException;
import net.ooder.common.database.dao.DAOException;
import net.ooder.common.database.metadata.MetadataFactory;
import net.ooder.common.database.metadata.TableInfo;
import net.ooder.common.logging.ChromeProxy;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.common.logging.LogSetpLog;
import net.ooder.common.util.ClassUtility;
import net.ooder.common.util.FileUtility;
import net.ooder.common.util.StringUtility;
import net.ooder.context.JDSActionContext;
import net.ooder.esb.config.manager.EsbBeanFactory;
import net.ooder.esd.bean.CustomRefBean;
import net.ooder.esd.bean.CustomViewBean;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.custom.ESDClassManager;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.context.AggViewRoot;
import net.ooder.esd.dsm.enums.RepositoryType;
import net.ooder.esd.dsm.gen.repository.*;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.repository.config.EntityConfig;
import net.ooder.esd.dsm.repository.database.FDTFactory;
import net.ooder.esd.dsm.repository.database.proxy.DSMTableProxy;
import net.ooder.esd.dsm.repository.database.ref.TableRef;
import net.ooder.esd.dsm.repository.entity.EntityRef;
import net.ooder.esd.dsm.temp.JavaTemp;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.MySpace;
import net.ooder.esd.engine.ProjectCacheManager;
import net.ooder.esd.engine.inner.INProject;
import net.ooder.esd.tool.properties.item.TabListItem;
import net.ooder.esd.util.OODUtil;
import net.ooder.server.JDSServer;
import net.ooder.vfs.FileInfo;
import net.ooder.vfs.Folder;
import net.ooder.vfs.VFSConstants;
import net.ooder.vfs.ct.CtVfsFactory;
import net.ooder.vfs.ct.CtVfsService;
import net.ooder.web.util.AnnotationUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class RepositoryManager {


    protected Log log = LogFactory.getLog(JDSConstants.CONFIG_KEY, RepositoryManager.class);

    public static final String EntityConfigPath = "entityconfig";

    private static final String THREAD_LOCK = "Thread Lock";

    private static Map<String, RepositoryManager> managerMap = new HashMap<String, RepositoryManager>();

    private final ProjectCacheManager projectCacheManager;

    private final FDTFactory fdtFactory;

    private final MySpace space;

    private Map<String, TableInfo> tableMap = new ConcurrentHashMap<>();

    private Map<String, DSMTableProxy> tableProxyMap = new HashMap<String, DSMTableProxy>();

    private Map<String, EntityConfig> entityConfigMap = new ConcurrentHashMap<>();

    private Folder entityConfigFolder;


    public static RepositoryManager getInstance(MySpace space) {
        String path = space.getPath();
        RepositoryManager manager = managerMap.get(path);
        if (manager == null) {
            synchronized (THREAD_LOCK) {
                if (manager == null) {
                    manager = new RepositoryManager(space);
                    managerMap.put(path, manager);
                }
            }
        }
        return manager;
    }

    RepositoryManager(MySpace space) {
        this.space = space;
        this.fdtFactory = FDTFactory.getInstance(space);
        this.projectCacheManager = ProjectCacheManager.getInstance(space);

        try {
            this.entityConfigFolder = space.getRootfolder().createChildFolder(EntityConfigPath, JDSServer.getInstance().getAdminUser().getId());
        } catch (JDSException e) {
            e.printStackTrace();
        }


    }


    public void reload() {
        tableMap.clear();
        tableProxyMap.clear();
        Map<String, Class> classMap = new HashMap<>();
        classMap.putAll(EsbBeanFactory.getInstance().getAllClass());
        classMap.putAll(ClassUtility.getDynClassMap());
        Set<Map.Entry<String, Class>> allClass = classMap.entrySet();
        for (Map.Entry<String, Class> clazzEntry : allClass) {
            Class clazz = clazzEntry.getValue();
            try {
                if (!clazz.equals(Void.class) && !clazz.equals(Void.TYPE) && !clazz.equals(Enum.class)) {
                    loadTableInfo(clazz.getName());
                }
            } catch (Exception e) {
                log.warn(e);
                e.printStackTrace();
            }
        }
    }


    public void updateEntityConfig(EntityConfig entityConfig) throws JDSException {
        String uKey = entityConfig.getRootClassName();
        entityConfigMap.put(uKey, entityConfig);
        FileInfo fileInfo = entityConfigFolder.createFile(entityConfig.getRootClassName(), entityConfig.getRootClassName(), null);
        String content = JSON.toJSONString(entityConfig);
        this.getVfsClient().saveFileAsContent(fileInfo.getPath(), content, VFSConstants.Default_Encoding);
    }

    public void reloadEntityConfig(String className) throws JDSException {
        entityConfigMap.remove(className);
        ESDClassManager esdClassManager = BuildFactory.getInstance().getClassManager();
        esdClassManager.removeRepositoryClass(className);
    }


    public EntityConfig getEntityConfig(String className, boolean reload) {
        String tableKey = className;
        EntityConfig entityConfig = entityConfigMap.get(tableKey);
        ESDClassManager esdClassManager = null;
        try {
            esdClassManager = BuildFactory.getInstance().getClassManager();
            ESDClass esdClass = esdClassManager.getRepositoryClass(className, reload);
            if (entityConfig == null || reload) {
                if (esdClass != null) {
                    entityConfig = new EntityConfig(esdClass);
                    entityConfigMap.put(tableKey, entityConfig);
                }
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return entityConfig;
    }


    public void clear() {
        tableMap.clear();
        tableProxyMap.clear();

    }

    public RepositoryInst updateRepositoryInst(RepositoryInst repositoryInst, boolean autocommit) throws JDSException {
        if (repositoryInst != null) {
            INProject config = projectCacheManager.getProjectByName(repositoryInst.getProjectVersionName());
            config.setRepository(repositoryInst);
            if (autocommit) {
                this.commitProjectInst(repositoryInst.getProjectVersionName());
            }
        }
        return repositoryInst;
    }


    public RepositoryInst getProjectRepository(String projectName) {
        RepositoryInst repositoryInst = null;
        try {
            if (projectName == null || projectName.equals("")) {
                projectName = DSMFactory.getInstance().getDefaultProjectName();
            }
            INProject project = projectCacheManager.getProjectByName(projectName);
            repositoryInst = project.getRepository();
            repositoryInst.setProjectVersionName(project.getProjectName());
        } catch (JDSException e) {
            e.printStackTrace();
        }

        return repositoryInst;
    }


    public void commitProjectInst(String projectName) throws JDSException {
        RepositoryInst config = projectCacheManager.getProjectByName(projectName).getRepository();
        projectCacheManager.updateRepositoryConfig(projectName, config);
    }

    public TableInfo loadTableInfo(String clazzName) throws ClassNotFoundException {
        TableInfo tableInfo = tableMap.get(clazzName);
        if (tableInfo == null) {
            Class clazz = ClassUtility.loadClass(clazzName);
            DBTable table = AnnotationUtil.getClassAnnotation(clazz, DBTable.class);
            if (table != null) {
                tableInfo = fdtFactory.createTable(clazz);
                if (tableInfo != null) {
                    tableMap.put(clazzName, tableInfo);
                }
            }
        }
        return tableInfo;
    }

    public TableRef getTableRefById(String refId, String projectName) {
        RepositoryInst repositoryInst = this.getProjectRepository(projectName);
        return repositoryInst.getTableRefMap().get(refId);
    }

    public DSMTableProxy getTableProxyByName(String tableName, String projectName) throws JDSException {
        String tableKey = tableName + "[" + projectName + "]";
        DSMTableProxy tableProxy = tableProxyMap.get(tableKey);
        if (tableProxy == null) {
            TableInfo tableInfo = null;
            try {
                tableInfo = fdtFactory.getTableInfoByFullName(tableName);
            } catch (DAOException e) {
                throw new JDSException(e);
            }
            if (tableInfo != null) {
                tableProxy = new DSMTableProxy(tableInfo, projectName);
                tableProxyMap.put(tableKey, tableProxy);
            }

        }
        return tableProxy;
    }


    public EntityRef getEntityRefById(String refId, String projectName) {
        RepositoryInst repositoryInst = this.getProjectRepository(projectName);
        Map<String, EntityRef> refMap = repositoryInst.getEntityRefMap();
        return refMap.get(refId);
    }

    public List<EntityRef> getEntityRefByName(String className, String repositoryInstId, RefType refType) {
        List<EntityRef> refs = new ArrayList<>();
        List<EntityRef> entityRefs = getEntityRefByName(className, repositoryInstId);
        for (EntityRef ref : entityRefs) {
            if (ref.getRefBean().equals(refType)) {
                refs.add(ref);
            }
        }
        return refs;
    }

    public List<EntityRef> getEntityRefByName(String className, String projectName) {
        RepositoryInst repositoryInst = this.getProjectRepository(projectName);
        List<EntityRef> entityRefs = new ArrayList<>();
        Map<String, EntityRef> refMap = repositoryInst.getEntityRefMap();
        try {
            ESDClassManager esdClassManager = BuildFactory.getInstance().getClassManager();
            ESDClass esdClass = esdClassManager.getRepositoryClass(className, true);
            if (esdClass != null) {
                List<ESDField> methodInfos = esdClass.getRefFields();
                for (ESDField methodInfo : methodInfos) {
                    if (methodInfo.getRefBean() != null && methodInfo.getRefBean().getRef() != null) {
                        EntityRef entityRef = new EntityRef(methodInfo, projectName);
                        if (!refMap.containsKey(entityRef.getRefId())) {
                            refMap.put(entityRef.getRefId(), entityRef);
                        }
                    }
                }

            }

        } catch (JDSException e) {
            e.printStackTrace();
        }


        refMap.forEach((k, v) -> {
            if (v.getClassName().toLowerCase().equals(className.toLowerCase())) {
                v.setRefId(k);
                entityRefs.add(v);
            }
        });
        return entityRefs;
    }


    public List<TableRef> getTableRefByName(String tableName, String projectName) {
        RepositoryInst repositoryInst = this.getProjectRepository(projectName);
        List<TableRef> tableRefs = new ArrayList<>();
        Map<String, TableRef> refMap = repositoryInst.getTableRefMap();
        refMap.forEach((k, v) -> {
            if (v.getRefId() == null || v.getRefId().equals("")) {
                v.setRefId(UUID.randomUUID().toString());
            }
            if (v.getTablename().toLowerCase().equals(tableName.toLowerCase())
                    ) {
                v.setRefId(k);
                tableRefs.add(v);
            }
        });
        return tableRefs;
    }

    public List<TableRef> getTableRefs(String projectName) {
        RepositoryInst repositoryInst = this.getProjectRepository(projectName);
        List<TableRef> tableRefs = new ArrayList<>();
        Map<String, TableRef> refMap = repositoryInst.getTableRefMap();
        refMap.forEach((k, v) -> {
            tableRefs.add(v);
        });
        return tableRefs;
    }


    public List<TableInfo> getTableInfoBeans() {
        List<TableInfo> tableInfos = new ArrayList<>();
        tableMap.forEach((k, v) -> {
            tableInfos.add(v);
        });
        return tableInfos;
    }


    private void clearEntityConfig(String projectName) throws JDSException {
        RepositoryInst repositoryInst = this.getProjectRepository(projectName);
        List<JavaSrcBean> srcBeans = repositoryInst.getJavaSrcBeans();
        for (JavaSrcBean javaSrcBean : srcBeans) {
            this.delEntity(javaSrcBean.getClassName(), projectName);
            File file = javaSrcBean.getFile();
            try {
                FileUtility.deleteDirectory(file.getParentFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void delEntityRef(String refId, String repositoryInstId) throws JDSException {
        delEntityRef(refId, repositoryInstId, true);
    }

    public void delEntityRef(String refId, String projectName, boolean autocommit) throws JDSException {
        RepositoryInst repositoryInst = this.getProjectRepository(projectName);
        Map<String, EntityRef> refMap = repositoryInst.getEntityRefMap();
        if (refId != null) {
            String[] refIds = StringUtility.split(refId, ";");
            for (String id : refIds) {
                refMap.remove(id);
            }
        }
        if (autocommit) {
            this.commitProjectInst(repositoryInst.getProjectVersionName());
        }
    }


    public void delTableRef(String refId, String repositoryInstId) throws JDSException {
        delTableRef(refId, repositoryInstId, true);
    }

    public void delTableRef(String refId, String projectName, boolean autocommit) throws JDSException {
        RepositoryInst repositoryInst = this.getProjectRepository(projectName);
        Map<String, TableRef> refMap = repositoryInst.getTableRefMap();
        if (refId != null) {
            String[] refIds = StringUtility.split(refId, ";");
            for (String id : refIds) {
                refMap.remove(id);
            }
        }
        if (autocommit) {
            this.commitProjectInst(repositoryInst.getProjectVersionName());
        }
    }


    public void updateEntityRef(EntityRef ref) throws JDSException {
        this.updateEntityRef(ref, true);
    }

    public void updateEntityRef(EntityRef ref, boolean autocommit) throws JDSException {
        RepositoryInst repositoryInst = this.getProjectRepository(ref.getProjectVersionName());
        String mainClassName = ref.getClassName().toLowerCase();
        String otherClassName = ref.getOtherClassName().toLowerCase();
        if (otherClassName == null || otherClassName == null) {
            throw new JDSException("格式错误");
        }
        Map<String, EntityRef> refMap = repositoryInst.getEntityRefMap();
        String refId = ref.getRefId();
        if (refId == null || refId.equals("")) {
            refMap.remove(refId);
            refId = UUID.randomUUID().toString();
        }

        EntityRef oClassRef = refMap.get(refId);
        if (oClassRef == null) {
            oClassRef = new EntityRef();
            oClassRef.setRefId(refId);
        }
        oClassRef.setProjectVersionName(ref.getProjectVersionName());
        oClassRef.setClassName(mainClassName);
        oClassRef.setOtherClassName(otherClassName);
        oClassRef.setRefBean(ref.getRefBean());
        refMap.put(refId, oClassRef);

        //更新OTHERTABLE
        EntityRef tentityRef = this.getEntityRef(otherClassName, mainClassName, ref.getProjectVersionName());
        CustomRefBean tref = ref.getRefBean();
        switch (ref.getRefBean().getRef()) {
            case O2M:
                tref.setRef(RefType.M2O);
                break;
            case M2O:
                tref.setRef(RefType.O2M);
                break;
        }
        if (tentityRef == null) {
            tentityRef = new EntityRef();
            tentityRef.setRefId(UUID.randomUUID().toString());
        }
        tentityRef.setProjectVersionName(ref.getProjectVersionName());
        tentityRef.setClassName(otherClassName);
        tentityRef.setOtherClassName(mainClassName);
        tentityRef.setRefBean(tref);
        refMap.put(tentityRef.getRefId(), tentityRef);
        if (autocommit) {
            this.commitProjectInst(repositoryInst.getProjectVersionName());

        }
    }


    public void updateTableRef(TableRef ref) throws JDSException {
        this.updateTableRef(ref, true);
    }

    public void updateTableRef(TableRef ref, boolean autocommit) throws JDSException {
        RepositoryInst repositoryInst = this.getProjectRepository(ref.getProjectVersionName());
        String mainTableName = ref.getTablename().toLowerCase();
        String otherTableName = ref.getOtherTableName().toLowerCase();
        if (mainTableName == null || otherTableName == null) {
            throw new JDSException("格式错误");
        }
        Map<String, TableRef> refMap = repositoryInst.getTableRefMap();
        String refId = ref.getRefId();
        if (refId == null || refId.equals("")) {
            refMap.remove(refId);
            refId = UUID.randomUUID().toString();
        }

        TableRef oTableRef = refMap.get(refId);
        if (oTableRef == null) {
            oTableRef = new TableRef();
            oTableRef.setRefId(refId);
        }
        oTableRef.setProjectVersionName(ref.getProjectVersionName());
        oTableRef.setFkField(ref.getFkField());
        oTableRef.setPkField(ref.getPkField());
        oTableRef.setMainCaption(ref.getMainCaption());
        oTableRef.setOtherCaption(ref.getOtherCaption());
        oTableRef.setTablename(mainTableName);
        oTableRef.setOtherTableName(otherTableName);
        oTableRef.setRef(ref.getRef());
        refMap.put(refId, oTableRef);

        //更新OTHERTABLE

        TableRef ttableRef = this.getTableRef(otherTableName, mainTableName, ref.getProjectVersionName());
        RefType tref = ref.getRef();
        switch (ref.getRef()) {
            case O2M:
                tref = RefType.M2O;
                break;
            case M2O:
                tref = RefType.O2M;
                break;
        }
        if (ttableRef == null) {
            ttableRef = new TableRef();
            ttableRef.setRefId(UUID.randomUUID().toString());
        }
        ttableRef.setProjectVersionName(ref.getProjectVersionName());
        ttableRef.setFkField(ref.getPkField());
        ttableRef.setPkField(ref.getFkField());
        ttableRef.setTablename(otherTableName);
        ttableRef.setOtherTableName(mainTableName);
        ttableRef.setMainCaption(ref.getOtherCaption());
        ttableRef.setOtherCaption(ref.getMainCaption());
        ttableRef.setRef(tref);
        refMap.put(ttableRef.getRefId(), ttableRef);

        if (autocommit) {
            this.commitProjectInst(repositoryInst.getProjectVersionName());

        }
    }

    public EntityRef getEntityRef(String mainEntityName, String otherEntityName, String projectVersionName) {
        List<EntityRef> entityRefs = this.getEntityRefByName(mainEntityName, projectVersionName);
        for (EntityRef tableRef : entityRefs) {
            if (tableRef.getOtherClassName().toLowerCase().equals(otherEntityName.toLowerCase())) {
                return tableRef;
            }
        }
        return null;
    }

    private void delEntity(RepositoryInst repositoryInst, UserSpace catType, String... classNameList) throws JDSException {

        Set<String> entityNames = new HashSet();
        switch (catType) {
            case FORM:
                entityNames = repositoryInst.getBpmEntityNames();
                break;
            case CRUD:
                entityNames = repositoryInst.getEntityNames();
                break;
            case USER:
                entityNames = repositoryInst.getUserEntityNames();
                break;
            case VIEW:
                entityNames = repositoryInst.getViewEntityNames();
                break;
        }

        for (String className : classNameList) {
            entityNames.remove(className);
            this.delEntity(className, repositoryInst.getProjectVersionName());
        }
        updateRepositoryInst(repositoryInst, true);
    }

    public void delEntity(String className, String projectName) throws JDSException {
        String configKey = className + "[" + projectName + "]";
        List<EntityRef> entityRefs = this.getEntityRefByName(className, projectName);
        for (EntityRef entityRef : entityRefs) {
            this.delEntityRef(entityRef.getRefId(), projectName);
        }
        ESDClassManager esdClassManager = BuildFactory.getInstance().getClassManager();
        esdClassManager.removeRepositoryClass(configKey);
        entityConfigMap.remove(configKey);
        Folder dsmFolder = entityConfigFolder.createChildFolder(projectName, JDSServer.getInstance().getAdminUser().getId());
        FileInfo fileInfo = this.getVfsClient().getFileByPath(dsmFolder.getPath() + className);
        if (fileInfo != null) {
            this.getVfsClient().deleteFile(fileInfo.getID());
        }
    }

    public void delUrl(String url, String projectName) throws JDSException {
        RepositoryInst repositoryInst = this.getProjectRepository(projectName);
        repositoryInst.getUrlNames().remove(url);
    }


    public void delTable(String tablename, String projectName) throws JDSException {
        String configKey = tablename + "[" + projectName + "]";
        tableMap.remove(configKey);
        tableProxyMap.remove(configKey);
    }

    public TableRef getTableRef(String mainTableName, String otherTableName, String projectName) {
        List<TableRef> tableRefs = this.getTableRefByName(mainTableName, projectName);
        for (TableRef tableRef : tableRefs) {
            if (tableRef.getOtherTableName().toLowerCase().equals(otherTableName.toLowerCase())) {
                return tableRef;
            }
        }
        return null;
    }

    public void buildRepository(RepositoryInst repositoryInst, ChromeProxy chrome) throws JDSException {
        Set<String> daoTemps = new HashSet<>();
        buildRepository(repositoryInst, daoTemps, null, chrome);
        this.updateRepositoryInst(repositoryInst, true);
    }

    public void compileRepository(RepositoryInst repositoryInst, ChromeProxy chrome) throws JDSException {
        DSMFactory.getInstance().compileRepositoryInst(repositoryInst, chrome);
    }


    private List<DSMTableProxy> getProjectTables(RepositoryInst repositoryInst, UserSpace catType) throws JDSException {
        String configName = repositoryInst.getSchema();
        MetadataFactory factory = ESDFacrory.getAdminESDClient().getDbFactory(configName);
        List<DSMTableProxy> projectTables = new ArrayList<>();
        List<String> tableNames = new ArrayList<>();
        switch (catType) {
            case FORM:
                tableNames.addAll(repositoryInst.getBpmTables());
                break;
            case CRUD:
                tableNames.addAll(repositoryInst.getTableNames());
                break;
            case USER:
                tableNames.addAll(repositoryInst.getUserTables());
                break;
            case VIEW:
                tableNames.addAll(repositoryInst.getViewTables());
                break;
        }
        try {
            List<TableInfo> tables = factory.getTableInfos(null);
            for (TableInfo table : tables) {
                if (tableNames == null || tableNames.contains(table.getName()) || tableNames.contains(table.getName().toLowerCase()) || tableNames.contains(table.getName().toUpperCase())) {
                    DSMTableProxy proxy = getTableProxyByName(table.getName(), repositoryInst.getProjectVersionName());
                    projectTables.add(proxy);
                }
            }
        } catch (DAOException e) {
            throw new JDSException(e);
        }
        return projectTables;
    }


    public List<ESDClass> getProjectEntityList(RepositoryInst repositoryInst, UserSpace catType) throws JDSException {
        List<ESDClass> projectEntityList = new ArrayList<>();
        List<String> entityNamesList = new ArrayList<>();
        switch (catType) {
            case FORM:
                entityNamesList.addAll(repositoryInst.getBpmEntityNames());
                break;
            case CRUD:
                entityNamesList.addAll(repositoryInst.getEntityNames());
                break;
            case USER:
                entityNamesList.addAll(repositoryInst.getUserEntityNames());
                break;
            case VIEW:
                entityNamesList.addAll(repositoryInst.getViewEntityNames());
                break;
        }

        for (String entityName : entityNamesList) {
            projectEntityList.add(BuildFactory.getInstance().getClassManager().getRepositoryClass(entityName, true));
        }

        return projectEntityList;
    }


    public Set<String> getDefaultTempIds(UserSpace... userSpaces) throws JDSException {
        Set tempIds = new HashSet<>();
        List<JavaTemp> temps = new ArrayList<>();
        List<RepositoryType> repositoryTypes = RepositoryType.getRepositoryTypeByCat(userSpaces);
        temps.addAll(BuildFactory.getInstance().getTempManager().getRepositoryTypeTemps(repositoryTypes.toArray(new RepositoryType[]{})));
        for (JavaTemp javaTemp : temps) {
            tempIds.add(javaTemp.getJavaTempId());
        }
        return tempIds;
    }

    public Set<String> getRepositoryTypeTemps(RepositoryType... repositoryTypes) throws JDSException {
        Set tempIds = new HashSet<>();
        List<JavaTemp> temps = new ArrayList<>();
        temps.addAll(BuildFactory.getInstance().getTempManager().getRepositoryTypeTemps(repositoryTypes));
        for (JavaTemp javaTemp : temps) {
            tempIds.add(javaTemp.getJavaTempId());
        }
        return tempIds;
    }


    public List<Callable<List<JavaSrcBean>>> genBPMFormTask(RepositoryInst repositoryInst, Set<String> tempIds, List<String> tableNames, ChromeProxy chrome) throws JDSException {
        List<Callable<List<JavaSrcBean>>> repositoryTasks = new ArrayList<>();
        if (chrome == null) {
            chrome = this.getCurrChromeDriver();
        }

        if (tableNames == null || tableNames.isEmpty()) {
            throw new JDSException("请选择数据库表！");
        }

        if (tempIds == null || tempIds.isEmpty()) {
            tempIds = getDefaultTempIds(UserSpace.FORM);
        }
        List<DSMTableProxy> allTables = getProjectTables(repositoryInst, UserSpace.FORM);
        List<DSMTableProxy> projectTables = new ArrayList<>();
        for (DSMTableProxy tableProxy : allTables) {
            if (tableNames.contains(tableProxy.getTableName())) {
                projectTables.add(tableProxy);
            }
        }


        if (projectTables.size() > 0) {
            chrome.printLog("创建流程表单共计：[" + projectTables.size() + "]张数据库表", true);
            for (DSMTableProxy table : projectTables) {
                GenTableJava genTableJava = new GenTableJava(repositoryInst, table, tempIds, UserSpace.FORM, chrome);
                repositoryTasks.add(genTableJava);
            }
        } else {
            chrome.printLog("共发现" + projectTables.size() + "张流程表单", true);
        }
        return repositoryTasks;
    }


    public List<JavaSrcBean> genTableJava(DSMTableProxy tableProxy, RepositoryInst repositoryInst, Set<String> tempIds, UserSpace userSpace, ChromeProxy chrome) throws JDSException {
        List<Callable<List<JavaSrcBean>>> repositoryTasks = new ArrayList<>();
        GenTableJava genTableJava = new GenTableJava(repositoryInst, tableProxy, tempIds, userSpace, chrome);
        repositoryTasks.add(genTableJava);
        List<JavaSrcBean> srcBeans = BuildFactory.getInstance().syncTasks(repositoryInst.getDsmId(), repositoryTasks);
        return srcBeans;
    }


    public List<Callable<List<JavaSrcBean>>> genTableJavaTask(RepositoryInst repositoryInst, Set<String> temps, UserSpace userSpace, List<String> tableNames, ChromeProxy chrome) throws JDSException {
        List<Callable<List<JavaSrcBean>>> repositoryTasks = new ArrayList<>();
        if (chrome == null) {
            chrome = this.getCurrChromeDriver();
        }

        if (tableNames == null || tableNames.isEmpty()) {
            throw new JDSException("请选择数据库表！");
        }

        List<DSMTableProxy> projectTables = new ArrayList<>();
        List<DSMTableProxy> allTables = getProjectTables(repositoryInst, userSpace);
        for (DSMTableProxy tableProxy : allTables) {
            if (tableNames.contains(tableProxy.getTableName())) {
                projectTables.add(tableProxy);
            }
        }


        if (temps == null || temps.isEmpty()) {
            temps = getDefaultTempIds(userSpace);
        }

        if (projectTables.size() > 0) {
            chrome.printLog("共发现" + projectTables.size() + "张数据库表", true);
            for (DSMTableProxy table : projectTables) {
                GenTableJava genTableJava = new GenTableJava(repositoryInst, table, temps, userSpace, chrome);
                repositoryTasks.add(genTableJava);
            }

            chrome.printLog("创建 关联关系", true);
            for (DSMTableProxy table : projectTables) {
                List<TableRef> refs = getTableRefByName(table.getTableName(), repositoryInst.getProjectVersionName());
                for (TableRef ref : refs) {
                    GenTableRefJava genTableRefJava = new GenTableRefJava(repositoryInst, ref, temps, chrome);
                    repositoryTasks.add(genTableRefJava);
                }
            }
            chrome.printLog("创建 导航页", true);
            GenTableRootJava tableRootJava = new GenTableRootJava(repositoryInst, projectTables, temps, userSpace, chrome);
            repositoryTasks.add(tableRootJava);

        } else {
            chrome.printLog("共发现" + projectTables.size() + "张数据库表", true);
        }


        return repositoryTasks;
    }

    public List<JavaSrcBean> genTableJava(RepositoryInst repositoryInst, Set<String> temps, UserSpace userSpace, List<String> tableNames, ChromeProxy chrome) throws JDSException {
        List<Callable<List<JavaSrcBean>>> repositoryTasks = genTableJavaTask(repositoryInst, temps, userSpace, tableNames, chrome);
        List<JavaSrcBean> srcBeans = BuildFactory.getInstance().syncTasks(repositoryInst.getDsmId(), repositoryTasks);
        return srcBeans;
    }

    public List<JavaSrcBean> genBPMTableJava(RepositoryInst repositoryInst, Set<String> temps, List<String> tableNames, ChromeProxy chrome) throws JDSException {
        List<Callable<List<JavaSrcBean>>> repositoryTasks = genTableJavaTask(repositoryInst, temps, UserSpace.FORM, tableNames, chrome);
        List<JavaSrcBean> srcBeans = BuildFactory.getInstance().syncTasks(repositoryInst.getDsmId(), repositoryTasks);
        return srcBeans;
    }

    private List<Callable<List<JavaSrcBean>>> genEntityJavaTask(RepositoryInst repositoryInst, Set<String> temps, ChromeProxy chrome) {
        List<Callable<List<JavaSrcBean>>> repositoryTasks = new ArrayList<>();
        for (String className : repositoryInst.getEntityNames()) {
            EntityConfig entityConfig = this.getEntityConfig(className, true);
            if (entityConfig != null) {
                GenEntityJava genEntityJava = new GenEntityJava(repositoryInst, entityConfig, temps, chrome);
                repositoryTasks.add(genEntityJava);
                if (this.getEntityRefByName(className, repositoryInst.getProjectVersionName()).size() > 0) {
                    EntityConfig mainClass = this.getEntityConfig(className, true);
                    GenEntityRefJava genEntityRefJava = new GenEntityRefJava(repositoryInst, mainClass, temps, chrome);
                    repositoryTasks.add(genEntityRefJava);
                }
            }
        }
        return repositoryTasks;
    }

    public List<JavaSrcBean> genEntityJava(RepositoryInst repositoryInst, Set<String> temps, ChromeProxy chrome) throws JDSException {
        List<Callable<List<JavaSrcBean>>> repositoryTasks = genEntityJavaTask(repositoryInst, temps, chrome);
        List<JavaSrcBean> srcBeans = BuildFactory.getInstance().syncTasks(repositoryInst.getDsmId(), repositoryTasks);
        return srcBeans;
    }


    public void buildTableByRepositoryType(RepositoryInst repositoryInst, List<String> tableNames, ChromeProxy chrome, RepositoryType... repositoryTypes) throws JDSException {
        Set<String> temps = getRepositoryTypeTemps(repositoryTypes);
        List<Callable<List<JavaSrcBean>>> tableTasks = genTableJavaTask(repositoryInst, temps, UserSpace.CRUD, tableNames, chrome);
        List<JavaSrcBean> srcBeans = buildTasks(repositoryInst, tableTasks, chrome);
    }


    public void buildCurdTable(RepositoryInst repositoryInst, Set<String> temps, List<String> tableNames, ChromeProxy chrome) throws JDSException {
        List<Callable<List<JavaSrcBean>>> tableTasks = genTableJavaTask(repositoryInst, temps, UserSpace.CRUD, tableNames, chrome);
        List<JavaSrcBean> srcBeans = buildTasks(repositoryInst, tableTasks, chrome);
    }

    public void buildBpmFormTable(RepositoryInst repositoryInst, Set<String> temps, List<String> tableNames, ChromeProxy chrome) throws JDSException {
        List<Callable<List<JavaSrcBean>>> tableTasks = genBPMFormTask(repositoryInst, temps, tableNames, chrome);
        List<JavaSrcBean> srcBeans = buildTasks(repositoryInst, tableTasks, chrome);
    }

    private List<JavaSrcBean> buildTasks(RepositoryInst repositoryInst, List<Callable<List<JavaSrcBean>>> tableTasks, ChromeProxy chrome) throws JDSException {
        List<JavaSrcBean> srcBeans = BuildFactory.getInstance().syncTasks(repositoryInst.getDsmId(), tableTasks);
        repositoryInst.updateBeans(new HashSet<>(srcBeans));
        for (JavaSrcBean javaSrcBean : srcBeans) {
            reloadEntityConfig(javaSrcBean.getClassName());
        }
        BuildFactory.getInstance().compileJavaSrc(srcBeans, repositoryInst.getProjectVersionName(), chrome);
        return srcBeans;
    }

    public void buildRepository(RepositoryInst repositoryInst, Set<String> temps, List<String> tableNames, ChromeProxy chrome) throws JDSException {
        if (chrome == null) {
            chrome = getCurrChromeDriver();
        }
        List<Callable<List<JavaSrcBean>>> repositoryTasks = new ArrayList<>();
        for (UserSpace userSpace : UserSpace.values()) {
            if (!userSpace.equals(UserSpace.FORM)) {
                List<Callable<List<JavaSrcBean>>> tableTasks = genTableJavaTask(repositoryInst, temps, userSpace, tableNames, chrome);
                repositoryTasks.addAll(tableTasks);
            }
        }

        List<Callable<List<JavaSrcBean>>> bpmFormTask = genBPMFormTask(repositoryInst, temps, tableNames, chrome);

        repositoryTasks.addAll(bpmFormTask);
        List<JavaSrcBean> srcBeans = BuildFactory.getInstance().syncTasks(repositoryInst.getDsmId(), repositoryTasks);
        // srcBeans.addAll(genTableJava(repositoryInst, temps, chrome));
        repositoryInst.updateBeans(new HashSet<>(srcBeans));
        for (JavaSrcBean javaSrcBean : srcBeans) {
            reloadEntityConfig(javaSrcBean.getClassName());
        }
        BuildFactory.getInstance().compileJavaSrc(srcBeans, repositoryInst.getProjectVersionName(), chrome);
    }


    public List<JavaSrcBean> genModuleViewJava(RepositoryInst repositoryInst, CustomViewBean viewBean, String moduleName, String className, ChromeProxy chrome) throws JDSException {
        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
        List<Callable<List<JavaSrcBean>>> votasks = genModuleViewJavaTask(repositoryInst, viewBean, moduleName, className, true, chrome, new RepositoryType[]{RepositoryType.VO, RepositoryType.VIEWBEAN, RepositoryType.REPOSITORY});
        String taskId = repositoryInst.getDsmId() + "[" + System.currentTimeMillis() + "]";
        List<JavaSrcBean> voList = BuildFactory.getInstance().syncTasks(taskId, votasks);
        javaSrcBeans.addAll(voList);
        taskId = repositoryInst.getDsmId() + "[" + System.currentTimeMillis() + "]";
        List<Callable<List<JavaSrcBean>>> impltasks = genModuleViewJavaTask(repositoryInst, viewBean, moduleName, className, false, chrome, new RepositoryType[]{RepositoryType.DO, RepositoryType.VIEWSERVICE, RepositoryType.REPOSITORYIMPL});
        List<JavaSrcBean> implList = BuildFactory.getInstance().syncTasks(taskId, impltasks);
        javaSrcBeans.addAll(implList);
        List<String> repositoryClassList = new ArrayList<>();
        for (JavaSrcBean javaSrcBean : javaSrcBeans) {
            repositoryClassList.add(javaSrcBean.getClassName());
        }
        viewBean.getViewJavaSrcBean().setRepositoryClassList(repositoryClassList);
        return javaSrcBeans;
    }

    public List<Callable<List<JavaSrcBean>>> genModuleViewJavaTask(RepositoryInst repositoryInst, CustomViewBean viewBean, String moduleName, String className, boolean clear, ChromeProxy chrome, RepositoryType... repositoryTypes) throws JDSException {
        List<Callable<List<JavaSrcBean>>> repositoryTasks = new ArrayList<>();
        AggViewRoot aggViewRoot = new AggViewRoot(repositoryInst);
        GenRepositoryViewJava genRepositoryViewJava = new GenRepositoryViewJava(aggViewRoot, viewBean, moduleName, className, clear, chrome, repositoryTypes);
        repositoryTasks.add(genRepositoryViewJava);
        return repositoryTasks;
    }

    public List<JavaSrcBean> genDicJava(RepositoryInst repositoryInst, List<? extends TabListItem> items, String moduleName, String className, ChromeProxy chrome) throws JDSException {
        List<Callable<List<JavaSrcBean>>> dicTasks = new ArrayList<>();
        List<JavaSrcBean> rootBeans = new ArrayList<>();
        for (TabListItem subItem : items) {
            List<Callable<List<JavaSrcBean>>> genJavaTasks = genDicJavaTask(repositoryInst, subItem, moduleName + "." + OODUtil.formatJavaName(subItem.getId(), false), OODUtil.formatJavaName(subItem.getId(), true), chrome);
            dicTasks.addAll(genJavaTasks);
        }
        List<JavaSrcBean> javaSrcBeans = BuildFactory.getInstance().syncTasks(repositoryInst.getDsmId(), dicTasks);
        //重做一遍重新绑定
        javaSrcBeans = BuildFactory.getInstance().syncTasks(repositoryInst.getDsmId(), dicTasks);
        try {
            GenDicJava genDicJava = new GenDicJava(repositoryInst, items, moduleName, className);
            rootBeans = genDicJava.call();
            javaSrcBeans.addAll(rootBeans);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (JavaSrcBean srcBean : javaSrcBeans) {
            repositoryInst.addJavaBean(srcBean);
        }
        this.updateRepositoryInst(repositoryInst, true);
        return rootBeans;

    }


    public List<Callable<List<JavaSrcBean>>> genDicJavaTask(RepositoryInst repositoryInst, TabListItem tabItem, String moduleName, String className, ChromeProxy chrome) throws JDSException {
        List<Callable<List<JavaSrcBean>>> dicTasks = new ArrayList<>();
        if (tabItem instanceof TreeListItem) {
            TreeListItem treeItem = (TreeListItem) tabItem;
            List<TreeListItem> items = treeItem.getSub();
            if (items != null) {
                for (TreeListItem subItem : items) {
                    List<TabListItem> treeListItems = subItem.getSub();
                    if (treeListItems != null && treeListItems.size() > 0) {
                        GenDicJava genDicJava = new GenDicJava(repositoryInst, treeListItems, moduleName, className);
                        dicTasks.add(genDicJava);
                        List<Callable<List<JavaSrcBean>>> genJavaTasks = genDicJavaTask(repositoryInst, subItem, moduleName + "." + OODUtil.formatJavaName(subItem.getId(), false), OODUtil.formatJavaName(subItem.getId(), true), chrome);
                        dicTasks.addAll(genJavaTasks);
                    }
                }
            }
        }
        return dicTasks;
    }


    public void addEntity(String projectVersionName, String... esdClassNames) throws JDSException {
        RepositoryInst bean = this.getProjectRepository(projectVersionName);
        if (bean != null) {
            Set<String> esdClassNameList = bean.getEntityNames();
            for (String esdClassName : esdClassNames) {
                try {
                    Class clazz = ClassUtility.loadClass(esdClassName);
                    if (!esdClassNameList.contains(esdClassName) && clazz != null) {
                        esdClassNameList.add(esdClassName);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
            bean.getEntityNames().addAll(esdClassNameList);
            updateRepositoryInst(bean, true);
        }
    }

    public void addUrls(String projectVersionName, String... urls) throws JDSException {
        RepositoryInst bean = this.getProjectRepository(projectVersionName);
        if (bean != null) {
            Set<String> urlNames = bean.getUrlNames();
            bean.getUrlNames().addAll(urlNames);
            updateRepositoryInst(bean, true);
        }
    }

    public ChromeProxy getCurrChromeDriver() {
        ChromeProxy chrome = JDSActionContext.getActionContext().Par("$currChromeDriver", ChromeProxy.class);
        if (chrome == null) {
            chrome = new LogSetpLog();
        }
        return chrome;
    }

    public CtVfsService getVfsClient() {
        CtVfsService vfsClient = CtVfsFactory.getCtVfsService();
        return vfsClient;
    }

}
