package net.ooder.esd.dsm.repository.database.ref;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.repository.database.FDTFactory;
import net.ooder.esd.dsm.repository.database.proxy.DSMColProxy;
import net.ooder.esd.dsm.repository.database.proxy.DSMTableProxy;
import net.ooder.esd.util.json.CaseEnumsSerializer;
import net.ooder.annotation.RefType;

public class TableRefProxy {


    String refId;
    @JSONField(deserializeUsing = CaseEnumsSerializer.class)
    RefType ref;
    DSMTableProxy mainTable;
    DSMTableProxy otherTable;
    DSMColProxy pk;
    DSMColProxy fk;
    DSMColProxy caption;
    DSMColProxy otherCaption;


    public TableRefProxy(TableRef tableRef) {
        String fkString = tableRef.getFkField();
        String pkString = tableRef.getPkField();
        String captionString = tableRef.getMainCaption();
        String otherCaptionString = tableRef.getOtherCaption();
        this.ref = tableRef.getRef();
        this.refId = tableRef.getRefId();
        try {
            mainTable = DSMFactory.getInstance().getRepositoryManager().getTableProxyByName(tableRef.getTablename(), tableRef.getProjectVersionName());
            otherTable = DSMFactory.getInstance().getRepositoryManager().getTableProxyByName(tableRef.getOtherTableName(), tableRef.getProjectVersionName());
            if (fkString != null && fkString.indexOf(".") > -1) {
                this.fk = FDTFactory.getInstance().getColByFullName(fkString, tableRef.getProjectVersionName());
//                this.fk.getConfig().setHidden(true);
//                this.fk.getConfig().setPid(true);
            }
            if (pkString != null && pkString.indexOf(".") > -1) {
                this.pk = FDTFactory.getInstance().getColByFullName(pkString, tableRef.getProjectVersionName());

            }
            if (captionString != null && captionString.indexOf(".") > -1) {
                this.caption = FDTFactory.getInstance().getColByFullName(captionString, tableRef.getProjectVersionName());
            } else {
                this.caption = pk;
            }

            if (otherCaptionString != null && otherCaptionString.indexOf(".") > -1) {
                this.otherCaption = FDTFactory.getInstance().getColByFullName(otherCaptionString, tableRef.getProjectVersionName());
            } else {
                otherCaption = fk;
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }


    }

    public void setRefId(String refId) {
        this.refId = refId;
    }


    public String getRefId() {
        return refId;
    }

    public RefType getRef() {
        return ref;
    }

    public void setRef(RefType ref) {
        this.ref = ref;
    }

    public DSMColProxy getPk() {
        return pk;
    }

    public void setPk(DSMColProxy pk) {
        this.pk = pk;
    }

    public DSMColProxy getFk() {
        return fk;
    }

    public void setFk(DSMColProxy fk) {
        this.fk = fk;
    }

    public DSMColProxy getCaption() {
        return caption;
    }

    public void setCaption(DSMColProxy caption) {
        this.caption = caption;
    }

    public DSMColProxy getOtherCaption() {
        return otherCaption;
    }

    public void setOtherCaption(DSMColProxy otherCaption) {
        this.otherCaption = otherCaption;
    }


    public DSMTableProxy getMainTable() {
        return mainTable;
    }

    public void setMainTable(DSMTableProxy mainTable) {
        this.mainTable = mainTable;
    }

    public DSMTableProxy getOtherTable() {
        return otherTable;
    }

    public void setOtherTable(DSMTableProxy otherTable) {
        this.otherTable = otherTable;
    }
}
