package net.ooder.esd.manager.plugins.font;

import com.alibaba.fastjson.JSONObject;
import net.ooder.common.JDSException;
import net.ooder.context.JDSActionContext;
import net.ooder.context.JDSContext;
import net.ooder.esd.engine.ProjectCacheManager;
import net.ooder.esd.manager.plugins.font.node.FontConfig;
import net.ooder.esd.manager.plugins.font.node.IFontMenu;
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

public class FontFactory {

    private final ProjectCacheManager projectCacheManager;

    Map<String, FontConfig> fontConfigMap = new HashMap<String, FontConfig>();

    private final MySpace space;

    public static final String defaultIconfontName = "iconfont.json";

    public static final String SPLIC = "::";

    public static final String IFontMenuJsonFileName = "IFontMenuJson.json";

    public static final String THREAD_LOCK = "Thread Lock";


    static Map<String, FontFactory> managerMap = new HashMap<String, FontFactory>();


    public static FontFactory getInstance(MySpace space) {
        String path = space.getPath();
        FontFactory manager = managerMap.get(path);
        if (manager == null) {
            synchronized (path) {
                manager = managerMap.get(path);
                if (manager == null) {
                    manager = new FontFactory(space);
                    managerMap.put(path, manager);
                }
            }
        }
        return manager;
    }


    public void reLoad() {
        managerMap.clear();
        fontConfigMap.clear();
    }

    void buildCss(List<FontConfig> fontConfigs) {
        List<BuildCssLinkTask<FontConfig>> cssTasks = new ArrayList<>();
        for (FontConfig fontConfig : fontConfigs) {
            cssTasks.add(new BuildCssLinkTask(fontConfig));
        }
       // RemoteConnectionManager.initConnection("CTLoadCss", cssTasks.size());
        try {
            List<Future<FontConfig>> futures = RemoteConnectionManager.getConntctionService("CTLoadCss").invokeAll(cssTasks);
            for (Future<FontConfig> resultFuture : futures) {
                try {
                    FontConfig result = resultFuture.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            RemoteConnectionManager.getConntctionService("CTLoadCss").shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    FontFactory(MySpace space) {
        this.space = space;
        this.projectCacheManager = ProjectCacheManager.getInstance(space);
        List<INProject> projects = this.projectCacheManager.getResourceProjectList(ProjectResourceType.font);
        List<BuildTempTask<INProject>> tasks = new ArrayList<>();

        for (INProject project : projects) {
            // tasks.add(new BuildTempTask<>(project));

            buildTemp(project);
        }
//        try {
//            RemoteConnectionManager.initConnection("CTLoadFonts", tasks.size());
//            List<Future<INProject>> futures = RemoteConnectionManager.getConntctionService("CTLoadFonts").invokeAll(tasks);
//            for (Future<INProject> resultFuture : futures) {
//                try {
//                    INProject result = resultFuture.get();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                }
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


    }


    class BuildCssLinkTask<T extends FontConfig> implements Callable<T> {
        private final FontConfig fontConfig;
        private final MinServerActionContextImpl autoruncontext;

        public BuildCssLinkTask(FontConfig fontConfig) {
            this.fontConfig = fontConfig;
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
            return (T) FontFactory.this.buildCssLink(fontConfig);
        }
    }

    FontConfig buildCssLink(FontConfig fontNode) throws JDSException {
        List<FileInfo> cssFiles = new ArrayList<FileInfo>();
        Folder folder = getVfsClient().getFolderByPath(fontNode.getFile());
        if (folder != null) {
            List<FileInfo> fileInfos = folder.getFileListRecursively();
            for (FileInfo cssFile : fileInfos) {
                if (cssFile.getName().endsWith(".css") && cssFile.getCurrentVersion() != null && cssFile.getCurrentVersion().getLength() > 0) {
                    if (!cssFiles.contains(cssFile)) {
                        fontNode.getListCachePath().add(cssFile.getPath());
                    }
                }
            }
        }

        return fontNode;
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
            return (T) FontFactory.this.buildTemp(project);
        }
    }


    INProject buildTemp(INProject project) {

        if (project.getResourceType() != null && project.getResourceType().equals(ProjectResourceType.font)) {
            Folder versionfolder = project.getActiviteVersion().getRootFolder();
            for (Folder folder : versionfolder.getChildrenList()) {
                List<FileInfo> allFile = folder.getFileListRecursively();
                for (FileInfo fileInfo : allFile) {
                    if (fileInfo.getName().equals(defaultIconfontName)) {
                        try {
                            StringBuffer conf = this.getVfsClient().readFileAsString(fileInfo.getPath(), VFSConstants.Default_Encoding);
                            FontConfig fontConfig = JSONObject.parseObject(conf.toString(), FontConfig.class);
                            fontConfig.setId(project.getId() + SPLIC + fontConfig.getId());
                            fontConfig.setFile(fileInfo.getFolder().getPath());
                            fontConfig.setProjectName(project.getActiviteVersion().getVersionName());
                            fontConfig = buildCssLink(fontConfig);
                            if (fontConfig.getId().indexOf(SPLIC) > -1) {
                                fontConfigMap.put(fontConfig.getId(), fontConfig);
                            } else {
                                fontConfigMap.put(project.getId() + SPLIC + fontConfig.getId(), fontConfig);
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return project;
    }

    private IFontMenu createDefauleIcon(List<FontConfig> fontConfigs, String projectId) {
        IFontMenu iFontMenu = new IFontMenu();
        iFontMenu.setCaption("所有图标");
        List<IFontMenu> ids = iFontMenu.getSubItems();
        for (FontConfig fontConfig : fontConfigs) {
            IFontMenu fontMenu = new IFontMenu(fontConfig.getId(), fontConfig.getCaption(), fontConfig.getName(), fontConfig.getImageClass());
            if (!ids.contains(fontMenu)) {
                ids.add(fontMenu);
            }

        }
        return iFontMenu;
    }


    private FontConfig getFontTmep(String projectName) throws JDSException {
        INProjectVersion projectVersion = projectCacheManager.getProjectVersionByName(projectName);
        List<FontConfig> fontConfigs = this.loadAllFontConfig(projectVersion);
        IFontMenu rootFontMenu = loadFontMenu(projectVersion);
        if (rootFontMenu == null) {
            rootFontMenu = new IFontMenu();
            rootFontMenu.setId(projectVersion.getProjectId() + "root");
        }

        List<IFontMenu> subFontMenu = rootFontMenu.getSubItems();
        for (FontConfig fontConfig : fontConfigs) {
            IFontMenu fontMenu = new IFontMenu(fontConfig);
            if (!subFontMenu.contains(fontMenu)) {
                subFontMenu.add(fontMenu);
            }
        }
        saveFontMenu(projectVersion, rootFontMenu);
        FontConfig rootFontConfig = new FontConfig(rootFontMenu);
        rootFontConfig.setFile(projectVersion.getRootFolder().getPath());
        rootFontConfig = buildCssLink(rootFontConfig);

        return rootFontConfig;
    }

    private IFontMenu loadFontMenu(INProjectVersion projectVersion) throws JDSException {
        IFontMenu fontMenu = null;
        FileInfo fileInfo = this.getVfsClient().getFileByPath(projectVersion.getPath() + IFontMenuJsonFileName);
        if (fileInfo != null) {
            String json = this.getVfsClient().readFileAsString(fileInfo.getPath(), VFSConstants.Default_Encoding).toString();
            fontMenu = JSONObject.parseObject(json, IFontMenu.class);
        }
        return fontMenu;
    }

    private List<FontConfig> loadAllFontConfig(INProjectVersion projectVersion) {
        Folder rootFolder = projectVersion.getRootFolder();
        List<FontConfig> fontConfigs = new ArrayList<FontConfig>();
        List<FileInfo> allFile = rootFolder.getFileListRecursively();
        for (FileInfo fileInfo : allFile) {
            if (fileInfo.getName().equals(defaultIconfontName)) {
                try {
                    StringBuffer conf = this.getVfsClient().readFileAsString(fileInfo.getPath(), VFSConstants.Default_Encoding);
                    FontConfig fontConfig = JSONObject.parseObject(conf.toString(), FontConfig.class);
                    if (fontConfig.getId().indexOf(SPLIC) == -1) {
                        fontConfig.setId(projectVersion.getProjectId() + SPLIC + fontConfig.getId());
                    }
                    fontConfigMap.put(fontConfig.getId(), fontConfig);
                    fontConfig.setFile(fileInfo.getFolder().getPath());
                    fontConfigs.add(fontConfig);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
        return fontConfigs;
    }


    private void saveFontMenu(INProjectVersion projectVersion, IFontMenu rootFontMenu) throws JDSException {
        FileInfo fileInfo = this.getVfsClient().getFileByPath(projectVersion.getPath() + IFontMenuJsonFileName);
        this.getVfsClient().saveFileAsContent(fileInfo.getPath(), JSONObject.toJSONString(rootFontMenu, true), VFSConstants.Default_Encoding);
    }


    public List<FontConfig> getFontByProject(String projectName) throws JDSException {
        INProjectVersion projectVersion = projectCacheManager.getProjectVersionByName(projectName);
        List<FontConfig> fontConfigs = new ArrayList<FontConfig>();
        if (projectVersion.getProject().getResourceType() != null && projectVersion.getProject().getResourceType().equals(ProjectResourceType.font)) {
            fontConfigs.add(this.getFontTmep(projectName));
        } else {
            List<String> fontIds = projectVersion.getProject().getConfig().getFonts();
            for (String fontId : fontIds) {
                FontConfig fontConfig = this.getFontById(fontId);
                if (fontConfig != null) {
                    if (fontConfig.getListCachePath().isEmpty()) {
                        this.buildCssLink(fontConfig);
                    }
                    fontConfigs.add(fontConfig);
                }
            }
        }
        return fontConfigs;
    }

    public void setFontConfigMap(Map<String, FontConfig> fontConfigMap) {
        this.fontConfigMap = fontConfigMap;
    }

    public FontConfig getFontById(String fontId) {
        FontConfig fontConfig = fontConfigMap.get(fontId);
        if (fontConfig == null && fontId.indexOf(SPLIC) > -1) {
            String projectId = fontId.split(SPLIC)[0];
            INProject inProject = projectCacheManager.getProjectById(projectId);
            this.buildTemp(inProject);
        }
        return fontConfigMap.get(fontId);
    }


    public CtVfsService getVfsClient() {
        CtVfsService vfsClient = CtVfsFactory.getCtVfsService();
        return vfsClient;
    }


}


