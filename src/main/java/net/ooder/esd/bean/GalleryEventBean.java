package net.ooder.esd.bean;

import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.event.CustomGalleryEvent;
import net.ooder.esd.annotation.event.GalleryEvent;
import net.ooder.esd.annotation.event.GalleryEventEnum;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.Event;
import net.ooder.web.util.AnnotationUtil;

import java.util.ArrayList;


@AnnotationType(clazz = GalleryEvent.class)
public class GalleryEventBean<T extends Action> extends Event<T, GalleryEventEnum> implements CustomBean {

    String eventId;

    String sourceClassName;

    String methodName;

    String expression;

    String domainId;


    public GalleryEventBean(String domainId, String sourceClassName, String methodName) {
        this.domainId = domainId;
        this.sourceClassName = sourceClassName;
        this.methodName = methodName;
    }

    public GalleryEventBean() {

    }

    public GalleryEventBean(CustomGalleryEvent galleryEventEnum) {
        this.eventKey = galleryEventEnum.getEventEnum();
        this.desc = galleryEventEnum.getName();
        this.eventId = eventKey.name() + "|" + eventKey.getEvent();
        this.expression = galleryEventEnum.getExpression();
        CustomAction[] actionSet = galleryEventEnum.getActions();

        addAction(actionSet);

    }

    public GalleryEventBean(GalleryEvent event) {
        this.eventKey = event.eventEnum();
        this.desc = event.desc();
        this.expression = event.expression();
        this.eventId = eventKey.name() + "|" + eventKey.getEvent();
        if (!event._return()) {
            this.eventReturn = "{false}";
        }
        addAction(event.actions());
        addAction(event.customActions());
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
