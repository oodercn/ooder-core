package net.ooder.esd.dsm.view;

import com.alibaba.fastjson.JSON;
import net.ooder.annotation.RefType;
import net.ooder.annotation.UserSpace;
import net.ooder.common.JDSConstants;
import net.ooder.common.JDSException;
import net.ooder.common.logging.ChromeProxy;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.common.logging.LogSetpLog;
import net.ooder.common.util.ClassUtility;
import net.ooder.common.util.FileUtility;
import net.ooder.common.util.StringUtility;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.ViewType;
import net.ooder.esd.annotation.event.CustomEvent;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.CustomRefBean;
import net.ooder.esd.bean.CustomViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.bean.view.CustomBlockFormViewBean;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.dsm.aggregation.AggregationManager;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.aggregation.context.AggViewRoot;
import net.ooder.esd.dsm.enums.DSMTempType;
import net.ooder.esd.dsm.enums.DSMType;
import net.ooder.esd.dsm.enums.RangeType;
import net.ooder.esd.dsm.gen.GenJava;
import net.ooder.esd.dsm.gen.view.GenCustomViewJava;
import net.ooder.esd.dsm.gen.view.GenViewEntityRefJava;
import net.ooder.esd.dsm.gen.view.UpdateCustomViewJava;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.temp.JavaTemp;
import net.ooder.esd.dsm.view.context.ActionViewRoot;
import net.ooder.esd.dsm.view.context.DicViewRoot;
import net.ooder.esd.dsm.view.context.ViewRoot;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.dsm.view.ref.ViewEntityRef;
import net.ooder.esd.engine.MySpace;
import net.ooder.esd.engine.ProjectCacheManager;
import net.ooder.esd.engine.config.dsm.DSMProjectConfig;
import net.ooder.esd.engine.enums.MenuBarBean;
import net.ooder.esd.engine.enums.PackagePathType;
import net.ooder.esd.engine.inner.INProject;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.MenuBarComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.item.TabListItem;
import net.ooder.esd.tool.properties.list.ListFieldProperties;
import net.ooder.esd.util.OODUtil;
import net.ooder.server.JDSServer;
import net.ooder.vfs.FileInfo;
import net.ooder.vfs.Folder;
import net.ooder.vfs.VFSConstants;
import net.ooder.vfs.ct.CtVfsFactory;
import net.ooder.vfs.ct.CtVfsService;
import net.ooder.web.*;
import net.ooder.web.util.MethodUtil;
import ognl.OgnlException;
import ognl.OgnlRuntime;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class ViewManager {

    protected Log log = LogFactory.getLog(JDSConstants.CONFIG_KEY, ViewManager.class);

    private static final String THREAD_LOCK = "Thread Lock";

    public static final String ViewConfigPath = "viewclassconfig";

    private final ProjectCacheManager projectCacheManager;

    private final MySpace space;

    private Folder viewClassConfigFolder;

    Set<String> aggServiceClassNames = new HashSet<>();

    AggregationManager aggregationManager;

    List<SaveEntityConfigTask<FileInfo>> viewEntityConfigTasks = new ArrayList<>();

    Map<String, ViewEntityConfig> viewClassConfigMap = new ConcurrentHashMap<String, ViewEntityConfig>();

    private static Map<String, ViewManager> managerMap = new HashMap<String, ViewManager>();

    public static ViewManager getInstance(MySpace space) {

        String path = space.getPath();
        ViewManager manager = managerMap.get(path);
        if (manager == null) {
            synchronized (THREAD_LOCK) {
                if (manager == null) {
                    manager = new ViewManager(space);
                    managerMap.put(path, manager);
                }
            }
        }
        return manager;
    }

    ViewManager(MySpace space) {
        this.space = space;
        this.projectCacheManager = ProjectCacheManager.getInstance(space);

        try {
            this.viewClassConfigFolder = space.getRootfolder().createChildFolder(ViewConfigPath, JDSServer.getInstance().getAdminUser().getId());
        } catch (JDSException e) {
            e.printStackTrace();
        }

        aggregationManager = AggregationManager.getInstance(space);

    }


    public void reload() {
        aggServiceClassNames.clear();
        viewClassConfigMap.clear();
    }

    public List<ViewEntityRef> getViewEntityRefByName(String className, String viewInstId, RefType refType) throws JDSException {
        List<ViewEntityRef> refs = new ArrayList<>();
        List<ViewEntityRef> entityRefs = getViewEntityRefByName(className, viewInstId);
        for (ViewEntityRef ref : entityRefs) {
            if (ref.getRefBean().getRef().equals(refType)) {
                refs.add(ref);
            }
        }
        return refs;
    }

    public List<ViewEntityRef> getViewEntityRefByName(String className, String domainId) throws JDSException {
        //   ViewInst viewInst = this.getViewInstById(viewInstId);
        List<ViewEntityRef> entityRefs = new ArrayList<>();
        DomainInst domainInst = DSMFactory.getInstance().getDomainInstById(domainId);
        ViewInst viewInst = this.createDefaultView(domainInst);
        ESDClass esdClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(className, false);
        if (esdClass != null) {
            List<ESDField> methodInfos = esdClass.getRefFields();
            Map<String, ViewEntityRef> refMap = viewInst.getViewEntityRefMap();
            for (ESDField methodInfo : methodInfos) {
                if (methodInfo.getRefBean() != null && methodInfo.getRefBean().getRef() != null) {
                    ViewEntityRef entityRef = new ViewEntityRef(methodInfo, domainId);
                    if (!refMap.containsKey(entityRef.getRefId())) {
                        refMap.put(entityRef.getRefId(), entityRef);
                    }
                }
            }
            refMap.forEach((k, v) -> {
                if (v.getClassName().toLowerCase().equals(className.toLowerCase())) {
                    v.setRefId(k);
                    entityRefs.add(v);
                }
            });
        }

        return entityRefs;
    }


    class SaveEntityConfigTask<T extends FileInfo> implements Callable<FileInfo> {
        String content = null;
        String domainId = null;
        String className = null;

        SaveEntityConfigTask(ViewEntityConfig esdClassConfig) {
            this.domainId = esdClassConfig.getDomainId();
            this.content = JSON.toJSONString(esdClassConfig);
            this.className = esdClassConfig.getClassName();

        }

        @Override
        public FileInfo call() {
            FileInfo fileInfo = null;
            try {
                Folder dsmFolder = viewClassConfigFolder.createChildFolder(domainId, JDSServer.getInstance().getAdminUser().getId());
                String folerPath = formartPackagePath(className);
                String simClassName = formartClassName(className);
                Folder packageFolder = getVfsClient().mkDir(dsmFolder.getPath() + folerPath);
                fileInfo = packageFolder.createFile(simClassName, className, null);
                getVfsClient().saveFileAsContent(fileInfo.getPath(), content, VFSConstants.Default_Encoding);
            } catch (JDSException e) {
                e.printStackTrace();
            }
            return fileInfo;
        }
    }


    public void updateViewEntityConfig(ViewEntityConfig esdClassConfig) throws JDSException {
        updateViewEntityConfig(esdClassConfig, true);
    }

    public void updateViewEntityConfig(ViewEntityConfig esdClassConfig, boolean sync) throws JDSException {
        String uKey = esdClassConfig.getClassName() + "[" + esdClassConfig.getDomainId() + "]";
        ExecutorService executorService = RemoteConnectionManager.getConntctionService(uKey);

        viewClassConfigMap.put(uKey, esdClassConfig);
        SaveEntityConfigTask task = new SaveEntityConfigTask(esdClassConfig);
        if (sync) {
            executorService.submit(task);
        } else {
            try {
                executorService.submit(task).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        executorService.shutdownNow();
    }

    public void clearViewEntityConfig(String viewInstId) throws JDSException {
        if (viewInstId == null || viewInstId.equals("")) {
            throw new JDSException("viewInstId is null!");
        }

        Folder dsmFolder = viewClassConfigFolder.createChildFolder(viewInstId, JDSServer.getInstance().getAdminUser().getId());
        CtVfsFactory.getCtVfsService().deleteFolder(dsmFolder.getID());

    }

    public ViewEntityConfig createViewEntityConfig(String className, String domainId) throws JDSException {
        ESDClass esdClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(className, false);
        String uKey = className + "[" + domainId + "]";
        ViewEntityConfig esdClassConfig = viewClassConfigMap.get(uKey);
        if (esdClass != null) {
            RequestMappingBean requestMethodBean = esdClass.getSourceClass().getRequestMappingBean();
            if (requestMethodBean != null) {
                PackagePathType packagePathType = PackagePathType.startPath(requestMethodBean.getFristUrl());
                if (packagePathType != null && packagePathType.getApiType() != null && packagePathType.getApiType().getDefaultProjectName() != null) {
                    domainId = packagePathType.getApiType().getDefaultProjectName();
                }
            }

            if (className == null || className.equals("") || !MethodUtil.checkType(className)) {
                throw new JDSException("className is error!");
            }

            //    ViewInst viewInst = this.getViewInstById(viewInstId);
            // String domainId = viewInst.getDomainId();
            if (esdClassConfig == null) {
                Folder dsmFolder = viewClassConfigFolder.createChildFolder(domainId, JDSServer.getInstance().getAdminUser().getId());
                String folerPath = formartPackagePath(className);
                String simClassName = formartClassName(className);
                Folder packageFolder = getVfsClient().mkDir(dsmFolder.getPath() + folerPath);
                FileInfo fileInfo = this.getVfsClient().getFileByPath(packageFolder.getPath() + simClassName);
                if (fileInfo != null && fileInfo.getCurrentVersion().getLength() > 0) {
                    StringBuffer buffer = this.getVfsClient().readFileAsString(fileInfo.getPath(), VFSConstants.Default_Encoding);
                    esdClassConfig = JSON.parseObject(buffer.toString(), ViewEntityConfig.class);
                    viewClassConfigMap.put(uKey, esdClassConfig);
                }


                if (esdClassConfig == null) {
                    try {
                        AggEntityConfig aggEntityConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(className, false);
                        if (aggEntityConfig != null) {
                            esdClassConfig = new ViewEntityConfig(aggEntityConfig, domainId);
                            viewClassConfigMap.put(uKey, esdClassConfig);
                            viewEntityConfigTasks.add(new SaveEntityConfigTask<FileInfo>(esdClassConfig));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
            if (esdClassConfig != null) {
                esdClassConfig.setDomainId(domainId);
                esdClassConfig.setClassName(className);
            }

        }

        return esdClassConfig;
    }

    public ViewEntityConfig getViewEntityConfig(String className, String viewInstId) throws JDSException {
        if (className == null || className.equals("") || !MethodUtil.checkType(className)) {
            throw new JDSException("className is null!");
        }
        if (viewInstId == null || viewInstId.equals("")) {
            viewInstId = DSMFactory.getInstance().getDefaultProjectName();
        }

        String uKey = className + "[" + viewInstId + "]";
        ViewEntityConfig esdClassConfig = viewClassConfigMap.get(uKey);
        if (esdClassConfig == null) {
            esdClassConfig = this.createViewEntityConfig(className, viewInstId);
            if (esdClassConfig != null) {
                viewClassConfigMap.put(uKey, esdClassConfig);
            }
        }
        return esdClassConfig;
    }

    public ViewEntityConfig reSetViewEntityConfig(String className, String domainId) throws JDSException, ClassNotFoundException {
        return reSetViewEntityConfig(className, domainId, true);
    }

    public ViewEntityConfig reSetViewEntityConfig(String className, String domainId, boolean sync) throws JDSException, ClassNotFoundException {
        if (domainId == null || domainId.equals("")) {
            throw new JDSException("domainId is null!");
        }
        if (className == null || className.equals("")) {
            throw new JDSException("className is null!");
        }
        ViewEntityConfig viewEntityConfig = null;
        ESDClass esdClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(className, false);
        if (esdClass != null) {
            viewEntityConfig = new ViewEntityConfig(esdClass, domainId);
        }
        updateViewEntityConfig(viewEntityConfig, sync);
        return viewEntityConfig;
    }

    public void delViewEntity(String className, String viewInstId) throws JDSException {
        ViewInst viewInst = this.getViewInstById(viewInstId);
        ViewEntityConfig entityConfig = this.getViewEntityConfig(className, viewInstId);
        if (entityConfig != null) {
            aggregationManager.deleteApiClassConfig(entityConfig.getSourceConfig());
            aggregationManager.deleteApiClassConfig(entityConfig.getCurrConfig());
            ClassUtility.clear(className);
            BuildFactory.getInstance().getClassManager().clear(className);
            List<ViewEntityRef> entityRefs = this.getViewEntityRefByName(className, viewInstId);
            for (ViewEntityRef entityRef : entityRefs) {
                this.delViewEntityRef(entityRef.getRefId(), viewInstId);
            }


            Folder dsmFolder = viewClassConfigFolder.createChildFolder(viewInstId, JDSServer.getInstance().getAdminUser().getId());
            String folerPath = formartPackagePath(className);
            String simClassName = formartClassName(className);
            Folder packageFolder = getVfsClient().mkDir(dsmFolder.getPath() + folerPath);

            FileInfo fileInfo = this.getVfsClient().getFileByPath(packageFolder.getPath() + simClassName);
            if (fileInfo != null) {
                this.getVfsClient().deleteFile(fileInfo.getID());
            }
        }
        JavaSrcBean javaSrcBean = viewInst.getJavaSrcByClassName(className);
        if (javaSrcBean != null) {
            File file = javaSrcBean.getFile();
            try {
                FileUtility.deleteDirectory(file.getParentFile());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public Set<ViewEntityRef> getAllViewEntityRefs(String viewInstId) {
        Set<ViewEntityRef> tableRefs = new LinkedHashSet<>();
        ViewInst viewInst = this.getViewInstById(viewInstId);
        Map<String, ViewEntityRef> refMap = viewInst.getViewEntityRefMap();
        refMap.forEach((k, v) -> {
            tableRefs.add(v);
        });
        return tableRefs;
    }

    public Set<ViewEntityRef> getViewEntityRefs(String domainId) {
        Set<ViewEntityRef> dsmRefs = new LinkedHashSet<>();
        for (ViewEntityRef dsmRef : getAllViewEntityRefs(domainId)) {
            if (dsmRef.getDomainId() != null && dsmRef.getDomainId().equals("") && dsmRef.getDomainId().equals(domainId)) {
                dsmRefs.add(dsmRef);
            }
        }
        return dsmRefs;
    }

    public void delViewEntityRef(String refId, String viewInstId) throws JDSException {
        delViewEntityRef(refId, viewInstId, true);
    }

    public void delViewEntityRef(String refId, String viewInstId, boolean autocommit) throws JDSException {
        ViewInst viewInst = this.getViewInstById(viewInstId);
        Map<String, ViewEntityRef> refMap = viewInst.getViewEntityRefMap();
        if (refId != null) {
            String[] refIds = StringUtility.split(refId, ";");
            for (String id : refIds) {
                refMap.remove(id);
            }
        }
        if (autocommit) {
            this.updateViewInst(viewInst, true);
        }
    }


    public void updateViewEntityRef(ViewEntityRef ref) throws JDSException {
        this.updateViewEntityRef(ref, true);
    }


    public void updateViewEntityRef(ViewEntityRef ref, boolean autocommit) throws JDSException {
        String className = ref.getClassName();
        String otherClassName = ref.getOtherClassName();
        if (className == null || otherClassName == null) {
            throw new JDSException("格式错误");
        }
        ViewInst viewInst = this.getViewInstById(ref.getDomainId());
        Map<String, ViewEntityRef> refMap = viewInst.getViewEntityRefMap();
        String refId = ref.getRefId();
        if (refId == null || refId.equals("") || refId.equals(otherClassName + otherClassName)) {
            refMap.remove(refId);
            refId = UUID.randomUUID().toString();
        }

        ViewEntityRef oDSMRef = refMap.get(refId);
        if (oDSMRef == null) {
            oDSMRef = new ViewEntityRef();
            oDSMRef.setRefId(refId);
        }
        oDSMRef.setFkField(ref.getFkField());
        oDSMRef.setPkField(ref.getPkField());
        oDSMRef.setMainCaption(ref.getMainCaption());
        oDSMRef.setOtherCaption(ref.getOtherCaption());
        oDSMRef.setClassName(className);
        oDSMRef.setOtherClassName(otherClassName);
        oDSMRef.setRefBean(ref.getRefBean());
        refMap.put(refId, oDSMRef);
        //更新OTHERTABLE
        ViewEntityRef tDSMRef = this.getViewEntityRef(ref.getDomainId(), otherClassName, className);

        if (tDSMRef == null) {
            tDSMRef = new ViewEntityRef();
            tDSMRef.setRefId(UUID.randomUUID().toString());
        }

        CustomRefBean tref = tDSMRef.getRefBean();
        switch (ref.getRefBean().getRef()) {
            case O2M:
                tref.setRef(RefType.M2O);
                break;
            case M2O:
                tref.setRef(RefType.O2M);
                break;
        }
        tDSMRef.setDomainId(ref.getDomainId());

        tDSMRef.setFkField(ref.getPkField());
        tDSMRef.setPkField(ref.getFkField());
        tDSMRef.setClassName(otherClassName);
        tDSMRef.setOtherClassName(className);
        tDSMRef.setMainCaption(ref.getOtherCaption());
        tDSMRef.setOtherCaption(ref.getMainCaption());
        tDSMRef.setRefBean(tref);
        refMap.put(tDSMRef.getRefId(), tDSMRef);

        if (autocommit) {
            this.updateViewInst(viewInst, autocommit);
        }
    }


    private List<Callable<List<JavaSrcBean>>> genUpdateViewJavaTask(MethodConfig methodConfig, ViewInst viewInst, ChromeProxy chrome) throws JDSException {
        List<Callable<List<JavaSrcBean>>> tasks = new ArrayList<>();
        DomainInst domainInst = DSMFactory.getInstance().getAggregationManager().getDomainInstById(methodConfig.getDomainId(), viewInst.getProjectVersionName());
        JavaSrcBean javaSrcBean = viewInst.getJavaSrcBeanByMethod(methodConfig);
        String euClassName = methodConfig.getEUClassName();
        CustomViewBean formViewBean = methodConfig.getView();
        ESDClass viewClass = methodConfig.getViewClass();
        if (viewClass != null) {
            formViewBean.setCaption(viewClass.getDesc() == null ? viewClass.getCaption() : viewClass.getDesc());
        }

        if (javaSrcBean == null) {
            ViewInst defaultView = domainInst.getViewInst();
            javaSrcBean = defaultView.getJavaSrcBeanByMethod(methodConfig);
        }
        AggViewRoot viewRoot = new AggViewRoot(domainInst, euClassName, methodConfig.getModuleBean());
        euClassName = javaSrcBean.getClassName();
        GenCustomViewJava customViewJava = new GenCustomViewJava(viewRoot, formViewBean, euClassName, chrome);
        tasks.add(customViewJava);

        return tasks;
    }

    public List<Callable<List<JavaSrcBean>>> genMethodTask(MethodConfig methodConfig, ViewInst viewInst, ChromeProxy chrome) throws JDSException {
        List<Callable<List<JavaSrcBean>>> tasks = new ArrayList<>();
        List<MethodConfig> childMethods = methodConfig.getAllChildViewMethod();
        for (MethodConfig childMethod : childMethods) {
            List<Callable<List<JavaSrcBean>>> childTasks = genUpdateViewJavaTask(childMethod, viewInst, chrome);
            tasks.addAll(childTasks);
        }
        List<Callable<List<JavaSrcBean>>> methodTasks = genUpdateViewJavaTask(methodConfig, viewInst, chrome);
        tasks.addAll(methodTasks);
        return tasks;
    }


    public List<JavaSrcBean> buildView(MethodConfig methodConfig, ViewInst viewInst, ChromeProxy chrome) throws JDSException {
        List<Callable<List<JavaSrcBean>>> tasks = genMethodTask(methodConfig, viewInst, chrome);
        List<JavaSrcBean> javaSrcBeans = BuildFactory.getInstance().syncTasks(viewInst.getViewInstId(), tasks);

        return javaSrcBeans;
    }

    public List<JavaSrcBean> buildView(ApiClassConfig esdClassConfig, ViewInst viewInst, ChromeProxy chrome) throws JDSException {
        List<Callable<List<JavaSrcBean>>> tasks = new ArrayList<>();
        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
        List<MethodConfig> currViewConfigs = esdClassConfig.getAllViewMethods();
        for (MethodConfig childMethod : currViewConfigs) {
            List<Callable<List<JavaSrcBean>>> childtasks = genMethodTask(childMethod, viewInst, chrome);
            tasks.addAll(childtasks);
        }
        javaSrcBeans = BuildFactory.getInstance().syncTasks(viewInst.getViewInstId(), tasks);
        return javaSrcBeans;
    }

    public void reBuildView(DomainInst domainInst, ChromeProxy chrome, boolean compile) throws JDSException {
        List<ESDClass> viewEntityList = domainInst.getViewEntityList();
        reBuildESDClassView(domainInst, viewEntityList, chrome);
        ViewInst viewInst = this.createDefaultView(domainInst);
        if (compile) {
            DSMFactory.getInstance().compileViewInst(viewInst, chrome);
        }
        updateViewInst(viewInst, true);

    }

    public List<JavaSrcBean> reBuildESDClassView(DomainInst dsmInst, List<ESDClass> viewEntityList, ChromeProxy chrome) throws JDSException {
        List<Callable<List<JavaSrcBean>>> tasks = genESDClassTask(dsmInst, viewEntityList, chrome);
        List<JavaSrcBean> viewBeans = BuildFactory.getInstance().syncTasks(dsmInst.getDomainId(), tasks);
        return viewBeans;
    }

    public ViewEntityRef getViewEntityRefById(String refId, String viewInstId) {
        ViewInst domainInst = this.getViewInstById(viewInstId);
        Map<String, ViewEntityRef> refMap = domainInst.getViewEntityRefMap();
        return refMap.get(refId);
    }

    public ViewEntityRef getViewEntityRef(String viewInstId, String className, String otherClassName) throws JDSException {
        List<ViewEntityRef> dsmRefs = this.getViewEntityRefByName(className, viewInstId);
        for (ViewEntityRef dsmRef : dsmRefs) {
            if (dsmRef.getOtherClassName().toLowerCase().equals(otherClassName.toLowerCase())) {
                return dsmRef;
            }
        }
        return null;
    }

    public void clear() {
        viewClassConfigMap.clear();
    }

    public void deleteViewEntityConfig(String className, String domainId) {
        try {
            Folder dsmFolder = this.getVfsClient().mkDir(viewClassConfigFolder.getPath() + domainId);
            BuildFactory.getInstance().getClassManager().clear(className);
            FileInfo fileInfo = this.getVfsClient().getFileByPath(dsmFolder.getPath() + className);
            if (fileInfo != null) {
                this.getVfsClient().deleteFile(fileInfo.getID());
            }
            ESDClass esdClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(className, false);
            if (esdClass != null && esdClass.isProxy() && esdClass.getSourceClass() != null && !esdClass.getSourceClass().getClassName().equals(className)) {
                deleteViewEntityConfig(esdClass.getSourceClass().getClassName(), domainId);
            }

        } catch (JDSException e) {
            e.printStackTrace();
        }

    }

    private void deleteViewEntityClass(String className) {

        try {
            BuildFactory.getInstance().getClassManager().clear(className);
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }

    public Set<APIConfig> getAllAggAPIConfig() {
        Set<APIConfig> viewRoots = new LinkedHashSet<>();
        try {
            for (ESDClass esdClass : BuildFactory.getInstance().getClassManager().getAllAggAPI()) {
                try {
                    APIConfig apiConfig = APIConfigFactory.getInstance().getAPIConfig(esdClass.getName());
                    if (apiConfig != null) {
                        viewRoots.add(apiConfig);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return viewRoots;
    }


    public void commitProjectConfig(String projectVersionName) throws JDSException {
        DSMProjectConfig dsmProjectConfig = projectCacheManager.getProjectByName(projectVersionName).getDsmProjectConfig();
        projectCacheManager.updateDSMConfig(projectVersionName, dsmProjectConfig);
    }

    /**
     * 更新视图模板
     *
     * @param domainInst
     * @param serviceClassName
     * @param chrome
     * @throws JDSException
     */
    public List<JavaSrcBean> buildView(DomainInst domainInst, String serviceClassName, ChromeProxy chrome) throws JDSException {
        List<ESDClass> esdClassList = new ArrayList<>();
        try {
            ESDClass esdClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(serviceClassName, false);
            if (esdClass != null) {
                esdClassList.add(esdClass);
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }
        List<Callable<List<JavaSrcBean>>> tasks = genESDClassTask(domainInst, esdClassList, chrome);
        List<JavaSrcBean> viewBeans = BuildFactory.getInstance().syncTasks(domainInst.getDomainId(), tasks);
        ViewInst viewInst = this.createDefaultView(domainInst);

        for (JavaSrcBean srcBean : viewBeans) {
            viewInst.updateView(srcBean);
        }
        this.updateViewInst(viewInst, true);
        return viewBeans;

    }


    public List<Callable<List<JavaSrcBean>>> genESDClassTask(DomainInst domainInst, List<ESDClass> esdClassList, ChromeProxy chrome) throws JDSException {
        if (chrome == null) {
            chrome = getCurrChromeDriver();
        }
        List<Callable<List<JavaSrcBean>>> customViewTasks = new ArrayList<>();
        chrome.printLog("共发现" + esdClassList.size() + "视图", true);
        for (ESDClass esdClass : esdClassList) {
            ViewEntityConfig esdClassConfig = this.getViewEntityConfig(esdClass.getClassName(), esdClass.getDomainId());
            if (esdClassConfig == null) {
                esdClassConfig = this.createViewEntityConfig(esdClass.getClassName(), esdClass.getDomainId());
            }
            chrome.printLog("开始创建" + esdClass.getCtClass().getName() + "(" + esdClass.getDesc() + ")", true);
            List<Callable<List<JavaSrcBean>>> customViewEntityTasks = genEntityTask(domainInst, esdClassConfig, chrome);
            customViewTasks.addAll(customViewEntityTasks);
        }
        return customViewTasks;

    }


    private List<Callable<List<JavaSrcBean>>> genEntityTask(DomainInst domainInst, ViewEntityConfig esdClassConfig, ChromeProxy chrome) throws JDSException {
        List<Callable<List<JavaSrcBean>>> customViewTasks = new ArrayList<>();
        List<MethodConfig> methodAPIBeans = esdClassConfig.getSourceConfig().getAllViewMethods();
        for (MethodConfig methodConfig : methodAPIBeans) {
            List<Callable<List<JavaSrcBean>>> genViewJavaTask = genMethodTask(methodConfig, domainInst, chrome);
            customViewTasks.addAll(genViewJavaTask);
        }

        List<MethodConfig> methodOtherAPIBeans = esdClassConfig.getSourceConfig().getAllMethods();
        for (MethodConfig methodConfig : methodOtherAPIBeans) {
            if (methodConfig.getView() != null && methodConfig.getRefBean() != null && methodConfig.getRefBean().getRef() != null) {
                GenViewEntityRefJava refJava = new GenViewEntityRefJava(domainInst, methodConfig, chrome);
                customViewTasks.add(refJava);
            }
        }
        return customViewTasks;
    }

    public List<JavaSrcBean> reBuildMethodJava(MethodConfig methodConfig, DomainInst domainInst, ChromeProxy chrome) throws JDSException {
        List<Callable<List<JavaSrcBean>>> customViewTasks = genMethodTask(methodConfig, domainInst, chrome);
        return BuildFactory.getInstance().syncTasks(domainInst.getDomainId(), customViewTasks);
    }

    public List<Callable<List<JavaSrcBean>>> genMethodTask(MethodConfig methodConfig, DomainInst domainInst, ChromeProxy chrome) throws JDSException {
        List<Callable<List<JavaSrcBean>>> customViewTasks = new ArrayList<>();
        String euClassName = methodConfig.getEUClassName();
        if (euClassName.indexOf(".") == -1 || !methodConfig.getViewClass().isProxy()) {
            String packageName = domainInst.getEuPackage() + "." + methodConfig.getSourceClass().getTopSourceClass().getEntityClass().getName().toLowerCase();
            if (!euClassName.startsWith(packageName)) {
                euClassName = packageName + "." + euClassName;
            }
        }


        CustomViewBean formViewBean = methodConfig.getView();
        ESDClass viewClass = methodConfig.getViewClass();
        if (viewClass != null) {
            formViewBean.setCaption(viewClass.getDesc() == null ? viewClass.getCaption() : viewClass.getDesc());
        }
        if (euClassName != null && formViewBean != null) {
            AggViewRoot viewRoot = new AggViewRoot(domainInst, euClassName, methodConfig.getModuleBean());
            GenCustomViewJava viewJava = new GenCustomViewJava(viewRoot, formViewBean, euClassName, chrome);
            customViewTasks.add(viewJava);
        }
        return customViewTasks;
    }


    public void reBuildJavaSrcBean(CustomViewBean viewBean, JavaSrcBean javaSrcBean, String moduleName, ChromeProxy chrome) throws JDSException {
        UpdateCustomViewJava updateViewJava = new UpdateCustomViewJava(viewBean, javaSrcBean, moduleName, chrome);
        BuildFactory.getInstance().syncTasks(viewBean.getDomainId(), Arrays.asList(updateViewJava));
    }


    public GenCustomViewJava genCustomViewJava(AggViewRoot viewRoot, CustomViewBean viewBean, String className, boolean autocommit, ChromeProxy chrome) throws JDSException {
        DomainInst domainInst = (DomainInst) viewRoot.getDsmBean();
        ViewInst viewInst = this.createDefaultView(domainInst);
        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
        GenCustomViewJava customViewJava=null;
        boolean canCreate = true;
        if (viewBean instanceof CustomBlockFormViewBean) {
            if (viewBean.getAllFields().size() == 1 && (viewBean.getAllFields().get(0) instanceof FieldFormConfig)) {
                FieldFormConfig fieldFormConfig = (FieldFormConfig) viewBean.getAllFields().get(0);
                ComponentType componentType = fieldFormConfig.getComponentType();
                if (componentType != null && componentType.equals(ComponentType.BLOCK) && fieldFormConfig.getFieldname().endsWith(ModuleComponent.DefaultTopBoxfix)) {
                    canCreate = false;
                }
            }
        }
        if (canCreate) {
            customViewJava = new GenCustomViewJava(viewRoot, viewBean, className, chrome);
            List<JavaSrcBean> viewFileList = BuildFactory.getInstance().syncTasks(viewRoot.getDsmBean().getDsmId(), Arrays.asList(customViewJava));
            javaSrcBeans.addAll(viewFileList);
        }

        this.updateViewInst(viewInst, autocommit);
        return customViewJava;
    }

    public CustomViewBean getDefaultViewBean(ModuleComponent moduleComponent, String domainId) {
        ModuleViewType moduleViewType = moduleComponent.getModuleViewType();
        Class<CustomViewBean> viewBeanClass = null;
        try {
            viewBeanClass = ClassUtility.loadClass(moduleViewType.getBeanClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        CustomViewBean viewBean = null;
        if (moduleComponent.getMethodAPIBean() != null
                && moduleComponent.getMethodAPIBean().getView() != null
                && !moduleComponent.getMethodAPIBean().getModuleViewType().equals(moduleViewType)
                ) {
            viewBean = moduleComponent.getMethodAPIBean().getView();
            viewBean.updateModule(moduleComponent);
        } else {
            try {
                viewBean = (CustomViewBean) OgnlRuntime.callConstructor(JDSActionContext.getActionContext().getOgnlContext(), viewBeanClass.getName(), new Object[]{moduleComponent});
            } catch (OgnlException e) {
                e.printStackTrace();
            }
        }
        if (domainId == null) {
            try {
                domainId = DSMFactory.getInstance().getDefaultDomain(moduleComponent.getProjectName(), UserSpace.VIEW).getDomainId();
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        if (viewBean != null) {
            viewBean.setDomainId(domainId);
        }

        return viewBean;
    }

//    public List<JavaSrcBean> genCustomModuleJava(AggViewRoot viewRoot, ModuleComponent moduleComponent, String className, ChromeProxy chrome) throws JDSException {
//        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
//        CustomViewBean viewBean = this.getDefaultViewBean(moduleComponent, viewRoot.getDsmBean().getDsmId());
//        boolean canCreate = true;
//        if (viewBean instanceof CustomBlockFormViewBean) {
//            if (viewBean.getAllFields().size() == 1 && (viewBean.getAllFields().get(0) instanceof FieldFormConfig)) {
//                ComponentType componentType = ((FieldFormConfig) viewBean.getAllFields().get(0)).getComponentType();
//                if (componentType != null && componentType.equals(ComponentType.Block)) {
//                    canCreate = false;
//                }
//            }
//        }
//        if (canCreate) {
//            GenCustomViewJava customViewJava = new GenCustomViewJava(viewRoot, viewBean, className, chrome);
//            List<JavaSrcBean> viewFileList = BuildFactory.getInstance().syncTasks(viewRoot.getDsmBean().getDsmId(), Arrays.asList(customViewJava));
//
//            javaSrcBeans.addAll(viewFileList);
//        }
//        DSMFactory.getInstance().saveCustomViewBean(viewBean);
//        ViewInst viewInst = DSMFactory.getInstance().getViewManager().getDefaultViewInst(viewRoot.getDsmBean().getProjectVersionName());
//        this.updateViewInst(viewInst, true);
//
//        return javaSrcBeans;
//    }


    private List<JavaSrcBean> genChildAction(ViewInst viewInst, TreeListItem item, MenuBarBean menuBarBean, List<RequestParamBean> paramBeans, List<CustomEvent> events, String className) {
        List<TreeListItem> treeListItems = item.getSub();
        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
        if (treeListItems != null && treeListItems.size() > 0) {
            String packageName = className.substring(0, className.lastIndexOf("."));
            String name = className.substring(className.lastIndexOf(".") + 1);
            List<Class> bindClass = new ArrayList<>();
            Class[] clazzList = item.getBindClass();
            String bindClassName = null;
            Class clazz = null;
            if (clazzList != null && clazzList.length > 0) {
                bindClassName = clazzList[0].getName();
            }
            if (bindClassName == null) {
                bindClassName = packageName + "." + OODUtil.formatJavaName(name + item.getId(), true);
            }
            try {
                try {
                    clazz = ClassUtility.loadClass(bindClassName);
                } catch (ClassNotFoundException e) {
                    javaSrcBeans = DSMFactory.getInstance().getViewManager().genAction(viewInst, treeListItems, menuBarBean, paramBeans, events, bindClassName, null);
                }
                try {
                    clazz = ClassUtility.loadClass(bindClassName);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } catch (JDSException e) {
                e.printStackTrace();
            }

            if (clazz != null) {
                bindClass.add(clazz);
                // item.setBindClassName(clazz.getName());
            }
            item.setBindClass(bindClass.toArray(new Class[]{}));
        }

        return javaSrcBeans;

    }


    public List<JavaSrcBean> genAction(ViewInst viewInst, List<TreeListItem> items, MenuBarBean menuBarBean, List<RequestParamBean> paramBeans, List<CustomEvent> events, String className, ChromeProxy chrome) throws JDSException {
        Class clazz = null;
        try {
            clazz = ClassUtility.loadClass(className);
        } catch (ClassNotFoundException e) {
        }
        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
        if (clazz == null) {
            for (TreeListItem listItem : items) {
                genChildAction(viewInst, listItem, menuBarBean, paramBeans, events, className);
            }
            ActionViewRoot actionViewRoot = new ActionViewRoot(viewInst, items, menuBarBean, paramBeans, events, className);
            GenJava javaGen = GenJava.getInstance(viewInst.getProjectVersionName());
            List<JavaTemp> viewTemps = BuildFactory.getInstance().getTempManager().getCustomViewTemps(ViewType.NAVMENUBAR);
            for (JavaTemp javatemp : viewTemps) {
                if (javatemp.getRangeType() != null && javatemp.getRangeType().equals(RangeType.MODULEVIEW)) {
                    File file = javaGen.createJava(javatemp, actionViewRoot, chrome);
                    JavaSrcBean srcBean = BuildFactory.getInstance().getTempManager().genJavaSrc(file, viewInst, javatemp.getJavaTempId());
                    try {
                        clazz = javaGen.dynCompile(srcBean.getClassName(), srcBean.getContent());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    javaSrcBeans.add(srcBean);
                }
            }

        }

        return javaSrcBeans;
    }


    public List<JavaSrcBean> genMenuJava(ViewInst viewInst, MenuBarComponent menuBarComponent, String className, ChromeProxy chrome) throws JDSException {
        Class clazz = null;
        try {
            clazz = ClassUtility.loadClass(className);
        } catch (ClassNotFoundException e) {
        }

        ModuleComponent moduleComponent = menuBarComponent.getModuleComponent();
        ListFieldProperties listFieldProperties = menuBarComponent.getProperties();
        List<TreeListItem> items = listFieldProperties.getItems();

        List<Component> components = moduleComponent.findComponents(ComponentType.HIDDENINPUT, null);
        List<RequestParamBean> paramBeans = new ArrayList<>();
        for (Component component : components) {
            if (!Arrays.asList(DSMFactory.SkipParams).contains(component.getAlias())) {
                RequestParamBean requestParamBean = new RequestParamBean(component.getAlias(), String.class, null);
                paramBeans.add(requestParamBean);
            }
        }
        MenuBarBean menuBarBean = new MenuBarBean(menuBarComponent);
        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
        if (clazz == null) {
            for (TreeListItem listItem : items) {
                List<JavaSrcBean> childSrcBeans = genChildAction(viewInst, listItem, menuBarBean, paramBeans, new ArrayList<>(), className);
                javaSrcBeans.addAll(childSrcBeans);
            }
            ActionViewRoot dicViewRoot = new ActionViewRoot(viewInst, items, menuBarBean, paramBeans, new ArrayList<>(), className);
            GenJava javaGen = GenJava.getInstance(viewInst.getProjectVersionName());
            List<JavaTemp> viewTemps = BuildFactory.getInstance().getTempManager().getCustomViewTemps(ViewType.NAVMENUBAR);
            for (JavaTemp javatemp : viewTemps) {
                if (javatemp.getRangeType() != null && javatemp.getRangeType().equals(RangeType.MODULEVIEW)) {
                    File file = javaGen.createJava(javatemp, dicViewRoot, chrome);
                    JavaSrcBean srcBean = BuildFactory.getInstance().getTempManager().genJavaSrc(file, viewInst, javatemp.getJavaTempId());
                    try {
                        javaGen.dynCompile(srcBean.getClassName(), srcBean.getContent());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    javaSrcBeans.add(srcBean);
                }
            }
            for (JavaSrcBean srcBean : javaSrcBeans) {
                viewInst.updateView(srcBean);
            }
            this.updateViewInst(viewInst, true);
        }
        return javaSrcBeans;
    }


    public Class genDicJava(ViewInst viewInst, List<TabListItem> items, String moduleName, String className, ChromeProxy chrome) throws JDSException {

        Class clazz = null;
        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
        for (TabListItem listItem : items) {
            if (listItem instanceof TreeListItem) {
                genChildTree(viewInst, moduleName, (TreeListItem) listItem, className);
            }
        }
        DicViewRoot dicViewRoot = new DicViewRoot(viewInst, moduleName, items, className);
        GenJava javaGen = GenJava.getInstance(viewInst.getProjectVersionName());
        List<JavaTemp> viewTemps = BuildFactory.getInstance().getTempManager().getCustomViewTemps(ViewType.DIC);
        for (JavaTemp javatemp : viewTemps) {
            if (javatemp.getRangeType() != null && javatemp.getRangeType().equals(RangeType.MODULEVIEW)) {
                String packageName = className.substring(0, className.lastIndexOf("."));
                String simClass = className.substring(className.lastIndexOf(".") + 1);
                if (javatemp.getPackagePostfix() != null && !javatemp.getPackagePostfix().equals("..") && !javatemp.getPackagePostfix().equals("")) {
                    packageName = packageName + "." + javatemp.getPackagePostfix();
                }
                className = StringUtility.replace(javatemp.getNamePostfix(), "**", simClass);
                dicViewRoot.setClassName(className);
                dicViewRoot.setPackageName(packageName);
                File file = javaGen.createJava(javatemp, dicViewRoot, chrome);
                JavaSrcBean srcBean = BuildFactory.getInstance().getTempManager().genJavaSrc(file, viewInst, javatemp.getJavaTempId());
                try {
                    clazz = javaGen.dynCompile(srcBean.getClassName(), srcBean.getContent());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                javaSrcBeans.add(srcBean);
            }
        }
        for (JavaSrcBean srcBean : javaSrcBeans) {
            viewInst.updateView(srcBean);
        }
        this.updateViewInst(viewInst, true);

        return clazz;
    }

    private void genChildTree(ViewInst viewInst, String moduleName, TreeListItem item, String className) {
        List<TabListItem> treeListItems = item.getSub();
        if (treeListItems != null && treeListItems.size() > 0) {
            String packageName = className.substring(0, className.lastIndexOf("."));
            String name = className.substring(className.lastIndexOf(".") + 1);
            List<Class> bindClass = new ArrayList<>();
            Class clazz = null;
            Class[] clazzList = item.getBindClass();
            String bindClassName = null;
            if (clazzList != null && clazzList.length > 0) {
                bindClassName = clazzList[0].getName();
            }
            if (bindClassName == null) {
                bindClassName = packageName + "." + OODUtil.formatJavaName(name + item.getId(), true);
            }
            try {
                try {
                    clazz = ClassUtility.loadClass(bindClassName);
                } catch (ClassNotFoundException e) {
                    DSMFactory.getInstance().getViewManager().genDicJava(viewInst, treeListItems, moduleName, bindClassName, null);
                }
                try {
                    clazz = ClassUtility.loadClass(bindClassName);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } catch (JDSException e) {
                e.printStackTrace();
            }

            if (clazz != null) {
                bindClass.add(clazz);
            }
            item.setBindClass(bindClass.toArray(new Class[]{}));
        }

    }


    public List<JavaSrcBean> genViewsBySrc(DomainInst domainInst, List<JavaSrcBean> srcBeanList, ChromeProxy chrome) throws JDSException {
        List<ESDClass> esdClassList = new ArrayList<>();
        for (JavaSrcBean srcBean : srcBeanList) {
            ESDClass esdClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(srcBean.getClassName(), false);
            if (esdClass != null && !esdClassList.contains(esdClass)) {
                esdClassList.add(esdClass);
            }
        }
        List<Callable<List<JavaSrcBean>>> tasks = genESDClassTask(domainInst, esdClassList, chrome);
        List<JavaSrcBean> viewBeans = BuildFactory.getInstance().syncTasks(domainInst.getDomainId(), tasks);
        ViewInst viewInst = this.createDefaultView(domainInst);
        for (JavaSrcBean srcBean : viewBeans) {
            viewInst.updateView(srcBean);
        }
        this.updateViewInst(viewInst, true);

        return viewBeans;

    }


    public ViewInst updateViewInst(ViewInst dsmInst, boolean autocommit) throws JDSException {
        if (dsmInst != null) {
            if (dsmInst.getViewInstId() == null || dsmInst.getViewInstId().equals("")) {
                dsmInst.setViewInstId(UUID.randomUUID().toString());
            }
            DSMProjectConfig config = projectCacheManager.getProjectByName(dsmInst.getProjectVersionName()).getDsmProjectConfig();
            Map<String, ViewInst> dsmInstMap = config.getViewDomainInst();
            dsmInstMap.put(dsmInst.getViewInstId(), dsmInst);
            if (autocommit) {
                this.commitProjectConfig(dsmInst.getProjectVersionName());
            }
        }
        return dsmInst;
    }


    public List<ESDClass> findAPIConfig(String realClass, String domainId) throws ClassNotFoundException, JDSException {
        List<ESDClass> apis = new ArrayList<>();

        DomainInst domainInst = aggregationManager.getDomainInstById(domainId);
        List<ESDClass> esdClasses = domainInst.getAllProxyClass();
        for (ESDClass serviceClass : esdClasses) {
            if (serviceClass.getRootClass() != null && serviceClass.getRootClass().getClassName().equals(realClass) && !apis.contains(serviceClass)) {
                apis.add(serviceClass);
            }
        }
        return apis;
    }


    public String changeSourceClass(String realClass, String domainId) throws ClassNotFoundException, JDSException {
        List<ESDClass> apiConfigs = findAPIConfig(realClass, domainId);
        if (apiConfigs.size() > 0) {
            realClass = apiConfigs.get(0).getClassName();
        }
        return realClass;
    }


    private void deleteProjectViewInst(String projectName, String viewInstId, boolean autocommit) throws JDSException {
        if (projectName == null || projectName.equals("")) {
            List<INProject> projects = projectCacheManager.getAllProjectList();
            for (INProject inProject : projects) {
                DSMProjectConfig config = projectCacheManager.getProjectByName(inProject.getId()).getDsmProjectConfig();
                Map<String, ViewInst> dsmInstMap = config.getViewDomainInst();
                if (dsmInstMap.containsKey(viewInstId)) {
                    projectName = inProject.getProjectName();
                }
            }
        }

        if (projectName != null && !projectName.equals("")) {
            DSMProjectConfig config = projectCacheManager.getProjectByName(projectName).getDsmProjectConfig();
            Map<String, ViewInst> dsmInstMap = config.getViewDomainInst();
            if (viewInstId != null) {
                String[] refIds = StringUtility.split(viewInstId, ";");
                for (String id : refIds) {
                    dsmInstMap.remove(id);
                }
            }
            if (autocommit) {
                this.commitProjectConfig(projectName);
            }
        }
    }

//    private void clearViewConfig(String domainId) throws JDSException {
//        ViewInst viewInst = DSMFactory.getInstance().getViewManager().getViewInstById(viewInstId);
//        List<JavaSrcBean> srcBeans = viewInst.getJavaSrcBeans();
//
//        Set<ViewEntityRef> viewEntityRefs = this.getAllViewEntityRefs(viewInstId);
//        for (ViewEntityRef viewEntityRef : viewEntityRefs) {
//            this.delViewEntityRef(viewEntityRef.getRefId(), viewInstId);
//        }
//
//        Set<String> esdClassName = new HashSet<>();
//        for (JavaSrcBean javaSrcBean : srcBeans) {
//            this.delViewEntity(javaSrcBean.getClassName(), viewInstId);
//            this.deleteViewEntityConfig(javaSrcBean.getClassName(), viewInstId);
//            esdClassName.add(javaSrcBean.getClassName());
//        }
//        List<String> classNameList = getViewListByViewId();
//        for (String className : classNameList) {
//            if (!esdClassName.contains(className)) {
//                esdClassName.add(className);
//            }
//        }
//        aggregationManager.delAggEntity(viewInstId, esdClassName, viewInst.getProjectVersionName(), true);
//        aggregationManager.delAggRoot(viewInstId, esdClassName, viewInst.getProjectVersionName());
//        this.clearViewEntityConfig(viewInstId);
//
//    }
//
//    private List<String> getViewListByViewId() {
//        List<String> classNameList = new ArrayList<>();
//        viewClassConfigMap.forEach((k, v) -> {
//            if (!classNameList.contains(k)) {
//                classNameList.add(k);
//            }
//        });
//        return classNameList;
//    }


    public void commitTask() {
        try {
            updateApiEntityTask(viewEntityConfigTasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void updateApiEntityTask(List<SaveEntityConfigTask<FileInfo>> tasks) throws InterruptedException {
        RemoteConnectionManager.initConnection("ViewEntityConfig", tasks.size());
        List<Future<FileInfo>> futures = RemoteConnectionManager.getStaticConntction("ViewEntityConfig").invokeAll(tasks);
        for (Future<FileInfo> future : futures) {
            try {
                future.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        this.viewEntityConfigTasks.clear();
    }


    private void deleteProjectViewInst(String projectId, String dsmInstId) throws JDSException {
        deleteProjectViewInst(projectId, dsmInstId, true);
    }

    public void deleteViewInst(String viewInstId, boolean autocommit) throws JDSException {
        if (viewInstId != null) {
            String[] refIds = StringUtility.split(viewInstId, ";");
            for (String id : refIds) {
                ViewInst domainInst = this.getViewInstById(id);
                if (domainInst != null) {
                    deleteProjectViewInst(domainInst.getProjectVersionName(), id, autocommit);
                }

            }
        }
    }


    /**
     * 根据表信息创建java文件
     *
     * @throws IOException
     */
    public List<JavaSrcBean> genViewRootJava(ViewInst viewInst, List<ESDClass> esdClassList, Set<String> tempIds, List<JavaSrcBean> srcFiles, ChromeProxy chrome) throws JDSException {
        ViewRoot allRoot = new ViewRoot(viewInst, esdClassList, srcFiles);
        GenJava javaGen = GenJava.getInstance(viewInst.getProjectVersionName());
        if (tempIds == null) {
            tempIds = viewInst.getJavaTempIds();
        }
        for (String javaTempId : tempIds) {
            JavaTemp javatemp = BuildFactory.getInstance().getTempManager().getJavaTempById(javaTempId);
            if (javatemp != null && javatemp.getRangeType().equals(RangeType.MODULE)) {
                String packageName = viewInst.getPackageName();

                if (javatemp.getPackagePostfix() != null && !javatemp.getPackagePostfix().equals("")) {
                    packageName = packageName + "." + javatemp.getPackagePostfix();
                }
                String className = StringUtility.replace(javatemp.getNamePostfix(), "**", OODUtil.formatJavaName(viewInst.getSpace(), true));
                allRoot.setPackageName(packageName);
                allRoot.setClassName(className);
                log.debug("start create [" + className + "]");
                File file = javaGen.createJava(javatemp, allRoot, chrome);
                JavaSrcBean srcBean = BuildFactory.getInstance().getTempManager().genJavaSrc(file, viewInst, javaTempId);
                srcFiles.add(srcBean);
            }
        }
        return srcFiles;
    }

//    public ViewInst getDefaultViewInst(String projectVersionName) throws JDSException {
//        INProject inProject = projectCacheManager.getProjectByName(projectVersionName);
//
//        ViewInst viewInst = this.getViewInstById(inProject.getProjectName());
//        if (viewInst == null) {
//            List<ViewInst> viewInstList = this.getViewList(inProject.getProjectName());
//            if (viewInstList.size() > 0) {
//                viewInst = viewInstList.get(0);
//            }
//        }
//        return viewInst;
//    }


    public ViewInst createDefaultView(DomainInst domainInst) throws JDSException {
        ViewInst viewInst = this.getViewInstById(domainInst.getDomainId(), domainInst.getProjectVersionName());
        if (viewInst == null) {
            Set<String> tempIds = BuildFactory.getInstance().getTempManager().getDSMBeanTempId(DSMTempType.custom, DSMType.VIEW);
            viewInst = new ViewInst(domainInst);
            viewInst.setJavaTempIds(tempIds);
            viewInst.setViewInstId(domainInst.getDomainId());
            updateViewInst(viewInst, true);
        }
        return viewInst;
    }

    private ViewInst getProjectViewInstById(String projectId, String viewInstId) {
        INProject project = projectCacheManager.getProjectById(projectId);
        DSMProjectConfig config = project.getDsmProjectConfig();
        Map<String, ViewInst> dsmInstMap = config.getViewDomainInst();
        return dsmInstMap.get(viewInstId);
    }

    public ViewInst getViewInstById(String viewInstId) {
        return getViewInstById(viewInstId, null);
    }

    public ViewInst getViewInstById(String viewInstId, String projectName) {
        ViewInst viewInst = null;
        if (viewInstId != null) {
            try {
                if (projectName == null) {
                    projectName = DSMFactory.getInstance().getDefaultProjectName();
                }
                INProject project = projectCacheManager.getProjectByName(projectName);
                if (project != null) {
                    viewInst = this.getProjectViewInstById(project.getId(), viewInstId);
                }
            } catch (JDSException e) {
                //e.printStackTrace();
            }
            if (viewInst == null) {
                List<INProject> projects = projectCacheManager.getAllProjectList();
                for (INProject inProject : projects) {
                    viewInst = this.getProjectViewInstById(inProject.getId(), viewInstId);
                    if (viewInst != null) {
                        return viewInst;
                    }
                }
            }
        }
        return viewInst;
    }


    public List<ViewInst> getViewList(String projectVersionName) throws JDSException {
        List<ViewInst> tempInsts = new ArrayList<>();
        DSMProjectConfig config = projectCacheManager.getProjectByName(projectVersionName).getDsmProjectConfig();
        Map<String, ViewInst> dsmInstMap = config.getViewDomainInst();
        dsmInstMap.forEach((k, v) -> {
            v.setViewInstId(k);
            tempInsts.add(v);
        });
        return tempInsts;
    }

    public List<ViewInst> getAllUserViewList(String projectVersionName) throws JDSException {
        List<ViewInst> viewInsts = new ArrayList<>();
        List<DomainInst> domainInsts = aggregationManager.getAllUserDomainInst(projectVersionName);
        for (DomainInst domainInst : domainInsts) {
            if (domainInst.getUserSpace().equals(UserSpace.VIEW)) {
                viewInsts.add(domainInst.getViewInst());
            }
        }

        for (DomainInst domainInst : domainInsts) {
            if (!domainInst.getUserSpace().equals(UserSpace.VIEW) && !domainInst.getUserSpace().equals(UserSpace.SYS)) {
                viewInsts.add(domainInst.getViewInst());
            }
        }
        return viewInsts;
    }


    private String formartPackagePath(String className) {
        String packageName = StringUtility.replace(className, ".", "/");
        if (packageName.indexOf("/") > -1) {
            packageName = packageName.substring(0, packageName.lastIndexOf("/"));
        }
        return packageName;
    }

    public Folder getViewClassConfigFolder() {
        return viewClassConfigFolder;
    }

    public void setViewClassConfigFolder(Folder viewClassConfigFolder) {
        this.viewClassConfigFolder = viewClassConfigFolder;
    }

    private String formartClassName(String className) {
        className = StringUtility.replace(className, ".", "/");
        if (className.indexOf("/") > -1) {
            className = className.substring(className.lastIndexOf("/") + 1);
        }
        return className;
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
