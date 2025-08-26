package net.ooder.esd.dsm.repository.database.proxy;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.common.database.metadata.ColInfo;
import net.ooder.common.database.metadata.TableInfo;
import net.ooder.common.util.StringUtility;
import net.ooder.esd.bean.DBTableBean;
import net.ooder.esd.bean.RepositoryBean;
import net.ooder.web.EntityBean;

import java.io.Serializable;
import java.util.*;

/**
 * @author Administrator
 */
public class DSMTableProxy implements Serializable {

    private TableInfo table;

    private List<DSMColProxy> fieldList = new ArrayList<>();

    private Map<String, DSMColProxy> fieldMap = new HashMap<>();

    private Set<DSMColProxy> allFields = new LinkedHashSet<>();

    private Set<DSMColProxy> hiddenFields = new HashSet<DSMColProxy>();

    private Set<DSMColProxy> pkFields = new HashSet<DSMColProxy>();

    private DSMColProxy captionField;

    private String projectVersionName;

    DBTableBean dbTableBean;

    RepositoryBean repositoryBean;

    EntityBean entityBean;

    public boolean isHidden(String fieldName) {
        return false;
    }

    public DSMTableProxy(TableInfo table, String projectVersionName) {
        this.table = table;
        this.projectVersionName = projectVersionName;
        for (int k = 0; k < table.getColList().size(); k++) {
            ColInfo col = table.getColList().get(k);
            DSMColProxy colProxy = new DSMColProxy(col, k, projectVersionName);
            allFields.add(colProxy);
            if (isHidden(col.getFieldname())) {
                colProxy.getTableFieldInfo().setHidden(true);
                hiddenFields.add(colProxy);
            } else if (!table.getPkNames().contains(col.getName())) {
                fieldList.add(colProxy);
            } else {
                colProxy.getTableFieldInfo().setUid(true);
                colProxy.getTableFieldInfo().setHidden(true);
                pkFields.add(colProxy);
            }
            fieldMap.put(colProxy.getTableFieldInfo().getFieldName(), colProxy);
            fieldMap.put(colProxy.getDbcol().getName(), colProxy);
        }

        repositoryBean = new RepositoryBean();
        entityBean = new EntityBean();
        this.dbTableBean = new DBTableBean();
        dbTableBean.setCname(this.getCnname());
        dbTableBean.setConfigKey(this.getConfigKey());
        dbTableBean.setPrimaryKey(this.getPkFieldName());
        dbTableBean.setTableName(this.getTableName());
    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        if (dbTableBean != null) {
            annotationBeans.add(dbTableBean);
        }
        if (repositoryBean != null) {
            annotationBeans.add(repositoryBean);
        }
        if (entityBean != null) {
            annotationBeans.add(entityBean);
        }
        return annotationBeans;
    }

    public DBTableBean getDbTableBean() {
        return dbTableBean;
    }

    public void setDbTableBean(DBTableBean dbTableBean) {
        this.dbTableBean = dbTableBean;
    }

    public TableInfo getTable() {
        return table;
    }

    public Set<DSMColProxy> getDSMColList() {
        Set colSet = new LinkedHashSet();
        for (DSMColProxy colProxy : allFields) {
            colSet.add(colProxy);
        }
        return colSet;
    }

    public String getProjectVersionName() {
        return projectVersionName;
    }

    public void setProjectVersionName(String projectVersionName) {
        this.projectVersionName = projectVersionName;
    }

    public void setTable(TableInfo table) {
        this.table = table;
    }

    public List<DSMColProxy> getFieldList() {
        return fieldList;
    }

    public String getConfigKey() {
        return table.getConfigKey();
    }

    public Set<DSMColProxy> getPkFields() {
        return pkFields;
    }

    public String getPkName() {
        return table.getPkName();
    }

    public String getPkClassName() {
        return StringUtility.formatJavaName(table.getPkName().toLowerCase(), true);
    }


    public DSMColProxy getFieldByName(String name) {
        return fieldMap.get(name);
    }

    public String getFieldName() {
        return StringUtility.formatJavaName(table.getName().toLowerCase(), false);
    }

    public String getClassName() {
        return StringUtility.formatJavaName(table.getName().toLowerCase(), true);
    }

    public String getTableName() {
        return table.getName();
    }

    public Set<String> getPkNames() {
        return table.getPkNames();
    }

    public Set<DSMColProxy> getHiddenFields() {
        return hiddenFields;
    }


    public String getTitleKey() {
        String cnname = this.table.getCnname().replaceAll("\n", "");
        if ((cnname != null) && (!"".equals(cnname)) && (cnname.contains(","))) {
            String temp = "";
            String[] name = cnname.split(",");
            temp = name[1];
            return temp.toLowerCase();
        }
        return "";
    }

    public RepositoryBean getRepositoryBean() {
        return repositoryBean;
    }

    public void setRepositoryBean(RepositoryBean repositoryBean) {
        this.repositoryBean = repositoryBean;
    }

    public Set<DSMColProxy> getAllFields() {
        return allFields;
    }


    public String getPkFieldName() {
        return StringUtility
                .formatJavaName(table.getPkName().toLowerCase(), false);
    }

    public DSMColProxy getCaptionField() {
        if (captionField == null) {
            captionField = this.getFieldByName("caption");
        }
        if (captionField == null) {
            captionField = this.getFieldByName("cnname");
        }
        if (captionField == null) {
            captionField = this.getFieldByName("name");
        }
        return captionField;
    }

    public void setCaptionField(DSMColProxy captionField) {
        this.captionField = captionField;
    }

    public String getPkUpDbName() {
        return table.getPkName().toUpperCase();
    }

    public String getCnname() {
        String cnname = this.table.getCnname().replaceAll("\n", "");
        if ((cnname != null) && (!"".equals(cnname)) && (cnname.contains(","))) {
            String temp = "";
            String[] name = cnname.split(",");
            temp = name[0];
            return temp;
        }
        return cnname;
    }


}
