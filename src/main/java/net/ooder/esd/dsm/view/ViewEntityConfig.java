package net.ooder.esd.dsm.view;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.web.RequestMappingBean;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ViewEntityConfig {

    String domainId;

    String className;

    String sourceClassName;

    Set<String> serviceClassNames = new HashSet<>();

    Set<String> javaTempIds = new LinkedHashSet<>();

    RequestMappingBean requestMapping = new RequestMappingBean();


    public ViewEntityConfig() {


    }

    public ViewEntityConfig(AggEntityConfig aggEntityConfig, String domainId) {
        init(aggEntityConfig.getESDClass());
        this.domainId=domainId;
        requestMapping = aggEntityConfig.getRequestMappingBean();
        if (domainId==null){
            this.domainId = aggEntityConfig.getDomainId();
        }

    }

    public ViewEntityConfig(ESDClass esdClass, String domainId) {
        init(esdClass);
        this.domainId = domainId;
    }

    public ViewEntityConfig(ESDClass esdClass) {
        init(esdClass);
    }


    void init(ESDClass esdClass) {
        if (esdClass != null) {
            this.className = esdClass.getCtClass().getName();
            this.sourceClassName = esdClass.getSourceClass().getClassName();
            this.serviceClassNames.addAll(esdClass.getServiceClassNames());
            this.domainId = esdClass.getDomainId();
            if (esdClass.getRequestMappingBean() != null) {
                this.requestMapping = esdClass.getRequestMappingBean();
            }
        }
    }

    @JSONField(serialize = false)
    public ESDClass getCurrClass() {
        ESDClass esdClass = null;
        if (className != null) {
            try {
                esdClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(className, false);
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        return esdClass;
    }


    @JSONField(serialize = false)
    public ESDClass getSourceClass() {
        ESDClass esdClass = null;
        if (sourceClassName != null) {
            try {
                esdClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(sourceClassName, false);
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        return esdClass;
    }

    @JSONField(serialize = false)
    public Set<ESDClass> getServiceClass() {
        Set<ESDClass> esdClasses = new HashSet<>();
        for (String serviceClassName : this.getServiceClassNames()) {
            try {
                ESDClass esdClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(serviceClassName,  false);
                esdClasses.add(esdClass);
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        return esdClasses;
    }

    @JSONField(serialize = false)
    public ApiClassConfig getCurrConfig() {
        ApiClassConfig currClassConfig = null;
        try {
            currClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(className);

        } catch (JDSException e) {
            e.printStackTrace();
        }

        return currClassConfig;
    }

    @JSONField(serialize = false)
    public Set<ApiClassConfig> getServiceConfig() {
        Set<ApiClassConfig> apiClassConfigs = new HashSet<>();
        for (String serviceClassName : this.getServiceClassNames()) {
            try {
                ApiClassConfig serviceClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(serviceClassName);

                apiClassConfigs.add(serviceClassConfig);
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }

        return apiClassConfigs;
    }


    @JSONField(serialize = false)
    public ApiClassConfig getSourceConfig() {
        ApiClassConfig sourceClassConfig = null;
        try {
            sourceClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(sourceClassName);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return sourceClassConfig;

    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public Set<String> getServiceClassNames() {
        return serviceClassNames;
    }

    public void setServiceClassNames(Set<String> serviceClassNames) {
        this.serviceClassNames = serviceClassNames;
    }

    public String getSourceClassName() {
        return sourceClassName;
    }

    public void setSourceClassName(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }

    public RequestMappingBean getRequestMapping() {
        return requestMapping;
    }

    public void setRequestMapping(RequestMappingBean requestMapping) {
        this.requestMapping = requestMapping;
    }


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Set<String> getJavaTempIds() {
        return javaTempIds;
    }

    public void setJavaTempIds(Set<String> javaTempIds) {
        this.javaTempIds = javaTempIds;
    }

    @JSONField(serialize = false)
    public Set<JavaSrcBean> getViewBeans() {
        Set<JavaSrcBean> javaSrcBeans = new HashSet<>();
        ApiClassConfig classConfig = this.getCurrConfig();
        List<MethodConfig> methodAPIBeans = classConfig.getAllProxyMethods();
        DomainInst domainInst = null;
        try {
            domainInst = DSMFactory.getInstance().getDomainInstById(domainId);
            for (MethodConfig methodAPIBean : methodAPIBeans) {
                JavaSrcBean javaSrcBean = domainInst.getJavaSrcByClassName(methodAPIBean.getJavaClassName());
                if (javaSrcBean != null) {
                    javaSrcBeans.add(javaSrcBean);
                }
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return javaSrcBeans;
    }
}
