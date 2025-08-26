package net.ooder.esd.dsm.view.ref;


import net.ooder.common.JDSException;
import net.ooder.esd.annotation.ui.AppendType;
import net.ooder.esd.bean.CustomRefBean;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.custom.ESDField;

import java.util.HashSet;
import java.util.Set;

public class ViewEntityRef {

    String desc;
    String methodName;
    String domainId;
    String refId;
    String className;
    String otherClassName;

    CustomRefBean refBean;
    String mainCaption;
    String otherCaption;
    String fkField;
    String expression;
    String pkField;
    String otherField;
    Set<String> javaTempIds = new HashSet<>();
    AppendType appendType = AppendType.append;

    public ViewEntityRef() {
        refBean = new CustomRefBean();
    }

    public ViewEntityRef(ESDField methodInfo, String domainId) {
        try {

            ESDClass esdClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(methodInfo.getReturnType().getName(),  false);
            this.desc = methodInfo.getDesc();
            this.className = methodInfo.getESDClass().getClassName();
            this.otherClassName = methodInfo.getReturnType().getName();
            this.pkField = esdClass.getUid();
            this.refBean = methodInfo.getRefBean();
            this.fkField = methodInfo.getId();
            this.domainId = methodInfo.getDomainId();
            this.refId = className + pkField + "|" + otherClassName + fkField;
            this.methodName = methodInfo.getName();

        } catch (JDSException e) {
            e.printStackTrace();
        }


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

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }


    public String getMainCaption() {
        return mainCaption;
    }

    public void setMainCaption(String mainCaption) {
        this.mainCaption = mainCaption;
    }

    public String getOtherCaption() {
        return otherCaption;
    }

    public void setOtherCaption(String otherCaption) {
        this.otherCaption = otherCaption;
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

    public CustomRefBean getRefBean() {
        return refBean;
    }

    public void setRefBean(CustomRefBean refBean) {
        this.refBean = refBean;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getFkField() {
        return fkField;
    }

    public void setFkField(String fkField) {
        this.fkField = fkField;
    }

    public String getPkField() {
        return pkField;
    }

    public void setPkField(String pkField) {
        this.pkField = pkField;
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

    public String getOtherField() {
        return otherField;
    }

    public void setOtherField(String otherField) {
        this.otherField = otherField;
    }

}
