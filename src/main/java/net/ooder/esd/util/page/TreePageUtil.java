package net.ooder.esd.util.page;

import net.ooder.common.JDSConstants;
import net.ooder.common.JDSException;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.common.util.StringUtility;
import net.ooder.config.ErrorListResultModel;
import net.ooder.config.ListResultModel;
import net.ooder.config.TreeListResultModel;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.ui.SelModeType;
import net.ooder.esd.bean.CustomViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.bean.view.ChildTreeViewBean;
import net.ooder.esd.bean.view.CustomTreeViewBean;
import net.ooder.esd.bean.view.NavTreeComboViewBean;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.web.RemoteConnectionManager;

import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TreePageUtil {
    private static final Log logger1 = LogFactory.getLog(JDSConstants.CONFIG_KEY, TreePageUtil.class);

    public static final String ID = "id";
    public static final String IDSARR = "idsArr";
    public static final String TREEVIEWId = TreeItemTask.treeViewIdName;


    public static <T extends TreeListItem, K> TreeListResultModel<List<T>> getTreeList(Set<K> objs, Class<T> clazz) {
        return getTreeList(objs, clazz, null);
    }

    public static <T extends TreeListItem, K> TreeListResultModel<List<T>> getTreeList(Set<K> objs, Class<T> clazz, List<String> ids) {
        List<K> result = new ArrayList<K>();
        result.addAll(objs);
        return getTreeList(result, clazz, ids);
    }

    public static <T extends TreeListItem, K> TreeListResultModel<List<T>> getTreeList(List<K> objs, Class<T> clazz) {

        return getTreeList(objs, clazz, null);
    }


    private static List<String> checkIds(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            Object object = JDSActionContext.getActionContext().getContext().get(IDSARR);
            if (object != null && object instanceof List) {
                ids = (List<String>) object;
            } else {
                ids = new ArrayList<>();
            }
        } else {
            JDSActionContext.getActionContext().getContext().put(IDSARR, ids);
        }

        if (ids.isEmpty()) {
            Object id = JDSActionContext.getActionContext().getParams(ID);
            if (id != null && !id.equals("")) {
                String[] orgIdArr = StringUtility.split(id.toString(), ";");
                ids = Arrays.asList(orgIdArr);
            }
        }
        return ids;
    }

    public static <T extends TreeListItem, K> TreeListResultModel<List<T>> getTreeList(List<K> objs, Class<T> clazz, List<String> ids) {

        TreeListResultModel<List<T>> userStatusInfo = new TreeListResultModel<List<T>>();
        ids = checkIds(ids);
        List<T> pageResult = fillObjs(objs, clazz, ids);

        CustomTreeViewBean viewBean = null;
        MethodConfig methodConfig = (MethodConfig) JDSActionContext.getActionContext().getContext().get(CustomViewFactory.TopMethodBeanKey);
        if (methodConfig != null) {
            viewBean = getTreeViewBean(methodConfig);
        }

        if (viewBean == null) {
            methodConfig = (MethodConfig) JDSActionContext.getActionContext().getContext().get(CustomViewFactory.MethodBeanKey);
            viewBean = getTreeViewBean(methodConfig);
        }


        if (viewBean != null && viewBean.getSelMode() != null && (viewBean.getSelMode().equals(SelModeType.single) || viewBean.getSelMode().equals(SelModeType.none))) {
            ids = fillIds(ids, pageResult);
        }


        userStatusInfo.setIds(ids);
        userStatusInfo.setData(pageResult);
        userStatusInfo.setSize(objs.size());
        return userStatusInfo;
    }

    private static <T extends TreeListItem> List<String> fillIds(List<String> ids, List<T> pageResult) {
        if (ids == null) {
            ids = new ArrayList<>();
        }

        if (pageResult.size() > 0) {
            boolean checkIds = false;
            for (T item : pageResult) {
                if (ids.contains(item.getId())) {
                    checkIds = true;
                    break;
                }
            }

            if (!checkIds && JDSActionContext.getActionContext().getContext().get(IDSARR) == null) {
                ids = new ArrayList<>();
            }

            if (ids.isEmpty()) {
                ids.add(pageResult.get(0).getId());
            }
        }

        return ids;
    }

    public static <T extends TreeListItem> List<T> fillObjs(List objs, Class<T> clazz) {
        return fillObjs(objs, clazz, null);
    }


    private static CustomTreeViewBean getTreeViewBean(MethodConfig methodConfig) {
        CustomTreeViewBean viewBean = null;
        if (methodConfig != null) {
            ApiClassConfig classConfig = null;
            try {
                classConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(methodConfig.getSourceClassName());
                MethodConfig customMethodAPIBean = classConfig.getMethodByName(methodConfig.getMethodName());
                CustomViewBean customViewBean = customMethodAPIBean.getView();
                if (customViewBean instanceof NavTreeComboViewBean) {
                    viewBean = ((NavTreeComboViewBean) customViewBean).getTreeViewBean();
                } else if (customViewBean instanceof CustomTreeViewBean) {
                    viewBean = (CustomTreeViewBean) customViewBean;
                }
            } catch (JDSException e) {
                e.printStackTrace();
            }


        }

        return viewBean;
    }

    public static <T extends TreeListItem> List<T> fillObjs(List objs, Class<T> clazz, List<String> ids) {
        List<TreeItemTask<T>> tasks = new ArrayList<TreeItemTask<T>>();
        List<T> pageResult = new ArrayList<T>();
        CustomTreeViewBean viewBean = null;
        MethodConfig methodConfig = (MethodConfig) JDSActionContext.getActionContext().getContext().get(CustomViewFactory.TopMethodBeanKey);
        if (methodConfig != null) {
            viewBean = getTreeViewBean(methodConfig);

        }
        if (viewBean == null) {
            methodConfig = (MethodConfig) JDSActionContext.getActionContext().getContext().get(CustomViewFactory.MethodBeanKey);
            viewBean = getTreeViewBean(methodConfig);
        }


        ChildTreeViewBean childTreeViewBean = null;
        Object treeViewId = JDSActionContext.getActionContext().getParams(TREEVIEWId);
        String taskId = viewBean.getId();
        if (treeViewId != null) {
            childTreeViewBean = viewBean.getChildTreeBean(treeViewId.toString());
            taskId = treeViewId.toString();
        }

        Set<String> classSet = new HashSet<>();

        for (Object obj : objs)
            if (obj != null) {
                if (clazz == null || clazz.isAssignableFrom(obj.getClass())) {
                    pageResult.add((T) obj);
                } else {
                    childTreeViewBean = viewBean.getChildTreeBean(obj);
                    if (childTreeViewBean == null) {
                        childTreeViewBean = viewBean.getLikeChildTreeBean(obj);
                    }
                    if (childTreeViewBean == null) {
                        CustomTreeViewBean otherViewBean = new CustomTreeViewBean(clazz, viewBean);
                        childTreeViewBean = otherViewBean.getChildTreeBean(obj);
                        if (childTreeViewBean == null) {
                            childTreeViewBean = otherViewBean.getLikeChildTreeBean(obj);
                        }
                    }
                    TreeItemTask<T> task = new TreeItemTask(obj, clazz, viewBean, childTreeViewBean, ids);
                    classSet.add(obj.getClass().getSimpleName());
                    tasks.add(task);
                }
            }
        taskId = taskId + "_" + classSet.toString() + "_" + objs.size();
        try {
            RemoteConnectionManager.initConnection(taskId, tasks.size());
            List<Future<T>> futures = RemoteConnectionManager.getConntctionService(taskId).invokeAll(tasks);
            for (Future<T> resultFuture : futures) {
                try {
                    T item = resultFuture.get(200, TimeUnit.MILLISECONDS);
                    if (item != null) {
                        if (item.getPattern() != null && !item.getPattern().equals("")
                                // && (childTreeViewBean == null || (childTreeViewBean != null && (childTreeViewBean.getDeepSearch() != null && childTreeViewBean.getDeepSearch())))) {
                                //存在单根节点的时候会有问题
                                && ((childTreeViewBean != null && (childTreeViewBean.getDeepSearch() != null && !childTreeViewBean.getDeepSearch())))) {
                            Pattern p = Pattern.compile(item.getPattern(), Pattern.CASE_INSENSITIVE);
                            Matcher namematcher = p.matcher(item.getName() == null ? "" : item.getName());
                            Matcher cnnamematcher = p.matcher(item.getEuClassName() == null ? "" : item.getEuClassName());
                            Matcher cnmatcher = p.matcher(item.getDesc() == null ? "" : item.getDesc());
                            Matcher captionmatcher = p.matcher(item.getCaption() == null ? "" : item.getCaption());
                            if (namematcher.find()
                                    || cnnamematcher.find()
                                    || captionmatcher.find()
                                    || cnmatcher.find()
                                    || (item.getSub() != null && item.getSub().size() > 0)
                                    ) {
                                if (!pageResult.contains(item)) {
                                    item.setPatterned(true);
                                    pageResult.add(item);
                                }
                            }
                        } else {
                            item.setPatterned(true);
                            pageResult.add(item);
                        }
                        if (item.iniFold == null && item.getSub() != null && item.getSub().size() == 1) {
                            item.setIniFold(false);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            RemoteConnectionManager.getConntctionService(taskId).shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return pageResult;
    }


    public static <T extends TreeListItem, K> TreeListResultModel<List<T>> changTreeList(ListResultModel<List<K>> objs, Class<T> clazz) {
        return changTreeList(objs, clazz, null);
    }

    public static <T extends TreeListItem, K> TreeListResultModel<List<T>> changTreeList(ListResultModel<List<K>> objs, Class<T> clazz, List<String> ids) {
        TreeListResultModel<List<T>> userStatusInfo = new TreeListResultModel<List<T>>();
        try {
            List<T> pageResult = new ArrayList<T>();
            List<K> list = objs.get();
            ids = checkIds(ids);
            pageResult = fillObjs(list, clazz, ids);
            ids = fillIds(ids, pageResult);
            userStatusInfo.setIds(ids);
            userStatusInfo.setData(pageResult);
            userStatusInfo.setSize(objs.getSize());
        } catch (Exception e) {
            e.printStackTrace();
            userStatusInfo = new ErrorListResultModel();
            ((ErrorListResultModel) userStatusInfo).setErrdes("内部错误");
        }

        return userStatusInfo;
    }

    public static <T extends TreeListItem> TreeListResultModel<List<T>> getDefaultTreeList(List<T> objs) {
        return getDefaultTreeList(objs, new ArrayList<>());
    }

    public static <T extends TreeListItem> TreeListResultModel<List<T>> getDefaultTreeList(List<T> objs, List<String> ids) {
        return getDefaultTreeList(objs, null, ids);
    }

    public static <T extends TreeListItem> TreeListResultModel<List<T>> getDefaultTreeList(T... objs) {
        List<T> result = new ArrayList<T>();
        result.addAll(Arrays.asList(objs));
        return getDefaultTreeList(objs);
    }

    public static <T extends TreeListItem> TreeListResultModel<List<T>> getDefaultTreeList(Set<T> objs, List<String> ids) {
        List<T> result = new ArrayList<T>();
        result.addAll(objs);
        return getDefaultTreeList(result, null, ids);
    }

    public static <T extends TreeListItem, K> TreeListResultModel<List<T>> getDefaultTreeList(Set<K> objs, Class<T> clazz) {
        return getDefaultTreeList(objs, clazz, null);
    }

    public static <T extends TreeListItem, K> TreeListResultModel<List<T>> getDefaultTreeList(Set<K> objs, Class<T> clazz, List<String> ids) {
        List<K> result = new ArrayList<K>();
        result.addAll(objs);
        return getDefaultTreeList(result, clazz, ids);
    }

    public static <T extends TreeListItem, K> TreeListResultModel<List<T>> getDefaultTreeList(ListResultModel<List<K>> objs, Class<T> clazz) {
        return getDefaultTreeList(objs, clazz, null);
    }

    public static <T extends TreeListItem, K> TreeListResultModel<List<T>> getDefaultTreeList(ListResultModel<List<K>> objs, Class<T> clazz, List<String> ids) {
        TreeListResultModel<List<T>> userStatusInfo = new TreeListResultModel<List<T>>();
        try {
            List<T> pageResult = new ArrayList<T>();
            List<K> list = objs.get();
            ids = checkIds(ids);
            userStatusInfo.setData(fillObjs(list, clazz, ids));
            userStatusInfo.setSize(objs.getSize());
            ids = fillIds(ids, pageResult);
            userStatusInfo.setIds(ids);
        } catch (Exception e) {
            e.printStackTrace();
            userStatusInfo = new ErrorListResultModel();
            ((ErrorListResultModel) userStatusInfo).setErrdes("内部错误");
        }

        return userStatusInfo;
    }

    public static <T extends TreeListItem, K> TreeListResultModel<List<T>> getDefaultTreeList(List<K> objs, Class<T> clazz) {
        return getDefaultTreeList(objs, clazz, null);
    }

    public static <T extends TreeListItem, K> TreeListResultModel<List<T>> getDefaultTreeList(List<K> objs, Class<T> clazz, List<String> ids) {
        TreeListResultModel userStatusInfo = null;
        try {
            userStatusInfo = getTreeList(objs, clazz, ids);
        } catch (Exception e) {
            e.printStackTrace();
            userStatusInfo = new ErrorListResultModel();
            // ((ErrorResultModel) userStatusInfo).setErrcode(e.getErrorCode());
            ((ErrorListResultModel) userStatusInfo).setErrdes("内部错误");
        }

        return userStatusInfo;

    }


}
