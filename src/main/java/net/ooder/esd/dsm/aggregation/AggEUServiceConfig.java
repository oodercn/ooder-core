package net.ooder.esd.dsm.aggregation;

import net.ooder.esd.bean.MethodChinaBean;
import net.ooder.web.RequestMappingBean;

public class AggEUServiceConfig {

    public String domainId;

    public MethodChinaBean methodChinaBean;

    public String sourceClassName;

    public String currClassName;

    public String entityClassName;

    public RequestMappingBean requestMappingBean;

    public AggEUServiceConfig() {

    }

    public AggEUServiceConfig(AggEntityConfig aggEntityConfig) {
        this.domainId = aggEntityConfig.getDomainId();
        this.sourceClassName = aggEntityConfig.getSourceClassName();
        this.currClassName = aggEntityConfig.getCurrClassName();
        this.entityClassName = aggEntityConfig.getEntityClassName();
        this.methodChinaBean = aggEntityConfig.getMethodChinaBean();
    }


    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getSourceClassName() {
        return sourceClassName;
    }

    public void setSourceClassName(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }

    public String getCurrClassName() {
        return currClassName;
    }

    public void setCurrClassName(String currClassName) {
        this.currClassName = currClassName;
    }

    public String getEntityClassName() {
        return entityClassName;
    }

    public void setEntityClassName(String entityClassName) {
        this.entityClassName = entityClassName;
    }

    public MethodChinaBean getMethodChinaBean() {
        return methodChinaBean;
    }

    public void setMethodChinaBean(MethodChinaBean methodChinaBean) {
        this.methodChinaBean = methodChinaBean;
    }

    public RequestMappingBean getRequestMappingBean() {
        return requestMappingBean;
    }

    public void setRequestMappingBean(RequestMappingBean requestMappingBean) {
        this.requestMappingBean = requestMappingBean;
    }
}
