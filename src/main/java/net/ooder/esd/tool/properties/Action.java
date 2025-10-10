package net.ooder.esd.tool.properties;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.common.EventKey;
import net.ooder.common.util.StringUtility;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.CustomCondition;
import net.ooder.esd.annotation.event.APIEvent;
import net.ooder.esd.annotation.event.ActionTypeEnum;
import net.ooder.esd.annotation.event.MQTTEvent;
import net.ooder.esd.annotation.event.ModuleEvent;
import net.ooder.esd.util.json.ConditionCollectionCodec;
import net.ooder.web.util.AnnotationUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AnnotationType(clazz = CustomAction.class)
public class Action<K extends EventKey> implements CustomBean {
    String id;
    String desc;
    ActionTypeEnum type;
    String target;


    @JSONField(serialize = false)
    K eventKey;
    String className;
    String enumClassName;
    String enumValue;
    String expression;
    String script;
    List<String> args = new ArrayList();
    @JSONField(deserializeUsing = ConditionCollectionCodec.class, serializeUsing = ConditionCollectionCodec.class)
    List<Condition> conditions = new ArrayList<>();
    String method;
    String redirection;
    String okFlag;
    String koFlag;
    String eventValue;
    Class eventClass;

    @JSONField(name = "return")
    Boolean _return;


    public Boolean get_return() {
        return _return;
    }

    public void set_return(Boolean _return) {
        this._return = _return;
    }


    public Action(K eventEnum) {
        this.eventKey = eventEnum;
    }


    public String getClassName() {
        return className;
    }


    public void setClassName(String className) {
        this.className = className;
    }


    public Action(CustomAction customAction, K eventEnum) {
        this.eventKey = eventEnum;
        this.eventClass = eventEnum.getClass();
        this.eventValue = eventEnum.getEvent();
        initAction(customAction, customAction.target());
    }


    public Action(CustomAction customAction, K eventEnum, String target) {
        this.eventKey = eventEnum;
        this.eventClass = eventEnum.getClass();
        this.eventValue = eventEnum.getEvent();
        initAction(customAction, target);
    }


    public Action(CustomAction customAction, MQTTEvent eventEnum) {
        this.eventKey = (K) eventEnum.event();
        this.eventClass = eventEnum.getClass();
        this.eventValue = eventEnum.eventName();
        initAction(customAction, customAction.target());
    }

    public Action(CustomAction customAction, APIEvent eventEnum) {
        this.eventKey = (K) eventEnum.event();
        this.eventClass = eventEnum.getClass();
        this.eventValue = eventEnum.eventName();
        initAction(customAction, customAction.target());
    }

    public Action(CustomAction customAction, ModuleEvent eventEnum) {
        this.eventKey = (K) eventEnum.event();
        this.eventClass = eventEnum.getClass();
        this.eventValue = eventEnum.eventName();
        initAction(customAction, customAction.target());
    }

    public Action() {

    }

    public Action(CustomAction customAction, ModuleEvent eventEnum, String target) {
        this.eventKey = (K) eventEnum.event();
        this.eventClass = eventEnum.getClass();
        this.eventValue = eventEnum.eventName();
        initAction(customAction, target);
    }

    public Action(CustomAction customAction, APIEvent eventEnum, String target) {
        this.eventKey = (K) eventEnum.event();
        this.eventClass = eventEnum.getClass();
        this.eventValue = eventEnum.eventName();
        initAction(customAction, target);
    }

    void initAction(CustomAction customAction, String target) {
        if (customAction.script() != null && !customAction.script().equals("")) {
            method = "call";
            type = ActionTypeEnum.other;
            this.target = "callback";
            this._return = customAction._return();
            String script = customAction.script();
            if (!script.startsWith("{") && !script.endsWith("}")) {
                script = "{" + script + "}";
            }
            List<String> params = new ArrayList<>();
            for (String param : customAction.params()) {
                if (!param.startsWith("{") && !param.endsWith("}")) {
                    param = "{" + param + "}";
                }
                params.add(param);
            }
            String[] argArr = new String[]{script, null, null, null};
            args.addAll(Arrays.asList(argArr));
            args.addAll(params);
            this.id = customAction.getClass().getSimpleName() + "_" + customAction.name() + "_" + method;
        } else {
            this.id = type + "_" + method;
            this.type = customAction.type();
            this._return = customAction._return();
            if (target != null && (!target.equals(customAction.target()))) {
                this.target = target;
                this.id = target + "_" + this.id;
                if (customAction.args() != null) {
                    this.args = Arrays.asList(customAction.args());
                    int k = 0;
                    for (String arg : this.args) {
                        String newArg = StringUtility.replace(arg, "page." + customAction.target() + ".", "page." + target + ".");
                        this.updateArgs(newArg, k);
                    }
                }
            } else {
                this.target = customAction.target();
                if (customAction.args() != null) {
                    this.args = Arrays.asList(customAction.args());
                }
            }

            this.method = customAction.method();
            this.redirection = customAction.redirection();

            if (customAction.getClass().isEnum()) {
                this.enumClassName = customAction.getClass().getName();
                this.enumValue = customAction.name();
                this.id = enumClassName + "_" + enumValue + "_" + this.id;
            } else {
                this.id = customAction.getClass().getSimpleName() + "_" + this.id;
            }
        }


        if (customAction.conditions() != null) {
            conditions = new ArrayList<>();
            for (CustomCondition customCondition : customAction.conditions()) {
                conditions.add(new Condition(customCondition));
            }
        }
        this.desc = customAction.desc().equals("") ? eventKey + "(" + method + ")" : customAction.desc();
    }


    public String getEventValue() {
        return eventValue;
    }

    public void setEventValue(String eventValue) {
        this.eventValue = eventValue;
    }

    public Class<? extends EventKey> getEventClass() {
        return eventClass;
    }

    public void setEventClass(Class<? extends EventKey> eventClass) {
        this.eventClass = eventClass;
    }

    public String getEnumValue() {
        return enumValue;
    }

    public void setEnumValue(String enumValue) {
        this.enumValue = enumValue;
    }

    public String getEnumClassName() {
        return enumClassName;
    }

    public void setEnumClassName(String enumClassName) {
        this.enumClassName = enumClassName;
    }


    public K getEventKey() {

        if (eventKey == null && eventClass != null && eventValue != null) {
            try {
                eventKey = (K) eventClass.getMethod("valueOf", String.class).invoke(eventClass.getEnumConstants(), eventValue);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        return eventKey;
    }

    public void setEventKey(K eventKey) {
        this.eventKey = eventKey;
    }

    public void updateArgs(String value, int k) {
        this.args.set(k, value);
    }


    public void addCondition(Condition condition) {
        if (this.conditions == null) {
            conditions = new ArrayList<Condition>();
        }
        if (!conditions.contains(condition)) {
            conditions.add(condition);
        }
    }


    public Condition getConditionById(String conditionId) {
        if (this.conditions != null) {
            for (Condition condition : conditions) {
                if (condition.getConditionId().equals(conditionId)) {
                    return condition;
                }
            }
        }
        return null;
    }

    public void deleteCondition(String conditionId) {
        Condition currCondition = getConditionById(conditionId);
        if (currCondition != null) {
            conditions.remove(currCondition);
        }
    }

    public String getRedirection() {
        return redirection;
    }

    public void setRedirection(String redirection) {
        this.redirection = redirection;
    }

    public String getOkFlag() {
        return okFlag;
    }

    public void setOkFlag(String okFlag) {
        this.okFlag = okFlag;
    }

    public String getKoFlag() {
        return koFlag;
    }

    public void setKoFlag(String koFlag) {
        this.koFlag = koFlag;
    }


    public void setArgs(List args) {
        this.args = args;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public List getArgs() {
        return args;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public ActionTypeEnum getType() {
        return type;
    }

    public void setType(ActionTypeEnum type) {
        this.type = type;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getId() {
        return id;
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


    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }





    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (this.getId() != null) {
            if (obj instanceof Action) {
                String tid = ((Action) obj).getId();
                if (tid != null && tid.equals(this.getId())) {
                    return true;
                }
            }
            return false;
        } else {
            return JSONObject.toJSONString(obj).equals(JSONObject.toJSONString(this));
        }
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
