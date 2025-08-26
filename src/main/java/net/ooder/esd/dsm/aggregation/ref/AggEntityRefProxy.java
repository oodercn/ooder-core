package net.ooder.esd.dsm.aggregation.ref;

import net.ooder.common.JDSException;
import net.ooder.esd.bean.CustomRefBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.dsm.aggregation.FieldAggConfig;

public class AggEntityRefProxy {
    String refId;
    String domainId;
    CustomRefBean ref;
    AggEntityRef dsmRef;


    public AggEntityRefProxy(AggEntityRef dsmRef, String domainId) {
        this.ref = dsmRef.getRefBean();
        this.refId = dsmRef.getRefId();
        this.domainId = domainId;
        this.dsmRef = dsmRef;

    }

    public FieldAggConfig getField() {
        return this.getMainClass().getFieldByName(dsmRef.getFieldName());

    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public CustomRefBean getRef() {
        return ref;
    }

    public void setRef(CustomRefBean ref) {
        this.ref = ref;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public AggEntityRef getDsmRef() {
        return dsmRef;
    }

    public void setDsmRef(AggEntityRef dsmRef) {
        this.dsmRef = dsmRef;
    }

    public AggEntityConfig getMainClass() {

        try {
            return DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(dsmRef.getClassName(),false);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return null;

    }

    public AggEntityConfig getOtherClass() {
        try {
            return DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(dsmRef.getOtherClassName(),false);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return null;
    }


    public FieldAggConfig getPk() {
        return this.getMainClass().getFieldByName(this.getMainClass().getSourceClass().getUid());
    }


    public MethodConfig getMethodConfig() {
        MethodConfig methodConfig = this.getMainClass().getMethodByName(dsmRef.getMethodName());

        if (methodConfig.getRequestMapping().getValue().isEmpty()) {
            methodConfig.getRequestMapping().getValue().add(methodConfig.getDefaultMenuItem().getMethodName());
        }
        return methodConfig;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof AggEntityRefProxy)) {
            return ((AggEntityRefProxy) obj).getRefId().equals(this.getRefId());
        }
        return super.equals(obj);
    }
}
