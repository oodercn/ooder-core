package net.ooder.esd.dsm.domain;

import net.ooder.esd.dsm.domain.annotation.*;
import net.ooder.esd.dsm.domain.enums.*;
import net.ooder.web.util.AnnotationUtil;

public class CustomDomain {

    BpmDomainType bpmDomainType;

    MsgDomainType msgDomainType;

    NavDomainType navDomainType;

    OrgDomainType orgDomainType;

    VfsDomainType vfsDomainType;

    CustomDomainType domainType;

    String tempId;

    public CustomDomain(Class ctClass) {
        BpmDomain bpmDomainClass = AnnotationUtil.getClassAnnotation(ctClass, BpmDomain.class);
        MsgDomain msgDomainClass = AnnotationUtil.getClassAnnotation(ctClass, MsgDomain.class);
        NavDomain navDomainClass = AnnotationUtil.getClassAnnotation(ctClass, NavDomain.class);
        VfsDomain vfsDomainClass = AnnotationUtil.getClassAnnotation(ctClass, VfsDomain.class);
        OrgDomain orgDomainClass = AnnotationUtil.getClassAnnotation(ctClass, OrgDomain.class);

        if (bpmDomainClass != null) {
            domainType = CustomDomainType.BPM;
            bpmDomainType = bpmDomainClass.type();
            tempId = bpmDomainClass.tempId();
        } else if (msgDomainClass != null) {
            domainType = CustomDomainType.MSG;
            msgDomainType = msgDomainClass.type();
            tempId = msgDomainClass.tempId();
        } else if (navDomainClass != null) {
            domainType = CustomDomainType.NAV;
            navDomainType = navDomainClass.type();
            tempId = navDomainClass.tempId();
        } else if (vfsDomainClass != null) {
            domainType = CustomDomainType.VFS;
            vfsDomainType = vfsDomainClass.type();
            tempId = vfsDomainClass.tempId();
        } else if (orgDomainClass != null) {
            domainType = CustomDomainType.ORG;
            tempId = orgDomainClass.tempId();
            orgDomainType = orgDomainClass.type();
        }

    }


    public BpmDomainType getBpmDomainType() {
        return bpmDomainType;
    }

    public void setBpmDomainType(BpmDomainType bpmDomainType) {
        this.bpmDomainType = bpmDomainType;
    }

    public MsgDomainType getMsgDomainType() {
        return msgDomainType;
    }

    public void setMsgDomainType(MsgDomainType msgDomainType) {
        this.msgDomainType = msgDomainType;
    }

    public NavDomainType getNavDomainType() {
        return navDomainType;
    }

    public void setNavDomainType(NavDomainType navDomainType) {
        this.navDomainType = navDomainType;
    }

    public OrgDomainType getOrgDomainType() {
        return orgDomainType;
    }

    public void setOrgDomainType(OrgDomainType orgDomainType) {
        this.orgDomainType = orgDomainType;
    }

    public VfsDomainType getVfsDomainType() {
        return vfsDomainType;
    }

    public void setVfsDomainType(VfsDomainType vfsDomainType) {
        this.vfsDomainType = vfsDomainType;
    }

    public CustomDomainType getDomainType() {
        return domainType;
    }

    public void setDomainType(CustomDomainType domainType) {
        this.domainType = domainType;
    }
}
