package net.ooder.esd.dsm.java;

import java.util.ArrayList;
import java.util.List;

public class ViewJavaSrcBean {

    List<String> viewClassList;

    List<String> repositoryClassList;

    List<String> aggClassList;

    List<String> childClassList = new ArrayList<>();

    List<String> serviceClassList;

    List<String> rootServicesClassName = new ArrayList<>();

    String parentModuleClassName;

    String className;

    public ViewJavaSrcBean() {

    }

    public ViewJavaSrcBean(String parentModuleClassName, String className) {
        this.parentModuleClassName = parentModuleClassName;
        this.className = className;
    }

    public List<String> getChildClassList() {
        if (childClassList == null) {
            childClassList = new ArrayList<>();
        }
        return childClassList;
    }

    public void setChildClassList(List<String> childClassList) {
        this.childClassList = childClassList;
    }

    public List<String> getViewClassList() {

        if (viewClassList == null) {
            viewClassList = new ArrayList<>();
        }
        return viewClassList;
    }

    public void setViewClassList(List<String> viewClassList) {
        this.viewClassList = viewClassList;
    }

    public List<String> getRepositoryClassList() {
        return repositoryClassList;
    }

    public void setRepositoryClassList(List<String> repositoryClassList) {
        this.repositoryClassList = repositoryClassList;
    }

    public List<String> getAggClassList() {
        return aggClassList;
    }

    public void setAggClassList(List<String> aggClassList) {
        this.aggClassList = aggClassList;
    }

    public List<String> getServiceClassList() {
        return serviceClassList;
    }

    public void setServiceClassList(List<String> serviceClassList) {
        this.serviceClassList = serviceClassList;
    }

    public List<String> getRootServicesClassName() {
        return rootServicesClassName;
    }

    public void setRootServicesClassName(List<String> rootServicesClassName) {
        this.rootServicesClassName = rootServicesClassName;
    }

    public String getParentModuleClassName() {
        return parentModuleClassName;
    }

    public void setParentModuleClassName(String parentModuleClassName) {
        this.parentModuleClassName = parentModuleClassName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
