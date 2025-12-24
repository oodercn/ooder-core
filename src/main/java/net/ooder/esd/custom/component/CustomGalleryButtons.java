package net.ooder.esd.custom.component;


import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSConstants;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.CustomMenu;
import net.ooder.esd.annotation.event.ActionTypeEnum;
import net.ooder.esd.annotation.event.GalleryEventEnum;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.bar.DynBar;
import net.ooder.esd.bean.field.CustomGalleryFieldBean;
import net.ooder.esd.custom.action.CustomConditionAction;
import net.ooder.esd.custom.action.ShowPageAction;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.component.GalleryComponent;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.Condition;
import net.ooder.esd.tool.properties.GalleryProperties;
import net.ooder.esd.tool.properties.item.GalleryItem;
import net.ooder.jds.core.esb.EsbUtil;
import net.ooder.jds.core.esb.task.ExcuteObj;

import java.util.*;


public class CustomGalleryButtons extends GalleryComponent implements DynBar<DynBar, GalleryItem> {


    private static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, CustomToolsBar.class);

    @JSONField(serialize = false)
    Integer width = 6;

    @JSONField(serialize = false)
    Map<String, GalleryItem> itemMap = new HashMap<>();

    @JSONField(serialize = false)
    String id;


    @JSONField(serialize = false)
    CustomGalleryFieldBean menu;

    public CustomGalleryButtons(String alias, GalleryProperties properties) {
        super(alias, properties);
    }

    public CustomGalleryButtons(String alias, CustomGalleryFieldBean bean) {
        super(alias, new GalleryProperties(bean.getViewBean()));
    }


    public CustomGalleryButtons(String alias) {
        super(alias, new GalleryProperties());
        getProperties().setDock(Dock.center);
        getProperties().setItemWidth(width + "em");
        getProperties().setBorderType(BorderType.none);
        getProperties().setFormField(false);
    }


    @Override
    public String getId() {
        return id;
    }

    @Override
    public void addSplit(String id) {
        //   this.getProperties().addItem(new GalleryItem(CmdButtonType.split, id));
    }


    @Override
    public List<GalleryItem> filter(Object rowData) {

        List<GalleryItem> menuTypes = new ArrayList<GalleryItem>();
        List<GalleryItem> items = (List<GalleryItem>) this.getProperties().getItems();
        List<ExcuteObj> tasks = new ArrayList<ExcuteObj>();
        for (GalleryItem item : items) {
            ExcuteObj obj = new ExcuteObj(item.getExpression(), Boolean.class, item);
            tasks.add(obj);
        }
        List<ExcuteObj> results = EsbUtil.parExpression(tasks);
        for (ExcuteObj result : results) {
            if (Boolean.valueOf(result.getObj().toString())) {
                menuTypes.add((GalleryItem) result.getSource());
            }
        }
        return menuTypes;
    }


    public List<GalleryItem> addMenu(CustomMenu... formTypes) {
        List<GalleryItem> treeListItems = new ArrayList<>();
        for (CustomMenu type : formTypes) {
            if (!type.type().equals("")) {
                String menuId = type.type() + "_" + ComboInputType.button.name();
                GalleryItem menuItem = itemMap.get(menuId);
                if (menuItem == null) {
                    menuItem = new GalleryItem(menuId, type.caption(), type.imageClass());
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
                    fillActions(type);
                }

            }
        }
        this.getProperties().setWidth(((width + 1.5) * itemMap.size()) + "em");
        return treeListItems;
    }


    public void addComponentMenu(APICallerComponent component, Map<String, Object> paramsMap) {
        String expression = component.getProperties().getExpression();
        if (expression == null || expression.equals("") || parExpression(expression)) {
            String caption = component.getProperties().getDesc();
            String imageClass = component.getProperties().getImageClass();
            String menuId = component.getAlias() + ComboInputType.button.name();
            GalleryItem menuItem = itemMap.get(menuId);
            if (component.getProperties().getBindMenu() != null && component.getProperties().getBindMenu().size() > 0) {
                Set<CustomMenuItem> items = component.getProperties().getBindMenu();
                for (CustomMenuItem item : items) {
                    this.addMenu(item.getMenu());
                }
            } else {
                if (menuItem == null) {
                    menuItem = new GalleryItem(menuId, caption, imageClass);
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
                        List<Condition> conditions = new ArrayList<>();
                        Condition condition = new Condition("{args[1].id}", SymbolType.equal, menuId);
                        conditions.add(condition);
                        action.setDesc(action.getDesc().equals("") ? caption : action.getDesc());
                        action.setConditions(conditions);
                        action.set_return(false);
                        this.addAction(action);
                    }
                } else {
                    Action action = new Action(GalleryEventEnum.onClick);
                    action.setArgs(Arrays.asList(new String[]{"{page." + component.getAlias() + ".invoke()}"}));
                    action.setType(ActionTypeEnum.control);
                    action.setTarget(component.getAlias());
                    action.setDesc(caption);
                    action.setMethod("invoke");
                    action.setRedirection("other:callback:call");
                    Condition condition = new Condition("{args[1].id}", SymbolType.equal, menuId);
                    List<Condition> conditions = new ArrayList<>();
                    conditions.add(condition);
                    action.setConditions(conditions);
                    action.set_return(false);
                    this.addAction(action);
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

    public void addModuleMenu(String className, APICallerComponent component, Map<String, Object> paramsMap) {
        String expression = component.getProperties().getExpression();
        if (expression == null || expression.equals("") || parExpression(expression)) {

            String caption = component.getProperties().getDesc();
            String imageClass = component.getProperties().getImageClass();
            String menuId = component.getAlias() + ComboInputType.button.name();
            GalleryItem menuItem = itemMap.get(menuId);
            if (component.getProperties().getBindMenu() != null && component.getProperties().getBindMenu().size() > 0) {
                Set<CustomMenuItem> items = component.getProperties().getBindMenu();
                for (CustomMenuItem item : items) {
                    this.addMenu(item.getMenu());
                }

            } else {
                if (menuItem == null) {
                    menuItem = new GalleryItem(menuId, caption, imageClass);

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

                ShowPageAction action = new ShowPageAction(GalleryEventEnum.onClick);
                action.setTarget(className);
                action.updateArgs("{args[1].tagVar}", 5);
                Condition condition = new Condition("{args[1].id}", SymbolType.equal, menuId);
                List<Condition> conditions = new ArrayList<>();
                conditions.add(condition);
                action.setConditions(conditions);
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
                    CustomConditionAction action = new CustomConditionAction(actionType, type, GalleryEventEnum.onClick);
                    this.addAction(action);
                }
            } catch (Throwable e) {
                e.printStackTrace();
                // log.error("expression err[" + actionType.getExpression() + "]");
            }
        }
    }


}

