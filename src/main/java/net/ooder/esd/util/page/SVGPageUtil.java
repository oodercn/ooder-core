package net.ooder.esd.util.page;

public class SVGPageUtil {

//    static <K extends Key> SVGBaseComponent fillObj(K obj) {
//        MethodConfig methodConfig = (MethodConfig) JDSActionContext.getActionContext().getContext().get(CustomViewFactory.MethodBeanKey);
//        CustomViewBean viewBean = methodConfig.getView();
//        NavTabsViewBean tabsViewBean = null;
//
//        if (viewBean instanceof NavTabsBaseViewBean) {
//            NavTabsBaseViewBean navTabsBaseViewBean = (NavTabsBaseViewBean) viewBean;
//            tabsViewBean = navTabsBaseViewBean.getTabsViewBean();
//        } else {
//            tabsViewBean = (NavTabsViewBean) viewBean;
//        }
//        Constructor[] constructors = clazz.getDeclaredConstructors();
//        T t = null;
//        TabItemBean tabItemBean = getChildTabBean(tabsViewBean, clazz, obj);
//        if (tabItemBean != null) {
//            try {
//                ConstructorBean constructorBean = tabItemBean.getConstructorBean();
//                t = (T) constructorBean.invok(obj);
//                List<String> hiddenList = viewBean.getHiddenFieldNames();
//                for (String hiddenName : hiddenList) {
//                    t.addTagVar(hiddenName, OgnlUtil.getValue(hiddenName, getOgnlContext(), t));
//                }
//
//                if (t.getCaption() == null && viewBean.getCaptionField() != null) {
//                    t.setCaption(OgnlUtil.getValue(viewBean.getCaptionField().getFieldname(), getOgnlContext(), t).toString());
//                }
//
//
//                if (t.getId() == null && viewBean.getUidField() != null) {
//                    Object pkValue = OgnlUtil.getValue(viewBean.getUidField().getFieldname(), getOgnlContext(), t);
//                    if (pkValue != null) {
//                        t.setId(pkValue.toString());
//                    }
//                }
//
//                if (t.getCaption() == null) {
//                    t.setCaption(tabItemBean.getCaption());
//                }
//                if (t.getId() == null) {
//                    t.setId(tabItemBean.getId());
//                }
//
//                String bindClassName = t.getBindClassName();
//                if (bindClassName == null) {
//                    bindClassName = tabItemBean.getBindService().getName();
//                }
//
//                if (bindClassName == null) {
//                    bindClassName = tabsViewBean.getBindService().getName();
//                }
//                t.setCloseBtn(tabItemBean.getCloseBtn());
//                t.setImageClass(tabItemBean.getImageClass());
//
//
//                t.setTabindex(tabItemBean.getIndex());
//                t.setDynLoad(tabItemBean.getLazyLoad());
//
//
//                if (tabItemBean.getBindService() != null && !tabItemBean.getBindService().equals(Void.class)) {
//                    bindClassName = tabItemBean.getBindService().getName();
//                }
//                if (bindClassName != null && !bindClassName.equals("") && !bindClassName.equals(Void.class.getName())) {
//                    ApiClassConfig config = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(bindClassName, viewBean.getDomainId());
//                    MethodConfig editorMethod = config.getMethodByItem(CustomMenuItem.tabEditor);
//                    if (editorMethod != null) {
//                        if (!editorMethod.getImageClass().equals(MethodConfig.DefaultImageClass)) {
//                            t.setImageClass(editorMethod.getImageClass());
//                        }
//                        t.setEuClassName(editorMethod.getEUClassName());
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//
//        }
//
//        return t;
//    }
//
//
//    public static OgnlContext getOgnlContext() {
//        HttpRequest request = (HttpRequest) JDSActionContext.getActionContext().getHttpRequest();
//        return request.getOgnlContext();
//    }
//
//
//    static <T extends TabListItem, K extends IconEnumstype> List<T> fillObjs(List<K> objs, Class<T> clazz) {
//        List<T> result = new ArrayList<T>();
//        if (objs != null) {
//            for (K obj : objs) {
//                if (obj != null) {
//                    if (clazz == null || obj.getClass().isAssignableFrom(clazz)) {
//                        result.add((T) obj);
//                    } else {
//
//                        T t = (T) fillObj(obj, clazz);
//                        result.add(t);
//
//                    }
//                }
//            }
//        }
//
//        return result;
//    }
//
//
//    public static <T extends TabListItem, K extends IconEnumstype> TreeListResultModel<List<T>> changTabList(ListResultModel<List<K>> objs, Class<T> clazz) {
//        TreeListResultModel<List<T>> userStatusInfo = new TreeListResultModel<List<T>>();
//        try {
//            List<T> pageResult = new ArrayList<T>();
//            List<K> list = objs.get();
//            userStatusInfo.setData(fillObjs(list, clazz));
//            userStatusInfo.setSize(objs.getSize());
//            Object id = JDSActionContext.getActionContext().getParams("id");
//            if (id != null && !id.equals("")) {
//                String[] orgIdArr = StringUtility.split(id.toString(), ";");
//                userStatusInfo.setIds(Arrays.asList(orgIdArr));
//            } else {
//                if (pageResult.size() > 0) {
//                    userStatusInfo.setIds(Arrays.asList(new String[]{pageResult.get(0).getId()}));
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            userStatusInfo = new ErrorListResultModel();
//            ((ErrorListResultModel) userStatusInfo).setErrdes("内部错误");
//        }
//
//        return userStatusInfo;
//    }
//
//
//    public static <T extends TabListItem, K extends IconEnumstype> TreeListResultModel<List<T>> getDefaultTabList(Set<K> objs, Class<T> clazz) {
//        List<K> result = new ArrayList<K>();
//        result.addAll(objs);
//        return getDefaultTabList(result, clazz);
//    }
//
//    public static <T extends TabListItem, K extends IconEnumstype> TreeListResultModel<List<T>> getDefaultTabList(ListResultModel<List<K>> objs, Class<T> clazz) {
//        TreeListResultModel<List<T>> userStatusInfo = new TreeListResultModel<List<T>>();
//        try {
//            List<T> pageResult = new ArrayList<T>();
//            List<K> list = objs.get();
//            userStatusInfo.setData(fillObjs(list, clazz));
//            userStatusInfo.setSize(objs.getSize());
//            Object id = JDSActionContext.getActionContext().getParams("id");
//            if (id != null && !id.equals("")) {
//                String[] orgIdArr = StringUtility.split(id.toString(), ";");
//                userStatusInfo.setIds(Arrays.asList(orgIdArr));
//            } else {
//                if (pageResult.size() > 0) {
//                    userStatusInfo.setIds(Arrays.asList(new String[]{pageResult.get(0).getId()}));
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            userStatusInfo = new ErrorListResultModel();
//            ((ErrorListResultModel) userStatusInfo).setErrdes("内部错误");
//        }
//
//        return userStatusInfo;
//    }
//
//
//    public static <T extends TabListItem, K extends IconEnumstype> TreeListResultModel<List<T>> getDefaultTabList(List<K> objs, Class<T> clazz) {
//        TreeListResultModel userStatusInfo = null;
//        try {
//            userStatusInfo = getTabList(objs, clazz);
//        } catch (Exception e) {
//            e.printStackTrace();
//            userStatusInfo = new ErrorListResultModel();
//            // ((ErrorResultModel) userStatusInfo).setErrcode(e.getErrorCode());
//            ((ErrorListResultModel) userStatusInfo).setErrdes("内部错误");
//        }
//
//        return userStatusInfo;
//
//    }
}
