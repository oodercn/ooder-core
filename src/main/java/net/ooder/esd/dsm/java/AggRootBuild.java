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
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.dsm.aggregation.AggregationManager;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.aggregation.context.AggViewRoot;
import net.ooder.esd.dsm.gen.GenJava;
import net.ooder.esd.dsm.repository.RepositoryInst;
import net.ooder.esd.dsm.repository.RepositoryManager;
import net.ooder.esd.dsm.view.ViewInst;
import net.ooder.esd.dsm.view.ViewManager;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.web.AggregationBean;

import java.util.ArrayList;
import java.util.List;

public class AggRootBuild {

    List<JavaSrcBean> javaViewBeans;

    List<JavaSrcBean> childBeans;

    List<JavaSrcBean> repositoryBeans;

    List<JavaSrcBean> aggBeans;

    List<JavaSrcBean> serviceBeans;

    List<JavaSrcBean> aggServiceRootBean = new ArrayList<>();

    String packageName;

    String moduleName;

    String euClassName;

    String domainId;

    CustomViewBean customViewBean;

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
        this.customViewBean = customViewBean;
        this.customViewBean.setDomainId(domainId);
        this.init(customViewBean.getViewJavaSrcBean());
    }


    public void reBuildModule() throws JDSException {
        //1.1创建视图层
        this.javaViewBeans = genCustomViewJava();
        //2.1创建资源层接口V
        this.repositoryBeans = genRepositoryViewJava();
        //3.1预编译一次
        this.javaGen.dynCompile(getModuleBeans());

    }

    public void buildRepository() throws JDSException {
        //2.1创建资源层接口V
        this.repositoryBeans = genRepositoryViewJava();
        //3.1预编译一次
        this.javaGen.dynCompile(getModuleBeans());

    }

    public void genBuildModule() throws JDSException {
        //1.1创建视图层
        this.javaViewBeans = genCustomViewJava();
        buildRepository();
    }


    public List<JavaSrcBean> build() throws JDSException {
        //1.1创建视图层
        this.javaViewBeans = genCustomViewJava();
        if (domainInst != null && domainInst.getUserSpace().equals(UserSpace.VIEW)) {
            //2.1创建资源层接口V
            this.repositoryBeans = genRepositoryViewJava();
            //3.1预编译一次
            this.javaGen.dynCompile(getModuleBeans());
            this.aggServiceRootBean = genRootBean();
            reBindService();
        }
        //3.5重新编译视图
        this.javaGen.dynCompile(getAllSrcBean());
        reSetViewBean(customViewBean);
        if (childBeans == null || childBeans.isEmpty()) {
            this.genChildJava();
        }

        DSMFactory.getInstance().saveCustomViewBean(customViewBean);
        CustomViewFactory.getInstance().reLoad();
        return aggServiceRootBean;
    }


    public List<JavaSrcBean> genChildJava() throws JDSException {
        chrome.printLog("创建关联子对象...", true);
        EUModule euModule = ESDFacrory.getAdminESDClient().getModule(euClassName, domainInst.getProjectVersionName());
        if (euModule != null && euModule.getComponent() != null) {
            childBeans = customViewBean.updateModule(euModule.getComponent());
        }

        return childBeans;
    }

    public void update() throws JDSException {
        this.reSetViewBean(customViewBean);
        DSMFactory.getInstance().saveCustomViewBean(customViewBean);
    }


    /**
     * 根据视图创建单一视图
     *
     * @return
     * @throws JDSException
     */
    public List<JavaSrcBean> genCustomViewJava() throws JDSException {
        chrome.printLog("创建关联视图模型...", true);
        ViewManager viewManager = DSMFactory.getInstance().getViewManager();
        CustomModuleBean customModuleBean = customViewBean.getModuleBean();
        if (customModuleBean == null){
            if (customViewBean.getMethodConfig() != null) {
                customModuleBean = customViewBean.getMethodConfig().getModuleBean();
            }

        }
        AggViewRoot viewRoot = new AggViewRoot(domainInst, euClassName, customModuleBean);
        javaViewBeans = viewManager.genCustomViewJava(viewRoot, customViewBean, euClassName, false, chrome);
        return javaViewBeans;
    }

    public List<JavaSrcBean> genRepositoryViewJava() throws JDSException {
        chrome.printLog("创建创库模型模型...", true);
        RepositoryManager repositoryManager = DSMFactory.getInstance().getRepositoryManager();
        this.repositoryBeans = repositoryManager.genModuleViewJava(repositoryInst, customViewBean, moduleName, euClassName, chrome);
        return repositoryBeans;
    }

    private void reBindService() throws JDSException {
        ViewManager viewManager = DSMFactory.getInstance().getViewManager();
        customViewBean.setViewClassName(euClassName);
        for (JavaSrcBean serviceBean : aggServiceRootBean) {
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
                        } else {
                            customViewBean.getCustomServiceClass().add(serviceBean.getClassName());
                        }
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                //3.4重新绑定 重做生成
                for (JavaSrcBean javaViewBean : javaViewBeans) {
                    chrome.printLog("重新绑定视图关系：" + serviceBean.getClassName(), true);
                    if (javaViewBean != null) {
                        viewManager.reBuildJavaSrcBean(customViewBean, javaViewBean, moduleName, chrome);
                    }
                }
            }
        }
    }

    public List<JavaSrcBean> genRootBean() throws JDSException {
        AggregationManager aggregationManager = DSMFactory.getInstance().getAggregationManager();
        List<JavaSrcBean> aggServiceRoots = new ArrayList<>();
        CustomModuleBean customModuleBean = customViewBean.getModuleBean();
        if (customModuleBean == null && customViewBean.getMethodConfig() != null) {
            customModuleBean = customViewBean.getMethodConfig().getModuleBean();
        }

        AggViewRoot defaultViewRoot = new AggViewRoot(domainInst, euClassName, customModuleBean);
        JavaSrcBean aggRootBean = DSMFactory.getInstance().getAggregationManager().genAggViewJava(defaultViewRoot, customViewBean, getCurrChromeDriver());
        if (aggRootBean != null) {
            aggServiceRoots.add(aggRootBean);
        }

        //3.2创建资源层接口
        chrome.printLog("3.2创建资源层接口...", true);
        List<JavaSrcBean> repositoryBeans = this.getRepositoryBeans();
        if (repositoryBeans != null && !repositoryBeans.isEmpty()) {
            List<JavaSrcBean> aggServerBeans = rebuildAggServiceBean(repositoryBeans);
            aggServiceRoots.addAll(aggServerBeans);
        }

        List<JavaSrcBean> customEntityMap = aggregationManager.genAggMapJava(defaultViewRoot, customViewBean, getCurrChromeDriver());
        aggBeans.addAll(customEntityMap);

        return aggServiceRoots;


    }

    /**
     * 根据仓储模型构建领域模型
     *
     * @param repositoryBeans
     * @return
     * @throws JDSException
     */
    public List<JavaSrcBean> rebuildAggServiceBean(List<JavaSrcBean> repositoryBeans) throws JDSException {

        AggregationManager aggregationManager = DSMFactory.getInstance().getAggregationManager();
        List<String> serviceNames = new ArrayList<>();
        List<AggEntityConfig> aggEntityConfigs = new ArrayList<>();
        for (JavaSrcBean srcBean : repositoryBeans) {
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

    void init(ViewJavaSrcBean viewJavaSrcBean) {
        if (viewJavaSrcBean == null) {
            viewJavaSrcBean = new ViewJavaSrcBean(packageName, euClassName);
        } else {
            viewJavaSrcBean.setParentModuleClassName(packageName);
            viewJavaSrcBean.setClassName(euClassName);
        }

        if (viewJavaSrcBean.getViewClassList() != null) {
            javaViewBeans = viewInst.loadJavaSrc(viewJavaSrcBean.getViewClassList());
        } else {
            javaViewBeans = new ArrayList<>();
        }

        if (viewJavaSrcBean.getAggClassList() != null) {
            aggBeans = domainInst.loadJavaSrc(viewJavaSrcBean.getAggClassList());
        } else {
            aggBeans = new ArrayList<>();
        }

        if (viewJavaSrcBean.getChildClassList() != null) {
            childBeans = domainInst.loadJavaSrc(viewJavaSrcBean.getChildClassList());
        } else {
            childBeans = new ArrayList<>();
        }

        if (viewJavaSrcBean.getAggClassList() != null) {
            serviceBeans = domainInst.loadJavaSrc(viewJavaSrcBean.getServiceClassList());
        } else {
            serviceBeans = new ArrayList<>();
        }

        if (viewJavaSrcBean.getRepositoryClassList() != null) {
            repositoryBeans = repositoryInst.loadJavaSrc(viewJavaSrcBean.getRepositoryClassList());
        } else {
            repositoryBeans = new ArrayList<>();
        }

        if (viewJavaSrcBean.getRootServicesClassName().size() > 0) {
            for (String serviceClassName : viewJavaSrcBean.getRootServicesClassName()) {
                aggServiceRootBean.add(domainInst.getJavaSrcByClassName(serviceClassName));
            }
        }


    }


    public List<JavaSrcBean> getModuleBeans() {
        List<JavaSrcBean> allSrcBean = new ArrayList<>();
        for (JavaSrcBean viewBean : javaViewBeans) {
            if (!allSrcBean.contains(viewBean)) {
                allSrcBean.add(viewBean);
            }
        }
        for (JavaSrcBean viewBean : repositoryBeans) {
            if (!allSrcBean.contains(viewBean)) {
                allSrcBean.add(viewBean);
            }
        }
        return allSrcBean;
    }

    public List<JavaSrcBean> getAllSrcBean() {
        List<JavaSrcBean> allSrcBean = getModuleBeans();
        for (JavaSrcBean viewBean : aggBeans) {
            if (!allSrcBean.contains(viewBean)) {
                allSrcBean.add(viewBean);
            }
        }
        for (JavaSrcBean viewBean : serviceBeans) {
            if (!allSrcBean.contains(viewBean)) {
                allSrcBean.add(viewBean);
            }
        }

        for (JavaSrcBean viewBean : javaViewBeans) {
            if (!allSrcBean.contains(viewBean)) {
                allSrcBean.add(viewBean);
            }
        }


        for (JavaSrcBean viewBean : javaViewBeans) {
            if (!allSrcBean.contains(viewBean)) {
                allSrcBean.add(viewBean);
            }
        }

        for (JavaSrcBean viewBean : childBeans) {
            if (!allSrcBean.contains(viewBean)) {
                allSrcBean.add(viewBean);
            }
        }

        for (JavaSrcBean viewBean : repositoryBeans) {
            if (!allSrcBean.contains(viewBean)) {
                allSrcBean.add(viewBean);
            }
        }

        for (JavaSrcBean javaSrcBean : aggServiceRootBean) {
            if (javaSrcBean != null && !allSrcBean.contains(javaSrcBean)) {
                allSrcBean.add(javaSrcBean);
            }

        }


        return allSrcBean;
    }


    void reSetViewBean(CustomViewBean customViewBean) {
        ViewJavaSrcBean viewJavaSrcBean = customViewBean.getViewJavaSrcBean();
        if (viewJavaSrcBean == null) {
            viewJavaSrcBean = new ViewJavaSrcBean(packageName, euClassName);
        }

        List<String> viewClassList = new ArrayList<>();
        for (JavaSrcBean javaSrcBean : javaViewBeans) {
            viewClassList.add(javaSrcBean.getClassName());
        }
        viewJavaSrcBean.setViewClassList(viewClassList);

        List<String> aggClassList = new ArrayList<>();
        for (JavaSrcBean javaSrcBean : aggBeans) {
            aggClassList.add(javaSrcBean.getClassName());
        }
        viewJavaSrcBean.setAggClassList(aggClassList);


        List<String> childClass = new ArrayList<>();
        for (JavaSrcBean javaSrcBean : childBeans) {
            childClass.add(javaSrcBean.getClassName());
        }
        viewJavaSrcBean.setAggClassList(childClass);

        List<String> serviceClassList = new ArrayList<>();
        for (JavaSrcBean javaSrcBean : serviceBeans) {
            serviceClassList.add(javaSrcBean.getClassName());
        }
        viewJavaSrcBean.setServiceClassList(serviceClassList);

        List<String> repositoryClassList = new ArrayList<>();
        for (JavaSrcBean javaSrcBean : repositoryBeans) {
            repositoryClassList.add(javaSrcBean.getClassName());
        }
        viewJavaSrcBean.setRepositoryClassList(repositoryClassList);

        for (JavaSrcBean javaSrcBean : aggServiceRootBean) {
            if (javaSrcBean != null && !viewJavaSrcBean.getRootServicesClassName().contains(javaSrcBean.getClassName())) {
                viewJavaSrcBean.getRootServicesClassName().add(javaSrcBean.getClassName());
            }
        }

        customViewBean.setViewJavaSrcBean(viewJavaSrcBean);
    }

    public List<JavaSrcBean> getChildBeans() {
        return childBeans;
    }

    public void setChildBeans(List<JavaSrcBean> childBeans) {
        this.childBeans = childBeans;
    }

    public RepositoryInst getRepositoryInst() {
        if (repositoryInst == null) {
            repositoryInst = this.getDomainInst().getRepositoryInst();
        }
        return repositoryInst;
    }

    public List<JavaSrcBean> getAggBeans() {
        return aggBeans;
    }

    public void setAggBeans(List<JavaSrcBean> aggBeans) {
        this.aggBeans = aggBeans;
    }

    public List<JavaSrcBean> getServiceBeans() {
        return serviceBeans;
    }

    public void setServiceBeans(List<JavaSrcBean> serviceBeans) {
        this.serviceBeans = serviceBeans;
    }


    public List<JavaSrcBean> getAggServiceRootBean() {
        return aggServiceRootBean;
    }

    public void setAggServiceRootBean(List<JavaSrcBean> aggServiceRootBean) {
        this.aggServiceRootBean = aggServiceRootBean;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public List<JavaSrcBean> getJavaViewBeans() {
        return javaViewBeans;
    }

    public void setJavaViewBeans(List<JavaSrcBean> javaViewBeans) {
        this.javaViewBeans = javaViewBeans;
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


    public List<JavaSrcBean> getRepositoryBeans() {
        return repositoryBeans;
    }

    public void setRepositoryBeans(List<JavaSrcBean> repositoryBeans) {
        this.repositoryBeans = repositoryBeans;
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
