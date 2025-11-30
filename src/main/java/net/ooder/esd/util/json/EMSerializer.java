package net.ooder.esd.util.json;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.util.TypeUtils;

import java.lang.reflect.Type;

public class EMSerializer implements ObjectSerializer {
    public static final EMSerializer instance = new EMSerializer();

    public EMSerializer() {
        super();
    }

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) {
        String result = "";
        if (object instanceof String) {
            String str = object.toString();
            if (str.toLowerCase().endsWith("em")) {
                str = str.substring(0, str.length() - 2);
                result = formatNum(str);
                result = result + "em";
            } else if (str.toLowerCase().endsWith("px")) {
                str = str.substring(0, str.length() - 2);
                result = formatNum(str);
                result = result + "px";
            } else if(!str.endsWith("%")){
                result = formatNum(str);
            }
        }
        SerializeWriter out = serializer.out;
        out.writeString(result);

    }


    String formatNum(String str) {
        if (!str.equals("auto") && !str.equals("")) {
            try {
                Double dstr = TypeUtils.castToDouble(str);
                double result = Math.round(dstr * 100) / 100.0;
                str = Double.toString(result);
            } catch (JSONException e) {

            }
        }
        return str;
    }

}