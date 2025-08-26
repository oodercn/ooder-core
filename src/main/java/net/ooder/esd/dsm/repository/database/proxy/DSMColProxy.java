package net.ooder.esd.dsm.repository.database.proxy;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.database.metadata.ColInfo;
import net.ooder.common.database.util.TypeMapping;
import net.ooder.common.util.StringUtility;
import net.ooder.annotation.CustomBean;
import net.ooder.annotation.SimpleCustomBean;
import net.ooder.esd.bean.CustomRefBean;
import net.ooder.esd.bean.DBFieldBean;
import net.ooder.esd.bean.field.FieldBean;
import net.ooder.esd.bean.field.combo.ComboInputFieldBean;
import net.ooder.esd.custom.TableFieldInfo;
import net.ooder.annotation.Caption;
import net.ooder.annotation.Pid;
import net.ooder.annotation.Uid;
import net.ooder.web.util.AnnotationUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class DSMColProxy implements Serializable {

    private Integer index;

    private String projectVersionName;

    public DBFieldBean dbField;

    public FieldBean fieldBean;

    public DBFieldConfig customBean;

    TableFieldInfo tableFieldInfo;

    ColInfo dbcol;

    CustomRefBean refBean;

    public ComboInputFieldBean comboBoxBean;


    DSMColProxy(TableFieldInfo tableFieldInfo) {
        this.tableFieldInfo = tableFieldInfo;
    }


    DSMColProxy(ColInfo col, Integer index, String projectVersionName) {
        this.dbcol = col;
        this.tableFieldInfo = new TableFieldInfo(col, index, projectVersionName);
        this.init(tableFieldInfo);
        dbField = new DBFieldBean();
        dbField.setCnName(tableFieldInfo.getDesc());
        dbField.setDbFieldName(tableFieldInfo.getFieldName());
        dbField.setDbType(col.getColType());
        dbField.setLength(col.getLength());
        dbField.setNull(col.isCanNull());
        dbField.setEnums(col.getEnums());

    }

    void init(TableFieldInfo tableFieldInfo) {
        this.tableFieldInfo = tableFieldInfo;
        this.index = tableFieldInfo.getIndex();
        this.projectVersionName = tableFieldInfo.getProjectVersionName();
        this.refBean = tableFieldInfo.getRefBean();
        this.customBean = new DBFieldConfig(tableFieldInfo);
        this.fieldBean = tableFieldInfo.getFieldBean();
        this.comboBoxBean = tableFieldInfo.getComboBean();
    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        if (customBean != null) {
            if (customBean.getPid()) {
                SimpleCustomBean simpleCustomBean = new SimpleCustomBean(Pid.class);
                annotationBeans.add(simpleCustomBean);
            } else if (customBean.getCaptionField()) {
                SimpleCustomBean simpleCustomBean = new SimpleCustomBean(Caption.class);
                annotationBeans.add(simpleCustomBean);
                annotationBeans.add(customBean);
            } else if (customBean.getUid()) {
                SimpleCustomBean simpleCustomBean = new SimpleCustomBean(Uid.class);
                annotationBeans.add(simpleCustomBean);
            } else {
                annotationBeans.add(customBean);
            }

            if (refBean != null) {
                annotationBeans.add(refBean);
            }
        }
        if (comboBoxBean != null && !AnnotationUtil.getAnnotationMap(comboBoxBean).isEmpty()) {
            annotationBeans.add(comboBoxBean);
        }
        if (fieldBean != null && !AnnotationUtil.getAnnotationMap(fieldBean).isEmpty()) {
            annotationBeans.add(fieldBean);
        }
        if (dbField != null) {
            annotationBeans.add(dbField);
        }
        return annotationBeans;
    }


    public void setIndex(Integer index) {
        this.index = index;
    }

    public TableFieldInfo getTableFieldInfo() {
        return tableFieldInfo;
    }

    public void setTableFieldInfo(TableFieldInfo tableFieldInfo) {
        this.tableFieldInfo = tableFieldInfo;
    }

    public CustomRefBean getRefBean() {
        return refBean;
    }

    public void setRefBean(CustomRefBean refBean) {
        this.refBean = refBean;
    }

    public String getProjectVersionName() {
        return projectVersionName;
    }

    public FieldBean getFieldBean() {
        return fieldBean;
    }

    public void setFieldBean(FieldBean fieldBean) {
        this.fieldBean = fieldBean;
    }

    public ComboInputFieldBean getComboBoxBean() {
        return comboBoxBean;
    }

    public void setComboBoxBean(ComboInputFieldBean comboBoxBean) {
        this.comboBoxBean = comboBoxBean;
    }

    public void setProjectVersionName(String projectVersionName) {
        this.projectVersionName = projectVersionName;
    }

    public DBFieldBean getDbField() {
        return dbField;
    }

    public void setDbField(DBFieldBean dbField) {
        this.dbField = dbField;
    }

    public DBFieldConfig getCustomBean() {
        return customBean;
    }

    public void setCustomBean(DBFieldConfig customBean) {
        this.customBean = customBean;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }


    public Integer getIndex() {
        return index;
    }


    public String getShortFieldtype() {
        return TypeMapping.getSimpleType(dbcol);
    }


    public String getLength() {
        Integer length = dbcol.getLength();
        return Integer.toString(length);
    }

    public String getFieldName() {
        return StringUtility.formatJavaName(dbcol.getName().toLowerCase(), false);
    }

    public String getClassName() {
        return StringUtility.formatJavaName(dbcol.getName().toLowerCase(), true);
    }

    public ColInfo getDbcol() {
        return dbcol;
    }

    public void setDbcol(ColInfo dbcol) {
        this.dbcol = dbcol;
    }


}
