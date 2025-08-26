package net.ooder.esd.util.json;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.serializer.CollectionCodec;
import com.alibaba.fastjson.serializer.JSONSerializer;
import net.ooder.esd.annotation.ui.SymbolType;
import net.ooder.esd.tool.properties.Condition;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConditionCollectionCodec extends CollectionCodec {
    public static final ConditionCollectionCodec instance = new ConditionCollectionCodec();

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        JSONArray array = new JSONArray();
        parser.parseArray(array);
        List<Condition> conditions = new ArrayList<Condition>();
        for (Object obj : array) {
            JSONObject jsonObject = (JSONObject) obj;
            Condition condition = new Condition();
            Object leftObj = jsonObject.get("left");
            if (leftObj != null) {
                condition.setLeft(leftObj.toString());
            }
            Object rightObj = jsonObject.get("right");
            if (rightObj != null) {
                condition.setRight(rightObj.toString());
            }
            Object expressionObj = jsonObject.get("expression");
            if (expressionObj != null) {
                condition.setExpression(expressionObj.toString());
            }
            Object symbolObj = jsonObject.get("symbol");
            if (symbolObj != null) {
                SymbolType symbolType = SymbolType.formType(symbolObj.toString());
                condition.setSymbol(symbolType);
            }

            conditions.add(condition);
        }
        return (T) conditions;


    }


    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        List<Condition> conditions = (List<Condition>) object;
        List<Map> conditionValue = new ArrayList<>();
        for (Condition condition : conditions) {
            Map<String, String> symbolMap = new HashMap<>();
            if (condition.getSymbol() != null) {
                symbolMap.put("symbol", condition.getSymbol().getType());
            }
            if (condition.getExpression() != null) {
                symbolMap.put("expression", condition.getExpression());
            }
            symbolMap.put("left", condition.getLeft());
            symbolMap.put("right", condition.getRight());
            conditionValue.add(symbolMap);
        }
        super.write(serializer, conditionValue, fieldName, fieldType, features);

    }
}