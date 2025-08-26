package net.ooder.esd.util.json;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import net.ooder.esd.annotation.field.TabItem;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class TabItemSerializer implements ObjectSerializer {
    public static final TabItemSerializer instance = new TabItemSerializer();

    public TabItemSerializer() {
        super();
    }

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) {
        SerializeWriter out = serializer.out;

        if (object instanceof TabItem) {
            TabItem tabItem = (TabItem) object;
            if (object.getClass().isEnum()) {
                Map<String, String> map = new HashMap<>();
                map.put("type", tabItem.getType());
                map.put("name", tabItem.getName());
                map.put("imageClassName", tabItem.getImageClass());
                if (tabItem.getBindClass() != null && tabItem.getBindClass().length > 0) {
                    map.put("bindClass", tabItem.getBindClass()[0].getName());
                }
                map.put("tabItemClassName", tabItem.getClass().getName());
                out.write(JSONObject.toJSONString(map));
            } else if (tabItem.toString().endsWith("{}")) {
                out.write(JSONObject.toJSONString(new DefaultTabItem()));
            } else {
                out.write(JSONObject.toJSONString(new DefaultTabItem(tabItem)));
            }
        } else {
            out.write(JSONObject.toJSONString(object));
        }


    }


}