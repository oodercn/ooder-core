package net.ooder.esd.tool.properties;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.util.json.TrimSerializer;

import java.util.Map;

/**
 * 所有组件基础属性
 */
public class Properties implements Comparable<Properties> {

    @JSONField(serializeUsing = TrimSerializer.class, deserializeUsing = TrimSerializer.class)
    public String id;
    @JSONField(serializeUsing = TrimSerializer.class, deserializeUsing = TrimSerializer.class)
    public String name;

    //控件锁定
    public Boolean locked;

    public String desc;


    //层叠顺序
    public Integer zIndex;
    //Tab顺序
    public Integer tabindex;

    public String expression;



    public Map<?, Event> events;


    public Map<?, Event> getEvents() {
        return events;
    }

    public void setEvents(Map<?, Event> events) {
        this.events = events;
    }


    public String getId() {
        return id;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getzIndex() {
        return zIndex;
    }

    public void setzIndex(Integer zIndex) {
        this.zIndex = zIndex;
    }

    public Integer getTabindex() {
        return tabindex;
    }

    public void setTabindex(Integer tabindex) {
        this.tabindex = tabindex;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getName() {
        return name;

    }


    public void setName(String name) {
        this.name = name;
    }

    public String toJson() {
        return JSONObject.toJSONString(this, true);
    }

    @Override
    public int compareTo(Properties o) {
        if (this.tabindex != null && o.getTabindex() != null) {
            return this.tabindex - o.getTabindex();
        }
        return 0;
    }
}
