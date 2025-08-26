package net.ooder.esd.util.json;

import com.alibaba.fastjson.serializer.CollectionCodec;
import com.alibaba.fastjson.serializer.JSONSerializer;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.tool.properties.item.CmdItem;

import net.ooder.jds.core.esb.EsbUtil;
import net.ooder.jds.core.esb.task.ExcuteObj;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ItemSubDeserializer<T> extends CollectionCodec {
    public static final ItemSubDeserializer instance = new ItemSubDeserializer();

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        List<CmdItem> itemList = (List<CmdItem>) object;
        itemList = filter(itemList);
        super.write(serializer, itemList, fieldName, fieldType, features);

    }


    public List<CmdItem> filter(List<CmdItem> items) {
        Object rowData = JDSActionContext.getActionContext().getContext().get("rowData");

        List<CmdItem> itemList = new ArrayList<CmdItem>();
        List<ExcuteObj> tasks = new ArrayList<ExcuteObj>();

        for (CmdItem item : items) {
            String expression = item.getExpression();
            if (expression == null || expression.equals("")) {
                expression = "true";
            }
            ExcuteObj obj = new ExcuteObj(expression, Boolean.class, item);
            tasks.add(obj);
        }
        List<ExcuteObj> results = EsbUtil.parExpression(tasks);
        for (ExcuteObj result : results) {
            if (Boolean.valueOf(result.getObj().toString())) {
                itemList.add((CmdItem) result.getSource());
            }
        }
        return itemList;
    }

}