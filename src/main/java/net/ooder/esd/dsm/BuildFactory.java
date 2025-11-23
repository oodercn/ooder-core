package net.ooder.esd.dsm;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import net.ooder.annotation.Aggregation;
import net.ooder.annotation.EsbBeanAnnotation;
import net.ooder.common.JDSConstants;
import net.ooder.common.JDSException;
import net.ooder.common.logging.ChromeProxy;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.common.util.ClassUtility;
import net.ooder.context.JDSActionContext;
import net.ooder.context.JDSContext;
import net.ooder.esb.config.manager.EsbBeanFactory;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.CustomViewBean;
import net.ooder.esd.bean.view.*;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.custom.ESDClassManager;
import net.ooder.esd.dsm.aggregation.context.AggViewRoot;
import net.ooder.esd.dsm.enums.DSMType;
import net.ooder.esd.dsm.gen.GenJava;
import net.ooder.esd.dsm.java.*;
import net.ooder.esd.dsm.temp.JavaTemp;
import net.ooder.esd.dsm.temp.JavaTempManager;
import net.ooder.esd.dsm.view.context.*;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.MySpace;
import net.ooder.esd.manager.plugins.api.APIFactory;
import net.ooder.server.context.MinServerActionContextImpl;
import net.ooder.web.APIConfigFactory;
import net.ooder.web.RemoteConnectionManager;
import net.ooder.web.util.AnnotationUtil;
import net.ooder.web.util.MethodUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class BuildFactory {
    private static final String THREAD_LOCK = "Thread Lock";

    protected Log log = LogFactory.getLog(JDSConstants.CONFIG_KEY, BuildFactory.class);

    private final MySpace space;

    private static Map<String, BuildFactory> managerMap = new HashMap<String, BuildFactory>();

    private Map<CustomViewBean, AggRootBuild> aggRootBuildMap = new HashMap<>();

    private Map<String, JavaGenSource> javaGenSourceMap = new HashMap<>();

    private Map<String, AggRootBuild> classRootBuildMap = new HashMap<>();

    private final ESDClassManager classManager;

    private final JavaTempManager tempManager;


    public ESDClassManager getClassManager() {
        return classManager;
    }


    public static BuildFactory getInstance() throws JDSException {
        return getInstance(ESDFacrory.getAdminESDClient().getSpace());
    }


    public static BuildFactory getInstance(MySpace space) {

        String path = space.getPath();
        BuildFactory manager = managerMap.get(path);
        if (manager == null) {
            synchronized (THREAD_LOCK) {
                if (manager == null) {
                    manager = new BuildFactory(space);
                    managerMap.put(path, manager);
                }
            }
        }
        return manager;
    }

    public void clear() {
        aggRootBuildMap.clear();
        classRootBuildMap.clear();
        classManager.clear();

    }


    BuildFactory(MySpace space) {
        this.space = space;
        this.classManager = ESDClassManager.getInstance(space);
        this.tempManager = JavaTempManager.getInstance(space);
    }


    public void reload() {
        classManager.reload();
    }


    public void dyReload() {
        RemoteConnectionManager.getConntctionService(DSMFactory.DefaultDsmName).execute(new Runnable() {
            @Override
            public void run() {
                try {
                    ESDFacrory.getInstance().dyReload(null);
                } catch (JDSException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public List<ESDClass> syncEsdClassTasks(String taskId, Set<String> esdClassNameSet) {
        List<ESDClass> srcBeans = new ArrayList<>();
        try {
            if (taskId == null) {
                taskId = DSMFactory.DefaultDsmName;
            }
            int pageSize = 5;
            //批量装载数据
            if (esdClassNameSet.size() > 0) {
                Integer start = 0;
                int size = esdClassNameSet.size();
                String[] delfileInfoIds = esdClassNameSet.toArray(new String[esdClassNameSet.size()]);
                int page = 0;
                while (page * pageSize < size) {
                    page++;
                }
                List<SyncLoadClass<List<ESDClass>>> syncLoadClassTasks = new ArrayList<>();
                for (int k = 0; k < page; k++) {
                    int end = start + pageSize;
                    if (end >= size) {
                        end = size;
                    }
                    String[] loadFileIds = Arrays.copyOfRange(delfileInfoIds, start, start + pageSize);
                    SyncLoadClass syncLoadClass = new SyncLoadClass(loadFileIds);
                    syncLoadClassTasks.add(syncLoadClass);
                    start = end;
                }
                RemoteConnectionManager.initConnection(taskId, syncLoadClassTasks.size());

                ExecutorService executorService = RemoteConnectionManager.createConntctionService(taskId);
                List<Future<List<ESDClass>>> futures = executorService.invokeAll(syncLoadClassTasks);
                for (Future<List<ESDClass>> resultFuture : futures) {
                    try {
                        List<ESDClass> result = resultFuture.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                executorService.shutdown();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return srcBeans;
    }

    class SyncLoadClass<T extends List<ESDClass>> implements Callable<List<ESDClass>> {
        protected MinServerActionContextImpl autoruncontext;
        Set<String> classNameList = new HashSet();

        public SyncLoadClass(String[] esdClassNameList) {
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
        public List<ESDClass> call() throws Exception {
            JDSActionContext.setContext(autoruncontext);
            List<ESDClass> esdClassList = new ArrayList<>();
            for (String esdClassName : classNameList) {
                Long start = System.currentTimeMillis();
                ESDClass esdClass = classManager.getAggEntityByName(esdClassName, false);
                if (esdClass != null) {
                    esdClassList.add(esdClass);
                    if (System.currentTimeMillis() - start > 50) {
                        log.warn("end load " + esdClassName + "timeout times=" + (System.currentTimeMillis() - start));
                    }
                }
            }
            return esdClassList;
        }


    }


    public List<JavaSrcBean> syncTasks(String taskId, List<? extends Callable<List<JavaSrcBean>>> buildTasks) {
        List<JavaSrcBean> srcBeans = new ArrayList<>();
        try {
            if (taskId == null) {
                taskId = DSMFactory.DefaultDsmName;
            }
            ExecutorService executorService = RemoteConnectionManager.createConntctionService(taskId);
            List<Future<List<JavaSrcBean>>> projectFutures = executorService.invokeAll(buildTasks);
            for (Future<List<JavaSrcBean>> resultFuture : projectFutures) {
                try {
                    List<JavaSrcBean> result = resultFuture.get();
                    srcBeans.addAll(result);
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
        return srcBeans;
    }


    public void compileJavaSrc(List<JavaSrcBean> srcBeanList, String projectName, ChromeProxy chrome) {
        GenJava javaGen = GenJava.getInstance(projectName);
        try {
            javaGen.compileJavaSrc(srcBeanList, chrome);
        } catch (JDSException e) {
            e.printStackTrace();
        }

        Set<Class<?>> classSet = new HashSet<>();
        //dump 本地文件
        // javaGen.jarFiles(srcBeanList, dsmInst.getName() + "_" + dsmInst.getDsmType().getType(), dsmInst);
        //更新服务注册
        for (JavaSrcBean srcBean : srcBeanList) {
            Class clazz = null;
            try {
                clazz = ClassUtility.loadClass(srcBean.getClassName());
                classSet.add(clazz);
                registerClass(clazz);
            } catch (ClassNotFoundException e) {
                log.warn(e.getMessage());
                //e.printStackTrace();
            }
        }
        APIConfigFactory.getInstance().dyReload(classSet);
    }

    public void registerClass(Class clazz) {
        if (clazz != null) {
            if (AnnotationUtil.getClassAnnotation(clazz, Aggregation.class) != null
                    || AnnotationUtil.getClassAnnotation(clazz, EsbBeanAnnotation.class) != null) {
                EsbBeanFactory.getInstance().registerService(clazz);
            }
            String className = clazz.getName();
            try {
                classManager.clear(className);
                APIConfigFactory.getInstance().reload(className);
                APIFactory.getInstance().loadApiConfig(className);
                ClassUtility.loadClass(className);

            } catch (Exception e) {
            }
        }
    }

    public JavaGenSource createSource(String className, JavaRoot javaRoot, JavaTemp javatemp, JavaSrcBean srcBean) {
        JavaGenSource source = new JavaGenSource(className, javaRoot, javatemp, srcBean);
        javaGenSourceMap.put(className, source);
        return source;
    }

    public JavaGenSource getJavaGenSource(String className) throws JDSException {
        JavaGenSource source = javaGenSourceMap.get(className);
        return source;
    }


    public JavaGenSource updateSource(JavaGenSource source) {
        javaGenSourceMap.put(source.getClassName(), source);
        return source;
    }


    public Map<String, JavaGenSource> getJavaGenSourceMap() {
        return javaGenSourceMap;
    }

    public void setJavaGenSourceMap(Map<String, JavaGenSource> javaGenSourceMap) {
        this.javaGenSourceMap = javaGenSourceMap;
    }

    public JavaPackage findJavaPackage(String projectName, String packageName) {
        JavaPackage javaPackage = null;
        try {
            javaPackage = JavaPackageManager.getInstance(projectName).findJavaPackageByName(packageName);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return javaPackage;
    }

    public AggRootBuild getAggRootBuild(String euClassName) throws JDSException {
        AggRootBuild aggRootBuild = classRootBuildMap.get(euClassName);
        return aggRootBuild;
    }

    public void clear(String euClassName) throws JDSException {
        AggRootBuild aggRootBuild = classRootBuildMap.remove(euClassName);
    }

    public AggRootBuild getAggRootBuild(CustomViewBean customViewBean, String className, String projectName) throws JDSException {
        if (projectName == null) {
            projectName = DSMFactory.getInstance().getDefaultProjectName();
        }
        AggRootBuild aggRootBuild = classRootBuildMap.get(className);
        if (aggRootBuild == null) {
            aggRootBuild = aggRootBuildMap.get(customViewBean);
            if (aggRootBuild == null) {
                aggRootBuild = new AggRootBuild(customViewBean, className, projectName);
                aggRootBuildMap.put(customViewBean, aggRootBuild);
            } else {
                aggRootBuild.reSetViewBean(customViewBean);
            }
            classRootBuildMap.put(className, aggRootBuild);
        } else {
            aggRootBuild.reSetViewBean(customViewBean);
        }
        return aggRootBuild;
    }


    public JavaSrcBean copy(JavaSrcBean javaSrcBean, String packageName) throws IOException, JDSException {
        JavaDeclaration javaDeclaration = javaSrcBean.getDeclaration();
        javaDeclaration.getJavaPackage().setName(packageName);
        JavaSrcBean tSrcBean = null;
        try {
            DSMInst dsmInst = DSMFactory.getInstance().getDSMInst(javaSrcBean.getDsmId(), javaSrcBean.getDsmType());
            JavaPackage tPackage = dsmInst.getPackageByName(packageName);
            tSrcBean = tPackage.update(javaSrcBean.getFile().getName(), javaDeclaration.toString());
            tSrcBean.setJavaTempId(javaSrcBean.getJavaTempId());
            //tPackage.listAllFile().add(tSrcBean);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return tSrcBean;

    }

    public JavaSrcBean createJava(String content, DSMType dsmType, String dsmId, String className, String packageName) throws JDSException, IOException {
        DSMInst dsmInst = DSMFactory.getInstance().getDSMInst(dsmId, dsmType);
        JavaPackage tPackage = dsmInst.getPackageByName(packageName);
        JavaSrcBean javaSrcBean = tPackage.update(className, content);

        return javaSrcBean;
    }

    public void updateJava(String content, DSMType dsmType, String dsmId, String javaTempId, String filePath) throws JDSException, IOException {
        File desFile = new File(filePath);
        DSMInst dsmInst = DSMFactory.getInstance().getDSMInst(dsmId, dsmType);
        JavaSrcBean srcBean = BuildFactory.getInstance().getTempManager().genJavaSrc(desFile, dsmInst, javaTempId);
        srcBean.getJavaPackage().update(srcBean.getName(), content);

        JavaDeclaration javaDeclaration = srcBean.getDeclaration();
        TypeDeclaration typeDeclaration = javaDeclaration.getTypeDeclaration();
        if (!typeDeclaration.getName().equals(srcBean.getName()) || !javaDeclaration.getJavaPackage().getName().equals(srcBean.getPackageName())) {
            javaDeclaration.getJavaPackage().setName(srcBean.getPackageName());
            List<ConstructorDeclaration> constructorDeclarations = typeDeclaration.getConstructors();
            for (ConstructorDeclaration md : constructorDeclarations) {
                md.setName(srcBean.getName());
            }
            typeDeclaration.setName(srcBean.getName());
            srcBean = javaDeclaration.update();
        }

        Class fileClass = srcBean.dynCompile(true);
        registerClass(fileClass);

    }

    public MySpace getSpace() {
        return space;
    }

    public JavaTempManager getTempManager() {
        return tempManager;
    }

    public JavaSrcBean reNameJava(JavaSrcBean javaSrcBean, String newname) throws IOException {
        JavaDeclaration javaDeclaration = javaSrcBean.getDeclaration();
        List<ConstructorDeclaration> constructorDeclarations = javaDeclaration.getConstructors();
        for (ConstructorDeclaration md : constructorDeclarations) {
            md.setName(newname);
        }
        javaDeclaration.getTypeDeclaration().setName(newname);
        JavaSrcBean tSrcBean = javaDeclaration.update();
        return tSrcBean;

    }


    public JavaRoot buildJavaRoot(AggViewRoot viewRoot, CustomViewBean viewBean, String moduleName, String className) {
        ModuleViewType moduleViewType = viewBean.getModuleViewType();
        JavaRoot javaRoot = null;

        if (moduleName.equals(viewRoot.getDsmBean().getSpace())) {
            moduleName = className.substring(className.lastIndexOf(".") + 1).toLowerCase();
        } else if (moduleName.startsWith(viewRoot.getDsmBean().getSpace() + ".")) {
            moduleName = moduleName.substring((viewRoot.getDsmBean().getSpace() + ".").length());
        }

        switch (moduleViewType) {
            case GRIDCONFIG:
                javaRoot = new GridViewRoot(viewRoot, (CustomGridViewBean) viewBean, moduleName, className);
                break;
            case NAVMENUBARCONFIG:
                javaRoot = new MenubarViewRoot(viewRoot, (NavMenuBarViewBean) viewBean, moduleName, className);
                break;
            case DIVCONFIG:
                javaRoot = new DivViewRoot(viewRoot, (CustomDivFormViewBean) viewBean, moduleName, className);
                break;
            case MGRIDCONFIG:
                javaRoot = new GridViewRoot(viewRoot, (CustomGridViewBean) viewBean, moduleName, className);
                break;
            case BLOCKCONFIG:
                javaRoot = new BlockViewRoot(viewRoot, (CustomBlockFormViewBean) viewBean, moduleName, className);
                break;
            case PANELCONFIG:
                javaRoot = new PanelViewRoot(viewRoot, (CustomPanelFormViewBean) viewBean, moduleName, className);
                break;
            case FORMCONFIG:
                javaRoot = new FormViewRoot(viewRoot, (CustomFormViewBean) viewBean, null, moduleName, className);
                break;
            case MFORMCONFIG:
                javaRoot = new FormViewRoot(viewRoot, (CustomFormViewBean) viewBean, null, moduleName, className);
                break;
            case CONTENTBLOCKCONFIG:
                javaRoot = new ContentBlockViewRoot(viewRoot, (CustomContentBlockViewBean) viewBean, moduleName, className);
                break;
            case TREECONFIG:
                javaRoot = new TreeViewRoot(viewRoot, (CustomTreeViewBean) viewBean, moduleName, className);
                break;
            case POPTREECONFIG:
                javaRoot = new TreeViewRoot(viewRoot, (PopTreeViewBean) viewBean, moduleName, className);
                break;
            case GALLERYCONFIG:
                javaRoot = new GalleryViewRoot(viewRoot, (CustomGalleryViewBean) viewBean, moduleName, className);
                break;
            case SVGPAPERCONFIG:
                javaRoot = new SVGViewRoot(viewRoot, (CustomSVGPaperViewBean) viewBean, moduleName, className);
                break;
            case TITLEBLOCKCONFIG:
                javaRoot = new TitleBlockViewRoot(viewRoot, (CustomTitleBlockViewBean) viewBean, moduleName, className);
                break;
            case NAVBUTTONVIEWSCONFIG:
                javaRoot = new ButtonViewsViewRoot(viewRoot, (CustomButtonViewsViewBean) viewBean, moduleName, className);
                break;
            case NAVSTACKSCONFIG:
                javaRoot = new StacksViewRoot(viewRoot, (StacksViewBean) viewBean, moduleName, className);
                break;
            case NAVGALLERYCONFIG:
                javaRoot = new NavGalleryViewRoot(viewRoot, (NavGalleryComboViewBean) viewBean, moduleName, className);
                break;
            case NAVGROUPCONFIG:
                javaRoot = new NavGroupViewRoot(viewRoot, (NavGroupViewBean) viewBean, moduleName, className);
                break;
            case NAVTABSCONFIG:
                javaRoot = new TabsViewRoot(viewRoot, (TabsViewBean) viewBean, moduleName, className);
                break;
            case BUTTONLAYOUTCONFIG:
                javaRoot = new ButtonLayoutViewRoot(viewRoot, (CustomButtonLayoutViewBean) viewBean, moduleName, className);
                break;
            case NAVBUTTONLAYOUTCONFIG:
                javaRoot = new ButtonLayoutViewRoot(viewRoot, (CustomButtonLayoutViewBean) viewBean, moduleName, className);
                break;
            case NAVFOLDINGTABSCONFIG:
                javaRoot = new FoldingTabsViewRoot(viewRoot, (NavFoldingTabsViewBean) viewBean, moduleName, className);
                break;

            case LAYOUTCONFIG:
                javaRoot = new LayoutViewRoot(viewRoot, (CustomLayoutViewBean) viewBean, moduleName, className);
                break;
            case NAVTREECONFIG:
                javaRoot = new NavTreeViewRoot(viewRoot, (NavTreeComboViewBean) viewBean, moduleName, className);
                break;
            case CHARTCONFIG:
                javaRoot = new FChartRoot(viewRoot, (CustomFChartViewBean) viewBean, moduleName, className);
                break;
            case OPINIONCONFIG:
                javaRoot = new OpinionViewRoot(viewRoot, (CustomOpinionViewBean) viewBean, moduleName, className);
                break;
            case ECHARTCONFIG:
                javaRoot = new EChartRoot(viewRoot, (CustomEChartViewBean) viewBean, moduleName, className);
                break;

        }

        return javaRoot;
    }

}
