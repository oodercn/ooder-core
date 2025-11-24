package net.ooder.esd.dsm.java;

import net.ooder.annotation.AggregationType;
import net.ooder.annotation.UserSpace;
import net.ooder.common.JDSException;
import net.ooder.common.logging.ChromeProxy;
import net.ooder.common.logging.LogSetpLog;
import net.ooder.common.util.ClassUtility;
import net.ooder.context.JDSActionContext;
import net.ooder.esb.config.manager.EsbBeanFactory;
import net.ooder.esb.config.manager.ExpressionTempBean;
import net.ooder.esd.bean.CustomViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.CustomModuleBean;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.JavaRoot;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.dsm.aggregation.AggregationManager;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.aggregation.context.AggViewRoot;
import net.ooder.esd.dsm.enums.RepositoryType;
import net.ooder.esd.dsm.gen.GenJava;
import net.ooder.esd.dsm.gen.agg.GenAggCustomJava;
import net.ooder.esd.dsm.gen.agg.GenAggCustomViewJava;
import net.ooder.esd.dsm.gen.repository.GenRepositoryViewJava;
import net.ooder.esd.dsm.gen.view.GenCustomViewJava;
import net.ooder.esd.dsm.repository.RepositoryInst;
import net.ooder.esd.dsm.temp.JavaTemp;
import net.ooder.esd.dsm.view.ViewInst;
import net.ooder.esd.dsm.view.ViewManager;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.web.AggregationBean;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AggRootBuild {


    GenCustomViewJava viewTask;

    GenRepositoryViewJava voRepositoryTask;

    GenRepositoryViewJava serviceRepositoryTask;

    GenAggCustomViewJava rootTask;

    AggViewRoot viewRoot;

    List<JavaGenSource> javaViewSource = new ArrayList<>();

    List<JavaGenSource> repositorySource = new ArrayList<>();

    List<JavaGenSource> childBeans;

    List<JavaGenSource> aggBeans;

    List<JavaGenSource> serviceBeans;

    List<JavaGenSource> aggServiceRootBean = new ArrayList<>();

    String packageName;

    String moduleName;

    String euClassName;

    String domainId;

    CustomViewBean customViewBean;

    CustomModuleBean customModuleBean;

    AggViewRoot mainView;

    DomainInst domainInst;

    ViewInst viewInst;

    RepositoryInst repositoryInst;

    GenJava javaGen;

    ChromeProxy chrome;


    public AggRootBuild() {

    }

    public AggRootBuild(CustomViewBean customViewBean, String euClassName, String projectName) throws JDSException {
        this.chrome = this.getCurrChromeDriver();
        this.euClassName = euClassName;
        if (domainId != null) {
            this.domainInst = DSMFactory.getInstance().getDomainInstById(domainId);
        }
        if (euClassName.indexOf(".") > -1) {
            this.packageName = euClassName.substring(0, euClassName.lastIndexOf("."));
        } else if (domainInst != null) {
            this.packageName = domainInst.getEuPackage();
        }

        this.moduleName = packageName;
        this.customViewBean = customViewBean;
        this.domainId = customViewBean.getDomainId();
        MethodConfig methodConfig = customViewBean.getMethodConfig();
        if (methodConfig != null && methodConfig.getSourceClass() != null) {
            AggregationBean aggregationBean = customViewBean.getMethodConfig().getSourceClass().getAggregationBean();
            if (aggregationBean != null && aggregationBean.getUserSpace() != null && aggregationBean.getUserSpace().size() > 0) {
                UserSpace userSpace = aggregationBean.getUserSpace().iterator().next();
                this.domainInst = DSMFactory.getInstance().getDefaultDomain(projectName, userSpace);
                domainId = domainInst.getDomainId();
            }
        }

        if (domainInst == null) {
            domainInst = DSMFactory.getInstance().getDefaultDomain(projectName, UserSpace.VIEW);
            domainId = domainInst.getDomainId();
        }

        if (domainInst != null) {
            String basePackage = domainInst.getEuPackage();
            if (moduleName.equals(basePackage)) {
                moduleName = euClassName.substring(euClassName.lastIndexOf(".") + 1).toLowerCase();
            } else if (moduleName.startsWith(basePackage + ".")) {
                moduleName = moduleName.substring((basePackage + ".").length()).toLowerCase();
            }
        }

        this.viewInst = domainInst.getViewInst();
        this.repositoryInst = domainInst.getRepositoryInst();
        this.javaGen = GenJava.getInstance(domainInst.getProjectVersionName());
        this.serviceBeans = new ArrayList<>();

        this.reSetViewBean(customViewBean);
    }


    public void reBuildModule() throws JDSException {
        //1.1创建视图层
        viewTask = genCustomViewJava();
        this.javaViewSource = viewTask.getSourceList();
        //2.1创建资源层接口V
        this.repositorySource = initRepositoryViewJava(true);
        //3.1预编译一次
        this.javaGen.dynCompile(getModuleJavaSrc());

    }

    public void buildRepository() throws JDSException {
        //2.1创建资源层接口V
        this.repositorySource = initRepositoryViewJava(true);
        //3.1预编译一次
        this.javaGen.dynCompile(getModuleJavaSrc());

    }

    public void genBuildModule() throws JDSException {
        //1.1创建视图层

        viewTask = genCustomViewJava();
        this.javaViewSource = viewTask.getSourceList();
        buildRepository();
    }


    public List<JavaGenSource> build() throws JDSException {
        //1.1创建视图层
        viewTask = genCustomViewJava();
        this.javaViewSource = viewTask.getSourceList();

        if (domainInst != null && domainInst.getUserSpace().equals(UserSpace.VIEW)) {
            //2.1创建资源层接口V
            this.repositorySource = initRepositoryViewJava(true);
            //3.1预编译一次
            this.javaGen.dynCompile(getModuleJavaSrc());
            this.aggServiceRootBean = genRootBean();
            reBindService();
        }
        //3.5重新编译视图
        this.javaGen.dynCompile(getAllSrcBean());
        updateViewBean(customViewBean);
        if (childBeans == null || childBeans.isEmpty()) {
            this.genChildJava();
        }

        DSMFactory.getInstance().saveCustomViewBean(customViewBean);
        CustomViewFactory.getInstance().reLoad();
        return aggServiceRootBean;
    }


    public List<JavaGenSource> genChildJava() throws JDSException {
        chrome.printLog("创建关联子对象...", true);
        EUModule euModule = ESDFacrory.getAdminESDClient().getModule(euClassName, domainInst.getProjectVersionName());
        if (euModule != null && euModule.getComponent() != null) {
            childBeans = customViewBean.updateModule(euModule.getComponent());
        }
        return childBeans;
    }

    public void update() throws JDSException {
        this.updateViewBean(customViewBean);
        DSMFactory.getInstance().saveCustomViewBean(customViewBean);
    }

    public List<JavaGenSource> getViewSrcList() {
        List<JavaGenSource> viewClassList = new ArrayList<>();
        for (JavaGenSource source : javaViewSource) {
            viewClassList.add(source);
        }
        viewClassList.addAll(aggBeans);
        return viewClassList;
    }

    public List<JavaGenSource> getRepositorySrcList() {
        List<JavaGenSource> classList = new ArrayList<>();
        for (JavaGenSource source : repositorySource) {
            classList.add(source);
        }
        return classList;
    }

    public List<JavaGenSource> getAggSrcList() {
        List<JavaGenSource> classList = new ArrayList<>();

        classList.addAll(serviceBeans);
        classList.addAll(aggServiceRootBean);
        return classList;
    }

    /**
     * 根据视图创建单一视图
     *
     * @return
     * @throws JDSException
     */
    public GenCustomViewJava genCustomViewJava() throws JDSException {
        chrome.printLog("创建关联视图模型...", true);
        ViewManager viewManager = DSMFactory.getInstance().getViewManager();
        if (viewTask == null) {
            this.viewTask = viewManager.genCustomViewJava(viewRoot, customViewBean, euClassName, false, chrome);
        }
        this.javaViewSource = viewTask.getSourceList();
        return viewTask;
    }

    public JavaGenSource buildViewContext(JavaSrcBean javaSrcBean) throws JDSException {
        JavaGenSource javaGenSource = BuildFactory.getInstance().getJavaGenSource(javaSrcBean.getClassName());
        if (javaGenSource == null) {
            JavaTemp javaTemp = BuildFactory.getInstance().getTempManager().getJavaTempById(javaSrcBean.getJavaTempId());
            JavaRoot javaRoot = buildRootBySrc(javaSrcBean);
            javaGenSource = BuildFactory.getInstance().createSource(javaSrcBean.getClassName(), javaRoot, javaTemp, javaSrcBean);
        }
        return javaGenSource;

    }


    public List<JavaGenSource> loadJavaGenSoruce(List<String> classNameList) {
        List<JavaGenSource> javaGenSources = new ArrayList<>();
        List<JavaSrcBean> javaSrcList = domainInst.loadJavaSrc(classNameList);
        for (JavaSrcBean javaSrcBean : javaSrcList) {
            JavaGenSource genSource = null;
            try {
                genSource = buildViewContext(javaSrcBean);
            } catch (JDSException e) {
                e.printStackTrace();
            }
            if (genSource != null && !javaGenSources.contains(genSource)) {
                javaGenSources.add(genSource);
            }
        }
        return javaGenSources;
    }


    private JavaRoot buildRootBySrc(JavaSrcBean javaSrcBean) throws JDSException {
        JavaRoot javaRoot = BuildFactory.getInstance().buildJavaRoot(viewRoot, this.customViewBean, javaSrcBean.getPackageName(), javaSrcBean.getClassName());
        return javaRoot;
    }

    private List<JavaGenSource> reBuildRepositoryVO(boolean clear) throws JDSException {
        if (voRepositoryTask == null || clear) {
            String taskId = "voRepositoryTask[" + this.getEuClassName() + "]";
            this.voRepositoryTask = new GenRepositoryViewJava(repositoryInst.getViewRoot(), customViewBean, moduleName, euClassName, true, chrome, new RepositoryType[]{RepositoryType.VO, RepositoryType.VIEWBEAN, RepositoryType.REPOSITORY});
            BuildFactory.getInstance().syncTasks(taskId, Arrays.asList(voRepositoryTask));

        }
        return voRepositoryTask.getSourceList();
    }

    private List<JavaGenSource> reBuildRepositoryService(boolean clear) throws JDSException {
        if (serviceRepositoryTask == null || clear) {
            String taskId = "serviceRepositoryTask[" + this.getEuClassName() + "]";
            serviceRepositoryTask = new GenRepositoryViewJava(repositoryInst.getViewRoot(), customViewBean, moduleName, euClassName, true, chrome, new RepositoryType[]{RepositoryType.DO, RepositoryType.VIEWSERVICE, RepositoryType.REPOSITORYIMPL});
            BuildFactory.getInstance().syncTasks(taskId, Arrays.asList(serviceRepositoryTask));
        }
        return serviceRepositoryTask.getSourceList();
    }

    public List<JavaGenSource> initRepositoryViewJava(boolean clear) throws JDSException {
        chrome.printLog("创建创库模型模型...", true);
        List<JavaGenSource> repositorySource = new ArrayList<>();
        repositorySource.addAll(reBuildRepositoryVO(clear));
        repositorySource.addAll(reBuildRepositoryService(clear));
        List<String> repositoryClassList = new ArrayList<>();
        for (JavaGenSource javaSrcBean : repositorySource) {
            repositoryClassList.add(javaSrcBean.getClassName());
            repositoryInst.addJavaBean(javaSrcBean.getSrcBean());
        }

        customViewBean.getViewJavaSrcBean().setRepositoryClassList(repositoryClassList);
        return repositorySource;
    }

    private void reBindService() throws JDSException {
        customViewBean.setViewClassName(euClassName);
        for (JavaGenSource genSource : aggServiceRootBean) {
            JavaSrcBean serviceBean = genSource.getSrcBean();
            if (serviceBean != null) {
                try {
                    String targter = customViewBean.getModuleBean().getTarget();
                    ApiClassConfig aggEntityConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(serviceBean.getClassName());
                    if (aggEntityConfig != null) {
                        MethodConfig methodConfig = aggEntityConfig.findEditorMethod();
                        if (methodConfig != null && methodConfig.isModule()) {
                            if (targter != null && serviceBean.getTarget() != null && targter.equals(serviceBean.getTarget())) {
                                customViewBean.reBindService(ClassUtility.loadClass(serviceBean.getClassName()));
                            } else {
                                customViewBean.reBindService(ClassUtility.loadClass(serviceBean.getClassName()));
                            }
                            methodConfig.setView(customViewBean);
                        } else {
                            customViewBean.getCustomServiceClass().add(serviceBean.getClassName());
                        }
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                //3.4重新绑定 重做生成
                for (JavaGenSource javaGenSource : javaViewSource) {
                    chrome.printLog("重新绑定视图关系：" + serviceBean.getClassName(), true);
                    GenJava javaGen = GenJava.getInstance(domainInst.getProjectVersionName());
                    JavaTemp javaTemp = javaGenSource.getJavatemp();
                    JavaRoot javaRoot = BuildFactory.getInstance().buildJavaRoot(viewRoot, customViewBean, moduleName, javaGenSource.getClassName());
                    File file = javaGen.createJava(javaTemp, javaRoot, chrome);
                    ViewInst defaultView = domainInst.getViewInst();
                    BuildFactory.getInstance().getTempManager().genJavaSrc(file, defaultView, javaTemp.getJavaTempId());
                    BuildFactory.getInstance().createSource(javaGenSource.getClassName(), viewRoot, javaTemp, javaGenSource.getSrcBean());

                }

//
//                //3.4重新绑定 重做生成
//                for (JavaSrcBean javaViewBean : javaViewBeans) {
//                    chrome.printLog("重新绑定视图关系：" + serviceBean.getClassName(), true);
//                    if (javaViewBean != null) {
//                        viewManager.reBuildJavaSrcBean(customViewBean, javaViewBean, moduleName, chrome);
//                    }
//                }

            }
        }
    }

    public List<JavaGenSource> genRootBean() throws JDSException {
        AggregationManager aggregationManager = DSMFactory.getInstance().getAggregationManager();
        List<JavaGenSource> aggServiceRoots = new ArrayList<>();
        String className = viewRoot.getClassName();
        String moduleName = className.substring(0, className.lastIndexOf(".")).toLowerCase();
        if (rootTask == null) {
            String taskId = className;
            rootTask = new GenAggCustomViewJava(viewRoot, customViewBean, moduleName, className, chrome);
            BuildFactory.getInstance().syncTasks(taskId, Arrays.asList(rootTask));
            aggServiceRoots = rootTask.getSourceList();
            GenAggCustomJava genAggCustomJava = aggregationManager.genAggMapJava(viewRoot, customViewBean, getCurrChromeDriver());
            aggBeans.addAll(genAggCustomJava.getSourceList());
        }

//
//        JavaSrcBean aggRootBean = DSMFactory.getInstance().getAggregationManager().genAggViewJava(defaultViewRoot, customViewBean, getCurrChromeDriver());
//        if (aggRootBean != null) {
//            aggServiceRoots.add(aggRootBean);
//        }

        //3.2创建资源层接口
        chrome.printLog("3.2绑定资源层接口...", true);
        List<JavaGenSource> repositoryBeans = this.getRepositorySource();
        if (repositoryBeans != null && !repositoryBeans.isEmpty()) {
            List<JavaSrcBean> aggServerBeans = rebuildAggServiceBean(repositoryBeans);
            for (JavaSrcBean javaSrcBean : aggServerBeans) {
                JavaGenSource javaSource = BuildFactory.getInstance().getJavaGenSource(javaSrcBean.getClassName());
                if (javaSource != null) {
                    aggServiceRoots.add(javaSource);
                }
            }
        }


        return aggServiceRoots;


    }

    /**
     * 根据仓储模型构建领域模型
     *
     * @param repositorySource
     * @return
     * @throws JDSException
     */
    public List<JavaSrcBean> rebuildAggServiceBean(List<JavaGenSource> repositorySource) throws JDSException {

        AggregationManager aggregationManager = DSMFactory.getInstance().getAggregationManager();
        List<String> serviceNames = new ArrayList<>();
        List<AggEntityConfig> aggEntityConfigs = new ArrayList<>();
        for (JavaGenSource srcBean : repositorySource) {
            ESDClass esdClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(srcBean.getClassName(), true);
            if (esdClass != null && !esdClass.getCtClass().isInterface()) {
                ExpressionTempBean expressionTempBean = EsbBeanFactory.getInstance().registerService(esdClass.getCtClass());
                if (expressionTempBean != null) {
                    AggEntityConfig entityConfig = aggregationManager.getAggEntityConfig(expressionTempBean.getClazz(), true);
                    if (entityConfig.getAggregationBean() != null) {
                        serviceNames.add(expressionTempBean.getClazz());
                    }
                }
            }
        }
        chrome.printLog("3.3创建领域模型...", true);
        //3.3创建领域模型
        for (String serviceName : serviceNames) {
            AggEntityConfig entityConfig = aggregationManager.getAggEntityConfig(serviceName, true);
            if (entityConfig != null) {
                entityConfig.getAggregationBean().setModuleName(moduleName);
                aggEntityConfigs.add(entityConfig);
            }
        }

        chrome.printLog("重新创建绑定聚合关系...", true);
        List<JavaSrcBean> reBindBean = aggregationManager.reBuildServiceBean(domainInst, aggEntityConfigs, AggregationType.ENTITY, chrome, true);
        return reBindBean;
    }

    public void reSetViewBean(CustomViewBean customViewBean) {

        this.customViewBean = customViewBean;
        this.customViewBean.setDomainId(domainId);
        this.customModuleBean = customViewBean.getModuleBean();
        if (customModuleBean == null) {
            if (customViewBean.getMethodConfig() != null) {
                customModuleBean = customViewBean.getMethodConfig().getModuleBean();
            }
        }
        this.viewRoot = new AggViewRoot(domainInst, euClassName, customModuleBean);

        ViewJavaSrcBean viewJavaSrcBean = customViewBean.getViewJavaSrcBean();
        if (viewJavaSrcBean == null) {
            viewJavaSrcBean = new ViewJavaSrcBean(packageName, euClassName);
        } else {
            viewJavaSrcBean.setParentModuleClassName(packageName);
            viewJavaSrcBean.setClassName(euClassName);
        }

        if (viewJavaSrcBean.getViewClassList() != null) {
            List<JavaSrcBean> srcBeans = viewInst.loadJavaSrc(viewJavaSrcBean.getViewClassList());
            for (JavaSrcBean srcBean : srcBeans) {
                try {
                    javaViewSource.add(this.buildViewContext(srcBean));
                } catch (JDSException e) {
                    e.printStackTrace();
                }
            }
        } else {
            javaViewSource = new ArrayList<>();
        }

        if (viewJavaSrcBean.getAggClassList() != null) {
            aggBeans = loadJavaGenSoruce(viewJavaSrcBean.getAggClassList());
        } else {
            aggBeans = new ArrayList<>();
        }

        if (viewJavaSrcBean.getChildClassList() != null) {
            childBeans = loadJavaGenSoruce(viewJavaSrcBean.getChildClassList());
        } else {
            childBeans = new ArrayList<>();
        }

        if (viewJavaSrcBean.getAggClassList() != null) {
            serviceBeans = loadJavaGenSoruce(viewJavaSrcBean.getServiceClassList());
        } else {
            serviceBeans = new ArrayList<>();
        }

        if (viewJavaSrcBean.getRepositoryClassList() != null) {
            List<JavaSrcBean> repositoryBeans = repositoryInst.loadJavaSrc(viewJavaSrcBean.getRepositoryClassList());
            for (JavaSrcBean srcBean : repositoryBeans) {
                try {
                    repositorySource.add(this.buildViewContext(srcBean));
                } catch (JDSException e) {
                    e.printStackTrace();
                }
            }
        } else {
            repositorySource = new ArrayList<>();
        }

        if (viewJavaSrcBean.getRootServicesClassName().size() > 0) {
            for (String serviceClassName : viewJavaSrcBean.getRootServicesClassName()) {
                JavaSrcBean srcBean = domainInst.getJavaSrcByClassName(serviceClassName);
                try {
                    if (srcBean != null) {
                        aggServiceRootBean.add(buildViewContext(srcBean));
                    }
                } catch (JDSException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public List<JavaSrcBean> getModuleJavaSrc() {
        List<JavaSrcBean> allSrcBean = new ArrayList<>();
        List<JavaGenSource> genSources = getModuleBeans();
        for (JavaGenSource genSource : genSources) {
            if (!allSrcBean.contains(genSource.getSrcBean())) {
                allSrcBean.add(genSource.getSrcBean());
            }
        }
        return allSrcBean;
    }

    public List<JavaGenSource> getModuleBeans() {
        List<JavaGenSource> allSrcBean = new ArrayList<>();
        for (JavaGenSource genSource : javaViewSource) {
            if (!allSrcBean.contains(genSource)) {
                allSrcBean.add(genSource);
            }
        }
        for (JavaGenSource genSource : repositorySource) {
            if (!allSrcBean.contains(genSource)) {
                allSrcBean.add(genSource);
            }
        }
        return allSrcBean;
    }

    public List<JavaGenSource> getSrcBeanList() {
        List<JavaGenSource> allSrcBean = new ArrayList<>();
        for (JavaGenSource genSource : aggBeans) {
            if (!allSrcBean.contains(genSource)) {
                allSrcBean.add(genSource);
            }
        }
        for (JavaGenSource genSource : serviceBeans) {
            if (!allSrcBean.contains(genSource)) {
                allSrcBean.add(genSource);
            }
        }


        for (JavaGenSource genSource : javaViewSource) {
            if (!allSrcBean.contains(genSource)) {
                allSrcBean.add(genSource);
            }
        }


        for (JavaGenSource genSource : childBeans) {
            if (!allSrcBean.contains(genSource)) {
                allSrcBean.add(genSource);
            }
        }

        for (JavaGenSource genSource : repositorySource) {
            if (!allSrcBean.contains(genSource)) {
                allSrcBean.add(genSource);
            }
        }
        for (JavaGenSource genSource : aggServiceRootBean) {
            if (!allSrcBean.contains(genSource)) {
                allSrcBean.add(genSource);
            }

        }
        return allSrcBean;
    }

    public List<JavaGenSource> getAllGenBean() {
        List<JavaGenSource> allGenBeans = new ArrayList<>();
        allGenBeans.addAll(getModuleBeans());
        allGenBeans.addAll(getSrcBeanList());
        return allGenBeans;
    }

    public List<JavaSrcBean> getAllSrcBean() {
        List<JavaSrcBean> allSrcBean = new ArrayList<>();
        List<JavaGenSource> genSources = getAllGenBean();
        for (JavaGenSource genSource : genSources) {
            if (!allSrcBean.contains(genSource.getSrcBean())) {
                allSrcBean.add(genSource.getSrcBean());
            }
        }
        return allSrcBean;
    }


    void updateViewBean(CustomViewBean customViewBean) {

        ViewJavaSrcBean viewJavaSrcBean = customViewBean.getViewJavaSrcBean();
        if (viewJavaSrcBean == null) {
            viewJavaSrcBean = new ViewJavaSrcBean(packageName, euClassName);
        }

        List<String> viewClassList = new ArrayList<>();
        for (JavaGenSource source : javaViewSource) {
            viewClassList.add(source.getClassName());
        }

        viewJavaSrcBean.setViewClassList(viewClassList);
        List<String> aggClassList = new ArrayList<>();
        for (JavaGenSource source : aggBeans) {
            aggClassList.add(source.getClassName());
        }
        viewJavaSrcBean.setAggClassList(aggClassList);


        List<String> childClass = new ArrayList<>();
        for (JavaGenSource source : childBeans) {
            childClass.add(source.getClassName());
        }
        viewJavaSrcBean.setAggClassList(childClass);

        List<String> serviceClassList = new ArrayList<>();
        for (JavaGenSource source : serviceBeans) {
            serviceClassList.add(source.getClassName());
        }
        viewJavaSrcBean.setServiceClassList(serviceClassList);

        List<String> repositoryClassList = new ArrayList<>();
        for (JavaGenSource javaSrcBean : repositorySource) {
            repositoryClassList.add(javaSrcBean.getClassName());
        }
        viewJavaSrcBean.setRepositoryClassList(repositoryClassList);

        for (JavaGenSource source : aggServiceRootBean) {
            if (source != null && !viewJavaSrcBean.getRootServicesClassName().contains(source.getClassName())) {
                viewJavaSrcBean.getRootServicesClassName().add(source.getClassName());
            }
        }
        customViewBean.setViewJavaSrcBean(viewJavaSrcBean);
    }


    public RepositoryInst getRepositoryInst() {
        if (repositoryInst == null) {
            repositoryInst = this.getDomainInst().getRepositoryInst();
        }
        return repositoryInst;
    }

    public List<JavaGenSource> getChildBeans() {
        return childBeans;
    }

    public void setChildBeans(List<JavaGenSource> childBeans) {
        this.childBeans = childBeans;
    }

    public List<JavaGenSource> getAggBeans() {
        return aggBeans;
    }

    public void setAggBeans(List<JavaGenSource> aggBeans) {
        this.aggBeans = aggBeans;
    }

    public List<JavaGenSource> getServiceBeans() {
        return serviceBeans;
    }

    public void setServiceBeans(List<JavaGenSource> serviceBeans) {
        this.serviceBeans = serviceBeans;
    }

    public AggViewRoot getViewRoot() {
        return viewRoot;
    }

    public void setViewRoot(AggViewRoot viewRoot) {
        this.viewRoot = viewRoot;
    }

    public List<JavaGenSource> getAggServiceRootBean() {
        return aggServiceRootBean;
    }

    public void setAggServiceRootBean(List<JavaGenSource> aggServiceRootBean) {
        this.aggServiceRootBean = aggServiceRootBean;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }


    public void setRepositoryInst(RepositoryInst repositoryInst) {
        this.repositoryInst = repositoryInst;
    }

    public GenJava getJavaGen() {
        return javaGen;
    }

    public void setJavaGen(GenJava javaGen) {
        this.javaGen = javaGen;
    }

    public GenCustomViewJava getViewTask() {
        return viewTask;
    }

    public void setViewTask(GenCustomViewJava viewTask) {
        this.viewTask = viewTask;
    }

    public GenRepositoryViewJava getVoRepositoryTask() {
        return voRepositoryTask;
    }

    public void setVoRepositoryTask(GenRepositoryViewJava voRepositoryTask) {
        this.voRepositoryTask = voRepositoryTask;
    }

    public GenRepositoryViewJava getServiceRepositoryTask() {
        return serviceRepositoryTask;
    }

    public void setServiceRepositoryTask(GenRepositoryViewJava serviceRepositoryTask) {
        this.serviceRepositoryTask = serviceRepositoryTask;
    }

    public GenAggCustomViewJava getRootTask() {
        return rootTask;
    }

    public void setRootTask(GenAggCustomViewJava rootTask) {
        this.rootTask = rootTask;
    }

    public CustomModuleBean getCustomModuleBean() {
        return customModuleBean;
    }

    public void setCustomModuleBean(CustomModuleBean customModuleBean) {
        this.customModuleBean = customModuleBean;
    }

    public List<JavaGenSource> getJavaViewSource() {
        return javaViewSource;
    }

    public void setJavaViewSource(List<JavaGenSource> javaViewSource) {
        this.javaViewSource = javaViewSource;
    }

    public List<JavaGenSource> getRepositorySource() {
        return repositorySource;
    }

    public void setRepositorySource(List<JavaGenSource> repositorySource) {
        this.repositorySource = repositorySource;
    }

    public CustomViewBean getCustomViewBean() {
        return customViewBean;
    }

    public void setCustomViewBean(CustomViewBean customViewBean) {
        this.customViewBean = customViewBean;
    }

    public DomainInst getDomainInst() {
        return domainInst;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public void setDomainInst(DomainInst domainInst) {
        this.domainInst = domainInst;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getEuClassName() {
        return euClassName;
    }

    public void setEuClassName(String euClassName) {
        this.euClassName = euClassName;
    }

    public ViewInst getViewInst() {
        return viewInst;
    }

    public void setViewInst(ViewInst viewInst) {
        this.viewInst = viewInst;
    }

    public ChromeProxy getChrome() {
        return chrome;
    }

    public void setChrome(ChromeProxy chrome) {
        this.chrome = chrome;
    }

    public AggViewRoot getMainView() {
        return mainView;
    }

    public void setMainView(AggViewRoot mainView) {
        this.mainView = mainView;
    }

    public ChromeProxy getCurrChromeDriver() {
        ChromeProxy chrome = JDSActionContext.getActionContext().Par("$currChromeDriver", ChromeProxy.class);
        if (chrome == null) {
            chrome = new LogSetpLog();
        }
        return chrome;
    }
}
