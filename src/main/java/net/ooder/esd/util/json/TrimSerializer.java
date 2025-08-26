package net.ooder.esd.util.json;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

import java.lang.reflect.Type;

public class TrimSerializer implements ObjectSerializer, ObjectDeserializer {

    public static final TrimSerializer instance = new TrimSerializer();

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) {
        String result = "";
        if (object instanceof String) {
            String json = object.toString();
            result = json.replaceAll(" ", "");
        }
        SerializeWriter out = serializer.out;
        out.writeString(result);
    }

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        final JSONLexer lexer = parser.getLexer();
        String result = "";
        if (lexer.token() == JSONToken.LITERAL_STRING) {
            String val = lexer.stringVal();
            lexer.nextToken(JSONToken.COMMA);
            return (T) val;
        }

        if (lexer.token() == JSONToken.LITERAL_INT) {
            String val = lexer.numberString();
            lexer.nextToken(JSONToken.COMMA);
            return (T) val;
        }

        Object value = parser.parse();

        if (value instanceof String) {
            String json = value.toString();
            result = json.replaceAll(" ", "");
        }
        return (T) result;

    }


    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }
}