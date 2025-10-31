package net.ooder.esd.tool.properties;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.HttpMethod;
import net.ooder.annotation.ProxyType;
import net.ooder.annotation.RequestType;
import net.ooder.annotation.ResponseType;
import net.ooder.esd.annotation.*;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.field.CustomFieldBean;
import net.ooder.esd.custom.CustomMethodInfo;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.list.DataProperties;
import net.ooder.esd.util.DSMAnnotationUtil;
import net.ooder.esd.util.json.CallBackPathDeserializer;
import net.ooder.esd.util.json.EnumsSerializer;
import net.ooder.esd.util.json.RequestPathDeserializer;
import net.ooder.esd.util.json.ResponsePathDeserializer;
import net.ooder.web.RequestMethodBean;
import net.ooder.web.RequestParamBean;
import net.ooder.web.util.AnnotationUtil;

import java.util.*;


public class APICallerProperties extends DataProperties {

    String queryURL;

    @JSONField(serialize = false)
    String metaInfo;

    String src;

    Class<? extends Enum> enumClass;

    HttpMethod queryMethod = HttpMethod.auto;

    RequestType requestType = RequestType.FORM;

    ResponseType responseType = ResponseType.JSON;

    ProxyType proxyType = ProxyType.auto;

    Boolean isAllform;

    Boolean autoRun;

    Boolean queryAsync;

    @JSONField(serialize = false)
    ComboInputType menuType;

    String tips;

    String sourceClassName;

    String domainId;

    @JSONField(deserialize = false)
    String currClassName;

    @JSONField(serialize = false)
    Set<CustomMenuItem> bindMenu = new LinkedHashSet<>();

    String methodName;

    @JSONField(serialize = false)
    Map<String, UrlPath> requestDataMap = new HashMap();


    @JSONField(serialize = false)
    Map<String, UrlPath> responseCallBackMap = new HashMap();


    @JSONField(deserializeUsing = RequestPathDeserializer.class)
    List<UrlPath> requestDataSource = new ArrayList<UrlPath>();
    //
    @JSONField(deserializeUsing = ResponsePathDeserializer.class)
    List<UrlPath> responseDataTarget = new ArrayList<UrlPath>();

    @JSONField(deserializeUsing = CallBackPathDeserializer.class)
    List<UrlPath> responseCallback = new ArrayList<UrlPath>();


    @JSONField(serializeUsing = EnumsSerializer.class, deserializeUsing = EnumsSerializer.class)
    public IconColorEnum iconColor;
    @JSONField(serializeUsing = EnumsSerializer.class, deserializeUsing = EnumsSerializer.class)
    public ItemColorEnum itemColor;
    @JSONField(serializeUsing = EnumsSerializer.class, deserializeUsing = EnumsSerializer.class)
    public FontColorEnum fontColor;


    Map<String, Object> fakeCookies;
    Map<String, Object> queryHeader;
    Map<String, Object> queryOptions;
    Map<String, Object> queryArgs;//= new HashMap<String, Object>();


    public APICallerProperties() {

    }

    public APICallerProperties clone() {
        APICallerProperties apiCallerProperties = JSON.parseObject(JSON.toJSONString(this), APICallerProperties.class);
        return apiCallerProperties;
    }


    private void update(RequestMethodBean methodBean) {

        Set<RequestParamBean> paramSet = methodBean.getParamSet();
        this.methodName = methodBean.getMethodName();
        this.sourceClassName = methodBean.getClassName();
        this.name = methodBean.getName();
        this.id = methodBean.getUrl();
        queryURL = methodBean.getUrl();
        queryMethod = methodBean.getQueryMethod();
        requestType = methodBean.getRequestType();
        responseType = methodBean.getResponseType();
        metaInfo = "(";
        if (!methodBean.getRequestType().equals(RequestType.JSON)) {
            for (RequestParamBean param : paramSet) {
                Class clazz = param.getParamClass();
                metaInfo = metaInfo + clazz.getSimpleName() + " " + param.getParamName() + ",";
            }
            if (metaInfo.endsWith(",")) {
                metaInfo = metaInfo.substring(0, metaInfo.length() - 1);
            }
        }
        metaInfo = metaInfo + ")";
        if (methodBean.getMethodChinaName() != null) {
            this.desc = methodBean.getMethodChinaName().cname();
            metaInfo = metaInfo + "[" + methodBean.getMethodChinaName().cname() + "]";
        } else {
            this.desc = name;
        }


        try {
            CustomAnnotation customAnnotation = AnnotationUtil.getMethodAnnotation(methodBean.getSourceMethod(), CustomAnnotation.class);
            FieldAnnotation fieldAnnotation = AnnotationUtil.getMethodAnnotation(methodBean.getSourceMethod(), FieldAnnotation.class);
            Tips tipsAnn = AnnotationUtil.getMethodAnnotation(methodBean.getSourceMethod(), Tips.class);
            ModuleAnnotation annotation = AnnotationUtil.getMethodAnnotation(methodBean.getSourceMethod(), ModuleAnnotation.class);
            if (annotation != null) {
                if (!annotation.caption().equals("")) {
                    this.desc = annotation.caption();
                    this.tips=desc;
                }

                if (!annotation.imageClass().equals("")) {
                    this.imageClass = annotation.imageClass();
                }
            }
            if (fieldAnnotation != null) {
                if (!fieldAnnotation.expression().equals("")) {
                    this.expression = fieldAnnotation.expression();
                }

            }

            if (customAnnotation != null) {
                if (!customAnnotation.caption().equals("")) {
                    this.desc = customAnnotation.caption();
                    this.tips=desc;
                }

                if (!customAnnotation.imageClass().equals("")) {
                    this.imageClass = customAnnotation.imageClass();
                }

                if (!customAnnotation.iconColor().equals(IconColorEnum.NONE)) {
                    this.iconColor = customAnnotation.iconColor();
                }
                if (!customAnnotation.itemColor().equals(ItemColorEnum.NONE)) {
                    this.itemColor = customAnnotation.itemColor();
                }
                if (!customAnnotation.fontColor().equals(FontColorEnum.NONE)) {
                    this.fontColor = customAnnotation.fontColor();
                }
            }
            if (tipsAnn != null) {
                this.tips = tipsAnn.tips();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        if (requestType.equals(RequestType.JSON)) {
            for (RequestParamBean param : paramSet) {
                if (!param.getJsonData() && !ModuleComponent.class.isAssignableFrom(param.getParamClass())) {
                    UrlPath urlPath = new UrlPathData(param.getParamName(), RequestPathTypeEnum.FORM, "");
                    String key = urlPath.getType().getType() + "[" + urlPath.getName() + "]";
                    requestDataMap.put(key, urlPath);
                    requestDataSource.add(urlPath);
                }
            }
        }

        APIEventAnnotation apiEventAnnotation = null;
        try {
            apiEventAnnotation = AnnotationUtil.getMethodAnnotation(methodBean.getSourceMethod(), APIEventAnnotation.class);

            if (apiEventAnnotation != null) {
                for (CustomMenuItem item : apiEventAnnotation.bindMenu()) {
                    this.getBindMenu().add(item);
                }

                for (RequestPathAnnotation requestData : apiEventAnnotation.customRequestData()) {
                    addRequestData(new UrlPathData(requestData));
                }

                for (ResponsePathAnnotation responseData : apiEventAnnotation.customResponseData()) {
                    addResponseData(new UrlPathData(responseData));
                }
                for (CallBackPathAnnotation responseCallback : apiEventAnnotation.customResponseCallback()) {
                    addResponseCallBack(new UrlPathData(responseCallback));
                }

                for (ResponsePathAnnotation responseData : apiEventAnnotation.responseDataTarget()) {
                    addResponseData(new UrlPathData(responseData));
                }
                for (RequestPathAnnotation requestData : apiEventAnnotation.requestDataSource()) {
                    addRequestData(new UrlPathData(requestData));
                }
                for (CallBackPathAnnotation responseCallback : apiEventAnnotation.responseCallback()) {
                    addResponseCallBack(new UrlPathData(responseCallback));
                }
                setAutoRun(apiEventAnnotation.autoRun());
                setQueryAsync(apiEventAnnotation.queryAsync());
                setIsAllform(apiEventAnnotation.isAllform());


                APIExtparams apiExtparams = DSMAnnotationUtil.getMethodCustomAnnotation(methodBean.getSourceMethod(), APIExtparams.class);
                if (apiExtparams != null) {

                    for (UrlPath urlPath : apiExtparams.getResponseDataTarget()) {
                        addResponseData(urlPath);
                    }

                    for (UrlPath urlPath : apiExtparams.getRequestDataSource()) {
                        addRequestData(urlPath);
                    }

                    for (UrlPath urlPath : apiExtparams.getResponseCallback()) {
                        addResponseCallBack(urlPath);
                    }

                    for (ResponsePathAnnotation responseData : apiEventAnnotation.customResponseData()) {
                        addResponseData(new UrlPathData(responseData));
                    }

                    for (RequestPathAnnotation requestData : apiEventAnnotation.customRequestData()) {
                        addRequestData(new UrlPathData(requestData));
                    }
                    for (CallBackPathAnnotation responseCallback : apiEventAnnotation.customResponseCallback()) {
                        addResponseCallBack(new UrlPathData(responseCallback));
                    }

                    setIsAllform(apiExtparams.isAllform());
                    setAutoRun(apiExtparams.isAutoRun());
                    setQueryAsync(apiExtparams.getQueryAsync());

                }
            }

            this.caption = this.desc;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public APICallerProperties(RequestMethodBean methodBean) {
        this.update(methodBean);
    }

    public void update(MethodConfig methodBean) {
        Set<RequestParamBean> paramSet = methodBean.getParamSet();
        CustomFieldBean fieldBean = methodBean.getFieldBean();
        if (fieldBean != null) {
            this.iconColor = fieldBean.getIconColor();
            this.itemColor = fieldBean.getItemColor();
            this.fontColor = fieldBean.getFontColor();
        }
        this.methodName = methodBean.getMethodName();
        this.sourceClassName = methodBean.getSourceClassName();
        this.name = methodBean.getName();
        this.id = methodBean.getUrl();
        this.queryURL = methodBean.getUrl();
        this.expression = methodBean.getExpression();
        this.imageClass = methodBean.getImageClass();

        if (methodBean.getCaption() != null && !methodBean.getCaption().equals("")) {
            this.desc = methodBean.getCaption();


        }

        this.tips = methodBean.getTips();
        this.queryURL = methodBean.getUrl();

        RequestMethodBean requestMethodBean = methodBean.getRequestMethodBean();
        if (requestMethodBean == null) {
            requestMethodBean = new RequestMethodBean(methodBean.getMethod(), methodBean.getRequestMapping(), methodBean.getDomainId());
        } else {
            queryMethod = requestMethodBean.getQueryMethod();
            requestType = requestMethodBean.getRequestType();
            responseType = requestMethodBean.getResponseType();
        }

        metaInfo = "(";
        if (!requestType.equals(RequestType.JSON)) {
            for (RequestParamBean param : paramSet) {
                if (param != null && param.getParamClass() != null) {
                    Class clazz = param.getParamClass();
                    metaInfo = metaInfo + clazz.getSimpleName() + " " + param.getParamName() + ",";
                } else {
                    metaInfo = metaInfo + String.class.getSimpleName() + " " + param.getParamName() + ",";
                }
            }

            if (metaInfo.endsWith(",")) {
                metaInfo = metaInfo.substring(0, metaInfo.length() - 1);
            }
        }
        metaInfo = metaInfo + ")";

        if (requestType.equals(RequestType.JSON)) {
            for (RequestParamBean param : paramSet) {
                if (param != null && param.getParamClass() != null) {
                    if (!param.getJsonData() && !ModuleComponent.class.isAssignableFrom(param.getParamClass())) {
                        UrlPath urlPath = new UrlPathData(param.getParamName(), RequestPathTypeEnum.FORM, "");
                        String key = urlPath.getType().getType() + "[" + urlPath.getName() + "]";
                        requestDataMap.put(key, urlPath);
                        requestDataSource.add(urlPath);
                    }
                }
            }
        }
    }


    public APICallerProperties(MethodConfig methodBean) {
        update(methodBean);
    }


    public void addQuaryArgs(String name, Object value) {
        if (queryArgs == null) {
            queryArgs = new HashMap<>();
        }
        queryArgs.put(name, value);
    }


    public void addResponseData(UrlPath urlPath) {
        if (urlPath.getType() != null) {

            Map<String, UrlPath> responseDataMap = new HashMap();
            for (UrlPath reqPath : responseDataTarget) {
                if (reqPath != null) {
                    String key = reqPath.getType().getType() + "[" + reqPath.getName() + "]";
                    responseDataMap.put(key, reqPath);
                }
            }
            String key = urlPath.getType().getType() + "[" + urlPath.getName() + "]";
            if (!responseDataMap.containsKey(key)) {
                responseDataTarget.add(new UrlPathData(urlPath));
            }

        }
    }

    public void addResponseCallBack(UrlPath urlPath) {
        if (urlPath.getType() != null) {
            Map<String, UrlPath> responseCallBackMap = this.getResponseCallBackMap();
            String key = urlPath.getType().getType() + "[" + urlPath.getName() + "]";
            if (!responseCallBackMap.containsKey(key)) {
                responseCallback.add(new UrlPathData(urlPath));
            }
        }
    }


    public void addRequestData(UrlPath urlPath) {
        Map<String, UrlPath> reqMap = this.getRequestDataMap();
        String key = urlPath.getType().getType() + "[" + urlPath.getName() + "]";
        if (!reqMap.containsKey(key)) {
            UrlPathData urlPathData = new UrlPathData(urlPath);
            requestDataSource.add(urlPathData);
            requestDataMap.put(key, urlPathData);
        }
    }

    void hasParams() {

    }


    public Boolean getIsAllform() {
        return isAllform;
    }

    public void setIsAllform(Boolean isAllform) {
        this.isAllform = isAllform;
    }


    public String getMetaInfo() {
        return metaInfo;
    }

    public void setMetaInfo(String metaInfo) {
        this.metaInfo = metaInfo;
    }

    public Boolean getQueryAsync() {
        return queryAsync;
    }

    public void setQueryAsync(Boolean queryAsync) {
        this.queryAsync = queryAsync;
    }

    public String getTips() {
        if (tips == null || tips.equals("")) {
            tips = this.getCaption();
        }
        if (tips == null || tips.equals("")) {
            tips = this.getDesc();
        }

        if (tips == null || tips.equals("")) {
            tips = this.getMethodName();
        }
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public Boolean getAutoRun() {
        return autoRun;
    }

    public void setAutoRun(Boolean autoRun) {
        this.autoRun = autoRun;
    }

    public Map<String, Object> getFakeCookies() {
        return fakeCookies;
    }

    public void setFakeCookies(Map<String, Object> fakeCookies) {
        this.fakeCookies = fakeCookies;
    }

    public Map<String, Object> getQueryHeader() {
        return queryHeader;
    }

    public void setQueryHeader(Map<String, Object> queryHeader) {
        this.queryHeader = queryHeader;
    }

    public Map<String, Object> getQueryOptions() {
        return queryOptions;
    }

    public void setQueryOptions(Map<String, Object> queryOptions) {
        this.queryOptions = queryOptions;
    }

    public List<UrlPath> getResponseCallback() {
        return responseCallback;
    }

    public void setResponseCallback(List<UrlPath> responseCallback) {
        this.responseCallback = responseCallback;
    }

    public String getQueryURL() {
        return queryURL;
    }

    public void setQueryURL(String queryURL) {
        this.queryURL = queryURL;
    }

    public HttpMethod getQueryMethod() {
        return queryMethod;
    }

    public void setQueryMethod(HttpMethod queryMethod) {
        this.queryMethod = queryMethod;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(ResponseType responseType) {
        this.responseType = responseType;
    }

    public ProxyType getProxyType() {
        return proxyType;
    }

    public void setProxyType(ProxyType proxyType) {
        this.proxyType = proxyType;
    }

    public Map<String, Object> getQueryArgs() {
        return queryArgs;
    }

    public void setQueryArgs(Map<String, Object> queryArgs) {
        this.queryArgs = queryArgs;
    }

    public List<UrlPath> getRequestDataSource() {
        return requestDataSource;
    }

    public void setRequestDataSource(List<UrlPath> requestDataSource) {
        this.requestDataSource = requestDataSource;
    }

    public Map<String, UrlPath> getRequestDataMap() {
        if (requestDataMap == null || requestDataMap.isEmpty()) {
            for (UrlPath reqPath : requestDataSource) {
                if (reqPath != null) {
                    String key = reqPath.getType().getType() + "[" + reqPath.getName() + "]";
                    requestDataMap.put(key, reqPath);
                }
            }
        }
        return requestDataMap;
    }

    public Class<? extends Enum> getEnumClass() {
        return enumClass;
    }

    public void setEnumClass(Class<? extends Enum> enumClass) {
        this.enumClass = enumClass;
    }

    public void setRequestDataMap(Map<String, UrlPath> requestDataMap) {
        this.requestDataMap = requestDataMap;
    }

    public List<UrlPath> getResponseDataTarget() {
        return responseDataTarget;
    }

    public void setResponseDataTarget(List<UrlPath> responseDataTarget) {
        this.responseDataTarget = responseDataTarget;
    }

    public Map<String, UrlPath> getResponseCallBackMap() {
        if (responseCallBackMap == null || responseCallBackMap.isEmpty()) {
            for (UrlPath reqPath : responseCallback) {
                if (reqPath != null) {
                    String key = reqPath.getType().getType() + "[" + reqPath.getName() + "]";
                    responseCallBackMap.put(key, reqPath);
                }
            }
        }
        return responseCallBackMap;
    }

    public void setResponseCallBackMap(Map<String, UrlPath> responseCallBackMap) {
        this.responseCallBackMap = responseCallBackMap;
    }

    public Set<CustomMenuItem> getBindMenu() {
        return bindMenu;
    }

    public void setBindMenu(Set<CustomMenuItem> bindMenu) {
        this.bindMenu = bindMenu;
    }

    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public Boolean getAllform() {
        return isAllform;
    }

    public void setAllform(Boolean allform) {
        isAllform = allform;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
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

    public String getCurrClassName() {
        return currClassName;
    }

    public void setCurrClassName(String currClassName) {
        this.currClassName = currClassName;
    }

    public ComboInputType getMenuType() {
        return menuType;
    }

    public void setMenuType(ComboInputType menuType) {
        this.menuType = menuType;
    }

    public IconColorEnum getIconColor() {
        return iconColor;
    }

    public void setIconColor(IconColorEnum iconColor) {
        this.iconColor = iconColor;
    }

    public ItemColorEnum getItemColor() {
        return itemColor;
    }

    public void setItemColor(ItemColorEnum itemColor) {
        this.itemColor = itemColor;
    }

    public FontColorEnum getFontColor() {
        return fontColor;
    }

    public void setFontColor(FontColorEnum fontColor) {
        this.fontColor = fontColor;
    }
}
