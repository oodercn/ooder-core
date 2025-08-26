package net.ooder.esd.util.page;

import com.alibaba.fastjson.util.TypeUtils;
import net.ooder.config.ErrorListResultModel;
import net.ooder.config.ListResultModel;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.bean.view.CustomGridViewBean;

import net.ooder.jds.core.esb.EsbUtil;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.BaseParamsEnums;
import net.ooder.web.ConstructorBean;
import net.ooder.web.RequestParamBean;

import java.lang.reflect.Constructor;
import java.util.*;

public class GridPageUtil {

    public static <T, K> ListResultModel<List<T>> getPageList(Set<K> objs, Integer pageIndex, Integer pageSize, Class<T> clazz) {
        List<K> result = new ArrayList<K>();
        result.addAll(objs);
        return getPageList(result, pageIndex, pageSize, clazz);
    }

    public static <T, K> ListResultModel<List<T>> getPageList(List<K> objs, Integer pageIndex, Integer pageSize, Class<T> clazz) {
        ListResultModel<List<T>> userStatusInfo = new ListResultModel<List<T>>();
        List<T> pageResult = new ArrayList<T>();
        Integer objSize = objs.size();
        Object size = JDSActionContext.getActionContext().getParams(BaseParamsEnums.JDS_SIZE.name());
        if (size != null) {
            objSize = Integer.valueOf(size.toString());
            pageIndex = 1;
        }
        if (pageIndex < 1) {
            pageIndex = 1;
        }
        int start = (pageIndex - 1) * pageSize;
        int end = pageSize * pageIndex;

        if (end > objs.size()) {
            end = objs.size();
        }
        for (int k = start; k < end; k++) {
            K obj = objs.get(k);
            if (obj != null) {


                if (clazz == null || obj.getClass().isAssignableFrom(clazz)) {
                    pageResult.add((T) obj);
                } else if (clazz.isInterface()) {
                    Object object = EsbUtil.parExpression(clazz);
                    if (object != null) {
                        OgnlUtil.copy(obj, object, JDSActionContext.getActionContext().getContext());
                        pageResult.add((T) object);
                    } else {
                        pageResult.add((T) obj);
                    }
                } else {
                    Constructor[] constructors = clazz.getDeclaredConstructors();
                    for (Constructor constructor : constructors) {
                        List<Object> objectSet = new ArrayList<>();
                        Class[] paramClass = constructor.getParameterTypes();
                        if (paramClass.length > 0 && paramClass[0].isAssignableFrom(obj.getClass())) {
                            ConstructorBean constructorBean = new ConstructorBean(constructor);
                            List<RequestParamBean> paramsList = constructorBean.getParamList();
                            RequestParamBean[] requestParamBeans = paramsList.toArray(new RequestParamBean[]{});
                            if (requestParamBeans.length > 0) {
                                RequestParamBean fristParam = requestParamBeans[0];
                                if (obj.getClass().equals(fristParam.getParamClass()) || fristParam.getParamClass().isAssignableFrom(obj.getClass())) {
                                    objectSet.add(obj);
                                } else {
                                    Object value = TypeUtils.cast(JDSActionContext.getActionContext().getParams(fristParam.getParamName()), fristParam.getParamClass(), null);
                                    objectSet.add(value);
                                }
                                for (RequestParamBean paramBean : requestParamBeans) {
                                    if (!fristParam.equals(paramBean)) {
                                        Object value = TypeUtils.cast(JDSActionContext.getActionContext().getParams(paramBean.getParamName()), paramBean.getParamClass(), null);
                                        objectSet.add(value);
                                    }
                                }
                            }
                            try {
                                T t = (T) constructorBean.invok(obj);
                                pageResult.add(t);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }

                }
            }
        }


        userStatusInfo.setSize(objSize);
        userStatusInfo.setData(pageResult);
        userStatusInfo.setCtx(fillCtx());

        return userStatusInfo;
    }

    static Map<String, Object> fillCtx() {
        CustomGridViewBean viewBean = null;
        MethodConfig methodConfig = (MethodConfig) JDSActionContext.getActionContext().getContext().get(CustomViewFactory.TopMethodBeanKey);
        if (methodConfig != null && (methodConfig.getView() instanceof CustomGridViewBean)) {
            viewBean = (CustomGridViewBean) methodConfig.getView();
        }
        if (viewBean == null) {
            methodConfig = (MethodConfig) JDSActionContext.getActionContext().getContext().get(CustomViewFactory.MethodBeanKey);
            viewBean = (CustomGridViewBean) methodConfig.getView();
        }
        Map<String, Object> ctx = new HashMap();
        List<String> ctxNames = viewBean.getHiddenFieldNames();
        for (String name : ctxNames) {
            Object object = JDSActionContext.getActionContext().getParams(name);
            if (object != null) {
                ctx.put(name, object);
            }
        }
        return ctx;
    }

    static <T> List<T> fillObj(List objs, Class<T> clazz) {
        List<T> result = new ArrayList<T>();
        if (objs != null) {
            for (Object obj : objs) {
                if (obj != null) {
                    if (clazz == null || obj.getClass().isAssignableFrom(clazz)) {
                        result.add((T) obj);
                    } else {
                        Constructor[] constructors = clazz.getDeclaredConstructors();
                        for (Constructor constructor : constructors) {
                            List<Object> objectSet = new ArrayList<>();
                            Class[] paramClass = constructor.getParameterTypes();
                            if (paramClass.length > 0 && paramClass[0].isAssignableFrom(obj.getClass())) {
                                ConstructorBean constructorBean = new ConstructorBean(constructor);
                                List<RequestParamBean> paramsList = constructorBean.getParamList();
                                RequestParamBean[] requestParamBeans = paramsList.toArray(new RequestParamBean[]{});
                                if (requestParamBeans.length > 0) {
                                    RequestParamBean fristParam = requestParamBeans[0];
                                    if (obj.getClass().equals(fristParam.getParamClass()) || fristParam.getParamClass().isAssignableFrom(obj.getClass())) {
                                        objectSet.add(obj);
                                    } else {
                                        Object value = TypeUtils.cast(JDSActionContext.getActionContext().getParams(fristParam.getParamName()), fristParam.getParamClass(), null);
                                        objectSet.add(value);
                                    }
                                    for (RequestParamBean paramBean : requestParamBeans) {
                                        if (!fristParam.equals(paramBean)) {
                                            Object value = TypeUtils.cast(JDSActionContext.getActionContext().getParams(paramBean.getParamName()), paramBean.getParamClass(), null);
                                            objectSet.add(value);
                                        }
                                    }
                                }
                                try {
                                    T t = (T) constructorBean.invok(obj);
                                    result.add(t);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }
                }
            }
        }

        return result;
    }

    public static String appUrlParams(String url, String[] paramNames) {
        //添加默认分页参数
        Object npageIndex = JDSActionContext.getActionContext().getParams(BaseParamsEnums.pageIndex.name());
        Object npageSize = JDSActionContext.getActionContext().getParams(BaseParamsEnums.pageSize.name());
        if (npageIndex != null && !Arrays.asList(paramNames).contains(BaseParamsEnums.pageIndex.name()) && url.indexOf(BaseParamsEnums.pageIndex.name()) == -1) {
            url = url + "&" + BaseParamsEnums.pageIndex.name() + "=" + npageIndex;
        }
        if (npageSize != null && !Arrays.asList(paramNames).contains(BaseParamsEnums.pageSize.name()) && url.indexOf(BaseParamsEnums.pageSize.name()) == -1) {
            url = url + "&" + BaseParamsEnums.pageSize.name() + "=" + npageSize;
        }
        return url;

    }


    public static <T, K> ListResultModel<List<T>> changPageList(ListResultModel<List<K>> objs, Class<T> clazz) {
        ListResultModel<List<T>> userStatusInfo = new ListResultModel<List<T>>();
        try {
            List<T> pageResult = new ArrayList<T>();
            List<K> list = objs.get();
            userStatusInfo.setData(fillObj(list, clazz));
            userStatusInfo.setSize(objs.getSize());
            userStatusInfo.setCtx(fillCtx());
        } catch (Exception e) {
            e.printStackTrace();
            userStatusInfo = new ErrorListResultModel();
            // ((ErrorResultModel) userStatusInfo).setErrcode(e.getErrorCode());
            ((ErrorListResultModel) userStatusInfo).setErrdes("内部错误");
        }

        return userStatusInfo;
    }

    public static <T> ListResultModel<List<T>> getDefaultPageList(List<T> objs) {
        return getDefaultPageList(objs, null);
    }

    public static <T> ListResultModel<List<T>> getDefaultPageList(Set<T> objs) {
        List<T> result = new ArrayList<T>();
        result.addAll(objs);
        return getDefaultPageList(result, null);
    }

    public static <T, K> ListResultModel<List<T>> getDefaultPageList(Set<K> objs, Class<T> clazz) {
        List<K> result = new ArrayList<K>();
        result.addAll(objs);
        return getDefaultPageList(result, clazz);
    }

    public static <T, K> ListResultModel<List<T>> getDefaultPageList(ListResultModel<List<K>> objs, Class<T> clazz) {
        ListResultModel<List<T>> userStatusInfo = new ListResultModel<List<T>>();
        try {
            List<T> pageResult = new ArrayList<T>();
            List<K> list = objs.get();

            userStatusInfo.setData(fillObj(list, clazz));
            userStatusInfo.setSize(objs.getSize());
            userStatusInfo.setCtx(fillCtx());
        } catch (Exception e) {
            e.printStackTrace();
            userStatusInfo = new ErrorListResultModel();
            // ((ErrorResultModel) userStatusInfo).setErrcode(e.getErrorCode());
            ((ErrorListResultModel) userStatusInfo).setErrdes("内部错误");
        }

        return userStatusInfo;
    }


    public static <T, K> ListResultModel<List<T>> getDefaultPageList(List<K> objs, Class<T> clazz) {
        ListResultModel userStatusInfo = null;
        try {
            Object pageIndex = JDSActionContext.getActionContext().getParams(BaseParamsEnums.pageIndex.name());
            Object pageSize = JDSActionContext.getActionContext().getParams(BaseParamsEnums.pageSize.name());
            if (pageIndex == null) {
                pageIndex = 1;
            } else {
                pageIndex = Integer.valueOf(pageIndex.toString());
            }
            if (pageSize == null) {
                pageSize = 100;
            } else {
                pageSize = Integer.valueOf(pageSize.toString());
            }
            userStatusInfo = getPageList(objs, ((Integer) pageIndex), ((Integer) pageSize), clazz);
        } catch (Exception e) {
            e.printStackTrace();
            userStatusInfo = new ErrorListResultModel();
            // ((ErrorResultModel) userStatusInfo).setErrcode(e.getErrorCode());
            ((ErrorListResultModel) userStatusInfo).setErrdes("内部错误");
        }

        return userStatusInfo;

    }
}
