package net.ooder.esd.util.json;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.JavaObjectDeserializer;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.ui.ModuleViewType;

import java.lang.reflect.Type;

public class CustomDataDeserializer extends JavaObjectDeserializer {
    public static final JavaObjectDeserializer instance = new JavaObjectDeserializer();

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        T object = null;
        JSONObject jsonObject = parser.parseObject();
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
                    object = (T) JSONObject.parseObject(jsonObject.toJSONString(), ClassUtility.loadClass(moduleView.getDataClassName()));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                object = (T) JSONObject.parseObject(jsonObject.toJSONString(), type);

            }
        } else {
            object = (T) JSONObject.parseObject(jsonObject.toJSONString(), type);
        }

        return object;

    }
}