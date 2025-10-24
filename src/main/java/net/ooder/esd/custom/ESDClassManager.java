package net.ooder.esd.custom;

import net.ooder.annotation.Aggregation;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.ESDEntity;
import net.ooder.common.JDSConstants;
import net.ooder.common.JDSException;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.common.util.ClassUtility;
import net.ooder.esb.config.manager.EsbBeanFactory;
import net.ooder.esb.config.manager.ExpressionTempBean;
import net.ooder.esd.annotation.TreeAnnotation;
import net.ooder.esd.annotation.View;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.MySpace;
import net.ooder.server.JDSServer;
import net.ooder.web.RemoteConnectionManager;
import net.ooder.web.util.AnnotationUtil;
import net.ooder.web.util.MethodUtil;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.*;

public class ESDClassManager {
    private static final String THREAD_LOCK = "Thread Lock";


    private static final String DEFAULTDOMAIN = "default";

    protected Log log = LogFactory.getLog(JDSConstants.CONFIG_KEY, ESDClassManager.class);


    private final MySpace space;


    Set<String> aggMenuClassNames = new HashSet<>();

    Set<String> aggViewClassNames = new HashSet<>();

    Set<String> aggAPIClassNames = new HashSet<>();

    Set<String> aggRootClassNames = new HashSet<>();

    Set<String> aggDomainClassNames = new HashSet<>();


    Set<String> aggEntityClassNames = new HashSet<>();

    Set<String> entityClassNames = new HashSet<>();

    Map<String, Class> realClassMap = new ConcurrentHashMap<>();

    Map<String, ESDClass> allClassMap = new ConcurrentHashMap<>();

    Map<String, ESDClass> esdClassMap = new ConcurrentHashMap<>();
    //
    Map<String, ESDClass> repositoryClassMap = new ConcurrentHashMap<>();

    Map<String, CustomMethodInfo> customMethodInfoMap = new ConcurrentHashMap<>();


    private static Map<String, ESDClassManager> managerMap = new HashMap<String, ESDClassManager>();

    public static ESDClassManager getInstance() throws JDSException {
        MySpace space = ESDFacrory.getAdminESDClient().getSpace();
        return getInstance(space);
    }

    public static ESDClassManager getInstance(MySpace space) {

        String path = space.getPath();
        ESDClassManager manager = managerMap.get(path);
        if (manager == null) {
            synchronized (THREAD_LOCK) {
                if (manager == null) {
                    manager = new ESDClassManager(space);
                    managerMap.put(path, manager);
                }
            }
        }
        return manager;
    }


    ESDClassManager(MySpace space) {
        this.space = space;
        if (!JDSServer.getClusterClient().isLogin()) {
            reload();
        }

    }

    public void clear() {

        aggMenuClassNames.clear();
        aggRootClassNames.clear();
        aggEntityClassNames.clear();
        aggAPIClassNames.clear();
        aggViewClassNames.clear();
        aggDomainClassNames.clear();
        repositoryClassMap.clear();
        esdClassMap.clear();
    }

    public void removeRepositoryClass(String uKey) {
        repositoryClassMap.remove(uKey);
    }


    public void reload() {
        aggAPIClassNames.clear();
        aggViewClassNames.clear();
        aggMenuClassNames.clear();
        aggDomainClassNames.clear();
        aggEntityClassNames.clear();
        aggRootClassNames.clear();
        repositoryClassMap.clear();
        esdClassMap.clear();
        allClassMap.clear();
        Map<String, Class> classMap = new HashMap<>();
        classMap.putAll(EsbBeanFactory.getInstance().getAllClass());
        classMap.putAll(ClassUtility.getDynClassMap());
        Set<Map.Entry<String, Class>> allClass = classMap.entrySet();

        for (Map.Entry<String, Class> clazzEntry : allClass) {
            Class clazz = clazzEntry.getValue();
            try {
                if (!clazz.equals(Void.class) && !clazz.equals(Void.TYPE)) {
                    loadEntity(clazz.getName());
                }
            } catch (Exception e) {
                log.warn(e);
                e.printStackTrace();
            }
        }

        for (Map.Entry<String, Class> clazzEntry : allClass) {
            Class clazz = clazzEntry.getValue();
            try {
                // if (!clazz.equals(Void.class) && !clazz.equals(Void.TYPE) && !clazz.isInterface()) {
                if (!clazz.equals(Void.class) && !clazz.equals(Void.TYPE)) {
                    loadAggregation(clazz.getName());
                }
            } catch (Exception e) {
                log.warn(e);
                e.printStackTrace();
            }
        }

        for (Map.Entry<String, Class> clazzEntry : allClass) {
            Class clazz = clazzEntry.getValue();
            try {
                if (!clazz.equals(Void.class) && !clazz.equals(Void.TYPE) && !clazz.isInterface()) {
                    loadViewClass(clazz.getName());
                }
            } catch (Exception e) {
                log.warn(e);
                e.printStackTrace();
            }
        }


        List<Callable<ESDClass>> esdTasks = new ArrayList<>();

        List<Callable<ESDClass>> serviceTasks = new ArrayList<>();

        List<Callable<ESDClass>> treeItemTasks = new ArrayList<>();

        for (Map.Entry<String, Class> clazzEntry : allClass) {
            Class clazz = clazzEntry.getValue();
            try {
                if (!clazz.equals(Void.class) && !clazz.equals(Void.TYPE) && !clazz.isInterface()) {
                    AnnotationType viewClass = AnnotationUtil.getClassAnnotation(clazz, AnnotationType.class);
                    RequestMapping requestClass = AnnotationUtil.getClassAnnotation(clazz, RequestMapping.class);
                    if (viewClass != null && viewClass.annotationType().isAnnotation()) {
                        esdTasks.add(new InitViewClass<>(clazz.getName()));
                    } else if (requestClass != null) {
                        serviceTasks.add(new InitViewClass<>(clazz.getName()));
                    } else if (AnnotationUtil.getClassAnnotation(clazz, TreeAnnotation.class) != null) {
                        treeItemTasks.add(new InitViewClass<>(clazz.getName()));
                    }
                }
            } catch (Exception e) {
                log.warn(e);
                e.printStackTrace();
            }
        }
        log.info("--------------------------load RequestMappingClass[ " + esdTasks.size() + "]-------------------------------------------");
        invokTasks("esdTasks", esdTasks);
        log.info("--------------------------load serviceTasksClass[ " + esdTasks.size() + "]--------------------------");
        invokTasks("serviceTasks", serviceTasks);
        log.info("--------------------------load TreeAnnotationClass[ " + treeItemTasks.size() + "]--------------------------");
        invokTasks("treeItemTasks", treeItemTasks);


    }

    private void invokTasks(String taskName, List<Callable<ESDClass>> esdTasks) {
        List<Future<ESDClass>> domainFutures = null;
        try {
            RemoteConnectionManager.initConnection(taskName, esdTasks.size());
            ExecutorService executorService = RemoteConnectionManager.getConntctionService(taskName);
            domainFutures = executorService.invokeAll(esdTasks);
            for (Future<ESDClass> resultFuture : domainFutures) {
                try {
                    ESDClass result = resultFuture.get(200, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
            executorService.shutdownNow();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public List<ESDClass> getAllAggAPI() {
        List<ESDClass> esdClassList = new ArrayList<>();
        if (aggAPIClassNames.isEmpty()) {
            initEntityClass();
        }
        for (String className : aggAPIClassNames) {
            if (MethodUtil.checkType(className)) {
                ESDClass esdClass = esdClassMap.get(className);
                if (esdClass == null) {
                    esdClass = this.getAggEntityByName(className, false);
                    esdClassMap.put(className, esdClass);
                }
                if (esdClass != null && !esdClass.isProxy()) {
                    esdClassList.add(esdClass);
                }
            }
        }
        return esdClassList;
    }

    public List<ESDClass> getAllAggMenu() {
        List<ESDClass> esdClassList = new ArrayList<>();
        if (aggMenuClassNames.isEmpty()) {
            initEntityClass();
        }
        for (String className : aggMenuClassNames) {
            if (MethodUtil.checkType(className)) {
                ESDClass esdClass = esdClassMap.get(className);
                if (esdClass == null) {

                    esdClass = this.getAggEntityByName(className, false);
                    esdClassMap.put(className, esdClass);
                }
                if (esdClass != null && !esdClass.isProxy()) {
                    esdClassList.add(esdClass);
                }
            }
        }
        return esdClassList;
    }

    public List<ESDClass> getAllAggDomain() {
        List<ESDClass> esdClassList = new ArrayList<>();
        if (aggDomainClassNames.isEmpty()) {
            initEntityClass();
        }

        for (String className : aggDomainClassNames) {
            if (MethodUtil.checkType(className)) {
                ESDClass esdClass = esdClassMap.get(className);
                if (esdClass == null) {
                    esdClass = this.getAggEntityByName(className, false);
                    esdClassMap.put(className, esdClass);
                }
                if (esdClass != null && !esdClass.isProxy()) {
                    esdClassList.add(esdClass);
                }
            }
        }
        return esdClassList;
    }

    public CustomMethodInfo getMethodInfo(Method method, ESDClass esdClass) {
        String key = esdClass.getClassName() + "##" + method.getName() + method.getParameterTypes();
        CustomMethodInfo methodInfo = customMethodInfoMap.get(key);
        if (methodInfo == null) {
            methodInfo = new CustomMethodInfo(method, esdClass);
            customMethodInfoMap.put(key, methodInfo);
        }
        return methodInfo;
    }

    public List<ESDClass> getAllAggView() {
        List<ESDClass> esdClassList = new ArrayList<>();
        if (aggViewClassNames.isEmpty()) {
            initEntityClass();
        }
        for (String className : aggViewClassNames) {
            if (MethodUtil.checkType(className)) {
                ESDClass esdClass = esdClassMap.get(className);
                if (esdClass == null) {
                    esdClass = this.getAggEntityByName(className, false);
                    esdClassMap.put(className, esdClass);
                }
                if (esdClass != null && !esdClass.isProxy()) {
                    esdClassList.add(esdClass);
                }
            }
        }
        return esdClassList;
    }


    public List<ESDClass> getAllAggRoot() {
        List<ESDClass> esdClassList = new ArrayList<>();
        if (aggRootClassNames.isEmpty()) {
            initEntityClass();
        }
        for (String className : aggRootClassNames) {
            if (MethodUtil.checkType(className)) {
                ESDClass esdClass = esdClassMap.get(className);
                if (esdClass == null) {
                    esdClass = this.getAggEntityByName(className, false);
                    esdClassMap.put(className, esdClass);
                }
                if (esdClass != null && !esdClass.isProxy()) {
                    esdClassList.add(esdClass);
                }
            }
        }
        return esdClassList;
    }

    public List<ESDClass> getEntities() {
        List<ESDClass> esdClassList = new ArrayList<>();
        if (entityClassNames.isEmpty()) {
            initEntityClass();
        }
        for (String className : entityClassNames) {
            if (MethodUtil.checkType(className)) {
                ESDClass esdClass = esdClassMap.get(className);
                if (esdClass == null) {
                    esdClass = this.getRepositoryClass(className, false);
                    esdClassMap.put(className, esdClass);
                }
                if (esdClass != null && !esdClass.isProxy()) {
                    esdClassList.add(esdClass);
                }
            }
        }
        return esdClassList;

    }

    private void initEntityClass() {
        Collection<ESDClass> values = allClassMap.values();

        for (ESDClass esdClass : values) {
            try {
                this.loadAggregation(esdClass.getClassName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public List<ESDClass> getAggEntities() {
        List<ESDClass> esdClassList = new ArrayList<>();
        if (aggEntityClassNames.isEmpty()) {
            initEntityClass();
        }
        for (String className : aggEntityClassNames) {
            if (MethodUtil.checkType(className)) {
                ESDClass esdClass = esdClassMap.get(className);
                if (esdClass == null) {
                    esdClass = this.getAggEntityByName(className, false);
                    esdClassMap.put(className, esdClass);
                }
                if (esdClass != null && !esdClass.isProxy()) {
                    esdClassList.add(esdClass);

                }
            }
        }
        return esdClassList;

    }


    class InitViewClass<T> implements Callable<T> {
        private final String classNsme;

        public InitViewClass(String className) {
            this.classNsme = className;
        }

        @Override
        public T call() {
            ESDClass esdClass = initViewClass(classNsme);
            return (T) esdClass;
        }

    }


    public ESDClass getRepositoryClass(String className, boolean reload) {
        ESDClass esdClass = repositoryClassMap.get(className);
        if (MethodUtil.checkType(className)) {
            if (esdClass == null || reload) {
                esdClass = esdClassMap.get(className);
                if (esdClass == null) {
                    try {
                        Class aClass = ClassUtility.loadClass(className);
                        if (aClass != null) {
                            esdClass = new ESDClass(aClass);
                            repositoryClassMap.put(className, esdClass);
                        }
                    } catch (Throwable e) {
                        log.warn(e);
                        //e.printStackTrace();
                    }
                }

            }
        }
        return esdClass;
    }

    public List<ESDClass> getRepositoryClassList() {
        List<ESDClass> esdClassList = new ArrayList<>();
        repositoryClassMap.forEach((k, v) -> {
            if (!esdClassList.contains(v)) {
                esdClassList.add(v);
            }
        });
        return esdClassList;
    }

    public ESDClass initViewClass(String clazzName) {
        ESDClass esdClass = esdClassMap.get(clazzName);
        if (esdClass == null) {
            if (MethodUtil.checkType(clazzName)) {
                //synchronized (clazzName) {
                try {
                    Class clazz = ClassUtility.loadClass(clazzName);
                    esdClass = new ESDClass(clazz);
                    esdClassMap.put(clazzName, esdClass);
                } catch (Throwable e) {
                    e.printStackTrace();

                }

            }
            // }
        }
        return esdClass;
    }

    public ESDClass loadViewClass(String clazzName) throws ClassNotFoundException {

        ESDClass esdClass = esdClassMap.get(clazzName);

        if (MethodUtil.checkType(clazzName)) {
            Class clazz = ClassUtility.loadClass(clazzName);
            View viewClass = AnnotationUtil.getClassAnnotation(clazz, View.class);
            if (viewClass != null) {
                esdClass = new ESDClass(clazz);
                esdClassMap.put(clazzName, esdClass);
            }
        }

        return esdClass;
    }

    public ESDClass loadAggregation(String clazzName) throws ClassNotFoundException {
        ESDClass esdClass = esdClassMap.get(clazzName);
        //if (esdClass == null && clazzName.startsWith("net.ooder.)) {
        if (esdClass == null && MethodUtil.checkType(clazzName)) {
            Class clazz = ClassUtility.loadClass(clazzName);
            Aggregation entityClass = (Aggregation) clazz.getAnnotation(Aggregation.class);
            if (entityClass != null) {
                esdClass = new ESDClass(clazz);
                esdClassMap.put(clazzName, esdClass);
            }
            if (entityClass != null) {
                switch (entityClass.type()) {
                    case MODULE:
                        aggRootClassNames.add(entityClass.rootClass().getName());
                        break;
                    case MENU:
                        aggMenuClassNames.add(clazzName);
                        break;
                    case VIEW:
                        aggViewClassNames.add(clazzName);
                        break;
                    case API:
                        aggAPIClassNames.add(clazzName);
                        break;
                    case ENTITY:
                        aggEntityClassNames.add(clazzName);
                        break;
                    case DOMAIN:
                        aggDomainClassNames.add(clazzName);
                        break;
                    default:
                        aggAPIClassNames.add(clazzName);
                        break;

                }
            }
            //}
        }

        return esdClass;
    }


    public ESDClass loadEntity(String clazzName) throws ClassNotFoundException {
        ESDClass esdClass = esdClassMap.get(clazzName);
        if (MethodUtil.checkType(clazzName)) {
            Class clazz = ClassUtility.loadClass(clazzName);
            ESDEntity entity = (ESDEntity) clazz.getAnnotation(ESDEntity.class);
            if (esdClass == null) {
                if (entity != null) {
                    esdClass = new ESDClass(clazz);
                    esdClassMap.put(clazzName, esdClass);
                    repositoryClassMap.put(clazzName, esdClass);
                }
            }
            if (entity != null && esdClass != null) {
                entityClassNames.add(clazzName);
            }
        }

        return esdClass;
    }

    public void clear(String className) {
        if (MethodUtil.checkType(className)) {
            esdClassMap.remove(className);
            ClassUtility.clear(className);
        }

    }


    public Class checkInterface(String className) {
        Class clazz = realClassMap.get(className);
        try {
            if (clazz == null) {
                clazz = ClassUtility.loadClass(className);
                if (clazz != null && clazz.isInterface()) {
                    ExpressionTempBean expressionTempBean = EsbBeanFactory.getInstance().getDefaultServiceBean(clazz);
                    if (expressionTempBean != null) {
                        className = EsbBeanFactory.getInstance().getDefaultServiceBean(clazz).getClazz();
                        clazz = ClassUtility.loadClass(className);
                    }
                }
                if (clazz != null) {
                    realClassMap.put(className, clazz);
                }
            }

        } catch (Throwable e) {
            log.warn(className + " not find...");
        }
        return clazz;

    }


    public ESDClass getAggEntityByName(String className, boolean reload) {
        ESDClass esdClass = null;
        if (MethodUtil.checkType(className)) {
            esdClass = esdClassMap.get(className);
            if (esdClass == null || reload) {
                synchronized (className) {
                    try {
                        long start = System.currentTimeMillis();
                        Class aClass = ClassUtility.loadClass(className);
                        // log.info("end  ClassUtility.loadClass  ---end= " + className + "[" + className + "] times=" + (System.currentTimeMillis() - start));
                        if (aClass != null) {
                            esdClass = new ESDClass(aClass);
                            esdClassMap.put(className, esdClass);
                        }
                    } catch (Throwable e) {
                        log.warn(e);
                        this.clear(className);
                    }
                }
            }
        }

        return esdClass;
    }


}
