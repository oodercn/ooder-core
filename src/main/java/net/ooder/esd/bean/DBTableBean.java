package net.ooder.esd.bean;

import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.annotation.DBField;
import net.ooder.annotation.DBTable;
import net.ooder.web.util.AnnotationUtil;

@AnnotationType(clazz = DBTable.class)
public class DBTableBean implements CustomBean {

    String tableName;

    String cname;

    String configKey;

    String url;

    String titleKey;

    String primaryKey;

    public DBTableBean() {

    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitleKey() {
        return titleKey;
    }

    public void setTitleKey(String titleKey) {
        this.titleKey = titleKey;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public DBTableBean(DBField annotation) {
        fillData(annotation);
    }

    public DBTableBean fillData(DBField annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
