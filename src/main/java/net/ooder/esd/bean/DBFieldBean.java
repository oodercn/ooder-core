package net.ooder.esd.bean;

import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.ColType;
import net.ooder.annotation.CustomBean;
import net.ooder.annotation.DBField;
import net.ooder.web.util.AnnotationUtil;

@AnnotationType(clazz = DBField.class)
public class DBFieldBean implements CustomBean {

    String dbFieldName;

    ColType dbType;

    String cnName;

    Boolean isNull;

    Integer length;

    String[] enums;

    Class enumClass;


    public DBFieldBean() {

    }

    public String[] getEnums() {
        return enums;
    }

    public void setEnums(String[] enums) {
        this.enums = enums;
    }

    public Class getEnumClass() {
        return enumClass;
    }

    public void setEnumClass(Class enumClass) {
        this.enumClass = enumClass;
    }

    public String getDbFieldName() {
        return dbFieldName;
    }

    public void setDbFieldName(String dbFieldName) {
        this.dbFieldName = dbFieldName;
    }

    public ColType getDbType() {
        return dbType;
    }

    public void setDbType(ColType dbType) {
        this.dbType = dbType;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public Boolean getNull() {
        return isNull;
    }

    public void setNull(Boolean aNull) {
        isNull = aNull;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public DBFieldBean(DBField annotation) {
        fillData(annotation);
    }

    public DBFieldBean fillData(DBField annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
