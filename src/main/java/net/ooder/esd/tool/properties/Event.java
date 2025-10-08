package net.ooder.esd.tool.properties;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.DSMEvent;
import net.ooder.common.EventKey;

import java.util.List;

public class Event<T extends Action, K extends EventKey> implements DSMEvent<T, K> {


    public List<T> actions;

    @JSONField(name = "return")
    public String eventReturn;

    public String script;

    @JSONField(serialize = false)
    public K eventKey;


    public String desc;

    public Event(K eventKey) {
        this.eventKey = eventKey;
    }

    public Event() {
    }


    public K getEventKey() {
        return eventKey;
    }

    public void setEventKey(K eventKey) {
        this.eventKey = eventKey;
    }

    public List<T> getActions() {
        return actions;
    }

    public void setActions(List<T> actions) {
        this.actions = actions;
    }


    public String getEventReturn() {
        return eventReturn;
    }

    public void setEventReturn(String eventReturn) {
        this.eventReturn = eventReturn;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


}
