package net.ooder.esd.util.json;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.serializer.CollectionCodec;
import com.alibaba.fastjson.serializer.JSONSerializer;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.ComponentList;
import net.ooder.esd.tool.properties.Properties;
import net.ooder.web.util.JSONGenUtil;
import org.mvel2.templates.TemplateRuntime;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ComponentsDeserializer extends CollectionCodec {
    public static final ComponentsDeserializer instance = new ComponentsDeserializer();

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        JSONArray array = new JSONArray();
        parser.parseArray(array);
        ComponentList components = new ComponentList();
        for (Object obj : array) {
            if (obj != null && ((JSONObject) obj).getString("key") != null) {
                String classType = ((JSONObject) obj).getString("key");
                Component component = JSONObject.parseObject(((JSONObject) obj).toJSONString(), (Class<Component>) ComponentType.fromType(classType).getClazz());
                JSONObject property = ((JSONObject) obj).getJSONObject("properties");
                //兼容版本，后期会移除
                String tagter = ((JSONObject) obj).getString("tagter");
                if (tagter != null) {
                    component.setTarget(tagter);
                }
                if (property != null) {
                    String propertyJson = ((JSONObject) obj).getJSONObject("properties").toJSONString();
                    Map context = JDSActionContext.getActionContext().getContext();
                    propertyJson = (String) TemplateRuntime.eval(propertyJson,MvelDSMRoot.getInstance(), context);
                    Class clazz = ComponentType.fromType(classType).getClazz();
                    Type realType = JSONGenUtil.getRealType(clazz, Properties.class);
                    if (realType != null) {
                        Properties properties = JSONObject.parseObject(propertyJson, realType);
                        component.setProperties(properties);
                    }
                }
                components.add(component);
            }

        }
        return (T) components;


    }


    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        List<ComponentList> conditions = (List<ComponentList>) object;
        List<String> conditionValue = new ArrayList<>();
        for (ComponentList componentList : conditions) {
            conditionValue.add(componentList.toJson(true));
        }
        super.write(serializer, conditionValue, fieldName, fieldType, features);

    }
}