package net.ooder.esd.tool.properties;

import java.util.List;

public class ModuleFunction {
    String desc;
    List<FunctionParams> params;
    List<Action> actions;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<FunctionParams> getParams() {
        return params;
    }

    public void setParams(List<FunctionParams> params) {
        this.params = params;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }
}
