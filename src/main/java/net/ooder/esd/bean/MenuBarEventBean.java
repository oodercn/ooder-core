package net.ooder.esd.bean;

import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.action.CustomLoadClassAction;
import net.ooder.esd.annotation.event.MenuBarEvent;
import net.ooder.esd.annotation.event.MenuBarEventEnum;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.Event;
import net.ooder.web.util.AnnotationUtil;

import java.lang.reflect.Constructor;
import java.util.ArrayList;


@AnnotationType(clazz = MenuBarEvent.class)
public class MenuBarEventBean<T extends Action> extends Event<T, MenuBarEventEnum> implements CustomBean {

    String eventId;

    String sourceClassName;

    String methodName;

    String expression;

    String domainId;

    String className;

    String targetFrame;

    String childName;


    public MenuBarEventBean() {

    }

    public MenuBarEventBean(String domainId, String sourceClassName, String methodName) {
        this.domainId = domainId;
        this.sourceClassName = sourceClassName;
        this.methodName = methodName;
    }


    public MenuBarEventBean(MenuBarEvent event, Constructor constructor) {
        this.sourceClassName = constructor.getDeclaringClass().getName();
        this.methodName = constructor.getName();
        this.initEvent(event);
    }


    public MenuBarEventBean(MenuBarEvent event) {
        this.initEvent(event);
    }

    public MenuBarEventBean(MenuBarEvent event, String sourceClassName, String methodName) {
        this.sourceClassName = sourceClassName;
        this.methodName = methodName;
        this.initEvent(event);
    }


    void initEvent(MenuBarEvent event) {
        this.eventKey = event.eventEnum();
        this.desc = event.desc();
        this.expression = event.expression();
        this.eventId = eventKey.name() + "_" + eventKey.getEvent() + "_" + sourceClassName + "_" + methodName;
        if (!event.pageAction().equals(CustomLoadClassAction.none)) {
            this.className = event.className();
            this.childName = event.childName();
            this.targetFrame = event.targetFrame();
            CustomLoadClassAction action = event.pageAction();
            action.reSet(event);
            this.addAction(new CustomAction[]{action});
        }
        this.eventReturn=event.eventReturn();
        if (!event._return()) {
            this.eventReturn = "{false}";
        }
        addAction(event.actions());
    }

    public void addAction(CustomAction[] actionSet) {
        if (actions == null) {
            actions = new ArrayList<>();
        }
        for (CustomAction customAction : actionSet) {
            if (!actions.contains(customAction)) {
                Action action = new Action(customAction, eventKey);
                actions.add((T) action);
            }
        }
    }


    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof ToolBarEventBean) {
            ToolBarEventBean toolBarEventBean = (ToolBarEventBean) obj;
            if (toolBarEventBean.getEventId() != null && eventId != null) {
                return toolBarEventBean.getEventId().equals(eventId);
            }
        }
        return super.equals(obj);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTargetFrame() {
        return targetFrame;
    }

    public void setTargetFrame(String targetFrame) {
        this.targetFrame = targetFrame;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
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
