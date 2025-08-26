package net.ooder.esd.bean;

import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.event.ModuleEvent;
import net.ooder.esd.annotation.event.ModuleEventEnum;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.Event;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.ArrayList;

@AnnotationType(clazz = ModuleEvent.class)
public class ModuleEventBean extends Event<Action, ModuleEventEnum> implements CustomBean {

    String eventId;

    String sourceClassName;

    String methodName;

    String domainId;

    ModuleEventEnum eventName;

    ModuleEvent event;

    public ModuleEventBean(String domainId, String sourceClassName, String methodName, ModuleEventEnum eventName) {
        this.domainId = domainId;
        this.sourceClassName = sourceClassName;
        this.methodName = methodName;
        this.eventName = eventName;
    }


    public ModuleEventBean(ModuleEvent event, ModuleEventEnum eventName) {
        this.eventKey = event.event();
        this.desc = event.desc();
        this.event = event;
        this.eventName = eventName;
        this.eventId = eventName.name() + "|" + event.event().getType();
        CustomAction[] actionSet = event.actions();
        addAction(actionSet);

    }

    public ModuleEventBean() {

    }

    public void addAction(CustomAction[] actionSet) {
        if (actions == null) {
            if (actionSet.length > 0) {
                actions = new ArrayList<>();
                for (CustomAction customAction : actionSet) {
                    if (!actions.contains(customAction)) {
                        Action action = new Action(customAction, event);
                        actions.add(action);
                    }
                }
            }
        }
    }

    public ModuleEventEnum getEventName() {
        return eventName;
    }

    public void setEventName(ModuleEventEnum eventName) {
        this.eventName = eventName;
    }

    public ModuleEvent getEvent() {
        return event;
    }

    public void setEvent(ModuleEvent event) {
        this.event = event;
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
