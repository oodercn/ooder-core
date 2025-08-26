package net.ooder.esd.tool.properties.form;


import net.ooder.esd.tool.properties.list.DataProperties;

public class HiddenInputProperties extends DataProperties {


    public Boolean isPid = false;


    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        if (value != null) {
            this.value = value.toString();
        } else {
            this.value = null;
        }

    }

    public HiddenInputProperties() {

    }




    public Boolean getPid() {
        return isPid;
    }

    public void setPid(Boolean pid) {
        isPid = pid;
    }


}
