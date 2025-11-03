package net.ooder.esd.engine;

import com.alibaba.fastjson.JSONObject;
import net.ooder.common.ConfigCode;
import net.ooder.common.JDSException;
import net.ooder.common.ReturnType;
import net.ooder.common.database.dao.DAOException;
import net.ooder.common.database.dao.DBMap;
import net.ooder.common.database.metadata.MetadataFactory;
import net.ooder.common.database.metadata.ProviderConfig;
import net.ooder.common.database.metadata.TableInfo;
import net.ooder.common.logging.ChromeProxy;
import net.ooder.common.logging.LogSetpLog;
import net.ooder.common.md5.MD5;
import net.ooder.common.md5.MD5InputStream;
import net.ooder.common.util.CnToSpell;
import net.ooder.common.util.IOUtility;
import net.ooder.common.util.StringUtility;
import net.ooder.common.util.ZipUtil;
import net.ooder.config.BPDProjectConfig;
import net.ooder.config.JDSConfig;
import net.ooder.config.JDSUtil;
import net.ooder.config.UserBean;
import net.ooder.context.JDSActionContext;
import net.ooder.engine.ConnectInfo;
import net.ooder.engine.JDSSessionHandle;
import net.ooder.esb.config.formula.FormulaInst;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.custom.ESDClassManager;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.repository.RepositoryInst;
import net.ooder.esd.dsm.repository.database.FDTFactory;
import net.ooder.esd.dsm.repository.database.proxy.DSMTableProxy;
import net.ooder.esd.dsm.temp.DSMBean;
import net.ooder.esd.engine.config.*;
import net.ooder.esd.engine.config.dsm.DSMProjectConfig;
import net.ooder.esd.engine.enums.*;
import net.ooder.esd.engine.inner.INProject;
import net.ooder.esd.engine.inner.INProjectVersion;
import net.ooder.esd.engine.proxy.ProjectProxy;
import net.ooder.esd.engine.proxy.ProjectVersionProxy;
import net.ooder.esd.engine.task.SyncLoadClass;
import net.ooder.esd.manager.esdserver.ESDServerExe;
import net.ooder.esd.manager.esdserver.ESDServerUtil;
import net.ooder.esd.manager.plugins.api.APIFactory;
import net.ooder.esd.manager.plugins.api.enums.APIType;
import net.ooder.esd.manager.plugins.api.node.APIPaths;
import net.ooder.esd.manager.plugins.api.node.OODAPIConfig;
import net.ooder.esd.manager.plugins.font.FontFactory;
import net.ooder.esd.manager.plugins.font.node.FontConfig;
import net.ooder.esd.manager.plugins.img.ImgFactory;
import net.ooder.esd.manager.plugins.img.node.ImgConfig;
import net.ooder.esd.manager.plugins.style.StyleFactory;
import net.ooder.esd.manager.plugins.style.node.StyleConfig;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.ComponentList;
import net.ooder.esd.tool.component.HiddenInputComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.APICallerProperties;
import net.ooder.org.Person;
import net.ooder.org.PersonNotFoundException;
import net.ooder.server.*;
import net.ooder.server.ct.CtSubSystem;
import net.ooder.server.httpproxy.ServerProxyFactory;
import net.ooder.server.httpproxy.core.ProxyHost;
import net.ooder.vfs.*;
import net.ooder.vfs.ct.CtVfsFactory;
import net.ooder.vfs.ct.CtVfsService;
import net.ooder.web.APIConfig;
import net.ooder.web.APIConfigFactory;
import net.ooder.web.RemoteConnectionManager;
import net.ooder.web.RequestMethodBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.fluent.Async;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.util.CharsetUtils;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ESDClientImpl implements ESDClient {

    public static Log logger = LogFactory.getLog(ESDClientImpl.class);

    private JDSClientService clientService;

    private ProjectCacheManager cacheManager;

    private FontFactory fontManager;

    private APIFactory apiFactory;

    private ImgFactory imgFactory;

    private StyleFactory styleFactory;

    private FDTFactory fdtFactory;

    private DSMFactory dsmFactory;

    private BuildFactory buildFactory;

    private CustomViewFactory viewFactory;


    MySpace space;


    public ESDClientImpl(JDSClientService clientService) {
        this.clientService = clientService;
        init();
    }


    public ESDClientImpl(MySpace mySpace, JDSClientService clientService) {
        this.clientService = clientService;
        this.space = mySpace;
        init();

    }

    void init() {
        try {
            if (space == null) {
                String spaceformUrl = ESDFacrory.defaultSpace;
                if (JDSServer.getClusterClient().isLogin()) {
                    spaceformUrl = JDSServer.getClusterClient().getSystem(clientService.getSystemCode()).getVfsPath();
                }
                this.space = new MySpace(this.getVfsClient().mkDir(spaceformUrl), clientService.getSystemCode());
            }
            initFactory(space);
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }

    void initFactory(MySpace mySpace) {
        this.cacheManager = ProjectCacheManager.getInstance(mySpace);
        this.fontManager = FontFactory.getInstance(mySpace);
        this.apiFactory = APIFactory.getInstance(mySpace);
        this.styleFactory = StyleFactory.getInstance(mySpace);
        this.imgFactory = ImgFactory.getInstance(mySpace);

        this.fdtFactory = FDTFactory.getInstance(mySpace);
        this.dsmFactory = DSMFactory.getInstance(mySpace);

        this.buildFactory = BuildFactory.getInstance(mySpace);
        this.viewFactory = CustomViewFactory.getInstance(mySpace);
    }

    @Override
    public void connect(ConnectInfo connInfo) throws JDSException {
        clientService.connect(connInfo);
    }

    @Override
    public ReturnType disconnect() throws JDSException {
        return clientService.disconnect();
    }

    @Override
    public ConnectInfo getConnectInfo() {
        return clientService.getConnectInfo();
    }

    @Override
    public ProjectVersion createProcessVersion(String projectName) throws JDSException {
        INProject inProject = cacheManager.getProjectByName(projectName);
        INProjectVersion projectVersion = this.cacheManager.createProjectVersion(inProject);
        return this.getProjectVersionById(projectVersion.getVersionId());
    }

    @Override
    public ModuleComponent cloneModuleComponent(ModuleComponent component) throws JDSException {
        return cacheManager.cloneModuleComponent(component);
    }


    @Override
    public void quickReLoad() {
        try {
            this.getVfsClient().getFolderByPath(space.getPath()).getChildrenRecursivelyList();
            this.getVfsClient().getFolderByPath(space.getPath()).getFileListRecursively();
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int removeProcessVersion(String versionName) throws JDSException {
        ProjectVersion version = this.getProjectVersionByName(versionName);
        cacheManager.removeProjectVersion(version.getVersionId());
        return 1;
    }

    @Override
    public ProjectVersion getActivityProjectVersion(String projectId) throws JDSException {
        INProject project = cacheManager.getProjectById(projectId);
        INProjectVersion version = project.getActiviteVersion();
        return this.getProjectVersionById(version.getVersionId());
    }

    public MySpace getSpace() {
        return space;
    }

    public void setSpace(MySpace space) {
        this.space = space;
    }

    @Override
    public String getSystemCode() {
        return clientService.getSystemCode();
    }

    @Override
    public ConfigCode getConfigCode() {
        return clientService.getConfigCode();
    }

    @Override
    public JDSSessionHandle getSessionHandle() {
        return this.clientService.getSessionHandle();
    }

    @Override
    public void saveModule(EUModule module, boolean dynBuild) throws JDSException {
        this.cacheManager.saveModule(module, dynBuild);
        try {
            if (dynBuild) {
                this.publicLocal(module.getProjectVersion().getProjectName(), module, null, false);
            }

        } catch (JDSException e) {

        }
    }

    @Override
    public EUModule saveModuleAsJson(String versionName, String path, String json) throws JDSException {
        path = formartPath(path, versionName);
        EUModule module = this.createModule(versionName, path);
        ModuleComponent moduleComponent = JSONObject.parseObject(json, ModuleComponent.class);
        module.setComponent(moduleComponent);
        this.saveModule(module, true);
        return module;
    }

    @Override
    public Set<EUModule> loadModules(Set<String> classNames, String versionName) {
        return syncLoadModuleTasks(versionName, classNames);
    }

    @Override
    public EUModule getModule(String className, String versionName) throws JDSException {
        return this.getModule(className, versionName, false);
    }

    @Override
    public EUModule getCustomModule(String path, String versionName, Map<String, ?> valueMap) throws JDSException {
        return this.viewFactory.getView(path, versionName, valueMap);
    }

    public EUModule getCustomModule(MethodConfig methodConfig, String versionName, Map<String, ?> valueMap) throws JDSException {

        return this.viewFactory.getViewByMethod(methodConfig, versionName, valueMap);
    }


    @Override
    public EUModule rebuildCustomModule(String path, String versionName, Map<String, ?> valueMap) throws JDSException {
        EUModule module = null;
        RequestMethodBean methodBean = viewFactory.getMethodBeanByPath(path, versionName);
        if (methodBean != null) {
            MethodConfig methodAPIBean = viewFactory.getMethodAPIBean(methodBean.getUrl(), versionName);
            module = viewFactory.buildView(methodAPIBean, null, versionName, valueMap, true);
        }
        return module;
    }

    @Override
    public <T extends ModuleComponent> EUModule<T> buildDynCustomModule(Class<T> customClass, Map<String, ?> valueMap, boolean save) throws JDSException {
        EUModule<T> module = viewFactory.dynBuild(customClass, valueMap);
        return module;
    }

    @Override
    public Boolean delModule(EUModule moduleComponent) {
        boolean delModule = this.cacheManager.delModule(moduleComponent);
        try {
            this.removeLocalModule(moduleComponent.getProjectVersion().getProjectName(), moduleComponent.getClassName(), null);
        } catch (JDSException e) {

        }
        return delModule;
    }

    @Override
    public void delPackage(EUPackage euPackage) throws JDSException {
        List<String> paths = new ArrayList<>();
        paths.add(euPackage.getPath());
        this.delFile(paths, euPackage.getProjectVersion().getVersionName());
    }

    @Override
    public List<Object> importFile(List<String> spaths, String... paths) throws JDSException {
        List<Object> fileInfos = new ArrayList<Object>();
        String tpath = this.formartPath(paths);
        Folder tfolder = getVfsClient().mkDir(tpath);

        for (String spath : spaths) {
            if (!spath.endsWith("/")) {
                FileInfo tempFile = getVfsClient().getFileByPath(spath);
                FileInfo newFile = getVfsClient().copyFile(tempFile, tfolder);
                if (tempFile.getName().endsWith(".cls")) {
                    fileInfos.add(this.getModule(newFile.getPath(), paths[1], true));
                } else {
                    fileInfos.add(newFile);
                }
            }
        }
        return fileInfos;

    }

    @Override
    public List<ImgConfig> getImgByProject(String projectName) throws JDSException {
        return imgFactory.getImgByProject(projectName);
    }

    @Override
    public List<StyleConfig> getStyleByProject(String projectName) throws JDSException {
        return styleFactory.getStyleByProject(projectName);
    }

    @Override
    public StringBuffer genJSON(EUModule module, String tempName, boolean prettyFormat) {
        return cacheManager.genJSON(module, tempName, prettyFormat);
    }

    @Override
    public StringBuffer genJSON(ModuleComponent moduleComponent, String tempName, boolean prettyFormat) {
        return cacheManager.genJSON(moduleComponent, tempName, prettyFormat);
    }

    @Override
    public List<EUModule> getComponentByProject(String projectName) throws JDSException {
        return null;
    }

    @Override
    public Set<EUModule> getExtModule(String versionName) throws JDSException {
        ProjectVersion version = this.getProjectVersionByName(versionName);
        return version.getExtModule();
    }

    @Override
    public Set<Component> getExtCom(String versionName) throws JDSException {
        ProjectVersion version = this.getProjectVersionByName(versionName);
        List<String> extComIds = version.getProject().getConfig().getExtcoms();

        List<Project> projects = getAllProject(ProjectDefAccess.Component);
        for (Project project : projects) {
            Set<EUModule> modules = null;
            ProjectVersion projectVersion = project.getActiveProjectVersion();
            try {
                modules = projectVersion.getAllModule();
            } catch (JDSException e) {
                e.printStackTrace();
            }


        }
        return new HashSet<Component>();

    }

    @Override
    public List<TableInfo> getTablesByProject(String projectName, String simpleName) throws JDSException {
        List<TableInfo> tables = new ArrayList<TableInfo>();
        ProjectVersion projectVersion = getProjectVersionByName(projectName);
        List<DataBaseConfig> configs = projectVersion.getProject().getConfig().getDbConfigs();
        for (DataBaseConfig config : configs) {
            try {
                if (config.getConfigKey() != null && getDbFactory(config.getConfigKey()) != null) {
                    MetadataFactory factory = getDbFactory(config.getConfigKey());
                    List<TableInfo> alltables = factory.getTableInfos(simpleName);
                    List<String> tableNames = config.getTableName();
                    Set<String> tableNameSet = new HashSet<>();
                    for (String tableName : tableNames) {
                        tableNameSet.add(tableName.toUpperCase());
                    }
                    for (TableInfo table : alltables) {
                        if (tableNames == null || tableNameSet.contains(table.getName().toUpperCase())) {
                            tables.add(factory.getTableInfo(table.getName()));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return tables;
    }

    @Override
    public TableInfo getTableInfoByFullName(String tableFullName) throws JDSException {
        TableInfo tableInfo = null;
        try {
            tableInfo = this.fdtFactory.getTableInfoByFullName(tableFullName);
        } catch (DAOException e) {
            new JDSException(e);
        }
        return tableInfo;
    }
//
//    @Override
//    public CApplication getCurrApplication() throws JDSException {
//        return bpmFactory.getApplication();
//    }
//
//    @Override
//    public CApplication reBuildAppConfig() throws JDSException {
//        bpmFactory.initFactory();
//        return bpmFactory.getApplication();
//    }
//
//    @Override
//    public BPDProjectConfig getProjectBPDConfig(String projectId) throws JDSException {
//        return bpmFactory.getProjectConfig(projectId);
//    }

    @Override
    public EUPackage getPackageByPath(String projectVersionName, String packageName) throws JDSException {
        EUPackage euPackage = this.cacheManager.loadPackage(projectVersionName, packageName);
        return euPackage;
    }


    @Override
    public List<EUPackage> getTopPackages(String projectVersionName) throws JDSException {
        List<EUPackage> euPackages = this.cacheManager.loadTopPackage(projectVersionName);
        return euPackages;
    }

    @Override
    public void exportProject(String projectName, ChromeProxy chrome, boolean deploy, boolean download) throws JDSException {
        if (chrome == null) {
            chrome = this.getCurrChromeDriver();
        }
        long time = System.currentTimeMillis();
        chrome.printLog("开始导出离线包文件。。。", true);
        if (projectName == null) {
            chrome.printLog("未检测到工程名称，请先打开工程再导出！", true);
        }
        try {
            ProjectVersion version = getProjectVersionByName(projectName.toString());
            String localPath = null;
            if (version == null) {
                chrome.printLog("未检测到工程名称，请先打开工程再导出！", true);
                return;
            }
            chrome.printLog("开始导出文件。。。", true);
            chrome.execScript("ood.busy('export','正在导出文件 请勿操作！');");
            localPath = JDSConfig.Config.rootServerHome().getAbsolutePath() + File.separator + EXPORT_PATH + "/" + version.getProject().getProjectName() + "/";
            File configfile = new File(localPath + "/" + ServerProjectConfig.fileName);

            ServerProjectConfig config = new ServerProjectConfig();
            config.setHost(version.getProject().getConfig().getPublicServerUrl());
            config.setProjectName(version.getProject().getProjectName());
            config.setVersionName(version.getVersionName());
            config.setUserId(this.getConnectInfo().getUserID());
            config.setUserName(this.getConnectInfo().getLoginName());
            InputStream configinput = new ByteArrayInputStream(JSONObject.toJSONString(config).getBytes(VFSConstants.Default_Encoding));
            IOUtility.copy(configinput, configfile);

            List<FileInfo> fileInfos = version.getRootFolder().getFileListRecursively();

            List<FileInfo> resources = new ArrayList<>();

            for (FileInfo fileInfo : fileInfos) {
                if (!fileInfo.getName().endsWith(".cls")) {
                    resources.add(fileInfo);
                }
            }


            for (FileInfo fileInfo : resources) {
                String realPath = StringUtility.replace(fileInfo.getPath(), version.getRootFolder().getPath(), "/");
                File file = new File(localPath + realPath);
                InputStream input = fileInfo.getCurrentVersion().getInputStream();
                if (input != null) {
                    IOUtility.copy(input, file);
                }
            }

            Set<EUModule> modules = version.getAllModule();
            List<EUModule> localModules = new ArrayList<>();

            PackageType[] packageTypes = version.getProject().getProjectType().getPackageTypes();

            for (EUModule module : modules) {
                String path = StringUtility.replace(module.getPath(), module.getProjectVersion().getPath(), "/");
                PackagePathType packagePathType = PackagePathType.startPath(path);
                if (packagePathType == null || Arrays.asList(packageTypes).contains(packagePathType.getApiType())) {
                    localModules.add(module);
                }
            }


            chrome.printLog("共发现" + (resources.size()) + "个文件导出耗时：" + (System.currentTimeMillis() - time) + "ms  开始导出编译文件...", true);
            JDSActionContext.getActionContext().getContext().put("build", "false");
            time = System.currentTimeMillis();
            int k = 0;
            for (EUModule module : localModules) {
                k = k + 1;
                chrome.printLog("开始编译：" + module.getClassName() + "剩余：" + (modules.size() - k) + "个", true);
                try {
                    File file = new File(localPath + "cls/" + MD5.getHashString(module.getClassName()));
                    List<HiddenInputComponent> components = module.getComponent().findComponents(ComponentType.HIDDENINPUT, null);
                    for (HiddenInputComponent component : components) {
                        component.getProperties().setValue("@{'@{" + component.getProperties().getName() + "}'}");
                    }
                    StringBuffer json = genJSON(module, null, true);
                    InputStream input = null;
                    input = new ByteArrayInputStream(json.toString().getBytes(VFSConstants.Default_Encoding));
                    IOUtility.copy(input, file);
                } catch (Throwable e) {
                    chrome.printLog("编译失败：" + module.getClassName(), true);
                    e.printStackTrace();
                }
            }


            JDSActionContext.getActionContext().getContext().remove("build");
            chrome.printLog("共编译" + localModules.size() + "个文件耗时：" + (System.currentTimeMillis() - time) + "ms 开始导出css文件...", true);

            List<FileInfo> cssFiles = this.getCssfile(projectName);

            for (FileInfo cssFile : cssFiles) {
                String fileName = cssFile.getCurrentVersion().getFileObject().getHash() + ".css";
                File file = new File(localPath + "/css/" + fileName);
                InputStream input = cssFile.getCurrentVersion().getInputStream();
                IOUtility.copy(input, file);

            }

            List<FileInfo> imgFiles = this.getImgfile(projectName);
            for (FileInfo imgFile : imgFiles) {
                File file = new File(localPath + "/img/" + imgFile.getPath());
                InputStream input = imgFile.getCurrentVersion().getInputStream();
                IOUtility.copy(input, file);

            }


            File exportFile = new File(localPath);
            File zipFile = new File(exportFile.getParentFile().getAbsolutePath() + "/" + version.getProject().getProjectName() + ".zip");

            if (deploy) {
                Object serverId = JDSActionContext.getActionContext().getParams("serverId");
                if (serverId != null) {
                    LocalServer server = getUserConfig().getServerById(serverId.toString());
                    if (server != null) {
                        File deployHomme = new File(server.getPath(), JAR_PATH);
                        if (deployHomme.exists()) {
                            ZipUtil.zip(exportFile.getAbsolutePath(), deployHomme.getAbsolutePath() + "/" + version.getProject().getProjectName() + ".zip");
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Process process = null;
                                    try {
                                        process = Runtime.getRuntime().exec(server.getStartScript());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    InputStream stream = process.getInputStream();
                                }
                            });


                        } else {
                            chrome.printLog("服务器配置出错，请检查配置！", true);
                        }

                    }

                }

            } else {
                ZipUtil.zip(exportFile.getAbsolutePath(), zipFile.getAbsolutePath());
                Desktop.getDesktop().open(exportFile.getParentFile());
            }

            chrome.printLog("导出完毕，请将导出copy 运行环境！ 耗时：" + (System.currentTimeMillis() - time) + "ms，", true);
            chrome.execScript("ood.free('export')");
            File rootFile = new File(JDSUtil.getJdsRealPath());
            String zipFilePath = StringUtility.replace(zipFile.getAbsolutePath(), rootFile.getAbsolutePath(), "");
            zipFilePath = StringUtility.replace(zipFilePath, "\\", "/");
            if (download) {
                chrome.sendDownLoadCommand(zipFilePath);
            }


        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            chrome.execScript("ood.free('export')");
        }


    }

    @Override
    public void exportLocalServer(String projectName, String serverId, ChromeProxy chrome) throws JDSException {

        try {
            if (chrome == null) {
                chrome = this.getCurrChromeDriver();
            }
            ProjectVersion version = ESDFacrory.getAdminESDClient().getProjectVersionByName(projectName);
            String localPath = JDSConfig.Config.rootServerHome().getAbsolutePath() + File.separator + EXPORT_PATH + "/" + projectName + "/";
            File exportFile = new File(localPath);
            File zipFile = new File(exportFile.getParentFile().getAbsolutePath() + "/" + version.getProject().getProjectName() + ".zip");
            DevUserConfig userConfig = ESDFacrory.getAdminESDClient().getUserConfig();
            LocalServer localServer = userConfig.getServerById(serverId);

            if (localServer == null) {
                localServer = getDefaultLocalServer(version.getProject().getProjectName());
            }

            if (localServer == null) {
                chrome.printLog("请配置本地服务地址！", true);
                return;
            }


            String proxyUrl = localServer.getProxyUrl();

            if (proxyUrl == null || proxyUrl.equals("")) {
                localServer.setProjectName(projectName);
                URL url = new URL(version.getProject().getPublicServerUrl());
                ProxyHost host = ServerProxyFactory.getInstance().getProxy(url);
                proxyUrl = host.getProxyUrl();
            }
            localServer.setProxyUrl(proxyUrl);

            File localFile = new File(localServer.getPath() + "/deploy/" + version.getProject().getProjectName() + ".zip");
            if (localFile.exists()) {
                localFile.delete();
            } else {
                localFile.createNewFile();
            }

            if (!zipFile.exists()) {
                this.exportProject(projectName, chrome, false, false);
            }

            if (zipFile.exists()) {
                IOUtility.copy(zipFile, localFile);
            }
            chrome.printLog("启动服务器!", true);
            ESDServerExe esdServerExe = new ESDServerExe(version.getProject(), localServer);
            ESDServerUtil.startESDServer(esdServerExe, projectName);


        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public void exportRemoteServer(String projectName, String serverId, ChromeProxy chrome) throws JDSException {
        try {
            ProjectVersion version = ESDFacrory.getAdminESDClient().getProjectVersionByName(projectName);
            String localPath = JDSConfig.Config.rootServerHome().getAbsolutePath() + File.separator + EXPORT_PATH + "/" + projectName + "/";
            File exportFile = new File(localPath);
            File zipFile = new File(exportFile.getParentFile().getAbsolutePath() + "/" + version.getProject().getProjectName() + ".zip");
            if (!zipFile.exists()) {
                this.exportProject(projectName, chrome, false, false);
            }

            DevUserConfig userConfig = ESDFacrory.getAdminESDClient().getUserConfig();
            RemoteServer remoteServer = userConfig.getRemoteServerById(serverId);

            if (remoteServer == null) {
                remoteServer = getDefaultRemoteServer(version.getProject().getProjectName());
            }
            if (remoteServer != null) {
                String url = remoteServer.getUrl();
                if (!url.startsWith("http")) {
                    url = "http://" + url;
                }

                Async async = Async.newInstance().use(RemoteConnectionManager.getConntctionService(remoteServer.getUrl()));
                Request request = Request.Post(url + "/deployProject");
                HttpEntity reqEntity = null;

                FileBody fileBody = new FileBody(zipFile);
                reqEntity = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE).addTextBody("path", zipFile.getName())
                        .addPart("file", fileBody).setCharset(CharsetUtils.get("utf-8")).build();
                request.body(reqEntity);
                Future<Content> future = async.execute(request);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public void publicRemote(String projectName, String className, String remoteServerId, Boolean open) throws JDSException {
        try {
            ProjectVersion version = ESDFacrory.getAdminESDClient().getProjectVersionByName(projectName);
            EUModule module = version.getModule(className);
            StringBuffer stringBuffer = ESDFacrory.getAdminESDClient().genJSON(module, null, true);
            RemoteServer remoteServer = this.getUserConfig().getRemoteServerById(remoteServerId);
            if (remoteServer == null) {
                remoteServer = this.getDefaultRemoteServer(version.getProject().getProjectName());
            }
            if (remoteServer == null) {
                throw new JDSException("请配置服务器！");
            }
            String url = remoteServer.getUrl();
            if (!url.startsWith("http")) {
                url = "http://" + url;
            }
            Form form = Form.form();
            form.add("projectVersionName", version.getProject().getProjectName())
                    .add("path", "cls/" + MD5.getHashString(module.getClassName()))
                    .add("content", stringBuffer.toString());

            Async async = Async.newInstance().use(RemoteConnectionManager.getConntctionService(remoteServer.getUrl()));
            Request request = Request.Post(url + "/server/debugCls");

            request.bodyForm(form.build(), Charset.forName("utf-8"));
            Future<Content> future = async.execute(request);

            URI uri = new URI("http://" + remoteServer.getUrl() + "/" + version.getProject().getProjectName() + "/" + className + ".cls");
            if (open) {
                Desktop.getDesktop().browse(uri);
            }

        } catch (Exception e) {
            throw new JDSException(e);

        }
    }


    public void clearProjectResource(String projectName) throws JDSException {
        try {
            ProjectVersion version = ESDFacrory.getAdminESDClient().getProjectVersionByName(projectName);

            LocalServer server = this.getDefaultLocalServer(projectName);
            File localFile = new File(JDSUtil.getJdsRealPath() + WEBAPP_PATH + "/" + version.getProject().getProjectName() + "/");
            localFile.delete();
            if (server != null) {
                File clsFile = new File(server.getPath() + "/" + WEBAPP_PATH + "/" + version.getProject().getProjectName() + "/");
                clsFile.delete();
            }
            File devFile = new File(JDSConfig.Config.rootServerHome().getAbsolutePath() + "/" + EXPORT_PATH + "/" + version.getProject().getProjectName() + "/");
            devFile.delete();
        } catch (Exception e) {
            throw new JDSException(e);
        }
    }


    @Override
    public void publicLocalResource(String projectName, String filePath, String json, String localServerId) throws JDSException {
        try {
            ProjectVersion version = ESDFacrory.getAdminESDClient().getProjectVersionByName(projectName);
            LocalServer server = this.getUserConfig().getServerById(localServerId);
            if (server == null) {
                server = this.getDefaultLocalServer(projectName);
            }
            if (server == null) {
                throw new JDSException("请配置服务器！");
            }

            if (filePath.startsWith("/")) {
                filePath.substring(1);
            }
            File localFile = new File(JDSUtil.getJdsRealPath() + WEBAPP_PATH + "/" + version.getProject().getProjectName() + "/" + filePath);
            File clsFile = new File(server.getPath() + "/" + WEBAPP_PATH + "/" + version.getProject().getProjectName() + "/" + filePath);

            File devFile = new File(JDSConfig.Config.rootServerHome().getAbsolutePath() + "/" + EXPORT_PATH + "/" + version.getProject().getProjectName() + "/" + filePath);

            InputStream input = new ByteArrayInputStream(json.getBytes(VFSConstants.Default_Encoding));
            IOUtility.copy(input, clsFile);
            IOUtility.copyFile(clsFile, localFile);
            IOUtility.copyFile(clsFile, devFile);

        } catch (Exception e) {
            throw new JDSException(e);
        }
    }


    private void removeLocalModule(String projectName, String className, String localServerId) throws JDSException {
        try {
            ProjectVersion version = ESDFacrory.getAdminESDClient().getProjectVersionByName(projectName);
            LocalServer server = this.getUserConfig().getServerById(localServerId);
            APIConfigFactory.getInstance().clear(className);
            CustomViewFactory.getInstance().clear();
            ESDClassManager.getInstance().clear(className);

            File localFile = new File(JDSUtil.getJdsRealPath() + WEBAPP_PATH + "/" + version.getProject().getProjectName() + "/cls/" + MD5.getHashString(className));
            File devFile = new File(JDSConfig.Config.rootServerHome().getAbsolutePath() + "/" + EXPORT_PATH + "/" + version.getProject().getProjectName() + "/cls/" + MD5.getHashString(className));

            if (localFile.exists()) {
                localFile.delete();
            }


            if (devFile.exists()) {
                devFile.delete();
            }


            if (server == null) {
                server = this.getDefaultLocalServer(projectName);
            }
            if (server == null) {
                throw new JDSException("请配置服务器！");
            }

            File clsFile = new File(server.getPath() + "/" + WEBAPP_PATH + "/" + version.getProject().getProjectName() + "/cls/" + MD5.getHashString(className));
            if (clsFile.exists()) {
                clsFile.delete();
            }


        } catch (Exception e) {
            throw new JDSException(e);
        }
    }


    @Override
    public void publicLocal(String projectName, EUModule module, String localServerId, Boolean open) throws JDSException {
        try {
            ProjectVersion version = ESDFacrory.getAdminESDClient().getProjectVersionByName(projectName);

            LocalServer server = this.getUserConfig().getServerById(localServerId);
            if (server == null) {
                server = this.getDefaultLocalServer(projectName);
            }

            StringBuffer json = ESDFacrory.getAdminESDClient().genJSON(module, null, true);
            if (module.getComponent().isCache()) {
                File localFile = new File(JDSUtil.getJdsRealPath() + WEBAPP_PATH + "/" + version.getProject().getProjectName() + "/cls/" + MD5.getHashString(module.getClassName()));
                File devFile = new File(JDSUtil.getJdsRealPath() + "/" + EXPORT_PATH + "/" + version.getProject().getProjectName() + "/cls/" + MD5.getHashString(module.getClassName()));
                File exportFile = new File(JDSConfig.Config.currServerHome().getAbsolutePath() + "/" + EXPORT_PATH + "/" + version.getProject().getProjectName() + "/cls/" + MD5.getHashString(module.getClassName()));


                InputStream input = new ByteArrayInputStream(json.toString().getBytes(VFSConstants.Default_Encoding));
                IOUtility.copy(input, localFile);
                IOUtility.copyFile(localFile, devFile);
                IOUtility.copyFile(localFile, exportFile);

                if (server != null && !server.getPath().equals("")) {
                    File clsFile = new File(server.getPath() + "/" + WEBAPP_PATH + "/" + version.getProject().getProjectName() + "/cls/" + MD5.getHashString(module.getClassName()));
                    IOUtility.copy(localFile, clsFile);
                    if (open) {
                        URI uri = new URI("http://localhost:" + server.getProxyPort() + "/" + version.getProject().getProjectName() + "/" + module.getClassName() + ".cls");
                        Desktop.getDesktop().browse(uri);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new JDSException(e);
        }
    }

    @Override
    public EUPackage createPackage(String projectVersionName, String packageName) throws JDSException {
        EUPackage euPackage = this.cacheManager.createPackage(projectVersionName, packageName);
        return euPackage;
    }


    public Set<EUModule> syncLoadModuleTasks(String versionName, Set<String> esdClassNameSet) {
        Set<EUModule> srcBeans = new HashSet<>();
        try {
            String defaultClassName = versionName;
            List<SyncLoadClass<EUModule>> syncLoadClassTasks = new ArrayList<>();
            for (String esdClassName : esdClassNameSet) {
                if (esdClassName != null && !esdClassName.equals("")) {
                    defaultClassName = esdClassName;
                    SyncLoadClass syncLoadClass = new SyncLoadClass(this, esdClassName, versionName);
                    syncLoadClassTasks.add(syncLoadClass);
                }
            }

            List<Future<EUModule>> projectFutures = RemoteConnectionManager.getConntctionService(defaultClassName).invokeAll(syncLoadClassTasks);
            for (Future<EUModule> resultFuture : projectFutures) {
                try {
                    EUModule result = resultFuture.get();
                    srcBeans.add(result);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            RemoteConnectionManager.getConntctionService(defaultClassName).shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return srcBeans;
    }


    @Override
    public EUModule getModule(String className, String versionName, boolean reload) throws JDSException {

        if (className == null || className.equals("")) {
            return null;
        }
        String pattern = ".*\\.(gif|jpg|jpeg|bmp|png|js|html|htm|json|css|sql|wav|woff2|woff|ico|dyn|swf|xml|ttf|txt|ftl|jar|cfg)$";
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(className);
        if (matcher.find()) {
            return null;
        }

        if (versionName == null) {
            versionName = DSMFactory.getInstance().getDefaultProjectName();
        }
        String path = classToPath(className, versionName);
        EUModule module = null;
        try {

            PackagePathType packagePathType = PackagePathType.startPath(StringUtility.replace(className, ".", "/"));
            if (packagePathType != null && packagePathType.getApiType() != null && packagePathType.getApiType().getDefaultProjectName() != null) {
                path = classToPath(className, packagePathType.getApiType().getDefaultProjectName());
                module = this.cacheManager.loadEUClass(path, packagePathType.getApiType().getDefaultProjectName(), reload);// this.getDSMModule(path, new HashMap<>());
            } else {
                module = this.cacheManager.loadEUClass(path, versionName, reload);
            }


        } catch (JDSException e) {
            e.printStackTrace();
        }


        return module;
    }

    @Override
    public Set<EUModule> build(String versionName) throws JDSException {
        return cacheManager.build(versionName);
    }

    @Override
    public List<EUPackage> getAllPackage(String versionName) throws JDSException {
        return getAllPackage(versionName, null);
    }


    @Override
    public List<EUPackage> getAllPackage(String versionName, PackageType packageType) throws JDSException {
        ProjectVersion projectVersion = this.getProjectVersionByName(versionName);
        List<EUPackage> packages = new ArrayList<>();
        Set<EUModule> allModules = projectVersion.getAllModule();
        List<Folder> folders = new ArrayList<Folder>();
        for (EUModule module : allModules) {
            String path = StringUtility.replace(module.getPath(), module.getProjectVersion().getPath(), "/");
            PackagePathType packagePathType = PackagePathType.startPath(path);
            if (packageType == null || packagePathType == null || packageType.equals(packagePathType.getApiType())) {
                Folder folder = null;
                try {
                    folder = getFileByPath(module.getPath(), versionName).getFolder();
                    if (!folders.contains(folder)) {
                        folders.add(folder);
                        String packageName = StringUtility.replace(folder.getPath(), projectVersion.getPath(), "");
                        packageName = StringUtility.replace(packageName, "/", ".");
                        if (packageName.endsWith(".")) {
                            packageName = packageName.substring(0, packageName.length() - 1);
                        }
                        packages.add(this.getPackageByPath(versionName, packageName));
                    }
                } catch (JDSException e) {
                    e.printStackTrace();
                }

            }
        }
        Collections.sort(packages, new Comparator<EUPackage>() {
            public int compare(EUPackage o1, EUPackage o2) {
                return o1.getPath().compareTo(o1.getPath());
            }
        });

        return packages;
    }


    @Override
    public Set<EUModule> buildCustomModule(String versionName, String packageName, String esdPackageName, Map<String, ?> valueMap, ChromeProxy chrome) throws JDSException {
        Set<EUModule> modules = this.viewFactory.buildProjectModule(versionName, packageName, esdPackageName, valueMap, chrome);
        return modules;
    }

    @Override
    public Set<EUModule> buildPackage(String versionName, String packageName, Map<String, ?> valueMap, ChromeProxy chrome) throws JDSException {
        Set<EUModule> modules = this.viewFactory.buildPackage(versionName, packageName, valueMap, chrome);
        return modules;
    }


    @Override
    public EUModule createModule(String className, String versionName) throws JDSException {
        INProjectVersion inProjectVersion = this.cacheManager.getProjectVersionByName(versionName);
        EUModule module = this.cacheManager.createModule(inProjectVersion, formatClassName(className, versionName));
        return module;
    }

    @Override
    public EUModule createCustomModule(String className, String versionName, Map<String, ?> valueMap) throws JDSException {
        INProjectVersion inProjectVersion = this.cacheManager.getProjectVersionByName(versionName);
        EUModule module = this.cacheManager.createModule(inProjectVersion, formatClassName(className, versionName));
        return module;
    }

    @Override
    public EUModule createTableModule(String versionName, String tableName, DBMap<String, ?> valueMap, String viewInstId) throws JDSException {
        EUModule module = this.fdtFactory.createTableModule(tableName, versionName, valueMap, viewInstId);
        return module;

    }

    @Override
    public EUModule getTableModule(String versionName, String tableName, String viewInstId) throws JDSException {

        DSMTableProxy proxy = DSMFactory.getInstance().getRepositoryManager().getTableProxyByName(tableName, viewInstId);
        String className = "from." + proxy.getFieldName() + "Module";
        EUModule tableModule = getModule(className, versionName);
        return tableModule;

    }


    @Override
    public FileInfo uploadFile(MD5InputStream inputStream, String... paths) throws JDSException {
        String path = formartPath(paths);
        FileVersion version = this.getVfsClient().upload(path, inputStream, this.getConnectInfo().getUserID());
        FileInfo fileInfo = this.getVfsClient().getFileById(version.getFileId());
        return fileInfo;
    }

    @Override
    public String readFileAsString(String... paths) throws JDSException {
        String path = formartPath(paths);
        StringBuffer buffer = getVfsClient().readFileAsString(path, VFSConstants.Default_Encoding);
        return buffer.toString();
    }


    @Override
    public FileInfo getFileByPath(String... paths) throws JDSException {
        String path = formartPath(paths);
        return this.getVfsClient().getFileByPath(path);
    }

    @Override
    public Folder getFolderByPath(String... paths) throws JDSException {
        String path = formartPath(paths);
        return this.getVfsClient().getFolderByPath(path);
    }

    @Override
    public Folder createFolder(String... paths) throws JDSException {
        String path = formartPath(paths);

        Folder folder = this.getVfsClient().getFolderByPath(path);
        if (folder != null) {
            throw new JDSException("文件夹已存在！");
        }

        return this.getVfsClient().mkDir(path);
    }

    @Override
    public Object copy(String projectName, String spath, String tpath) throws JDSException {
        spath = this.formartPath(spath, projectName);
        tpath = this.formartPath(tpath, projectName);
        Folder folder = getVfsClient().getFolderByPath(spath);
        Folder tfolder = getVfsClient().mkDir(tpath);

        Object obj = null;
        if (folder != null) {
            getVfsClient().copyFolder(spath, tfolder.getPath());
            obj = this.getVfsClient().getFolderByPath(tfolder.getPath() + folder.getName());
        } else {
            FileInfo tempFile = getVfsClient().getFileByPath(spath);
            if (tempFile.getFolder().getPath().equals(tpath)) {
                throw new VFSException("不能拷贝到自己目录内！");
            }
            if (tempFile.getName().endsWith(".cls")) {
                EUModule module = this.getModule(this.formatClassName(spath, projectName), projectName, true);
                EUModule newModule = copyClass(projectName, this.formatClassName(spath, projectName), this.formatClassName(tpath + module.getName() + ".cls", projectName));
                this.cacheManager.saveModule(newModule, false);
                obj = newModule;
            } else {
                FileInfo newFile = getVfsClient().copyFile(tempFile, tfolder);
                obj = newFile;
            }
        }
        return obj;
    }


    @Override
    public Object reName(String projectName, String path, String newName) throws JDSException {
        Object obj = null;
        path = this.formartPath(path, projectName);
        Folder folder = getVfsClient().getFolderByPath(path);
        if (folder != null) {
            if (!folder.getDescrition().equals(newName)) {
                folder.setDescrition(newName);
                folder.setName(CnToSpell.getFullSpell(newName));
                getVfsClient().updateFolderInfo(folder, CnToSpell.getFullSpell(newName), newName);
                Folder newFolder = this.getFolderByPath(folder.getParent().getPath() + newName, projectName);
                obj = newFolder;
            }
        } else {
            FileInfo fileInfo = getVfsClient().getFileByPath(path);
            FileInfo newFile = this.getVfsClient().getFileByPath(fileInfo.getFolder().getPath() + newName);
            if (newFile == null) {
                if (fileInfo.getDescrition() == null || !fileInfo.getDescrition().equals(newName)) {
                    if (newName.indexOf(".") == -1) {
                        newName = newName + ".cls";
                    }
                    if (newName.endsWith(".cls")) {
                        EUModule module = this.getModule(this.formatClassName(path, projectName), projectName, false);
                        EUModule newModule = copyClass(projectName, this.formatClassName(path, projectName), this.formatClassName(fileInfo.getFolder().getPath() + newName, projectName));
                        this.saveModule(newModule, false);
                        this.delModule(module);
                        obj = newModule;
                    } else {
                        newFile = getVfsClient().updateFileInfo(fileInfo, CnToSpell.getFullSpell(newName), newName);
                        obj = newFile;
                    }
                }
            } else {
                throw new JDSException("文件已存在");

            }
        }
        return obj;
    }


    private boolean isOwner(String projectName) throws JDSException {

        ProjectConfig config = cacheManager.getProjectByName(projectName).getConfig();
        Set<String> owns = config.getDevPersons(ProjectRoleType.own);
        String adminId = JDSServer.getClusterClient().getSystem(this.getSystemCode()).getAdminId();

        return owns.contains(adminId);
    }


    private boolean isAdmin() throws JDSException {
        boolean isAdmin = false;

        UserBean userBean = UserBean.getInstance();
        if (this.getSystemCode() == null || this.getSystemCode().equals(userBean.getSystemCode())) {
            return true;
        }
        SubSystem subSystem = JDSServer.getClusterClient().getSystem(this.getSystemCode());
        if (subSystem != null) {
            String adminId = JDSServer.getClusterClient().getSystem(this.getSystemCode()).getAdminId();
            if (this.getConnectInfo().getUserID().equals(adminId) || !JDSServer.getClusterClient().isLogin()) {
                isAdmin = true;
            }
        } else {
            isAdmin = true;
        }
        return isAdmin;
    }

    @Override
    public void delFile(List<String> paths, String projectName) throws JDSException {
        List<String> realPaths = new ArrayList<String>();

        boolean isAdmin = isAdmin() || isOwner(projectName);
        for (String path : paths) {
            if (path.startsWith("App") || path.startsWith("Module")) {
                if (!isAdmin) {
                    throw new JDSException("系统目录 禁止删除！ 请联系管理员！" + path);
                }

            }
            path = formartPath(path, projectName);
            ProjectVersion version = this.getProjectVersionByName(projectName);
            if (!path.startsWith(version.getPath())) {
                throw new JDSException("删除文件存在越权路径，禁止删除本工程以外数据！" + path);
            }
            realPaths.add(path);
        }

        for (String path : realPaths) {
            Folder folder = null;
            if (path.endsWith("/")) {
                folder = getVfsClient().getFolderByPath(path);
            } else {
                String name = path.substring(path.lastIndexOf("/") + 1, path.length());
                if (name.indexOf(".") > -1) {
                    FileInfo fileInfo = getVfsClient().getFileByPath(path);
                    if (fileInfo != null) {
                        if (fileInfo.getName().endsWith(".cls")) {
                            this.delModule(this.getModule(fileInfo.getPath(), projectName));
                        }
                        getVfsClient().deleteFile(fileInfo.getID());
                    }
                } else {
                    folder = getVfsClient().getFolderByPath(path);
                }
            }

            if (folder != null) {
                EUPackage euPackage = this.getPackageByPath(projectName, folder.getPath());
                if (euPackage != null) {
                    List<EUModule> modules = euPackage.listAllModule();
                    for (EUModule module : modules) {
                        this.delModule(module);
                    }
                }
                getVfsClient().deleteFolder(folder.getID());
            }
        }
    }

    @Override
    public void activateProjectVersion(String versionId) throws JDSException {
        INProjectVersion version = this.cacheManager.getProjectVersionById(versionId);
        cacheManager.updateFolderState(version, ProjectVersionStatus.RELEASED);
    }

    @Override
    public void freezeProjectVersion(String versionId) throws JDSException {
        INProjectVersion version = this.cacheManager.getProjectVersionById(versionId);
        cacheManager.updateFolderState(version, ProjectVersionStatus.UNDERREVISION);

    }

    @Override
    public void delProject(String projectId) throws JDSException {

        Project project = this.getProjectById(projectId);
        if (isAdmin() || isOwner(project.getProjectName())) {
            cacheManager.removeProjectById(projectId);
        } else {
            throw new JDSException("无权限 请联系管理员删除！");

        }

    }

    @Override
    public Project getProjectByName(String projectName) throws JDSException {
        Project project = null;
        INProject inproject = null;
        if (projectName.indexOf(ProjectCacheManager.VERSIONTOKEN) == -1) {
            inproject = cacheManager.getProjectByName(projectName);
        } else {
            INProjectVersion version = cacheManager.getProjectVersionByName(projectName);
            if (version != null) {
                inproject = version.getProject();
            }
        }
        if (inproject != null) {
            project = this.getProjectById(inproject.getId());
        }
        return project;
    }

    @Override
    public ProjectVersion getProjectVersionById(String versionId) throws JDSException {
        INProjectVersion inProjectVersion = this.cacheManager.getProjectVersionById(versionId);
        ProjectVersion version = new ProjectVersionProxy(inProjectVersion, this);
        return version;
    }


    @Override
    public ProjectVersion getProjectVersionByName(String versionName) throws JDSException {
        ProjectVersion projectVersion = null;
        INProjectVersion inProjectVersion = cacheManager.getProjectVersionByName(versionName);
        if (inProjectVersion != null) {
            projectVersion = this.getProjectVersionById(inProjectVersion.getVersionId());
        }
        return projectVersion;
    }

    @Override
    public List<ProjectVersion> getProjectVersionList(String projectName) throws JDSException {
        List<INProjectVersion> versions = cacheManager.getVersions(projectName);
        List<ProjectVersion> versionList = new ArrayList<ProjectVersion>();
        for (INProjectVersion version : versions) {
            if (version != null) {
                versionList.add(this.getProjectVersionById(version.getVersionId()));
            }

        }
        return versionList;
    }

    @Override
    public Project getProjectById(String projectId) throws JDSException {
        ProjectProxy projectProxy = null;
        INProject inProject = this.cacheManager.getProjectById(projectId);
        if (inProject != null) {
            projectProxy = new ProjectProxy(inProject, this);
        }
        return projectProxy;
    }


    @Override
    public void updateSpaceConfig(MySpaceConfig config) throws JDSException {
//        if (!isAdmin()) {
//            throw new JDSException("权限错误，非管理员禁止操作");
//        }
        this.cacheManager.updateSpaceConfig(config);
    }


    @Override
    public void updateUserConfig(DevUserConfig config) throws JDSException {
        this.cacheManager.updateUserConfig(this.getConnectInfo().getUserID(), config);
    }

    @Override
    public DevUserConfig getUserConfig() throws JDSException {
        DevUserConfig userConfig = this.cacheManager.getUserConfig(this.getConnectInfo().getUserID());
        if (userConfig == null) {
            userConfig = new DevUserConfig(this.getConnectInfo().getUserID());
            updateUserConfig(userConfig);
        }
        return userConfig;
    }

    @Override
    public List<APIPaths> getAPIPathsByProject(String versionName) throws JDSException {
        return this.apiFactory.getAPIPathsByProject(versionName);
    }

    @Override
    public List<OODAPIConfig> searchLocalService(String versionName, String pattern) throws JDSException {
        return this.apiFactory.searchLocalService(versionName, pattern);
    }

    @Override
    public FontConfig getFont(String projectName, String fontId) throws JDSException {
        return fontManager.getFontById(fontId);
    }

    @Override
    public ImgConfig getImgConfig(String projectName, String imgConfigId) throws JDSException {
        return imgFactory.getImgConfigById(projectName, imgConfigId);

    }

    @Override
    public ImgConfig buildImgConfig(String projectName, String path) throws JDSException {
        Folder folder = this.getFolderByPath(path, projectName);
        if (folder == null) {
            String projectPath = formartPath(path, projectName);
            this.getVfsClient().mkDir(projectPath);
        }
//        if (folder == null) {
//            throw new JDSException("图片资源地址不存在！");
//        }
        return imgFactory.buildFolder(projectName, folder);
    }

    @Override
    public void reLoadImageConfig() throws JDSException {
        imgFactory.reLoad();
    }

    @Override
    public StyleConfig getStyleConfig(String styleConfigId) throws JDSException {
        return styleFactory.getStyleConfigById(styleConfigId);
    }

    @Override
    public StyleConfig buildStyleConfig(String projectName, String path) throws JDSException {
        Folder folder = this.getFolderByPath(path, projectName);
        if (folder == null) {
            String projectPath = formartPath(path, projectName);
            this.getVfsClient().mkDir(projectPath);
            throw new JDSException("样式资源地址不存在！");
        }
        return styleFactory.buildFolder(folder);
    }


    @Override
    public List<APIConfig> scannerLocalClass(String packageName, String patten) throws JDSException {
        return this.apiFactory.scannerAPIConfig(packageName, patten);
    }

    @Override
    public List<APICallerProperties> scannerLocalMethod(String packageName, String patten) throws JDSException {
        return this.apiFactory.scannerAPIMethodConfig(packageName, patten);
    }

    @Override
    public List<APIPaths> scannerLocalPaths(String packageName, String patten) throws JDSException {
        return apiFactory.scannerAPIPaths(patten);
    }

    @Override
    public APIPaths getAPIPaths(String path) throws JDSException {
        return apiFactory.getAPIPaths(path);
    }

    @Override
    public List<APIPaths> getAPITopPaths(String pattern, APIType apiType) throws JDSException {
        return apiFactory.getAPITopPaths(pattern, apiType);
    }

    @Override
    public APICallerProperties getAPIMethodConfig(String path) throws JDSException {
        return (APICallerProperties) this.apiFactory.getAPIPaths(path).getSource();
    }


    @Override
    public RequestMethodBean getRequestMethodBean(String path, String projectName) throws JDSException {
        return this.viewFactory.getMethodBeanByPath(path, projectName);
    }

    @Override
    public MethodConfig getMethodAPIBean(String path, String projectName) throws JDSException {
        return this.viewFactory.getMethodAPIBean(path, projectName);
    }


    @Override
    public List<APICallerProperties> getAPIMethodsByProject(String projectName) throws JDSException {
        return this.apiFactory.getAPIMethodConfigByProject(projectName);
    }

    @Override
    public List<APIConfig> getAPIConfigByProject(String projectName) throws JDSException {
        return this.apiFactory.getAPIConfigByProject(projectName);
    }

    @Override
    public List<ProviderConfig> getAllDbConfig() throws JDSException {
        return this.fdtFactory.getAllDbConfig();
    }

    @Override
    public void removeDbConfig(String dbConfigKey) throws JDSException {
        if (!isAdmin()) {
            throw new JDSException("权限错误，非管理员禁止操作");
        }
        this.fdtFactory.removeDbConfig(dbConfigKey);
    }

    @Override
    public ProviderConfig getDbConfig(String dbConfigKey) throws JDSException {
        return this.fdtFactory.getDbConfig(dbConfigKey);
    }

    @Override
    public void updateDbConfig(ProviderConfig config) throws JDSException {
        if (!isAdmin()) {
            throw new JDSException("权限错误，非管理员禁止操作");
        }
        this.fdtFactory.updateDbConfig(config);
    }

    @Override
    public void updateProjectConfig(String projectId, ProjectConfig config) throws JDSException {
        this.cacheManager.updateProjectConfig(projectId, config);
    }

    @Override
    public void updateBPDConfig(String projectId, BPDProjectConfig config) throws JDSException {
        this.cacheManager.updateBPDProjectConfig(projectId, config);
    }

    @Override
    public void updateDSMConfig(String projectId, DSMProjectConfig config) throws JDSException {
        this.cacheManager.updateDSMConfig(projectId, config);
    }

    @Override
    public void updateFormulaConfig(String projectId, FormulaInst formulaInst) throws JDSException {
        this.cacheManager.updateFormulaConfig(projectId, formulaInst);
    }

    @Override
    public void deleteFormulaConfig(String projectId, String formulaInstId) throws JDSException {
        this.cacheManager.deleteFormulaConfig(projectId, formulaInstId);
    }


    @Override
    public void updateRepositoryInstConfig(String projectId, RepositoryInst config) throws JDSException {
        this.cacheManager.updateRepositoryConfig(projectId, config);
    }

    @Override
    public MetadataFactory getDbFactory(String configKey) throws JDSException {
        MetadataFactory factory = null;
        try {
            factory = this.fdtFactory.getMetadataFactory(configKey);
        } catch (DAOException e) {
            e.printStackTrace();
        }
        return factory;
    }

    @Override
    public List<FontConfig> getFontByProject(String versionName) throws JDSException {
        return fontManager.getFontByProject(versionName);
    }

    @Override
    public FileInfo saveFile(StringBuffer content, String... paths) throws JDSException {
        String path = this.formartPath(paths);
        FileInfo fileInfo = this.getVfsClient().saveFileAsContent(path, content.toString(), VFSConstants.Default_Encoding);

        if (paths.length > 1) {
            String versionName = paths[1];
            try {
                this.publicLocalResource(versionName, paths[0], content.toString(), null);
            } catch (JDSException e) {

            }

        }

        return fileInfo;
    }

    @Override
    public List<Project> getAllProject(ProjectDefAccess type) {
        Long start = System.currentTimeMillis();
        List<INProject> inProjects = cacheManager.getProjectList(type);
        logger.info("cacheManager.getProjectList project  end times=" + (System.currentTimeMillis() - start));
        List<Project> projects = new ArrayList<Project>();
        try {
            Person currPerson = OrgManagerFactory.getOrgManager(this.clientService.getConfigCode()).getPersonByID(this.getConnectInfo().getUserID());
            String adminId = JDSServer.getClusterClient().getSystem(this.getSystemCode()).getAdminId();
            boolean isAdmin = false;
            if (this.getConnectInfo().getUserID().equals(adminId)) {
                isAdmin = true;
            }

            for (INProject inProject : inProjects) {
                try {
                    List<Person> devPersons = inProject.getDevPersons(ProjectRoleType.all);
                    if (devPersons.size() == 0 || devPersons.contains(currPerson) || isAdmin) {
                        projects.add(this.getProjectById(inProject.getId()));
                    }
                } catch (JDSException e) {
                    e.printStackTrace();
                }
            }
        } catch (PersonNotFoundException e) {
            e.printStackTrace();
        }
        ;
        logger.info("getProjectById  end times=" + (System.currentTimeMillis() - start));
        return projects;
    }


    @Override
    public List<Project> getResourceAllProject(ProjectResourceType type) {
        List<INProject> inProjects = cacheManager.getResourceProjectList(type);
        List<Project> projects = new ArrayList<Project>();
        for (INProject inProject : inProjects) {
            try {
                projects.add(this.getProjectById(inProject.getId()));
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        return projects;
    }

    @Override
    public SubSystem getOwnSystem() {
        SubSystem system = null;
        if (JDSServer.getClusterClient().isLogin()) {
            system = JDSServer.getClusterClient().getSystem(this.getSystemCode());
        } else {
            system = new CtSubSystem();
            system.setName(UserBean.getInstance().getSystemCode());
            system.setSysId(UserBean.getInstance().getSystemCode());
            system.setUrl(UserBean.getInstance().getServerUrl());
            system.setAdminId(UserBean.getInstance().getUsername());
        }

        return system;
    }

    @Override
    public EUModule copyClass(String versionName, String className, String tclassName) throws JDSException {
        ModuleComponent moduleComponent = this.getModule(className, versionName, true).getComponent();
        EUModule tmodule = this.getModule(tclassName, versionName, true);
        if (tmodule != null) {
            throw new JDSException("文件已存在!");
        }
        tmodule = this.createModule(tclassName, versionName);
        tmodule.getComponent().setProperties(moduleComponent.getProperties());
        tmodule.setDesc("copy by " + className);
        tmodule.getComponent().setFunctions(moduleComponent.getFunctions());
        tmodule.getComponent().setDependencies(moduleComponent.getDependencies());
        tmodule.getComponent().setViewConfig(moduleComponent.getViewConfig());
        tmodule.getComponent().setRequired(moduleComponent.getRequired());
        tmodule.getComponent().setEvents(moduleComponent.getEvents());
        tmodule.getComponent().setFormulas(moduleComponent.getFormulas());
        tmodule.getComponent().setCustomFunctions(moduleComponent.getCustomFunctions());
        tmodule.getComponent().setModuleVar(moduleComponent.getModuleVar());
        tmodule.getComponent().setCustomAppend(moduleComponent.getCustomAppend());
        tmodule.getComponent().setCS(moduleComponent.getCS());
        ComponentList components = moduleComponent.getChildren();

        if (components != null) {
            for (net.ooder.esd.tool.component.Component ccomponent : components) {
                tmodule.getComponent().addChildren(ccomponent);
            }
        }

        return tmodule;
    }

    @Override
    public Project cloneProject(String projectName, String desc, String tempName, ProjectDefAccess type) throws JDSException {
        ConnectInfo connectInfo = this.getConnectInfo();
//        if (!isAdmin() && !type.equals(ProjectDefAccess.Public)) {
//            throw new JDSException("权限错误，非管理员禁止操作");
//        }
        INProject inProject = cacheManager.cloneProject(projectName, desc, tempName, type, connectInfo.getUserID());
        return this.getProjectById(inProject.getId());
    }

    @Override
    public Project cloneResourceProject(String projectName, String desc, String tempName, ProjectResourceType resourceType) throws JDSException {
        ConnectInfo connectInfo = this.getConnectInfo();
        if (!isAdmin()) {
            throw new JDSException("权限错误，非管理员禁止操作");
        }
        INProject inProject = cacheManager.cloneResourceProject(projectName, desc, tempName, resourceType, connectInfo.getUserID());
        return this.getProjectById(inProject.getId());

    }


    @Override
    public void reLoadProject(String projectName) throws JDSException {

        cacheManager.reLoadProject(projectName);


    }

    @Override
    public void reLoadProjectRoot(String projectName) throws JDSException {
        //viewFactory.clearProject(projectName);

        RemoteConnectionManager.getConntctionService(projectName).execute(new Runnable() {
            @Override
            public void run() {
                try {
                    cacheManager.reLoadProject(projectName);
                } catch (JDSException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    @Override
    public Project createProject(String projectName, String desc, ProjectDefAccess type) throws JDSException {
        ConnectInfo connectInfo = this.getConnectInfo();
        if (!isAdmin() && !type.equals(ProjectDefAccess.Public)) {
            throw new JDSException("权限错误，非管理员禁止操作");
        }
        INProject inProject = cacheManager.createProject(projectName, desc, connectInfo.getUserID(), type);
        return this.getProjectById(inProject.getId());
    }


    @Override
    public Project createResourceProject(String projectName, String desc, ProjectResourceType type) throws JDSException {
        ConnectInfo connectInfo = this.getConnectInfo();
        if (!isAdmin()) {
            throw new JDSException("权限错误，非管理员禁止操作");
        }
        INProject inProject = cacheManager.createResourceProject(projectName, desc, connectInfo.getUserID(), type);
        return this.getProjectById(inProject.getId());
    }

    @Override
    public Project updateProjectInfo(String projectName, String desc) throws JDSException {
        ConnectInfo connectInfo = this.getConnectInfo();
        INProject inProject = cacheManager.updateProjectInfo(projectName, desc, connectInfo.getUserID());
        return this.getProjectById(inProject.getId());
    }


    @Override
    public List<DSMBean> getDSMBeanList() throws JDSException {
        return buildFactory.getTempManager().getDSMBeanList();
    }

    @Override
    public DSMBean getDSMBeanById(String dsmid) throws JDSException {
        return buildFactory.getTempManager().getDSMBeanById(dsmid);
    }

    @Override
    public DSMBean createDSMBean(String configKey) throws JDSException {
        return buildFactory.getTempManager().createDSMBean(configKey);
    }

    @Override
    public DSMBean updateDSMBean(DSMBean tempBean) throws JDSException {
        return buildFactory.getTempManager().updateDSMBean(tempBean);
    }

    @Override
    public void deleteDSMBean(String dsmid) throws JDSException {
        buildFactory.getTempManager().deleteDSMBean(dsmid);
    }


    private List<FileInfo> getImgfile(String projectName) {
        List<FileInfo> imgFiles = new ArrayList<FileInfo>();
        ProjectVersion version = null;
        try {
            version = getProjectVersionByName(projectName);
            List<ImgConfig> imgConfigs = version.getProject().getImgs();
            for (ImgConfig imgConfig : imgConfigs) {
                Folder folder = CtVfsFactory.getCtVfsService().getFolderById(imgConfig.getId());
                if (folder != null) {
                    List<FileInfo> fileInfos = folder.getFileListRecursively();
                    for (FileInfo cssFile : fileInfos) {
                        if (!imgFiles.contains(cssFile)) {
                            imgFiles.add(cssFile);
                        }

                    }
                }
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return imgFiles;
    }


    private List<FileInfo> getCssfile(String projectName) {
        List<FileInfo> cssFiles = new ArrayList<FileInfo>();
        ProjectVersion version = null;
        try {
            version = getProjectVersionByName(projectName);
            List<FontConfig> fontNodes = version.getProject().getFonts();
            for (FontConfig fontNode : fontNodes) {
                Folder folder = getFolderByPath(fontNode.getFile());
                if (folder != null) {
                    List<FileInfo> fileInfos = folder.getFileListRecursively();
                    for (FileInfo cssFile : fileInfos) {
                        if (cssFile.getName().endsWith(".css") && cssFile.getCurrentVersion() != null && cssFile.getCurrentVersion().getLength() > 0) {
                            if (!cssFiles.contains(cssFile)) {
                                cssFiles.add(cssFile);
                            }
                        }
                    }
                }

            }
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return cssFiles;
    }


    public String classToPath(String className, String versionName) {
        String versionPath = "";
        try {

            versionPath = this.getProjectVersionByName(versionName).getPath();

        } catch (JDSException e) {
            e.printStackTrace();
        }
        if (className.startsWith("/")) {
            className = className.substring(1);
        }


        if (className.endsWith(CLASSPATT)) {
            className = className.substring(0, className.length() - CLASSPATT.length());
        }
        String classPath = StringUtility.replace(className, ".", "/");

        if (!classPath.startsWith(versionPath)) {
            versionPath = versionPath + classPath + CLASSPATT;
        } else {
            versionPath = classPath + CLASSPATT;
        }
        // String path = versionPath + classPath + CLASSPATT;
        return versionPath;
    }

    private String formatClassName(String path, String versionName) {
        String versionPath = "";
        try {
            versionPath = this.getProjectVersionByName(versionName).getPath();
        } catch (JDSException e) {
            e.printStackTrace();
        }

        path = StringUtility.replace(path, "//", "/");
        if (!path.endsWith(CLASSPATT)) {
            if (path.endsWith(JSPATT)) {
                try {
                    FileInfo fileInfo = this.getFileByPath(path, versionName);
                    if (fileInfo != null) {
                        path = path.substring(0, path.length() - 3);
                    }
                } catch (JDSException e) {
                    e.printStackTrace();
                }

            }
            path = StringUtility.replace(path, "/", ".");
            if (path.startsWith(".")) {
                path = path.substring(1);
            }
            if (path.endsWith(".")) {
                path = path.substring(0, path.length() - 1);
            }
        } else {
            path = StringUtility.replace(path, "/", ".");
        }


        String projectPkName = StringUtility.replace(versionPath, "/", ".");
        String className = StringUtility.replace(path, projectPkName, "");

        return className;
    }

    private String formartPath(String... paths) throws JDSException {
        String versionName = null;
        String path = paths[0];
        if (paths.length > 1) {
            versionName = paths[1];
            String packagePath = "";
            try {

                ProjectVersion version = this.getProjectVersionByName(versionName);
                if (version != null) {
                    packagePath = version.getPath();
                } else {
                    throw new JDSException("versionName [" + versionName + "] not fround! path is [" + path + "]");
                }


            } catch (JDSException e) {

                e.printStackTrace();
            }
            if (packagePath != null && !packagePath.equals("") && !path.startsWith(space.getPath()) && !path.startsWith(packagePath)) {

                if (!packagePath.endsWith("/")) {
                    packagePath = packagePath + "/";
                }
                if (path.startsWith("/")) {
                    path = path.substring(1);
                }
                path = packagePath + path;
                path = StringUtility.replace(path, "//", "/");
                //  path = StringUtility.replace(path, "//", "/.");
                if (!path.startsWith(packagePath)) {
                    throw new JDSException("文件路径运算错误！" + path);
                }

            }


        } else {
            String spacepath = space.getPath();
            if (!path.startsWith(spacepath)) {
                path = spacepath + path;
                path = StringUtility.replace(path, "//", "/");
                // path = StringUtility.replace(path, "//", "/.");
            }
        }
        return path;

    }


    public RemoteServer getDefaultRemoteServer(String projectName) throws JDSException {

        DevUserConfig userConfig = this.getUserConfig();
        RemoteServer server = null;
        if (userConfig.getRemoteServerByProjectName(projectName).size() > 0) {
            server = userConfig.getRemoteServerByProjectName(projectName).get(0);
        } else if (userConfig.getRemoteServers().size() > 0) {
            server = userConfig.getRemoteServers().get(0);
        }
        if (server != null) {
            server.setProjectName(projectName);
        }
        return server;
    }


    public LocalServer getDefaultLocalServer(String projectName) throws JDSException {
        DevUserConfig userConfig = this.getUserConfig();
        LocalServer server = null;
        if (userConfig.getLocalServerByProjectName(projectName).size() > 0) {
            server = userConfig.getLocalServerByProjectName(projectName).get(0);
        } else if (userConfig.getServers().size() > 0) {
            server = userConfig.getServers().get(0);
        }
        if (server != null) {
            server.setProjectName(projectName);
        }
        return server;

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
