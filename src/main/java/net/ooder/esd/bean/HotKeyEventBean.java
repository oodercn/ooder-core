package net.ooder.esd.bean;

import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.event.*;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.Event;
import net.ooder.web.util.AnnotationUtil;

import java.util.ArrayList;


@AnnotationType(clazz = FieldHotKeyEvent.class)
public class HotKeyEventBean<T extends Action> extends Event<T, HotKeyEventEnum> implements CustomBean {

    String eventId;

    String sourceClassName;

    String methodName;

    String expression;

    String domainId;


    public HotKeyEventBean(String domainId, String sourceClassName, String methodName) {
        this.domainId = domainId;
        this.sourceClassName = sourceClassName;
        this.methodName = methodName;
    }

    public HotKeyEventBean() {

    }

    public HotKeyEventBean(CustomHotKeyEvent hotKeyEvent) {
        this.eventKey = HotKeyEventEnum.valueOf(hotKeyEvent.getEventEnum().name());
        this.desc = hotKeyEvent.getName();
        this.eventId = eventKey.name() + "_" + eventKey.getEvent();
        this.expression = hotKeyEvent.getExpression();
        CustomAction[] actionSet = hotKeyEvent.getActions();
        addAction(actionSet);

    }

    public HotKeyEventBean(FieldHotKeyEvent event) {
        this.eventKey = event.eventEnum();
        this.desc = event.desc();
        this.expression = event.expression();
        this.eventId = eventKey.name() + "_" + eventKey.getEvent();
        if (!event._return()) {
            this.eventReturn = "{false}";
        }
        addAction(event.actions());
    }

    public void addAction(CustomAction[] actionSet) {
        if (actions == null) {
            if (actionSet.length > 0) {
                actions = new ArrayList<>();
                for (CustomAction customAction : actionSet) {
                    if (!actions.contains(customAction)) {
                        Action action = new Action(customAction, eventKey);
                        actions.add((T) action);
                    }
                }
            }
        }
    }


    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }


    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getSourceClassName() {
        return sourceClassName;
    }

    public void setSourceClassName(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    @Override
    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
