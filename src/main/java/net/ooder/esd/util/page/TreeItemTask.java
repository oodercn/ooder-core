package net.ooder.esd.util.page;

import net.ooder.common.JDSConstants;
import net.ooder.common.JDSException;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.config.TreeListResultModel;
import net.ooder.context.JDSActionContext;
import net.ooder.context.JDSContext;
import net.ooder.esd.annotation.event.CustomTreeEvent;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.bean.view.ChildTreeViewBean;
import net.ooder.esd.bean.view.CustomTreeViewBean;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.dsm.aggregation.FieldAggConfig;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.server.context.MinServerActionContextImpl;
import net.ooder.web.ConstructorBean;
import ognl.OgnlContext;
import ognl.OgnlException;

import java.util.*;
import java.util.concurrent.Callable;

public class TreeItemTask<T extends TreeListItem> implements Callable<T> {
    private static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, TreeItemTask.class);
    public final static String treeViewIdName = "treeViewId";
    public ChildTreeViewBean childTreeViewBean;
    private MinServerActionContextImpl autoruncontext;
    private Object obj;
    private Class<T> clazz;
    private List<String> ids = new ArrayList<>();
    private OgnlContext onglContext;
    private CustomTreeViewBean viewBean;
    private AggEntityConfig aggEntityConfig;

    public TreeItemTask(Object object, Class<T> clazz, CustomTreeViewBean viewBean, ChildTreeViewBean childTreeViewBean, List<String> ids) {
        this.clazz = clazz;
        this.obj = object;
        this.childTreeViewBean = childTreeViewBean;
        if (ids != null) {
            this.ids = ids;
        }
        this.viewBean = viewBean;
        JDSContext context = JDSActionContext.getActionContext();
        this.autoruncontext = new MinServerActionContextImpl(context.getHttpRequest(), context.getOgnlContext());
        autoruncontext.getParamMap().putAll(context.getPagectx());
        autoruncontext.getParamMap().putAll(context.getContext());
        if (context.getSessionId() != null) {
            autoruncontext.setSessionId(context.getSessionId());
            autoruncontext.getSession().put("sessionHandle", context.getSession().get("sessionHandle"));
        }
        autoruncontext.setSessionMap(context.getSession());
        onglContext = autoruncontext.getOgnlContext();
        try {
            aggEntityConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(clazz.getName(), false);
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public T call() throws Exception {
        long start = System.currentTimeMillis();
        T t = null;
        JDSActionContext.setContext(autoruncontext);

        if (childTreeViewBean != null) {
            ConstructorBean constructorBean = childTreeViewBean.getConstructorBean();
            t = (T) constructorBean.invok(obj);
            if (viewBean != null && viewBean.getViewClassName().equals(clazz.getName())) {
                List<String> hiddenList = viewBean.getHiddenFieldNames();
                for (String hiddenName : hiddenList) {
                    try {
                        t.addTagVar(hiddenName, OgnlUtil.getValue(hiddenName, onglContext, t));
                    } catch (OgnlException e) {
                        //  logger.warn(viewBean.getViewClassName() + " not from field: " + hiddenName);
                    }
                }
                t.addTagVar(treeViewIdName, childTreeViewBean.getId());
                if (t.getCaption() == null || t.getCaption().equals("")) {
                    if (viewBean.getCaptionField() != null) {
                        t.setCaption(OgnlUtil.getValue(viewBean.getCaptionField().getFieldname(), onglContext, t).toString());
                    } else if (aggEntityConfig != null && aggEntityConfig.getCaptionField() != null) {
                        t.setCaption(OgnlUtil.getValue(aggEntityConfig.getCaptionField().getFieldname(), onglContext, obj).toString());
                    }
                }

                if (t.getImageClass() == null || t.getImageClass().equals("")) {
                    t.setImageClass(childTreeViewBean.getImageClass());
                }

                if (t.getId() == null || t.getId().equals("")) {
                    if (viewBean.getUidField() != null) {
                        Object pkValue = OgnlUtil.getValue(viewBean.getUidField().getFieldname(), onglContext, t);
                        if (pkValue != null) {
                            t.setId(pkValue.toString());
                        }
                    } else if (aggEntityConfig != null && aggEntityConfig.getUidField() != null) {
                        Object pkValue = OgnlUtil.getValue(aggEntityConfig.getUidField().getFieldname(), onglContext, obj);
                        if (pkValue != null) {
                            t.setId(pkValue.toString());
                        }
                    }
                }
            } else {
                List<FieldAggConfig> hiddenList = aggEntityConfig.getHiddenFieldList();
                for (FieldAggConfig field : hiddenList) {
                    try {
                        t.addTagVar(field.getFieldname(), OgnlUtil.getValue(field.getFieldname(), onglContext, t));
                    } catch (OgnlException e) {
                        logger.warn("viewClass:[" + viewBean.getViewClassName() + "] targetClass:[" + t.getClassName() + "] not from field: " + field.getFieldname());
                    }
                }
                t.addTagVar(treeViewIdName, childTreeViewBean.getId());
                if (t.getCaption() == null || t.getCaption().equals("")) {
                    if (aggEntityConfig.getCaptionField() != null) {
                        t.setCaption(OgnlUtil.getValue(aggEntityConfig.getCaptionField().getFieldname(), onglContext, t).toString());
                    }
                }
                if (t.getImageClass() == null || t.getImageClass().equals("")) {
                    t.setImageClass(childTreeViewBean.getImageClass());
                }

                if (t.getId() == null || t.getId().equals("")) {
                    if (aggEntityConfig.getUidField() != null) {
                        Object pkValue = OgnlUtil.getValue(aggEntityConfig.getUidField().getFieldname(), onglContext, t);
                        if (pkValue != null) {
                            t.setId(pkValue.toString());
                        }
                    }
                }
            }

            if (childTreeViewBean != null) {
                t.initChildTree(childTreeViewBean);
                Set<Class> bindClassList = new HashSet<>();
                if (childTreeViewBean.getBindClass() != null && childTreeViewBean.getBindClass().length > 0) {
                    bindClassList.addAll(Arrays.asList(childTreeViewBean.getBindClass()));
                }
                if (childTreeViewBean.getTreeItem() != null && childTreeViewBean.getTreeItem().getBindClass().length > 0) {
                    bindClassList.addAll(Arrays.asList(childTreeViewBean.getTreeItem().getBindClass()));
                }
                if (bindClassList.size() > 0) {

                    MethodConfig editorMethod = findMethod(bindClassList, CustomTreeEvent.TREENODEEDITOR);
                    MethodConfig loadChildMethod = findMethod(bindClassList, CustomTreeEvent.RELOADCHILD);
                    t.setGroupName(childTreeViewBean.getGroupName());
                    if (editorMethod != null && editorMethod.getRequestMethodBean() != null && childTreeViewBean.getCanEditor() != null && childTreeViewBean.getCanEditor()) {
                        t.setEuClassName(editorMethod.getEUClassName());
                    }

                    if (loadChildMethod != null && loadChildMethod.getRequestMethodBean() != null) {
                        Map valueMap = new HashMap();
                        valueMap.putAll(JDSActionContext.getActionContext().getContext());
                        Map tagVar = t.getTagVar();
                        if (tagVar != null) {
                            Set<String> keySet = tagVar.keySet();
                            for (String key : keySet) {
                                if (tagVar.get(key) != null && !tagVar.get(key).equals("")) {
                                    valueMap.put(key, tagVar.get(key));
                                }
                            }
                        }

                        List<TreeListItem> childItems = new ArrayList();
                        if (!childTreeViewBean.getLazyLoad() || (t.getPattern() != null && !t.getPattern().equals("") && (childTreeViewBean.getDeepSearch() != null && childTreeViewBean.getDeepSearch()))) {
                            start = System.currentTimeMillis();
                            Object object = loadChildMethod.getRequestMethodBean().invok(onglContext, valueMap);
                            if (object instanceof TreeListResultModel) {
                                TreeListResultModel<Collection<TreeListItem>> childList = (TreeListResultModel) object;
                                if (childList.getData() != null && !childList.getData().isEmpty()) {
                                    Collection<TreeListItem> items = childList.getData();
                                    for (TreeListItem treeListItem : items) {
                                        if (treeListItem.getParentClassName() != null && !treeListItem.getParentClassName().equals("")) {
                                            t.setEuClassName(treeListItem.getParentClassName());
                                        }
                                        if (this.ids.contains(treeListItem.getId())) {
                                            t.setIniFold(false);
                                        }
                                        childItems.add(treeListItem);
                                    }
                                    t.setSub(childItems);
                                } else if (editorMethod == null && childTreeViewBean.getAutoHidden() != null && childTreeViewBean.getAutoHidden()) {
                                    t.setHidden(true);
                                }
                                if (t.getPattern() != null && !t.getPattern().equals("") && childTreeViewBean.getDeepSearch()) {
                                    t.setIniFold(false);
                                }
                            } else {

                                if (object instanceof List) {
                                    childItems = (List<TreeListItem>) TreePageUtil.getTreeList((List) object, childTreeViewBean.getConstructorBean().getSourceConstructor().getDeclaringClass()).getData();
                                } else if (object instanceof Set) {
                                    childItems = (List<TreeListItem>) TreePageUtil.getTreeList((Set) object, childTreeViewBean.getConstructorBean().getSourceConstructor().getDeclaringClass()).getData();
                                }

                                if (childItems.size() > 0) {
                                    t.setSub(childItems);
                                }

                            }
                        } else {
                            t.setSub(childItems);
                        }
                    }
                }
            }

        }
        //logger.info("end FillItemTask= " + viewBean.getName() + "[" + obj + "] times=" + (System.currentTimeMillis() - start));
        return t;
    }

    public MethodConfig findMethod(Set<Class> bindClassList, CustomTreeEvent event) {
        for (Class bindClass : bindClassList) {
            if (!bindClass.equals(Void.class)) {
                try {
                    ApiClassConfig config = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(bindClass.getName());
                    if (config != null) {
                        MethodConfig methodConfig = config.getTreeEvent(event);
                        if (methodConfig != null) {
                            return methodConfig;
                        }
                    }
                } catch (JDSException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}