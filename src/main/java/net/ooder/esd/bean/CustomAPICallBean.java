package net.ooder.esd.bean;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.annotation.ResponseType;
import net.ooder.annotation.SimpleCustomBean;
import net.ooder.common.EventKey;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.*;
import net.ooder.esd.annotation.event.*;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.menu.CustomFormMenu;
import net.ooder.esd.annotation.menu.CustomGalleryMenu;
import net.ooder.esd.annotation.menu.GridMenu;
import net.ooder.esd.annotation.menu.TreeMenu;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.field.FieldComponentBean;
import net.ooder.esd.custom.CustomMethodInfo;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.custom.component.form.field.CustomAPICallComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.*;
import net.ooder.esd.util.DSMAnnotationUtil;
import net.ooder.web.RequestMethodBean;
import net.ooder.web.util.AnnotationUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

@CustomClass(viewType = CustomViewType.COMPONENT, clazz = CustomAPICallComponent.class, componentType = ComponentType.APICALLER)
@AnnotationType(clazz = APIEventAnnotation.class)
public class CustomAPICallBean implements FieldComponentBean<APICallerComponent> {


    public Integer index = -1;

    public Boolean queryAsync;

    public Boolean autoRun;

    public Boolean autoDisplay;

    public String xpath;


    public Boolean checkValid;

    public Boolean checkRequired;

    public Class<? extends Enum> enumClass;

    public Boolean isAllform;

    public APICallerProperties apiCallerProperties;

    public Set<Action> customAction = new LinkedHashSet<>();

    public Set<Action> bindAction = new LinkedHashSet<>();
    //准备调
    public LinkedHashSet<CustomBeforData> beforeData = new LinkedHashSet<>();

    public Set<Action> beforeDataAction = new LinkedHashSet<>();

    //开始调用
    public LinkedHashSet<CustomBeforInvoke> beforeInvoke = new LinkedHashSet<>();

    public Set<Action> beforeInvokeAction = new LinkedHashSet<>();

    //调用后
    public LinkedHashSet<CustomCallBack> callback = new LinkedHashSet<>();

    public Set<Action> callbackAction = new LinkedHashSet<>();

    public LinkedHashSet<CustomCallBack> afterInvoke = new LinkedHashSet<>();

    public Set<Action> afterInvokAction = new LinkedHashSet<>();


    //调用出错
    public LinkedHashSet<CustomOnError> onError = new LinkedHashSet<>();

    public Set<Action> onErrorAction = new LinkedHashSet<>();

    //开始调用
    public LinkedHashSet<CustomOnData> onData = new LinkedHashSet<>();


    public Set<Action> onDataAction = new LinkedHashSet<>();

    //调用成功
    public LinkedHashSet<CustomOnExecueSuccess> onExecuteSuccess = new LinkedHashSet<>();

    public Set<Action> onExecuteSuccessAction = new LinkedHashSet<>();

    //执行失败
    public LinkedHashSet<CustomOnExecueError> onExecuteError = new LinkedHashSet<>();

    public Set<Action> onExecuteErrorAction = new LinkedHashSet<>();

    public LinkedHashSet<APIEventBean> extAPIEvent = new LinkedHashSet<>();

    public LinkedHashSet<APIEventBean> customAPIEvent = new LinkedHashSet<>();

    public LinkedHashSet<CustomMenuItem> bindMenu = new LinkedHashSet<>();


    public LinkedHashSet<GridMenu> bindGridMenu = new LinkedHashSet<>();

    public LinkedHashSet<TreeMenu> bindTreeMenu = new LinkedHashSet<>();

    public LinkedHashSet<CustomGalleryMenu> bindGalleryMenu = new LinkedHashSet<>();

    public LinkedHashSet<CustomFormMenu> bindFormMenu = new LinkedHashSet<>();

    public LinkedHashSet<CustomGridEvent> bindGridEvent = new LinkedHashSet<>();

    public LinkedHashSet<CustomMGridEvent> bindMGridEvent = new LinkedHashSet<>();

    public LinkedHashSet<CustomTreeEvent> bindTreeEvent = new LinkedHashSet<>();

    public LinkedHashSet<CustomFormEvent> bindFormEvent = new LinkedHashSet<>();

    public LinkedHashSet<CustomMFormEvent> bindMFormEvent = new LinkedHashSet<>();

    public LinkedHashSet<CustomTabsEvent> bindTabsEvent = new LinkedHashSet<>();

    public LinkedHashSet<CustomTitleBlockEvent> bindTitleBlockEvent = new LinkedHashSet<>();

    public LinkedHashSet<CustomContentBlockEvent> bindContentBlockEvent = new LinkedHashSet<>();

    public LinkedHashSet<CustomFieldEvent> bindFieldEvent = new LinkedHashSet<>();

    public LinkedHashSet<CustomHotKeyEvent> bindHotKeyEvent = new LinkedHashSet<>();

    public LinkedHashSet<CustomGalleryEvent> bindGalleryEvent = new LinkedHashSet<>();

    //自定义参数
    public Set<RequestPathBean> requestDataSource = new LinkedHashSet<>();

    public Set<ResponsePathBean> responseDataTarget = new LinkedHashSet<>();

    public Set<CallBackPathBean> responseCallback = new LinkedHashSet<>();

    //常用数参数
    public LinkedHashSet<RequestPathEnum> customRequestData = new LinkedHashSet<>();

    public LinkedHashSet<ResponsePathEnum> customResponseData = new LinkedHashSet<>();

    public LinkedHashSet<CallBackPathEnum> customResponseCallback = new LinkedHashSet<>();


    public CustomAPICallBean() {

    }


    public CustomAPICallBean(APICallerComponent apiCallerComponent) {
        this.xpath = apiCallerComponent.getPath();
        this.apiCallerProperties = apiCallerComponent.getProperties();
        this.index = apiCallerProperties.getTabindex();
        this.queryAsync = apiCallerProperties.getQueryAsync();
        this.autoRun = apiCallerProperties.getAutoRun();
        this.isAllform = apiCallerProperties.getIsAllform();
        this.checkRequired = apiCallerProperties.getCheckRequired();
        this.checkValid = apiCallerProperties.getCheckValid();

        Map<APIEventEnum, Event> eventMap = apiCallerComponent.getEvents();
        Set<APIEventEnum> eventEnums = eventMap.keySet();
        for (APIEventEnum apiEventEnum : eventEnums) {
            Event event = eventMap.get(apiEventEnum);
            switch (apiEventEnum) {
                case beforeInvoke:
                    beforeDataAction.addAll(event.getActions());
                    break;
                case callback:
                    callbackAction.addAll(event.getActions());
                    break;
                case afterInvoke:
                    afterInvokAction.addAll(event.getActions());
                    break;
                case onError:
                    onErrorAction.addAll(event.getActions());
                    break;
                case onExecuteSuccess:
                    onExecuteSuccessAction.addAll(event.getActions());
                    break;
                case onExecuteError:
                    onExecuteError.addAll(event.getActions());
                    break;
                case onData:
                    onData.addAll(event.getActions());
            }
        }

    }

    @Override
    @JSONField(serialize = false)
    public List<JavaSrcBean> getJavaSrcBeans() {
        return new ArrayList<>();
    }

    public CustomAPICallBean(ESDField esdField, Set<Annotation> annotations) {
        if (esdField instanceof CustomMethodInfo) {
            CustomMethodInfo methodInfo = (CustomMethodInfo) esdField;
            Method method = methodInfo.getInnerMethod();
            RequestMapping requestMapping = AnnotationUtil.getMethodAnnotation(method, RequestMapping.class);
            if (requestMapping != null) {
                RequestMethodBean requestMethodBean = new RequestMethodBean(method);
                this.init(requestMethodBean);
            }
        }
    }

    @Override
    public List<JavaSrcBean> update(ModuleComponent moduleComponent, APICallerComponent component) {
        return new ArrayList<>();
    }

    public CustomAPICallBean(MethodConfig methodConfig) {
        this.apiCallerProperties = new APICallerProperties(methodConfig);
        this.initMethod(methodConfig.getMethod());

    }

    public CustomAPICallBean(RequestMethodBean requestMethodBean) {
        this.init(requestMethodBean);
    }

    void initCustomAction(Method method) {
        Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation : annotations) {
            Class enumType = annotation.annotationType();
            Method[] methods = enumType.getMethods();
            for (Method aMethod : methods) {
                EventKey[] eventKeys = new EventKey[]{};
                if (aMethod.getReturnType().isArray() && EventKey.class.isAssignableFrom(aMethod.getReturnType().getComponentType())) {
                    eventKeys = (EventKey[]) aMethod.getDefaultValue();
                }

                for (EventKey eventKey : eventKeys) {
                    if (aMethod.getReturnType().isArray() && CustomAction.class.isAssignableFrom(aMethod.getReturnType().getComponentType())) {
                        CustomAction[] actions = (CustomAction[]) aMethod.getDefaultValue();
                        for (CustomAction action : actions) {
                            this.customAction.add(new Action(action, eventKey));
                        }
                    }
                }
            }

        }
    }

    void initMethod(Method method) {
        initCustomAction(method);
        APIEventAnnotation apiEventAnnotation = AnnotationUtil.getMethodAnnotation(method, APIEventAnnotation.class);
        APIEvent[] apiEvents = AnnotationUtil.getMethodAnnotations(method, APIEvent.class);
        if (apiEvents != null) {
            for (APIEvent event : apiEvents) {
                this.extAPIEvent.add(new APIEventBean(event));
            }
        }

        if (apiEventAnnotation != null) {
            if (apiEventAnnotation != null) {
                this.fillData(apiEventAnnotation);
            }
            for (CustomMenuItem item : apiEventAnnotation.bindMenu()) {
                this.getBindMenu().add(item);
            }

            for (TreeMenu item : apiEventAnnotation.bindTreeMenu()) {
                this.getBindTreeMenu().add(item);
            }
            for (CustomFormMenu item : apiEventAnnotation.bindFormMenu()) {
                this.getBindFormMenu().add(item);
            }

            for (GridMenu item : apiEventAnnotation.bindGridMenu()) {
                this.getBindGridMenu().add(item);
            }
            for (RequestPathAnnotation requestData : apiEventAnnotation.customRequestData()) {
                apiCallerProperties.addRequestData(new UrlPathData(requestData));
            }

            for (ResponsePathAnnotation responseData : apiEventAnnotation.customResponseData()) {
                apiCallerProperties.addResponseData(new UrlPathData(responseData));
            }
            for (CallBackPathAnnotation responseCallback : apiEventAnnotation.customResponseCallback()) {
                apiCallerProperties.addResponseCallBack(new UrlPathData(responseCallback));
            }

            for (ResponsePathAnnotation responseData : apiEventAnnotation.responseDataTarget()) {
                apiCallerProperties.addResponseData(new UrlPathData(responseData));
            }
            for (RequestPathAnnotation requestData : apiEventAnnotation.requestDataSource()) {
                apiCallerProperties.addRequestData(new UrlPathData(requestData));
            }
            for (CallBackPathAnnotation responseCallback : apiEventAnnotation.responseCallback()) {
                apiCallerProperties.addResponseCallBack(new UrlPathData(responseCallback));
            }


            apiCallerProperties.setAutoRun(apiEventAnnotation.autoRun());
            apiCallerProperties.setQueryAsync(apiEventAnnotation.queryAsync());
            apiCallerProperties.setIsAllform(apiEventAnnotation.isAllform());
            apiCallerProperties.setCheckRequired(apiEventAnnotation.checkRequired());
            apiCallerProperties.setCheckValid(apiEventAnnotation.checkValid());

            APIExtparams apiExtparams = DSMAnnotationUtil.getMethodCustomAnnotation(method, APIExtparams.class);
            if (apiExtparams != null) {
                if (apiExtparams != null) {
                    List<APIEvent> events = (List<APIEvent>) apiExtparams.getEvents();
                    for (APIEvent apiEvent : events) {
                        APIEventBean eventBean = new APIEventBean(apiEvent);
                        customAPIEvent.add(eventBean);
                    }
                }

                for (UrlPath urlPath : apiExtparams.getResponseDataTarget()) {
                    apiCallerProperties.addResponseData(urlPath);
                }

                for (UrlPath urlPath : apiExtparams.getRequestDataSource()) {
                    apiCallerProperties.addRequestData(urlPath);
                }

                for (UrlPath urlPath : apiExtparams.getResponseCallback()) {
                    apiCallerProperties.addResponseCallBack(urlPath);
                }

                for (ResponsePathAnnotation responseData : apiEventAnnotation.customResponseData()) {
                    apiCallerProperties.addResponseData(new UrlPathData(responseData));
                }

                for (RequestPathAnnotation requestData : apiEventAnnotation.customRequestData()) {
                    apiCallerProperties.addRequestData(new UrlPathData(requestData));
                }

                for (CallBackPathAnnotation responseCallback : apiEventAnnotation.customResponseCallback()) {
                    apiCallerProperties.addResponseCallBack(new UrlPathData(responseCallback));
                }

                apiCallerProperties.setIsAllform(apiExtparams.isAllform());
                apiCallerProperties.setAutoRun(apiExtparams.isAutoRun());
                apiCallerProperties.setQueryAsync(apiExtparams.getQueryAsync());

            }
        }


    }

    void init(RequestMethodBean methodConfig) {
        this.apiCallerProperties = new APICallerProperties(methodConfig);
        initMethod(methodConfig.getMethod());
    }


    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<APIEventBean> eventBeans = this.getAllEvent();
        for (APIEventBean eventBean : eventBeans) {
            APIEventEnum apiEventEnum = eventBean.getApiEventEnum();
            List<Action<EventKey>> actions = eventBean.getActions();
            for (Action action : actions) {
                switch (apiEventEnum) {
                    case beforeInvoke:
                        beforeInvoke.add(CustomBeforInvoke.valueOf(action.getEventValue()));
                        break;
                    case callback:
                        callback.add(CustomCallBack.valueOf(action.getEventValue()));
                        break;
                    case afterInvoke:
                        afterInvoke.add(CustomCallBack.valueOf(action.getEventValue()));
                        break;
                    case onError:
                        onError.add(CustomOnError.valueOf(action.getEventValue()));
                        break;
                    case onExecuteSuccess:
                        onExecuteSuccess.add(CustomOnExecueSuccess.valueOf(action.getEventValue()));
                        break;
                    case onExecuteError:
                        onExecuteError.add(CustomOnExecueError.valueOf(action.getEventValue()));
                        break;
                    case onData:
                        onData.add(CustomOnData.valueOf(action.getEventValue()));
                        break;
                    case beforeData:
                        beforeData.add(CustomBeforData.valueOf(action.getEventValue()));
                        break;
                }
            }
        }


        List<CustomBean> annotationBeans = new ArrayList<>();
        annotationBeans.addAll(customAPIEvent);
        annotationBeans.addAll(extAPIEvent);
        if (apiCallerProperties.getResponseType().equals(ResponseType.JSON)) {
            annotationBeans.add(new SimpleCustomBean(ResponseBody.class));
        }
        annotationBeans.add(this);
        return annotationBeans;
    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classes = new HashSet<>();
        if (enumClass != null) {
            classes.add(enumClass);
        }

        return ClassUtility.checkBase(classes);
    }

    public Action getActionById(String actionId) {
        Set<Action> actions = this.getAllActions();
        for (Action action : actions) {
            if (action.getId().equals(actionId)) {
                return action;
            }
        }
        return null;
    }

    public APIEventBean getEventById(String eventId) {
        List<APIEventBean> eventBeans = this.getAllEvent();
        for (APIEventBean eventBean : eventBeans) {
            if (eventBean.getEventId().equals(eventId)) {
                return eventBean;
            }
        }
        return null;
    }

    public Class<? extends Enum> getEnumClass() {
        return enumClass;
    }

    public void setEnumClass(Class<? extends Enum> enumClass) {
        this.enumClass = enumClass;
    }


    @JSONField(serialize = false)
    public Set<Action> getAllActions() {
        Set<Action> actions = new HashSet<>();
        List<APIEventBean> events = getAllEvent();
        for (APIEventBean<Action> event : events) {
            for (Action customAction : event.getActions()) {
                actions.add(customAction);
            }
        }
        for (Action action : this.getBindAction()) {
            actions.add(action);
        }
        return actions;

    }

    public CustomAPICallBean(APIEventAnnotation annotation) {
        fillData(annotation);
    }


    public RequestPathBean getRequestParamByName(String paramName) {
        RequestPathBean oldParamBean = null;
        for (RequestPathBean paramBean : requestDataSource) {
            if (paramBean.getParamsname().equals(paramName)) {
                oldParamBean = paramBean;
            }
        }
        return oldParamBean;
    }


    public void updateRequestParam(RequestPathBean requestParamBean) {
        RequestPathBean oldParamBean = getRequestParamByName(requestParamBean.getParamsname());
        if (oldParamBean == null) {
            oldParamBean = new RequestPathBean();
            oldParamBean.setPath(requestParamBean.getPath());
            oldParamBean.setDomainId(requestParamBean.getDomainId());
            oldParamBean.setMethodName(requestParamBean.getMethodName());
            oldParamBean.setParamsname(requestParamBean.getParamsname());
            oldParamBean.setType(requestParamBean.getType());
            requestDataSource.add(oldParamBean);
        } else {
            requestDataSource.add(requestParamBean);
        }

    }

    public ResponsePathBean getResponseParamByName(String paramName) {
        ResponsePathBean oldParamBean = null;
        for (ResponsePathBean paramBean : responseDataTarget) {
            if (paramBean.getParamsname().equals(paramName)) {
                oldParamBean = paramBean;
            }
        }
        return oldParamBean;
    }


    public void updateResponseParam(ResponsePathBean responseParamBean) {
        ResponsePathBean oldParamBean = getResponseParamByName(responseParamBean.getParamsname());
        if (oldParamBean == null) {
            oldParamBean = new ResponsePathBean();
            oldParamBean.setPath(responseParamBean.getPath());
            oldParamBean.setDomainId(responseParamBean.getDomainId());
            oldParamBean.setMethodName(responseParamBean.getMethodName());
            oldParamBean.setParamsname(responseParamBean.getParamsname());
            oldParamBean.setType(responseParamBean.getType());
            responseDataTarget.add(oldParamBean);
        } else {
            responseDataTarget.add(responseParamBean);
        }

    }

    public void fillData(APIEventAnnotation apiEventAnnotation) {
        AnnotationUtil.fillBean(apiEventAnnotation, this);
        CustomAction[] customActions = apiEventAnnotation.bindAction();
        if (customActions.length > 0) {
            bindAction = new LinkedHashSet<>();
            for (CustomAction customAction : customActions) {
                bindAction.add(new Action(customAction, APIEventEnum.afterInvoke));
            }
        }

        if (apiEventAnnotation.beforeDataAction().length > 0) {
            beforeDataAction = new LinkedHashSet<>();
            for (CustomAction customAction : apiEventAnnotation.beforeDataAction()) {
                beforeDataAction.add(new Action(customAction, APIEventEnum.beforeData));
            }
        }

        if (apiEventAnnotation.beforeInvokeAction().length > 0) {
            beforeInvokeAction = new LinkedHashSet<>();
            for (CustomAction customAction : apiEventAnnotation.beforeInvokeAction()) {
                beforeInvokeAction.add(new Action(customAction, APIEventEnum.beforeInvoke));
            }
        }

        if (apiEventAnnotation.afterInvokAction().length > 0) {
            if (afterInvokAction == null) {
                afterInvokAction = new LinkedHashSet<>();
            }
            for (CustomAction customAction : apiEventAnnotation.afterInvokAction()) {
                afterInvokAction.add(new Action(customAction, APIEventEnum.afterInvoke));
            }
        }


        if (apiEventAnnotation.onDataAction().length > 0) {
            onDataAction = new LinkedHashSet<>();
            for (CustomAction customAction : apiEventAnnotation.onDataAction()) {
                onDataAction.add(new Action(customAction, APIEventEnum.onData));
            }
        }

        if (apiEventAnnotation.onErrorAction().length > 0) {
            onErrorAction = new LinkedHashSet<>();
            for (CustomAction customAction : apiEventAnnotation.onErrorAction()) {
                onErrorAction.add(new Action(customAction, APIEventEnum.onError));
            }
        }

        if (apiEventAnnotation.onExecuteSuccessAction().length > 0) {
            onExecuteSuccessAction = new LinkedHashSet<>();
            for (CustomAction customAction : apiEventAnnotation.onExecuteSuccessAction()) {
                onExecuteSuccessAction.add(new Action(customAction, APIEventEnum.onExecuteSuccess));
            }
        }
        if (apiEventAnnotation.onExecuteErrorAction().length > 0) {
            onExecuteErrorAction = new LinkedHashSet<>();
            for (CustomAction customAction : apiEventAnnotation.onExecuteErrorAction()) {
                onExecuteErrorAction.add(new Action(customAction, APIEventEnum.onExecuteError));
            }
        }

        if (apiEventAnnotation.onErrorAction().length > 0) {
            onErrorAction = new LinkedHashSet<>();
            for (CustomAction customAction : apiEventAnnotation.onErrorAction()) {
                onErrorAction.add(new Action(customAction, APIEventEnum.onError));
            }
        }

        if (apiEventAnnotation.requestDataSource().length > 0) {
            requestDataSource = new LinkedHashSet<>();
            for (RequestPathAnnotation requestPathAnnotation : apiEventAnnotation.requestDataSource()) {
                requestDataSource.add(new RequestPathBean(requestPathAnnotation));
            }
        }

        if (apiEventAnnotation.responseDataTarget().length > 0) {
            responseDataTarget = new LinkedHashSet<>();
            for (ResponsePathAnnotation requestPathAnnotation : apiEventAnnotation.responseDataTarget()) {
                responseDataTarget.add(new ResponsePathBean(requestPathAnnotation));
            }
        }

        if (apiEventAnnotation.responseCallback().length > 0) {
            responseCallback = new LinkedHashSet<>();
            for (CallBackPathAnnotation callBackPathAnnotation : apiEventAnnotation.responseCallback()) {
                responseCallback.add(new CallBackPathBean(callBackPathAnnotation));
            }
        }

    }


    @JSONField(serialize = false)
    public List<APIEventBean> getAllEvent() {

        List<APIEventBean> apiEvents = new ArrayList<>();

        if (extAPIEvent != null) {
            apiEvents.addAll(apiEvents);
        }
        if (customAPIEvent != null) {
            apiEvents.addAll(customAPIEvent);
        }

        if (beforeData != null) {
            for (CustomBeforData beforDataEvent : beforeData) {
                if (beforDataEvent != null) {
                    apiEvents.add(new APIEventBean(beforDataEvent));
                }
            }
        }
        if (beforeInvoke != null) {
            for (CustomBeforInvoke beforInvokeEvent : beforeInvoke) {
                if (beforInvokeEvent != null) {
                    apiEvents.add(new APIEventBean(beforInvokeEvent));
                }
            }
        }
        if (callback != null) {
            for (CustomCallBack callBackEvent : callback) {
                if (callBackEvent != null) {
                    apiEvents.add(new APIEventBean(callBackEvent));
                }
            }
        }
        if (onError != null) {
            for (CustomOnError onErrorEvent : onError) {
                if (onErrorEvent != null) {
                    apiEvents.add(new APIEventBean(onErrorEvent));
                }
            }
        }

        if (onData != null) {
            for (CustomOnData onDataEvent : onData) {
                if (onDataEvent != null) {
                    apiEvents.add(new APIEventBean(onDataEvent));
                }
            }
        }
        if (onExecuteSuccess != null) {
            for (CustomOnExecueSuccess onExecuteSuccessEvent : onExecuteSuccess) {
                if (onExecuteSuccessEvent != null) {
                    apiEvents.add(new APIEventBean(onExecuteSuccessEvent));
                }
            }

        }

        if (onExecuteError != null) {
            for (CustomOnExecueError onExecueErrorEvent : onExecuteError) {
                if (onExecueErrorEvent != null) {
                    apiEvents.add(new APIEventBean(onExecueErrorEvent));
                }
            }
        }


        if (this.customAPIEvent.size() > 0) {
            apiEvents.addAll(customAPIEvent);
        }
        return apiEvents;

    }


    public APICallerProperties getApiCallerProperties() {

        for (ResponsePathBean responsePathBean : this.getResponseDataTarget()) {
            if (responsePathBean != null) {
                apiCallerProperties.addResponseData(new UrlPathData(responsePathBean));
            }
        }

        for (ResponsePathEnum responsePathBean : this.getCustomResponseData()) {
            if (responsePathBean != null) {
                apiCallerProperties.addResponseData(responsePathBean);
            }
        }

        for (RequestPathBean requestPathBean : this.getRequestDataSource()) {
            if (requestPathBean != null) {
                apiCallerProperties.addRequestData(new UrlPathData(requestPathBean));
            }
        }

        for (RequestPathEnum requestPathBean : this.getCustomRequestData()) {
            if (requestPathBean != null) {
                apiCallerProperties.addRequestData(requestPathBean);
            }
        }

        for (CallBackPathEnum callBackPathBean : this.getCustomResponseCallback()) {
            if (callBackPathBean != null) {
                apiCallerProperties.addResponseCallBack(callBackPathBean);
            }
        }
        for (CallBackPathBean callBackPathBean : this.getResponseCallback()) {
            if (callBackPathBean != null) {
                apiCallerProperties.addResponseCallBack(new UrlPathData(callBackPathBean));
            }
        }


        return apiCallerProperties;
    }


    @JSONField(serialize = false)
    public List<CustomEvent> getAllBindEvent() {
        List<CustomEvent> customEvents = new ArrayList<>();

        if (getBindGridEvent().size() > 0) {
            customEvents.addAll(getBindGridEvent());
        } else if (getBindMGridEvent().size() > 0) {
            customEvents.addAll(getBindMGridEvent());
        } else if (getBindTabsEvent().size() > 0) {
            customEvents.addAll(getBindTabsEvent());
        } else if (getBindTreeEvent().size() > 0) {
            customEvents.addAll(getBindTreeEvent());
        } else if (getBindFormEvent().size() > 0) {
            customEvents.addAll(getBindFormEvent());
        } else if (getBindMFormEvent().size() > 0) {
            customEvents.addAll(getBindMFormEvent());
        } else if (getBindGalleryEvent().size() > 0) {
            customEvents.addAll(getBindGalleryEvent());
        } else if (getBindFieldEvent().size() > 0) {
            customEvents.addAll(getBindFieldEvent());
        } else if (getBindTitleBlockEvent().size() > 0) {
            customEvents.addAll(getBindTitleBlockEvent());
        } else if (getBindContentBlockEvent().size() > 0) {
            customEvents.addAll(getBindContentBlockEvent());
        } else if (getBindHotKeyEvent().size() > 0) {
            customEvents.addAll(getBindHotKeyEvent());
        }

        return customEvents;
    }


    public void addBindEvent(CustomEvent customEvent) {
        if (customEvent instanceof CustomGridEvent) {
            getBindGridEvent().add((CustomGridEvent) customEvent);
        } else if (customEvent instanceof CustomMGridEvent) {
            getBindMGridEvent().add((CustomMGridEvent) customEvent);
        } else if (customEvent instanceof CustomFormEvent) {
            getBindFormEvent().add((CustomFormEvent) customEvent);
        } else if (customEvent instanceof CustomMFormEvent) {
            getBindMFormEvent().add((CustomMFormEvent) customEvent);
        } else if (customEvent instanceof CustomTreeEvent) {
            getBindTreeEvent().add((CustomTreeEvent) customEvent);
        } else if (customEvent instanceof CustomTabsEvent) {
            getBindTabsEvent().add((CustomTabsEvent) customEvent);
        } else if (customEvent instanceof CustomGalleryEvent) {
            getBindGalleryEvent().add((CustomGalleryEvent) customEvent);
        } else if (customEvent instanceof CustomFieldEvent) {
            getBindFieldEvent().add((CustomFieldEvent) customEvent);
        } else if (customEvent instanceof CustomTitleBlockEvent) {
            getBindTitleBlockEvent().add((CustomTitleBlockEvent) customEvent);
        } else if (customEvent instanceof CustomContentBlockEvent) {
            getBindContentBlockEvent().add((CustomContentBlockEvent) customEvent);
        } else if (customEvent instanceof CustomHotKeyEvent) {
            getBindHotKeyEvent().add((CustomHotKeyEvent) customEvent);
        }
    }

    @Override
    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public Boolean getAutoDisplay() {
        return autoDisplay;
    }

    public void setAutoDisplay(Boolean autoDisplay) {
        this.autoDisplay = autoDisplay;
    }

    public Boolean getQueryAsync() {
        return queryAsync;
    }

    public void setQueryAsync(Boolean queryAsync) {
        this.queryAsync = queryAsync;
    }

    public Boolean getAutoRun() {
        return autoRun;
    }

    public void setAutoRun(Boolean autoRun) {
        this.autoRun = autoRun;
    }

    public Boolean getAllform() {
        return isAllform;
    }

    public void setAllform(Boolean allform) {
        isAllform = allform;
    }

    public LinkedHashSet<CustomHotKeyEvent> getBindHotKeyEvent() {
        return bindHotKeyEvent;
    }

    public void setBindHotKeyEvent(LinkedHashSet<CustomHotKeyEvent> bindHotKeyEvent) {
        this.bindHotKeyEvent = bindHotKeyEvent;
    }

    public Set<Action> getBindAction() {
        return bindAction;
    }

    public void setBindAction(Set<Action> bindAction) {
        this.bindAction = bindAction;
    }


    public LinkedHashSet<APIEventBean> getCustomAPIEvent() {
        return customAPIEvent;
    }

    public void setCustomAPIEvent(LinkedHashSet<APIEventBean> customAPIEvent) {
        this.customAPIEvent = customAPIEvent;
    }

    public LinkedHashSet<CustomGridEvent> getBindGridEvent() {
        return bindGridEvent;
    }

    public void setBindGridEvent(LinkedHashSet<CustomGridEvent> bindGridEvent) {
        this.bindGridEvent = bindGridEvent;
    }

    public LinkedHashSet<CustomTreeEvent> getBindTreeEvent() {
        return bindTreeEvent;
    }

    public void setBindTreeEvent(LinkedHashSet<CustomTreeEvent> bindTreeEvent) {
        this.bindTreeEvent = bindTreeEvent;
    }

    public LinkedHashSet<CustomFormEvent> getBindFormEvent() {
        return bindFormEvent;
    }

    public void setBindFormEvent(LinkedHashSet<CustomFormEvent> bindFormEvent) {
        this.bindFormEvent = bindFormEvent;
    }

    public LinkedHashSet<CustomTabsEvent> getBindTabsEvent() {
        return bindTabsEvent;
    }

    public void setBindTabsEvent(LinkedHashSet<CustomTabsEvent> bindTabsEvent) {
        this.bindTabsEvent = bindTabsEvent;
    }

    public Set<Action> getBeforeDataAction() {
        return beforeDataAction;
    }

    public void setBeforeDataAction(Set<Action> beforeDataAction) {
        this.beforeDataAction = beforeDataAction;
    }

    public Set<Action> getBeforeInvokeAction() {
        return beforeInvokeAction;
    }

    public void setBeforeInvokeAction(Set<Action> beforeInvokeAction) {
        this.beforeInvokeAction = beforeInvokeAction;
    }

    public Set<Action> getCallbackAction() {
        return callbackAction;
    }

    public void setCallbackAction(Set<Action> callbackAction) {
        this.callbackAction = callbackAction;
    }

    public Set<Action> getOnErrorAction() {
        return onErrorAction;
    }

    public void setOnErrorAction(Set<Action> onErrorAction) {
        this.onErrorAction = onErrorAction;
    }

    public Set<Action> getOnDataAction() {
        return onDataAction;
    }

    public void setOnDataAction(Set<Action> onDataAction) {
        this.onDataAction = onDataAction;
    }

    public Set<Action> getOnExecuteSuccessAction() {
        return onExecuteSuccessAction;
    }

    public void setOnExecuteSuccessAction(Set<Action> onExecuteSuccessAction) {
        this.onExecuteSuccessAction = onExecuteSuccessAction;
    }

    public Set<Action> getOnExecuteErrorAction() {
        return onExecuteErrorAction;
    }

    public void setOnExecuteErrorAction(Set<Action> onExecuteErrorAction) {
        this.onExecuteErrorAction = onExecuteErrorAction;
    }

    public void setApiCallerProperties(APICallerProperties apiCallerProperties) {
        this.apiCallerProperties = apiCallerProperties;
    }


    public LinkedHashSet<CustomBeforData> getBeforeData() {
        return beforeData;
    }

    public void setBeforeData(LinkedHashSet<CustomBeforData> beforeData) {
        this.beforeData = beforeData;
    }

    public LinkedHashSet<CustomBeforInvoke> getBeforeInvoke() {
        return beforeInvoke;
    }

    public void setBeforeInvoke(LinkedHashSet<CustomBeforInvoke> beforeInvoke) {
        this.beforeInvoke = beforeInvoke;
    }

    public LinkedHashSet<CustomCallBack> getCallback() {
        return callback;
    }

    public void setCallback(LinkedHashSet<CustomCallBack> callback) {
        this.callback = callback;
    }

    public LinkedHashSet<CustomOnError> getOnError() {
        return onError;
    }

    public void setOnError(LinkedHashSet<CustomOnError> onError) {
        this.onError = onError;
    }

    public LinkedHashSet<CustomOnData> getOnData() {
        return onData;
    }

    public void setOnData(LinkedHashSet<CustomOnData> onData) {
        this.onData = onData;
    }

    public LinkedHashSet<CustomOnExecueSuccess> getOnExecuteSuccess() {
        return onExecuteSuccess;
    }

    public void setOnExecuteSuccess(LinkedHashSet<CustomOnExecueSuccess> onExecuteSuccess) {
        this.onExecuteSuccess = onExecuteSuccess;
    }

    public LinkedHashSet<CustomOnExecueError> getOnExecuteError() {
        return onExecuteError;
    }

    public void setOnExecuteError(LinkedHashSet<CustomOnExecueError> onExecuteError) {
        this.onExecuteError = onExecuteError;
    }

    public LinkedHashSet<CustomMFormEvent> getBindMFormEvent() {
        return bindMFormEvent;
    }

    public void setBindMFormEvent(LinkedHashSet<CustomMFormEvent> bindMFormEvent) {
        this.bindMFormEvent = bindMFormEvent;
    }

    public LinkedHashSet<CustomFieldEvent> getBindFieldEvent() {
        return bindFieldEvent;
    }

    public void setBindFieldEvent(LinkedHashSet<CustomFieldEvent> bindFieldEvent) {
        this.bindFieldEvent = bindFieldEvent;
    }

    public LinkedHashSet<CustomGalleryEvent> getBindGalleryEvent() {
        return bindGalleryEvent;
    }

    public void setBindGalleryEvent(LinkedHashSet<CustomGalleryEvent> bindGalleryEvent) {
        this.bindGalleryEvent = bindGalleryEvent;
    }

    public LinkedHashSet<CustomMenuItem> getBindMenu() {
        return bindMenu;
    }

    public void setBindMenu(LinkedHashSet<CustomMenuItem> bindMenu) {
        this.bindMenu = bindMenu;
    }

    public Set<RequestPathBean> getRequestDataSource() {
        return requestDataSource;
    }

    public void setRequestDataSource(Set<RequestPathBean> requestDataSource) {
        this.requestDataSource = requestDataSource;
    }

    public Set<ResponsePathBean> getResponseDataTarget() {
        return responseDataTarget;
    }

    public void setResponseDataTarget(Set<ResponsePathBean> responseDataTarget) {
        this.responseDataTarget = responseDataTarget;
    }

    public Set<CallBackPathBean> getResponseCallback() {
        return responseCallback;
    }

    public void setResponseCallback(Set<CallBackPathBean> responseCallback) {
        this.responseCallback = responseCallback;
    }

    public LinkedHashSet<RequestPathEnum> getCustomRequestData() {
        return customRequestData;
    }

    public void setCustomRequestData(LinkedHashSet<RequestPathEnum> customRequestData) {
        this.customRequestData = customRequestData;
    }

    public LinkedHashSet<CustomTitleBlockEvent> getBindTitleBlockEvent() {
        return bindTitleBlockEvent;
    }

    public void setBindTitleBlockEvent(LinkedHashSet<CustomTitleBlockEvent> bindTitleBlockEvent) {
        this.bindTitleBlockEvent = bindTitleBlockEvent;
    }


    public LinkedHashSet<CustomContentBlockEvent> getBindContentBlockEvent() {
        return bindContentBlockEvent;
    }

    public void setBindContentBlockEvent(LinkedHashSet<CustomContentBlockEvent> bindContentBlockEvent) {
        this.bindContentBlockEvent = bindContentBlockEvent;
    }

    public LinkedHashSet<ResponsePathEnum> getCustomResponseData() {
        return customResponseData;
    }

    public void setCustomResponseData(LinkedHashSet<ResponsePathEnum> customResponseData) {
        this.customResponseData = customResponseData;
    }

    public LinkedHashSet<CallBackPathEnum> getCustomResponseCallback() {
        return customResponseCallback;
    }

    public void setCustomResponseCallback(LinkedHashSet<CallBackPathEnum> customResponseCallback) {
        this.customResponseCallback = customResponseCallback;
    }

    public LinkedHashSet<CustomMGridEvent> getBindMGridEvent() {
        return bindMGridEvent;
    }

    public void setBindMGridEvent(LinkedHashSet<CustomMGridEvent> bindMGridEvent) {
        this.bindMGridEvent = bindMGridEvent;
    }

    public LinkedHashSet<APIEventBean> getExtAPIEvent() {
        return extAPIEvent;
    }

    public void setExtAPIEvent(LinkedHashSet<APIEventBean> extAPIEvent) {
        this.extAPIEvent = extAPIEvent;
    }

    public LinkedHashSet<GridMenu> getBindGridMenu() {
        return bindGridMenu;
    }

    public void setBindGridMenu(LinkedHashSet<GridMenu> bindGridMenu) {
        this.bindGridMenu = bindGridMenu;
    }

    public LinkedHashSet<TreeMenu> getBindTreeMenu() {
        return bindTreeMenu;
    }

    public void setBindTreeMenu(LinkedHashSet<TreeMenu> bindTreeMenu) {
        this.bindTreeMenu = bindTreeMenu;
    }

    public LinkedHashSet<CustomGalleryMenu> getBindGalleryMenu() {
        return bindGalleryMenu;
    }

    public void setBindGalleryMenu(LinkedHashSet<CustomGalleryMenu> bindGalleryMenu) {
        this.bindGalleryMenu = bindGalleryMenu;
    }

    public LinkedHashSet<CustomFormMenu> getBindFormMenu() {
        return bindFormMenu;
    }

    public void setBindFormMenu(LinkedHashSet<CustomFormMenu> bindFormMenu) {
        this.bindFormMenu = bindFormMenu;
    }

    public LinkedHashSet<CustomCallBack> getAfterInvoke() {
        return afterInvoke;
    }

    public void setAfterInvoke(LinkedHashSet<CustomCallBack> afterInvoke) {
        this.afterInvoke = afterInvoke;
    }

    public Set<Action> getAfterInvokAction() {
        return afterInvokAction;
    }

    public void setAfterInvokAction(Set<Action> afterInvokAction) {
        this.afterInvokAction = afterInvokAction;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.APICALLER;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Boolean getCheckValid() {
        return checkValid;
    }

    public void setCheckValid(Boolean checkValid) {
        this.checkValid = checkValid;
    }

    public Boolean getCheckRequired() {
        return checkRequired;
    }

    public void setCheckRequired(Boolean checkRequired) {
        this.checkRequired = checkRequired;
    }

    public Set<Action> getCustomAction() {
        return customAction;
    }

    public void setCustomAction(Set<Action> customAction) {
        this.customAction = customAction;
    }

    public String toAnnotationStr() {

        return AnnotationUtil.toAnnotationStr(this);
    }
}
