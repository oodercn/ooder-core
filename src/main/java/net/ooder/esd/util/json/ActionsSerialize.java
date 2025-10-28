package net.ooder.esd.util.json;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.CollectionCodec;
import com.alibaba.fastjson.serializer.JSONSerializer;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.tool.properties.Action;
import org.mvel2.templates.TemplateRuntime;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class ActionsSerialize extends CollectionCodec {
    public static final ActionsSerialize instance = new ActionsSerialize();


    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        List<Action> actions = (List<Action>) object;
        String json = JSONArray.toJSONString(actions, false);
        Map context = JDSActionContext.getActionContext().getContext();
        String objStr = (String) TemplateRuntime.eval(json, MvelDSMRoot.getInstance(), context);
        List<Action> realAction = JSONArray.parseArray(objStr, Action.class);
        super.write(serializer, realAction, fieldName, fieldType, features);
    }
}