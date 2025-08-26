package net.ooder.esd.util.page;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.TypeUtils;
import net.ooder.annotation.IconEnumstype;
import net.ooder.common.util.StringUtility;
import net.ooder.config.ErrorListResultModel;
import net.ooder.config.ListResultModel;
import net.ooder.config.TreeListResultModel;
import net.ooder.context.JDSActionContext;

import net.ooder.esd.annotation.event.CustomTabsEvent;
import net.ooder.esd.annotation.fchart.IRawData;
import net.ooder.esd.annotation.field.TabItem;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.CustomFChartViewBean;
import net.ooder.esd.bean.fchart.items.RawDataItemBean;
import net.ooder.esd.bean.fchart.items.RawDataListItem;
import net.ooder.esd.bean.view.CustomGridViewBean;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.dsm.DSMFactory;

import net.ooder.jds.core.esb.EsbUtil;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.server.httpproxy.core.HttpRequest;
import net.ooder.web.ConstructorBean;
import net.ooder.web.RequestParamBean;
import ognl.OgnlContext;

import java.lang.reflect.Constructor;
import java.util.*;

public class RawDataUtil {

    public static <T extends IRawData, K> TreeListResultModel<List<T>> getDataList(Set<K> objs, Class<T> clazz) {
        List<K> result = new ArrayList<K>();
        result.addAll(objs);
        return getDataList(result, clazz);
    }

    public static <T extends IRawData, K> TreeListResultModel<List<T>> getDataList(List<K> objs, Class<T> clazz) {
        TreeListResultModel<List<T>> userStatusInfo = new TreeListResultModel<List<T>>();
        List<T> pageResult = new ArrayList<T>();
        Integer objSize = objs.size();
        for (K obj : objs)
            if (obj != null) {
                if (clazz == null || IRawData.class.isAssignableFrom(obj.getClass())) {
                    pageResult.add((T) new RawDataListItem((IRawData) obj));
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
                    T t = null;
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
                                t = (T) constructorBean.invok(obj);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }
                    if (t != null) {
                        pageResult.add(t);
                    } else {
                        pageResult.add((T) obj);
                    }


                }
            }
        userStatusInfo.setSize(objSize);
        userStatusInfo.setData(pageResult);
        userStatusInfo.setCtx(fillCtx());

        return userStatusInfo;
    }

    static Map<String, Object> fillCtx() {
        CustomFChartViewBean viewBean = null;
        MethodConfig methodConfig = (MethodConfig) JDSActionContext.getActionContext().getContext().get(CustomViewFactory.TopMethodBeanKey);
        if (methodConfig != null && (methodConfig.getView() instanceof CustomGridViewBean)) {
            viewBean = (CustomFChartViewBean) methodConfig.getView();
        }
        if (viewBean == null) {
            methodConfig = (MethodConfig) JDSActionContext.getActionContext().getContext().get(CustomViewFactory.MethodBeanKey);
            viewBean = (CustomFChartViewBean) methodConfig.getView();
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


    static <T extends RawDataListItem, K> RawDataListItem fillObj(K obj, Class<T> clazz) {
        MethodConfig methodConfig = (MethodConfig) JDSActionContext.getActionContext().getContext().get(CustomViewFactory.MethodBeanKey);
        CustomFChartViewBean viewBean = (CustomFChartViewBean) methodConfig.getView();
        T t = null;
        RawDataItemBean tabItemBean = getRawDataBean(viewBean, clazz, obj);
        if (tabItemBean != null) {
            try {
                ConstructorBean constructorBean = tabItemBean.getConstructorBean();
                t = (T) constructorBean.invok(obj);

                OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(tabItemBean), Map.class), t, false, false);

                List<String> hiddenList = viewBean.getHiddenFieldNames();
                for (String hiddenName : hiddenList) {
                    t.addTagVar(hiddenName, OgnlUtil.getValue(hiddenName, getOgnlContext(), t));
                }

                if (t.getCaption() == null && viewBean.getCaptionField() != null) {
                    t.setCaption(OgnlUtil.getValue(viewBean.getCaptionField().getFieldname(), getOgnlContext(), t).toString());
                }


                if (t.getId() == null && viewBean.getUidField() != null) {
                    Object pkValue = OgnlUtil.getValue(viewBean.getUidField().getFieldname(), getOgnlContext(), t);
                    if (pkValue != null) {
                        t.setId(pkValue.toString());
                    }
                }

                if (t.getCaption() == null) {
                    t.setCaption(tabItemBean.getCaption());
                }
                if (t.getId() == null) {
                    t.setId(tabItemBean.getId());
                }

                String bindClassName = t.getBindClassName();
                if (bindClassName == null) {
                    bindClassName = tabItemBean.getBindService().getName();
                }

                if (bindClassName == null) {
                    bindClassName = viewBean.getBindService().getName();
                }
                t.setTabindex(tabItemBean.getIndex());
                t.setDynLoad(tabItemBean.getLazyLoad());


                if (tabItemBean.getBindService() != null && !tabItemBean.getBindService().equals(Void.class)&&!tabItemBean.getBindService().equals(Enum.class)) {
                    bindClassName = tabItemBean.getBindService().getName();
                }
                if (bindClassName != null && !bindClassName.equals("") && !bindClassName.equals(Void.class.getName()) && !bindClassName.equals(Enum.class.getName())) {
                    ApiClassConfig config = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(bindClassName);
                    MethodConfig editorMethod = config.getMethodByEvent(CustomTabsEvent.TABEDITOR);
                    if (editorMethod != null) {
                        t.setEuClassName(editorMethod.getEUClassName());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        return t;
    }


    static RawDataItemBean getRawDataBean(CustomFChartViewBean viewBean, Class sourceClazz, Object obj) {
        RawDataItemBean rawDataItemBean = null;
        Class treeClass = obj.getClass();
        if (obj instanceof RawDataListItem) {
            return viewBean.getRawDataItemBean((TabItem) obj);
        }

        List<RawDataItemBean> childTabs = viewBean.getRawDataBeans();
        for (RawDataItemBean child : childTabs) {
            try {
                Constructor constructor = child.getConstructorBean().getSourceConstructor();
                if (constructor != null) {
                    Class[] paramClass = constructor.getParameterTypes();
                    if (paramClass.length > 0 && paramClass[0].isAssignableFrom(treeClass) && constructor.getDeclaringClass().equals(sourceClazz)) {
                        return child;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Constructor[] constructors = sourceClazz.getDeclaredConstructors();
        for (Constructor constructor : constructors) {
            Class[] paramClass = constructor.getParameterTypes();
            if (paramClass.length > 0 && paramClass[0].isAssignableFrom(treeClass)) {
                try {
                    rawDataItemBean = new RawDataItemBean(constructor, viewBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return rawDataItemBean;
    }


    public static OgnlContext getOgnlContext() {
        return JDSActionContext.getActionContext().getOgnlContext();
    }


    static <T extends RawDataListItem, K extends IconEnumstype> List<T> fillObjs(List<K> objs, Class<T> clazz) {
        List<T> result = new ArrayList<T>();
        if (objs != null) {
            for (K obj : objs) {
                if (obj != null) {
                    if (clazz == null || obj.getClass().isAssignableFrom(clazz)) {
                        result.add((T) obj);
                    } else {

                        T t = (T) fillObj(obj, clazz);
                        result.add(t);

                    }
                }
            }
        }

        return result;
    }


    public static <T extends RawDataListItem, K extends IconEnumstype> TreeListResultModel<List<T>> changDataList(ListResultModel<List<K>> objs, Class<T> clazz) {
        TreeListResultModel<List<T>> userStatusInfo = new TreeListResultModel<List<T>>();
        try {
            List<T> pageResult = new ArrayList<T>();
            List<K> list = objs.get();
            userStatusInfo.setData(fillObjs(list, clazz));
            userStatusInfo.setSize(objs.getSize());
            Object id = JDSActionContext.getActionContext().getParams("id");
            if (id != null && !id.equals("")) {
                String[] orgIdArr = StringUtility.split(id.toString(), ";");
                userStatusInfo.setIds(Arrays.asList(orgIdArr));
            } else {
                if (pageResult.size() > 0) {
                    userStatusInfo.setIds(Arrays.asList(new String[]{pageResult.get(0).getId()}));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            userStatusInfo = new ErrorListResultModel();
            ((ErrorListResultModel) userStatusInfo).setErrdes("内部错误");
        }

        return userStatusInfo;
    }


    public static <T extends RawDataListItem, K extends IconEnumstype> TreeListResultModel<List<T>> getDefaultDataList(Set<K> objs, Class<T> clazz) {
        List<K> result = new ArrayList<K>();
        result.addAll(objs);
        return getDefaultDataList(result, clazz);
    }

    public static <T extends RawDataListItem, K extends IconEnumstype> TreeListResultModel<List<T>> getDefaultDataList(ListResultModel<List<K>> objs, Class<T> clazz) {
        TreeListResultModel<List<T>> userStatusInfo = new TreeListResultModel<List<T>>();
        try {
            List<T> pageResult = new ArrayList<T>();
            List<K> list = objs.get();
            userStatusInfo.setData(fillObjs(list, clazz));
            userStatusInfo.setSize(objs.getSize());
            Object id = JDSActionContext.getActionContext().getParams("id");
            if (id != null && !id.equals("")) {
                String[] orgIdArr = StringUtility.split(id.toString(), ";");
                userStatusInfo.setIds(Arrays.asList(orgIdArr));
            } else {
                if (pageResult.size() > 0) {
                    userStatusInfo.setIds(Arrays.asList(new String[]{pageResult.get(0).getId()}));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            userStatusInfo = new ErrorListResultModel();
            ((ErrorListResultModel) userStatusInfo).setErrdes("内部错误");
        }

        return userStatusInfo;
    }


    public static <T extends RawDataListItem, K extends IconEnumstype> TreeListResultModel<List<T>> getDefaultDataList(List<K> objs, Class<T> clazz) {
        TreeListResultModel userStatusInfo = null;
        try {
            userStatusInfo = getDataList(objs, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            userStatusInfo = new ErrorListResultModel();
            // ((ErrorResultModel) userStatusInfo).setErrcode(e.getErrorCode());
            ((ErrorListResultModel) userStatusInfo).setErrdes("内部错误");
        }

        return userStatusInfo;

    }
}
