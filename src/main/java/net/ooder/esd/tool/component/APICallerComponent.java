package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.event.APIEvent;
import net.ooder.esd.annotation.event.APIEventEnum;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.APIEventBean;
import net.ooder.esd.bean.CustomAPICallBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.tool.properties.APICallerProperties;
import net.ooder.esd.tool.properties.Action;
import net.ooder.web.RequestMethodBean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            for (Action customAction : customActions) {
                actions.add(customAction);
            }
        }


        this.addEvent(apiCallBean.getBeforeData());
        this.addAction(apiCallBean.getBeforeDataAction());
        this.addEvent(apiCallBean.getBeforeInvoke());
        this.addAction(apiCallBean.getBeforeInvokeAction());
        this.addEvent(apiCallBean.getOnData());
        this.addAction(apiCallBean.getOnDataAction());
        this.addEvent(apiCallBean.getOnExecuteSuccess());
        this.addAction(apiCallBean.getOnExecuteSuccessAction());
        this.addEvent(apiCallBean.getOnExecuteError());
        this.addAction(apiCallBean.getOnExecuteErrorAction());
        this.addEvent(apiCallBean.getOnError());
        this.addAction(apiCallBean.getOnErrorAction());
        this.addEvent(apiCallBean.getCallback());
        this.addAction(apiCallBean.getCallbackAction());
        this.addEvent(apiCallBean.getAfterInvoke());
        this.addAction(apiCallBean.getAfterInvokAction());

        List<APIEventBean> events = apiCallBean.getAllEvent();
        for (APIEventBean apiEvent : events) {
            List<Action> actions = apiEvent.getActions();
            for (Action action : actions) {
                this.addAction(action);
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
                    this.addAction(action);
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


    void addAction(Set<Action> actions) {
        for (Action action : actions) {
            if (action != null) {
                this.addAction(action);
            }
        }

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
