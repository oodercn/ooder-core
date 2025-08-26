package net.ooder.esd.custom.component.form.field;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSConstants;
import net.ooder.common.JDSException;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.CustomMenu;
import net.ooder.esd.annotation.event.ActionTypeEnum;
import net.ooder.esd.annotation.event.CustomFieldEvent;
import net.ooder.esd.annotation.event.ListEventEnum;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.ListMenuBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.bean.field.CustomListBean;
import net.ooder.esd.bean.field.base.ListFieldBean;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.custom.CustomMethodInfo;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.custom.action.CustomConditionAction;
import net.ooder.esd.custom.action.ShowPageAction;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.FieldAggConfig;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.component.ListComponent;
import net.ooder.esd.tool.properties.APICallerProperties;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.Condition;
import net.ooder.esd.tool.properties.UrlPathData;
import net.ooder.esd.tool.properties.list.ListFieldProperties;
import net.ooder.esd.util.json.APICallSerialize;

import net.ooder.jds.core.esb.EsbUtil;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.APIConfig;
import net.ooder.web.APIConfigFactory;
import net.ooder.web.RequestMethodBean;

import java.util.*;

public class CustomListComponent extends ListComponent {

    public static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, CustomListComponent.class);

    @JSONField(serialize = false)
    Map<String, TreeListItem> itemMap = new HashMap<>();

    public String id;
    @JSONField(serializeUsing = APICallSerialize.class)
    List<APICallerComponent> apis = new ArrayList<>();


    public CustomListComponent(EUModule euModule, FieldFormConfig<ListFieldBean, ?> field, String target, Object value, Map valueMap) {
        super(field.getFieldname());
        this.setAlias(field.getFieldname());

        if (field.getFieldBean() != null) {
            OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(field.getFieldBean()), Map.class), this, false, false);
        }
        FieldAggConfig aggConfig = field.getAggConfig();
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(aggConfig), Map.class), this, false, false);

        ListFieldBean fieldBean = field.getWidgetConfig();
        ContainerBean containerBean = field.getContainerBean();
        CustomListBean customListBean = fieldBean.getCustomListBean();

        ListFieldProperties listProperties = new ListFieldProperties(fieldBean, containerBean);
        listProperties.setId(field.getId());
        listProperties.setName(field.getFieldname());
        listProperties.setValue(value);

        if (customListBean.getDynLoad() != null && customListBean.getDynLoad() && customListBean.getBindClass() != null && !customListBean.getBindClass().equals(Enum.class)) {
            lazyLoad(euModule, field, this, customListBean.getBindClass());
        }
        if (fieldBean.getListMenuBean() != null) {
            this.initMenu(fieldBean.getListMenuBean());
            Set<String> keset = itemMap.keySet();
            for (String key : keset) {
                TreeListItem item = itemMap.get(key);
                listProperties.addItem(item);
            }
            euModule.getComponent().addChildren(this.getApis().toArray(new APICallerComponent[]{}));
        }

        this.setProperties(listProperties);
        this.setTarget(target);

    }


    private void lazyLoad(EUModule euModule, FieldFormConfig field, ListComponent listComponent, Class bindClass) {
        try {
            ApiClassConfig config = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(bindClass.getName());
            MethodConfig loadMethod = config.getMethodByItem(CustomMenuItem.RELOAD);
            if (loadMethod == null) {
                loadMethod = config.getFieldEvent(CustomFieldEvent.LOADITEMS);
            }
            if (loadMethod != null) {
                APICallerComponent apiCallerComponent = new APICallerComponent(loadMethod);
                UrlPathData ctxData = new UrlPathData(euModule.getComponent().getTopComponentBox().getAlias(), RequestPathTypeEnum.FORM, "");
                apiCallerComponent.getProperties().addRequestData(ctxData);
                UrlPathData pageBarPathData = new UrlPathData(listComponent.getAlias(), ResponsePathTypeEnum.LIST, "data");
                apiCallerComponent.getProperties().addResponseData(pageBarPathData);
                euModule.getComponent().addChildren(apiCallerComponent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void initMenu(ListMenuBean menuBean) {
        for (Class clazz : menuBean.getMenuClasses()) {
            try {
                ESDClass menuClass = BuildFactory.getInstance().getClassManager().loadAggregation(clazz.getName());
                List<CustomMethodInfo> esdMethods = new ArrayList<>();
                esdMethods.addAll(menuClass.getMethodsList());
                esdMethods.addAll(menuClass.getOtherMethodsList());
                Collections.sort(esdMethods);
                APIConfig apiConfig = APIConfigFactory.getInstance().getAPIConfig(clazz.getName());
                for (CustomMethodInfo field : esdMethods) {
                    CustomMethodInfo methodField = field;
                    if (!methodField.isSplit()) {
                        RequestMethodBean methodBean = apiConfig.getMethodByName(methodField.getInnerMethod().getName());
                        if (methodBean != null) {
                            ApiClassConfig classConfig = null;
                            try {
                                classConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(menuClass.getClassName(), false);
                                MethodConfig methodAPIBean = classConfig.getMethodByName(methodBean.getMethodName());
                                if (methodAPIBean == null) {
                                    methodAPIBean = new MethodConfig(field, classConfig);
                                }
                                APICallerComponent component = new APICallerComponent(methodAPIBean);
                                component.setAlias(menuBean.getId() + "_" + component.getAlias());
                                APICallerProperties properties = component.getProperties();
                                properties.setImageClass(methodField.getImageClass());
                                if (methodAPIBean != null && methodAPIBean.isModule()) {
                                    this.addMenu(methodAPIBean.getEUClassName(), component);
                                } else {
                                    this.addMenu(component);
                                }
                            } catch (JDSException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void addMenu(CustomMenu... types) {
        for (CustomMenu type : types) {
            if (!type.type().equals("")) {
                String menuId = type.type() + "_" + ComboInputType.button.name();
                TreeListItem menuItem = itemMap.get(menuId);
                String caption = type.caption();
                if (menuItem == null) {
                    menuItem = new TreeListItem(menuId, caption, type.imageClass());
                    menuItem.setExpression(type.expression());
                    itemMap.put(menuId, menuItem);
                } else {
                    menuItem.setCaption(caption);
                    if (!type.expression().equals("")) {
                        menuItem.setExpression(type.expression());
                    }
                    if (!type.imageClass().equals("")) {
                        menuItem.setImageClass(type.imageClass());
                    }
                }
                fillActions(type);
            }
        }
    }

    public void addMenu(APICallerComponent component) {
        this.addComponentMenu(component, null);
    }

    public void addMenu(String className, APICallerComponent component) {
        addModuleMenu(className, component, null);
    }

    public APICallerComponent addModuleMenu(String className, APICallerComponent component, Map<String, Object> paramsMap) {
        String expression = component.getProperties().getExpression();
        if (expression == null || expression.equals("") || this.parExpression(expression)) {
            String caption = component.getProperties().getDesc();
            String imageClass = component.getProperties().getImageClass();
            String menuId = component.getAlias() + ComboInputType.button.name();
            TreeListItem menuItem = itemMap.get(menuId);

            Set<CustomMenuItem> items = component.getProperties().getBindMenu();
            Set<CustomMenuItem> customItems = new HashSet<>();

            for (CustomMenuItem item : items) {
                if (!item.equals(CustomMenuItem.INDEX) && item.equals(CustomMenuItem.INDEXS)) {
                    customItems.add(item);
                }
            }

            if (customItems.size() > 0) {
                for (CustomMenuItem item : customItems) {
                    this.addMenu(item.getMenu());
                }
                if (!apis.contains(component)) {
                    this.apis.add(component);
                }
            } else {
                if (menuItem == null) {
                    menuItem = new TreeListItem(menuId, caption, imageClass, component.getProperties().getTips(), component.getProperties().getMenuType());
                    itemMap.put(menuId, menuItem);
                } else {
                    menuItem.setCaption(caption);
                    menuItem.setTips(component.getProperties().getTips());
                    if (expression != null && !expression.equals("")) {
                        menuItem.setExpression(expression);
                    }
                    if (imageClass != null && !imageClass.equals("")) {
                        menuItem.setImageClass(imageClass);
                    }
                }
                if (paramsMap != null) {
                    if (menuItem.getTagVar() == null) {
                        menuItem.setTagVar(paramsMap);
                    } else {
                        menuItem.getTagVar().putAll(paramsMap);
                    }
                }

                ShowPageAction action = new ShowPageAction(ListEventEnum.onClick);
                action.setTarget(className);
                action.updateArgs("{args[1].tagVar}", 5);
                action.setDesc("打开" + component.getProperties().getDesc());
                Condition condition = new Condition("{args[1].id}", SymbolType.equal, menuId);
                List<Condition> conditions = new ArrayList<>();
                conditions.add(condition);
                action.setConditions(conditions);
                action.set_return(false);
                this.addAction(action);

            }
        }
        return component;
    }


    public APICallerComponent addComponentMenu(APICallerComponent component, Map<String, Object> paramsMap) {
        String expression = component.getProperties().getExpression();
        if (expression == null || expression.equals("") || this.parExpression(expression)) {

            String caption = component.getProperties().getDesc();

            String imageClass = component.getProperties().getImageClass();
            Set<CustomMenuItem> items = component.getProperties().getBindMenu();
            if (items != null && items.size() > 0) {
                for (CustomMenuItem item : items) {
                    this.addMenu(item.getMenu());
                    String menuId = item.getMenu().type() + ComboInputType.button.name();
                    Set<Action> actions = component.getActions();
                    if (actions != null && actions.size() > 0) {
                        for (Action action : actions) {
                            List<Condition> conditions = new ArrayList<>();
                            action.setDesc(action.getDesc().equals("") ? caption : action.getDesc());
                            action.set_return(false);
                            Condition condition = new Condition("{args[1].id}", SymbolType.equal, menuId);
                            conditions.add(condition);
                            action.setConditions(conditions);
                            this.addAction(action);
                        }
                    } else {
                        if (!apis.contains(component)) {
                            this.apis.add(component);
                        }
                        Action action = new Action(ListEventEnum.onClick);
                        action.setArgs(Arrays.asList(new String[]{"{page." + component.getAlias() + ".invoke()}"}));
                        action.setType(ActionTypeEnum.control);
                        action.setTarget(component.getAlias());
                        action.setDesc(component.getProperties().getTips());
                        action.setMethod("invoke");
                        action.setRedirection("other:callback:call");
                        action.set_return(false);
                        List<Condition> conditions = new ArrayList<>();
                        Condition condition = new Condition("{args[1].id}", SymbolType.equal, menuId);
                        conditions.add(condition);
                        action.setConditions(conditions);
                        this.addAction(action);
                    }

                }
                if (!apis.contains(component)) {
                    this.apis.add(component);
                }

            } else {
                String menuId = component.getAlias() + ComboInputType.button.name();
                TreeListItem menuItem = itemMap.get(menuId);
                if (menuItem == null) {
                    menuItem = new TreeListItem(menuId, caption, imageClass, component.getProperties().getTips(), component.getProperties().getMenuType());

                    itemMap.put(menuId, menuItem);
                } else {
                    menuItem.setTips(component.getProperties().getTips());

                    menuItem.setCaption(caption);

                    if (expression != null && !expression.equals("")) {
                        menuItem.setExpression(expression);
                    }
                    if (imageClass != null && !imageClass.equals("")) {
                        menuItem.setImageClass(imageClass);
                    }
                }
                if (paramsMap != null) {
                    menuItem.getTagVar().putAll(paramsMap);
                }
                Set<Action> actions = component.getActions();
                if (actions != null && actions.size() > 0) {
                    for (Action action : actions) {
                        List<Condition> conditions = new ArrayList<>();
                        action.setDesc((action.getDesc() == null || action.getDesc().equals("")) ? component.getProperties().getTips() : action.getDesc());
                        action.set_return(false);
                        Condition condition = new Condition("{args[1].id}", SymbolType.equal, menuId);
                        conditions.add(condition);
                        action.setConditions(conditions);
                        this.addAction(action);
                    }
                } else {
                    if (!apis.contains(component)) {
                        this.apis.add(component);
                    }
                    Action action = new Action(ListEventEnum.onClick);
                    action.setArgs(Arrays.asList(new String[]{"{page." + component.getAlias() + ".invoke()}"}));
                    action.setType(ActionTypeEnum.control);
                    action.setTarget(component.getAlias());
                    action.setDesc(component.getProperties().getTips());
                    action.setMethod("invoke");
                    action.setRedirection("other:callback:call");
                    action.set_return(false);
                    List<Condition> conditions = new ArrayList<>();
                    Condition condition = new Condition("{args[1].id}", SymbolType.equal, menuId);
                    conditions.add(condition);
                    action.setConditions(conditions);
                    this.addAction(action);


                }


            }
        }
        return component;
    }


    boolean parExpression(String expression) {
        boolean result = false;
        try {
            result = EsbUtil.parExpression(expression, Boolean.class);
        } catch (Throwable e) {
            //  e.printStackTrace();
            logger.error("expression[" + expression + "] par err[" + e.getMessage() + "]");
        }
        return result;

    }

    public List<Action> fillActions(CustomMenu type) {
        List<Action> actions = new ArrayList<>();
        CustomAction[] actionTypes = type.actions();
        for (CustomAction actionType : actionTypes) {
            String exprossion = actionType.expression();
            try {
                if (exprossion != null && !exprossion.equals("") && EsbUtil.parExpression(exprossion, Boolean.class)) {
                    CustomConditionAction action = new CustomConditionAction(actionType, type, ListEventEnum.onClick);
                    this.addAction(action);
                    actions.add(action);
                }
            } catch (Throwable e) {
                // e.printStackTrace();
                logger.error("expression[" + exprossion + "] par err[" + e.getMessage() + "]");
                // log.error("expression err[" + actionType.getExpression() + "]");
            }
        }
        return actions;
    }


    public List<APICallerComponent> getApis() {
        return apis;
    }

    public void setApis(List<APICallerComponent> apis) {
        this.apis = apis;
    }

}
