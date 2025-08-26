package net.ooder.esd.dsm.repository.database.ref;


import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.util.json.CaseEnumsSerializer;
import net.ooder.annotation.RefType;

public class TableRef {


    String projectVersionName;
    String refId;
    String tablename;
    String otherTableName;
    @JSONField(deserializeUsing = CaseEnumsSerializer.class)
    RefType ref;

    String fkField;
    String expression;
    String pkField;
    String mainCaption;
    String otherCaption;


    public TableRef() {

    }

    public TableRef(String tablename, String otherTableName, RefType ref, String fkField, String mainCaption, String otherCaption, String projectVersionName) {
        this.tablename = tablename;
        this.otherTableName = otherTableName;
        this.ref = ref;
        this.fkField = fkField;
        this.refId = tablename + otherTableName;
        this.mainCaption = mainCaption;
        this.otherCaption = otherCaption;
        this.projectVersionName = projectVersionName;
    }

    public String getProjectVersionName() {
        return projectVersionName;
    }

    public void setProjectVersionName(String projectVersionName) {
        this.projectVersionName = projectVersionName;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getMainCaption() {
        return mainCaption;
    }

    public void setMainCaption(String mainCaption) {
        this.mainCaption = mainCaption;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public String getOtherTableName() {
        return otherTableName;
    }

    public void setOtherTableName(String otherTableName) {
        this.otherTableName = otherTableName;
    }

    public RefType getRef() {
        return ref;
    }

    public void setRef(RefType ref) {
        this.ref = ref;
    }


    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getFkField() {
        return fkField;
    }

    public void setFkField(String fkField) {
        this.fkField = fkField;
    }

    public String getPkField() {
        return pkField;
    }

    public void setPkField(String pkField) {
        this.pkField = pkField;
    }

    public String getOtherCaption() {
        return otherCaption;
    }

    public void setOtherCaption(String otherCaption) {
        this.otherCaption = otherCaption;
    }

}
