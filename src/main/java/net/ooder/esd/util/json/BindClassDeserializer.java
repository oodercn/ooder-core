package net.ooder.esd.util.json;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.JavaObjectDeserializer;
import net.ooder.common.util.ClassUtility;

import java.lang.reflect.Type;

public class BindClassDeserializer extends JavaObjectDeserializer {
    public static final BindClassDeserializer instance = new BindClassDeserializer();


    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        String json = parser.parse().toString();
        Class clazz = null;
        if (!json.equals("") && !json.startsWith("{") && !json.endsWith("}")) {
            try {
                if (json.endsWith(".class")) {
                    json = json.substring(0, json.length() - ".class".length());
                }
                clazz = ClassUtility.loadClass(json);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return (T) clazz;
    }

}