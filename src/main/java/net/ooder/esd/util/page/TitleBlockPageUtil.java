package net.ooder.esd.util.page;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.ooder.annotation.IconEnumstype;
import net.ooder.common.util.StringUtility;
import net.ooder.config.ErrorListResultModel;
import net.ooder.config.ListResultModel;
import net.ooder.config.TreeListResultModel;
import net.ooder.context.JDSActionContext;

import net.ooder.esd.annotation.event.CustomTabsEvent;
import net.ooder.esd.annotation.field.TabItem;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.fchart.items.LineListItem;
import net.ooder.esd.bean.view.CustomTitleBlockViewBean;
import net.ooder.esd.bean.gallery.TitleBlockItemBean;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.tool.properties.item.TitleBlockItem;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.server.httpproxy.core.HttpRequest;
import net.ooder.web.ConstructorBean;
import ognl.OgnlContext;

import java.lang.reflect.Constructor;
import java.util.*;

public class TitleBlockPageUtil {

    public static <T extends TitleBlockItem, K> TreeListResultModel<List<T>> getTabList(Set<K> objs, Class<T> clazz) {
        List<K> result = new ArrayList<K>();
        result.addAll(objs);
        return getTabList(result, clazz);
    }

    public static <T extends TitleBlockItem, K> TreeListResultModel<List<T>> getTabList(List<K> objs, Class<T> clazz) {
        TreeListResultModel<List<T>> userStatusInfo = new TreeListResultModel<List<T>>();
        List<T> pageResult = new ArrayList<T>();
        for (K obj : objs)
            if (obj != null) {
                if (clazz == null || obj.getClass().isAssignableFrom(clazz)) {
                    pageResult.add((T) obj);
                } else {
                    T t = (T) fillObj(obj, clazz);
                    pageResult.add(t);

                }
            }
        Object id = JDSActionContext.getActionContext().getParams("id");

        if (id != null && !id.equals("")) {
            String[] orgIdArr = StringUtility.split(id.toString(), ";");
            userStatusInfo.setIds(Arrays.asList(orgIdArr));
        } else {
            if (pageResult.size() > 0) {
                userStatusInfo.setIds(Arrays.asList(new String[]{pageResult.get(0).getId()}));
            }
        }
        userStatusInfo.setData(pageResult);
        return userStatusInfo;
    }


    static TitleBlockItemBean getTitleBlockItem(CustomTitleBlockViewBean viewBean, Class sourceClazz, Object obj) {
        TitleBlockItemBean titleBlockItemBean = null;
        Class treeClass = obj.getClass();
        if (obj instanceof LineListItem) {
            return viewBean.getChildItemBean((TabItem) obj);
        }

        List<TitleBlockItemBean> childTabs = viewBean.getTitleBlockItemBeans();
        for (TitleBlockItemBean child : childTabs) {
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
                    titleBlockItemBean = new TitleBlockItemBean(constructor, viewBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return titleBlockItemBean;
    }

    public static OgnlContext getOgnlContext() {

        return JDSActionContext.getActionContext().getOgnlContext();
    }


    static <T extends TitleBlockItem, K extends IconEnumstype> List<T> fillObjs(List<K> objs, Class<T> clazz) {
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


    static <T extends TitleBlockItem, K> TitleBlockItem fillObj(K obj, Class<T> clazz) {
        MethodConfig methodConfig = (MethodConfig) JDSActionContext.getActionContext().getContext().get(CustomViewFactory.MethodBeanKey);
        CustomTitleBlockViewBean viewBean = (CustomTitleBlockViewBean) methodConfig.getView();
        T t = null;
        TitleBlockItemBean tabItemBean = getTitleBlockItem(viewBean, clazz, obj);
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

                if (t.getId() == null) {
                    t.setId(tabItemBean.getId());
                }

                String bindClassName = t.getBindClassName();
                if (bindClassName == null && tabItemBean.getBindService() != null) {
                    bindClassName = tabItemBean.getBindService().getName();
                }
                if (bindClassName == null && viewBean.getBindService() != null) {
                    bindClassName = viewBean.getBindService().getName();
                }
                t.setTabindex(tabItemBean.getIndex());
                t.setDynLoad(tabItemBean.getLazyLoad());
                if (bindClassName != null && !bindClassName.equals("") && !bindClassName.equals(Void.class.getName()) && !bindClassName.equals(Enum.class.getName())) {
                    ApiClassConfig config = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(bindClassName);
                    MethodConfig editorMethod = config.getMethodByEvent(CustomTabsEvent.TABEDITOR);
                    if (editorMethod != null) {
                        if (!editorMethod.getImageClass().equals(MethodConfig.DefaultImageClass)) {
                            t.setImageClass(editorMethod.getImageClass());
                        }
                        t.setEuClassName(editorMethod.getEUClassName());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            t = JSONObject.parseObject(JSONObject.toJSONString(obj), clazz);
        }

        return t;
    }


    public static <T extends TitleBlockItem, K extends IconEnumstype> TreeListResultModel<List<T>> changTabList(ListResultModel<List<K>> objs, Class<T> clazz) {
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


    public static <T extends TitleBlockItem, K extends IconEnumstype> TreeListResultModel<List<T>> getDefaultDataList(ListResultModel<List<K>> objs, Class<T> clazz) {
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


}
