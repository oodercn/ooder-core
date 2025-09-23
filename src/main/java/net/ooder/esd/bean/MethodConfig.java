package net.ooder.esd.bean;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.*;
import net.ooder.common.JDSConstants;
import net.ooder.common.JDSException;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.common.util.ClassUtility;
import net.ooder.common.util.StringUtility;
import net.ooder.esd.annotation.*;
import net.ooder.esd.annotation.event.*;
import net.ooder.esd.annotation.field.ModuleRefFieldAnnotation;
import net.ooder.esd.annotation.menu.CustomFormMenu;
import net.ooder.esd.annotation.menu.CustomGalleryMenu;
import net.ooder.esd.annotation.menu.GridMenu;
import net.ooder.esd.annotation.menu.TreeMenu;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.view.DynLoadAnnotation;
import net.ooder.esd.bean.data.CustomDataBean;
import net.ooder.esd.bean.data.CustomDynDataBean;
import net.ooder.esd.bean.field.CustomFieldBean;
import net.ooder.esd.bean.view.CustomModuleBean;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.custom.CustomMethodInfo;
import net.ooder.esd.custom.CustomViewConfigFactory;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.dsm.aggregation.AggregationManager;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.aggregation.FieldAggConfig;
import net.ooder.esd.dsm.aggregation.context.MethodRoot;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.temp.JavaTemp;
import net.ooder.esd.dsm.view.ViewEntityConfig;
import net.ooder.esd.dsm.view.ViewInst;
import net.ooder.esd.engine.ESDClient;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.util.DSMAnnotationUtil;
import net.ooder.esd.util.OODUtil;
import net.ooder.esd.util.json.CustomDataDeserializer;
import net.ooder.esd.util.json.CustomViewDeserializer;
import net.ooder.web.APIConfigFactory;
import net.ooder.web.RequestMappingBean;
import net.ooder.web.RequestMethodBean;
import net.ooder.web.RequestParamBean;
import net.ooder.web.util.AnnotationUtil;
import net.ooder.web.util.JSONGenUtil;
import net.ooder.web.util.MethodUtil;
import javassist.NotFoundException;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class MethodConfig<T extends CustomViewBean, K extends CustomDataBean> implements Comparable<MethodConfig> {

    private static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, MethodConfig.class);

    public static String DefaultImageClass = "fa-solid fa-code";

    String imageClass = DefaultImageClass;

    Integer index = -1;

    String fieldName;

    String name;

    String id;

    String domainId;

    String url;

    String caption;

    String methodName;

    String expression;

    String filter;

    String itemsExpression;

    String metaInfo;

    String metaProxyInfo;

    String body;

    RefType refType;

    String viewClassName;

    String sourceClassName;

    CustomLayoutItemBean layoutItem;

    CustomDynDataBean dynDataBean;

    String realViewClassName;

    Boolean responseBody = true;

    Boolean requestBody = false;

    RequestMappingBean requestMapping;//= new RequestMappingBean();

    Set<String> serviceClassNames = new LinkedHashSet<>();

    Set<String> javaTempIds = new LinkedHashSet<>();

    Set<CustomMenuItem> bindMenus = new LinkedHashSet<>();

    Set<CustomFormMenu> formMenus = new LinkedHashSet<>();

    Set<CustomGalleryMenu> galleryMenus = new LinkedHashSet<>();

    Set<TreeMenu> treeMenus = new LinkedHashSet<>();

    Set<GridMenu> gridMenus = new LinkedHashSet<>();

    LinkedHashSet<RequestParamBean> paramSet = new LinkedHashSet<>();

    Map tagVar = new HashMap<>();

    CustomRefBean refBean;

    Boolean publicMethod;

    CustomAPICallBean api;

    CustomFieldBean fieldBean;

    RouteMenuBean routeMenuBean;


    @JSONField(serialize = false)
    Method method;

    @JSONField(deserializeUsing = CustomDataDeserializer.class)
    K dataBean;

    @JSONField(serialize = false)
    CustomMethodInfo customMethodInfo;

    @JSONField(serialize = false)
    RequestMethodBean requestMethodBean;

    @JSONField(serialize = false)
    ApiClassConfig sourceClassConfig;

    @JSONField(serialize = false)
    ESDClass esdClass;

    @JSONField(deserializeUsing = CustomViewDeserializer.class)
    T view;

    @JSONField(serialize = false)
    List<CustomBean> annotationBeans;

    @JSONField(serialize = false)
    String parentEuPackage;


    public MethodConfig() {
    }

    public MethodConfig(Method method, CustomMenuItem menuItem, ApiClassConfig sourceClassConfig) {
        this.domainId = sourceClassConfig.getDomainId();
        this.sourceClassConfig = sourceClassConfig;
        String requestUrl = method.getName();
        if (menuItem.getDefaultView()) {
            requestUrl = StringUtility.formatUrl(requestUrl);
        }
        requestBody = menuItem.getRequestBody();

        if (this.getParamSet().size() == 1) {
            Class clazz = this.getParamSet().iterator().next().getParamClass();
            if (!MethodUtil.checkType(clazz.getSimpleName()) && !clazz.equals(String.class)) {
                requestBody = true;
            }
        }
        RequestMappingBean requestMappingBean = new RequestMappingBean(requestUrl, sourceClassConfig.getUrl());
        this.requestMethodBean = new RequestMethodBean(method, requestMappingBean, domainId);
        init(requestMethodBean);
    }

    public MethodConfig(Method method, ApiClassConfig sourceClassConfig) {
        this.domainId = sourceClassConfig.getDomainId();
        this.sourceClassConfig = sourceClassConfig;
        String requestUrl = method.getName();
        requestUrl = StringUtility.formatUrl(requestUrl);
        RequestMappingBean requestMappingBean = new RequestMappingBean(requestUrl, sourceClassConfig.getUrl());
        this.requestMethodBean = new RequestMethodBean(method, requestMappingBean, domainId);
        init(requestMethodBean);
    }


    public MethodConfig(CustomMethodInfo customMethodInfo) {
        this.method = customMethodInfo.getInnerMethod();
        this.methodName = method.getName();
        this.index = customMethodInfo.getIndex();
        this.fieldName = customMethodInfo.getFieldName();
        this.layoutItem = customMethodInfo.getLayoutItemBean();
        try {
            initView(method);
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }


    public MethodConfig(CustomMethodInfo customMethodInfo, String path) {
        this.domainId = customMethodInfo.getDomainId();
        this.method = customMethodInfo.getInnerMethod();
        this.name = customMethodInfo.getName();
        this.methodName = method.getName();
        String urlName = name;
        if (customMethodInfo.isModule()) {
            urlName = StringUtility.formatUrl(urlName);
        } else {
            urlName = methodName;
        }
        this.fieldName = customMethodInfo.getFieldName();
        this.index = customMethodInfo.getIndex();
        this.sourceClassName = method.getDeclaringClass().getName();
        this.layoutItem = customMethodInfo.getLayoutItemBean();
//        if (moduleBean == null) {
//            moduleBean = new CustomModuleBean(customMethodInfo);
//        }
        if (customMethodInfo.getViewType() != null) {
            //  moduleBean.setModuleViewType(ModuleViewType.getModuleViewByViewType(customMethodInfo.getViewType()));
            this.requestMapping = new RequestMappingBean(urlName, path);
            this.requestMethodBean = new RequestMethodBean(method, requestMapping, domainId);
            init(requestMethodBean);
        } else {
            try {
                initView(method);
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }

    }

    public MethodConfig(MethodRoot methodRoot, String domainId) {
        this.domainId = domainId;
        init(methodRoot.getRequestMethodBean());
    }

    public MethodConfig(RequestMethodBean methodBean, String domainId) {
        this.domainId = domainId;
        init(methodBean);
    }


    public MethodConfig(CustomMethodInfo customMethodInfo, ViewEntityConfig viewConfig) {
        this.domainId = viewConfig.getDomainId();
        this.method = customMethodInfo.getInnerMethod();
        this.name = customMethodInfo.getName();
        this.methodName = method.getName();
        String urlName = name;
        if (customMethodInfo.isModule()) {
            urlName = OODUtil.formatJavaName(customMethodInfo.getName(), true);
        } else {
            urlName = methodName;
        }


        this.fieldName = customMethodInfo.getFieldName();
        this.sourceClassName = viewConfig.getSourceClassName();
        this.index = customMethodInfo.getIndex();
        this.layoutItem = customMethodInfo.getLayoutItemBean();
        if (customMethodInfo.getViewType() != null) {
            this.getModuleBean().setModuleViewType(ModuleViewType.getModuleViewByViewType(customMethodInfo.getViewType()));
            this.requestMapping = new RequestMappingBean(urlName, viewConfig.getSourceConfig().getUrl());
            this.requestMethodBean = new RequestMethodBean(method, requestMapping, domainId);
            init(requestMethodBean);
        } else {
            try {
                initView(method);
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }

    }

    public MethodConfig(CustomMethodInfo customMethodInfo, AggEntityConfig aggregationConfig) {
        this.domainId = aggregationConfig.getDomainId();
        this.method = customMethodInfo.getInnerMethod();
        this.name = customMethodInfo.getName();
        this.methodName = method.getName();
        String urlName = name;
        if (customMethodInfo.isModule()) {
            urlName = OODUtil.formatJavaName(customMethodInfo.getName(), true);
        } else {
            urlName = methodName;
        }

        this.fieldName = customMethodInfo.getFieldName();
        this.index = customMethodInfo.getIndex();
        this.sourceClassName = aggregationConfig.getSourceClassName();

        if (customMethodInfo.getViewType() != null) {
            this.getModuleBean().setModuleViewType(ModuleViewType.getModuleViewByViewType(customMethodInfo.getViewType()));
        }
        RequestMappingBean requestMappingBean = new RequestMappingBean(urlName, aggregationConfig.getUrl());
        this.requestMethodBean = new RequestMethodBean(method, requestMappingBean, domainId);
        init(requestMethodBean);
        CustomMenuItem defaultMenuItem = this.getDefaultMenuItem();
        if (defaultMenuItem != null) {
            urlName = defaultMenuItem.getDefaultMethodName();
            this.requestMapping = new RequestMappingBean(urlName, aggregationConfig.getUrl());
        }

    }

    public MethodConfig(CustomMethodInfo customMethodInfo, ApiClassConfig sourceClassConfig) {
        this.domainId = sourceClassConfig.getDomainId();
        this.method = customMethodInfo.getInnerMethod();
        this.name = customMethodInfo.getName();
        this.methodName = method.getName();

        String urlName = name;
        if (customMethodInfo.isModule()) {
            urlName = OODUtil.formatJavaName(customMethodInfo.getName(), true);
        } else {
            urlName = methodName;
        }

        this.sourceClassConfig = sourceClassConfig;
        this.sourceClassName = sourceClassConfig.getServiceClass();
        this.index = customMethodInfo.getIndex();
        this.layoutItem = customMethodInfo.getLayoutItemBean();
        if (customMethodInfo.getViewType() != null) {
            this.getModuleBean().setModuleViewType(ModuleViewType.getModuleViewByViewType(customMethodInfo.getViewType()));
            this.requestMapping = new RequestMappingBean(urlName, sourceClassConfig.getUrl());
            this.requestMethodBean = new RequestMethodBean(method, requestMapping, domainId);
            init(requestMethodBean);
        } else {
            try {
                initView(method);
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }


    }


    @JSONField(serialize = false)
    public ESDClass getEsdClass() {
        if (esdClass == null && this.getMethod() != null) {
            Class clazz = this.getMethod().getDeclaringClass();
            try {
                esdClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(clazz.getName(), false);
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        return esdClass;
    }

    public void setEsdClass(ESDClass esdClass) {
        this.esdClass = esdClass;
    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        if (this.getApi() != null) {
            annotationBeans.addAll(this.getApi().getAnnotationBeans());
        }
        if (this.isModule()) {
            annotationBeans.addAll(this.getModuleBean().getAnnotationBeans());
            if (this.getDataBean() != null) {
                annotationBeans.add(this.getDataBean());
            }
        }
        if (routeMenuBean != null) {
            annotationBeans.add(routeMenuBean);
        }

        if (this.getFieldAggConfig() != null) {
            List<CustomBean> fieldBeans = this.getFieldAggConfig().getAnnotationBeans();
            for (CustomBean customBean : fieldBeans) {
                if (!annotationBeans.contains(customBean)) {
                    annotationBeans.add(customBean);
                }
            }
        } else if (fieldBean != null) {
            if (refBean != null) {
                annotationBeans.add(refBean);
            }
            annotationBeans.add(fieldBean);
        }

        if (requestMapping != null && this.getPublicMethod()) {
            Set<String> urlSet = new HashSet<>();
            for (String url : requestMapping.getValue()) {
                if (this.getSourceClass() != null && this.getSourceClass().getRequestMappingBean() != null && this.getSourceClass().getRequestMappingBean().getFristUrl().startsWith(url)) {
                    urlSet.add(StringUtility.replace(url, this.getSourceClass().getRequestMappingBean().getFristUrl(), ""));
                } else {
                    urlSet.add(url);
                }
            }
            annotationBeans.add(requestMapping);
            requestMapping.setValue(urlSet);
        }

        if (getResponseBody() || api != null) {
            if (!annotationBeans.contains(new SimpleCustomBean(ResponseBody.class))) {
                annotationBeans.add(new SimpleCustomBean(ResponseBody.class));
            }
        }

        return annotationBeans;
    }


    public void reSetAPI() {
        this.api = null;
    }


    void init(RequestMethodBean methodBean) {
        this.requestMapping = methodBean.getMappingBean();
        this.requestMethodBean = methodBean;
        this.name = methodBean.getName();
        this.methodName = methodBean.getMethodName();
        if (this.requestMapping.getMethod() == null) {
            this.requestMapping.setMethod(new HashSet<>());
        }
        this.requestMapping.getMethod().add(RequestMethod.POST);
        this.url = methodBean.getUrl();
        this.paramSet = methodBean.getParamSet();
        ResponseBody response = methodBean.getResponseBody();
        if (response == null) {
            responseBody = false;
        }
        try {
            initView(methodBean.getSourceMethod());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    void initMenuItems(Set<CustomMenuItem> bindMenuItems) {
        for (CustomMenuItem menuItem : bindMenuItems) {
            bindMenus.add(menuItem);
            caption = menuItem.getName();
            requestBody = menuItem.getRequestBody();
        }
        this.getApi().getBindMenu().addAll(bindMenuItems);
    }

    void initFormMenus(Set<CustomFormMenu> bindMenuItems) {
        for (CustomFormMenu menuItem : bindMenuItems) {
            formMenus.add(menuItem);
            this.caption = menuItem.getName();
        }
        this.getApi().getBindFormMenu().addAll(bindMenuItems);
    }

    void initTreeMenus(Set<TreeMenu> bindMenuItems) {
        for (TreeMenu menuItem : bindMenuItems) {
            treeMenus.add(menuItem);
            this.caption = menuItem.getName();
        }
        this.getApi().getBindTreeMenu().addAll(bindMenuItems);
    }


    void initGridMenus(Set<GridMenu> bindMenuItems) {
        for (GridMenu menuItem : bindMenuItems) {
            gridMenus.add(menuItem);
            this.caption = menuItem.getName();
        }
        this.getApi().getBindGridMenu().addAll(bindMenuItems);
    }

    void initGalleryMenus(Set<CustomGalleryMenu> bindMenuItems) {
        for (CustomGalleryMenu menuItem : bindMenuItems) {
            galleryMenus.add(menuItem);
            this.caption = menuItem.getName();
        }
        this.getApi().getBindGalleryMenu().addAll(bindMenuItems);
    }

    void initView(Method method) throws JDSException {
        this.method = method;
        String realClassName = method.getDeclaringClass().getName();
        ESDClass readClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(realClassName, false);
        this.sourceClassName = readClass.getCtClass().getName();
        fieldName = MethodUtil.getFieldName(method);
        publicMethod = Modifier.isPublic(method.getModifiers());
        this.id = fieldName;

        if (this.getApi() != null) {
            if (this.getApi().getIndex() != -1) {
                this.index = this.getApi().getIndex();
            }
            Set<CustomMenuItem> items = this.getApi().getBindMenu();
            if (items != null) {
                this.initMenuItems(items);
            }

            Set<CustomFormMenu> formMenus = this.getApi().getBindFormMenu();
            if (formMenus != null) {
                this.initFormMenus(formMenus);
            }

            Set<GridMenu> gridMenus = this.getApi().getBindGridMenu();
            if (gridMenus != null) {
                this.initGridMenus(gridMenus);
            }

            Set<CustomGalleryMenu> galleryMenus = this.getApi().getBindGalleryMenu();
            if (galleryMenus != null) {
                this.initGalleryMenus(galleryMenus);
            }

            Set<TreeMenu> treeMenus = this.getApi().getBindTreeMenu();
            if (treeMenus != null) {
                this.initTreeMenus(treeMenus);
            }


            if (this.getParamSet().size() == 1) {
                Class clazz = this.getParamSet().iterator().next().getParamClass();
                if (!MethodUtil.checkType(clazz.getSimpleName()) && !clazz.equals(String.class)) {
                    requestBody = true;
                }
            }

        }

        if (layoutItem == null) {
            LayoutItemAnnotation annotation = AnnotationUtil.getMethodAnnotation(this.getMethod(), LayoutItemAnnotation.class);
            if (annotation != null) {
                this.layoutItem = new CustomLayoutItemBean(annotation);
            }

        }


        RouteCustomMenu routeCustomMenu = AnnotationUtil.getMethodAnnotation(this.getMethod(), RouteCustomMenu.class);
        if (routeCustomMenu != null) {
            this.routeMenuBean = new RouteMenuBean(routeCustomMenu);
        }


        RequestMapping requestMapping = AnnotationUtil.getMethodAnnotation(this.getMethod(), RequestMapping.class);
        if (requestMapping != null && sourceClassConfig != null) {
            if (this.getSourceClassConfig() != null) {
                RequestMappingBean requestMappingBean = new RequestMappingBean(requestMapping, this.getSourceClassConfig().getUrl());
                requestMethodBean = new RequestMethodBean(method, requestMappingBean, domainId);
            }
        }

        GetMapping getMapping = AnnotationUtil.getMethodAnnotation(this.getMethod(), GetMapping.class);
        if (getMapping != null && sourceClassConfig != null) {
            if (this.getSourceClassConfig() != null) {
                RequestMappingBean requestMappingBean = new RequestMappingBean(getMapping, this.getSourceClassConfig().getUrl());
                requestMethodBean = new RequestMethodBean(method, requestMappingBean, domainId);
            }
        }

        PostMapping postMapping = AnnotationUtil.getMethodAnnotation(this.getMethod(), PostMapping.class);
        if (postMapping != null && sourceClassConfig != null) {
            if (this.getSourceClassConfig() != null) {
                RequestMappingBean requestMappingBean = new RequestMappingBean(postMapping, this.getSourceClassConfig().getUrl());
                requestMethodBean = new RequestMethodBean(method, requestMappingBean, domainId);
            }
        }


        ModuleAnnotation annotation = AnnotationUtil.getMethodAnnotation(this.getMethod(), ModuleAnnotation.class);
        if (annotation != null) {
            Class bindClazz = annotation.bindService();
            if (bindClazz != null && !bindClazz.equals(Void.class) && !bindClazz.equals(Enum.class) && !bindClazz.getName().equals(this.getSourceClassName())) {
                serviceClassNames.add(bindClazz.getName());
                ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(bindClazz.getName());
                MethodConfig methodConfig = apiClassConfig.findEditorMethod();
                if (methodConfig != null) {
                    this.viewClassName = methodConfig.getViewClassName();
                }
            }
            if (!annotation.name().equals("")) {
                this.name = annotation.name();
            }
//
        }


        if (viewClassName == null || viewClassName.equals("")) {
            if (method != null) {
                Class innerClazz = JSONGenUtil.getInnerReturnType(method);
                viewClassName = innerClazz.getName();
            }
        }


        ESDClass sourceClass = this.getSourceClass();
        this.customMethodInfo = new CustomMethodInfo(method, sourceClass);
        CustomModuleBean customModuleBean = this.getModuleBean();
        if (customModuleBean != null) {
            if (customModuleBean.getCaption() != null && !customModuleBean.getCaption().equals("")) {
                caption = customModuleBean.getCaption();
            }
            if (customModuleBean.getImageClass() != null && !customModuleBean.getImageClass().equals("")) {
                imageClass = customModuleBean.getImageClass();
            }
            this.refBean = customMethodInfo.getRefBean();
            if (refBean != null) {
                this.refType = refBean.getRef();
            }
        }


        MethodChinaName methodChinaName = AnnotationUtil.getMethodAnnotation(method, MethodChinaName.class);
        if (caption == null || caption.equals("")) {
            if (methodChinaName != null) {
                caption = methodChinaName.cname();
            } else {
                this.caption = fieldName;
            }
        }

        CustomAnnotation customAnnotation = AnnotationUtil.getMethodAnnotation(this.getMethod(), CustomAnnotation.class);
        if (customAnnotation != null) {
            this.fieldBean = new CustomFieldBean(customAnnotation);
            if (!customAnnotation.imageClass().equals("")) {
                this.imageClass = customAnnotation.imageClass();
            }
            if (!customAnnotation.caption().equals("")) {
                this.caption = customAnnotation.caption();
            }
        }

        if (requestMethodBean != null) {
            this.url = requestMethodBean.getUrl();
        }
        // this.getView();

    }

    @JSONField(serialize = false)
    public ApiClassConfig getSourceClassConfig() {
        if (sourceClassConfig == null) {
            try {
                sourceClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(this.sourceClassName);
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        return sourceClassConfig;
    }


    @JSONField(serialize = false)
    public ApiClassConfig getViewClassConfig() {
        ApiClassConfig sourceClassConfig = null;
        try {
            sourceClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(this.viewClassName);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return sourceClassConfig;
    }

    @JSONField(serialize = false)
    public AggEntityConfig getAggEntityConfig() {
        AggEntityConfig entityConfig = null;
        try {
            entityConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(this.viewClassName, false);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return entityConfig;
    }

    @JSONField(serialize = false)
    public List<MethodConfig> getAllChildViewMethod() {
        List<MethodConfig> allChildMethods = new ArrayList<>();
        try {
            List<MethodConfig> childMethods = getChildViewMethod();
            allChildMethods.addAll(childMethods);
            for (MethodConfig methodConfig : childMethods) {
                allChildMethods.addAll(methodConfig.getAllChildViewMethod());
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return allChildMethods;
    }

    @JSONField(serialize = false)
    public List<MethodConfig> getChildViewMethod() throws JDSException {
        List<MethodConfig> childMethods = new ArrayList<>();
        if (this.isModule() && this.getViewClassName() != null && this.getMethod() != null && !this.getViewClassName().equals(this.getMethod().getDeclaringClass().getName())) {
            ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(this.getViewClassName());
            childMethods.addAll(apiClassConfig.getAllViewMethods());
        }

        return childMethods;
    }


    @JSONField(serialize = false)
    public Set<ApiClassConfig> getServiceClassConfig() {
        Set<ApiClassConfig> apiClassConfigs = new HashSet<>();
        ApiClassConfig sourceClassConfig = null;
        Set<String> serviceClassNames = this.getServiceClassNames();

        if (this.isModule()) {
            CustomViewBean viewBean = this.getView();
            if (viewBean != null) {
                serviceClassNames.addAll(viewBean.getCustomServiceClass());
                serviceClassNames.add(this.getViewClassName());
            }

        }

        for (String serviceClassName : serviceClassNames) {
            try {
                sourceClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(serviceClassName);
                if (sourceClassConfig != null) {
                    apiClassConfigs.add(sourceClassConfig);
                }
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        return apiClassConfigs;
    }

    @JSONField(serialize = false)
    public String getJavaClassName() {
        String className = this.getViewClassName();
        if (this.getMethod() != null) {
            Class clazz = this.getMethod().getReturnType();
            className = clazz.getName();
            if (MethodUtil.checkType(clazz.getName())) {
                className = getJavaSimpleName();
                String packageName = this.getJavaPackageName();
                className = packageName + "." + className;
            }
        }
        return className;
    }

    @JSONField(serialize = false)
    public String getJavaPackageName() {

        String packageName = "";
        try {
            DomainInst domainInst = DSMFactory.getInstance().getAggregationManager().getDomainInstById(this.getDomainId(), null);
            List<JavaSrcBean> temps = new ArrayList<>();
            if (!isModule()) {

                if (domainInst != null) {
                    temps = domainInst.getJavaSrcListByMethod(this.getSourceClass().getClassName(), this.getMethodName());
                    if (this.getViewClass() != null) {
                        packageName = domainInst.getPackageName() + "." + this.getViewClass().getName().toLowerCase();
                    }
                }
            } else {
                ViewInst viewInst = DSMFactory.getInstance().getViewManager().createDefaultView(domainInst);
                if (viewInst != null) {
                    temps = viewInst.getJavaSrcListByMethod(this.getSourceClass().getClassName(), this.getMethodName());
                    if (temps.isEmpty()) {
                        temps = viewInst.getJavaSrcListByMethod(this.getSourceClass().getSourceClass().getClassName(), this.getMethodName());
                    }
                }
                if (this.getViewClass() != null) {
                    packageName = viewInst.getPackageName() + "." + this.getSourceClass().getEntityClass().getName().toLowerCase();
                }

            }
            if (temps.size() > 0) {
                JavaSrcBean javaSrcBean = temps.get(0);
                JavaTemp javaTemp = BuildFactory.getInstance().getTempManager().getJavaTempById(javaSrcBean.getJavaTempId());
                if (javaTemp.getPackagePostfix() != null && !javaTemp.getPackagePostfix().equals("")) {
                    packageName = packageName + "." + javaTemp.getPackagePostfix();
                }
            }

        } catch (JDSException e) {
            e.printStackTrace();
        }

        return packageName;
    }


    public CustomLayoutItemBean getLayoutItem() {
        return layoutItem;
    }

    public void setLayoutItem(CustomLayoutItemBean layoutItem) {
        this.layoutItem = layoutItem;
    }

    @JSONField(serialize = false)
    public Set<String> getServiceClassNames() {
        if (this.isModule()) {
            if (this.getViewClass() != null) {
                serviceClassNames.addAll(this.getViewClass().getServiceClassNames());
            }

            if (this.getDataBean() != null) {
                serviceClassNames.addAll(this.getDataBean().getCustomServiceClass());
            }
        }
        return serviceClassNames;
    }

    public void setServiceClassNames(Set<String> serviceClassNames) {
        this.serviceClassNames = serviceClassNames;
    }

    @JSONField(serialize = false)
    public String getJavaSimpleName() {
        String className = genJavaSimpleName(isModule());
        return className;
    }

    @JSONField(serialize = false)
    private String genJavaSimpleName(boolean isProxy) {
        String className = methodName;
        Class clazz = JSONGenUtil.getInnerReturnType(this.getMethod());
        if (Component.class.isAssignableFrom(clazz)) {
            className = clazz.getSimpleName();
        } else if (ModuleComponent.class.isAssignableFrom(clazz)) {
            className = clazz.getSimpleName();
        } else if (MethodUtil.checkType(clazz.getName())) {
            if (this.getViewClass() != null) {
                className = this.getViewClass().getCtClass().getSimpleName();
            }
            if (isProxy) {
                className = OODUtil.formatJavaName(fieldName, true);
            }
        } else {
            className = clazz.getSimpleName();
        }


        if (isProxy && parentEuPackage != null && !clazz.getSimpleName().equals(className)) {
            String euClassName = parentEuPackage + "." + className;
            try {
                ClassUtility.loadClass(euClassName);
            } catch (ClassNotFoundException e) {
                className = clazz.getSimpleName();
            }
        }


        return className;
    }

    @JSONField(serialize = false)
    public String getEUClassName() {
        String className = null;
        if (this.getUrl() != null) {
            className = this.getUrl().substring(this.getUrl().lastIndexOf("/") + 1);
            className = OODUtil.formatJavaName(className, true);
        }

        if (this.getPackageName() != null && !this.getPackageName().equals("")) {
            className = this.getPackageName() + "." + className;
        }
        return className;

    }

    @JSONField(serialize = false)
    public String getPackageName() {
        String packageName = "";
        String apiUrl = this.getUrl().substring(0, this.getUrl().lastIndexOf("/"));
        if (apiUrl.indexOf("/") > -1) {

            String[] paths = apiUrl.split("/");
            for (int k = 0; k < paths.length; k++) {
                if (packageName.equals("")) {
                    packageName = paths[k];
                } else {
                    packageName = packageName + "." + paths[k];
                }
            }
        } else {
            packageName = apiUrl;
        }
        return packageName;

    }

    @JSONField(serialize = false)
    public ESDClass getTopSourceClass() {
        ESDClass sourceClass = getSourceClass().getTopSourceClass();
        return sourceClass;
    }

    public CustomRefBean getRefBean() {
        return refBean;
    }

    public void setRefBean(CustomRefBean refBean) {
        this.refBean = refBean;
    }

    public RefType getRefType() {
        return refType;
    }

    public void setRefType(RefType refType) {
        this.refType = refType;
    }

    @JSONField(serialize = false)
    public RequestParamBean getParamByName(String paramName) {
        RequestParamBean oldParamBean = null;
        for (RequestParamBean paramBean : paramSet) {
            if (paramBean != null && paramBean.getParamName().equals(paramName)) {
                oldParamBean = paramBean;
            }
        }
        return oldParamBean;
    }


    public void updateParam(RequestParamBean requestParamBean) {
        RequestParamBean oldParamBean = getParamByName(requestParamBean.getParamName());
        if (oldParamBean == null) {
            oldParamBean = new RequestParamBean();
            oldParamBean.setParamName(requestParamBean.getParamName());
            oldParamBean.setParamClassName(requestParamBean.getParamClassName());
            oldParamBean.setDomainId(requestParamBean.getDomainId());
            oldParamBean.setMethodName(requestParamBean.getMethodName());
            oldParamBean.setJsonData(requestParamBean.getJsonData());
            oldParamBean.setParamType(requestParamBean.getParamType());
            paramSet.add(oldParamBean);
        } else {
            oldParamBean.setParamName(requestParamBean.getParamName());
            oldParamBean.setParamClassName(requestParamBean.getParamClassName());
            oldParamBean.setAnnotations(requestParamBean.getAnnotations());
            oldParamBean.setDomainId(requestParamBean.getDomainId());
            oldParamBean.setParamType(requestParamBean.getParamType());
            oldParamBean.setJsonData(requestParamBean.getJsonData());
            oldParamBean.setSourceClassName(this.getSourceClassName());
            oldParamBean.setMethodName(this.getMethodName());
        }

    }

    public LinkedHashSet<RequestParamBean> getParamSet() {
        return paramSet;
    }

    public void setParamSet(LinkedHashSet<RequestParamBean> paramSet) {
        this.paramSet = paramSet;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }


    public void setDynDataBean(CustomDynDataBean dynDataBean) {
        this.dynDataBean = dynDataBean;
    }

    public void setAnnotationBeans(List<CustomBean> annotationBeans) {
        this.annotationBeans = annotationBeans;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public RequestMethodBean getRequestMethodBean() {
        if (requestMethodBean == null) {
            try {
                requestMethodBean = APIConfigFactory.getInstance().getAPIConfig(this.sourceClassName).getMethodByName(this.methodName);
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }
//        if (requestMethodBean == null) {
//            requestMethodBean = new RequestMethodBean(this.getMethod(), this.getRequestMapping(), this.getDomainId());
//        }
        return requestMethodBean;
    }


    public void setRequestMethodBean(RequestMethodBean requestMethodBean) {
        this.requestMethodBean = requestMethodBean;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getItemsExpression() {
        return itemsExpression;
    }

    public void setItemsExpression(String itemsExpression) {
        this.itemsExpression = itemsExpression;
    }

    @JSONField(serialize = false)
    public String getBody() {
        if (body == null && this.getMethod() != null) {
            body = this.getMetaInfo() + "{";
            body = body + this.getMetaProxyInfo();
            body = body + "};";
        }
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public RequestMappingBean getRequestMapping() {
        if (requestMapping == null && this.getMethod() != null) {
            this.requestMapping = new RequestMappingBean(name, this.getSourceClassConfig().getUrl());
        }
        return requestMapping;
    }

    public void setRequestMapping(RequestMappingBean requestMapping) {
        this.requestMapping = requestMapping;
    }


    @JSONField(serialize = false)
    public String getFieldInfo() {
        return MethodUtil.toFieldStr(this.getMethod(), getJavaSimpleName(), this.getFieldName()).toString();
    }

    @JSONField(serialize = false)
    public String getMetaInfo() {
        if (this.getMethod() != null) {
            List<RequestParamBean> params = new ArrayList(this.getParamSet());
            if (isModule() && parentEuPackage != null) {
                String euClassName = parentEuPackage + "." + getJavaSimpleName();
                this.metaInfo = MethodUtil.toMethodStr(this.getMethod(), getViewType(), euClassName, requestBody, params).toString();
            } else {
                this.metaInfo = MethodUtil.toMethodStr(this.getMethod(), getViewType(), getJavaSimpleName(), requestBody, params).toString();
            }

        }
        return metaInfo;
    }

    @JSONField(serialize = false)
    public String getGenMethodInfo() {

        if (this.getMethod() != null) {
            List<RequestParamBean> params = new ArrayList(this.getParamSet());
            if (isModule() && getModuleBean().getEuClassName() != null) {
                this.metaInfo = MethodUtil.toMethodStr(this.getMethod(), getViewType(), getModuleBean().getEuClassName(), requestBody, params).toString();
            } else {
                this.metaInfo = MethodUtil.toMethodStr(this.getMethod(), getViewType(), getJavaSimpleName(), requestBody, params).toString();
            }

        }
        return metaInfo;
    }

    @JSONField(serialize = false)
    public String getSourceMetaInfo() {
        String methodInfo = getMetaInfo();
        if (this.getMethod() != null) {
            List<RequestParamBean> params = new ArrayList(this.getParamSet());
            methodInfo = MethodUtil.toMethodStr(this.getMethod(), getViewType(), genJavaSimpleName(false), requestBody, params).toString();
        } else if (methodInfo == null || methodInfo.equals("") && this.getViewClass() != null) {
            methodInfo = this.getViewClass().getClass().getSimpleName() + " " + methodName + "()";
        }
        return methodInfo;
    }

    @JSONField(serialize = false)
    public ViewType getViewType() {
        if (this.getModuleViewType() != null) {
            return this.getModuleViewType().getDefaultView();
        }
        return null;
    }

    public String getFieldName() {
        if (fieldName == null) {
            fieldName = methodName;
        }
        return fieldName;
    }

    public void setMetaInfo(String metaInfo) {
        this.metaInfo = metaInfo;
    }

    @JSONField(serialize = false)
    public CustomAPICallBean getServiceApi() {
        CustomAPICallBean api = this.getApi();
        if (api == null) {
            for (ApiClassConfig serviceApi : this.getServiceClassConfig()) {
                if (serviceApi != null) {
                    MethodConfig methodConfig = serviceApi.getMethodByItem(CustomMenuItem.INDEX);
                    if (methodConfig == null) {
                        methodConfig = serviceApi.getTabsEvent(CustomTabsEvent.TABEDITOR);
                    }
                    if (methodConfig == null) {
                        methodConfig = serviceApi.getTreeEvent(CustomTreeEvent.RELOADCHILD);
                    }
                    if (methodConfig == null) {
                        methodConfig = serviceApi.getGalleryEvent(CustomGalleryEvent.RELOAD);
                    }
                    if (methodConfig == null) {
                        methodConfig = serviceApi.getFieldEvent(CustomFieldEvent.POPEDITOR);
                    }
                    if (methodConfig != null) {
                        api = methodConfig.getApi();
                    }
                }
            }
        }

        return api;

    }

    public CustomAPICallBean getApi() {
        if (api == null && this.getMethod() != null) {
            RequestMapping requestMapping = AnnotationUtil.getMethodAnnotation(getMethod(), RequestMapping.class);
            if (requestMapping != null) {
                api = new CustomAPICallBean(this);
            } else {
                CustomAPICallBean apiCallBean = null;
                if (getRequestMethodBean() != null) {
                    apiCallBean = new CustomAPICallBean(getRequestMethodBean());
                } else {
                    apiCallBean = new CustomAPICallBean(this);
                }
                if (apiCallBean != null && (apiCallBean.getBindMenu().size() > 0 || apiCallBean.getAllBindEvent().size() > 0)) {
                    this.api = apiCallBean;
                }

            }
        }
        return api;
    }

    public void setApi(CustomAPICallBean api) {
        this.api = api;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }

    public T getView() {
        if (view == null) {
            view = getCustomViewBean();
        }
        return view;
    }

    @JSONField(serialize = false)
    public K getCustomDataBean(CustomMenuItem customMenuItem) {
        K dataBean = null;
        ModuleViewType moduleViewType = customMenuItem.getReturnView();
        if (moduleViewType != null && !moduleViewType.equals(ModuleViewType.NONE)) {
            dataBean = getDataBeanByType(moduleViewType);
        }
        return dataBean;
    }


    @JSONField(serialize = false)
    private T getCustomViewBean() {
        T viewBean = null;
        ModuleViewType moduleViewType = this.getModuleViewType();
        try {
            if (moduleViewType != null) {
                Class beanClass = ClassUtility.loadClass(moduleViewType.getBeanClassName());
                if (!beanClass.equals(Void.class) && !beanClass.equals(Enum.class) && this.getViewClassName() != null && !this.getViewClassName().startsWith("java.lang")) {
                    try {
                        Constructor<T> constructor = beanClass.getConstructor(new Class[]{MethodConfig.class});
                        viewBean = constructor.newInstance(new Object[]{this});
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return viewBean;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setView(T view) {
        this.view = view;
    }

    public Boolean getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(Boolean responseBody) {
        this.responseBody = responseBody;
    }


    public Boolean getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(Boolean requestBody) {
        this.requestBody = requestBody;
    }

    public Map getTagVar() {
        return tagVar;
    }

    public void setTagVar(Map tagVar) {
        this.tagVar = tagVar;
    }

    @JSONField(serialize = false)
    public String getNamePostfix() {
        ModuleViewType viewType = this.getModuleViewType();
        String namePostfix = "";
        if (viewType != null) {
            switch (viewType) {
                case DYNCONFIG:
                    namePostfix = "DYN";
                    break;
                case GRIDCONFIG:
                    namePostfix = "GridView";
                    break;
                case MGRIDCONFIG:
                    namePostfix = "MGridView";
                    break;
                case FORMCONFIG:
                    namePostfix = "View";
                    break;
                case MFORMCONFIG:
                    namePostfix = "MView";
                    break;
                case TREECONFIG:
                    namePostfix = "Tree";
                    break;
                case MTREECONFIG:
                    namePostfix = "MTree";
                    break;
                case NAVTREECONFIG:
                    namePostfix = "NavTree";
                    break;
                case POPTREECONFIG:
                    namePostfix = "PopTree";
                    break;
                case GALLERYCONFIG:
                    namePostfix = "GalleryView";
                    break;
                case NAVGROUPCONFIG:
                    namePostfix = "GroupView";
                    break;
                case NAVMENUBARCONFIG:
                    namePostfix = "NavMenu";
                    break;
                case NAVFOLDINGTREECONFIG:
                    namePostfix = "NavFTree";
                    break;
                case NAVFOLDINGTABSCONFIG:
                    namePostfix = "NavFTab";
                    break;
                case NAVBUTTONVIEWSCONFIG:
                    namePostfix = "NavButton";
                    break;
                case NAVSTACKSCONFIG:
                    namePostfix = "NavStacks";
                    break;
                case NAVTABSCONFIG:
                    namePostfix = "NavTab";
                    break;
                case MBUTTONVIEWSCONFIG:
                    namePostfix = "MButtonViews";
                    break;

                case NONE:
                    namePostfix = "";
                    break;

                default:
                    namePostfix = "Nav";
                    break;
            }
        }
        return namePostfix;
    }

    @JSONField(serialize = false)
    public String getMetaProxyInfo() {
        if (metaProxyInfo == null && this.getMethod() != null) {
            this.metaProxyInfo = MethodUtil.toMethodProxyStr(this.getMethod(), getViewType(), getJavaSimpleName(), null).toString();
        }
        return metaProxyInfo;
    }

    @JSONField(serialize = false)
    public String getMetaProxyInfo(String sourceClassName) {
        return MethodUtil.toMethodProxyStr(this.getMethod(), getViewType(), getJavaSimpleName(), sourceClassName).toString();
    }

    public void setMetaProxyInfo(String metaProxyInfo) {
        this.metaProxyInfo = metaProxyInfo;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    @JSONField(serialize = false)
    public CustomModuleBean getModuleBean() {
        CustomModuleBean moduleBean = null;
        if (view != null) {
            moduleBean = view.getModuleBean();
        }
        if (moduleBean == null) {
            moduleBean = new CustomModuleBean(this);
            if (this.getMethod() != null) {
                Set<Annotation> annotations = AnnotationUtil.getAllAnnotations(this.getMethod(), true);
                moduleBean.fillData(annotations);
            }
            if (view != null) {
                view.setModuleBean(moduleBean);
            }
        }

        return moduleBean;
    }


    @JSONField(serialize = false)
    public CustomDynDataBean getDynDataBean() {
        if (dynDataBean == null && this.getMethod() != null) {
            DynLoadAnnotation dynLoadAnnotation = AnnotationUtil.getMethodAnnotation(this.getMethod(), DynLoadAnnotation.class);
            if (dynLoadAnnotation != null) {
                this.dynDataBean = new CustomDynDataBean(dynLoadAnnotation);
            }
        }
        return dynDataBean;

    }

    @JSONField(serialize = false)
    public K getDataBean() {
        ModuleViewType moduleViewType = this.getModuleViewType();
        try {
            if (moduleViewType != null) {
                Class beanClass = ClassUtility.loadClass(moduleViewType.getDataClassName());
                if (!beanClass.equals(Void.class) && !beanClass.equals(Enum.class)) {
                    if (dataBean == null || !dataBean.getClass().equals(beanClass)) {
                        try {
                            Constructor<K> constructor = beanClass.getConstructor(new Class[]{MethodConfig.class});
                            dataBean = constructor.newInstance(new Object[]{this});
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        if (dataBean == null && this.getDefaultMenuItem() != null) {
            dataBean = this.getCustomDataBean(this.getDefaultMenuItem());
        }
        if (dataBean != null) {
            dataBean.setDomainId(domainId);
            dataBean.setSourceClassName(sourceClassName);
            dataBean.setMethodName(methodName);
        }
        return dataBean;
    }


    private K getDataBeanByType(ModuleViewType moduleViewType) {
        K dataBean = null;
        Class beanClass = null;
        try {

            if (moduleViewType != null) {
                if (!beanClass.equals(Void.class) && !beanClass.equals(Enum.class)) {
                    beanClass = ClassUtility.loadClass(moduleViewType.getDataClassName());
                    try {
                        Constructor<K> constructor = beanClass.getConstructor(new Class[]{MethodConfig.class});
                        dataBean = constructor.newInstance(new Object[]{this});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return dataBean;
    }


    @JSONField(serialize = false)
    public Class<ModuleComponent> getRealViewClass() {
        Class clazz = null;
        if (this.getRealViewClassName() != null) {
            try {
                clazz = ClassUtility.loadClass(this.getRealViewClassName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return clazz;
    }

    @JSONField(serialize = false)
    public String getRealViewClassName() {
        Method method = this.getMethod();
        if (realViewClassName == null && method != null) {
            Class viewClass = null;
            CustomClass customClass = CustomViewConfigFactory.getInstance().getRealCustomAnnotation(method);
            if (customClass == null && this.getDataBean() != null) {
                AnnotationType annotationType = this.getDataBean().getClass().getAnnotation(AnnotationType.class);
                if (annotationType != null && annotationType.clazz() != null) {
                    customClass = (CustomClass) annotationType.clazz().getAnnotation(CustomClass.class);
                }
            }

            if (customClass != null) {
                Class<Component> componentClass = CustomViewConfigFactory.getInstance().getComponent(customClass.moduleType());
                if (componentClass != null) {
                    realViewClassName = componentClass.getName();
                } else {
                    viewClass = customClass.clazz();
                }
            } else if (this.getModuleBean() != null && !this.getModuleBean().getBindService().equals(Void.class) && !this.getModuleBean().getBindService().equals(Enum.class)) {
                viewClass = this.getModuleBean().getBindService();
            }

            if (viewClass != null && ModuleComponent.class.isAssignableFrom(viewClass)) {
                realViewClassName = viewClass.getName();
            }


        }
        return realViewClassName;
    }

    public void setRealViewClassName(String realViewClassName) {
        this.realViewClassName = realViewClassName;
    }

    @JSONField(serialize = false)
    public ModuleViewType getModuleViewType() {
        boolean isDYN = false;
        Method method = this.getMethod();
        ModuleViewType moduleViewType = this.getModuleBean().getModuleViewType();
        ModuleViewType[] skipViewType = new ModuleViewType[]{ModuleViewType.NONE, ModuleViewType.DYNCONFIG, ModuleViewType.LAYOUTCONFIG};
        if (moduleViewType == null || Arrays.asList(skipViewType).contains(moduleViewType)) {

            if (this.view != null) {
                ModuleViewType[] types = ModuleViewType.values();
                for (ModuleViewType type : types) {
                    Class beanClass = null;
                    try {
                        beanClass = ClassUtility.loadClass(type.getBeanClassName());
                        if (beanClass.isAssignableFrom(view.getClass())) {
                            moduleViewType = type;
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            }

            if (moduleViewType == null || Arrays.asList(skipViewType).contains(moduleViewType)) {
                if (method != null) {
                    moduleViewType = CustomViewConfigFactory.getInstance().getModuleViewType(method);
                }
            }
            if (moduleViewType == null || Arrays.asList(skipViewType).contains(moduleViewType)) {
                if (this.isModule() && this.getDefaultMenuItem() != null) {
                    moduleViewType = this.getDefaultMenuItem().getReturnView();
                }
            }


            if (moduleViewType == null || Arrays.asList(skipViewType).contains(moduleViewType)) {
                if (this.getMethod() != null && DSMAnnotationUtil.isDYNView(this.getMethod())) {
                    moduleViewType = ModuleViewType.DYNCONFIG;
                }
            }

            if (moduleViewType != null) {
                this.getModuleBean().setModuleViewType(moduleViewType);
            }

        }
        return moduleViewType;
    }

    @JSONField(serialize = false)
    public CustomMenuItem getDefaultMenuItem() {
        for (CustomMenuItem customMenuItem : bindMenus) {
            if (!customMenuItem.getReturnView().equals(ModuleViewType.NONE) && customMenuItem.getDefaultView()) {
                return customMenuItem;
            }
        }
        return null;
    }


    public void setDataBean(K dataBean) {
        this.dataBean = dataBean;
    }


    public Set<String> getJavaTempIds() {
        return javaTempIds;
    }

    public void setJavaTempIds(Set<String> javaTempIds) {
        this.javaTempIds = javaTempIds;
    }


    @JSONField(serialize = false)
    public List<CustomEvent> getBindEvent() {
        List<CustomEvent> events = new ArrayList<>();
        if (this.getApi() != null) {
            events.addAll(this.getApi().getBindGalleryEvent());
            events.addAll(this.getApi().getBindGridEvent());
            events.addAll(this.getApi().getBindFormEvent());
            events.addAll(this.getApi().getBindHotKeyEvent());
            events.addAll(this.getApi().getBindTabsEvent());
            events.addAll(this.getApi().getBindTreeEvent());
            events.addAll(this.getApi().getBindFieldEvent());
            events.addAll(this.getApi().getBindTitleBlockEvent());
        }

        return events;
    }


    public Set<CustomMenuItem> getBindMenus() {
        return bindMenus;
    }

    public void setBindMenus(Set<CustomMenuItem> bindMenus) {
        this.bindMenus = bindMenus;
    }

    public String getUrl() {
        if (this.getRequestMethodBean() != null) {
            url = this.getRequestMethodBean().getUrl();
        }
        if (url == null && this.getRequestMapping() != null) {
            url = this.getRequestMapping().getFristUrl();
        }
        return url;
    }

    public void setUrl(String url) {

        this.url = url;
    }

    public String getSourceClassName() {
        return sourceClassName;
    }

    public void setSourceClassName(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }

    public CustomMethodInfo getCustomMethodInfo() {
        if (customMethodInfo == null) {
            customMethodInfo = this.getSourceClass().getMethodInfo(this.methodName);
        }
        return customMethodInfo;
    }

    public Integer getIndex() {
        if (index == -1 && getCustomMethodInfo() != null) {
            index = getCustomMethodInfo().getIndex();
        }
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setCustomMethodInfo(CustomMethodInfo customMethodInfo) {
        this.customMethodInfo = customMethodInfo;
    }

    public String getViewClassName() {
        return viewClassName;
    }

    public void setViewClassName(String viewClassName) {
        this.viewClassName = viewClassName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @JSONField(serialize = false)
    public boolean isModule() {
        boolean isModule = false;
        for (CustomMenuItem menuItem : this.getBindMenus()) {
            if (!menuItem.getReturnView().equals(ModuleViewType.NONE) && menuItem.getDefaultView()) {
                isModule = true;
            }
        }
        if (this.getMethod() != null) {
            ModuleAnnotation annotation = AnnotationUtil.getMethodAnnotation(this.getMethod(), ModuleAnnotation.class);
            if (annotation != null) {
                isModule = true;
            }

            ModuleRefFieldAnnotation comboModuleAnnotation = AnnotationUtil.getMethodAnnotation(this.getMethod(), ModuleRefFieldAnnotation.class);
            if (comboModuleAnnotation != null) {
                isModule = true;
            }
        }


        if (this.getModuleBean().getModuleViewType() != null && !this.getModuleBean().getModuleViewType().equals(ModuleViewType.NONE)) {
            isModule = true;
        }


        return isModule;
    }

    @JSONField(serialize = false)
    public ESDClass getSourceClass() {
        ESDClass sourceClass = null;
        try {
            if (sourceClassName != null && !sourceClassName.equals("")) {

                sourceClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(sourceClassName, false);

            }
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return sourceClass;
    }

    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet = new HashSet<>();
        if (this.getView() != null) {
            classSet.addAll(this.getView().getOtherClass());
        } else {
            if (this.getViewClassName() != null && !this.getViewClassName().equals("void")) {
                try {
                    Class viewClass = ClassUtility.loadClass(this.getViewClassName());
                    if (viewClass != null) {
                        classSet.add(viewClass);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        return classSet;
    }


    @JSONField(serialize = false)
    public ESDClass getViewClass() {
        ESDClass esdClass = null;
        try {
            if (viewClassName == null
                    || viewClassName.equals("")
                    || viewClassName.equals("void")
                    || viewClassName.startsWith("java.")
                    || viewClassName.equals(this.getSourceClassName())) {
                if (this.getMethod() != null) {
                    Class innerClazz = JSONGenUtil.getInnerReturnType(method);
                    viewClassName = innerClazz.getName();
                }
            } else {
                esdClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(viewClassName, false);
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return esdClass;
    }


    @JSONField(serialize = false)
    public EUModule getModule(Map valueMap, String projectName) throws JDSException {
        ESDClient client = ESDFacrory.getAdminESDClient();
        EUModule module = client.getCustomModule(this, projectName, valueMap);
        if (module != null) {
            module.getComponent().setTarget(this.getMethodName());
        }
        return module;

    }

    @JSONField(serialize = false)
    public FieldAggConfig getFieldAggConfig() {
        FieldAggConfig fieldAggConfig = null;
        try {
            AggregationManager aggregationManager = DSMFactory.getInstance().getAggregationManager();
            AggEntityConfig aggEntityConfig = aggregationManager.getAggEntityConfig(this.getSourceClassName(), false);
            fieldAggConfig = aggEntityConfig.getFieldByName(this.methodName);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return fieldAggConfig;
    }


    @JSONField(serialize = false)
    public Method getMethod() {
        try {
            if (method == null && sourceClassName != null && !sourceClassName.equals("")) {
                RequestMethodBean requestMethodBean = getRequestMethodBean();
                if (requestMethodBean != null) {
                    method = requestMethodBean.getSourceMethod();
                } else {
                    List<Class> params = new ArrayList<>();
                    for (RequestParamBean paramClass : this.getParamSet()) {
                        if (paramClass.getParamClass() != null) {
                            params.add(paramClass.getParamClass());
                        } else {
                            params.add(Object.class);
                        }

                    }
                    method = MethodUtil.getEqualMethod(ClassUtility.loadClass(sourceClassName), this.methodName, params.toArray(new Class[]{}));
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return method;
    }


    @JSONField(serialize = false)
    public Boolean getField() {
        boolean isGetMethod = MethodUtil.isFieldName(this.getMethod());
        return getPublicMethod() && isGetMethod && this.getApi() == null;
    }

    @JSONField(serialize = false)
    public Class getInnerReturnType() {
        return JSONGenUtil.getInnerReturnType(this.getMethod());
    }

    @JSONField(serialize = false)
    public Boolean getPublicMethod() {
        if (publicMethod == null && this.getMethod() != null) {
            publicMethod = Modifier.isPublic(method.getModifiers());
        }
        if (publicMethod == null) {
            publicMethod = false;
        }
        return publicMethod;
    }


    public String getParentEuPackage() {
        return parentEuPackage;
    }

    public void setParentEuPackage(String parentEuPackage) {
        this.parentEuPackage = parentEuPackage;
    }

    public CustomFieldBean getFieldBean() {
        return fieldBean;
    }

    public void setFieldBean(CustomFieldBean fieldBean) {
        this.fieldBean = fieldBean;
    }

    public RouteMenuBean getRouteMenuBean() {
        return routeMenuBean;
    }

    public void setRouteMenuBean(RouteMenuBean routeMenuBean) {
        this.routeMenuBean = routeMenuBean;
    }

    public void setPublicMethod(Boolean publicMethod) {
        this.publicMethod = publicMethod;
    }

    @Override
    public int compareTo(MethodConfig o) {
        if (index == null) {
            return -1;
        }

        if (index != null && o.getIndex() != null) {
            return this.index - o.getIndex();
        }

        return 1;
    }
}


