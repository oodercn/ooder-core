package net.ooder.esd.dsm.repository;

import net.ooder.esd.dsm.JavaRoot;
import net.ooder.esd.dsm.repository.database.proxy.DSMTableProxy;
import net.ooder.esd.dsm.repository.database.ref.TableRefProxy;
import net.ooder.annotation.UserSpace;

import java.util.ArrayList;
import java.util.List;


public class RepositoryRoot implements JavaRoot {

    public String packageName;

    public String className;

    public String moduleName;

    public String cnName;

    public RepositoryInst repositoryInst;

    public String basepath;

    public String space;

    public UserSpace userSpace;

    public DSMTableProxy table;

    public List<TableRefProxy> refs;

    public List<DSMTableProxy> tables;


    public RepositoryRoot(RepositoryInst repositoryInst, List<DSMTableProxy> tableInfos, UserSpace userSpace) {
        this.repositoryInst = repositoryInst;
        this.packageName = repositoryInst.getPackageName();
        this.tables = new ArrayList<>();
        this.userSpace = userSpace;
        this.cnName = repositoryInst.getDesc();
        this.basepath = repositoryInst.getPackageName();
        this.space = repositoryInst.getSpace();
        this.tables = tableInfos;
    }

    @Override
    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public UserSpace getUserSpace() {
        return userSpace;
    }

    public void setUserSpace(UserSpace userSpace) {
        this.userSpace = userSpace;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public List<DSMTableProxy> getTables() {
        return tables;
    }

    public void setTables(List<DSMTableProxy> tables) {
        this.tables = tables;
    }

    public RepositoryInst getRepositoryInst() {
        return repositoryInst;
    }

    public void setRepositoryInst(RepositoryInst repositoryInst) {
        this.repositoryInst = repositoryInst;
    }

    public DSMTableProxy getTable() {
        return table;
    }

    public void setTable(DSMTableProxy tableProxy) {
        this.table = tableProxy;
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

    public String getBasepath() {
        return basepath;
    }

    public void setBasepath(String basepath) {
        this.basepath = basepath;
    }

    public List<TableRefProxy> getRefs() {
        return refs;
    }

    public void setRefs(List<TableRefProxy> refs) {
        this.refs = refs;
    }

}
