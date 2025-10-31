package net.ooder.esd.custom.component;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.EsbBeanAnnotation;
import net.ooder.common.JDSConstants;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.CustomMenu;
import net.ooder.esd.annotation.event.ActionTypeEnum;
import net.ooder.esd.annotation.event.MenuEventEnum;
import net.ooder.esd.annotation.menu.CustomMenuType;
import net.ooder.esd.annotation.menu.GridMenu;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.annotation.ui.SymbolType;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.bean.bar.MenuDynBar;
import net.ooder.esd.custom.action.CustomConditionAction;
import net.ooder.esd.custom.action.ShowPageAction;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.engine.enums.MenuBarBean;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.component.MenuBarComponent;
import net.ooder.esd.tool.properties.*;
import net.ooder.esd.util.json.APICallSerialize;
import net.ooder.esd.util.json.CaseEnumsSerializer;
import net.ooder.jds.core.esb.EsbUtil;
import net.ooder.jds.core.esb.task.ExcuteObj;

import java.util.*;

@EsbBeanAnnotation
public class CustomMenusBar extends MenuBarComponent implements MenuDynBar<MenuDynBar, TreeListItem> {

    private static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, CustomMenusBar.class);

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

    @JSONField(deserializeUsing = CaseEnumsSerializer.class)
    public CustomMenuType menuType = CustomMenuType.MENUBAR;

    public CustomMenusBar() {

    }

    @Override
    public boolean initMenuClass(Class bindClazz) {

        return true;
    }

    public CustomMenusBar(String alias) {
        super(alias, new MenuBarProperties());
        this.id = alias;
        this.caption = alias;
    }

    public CustomMenusBar(EUModule euModule, MenuBarBean menuBarBean, String target, Object value) {
        super(menuBarBean.getId(), new MenuBarProperties(menuBarBean));
        this.id = menuBarBean.getId();
        this.parentId = menuBarBean.getParentId();
        this.index = menuBarBean.getIndex();
        this.showCaption = menuBarBean.getShowCaption();
        this.caption = menuBarBean.getCaption();
        this.imageClass = menuBarBean.getImageClass();
        this.menuType = menuBarBean.getMenuType();
    }

    public CustomMenusBar(MenuBarBean menuBarBean) {
        super(menuBarBean.getId(), new MenuBarProperties(menuBarBean));
        this.id = menuBarBean.getId();
        this.parentId = menuBarBean.getParentId();
        this.index = menuBarBean.getIndex();
        this.showCaption = menuBarBean.getShowCaption();
        this.caption = menuBarBean.getCaption();
        this.imageClass = menuBarBean.getImageClass();
        this.menuType = menuBarBean.getMenuType();
    }


    public CustomMenusBar(String id, String caption, String imageClass, Integer index) {
        super(id, new MenuBarProperties());
        this.id = alias;
        this.caption = caption;
        this.imageClass = imageClass;
    }


    @Override
    public List<TreeListItem> filter(Object rowData) {

        List<TreeListItem> menuTypes = new ArrayList<TreeListItem>();
        List<TreeListItem> items = this.getProperties().getItems();
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
    public void addSplit(String id) {
        this.getProperties().addItem(new TreeListItem(ComboInputType.split, id));
    }


    @Override
    public void addChild(MenuDynBar bars) {
        String caption = bars.getCaption();
        if (!showCaption) {
            caption = "";
        }
        TreeListItem menuItem = new TreeListItem(bars.getId(), caption, bars.getImageClass());
        CustomMenusBar topMenu = (CustomMenusBar) bars;
        List<TreeListItem> items = topMenu.getProperties().getItems();
        if (items != null) {
            for (TreeListItem childItem : items) {
                menuItem.addChild(childItem);
            }
            if (this.parentId != null && !this.parentId.equals("")) {
                TreeListItem item = new TreeListItem(ComboInputType.split, parentId + "spilit");
                menuItem.addChild(item);
            }

            this.getProperties().addItem(menuItem);
            Map<MenuEventEnum, Event> event = ((CustomMenusBar) bars).getEvents();
            Set<Map.Entry<MenuEventEnum, Event>> entrySet = event.entrySet();
            for (Map.Entry<MenuEventEnum, Event> entry : entrySet) {
                List<Action> actions = entry.getValue().getActions();
                for (Action action : actions) {
                    this.addAction(action);
                }
            }

            List<APICallerComponent> apiCallerComponents = ((CustomMenusBar) bars).getApis();
            for (APICallerComponent apiCallerComponent : apiCallerComponents) {
                if (!apis.contains(apiCallerComponent)) {
                    this.apis.add(apiCallerComponent);
                }
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
                if (menuItem == null) {
                    if (showCaption != null && !showCaption) {
                        caption = "";
                    }
                    menuItem = new TreeListItem(menuId, caption, type.imageClass(), type.caption(), type.itemType(), type.iconColor(), type.itemColor(), type.fontColor());
                    menuItem.setExpression(type.expression());
                    this.getProperties().addItem(menuItem);
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
        this.addComponentMenu(component, null, new ArrayList<>());
    }

    public void addMenu(String euModule, APICallerComponent component) {
        addModuleMenu(euModule, component, null, new ArrayList<>());
    }


    public APICallerComponent addModuleMenu(String className, APICallerComponent component, Map<String, Object> paramsMap, List<Condition> conditions) {
        if (conditions == null) {
            conditions = new ArrayList<>();
        }

        List<Condition> conditionList = new ArrayList<>();
        conditionList.addAll(conditions);
        String expression = component.getProperties().getExpression();
        if (expression == null || expression.equals("") || this.parExpression(expression)) {
            APICallerProperties apiCallerProperties = component.getProperties();
            String caption = apiCallerProperties.getDesc();
            String imageClass = apiCallerProperties.getImageClass();
            String menuId = component.getAlias() + "_" + ComboInputType.button.name();
            TreeListItem menuItem = itemMap.get(menuId);
            Set<CustomMenuItem> items = apiCallerProperties.getBindMenu();
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
                    menuItem = new TreeListItem(menuId, caption, imageClass, apiCallerProperties.getTips(), apiCallerProperties.getMenuType(), apiCallerProperties.getIconColor(), apiCallerProperties.getItemColor(), apiCallerProperties.getFontColor());
                    this.getProperties().addItem(menuItem);
                    itemMap.put(menuId, menuItem);
                } else {
                    menuItem.setCaption(caption);
                    String tips = component.getProperties().getTips();
                    if (tips == null || tips.equals("")) {
                        if (caption != null && !caption.equals("")) {
                            tips = caption;
                        } else {
                            tips = menuId;
                        }
                    }
                    menuItem.setTips(tips);
                    if (expression != null && !expression.equals("")) {
                        menuItem.setExpression(expression);
                    }
                    if (!imageClass.equals("")) {
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

                ShowPageAction action = new ShowPageAction(MenuEventEnum.onMenuSelected);
                action.setTarget(className);
                action.updateArgs("{args[1].tagVar}", 5);
                action.updateArgs("{args[1]}", 6);
                action.updateArgs("{args[0]}", 7);

                if (this.parentId != null && !this.parentId.equals("")) {
                    action.setDesc("打开" + component.getProperties().getDesc());
                    Condition condition = new Condition("{args[2].id}", SymbolType.equal, menuId);
                    conditionList.add(condition);
                    action.setConditions(conditionList);
                    action.set_return(false);
                    action.setEventKey(MenuEventEnum.onMenuSelected);
                    this.addAction(action);
                } else {
                    action.setDesc("打开" + component.getProperties().getDesc());
                    Condition condition = new Condition("{args[1].id}", SymbolType.equal, menuId);
                    conditionList.add(condition);
                    action.setConditions(conditionList);
                    action.set_return(false);
                    action.setEventKey(MenuEventEnum.onMenuBtnClick);
                    this.addAction(action);
                }


            }
        }
        return component;
    }


    public APICallerComponent addComponentMenu(APICallerComponent component, Map<String, Object> paramsMap, List<Condition> conditions) {
        if (conditions == null) {
            conditions = new ArrayList<>();
        }

        List<Condition> conditionList = new ArrayList<>();
        conditionList.addAll(conditions);
        String expression = component.getProperties().getExpression();


        if (expression == null || expression.equals("") || this.parExpression(expression)) {
            String caption = component.getProperties().getDesc();
            String imageClass = component.getProperties().getImageClass();
            Set<CustomMenuItem> items = component.getProperties().getBindMenu();
            if (items != null && items.size() > 0) {
                List<CustomMenu> customMenuList = new ArrayList<>();
                for (CustomMenuItem item : items) {
                    customMenuList.add(item.getMenu());
                }
                List<TreeListItem> listItem = this.addMenu(customMenuList.toArray(new CustomMenu[]{}));
                if (component.getActions() == null && component.getActions().isEmpty()) {
                    if (!apis.contains(component)) {
                        this.apis.add(component);
                    }
                } else {
                    for (TreeListItem item : listItem) {
                        for (Action action : component.getActions()) {
                            if (this.parentId != null && !this.parentId.equals("")) {
                                Condition condition = new Condition("{args[2].id}", SymbolType.equal, item.getId());
                                conditionList.add(condition);
                                action.setEventKey(MenuEventEnum.onMenuSelected);
                            } else {
                                Condition condition = new Condition("{args[1].id}", SymbolType.equal, item.getId());
                                conditionList.add(condition);
                                action.setEventKey(MenuEventEnum.onMenuBtnClick);
                            }
                        }
                    }
                }

            } else {
                String menuId = component.getAlias() + "_" + ComboInputType.button.name();
                TreeListItem menuItem = itemMap.get(menuId);
                if (menuItem == null) {
                    menuItem = new TreeListItem(menuId, caption, imageClass, component.getProperties().getTips(), component.getProperties().getMenuType());
                    this.getProperties().addItem(menuItem);
                    itemMap.put(menuId, menuItem);
                } else {
                    menuItem.setTips(component.getProperties().getTips());
                    if (caption != null && !caption.equals("")) {
                        menuItem.setCaption(caption);
                    }
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
                        if (this.parentId != null && !this.parentId.equals("")) {
                            Condition condition = new Condition("{args[2].id}", SymbolType.equal, menuId);
                            conditionList.add(condition);
                            action.setEventKey(MenuEventEnum.onMenuSelected);
                        } else {
                            Condition condition = new Condition("{args[1].id}", SymbolType.equal, menuId);
                            conditionList.add(condition);
                            action.setEventKey(MenuEventEnum.onMenuBtnClick);
                        }
                        action.setDesc(action.getDesc().equals("") ? caption : action.getDesc());
                        action.updateArgs("{page." + component.getAlias() + "}", 3);
                        action.setConditions(conditionList);
                        this.addAction(action);
                    }
                } else if (!apis.contains(component)) {
                    this.apis.add(component);
                    Action action = new Action(MenuEventEnum.onMenuSelected);
                    action.setArgs(Arrays.asList(new String[]{"{page." + component.getAlias() + ".invoke()}"}));
                    action.setType(ActionTypeEnum.control);
                    action.setTarget(component.getAlias());
                    action.setDesc(caption);
                    action.setMethod("invoke");
                    action.setRedirection("other:callback:call");
                    action.set_return(false);

                    if (this.parentId != null && !this.parentId.equals("")) {
                        Condition condition = new Condition("{args[2].id}", SymbolType.equal, menuId);
                        conditionList.add(condition);
                        action.setConditions(conditionList);
                        action.setEventKey(MenuEventEnum.onMenuSelected);
                        this.addAction(action);
                    } else {
                        Condition condition = new Condition("{args[1].id}", SymbolType.equal, menuId);
                        conditionList.add(condition);
                        action.setConditions(conditionList);
                        action.setEventKey(MenuEventEnum.onMenuBtnClick);
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


    public List<Action> fillActions(CustomMenu type) {
        List<Action> actions = new ArrayList<>();
        CustomAction[] actionTypes = type.actions();
        for (CustomAction actionType : actionTypes) {
            String exprossion = actionType.expression();
            try {
                if (exprossion != null && !exprossion.equals("") && EsbUtil.parExpression(exprossion, Boolean.class)) {
                    CustomConditionAction action = new CustomConditionAction(actionType, type, MenuEventEnum.onMenuBtnClick);
                    this.addAction(action);
                    actions.add(action);
                }
            } catch (Throwable e) {
                logger.error("expression[" + exprossion + "] par err[" + e.getMessage() + "]");
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
    public Integer getIndex() {
        return index;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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


    public Boolean getShowCaption() {
        return showCaption;
    }

    public void setShowCaption(Boolean showCaption) {
        this.showCaption = showCaption;
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
