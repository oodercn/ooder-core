package net.ooder.esd.manager.plugins.style;

import com.alibaba.fastjson.JSONObject;
import net.ooder.common.FolderState;
import net.ooder.common.JDSException;
import net.ooder.context.JDSActionContext;
import net.ooder.context.JDSContext;
import net.ooder.esd.engine.MySpace;
import net.ooder.esd.engine.ProjectCacheManager;
import net.ooder.esd.engine.enums.ProjectResourceType;
import net.ooder.esd.engine.inner.INProject;
import net.ooder.esd.engine.inner.INProjectVersion;
import net.ooder.esd.manager.plugins.style.node.ALStyleFolder;
import net.ooder.esd.manager.plugins.style.node.StyleConfig;
import net.ooder.server.context.MinServerActionContextImpl;
import net.ooder.vfs.FileInfo;
import net.ooder.vfs.Folder;
import net.ooder.vfs.VFSConstants;
import net.ooder.vfs.ct.CtVfsFactory;
import net.ooder.vfs.ct.CtVfsService;
import net.ooder.web.RemoteConnectionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class StyleFactory {

    private final ProjectCacheManager projectCacheManager;

    Map<String, StyleConfig> styleConfigMap = new HashMap<String, StyleConfig>();


    private final MySpace space;

    public static final String defaultstyleJSONName = "styleConfig.json";

    public static final String IFstyleMenuJsonFileName = "IstyleMenuJson.json";

    public static final String THREAD_LOCK = "Thread Lock";


    static Map<String, StyleFactory> managerMap = new HashMap<String, StyleFactory>();


    public static StyleFactory getInstance(MySpace space) {
        String path = space.getPath();
        StyleFactory manager = managerMap.get(path);
        if (manager == null) {
            synchronized (path) {
                manager = managerMap.get(path);
                if (manager == null) {
                    manager = new StyleFactory(space);
                    managerMap.put(path, manager);
                }
            }
        }
        return manager;
    }

    StyleFactory(MySpace space) {
        this.space = space;
        this.projectCacheManager = ProjectCacheManager.getInstance(space);
        List<INProject> projects = this.projectCacheManager.getResourceProjectList(ProjectResourceType.css);
        List<BuildTempTask<INProject>> tasks = new ArrayList<>();
        for (INProject project : projects) {
            if (project != null) {
                tasks.add(new BuildTempTask<>(project));
            }

            //buildTemp(project);
        }
        try {
            //  RemoteConnectionManager.initConnection("CTLoadstyles", tasks.size());
            List<Future<INProject>> futures = RemoteConnectionManager.getConntctionService("tasks").invokeAll(tasks);
            for (Future<INProject> resultFuture : futures) {
                try {
                    INProject result = resultFuture.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            RemoteConnectionManager.getConntctionService("tasks").shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


    class BuildTempTask<T extends INProject> implements Callable<T> {
        private final INProject project;
        private final MinServerActionContextImpl autoruncontext;

        public BuildTempTask(INProject project) {
            this.project = project;
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
        public T call() throws Exception {
            JDSActionContext.setContext(autoruncontext);
            return (T) StyleFactory.this.buildTemp(project);
        }
    }

    public void reLoad() {
        managerMap.clear();
        styleConfigMap.clear();
    }

    public StyleConfig buildFolder(Folder folder) {
        StyleConfig config = null;
        if (folder != null) {
            config = styleConfigMap.get(folder.getID());
            if (config == null) {
                ALStyleFolder alstyleFolder = new ALStyleFolder(folder);
                alstyleFolder.build(folder, true);
                config = new StyleConfig(alstyleFolder);
                this.styleConfigMap.put(folder.getID(), config);
            }
        }
        return config;
    }


    INProject buildTemp(INProject project) {


        if (project.getResourceType() != null && project.getResourceType().equals(ProjectResourceType.css)) {
            List<INProjectVersion> versions = new ArrayList<INProjectVersion>();
            try {
                Folder versionfolderroot = getVfsClient().getFolderByPath(projectCacheManager.projectversiospace + project.getProjectName());
                for (Folder versionfolder : versionfolderroot.getChildrenList()) {
                    if (versionfolder.getState().equals(FolderState.normal)) {

                        List<Folder> folders = versionfolder.getChildrenRecursivelyList();
                        for (Folder cfolder : folders) {
                            FileInfo fileInfo = this.getVfsClient().getFileByPath(cfolder.getPath() + defaultstyleJSONName);
                            if (fileInfo == null && cfolder.getChildrenIdList().size() > 0 && cfolder.getChildrenIdList().isEmpty()) {
                                String json = JSONObject.toJSONString(new ALStyleFolder(cfolder), true);
                                fileInfo = this.getVfsClient().saveFileAsContent(cfolder.getPath() + defaultstyleJSONName, json, VFSConstants.Default_Encoding);
                            } else {
                                StringBuffer conf = this.getVfsClient().readFileAsString(fileInfo.getPath(), VFSConstants.Default_Encoding);
                                StyleConfig alstyleFolder = JSONObject.parseObject(conf.toString(), StyleConfig.class);
                            }
                        }

                        List<FileInfo> allFile = versionfolder.getFileListRecursively();
                        for (FileInfo fileInfo : allFile) {
                            if (fileInfo.getName().equals(defaultstyleJSONName)) {
                                try {
                                    StringBuffer conf = this.getVfsClient().readFileAsString(fileInfo.getPath(), VFSConstants.Default_Encoding);
                                    ALStyleFolder styleFolder = JSONObject.parseObject(conf.toString(), ALStyleFolder.class);
                                    styleFolder.build(fileInfo.getFolder(), false);
                                    styleConfigMap.put(styleFolder.getId(), new StyleConfig(styleFolder));
                                } catch (Throwable e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    }
                    getStyleTmep(project.getActiviteVersion().getVersionName());
                }


            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        return project;
    }


    private StyleConfig getStyleTmep(String projectName) throws JDSException {
        INProjectVersion projectVersion = projectCacheManager.getProjectVersionByName(projectName);
        Folder rootFolder = projectVersion.getRootFolder();
        List<StyleConfig> styleConfigs = new ArrayList<StyleConfig>();
        StyleConfig menuConfig = null;


        FileInfo fileInfo = this.getVfsClient().getFileByPath(projectVersion.getPath() + IFstyleMenuJsonFileName);
        if (fileInfo == null) {
            List<FileInfo> allFile = rootFolder.getFileListRecursively();
            for (FileInfo cfileInfo : allFile) {
                if (cfileInfo.getName().equals(defaultstyleJSONName)) {
                    try {
                        StringBuffer conf = this.getVfsClient().readFileAsString(cfileInfo.getPath(), VFSConstants.Default_Encoding);
                        StyleConfig alstyleFolder = JSONObject.parseObject(conf.toString(), StyleConfig.class);
                        styleConfigs.add(alstyleFolder);
                        styleConfigMap.put(alstyleFolder.getId(), alstyleFolder);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
            ALStyleFolder mainstyleMenu = new ALStyleFolder();
            mainstyleMenu.setCaption("所有图片");
            List<ALStyleFolder> ids = mainstyleMenu.getSubItems();
            for (StyleConfig styleConfig : styleConfigs) {
                ALStyleFolder styleMenu = new ALStyleFolder(styleConfig.getId(), styleConfig.getCaption(), styleConfig.getName(), styleConfig.getImageClass());
                ids.add(styleMenu);
            }
            mainstyleMenu.setSubItems(ids);
            String json = JSONObject.toJSONString(mainstyleMenu, true);
            fileInfo = this.getVfsClient().saveFileAsContent(projectVersion.getPath() + IFstyleMenuJsonFileName, json, VFSConstants.Default_Encoding);
        }

        StringBuffer confMenu = this.getVfsClient().readFileAsString(projectVersion.getPath() + IFstyleMenuJsonFileName, VFSConstants.Default_Encoding);
        ALStyleFolder alstyleFolder = JSONObject.parseObject(confMenu.toString(), ALStyleFolder.class);
        menuConfig = new StyleConfig(alstyleFolder);
        menuConfig.setId(projectVersion.getProjectId() + "allstyle");

        return menuConfig;
    }

    public StyleConfig getStyleConfigById(String menuId) {

        StyleConfig config = styleConfigMap.get(menuId);
        if (config == null) {
            try {
                Folder folder = this.getVfsClient().getFolderById(menuId);
                if (folder != null) {
                    config = this.buildFolder(folder);
                    styleConfigMap.put(config.getId(), config);
                }

            } catch (JDSException e) {
                e.printStackTrace();
            }
        }

        return config;
    }

    public List<StyleConfig> getStyleByProject(String projectName) throws JDSException {
        INProjectVersion projectVersion = projectCacheManager.getProjectVersionByName(projectName);
        List<StyleConfig> styleConfigs = new ArrayList<StyleConfig>();
        if (projectVersion.getProject().getResourceType() != null && projectVersion.getProject().getResourceType().equals(ProjectResourceType.css)) {
            styleConfigs.add(this.getStyleTmep(projectName));
        } else {
            List<String> styleIds = projectVersion.getProject().getConfig().getStyles();
            for (String styleId : styleIds) {
                StyleConfig styleConfig = this.getStyleConfigById(styleId);
                if (styleConfig != null) {
                    styleConfigs.add(styleConfig);
                }
            }
        }
        return styleConfigs;
    }

    public CtVfsService getVfsClient() {
        CtVfsService vfsClient = CtVfsFactory.getCtVfsService();
        return vfsClient;
    }


}


