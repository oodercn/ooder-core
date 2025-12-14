package net.ooder.esd.custom.component;


import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.EsbBeanAnnotation;
import net.ooder.common.JDSConstants;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.CustomMenu;
import net.ooder.esd.annotation.event.ActionTypeEnum;
import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.annotation.menu.CustomMenuType;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.BottomBarMenuBean;
import net.ooder.esd.bean.bar.DynBar;
import net.ooder.esd.custom.action.CustomConditionAction;
import net.ooder.esd.custom.action.ShowPageAction;
import net.ooder.esd.engine.enums.MenuBarBean;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.component.StatusButtonsComponent;
import net.ooder.esd.tool.properties.APICallerProperties;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.Condition;
import net.ooder.esd.tool.properties.form.StatusButtonsProperties;
import net.ooder.esd.tool.properties.item.CmdItem;
import net.ooder.esd.util.json.APICallSerialize;
import net.ooder.jds.core.esb.EsbUtil;
import net.ooder.jds.core.esb.task.ExcuteObj;

import java.util.*;

@EsbBeanAnnotation
public class CustomBottomBar extends StatusButtonsComponent implements DynBar<DynBar, CmdItem> {


    private static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, CustomBottomBar.class);

    @JSONField(serialize = false)
    Map<String, CmdItem> itemMap = new HashMap<>();

    @JSONField(serialize = false)
    public String id;
    @JSONField(serialize = false)
    BottomBarMenuBean menu;
    @JSONField(serializeUsing = APICallSerialize.class)
    List<APICallerComponent> apis = new ArrayList<>();

    public CustomMenuType menuType = CustomMenuType.BOTTOMBAR;

    public CustomBottomBar() {

    }

    public CustomBottomBar(MenuBarBean menuBarBean) {
        this.id = menuBarBean.getId();
        this.menuType = menuBarBean.getMenuType();

    }

    public CustomBottomBar(BottomBarMenuBean menuBarBean) {
        super(menuBarBean.getAlias(), new StatusButtonsProperties(menuBarBean));
        this.id = menuBarBean.getId();
        getProperties().setPosition(UIPositionType.STATIC);
        this.menuType = menuBarBean.getMenuType();

    }

    public CustomBottomBar(String alias, StatusButtonsProperties properties) {
        super(alias, properties);
    }

    public CustomBottomBar(String alias, BottomBarMenuBean bean) {
        super(alias, new StatusButtonsProperties(bean));
        this.menu = bean;
    }


    public CustomBottomBar(String alias) {
        super(alias, new StatusButtonsProperties());
        getProperties().setDock(Dock.center);
        getProperties().setPosition(UIPositionType.STATIC);
        getProperties().setWidth("auto");
        getProperties().setBorderType(BorderType.none);
        getProperties().setFormField(false);
    }


    @Override
    public String getId() {
        return id;
    }

    @Override
    public void addSplit(String id) {
        this.getProperties().addItem(new CmdItem(CmdButtonType.split, id));
    }


    @Override
    public List<CmdItem> filter(Object rowData) {
        List<CmdItem> menuTypes = new ArrayList<CmdItem>();
        List<CmdItem> items = this.getProperties().getItems();
        List<ExcuteObj> tasks = new ArrayList<ExcuteObj>();
        for (CmdItem item : items) {
            ExcuteObj obj = new ExcuteObj(item.getExpression(), Boolean.class, item);
            tasks.add(obj);
        }
        List<ExcuteObj> results = EsbUtil.parExpression(tasks);
        for (ExcuteObj result : results) {
            if (Boolean.valueOf(result.getObj().toString())) {
                menuTypes.add((CmdItem) result.getSource());
            }
        }
        return menuTypes;
    }


    public List<CmdItem> addMenu(CustomMenu... formTypes) {
        List<CmdItem> treeListItems = new ArrayList<>();
        for (CustomMenu type : formTypes) {
            if (!type.type().equals("")) {
                String menuId = type.type() + "_" + ComboInputType.button.name();
                CmdItem menuItem = itemMap.get(menuId);
                if (menuItem == null) {
                    menuItem = new CmdItem(type);
                    menuItem.setId(menuId);
                    menuItem.setItemType(this.getProperties().getItemType());
                    getProperties().addItem(menuItem);
                    itemMap.put(menuId, menuItem);
                } else {
                    if (!type.caption().equals("")) {
                        menuItem.setCaption(type.caption());
                    }
                    if (!type.expression().equals("")) {
                        menuItem.setExpression(type.expression());
                    }
                    if (!type.imageClass().equals("")) {
                        menuItem.setImageClass(type.imageClass());
                    }
                }
                if (!treeListItems.contains(menuItem)) {
                    treeListItems.add(menuItem);
                }
                fillActions(type);
            }
        }
        return treeListItems;
    }

    public void addComponentMenu(APICallerComponent component, Map<String, Object> paramsMap) {
        addComponentMenu(component, paramsMap, new ArrayList<>());
    }

    public void addComponentMenu(APICallerComponent component, Map<String, Object> paramsMap, List<Condition> conditions) {
        String expression = component.getProperties().getExpression();
        if (conditions == null) {
            conditions = new ArrayList<>();
        }
        List<Condition> conditionList = new ArrayList<>();
        conditionList.addAll(conditions);
        if (expression == null || expression.equals("") || parExpression(expression)) {
            if (!apis.contains(component)) {
                this.apis.add(component);
                APICallerProperties properties = component.getProperties();
                String caption = properties.getDesc();
                String imageClass = properties.getImageClass();
                String menuId = component.getAlias() + ComboInputType.button.name();
                CmdItem menuItem = itemMap.get(menuId);
                if (properties.getBindMenu() != null && properties.getBindMenu().size() > 0) {
                    Set<CustomMenuItem> items = properties.getBindMenu();
                    for (CustomMenuItem item : items) {
                        this.addMenu(item.getMenu());
                    }
                } else {
                    if (menuItem == null) {
                        menuItem = new CmdItem(menuId, caption, imageClass, properties.getIconColor(), properties.getItemColor(), properties.getFontColor());
                        menuItem.setItemType(this.getProperties().getItemType());
                        this.getProperties().addItem(menuItem);
                        itemMap.put(menuId, menuItem);
                    } else {
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
                        if (menuItem.getTagVar() == null) {
                            menuItem.setTagVar(paramsMap);
                        } else {
                            menuItem.getTagVar().putAll(paramsMap);
                        }
                    }
                    Set<Action> actions = component.getActions();
                    if (actions != null && actions.size() > 0) {
                        for (Action action : actions) {
                            Condition condition = new Condition("{args[1].id}", SymbolType.equal, menuId);
                            conditionList.add(condition);
                            action.setDesc(action.getDesc().equals("") ? caption : action.getDesc());
                            action.setConditions(conditionList);
                            action.set_return(false);
                            this.addAction(action);
                        }
                    } else {
                        Action action = new Action(FieldEventEnum.onClick);
                        action.setArgs(Arrays.asList(new String[]{"{page." + component.getAlias() + ".invoke()}"}));
                        action.setType(ActionTypeEnum.control);
                        action.setTarget(component.getAlias());
                        action.setDesc(caption);
                        action.setMethod("invoke");
                        action.setRedirection("other:callback:call");
                        Condition condition = new Condition("{args[1].id}", SymbolType.equal, menuId);
                        conditionList.add(condition);
                        action.setConditions(conditionList);
                        action.set_return(false);
                        this.addAction(action);
                    }
                }
            }

        }
    }


    public void addMenu(APICallerComponent component) {
        addComponentMenu(component, null);
    }

    public void addMenu(String className, APICallerComponent component) {
        addModuleMenu(className, component, null);
    }

    @Override
    public boolean initMenuClass(Class bindClazz) {

        return true;
    }

    public Map<String, CmdItem> getItemMap() {
        return itemMap;
    }

    public void setItemMap(Map<String, CmdItem> itemMap) {
        this.itemMap = itemMap;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BottomBarMenuBean getMenu() {
        return menu;
    }

    public void setMenu(BottomBarMenuBean menu) {
        this.menu = menu;
    }

    public void addModuleMenu(String className, APICallerComponent component, Map<String, Object> paramsMap) {
        addModuleMenu(className, component, paramsMap, new ArrayList<>());
    }

    public void addModuleMenu(String className, APICallerComponent component, Map<String, Object> paramsMap, List<Condition> conditions) {
        String expression = component.getProperties().getExpression();

        if (conditions == null) {
            conditions = new ArrayList<>();
        }

        List<Condition> conditionList = new ArrayList<>();
        conditionList.addAll(conditions);

        if (expression == null || expression.equals("") || parExpression(expression)) {
            String caption = component.getProperties().getDesc();
            String imageClass = component.getProperties().getImageClass();
            String menuId = component.getAlias() + ComboInputType.button.name();
            CmdItem menuItem = itemMap.get(menuId);
            if (component.getProperties().getBindMenu() != null && component.getProperties().getBindMenu().size() > 0) {
                Set<CustomMenuItem> items = component.getProperties().getBindMenu();
                for (CustomMenuItem item : items) {
                    this.addMenu(item.getMenu());
                }
                if (!apis.contains(component)) {
                    this.apis.add(component);
                }

            } else {
                if (menuItem == null) {
                    menuItem = new CmdItem(menuId, caption, imageClass);
                    menuItem.setItemType(getProperties().getItemType());
                    getProperties().addItem(menuItem);
                    itemMap.put(menuId, menuItem);
                } else {
                    if (!caption.equals("")) {
                        menuItem.setCaption(caption);
                    }
                    if (!expression.equals("")) {
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
                ShowPageAction action = new ShowPageAction(FieldEventEnum.onClick);
                action.setTarget(className);
                action.updateArgs("{args[1].tagVar}", 5);
                Condition condition = new Condition("{args[1].id}", SymbolType.equal, menuId);
                conditionList.add(condition);
                action.setConditions(conditionList);
                action.set_return(false);
                this.addAction(action);
            }
        }
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

    public void fillActions(CustomMenu type) {
        CustomAction[] formActionTypes = type.actions();
        for (CustomAction actionType : formActionTypes) {
            try {
                if (EsbUtil.parExpression(actionType.expression(), Boolean.class)) {
                    CustomConditionAction action = new CustomConditionAction(actionType, type, FieldEventEnum.onClick);
                    this.addAction(action);
                }
            } catch (Throwable e) {
                e.printStackTrace();
                // log.error("expression err[" + actionType.getExpression() + "]");
            }
        }
    }


    public List<APICallerComponent> getApis() {
        return apis;
    }

    public void setApis(List<APICallerComponent> apis) {
        this.apis = apis;
    }

    public CustomMenuType getMenuType() {
        return menuType;
    }

    public void setMenuType(CustomMenuType menuType) {
        this.menuType = menuType;
    }

    @Override
    public int compareTo(DynBar o) {
        return 0;
    }

}

