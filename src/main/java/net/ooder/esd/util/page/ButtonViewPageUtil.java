package net.ooder.esd.util.page;

import net.ooder.annotation.IconEnumstype;
import net.ooder.common.util.StringUtility;
import net.ooder.config.ErrorListResultModel;
import net.ooder.config.ListResultModel;
import net.ooder.config.TreeListResultModel;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.event.CustomTabsEvent;
import net.ooder.esd.annotation.field.TabItem;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.nav.TabItemBean;
import net.ooder.esd.bean.view.CustomButtonViewsViewBean;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.tool.properties.item.TabListItem;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.ConstructorBean;
import ognl.OgnlContext;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ButtonViewPageUtil {

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


    static <T extends TabListItem, K> TabListItem fillObj(K obj, Class<T> clazz) {
        MethodConfig methodConfig = (MethodConfig) JDSActionContext.getActionContext().getContext().get(CustomViewFactory.MethodBeanKey);
        CustomButtonViewsViewBean tabsViewBean = (CustomButtonViewsViewBean) methodConfig.getView();
        T t = null;
        TabItemBean tabItemBean = getChildTabBean(tabsViewBean, clazz, obj);
        if (tabItemBean != null) {
            try {
                ConstructorBean constructorBean = tabItemBean.getConstructorBean();
                t = (T) constructorBean.invok(obj);
                List<String> hiddenList = tabsViewBean.getHiddenFieldNames();
                for (String hiddenName : hiddenList) {
                    t.addTagVar(hiddenName, OgnlUtil.getValue(hiddenName, getOgnlContext(), t));
                }

                if (t.getCaption() == null && tabsViewBean.getCaptionField() != null) {
                    t.setCaption(OgnlUtil.getValue(tabsViewBean.getCaptionField().getFieldname(), getOgnlContext(), t).toString());
                }

                if (t.getId() == null && tabsViewBean.getUidField() != null) {
                    Object pkValue = OgnlUtil.getValue(tabsViewBean.getUidField().getFieldname(), getOgnlContext(), t);
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
                    if (tabItemBean.getImageClass() == null && !editorMethod.getImageClass().equals(MethodConfig.DefaultImageClass)) {
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

    static TabItemBean getChildTabBean(CustomButtonViewsViewBean viewBean, Class sourceClazz, Object obj) {
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


    public static <T extends TabListItem, K extends
            IconEnumstype> TreeListResultModel<List<T>> changTabList(ListResultModel<List<K>> objs, Class<T> clazz) {
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


    public static <T extends TabListItem, K extends
            IconEnumstype> TreeListResultModel<List<T>> getDefaultTabList(Set<K> objs, Class<T> clazz) {
        List<K> result = new ArrayList<K>();
        result.addAll(objs);
        return getDefaultTabList(result, clazz);
    }

    public static <T extends TabListItem, K extends
            IconEnumstype> TreeListResultModel<List<T>> getDefaultTabList(ListResultModel<List<K>> objs, Class<T> clazz) {
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


    public static <T extends TabListItem, K extends
            IconEnumstype> TreeListResultModel<List<T>> getDefaultTabList(List<K> objs, Class<T> clazz) {
        TreeListResultModel userStatusInfo = null;
        try {
            userStatusInfo = getTabList(objs, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            userStatusInfo = new ErrorListResultModel();
            ((ErrorListResultModel) userStatusInfo).setErrdes("内部错误");
        }

        return userStatusInfo;

    }
}
