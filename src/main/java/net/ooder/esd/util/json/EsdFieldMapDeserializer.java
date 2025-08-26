package net.ooder.esd.util.json;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.MapDeserializer;
import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import net.ooder.common.util.CaselessStringKeyHashMap;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.dsm.view.field.ESDFieldConfig;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

public class EsdFieldMapDeserializer extends MapDeserializer {
    public static final EsdFieldMapDeserializer instance = new EsdFieldMapDeserializer();

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        JSONObject obj = new JSONObject();
        parser.parseObject(obj);
        Set<String> keySet = obj.keySet();
        Map<String, ESDFieldConfig> esdFieldConfigMap = new CaselessStringKeyHashMap<String, ESDFieldConfig>();
        for (String key : keySet) {
            ESDFieldConfig esdFieldConfig = null;
            JSONObject esdFieldConfigJson = obj.getJSONObject(key);
            String clazz = esdFieldConfigJson.getString("clazz");
            if (clazz != null && !clazz.equals("")) {
                try {
                    esdFieldConfig = JSONObject.parseObject(JSONObject.toJSONString(esdFieldConfigJson), (Class<ESDFieldConfig>) ClassUtility.loadClass(clazz));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            } else {
                esdFieldConfig = JSONObject.parseObject(JSONObject.toJSONString(esdFieldConfigJson), ((ParameterizedTypeImpl) type).getActualTypeArguments()[1]);
            }
            esdFieldConfigMap.put(key, esdFieldConfig);
        }
        return (T) esdFieldConfigMap;


    }
}