package net.ooder.esd.util.json;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

import java.lang.reflect.Type;

public class JSONEditorSerializer implements ObjectSerializer, ObjectDeserializer {
    public static final JSONEditorSerializer instance = new JSONEditorSerializer();

    public JSONEditorSerializer() {
        super();
    }

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) {
        String json = JSONObject.toJSONString(object, true);
        SerializeWriter out = serializer.out;
        out.write(json);
    }

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        String json = parser.parse().toString();
        return JSONObject.parseObject(json, type);
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }
}