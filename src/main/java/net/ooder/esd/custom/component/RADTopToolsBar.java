package net.ooder.esd.custom.component;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSConstants;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.annotation.EsbBeanAnnotation;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.CustomMenu;
import net.ooder.esd.annotation.menu.CustomMenuType;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.bean.bar.MenuDynBar;
import net.ooder.esd.annotation.event.ActionTypeEnum;
import net.ooder.esd.annotation.event.ToolBarEventEnum;
import net.ooder.esd.annotation.menu.GridMenu;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.annotation.ui.SymbolType;
import net.ooder.esd.bean.ToolBarMenuBean;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.custom.action.CustomConditionAction;
import net.ooder.esd.custom.action.ShowPageAction;
import net.ooder.esd.util.json.CaseEnumsSerializer;
import net.ooder.esd.engine.enums.MenuBarBean;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.component.ToolBarComponent;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.Condition;
import net.ooder.esd.tool.properties.Event;
import net.ooder.esd.tool.properties.ToolBarProperties;

import net.ooder.jds.core.esb.EsbUtil;
import net.ooder.jds.core.esb.task.ExcuteObj;

import java.util.*;

@EsbBeanAnnotation
public class RADTopToolsBar extends ToolBarComponent implements MenuDynBar<MenuDynBar, TreeListItem> {

    private static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, RADTopToolsBar.class);
    @JSONField(serialize = false)
    Map<String, TreeListItem> itemMap = new HashMap<>();

    public String id;

    public String parentId;

    public String caption;

    public String imageClass;

    public Boolean showCaption = true;

    public Integer index = 100;

    List<APICallerComponent> apis = new ArrayList<>();
    @JSONField(deserializeUsing = CaseEnumsSerializer.class)
    public CustomMenuType menuType = CustomMenuType.TOOLBAR;

    public RADTopToolsBar(ToolBarMenuBean toolBarBean) {
        super(toolBarBean.getGroupId(), new ToolBarProperties(toolBarBean));

    }


    public RADTopToolsBar(String id) {
        super(id,  new ToolBarProperties());
        this.id = id;
    }

    public RADTopToolsBar(MenuBarBean menuBarBean) {
        super(menuBarBean.getId(), new ToolBarProperties(menuBarBean));
        this.id = menuBarBean.getId();
        this.parentId = menuBarBean.getParentId();
        this.index = menuBarBean.getIndex();
        this.caption = menuBarBean.getCaption();
        this.showCaption = menuBarBean.getShowCaption();
        this.imageClass = menuBarBean.getImageClass();
        this.menuType = menuBarBean.getMenuType();
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
        RADTopToolsBar topMenu = (RADTopToolsBar) bars;
        this.getProperties().addItem(topMenu.getGroup());
        Map<ToolBarEventEnum, Event> event = ((RADTopToolsBar) bars).getEvents();
        Set<Map.Entry<ToolBarEventEnum, Event>> entrySet = event.entrySet();
        for (Map.Entry<ToolBarEventEnum, Event> entry : entrySet) {
            List<Action> actions = entry.getValue().getActions();
            for (Action action : actions) {
                this.addAction(action);
            }
        }

        List<APICallerComponent> apiCallerComponents = ((RADTopToolsBar) bars).getApis();

        for (APICallerComponent apiCallerComponent : apiCallerComponents) {
            if (!apis.contains(apiCallerComponent)) {
                this.apis.add(apiCallerComponent);
            }

        }
    }


    public void addMenu(CustomMenu... types) {
        for (CustomMenu type : types) {
            if (!type.type().equals("")) {
                String menuId = type.type()+ "_" +  ComboInputType.button.name();
                TreeListItem menuItem = itemMap.get(menuId);
                String caption = type.caption();
                if (!showCaption) {
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
            if (!showCaption) {
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
            if (!showCaption) {
                caption = "";
            }

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
                        this.addAction( action);
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
                    this.getGroup().addChild(menuItem);
                    itemMap.put(menuId, menuItem);
                } else {
                    menuItem.setTips(component.getProperties().getTips());

                    if (!showCaption) {
                        caption = "";
                    }
                    menuItem.setCaption(caption);

                    if (expression != null && !expression.equals("")) {
                        menuItem.setExpression(expression);
                    }
                    if (!imageClass.equals("")) {
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
                        action.setDesc(action.getDesc().equals("") ? component.getProperties().getTips() : action.getDesc());
                        action.set_return(false);
                        Condition condition = new Condition("{args[1].id}", SymbolType.equal, menuId);
                        conditions.add(condition);
                        action.setConditions(conditions);
                        this.addAction(  action);
                    }
                } else {
                    if (!apis.contains(component)) {
                        this.apis.add(component);
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
                    this.addAction( action);


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
                    CustomConditionAction action = new CustomConditionAction(actionType, type,ToolBarEventEnum.onMenuBtnClick);
                    this.addAction( action);
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


    @Override
    public int compareTo(MenuDynBar o) {
        if (index == null) {
            return -1;
        }

        if (index != null && o.getIndex() != null) {
            return this.index - o.getIndex();
        }

        return 1;
    }


    public List<APICallerComponent> getApis() {
        return apis;
    }

    public void setApis(List<APICallerComponent> apis) {
        this.apis = apis;
    }

}
