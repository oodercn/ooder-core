package net.ooder.esd.util.json;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import net.ooder.esd.annotation.ui.SymbolType;

import java.io.IOException;
import java.lang.reflect.Type;

public class SymbolCodec implements ObjectSerializer, ObjectDeserializer {


    public final static SymbolCodec instance = new SymbolCodec();


    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        out.write(((SymbolType) object).getType());

    }


    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        Object value = parser.parse();
        if (value == null) {
            return null;
        }
        SymbolType symbolType = SymbolType.formType(value.toString());
        return (T) symbolType;
    }


    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }
}