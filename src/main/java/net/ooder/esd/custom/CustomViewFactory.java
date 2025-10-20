package net.ooder.esd.custom;

import com.alibaba.fastjson.JSONObject;
import net.ooder.annotation.UserSpace;
import net.ooder.common.JDSConstants;
import net.ooder.common.JDSException;
import net.ooder.common.logging.ChromeProxy;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.common.logging.LogSetpLog;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.ModuleAnnotation;
import net.ooder.esd.bean.CustomData;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.custom.component.CustomDynLoadView;
import net.ooder.esd.custom.component.CustomModuleComponent;
import net.ooder.esd.custom.component.grid.CustomGridComponent;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.engine.MySpace;
import net.ooder.esd.engine.ProjectVersion;
import net.ooder.esd.engine.enums.PackagePathType;
import net.ooder.esd.engine.enums.PackageType;
import net.ooder.esd.manager.plugins.api.APIFactory;
import net.ooder.esd.tool.DSMProperties;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.ModuleProperties;
import net.ooder.esd.tool.properties.Properties;
import net.ooder.esd.tool.properties.form.FormField;
import net.ooder.esd.tool.properties.list.ListFieldProperties;
import net.ooder.esd.util.DSMAnnotationUtil;
import net.ooder.server.JDSServer;
import net.ooder.server.httpproxy.core.Handler;
import net.ooder.vfs.ct.CtVfsFactory;
import net.ooder.vfs.ct.CtVfsService;
import net.ooder.web.APIConfig;
import net.ooder.web.APIConfigFactory;
import net.ooder.web.RequestMethodBean;
import net.ooder.web.util.AnnotationUtil;
import org.mvel2.templates.TemplateRuntime;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class CustomViewFactory {

    private MySpace space;

    private static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, CustomViewFactory.class);

    public static final String dynBuild = "DYN";

    public static final String INMODULE__ = "__";

    public static final String DSMdsm = "DSMdsm";

    public static final String TopModuleKey = "TopModule";

    public static final String CurrModuleKey = "CurrModule";

    public static final String MethodBeanKey = "MethodBean";

    public static final String TopMethodBeanKey = "TopMethodBean";

    public static final String THREAD_LOCK = "Thread Lock";

    static Map<String, CustomViewFactory> managerMap = new HashMap<String, CustomViewFactory>();

    private Map<String, RequestMethodBean> methodMap = new HashMap<String, RequestMethodBean>();

    private Map<String, MethodConfig> methodConfigMap = new HashMap<String, MethodConfig>();

    public static CustomViewFactory getInstance() throws JDSException {
        MySpace space = ESDFacrory.getAdminESDClient().getSpace();
        return getInstance(space);
    }

    public static CustomViewFactory getInstance(MySpace space) {
        String path = space.getPath();
        CustomViewFactory manager = managerMap.get(path);
        if (manager == null) {
            synchronized (path) {
                manager = managerMap.get(path);
                if (manager == null) {
                    manager = new CustomViewFactory(space);
                    managerMap.put(path, manager);
                }
            }
        }
        return manager;
    }


    CustomViewFactory(MySpace space) {
        this.space = space;
    }

    /**
     * 动态编译MODULE
     *
     * @return
     * @throws JDSException
     */
    public <T extends ModuleComponent> EUModule<T> dynBuild(Class customClass, Map<String, ?> valueMap) throws JDSException {
        EUModule module = (EUModule) JDSActionContext.getActionContext().getContext().get(TopModuleKey);
        MethodConfig methodConfig = (MethodConfig) JDSActionContext.getActionContext().getContext().get(MethodBeanKey);
        EUModule dynmodule = dynModule(methodConfig, customClass, module, valueMap);
        return dynmodule;
    }


    /**
     * @param methodConfig
     * @param module
     * @param valueMap
     * @param <T>
     * @return
     * @throws JDSException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    public <T extends ModuleComponent> EUModule<T> createModuleComponent(MethodConfig methodConfig, Class<T> customClass, EUModule module, Map<String, ?> valueMap, boolean dynbuild) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        synchronized (module.getClassName()) {
            if (customClass == null) {
                customClass = methodConfig.getRealViewClass();
            }
            if (methodConfig.getDynDataBean() != null && module.getRealClassName().equals(module.getClassName())) {
                customClass = (Class<T>) CustomDynLoadView.class;
            }

            if (customClass != null && methodConfig.getView() != null) {
                Constructor<T> constructor = customClass.getConstructor(new Class[]{EUModule.class, MethodConfig.class, Map.class});
                if (valueMap == null) {
                    valueMap = new HashMap<>();
                }
                EUModule oTopModule = (EUModule) JDSActionContext.getActionContext().getContext().get(CustomViewFactory.TopModuleKey);
                EUModule omodule = (EUModule) JDSActionContext.getActionContext().getContext().get(CustomViewFactory.CurrModuleKey);

                if (oTopModule == null) {
                    oTopModule = module;
                    JDSActionContext.getActionContext().getContext().put(CustomViewFactory.TopModuleKey, module);
                }
                if (omodule == null) {
                    omodule = module;
                    JDSActionContext.getActionContext().getContext().put(CustomViewFactory.CurrModuleKey, module);
                }

                MethodConfig omethodConfig = (MethodConfig) JDSActionContext.getActionContext().getContext().get(CustomViewFactory.MethodBeanKey);
                JDSActionContext.getActionContext().getContext().put(CustomViewFactory.CurrModuleKey, module);
                JDSActionContext.getActionContext().getContext().put(CustomViewFactory.MethodBeanKey, methodConfig);

                T component = constructor.newInstance(new Object[]{module, methodConfig, valueMap});
                ModuleProperties properties = new ModuleProperties(customClass, methodConfig, module.getProjectVersion().getProjectName());
                component.setProperties(properties);
                String json = JSONObject.toJSONString(component);
                String obj = (String) TemplateRuntime.eval(json, perContext(oTopModule, component.getEuModule()));
                T moduleComponent = JSONObject.parseObject(obj, customClass);
                moduleComponent.setProperties(properties);
                moduleComponent.setCache(false);

                try {
                    saveModule(module, dynbuild);
                } catch (Throwable e) {
                    logger.error(e);
                }


                moduleComponent.setCtxBaseComponent(component.getCtxBaseComponent());
                if (component instanceof CustomGridComponent) {
                    ((CustomGridComponent) moduleComponent).setCmdBar(((CustomGridComponent) component).getCmdBar());
                }
                if (component instanceof CustomModuleComponent) {
                    ((CustomModuleComponent) moduleComponent).setMainComponent(((CustomModuleComponent) component).getMainComponent());
                }
                module.setComponent(moduleComponent);
                module.getComponent().fillFormValues(valueMap, true);
                JDSActionContext.getActionContext().getContext().put(CustomViewFactory.CurrModuleKey, omodule);
                JDSActionContext.getActionContext().getContext().put(CustomViewFactory.MethodBeanKey, omethodConfig);
            }
        }
        return module;
    }


    /**
     * 动态编译MODULE
     *
     * @return
     * @throws JDSException
     */
    public List<TreeListItem> dynLoadItem() {
        List<TreeListItem> items = new ArrayList<>();
        EUModule module = (EUModule) JDSActionContext.getActionContext().getContext().get(TopModuleKey);
        Properties properties = module.getComponent().getCurrComponent().getProperties();
        if (properties instanceof ListFieldProperties) {
            items = ((ListFieldProperties) properties).getItems();
        }
        return items;
    }


    /**
     * 动态编译MODULE
     *
     * @return
     * @throws JDSException
     */
    public <T extends ModuleComponent> EUModule<T> dynModule(MethodConfig methodConfig, Class<T> customClass, EUModule module, Map<String, ?> valueMap) throws JDSException {
        ProjectVersion version = ESDFacrory.getAdminESDClient().getProjectVersionByName(module.getProjectVersion().getVersionName());
        perContext(module, module);
        EUModule<T> dynmodule = null;
        if (!module.getName().endsWith(dynBuild)) {
            dynmodule = version.createModule(module.getPackageName() + "." + module.getName() + dynBuild);
            dynmodule.setRealClassName(module.getClassName());
            try {
                dynmodule = createModuleComponent(methodConfig, customClass, dynmodule, valueMap, false);
            } catch (Exception e) {
                e.printStackTrace();
                throw new JDSException(e);
            }
        } else {
            dynmodule = module;
        }

        return dynmodule;
    }


    public EUModule getViewByMethod(MethodConfig methodAPIBean, String projectName, Map<String, ?> valueMap) throws JDSException {
        EUModule module = null;
        Map contextMap = JDSActionContext.getActionContext().getContext();
        if (methodAPIBean != null && methodAPIBean.isModule()) {
            if (methodAPIBean.getDomainId() == null || methodAPIBean.getDomainId().equals("")) {
                ProjectVersion version = ESDFacrory.getAdminESDClient().getProjectVersionByName(projectName);
                methodAPIBean.setDomainId(version.getProject().getProjectName());
            }
            String url = methodAPIBean.getUrl();
            PackagePathType packagePathType = PackagePathType.startPath(url);
            //添加线程缓存避免嵌套调用\
            Long createTime = (Long) contextMap.get(url + "[TIME]");
            module = (EUModule) contextMap.get(url);
            if (module == null && (createTime == null || System.currentTimeMillis() - createTime > 500)) {
                contextMap.put(url + "[TIME]", System.currentTimeMillis());

                if (packagePathType != null && packagePathType.getApiType() != null && packagePathType.getApiType().getDefaultProjectName() != null) {
                    projectName = packagePathType.getApiType().getDefaultProjectName();
                }

                if (methodAPIBean.getModuleBean().getDynLoad() != null && !methodAPIBean.getModuleBean().getDynLoad()) {
                    module = ESDFacrory.getAdminESDClient().getModule(url, projectName);
                }

                if (module == null || !isCache(methodAPIBean)) {
                    module = buildView(methodAPIBean, null, projectName, valueMap, !isCache(methodAPIBean));
                }

                contextMap.put(url, module);
            }
            if (module != null) {
                //   this.perContext(module, module);
                module.getComponent().fillFormValues(valueMap, true);
            }

        }

        return module;
    }

    public EUModule createRealView(MethodConfig methodAPIBean, Class customClass, String projectName, Map<String, ?> valueMap, boolean isDyn) throws JDSException {
        EUModule module = null;
        try {
            ProjectVersion version = ESDFacrory.getAdminESDClient().getProjectVersionByName(projectName);
            String url = methodAPIBean.getUrl();
            boolean dyn = (isDyn || url.endsWith(".dyn"));
            if (dyn && !methodAPIBean.getName().endsWith(dynBuild)) {
                module = version.createModule(methodAPIBean.getPackageName() + "." + methodAPIBean.getName() + dynBuild);
                module = createModuleComponent(methodAPIBean, customClass, module, valueMap, false);
            } else {
                module = version.createCustomModule(url);
                module = createModuleComponent(methodAPIBean, customClass, module, valueMap, true);
            }


        } catch (Exception e) {
            e.printStackTrace();
            throw new JDSException(e);
        }
        return module;
    }

    public EUModule buildView(MethodConfig methodAPIBean, String projectName, Map<String, ?> valueMap, boolean dynBuild) throws JDSException {
        EUModule module = null;
        try {
            if (projectName == null) {
                projectName = DSMFactory.getInstance().getDefaultProjectName();
            }
            module = buildView(methodAPIBean, null, projectName, valueMap, dynBuild);
        } catch (Exception e) {
            e.printStackTrace();
            throw new JDSException(e);
        }
        return module;
    }


    public EUModule buildView(MethodConfig methodAPIBean, Class customClass, String projectName, Map<String, ?> valueMap, boolean dynBuild) throws JDSException {
        EUModule module = null;
        try {
            if (projectName == null) {
                projectName = DSMFactory.getInstance().getDefaultProjectName();
            }
            ProjectVersion version = ESDFacrory.getAdminESDClient().getProjectVersionByName(projectName);
            module = version.createCustomModule(methodAPIBean.getEUClassName());
            module = createModuleComponent(methodAPIBean, customClass, module, valueMap, dynBuild);
        } catch (Exception e) {
            e.printStackTrace();
            throw new JDSException(e);
        }
        return module;
    }


    public EUModule getView(String path, String projectName) throws JDSException {
        return getView(path, projectName, null);
    }

    public EUModule getView(EUModule euModule, String path, Map valueMap) throws JDSException {
        EUModule newmodule = null;

        RequestMethodBean bean = getMethodBeanByPath(path, euModule.getProjectVersion().getVersionName());

        if (bean == null) {
            bean = getMethodBeanByPath(euModule.getEuPackage().getPath() + path, euModule.getProjectVersion().getVersionName());
        }
        if (bean != null) {
            String domainId = null;
            DSMProperties dsmProperties = euModule.getComponent().getProperties().getDsmProperties();
            if (dsmProperties != null) {
                domainId = dsmProperties.getDomainId();
            }

            ApiClassConfig classConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(bean.getClassName());
            MethodConfig methodAPIBean = classConfig.getMethodByName(bean.getMethodName());
            newmodule = this.getViewByMethod(methodAPIBean, euModule.getProjectVersion().getVersionName(), valueMap);

        }
        return newmodule;
    }

    public EUModule getView(String path, String projectName, Map<String, ?> valueMap) throws JDSException {
        EUModule euModule = null;
        RequestMethodBean methodBean = getMethodBeanByPath(path, projectName);
        if (methodBean != null) {
            PackagePathType packagePathType = PackagePathType.startPath(path);
            DomainInst domainInst = null;
            if (packagePathType != null && !packagePathType.getApiType().equals(PackageType.userdef)) {
                domainInst = DSMFactory.getInstance().getDefaultDomain(packagePathType.getApiType().getDefaultProjectName(), packagePathType.getApiType().getCatType());
            } else {
                domainInst = DSMFactory.getInstance().getDefaultDomain(projectName, UserSpace.VIEW);
            }

            ApiClassConfig classConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(methodBean.getClassName());
            MethodConfig methodAPIBean = classConfig.getMethodByName(methodBean.getMethodName());
            euModule = getViewByMethod(methodAPIBean, projectName, valueMap);
        }
        return euModule;

    }


    public Set<EUModule> buildPackage(String projectName, String packageName, Map<String, ?> valueMap, ChromeProxy chrome) throws JDSException {
        if (chrome == null) {
            chrome = this.getCurrChromeDriver();
        }
        List<APIConfig> allConfigs = APIFactory.getInstance(ESDFacrory.getAdminESDClient().getSpace()).listAllAPIConfig(packageName);
        List<APIConfig> localConfigs = new ArrayList<>();
        ProjectVersion version = ESDFacrory.getAdminESDClient().getProjectVersionByName(projectName);
        PackageType[] userPackageTypes = version.getProject().getProjectType().getPackageTypes();
        for (APIConfig apiConfig : allConfigs) {
            PackagePathType packagePathType = PackagePathType.startPath(apiConfig.getUrl());
            if (packagePathType == null || Arrays.asList(userPackageTypes).contains(packagePathType.getApiType())) {
                localConfigs.add(apiConfig);
            }
        }


        Set<EUModule> moduleSet = new LinkedHashSet<>();
        if (chrome != null) {
            chrome.printLog("共发现：" + localConfigs.size() + "个 视图", true);
        }
        int k = 0;
        for (APIConfig apiConfig : localConfigs) {
            k = k + 1;
            ApiClassConfig classConfig = new ApiClassConfig(apiConfig);
            try {
                Set<EUModule> apiModules = buildModule(classConfig, projectName, valueMap);
                if (chrome != null) {
                    for (EUModule apiModule : apiModules) {
                        chrome.printLog("编译完成：" + apiModule.getPackageName() + "." + apiModule.getClassName() + "[" + apiModule.getComponent().getTitle() + "]", true);
                        apiModule.update(false);
                    }
                    chrome.printLog("已完成编译 " + k + "个 视图 剩余：" + (localConfigs.size() - k), true);
                }
                if (apiModules != null && apiModules.size() > 0) {
                    moduleSet.addAll(apiModules);
                }
                DSMFactory.getInstance().getAggregationManager().updateApiClassConfig(classConfig);
            } catch (Throwable e) {
                chrome.printLog(e.getMessage(), true);
            }


        }
        return moduleSet;
    }


    public Set<EUModule> buildProjectModule(String projectName, String packageName, String esdPackageName, Map<String, ?> valueMap, ChromeProxy chrome) throws JDSException {
        if (chrome == null) {
            chrome = this.getCurrChromeDriver();
        }
        List<APIConfig> allConfigs = APIFactory.getInstance(ESDFacrory.getAdminESDClient().getSpace()).getAPIConfigByEsdPackName(packageName, esdPackageName);
        List<APIConfig> localConfigs = new ArrayList<>();
        ProjectVersion version = ESDFacrory.getAdminESDClient().getProjectVersionByName(projectName);
        PackageType[] userPackageTypes = version.getProject().getProjectType().getPackageTypes();
        for (APIConfig apiConfig : allConfigs) {
            PackagePathType packagePathType = PackagePathType.startPath(apiConfig.getUrl());
            if (packagePathType == null || Arrays.asList(userPackageTypes).contains(packagePathType.getApiType())) {
                localConfigs.add(apiConfig);
            }
        }


        Set<EUModule> moduleSet = new LinkedHashSet<>();
        if (chrome != null) {
            chrome.printLog("共发现：" + localConfigs.size() + "个 视图", true);
        }
        int k = 0;
        for (APIConfig apiConfig : localConfigs) {
            k = k + 1;
            ApiClassConfig classConfig = new ApiClassConfig(apiConfig);
            Set<EUModule> apiModules = buildModule(classConfig, projectName, valueMap);
            if (chrome != null) {
                for (EUModule apiModule : apiModules) {
                    chrome.printLog("编译完成：" + apiModule.getPackageName() + "." + apiModule.getClassName() + "[" + apiModule.getComponent().getTitle() + "]", true);
                    try {
                        apiModule.update(false);
                    } catch (Throwable e) {
                        logger.warn("build " + apiModule.getClassName() + " error!" + e.getMessage());
                    }

                }
                chrome.printLog("已完成编译 " + k + "个 视图 剩余：" + (localConfigs.size() - k), true);
            }
            if (apiModules != null && apiModules.size() > 0) {
                moduleSet.addAll(apiModules);
            }


        }
        return moduleSet;
    }

    public Set<EUModule> getModuleByPackage(String projectName, String packageName, Map<String, ?> valueMap) throws JDSException {

        List<APIConfig> configs = APIFactory.getInstance(ESDFacrory.getAdminESDClient().getSpace()).getAPIConfigByPackageName(packageName);
        Set<EUModule> moduleSet = new LinkedHashSet<>();
        for (APIConfig apiConfig : configs) {
            ApiClassConfig classConfig = new ApiClassConfig(apiConfig);
            Set<EUModule> apiModules = buildModule(classConfig, projectName, valueMap);
            moduleSet.addAll(apiModules);
        }
        return moduleSet;
    }


    private Set<EUModule> buildModule(ApiClassConfig classConfig, String projectName, Map<String, ?> valueMap) throws JDSException {
        Set<EUModule> moduleSet = new LinkedHashSet<>();
        ProjectVersion version = ESDFacrory.getAdminESDClient().getProjectVersionByName(projectName);
        PackagePathType packagePathType = PackagePathType.startPath(classConfig.getUrl());
        PackageType[] userPackageTypes = version.getProject().getProjectType().getPackageTypes();
        if (packagePathType == null || Arrays.asList(userPackageTypes).contains(packagePathType.getApiType())) {
            List<MethodConfig> requestMethodBeans = classConfig.getAllMethods();
            for (MethodConfig bean : requestMethodBeans) {
                EUModule module = null;
                try {
                    module = this.getViewByMethod(bean, projectName, valueMap);
                    if (module != null) {
                        moduleSet.add(module);
                    }
                } catch (JDSException e) {
                    e.printStackTrace();
                }

            }
        }
        return moduleSet;
    }


    public void clear() {
        clearViewCache();
        clearMethod();
        methodConfigMap.clear();
    }

    public void reLoad() {
        clearViewCache();
        clearMethod();
        methodConfigMap.clear();
    }

    public void clearViewCache() {
        //    projectCache.clear();
    }

    public void clearMethod() {
        methodMap.clear();
    }


    public void clearFormValues(EUModule module) {
        if (module != null) {
            List<Component> allComponent = module.getComponent().getChildrenRecursivelyList();
            for (Component component : allComponent) {
                if (component.getProperties() instanceof FormField) {
                    FormField field = (FormField) component.getProperties();
                    field.setValue(null);
                }
            }
        }
    }

    private boolean isCache(MethodConfig methodAPIBean) {
        boolean dynCache = true;
        if (methodAPIBean.getModuleBean().getCache() != null) {
            dynCache = methodAPIBean.getModuleBean().getCache();
        }
        boolean viewCache = true;
        CustomData dataBean = methodAPIBean.getDataBean();
        if (dataBean != null && dataBean.getCache() != null) {
            viewCache = dataBean.getCache();
        }

        return dynCache || viewCache;
    }

    public MethodConfig getMethodAPIBean(String path, String projectName) throws JDSException {
        MethodConfig methodAPIBean = null;
        RequestMethodBean bean = getMethodBeanByPath(path, projectName);
        if (bean != null) {
            path = bean.getUrl();
            methodAPIBean = methodConfigMap.get(path);
            if (methodAPIBean == null) {
                ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(bean.getClassName());
                if (apiClassConfig != null) {
                    methodAPIBean = apiClassConfig.getMethodByName(bean.getMethodName());
                    if (methodAPIBean == null) {
                        AggEntityConfig entityConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(bean.getClassName(), false);
                        if (entityConfig != null) {
                            methodAPIBean = entityConfig.getMethodByName(bean.getMethodName());
                        }
                    }
                }
                methodConfigMap.put(path, methodAPIBean);
            }
        }
        return methodAPIBean;
    }


    public RequestMethodBean getMethodBeanByPath(String path, String projectName) throws JDSException {
        RequestMethodBean methodBean = null;
        if (path != null && !path.equals("")) {
            methodBean = APIConfigFactory.getInstance().getRequestMappingBean(path);
            if (methodBean == null) {
                path = formatPath(path, projectName);
                methodBean = methodMap.get(path);
                if (methodBean == null) {
                    methodBean = methodMap.get("/" + projectName + path);
                }
                if (methodBean == null) {
                    methodBean = APIConfigFactory.getInstance().getRequestMappingBean(path);
                }
            }

            if (methodBean != null) {
                methodMap.put(methodBean.getUrl(), methodBean);
                methodMap.put(path, methodBean);
            }
        }

        return methodBean;
    }


    public String formatPath(String className, String projectName) throws JDSException {
        String path = className;

        for (String suffix : Handler.pattArr) {
            if (path.endsWith(suffix)) {
                path = path.substring(0, path.lastIndexOf("."));
            }
        }

        path = path.replace(".", "/");
        if (path.startsWith("/")) {
            path = path.substring(1, path.length());
        }

        for (String ckey : Handler.CKEY) {
            String key = ckey + "/";
            if (path.startsWith(key)) {
                path = path.substring(key.length());
            }
        }

        if (projectName != null && JDSServer.getClusterClient().isLogin()) {
            ProjectVersion projectVersion = ESDFacrory.getAdminESDClient().getProjectVersionByName(projectName);
            if (path.startsWith(projectVersion.getPath())) {
                path = path.substring((projectVersion.getPath()).length());
            } else if (path.startsWith("/" + projectName + "/")) {
                path = path.substring(("/" + projectName + "/").length());
            } else if (path.startsWith(projectName + "/")) {
                path = path.substring((projectName + "/").length());
            }
        }

        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return path;

    }

    void saveModule(EUModule dynmodule, boolean dynBuild) throws JDSException {
        ESDFacrory.getAdminESDClient().saveModule(dynmodule, dynBuild);
    }


    public boolean isView(RequestMethodBean methodBean) throws ClassNotFoundException {
        Method method = methodBean.getSourceMethod();
        ModuleAnnotation moduleAnnotation = AnnotationUtil.getMethodAnnotation(method, ModuleAnnotation.class);
        if (moduleAnnotation != null) {
            CustomClass customClass = DSMAnnotationUtil.getMethodCustomClass(method, ModuleComponent.class);//method.getAnnotations();
            if (customClass != null) {
                return true;
            } else {
                this.logger.error("视图注解不完整[" + methodBean.getClassName() + "===》" + methodBean.getName());
            }
        }

        return false;
    }

    private CtVfsService getVfsClient() {
        CtVfsService vfsClient = CtVfsFactory.getCtVfsService();
        return vfsClient;
    }


    private Map<String, Object> perContext(EUModule topModule, EUModule module) {
        Map<String, Object> context = new HashMap<>();
        Map<String, Object> actionContext = JDSActionContext.getActionContext().getContext();
        EUModule contextTopModule = (EUModule) actionContext.get(TopModuleKey);
        if (contextTopModule != null) {
            topModule = contextTopModule;
        }

        if (topModule == null) {
            topModule = module;
        }


        actionContext.put(TopModuleKey, topModule);
        actionContext.put(CurrModuleKey, module);
        context.putAll(actionContext);

        actionContext.put(module.getClassName(), module);
        return context;

    }


    public ChromeProxy getCurrChromeDriver() {
        ChromeProxy chrome = JDSActionContext.getActionContext().Par("$currChromeDriver", ChromeProxy.class);
        if (chrome == null) {
            chrome = new LogSetpLog();
        }
        return chrome;
    }
}


