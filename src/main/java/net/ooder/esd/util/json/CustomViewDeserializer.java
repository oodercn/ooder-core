package net.ooder.esd.util.json;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.JavaObjectDeserializer;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.ui.ModuleViewType;

import java.lang.reflect.Type;

public class CustomViewDeserializer extends JavaObjectDeserializer {
    public static final CustomViewDeserializer instance = new CustomViewDeserializer();

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        T object = null;
        JSONObject jsonObject = parser.parseObject();
        if (jsonObject != null) {
            String moduleViewType = jsonObject.getString("moduleViewType");
            if (moduleViewType != null) {
                ModuleViewType moduleView = null;
                for (ModuleViewType viewType : ModuleViewType.values()) {
                    if (viewType.getType().equals(moduleViewType)) {
                        moduleView = viewType;
                    }
                }
                if (moduleView != null) {
                    try {
                        object = (T) JSONObject.parseObject(jsonObject.toJSONString(), ClassUtility.loadClass(moduleView.getBeanClassName()));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    object = JSONObject.parseObject(jsonObject.toJSONString(), type);
                }
            } else {
                object = JSONObject.parseObject(jsonObject.toJSONString(), type);
            }
        }


        return object;

    }
}