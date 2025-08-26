package net.ooder.esd.util.json;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.JavaObjectDeserializer;
import net.ooder.common.JDSConstants;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.common.util.ClassUtility;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BindClassArrDeserializer extends JavaObjectDeserializer {
    public static final BindClassArrDeserializer instance = new BindClassArrDeserializer();

    private static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, BindClassArrDeserializer.class);

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {

        String json = parser.parse().toString();
        if (!json.startsWith("[") && !json.endsWith("]")) {
            Class[] clazzs = new Class[]{};
            try {
                Class clazz = ClassUtility.loadClass(json);
                if (clazz != null) {
                    clazzs = new Class[]{clazz};
                } else {
                    logger.error("fieldName=["+fieldName+"] bingClass err :" + json);
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return (T) clazzs;
        } else {
            JSONArray array = JSONArray.parseArray(json);
            List<Class> classList = new ArrayList<>();
            for (Object className : array) {
                try {
                    if (className != null) {
                        Class clazz = ClassUtility.loadClass(className.toString());
                        classList.add(clazz);
                    }
                } catch (ClassNotFoundException e) {
                    logger.error("fieldName=["+fieldName+"] bingClass err :" + e);
                }
            }

            return (T) classList.toArray(new Class[]{});
        }
    }

}