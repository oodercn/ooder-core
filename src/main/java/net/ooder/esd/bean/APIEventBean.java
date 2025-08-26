package net.ooder.esd.bean;

import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.event.APIEvent;
import net.ooder.esd.annotation.event.APIEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.component.AnimBinderComponent;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.Event;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.ArrayList;


@AnnotationType(clazz = APIEvent.class)
public class APIEventBean<T extends Action> extends Event<T,APIEventEnum> implements CustomBean {

    String eventId;

    String sourceClassName;

    String methodName;

    String domainId;

    APIEventEnum apiEventEnum;

    APIEvent event;

    public APIEventBean(String domainId, String sourceClassName, String methodName) {
        this.domainId = domainId;
        this.sourceClassName = sourceClassName;
        this.methodName = methodName;
    }

    public APIEventBean() {

    }
    public APIEventBean(APIEvent event) {
        this.event=event;
        this.eventKey = event.event();
        this.desc = event.desc();
        this.apiEventEnum = event.event();
        this.eventId = apiEventEnum.name() + "|" + eventKey.getEvent();
        CustomAction[] actionSet = event.actions();
        addAction(actionSet);

    }
    public void addAction(CustomAction[] actionSet) {
        if (actions == null) {
            if (actionSet.length > 0) {
                actions = new ArrayList<>();
                for (CustomAction customAction : actionSet) {
                    if (!actions.contains(customAction)) {
                        Action action = new Action(customAction, event);
                        actions.add((T) action);
                    }
                }
            }
        }
    }


    public APIEvent getEvent() {
        return event;
    }

    public void setEvent(APIEvent event) {
        this.event = event;
    }

    public APIEventEnum getApiEventEnum() {
        return apiEventEnum;
    }

    public void setApiEventEnum(APIEventEnum apiEventEnum) {
        this.apiEventEnum = apiEventEnum;
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
