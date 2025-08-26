package net.ooder.esd.dsm.view.ref;

import net.ooder.common.JDSException;
import net.ooder.esd.bean.CustomRefBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.CustomMethodInfo;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.view.ViewEntityConfig;

public class ViewEntityRefProxy {

    String refId;
    CustomRefBean ref;
    ViewEntityConfig main;
    ViewEntityConfig other;
    ESDClass mainClass;
    ESDClass otherClass;
    ESDField pk;
    ESDField fk;
    ESDField field;
    MethodConfig methodConfig;
    ESDField caption;
    ESDField otherCaption;

    public ViewEntityRefProxy(ViewEntityRef dsmRef) throws JDSException {
        String fkString = dsmRef.getFkField();
        String pkString = dsmRef.getPkField();
        String captionString = dsmRef.getMainCaption();
        String otherCaptionString = dsmRef.getOtherCaption();
        this.ref = dsmRef.getRefBean();
        this.refId = dsmRef.getRefId();
        try {
            main = DSMFactory.getInstance().getViewManager().getViewEntityConfig(dsmRef.getClassName(),dsmRef.getDomainId());
            other = DSMFactory.getInstance().getViewManager().getViewEntityConfig(dsmRef.getOtherClassName(), dsmRef.getDomainId());
            this.mainClass = main.getCurrClass();
            this.otherClass = other.getCurrClass();
            this.field = main.getCurrClass().getField(dsmRef.getMethodName());
            this.methodConfig = main.getCurrConfig().getMethodByName(dsmRef.getMethodName());
            if (methodConfig == null) {
                CustomMethodInfo methodInfo = mainClass.getMethodInfo(dsmRef.getMethodName());
                methodConfig = new MethodConfig(methodInfo, main);
            }


            pk = mainClass.getField(pkString);
            if (pk == null) {
                pk = mainClass.getField(mainClass.getUid());
            }
            fk = otherClass.getField(fkString);
            if (fk == null) {
                fk = otherClass.getField(otherClass.getUid());
            }

            if (otherCaption == null) {
                otherCaption = otherClass.getCaptionField();
            }


            if (otherCaption == null && captionString != null && !captionString.equals("")) {
                caption = mainClass.getField(captionString);
            }

            if (otherCaptionString != null && !otherCaptionString.equals("")) {
                otherCaption = otherClass.getField(otherCaptionString);
            }

            if (caption == null) {
                caption = mainClass.getCaptionField();
            }
            if (otherCaption == null) {
                otherCaption = otherClass.getField(otherClass.getUid());
            }


        } catch (Throwable e) {
            e.printStackTrace();
            throw new JDSException(e);
        }
    }

    public ESDField getField() {
        return field;
    }

    public void setField(ESDField field) {
        this.field = field;
    }

    public ViewEntityConfig getMain() {
        return main;
    }

    public void setMain(ViewEntityConfig main) {
        this.main = main;
    }

    public ViewEntityConfig getOther() {
        return other;
    }

    public void setOther(ViewEntityConfig other) {
        this.other = other;
    }

    public MethodConfig getMethodConfig() {
        return methodConfig;
    }

    public void setMethodConfig(MethodConfig methodConfig) {
        this.methodConfig = methodConfig;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getRefId() {
        return refId;
    }

    public CustomRefBean getRef() {
        return ref;
    }

    public void setRef(CustomRefBean ref) {
        this.ref = ref;
    }

    public ESDClass getMainClass() {
        return mainClass;
    }

    public void setMainClass(ESDClass mainClass) {
        this.mainClass = mainClass;
    }

    public ESDClass getOtherClass() {
        return otherClass;
    }

    public void setOtherClass(ESDClass otherClass) {
        this.otherClass = otherClass;
    }

    public ESDField getCaption() {
        return caption;
    }

    public void setCaption(ESDField caption) {
        this.caption = caption;
    }

    public ESDField getOtherCaption() {
        return otherCaption;
    }

    public void setOtherCaption(ESDField otherCaption) {
        this.otherCaption = otherCaption;
    }


    public ESDField getPk() {
        return pk;
    }

    public void setPk(ESDField pk) {
        this.pk = pk;
    }

    public ESDField getFk() {
        return fk;
    }

    public void setFk(ESDField fk) {
        this.fk = fk;
    }

}
