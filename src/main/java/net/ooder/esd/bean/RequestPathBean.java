package net.ooder.esd.bean;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.RequestPathAnnotation;
import net.ooder.esd.annotation.ui.RequestPathTypeEnum;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

@AnnotationType(clazz = RequestPathAnnotation.class)
public class RequestPathBean implements CustomBean {


    @JSONField(serialize = false)
    private String sourceClassName;

    @JSONField(serialize = false)
    private String domainId;

    @JSONField(serialize = false)
    private String methodName;

    RequestPathTypeEnum type;

    String paramsname;

    String path;

    public RequestPathBean() {

    }

    public RequestPathBean(RequestPathAnnotation event) {
        type = event.type();
        paramsname = event.paramsname();
        path = event.path();
    }

    public RequestPathBean(MethodConfig methodConfig) {
        this.methodName = methodConfig.getMethodName();
        this.domainId = methodConfig.getDomainId();
        this.sourceClassName = methodConfig.getSourceClassName();

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

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public RequestPathTypeEnum getType() {
        return type;
    }

    public void setType(RequestPathTypeEnum type) {
        this.type = type;
    }

    public String getParamsname() {
        return paramsname;
    }

    public void setParamsname(String paramsname) {
        this.paramsname = paramsname;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
