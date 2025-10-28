package net.ooder.esd.engine;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.ooder.annotation.UserSpace;
import net.ooder.common.FolderState;
import net.ooder.common.FolderType;
import net.ooder.common.JDSException;
import net.ooder.common.util.StringUtility;
import net.ooder.config.BPDProjectConfig;
import net.ooder.context.JDSActionContext;
import net.ooder.esb.config.formula.FormulaInst;
import net.ooder.esb.config.formula.ModuleFormulaInst;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.EUFileType;
import net.ooder.esd.bean.CustomViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.custom.component.CustomDynLoadView;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.repository.RepositoryInst;
import net.ooder.esd.engine.config.DevUserConfig;
import net.ooder.esd.engine.config.ProjectConfig;
import net.ooder.esd.engine.config.dsm.DSMProjectConfig;
import net.ooder.esd.engine.config.dsm.FormulasConfig;
import net.ooder.esd.engine.enums.*;
import net.ooder.esd.engine.inner.INProject;
import net.ooder.esd.engine.inner.INProjectVersion;
import net.ooder.esd.engine.task.SyncLoadProject;
import net.ooder.esd.tool.DSMProperties;
import net.ooder.esd.tool.TempConfigFactory;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.component.ComponentList;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.ModuleFunction;
import net.ooder.jds.core.esb.util.ActionContext;
import net.ooder.jds.core.esb.util.ValueStack;
import net.ooder.org.PersonNotFoundException;
import net.ooder.server.JDSServer;
import net.ooder.server.OrgManagerFactory;
import net.ooder.template.JDSScopesHashModel;
import net.ooder.vfs.*;
import net.ooder.vfs.ct.CtVfsFactory;
import net.ooder.vfs.ct.CtVfsService;
import net.ooder.web.RemoteConnectionManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mvel2.templates.TemplateRuntime;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public class ProjectCacheManager {

    public static Log logger = LogFactory.getLog(ProjectCacheManager.class);

    public static final String THREAD_LOCK = "Thread Lock";

    public static final String defaultProjectTempName = "defaultProject" + ProjectDefAccess.Templat.getTag();

    public static final String versionspacePath = "versionspace/";

    public static final String defaultTempName = "module.ftl";

    public static final String VERSIONTOKEN = "VVVERSION";


    public static final String CLASSNAME = ".cls";

    public String projectversiospace = "versionspace/";


    public Map<String, DevUserConfig> userConfigMap = new HashMap<>();

    public static Map<String, ProjectCacheManager> managerMap = new HashMap<String, ProjectCacheManager>();

    private MySpace space;

    Map<String, INProject> projectMap = new HashMap<String, INProject>();

    Map<String, INProjectVersion> projectVersionCache = new HashMap<String, INProjectVersion>();

    Map<String, String> projectVersionNameMap = new HashMap<String, String>();

    Map<String, EUModule> moduleCache = new HashMap<String, EUModule>();

    Map<String, EUPackage> packageCache = new HashMap<String, EUPackage>();

    List<String> projectIdList = new ArrayList<String>();


    public static ProjectCacheManager getInstance(MySpace space) {
        String path = space.getPath();
        ProjectCacheManager manager = managerMap.get(path);
        if (manager == null) {
            synchronized (path) {
                manager = managerMap.get(path);
                if (manager == null) {
                    manager = new ProjectCacheManager(space);
                    managerMap.put(path, manager);
                }
            }
        }
        return manager;
    }


    public static ProjectCacheManager getCacheManager(String path) throws JDSException {
        ProjectCacheManager manager = null;
        CtVfsService vfsClientService = CtVfsFactory.getCtVfsService();
        Folder folder = vfsClientService.getFolderByPath(path);
        while (folder.getFolderType() != null && !folder.getFolderType().equals(FolderType.space)) {
            folder = folder.getParent();
        }

        if (folder != null) {
            MySpace space = ESDFacrory.getSpaceByPath(folder.getPath());
            if (space != null) {
                manager = getInstance(space);
            }

        }
        return manager;
    }


    ProjectCacheManager(MySpace space) {
        this.space = space;
        Long start = System.currentTimeMillis();
        projectversiospace = space.getPath() + versionspacePath;
        List<INProject> projectList = new ArrayList<>();
        ProjectDefAccess[] projectDefAccessess = ProjectDefAccess.values();
        for (ProjectDefAccess projectType : projectDefAccessess) {
            try {
                String path = space.getPath() + projectType.getPath();
                Folder spaceFolder = getVfsClient().mkDir(path);
                if (spaceFolder != null) {
                    List<Folder> infos = spaceFolder.getChildrenList();
                    for (Folder cfolder : infos) {
                        if (cfolder.getFolderType().equals(FolderType.project)) {
                            if (!projectIdList.contains(cfolder.getID())) {
                                projectIdList.add(cfolder.getID());
                            }
                        }
                    }
                }

            } catch (Exception e) {

            }
            List<INProject> projects = syncLoadProject(space.getSysId(), projectIdList, 3);
            projectList.addAll(projects);
            logger.info("load project " + projectList.size() + " end times=" + (System.currentTimeMillis() - start));
        }


    }


    public INProject cloneResourceProject(String projectName, String desc, String sproject, ProjectResourceType resourceType, String personId) throws JDSException {
        if (sproject == null || sproject.equals("")) {
            sproject = defaultProjectTempName;
        }
        INProjectVersion defaultProjectVersion = this.getProjectVersionByName(sproject);

        if (defaultProjectVersion == null) {
            throw new JDSException("模板工程不存在！,请先导入工程模板。");
        }

        INProject project = this.createResourceProject(projectName, desc, personId, resourceType);
        getVfsClient().cloneFolder(defaultProjectVersion.getPath(), project.getPath());
        this.reLoadProject(project.getId());

        return project;


    }


    public List<EUPackage> loadTopPackage(String projectVersionName) throws JDSException {
        List<EUPackage> packages = new ArrayList<>();
        INProjectVersion version = this.getProjectVersionByName(projectVersionName);
        List<Folder> folders = version.getRootFolder().getChildrenList();
        for (Folder folder : folders) {
            EUPackage euPackage = loadPackage(projectVersionName, folder.getName());
            if (euPackage.getPackagePathType() != null && euPackage.getPackagePathType().getApiType() != null) {
                PackageType packageType = euPackage.getPackagePathType().getApiType();
                if (packageType.getDefaultProjectName() == null || packageType.getDefaultProjectName().equals(version.getProjectName())) {
                    packages.add(euPackage);
                }
            } else {
                packages.add(euPackage);
            }
        }
        Collections.sort(packages, new Comparator<EUPackage>() {
            public int compare(EUPackage o1, EUPackage o2) {
                return o1.getPackageName().compareTo(o2.getPackageName());
            }
        });

        return packages;

    }


    public EUPackage loadPackage(String projectVersionName, String packageName) throws JDSException {
        String folderPath = packageName.replace(".", "/");
        String cacheName = projectVersionName + "[" + packageName + "]";
        EUPackage euPackage = this.packageCache.get(cacheName);
        if (euPackage == null || euPackage.getFolder() == null) {
            INProjectVersion version = this.getProjectVersionByName(projectVersionName);
            if (!folderPath.startsWith(version.getRootFolder().getPath())) {
                folderPath = version.getRootFolder().getPath() + folderPath;
            }
            Folder folder = this.getVfsClient().getFolderByPath(folderPath);
            if (folder != null) {
                euPackage = new EUPackage(version, folder);
                packageCache.put(cacheName, euPackage);
            }
        }
        return euPackage;

    }

    public EUPackage createPackage(String projectVersionName, String packageName) throws JDSException {
        EUPackage euPackage = loadPackage(projectVersionName, packageName);
        if (euPackage == null) {
            INProjectVersion version = this.getProjectVersionByName(projectVersionName);
            String folderPath = packageName.replace(".", "/");
            if (!folderPath.startsWith(version.getRootFolder().getPath())) {
                folderPath = version.getRootFolder().getPath() + folderPath;
            }
            Folder folder = this.getVfsClient().mkDir(folderPath);
            euPackage = new EUPackage(version, folder);
            String cacheName = projectVersionName + "[" + packageName + "]";
            packageCache.put(cacheName, euPackage);
        }

        return euPackage;
    }


    public INProject reLoadProjectRoot(String projectName) throws JDSException {
        INProject project = this.getProjectByName(projectName);
        if (project != null) {
            List<INProjectVersion> versions = project.getVersions();
            for (INProjectVersion version : versions) {
                this.getVfsClient().removeCache(version.getRootFolder().getPath());
            }
        }
        this.getVfsClient().clearCache(project.getPath());
        return this.getProjectByName(projectName);

    }


    public INProject reLoadProject(String projectName) throws JDSException {
        INProject project = this.getProjectByName(projectName);
        if (project != null) {
            List<INProjectVersion> versions = project.getVersions();
            for (INProjectVersion version : versions) {
                projectVersionNameMap.remove(projectName);
                projectVersionCache.remove(version.getVersionId());
            }
            projectMap.remove(projectName);
            projectMap.remove(project.getId());
            this.getVfsClient().clearCache(project.getActiviteVersion().getPath());


            for (INProjectVersion version : versions) {
                RemoteConnectionManager.getConntctionService(projectName).execute(new Runnable() {
                    @Override
                    public void run() {
                        rebuildModule(version.getVersionName(), true);
                    }
                });

            }
        }

        return this.getProjectByName(projectName);
    }

    public void updateFolderState(INProjectVersion version, ProjectVersionStatus status) throws JDSException {
        version.setStatus(status);
        this.getVfsClient().updateFolderState(version.getRootFolder(), status.getFolderState());
    }


    public void updateSpaceConfig(MySpaceConfig config) throws JDSException {
        this.space.setConfig(config);
        this.getVfsClient().saveFileAsContent(this.getSpace().getPath() + MySpace.configFileName, JSONObject.toJSONString(config), VFSConstants.Default_Encoding);
    }


    public DevUserConfig getUserConfig(String personId) {
        DevUserConfig userConfig = userConfigMap.get(personId);
        if (userConfig == null) {
            String configFileName = this.getSpace().getUserFolder().getPath() + personId + ".json";
            try {
                FileInfo fileInfo = CtVfsFactory.getCtVfsService().getFileByPath(configFileName);
                if (fileInfo != null) {
                    StringBuffer configJson = CtVfsFactory.getCtVfsService().readFileAsString(configFileName, VFSConstants.Default_Encoding);
                    if (configJson.length() > 0) {
                        userConfig = JSONObject.parseObject(configJson.toString(), DevUserConfig.class);
                        userConfigMap.put(personId, userConfig);
                    }
                }
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }

        return userConfig;
    }


    public void updateUserConfig(String personId, DevUserConfig userConfig) throws JDSException {
        String configFileName = this.getSpace().getUserFolder().getPath() + personId + ".json";
        userConfigMap.put(personId, userConfig);
        this.getVfsClient().saveFileAsContent(configFileName, JSONObject.toJSONString(userConfig), VFSConstants.Default_Encoding);
        ESDFacrory.getInstance().dumpCache();
    }

    public INProject getProjectByPath(String path) throws JDSException {
        Folder folder = null;
        INProject project = null;
        FileInfo fileInfo = this.getVfsClient().getFileByPath(path);
        if (fileInfo != null) {
            folder = fileInfo.getFolder();
        } else {
            folder = this.getVfsClient().getFolderByPath(path);
        }
        while (!folder.getFolderType().equals(FolderType.project)) {
            folder = folder.getParent();
        }
        if (folder != null) {
            if (folder.getPath().startsWith(projectversiospace)) {
                project = this.getProjectByName(folder.getParent().getName());
            } else {
                project = this.getProjectByName(folder.getName());
            }
        }
        return project;

    }

    public void deleteVersion(String versionName) throws JDSException {
        this.removeProjectVersion(this.getProjectVersionByName(versionName).getVersionId());
    }


    public MySpace getSpace() {
        return space;
    }

    public void setSpace(MySpace space) {
        this.space = space;
    }


    public List<INProjectVersion> getVersions(String projectName) throws JDSException {
        List<INProjectVersion> versions = new ArrayList<INProjectVersion>();
        INProject project = this.getProjectByName(projectName);
        Folder versionfolder = getVfsClient().mkDir(projectversiospace + projectName + "/");
        List<Folder> versionFolders = versionfolder.getChildrenList();
        if (versionFolders.isEmpty()) {
            Folder folder = getVfsClient().mkDir(versionfolder.getPath() + versions.size(), project.getProjectName() + ProjectCacheManager.VERSIONTOKEN + versions.size(), FolderType.project);
            getVfsClient().cloneFolder(project.getPath(), folder.getPath());
            INProjectVersion version = getProjectVersionByName(folder.getParent().getName() + ProjectCacheManager.VERSIONTOKEN + folder.getName());
            versions.add(version);
        } else {
            for (Folder versionFolder : versionFolders) {
                INProjectVersion version = getProjectVersionByName(project.projectName + ProjectCacheManager.VERSIONTOKEN + versionFolder.getName());
                if (version != null) {
                    versions.add(version);
                }
            }
        }

        Collections.sort(versions, new Comparator<INProjectVersion>() {
            public int compare(INProjectVersion o1, INProjectVersion o2) {
                if (o1 != null && o2 != null) {
                    return o2.getVersion() - o1.getVersion();
                }
                return -1;
            }
        });

        return versions;
    }


    public INProjectVersion createProjectVersion(INProject project) throws JDSException {
        Integer versionnum = 0;
        if (project.getVersions() != null && project.getVersions().size() > 0) {
            versionnum = project.getVersions().get(0).getVersion() + 1;
        }
        INProjectVersion activiteVersion = project.getActiviteVersion();

        Folder folder = getVfsClient().mkDir(projectversiospace + project.getProjectName() + "/" + versionnum, project.getProjectName() + VERSIONTOKEN + versionnum, FolderType.project);

        if (activiteVersion != null) {
            this.getVfsClient().cloneFolder(activiteVersion.getPath(), folder.getPath());
        }


        folder = this.getVfsClient().updateFolderState(folder, FolderState.tested);
        INProjectVersion version = getProjectVersionByName(project.getProjectName() + ProjectCacheManager.VERSIONTOKEN + versionnum);
        projectVersionNameMap.put(project.getProjectName() + VERSIONTOKEN + versionnum, version.getVersionId());
        return version;

    }


    /**
     * 新建资源 工程
     *
     * @param projectName
     * @return
     * @throws JDSException
     */
    public INProject createResourceProject(String projectName, String desc, String personId, ProjectResourceType type) throws JDSException {
        INProject project = createProject(projectName, desc, personId, ProjectDefAccess.Resource);
        ProjectConfig config = project.getConfig();
        config.setResourceType(type);
        this.updateProjectConfig(project.getId(), config);
        return project;
    }

    public Set<EUModule> rebuildModule(String versionName, boolean reload) {
        Folder folder = null;
        INProjectVersion version = this.getProjectVersionByName(versionName);
        Set<EUModule> modules = new HashSet<EUModule>();
        try {
            folder = this.getVfsClient().getFolderByPath(version.getPath());
        } catch (JDSException e) {
            e.printStackTrace();
        }
        List<FileInfo> fileInfos = folder.getFileListRecursively();

        Set<String> versionIds = new HashSet<>();

        Set<String> objIds = new HashSet<>();

        List<FileInfo> clsFileInfos = new ArrayList<>();

        for (FileInfo fileInfo : fileInfos) {
            if (fileInfo != null && fileInfo.getPath().endsWith(CLASSNAME)) {
                clsFileInfos.add(fileInfo);
                versionIds.add(fileInfo.getCurrentVersonId());
                objIds.add(fileInfo.getCurrentVersonFileHash());
            }
        }
        try {
            List<FileVersion> versions = CtVfsFactory.getCtVfsService().loadVersionByIds(versionIds);
            List<FileObject> objectList = CtVfsFactory.getCtVfsService().loadObjects(objIds);
        } catch (JDSException e) {
            e.printStackTrace();
        }


        for (FileInfo fileInfo : clsFileInfos) {
            EUModule euModule = null;
            try {
                euModule = loadEUClass(fileInfo.getPath(), versionName, reload);
                if (euModule != null) {
                    modules.add(euModule);
                }
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        return modules;
    }


    public Set<EUModule> build(String versionName) throws JDSException {
        return rebuildModule(versionName, false);
    }

    public EUModule loadEUClass(String path, String versionName, boolean reload) throws JDSException {
        FileInfo fileInfo = this.getVfsClient().getFileByPath(path);
        EUModule euModule = null;
        INProjectVersion projectVersion = getProjectVersionByName(versionName);
        if (fileInfo != null && fileInfo.getFolder() != null) {
            euModule = moduleCache.get(fileInfo.getPath());
            if (euModule == null || reload) {
                String className = StringUtility.replace(fileInfo.getPath(), projectVersion.getPath(), "");
                className = StringUtility.replace(className, "/", ".");
                euModule = new EUModule(projectVersion, className);
                ModuleComponent moduleComponent = loadEUClass(euModule);
                if (moduleComponent != null) {
                    euModule.setFiletype(euModule.getFiletype());
                    euModule.setComponent(moduleComponent);
                    moduleCache.put(fileInfo.getPath(), euModule);
                } else {
                    euModule = null;
                }
            }
        }
        return euModule;
    }


    public Boolean delModule(EUModule euModule) {
        String path = euModule.getPath();
        FileInfo fileInfo = null;
        try {
            fileInfo = this.getVfsClient().getFileByPath(path);
            if (fileInfo != null) {
                this.getVfsClient().deleteFile(fileInfo.getID());
            }
        } catch (JDSException e) {
            e.printStackTrace();
            return false;
        }
        moduleCache.remove(path);
        return true;
    }

    public void saveModule(EUModule euModule, boolean dynBuild) throws JDSException {
        String path = euModule.getPath();
        ModuleComponent moduleComponent = euModule.getComponent();
        if (moduleComponent instanceof CustomDynLoadView) {
            return;
        }

        moduleComponent.setModuleType(euModule.getFiletype());
        MethodConfig methodConfig = moduleComponent.getMethodAPIBean();

        if (methodConfig != null && dynBuild) {
            DSMProperties dsmProperties = moduleComponent.getProperties().getDsmProperties();
            if (dsmProperties == null) {
                dsmProperties = new DSMProperties(methodConfig, euModule.getProjectVersion().getProjectName());
                moduleComponent.getProperties().setDsmProperties(dsmProperties);
            }
            String domainId = dsmProperties.getDomainId();

            CustomViewBean customViewBean = methodConfig.getView();
            boolean canSave = false;
            if (!moduleComponent.getFormulas().isEmpty()) {
                List<ModuleFormulaInst> formulaInstMap = moduleComponent.getFormulas();
                if (dsmProperties != null) {
                    dsmProperties.update(methodConfig);
                    if (!formulaInstMap.equals(customViewBean.getFormulas())) {
                        customViewBean.setFormulas(formulaInstMap);
                    }
                    canSave = true;
                }
            }

            ProjectDefAccess defAccess = moduleComponent.getEuModule().getProjectVersion().getProject().getDefAccess();
            if (domainId != null) {
                DomainInst domainInst = DSMFactory.getInstance().getDomainInstById(domainId);
                if (defAccess.equals(ProjectDefAccess.Public) && domainInst.getUserSpace().equals(UserSpace.VIEW)) {
                    customViewBean.updateModule(moduleComponent);
                    canSave = true;
                }
            }
            if (canSave) {
                DSMFactory.getInstance().saveCustomViewBean(customViewBean);
            }
        }

        String json = JSONObject.toJSONString(moduleComponent, true);
        json = (String) TemplateRuntime.eval(json, moduleComponent, JDSActionContext.getActionContext().getContext());
        try {
            moduleCache.put(path, euModule);
            this.getVfsClient().saveFileAsContent(path, json, VFSConstants.Default_Encoding);
        } catch (Throwable e) {
            e.printStackTrace();
            logger.error(e);
        }
    }


    ModuleComponent loadEUClass(EUModule module) throws JDSException {
        String classPath = module.getPath();
        StringBuffer buffer = getVfsClient().readFileAsString(classPath, VFSConstants.Default_Encoding);
        ModuleComponent moduleComponent = null;
        try {
            if (buffer != null && buffer.length() > 0) {
                moduleComponent = JSONObject.parseObject(buffer.toString(), ModuleComponent.class);
                moduleComponent.setEuModule(module);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            logger.warn("文件格式错误[" + module.getPath() + "]");
            //throw new JDSException("文件格式错误["+module.getPath()+"]");
        }

        return moduleComponent;

    }

    public ModuleComponent cloneModuleComponent(ModuleComponent component) throws JDSException {
        ModuleComponent moduleComponent = null;
        try {
//                     EUModule oTopModule = (EUModule) JDSActionContext.getActionContext().getContext().get(CustomViewFactory.TopModuleKey);
//            EUModule omodule = (EUModule) JDSActionContext.getActionContext().getContext().get(CustomViewFactory.CurrModuleKey);
//            if (oTopModule == null) {
//                JDSActionContext.getActionContext().getContext().put(CustomViewFactory.TopModuleKey,component.getEuModule());
//            }
//            if (omodule == null) {
//                JDSActionContext.getActionContext().getContext().put(CustomViewFactory.CurrModuleKey, component.getEuModule());
//            }

            String json = JSONObject.toJSONString(component, true);
            //    String obj = (String) TemplateRuntime.eval(json, JDSActionContext.getActionContext().getContext());
            moduleComponent = JSONObject.parseObject(json, ModuleComponent.class);
        } catch (Throwable e) {
            e.printStackTrace();
            logger.warn("文件格式错误[" + component.getPath() + "]");
            //throw new JDSException("文件格式错误["+module.getPath()+"]");
        }

        return moduleComponent;

    }


    public EUModule createModule(INProjectVersion inProjectVersion, String className) throws JDSException {
        EUModule module = new EUModule(inProjectVersion, className);
        this.moduleCache.put(module.getPath(), module);
        return module;
    }

    public EUModule createCustomModule(INProjectVersion inProjectVersion, String className) throws JDSException {
        EUModule module = new EUModule(inProjectVersion, className, EUFileType.CustomView);
        this.moduleCache.put(module.getPath(), module);
        return module;
    }


    public List<INProject> syncLoadProject(String taskId, List<String> projectIds, int pageSize) {
        List<INProject> projectList = new ArrayList<>();
        try {
            if (taskId == null) {
                taskId = DSMFactory.DefaultDsmName;
            }
            //批量装载数据
            if (projectIds.size() > 0) {
                Integer start = 0;
                int size = projectIds.size();
                String[] delfileInfoIds = projectIds.toArray(new String[projectIds.size()]);
                int page = 0;
                while (page * pageSize < size) {
                    page++;
                }
                List<SyncLoadProject<List<INProject>>> syncLoadProjectTasks = new ArrayList<>();
                for (int k = 0; k < page; k++) {
                    int end = start + pageSize;
                    if (end >= size) {
                        end = size;
                    }
                    String[] loadFileIds = Arrays.copyOfRange(delfileInfoIds, start, start + pageSize);
                    SyncLoadProject loadProject = new SyncLoadProject(this, loadFileIds);
                    syncLoadProjectTasks.add(loadProject);
                    start = end;
                }
                RemoteConnectionManager.initConnection(taskId, syncLoadProjectTasks.size());
                List<Future<List<INProject>>> futures = RemoteConnectionManager.getConntctionService(taskId).invokeAll(syncLoadProjectTasks);
                for (Future<List<INProject>> resultFuture : futures) {
                    try {
                        projectList = resultFuture.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }

            RemoteConnectionManager.getConntctionService(taskId).shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return projectList;
    }


    public StringBuffer genJSON(ModuleComponent component, String tempName, boolean prettyFormat) {
        if (tempName == null || tempName.equals("")) {
            tempName = defaultTempName;
        }
        //boolean prettyFormat = true;
        if (JDSActionContext.getActionContext().getContext().get("build") != null) {
            prettyFormat = false;
        }
        EUModule topmodule = (EUModule) JDSActionContext.getActionContext().getContext().get(CustomViewFactory.TopModuleKey);


        //预装载应用
        List<APICallerComponent> apiCalls = component.findComponents(ComponentType.APICALLER, null);
        for (APICallerComponent apiCallerComponent : apiCalls) {
            apiCallerComponent.getProperties().setCurrClassName(component.getClassName());

        }


        Configuration cfg = TempConfigFactory.getInstance().getCfg();
        StringWriter out = new StringWriter();
        ValueStack stack = ActionContext.getContext().getValueStack();
        if (topmodule != null) {
            stack.push(topmodule);
        }

        ComponentList componentList = component.getChildren();
        if (componentList == null) {
            componentList = new ComponentList();
        }

        Map<String, ModuleFunction> functions = component.getFunctions();
        if (functions == null) {
            functions = new HashMap<>();
        }

        List<FormulaInst> rightFormulas = component.getFormulas();
        if (rightFormulas == null) {
            rightFormulas = new ArrayList<>();
        }
        Object projectVersionName = null;
        try {
            projectVersionName = DSMFactory.getInstance().getDefaultProjectName();
        } catch (JDSException e) {
            e.printStackTrace();
        }
        if (projectVersionName != null) {
            // component.getProperties().setProjectName(projectName);
            Map valueMap = new HashMap();
            valueMap.put("projectVersionName", projectVersionName);
            valueMap.put("projectName", projectVersionName);
            component.fillFormValues(valueMap, true);
        }


        stack.getContext().put("className", component.getClassName());
        stack.getContext().put("properties", JSONObject.toJSONString(component.getProperties(), prettyFormat));
        stack.getContext().put("childrenJson", componentList.toJson(prettyFormat));
        stack.getContext().put("functions", JSONObject.toJSONString(functions, prettyFormat));
        stack.getContext().put("formulas", JSONObject.toJSONString(rightFormulas, prettyFormat));


        Map<String, String> customFunctions = component.getCustomFunctions();
        JDSActionContext.getActionContext().getContext().remove(CustomViewFactory.TopModuleKey);
        StringBuffer customScript = new StringBuffer("");
        if (customFunctions.size() > 0) {
            Set<String> keySet = customFunctions.keySet();
            int k = 0;
            for (String key : keySet) {
                k = k + 1;
                customScript.append(key + ":" + customFunctions.get(key));
                if (k < keySet.size()) {
                    customScript.append(",\n");
                }
            }
        }

        Map<String, String> moduleVar = component.getModuleVar();
        StringBuffer moduleVarString = new StringBuffer("");
        if (moduleVar.size() > 0) {
            Set<String> keySet = moduleVar.keySet();
            int k = 0;
            for (String key : keySet) {
                k = k + 1;
                moduleVarString.append(key + ":" + moduleVar.get(key));
                if (k < keySet.size()) {
                    moduleVarString.append(",\n");
                }
            }
        }


        if (component.getViewConfig() != null) {
            stack.getContext().put("Static", JSONObject.toJSONString(component.getViewConfig(), prettyFormat));

        } else {
            stack.getContext().put("Static", "{}");

        }

        String customAppendStr = "function(parent, subId, left, top){ return false}";

        if (component.getCustomAppend() != null && !component.getCustomAppend().equals("")) {
            customAppendStr = component.getCustomAppend();
        }
        if (component.getAfterAppend() != null && !component.getAfterAppend().equals("")) {
            stack.getContext().put("afterAppend", component.getAfterAppend());
        }

        stack.getContext().put("customAppendStr", customAppendStr);

        if (moduleVarString.length() > 0) {
            stack.getContext().put("moduleVarStr", moduleVarString.toString());
        }
        if (customScript.length() > 0) {
            stack.getContext().put("customScriptStr", customScript.toString());
        }

        stack.getContext().put("events", JSONObject.toJSONString(component.getEvents(), prettyFormat));
        stack.getContext().put("required", JSONArray.toJSONString(component.getRequired(), prettyFormat));
        stack.getContext().put("dependencies", JSONArray.toJSONString(component.getDependencies(), prettyFormat));
        JDSScopesHashModel model = new JDSScopesHashModel(cfg.getObjectWrapper(), stack);
        try {
            Template temp = cfg.getTemplate(tempName, VFSConstants.Default_Encoding);
            temp.process(model, out);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }

        return out.getBuffer();


    }


    public StringBuffer genJSON(EUModule module, String tempName, boolean prettyFormat) {
        JDSActionContext.getActionContext().getContext().put("TopModule", module);
        JDSActionContext.getActionContext().getContext().put("CurrModule", module);

        return genJSON(module.getComponent(), tempName, prettyFormat);
    }


    /**
     * 新建资源 工程
     *
     * @param projectName
     * @return
     * @throws JDSException
     */
    public INProject createProject(String projectName, String desc, String personId, ProjectDefAccess type) throws JDSException {

        if (projectName == null) {
            throw new JDSException("工程名称不能为空！");
        }

        if (!projectName.endsWith(type.getTag())) {
            projectName = projectName + type.getTag();
        }
        INProject project = projectMap.get(projectName);
        if (project != null) {
            throw new JDSException("[" + projectName + "]工程已存在！");
        }
        if (desc == null || desc.equals("")) {
            desc = projectName;
        }
        Folder folder = getVfsClient().mkDir(this.space.getPath() + type.getPath() + "/" + projectName, desc, FolderType.project);
        folder = this.getVfsClient().getFolderByPath(folder.getPath());
        folder.createChildFolder("App", folder.getPersonId());
        folder.createChildFolder("Module", folder.getPersonId());
        project = new INProject(folder, this.space);

        projectMap.put(projectName, project);
        projectMap.put(project.getId(), project);
        if (!projectIdList.contains(project.getId())) {
            projectIdList.add(project.getId());
        }
        return project;

    }


    public INProject updateProjectInfo(String projectName, String desc, String personId) throws JDSException {
        if (projectName == null) {
            throw new JDSException("工程名称不能为空！");
        }
        INProject project = projectMap.get(projectName);
        if (desc == null || desc.equals("")) {
            desc = projectName;
        }
        if (project == null) {
            project = this.getProjectByName(projectName);
        }

        Folder folder = getVfsClient().updateFolderInfo(project.getRootfolder(), projectName, desc);
        folder = this.getVfsClient().getFolderByPath(folder.getPath());
        project = new INProject(folder, space);
        // project.setDefAccess(ProjectDefAccess.Public);
        projectMap.put(projectName, project);
        projectMap.put(project.getId(), project);
        if (!projectIdList.contains(project.getId())) {
            projectIdList.add(project.getId());
        }
        return project;

    }


    public INProject cloneProject(String projectName, String desc, String sproject, ProjectDefAccess type, String personId) throws JDSException {

        if (sproject == null || sproject.equals("")) {
            sproject = defaultProjectTempName;
        }
        INProjectVersion defaultProjectVersion = this.getProjectVersionByName(sproject);

        if (defaultProjectVersion == null) {
            throw new JDSException("[" + projectName + "]模板工程不存在！");
        }
        INProjectVersion projectVersion = this.getProjectVersionByName(projectName);
        if (projectVersion != null) {
            projectName = projectName + "1";
        }

        INProject project = this.createProject(projectName, desc, personId, type);
        getVfsClient().cloneFolder(defaultProjectVersion.getPath(), project.getPath());
        project.getConfig().clearDevPersons();
        try {
            project.getDevPersons(ProjectRoleType.own).add(OrgManagerFactory.getOrgManager().getPersonByID(personId));
        } catch (PersonNotFoundException e) {
            e.printStackTrace();
        }

        this.reLoadProject(project.getId());
        updateFolderState(project.getVersions().get(0), ProjectVersionStatus.RELEASED);
        return project;

    }


    public Map<String, INProject> getProjectMap() {
        return projectMap;
    }

    public void setProjectMap(Map<String, INProject> projectMap) {
        this.projectMap = projectMap;
    }

    public List<INProject> getResourceProjectList(ProjectResourceType type) {
        List<INProject> tempprojects = getProjectList(ProjectDefAccess.Resource);
        List<INProject> projects = new ArrayList<INProject>();
        for (INProject inProject : tempprojects) {
            if (inProject.getResourceType() != null && inProject.getResourceType().equals(type)) {
                projects.add(inProject);
            }
        }
        return projects;
    }

    public List<INProject> getProjectList(ProjectDefAccess type) {
        List<INProject> projects = new ArrayList<INProject>();
        for (String projectId : projectIdList) {
            if (projectId != null) {
                INProject inProject = this.getProjectById(projectId);
                if (inProject != null && inProject.getDefAccess() != null && inProject.getDefAccess().equals(type)) {
                    projects.add(inProject);
                }
            }
        }
        return projects;
    }


    public List<INProject> getAllProjectList() {
        List<INProject> projects = new ArrayList<INProject>();
        for (String projectId : projectIdList) {
            if (projectId != null) {
                INProject inProject = this.getProjectById(projectId);
                if (inProject != null) {
                    projects.add(inProject);
                }
            }

        }
        return projects;
    }


    public void updateProjectConfig(String projectName, ProjectConfig config) throws JDSException {
        INProject project = getProjectByName(projectName);
        if (config == null) {
            config = project.getConfig();
        } else {
            project.setConfig(config);
        }
        this.getVfsClient().saveFileAsContent(project.getPath() + INProject.configFileName, JSONObject.toJSONString(config, true), VFSConstants.Default_Encoding);

    }

    public void updateRepositoryConfig(String projectName, RepositoryInst repositoryInst) throws JDSException {
        INProject project = getProjectByName(projectName);
        if (repositoryInst == null) {
            repositoryInst = project.getRepository();
        } else {
            project.setRepository(repositoryInst);
        }
        this.getVfsClient().saveFileAsContent(project.getPath() + INProject.configFolderName + "/" + INProject.repositoryFileName, JSONObject.toJSONString(repositoryInst, true), VFSConstants.Default_Encoding);
    }

    public void updateDSMConfig(String projectName, DSMProjectConfig dsmProjectConfig) throws JDSException {
        INProject project = getProjectByName(projectName);
        if (dsmProjectConfig == null) {
            dsmProjectConfig = project.getDsmProjectConfig();
        } else {
            project.setDsmProjectConfig(dsmProjectConfig);
        }

        this.getVfsClient().saveFileAsContent(project.getPath() + INProject.configFolderName + "/" + INProject.dsmFileName, JSONObject.toJSONString(dsmProjectConfig, true), VFSConstants.Default_Encoding);
    }


    public void updateFormulaConfig(String projectName, FormulaInst formulaInst) throws JDSException {
        INProject project = getProjectByName(projectName);
        List<FormulaInst> formulaInstList = project.getFormulas();
        FormulaInst oldformulaInst = project.getFormula(formulaInst.getFormulaInstId());
        if (oldformulaInst != null) {
            oldformulaInst.setExpression(formulaInst.getExpression());
            oldformulaInst.setFormulaType(formulaInst.getFormulaType());
            oldformulaInst.setName(formulaInst.getName());
            oldformulaInst.setParams(formulaInst.getParams());
            oldformulaInst.setParticipantSelectId(formulaInst.getParticipantSelectId());
            oldformulaInst.setExpression(formulaInst.getExpression());
            oldformulaInst.setSelectDesc(formulaInst.getSelectDesc());
            oldformulaInst.setProjectName(formulaInst.getProjectName());
        } else {
            formulaInstList.add(formulaInst);
        }

        FormulasConfig formulasConfig = new FormulasConfig();
        formulasConfig.setFormulas(formulaInstList);
        this.getVfsClient().saveFileAsContent(project.getPath() + INProject.configFolderName + "/" + INProject.dsmFileName, JSONObject.toJSONString(formulasConfig, true), VFSConstants.Default_Encoding);
    }

    public void deleteFormulaConfig(String projectName, String formulaInstId) throws JDSException {
        INProject project = getProjectByName(projectName);
        List<FormulaInst> formulaInstList = project.getFormulas();
        formulaInstList.remove(formulaInstId);
        FormulasConfig formulasConfig = new FormulasConfig();
        formulasConfig.setFormulas(formulaInstList);
        this.getVfsClient().saveFileAsContent(project.getPath() + INProject.configFolderName + "/" + INProject.dsmFileName, JSONObject.toJSONString(formulasConfig, true), VFSConstants.Default_Encoding);
    }

    public void updateBPDProjectConfig(String projectName, BPDProjectConfig bpdProjectConfig) throws JDSException {
        INProject project = getProjectByName(projectName);
        if (bpdProjectConfig == null) {
            bpdProjectConfig = project.getBpdProjectConfig();
        } else {
            project.setBpdProjectConfig(bpdProjectConfig);
        }
        this.getVfsClient().saveFileAsContent(project.getPath() + INProject.configFolderName + "/" + INProject.bpdFileName, JSONObject.toJSONString(bpdProjectConfig, true), VFSConstants.Default_Encoding);
    }


    public INProject getProjectById(String id) {
        if (id == null) {
            return null;
        }
        INProject project = projectMap.get(id);
        if (project == null) {
            Folder projectFolder = null;
            try {
                projectFolder = getVfsClient().getFolderById(id);
            } catch (JDSException e) {
                e.printStackTrace();
            }
            if (projectFolder != null) {
                project = new INProject(projectFolder, space);
                projectMap.put(project.getId(), project);
                projectMap.put(project.getProjectName(), project);
            }

        }
        return project;
    }

    public INProject getProjectByName(String name, ProjectDefAccess type) throws JDSException {
        return getProjectByName(name + type.getPath());
    }

    public INProject getProjectByName(String name) throws JDSException {
        if (name.indexOf(VERSIONTOKEN) > -1) {
            name = name.split(VERSIONTOKEN)[0];
        }
        INProject project = projectMap.get(name);

        if (project == null) {

            for (ProjectDefAccess type : ProjectDefAccess.values()) {
                Folder spaceFolder = getVfsClient().getFolderByPath(space.getPath() + type.getPath());
                if (spaceFolder != null) {
                    Folder projectFolder = getVfsClient().getFolderByPath(spaceFolder.getPath() + name);
                    if (projectFolder != null) {
                        project = new INProject(projectFolder, space);
                        projectMap.put(name, project);
                        projectMap.put(project.getId(), project);
                    }
                }
            }
        }

        if (project == null && !JDSServer.getClusterClient().isLogin()) {
            Folder projectFolder = getVfsClient().mkDir(space.getPath() + ProjectDefAccess.Public.getPath() + "/" + name);
            project = new INProject(projectFolder, space);
            projectMap.put(name, project);
            projectMap.put(project.getId(), project);
        }

        return project;
    }

    public INProjectVersion getProjectVersionById(String vresionId) {
        INProjectVersion projectVersion = null;
        if (vresionId != null) {
            projectVersion = projectVersionCache.get(vresionId);
            if (projectVersion == null) {
                try {
                    Folder projectFolder = this.getVfsClient().getFolderById(vresionId);
                    INProject project = getProjectByName(projectFolder.getParent().getName());
                    projectVersion = new INProjectVersion(project.getId(), projectFolder.getPath(), space);
                    projectVersionNameMap.put(projectVersion.getVersionName(), projectVersion.getVersionId());
                    projectVersionCache.put(projectVersion.getVersionId(), projectVersion);

                } catch (JDSException e) {
                    e.printStackTrace();
                }

            }
        }
        return projectVersion;
    }


    public INProjectVersion getProjectVersionByName(String name) {
        String projectVersionId = projectVersionNameMap.get(name);
        INProjectVersion projectVersion = this.getProjectVersionById(projectVersionId);
        if (projectVersion == null) {
            try {
                if (name.indexOf(VERSIONTOKEN) > -1) {
                    Folder vresionFolder = getVfsClient().getFolderByPath(projectversiospace + StringUtility.replace(name, VERSIONTOKEN, "/"));
                    if (vresionFolder != null) {
                        projectVersion = this.getProjectVersionById(vresionFolder.getID());
                    }
                } else {
                    INProject project = this.getProjectByName(name);
                    if (project != null) {
                        projectVersion = project.getActiviteVersion();
                        if (projectVersion == null) {
                            projectVersion = this.createProjectVersion(project);
                        }
                    }

                }
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        return projectVersion;
    }

    public void removeProject(String name) throws JDSException {
        INProject project = this.getProjectByName(name);
        List<INProjectVersion> versions = project.getVersions();

        for (INProjectVersion version : versions) {
            if (version.getStatus().equals(ProjectVersionStatus.RELEASED)) {
                throw new JDSException("工程存在正式发布版本，请联系管理员冻结流程后，再删除");
            }
        }
        for (INProjectVersion version : versions) {
            this.removeProjectVersion(version.getVersionId());
        }


        this.getVfsClient().deleteFolder(project.getId());
        projectIdList.remove(project.getId());
        projectMap.remove(project.getProjectName());
        projectMap.remove(project.getId());
    }

    public void removeProjectVersion(String versionId) throws JDSException {
        if (versionId != null) {
            INProjectVersion version = this.getProjectVersionById(versionId);
            this.getVfsClient().deleteFolder(versionId);
            projectVersionNameMap.remove(version.getVersionName());
            projectVersionCache.remove(version.getVersionId());
        }

    }


    public void removeProjectById(String id) throws JDSException {
        INProject project = this.getProjectById(id);
        if (project != null) {
            this.removeProject(project.getProjectName());
        }

    }


    public CtVfsService getVfsClient() {
        CtVfsService vfsClient = CtVfsFactory.getCtVfsService();
        return vfsClient;
    }


}
