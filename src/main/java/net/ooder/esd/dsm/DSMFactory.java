package net.ooder.esd.dsm;

import net.ooder.annotation.AggregationType;
import net.ooder.annotation.UserSpace;
import net.ooder.common.JDSConstants;
import net.ooder.common.JDSException;
import net.ooder.common.logging.ChromeProxy;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.common.logging.LogSetpLog;
import net.ooder.common.util.java.DynamicClassLoader;
import net.ooder.config.JDSConfig;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.bean.CustomViewBean;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.dsm.aggregation.AggregationManager;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.enums.DSMTempType;
import net.ooder.esd.dsm.enums.DSMType;
import net.ooder.esd.dsm.gen.GenJava;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.repository.RepositoryInst;
import net.ooder.esd.dsm.repository.RepositoryManager;
import net.ooder.esd.dsm.view.ViewInst;
import net.ooder.esd.dsm.view.ViewManager;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.MySpace;
import net.ooder.esd.engine.ProjectCacheManager;
import net.ooder.esd.engine.ProjectVersion;
import net.ooder.esd.engine.config.ProjectConfig;
import net.ooder.esd.engine.enums.PackageType;
import net.ooder.esd.engine.enums.ProjectDefAccess;
import net.ooder.esd.engine.inner.INProject;
import net.ooder.esd.engine.inner.INProjectVersion;
import net.ooder.server.JDSServer;
import net.ooder.server.httpproxy.core.AbstractHandler;
import net.ooder.server.httpproxy.core.HttpRequest;
import net.ooder.vfs.FileInfo;
import net.ooder.vfs.FileVersion;
import net.ooder.vfs.Folder;
import net.ooder.vfs.ct.CtVfsFactory;
import net.ooder.vfs.ct.CtVfsService;
import net.ooder.web.RemoteConnectionManager;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

;

public class DSMFactory {

    private static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, DSMFactory.class);

    private static Map<String, DSMFactory> managerMap = new HashMap<String, DSMFactory>();

    private final MySpace space;

    public static final String THREAD_LOCK = "Thread Lock";

    public static final String DefaultDsmName = "DSM";

    public static final String[] SkipParams = new String[]{"projectName", "projectVersionName", "domaindId"};

    private final ProjectCacheManager projectCacheManager;

    private final ViewManager viewManager;

    private final RepositoryManager repositoryManager;

    private final AggregationManager aggregationManager;

    private final BuildFactory buildFactory;


    public static DSMFactory getInstance() throws JDSException {
        return getInstance(ESDFacrory.getAdminESDClient().getSpace());
    }

    public static DSMFactory getInstance(MySpace space) {
        String path = space.getPath();
        DSMFactory manager = managerMap.get(path);
        if (manager == null) {
            synchronized (path) {
                manager = managerMap.get(path);
                if (manager == null) {
                    manager = new DSMFactory(space);
                    managerMap.put(path, manager);
                }
            }
        }
        return manager;
    }


    DSMFactory(MySpace space) {
        this.space = space;
        this.projectCacheManager = ProjectCacheManager.getInstance(space);
        this.viewManager = ViewManager.getInstance(space);
        this.repositoryManager = RepositoryManager.getInstance(space);
        this.aggregationManager = AggregationManager.getInstance(space);
        this.buildFactory = BuildFactory.getInstance(space);


        new Thread(new Runnable() {
            @Override
            public void run() {
                compile();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    initDsm();
                } catch (JDSException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    initEsd();
                } catch (JDSException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    public ViewManager getViewManager() {
        return viewManager;
    }

    public void reload() {
        repositoryManager.reload();
        viewManager.reload();
        aggregationManager.reload();

    }


    public void clear() {
        buildFactory.clear();
        repositoryManager.clear();
        viewManager.clear();
        aggregationManager.clear();
    }


    public void commitProjectConfig(String projectVersionName) throws JDSException {
        ProjectConfig config = projectCacheManager.getProjectByName(projectVersionName).getConfig();
        projectCacheManager.updateProjectConfig(projectVersionName, config);
    }

    public void initEsd() throws JDSException {
        List<INProject> projects = projectCacheManager.getProjectList(ProjectDefAccess.admin);
        for (INProject project : projects) {
            initProject(project.getActiviteVersion());
        }
    }

    public void initUser() throws JDSException {
        List<INProject> projects = projectCacheManager.getProjectList(ProjectDefAccess.Public);
        for (INProject project : projects) {
            initProject(project.getActiviteVersion());
        }
    }

    public void initDsm() throws JDSException {
        List<INProject> projects = projectCacheManager.getProjectList(ProjectDefAccess.DSM);
        for (INProject project : projects) {
            initProject(project.getActiviteVersion());
        }
    }

    public void initProject(INProjectVersion project) throws JDSException {

        //批量预读配置
        Set<String> versionIds = new HashSet<>();
        List<Folder> folders = project.getRootFolder().getChildrenRecursivelyList();
        List<FileInfo> fileInfos = project.getRootFolder().getFileListRecursively();
        for (FileInfo fileInfo : fileInfos) {
            versionIds.add(fileInfo.getCurrentVersonId());
        }
        List<FileVersion> versionList = CtVfsFactory.getCtVfsService().loadVersionByIds(versionIds);
        Set<String> objecdIds = new HashSet<>();
        for (FileVersion version : versionList) {
            objecdIds.add(version.getFileObjectId());
        }
        CtVfsFactory.getCtVfsService().loadObjects(objecdIds);

    }


    public void reBuildClass(Class clazz) {
        if (clazz != null) {
            buildFactory.registerClass(clazz);
            String className = clazz.getName();
            try {
                // aggregationManager.deleteApiClassConfig(className, false);
                aggregationManager.delAggEntityClass(className, null, false);
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
    }

    public void reBuildRepositoryClass(Class clazz, String projectName) {
        if (clazz != null) {
            String className = clazz.getName();
            try {
                repositoryManager.delEntity(className, projectName);
            } catch (JDSException e) {
                e.printStackTrace();
            }
            buildFactory.registerClass(clazz);
        }
    }


    public void reBuildViewClass(Class clazz, String projectName) {
        if (clazz != null) {
            String className = clazz.getName();
            try {

                viewManager.deleteViewEntityConfig(clazz.getName(), projectName);
                repositoryManager.delEntity(className, projectName);
            } catch (JDSException e) {
                e.printStackTrace();
            }
            buildFactory.registerClass(clazz);
        }
    }

    class CompileRepositoryInst<T> implements Callable<T> {
        private final RepositoryInst repositoryInst;

        public CompileRepositoryInst(RepositoryInst repositoryInst) {
            this.repositoryInst = repositoryInst;
        }

        @Override
        public T call() {
            try {
                DSMFactory.getInstance(space).compileRepositoryInst(repositoryInst, null);
            } catch (JDSException e) {
                e.printStackTrace();
            }
            return (T) repositoryInst;
        }

    }

    class CompileView<T> implements Callable<T> {
        private final ViewInst viewInst;

        public CompileView(ViewInst viewInst) {
            this.viewInst = viewInst;
        }

        @Override
        public T call() {
            try {
                DSMFactory.getInstance(space).compileViewInst(viewInst, new LogSetpLog());
            } catch (JDSException e) {
                e.printStackTrace();
            }
            return (T) viewInst;
        }
    }


    public void compileProject(String projectVersionName) {
        try {
            RepositoryInst repositoryInst = repositoryManager.getProjectRepository(projectVersionName);
            compileRepositoryInst(repositoryInst, null);

            for (DomainInst domainInst : aggregationManager.getDomainInstList(projectVersionName)) {
                if (domainInst.getProjectVersionName() != null) {
                    compileDomainInst(domainInst, null);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    class CompileProject<T> implements Callable<T> {
        private final INProject project;

        public CompileProject(INProject project) {
            this.project = project;
        }

        @Override
        public T call() {
            compileProject(project.getActiviteVersion().getVersionName());
            return (T) project;
        }

    }

    public void compile() {
        List<Callable<INProject>> domainTasks = new ArrayList<>();
        for (INProject project : projectCacheManager.getProjectList(ProjectDefAccess.Public)) {
            domainTasks.add(new CompileProject(project));
        }
        List<Future<INProject>> projectFutures = null;
        try {
            ExecutorService executorService = RemoteConnectionManager.getConntctionService(ESDFacrory.ESDKEY);
            projectFutures = executorService.invokeAll(domainTasks);
            for (Future<INProject> resultFuture : projectFutures) {
                try {
                    INProject result = resultFuture.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            executorService.shutdownNow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void compileRepositoryInst(RepositoryInst repositoryInst, ChromeProxy chrome) throws JDSException {
        if (repositoryInst.getRootPackage() != null) {
            String projectName = repositoryInst.getProjectVersionName();
            List<JavaSrcBean> javaSrcBeans = repositoryInst.getRootPackage().listAllFile();
            buildFactory.compileJavaSrc(javaSrcBeans, projectName, chrome);
        }
    }

    public void compileDomainInst(DomainInst dsmInst, ChromeProxy chrome) throws JDSException {
        if (dsmInst.getRootPackage() != null) {
            String projectName = dsmInst.getProjectVersionName();
            List<JavaSrcBean> javaSrcBeans = dsmInst.getRootPackage().listAllFile();
            javaSrcBeans.addAll(dsmInst.getViewInst().getJavaSrcBeans());
            buildFactory.compileJavaSrc(javaSrcBeans, projectName, chrome);
        }
    }

    public void compileViewInst(ViewInst viewInst, ChromeProxy chrome) throws JDSException {
        if (viewInst.getRootPackage() != null) {
            String projectName = viewInst.getProjectVersionName();
            List<JavaSrcBean> javaSrcBeans = viewInst.getRootPackage().listAllFile();
            buildFactory.compileJavaSrc(javaSrcBeans, projectName, chrome);
        }
    }

    private ViewInst initDefaultView(RepositoryInst repositoryInst) throws JDSException {
        String projectName = repositoryInst.getProjectVersionName();
        DomainInst domainInst = this.initDefaultDomain(repositoryInst);
        ViewInst viewInst = viewManager.getViewInstById(projectName);
        if (viewInst == null) {
            viewInst = new ViewInst(domainInst);
            viewInst.setName(repositoryInst.getName());
            viewInst.setPackageName(repositoryInst.getSpace());
            viewInst.setDesc("默认视图模板（" + repositoryInst.getDesc() + ")");
            viewManager.updateViewInst(viewInst, true);
        }
        return viewInst;

    }

    public DomainInst newDomain(String projectVersionName, UserSpace space) throws JDSException {
        INProject project = projectCacheManager.getProjectByName(projectVersionName);
        DomainInst domainInst = createDefaultDomain(projectVersionName, space);
        domainInst.setDomainId(UUID.randomUUID().toString());
        aggregationManager.updateDomainInst(domainInst, false);
        int k = aggregationManager.getDomainInstList(projectVersionName).size();
        domainInst.setPackageName(domainInst.getName() + k);
        domainInst.setName(domainInst.getName() + k);
        domainInst.setSpace(domainInst.getName());
        domainInst.setDesc(domainInst.getDesc());
        return domainInst;
    }

    public DomainInst createDefaultDomain(String projectVersionName, UserSpace space) throws JDSException {
        Set<String> tempIds = buildFactory.getTempManager().getDSMBeanTempId(DSMTempType.custom, DSMType.AGGREGATION);
        INProject project = projectCacheManager.getProjectByName(projectVersionName);
        String projectName = project.getProjectName();
        DomainInst domainInst = new DomainInst(projectName, space);
        domainInst.setJavaTempIds(tempIds);
        domainInst.setDesc("默认聚合（" + projectName + ")");
        return domainInst;
    }

    public ViewInst createView(DomainInst domainInst) throws JDSException {
        ViewInst viewInst = viewManager.createDefaultView(domainInst);
        return viewInst;
    }

    public DomainInst getDomainInstById(String domainId) {
        return getDomainInstById(domainId, null);
    }


    public List<DomainInst> getAllDomainInst(String projectVersionName) throws JDSException {
        return aggregationManager.getDomainInstList(projectVersionName);
    }

    public List<DomainInst> getAllUserDomainInst(String projectVersionName) throws JDSException {
        return aggregationManager.getAllUserDomainInst(projectVersionName);
    }

    public List<DomainInst> findDomainInst(String projectVersionName, UserSpace... userSpace) throws JDSException {
        List<DomainInst> domainInstList = new ArrayList<>();
        List<DomainInst> domainInsts = aggregationManager.getDomainInstList(projectVersionName);
        for (DomainInst domainInst : domainInsts) {
            if (Arrays.asList(userSpace).contains(domainInst.getUserSpace())) {
                domainInstList.add(domainInst);
            }
        }
        return domainInstList;
    }

    public DomainInst getDomainInstById(String domainId, String projectVersionName) {
        DomainInst domainInst = aggregationManager.getDomainInstById(domainId, projectVersionName);
        return domainInst;
    }


    public ViewInst getViewInstById(String viewInstId) {
        ViewInst viewInst = viewManager.getViewInstById(viewInstId);
        return viewInst;
    }

    public DomainInst getDefaultDomain(String projectVersionName, UserSpace userSpace) {
        return aggregationManager.getDomainInstByCat(projectVersionName, userSpace);
    }

    public DomainInst getDefaultDomain(PackageType apiType) {
        return getDefaultDomain(apiType.getDefaultProjectName(), apiType.getCatType());
    }

    private DomainInst initDefaultDomain(RepositoryInst repositoryInst) throws JDSException {
        DomainInst domainInst = aggregationManager.getDomainInstById(repositoryInst.getProjectVersionName(), repositoryInst.getProjectVersionName());
        if (domainInst == null) {
            domainInst = createDefaultDomain(repositoryInst.getProjectVersionName(), UserSpace.SYS);
            domainInst.setName(repositoryInst.getName());
            domainInst.setDesc("默认聚合模板（" + repositoryInst.getDesc() + ")");
            aggregationManager.updateDomainInst(domainInst, true);
        }
        return domainInst;
    }


    public void reBuildTableView(RepositoryInst repositoryInst, UserSpace userSpace, ChromeProxy chrome) throws JDSException {
        DomainInst domainInst = DSMFactory.getInstance().getDefaultDomain(repositoryInst.getProjectVersionName(), userSpace);
        List<ESDClass> tableList = domainInst.getEntityList(AggregationType.REPOSITORY, false);
        List<Callable<List<JavaSrcBean>>> tasks = viewManager.genESDClassTask(domainInst, tableList, chrome);
        BuildFactory.getInstance().syncTasks(domainInst.getDomainId(), tasks);
        ViewInst viewInst = domainInst.getViewInst();
        compileViewInst(viewInst, chrome);

    }

    public void genRepository(RepositoryInst repositoryInst, UserSpace userSpace, ChromeProxy chrome) throws JDSException {
        DomainInst domainInst = DSMFactory.getInstance().getDefaultDomain(repositoryInst.getProjectVersionName(), userSpace);
        List<ESDClass> tableList = domainInst.getEntityList(AggregationType.REPOSITORY, false);
        List<Callable<List<JavaSrcBean>>> tasks = viewManager.genESDClassTask(domainInst, tableList, chrome);
        BuildFactory.getInstance().syncTasks(domainInst.getDomainId(), tasks);
        ViewInst viewInst = domainInst.getViewInst();
        compileViewInst(viewInst, chrome);

    }

    public void buildProject(RepositoryInst repositoryInst, ChromeProxy chrome) throws JDSException {
        try {
            repositoryManager.buildRepository(repositoryInst, chrome);
            //编译资源库
            compileRepositoryInst(repositoryInst, chrome);
            //初始化视图
            ViewInst viewInst = initDefaultView(repositoryInst);

            //初始域
            DomainInst domainInst = initDefaultDomain(repositoryInst);

            viewInst = buildRepositoryTable(domainInst, chrome, true);

        } catch (Exception e) {
            e.printStackTrace();

            throw e;
        }
    }


    private ViewInst buildRepositoryTable(DomainInst domainInst, ChromeProxy chrome, boolean compile) throws JDSException {
        ViewInst viewInst = domainInst.getViewInst();
        try {
            List<ESDClass> tableList = domainInst.getEntityList(AggregationType.REPOSITORY, false);
            List<Callable<List<JavaSrcBean>>> tasks = viewManager.genESDClassTask(domainInst, tableList, chrome);
            BuildFactory.getInstance().syncTasks(domainInst.getDomainId(), tasks);
            if (compile) {
                compileViewInst(viewInst, chrome);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return viewInst;

    }


    private ViewInst buildRepositoryView(DomainInst domainInst, ChromeProxy chrome, boolean compile) throws JDSException {
        RepositoryInst repositoryInst = domainInst.getRepositoryInst();
        ViewInst viewInst = initDefaultView(domainInst.getRepositoryInst());
        try {
            List<ESDClass> entityList = domainInst.getEntityList(AggregationType.ENTITY, false);
            ;
            List<Callable<List<JavaSrcBean>>> tasks = viewManager.genESDClassTask(domainInst, entityList, chrome);
            BuildFactory.getInstance().syncTasks(viewInst.getViewInstId(), tasks);
            if (compile) {
                compileViewInst(viewInst, chrome);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return viewInst;

    }

    public List<Class> dynDomainCompile(List<JavaSrcBean> javaSrcBeans, DomainInst domainInst, ChromeProxy chrome) throws JDSException {
        List<Class> classes = new ArrayList<>();
        for (JavaSrcBean srcBean : javaSrcBeans) {
            Class clazz = dynCompile(srcBean, domainInst.getProjectVersionName(), chrome);
            reBuildClass(clazz);
        }
        return classes;
    }


    public List<Class> dynViewCompile(List<JavaSrcBean> javaSrcBeans, String projectName, ChromeProxy chrome) throws JDSException {
        List<Class> classes = new ArrayList<>();
        INProject project = projectCacheManager.getProjectByName(projectName);
        for (JavaSrcBean srcBean : javaSrcBeans) {
            Class clazz = dynCompile(srcBean, projectName, chrome);
            reBuildViewClass(clazz, project.getProjectName());
        }
        return classes;
    }

    public List<Class> dynRepositoryCompile(List<JavaSrcBean> javaSrcBeans, String projectName, ChromeProxy chrome) throws JDSException {
        List<Class> classes = new ArrayList<>();
        for (JavaSrcBean srcBean : javaSrcBeans) {
            Class clazz = dynCompile(srcBean, projectName, chrome);
            reBuildRepositoryClass(clazz, projectName);
        }
        return classes;
    }


    public Class dynCompile(JavaSrcBean srcBean, String projectName, ChromeProxy chrome) throws JDSException {
        String className = srcBean.getClassName();
        ByteArrayInputStream input = null;
        Class clazz = null;
        try {
            clazz = GenJava.getInstance(projectName).dynCompile(className, srcBean.getContent());
            File packageFile = srcBean.getFile().getParentFile();
            if (className.indexOf(".") > -1) {
                className = className.substring(className.lastIndexOf(".") + 1);
            }
            className = className + ".class";
            File classFile = new File(packageFile, className);
            if (clazz.getClassLoader() instanceof DynamicClassLoader) {
                DynamicClassLoader loader = (DynamicClassLoader) clazz.getClassLoader();
                loader.dumpFile(classFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clazz;
    }

    public void clearDomain(DomainInst domainInst, ChromeProxy chrome) throws JDSException {
        ViewInst viewInst = this.initDefaultView(domainInst.getRepositoryInst());
        aggregationManager.genServiceBean(domainInst, AggregationType.ENTITY, chrome, domainInst.getProjectVersionName(), true);
        viewManager.reBuildView(domainInst, chrome, true);
        this.viewManager.updateViewInst(viewInst, true);
        this.aggregationManager.updateDomainInst(domainInst, true);
    }


    public void clearProject(String projectVersionName) throws JDSException {
        INProject project = projectCacheManager.getProjectByName(projectVersionName);
        List<DomainInst> domainInsts = aggregationManager.getDomainInstList(projectVersionName);
        List<String> domainIds = new ArrayList<>();
        for (DomainInst domainInst : domainInsts) {
            deleteDomainInst(domainInst);
        }
    }

    public void deleteDomainInst(String domainId) throws JDSException {
        DomainInst domainInst = this.getDomainInstById(domainId);
        deleteDomainInst(domainInst);
    }

    public void deleteDomainInst(DomainInst domainInst) throws JDSException {
        viewManager.deleteViewInst(domainInst.getViewInst().getViewInstId(), true);
        aggregationManager.deleteDomainInst(domainInst.getDomainId(), this.getDefaultProjectName(), true);

    }

    public DSMInst getDSMInst(String dsmId, DSMType dsmType) {
        DSMInst dsmInst = null;
        if (dsmType != null) {
            switch (dsmType) {
                case AGGREGATION:
                    dsmInst = getAggregationManager().getDomainInstById(dsmId, null);
                    break;
                case VIEW:
                    dsmInst = getViewManager().getViewInstById(dsmId, null);
                    break;
                case REPOSITORY:
                    dsmInst = getRepositoryManager().getProjectRepository(dsmId);
                    break;

            }
        } else {
            dsmInst = getAggregationManager().getDomainInstById(dsmId, null);
            if (dsmInst == null) {
                dsmInst = getViewManager().getViewInstById(dsmId, dsmInst.getProjectVersionName());
            }
            if (dsmInst == null) {
                dsmInst = getRepositoryManager().getProjectRepository(dsmId);
            }
        }

        return dsmInst;

    }

    public void saveCustomViewAPI(CustomViewBean viewBean) throws JDSException {
        String entityClassName = viewBean.getEntityClassName();
        ApiClassConfig apiClassConfig = getAggregationManager().getApiClassConfig(entityClassName);
        if (apiClassConfig != null) {
            getAggregationManager().updateApiClassConfig(apiClassConfig);
        }
    }

    public void saveCustomViewEntity(CustomViewBean viewBean) throws JDSException {
        String sourceClassName = viewBean.getSourceClassName();
        String domainId = viewBean.getDomainId();
        if (sourceClassName != null && domainId != null) {
            AggEntityConfig aggEntityConfig = getAggregationManager().getAggEntityConfig(viewBean.getViewClassName(), false);
            if (aggEntityConfig != null) {
                getAggregationManager().updateAggEntityConfig(aggEntityConfig);
            }

        }
    }

    public void saveCustomViewBean(CustomViewBean viewBean) throws JDSException {
        String sourceClassName = viewBean.getSourceClassName();
        String domainId = viewBean.getDomainId();
        if (sourceClassName != null && domainId != null) {
            ApiClassConfig apiClassConfig = getAggregationManager().getApiClassConfig(sourceClassName);
            if (apiClassConfig != null) {
                getAggregationManager().updateApiClassConfig(apiClassConfig);
            }
            AggEntityConfig aggEntityConfig = getAggregationManager().getAggEntityConfig(viewBean.getViewClassName(), false);
            if (aggEntityConfig != null) {
                getAggregationManager().updateAggEntityConfig(aggEntityConfig);
            }

        }
    }

    public AggregationManager getAggregationManager() {
        return aggregationManager;
    }

    public RepositoryManager getRepositoryManager() {
        return repositoryManager;
    }

    public BuildFactory getBuildFactory() {
        return buildFactory;
    }

    public CtVfsService getVfsClient() {
        CtVfsService vfsClient = CtVfsFactory.getCtVfsService();
        return vfsClient;
    }

    public String getDefaultProjectName() {
        String projectName = null;

        if (JDSServer.getClusterClient().isLogin()) {
            Object projectVersionName = JDSActionContext.getActionContext().getParams("projectVersionName");
            if (projectVersionName == null || projectVersionName.toString().equals("")) {
                projectVersionName = JDSActionContext.getActionContext().getParams("projectName");
            }

            if (projectVersionName == null || projectVersionName.toString().equals("")) {
                projectVersionName = JDSConfig.getValue("projectName");
            }
            if (projectVersionName == null || projectVersionName.toString().equals("")) {
                Object abstractHandler = JDSActionContext.getActionContext().getHandle();
                if (abstractHandler != null && abstractHandler instanceof AbstractHandler) {
                    try {
                        projectVersionName = ((AbstractHandler) abstractHandler).getProjectName((HttpRequest) JDSActionContext.getActionContext().getHttpRequest());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (projectVersionName != null && !projectVersionName.equals("")) {
                ProjectVersion version = null;
                try {
                    version = ESDFacrory.getAdminESDClient().getProjectVersionByName(projectVersionName.toString());
                    if (version != null) {
                        projectName = version.getProject().getProjectName();
                    }
                } catch (JDSException e) {
                    e.printStackTrace();
                }
            }
        } else {
            projectName = JDSConfig.getValue("projectName");
        }

        return projectName;
    }

    public ChromeProxy getCurrChromeDriver() {
        ChromeProxy chrome = JDSActionContext.getActionContext().Par("$currChromeDriver", ChromeProxy.class);
        if (chrome == null) {
            chrome = new LogSetpLog();
        }
        return chrome;
    }
}
