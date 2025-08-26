package net.ooder.esd.util.json;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.JavaObjectDeserializer;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.custom.CustomViewConfigFactory;

import java.lang.reflect.Type;

public class FormFieldComboDeserializer extends JavaObjectDeserializer {
    public static final FormFieldComboDeserializer instance = new FormFieldComboDeserializer();

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        T object = null;
        JSONObject jsonObject = parser.parseObject();
        String inputTypeStr = jsonObject.getString("inputType");
        ComboInputType inputType = ComboInputType.input;
        if (inputTypeStr != null && ComboInputType.valueOf(inputTypeStr) != null) {
            inputType = ComboInputType.valueOf(inputTypeStr);
        }
        Class comboBeanClass = CustomViewConfigFactory.getInstance().getDefaultComboBoxClass(inputType);
        if (comboBeanClass != null) {
            object = (T) JSONObject.parseObject(jsonObject.toJSONString(), comboBeanClass);
        } else {
            object = (T) JSONObject.parseObject(jsonObject.toJSONString(), type);
        }
        return object;

    }
}