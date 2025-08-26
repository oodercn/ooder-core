package net.ooder.esd.tool.component;

import com.alibaba.fastjson.JSONObject;
import net.ooder.context.JDSActionContext;
import org.mvel2.templates.TemplateRuntime;

import java.util.ArrayList;
import java.util.Map;

public class ComponentList extends ArrayList<Component> {

    public ComponentList() {
        super();
    }

    public ComponentList addComponent(Component component) {
        super.add(component);
        return this;
    }

    public String toJson(boolean prettyFormat) {
        String json = JSONObject.toJSONString(this, prettyFormat);
        Map context = JDSActionContext.getActionContext().getContext();
        String obj = (String) TemplateRuntime.eval(json, context);
        return obj;
    }
}
