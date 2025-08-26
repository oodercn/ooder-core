package net.ooder.esd.util.json;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.JavaObjectDeserializer;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.field.TabItem;

import java.lang.reflect.Type;

public class TabItemDeserializer extends JavaObjectDeserializer {
    public static final TabItemDeserializer instance = new TabItemDeserializer();

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        T object = null;
        JSONObject jsonObject = parser.parseObject();
        String tabItemClassName = jsonObject.getString("tabItemClassName");
        String enumName = jsonObject.getString("type");
        if (tabItemClassName != null) {
            if (tabItemClassName != null) {
                try {
                    Class clazz = ClassUtility.loadClass(tabItemClassName);
                    if (clazz.isEnum()) {
                        object = (T) clazz.getMethod("valueOf", String.class).invoke(clazz.getEnumConstants(), enumName);
                    } else if (TabItem.class.isAssignableFrom(clazz)) {
                        object = (T) JSONObject.parseObject(jsonObject.toJSONString(), clazz);
                    } else {
                        object = (T) JSONObject.parseObject(jsonObject.toJSONString(), DefaultTabItem.class);
                    }
                } catch (Exception e) {
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