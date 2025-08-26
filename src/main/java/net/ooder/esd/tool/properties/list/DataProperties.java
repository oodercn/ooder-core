package net.ooder.esd.tool.properties.list;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.Enumstype;
import net.ooder.annotation.IconEnumstype;
import net.ooder.common.util.ClassUtility;
import net.ooder.common.util.StringUtility;
import net.ooder.annotation.CaptionEnumstype;


import net.ooder.esd.annotation.ui.CustomImageType;
import net.ooder.esd.tool.properties.AbsUIProperties;
import net.ooder.esd.tool.properties.form.FormField;
import net.ooder.esd.util.ESDEnumsUtil;

import java.util.HashMap;
import java.util.Map;

public class DataProperties<T extends Enum> extends AbsUIProperties implements FormField {
    public Map<String, Object> tagVar;
    public Map<String, String> data;
    public String tag;
    public Object value;
    public Boolean formField;
    public String propBinder;
    public String dataBinder;
    public String dataField;
    public Boolean readonly;
    public Boolean disabled;
    @JSONField(serialize = false)
    public String enumName;
    @JSONField(serialize = false)
    public T enumItem;

    public DataProperties() {

    }

    public void init(T enumType) {
        enumItem = enumType;
        addTagVar("clazz", enumType.getClass().getName());
        addTagVar("name", enumType.name());
        this.setId(enumType.name());
        this.setCaption(enumType.name());
        if (enumType instanceof CustomImageType) {
            this.id = ((CustomImageType) enumType).getImageClass();
            this.caption = ((CustomImageType) enumType).getImageClass();
            this.imageClass = ((CustomImageType) enumType).getImageClass();
        } else if (enumType instanceof IconEnumstype) {
            this.id = ((IconEnumstype) enumType).getType();
            this.caption = ((IconEnumstype) enumType).getName();
            this.imageClass = ((IconEnumstype) enumType).getImageClass();
        } else if (enumType instanceof CaptionEnumstype) {
            this.id = ((CaptionEnumstype) enumType).getType();
            this.caption = ((CaptionEnumstype) enumType).getCaption();
        } else if (enumType instanceof Enumstype) {
            this.id = ((Enumstype) enumType).getType();
            this.caption = ((Enumstype) enumType).getName();
        }
    }

    public void updateEnumName(String name) {
        if (name != null && name.indexOf("_") > -1) {
            name = StringUtility.formatJavaName(name.substring(name.lastIndexOf("_") + 1), false);
        }
        this.enumName = name;
        this.addTagVar("name", name);

    }

    public String getEnumName() {
        if (enumName == null) {
            if (this.getEnumItem() != null) {
                enumName = this.getEnumItem().name();
            } else if (this.getTagVar() != null && this.getTagVar().get("name") != null && !this.getTagVar().get("name").equals("null")) {
                enumName = (String) this.getTagVar().get("name");
            }
        }

        if (enumName == null) {
            enumName = id;
        }
        if (enumName == null || enumName.equals("null")) {
            enumName = "node";
        }
        if (enumName != null && enumName.indexOf("_") > -1) {
            enumName = StringUtility.formatJavaName(enumName.substring(enumName.lastIndexOf("_") + 1), false);
        }
        return enumName;
    }

    public Map<String, Object> addTagVar(String name, Object value) {
        if (tagVar == null) {
            tagVar = new HashMap<>();
        }
        tagVar.put(name, value);
        return tagVar;
    }

    public Map<String, Object> getTagVar() {
        return tagVar;
    }


    public T getEnumItem() {
        if (enumItem == null) {
            Map<String, Object> tagVar = this.getTagVar();
            if (tagVar != null && !tagVar.isEmpty()) {
                String className = (String) tagVar.get("clazz");
                String name = (String) tagVar.get("name");
                if (className != null) {
                    try {
                        Class clazz = ClassUtility.loadClass(className);
                        if (clazz != null && clazz.isEnum()) {
                            enumItem = ESDEnumsUtil.getEnumsByName((Class<T>) clazz, name);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        return enumItem;
    }

    public void setEnumItem(T enumItem) {
        this.enumItem = enumItem;
    }

    public void setTagVar(Map<String, Object> tagVar) {
        this.tagVar = tagVar;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }


    public String getTag() {
        return tag;
    }


    public void setTag(String tag) {
        this.tag = tag;
    }


    public Object getValue() {
        return value;
    }


    public void setValue(Object value) {
        this.value = value;
    }


    public Boolean getFormField() {
        return formField;
    }

    public void setFormField(Boolean formField) {
        this.formField = formField;
    }

    public String getPropBinder() {
        return propBinder;
    }

    public void setPropBinder(String propBinder) {
        this.propBinder = propBinder;
    }

    public String getDataBinder() {
        return dataBinder;
    }

    public void setDataBinder(String dataBinder) {
        this.dataBinder = dataBinder;
    }

    public String getDataField() {
        return dataField;
    }

    public void setDataField(String dataField) {
        this.dataField = dataField;
    }

    public Boolean getReadonly() {
        return readonly;
    }

    public void setReadonly(Boolean readonly) {
        this.readonly = readonly;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }


}
