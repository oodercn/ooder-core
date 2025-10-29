package net.ooder.esd.custom.component;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.EsbBeanAnnotation;
import net.ooder.common.JDSConstants;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.CustomMenu;
import net.ooder.esd.annotation.event.ActionTypeEnum;
import net.ooder.esd.annotation.event.ListEventEnum;
import net.ooder.esd.annotation.menu.CustomMenuType;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.annotation.ui.SymbolType;
import net.ooder.esd.bean.ListMenuBean;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.bean.bar.DynBar;
import net.ooder.esd.custom.action.CustomConditionAction;
import net.ooder.esd.custom.action.ShowPageAction;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.component.ListComponent;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.Condition;
import net.ooder.esd.tool.properties.item.UIItem;
import net.ooder.esd.tool.properties.list.ListFieldProperties;
import net.ooder.esd.util.json.APICallSerialize;
import net.ooder.esd.util.json.CaseEnumsSerializer;
import net.ooder.jds.core.esb.EsbUtil;
import net.ooder.jds.core.esb.task.ExcuteObj;

import java.util.*;

@EsbBeanAnnotation
public class CustomListBar extends ListComponent implements DynBar<DynBar, TreeListItem> {

    private static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, CustomListBar.class);
    @JSONField(serialize = false)
    Map<String, TreeListItem> itemMap = new HashMap<>();

    public String id;
    @JSONField(serializeUsing = APICallSerialize.class)
    List<APICallerComponent> apis = new ArrayList<>();
    @JSONField(deserializeUsing = CaseEnumsSerializer.class)
    public CustomMenuType menuType = CustomMenuType.LISTMENU;


    public CustomListBar() {

    }

    public CustomListBar(String id) {
        super(id, new ListFieldProperties());
        this.id = id;
    }

    public CustomListBar(ListMenuBean menuBarBean) {
        super(menuBarBean.getId(), new ListFieldProperties(menuBarBean));
        this.id = menuBarBean.getId();

    }


    @Override
    public String getId() {
        return id;
    }


    @Override
    public List<TreeListItem> filter(Object rowData) {

        List<TreeListItem> menuTypes = new ArrayList<TreeListItem>();
        List<TreeListItem> items = this.getProperties().getItems();
        List<ExcuteObj> tasks = new ArrayList<ExcuteObj>();
        for (UIItem item : items) {
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

    }

    @Override
    public boolean initMenuClass(Class bindClazz) {

        return true;
    }


    public List<TreeListItem> addMenu(CustomMenu... types) {
        List<TreeListItem> treeListItems = new ArrayList<>();
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

    @Override
    public int compareTo(DynBar o) {
        return 0;
    }
}
