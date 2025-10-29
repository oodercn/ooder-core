package net.ooder.esd.manager.editor;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSConstants;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.annotation.EsbBeanAnnotation;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.CustomMenu;
import net.ooder.esd.annotation.MenuBarMenu;
import net.ooder.esd.annotation.event.ActionTypeEnum;
import net.ooder.esd.annotation.event.MenuEventEnum;
import net.ooder.esd.annotation.menu.CustomMenuType;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.annotation.ui.SymbolType;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.bean.bar.MenuDynBar;
import net.ooder.esd.custom.action.CustomConditionAction;
import net.ooder.esd.custom.action.ShowPageAction;
import net.ooder.esd.engine.enums.MenuBarBean;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.component.MenuBarComponent;
import net.ooder.esd.tool.properties.*;
import net.ooder.esd.util.json.CaseEnumsSerializer;

import net.ooder.jds.core.esb.EsbUtil;
import net.ooder.jds.core.esb.task.ExcuteObj;

import java.util.*;

@EsbBeanAnnotation
public class RADTopMenu extends MenuBarComponent implements MenuDynBar<MenuDynBar, TreeListItem> {

    private static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, RADTopMenu.class);

    @JSONField(serialize = false)
    Map<String, TreeListItem> itemMap = new HashMap<>();


    @JSONField(deserializeUsing = CaseEnumsSerializer.class)
    CustomMenuType menuType = CustomMenuType.TOP;

    String id;

    String caption;

    String imageClass;

    String src;


    List<APICallerComponent> apis = new ArrayList<>();

    private Integer index = 100;

    public RADTopMenu() {

    }

    public RADTopMenu(String alias) {
        super(alias, new MenuBarProperties());
    }

    public RADTopMenu(MenuBarMenu topMenu) {
        super(topMenu.id(), new MenuBarProperties());
        this.id = topMenu.id();
        this.index = topMenu.index();
        this.caption = topMenu.caption();
        this.imageClass = topMenu.imageClass();
        this.menuType = topMenu.menuType();
    }


    public RADTopMenu(MenuBarBean topMenuBean) {
        super(topMenuBean.getId(), new PopMenuProperties());
        this.id = topMenuBean.getId();
        this.index = topMenuBean.getIndex();
        this.caption = topMenuBean.getCaption();
        this.imageClass = topMenuBean.getImageClass();
        this.menuType = topMenuBean.getMenuType();
    }


    public RADTopMenu(String id, String caption, String imageClass, Integer index) {
        super(id, new MenuBarProperties());
        this.id = id;
        this.index = index;
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
    public Integer getIndex() {
        return index;
    }

    @Override
    public void addSplit(String id) {
        this.getProperties().addItem(new TreeListItem(ComboInputType.split, id));
    }

    @Override
    public boolean initMenuClass(Class bindClazz) {

        return true;
    }

    @Override
    public void addChild(MenuDynBar bars) {
        TreeListItem menuItem = new TreeListItem(bars.getId(), bars.getCaption(), bars.getImageClass());
        RADTopMenu topMenu = (RADTopMenu) bars;
        List<TreeListItem> items = topMenu.getProperties().getItems();
        for (TreeListItem childItem : items) {
            menuItem.addChild(childItem);
        }

        TreeListItem item = new TreeListItem(ComboInputType.split, bars.getId() + "Split");
        menuItem.addChild(item);
        this.getProperties().addItem(menuItem);

        Map<MenuEventEnum, Event> event = ((RADTopMenu) bars).getEvents();
        Set<Map.Entry<MenuEventEnum, Event>> entrySet = event.entrySet();
        for (Map.Entry<MenuEventEnum, Event> entry : entrySet) {
            List<Action> actions = entry.getValue().getActions();
            for (Action action : actions) {
                this.addAction(action);
            }
        }

        List<APICallerComponent> apiCallerComponents = ((RADTopMenu) bars).getApis();

        for (APICallerComponent apiCallerComponent : apiCallerComponents) {
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
                if (menuItem == null) {
                    menuItem = new TreeListItem(menuId, type.caption(), type.imageClass());
                    this.getProperties().addItem(menuItem);
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
                    fillActions(type);
                }
            }
        }
        return treeListItems;

    }


    public void addMenu(String className, APICallerComponent component) {
        String expression = component.getProperties().getExpression();
        if (expression == null || expression.equals("") || parExpression(expression)) {
            String caption = component.getProperties().getDesc();
            String imageClass = component.getProperties().getImageClass();
            String menuId = component.getAlias() + ComboInputType.button.name();
            TreeListItem menuItem = itemMap.get(menuId);
            if (component.getProperties().getBindMenu() != null && component.getProperties().getBindMenu().size() > 0) {
                Set<CustomMenuItem> items = component.getProperties().getBindMenu();
                for (CustomMenuItem item : items) {
                    this.addMenu(item.getMenu());
                }

            } else {
                if (menuItem == null) {
                    menuItem = new TreeListItem(menuId, caption, imageClass);
                    this.getProperties().addItem(menuItem);
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
                ShowPageAction action = new ShowPageAction(MenuEventEnum.onMenuSelected);
                action.setTarget(className);
                Condition condition = new Condition("{args[2].id}", SymbolType.equal, menuId);
                List<Condition> conditions = new ArrayList<>();
                conditions.add(condition);
                action.setConditions(conditions);
                action.set_return(false);
                this.addAction(action);
            }
        }
    }


    public void addMenu(APICallerComponent component) {

        String expression = component.getProperties().getExpression();
        if (expression == null || expression.equals("") || parExpression(expression)) {

            String caption = component.getProperties().getDesc();
            String imageClass = component.getProperties().getImageClass();
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
                String menuId = component.getAlias() + ComboInputType.button.name();
                TreeListItem menuItem = itemMap.get(menuId);

                if (menuItem == null) {
                    menuItem = new TreeListItem(menuId, caption, imageClass);
                    this.getProperties().addItem(menuItem);
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

                Set<Action> actions = component.getActions();

                if (actions != null && actions.size() > 0) {
                    for (Action action : actions) {

                        List<Condition> conditions = new ArrayList<>();
                        Condition condition = new Condition("{args[1].id}", SymbolType.equal, menuId);
                        conditions.add(condition);
                        action.setDesc(action.getDesc().equals("") ? caption : action.getDesc());
                        action.setConditions(conditions);
                        action.set_return(false);
                        this.addAction(action);
                    }
                } else {
                    if (!apis.contains(component)) {
                        this.apis.add(component);
                    }
                    Action action = new Action(MenuEventEnum.onMenuSelected);
                    action.setArgs(Arrays.asList(new String[]{"{page." + component.getAlias() + ".invoke()}"}));
                    action.setType(ActionTypeEnum.control);
                    action.setTarget(component.getAlias());
                    action.setDesc(caption);
                    action.setMethod("invoke");
                    action.setRedirection("other:callback:call");
                    Condition condition = new Condition("{args[2].id}", SymbolType.equal, menuId);
                    List<Condition> conditions = new ArrayList<>();
                    conditions.add(condition);
                    action.setConditions(conditions);
                    action.set_return(false);
                    this.addAction(action);
                }
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
        CustomAction[] workListActionTypes = type.actions();
        for (CustomAction actionType : workListActionTypes) {
            try {
                if (EsbUtil.parExpression(actionType.expression(), Boolean.class)) {
                    CustomConditionAction action = new CustomConditionAction(actionType, type, MenuEventEnum.onMenuSelected);
                    this.addAction(action);
                }
            } catch (Throwable e) {
                e.printStackTrace();
                // log.error("expression err[" + actionType.getExpression() + "]");
            }
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }

    public List<APICallerComponent> getApis() {
        return apis;
    }

    public void setApis(List<APICallerComponent> apis) {
        this.apis = apis;
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
}
