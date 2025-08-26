package net.ooder.esd.engine.config.dsm;

import net.ooder.common.util.CaselessStringKeyHashMap;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.view.ViewInst;

public class DSMProjectConfig {

    private CaselessStringKeyHashMap<String, DomainInst> aggregationInst = new CaselessStringKeyHashMap<String, DomainInst>();

    private CaselessStringKeyHashMap<String, ViewInst> viewDomainInst = new CaselessStringKeyHashMap<String, ViewInst>();


    public DSMProjectConfig(){

    }

    public CaselessStringKeyHashMap<String, DomainInst> getAggregationInst() {
        return aggregationInst;
    }

    public void setAggregationInst(CaselessStringKeyHashMap<String, DomainInst> aggregationInst) {
        this.aggregationInst = aggregationInst;
    }

    public CaselessStringKeyHashMap<String, ViewInst> getViewDomainInst() {
        return viewDomainInst;
    }

    public void setViewDomainInst(CaselessStringKeyHashMap<String, ViewInst> viewDomainInst) {
        this.viewDomainInst = viewDomainInst;
    }


}
