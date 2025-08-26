package net.ooder.esd.util.json;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.JavaObjectDeserializer;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.custom.CustomViewConfigFactory;
import net.ooder.esd.tool.component.OpinionComponent;

import java.lang.reflect.Type;

public class FormFieldDeserializer extends JavaObjectDeserializer {
    public static final FormFieldDeserializer instance = new FormFieldDeserializer();

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        T object = null;
        JSONObject jsonObject = parser.parseObject();
        String componentTypeName = jsonObject.getString("componentType");
        if (componentTypeName == null || componentTypeName.equals("")) {
            componentTypeName = jsonObject.getString("type");
        }
        if (componentTypeName != null && !componentTypeName.equals("") && ComponentType.valueOf(componentTypeName.toUpperCase()) != null) {
            ComponentType componentType = ComponentType.valueOf(componentTypeName.toUpperCase());
            Class widgetBeanClass = CustomViewConfigFactory.getInstance().getDefaultWidgetClass(componentType);
            if (widgetBeanClass != null) {
                object = (T) JSONObject.parseObject(jsonObject.toJSONString(), widgetBeanClass);
            } else {
                object = (T) JSONObject.parseObject(jsonObject.toJSONString(), type);
            }
        } else {
            object = (T) JSONObject.parseObject(jsonObject.toJSONString(), type);
        }

        return object;

    }
}