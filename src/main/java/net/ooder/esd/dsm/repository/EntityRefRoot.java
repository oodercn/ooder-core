package net.ooder.esd.dsm.repository;

import net.ooder.esd.dsm.JavaRoot;
import net.ooder.esd.dsm.repository.config.EntityConfig;
import net.ooder.esd.dsm.repository.entity.EntityRefProxy;
import net.ooder.annotation.RefType;

import java.util.List;


public class EntityRefRoot implements JavaRoot {

    public String packageName;

    public String className;

    public String basepath;

    public String moduleName;

    public String space;

    public String cnName;

    public RepositoryInst repositoryInst;

    public EntityConfig mainClass;

    public List<EntityRefProxy> refs;

    public  RefType refType;


    public EntityRefRoot(RepositoryInst repositoryInst, EntityConfig mainClass, List<EntityRefProxy> refs, RefType refType) {
        this.repositoryInst = repositoryInst;
        this.packageName = repositoryInst.getPackageName();
        this.mainClass=mainClass;
        this.className =mainClass.getRootClassName();
        this.cnName =mainClass.getRootClass().getDesc();
        this.basepath = repositoryInst.getPackageName();;
        this.space = repositoryInst.getSpace();
        this.refs = refs;
        this.refType=refType;


    }

    @Override
    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public RepositoryInst getRepositoryInst() {
        return repositoryInst;
    }

    public void setRepositoryInst(RepositoryInst repositoryInst) {
        this.repositoryInst = repositoryInst;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }


    public String getBasepath() {
        return basepath;
    }

    public void setBasepath(String basepath) {
        this.basepath = basepath;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public EntityConfig getMainClass() {
        return mainClass;
    }

    public void setMainClass(EntityConfig mainClass) {
        this.mainClass = mainClass;
    }

    public List<EntityRefProxy> getRefs() {
        return refs;
    }

    public void setRefs(List<EntityRefProxy> refs) {
        this.refs = refs;
    }

    public RefType getRefType() {
        return refType;
    }

    public void setRefType(RefType refType) {
        this.refType = refType;
    }
}
