package net.ooder.esd.util.json;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.serializer.CollectionCodec;
import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import net.ooder.esd.annotation.ui.CallBackTypeEnum;
import net.ooder.esd.tool.properties.UrlPathData;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CallBackPathDeserializer extends CollectionCodec {
    public static final CallBackPathDeserializer instance = new CallBackPathDeserializer();

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        JSONArray array = new JSONArray();
        parser.parseArray(array);
        List paths = new ArrayList();
        for (Object obj : array) {
            if (obj instanceof JSONObject) {
                UrlPathData<CallBackTypeEnum> component = JSONObject.parseObject(((JSONObject) obj).toJSONString(), buildType(UrlPathData.class, CallBackTypeEnum.class));
                paths.add(component);
            }
        }
        return (T) paths;

    }

    private static Type buildType(Type... types) {
        ParameterizedTypeImpl beforeType = null;
        if (types != null && types.length > 0) {
            for (int i = types.length - 1; i > 0; i--) {
                beforeType = new ParameterizedTypeImpl(new Type[]{beforeType == null ? types[i] : beforeType}, null, types[i - 1]);
            }
        }
        return beforeType;
    }
}