package net.ooder.esd.util.json;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.JavaObjectDeserializer;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.properties.Properties;
import net.ooder.web.util.JSONGenUtil;
import org.mvel2.templates.TemplateRuntime;

import java.lang.reflect.Type;
import java.util.Map;

public class ComponentDeserializer extends JavaObjectDeserializer {
    public static final ComponentDeserializer instance = new ComponentDeserializer();

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        T component = null;
        JSONObject obj = parser.parseObject();
        if (obj.getString("key") != null) {
            String classType = obj.getString("key");
            component = JSONObject.parseObject(obj.toJSONString(), (Class<T>) ComponentType.fromType(classType).getClazz());
            JSONObject property = obj.getJSONObject("properties");
            //兼容版本，后期会移除
            String tagter = obj.getString("tagter");
            if (tagter != null && component instanceof Component) {
                ((Component) component).setTarget(tagter);
            }
            if (property != null) {
                String propertyJson = obj.getJSONObject("properties").toJSONString();
                Map context = JDSActionContext.getActionContext().getContext();
                propertyJson = (String) TemplateRuntime.eval(propertyJson, context);
                Class clazz = ComponentType.fromType(classType).getClazz();
                Type realType = JSONGenUtil.getRealType(clazz, Properties.class);
                if (realType != null) {
                    Properties properties = JSONObject.parseObject(propertyJson, realType);
                    ((Component) component).setProperties(properties);
                }
            }
        }
        return component;

    }
}