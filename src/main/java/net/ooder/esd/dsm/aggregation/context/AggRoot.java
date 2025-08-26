package net.ooder.esd.dsm.aggregation.context;

import net.ooder.common.JDSException;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.JavaRoot;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.view.ref.ViewEntityRefProxy;

import java.util.List;


public class AggRoot implements JavaRoot {

    public String packageName;

    public String className;

    public String cnName;

    public DomainInst dsmBean;

    public String basepath;

    public String moduleName;

    public String space;

    public List<ViewEntityRefProxy> refs;

    public List<AggEntityConfig> aggEntities;


    public AggRoot(DomainInst dsmBean) {

        try {
            this.dsmBean = dsmBean;
            this.aggEntities = DSMFactory.getInstance().getAggregationManager().getAggEntityConfigs();
        } catch (JDSException e) {
            e.printStackTrace();
        }
        this.packageName = dsmBean.getPackageName();
        this.dsmBean = dsmBean;
        this.cnName = dsmBean.getDesc();
        this.basepath = dsmBean.getPackageName();
        this.space = dsmBean.getSpace();
    }

    @Override
    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public DomainInst getDsmBean() {
        return dsmBean;
    }

    public void setDsmBean(DomainInst dsmBean) {
        this.dsmBean = dsmBean;
    }

    public void setRefs(List<ViewEntityRefProxy> refs) {
        this.refs = refs;
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

    public List<AggEntityConfig> getAggEntities() {
        return aggEntities;
    }

    public void setAggEntities(List<AggEntityConfig> aggEntities) {
        this.aggEntities = aggEntities;
    }
}
