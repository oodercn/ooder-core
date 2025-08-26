package net.ooder.esd.util.json;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.JSONPDeserializer;

import java.lang.reflect.Type;

public class SVGKeyMapDeserializer extends JSONPDeserializer {
    public static final SVGKeyMapDeserializer instance = new SVGKeyMapDeserializer();

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        JSONObject obj = new JSONObject();
        parser.parseObject(obj);
        return null;

    }
}