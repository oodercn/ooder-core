package net.ooder.esd.dsm.view;


import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.DSMInst;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.enums.DSMType;
import net.ooder.esd.dsm.java.JavaPackage;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.view.ref.ViewEntityRef;
import net.ooder.esd.util.json.CaseEnumsSerializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewInst extends DSMInst implements Comparable<ViewInst> {
    public String viewInstId;
    public String domainId;
    @JSONField(deserializeUsing = CaseEnumsSerializer.class)
    public DSMType dsmType = DSMType.VIEW;
    public Map<String, ViewEntityRef> viewEntityRefMap = new HashMap<>();
    @JSONField(serialize = false)
    JavaPackage rootPackage;


    public ViewInst() {

    }


    public ViewInst(DomainInst domainInst) {
        this.projectVersionName = domainInst.getProjectVersionName();
        this.space = domainInst.getSpace();
        this.packageName = domainInst.getEuPackage();

        this.desc = domainInst.getDesc();
        this.viewInstId = domainInst.getDomainId();
        this.domainId = domainInst.getDomainId();
        this.name = domainInst.getName();

    }


    @JSONField(serialize = false)
    public List<ESDClass> getViewProxyClasses() throws JDSException {
        List<ESDClass> esdClassList = new ArrayList<>();
        for (JavaSrcBean javaSrcBean : this.getRootPackage().listAllFile()) {
            ESDClass esdClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(javaSrcBean.getClassName(), true);
            if (esdClass != null && esdClass.isProxy() && !esdClassList.contains(esdClass)) {
                esdClassList.add(esdClass);
            }
        }


        return esdClassList;
    }

    @JSONField(serialize = false)
    public List<JavaSrcBean> getJavaSrcListByMethod(String sourceClassName, String methodName) {
        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();

        for (JavaSrcBean srcBean : this.getJavaSrcBeans()) {
            if (srcBean.getSourceClassName() != null && srcBean.getSourceClassName().equals(sourceClassName) && srcBean.getMethodName() != null && srcBean.getMethodName().equals(methodName)) {
                javaSrcBeans.add(srcBean);
            }
        }
        return javaSrcBeans;
    }


    @JSONField(serialize = false)
    public List<ESDClass> getAggListClasses() throws JDSException {
        List<ESDClass> esdClassList = new ArrayList<>();

        for (JavaSrcBean javaSrcBean : this.getRootPackage().listAllFile()) {
            ESDClass esdClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(javaSrcBean.getClassName(), true);
            if (esdClass != null && !esdClass.isProxy() && !esdClassList.contains(esdClass)) {
                esdClassList.add(esdClass);
            }
        }

        return esdClassList;
    }

    //
    @JSONField(serialize = false)
    public DomainInst getDomainInst() {
        DomainInst domainInst = null;
        try {
            domainInst = DSMFactory.getInstance().getAggregationManager().getDomainInstById(domainId, projectVersionName);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return domainInst;
    }

    @Override
    public JavaPackage getRootPackage() {
        if (rootPackage == null || rootPackage.listAllChildren().isEmpty()) {
            rootPackage = getPackageByName(getEuPackage());
            if (rootPackage == null) {
                rootPackage = getProjectRoot().createChildPackage(space.replace(".", "/"));
            }
        }
        return rootPackage;
    }


    public void updateView(JavaSrcBean srcBean) {
        updateBeans(srcBean, this.getJavaSrcBeans());
    }


    @JSONField
    private void updateBeans(JavaSrcBean javaSrcBean, List<JavaSrcBean> beans) {
        boolean isUpdate = false;
        for (JavaSrcBean bean : beans) {
            if (javaSrcBean.equals(bean)) {
                isUpdate = true;
                bean.setJavaTempId(javaSrcBean.getJavaTempId());
                bean.setDate(System.currentTimeMillis());
                bean.setClassName(javaSrcBean.getClassName());
                bean.setPath(javaSrcBean.getPath());
            }
        }
        if (!isUpdate) {
            beans.add(javaSrcBean);
        }
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    @Override
    public String getDsmId() {
        return viewInstId;
    }

    @Override
    @JSONField(serialize = false)
    public List<JavaSrcBean> getJavaSrcBeans() {
        return this.getRootPackage().listAllFile();
    }


    public Map<String, ViewEntityRef> getViewEntityRefMap() {
        return viewEntityRefMap;
    }

    public void setViewEntityRefMap(Map<String, ViewEntityRef> viewEntityRefMap) {
        this.viewEntityRefMap = viewEntityRefMap;
    }

    public void setRootPackage(JavaPackage rootPackage) {
        this.rootPackage = rootPackage;
    }


    public String getViewInstId() {
        return viewInstId;
    }

    public void setViewInstId(String viewInstId) {
        this.viewInstId = viewInstId;
    }


    public DSMType getDsmType() {
        return dsmType;
    }

    public void setDsmType(DSMType dsmType) {
        this.dsmType = dsmType;
    }


    @Override
    public int compareTo(ViewInst o) {
        return o.getCreateTime().compareTo(this.getCreateTime());
    }
}
