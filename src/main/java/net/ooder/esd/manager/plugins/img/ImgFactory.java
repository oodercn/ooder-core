package net.ooder.esd.manager.plugins.img;

import com.alibaba.fastjson.JSONObject;
import net.ooder.common.FolderState;
import net.ooder.common.JDSException;
import net.ooder.context.JDSActionContext;
import net.ooder.context.JDSContext;
import net.ooder.esd.engine.ProjectCacheManager;
import net.ooder.esd.manager.plugins.img.node.ALImgFolder;
import net.ooder.esd.manager.plugins.img.node.ImgConfig;
import net.ooder.esd.engine.MySpace;
import net.ooder.esd.engine.inner.INProject;
import net.ooder.esd.engine.inner.INProjectVersion;
import net.ooder.esd.engine.enums.ProjectResourceType;
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

public class ImgFactory {

    private final ProjectCacheManager projectCacheManager;

    Map<String, ImgConfig> imgConfigMap = new HashMap<String, ImgConfig>();


    private final MySpace space;

    public static final String defaultImgJSONName = "imgConfig.json";

    public static final String IFImgMenuJsonFileName = "IIMGMenuJson.json";

    public static final String THREAD_LOCK = "Thread Lock";


    static Map<String, ImgFactory> managerMap = new HashMap<String, ImgFactory>();


    public static ImgFactory getInstance(MySpace space) {
        String path = space.getPath();
        ImgFactory manager = managerMap.get(path);
        if (manager == null) {
            synchronized (path) {
                manager = managerMap.get(path);
                if (manager == null) {
                    manager = new ImgFactory(space);
                    managerMap.put(path, manager);
                }
            }
        }
        return manager;
    }

    ImgFactory(MySpace space) {
        this.space = space;
        this.projectCacheManager = ProjectCacheManager.getInstance(space);
        List<INProject> projects = this.projectCacheManager.getResourceProjectList(ProjectResourceType.img);
        List<BuildTempTask<INProject>> tasks = new ArrayList<>();
        for (INProject project : projects) {
            tasks.add(new BuildTempTask<>(project));
            //  buildTemp(project);
        }
        try {
           // RemoteConnectionManager.initConnection("CTLoadImgs", tasks.size());
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        RemoteConnectionManager.getConntctionService("tasks").shutdown();

    }

    public void reLoad() {
        imgConfigMap.clear();
    }

    class BuildTempTask<T extends INProject> implements Callable<T> {
        private final INProject project;
        private final MinServerActionContextImpl autoruncontext;

        public BuildTempTask(INProject project) {
            this.project = project;
            JDSContext context = JDSActionContext.getActionContext();
            this.autoruncontext = new MinServerActionContextImpl(context.getHttpRequest(),context.getOgnlContext());
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
            return (T) ImgFactory.this.buildTemp(project);
        }
    }

    public ImgConfig buildFolder(String projectName, Folder folder) {
        ImgConfig config = null;
        if (folder != null) {
            config = imgConfigMap.get(folder.getID());
            if (config == null) {
                ALImgFolder alImgFolder = new ALImgFolder(projectName, folder);
                alImgFolder.build(folder, projectName, true);
                config = new ImgConfig(alImgFolder);
                this.imgConfigMap.put(folder.getID(), config);
            }
        }
        return config;
    }


    INProject buildTemp(INProject project) {
        if (project.getResourceType() != null && project.getResourceType().equals(ProjectResourceType.img)) {
            List<INProjectVersion> versions = new ArrayList<INProjectVersion>();
            try {
                Folder versionfolderroot = getVfsClient().getFolderByPath(projectCacheManager.projectversiospace + project.getProjectName());
                if (versionfolderroot != null) {
                    for (Folder versionfolder : versionfolderroot.getChildrenList()) {
                        if (versionfolder.getState().equals(FolderState.normal)) {

                            List<Folder> folders = versionfolder.getChildrenRecursivelyList();
                            for (Folder cfolder : folders) {
                                FileInfo fileInfo = this.getVfsClient().getFileByPath(cfolder.getPath() + defaultImgJSONName);
                                if (fileInfo == null && cfolder.getChildrenIdList().size() > 0 && cfolder.getChildrenIdList().isEmpty()) {
                                    String json = JSONObject.toJSONString(new ALImgFolder(project.getActiviteVersion().getVersionName(), cfolder), true);
                                    fileInfo = this.getVfsClient().saveFileAsContent(cfolder.getPath() + defaultImgJSONName, json, VFSConstants.Default_Encoding);
                                } else {
                                    StringBuffer conf = this.getVfsClient().readFileAsString(fileInfo.getPath(), VFSConstants.Default_Encoding);
                                    ImgConfig alImgFolder = JSONObject.parseObject(conf.toString(), ImgConfig.class);
                                }
                            }

                            List<FileInfo> allFile = versionfolder.getFileListRecursively();
                            for (FileInfo fileInfo : allFile) {
                                if (fileInfo.getName().equals(defaultImgJSONName)) {
                                    try {
                                        StringBuffer conf = this.getVfsClient().readFileAsString(fileInfo.getPath(), VFSConstants.Default_Encoding);
                                        ALImgFolder imgFolder = JSONObject.parseObject(conf.toString(), ALImgFolder.class);
                                        imgFolder.build(fileInfo.getFolder(), project.getActiviteVersion().getVersionName(), false);
                                        imgConfigMap.put(imgFolder.getId(), new ImgConfig(imgFolder));
                                    } catch (Throwable e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                        }
                        getImgTmep(project.getActiviteVersion().getVersionName());
                    }
                }
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        return project;
    }


    private ImgConfig getImgTmep(String projectName) throws JDSException {
        INProjectVersion projectVersion = projectCacheManager.getProjectVersionByName(projectName);
        Folder rootFolder = projectVersion.getRootFolder();
        List<ImgConfig> imgConfigs = new ArrayList<ImgConfig>();
        ImgConfig menuConfig = null;


        FileInfo fileInfo = this.getVfsClient().getFileByPath(projectVersion.getPath() + IFImgMenuJsonFileName);

        if (fileInfo == null|| fileInfo.getCurrentVersion().getLength()==0) {
            List<FileInfo> allFile = rootFolder.getFileListRecursively();
            for (FileInfo cfileInfo : allFile) {
                if (cfileInfo.getName().equals(defaultImgJSONName)) {
                    try {
                        StringBuffer conf = this.getVfsClient().readFileAsString(cfileInfo.getPath(), VFSConstants.Default_Encoding);
                        ImgConfig alImgFolder = JSONObject.parseObject(conf.toString(), ImgConfig.class);
                        imgConfigs.add(alImgFolder);
                        imgConfigMap.put(alImgFolder.getId(), alImgFolder);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
            ALImgFolder mainImgMenu = new ALImgFolder();
            mainImgMenu.setCaption("所有图片");
            List<ALImgFolder> ids = mainImgMenu.getSubItems();
            for (ImgConfig imgConfig : imgConfigs) {
                ALImgFolder imgMenu = new ALImgFolder(imgConfig.getId(), imgConfig.getCaption(), imgConfig.getName(), imgConfig.getImageClass());
                ids.add(imgMenu);
            }
            mainImgMenu.setSubItems(ids);
            String json = JSONObject.toJSONString(mainImgMenu, true);
            fileInfo = this.getVfsClient().saveFileAsContent(projectVersion.getPath() + IFImgMenuJsonFileName, json, VFSConstants.Default_Encoding);
        }

        StringBuffer confMenu = this.getVfsClient().readFileAsString(fileInfo.getPath(), VFSConstants.Default_Encoding);

        ALImgFolder alImgFolder = JSONObject.parseObject(confMenu.toString(), ALImgFolder.class);
        menuConfig = new ImgConfig(alImgFolder);
        menuConfig.setId(projectVersion.getProjectId() + "allimg");

        return menuConfig;
    }

    public ImgConfig getImgConfigById(String projectName, String imgConfigId) {

        ImgConfig config = imgConfigMap.get(imgConfigId);
        if (config == null) {
            try {
                Folder folder = this.getVfsClient().getFolderById(imgConfigId);
                if (folder != null) {
                    config = this.buildFolder(projectName, folder);
                    imgConfigMap.put(config.getId(), config);
                }

            } catch (JDSException e) {
                e.printStackTrace();
            }
        }

        return config;
    }

    public List<ImgConfig> getImgByProject(String projectName) throws JDSException {
        INProjectVersion projectVersion = projectCacheManager.getProjectVersionByName(projectName);
        List<ImgConfig> imgConfigs = new ArrayList<ImgConfig>();
        if (projectVersion.getProject().getResourceType() != null && projectVersion.getProject().getResourceType().equals(ProjectResourceType.img)) {
            imgConfigs.add(this.getImgTmep(projectName));
        } else {
            List<String> imgIds = projectVersion.getProject().getConfig().getImgs();
            for (String imgConfigId : imgIds) {
                ImgConfig imgConfig = this.getImgConfigById(projectName, imgConfigId);
                if (imgConfig != null) {
                    imgConfigs.add(imgConfig);
                }
            }
        }
        return imgConfigs;
    }

    public CtVfsService getVfsClient() {
        CtVfsService vfsClient = CtVfsFactory.getCtVfsService();
        return vfsClient;
    }


}


