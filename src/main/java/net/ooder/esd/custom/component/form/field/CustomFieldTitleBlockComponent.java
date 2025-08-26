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
import net.ooder.esd.annotation.event.TitleBlockEventEnum;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.ListMenuBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.bean.field.CustomListBean;
import net.ooder.esd.bean.field.CustomTitleBlockFieldBean;
import net.ooder.esd.bean.view.CustomTitleBlockViewBean;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.custom.CustomMethodInfo;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.custom.action.CustomConditionAction;
import net.ooder.esd.custom.action.ShowPageAction;
import net.ooder.esd.custom.component.CustomGalleryComponent;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.TitleBlockComponent;
import net.ooder.esd.tool.properties.*;
import net.ooder.esd.tool.properties.item.TitleBlockItem;
import net.ooder.esd.util.OODUtil;
import net.ooder.esd.util.json.APICallSerialize;

import net.ooder.jds.core.esb.EsbUtil;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.APIConfig;
import net.ooder.web.APIConfigFactory;
import net.ooder.web.RequestMethodBean;

import java.util.*;

public class CustomFieldTitleBlockComponent extends TitleBlockComponent {
    public static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, CustomGalleryComponent.class);

    @JSONField(serialize = false)
    Map<String, TreeListItem> itemMap = new HashMap<>();

    public String id;
    @JSONField(serializeUsing = APICallSerialize.class)
    List<APICallerComponent> apis = new ArrayList<>();


    public CustomFieldTitleBlockComponent(EUModule euModule, FieldFormConfig<CustomTitleBlockFieldBean, ?> field, String target, Object value, Map valueMap) {
        super(OODUtil.formatJavaName(field.getFieldname(), false));
        CustomTitleBlockFieldBean fieldBean = field.getWidgetConfig();
        CustomListBean customListBean = fieldBean.getCustomListBean();
        CustomTitleBlockViewBean titleBlockViewBean = (CustomTitleBlockViewBean) field.getMethodConfig().getView();
        if (titleBlockViewBean == null) {
            titleBlockViewBean = new CustomTitleBlockViewBean(field.getMethodConfig());
        }
        Component component = field.getWidgetConfig().getComponent();
        if (component != null) {
            if (component.getCS() != null) {
                this.CS = component.getCS();
            }
            this.events = component.getEvents();
        }

        //合并方法注解
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(fieldBean), Map.class), titleBlockViewBean, false, false);
        TitleBlockProperties titleBlockProperties = new TitleBlockProperties(titleBlockViewBean);
        titleBlockProperties.setId(field.getId());
        titleBlockProperties.setName(field.getFieldname());
        titleBlockProperties.setValue(value);

        if (customListBean.getDynLoad() != null && customListBean.getDynLoad()) {
            Class serviceClass = customListBean.getBindClass();
            if (serviceClass == null) {
                serviceClass = titleBlockViewBean.getBindService();
            }
            if (serviceClass == null) {
                serviceClass = field.getMethodConfig().getSourceClass().getCtClass();
            }
            lazyLoad(euModule, field, this, serviceClass);
        }

        if (customListBean.getCs() != null) {
            this.setCS(customListBean.getCs());
        }


        if (fieldBean.getListMenuBean() != null) {
            this.initMenu(fieldBean.getListMenuBean());
            Set<String> keset = itemMap.keySet();
            for (String key : keset) {
                TreeListItem item = itemMap.get(key);
                titleBlockProperties.addItem(new TitleBlockItem(item));
            }
            euModule.getComponent().addChildren(this.getApis().toArray(new APICallerComponent[]{}));
        }

//        try {
//            EUModule module = titleBlockViewBean.getMethodConfig().getModule(valueMap, euModule.getPackageName());
//        } catch (JDSException e) {
//            e.printStackTrace();
//        }

        this.setProperties(titleBlockProperties);
        this.setTarget(target);

    }


    private void lazyLoad(EUModule euModule, FieldFormConfig field, CustomFieldTitleBlockComponent listComponent, Class bindClass) {
        try {
            ApiClassConfig config = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(bindClass.getName());
            if (config != null) {
                MethodConfig loadMethod = config.getMethodByItem(CustomMenuItem.RELOAD);
                if (loadMethod != null) {
                    APICallerComponent apiCallerComponent = new APICallerComponent(loadMethod);
                    UrlPathData ctxData = new UrlPathData(euModule.getComponent().getTopComponentBox().getAlias(), RequestPathTypeEnum.FORM, "");
                    apiCallerComponent.getProperties().addRequestData(ctxData);
                    UrlPathData galleryData = new UrlPathData(listComponent.getAlias(), ResponsePathTypeEnum.TITLEBLOCK, "data");
                    apiCallerComponent.getProperties().addResponseData(galleryData);
                    apiCallerComponent.getProperties().setAutoRun(true);
                    euModule.getComponent().addChildren(apiCallerComponent);
                }
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

                ShowPageAction action = new ShowPageAction(TitleBlockEventEnum.onClick);
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
                        Action action = new Action(TitleBlockEventEnum.onClick);
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
                    Action action = new Action(TitleBlockEventEnum.onClick);
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
                    CustomConditionAction action = new CustomConditionAction(actionType, type, TitleBlockEventEnum.onClick);
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
