package net.ooder.esd.util.json;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.JavaObjectDeserializer;
import net.ooder.common.JDSConstants;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.TreeItem;

import java.lang.reflect.Type;

public class TreeItemDeserializer extends JavaObjectDeserializer {
    public static final TreeItemDeserializer instance = new TreeItemDeserializer();
    protected Log log = LogFactory.getLog(JDSConstants.CONFIG_KEY, TreeItemDeserializer.class);


    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        T object = null;
        JSONObject jsonObject = parser.parseObject();
        String treeItemClassName = jsonObject.getString("treeItemClassName");
        if (treeItemClassName == null || treeItemClassName.equals("")) {
            treeItemClassName = DefaultTreeItem.class.getName();
        }
        String enumName = jsonObject.getString("type");
        try {
            Class clazz = ClassUtility.loadClass(treeItemClassName);
            if (clazz.isEnum()) {
                object = (T) clazz.getMethod("valueOf", String.class).invoke(clazz.getEnumConstants(), enumName);
            } else if (TreeItem.class.isAssignableFrom(clazz)) {
                object = (T) JSONObject.parseObject(jsonObject.toJSONString(), clazz);
            } else {
                object = (T) JSONObject.parseObject(jsonObject.toJSONString(), DefaultTreeItem.class);
            }
        } catch (Exception e) {
            log.warn("fieldName=[" + fieldName + "] enumName [" + enumName + "] ont in " + treeItemClassName + " please update enum!");
            object = (T) JSONObject.parseObject(jsonObject.toJSONString(), DefaultTreeItem.class);
        }
        return object;

    }

}