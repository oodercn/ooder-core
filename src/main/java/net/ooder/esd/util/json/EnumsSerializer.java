package net.ooder.esd.util.json;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import net.ooder.annotation.Enumstype;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

public class EnumsSerializer<T extends Enumstype> implements ObjectSerializer, ObjectDeserializer {
    public static final JSONEditorSerializer instance = new JSONEditorSerializer();

    public EnumsSerializer() {
        super();
    }

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) {
        Enumstype enumstype = (Enumstype) object;
        SerializeWriter out = serializer.out;
        if (enumstype != null && enumstype.getName() != null) {
            out.writeString(enumstype.getName());
        } else {
            out.writeNull();
        }
    }

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object object) {
        String json = parser.parse().toString();
        if (type instanceof Class) {
            Class enumClass = (Class) type;
            if (enumClass.isEnum()) {
                try {
                    T[] enums = (T[]) enumClass.getMethod("values", null).invoke(enumClass.getEnumConstants(), null);
                    for (T enumstype : enums) {
                        if (enumClass.getName().toUpperCase().equals(json.toUpperCase())) {
                            return enumstype;
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }
}