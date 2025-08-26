package net.ooder.esd.util.json;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.JavaObjectDeserializer;
import net.ooder.common.util.ClassUtility;

import java.lang.reflect.Type;

public class EnumsClassDeserializer extends JavaObjectDeserializer {
    public static final EnumsClassDeserializer instance = new EnumsClassDeserializer();


    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        String json = parser.parse().toString();
        Class<Enum> clazz = null;
        if (!json.startsWith("{") && !json.endsWith("}")) {
            try {
                clazz = ClassUtility.loadClass(json);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return (T) clazz;
    }

}