package net.ooder.esd.custom.component;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.EsbBeanAnnotation;
import net.ooder.common.JDSConstants;
import net.ooder.common.JDSException;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.CustomMenu;
import net.ooder.esd.annotation.event.ActionTypeEnum;
import net.ooder.esd.annotation.event.ToolBarEventEnum;
import net.ooder.esd.annotation.menu.CustomMenuType;
import net.ooder.esd.annotation.menu.GridMenu;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.annotation.ui.SymbolType;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.ToolBarEventBean;
import net.ooder.esd.bean.ToolBarMenuBean;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.bean.bar.MenuDynBar;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.custom.action.CustomConditionAction;
import net.ooder.esd.custom.action.ShowPageAction;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.engine.enums.MenuBarBean;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.component.ToolBarComponent;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.Condition;
import net.ooder.esd.tool.properties.Event;
import net.ooder.esd.tool.properties.ToolBarProperties;
import net.ooder.esd.util.json.APICallSerialize;
import net.ooder.jds.core.esb.EsbUtil;
import net.ooder.jds.core.esb.task.ExcuteObj;

import java.util.*;

@EsbBeanAnnotation
public class CustomToolsBar extends ToolBarComponent implements MenuDynBar<MenuDynBar, TreeListItem> {

    private static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, CustomToolsBar.class);
    @JSONField(serialize = false)
    Map<String, TreeListItem> itemMap = new HashMap<>();

    public String id;

    public String parentId;

    public String caption;

    public String imageClass;

    public Boolean showCaption = true;

    public Integer index = 100;

    @JSONField(serializeUsing = APICallSerialize.class)
    List<APICallerComponent> apis = new ArrayList<>();

    public CustomMenuType menuType = CustomMenuType.TOOLBAR;


    public CustomToolsBar(EUModule euModule, FieldFormConfig<ToolBarMenuBean, ?> field, String target, Object value, Map valueMap) {
        super(field.getWidgetConfig().getAlias());
        ToolBarMenuBean fieldBean = field.getWidgetConfig();
        ToolBarProperties properties = new ToolBarProperties(fieldBean);
        this.setProperties(properties);
        this.setTarget(target);

    }


    public CustomToolsBar(ToolBarMenuBean toolBarBean) {
        super(toolBarBean.getAlias(), new ToolBarProperties(toolBarBean));
        this.showCaption = toolBarBean.getShowCaption();
        this.id = toolBarBean.getId();
        fillChildCustomAction(toolBarBean);
    }

    public CustomToolsBar() {

    }


    public CustomToolsBar(MenuBarBean menuBarBean) {
        super(menuBarBean.getAlias(), new ToolBarProperties(menuBarBean));
        this.id = menuBarBean.getId();
        this.parentId = menuBarBean.getParentId();
        this.index = menuBarBean.getIndex();
        this.caption = menuBarBean.getCaption();
        this.showCaption = menuBarBean.getShowCaption();
        this.imageClass = menuBarBean.getImageClass();
        this.menuType = menuBarBean.getMenuType();
    }

    public CustomToolsBar(String id) {
        super(id, new ToolBarProperties());
        this.id = id;
    }


    public Boolean getShowCaption() {
        return showCaption;
    }

    public void setShowCaption(Boolean showCaption) {
        this.showCaption = showCaption;
    }

    @JSONField(serialize = false)
    public TreeListItem getGroup() {
        return this.getProperties().getGroup();
    }


    @Override
    public String getId() {
        return id;
    }

    @Override
    public CustomMenuType getMenuType() {
        return menuType;
    }

    @Override
    public String getCaption() {
        return caption;
    }

    @Override
    public String getImageClass() {
        return imageClass;
    }

    @Override
    public List<TreeListItem> filter(Object rowData) {
        List<TreeListItem> menuTypes = new ArrayList<TreeListItem>();
        List<TreeListItem> items = this.getGroup().getSub();
        List<ExcuteObj> tasks = new ArrayList<ExcuteObj>();
        for (TreeListItem item : items) {
            ExcuteObj obj = new ExcuteObj(item.getExpression(), Boolean.class, item);
            tasks.add(obj);
        }
        List<ExcuteObj> results = EsbUtil.parExpression(tasks);
        for (ExcuteObj result : results) {
            if (Boolean.valueOf(result.getObj().toString())) {
                menuTypes.add((TreeListItem) result.getSource());
            }
        }
        return menuTypes;
    }

    @Override
    public boolean initMenuClass(Class bindClazz) {

        return true;
    }

    @Override
    public Integer getIndex() {
        return index;
    }

    @Override
    public void addSplit(String id) {
        this.getProperties().addChild(new TreeListItem(ComboInputType.split, id));
    }


    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Override
    public void addChild(MenuDynBar bars) {
        CustomToolsBar topMenu = (CustomToolsBar) bars;
        this.getProperties().addItem(topMenu.getGroup());
        Map<ToolBarEventEnum, Event> event = ((CustomToolsBar) bars).getEvents();
        Set<Map.Entry<ToolBarEventEnum, Event>> entrySet = event.entrySet();
        for (Map.Entry<ToolBarEventEnum, Event> entry : entrySet) {
            List<Action> actions = entry.getValue().getActions();
            for (Action action : actions) {
                this.addAction(action);
            }
        }
        List<APICallerComponent> barApis = bars.getApis();
        for (APICallerComponent apiCallerComponent : barApis) {
            if (!apis.contains(apiCallerComponent)) {
                this.apis.add(apiCallerComponent);
            }
        }
    }


    public List<TreeListItem> addMenu(CustomMenu... types) {
        List<TreeListItem> treeListItems = new ArrayList<>();
        for (CustomMenu type : types) {
            if (!type.type().equals("")) {
                String menuId = type.type() + "_" + ComboInputType.button.name();
                TreeListItem menuItem = itemMap.get(menuId);
                String caption = type.caption();
                if (showCaption != null && !showCaption) {
                    caption = "";
                }
                if (menuItem == null) {
                    menuItem = new TreeListItem(menuId, caption, type.imageClass());
                    menuItem.setExpression(type.expression());
                    this.getGroup().addChild(menuItem);
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
                if (!treeListItems.contains(menuItem)) {
                    treeListItems.add(menuItem);
                    fillActions(type);
                }
            }
        }
        return treeListItems;
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
            if (showCaption != null && !showCaption) {
                caption = "";
            }
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
                    this.getGroup().addChild(menuItem);
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

                ShowPageAction action = new ShowPageAction(ToolBarEventEnum.onClick);
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
            if (showCaption != null && !showCaption) {
                caption = "";
            }
            if (!apis.contains(component)) {
                this.apis.add(component);
                if (component.getActions() != null && component.getActions().size() > 0) {
                    Set<Action> actions = component.getActions();
                    for (Action action : actions) {
                        List<Condition> conditions = action.getConditions();
                        if (conditions == null) {
                            conditions = new ArrayList<>();
                        }

                        Set<CustomMenuItem> items = component.getProperties().getBindMenu();
                        if (items != null && items.size() > 0) {
                            for (CustomMenuItem item : items) {
                                this.addMenu(item.getMenu());
                                String menuId = item.getMenu().type() + ComboInputType.button.name();
                                Condition condition = new Condition("{args[1].id}", SymbolType.equal, menuId);
                                conditions.add(condition);
                                action.setConditions(conditions);
                                action.setDesc("点击：" + item.getMethodName());
                            }

                        } else {
                            String menuId = component.getAlias() + ComboInputType.button.name();
                            String tips = component.getProperties().getTips();
                            if (tips == null && !caption.equals("")) {
                                tips = caption;
                            }
                            TreeListItem menuItem = itemMap.get(menuId);
                            if (menuItem == null) {
                                menuItem = new TreeListItem(menuId, caption, component.getProperties().getImageClass(), tips, component.getProperties().getMenuType());
                                this.getGroup().addChild(menuItem);

                                itemMap.put(menuId, menuItem);
                            } else {
                                menuItem.setTips(tips);
                                menuItem.setCaption(caption);
                                if (expression != null && !expression.equals("")) {
                                    menuItem.setExpression(expression);
                                }
                            }
                            if (paramsMap != null) {
                                menuItem.getTagVar().putAll(paramsMap);
                            }
                            action.setDesc("点击：" + tips);
                            Condition condition = new Condition("{args[1].id}", SymbolType.equal, menuId);
                            conditions.add(condition);
                            action.setConditions(conditions);
                        }
                        action.setEventKey(ToolBarEventEnum.onClick);
                        this.addAction(action);

                    }
                } else {
                    String imageClass = component.getProperties().getImageClass();
                    Set<CustomMenuItem> items = component.getProperties().getBindMenu();
                    if (items != null && items.size() > 0) {
                        for (CustomMenuItem item : items) {
                            this.addMenu(item.getMenu());
                            String menuId = item.getMenu().type() + ComboInputType.button.name();
                            Action action = new Action(ToolBarEventEnum.onClick);
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
                    } else {
                        String menuId = component.getAlias() + ComboInputType.button.name();
                        TreeListItem menuItem = itemMap.get(menuId);
                        if (menuItem == null) {
                            menuItem = new TreeListItem(menuId, caption, imageClass, component.getProperties().getTips(), component.getProperties().getMenuType());
                            this.getGroup().addChild(menuItem);
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

                        Action action = new Action(ToolBarEventEnum.onClick);
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


    void fillChildCustomAction(ToolBarMenuBean menuBean) {
        Set<ToolBarEventBean> extAPIEvent = menuBean.getExtAPIEvent();
        for (ToolBarEventBean eventEnum : extAPIEvent) {
            MethodConfig methodConfig = findMethod(eventEnum);
            String menuId = methodConfig.getMethodName() + ComboInputType.button.name();
            String caption = methodConfig.getCaption();
            if (showCaption != null && !showCaption) {
                caption = "";
            }
            ComboInputType inputType = ComboInputType.button;
            if (methodConfig.getFieldBean() != null && methodConfig.getFieldBean().getInputType() != null) {
                inputType = methodConfig.getFieldBean().getInputType();
            }

            TreeListItem menuItem = new TreeListItem(menuId, caption, methodConfig.getImageClass(), methodConfig.getTips(), inputType);
            this.getGroup().addChild(menuItem);
            Condition condition = new Condition("{args[1].id}", SymbolType.equal, menuId);
            List<Action> actions = eventEnum.getActions();
            for (Action action : actions) {
                if (!action.getConditions().contains(condition)) {
                    action.getConditions().add(condition);
                }
                if (methodConfig != null) {
                    if (action.getType().equals(ActionTypeEnum.control)) {
                        action.setTarget(this.getAlias());
                    } else if (action.getMethod().equals("call")) {
                        APICallerComponent apiCallerComponent = (APICallerComponent) this.getModuleComponent().findComponentByAlias(methodConfig.getMethodName());
                        if (apiCallerComponent == null) {
                            apiCallerComponent = new APICallerComponent(methodConfig);
                            this.addChildren(apiCallerComponent);
                        }
                        action.updateArgs("{page." + apiCallerComponent.getAlias() + "}", 3);
                    } else if (methodConfig.isModule()) {
                        action.updateArgs(methodConfig.getEUClassName(), 3);
                    }
                }
                action.setId(methodConfig.getMethodName() + "_" + action.getEventKey().getEvent() + "_" + action.getEventValue());
                this.addAction(action, true, eventEnum.getEventReturn());
            }
        }

    }


    public List<Action> fillActions(CustomMenu type) {
        List<Action> actions = new ArrayList<>();
        CustomAction[] actionTypes = type.actions();
        for (CustomAction actionType : actionTypes) {
            String exprossion = actionType.expression();
            try {
                if (exprossion != null && !exprossion.equals("") && EsbUtil.parExpression(exprossion, Boolean.class)) {
                    CustomConditionAction action = new CustomConditionAction(actionType, type, ToolBarEventEnum.onMenuBtnClick);
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


    List<GridMenu> getCustomGridType() {
        GridMenu[] types = GridMenu.values();
        List<GridMenu> menuTypes = new ArrayList<GridMenu>();
        List<ExcuteObj> tasks = new ArrayList<ExcuteObj>();
        for (GridMenu menuType : types) {
            ExcuteObj obj = new ExcuteObj(menuType.getExpression(), Boolean.class, menuType);
            tasks.add(obj);
        }
        List<ExcuteObj> results = EsbUtil.parExpression(tasks);
        for (ExcuteObj result : results) {
            if (Boolean.valueOf(result.getObj().toString())) {
                menuTypes.add((GridMenu) result.getSource());
            }
        }
        return menuTypes;
    }


    @JSONField(serialize = false)
    public MethodConfig findMethod(ToolBarEventBean toolBarEventBean) {
        String soruceClassName = toolBarEventBean.getSourceClassName();
        String methodName = toolBarEventBean.getMethodName();
        if (soruceClassName != null) {
            try {
                ApiClassConfig config = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(soruceClassName);
                if (config != null) {
                    MethodConfig methodConfig = config.getMethodByName(methodName);
                    if (methodConfig != null) {
                        return methodConfig;
                    }
                }
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public int compareTo(MenuDynBar o) {
        return o.getIndex() - index;
    }


    public List<APICallerComponent> getApis() {
        return apis;
    }

    public void setApis(List<APICallerComponent> apis) {
        this.apis = apis;
    }

}
