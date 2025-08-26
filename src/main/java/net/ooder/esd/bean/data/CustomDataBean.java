package net.ooder.esd.bean.data;


import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.annotation.SimpleCustomBean;
import net.ooder.common.JDSException;
import net.ooder.common.util.ClassUtility;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.event.CustomEvent;
import net.ooder.esd.annotation.event.CustomFieldEvent;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.bean.CustomData;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.CustomModuleBean;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.custom.CustomViewConfigFactory;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.tool.properties.CS;
import net.ooder.server.httpproxy.core.AbstractHandler;
import net.ooder.web.RequestMappingBean;
import net.ooder.web.util.AnnotationUtil;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.Annotation;
import java.util.*;

public abstract class CustomDataBean implements CustomData {
    @JSONField(serialize = false)
    public Set<Class> customService;
    public Set<String> customServiceClass = new LinkedHashSet<>();
    public String expression;
    public String sourceClassName;
    public String methodName;
    public String name;
    public String domainId;
    @JSONField(serialize = false)
    MethodConfig methodConfig;
    public CS cs;


    public CustomDataBean() {

    }


    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        MethodConfig methodConfig = getMethodConfig();
        CustomModuleBean moduleBean = methodConfig.getModuleBean();
        annotationBeans = new ArrayList<>();
        if (moduleBean != null) {
            annotationBeans.add(moduleBean);
        }
        RequestMappingBean requestMapping = methodConfig.getRequestMapping();
        if (requestMapping != null) {
            annotationBeans.add(requestMapping);
        }
        if (methodConfig.getResponseBody()) {
            annotationBeans.add(new SimpleCustomBean(ResponseBody.class));
        }

        if (methodConfig.getApi() != null) {
            annotationBeans.add(methodConfig.getApi());
        }
        annotationBeans.add(this);
        return annotationBeans;
    }


    public MethodConfig getMethodConfig() {
        if (methodConfig == null) {
            try {
                ApiClassConfig esdClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(sourceClassName);
                methodConfig = esdClassConfig.getMethodByName(methodName);
                ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(sourceClassName);
                if (apiClassConfig != null) {
                    methodConfig = apiClassConfig.getMethodByName(methodName);
                }
                if (methodConfig == null) {
                    AggEntityConfig aggEntityConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(sourceClassName, false);
                    if (aggEntityConfig != null) {
                        methodConfig = aggEntityConfig.getMethodByName(methodName);
                    }
                }
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        return methodConfig;
    }


    public CustomDataBean(MethodConfig methodConfig) {
        Annotation viewAnnotation = CustomViewConfigFactory.getInstance().getViewAnnotation(methodConfig.getMethod());
        this.methodConfig = methodConfig;
        this.sourceClassName = methodConfig.getSourceClassName();
        this.domainId = methodConfig.getDomainId();
        this.methodName = methodConfig.getMethodName();
        this.expression = methodConfig.getExpression();
        if (viewAnnotation != null) {
            Class[] clazzes = (Class[]) AnnotationUtil.getValues(viewAnnotation, "customService");
            if (clazzes != null) {
                for (Class clazz : clazzes) {
                    customServiceClass.add(clazz.getName());
                }
            }
        }

        String serviceClass = methodConfig.getMethod().getDeclaringClass().getName();
        if (customServiceClass.isEmpty() || !customServiceClass.contains(serviceClass)) {
            customServiceClass.add(serviceClass);
        }
    }


    @JSONField(serialize = false)
    public CS getCs() {
        if (cs == null) {
            MethodConfig methodConfig = this.getMethodEvent(CustomFieldEvent.LOADCS);
            if (methodConfig != null) {
                Object handle = JDSActionContext.getActionContext().getHandle();
                if (handle != null && handle instanceof AbstractHandler) {
                    AbstractHandler abstractHandler = (AbstractHandler) handle;
                    cs = (CS) abstractHandler.invokMethod(methodConfig.getRequestMethodBean());
                }
            }
        }
        return cs;
    }

    public void setCs(CS cs) {
        this.cs = cs;
    }

    @JSONField(serialize = false)
    public MethodConfig getMethodEvent(CustomEvent bingEvent) {
        MethodConfig otherMethodConfig = null;
        try {
            if (this.getMethodConfig() != null && this.getMethodConfig().getView() != null) {
                Set<String> esdClassNames = this.getMethodConfig().getView().getCustomServiceClass();
                for (String esdClassName : esdClassNames) {
                    Class clazz = null;
                    try {
                        clazz = ClassUtility.loadClass(esdClassName);
                        Class sourceClazz = ClassUtility.loadClass(sourceClassName);
                        if (!esdClassName.equals(sourceClassName) && !clazz.isAssignableFrom(sourceClazz)) {
                            ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(esdClassName);
                            if (apiClassConfig.getUrl() != null && !apiClassConfig.getUrl().equals("")) {
                                otherMethodConfig = apiClassConfig.getMethodByEvent(bingEvent);
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (JDSException e) {
            e.printStackTrace();
        }

        if (otherMethodConfig == null) {
            try {
                ESDClass esdClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(sourceClassName, false);
                if (esdClass != null && esdClass.isProxy()) {
                    AggEntityConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(sourceClassName, false);
                    otherMethodConfig = apiClassConfig.getMethodByEvent(bingEvent);
                }
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }

        if (otherMethodConfig == null) {
            Set<ApiClassConfig> apiClassConfigSet = this.getMethodConfig().getServiceClassConfig();
            for (ApiClassConfig apiClassConfig : apiClassConfigSet) {
                if (apiClassConfig != null) {
                    otherMethodConfig = apiClassConfig.getMethodByEvent(bingEvent);
                    if (otherMethodConfig != null) {
                        break;
                    }
                }

            }
        }
        return otherMethodConfig;
    }

    @JSONField(serialize = false)
    public MethodConfig getMethodByItem(CustomMenuItem item) {
        MethodConfig otherMethodConfig = null;
        try {
            Set<String> esdClassNames = this.getMethodConfig().getView().getCustomServiceClass();
            for (String esdClassName : esdClassNames) {
                Class clazz = null;
                try {
                    clazz = ClassUtility.loadClass(esdClassName);
                    Class sourceClazz = ClassUtility.loadClass(sourceClassName);
                    if (!esdClassName.equals(sourceClassName) && !clazz.isAssignableFrom(sourceClazz)) {
                        ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(esdClassName);
                        if (apiClassConfig.getUrl() != null && !apiClassConfig.getUrl().equals("")) {
                            otherMethodConfig = apiClassConfig.getMethodByItem(item);
                        }
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }


        if (otherMethodConfig == null) {
            try {
                ESDClass esdClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(sourceClassName, false);
                if (esdClass.isProxy()) {
                    AggEntityConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(sourceClassName, false);
                    otherMethodConfig = apiClassConfig.getMethodByItem(item);
                }
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }

        if (otherMethodConfig == null) {
            Set<ApiClassConfig> apiClassConfigSet = this.getMethodConfig().getServiceClassConfig();
            for (ApiClassConfig apiClassConfig : apiClassConfigSet) {
                if (apiClassConfig != null) {
                    otherMethodConfig = apiClassConfig.getMethodByItem(item);
                    if (otherMethodConfig != null) {
                        break;
                    }
                }
            }
        }
        return otherMethodConfig;
    }


    @JSONField(serialize = false)
    public List<MethodConfig> getAllBindEvent() {
        List<MethodConfig> bindEvents = new ArrayList<>();
        try {
            Class sourceClazz = ClassUtility.loadClass(sourceClassName);
            Set<String> esdClassNames = this.getMethodConfig().getView().getCustomServiceClass();
            for (String esdClassName : esdClassNames) {
                try {
                    Class clazz = ClassUtility.loadClass(esdClassName);
                    if (!esdClassName.equals(sourceClassName) && !clazz.isAssignableFrom(sourceClazz)) {
                        ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(esdClassName);
                        if (apiClassConfig.getUrl() != null && !apiClassConfig.getUrl().equals("")) {
                            bindEvents.addAll(apiClassConfig.getAllBindEventMethod());
                        }
                        ESDClass esdClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(sourceClassName, false);
                        if (esdClass.isProxy()) {
                            AggEntityConfig entityConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(sourceClassName, false);
                            bindEvents.addAll(entityConfig.getAllBindEventMethod());
                        }
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bindEvents;

    }


    @JSONField(serialize = false)
    public List<MethodConfig> getAllEventMethod() {
        List<MethodConfig> bindEvents = new ArrayList<>();
        try {
            if (this.getMethodConfig().getView() != null) {
                Set<String> esdClassNames = this.getMethodConfig().getView().getCustomServiceClass();
                for (String esdClassName : esdClassNames) {
                    ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(esdClassName);
                    if (apiClassConfig.getUrl() != null && !apiClassConfig.getUrl().equals("")) {
                        bindEvents.addAll(apiClassConfig.getAllEventMethods());
                    }
                }
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return bindEvents;

    }

    @JSONField(serialize = false)
    public MethodConfig getMethodByName(String url) {
        MethodConfig otherMethodConfig = null;
        Set<ApiClassConfig> apiClassConfigSet = this.getMethodConfig().getServiceClassConfig();
        for (ApiClassConfig apiClassConfig : apiClassConfigSet) {
            if (!apiClassConfig.getServiceClass().equals(sourceClassName)) {
                otherMethodConfig = apiClassConfig.getMethodByName(url);
                if (otherMethodConfig != null) {
                    break;
                }
            }
        }

        if (otherMethodConfig == null) {
            for (ApiClassConfig apiClassConfig : apiClassConfigSet) {
                if (apiClassConfig.getServiceClass().equals(sourceClassName)) {
                    if (apiClassConfig != null) {
                        otherMethodConfig = apiClassConfig.getMethodByName(url);
                        if (otherMethodConfig != null) {
                            break;
                        }
                    }
                }
            }
        }


        return otherMethodConfig;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }


    public String getSourceClassName() {
        return sourceClassName;
    }

    public void setSourceClassName(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }


    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }


    public Set<String> getCustomServiceClass() {
        return customServiceClass;
    }

    public void setCustomServiceClass(Set<String> customServiceClass) {
        this.customServiceClass = customServiceClass;
    }

    public Set<Class> getCustomService() {
        customService = new HashSet<>();
        for (String className : this.getCustomServiceClass()) {
            try {
                if (className.endsWith(".class")) {
                    className.substring(0, className.length() - ".class".length());
                }
                customService.add(ClassUtility.loadClass(className));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return customService;
    }

    public void setCustomService(Set<Class> customService) {
        if (customService == null || customService.isEmpty()) {
            this.getCustomServiceClass().clear();
        } else {
            for (Class clazz : customService) {

                this.getCustomServiceClass().add(clazz.getName());
            }
        }
    }


}
