package net.ooder.esd.bean;

import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.event.MQTTEvent;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.Event;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.ArrayList;

@AnnotationType(clazz = MQTTEvent.class)
public class MQTTEventBean extends Event implements CustomBean {

    String eventId;

    String sourceClassName;

    String methodName;

    String domainId;

    MQTTEvent event;

    public MQTTEventBean(String domainId, String sourceClassName, String methodName) {
        this.domainId = domainId;
        this.sourceClassName = sourceClassName;
        this.methodName = methodName;
    }

    public MQTTEventBean() {

    }


    public MQTTEventBean(MQTTEvent event) {
        this.eventKey = event.event();
        this.desc = event.desc();
        this.event=event;
        this.eventId = event.event().getEvent() + "|" + event.event();
        CustomAction[] actionSet = event.actions();
        addAction(actionSet);

    }


    public void addAction(CustomAction[] actionSet) {
        if (actions == null) {
            actions = new ArrayList<>();
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


    public MQTTEvent getEvent() {
        return event;
    }

    public void setEvent(MQTTEvent event) {
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
