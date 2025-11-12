package net.ooder.esd.tool;

import net.ooder.annotation.UserSpace;
import net.ooder.common.JDSException;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.CustomViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.DomainInst;

public class DSMProperties {

    String projectName;

    String imageClass;

    ModuleViewType moduleViewType;

    String sourceClassName;

    String sourceMethodName;


    String domainId;

    String realPath;


    public DSMProperties() {

    }

    public void update(MethodConfig methodConfig) {
        this.moduleViewType = methodConfig.getModuleViewType();
        this.sourceMethodName = methodConfig.getMethodName();
        this.sourceClassName = methodConfig.getSourceClassName();
        if (methodConfig.getView() != null) {
            this.realPath = methodConfig.getView().getXpath();
        }

    }

    public DSMProperties(CustomViewBean viewBean) {
        this.moduleViewType = viewBean.getModuleViewType();
        this.sourceMethodName = viewBean.getMethodName();
        this.sourceClassName = viewBean.getSourceClassName();
        this.realPath = viewBean.getXpath();
    }

    public DSMProperties(MethodConfig methodConfig, String projectName) {
        this.projectName = projectName;
        this.domainId = methodConfig.getDomainId();
        if (domainId == null) {
            try {
                DomainInst domainInst = DSMFactory.getInstance().getDefaultDomain(projectName, UserSpace.USER);
                this.domainId = domainInst.getDomainId();
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        update(methodConfig);

    }


    public String getRealPath() {
        return realPath;
    }

    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }

    public ModuleViewType getModuleViewType() {
        return moduleViewType;
    }

    public void setModuleViewType(ModuleViewType moduleViewType) {
        this.moduleViewType = moduleViewType;
    }

    public String getSourceClassName() {
        return sourceClassName;
    }

    public void setSourceClassName(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }

    public String getSourceMethodName() {
        return sourceMethodName;
    }

    public void setSourceMethodName(String sourceMethodName) {
        this.sourceMethodName = sourceMethodName;
    }


    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }


    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }


}
