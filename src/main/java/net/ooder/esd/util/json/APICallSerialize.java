package net.ooder.esd.util.json;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.CollectionCodec;
import com.alibaba.fastjson.serializer.JSONSerializer;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.tool.component.APICallerComponent;
import org.mvel2.templates.TemplateRuntime;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class APICallSerialize extends CollectionCodec {
    public static final APICallSerialize instance = new APICallSerialize();


    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        if (object instanceof Set){
            LinkedHashSet<APICallerComponent> actions = (LinkedHashSet<APICallerComponent>) object;
            String json = JSONArray.toJSONString(actions, false);
            Map context = JDSActionContext.getActionContext().getContext();
            String objStr = (String) TemplateRuntime.eval(json,MvelDSMRoot.getInstance(), context);
            List<APICallerComponent> realAction = JSONArray.parseArray(objStr, APICallerComponent.class);
            actions.addAll(realAction);
            super.write(serializer, actions, fieldName, fieldType, features);
        }else{
            List<APICallerComponent> actions = (List<APICallerComponent>) object;
            String json = JSONArray.toJSONString(actions, false);
            Map context = JDSActionContext.getActionContext().getContext();
            String objStr = (String) TemplateRuntime.eval(json,MvelDSMRoot.getInstance(), context);
            List<APICallerComponent> realAction = JSONArray.parseArray(objStr, APICallerComponent.class);
            super.write(serializer, realAction, fieldName, fieldType, features);
        }


    }
}