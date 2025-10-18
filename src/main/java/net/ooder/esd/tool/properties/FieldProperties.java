package net.ooder.esd.tool.properties;

import com.alibaba.fastjson.JSON;
import net.ooder.annotation.Enumstype;
import net.ooder.annotation.IconEnumstype;
import net.ooder.esd.bean.field.FieldBean;
import net.ooder.esd.tool.properties.form.FormField;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.Map;

public class FieldProperties extends ContainerProperties implements FormField {

    public Boolean dynCheck;
    public Boolean required;
    public Boolean dirtyMark;
    public String excelCellId;
    public String excelCellFormula;


    public FieldProperties() {

    }


    public FieldProperties(FieldBean fieldBean) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(fieldBean), Map.class), this, false, false);
        if (fieldBean.getManualWidth() != null) {
            this.setWidth(fieldBean.getManualWidth() + "px");
        }
        if (fieldBean.getManualHeight() != null) {
            this.setHeight(fieldBean.getManualWidth() + "px");
        }
    }

    public FieldProperties(Enum enumType) {
        this.setId(enumType.name());

        if (enumType instanceof IconEnumstype) {
            this.id = ((IconEnumstype) enumType).getType();
            this.caption = ((IconEnumstype) enumType).getName();
            this.imageClass = ((IconEnumstype) enumType).getImageClass();
        } else if (enumType instanceof Enumstype) {
            this.id = ((Enumstype) enumType).getType();
            this.caption = ((Enumstype) enumType).getName();
        }
    }


    public FieldProperties(String id, String caption) {
        this.id = id;
        this.caption = caption;

    }

    public FieldProperties(String id, String caption, String imageClass, Map<String, Object> params) {
        this.id = id;
        this.caption = caption;
        this.imageClass = imageClass;
        this.tagVar = params;
    }

    public FieldProperties(String id, String caption, String imageClass) {
        this.id = id;
        this.caption = caption;
        this.imageClass = imageClass;
    }

    public Boolean getDynCheck() {
        return dynCheck;
    }

    public void setDynCheck(Boolean dynCheck) {
        this.dynCheck = dynCheck;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Boolean getDirtyMark() {
        return dirtyMark;
    }

    public void setDirtyMark(Boolean dirtyMark) {
        this.dirtyMark = dirtyMark;
    }

    public String getExcelCellId() {
        return excelCellId;
    }

    public void setExcelCellId(String excelCellId) {
        this.excelCellId = excelCellId;
    }

    public String getExcelCellFormula() {
        return excelCellFormula;
    }

    public void setExcelCellFormula(String excelCellFormula) {
        this.excelCellFormula = excelCellFormula;
    }

}
