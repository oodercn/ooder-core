package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.event.APIEvent;
import net.ooder.esd.annotation.event.APIEventEnum;
import net.ooder.esd.annotation.event.ActionTypeEnum;
import net.ooder.esd.annotation.event.TreeViewEventEnum;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.APIEventBean;
import net.ooder.esd.bean.CustomAPICallBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.tool.properties.APICallerProperties;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.Event;
import net.ooder.web.RequestMethodBean;

import java.util.*;

public class APICallerComponent extends Component<APICallerProperties, APIEventEnum> {

    public APICallerProperties properties;

    public Set<Action> actions;

    public APICallerComponent(String alias, APICallerProperties properties) {
        super(ComponentType.APICALLER, alias);
        this.properties = properties;
    }

    void init(CustomAPICallBean apiCallBean) {
        Set<Action> customActions = apiCallBean.getBindAction();
        if (customActions.size() > 0) {
            actions = new HashSet<>();
            int k = 0;
            if (this.getActions() != null) {
                k = this.getActions().size();
            }
            for (Action customAction : customActions) {
                customAction.updateArgs("{page." + this.getAlias() + "}", 3);
                customAction.setId(this.getAlias() + "_" + k);
                actions.add(customAction);
                k++;
            }
        }
        if (apiCallBean.getExtAPIEvent() != null) {
            this.addExtEvent(apiCallBean.getExtAPIEvent());
        }

        this.addEvent(apiCallBean.getBeforeData());
        this.addAction(apiCallBean.getBeforeDataAction(), APIEventEnum.beforeData);
        this.addEvent(apiCallBean.getBeforeInvoke());
        this.addAction(apiCallBean.getBeforeInvokeAction(), APIEventEnum.beforeInvoke);
        this.addEvent(apiCallBean.getOnData());
        this.addAction(apiCallBean.getOnDataAction(), APIEventEnum.onData);
        this.addEvent(apiCallBean.getOnExecuteSuccess());
        this.addAction(apiCallBean.getOnExecuteSuccessAction(), APIEventEnum.onExecuteSuccess);
        this.addEvent(apiCallBean.getOnExecuteError());
        this.addAction(apiCallBean.getOnExecuteErrorAction(), APIEventEnum.onExecuteError);
        this.addEvent(apiCallBean.getOnError());
        this.addAction(apiCallBean.getOnErrorAction(), APIEventEnum.onError);
        this.addEvent(apiCallBean.getCallback());
        this.addAction(apiCallBean.getCallbackAction(), APIEventEnum.callback);
        this.addEvent(apiCallBean.getAfterInvoke());
        this.addAction(apiCallBean.getAfterInvokAction(), APIEventEnum.afterInvoke);

        List<APIEventBean> events = apiCallBean.getAllEvent();
        for (APIEventBean apiEvent : events) {
            List<Action> actions = apiEvent.getActions();
            int k = 0;
            if (this.getActions() != null) {
                k = this.getActions().size();
            }
            for (Action action : actions) {
                action.setId(this.getAlias() + "_" + k);
                this.addAction(action, true, apiEvent.getEventReturn());
                k++;
            }
        }

    }


    public APICallerComponent(MethodConfig methodAPIBean) {
        super(ComponentType.APICALLER, methodAPIBean.getMethodName());
        // methodAPIBean.getAPIConfig()
        if (methodAPIBean.getApi() != null) {
            this.properties = methodAPIBean.getApi().getApiCallerProperties().clone();
            init(methodAPIBean.getApi());
        } else {
            CustomAPICallBean apiCallBean = new CustomAPICallBean(methodAPIBean);
            this.properties = apiCallBean.getApiCallerProperties().clone();
        }

        if (methodAPIBean.getMethod() != null) {
            this.properties.update(methodAPIBean);
        }

        if (methodAPIBean.getFieldBean() != null && methodAPIBean.getFieldBean().getInputType() != null) {
            ComboInputType inputType = methodAPIBean.getFieldBean().getInputType();
            if (!inputType.equals(ComboInputType.none)) {
                this.properties.setMenuType(inputType);
            }
        }


    }


    public APICallerComponent(RequestMethodBean methodAPIBean) {
        super(ComponentType.APICALLER, methodAPIBean.getMethodName());
        CustomAPICallBean apiCallBean = new CustomAPICallBean(methodAPIBean);
        this.properties = apiCallBean.getApiCallerProperties().clone();
        this.init(apiCallBean);

    }


    void addEvent(List<APIEventBean> eventBeanSet) {
        for (APIEventBean apiEventBean : eventBeanSet) {
            List<Action> actions = apiEventBean.getActions();
            for (Action action : actions) {
                if (action != null) {
                    this.addAction(action, true, apiEventBean.getEventReturn());
                }
            }
        }
    }

    void addExtEvent(Set<APIEventBean> eventBeanSet) {
        for (APIEventBean apiEventBean : eventBeanSet) {
            List<Action> actions = apiEventBean.getActions();
            for (Action action : actions) {
                if (action != null) {
                    this.addAction(action, true, apiEventBean.getEventReturn());
                }
            }
        }
    }

    void addEvent(Set<? extends APIEvent> events) {
        List<APIEventBean> eventBeans = new ArrayList<>();
        for (APIEvent apiEvent : events) {
            if (apiEvent != null) {
                eventBeans.add(new APIEventBean(apiEvent));
            }
        }
        addEvent(eventBeans);
    }


    void addAction(Set<Action> actions, APIEventEnum apiEventEnum) {
        for (Action action : actions) {
            if (action != null) {
                action.setEventKey(apiEventEnum);
                this.addAction(action);
            }
        }

    }

    @Override
    public Component addAction(Action<APIEventEnum> action) {
        action = updateArgs(action, alias);
        return super.addAction(action);
    }

    private Action updateArgs(Action action, String alias) {
        if (action != null
                && action.getType() != null && action.getType().equals(ActionTypeEnum.other)
                && action.getMethod() != null && action.getMethod().equals("call")
                && action.getArgs().size() > 3) {
            action.updateArgs("{page." + alias + "}", 3);
        }
        return action;
    }


    @Override
    public void setAlias(String alias) {
        if (!alias.equals(this.alias)) {
            List<Action> actionList = new ArrayList<>();
            Map<APIEventEnum, Event> events = getEvents();
            Set<APIEventEnum> keySet = events.keySet();
            for (APIEventEnum key : keySet) {
                Event<Action, APIEventEnum> event = events.get(key);
                for (Action action : event.getActions()) {
                    actionList.add(action);
                }
            }
            if (this.getActions() != null) {
                actionList.addAll(this.getActions());
            }
            for (Action action : actionList) {
                updateArgs(action, alias);
            }
        }
        super.setAlias(alias);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof APICallerComponent) {
            return ((APICallerComponent) obj).getAlias().equals(this.getAlias());
        }
        return super.equals(obj);
    }

    public APICallerComponent() {
        super(ComponentType.APICALLER);
    }

    @Override
    public APICallerProperties getProperties() {
        return properties;
    }

    public void setProperties(APICallerProperties properties) {
        this.properties = properties;
    }

    public Set<Action> getActions() {
        return actions;
    }

    public void setActions(Set<Action> actions) {
        this.actions = actions;
    }


}
