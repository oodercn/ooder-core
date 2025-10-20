package net.ooder.esd.util.page;

import net.ooder.annotation.IconEnumstype;
import net.ooder.common.JDSException;
import net.ooder.common.util.StringUtility;
import net.ooder.config.ErrorListResultModel;
import net.ooder.config.ListResultModel;
import net.ooder.config.TreeListResultModel;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.event.CustomTabsEvent;
import net.ooder.esd.annotation.field.TabItem;
import net.ooder.esd.bean.CustomViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.nav.TabItemBean;
import net.ooder.esd.bean.view.NavComboBaseViewBean;
import net.ooder.esd.bean.view.TabsViewBean;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.tool.properties.item.TabListItem;
import net.ooder.esd.util.ESDEnumsUtil;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.ConstructorBean;
import net.ooder.web.RequestParamBean;
import ognl.OgnlContext;
import ognl.OgnlException;

import java.lang.reflect.Constructor;
import java.util.*;

public class TabPageUtil {

    public static <T extends TabListItem, K> TreeListResultModel<List<T>> getTabList(Set<K> objs, Class<T> clazz) {
        List<K> result = new ArrayList<K>();
        result.addAll(objs);
        return getTabList(result, clazz);
    }

    public static <T extends TabListItem, K> TreeListResultModel<List<T>> getTabList(List<K> objs, Class<T> clazz) {
        TreeListResultModel<List<T>> userStatusInfo = new TreeListResultModel<List<T>>();
        List<T> pageResult = new ArrayList<T>();
        for (K obj : objs)
            if (obj != null) {
                if (obj instanceof TabListItem) {
                    pageResult.add(fillObj((T) obj));
                } else if (clazz == null || obj.getClass().isAssignableFrom(clazz)) {
                    pageResult.add((T) obj);
                } else {
                    T t = (T) fillObj(obj, clazz);
                    pageResult.add(t);

                }
            }
        List<String> ids = fillIds((List<TabListItem>) pageResult);
        userStatusInfo.setIds(ids);
        userStatusInfo.setData(pageResult);
        return userStatusInfo;
    }


    public static <T extends TabListItem> T fillObj(T tabListItem) {

        String bindClassName = tabListItem.getBindClassName();
        MethodConfig editorMethod = getEditorMethod(bindClassName);
        if (tabListItem.getBindClass() != null && tabListItem.getBindClass().length > 0) {
            for (Class clazz : tabListItem.getBindClass()) {
                if (clazz != null) {
                    editorMethod = getEditorMethod(clazz.getName());
                }
            }
        }


        if (editorMethod != null) {
            String euClassName = tabListItem.getEuClassName();
            if (tabListItem.getTabItem() != null && tabListItem.getTabItem().getClass().isEnum() && euClassName.indexOf(CustomViewFactory.INMODULE__) == -1) {
                euClassName = euClassName + CustomViewFactory.INMODULE__ + tabListItem.getTabItem();
            }
            tabListItem.setEuClassName(euClassName);
        } else if (tabListItem.getEuClassName() != null) {
            String euClassName = tabListItem.getEuClassName();
            try {
                editorMethod = ESDFacrory.getAdminESDClient().getMethodAPIBean(euClassName, DSMFactory.getInstance().getDefaultProjectName());
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }

        LinkedHashSet<RequestParamBean> paramBeans = editorMethod.getParamSet();
        for (RequestParamBean paramBean : paramBeans) {
            String name = paramBean.getParamName();
            Object value = JDSActionContext.getActionContext().getParams(name);
            if (value == null) {
                try {
                    value = OgnlUtil.getValue(name, JDSActionContext.getActionContext().getOgnlContext(), tabListItem);
                } catch (OgnlException e) {
                    e.printStackTrace();
                }
            }
            tabListItem.addTagVar(name, value);
        }
        return tabListItem;
    }

    public static TreeListResultModel<List<TabListItem>> fillTabList(List<TabListItem> pageResult) {

        TreeListResultModel<List<TabListItem>> userStatusInfo = new TreeListResultModel<List<TabListItem>>();
        for (TabListItem tabListItem : pageResult) {
            tabListItem = fillObj(tabListItem);
        }
        List<String> ids = fillIds(pageResult);
        userStatusInfo.setIds(ids);
        userStatusInfo.setData(pageResult);
        return userStatusInfo;
    }


    public static TreeListResultModel<List<TabListItem>> getEnumList(Class<Enum> enumClass) {
        List<TabListItem> pageResult = ESDEnumsUtil.getEnumItems(enumClass, TabListItem.class);
        TreeListResultModel<List<TabListItem>> userStatusInfo = fillTabList(pageResult);
        return userStatusInfo;
    }


    private static MethodConfig getEditorMethod(String bindClassName) {
        String domainId = null;
        MethodConfig methodConfig = (MethodConfig) JDSActionContext.getActionContext().getContext().get(CustomViewFactory.MethodBeanKey);
        if (methodConfig != null) {
            domainId = methodConfig.getDomainId();
        }

        MethodConfig editorMethod = null;
        try {
            if (bindClassName != null && !bindClassName.equals("") && !bindClassName.equals(Void.class.getName()) && !bindClassName.equals(Enum.class.getName())) {
                ApiClassConfig config = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(bindClassName);
                if (config != null) {
                    editorMethod = config.getMethodByEvent(CustomTabsEvent.TABEDITOR);
                } else {
                    AggEntityConfig entityConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(bindClassName, false);
                    editorMethod = entityConfig.getMethodByEvent(CustomTabsEvent.TABEDITOR);
                }
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }

        return editorMethod;
    }

    private static List<String> fillIds(List<TabListItem> pageResult) {

        List<String> ids = new ArrayList<>();
        if (pageResult.size() > 0) {
            Object id = JDSActionContext.getActionContext().getParams("id");
            List<String> allIds = new ArrayList<>();
            for (TabListItem item : pageResult) {
                allIds.add(item.getId());
            }
            if (id != null && !id.equals("")) {
                String[] idArr = StringUtility.split(id.toString(), ";");
                for (String sid : idArr) {
                    if (allIds.contains(sid)) {
                        ids.add(sid);
                    }
                }
            }
            if (ids.size() == 0) {
                ids.add(pageResult.get(0).getId());
            }

        }

        return ids;
    }

    static <T extends TabListItem, K> TabListItem fillObj(K obj, Class<T> clazz) {
        MethodConfig methodConfig = (MethodConfig) JDSActionContext.getActionContext().getContext().get(CustomViewFactory.MethodBeanKey);
        CustomViewBean viewBean = methodConfig.getView();
        TabsViewBean tabsViewBean = null;

        if (viewBean instanceof NavComboBaseViewBean) {
            NavComboBaseViewBean navTabsBaseViewBean = (NavComboBaseViewBean) viewBean;
            tabsViewBean = navTabsBaseViewBean.getTabsViewBean();
        } else {
            tabsViewBean = (TabsViewBean) viewBean;
        }


        Constructor[] constructors = clazz.getDeclaredConstructors();
        T t = null;
        TabItemBean tabItemBean = getChildTabBean(tabsViewBean, clazz, obj);
        if (tabItemBean != null) {
            try {
                ConstructorBean constructorBean = tabItemBean.getConstructorBean();
                t = (T) constructorBean.invok(obj);
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


                t.setCloseBtn(tabItemBean.getCloseBtn());
                t.setImageClass(tabItemBean.getImageClass());


                t.setTabindex(tabItemBean.getIndex());
                t.setDynLoad(tabItemBean.getLazyLoad());

                Class[] classes = tabItemBean.getBindClass();
                MethodConfig editorMethod = tabsViewBean.findMethodByEvent(CustomTabsEvent.TABEDITOR, classes);
                if (editorMethod != null) {
                    if (!editorMethod.getImageClass().equals(MethodConfig.DefaultImageClass)) {
                        t.setImageClass(editorMethod.getImageClass());
                    }
                    String euClassName = editorMethod.getEUClassName();
                    if (tabItemBean.getTabItem() != null && tabItemBean.getTabItem().getClass().isEnum() && euClassName.indexOf(CustomViewFactory.INMODULE__) == -1) {
                        euClassName = euClassName + CustomViewFactory.INMODULE__ + tabItemBean.getTabItem();
                    }

                    t.setEuClassName(euClassName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        return t;
    }


    static TabItemBean getChildTabBean(TabsViewBean viewBean, Class sourceClazz, Object obj) {
        TabItemBean buttonViewsViewBean = null;
        Class treeClass = obj.getClass();
        if (obj instanceof TabItem) {
            return viewBean.getChildTabBean((TabItem) obj);
        }

        List<TabItemBean> childTabs = viewBean.getItemBeans();
        for (TabItemBean child : childTabs) {
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
                    buttonViewsViewBean = new TabItemBean(constructor, viewBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return buttonViewsViewBean;
    }


    public static OgnlContext getOgnlContext() {
        return JDSActionContext.getActionContext().getOgnlContext();
    }


    static <T extends TabListItem, K extends IconEnumstype> List<T> fillObjs(List<K> objs, Class<T> clazz) {
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


    public static <T extends TabListItem, K extends IconEnumstype> TreeListResultModel<List<T>> changTabList(ListResultModel<List<K>> objs, Class<T> clazz) {
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


    public static <T extends TabListItem, K extends IconEnumstype> TreeListResultModel<List<T>> getDefaultTabList(Set<K> objs, Class<T> clazz) {
        List<K> result = new ArrayList<K>();
        result.addAll(objs);
        return getDefaultTabList(result, clazz);
    }

    public static <T extends TabListItem, K extends IconEnumstype> TreeListResultModel<List<T>> getDefaultTabList(ListResultModel<List<K>> objs, Class<T> clazz) {
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


    public static <T extends TabListItem, K extends IconEnumstype> TreeListResultModel<List<T>> getDefaultTabList(List<K> objs, Class<T> clazz) {
        TreeListResultModel userStatusInfo = null;
        try {
            userStatusInfo = getTabList(objs, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            userStatusInfo = new ErrorListResultModel();
            // ((ErrorResultModel) userStatusInfo).setErrcode(e.getErrorCode());
            ((ErrorListResultModel) userStatusInfo).setErrdes("内部错误");
        }

        return userStatusInfo;

    }
}
