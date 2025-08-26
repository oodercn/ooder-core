package net.ooder.esd.util.json;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.JavaObjectDeserializer;
import net.ooder.common.util.StringUtility;
import net.ooder.web.util.JSONGenUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class EnumSetDeserializer extends JavaObjectDeserializer {

    public static final EnumSetDeserializer instance = new EnumSetDeserializer();

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        String json = parser.parse().toString();
        if (type instanceof ParameterizedType && Collection.class.isAssignableFrom((Class<?>) ((ParameterizedType) type).getRawType())) {
            Class innerType = JSONGenUtil.getInnerType(type);
            if (!json.startsWith("[") && !json.endsWith("]")) {
                List<String> values = Arrays.asList(StringUtility.split(json, ";"));
                return JSONArray.parseObject(JSONArray.toJSONString(values), type, null);
            } else {
                Object obj = JSONArray.parseObject(json, type, null);
                return (T) obj;
            }
        } else {
            return super.deserialze(parser, type, fieldName);
        }

    }

}