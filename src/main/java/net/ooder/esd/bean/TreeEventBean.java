package net.ooder.esd.bean;

import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.action.CustomLoadClassAction;
import net.ooder.esd.annotation.event.CustomTreeEvent;
import net.ooder.esd.annotation.event.TreeEvent;
import net.ooder.esd.annotation.event.TreeViewEventEnum;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.Event;
import net.ooder.web.util.AnnotationUtil;

import java.lang.reflect.Constructor;
import java.util.ArrayList;


@AnnotationType(clazz = TreeEvent.class)
public class TreeEventBean<T extends Action> extends Event<T, TreeViewEventEnum> implements CustomBean {

    String eventId;

    String sourceClassName;

    String methodName;

    String expression;

    String domainId;

    String className;

    String targetFrame;

    String childName;


    public TreeEventBean(String domainId, String sourceClassName, String methodName) {
        this.domainId = domainId;
        this.sourceClassName = sourceClassName;
        this.methodName = methodName;
    }

    public TreeEventBean() {

    }

    public TreeEventBean(CustomTreeEvent customTreeEvent) {
        this.eventKey = customTreeEvent.getEventEnum();
        this.desc = customTreeEvent.getName();
        this.eventId = eventKey.name() + "_" + eventKey.getEvent();
        this.expression = customTreeEvent.getExpression();
        CustomAction[] actionSet = customTreeEvent.getActions();
        addAction(actionSet);

    }

    public TreeEventBean(TreeEvent event, Constructor constructor) {
        this.sourceClassName = constructor.getDeclaringClass().getName();
        this.methodName = constructor.getName();
        this.initEvent(event);
    }


    public TreeEventBean(TreeEvent event) {
        this.initEvent(event);
    }

    public TreeEventBean(TreeEvent event, String sourceClassName, String methodName) {
        this.sourceClassName = sourceClassName;
        this.methodName = methodName;
        this.initEvent(event);
    }


    void initEvent(TreeEvent event) {
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
        addAction(event.actions());
        addAction(event.customActions());
        addAction(event.localActions());
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
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof TreeEventBean) {
            TreeEventBean treeEventBean = (TreeEventBean) obj;
            if (treeEventBean.getEventId() != null && eventId != null) {
                return treeEventBean.getEventId().equals(eventId);
            }
        }
        return super.equals(obj);
    }

    @Override
    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
