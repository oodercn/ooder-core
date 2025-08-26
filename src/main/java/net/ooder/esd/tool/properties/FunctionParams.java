package net.ooder.esd.tool.properties;


import net.ooder.esd.annotation.ui.ParamsType;

public class FunctionParams {
    String id;

    ParamsType type;

    String desc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ParamsType getType() {
        return type;
    }

    public void setType(ParamsType type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
