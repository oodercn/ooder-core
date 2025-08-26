package net.ooder.esd.dsm.repository.entity;

import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.repository.config.EntityConfig;
import net.ooder.esd.dsm.repository.config.FieldEntityConfig;
import net.ooder.annotation.RefType;

public class EntityRefProxy {

    String refId;
    RefType ref;
    EntityConfig mainClass;
    EntityConfig otherClass;
    MethodConfig methodConfig;
    FieldEntityConfig pk;


    public EntityRefProxy(EntityRef dsmRef) {
        this.ref = dsmRef.getRefBean().getRef();
        this.refId = dsmRef.getRefId();


        try {
            mainClass = DSMFactory.getInstance().getRepositoryManager().getEntityConfig(dsmRef.getClassName(),false);
            otherClass = DSMFactory.getInstance().getRepositoryManager().getEntityConfig(dsmRef.getOtherClassName(), false);
            methodConfig = mainClass.getMethodByName(dsmRef.getMethodName());
            pk = mainClass.getFieldByName(mainClass.getRootClass().getUid());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public RefType getRef() {
        return ref;
    }

    public void setRef(RefType ref) {
        this.ref = ref;
    }

    public EntityConfig getMainClass() {
        return mainClass;
    }

    public void setMainClass(EntityConfig mainClass) {
        this.mainClass = mainClass;
    }

    public EntityConfig getOtherClass() {
        return otherClass;
    }

    public void setOtherClass(EntityConfig otherClass) {
        this.otherClass = otherClass;
    }

    public FieldEntityConfig getPk() {
        return pk;
    }

    public void setPk(FieldEntityConfig pk) {
        this.pk = pk;
    }

    public MethodConfig getMethodConfig() {
        return methodConfig;
    }

    public void setMethodConfig(MethodConfig methodConfig) {
        this.methodConfig = methodConfig;
    }
}
