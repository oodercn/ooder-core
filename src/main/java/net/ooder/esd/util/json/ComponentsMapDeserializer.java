package net.ooder.esd.util.json;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.MapDeserializer;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.CtxBaseComponent;
import net.ooder.esd.tool.properties.Properties;
import net.ooder.web.util.JSONGenUtil;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ComponentsMapDeserializer extends MapDeserializer {
    public static final ComponentsMapDeserializer instance = new ComponentsMapDeserializer();

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        JSONObject obj = new JSONObject();
        parser.parseObject(obj);
        Set<String> keySet = obj.keySet();
        Map<String, Component> componentMap = new HashMap<String, Component>();
        for (String key : keySet) {
            JSONObject componenobj = obj.getJSONObject(key);
            String alias = componenobj.getString("alias");
            String classType = componenobj.getString("key");
            JSONObject propertyJson = componenobj.getJSONObject("properties");
            if (alias != null && alias.equals(ModuleComponent.PAGECTXNAME)) {
                Component component = JSONObject.parseObject(componenobj.toJSONString(), (Class<Component>) ComponentType.fromType(classType).getClazz());
                CtxBaseComponent ctxcomponent = new CtxBaseComponent(component.getChildren());
                componentMap.put(key, ctxcomponent);
            } else {
                Component component = JSONObject.parseObject(componenobj.toJSONString(), (Class<Component>) ComponentType.fromType(classType).getClazz());
                if (propertyJson != null) {
                    Class clazz = ComponentType.fromType(classType).getClazz();
                    Type realType = JSONGenUtil.getRealType(clazz, Properties.class);
                    if (realType != null) {
                        Properties properties = JSONObject.parseObject(propertyJson.toJSONString(), realType);
                        component.setProperties(properties);
                    }
                }
                componentMap.put(key, component);
            }


        }
        return (T) componentMap;


    }
}