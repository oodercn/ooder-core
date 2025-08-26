package net.ooder.esd.util.json;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import net.ooder.esd.annotation.TreeItem;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class TreeItemSerializer implements ObjectSerializer {
    public static final TreeItemSerializer instance = new TreeItemSerializer();

    public TreeItemSerializer() {
        super();
    }

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) {
        SerializeWriter out = serializer.out;
        if (object.getClass().isEnum() && object instanceof TreeItem) {
            TreeItem treeItem = (TreeItem) object;
            Map<String, Object> map = new HashMap<>();
            map.put("type", treeItem.getType());
            map.put("name", treeItem.getName());
            map.put("imageClassName", treeItem.getImageClass());
            if (treeItem.getBindClass() != null && treeItem.getBindClass().length > 0) {
                map.put("bindClass", treeItem.getBindClass());
            }
            map.put("treeItemClassName", treeItem.getClass().getName());
            out.write(JSONObject.toJSONString(map));
        } else if (object.toString().equals("{}")) {
            out.write("{}");
        } else {
            out.write(JSONObject.toJSONString(object));
        }


    }


}