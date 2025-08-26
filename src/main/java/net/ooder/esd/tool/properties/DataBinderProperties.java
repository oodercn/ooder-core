package net.ooder.esd.tool.properties;


import net.ooder.esd.tool.properties.list.DataProperties;

public class DataBinderProperties extends DataProperties {

    public String dataBinder;
    public String dataField;
    public String tag;

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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public DataBinderProperties() {

    }


}
