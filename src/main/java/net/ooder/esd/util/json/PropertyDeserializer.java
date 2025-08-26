package net.ooder.esd.util.json;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.ComponentList;

import java.lang.reflect.Type;

public class PropertyDeserializer extends JavaBeanDeserializer {

    public PropertyDeserializer(ParserConfig config, Class<?> clazz) {
        super(config, clazz);
    }

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        JSONArray array = new JSONArray();
        parser.parseArray(array);
        ComponentList components = new ComponentList();
        for (Object obj : array) {
            if (obj != null && ((JSONObject) obj).getString("key") != null) {
                String classType = ((JSONObject) obj).getString("key");
                Component component = JSONObject.parseObject(((JSONObject) obj).toJSONString(), (Class<Component>) ComponentType.fromType(classType).getClazz());
                components.add(component);
            }

        }
        return (T) components;

    }
}