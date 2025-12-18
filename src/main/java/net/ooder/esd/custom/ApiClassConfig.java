package net.ooder.esd.custom;

import com.alibaba.fastjson.annotation.JSONField;
import javassist.NotFoundException;
import net.ooder.common.JDSConstants;
import net.ooder.common.JDSException;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.ModuleAnnotation;
import net.ooder.esd.annotation.event.*;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.properties.APICallerProperties;
import net.ooder.esd.tool.properties.Event;
import net.ooder.web.APIConfig;
import net.ooder.web.APIConfigFactory;
import net.ooder.web.RequestMethodBean;
import net.ooder.web.RequestParamBean;
import net.ooder.web.util.AnnotationUtil;

import java.util.*;

public class ApiClassConfig {

    private static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, ApiClassConfig.class);

    String domainId;

    String serviceClass;

    String simpleName;

    String url;

    String caption;

    String fullUrl;


    Set<String> proxyMethods = new HashSet<>();

    List<CustomMenuItem> cutomMethodItems = new ArrayList<>();

    Map<String, MethodConfig> allMethodMap = new LinkedHashMap<>();
    @JSONField(serialize = false)
    Map<CustomEvent, MethodConfig> eventMethodMap = new LinkedHashMap<>();

    @JSONField(serialize = false)
    Map<Event, MethodConfig> extMethodMap = new LinkedHashMap<>();

    @JSONField(serialize = false)
    Map<CustomMenuItem, MethodConfig> itemMethodMap = new LinkedHashMap<>();

    Set<String> otherClassNames = new LinkedHashSet<>();

    @JSONField(serialize = false)
    APIConfig apiConfig;


    public ApiClassConfig() {
    }


    public ApiClassConfig(ESDClass esdClass) {
        try {
            this.domainId = esdClass.getDomainId();
            this.serviceClass = esdClass.getClassName();
            this.caption = esdClass.getCaption();
            APIConfig apiConfig = APIConfigFactory.getInstance().getAPIConfig(serviceClass);
            initESDClass(esdClass, apiConfig);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    public ApiClassConfig(APIConfig config) {
        this.apiConfig = config;
        this.serviceClass = config.getClassName();
        try {
            ESDClass esdClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(serviceClass, false);
            this.domainId = esdClass.getDomainId();
            initESDClass(esdClass, apiConfig);

        } catch (JDSException e) {
            e.printStackTrace();
        }

    }


    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet = new HashSet<>();
        List<MethodConfig> allMethods = getAllMethods();
        for (MethodConfig methodConfig : allMethods) {
            classSet.addAll(methodConfig.getOtherClass());
        }
        return ClassUtility.checkBase(classSet);
    }

    void initESDClass(ESDClass esdServiceClass, APIConfig config) {
        if (config != null) {
            this.url = config.getUrl();
        }
        this.simpleName = esdServiceClass.getCtClass().getSimpleName();
        List<CustomMethodInfo> esdMethods = new ArrayList<>();
        for (CustomMethodInfo methodInfo : esdServiceClass.getMethodsList()) {
            if (methodInfo != null) {
                esdMethods.add(methodInfo);
            }
        }

        for (CustomMethodInfo methodInfo : esdServiceClass.getOtherMethodsList()) {
            if (methodInfo != null) {
                esdMethods.add(methodInfo);
            }
        }

        Collections.sort(esdMethods);
        for (CustomMethodInfo field : esdMethods) {
            CustomMethodInfo methodField = field;
            RequestMethodBean methodBean = null;
            if (config != null) {
                methodBean = config.getMethodByName(methodField.getInnerMethod().getName());
            }
            MethodConfig methodAPIBean = null;
            APIEventAnnotation apiEventAnnotation = AnnotationUtil.getMethodAnnotation(methodField.getInnerMethod(), APIEventAnnotation.class);
            ModuleAnnotation moduleAnnotation = AnnotationUtil.getMethodAnnotation(methodField.getInnerMethod(), ModuleAnnotation.class);

            if (methodBean != null && methodBean.getUrl() != null && !methodBean.getUrl().equals("")) {
                methodAPIBean = new MethodConfig(methodBean, domainId);
                APICallerComponent component = new APICallerComponent(methodAPIBean);
                APICallerProperties properties = component.getProperties();
                properties.setImageClass(methodField.getImageClass());
                proxyMethods.add(methodAPIBean.getMethodName());
            } else if (moduleAnnotation != null) {
                methodAPIBean = new MethodConfig(methodField.getInnerMethod(), this);
                proxyMethods.add(methodAPIBean.getMethodName());
            } else if (apiEventAnnotation != null && apiEventAnnotation.bindMenu().length > 0) {
                CustomMenuItem customMenuItem = apiEventAnnotation.bindMenu()[0];
                methodAPIBean = new MethodConfig(methodField.getInnerMethod(), customMenuItem, this);
                proxyMethods.add(methodAPIBean.getMethodName());
            } else if (field.getComponentType().equals(ComponentType.MODULE)) {
                methodAPIBean = new MethodConfig(methodField, this);
                proxyMethods.add(methodAPIBean.getMethodName());
            }

            if (methodAPIBean != null && !methodField.getReturnType().equals(Void.class) && !methodField.getReturnType().equals(Enum.class)) {
                allMethodMap.put(methodAPIBean.getMethodName(), methodAPIBean);
            }

        }

    }

    @JSONField(serialize = false)
    public List<MethodConfig> getAllMethods() {
        List<MethodConfig> methodAPIBeanList = new ArrayList<>();
        List<String> methodInfos = new ArrayList<>();
        Map<String, MethodConfig> methodConfigMap = new HashMap<>();
        methodConfigMap.putAll(allMethodMap);

        methodConfigMap.forEach((k, v) -> {
            if (!methodInfos.contains(v.getMetaInfo())) {
                methodAPIBeanList.add(v);
                methodInfos.add(v.getMetaInfo());
            }
        });
        return methodAPIBeanList;
    }


    @JSONField(serialize = false)
    public List<MethodConfig> getAllProxyMethods() {
        List<MethodConfig> proxyMethodList = new ArrayList<>();
        for (String methodName : proxyMethods) {
            proxyMethodList.add(this.getMethodByName(methodName));
        }
        Arrays.sort(proxyMethodList.toArray());
        return proxyMethodList;
    }

    @JSONField(serialize = false)
    public List<MethodConfig> getAllEventMethods() {
        List<MethodConfig> proxyMethodList = new ArrayList<>();
        for (String methodName : proxyMethods) {
            MethodConfig methodConfig = this.getMethodByName(methodName);
            if (methodConfig != null && !methodConfig.isModule()) {
                proxyMethodList.add(this.getMethodByName(methodName));
            }
        }
        Arrays.sort(proxyMethodList.toArray());
        return proxyMethodList;
    }

    @JSONField(serialize = false)
    public List<MethodConfig> getAllViewMethods() {
        List<MethodConfig> viewMethodList = new ArrayList<>();
        for (String methodName : proxyMethods) {
            MethodConfig methodConfig = this.getMethodByName(methodName);
            if (methodConfig != null && methodConfig.isModule()) {
                viewMethodList.add(this.getMethodByName(methodName));
            }
        }
        return viewMethodList;
    }


    @JSONField(serialize = false)
    public List<MethodConfig> getCustomMethods() {
        List<MethodConfig> methodAPIBeanList = new ArrayList<>();
        for (CustomMenuItem customMenuItem : cutomMethodItems) {
            MethodConfig methodConfig = this.getMethodByItem(customMenuItem);
            methodAPIBeanList.add(methodConfig);
        }
        return methodAPIBeanList;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    public List<CustomMenuItem> getCutomMethodItems() {
        return cutomMethodItems;
    }

    public void setCutomMethodItems(List<CustomMenuItem> cutomMethodItems) {
        this.cutomMethodItems = cutomMethodItems;
    }

    public String getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(String serviceClass) {
        this.serviceClass = serviceClass;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public APIConfig getApiConfig() {
        if (apiConfig == null) {
            try {
                apiConfig = APIConfigFactory.getInstance().getAPIConfig(serviceClass);
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }
        return apiConfig;
    }

    public void setApiConfig(APIConfig apiConfig) {
        this.apiConfig = apiConfig;
    }


    public MethodConfig getMethodByName(String methodName) {
        MethodConfig methodAPICallBean = null;
        if (methodName != null) {
            methodAPICallBean = allMethodMap.get(methodName);
            if (methodAPICallBean == null) {
                if (this.getESDClass().getMethodInfo(methodName) != null) {
                    CustomMethodInfo methodInfo = this.getESDClass().getMethodInfo(methodName);
                    methodAPICallBean = new MethodConfig(methodInfo, this);
                    allMethodMap.put(methodName, methodAPICallBean);
                } else if (this.getESDClass().getField(methodName) != null) {
                    ESDField methodInfo = this.getESDClass().getField(methodName);
                    if (methodInfo instanceof CustomMethodInfo) {
                        methodAPICallBean = new MethodConfig((CustomMethodInfo) methodInfo, this);
                        allMethodMap.put(methodName, methodAPICallBean);
                    }
                }
            }
        }

        return methodAPICallBean;
    }

    ;

    @JSONField(serialize = false)
    public List<MethodConfig> getAllBindEventMethod() {
        List<MethodConfig> methodAPIBeans = this.getAllMethods();
        for (MethodConfig methodAPIBean : methodAPIBeans) {
            if (methodAPIBean.getApi().getBindGridEvent().size() > 0) {
                methodAPIBeans.add(methodAPIBean);
            } else if (methodAPIBean.getApi().getBindMGridEvent().size() > 0) {
                methodAPIBeans.add(methodAPIBean);
            } else if (methodAPIBean.getApi().getBindTabsEvent().size() > 0) {
                methodAPIBeans.add(methodAPIBean);
            } else if (methodAPIBean.getApi().getBindTreeEvent().size() > 0) {
                methodAPIBeans.add(methodAPIBean);
            } else if (methodAPIBean.getApi().getBindFormEvent().size() > 0) {
                methodAPIBeans.add(methodAPIBean);
            } else if (methodAPIBean.getApi().getBindGalleryEvent().size() > 0) {
                methodAPIBeans.add(methodAPIBean);
            } else if (methodAPIBean.getApi().getBindFieldEvent().size() > 0) {
                methodAPIBeans.add(methodAPIBean);
            } else if (methodAPIBean.getApi().getBindTitleBlockEvent().size() > 0) {
                methodAPIBeans.add(methodAPIBean);
            } else if (methodAPIBean.getApi().getBindContentBlockEvent().size() > 0) {
                methodAPIBeans.add(methodAPIBean);
            } else if (methodAPIBean.getApi().getBindHotKeyEvent().size() > 0) {
                methodAPIBeans.add(methodAPIBean);
            }
        }
        return methodAPIBeans;
    }

    @JSONField(serialize = false)
    public List<MethodConfig> getAllMethodByEvent(CustomEvent[] eventEnums) {
        List<MethodConfig> methodConfigs = new ArrayList<>();
        for (CustomEvent eventEnum : eventEnums) {
            MethodConfig methodAPICallBean = getMethodByEvent(eventEnum);
            if (methodAPICallBean != null) {
                methodConfigs.add(methodAPICallBean);
            }
        }
        return methodConfigs;
    }

    @JSONField(serialize = false)
    public MethodConfig getMethodByEvent(CustomEvent eventEnum) {
        MethodConfig methodAPICallBean = null;
        if (eventEnum instanceof CustomGridEvent) {
            methodAPICallBean = this.getGridEvent((CustomGridEvent) eventEnum);
        } else if (eventEnum instanceof CustomMGridEvent) {
            methodAPICallBean = this.getMGridEvent((CustomMGridEvent) eventEnum);
        } else if (eventEnum instanceof CustomTreeEvent) {
            methodAPICallBean = this.getTreeEvent((CustomTreeEvent) eventEnum);
        } else if (eventEnum instanceof CustomTabsEvent) {
            methodAPICallBean = this.getTabsEvent((CustomTabsEvent) eventEnum);
        } else if (eventEnum instanceof CustomFormEvent) {
            methodAPICallBean = this.getFormEvent((CustomFormEvent) eventEnum);
        } else if (eventEnum instanceof CustomFieldEvent) {
            methodAPICallBean = this.getFieldEvent((CustomFieldEvent) eventEnum);
        } else if (eventEnum instanceof CustomGalleryEvent) {
            methodAPICallBean = this.getGalleryEvent((CustomGalleryEvent) eventEnum);
        } else if (eventEnum instanceof CustomTitleBlockEvent) {
            methodAPICallBean = this.getTitleBlockEvent((CustomTitleBlockEvent) eventEnum);
        } else if (eventEnum instanceof CustomContentBlockEvent) {
            methodAPICallBean = this.getContentBlockEvent((CustomContentBlockEvent) eventEnum);
        } else if (eventEnum instanceof CustomHotKeyEvent) {
            methodAPICallBean = this.getHotKeyEvent((CustomHotKeyEvent) eventEnum);
        }
        return methodAPICallBean;
    }

    @JSONField(serialize = false)
    public MethodConfig getFieldEvent(CustomFieldEvent eventEnum) {
        MethodConfig methodAPICallBean = eventMethodMap.get(eventEnum);
        if (methodAPICallBean == null) {
            List<MethodConfig> methodAPIBeans = this.getAllMethods();
            for (MethodConfig methodAPIBean : methodAPIBeans) {
                if (methodAPIBean.getApi() != null && methodAPIBean.getApi().getBindFieldEvent().contains(eventEnum)) {
                    eventMethodMap.put(eventEnum, methodAPIBean);
                    return methodAPIBean;
                }
            }
        }
        return methodAPICallBean;
    }

    @JSONField(serialize = false)
    public MethodConfig getHotKeyEvent(CustomHotKeyEvent eventEnum) {
        MethodConfig methodAPICallBean = eventMethodMap.get(eventEnum);
        if (methodAPICallBean == null) {
            List<MethodConfig> methodAPIBeans = this.getAllMethods();
            for (MethodConfig methodAPIBean : methodAPIBeans) {
                if (methodAPIBean.getApi() != null && methodAPIBean.getApi().getBindHotKeyEvent().contains(eventEnum)) {
                    eventMethodMap.put(eventEnum, methodAPIBean);
                    return methodAPIBean;
                }
            }
        }
        return methodAPICallBean;
    }


    @JSONField(serialize = false)
    public MethodConfig getGalleryEvent(CustomGalleryEvent eventEnum) {
        MethodConfig methodAPICallBean = eventMethodMap.get(eventEnum);
        if (methodAPICallBean == null) {
            List<MethodConfig> methodAPIBeans = this.getAllMethods();
            for (MethodConfig methodAPIBean : methodAPIBeans) {
                if (methodAPIBean.getApi() != null && methodAPIBean.getApi().getBindGalleryEvent().contains(eventEnum)) {
                    eventMethodMap.put(eventEnum, methodAPIBean);
                    return methodAPIBean;
                }
            }
        }
        return methodAPICallBean;
    }


    @JSONField(serialize = false)
    public MethodConfig getTitleBlockEvent(CustomTitleBlockEvent eventEnum) {
        MethodConfig methodAPICallBean = eventMethodMap.get(eventEnum);
        if (methodAPICallBean == null) {
            List<MethodConfig> methodAPIBeans = this.getAllMethods();
            for (MethodConfig methodAPIBean : methodAPIBeans) {
                if (methodAPIBean.getApi() != null && methodAPIBean.getApi().getBindTitleBlockEvent().contains(eventEnum)) {
                    eventMethodMap.put(eventEnum, methodAPIBean);
                    return methodAPIBean;
                }
            }
        }
        return methodAPICallBean;
    }


    @JSONField(serialize = false)
    public MethodConfig getContentBlockEvent(CustomContentBlockEvent eventEnum) {
        MethodConfig methodAPICallBean = eventMethodMap.get(eventEnum);
        if (methodAPICallBean == null) {
            List<MethodConfig> methodAPIBeans = this.getAllMethods();
            for (MethodConfig methodAPIBean : methodAPIBeans) {
                if (methodAPIBean.getApi() != null && methodAPIBean.getApi().getBindContentBlockEvent().contains(eventEnum)) {
                    eventMethodMap.put(eventEnum, methodAPIBean);
                    return methodAPIBean;
                }
            }
        }
        return methodAPICallBean;
    }

    @JSONField(serialize = false)
    public MethodConfig getGridEvent(CustomGridEvent eventEnum) {
        MethodConfig methodAPICallBean = eventMethodMap.get(eventEnum);
        if (methodAPICallBean == null) {
            List<MethodConfig> methodAPIBeans = this.getAllMethods();
            for (MethodConfig methodAPIBean : methodAPIBeans) {
                if (methodAPIBean.getApi() != null && methodAPIBean.getApi() != null && methodAPIBean.getApi().getBindGridEvent().contains(eventEnum)) {
                    eventMethodMap.put(eventEnum, methodAPIBean);
                    return methodAPIBean;
                }
            }
        }

        return methodAPICallBean;
    }


    @JSONField(serialize = false)
    public MethodConfig getMGridEvent(CustomMGridEvent eventEnum) {
        MethodConfig methodAPICallBean = eventMethodMap.get(eventEnum);
        if (methodAPICallBean == null) {
            List<MethodConfig> methodAPIBeans = this.getAllMethods();
            for (MethodConfig methodAPIBean : methodAPIBeans) {
                if (methodAPIBean.getApi() != null && methodAPIBean.getApi() != null && methodAPIBean.getApi().getBindGridEvent().contains(eventEnum)) {
                    eventMethodMap.put(eventEnum, methodAPIBean);
                    return methodAPIBean;
                }
            }
        }
        return methodAPICallBean;
    }

    @JSONField(serialize = false)
    public MethodConfig getTabsEvent(CustomTabsEvent eventEnum) {
        MethodConfig methodAPICallBean = eventMethodMap.get(eventEnum);
        if (methodAPICallBean == null) {
            List<MethodConfig> methodAPIBeans = this.getAllMethods();
            for (MethodConfig methodAPIBean : methodAPIBeans) {
                if (methodAPIBean.getApi() != null && methodAPIBean.getApi() != null && methodAPIBean.getApi().getBindTabsEvent().contains(eventEnum)) {
                    eventMethodMap.put(eventEnum, methodAPIBean);
                    return methodAPIBean;
                }
            }
        }
        return methodAPICallBean;
    }

    @JSONField(serialize = false)
    public MethodConfig getFormEvent(CustomFormEvent eventEnum) {
        MethodConfig methodAPICallBean = eventMethodMap.get(eventEnum);
        if (methodAPICallBean == null) {
            List<MethodConfig> methodAPIBeans = this.getAllMethods();
            for (MethodConfig methodAPIBean : methodAPIBeans) {
                if (methodAPIBean.getApi() != null && methodAPIBean.getApi() != null && methodAPIBean.getApi().getBindFormEvent().contains(eventEnum)) {
                    eventMethodMap.put(eventEnum, methodAPIBean);
                    return methodAPIBean;
                }
            }
        }
        return methodAPICallBean;
    }


    @JSONField(serialize = false)
    public MethodConfig getMFormEvent(CustomMFormEvent eventEnum) {
        MethodConfig methodAPICallBean = eventMethodMap.get(eventEnum);
        if (methodAPICallBean == null) {
            List<MethodConfig> methodAPIBeans = this.getAllMethods();
            for (MethodConfig methodAPIBean : methodAPIBeans) {
                if (methodAPIBean.getApi() != null && methodAPIBean.getApi() != null && methodAPIBean.getApi().getBindMFormEvent().contains(eventEnum)) {
                    eventMethodMap.put(eventEnum, methodAPIBean);
                    return methodAPIBean;
                }
            }
        }
        return methodAPICallBean;
    }

    @JSONField(serialize = false)
    public MethodConfig getTreeEvent(CustomTreeEvent eventEnum) {
        MethodConfig methodAPICallBean = eventMethodMap.get(eventEnum);
        if (methodAPICallBean == null) {
            List<MethodConfig> methodAPIBeans = this.getAllMethods();
            for (MethodConfig methodAPIBean : methodAPIBeans) {
                if (methodAPIBean.getApi() != null && methodAPIBean.getApi() != null && methodAPIBean.getApi().getBindTreeEvent().contains(eventEnum)) {
                    eventMethodMap.put(eventEnum, methodAPIBean);
                    return methodAPIBean;
                }
            }
        }
        return methodAPICallBean;
    }


    @JSONField(serialize = false)
    public MethodConfig getMethodByItem(CustomMenuItem item) {
        MethodConfig methodAPICallBean = itemMethodMap.get(item);
        if (methodAPICallBean == null) {
            methodAPICallBean = this.getMethodByName(item.getMethodName());
            if (methodAPICallBean == null) {
                List<MethodConfig> methodAPIBeans = this.getAllMethods();
                for (MethodConfig methodAPIBean : methodAPIBeans) {
                    if (methodAPIBean.getBindMenus().contains(item)) {
                        methodAPICallBean = methodAPIBean;
                        continue;
                    }
                }
            }
        }
        return methodAPICallBean;
    }

    public MethodConfig getMethodByItem(CustomMenuItem item, Class clazz) {
        MethodConfig methodAPICallBean = this.getMethodByName(item.getMethodName());
        if (methodAPICallBean == null) {
            List<MethodConfig> methodAPIBeans = this.getAllMethods();
            for (MethodConfig methodAPIBean : methodAPIBeans) {
                Set<RequestParamBean> paramClass = methodAPIBean.getParamSet();
                if (methodAPIBean.getBindMenus().contains(item) && clazz != null && paramClass.size() > 0 && paramClass.iterator().next().getParamClass().isAssignableFrom(clazz)) {
                    methodAPICallBean = methodAPIBean;
                    continue;
                }
            }
        }
        return methodAPICallBean;
    }

    public Set<String> getOtherClassNames() {
        return otherClassNames;
    }

    public void setOtherClassNames(Set<String> otherClassNames) {
        this.otherClassNames = otherClassNames;
    }

    @JSONField(serialize = false)
    public ESDClass getESDClass() {
        ESDClass esdClass = null;
        if (serviceClass != null) {
            try {
                esdClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(serviceClass, false);
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }

        return esdClass;
    }


    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public List<MethodConfig> getMethodByItems(CustomMenuItem item) {
        List<MethodConfig> methodConfigs = this.getAllMethods();
        List<MethodConfig> methodAPIBeans = this.getAllMethods();
        for (MethodConfig methodAPIBean : methodAPIBeans) {
            if (methodAPIBean.getBindMenus().contains(item)) {
                methodConfigs.add(methodAPIBean);
            }
        }
        return methodConfigs;
    }

    public MethodConfig findEditorMethod() {
        MethodConfig editorMethod = getMethodByEvent(CustomTabsEvent.TABEDITOR);
        if (editorMethod == null) {
            editorMethod = getMethodByEvent(CustomTreeEvent.TREENODEEDITOR);
        }
        if (editorMethod == null) {
            editorMethod = getMethodByItem(CustomMenuItem.INDEX);
        }
        return editorMethod;
    }

    public Map<String, MethodConfig> getAllMethodMap() {
        return allMethodMap;
    }

    public void setAllMethodMap(Map<String, MethodConfig> allMethodMap) {
        this.allMethodMap = allMethodMap;
    }

    public Set<String> getProxyMethods() {
        return proxyMethods;
    }

    public void setProxyMethods(Set<String> proxyMethods) {
        this.proxyMethods = proxyMethods;
    }

}


