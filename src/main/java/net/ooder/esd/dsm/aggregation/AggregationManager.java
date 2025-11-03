package net.ooder.esd.dsm.aggregation;

import com.alibaba.fastjson.JSON;
import net.ooder.annotation.Aggregation;
import net.ooder.annotation.AggregationType;
import net.ooder.annotation.RefType;
import net.ooder.annotation.UserSpace;
import net.ooder.common.JDSConstants;
import net.ooder.common.JDSException;
import net.ooder.common.cache.Cache;
import net.ooder.common.cache.CacheManagerFactory;
import net.ooder.common.logging.ChromeProxy;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.common.logging.LogSetpLog;
import net.ooder.common.util.ClassUtility;
import net.ooder.common.util.FileUtility;
import net.ooder.common.util.StringUtility;
import net.ooder.context.JDSActionContext;
import net.ooder.context.JDSContext;
import net.ooder.esd.bean.CustomRefBean;
import net.ooder.esd.bean.CustomViewBean;
import net.ooder.esd.bean.view.CustomModuleBean;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.custom.ESDClassManager;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.context.AggRoot;
import net.ooder.esd.dsm.aggregation.context.AggViewRoot;
import net.ooder.esd.dsm.aggregation.ref.AggEntityRef;
import net.ooder.esd.dsm.enums.DSMType;
import net.ooder.esd.dsm.enums.RangeType;
import net.ooder.esd.dsm.gen.GenJava;
import net.ooder.esd.dsm.gen.agg.*;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.temp.JavaTemp;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;
import net.ooder.esd.engine.MySpace;
import net.ooder.esd.engine.ProjectCacheManager;
import net.ooder.esd.engine.config.dsm.DSMProjectConfig;
import net.ooder.esd.engine.enums.PackagePathType;
import net.ooder.esd.engine.enums.PackageType;
import net.ooder.esd.engine.inner.INProject;
import net.ooder.esd.util.OODUtil;
import net.ooder.server.JDSServer;
import net.ooder.server.context.MinServerActionContextImpl;
import net.ooder.vfs.FileInfo;
import net.ooder.vfs.Folder;
import net.ooder.vfs.VFSConstants;
import net.ooder.vfs.ct.CtVfsFactory;
import net.ooder.vfs.ct.CtVfsService;
import net.ooder.web.APIConfig;
import net.ooder.web.APIConfigFactory;
import net.ooder.web.RemoteConnectionManager;
import net.ooder.web.RequestMappingBean;
import net.ooder.web.util.MethodUtil;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class AggregationManager {
    private static final String THREAD_LOCK = "Thread Lock";

    public static final String AggConfigPath = "aggclassconfig";

    public static final String APIConfigPath = "apiclassconfig";

    public static final String DSMCacheName = "dsm";

    protected Log log = LogFactory.getLog(JDSConstants.CONFIG_KEY, AggregationManager.class);

    private final ProjectCacheManager projectCacheManager;

    private final MySpace space;

    private Folder aggClassConfigFolder;

    private Folder apiClassConfigFolder;

    Map<String, AggEntityConfig> aggEntityConfigMap = new HashMap<>();

    Map<String, ApiClassConfig> apiConfigMap = new HashMap<>();

    Map<String, String> domainIdMap = new HashMap<>();

    Cache<String, String> apiConfigCache;

    Cache<String, String> aggEntityConfigCache;

    List<SaveApiEntityConfigTask<ApiClassConfig>> apiConfigTasks = new ArrayList<>();

    List<SaveAggEntityConfigTask<AggEntityConfig>> aggEntityConfigTasks = new ArrayList<>();

    ESDClassManager classManager;

    BuildFactory buildFactory;


    private static Map<String, AggregationManager> managerMap = new HashMap<>();

    public static AggregationManager getInstance(MySpace space) {
        String path = space.getPath();
        AggregationManager manager = managerMap.get(path);
        if (manager == null) {
            synchronized (THREAD_LOCK) {
                if (manager == null) {
                    manager = new AggregationManager(space);
                    managerMap.put(path, manager);
                }
            }
        }
        return manager;
    }

    AggregationManager(MySpace space) {
        Long start = System.currentTimeMillis();
        this.space = space;
        this.projectCacheManager = ProjectCacheManager.getInstance(space);
        this.classManager = ESDClassManager.getInstance(space);
        this.buildFactory = BuildFactory.getInstance(space);
        apiConfigCache = CacheManagerFactory.createCache(DSMCacheName, "apiConfig", 150 * 1024 * 1024, 1000 * 60 * 60 * 24 * 7);
        aggEntityConfigCache = CacheManagerFactory.createCache(DSMCacheName, "aggEntityConfig", 150 * 1024 * 1024, 1000 * 60 * 60 * 24 * 7);


        try {

            this.aggClassConfigFolder = space.getRootfolder().createChildFolder(AggConfigPath, JDSServer.getInstance().getAdminUser().getId());
            this.apiClassConfigFolder = space.getRootfolder().createChildFolder(APIConfigPath, JDSServer.getInstance().getAdminUser().getId());
            //批量预读配置
            Set<String> versions = new HashSet<>();
            Set<String> classNameSet = new HashSet<>();
            List<FileInfo> fileInfos = aggClassConfigFolder.getFileListRecursively();
            for (FileInfo fileInfo : fileInfos) {
                versions.add(fileInfo.getCurrentVersonId());
                if (fileInfo.getDescrition() != null && fileInfo.getDescrition().startsWith("net.ooder.dsm")) {
                    classNameSet.add(fileInfo.getDescrition());
                }

            }
            List<FileInfo> apiFiles = apiClassConfigFolder.getFileListRecursively();
            for (FileInfo fileInfo : apiFiles) {
                versions.add(fileInfo.getCurrentVersonId());
                if (fileInfo.getDescrition() != null && fileInfo.getDescrition().startsWith("net.ooder.dsm")) {
                    classNameSet.add(fileInfo.getDescrition());
                }
            }


            buildFactory.syncEsdClassTasks(AggConfigPath, classNameSet);
        } catch (JDSException e) {
            e.printStackTrace();
        }


    }


    public Map getContextEntityConfigMap() {
        Map contextEntityConfigMap = (Map) JDSActionContext.getActionContext().getContext().get("contextEntityConfigMap");
        if (contextEntityConfigMap == null) {
            contextEntityConfigMap = new HashMap();
            JDSActionContext.getActionContext().getContext().put("contextEntityConfigMap", contextEntityConfigMap);
        }
        return contextEntityConfigMap;
    }

    public AggEntityConfig getAggEntityConfig(String className, boolean reload) throws JDSException {
        if (className == null || className.equals("") || !MethodUtil.checkType(className)) {
            return null;
        }

        Map actionContext = JDSActionContext.getActionContext().getContext();
        String aggClassName = "AggEntityConfig[" + className + "]";
        AggEntityConfig esdClassConfig = (AggEntityConfig) actionContext.get(aggClassName);

        if (esdClassConfig == null || reload) {
            Class clazz = classManager.checkInterface(className);
            if (clazz != null) {
                className = clazz.getName();
                String domainId = getRealDomainId(className, true);
                if (ClassUtility.isDebug(className)) {
                    ESDClass esdClass = classManager.getAggEntityByName(className, reload);
                    esdClassConfig = new AggEntityConfig(esdClass, domainId);
                    esdClassConfig.setSourceClassName(className);
                } else {
                    String uKey = className + "[" + domainId + "]";
                    esdClassConfig = aggEntityConfigMap.get(uKey);
                    if (esdClassConfig == null || (reload && esdClassConfig.getLastUpdateTime() != null)) {
                        synchronized (uKey) {
                            if (reload) {
                                esdClassConfig = createAggEntityConfig(className, domainId);
                                aggEntityConfigMap.put(uKey, esdClassConfig);
                            } else if (esdClassConfig == null) {
                                esdClassConfig = loadAggEntityConfig(className, domainId);
                                if (esdClassConfig == null) {
                                    esdClassConfig = createAggEntityConfig(className, domainId);
                                    esdClassConfig.setDomainId(domainId);
                                    aggEntityConfigMap.put(uKey, esdClassConfig);
                                    SaveAggEntityConfigTask task = new SaveAggEntityConfigTask(esdClassConfig);
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            task.call();
                                        }
                                    }).start();
                                }
                            }
                        }
                        aggEntityConfigMap.put(uKey, esdClassConfig);
                    }
                }
                actionContext.put(aggClassName, esdClassConfig);
            }
        }


        return esdClassConfig;
    }


    private AggEntityConfig createAggEntityConfig(String className, String domainId) {
        String uKey = className + "[" + domainId + "]";

        AggEntityConfig esdClassConfig = aggEntityConfigMap.get(uKey);
        if (esdClassConfig == null) {
            ESDClass esdClass = classManager.getAggEntityByName(className, true);
            if (esdClass != null) {
                esdClass.initField();
                esdClassConfig = new AggEntityConfig(esdClass, domainId);
                esdClassConfig.setDomainId(domainId);
                esdClassConfig.setSourceClassName(className);
                aggEntityConfigMap.put(uKey, esdClassConfig);
                aggEntityConfigCache.put(uKey, JSON.toJSONString(esdClassConfig));


            }
        }
        return esdClassConfig;
    }

    private AggEntityConfig loadAggEntityConfig(String className, String domainId) {
        AggEntityConfig esdClassConfig = null;
        String uKey = className + "[" + domainId + "]";
        try {
            long updateTime = System.currentTimeMillis();
            String json = aggEntityConfigCache.get(uKey);

            if (json == null) {
                Folder dsmFolder = this.getVfsClient().getFolderByPath(aggClassConfigFolder.getPath() + domainId);
                if (dsmFolder == null) {
                    dsmFolder = aggClassConfigFolder.createChildFolder(domainId, JDSServer.getInstance().getAdminUser().getId());
                }
                String folerPath = formartPackagePath(className);
                String simClassName = formartClassName(className);
                String fileName = simClassName + ".entity";
                Folder packageFolder = getVfsClient().mkDir(dsmFolder.getPath() + folerPath);
                FileInfo fileInfo = this.getVfsClient().getFileByPath(packageFolder.getPath() + fileName);

                if (fileInfo != null && fileInfo.getCurrentVersion().getLength() > 0) {
                    updateTime = fileInfo.getCurrentVersion().getCreateTime();
                    StringBuffer buffer = this.getVfsClient().readFileAsString(fileInfo.getPath(), VFSConstants.Default_Encoding);
                    json = buffer.toString();
                    aggEntityConfigCache.put(uKey, json);
                }
            }

            if (json != null) {
                esdClassConfig = JSON.parseObject(json, AggEntityConfig.class);
            }

            if (esdClassConfig == null) {
                esdClassConfig = createAggEntityConfig(className, domainId);
            }
            if (esdClassConfig != null) {
                esdClassConfig.setLastUpdateTime(updateTime);
            }

        } catch (Throwable e) {
            aggEntityConfigCache.remove(uKey);
            e.printStackTrace();
        }
        return esdClassConfig;
    }

    public Set<APIConfig> getDynAggRoot(String domainId, String projectName) {
        DomainInst domainInst = this.getDomainInstById(domainId, projectName);
        List<JavaSrcBean> srcBeans = domainInst.getJavaSrcBeans();
        Set<APIConfig> aggRoots = new LinkedHashSet<>();
        for (JavaSrcBean srcBean : srcBeans) {
            try {
                Class clazz = null;
                try {
                    clazz = ClassUtility.loadClass(srcBean.getClassName());
                } catch (ClassNotFoundException e) {
                    GenJava javaGen = GenJava.getInstance(domainInst.getProjectVersionName());
                    clazz = ClassUtility.loadClassByFile(javaGen.getJavaBuildPath(), srcBean.getClassName());
                }
                JavaTemp javaTemp = BuildFactory.getInstance().getTempManager().getJavaTempById(srcBean.getJavaTempId());
                if (clazz != null && javaTemp.getDsmType().equals(DSMType.AGGREGATION)) {
                    APIConfig apiConfig = APIConfigFactory.getInstance().getAPIConfig(clazz.getName());
                    aggRoots.add(apiConfig);
                }


            } catch (Exception e1) {
                log.error(e1);
                //e1.printStackTrace();
            }

        }
        return aggRoots;
    }

    public List<AggEntityConfig> getAggEntityConfigs() {
        List<AggEntityConfig> esdClassList = new ArrayList<>();
        aggEntityConfigMap.forEach((k, v) -> {
            if (!esdClassList.contains(v)) {
                esdClassList.add(v);
            }

        });
        return esdClassList;
    }

    public List<JavaSrcBean> reBuildServiceBean(DomainInst dsmInst, List<AggEntityConfig> entityConfigList, AggregationType aggregationType, ChromeProxy chrome, boolean compile) throws JDSException {
        List<Callable<List<JavaSrcBean>>> buildTasks = new ArrayList<>();
        if (chrome == null) {
            chrome = this.getCurrChromeDriver();
        }
        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
        for (AggEntityConfig entityConfig : entityConfigList) {
            if (entityConfig.getAggregationBean() != null) {
                if (entityConfig.getESDClass().isProxy()) {
                    buildTasks.add(new GenAggAPIJava(dsmInst, entityConfig, dsmInst.getJavaTempIds(), chrome));
                } else {
                    buildTasks.add(new GenAggEntityJava(dsmInst, entityConfig, aggregationType, dsmInst.getJavaTempIds(), chrome));
                }

            }
        }
        javaSrcBeans = BuildFactory.getInstance().syncTasks(dsmInst.getDomainId(), buildTasks);
        chrome.printLog("共创建:" + javaSrcBeans.size() + "个 ，开始编译动态编译...", true);
        if (compile) {
            GenJava javaGen = GenJava.getInstance(dsmInst.getProjectVersionName());
            javaGen.compileJavaSrc(javaSrcBeans, chrome);
        }
        updateDomainInst(dsmInst, true);
        return javaSrcBeans;
    }

    private List<Callable<List<JavaSrcBean>>> genAggAPIJavaTask(DomainInst domainInst, AggEntityConfig esdClassConfig, Set<String> allTemps, ChromeProxy chrome) {
        List<Callable<List<JavaSrcBean>>> buildTasks = new ArrayList<>();
        GenAggAPIJava genAggAPIJava = new GenAggAPIJava(domainInst, esdClassConfig, allTemps, chrome);
        buildTasks.add(genAggAPIJava);
        return buildTasks;
    }

    public void genServiceBean(DomainInst dsmInst, AggregationType aggregationType, ChromeProxy chrome, String projectName, boolean compile) throws JDSException {
        Set<String> viewTemps = new HashSet<>();
        Set<String> temps = dsmInst.getJavaTempIds();
        for (String javaTempId : temps) {
            JavaTemp javatemp = BuildFactory.getInstance().getTempManager().getJavaTempById(javaTempId);
            if (javatemp != null) {
                viewTemps.add(javaTempId);
            }
        }
        buildAggregation(dsmInst, aggregationType, viewTemps, projectName, chrome);
        if (compile) {
            DSMFactory.getInstance().compileDomainInst(dsmInst, chrome);
        }
        updateDomainInst(dsmInst, true);
    }

    public AggEntityRef getAggEntityRefById(String refId, String domainId, String projectName) {
        DomainInst domainInst = this.getDomainInstById(domainId, projectName);
        Map<String, AggEntityRef> refMap = domainInst.getAggEntityRefMap();
        return refMap.get(refId);
    }

    public Set<AggEntityRef> getEntityRefByName(String className, String domainId, RefType refType, String projectName) {
        Set<AggEntityRef> refs = new HashSet<>();
        List<AggEntityRef> entityRefs = getEntityRefByName(className, domainId, projectName);
        for (AggEntityRef ref : entityRefs) {
            if (ref.getRefBean().getRef().equals(refType)) {
                refs.add(ref);
            }
        }
        return refs;
    }

    public List<AggEntityRef> getEntityRefByName(String className, String domainId, String projectName) {
        List<AggEntityRef> dsmRefs = new ArrayList<>();
        DomainInst domainInst = this.getDomainInstById(domainId, projectName);
        Map<String, AggEntityRef> refMap = domainInst.getAggEntityRefMap();
        refMap.forEach((k, v) -> {
            if (v.getRefId() == null || v.getRefId().equals("")) {
                v.setRefId(UUID.randomUUID().toString());
            }
            if (v.getClassName().toLowerCase().equals(className.toLowerCase()) && v.getDomainId() != null && v.getDomainId().equals(domainId)) {
                v.setRefId(k);
                dsmRefs.add(v);
            }
        });


        if (dsmRefs.isEmpty()) {
            try {
                AggEntityConfig aggConfigBean = this.getAggEntityConfig(className, false);
                dsmRefs.addAll(aggConfigBean.getRefs());
                for (AggEntityRef ref : dsmRefs) {
                    refMap.put(ref.getRefId(), ref);
                }

            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        return dsmRefs;
    }

    public AggEntityRef getAggEntityRef(String domainId, String className, String otherClassName, String projectName) {
        List<AggEntityRef> dsmRefs = this.getEntityRefByName(className, domainId, projectName);
        for (AggEntityRef dsmRef : dsmRefs) {
            if (dsmRef.getOtherClassName().toLowerCase().equals(otherClassName.toLowerCase())) {
                return dsmRef;
            }
        }
        return null;
    }

    public void deleteApiClassConfig(ApiClassConfig apiClassConfig) throws JDSException {
        if (apiClassConfig != null) {
            deleteApiClassConfig(apiClassConfig.getServiceClass(), false);
        }
    }

    public void deleteApiClassConfig(Set<String> classNames) throws JDSException {
        for (String esdClassName : classNames) {
            try {
                deleteApiClassConfig(esdClassName, false);
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteApiClassConfig(String className, boolean clear) throws JDSException {
        Class clazz = classManager.checkInterface(className);
        if (clazz != null) {
            className = clazz.getName();
            String domainId = getRealDomainId(className, true);
            String uKey = className + "[" + domainId + "]";
            apiConfigMap.remove(uKey);
            classManager.clear(className);
            aggEntityConfigMap.remove(uKey);
            apiConfigCache.remove(uKey);
            aggEntityConfigCache.remove(uKey);
            Folder dsmFolder = apiClassConfigFolder.createChildFolder(domainId, JDSServer.getInstance().getAdminUser().getId());
            String folerPath = formartPackagePath(className);
            String simClassName = formartClassName(className);
            String fileName = simClassName + ".api";
            Folder packageFolder = this.getVfsClient().mkDir(dsmFolder.getPath() + folerPath);
            FileInfo fileInfo = this.getVfsClient().getFileByPath(packageFolder.getPath() + fileName);
            if (fileInfo != null) {
                this.getVfsClient().deleteFile(fileInfo.getID());
            }
        }
    }


    public void updateAggEntityConfig(AggEntityConfig esdClassConfig) throws JDSException {
        Class sourceClazz = classManager.checkInterface(esdClassConfig.getSourceClassName());
        if (sourceClazz != null) {
            String className = sourceClazz.getName();
            CustomViewFactory.getInstance().reLoad();
            String oDomainId = esdClassConfig.getDomainId();
            String domainId = getRealDomainId(className, false);
            if (oDomainId != null && !oDomainId.equals(domainId)) {
                domainId = getRealDomainId(className, true);
            }
            String uKey = className + "[" + domainId + "]";
            String content = JSON.toJSONString(esdClassConfig);
            aggEntityConfigCache.put(uKey, content);
            aggEntityConfigMap.put(uKey, esdClassConfig);
            Folder dsmFolder = aggClassConfigFolder.createChildFolder(domainId, JDSServer.getInstance().getAdminUser().getId());
            String folerPath = formartPackagePath(className);
            String simClassName = formartClassName(className);
            String fileName = simClassName + ".entity";
            Folder packageFolder = this.getVfsClient().mkDir(dsmFolder.getPath() + folerPath);
            FileInfo fileInfo = packageFolder.createFile(fileName, className, null);
            this.getVfsClient().saveFileAsContent(fileInfo.getPath(), content, VFSConstants.Default_Encoding);
        }


    }

    public void updateApiClassConfigs(Set<ApiClassConfig> apiClassConfigSet) throws JDSException {
        for (ApiClassConfig apiClassConfig : apiClassConfigSet) {
            SaveApiEntityConfigTask task = new SaveApiEntityConfigTask(apiClassConfig);
            this.apiConfigTasks.add(task);
        }
        this.commitTask();
    }

    public void updateApiClassConfig(ApiClassConfig apiClassConfig) throws JDSException {
        Map context = JDSActionContext.getActionContext().getContext();
        Class serviceClass = classManager.checkInterface(apiClassConfig.getServiceClass());
        if (serviceClass != null) {
            CustomViewFactory.getInstance().reLoad();
            String className = serviceClass.getName();
            String oDomainId = apiClassConfig.getDomainId();
            String domainId = getRealDomainId(className, false);
            if (oDomainId != null && !oDomainId.equals(domainId)) {
                domainId = getRealDomainId(className, true);
            }
            String uKey = className + "[" + domainId + "]";
            if (!context.containsKey(uKey) && domainId != null) {
                String content = JSON.toJSONString(apiClassConfig);
                apiConfigMap.put(uKey, apiClassConfig);
                apiConfigCache.put(uKey, content);
                Folder dsmFolder = apiClassConfigFolder.createChildFolder(domainId, JDSServer.getInstance().getAdminUser().getId());
                String folerPath = formartPackagePath(className);
                String simClassName = formartClassName(className);
                String fileName = simClassName + ".api";
                Folder packageFolder = getVfsClient().mkDir(dsmFolder.getPath() + folerPath);
                FileInfo fileInfo = packageFolder.createFile(fileName, apiClassConfig.getServiceClass(), null);
                this.getVfsClient().saveFileAsContent(fileInfo.getPath(), content, VFSConstants.Default_Encoding);
                context.put(uKey, apiClassConfig);
            }
        }

    }

    public ApiClassConfig getApiClassConfig(String className) {
        return getApiClassConfig(className, false);
    }

    public ApiClassConfig getApiClassConfig(String className, boolean load) {
        if (className == null || className.equals("") || !MethodUtil.checkType(className)) {
            return null;
        }
        String apiClassName = "ApiClassConfig[" + className + "]";
        Map actionContext = JDSActionContext.getActionContext().getContext();
        ApiClassConfig apiClassConfig = (ApiClassConfig) actionContext.get(apiClassName);

        if (apiClassConfig == null || load) {
            Class clazz = classManager.checkInterface(className);
            if (clazz != null) {
                try {
                    className = clazz.getName();
                    String domainId = getRealDomainId(className, true);
                    if (ClassUtility.isDebug(className)) {
                        ESDClass esdClass = classManager.getAggEntityByName(className, load);
                        apiClassConfig = new ApiClassConfig(esdClass);
                        apiClassConfig.setDomainId(domainId);
                    } else {
                        String uKey = className + "[" + domainId + "]";
                        apiClassConfig = apiConfigMap.get(uKey);
                        if (apiClassConfig == null || load) {
                            synchronized (uKey) {
                                apiClassConfig = loadApiConfig(className, domainId);
                                if (apiClassConfig == null || load) {
                                    ESDClass esdClass = classManager.getAggEntityByName(className, load);
                                    if (esdClass != null) {
                                        apiClassConfig = new ApiClassConfig(esdClass);
                                        apiClassConfig.setDomainId(domainId);
                                        apiConfigMap.put(uKey, apiClassConfig);
                                        //异步持久化
                                        SaveApiEntityConfigTask task = new SaveApiEntityConfigTask(apiClassConfig);
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                task.call();
                                            }
                                        }).start();

                                    }
                                } else {
                                    apiConfigMap.put(uKey, apiClassConfig);
                                }
                            }
                        }
                    }
                    actionContext.put(apiClassName, apiClassConfig);
                } catch (JDSException e) {
                    e.printStackTrace();
                }
            }
        }


        return apiClassConfig;
    }


    private ApiClassConfig loadApiConfig(String className, String domainId) {
        ApiClassConfig apiClassConfig = null;
        String uKey = className + "[" + domainId + "]";
        try {

            String json = apiConfigCache.get(uKey);
            if (json == null) {
                Folder dsmFolder = this.getVfsClient().getFolderByPath(apiClassConfigFolder.getPath() + domainId);
                if (dsmFolder == null) {
                    dsmFolder = apiClassConfigFolder.createChildFolder(domainId, JDSServer.getInstance().getAdminUser().getId());
                }
                String folerPath = formartPackagePath(className);
                String simClassName = formartClassName(className);
                String fileName = simClassName + ".api";
                Folder packageFolder = getVfsClient().mkDir(dsmFolder.getPath() + folerPath);
                FileInfo fileInfo = this.getVfsClient().getFileByPath(packageFolder.getPath() + fileName);
                if (fileInfo != null) {
                    StringBuffer buffer = this.getVfsClient().readFileAsString(fileInfo.getPath(), VFSConstants.Default_Encoding);
                    json = buffer.toString();
                    apiClassConfig = JSON.parseObject(json, ApiClassConfig.class);
                    apiConfigCache.put(uKey, json);
                }
            }

            if (json != null) {
                apiClassConfig = JSON.parseObject(json, ApiClassConfig.class);
            }


        } catch (Throwable e) {
            apiConfigCache.remove(uKey);
            try {
                this.deleteApiClassConfig(className, true);
            } catch (JDSException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }


        return apiClassConfig;
    }


    public void addAggEntity(String domainId, Set<String> esdClassNames, String projectName, boolean buildDomain, ChromeProxy chrome) throws JDSException {
        DomainInst domainInst = getDomainInstById(domainId, projectName);
        List<ESDClass> esdClassList = new ArrayList<>();
        if (chrome == null) {
            chrome = this.getCurrChromeDriver();
        }
        chrome.printLog("开始创建聚合应用...", true);
        for (String esdClassName : esdClassNames) {
            esdClassList.add(BuildFactory.getInstance().getClassManager().getAggEntityByName(esdClassName, true));
        }
        List<JavaSrcBean> srcBeans = DSMFactory.getInstance().getViewManager().reBuildESDClassView(domainInst, esdClassList, chrome);
        chrome.printLog("共完成编译" + esdClassList.size() + "个", true);
        if (buildDomain) {
            BuildFactory.getInstance().compileJavaSrc(srcBeans, projectName, chrome);
            chrome.printLog("共完成编译" + esdClassList.size() + "个", true);
        }
        updateDomainInst(domainInst, true);
    }


    public void addAggTable(String domainId, Set<String> esdClassNames, String projectName, boolean buildDomain, ChromeProxy chrome) throws JDSException {
        DomainInst domainInst = getDomainInstById(domainId, projectName);
        List<ESDClass> esdClassList = new ArrayList<>();
        List<JavaSrcBean> srcBeans = new ArrayList<>();
        if (chrome == null) {
            chrome = this.getCurrChromeDriver();
        }
        chrome.printLog("开始创建聚合应用...", true);
        for (String esdClassName : esdClassNames) {
            esdClassList.add(BuildFactory.getInstance().getClassManager().getAggEntityByName(esdClassName, true));
        }
        List<JavaSrcBean> javaSrcBeans = DSMFactory.getInstance().getViewManager().reBuildESDClassView(domainInst, esdClassList, chrome);
        srcBeans.addAll(javaSrcBeans);
        for (String esdClassName : esdClassNames) {
            try {
                List<JavaSrcBean> moduleBeans = this.buildAggModule(domainInst, AggregationType.REPOSITORY, esdClassName, chrome);
                srcBeans.addAll(moduleBeans);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (buildDomain) {
            BuildFactory.getInstance().compileJavaSrc(srcBeans, projectName, chrome);
            chrome.printLog("共完成编译" + esdClassList.size() + "个", true);
        }

        updateDomainInst(domainInst, true);
    }

    public List<JavaSrcBean> buildAggTableView(String domainId, Set<String> esdClassNames, String projectName, ChromeProxy chrome) throws JDSException {
        DomainInst domainInst = getDomainInstById(domainId, projectName);
        List<ESDClass> esdClassList = new ArrayList<>();
        List<JavaSrcBean> srcBeans = new ArrayList<>();
        if (chrome == null) {
            chrome = this.getCurrChromeDriver();
        }
        chrome.printLog("开始创建聚合应用...", true);
        for (String esdClassName : esdClassNames) {
            esdClassList.add(BuildFactory.getInstance().getClassManager().getAggEntityByName(esdClassName, true));
        }
        List<JavaSrcBean> javaSrcBeans = DSMFactory.getInstance().getViewManager().reBuildESDClassView(domainInst, esdClassList, chrome);
        srcBeans.addAll(javaSrcBeans);

        return srcBeans;
    }


    public void addAggMenu(String domainId, Set<String> esdClassNames, String projectName) throws JDSException {
        DomainInst bean = getDomainInstById(domainId, projectName);

    }

    public void addAggDomain(String domainId, Set<String> esdClassNames, String projectName) throws JDSException {
        DomainInst bean = getDomainInstById(domainId, projectName);

    }

    public void addAggView(String domainId, Set<String> esdClassNames, String projectName) throws JDSException {
        DomainInst bean = getDomainInstById(domainId, projectName);

    }

    public void addAggAPI(String domainId, Set<String> esdClassNames, String projectName) throws JDSException {
        DomainInst bean = getDomainInstById(domainId, projectName);

    }

    public void addAggRoot(String domainId, Set<String> esdClassNames, String projectName) throws JDSException {
        DomainInst bean = getDomainInstById(domainId, projectName);
    }

    public void addAggSet(String domainId, Set<String> esdClassNames, String projectName) throws JDSException {
        DomainInst bean = getDomainInstById(domainId, projectName);
    }

    public void addAggMap(String domainId, Set<String> esdClassNames, String projectName) throws JDSException {
        DomainInst bean = getDomainInstById(domainId, projectName);
    }


    public List<JavaSrcBean> buildAggModule(DomainInst dsmInst, AggregationType aggregationType, String esdClassName, ChromeProxy chrome) throws JDSException, IOException {
        Set<String> tempList = new LinkedHashSet<>();
        for (String javaTempId : dsmInst.getJavaTempIds()) {
            JavaTemp javatemp = BuildFactory.getInstance().getTempManager().getJavaTempById(javaTempId);
            if (javatemp != null) {
                tempList.add(javaTempId);
            }
        }
        return buildAggModule(dsmInst, aggregationType, esdClassName, tempList, chrome);
    }

    public void clear() {
        aggEntityConfigMap.clear();
        domainIdMap.clear();
    }

    public void clearAggEntityConfig(String domainId) throws JDSException {
        if (domainId == null || domainId.equals("")) {
            throw new JDSException("domainId is null!");
        }
        Folder dsmFolder = aggClassConfigFolder.createChildFolder(domainId, JDSServer.getInstance().getAdminUser().getId());
        CtVfsFactory.getCtVfsService().deleteFolder(dsmFolder.getID());
    }

    public void clearApiClassConfig(String domainId) throws JDSException {
        if (domainId == null || domainId.equals("")) {
            throw new JDSException("domainId is null!");
        }
        Folder dsmFolder = apiClassConfigFolder.createChildFolder(domainId, JDSServer.getInstance().getAdminUser().getId());
        CtVfsFactory.getCtVfsService().deleteFolder(dsmFolder.getID());
    }

    private void clearEntitySrc(String className, String domainId, String projectName) throws JDSException {
        DomainInst domainInst = DSMFactory.getInstance().getAggregationManager().getDomainInstById(domainId, projectName);
        List<JavaSrcBean> srcBeans = domainInst.getJavaSrcBeans();
        for (JavaSrcBean javaSrcBean : srcBeans) {
            if (javaSrcBean.getClassName() != null && javaSrcBean.getClassName().equals(className)) {
                File file = javaSrcBean.getFile();
                try {
                    if (file.exists()) {
                        FileUtility.deleteDirectory(file.getParentFile());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void clearEntityConfig(String domainId, String projectName) throws JDSException {
        DomainInst domainInst = DSMFactory.getInstance().getAggregationManager().getDomainInstById(domainId, projectName);
        List<JavaSrcBean> srcBeans = domainInst.getJavaSrcBeans();
        for (JavaSrcBean javaSrcBean : srcBeans) {
            try {
                this.delAggEntityClass(javaSrcBean.getClassName(), projectName, true);
                File file = javaSrcBean.getFile();
                FileUtility.deleteDirectory(file.getParentFile());
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public void reload() {
        aggEntityConfigMap.clear();
        apiConfigMap.clear();
    }

    public void delAggView(String domainId, Set<String> esdClassNames, String projectName) throws JDSException {
        DomainInst bean = getDomainInstById(domainId, projectName);

        if (bean != null) {
            for (String esdClassName : esdClassNames) {
                try {
                    delAggEntityClass(esdClassName, projectName, true);
                } catch (JDSException e) {
                    e.printStackTrace();
                }
            }
            updateDomainInst(bean, true);
        }
    }

    public void delAggAPI(String domainId, Set<String> esdClassNames, String projectName) throws JDSException {
        DomainInst bean = getDomainInstById(domainId, projectName);

    }

    public void delAggSet(String domainId, Set<String> esdClassNames, String projectName) throws JDSException {
        DomainInst bean = getDomainInstById(domainId, projectName);

    }

    public void delAggMenu(String domainId, Set<String> esdClassNames, String projectName) throws JDSException {
        DomainInst bean = getDomainInstById(domainId, projectName);
    }

    public void delAggDomain(String domainId, Set<String> esdClassNames, String projectName) throws JDSException {
        DomainInst bean = getDomainInstById(domainId, projectName);

    }

    public void delAggEntity(String domainId, Set<String> esdClassNames, String projectName, boolean clear) throws JDSException {
        DomainInst bean = getDomainInstById(domainId, projectName);
        if (bean != null) {
            if (bean != null) {
                syncDelAggTableTasks(domainId + "delAgg", esdClassNames, projectName, clear);
                updateDomainInst(bean, true);
            }
        }
    }

    public void delAggTable(String domainId, Set<String> esdClassNames, String projectName, boolean clear) throws JDSException {
        DomainInst bean = getDomainInstById(domainId, projectName);
        if (bean != null) {
            syncDelAggTableTasks(domainId + "delTable", esdClassNames, projectName, clear);
            updateDomainInst(bean, true);
        }
    }


    public List<String> syncDelAggTableTasks(String taskId, Set<String> esdClassNameSet, String projectName, boolean clear) {
        List<String> srcBeans = new ArrayList<>();
        try {

            ExecutorService executorService = RemoteConnectionManager.getConntctionService(taskId);
            if (taskId == null) {
                taskId = DSMFactory.DefaultDsmName;
            }
            // int pageSize = esdClassNameSet.size() / 5 + 1;

            int pageSize = 5;
            //批量装载数据
            if (esdClassNameSet.size() > 0) {
                Integer start = 0;
                int size = esdClassNameSet.size();
                String[] esdclassArr = esdClassNameSet.toArray(new String[esdClassNameSet.size()]);
                int page = 0;
                while (page * pageSize < size) {
                    page++;
                }
                List<SyncDelAggTable<List<String>>> delAggTablTasks = new ArrayList<>();
                for (int k = 0; k < page; k++) {
                    int end = start + pageSize;
                    if (end >= size) {
                        end = size;
                    }
                    String[] esdClassNames = Arrays.copyOfRange(esdclassArr, start, start + pageSize);
                    SyncDelAggTable syncLoadClass = new SyncDelAggTable(esdClassNames, projectName, clear);
                    delAggTablTasks.add(syncLoadClass);
                    start = end;
                }
                RemoteConnectionManager.initConnection(taskId, esdClassNameSet.size() / pageSize + 1);
                List<Future<List<String>>> futures = executorService.invokeAll(delAggTablTasks);
                for (Future<List<String>> resultFuture : futures) {
                    try {
                        List<String> ids = resultFuture.get(500, TimeUnit.MILLISECONDS);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            executorService.shutdownNow();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return srcBeans;
    }

    class SyncDelAggTable<T extends List<String>> implements Callable<List<String>> {
        protected MinServerActionContextImpl autoruncontext;
        Set<String> classNameList = new HashSet();
        String projectName;
        boolean clear;

        public SyncDelAggTable(String[] esdClassNameList, String projectName, boolean clear) {
            this.projectName = projectName;
            this.clear = clear;
            for (String esdClassName : esdClassNameList) {
                if (MethodUtil.checkType(esdClassName)) {
                    classNameList.add(esdClassName);
                }
            }

            JDSContext context = JDSActionContext.getActionContext();
            this.autoruncontext = new MinServerActionContextImpl(context.getHttpRequest(), context.getOgnlContext());
            autoruncontext.setParamMap(context.getContext());
            if (context.getSessionId() != null) {
                autoruncontext.setSessionId(context.getSessionId());
                autoruncontext.getSession().put("sessionHandle", context.getSession().get("sessionHandle"));
            }
            autoruncontext.setSessionMap(context.getSession());
        }

        @Override
        public List<String> call() throws Exception {
            JDSActionContext.setContext(autoruncontext);
            List<String> esdClassList = new ArrayList<>();
            for (String esdClassName : classNameList) {
                delAggEntityClass(esdClassName, projectName, clear);
                log.info("del class:[" + esdClassName + "]");
            }
            return esdClassList;
        }


    }


    public void delAggRoot(String domainId, Set<String> esdClassNames, String projectName) throws JDSException {
        DomainInst bean = getDomainInstById(domainId, projectName);
    }

    private void delAgg(String domainId, String esdClassName, String projectName) {
        try {
            delAggEntityClass(esdClassName, projectName, true);
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }

    public void delAggSource(String domainId, Set<String> esdClassNames, String projectName) throws JDSException {
        DomainInst dsmInst = getDomainInstById(domainId, projectName);

        if (dsmInst != null) {
            for (String esdClassName : esdClassNames) {
                delAgg(domainId, esdClassName, projectName);
            }
            updateDomainInst(dsmInst, true);
        }
    }

    public void delAggMap(String domainId, Set<String> esdClassNames, String projectName) throws JDSException {
        DomainInst bean = getDomainInstById(domainId, projectName);
    }


    private String getRealDomainId(String className, boolean reload) throws JDSException {
        String domainId = domainIdMap.get(className);
        if (domainId == null || reload) {
            String sourceName = className;
            DomainInst domainInst = null;
            try {
                Aggregation aggregation = (Aggregation) ClassUtility.loadClass(className).getAnnotation(Aggregation.class);
                if (aggregation != null) {
                    domainId = aggregation.domainId();
                    if (aggregation.rootClass() != null) {
                        sourceName = aggregation.rootClass().getName();
                    } else if (aggregation.sourceClass() != null) {
                        sourceName = aggregation.sourceClass().getName();
                    }
                }

                RequestMapping requestMapping = (RequestMapping) ClassUtility.loadClass(sourceName).getAnnotation(RequestMapping.class);
                if (requestMapping != null) {
                    RequestMappingBean requestMethodBean = new RequestMappingBean(requestMapping);
                    PackagePathType packagePathType = PackagePathType.startPath(requestMethodBean.getFristUrl());
                    if (packagePathType != null && packagePathType.getApiType() != null && packagePathType.getApiType().getDefaultProjectName() != null) {
                        domainInst = DSMFactory.getInstance().getDefaultDomain(packagePathType.getApiType());
                    }
                }

                if (domainInst == null) {
                    String projectName = DSMFactory.getInstance().getDefaultProjectName();
                    PackageType packageType = PackageType.fromDefaultProject(projectName);
                    if (packageType != null) {
                        domainInst = DSMFactory.getInstance().getDefaultDomain(packageType);
                    } else {
                        domainInst = DSMFactory.getInstance().getDefaultDomain(projectName, UserSpace.VIEW);
                    }
                }

                if (domainInst != null) {
                    domainId = domainInst.getDomainId();
                }
                domainIdMap.put(className, domainId);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return domainId;
    }

    public void delAggEntity(String clazzName) throws JDSException {
        String domainId = getRealDomainId(clazzName, true);
        String configKey = clazzName + "[" + domainId + "]";
        aggEntityConfigMap.remove(configKey);
        aggEntityConfigCache.remove(configKey);
        classManager.clear(clazzName);
        Folder dsmFolder = aggClassConfigFolder.createChildFolder(domainId, JDSServer.getInstance().getAdminUser().getId());
        String folerPath = formartPackagePath(clazzName);
        String simClassName = formartClassName(clazzName);
        Folder packageFolder = this.getVfsClient().mkDir(dsmFolder.getPath() + folerPath);
        String fileName = simClassName + ".entity";
        FileInfo fileInfo = this.getVfsClient().getFileByPath(packageFolder.getPath() + fileName);
        if (fileInfo != null) {
            this.getVfsClient().deleteFile(fileInfo.getID());
        }

    }


    public void delAggEntityClass(String className, String projectName, boolean clear) throws JDSException {

        Class clazz = classManager.checkInterface(className);
        if (clazz != null) {

            String realClassName = clazz.getName();
            Set<String> classNameSet = new HashSet<>();
            classNameSet.add(className);
            classNameSet.add(realClassName);
            for (String clazzName : classNameSet) {
                ESDClass esdClass = classManager.getAggEntityByName(clazzName, false);
                if (esdClass != null) {
                    String oDomainId = esdClass.getDomainId();
                    String domainId = getRealDomainId(className, false);
                    if (oDomainId != null && !oDomainId.equals(domainId)) {
                        domainId = getRealDomainId(className, true);
                    }
                    this.delAggEntity(clazzName);
                    this.delEntityRef(clazzName, domainId, projectName, false);
                    if (clear) {
                        clearEntitySrc(clazzName, domainId, projectName);
                    }
                    if (esdClass != null && esdClass.isProxy() && esdClass.getSourceClass() != null) {
                        Class sourceClazz = classManager.checkInterface(esdClass.getSourceClass().getClassName());
                        if (sourceClazz != null) {
                            String sourceClassName = sourceClazz.getName();
                            if (!sourceClassName.equals(clazzName)) {
                                delAggEntityClass(sourceClassName, projectName, clear);
                            }
                        }
                    }
                    this.deleteApiClassConfig(className, clear);

                }
            }

        }


    }

    public void delEntityRef(String refId, String domainId, String projectName) throws JDSException {
        delEntityRef(refId, domainId, projectName, true);
    }

    public void delEntityRef(String refId, String domainId, String projectName, boolean autocommit) throws JDSException {
        DomainInst domainInst = this.getDomainInstById(domainId, projectName);
        if (domainInst != null) {
            Map<String, AggEntityRef> refMap = domainInst.getAggEntityRefMap();
            if (refId != null) {
                String[] refIds = StringUtility.split(refId, ";");
                for (String id : refIds) {
                    refMap.remove(id);
                }
            }
            if (autocommit) {
                this.commitProjectConfig(domainInst.getProjectVersionName());
            }
        }

    }

    public List<JavaSrcBean> buildAggEntity(DomainInst dsmInst, AggEntityConfig entityConfig, AggregationType aggregationType, ChromeProxy chrome) throws JDSException, IOException {
        return buildAggEntity(dsmInst, entityConfig, aggregationType, null, chrome);
    }

    public List<JavaSrcBean> buildAggEntity(DomainInst dsmInst, AggEntityConfig entityConfig, AggregationType aggregationType, Set<String> temps, ChromeProxy chrome) throws JDSException, IOException {
        List<Callable<List<JavaSrcBean>>> buildTasks = new ArrayList<>();
        List<JavaSrcBean> aggEntities = new ArrayList<>();
        if (temps == null) {
            temps = dsmInst.getJavaTempIds();
        }

        //删除信息会造成配置数据丢失
//        classManager.clear(entityConfig.getCurrClassName());
//        this.delAggEntityClass(entityConfig.getCurrClassName(), dsmInst.getProjectVersionName(), false);

        if (entityConfig.getESDClass().isProxy()) {
            buildTasks.add(new GenAggAPIJava(dsmInst, entityConfig, temps, chrome));
        } else {
            buildTasks.add(new GenAggEntityJava(dsmInst, entityConfig, aggregationType, temps, chrome));
        }
        buildTasks.add(new GenAggModuleJava(dsmInst, temps, chrome));
        aggEntities = BuildFactory.getInstance().syncTasks(dsmInst.getDomainId(), buildTasks);
        for (JavaSrcBean srcBean : aggEntities) {
            dsmInst.addJavaBean(srcBean);
        }

        this.updateDomainInst(dsmInst, true);
        return aggEntities;
    }

    private List<Callable<List<JavaSrcBean>>> genAggEntityJavaTask(DomainInst domainInst, AggEntityConfig esdClassConfig, AggregationType aggregationType, Set<String> allTemps, ChromeProxy chrome) {
        List<Callable<List<JavaSrcBean>>> buildTasks = new ArrayList<>();
        GenAggEntityJava genAggMenuJava = new GenAggEntityJava(domainInst, esdClassConfig, aggregationType, allTemps, chrome);
        buildTasks.add(genAggMenuJava);
        return buildTasks;
    }

    public List<JavaSrcBean> buildAggModule(DomainInst dsmInst, AggregationType aggregationType, String serviceClassName, Set<String> temps, ChromeProxy chrome) throws JDSException, IOException {
        AggEntityConfig entityConfig = this.getAggEntityConfig(serviceClassName, false);
        List<JavaSrcBean> aggBeans = buildAggEntity(dsmInst, entityConfig, aggregationType, temps, chrome);
        for (JavaSrcBean srcBean : aggBeans) {
            dsmInst.addJavaBean(srcBean);
        }
        this.updateDomainInst(dsmInst, true);
        return aggBeans;
    }

    public void buildAggregation(DomainInst domainInst, AggregationType aggregationType, Set<String> temps, String projectName, ChromeProxy chrome) throws JDSException {
        List<Callable<List<JavaSrcBean>>> buildTasks = new ArrayList<>();
        List<AggEntityConfig> aggEntityConfigs = new ArrayList<>();

        List<ESDClass> esdClassList = domainInst.getEntityList(AggregationType.ENTITY, true);
        ;
        for (ESDClass esdClass : esdClassList) {
            AggEntityConfig entityConfig = this.getAggEntityConfig(esdClass.getClassName(), false);
            if (entityConfig.getESDClass().isProxy()) {
                buildTasks.add(new GenAggAPIJava(domainInst, entityConfig, domainInst.getJavaTempIds(), chrome));
            } else {
                buildTasks.add(new GenAggEntityJava(domainInst, entityConfig, aggregationType, domainInst.getJavaTempIds(), chrome));
            }
            aggEntityConfigs.add(entityConfig);
        }

        List<ESDClass> viewClassList = domainInst.getViewEntityList();
        for (ESDClass esdClass : viewClassList) {
            AggEntityConfig entityConfig = this.getAggEntityConfig(esdClass.getClassName(), false);
            buildTasks.add(new GenAggViewJava(domainInst, entityConfig, temps, chrome));
            aggEntityConfigs.add(entityConfig);
        }

        List<ESDClass> rootClassList = domainInst.getAggModules();
        for (ESDClass esdClass : rootClassList) {
            AggEntityConfig entityConfig = this.getAggEntityConfig(esdClass.getClassName(), false);
            buildTasks.add(new GenAggRootJava(domainInst, entityConfig, temps, chrome));
            aggEntityConfigs.add(entityConfig);
        }
        List<ESDClass> domainList = domainInst.getAggDomains();
        for (ESDClass esdClass : domainList) {
            AggEntityConfig entityConfig = this.getAggEntityConfig(esdClass.getClassName(), false);
            buildTasks.add(new GenAggDomainJava(domainInst, entityConfig, temps, chrome));
            aggEntityConfigs.add(entityConfig);
        }


        for (AggEntityConfig entityConfig : aggEntityConfigs) {
            List<Callable<List<JavaSrcBean>>> entiryTasks = genChildAggEntityTask(domainInst, null, entityConfig, temps, chrome);
            buildTasks.addAll(entiryTasks);
            if (this.getEntityRefByName(entityConfig.getESDClass().getClassName(), domainInst.getDomainId(), projectName).size() > 0) {
                List<Callable<List<JavaSrcBean>>> refTasks = genAggRefJavaTask(domainInst, entityConfig, temps, chrome);
                buildTasks.addAll(refTasks);
            }

        }
        buildTasks.add(new GenAggModuleJava(domainInst, domainInst.getJavaTempIds(), chrome));
        List<JavaSrcBean> aggBeans = BuildFactory.getInstance().syncTasks(domainInst.getDomainId(), buildTasks);
        for (JavaSrcBean srcBean : aggBeans) {
            domainInst.addJavaBean(srcBean);
        }
        this.updateDomainInst(domainInst, true);

    }

    /**
     * 根据表信息创建java文件
     *
     * @throws IOException
     */
    public List<JavaSrcBean> genAggModuleJava(DomainInst domainInst, Set<String> tempIds, ChromeProxy chrome) throws JDSException {
        GenJava javaGen = GenJava.getInstance(domainInst.getProjectVersionName());
        AggRoot allRoot = new AggRoot(domainInst);
        List<JavaSrcBean> srcFiles = new ArrayList<>();
        if (tempIds == null) {
            tempIds = domainInst.getJavaTempIds();
        }
        for (String javaTempId : tempIds) {
            JavaTemp javatemp = BuildFactory.getInstance().getTempManager().getJavaTempById(javaTempId);
            if (javatemp != null && !javatemp.getRangeType().equals(RangeType.MODULEVIEW) && javatemp.getRangeType().equals(RangeType.MODULE)) {
                String packageName = domainInst.getPackageName();
                if (javatemp.getPackagePostfix() != null && !javatemp.getPackagePostfix().equals("")) {
                    packageName = packageName + "." + javatemp.getPackagePostfix();
                }
                String className = StringUtility.replace(javatemp.getNamePostfix(), "**", OODUtil.formatJavaName(domainInst.getEuPackage(), true));
                allRoot.setPackageName(packageName);
                allRoot.setClassName(className);
                File file = javaGen.createJava(javatemp, allRoot, chrome);
                JavaSrcBean srcBean = BuildFactory.getInstance().getTempManager().genJavaSrc(file, domainInst, javaTempId);
                srcFiles.add(srcBean);
            }
        }
        return srcFiles;
    }

    public JavaSrcBean genAggViewJava(AggViewRoot aggViewRoot, CustomViewBean viewBean, ChromeProxy chrome) throws JDSException {
        String className = aggViewRoot.getClassName();
        String moduleName = className.substring(0, className.lastIndexOf(".")).toLowerCase();
        return genAggViewJava(aggViewRoot, viewBean, moduleName, className, chrome);
    }

    public List<JavaSrcBean> genAggMapJava(AggViewRoot aggViewRoot, CustomViewBean viewBean, ChromeProxy chrome) throws JDSException {
        DomainInst domainInst = (DomainInst) aggViewRoot.getDsmBean();
        String className = aggViewRoot.getClassName();
        String moduleName = aggViewRoot.getPackageName();
        List<Callable<List<JavaSrcBean>>> aggViewJavaTask = genAggMapJavaTask(aggViewRoot, viewBean, moduleName, className, chrome);
        List<JavaSrcBean> srcFiles = BuildFactory.getInstance().syncTasks(domainInst.getDomainId(), aggViewJavaTask);
        for (JavaSrcBean srcBean : srcFiles) {
            domainInst.addJavaBean(srcBean);
        }
        this.updateDomainInst(domainInst, true);
        return srcFiles;
    }


    public JavaSrcBean genAggViewJava(AggViewRoot aggViewRoot, CustomViewBean viewBean, String className, ChromeProxy chrome) throws JDSException {
        String moduleName = className.substring(className.lastIndexOf(".") + 1).toLowerCase();
        return genAggViewJava(aggViewRoot, viewBean, moduleName, className, chrome);
    }


    private List<Callable<List<JavaSrcBean>>> genAggViewJavaTask(AggViewRoot aggViewRoot, CustomViewBean viewBean, String moduleName, String className, ChromeProxy chrome) {
        List<Callable<List<JavaSrcBean>>> buildTasks = new ArrayList<>();
        GenAggCustomViewJava genAggViewJava = new GenAggCustomViewJava(aggViewRoot, viewBean, moduleName, className, chrome);
        buildTasks.add(genAggViewJava);
        return buildTasks;
    }

    private List<Callable<List<JavaSrcBean>>> genAggMapJavaTask(AggViewRoot aggViewRoot, CustomViewBean viewBean, String moduleName, String className, ChromeProxy chrome) {
        List<Callable<List<JavaSrcBean>>> buildTasks = new ArrayList<>();
        DomainInst domainInst = (DomainInst) aggViewRoot.getDsmBean();

        GenAggCustomJava genAggViewJava = new GenAggCustomJava(aggViewRoot, viewBean, domainInst.getJavaTempIds(), AggregationType.NAVIGATION, moduleName, className, chrome);
        buildTasks.add(genAggViewJava);
        return buildTasks;
    }

    /**
     * 根据表信息创建java文件
     *
     * @throws IOException
     */
    public JavaSrcBean genAggViewJava(AggViewRoot aggViewRoot, CustomViewBean viewBean, String moduleName, String className, ChromeProxy chrome) throws JDSException {
        JavaSrcBean javaSrcBean = null;
        DomainInst domainInst = (DomainInst) aggViewRoot.getDsmBean();
        List<Callable<List<JavaSrcBean>>> aggViewJavaTask = genAggViewJavaTask(aggViewRoot, viewBean, moduleName, className, chrome);
        List<JavaSrcBean> srcFiles = BuildFactory.getInstance().syncTasks(domainInst.getDomainId(), aggViewJavaTask);
        for (JavaSrcBean srcBean : srcFiles) {
            domainInst.addJavaBean(srcBean);
        }
        if (srcFiles.size() > 0) {
            javaSrcBean = srcFiles.get(0);
        }
        this.updateDomainInst(domainInst, true);
        return javaSrcBean;
    }

    private List<Callable<List<JavaSrcBean>>> genAggModuleViewJavaTask(DomainInst domainInst, String className, List<CustomModuleBean> moduleBeans, ChromeProxy chrome) {
        List<Callable<List<JavaSrcBean>>> buildTasks = new ArrayList<>();
        GenAggModuleViewJava genAggMenuJava = new GenAggModuleViewJava(domainInst, moduleBeans, className, chrome);
        buildTasks.add(genAggMenuJava);
        return buildTasks;
    }

    private List<Callable<List<JavaSrcBean>>> genAggRefJavaTask(DomainInst domainInst, AggEntityConfig esdClassConfig, Set<String> allTemps, ChromeProxy chrome) {
        List<Callable<List<JavaSrcBean>>> buildTasks = new ArrayList<>();
        GenAggRefJava genAggMenuJava = new GenAggRefJava(domainInst, esdClassConfig, allTemps, chrome);
        buildTasks.add(genAggMenuJava);
        return buildTasks;
    }

    public void updateAggEntityRef(AggEntityRef ref, String projectName) throws JDSException {
        this.updateAggEntityRef(ref, projectName, true);
    }

    public void updateAggEntityRef(AggEntityRef ref, String projectName, boolean autocommit) throws JDSException {
        DomainInst domainInst = this.getDomainInstById(ref.getDomainId(), projectName);
        String mainClassName = ref.getClassName().toLowerCase();
        String otherClassName = ref.getOtherClassName().toLowerCase();
        if (otherClassName == null || otherClassName == null) {
            throw new JDSException("格式错误");
        }
        Map<String, AggEntityRef> refMap = domainInst.getAggEntityRefMap();
        String refId = ref.getRefId();
        if (refId == null || refId.equals("")) {
            refMap.remove(refId);
            refId = UUID.randomUUID().toString();
        }

        AggEntityRef oClassRef = refMap.get(refId);
        if (oClassRef == null) {
            oClassRef = new AggEntityRef();
            oClassRef.setRefId(refId);
        }
        oClassRef.setDomainId(ref.getDomainId());

        oClassRef.setClassName(mainClassName);
        oClassRef.setOtherClassName(otherClassName);
        oClassRef.setRefBean(ref.getRefBean());
        refMap.put(refId, oClassRef);

        //更新OTHERTABLE
        AggEntityRef tentityRef = this.getAggEntityRef(otherClassName, mainClassName, ref.getDomainId(), projectName);
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
            tentityRef = new AggEntityRef();
            tentityRef.setRefId(UUID.randomUUID().toString());
        }
        tentityRef.setDomainId(ref.getDomainId());
        tentityRef.setClassName(otherClassName);
        tentityRef.setOtherClassName(mainClassName);
        tentityRef.setRefBean(tref);
        refMap.put(tentityRef.getRefId(), tentityRef);
        if (autocommit) {
            this.updateDomainInst(domainInst, autocommit);

        }
    }

    public List<JavaSrcBean> genAggRootJava(DomainInst domainInst, AggEntityConfig esdClassConfig, Set<String> allTemps, ChromeProxy chrome) throws JDSException {
        if (allTemps == null) {
            allTemps = domainInst.getJavaTempIds();
        }
        List<Callable<List<JavaSrcBean>>> tasks = genAggRootJavaTask(domainInst, esdClassConfig, allTemps, chrome);
        return BuildFactory.getInstance().syncTasks(domainInst.getDomainId(), tasks);
    }

    private List<Callable<List<JavaSrcBean>>> genAggRootJavaTask(DomainInst domainInst, AggEntityConfig esdClassConfig, Set<String> allTemps, ChromeProxy chrome) {
        List<Callable<List<JavaSrcBean>>> buildTasks = new ArrayList<>();
        GenAggRootJava genAggMenuJava = new GenAggRootJava(domainInst, esdClassConfig, allTemps, chrome);
        buildTasks.add(genAggMenuJava);
        return buildTasks;
    }

    private List<Callable<List<JavaSrcBean>>> genAggMenuJavaTask(DomainInst domainInst, AggEntityConfig esdClassConfig, Set<String> allTemps, ChromeProxy chrome) {
        List<Callable<List<JavaSrcBean>>> buildTasks = new ArrayList<>();
        GenAggMenuJava genAggMenuJava = new GenAggMenuJava(domainInst, esdClassConfig, allTemps, chrome);
        buildTasks.add(genAggMenuJava);
        return buildTasks;
    }

    private List<Callable<List<JavaSrcBean>>> genAggMapJavaTask(DomainInst domainInst, AggEntityConfig esdClassConfig, Set<String> allTemps, ChromeProxy chrome) {
        List<Callable<List<JavaSrcBean>>> buildTasks = new ArrayList<>();
        GenAggMenuJava genAggMenuJava = new GenAggMenuJava(domainInst, esdClassConfig, allTemps, chrome);
        buildTasks.add(genAggMenuJava);
        return buildTasks;
    }

    public List<Callable<List<JavaSrcBean>>> genChildAggEntityTask(DomainInst domainInst, String moduleName, AggEntityConfig esdClassConfig, Set<String> allTemps, ChromeProxy chrome) throws JDSException {
        List<Callable<List<JavaSrcBean>>> buildTasks = new ArrayList<>();
        List<FieldModuleConfig> methodAPIBeans = esdClassConfig.getModuleMethods();
        for (String javaTempId : allTemps) {
            JavaTemp javatemp = BuildFactory.getInstance().getTempManager().getJavaTempById(javaTempId);
            if (javatemp != null && !javatemp.getRangeType().equals(RangeType.MODULEVIEW)) {
                for (FieldModuleConfig fieldModuleConfig : methodAPIBeans) {
                    buildTasks.add(new GenAggChildMethodJava(domainInst, moduleName, fieldModuleConfig.getMethodConfig(), javatemp, chrome));
                }
            }
        }
        return buildTasks;
    }

    public void commitProjectConfig(String projectVersionName) throws JDSException {
        DSMProjectConfig config = projectCacheManager.getProjectByName(projectVersionName).getDsmProjectConfig();
        projectCacheManager.updateDSMConfig(projectVersionName, config);
    }


    public DomainInst updateDomainInst(DomainInst dsmInst, boolean autocommit) throws JDSException {
        if (dsmInst != null) {

            DSMProjectConfig config = projectCacheManager.getProjectByName(dsmInst.getProjectVersionName()).getDsmProjectConfig();
            Map<String, DomainInst> dsmInstMap = config.getAggregationInst();
            dsmInstMap.put(dsmInst.getDomainId(), dsmInst);
            if (autocommit) {
                this.commitProjectConfig(dsmInst.getProjectVersionName());
            }
        }
        return dsmInst;
    }

    private void deleteProjectDomainInst(String projectName, String domainId, boolean autocommit) throws JDSException {
        if (projectName == null || projectName.equals("")) {
            projectName = DSMFactory.getInstance().getDefaultProjectName();
        }

        DSMProjectConfig config = projectCacheManager.getProjectByName(projectName).getDsmProjectConfig();
        Map<String, DomainInst> dsmInstMap = config.getAggregationInst();
        if (domainId != null) {
            String[] refIds = StringUtility.split(domainId, ";");
            for (String id : refIds) {
                dsmInstMap.remove(id);
            }
        }

        if (autocommit) {
            clearAggEntityConfig(domainId);
            clearApiClassConfig(domainId);
            this.commitProjectConfig(projectName);
        }

    }

    private void deleteProjectDomainInst(String projectName, String dsmInstId) throws JDSException {
        deleteProjectDomainInst(projectName, dsmInstId, true);
    }

    public void deleteDomainInst(String domainId, String projectName, boolean autocommit) throws JDSException {
        if (domainId != null) {
            String[] refIds = StringUtility.split(domainId, ";");
            for (String id : refIds) {
                this.clearEntityConfig(id, projectName);
                deleteProjectDomainInst(projectName, id, autocommit);
            }
        }
    }

    private DomainInst getProjectDomainInstById(String projectId, String domainId) {
        INProject project = projectCacheManager.getProjectById(projectId);
        DSMProjectConfig config = project.getDsmProjectConfig();
        Map<String, DomainInst> dsmInstMap = config.getAggregationInst();
        DomainInst domainInst = dsmInstMap.get(domainId);
        return domainInst;
    }


    public DomainInst getDomainInstById(String domainId) {
        return getDomainInstById(domainId, null);
    }


    public DomainInst getDomainInstByCat(String projectName, UserSpace userSpace) {
        DomainInst domainInst = null;
        try {
            if (projectName == null) {
                projectName = DSMFactory.getInstance().getDefaultProjectName();
            }
            INProject project = projectCacheManager.getProjectByName(projectName);
            String domainId = project.getProjectName() + "_" + userSpace.name();
            domainInst = getProjectDomainInstById(project.getId(), domainId);
        } catch (JDSException e) {
            //e.printStackTrace();
        }
        return domainInst;

    }


    public DomainInst getDomainInstById(String domainId, String projectName) {
        DomainInst domainInst = null;
        try {
            if (projectName == null) {
                projectName = DSMFactory.getInstance().getDefaultProjectName();
            }
            INProject project = projectCacheManager.getProjectByName(projectName);
            if (!domainId.equals(project.getProjectName())) {
                domainInst = this.getProjectDomainInstById(project.getId(), domainId);
            } else {
                domainInst = this.getDomainInstByCat(projectName, UserSpace.SYS);
            }


            if (domainInst != null) {
                return domainInst;
            }


        } catch (JDSException e) {
            //e.printStackTrace();
        }

        List<INProject> projects = projectCacheManager.getAllProjectList();
        for (INProject inProject : projects) {
            domainInst = this.getProjectDomainInstById(inProject.getId(), domainId);
            if (domainInst != null) {
                return domainInst;
            }
        }
        return null;
    }


    public List<DomainInst> getAllUserDomainInst(String projectVersionName) throws JDSException {
        List<DomainInst> domainInstList = new ArrayList<>();
        List<DomainInst> domainInsts = getDomainInstList(projectVersionName);

        for (DomainInst domainInst : domainInsts) {

            if (domainInst.getUserSpace() != null && domainInst.getUserSpace().equals(UserSpace.VIEW)) {
                domainInstList.add(domainInst);
            }
        }

        for (DomainInst domainInst : domainInsts) {
            if (domainInst.getUserSpace() != null && !domainInst.getUserSpace().equals(UserSpace.VIEW) && !domainInst.getUserSpace().equals(UserSpace.SYS)) {
                domainInstList.add(domainInst);
            }
        }

        return domainInstList;
    }


    public List<DomainInst> getDomainInstList(String projectVersionName) throws JDSException {
        List<DomainInst> tempInsts = new ArrayList<>();
        INProject project = projectCacheManager.getProjectByName(projectVersionName);
        DSMProjectConfig config = project.getDsmProjectConfig();
        Map<String, DomainInst> dsmInstMap = config.getAggregationInst();
        dsmInstMap.forEach((k, v) -> {
            if (v != null) {
                v.setDomainId(k);
                if (!tempInsts.contains(v)) {
                    tempInsts.add(v);
                }
            }
        });
        return tempInsts;
    }

    public void commitTask() {
        try {
            updateAggEntityTask(aggEntityConfigTasks);
            updateApiEntityTask(apiConfigTasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void updateAggEntityTask(List<SaveAggEntityConfigTask<AggEntityConfig>> tasks) throws InterruptedException {
        ExecutorService executorService = RemoteConnectionManager.getConntctionService("AggEntityConfig");

        List<Future<AggEntityConfig>> futures = executorService.invokeAll(tasks);
        for (Future<AggEntityConfig> future : futures) {
            try {
                future.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        executorService.shutdownNow();
        this.aggEntityConfigTasks.clear();
    }

    public void updateApiEntityTask(List<SaveApiEntityConfigTask<ApiClassConfig>> tasks) throws InterruptedException {
        ExecutorService executorService = RemoteConnectionManager.getConntctionService("ApiClassConfig");

        RemoteConnectionManager.initConnection("ApiClassConfig", tasks.size());
        List<Future<ApiClassConfig>> futures = executorService.invokeAll(tasks);
        for (Future<ApiClassConfig> future : futures) {
            try {
                future.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        executorService.shutdownNow();
        this.apiConfigTasks.clear();
    }


    class SaveAggEntityConfigTask<T extends AggEntityConfig> implements Callable<AggEntityConfig> {
        AggEntityConfig aggEntityConfig;
        MinServerActionContextImpl autoruncontext;
        String domainId = null;
        String className = null;

        SaveAggEntityConfigTask(AggEntityConfig esdClassConfig) {
            this.aggEntityConfig = esdClassConfig;
            JDSContext context = JDSActionContext.getActionContext();
            this.autoruncontext = new MinServerActionContextImpl(context.getHttpRequest(), context.getOgnlContext());
            autoruncontext.setParamMap(context.getContext());
            if (context.getSessionId() != null) {
                autoruncontext.setSessionId(context.getSessionId());
                autoruncontext.getSession().put("sessionHandle", context.getSession().get("sessionHandle"));
            }
            autoruncontext.setSessionMap(context.getSession());
            this.domainId = aggEntityConfig.getDomainId();
            this.className = aggEntityConfig.getSourceClassName();
        }

        @Override
        public AggEntityConfig call() {
            try {
                JDSActionContext.setContext(autoruncontext);
                Folder dsmFolder = aggClassConfigFolder.createChildFolder(domainId, JDSServer.getInstance().getAdminUser().getId());
                String folerPath = formartPackagePath(className);
                String simClassName = formartClassName(className);
                Folder packageFolder = getVfsClient().mkDir(dsmFolder.getPath() + folerPath);
                String fileName = simClassName + ".entity";
                FileInfo fileInfo = packageFolder.createFile(fileName, className, null);
                String content = JSON.toJSONString(aggEntityConfig);
                String uKey = className + "[" + domainId + "]";
                aggEntityConfigCache.put(uKey, content);
                aggEntityConfigMap.put(uKey, aggEntityConfig);
                getVfsClient().saveFileAsContent(fileInfo.getPath(), content, VFSConstants.Default_Encoding);
            } catch (JDSException e) {
                e.printStackTrace();
            }
            return aggEntityConfig;
        }


    }

    class SaveApiEntityConfigTask<T extends ApiClassConfig> implements Callable<ApiClassConfig> {
        ApiClassConfig apiClassConfig;
        MinServerActionContextImpl autoruncontext;
        String domainId = null;
        String className = null;

        SaveApiEntityConfigTask(ApiClassConfig apiClassConfig) {

            this.apiClassConfig = apiClassConfig;
            JDSContext context = JDSActionContext.getActionContext();
            this.autoruncontext = new MinServerActionContextImpl(context.getHttpRequest(), context.getOgnlContext());
            autoruncontext.setParamMap(context.getContext());
            if (context.getSessionId() != null) {
                autoruncontext.setSessionId(context.getSessionId());
                autoruncontext.getSession().put("sessionHandle", context.getSession().get("sessionHandle"));
            }
            autoruncontext.setSessionMap(context.getSession());
            this.domainId = apiClassConfig.getDomainId();
            this.className = apiClassConfig.getServiceClass();

        }

        @Override
        public ApiClassConfig call() {
            try {
                if (domainId != null) {
                    JDSActionContext.setContext(autoruncontext);
                    String content = JSON.toJSONString(apiClassConfig);
                    Folder dsmFolder = apiClassConfigFolder.createChildFolder(domainId, JDSServer.getInstance().getAdminUser().getId());
                    String folerPath = formartPackagePath(className);
                    String simClassName = formartClassName(className);
                    Folder packageFolder = getVfsClient().mkDir(dsmFolder.getPath() + folerPath);
                    String fileName = simClassName + ".api";
                    FileInfo fileInfo = packageFolder.createFile(fileName, className, null);
                    getVfsClient().saveFileAsContent(fileInfo.getPath(), content, VFSConstants.Default_Encoding);
                }

            } catch (JDSException e) {
                e.printStackTrace();
            }
            return apiClassConfig;
        }

    }

    private String formartPackagePath(String className) {
        String packageName = StringUtility.replace(className, ".", "/");
        if (packageName.indexOf("/") > -1) {
            packageName = packageName.substring(0, packageName.lastIndexOf("/"));
        }
        if (packageName.endsWith("/")) {
            packageName = packageName + "/";
        }
        return packageName;
    }

    private String formartClassName(String className) {
        className = StringUtility.replace(className, ".", "/");
        if (className.indexOf("/") > -1) {
            className = className.substring(className.lastIndexOf("/") + 1);
        }
        return className;
    }

    public CtVfsService getVfsClient() {
        CtVfsService vfsClient = CtVfsFactory.getCtVfsService();
        return vfsClient;
    }

    public Folder getAggClassConfigFolder() {
        return aggClassConfigFolder;
    }

    public void setAggClassConfigFolder(Folder aggClassConfigFolder) {
        this.aggClassConfigFolder = aggClassConfigFolder;
    }

    public Folder getApiClassConfigFolder() {
        return apiClassConfigFolder;
    }

    public void setApiClassConfigFolder(Folder apiClassConfigFolder) {
        this.apiClassConfigFolder = apiClassConfigFolder;
    }

    public ChromeProxy getCurrChromeDriver() {
        ChromeProxy chrome = JDSActionContext.getActionContext().Par("$currChromeDriver", ChromeProxy.class);
        if (chrome == null) {
            chrome = new LogSetpLog();
        }
        return chrome;
    }
}
