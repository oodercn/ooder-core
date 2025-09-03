package net.ooder.esd.manager.plugins.api;

import javassist.NotFoundException;
import net.ooder.cluster.ServerNode;
import net.ooder.common.JDSConstants;
import net.ooder.common.JDSException;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.common.util.ClassUtility;
import net.ooder.common.util.StringUtility;
import net.ooder.esb.config.manager.EsbBeanFactory;
import net.ooder.esb.config.manager.ServiceBean;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.MySpace;
import net.ooder.esd.engine.ProjectCacheManager;
import net.ooder.esd.engine.inner.INProject;
import net.ooder.esd.engine.inner.INProjectVersion;
import net.ooder.esd.manager.plugins.api.enums.APIPathType;
import net.ooder.esd.manager.plugins.api.enums.APIType;
import net.ooder.esd.manager.plugins.api.node.APIPaths;
import net.ooder.esd.manager.plugins.api.node.APIServer;
import net.ooder.esd.manager.plugins.api.node.OODAPIConfig;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.properties.APICallerProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.server.JDSServer;
import net.ooder.server.SubSystem;
import net.ooder.vfs.ct.CtVfsFactory;
import net.ooder.vfs.ct.CtVfsService;
import net.ooder.web.APIConfig;
import net.ooder.web.APIConfigFactory;
import net.ooder.web.RequestMethodBean;
import net.ooder.web.util.SpringPlugs;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class APIFactory {

    private static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, APIFactory.class);

    private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

    private final ProjectCacheManager projectCacheManager;

    Map<String, APICallerProperties> apiMethodMap = new HashMap<String, APICallerProperties>();

    Map<String, List<Class>> classCache = new HashMap<String, List<Class>>();

    Map<String, APIPaths> pathConfigMap = new HashMap<String, APIPaths>();

    Map<String, SubSystem> serverConfigMap = new HashMap<String, SubSystem>();

    private final MySpace space;

    public static final String IFontMenuJsonFileName = "IServiceJson.json";

    public static final String LoclHostName = "localhost";


    public static final String THREAD_LOCK = "Thread Lock";

    static Map<String, APIFactory> managerMap = new HashMap<String, APIFactory>();

    private Map<String, RequestMethodBean> methodMap = new HashMap<String, RequestMethodBean>();


    public static APIFactory getInstance() throws JDSException {
        MySpace space = ESDFacrory.getAdminESDClient().getSpace();
        return getInstance(space);
    }


    public APICallerComponent getFristBindAPICall(APIConfig apiConfig, CustomMenuItem item, ComponentType[] bindTypes) {
        APICallerComponent apiCallerComponent = null;
        List<APICallerComponent> apiCallers = getBindAPICalls(apiConfig, item, bindTypes);
        if (apiCallers.size() > 0) {
            apiCallerComponent = apiCallers.get(0);
        }
        return apiCallerComponent;
    }

    public static List<APICallerComponent> getAllApiCall(APIConfig apiConfig) {
        List<APICallerComponent> apiCallerComponents = new ArrayList<>();
        List<RequestMethodBean> requestMethodBeans = apiConfig.getMethods();
        for (RequestMethodBean requestMethodBean : requestMethodBeans) {
            apiCallerComponents.add(new APICallerComponent(requestMethodBean));
        }
        return apiCallerComponents;
    }


    public static List<APICallerComponent> getBindAPICalls(APIConfig apiConfig, CustomMenuItem item, ComponentType[] bindTypes) {
        List<APICallerComponent> apiCallers = new ArrayList<>();
        List<APICallerComponent> apiCallerComponents = getAllApiCall(apiConfig);
        for (APICallerComponent apiCallerComponent : apiCallerComponents) {
            Set<CustomMenuItem> customMenu = apiCallerComponent.getProperties().getBindMenu();
            if (customMenu.contains(item)) {
                if (checkComponentType(bindTypes, item.getBindTypes())) {
                    apiCallers.add(apiCallerComponent);
                }
            }
        }
        return apiCallers;
    }

    public static boolean checkComponentType(ComponentType[] currComTypes, ComponentType[] componentTypes) {
        if (componentTypes == null || componentTypes.length == 0) {
            return true;
        }
        for (ComponentType currComType : currComTypes) {
            for (ComponentType type : componentTypes) {
                if (type.equals(currComType)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Method bindUrl(String methodName, Object object, RequestMethodBean methodBean) {

        Method method = null;
        try {
            method = object.getClass().getMethod(StringUtility.getSetMethodName(methodName), new Class[]{String.class});
        } catch (NoSuchMethodException e) {
        }
        try {
            if (method == null) {
                method = object.getClass().getDeclaredMethod(StringUtility.getSetMethodName(methodName), new Class[]{String.class});
            }
        } catch (NoSuchMethodException e) {

        }
        if (method != null) {
            OgnlUtil.setProperty(methodName, methodBean.getUrl(), object, null);
        }
        return method;
    }


    public List<APICallerComponent> getBindAPICall(APIConfig apiConfig, CustomMenuItem item) {
        List<APICallerComponent> apiCallers = new ArrayList<>();
        List<APICallerComponent> apiCallerComponents = this.getAllApiCall(apiConfig);
        for (APICallerComponent apiCallerComponent : apiCallerComponents) {
            Set<CustomMenuItem> customMenu = apiCallerComponent.getProperties().getBindMenu();
            if (customMenu.contains(item)) {
                apiCallers.add(apiCallerComponent);
            }
        }
        return apiCallers;
    }

    public static APIFactory getInstance(MySpace space) {
        String path = space.getPath();
        APIFactory manager = managerMap.get(path);
        if (manager == null) {
            synchronized (path) {
                manager = managerMap.get(path);
                if (manager == null) {
                    manager = new APIFactory(space);
                    managerMap.put(path, manager);
                }
            }
        }

        return manager;
    }

    public void reload() {
        apiMethodMap.clear();
        classCache.clear();
        pathConfigMap.clear();
        serverConfigMap.clear();
        methodMap.clear();


        initLocal();
        initServer();

    }

    public APIConfig loadApiConfig(String className) {
        APIConfig config = null;
        try {
            config = APIConfigFactory.getInstance().getAPIConfig(className);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        if (config != null && config.getUrl() != null && !config.getUrl().equals("")) {
            initApiConfig(null, config);
        }
        return config;
    }

    public void dyReload(Set<Class<?>> classes) {
        for (Class clazz : classes) {
            APIConfig config = null;
            try {
                config = APIConfigFactory.getInstance().getAPIConfig(clazz.getName());
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
            if (config != null && config.getUrl() != null && !config.getUrl().equals("")) {
                initApiConfig(null, config);
            }
        }


    }


    APIFactory(MySpace space) {
        this.space = space;
        this.projectCacheManager = ProjectCacheManager.getInstance(space);
        this.reload();

    }

    void buildTemp(INProject project) {

    }

    void fillPath(APIPaths paths) {
        String parentPath = paths.getParentPath();
        if (parentPath != null) {
            APIPaths parentPaths = this.getAPIPaths(parentPath);
            if (parentPaths == null) {
                parentPaths = new APIPaths(parentPath);
                this.pathConfigMap.put(parentPaths.getPath(), parentPaths);
                this.fillPath(parentPaths);
            }
            if (parentPath != null && !parentPath.equals(paths.getPath())) {
                parentPaths.addChild(paths.getPath());
            }

        }


    }

    void initApiConfig(String serverId, APIConfig config) {
        if (serverId == null) {
            serverId = LoclHostName;
        }

        APIPaths beanPaths = new APIPaths(serverId, config);
        APIPaths otherConfig = this.pathConfigMap.get(beanPaths.getPath());
        if (otherConfig != null) {
            otherConfig.getApiConfigs().add(config);
            if (config.getChinaName() != null) {
                otherConfig.setDesc(config.getDesc());
                otherConfig.setImageClass(config.getImageClass());
            }
        } else {
            this.pathConfigMap.put(beanPaths.getPath(), beanPaths);
        }

        fillPath(beanPaths);
        for (RequestMethodBean methodBean : config.getMethods()) {
            APICallerProperties properties = new APICallerProperties(methodBean);
            APIPaths paths = new APIPaths(beanPaths.getSystemId(), properties);
            beanPaths.addChild(paths.getPath());
            this.pathConfigMap.put(paths.getPath(), paths);
            this.methodMap.put(paths.getPath(), methodBean);
            fillPath(beanPaths);
            apiMethodMap.put(paths.getPath(), properties);
        }
    }

    void initLocal() {
        List<APIConfig> apiConfigs = scannerAPIConfig(null, null);
        for (APIConfig config : apiConfigs) {
            if (config != null) {
                this.initApiConfig(null, config);
            }
        }
    }


    void initServer() {
        List<APIServer> apiServers = new ArrayList<APIServer>();
        List<ServerNode> serverNodes = JDSServer.getClusterClient().getAllServer();
        for (ServerNode bean : serverNodes) {
            initServerPath(bean);
        }
    }

    void initServerPath(ServerNode serverNode) {
        APIPaths serverPaths = new APIPaths(new APIServer(serverNode));
        this.pathConfigMap.put(serverPaths.getPath(), serverPaths);
        Set<? extends ServiceBean> beans = serverNode.getServices();
        for (ServiceBean serviceBean : beans) {
            APIConfig config = null;
            try {
                config = APIConfigFactory.getInstance().getAPIConfig(serviceBean.getClazz());
                initApiConfig(serverNode.getId(), config);
            } catch (NotFoundException e) {
                logger.warn("class load err [" + serviceBean.getId() + "]" + e.getMessage());
                //e.printStackTrace();
            }

        }
    }

    public List<APIPaths> getAPIServicesByPath(String pattern) {
        List<APIPaths> apiPaths = new ArrayList<APIPaths>();
        Set<String> keySet = pathConfigMap.keySet();
        for (String path : keySet) {
            APIPaths paths = pathConfigMap.get(path);

            if (pattern != null && !pattern.equals("")) {
                Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
                Matcher matcher = p.matcher(path);
                if (matcher.find()) {
                    apiPaths.add(paths);
                }
            } else {
                apiPaths.add(paths);
            }
        }
        return apiPaths;
    }

    public List<APIPaths> getAllAPIServices(String pattern) {
        List<APIPaths> apiPaths = new ArrayList<APIPaths>();
        Set<String> keySet = pathConfigMap.keySet();
        for (String path : keySet) {
            APIPaths paths = pathConfigMap.get(path);

            if (pattern != null && !pattern.equals("")) {
                Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
                Matcher matcher = p.matcher(path);
                if (matcher.find()) {
                    apiPaths.add(paths);
                }
            } else {
                apiPaths.add(paths);
            }
        }
        return apiPaths;
    }


    public List<APIPaths> scannerAPIPaths(String pattern) {
        List<APIPaths> apiPaths = new ArrayList<APIPaths>();
        Set<String> keySet = pathConfigMap.keySet();
        for (String path : keySet) {
            APIPaths paths = pathConfigMap.get(path);
            if (pattern != null && !pattern.equals("")) {
                Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
                Matcher matcher = p.matcher(path);
                if (matcher.find()) {
                    apiPaths.add(paths);
                }
            } else {
                apiPaths.add(paths);
            }
        }
        return apiPaths;
    }

    public RequestMethodBean getMethodBeanByPath(String path) {
        RequestMethodBean bean = methodMap.get(path);
        if (bean == null) {
            bean = methodMap.get(APIFactory.LoclHostName + ":" + path);
        }
        if (bean == null) {
            path = StringUtility.replace(path, ".", "/");
            bean = methodMap.get(path);
        }

        return bean;
    }

    private List<APIServer> getAllAPIServer() {
        List<APIServer> apiServers = new ArrayList<APIServer>();
        List<ServerNode> serviceBeans = JDSServer.getClusterClient().getAllServer();
        for (ServerNode bean : serviceBeans) {
            apiServers.add(new APIServer(bean));
        }
        return apiServers;
    }

    public List<APIPaths> getAPITopPaths(String pattern, APIType apiType) {
        APIPathType[] pathTypes = APIPathType.listType(apiType);
        List<APIPaths> topPaths = new ArrayList<APIPaths>();
        for (APIPathType apiPathType : pathTypes) {
            APIPaths paths = getAPIPaths(APIFactory.LoclHostName + ":" + apiPathType.getPattern());
            if (paths == null) {
                paths = new APIPaths(apiPathType);
            } else {
                paths.setDesc(apiPathType.getDesc());
                paths.setImageClass(apiPathType.getImageClass());
                paths.setPattern(pattern);
            }
            topPaths.add(paths);
        }
        return topPaths;

    }

    ;


    public Map<String, APICallerProperties> getApiMethodMap() {
        return apiMethodMap;
    }

    public void setApiMethodMap(Map<String, APICallerProperties> apiMethodMap) {
        this.apiMethodMap = apiMethodMap;
    }

    public APIPaths getAPIPaths(String path) {
        APIPaths paths = pathConfigMap.get(path);
        if (paths == null) {
            paths = pathConfigMap.get(APIFactory.LoclHostName + ":" + path);
        }
        return paths;
    }

    public CtVfsService getVfsClient() {
        CtVfsService vfsClient = CtVfsFactory.getCtVfsService();
        return vfsClient;
    }


    public List<APICallerProperties> scannerAPIMethodConfig(String packageName, String pattern) {
        List<APIConfig> configs = this.scannerAPIConfig(packageName, pattern);
        List<APICallerProperties> methodConfigs = new ArrayList<APICallerProperties>();
        for (APIConfig config : configs) {
            methodConfigs.addAll(this.getMethodConfigByConfig(config, pattern));
        }
        return methodConfigs;
    }

    public APICallerProperties getAPICallerPropertieByPath(String path) {
        RequestMethodBean bean = this.getMethodBeanByPath(path);
        APICallerProperties properties = new APICallerProperties(bean);
        return properties;
    }

    public List<APICallerProperties> getAPIMethodConfigByProject(String projectName) throws JDSException {
        INProjectVersion projectVersion = projectCacheManager.getProjectVersionByName(projectName);
        List<String> apiFitle = projectVersion.getProject().getConfig().getApiFilter();
        List<APICallerProperties> configs = scannerAPIMethodConfig(null, null);
        List<APICallerProperties> apiMethodConfigs = new ArrayList<APICallerProperties>();
        for (APICallerProperties apiMethodConfig : configs) {
            if (apiFitle.contains(APIFactory.LoclHostName + ":" + apiMethodConfig.getQueryURL())) {
                apiMethodConfigs.add(apiMethodConfig);
            }
        }
        return apiMethodConfigs;
    }


    public List<OODAPIConfig> searchLocalService(String projectName, String pattern) throws JDSException {
        List<OODAPIConfig> configs = new ArrayList<OODAPIConfig>();
        List<APIConfig> configsList = scannerAPIConfig(null, null);
        List<String> apiFitle = new ArrayList<String>();
        if (projectName != null) {
            INProjectVersion projectVersion = projectCacheManager.getProjectVersionByName(projectName);
            apiFitle = projectVersion.getProject().getConfig().getApiFilter();
        }

        for (APIConfig config : configsList) {
            OODAPIConfig classConfig = new OODAPIConfig(config.getName(), "-----------------------" + config.getDesc() + "----------------------------", true);
            List<OODAPIConfig> apiconfigs = new ArrayList<OODAPIConfig>();
            if (pattern != null && !pattern.equals("")) {
                Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
                List<RequestMethodBean> methods = config.getMethods();
                for (RequestMethodBean methodBean : methods) {
                    OODAPIConfig oodapiConfig = new OODAPIConfig(methodBean);
                    Matcher matcher = p.matcher(oodapiConfig.getId());
                    if (matcher.find()) {
                        if (!configs.contains(oodapiConfig)) {
                            // if (apiFitle.contains(APIFactory.LoclHostName + ":" + oodapiConfig.getId())) {
                            apiconfigs.add(oodapiConfig);
                            //}
                        }
                    }
                    // configs.add(oodapiConfig);
                }
            } else {
                configs.add(classConfig);
                List<RequestMethodBean> methods = config.getMethods();
                for (RequestMethodBean methodBean : methods) {
                    OODAPIConfig oodapiConfig = new OODAPIConfig(methodBean);
                    if (!configs.contains(oodapiConfig)) {
                        if (apiFitle.contains(APIFactory.LoclHostName + ":" + oodapiConfig.getId())) {
                            apiconfigs.add(oodapiConfig);
                        }
                    }
                }
            }

            if (!configs.contains(classConfig)) {
                if (apiconfigs.size() > 0) {
                    configs.add(classConfig);
                    configs.addAll(apiconfigs);
                }
            }

        }
        return configs;
    }


    public List<APIConfig> getAPIConfigByProject(String projectName) throws JDSException {
        INProjectVersion projectVersion = projectCacheManager.getProjectVersionByName(projectName);
        List<String> apiFitle = projectVersion.getProject().getConfig().getApiFilter();
        List<APIConfig> configs = scannerAPIConfig(null, null);
        List<APIConfig> apiConfigs = new ArrayList<APIConfig>();
        for (APIConfig apiConfig : configs) {
            if (apiFitle.contains(apiConfig.getUrl())) {
                apiConfigs.add(apiConfig);
            }
        }
        return apiConfigs;
    }

    public List<APIPaths> getAPIPathsByProject(String projectName) throws JDSException {
        INProjectVersion projectVersion = projectCacheManager.getProjectVersionByName(projectName);
        List<String> apiFitle = projectVersion.getProject().getConfig().getApiFilter();
        List<APIPaths> apiPaths = new ArrayList<APIPaths>();
        Set<String> keySet = pathConfigMap.keySet();
        for (String path : keySet) {
            APIPaths paths = pathConfigMap.get(path);
            if (paths.getSource() instanceof APICallerProperties && apiFitle.contains(path)) {
                apiPaths.add(paths);
            }
        }
        return apiPaths;
    }

    public List<APIConfig> listAllAPIConfig(String packageName) {
        List<Class> classList = this.scannerLocalClass(null, true);
        List<APIConfig> configs = new ArrayList<APIConfig>();
        for (Class clazz : classList) {
            try {
                APIConfig config = APIConfigFactory.getInstance().getAPIConfig(clazz.getName());
                if (config != null && config.getPackageName() != null && config.getPackageName().startsWith(packageName)) {
                    configs.add(config);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return configs;
    }


    public List<APIConfig> getAPIConfigByPackageName(String packageName) {
        List<Class> classList = this.scannerLocalClass(null, true);
        List<APIConfig> configs = new ArrayList<APIConfig>();
        for (Class clazz : classList) {
            try {
                APIConfig config = APIConfigFactory.getInstance().getAPIConfig(clazz.getName());
                if (config != null && config.getPackageName() != null && config.getPackageName().equals(packageName)) {
                    configs.add(config);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return configs;
    }


    public List<APIConfig> getAPIConfigByEsdPackName(String packageName, String esdPackageName) {
        if (packageName == null || packageName.equals("")) {
            packageName = "net.ooder.";
        }
        List<Class> classList = this.scannerLocalClass(packageName, true);
        List<APIConfig> configs = new ArrayList<APIConfig>();
        for (Class clazz : classList) {
            try {
                APIConfig config = APIConfigFactory.getInstance().getAPIConfig(clazz.getName());
                if (config != null && config.getPackageName() != null && (esdPackageName == null || config.getPackageName().startsWith(esdPackageName))) {
                    configs.add(config);
                }

            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return configs;
    }

    public List<APIConfig> scannerAPIConfig(String packageName, String pattern) {
        List<Class> classList = this.scannerLocalClass(packageName, true);
        List<APIConfig> configs = new ArrayList<APIConfig>();
        for (Class clazz : classList) {
            try {
                APIConfig config = APIConfigFactory.getInstance().getAPIConfig(clazz.getName());
                if (config != null && config.getUrl() != null && !config.getUrl().equals("")) {
                    if (pattern != null && !pattern.equals("")) {
                        Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
                        Matcher matcher = p.matcher(config.getUrl());
                        if (matcher.find()) {
                            configs.add(config);
                        }
                    } else {
                        configs.add(config);
                    }
                }

            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return configs;
    }


    public List<APICallerProperties> getMethodConfigByConfig(APIConfig config, String pattern) {
        List<APICallerProperties> methodConfigs = new ArrayList<APICallerProperties>();
        for (RequestMethodBean methodBean : config.getMethods()) {
            if (pattern != null && !pattern.equals("")) {
                Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
                Matcher matcher = p.matcher(methodBean.getUrl());
                if (matcher.find()) {
                    APICallerProperties properties = new APICallerProperties(methodBean);
                    methodConfigs.add(properties);
                }
            } else {
                APICallerProperties properties = new APICallerProperties(methodBean);
                methodConfigs.add(properties);
            }
        }
        return methodConfigs;
    }

    private List<Class> scannerLocalClass(String packageName, boolean reload) {
        if (packageName == null) {
            packageName = "net.ooder.";
        }
        synchronized (packageName) {
            Map<String, Class> classMap = new HashMap<>();
            List<Class> packageClassList = new ArrayList<>();
            List<Class> classList = classCache.get(packageName);
            if (classList == null || reload) {
                classList = new ArrayList<Class>();
                classList.addAll(this.scannerPackages(new String[]{packageName}));
                classCache.put(packageName, classList);
            }
            for (Class clazz : classList) {
                classMap.put(clazz.getName(), clazz);
            }

            classMap.putAll(ClassUtility.getDynClassMap());
            classMap.putAll(ClassUtility.getFileClassMap());
            Set<Map.Entry<String, Class>> dynClassSet = classMap.entrySet();
            for (Map.Entry<String, Class> classEntry : dynClassSet) {
                packageClassList.add(classEntry.getValue());
            }

            return packageClassList;
        }

    }


    /**
     * 根据包路径获取包及子包下的所有类
     *
     * @param basePackages basePackage
     */
    private Set<Class<?>> scannerPackages(String[] basePackages) {
        Set<Class<?>> classSet = new LinkedHashSet<Class<?>>();
        if (RequestContextHolder
                .getRequestAttributes() != null) {
            HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes()).getRequest();
            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(httpServletRequest.getServletContext());
            SpringPlugs springPlugs = webApplicationContext.getBean(SpringPlugs.class);
            classSet = springPlugs.scannerPackages(basePackages);
        }
        Set<Map.Entry<String, Class<?>>> allClass = EsbBeanFactory.getInstance().getAllClass().entrySet();
        for (Map.Entry<String, Class<?>> clazzEntry : allClass) {
            Class clazz = clazzEntry.getValue();
            if (clazz != null) {
                for (String basePackage : basePackages) {
                    if (clazz.getName().startsWith(basePackage)) {
                        classSet.add(clazz);
                        continue;
                    }
                }

            }
        }
        return classSet;
    }

    private List<OODAPIConfig> getMethodConfig(String className) {
        List<OODAPIConfig> configs = new ArrayList<OODAPIConfig>();
        try {
            APIConfig config = APIConfigFactory.getInstance().getAPIConfig(className);
            List<RequestMethodBean> methods = config.getMethods();
            for (RequestMethodBean methodBean : methods) {
                OODAPIConfig oodapiConfig = new OODAPIConfig(methodBean);
                configs.add(oodapiConfig);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return configs;
    }


}


