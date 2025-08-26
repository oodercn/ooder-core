package net.ooder.esd.dsm.repository.entity;


import net.ooder.esd.annotation.ui.AppendType;
import net.ooder.esd.bean.CustomRefBean;
import net.ooder.esd.custom.ESDField;

import java.util.HashSet;
import java.util.Set;

public class EntityRef {

    String refId;
    String className;
    String projectVersionName;
    String desc;
    String otherClassName;
    String methodName;

    CustomRefBean refBean;
    String expression;
    Set<String> javaTempIds = new HashSet<>();
    AppendType appendType = AppendType.append;

    public EntityRef() {
        refBean = new CustomRefBean();
    }

    public EntityRef(ESDField methodInfo, String projectVersionName) {
        this.desc = methodInfo.getDesc();
        this.className = methodInfo.getESDClass().getClassName();
        this.otherClassName = methodInfo.getReturnType().getName();
        this.refBean = methodInfo.getRefBean();
        this.projectVersionName = projectVersionName;
        this.methodName = methodInfo.getName();

    }

    public CustomRefBean getRefBean() {
        return refBean;
    }

    public void setRefBean(CustomRefBean refBean) {
        this.refBean = refBean;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Set<String> getJavaTempIds() {
        return javaTempIds;
    }

    public void setJavaTempIds(Set<String> javaTempIds) {
        this.javaTempIds = javaTempIds;
    }

    public AppendType getAppendType() {
        return appendType;
    }

    public void setAppendType(AppendType appendType) {
        this.appendType = appendType;
    }


    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getProjectVersionName() {
        return projectVersionName;
    }

    public void setProjectVersionName(String projectVersionName) {
        this.projectVersionName = projectVersionName;
    }

    public String getRefId() {
        if (refId == null) {
            refId = methodName + "_" + refBean.getRef().getType() + "_" + otherClassName;
        }
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getOtherClassName() {
        return otherClassName;
    }

    public void setOtherClassName(String otherClassName) {
        this.otherClassName = otherClassName;
    }


    @Override
    public boolean equals(Object obj) {

        if (obj != null && obj instanceof EntityRef) {
            EntityRef entityRef = (EntityRef) obj;
            return entityRef.getRefId().equals(this.getRefId());
        }
        return super.equals(obj);
    }
}
