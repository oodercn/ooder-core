package net.ooder.esd.custom.component;

import com.alibaba.fastjson.annotation.JSONField;
import javassist.NotFoundException;
import net.ooder.annotation.EsbBeanAnnotation;
import net.ooder.common.EventKey;
import net.ooder.common.JDSConstants;
import net.ooder.common.JDSException;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.CustomMenu;
import net.ooder.esd.annotation.MenuBarMenu;
import net.ooder.esd.annotation.action.CustomGlobalMethod;
import net.ooder.esd.annotation.action.CustomTreeAction;
import net.ooder.esd.annotation.action.CustomTreeMethod;
import net.ooder.esd.annotation.event.*;
import net.ooder.esd.annotation.menu.CustomMenuType;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.annotation.ui.SymbolType;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.RightContextMenuBean;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.bean.bar.PopDynBar;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.custom.action.*;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.enums.MenuBarBean;
import net.ooder.esd.manager.editor.MenuActionService;
import net.ooder.esd.tool.component.*;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.Condition;
import net.ooder.esd.tool.properties.Event;
import net.ooder.esd.tool.properties.PopMenuProperties;
import net.ooder.esd.util.json.APICallSerialize;
import net.ooder.esd.util.json.CaseEnumsSerializer;
import net.ooder.jds.core.esb.EsbUtil;
import net.ooder.jds.core.esb.task.ExcuteObj;

import java.util.*;

@EsbBeanAnnotation
public class CustomContextBar<T extends PopMenuProperties, K extends PopMenuEventEnum> extends PopMenuComponent<T, K> implements PopDynBar<PopDynBar, TreeListItem> {

    private static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, CustomContextBar.class);

    @JSONField(serialize = false)
    Map<String, TreeListItem> itemMap = new HashMap<>();

    Map<String, Object> tagVar = new HashMap<>();
    @JSONField(deserializeUsing = CaseEnumsSerializer.class)
    CustomMenuType menuType = CustomMenuType.CONTEXTMENU;

    Boolean dyn = false;

    String iconFontSize;

    String itemClass;

    String itemStyle;


    String id;

    EventPos pos;

    Class serviceClass = MenuActionService.class;

    String caption;

    Boolean showCaption;

    String imageClass;

    @JSONField(serializeUsing = APICallSerialize.class)
    List<APICallerComponent> apis = new ArrayList<>();


    private Integer index = 100;

    public CustomContextBar() {

    }


    public CustomContextBar(RightContextMenuBean contextMenuBean) {
        super(contextMenuBean.getId(), (T) new PopMenuProperties(contextMenuBean));
        this.id = contextMenuBean.getId();

        if (contextMenuBean.getServiceClass() != null && !contextMenuBean.getServiceClass().equals(Void.class)) {
            this.serviceClass = contextMenuBean.getServiceClass();
        }

        this.menuType = CustomMenuType.CONTEXTMENU;
        this.iconFontSize = contextMenuBean.getIconFontSize();
        this.itemClass = contextMenuBean.getItemClass();
        this.itemStyle = contextMenuBean.getItemStyle();

    }


    public CustomContextBar(String alias) {
        super(alias, (T) new PopMenuProperties());
    }

    CustomContextBar(MenuBarMenu customMenuBar) {
        super(customMenuBar.id(), (T) new PopMenuProperties());
        this.id = customMenuBar.id();
        this.index = customMenuBar.index();
        this.caption = customMenuBar.caption();
        this.imageClass = customMenuBar.imageClass();
        this.showCaption = customMenuBar.showCaption();
        this.menuType = customMenuBar.menuType();

        if (customMenuBar.serviceClass() != null && !customMenuBar.serviceClass().equals(Void.class)) {
            this.serviceClass = customMenuBar.serviceClass();
        }

    }

    void init(MenuBarBean topMenuBean) {
        this.id = topMenuBean.getId();
        this.index = topMenuBean.getIndex();
        this.showCaption = topMenuBean.getShowCaption();
        this.caption = topMenuBean.getCaption();
        this.imageClass = topMenuBean.getImageClass();
        this.menuType = topMenuBean.getMenuType();
        if (topMenuBean.getServiceClass() != null && !topMenuBean.getServiceClass().equals(Void.class)) {
            this.serviceClass = topMenuBean.getServiceClass();
        }


    }

    public CustomContextBar(MenuBarBean topMenuBean) {
        super(topMenuBean.getId(), (T) new PopMenuProperties());
        init(topMenuBean);
    }

    @Override
    public boolean initMenuClass(Class bindClazz) {

        return true;
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

    public void bindFieldAction(FieldComponent fieldComponent, ModuleComponent moduleComponent, FieldFormConfig field, EventKey eventKey) throws NotFoundException, JDSException {
        ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(this.getServiceClass().getName());
        if (apiClassConfig != null) {
            MethodConfig methodConfig = apiClassConfig.getFieldEvent(CustomFieldEvent.LOADMENU);
            APICallerComponent component = new APICallerComponent(methodConfig);// methodConfig.getApi();
            if (component != null) {
                component.setAlias(this.getId() + "_" + CustomTreeAction.LOADMENU.name());
                moduleComponent.addChildren(component);
                component.getProperties().addQuaryArgs("viewBarId", this.getId());
                if (!this.isDyn()) {
                    moduleComponent.getCustomFunctions().put("checkMenu", "function(viewBarId,pos,item){this.ViewMenuBar[viewBarId].setTagVar(item.tagVar);this.ViewMenuBar[viewBarId].pop(pos);}");
                    Action action = new Action(eventKey);
                    action.setDesc("判断缓存菜单");
                    action.setMethod(CustomGlobalMethod.call.getType());
                    action.setType(ActionTypeEnum.other);
                    action.setTarget("callback");
                    List<String> args = new ArrayList<>();
                    args.add("{page.checkMenu()}");
                    args.add(null);
                    args.add(null);
                    args.add(this.getId());
                    args.add("{args[4]}");
                    args.add("{args[3]}");
                    action.setArgs(args);
                    action.set_return(false);
                    Condition condition = new Condition("{page.ViewMenuBar}", SymbolType.objhaskey, this.getId());
                    action.addCondition(condition);
                    fieldComponent.addAction(action);
                }

                Condition baseCondition = new Condition("{args[3]}", SymbolType.nonempty, "");
                Action setParamsAction = new SetTagVarQueryDataAction(component.getAlias(), eventKey);

                fieldComponent.addAction(setParamsAction);
                SetCustomQueryDataAction dataAction = new SetCustomQueryDataAction(component.getAlias(), "pos", "{args[4]}", eventKey);

                fieldComponent.addAction(dataAction);

                Action callAction = new CustomAPICallAction(component, eventKey);
                //callAction.addCondition(baseCondition);
                fieldComponent.addAction(callAction);
                Event event = (Event) fieldComponent.getEvents().get(eventKey);
                event.setEventReturn("{false}");
            } else {
                logger.warn(" input contextmenu err in  class[" + field.getViewClassName() + "][" + field.getFieldname() + "]");
            }

        } else {
            logger.warn(" input contextmenu err in  class[" + field.getViewClassName() + "][" + field.getFieldname() + "]");
        }


    }

    public void bindModuleAction(Component currComponent, ModuleComponent moduleComponent, Set<ComponentType> bindTypes, List<Condition> otherConditions) throws NotFoundException, JDSException {
        ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(this.getServiceClass().getName());

        if (apiClassConfig != null && apiClassConfig.getTreeEvent(CustomTreeEvent.LOADMENU) != null) {
            MethodConfig methodConfig = apiClassConfig.getTreeEvent(CustomTreeEvent.LOADMENU);
            APICallerComponent component = new APICallerComponent(methodConfig);// methodConfig.getApi();
            if (component != null) {
                component.setAlias(this.getId() + "_" + CustomTreeAction.LOADMENU.name());
                moduleComponent.addChildren(component);
                if (component != null) {
                    component.getProperties().addQuaryArgs("viewBarId", this.getId());
                    if (!this.isDyn()) {
                        moduleComponent.getCustomFunctions().put("checkMenu", "function(viewBarId,pos,item){this.ViewMenuBar[viewBarId].setTagVar(item.tagVar);this.ViewMenuBar[viewBarId].pop(pos);}");
                        Action action = new Action(TreeViewEventEnum.onContextmenu);
                        action.setDesc("判断缓存菜单");
                        action.setMethod(CustomGlobalMethod.call.getType());
                        action.setType(ActionTypeEnum.other);
                        action.setTarget("callback");
                        List<String> args = new ArrayList<>();
                        args.add("{page.checkMenu()}");
                        args.add(null);
                        args.add(null);
                        args.add(this.getId());
                        args.add("{args[4]}");
                        args.add("{args[3]}");
                        action.setArgs(args);
                        action.set_return(false);
                        Condition condition = new Condition("{page.ViewMenuBar}", SymbolType.objhaskey, this.getId());
                        action.addCondition(condition);
                        if (otherConditions != null && !otherConditions.isEmpty()) {
                            for (Condition otherCondition : otherConditions) {
                                action.addCondition(otherCondition);
                            }
                        }
                        currComponent.addAction(action);
                    }
                    Action selectAction = new Action(TreeViewEventEnum.onContextmenu);
                    selectAction.setDesc("选中当前节点");
                    selectAction.setMethod(CustomTreeMethod.setValue.getType());
                    selectAction.setType(ActionTypeEnum.control);
                    selectAction.setTarget(currComponent.getAlias());
                    List<String> args = new ArrayList<>();
                    args.add("{args[3].id}");
                    selectAction.setArgs(args);

                    if (otherConditions != null && !otherConditions.isEmpty()) {
                        for (Condition otherCondition : otherConditions) {
                            selectAction.addCondition(otherCondition);
                        }
                    }

                    currComponent.addAction(selectAction);
                    Condition baseCondition = new Condition("{args[3]}", SymbolType.nonempty, "");
                    Action setParamsAction = new SetTagVarQueryDataAction(component.getAlias(), TreeViewEventEnum.onContextmenu);

                    if (otherConditions != null && !otherConditions.isEmpty()) {
                        for (Condition otherCondition : otherConditions) {
                            setParamsAction.addCondition(otherCondition);
                        }
                    }

                    setParamsAction.addCondition(baseCondition);
                    currComponent.addAction(setParamsAction);
                    SetCustomQueryDataAction dataAction = new SetCustomQueryDataAction(component.getAlias(), "pos", "{args[4]}", TreeViewEventEnum.onContextmenu);
                    dataAction.addCondition(baseCondition);
                    if (otherConditions != null && !otherConditions.isEmpty()) {
                        for (Condition otherCondition : otherConditions) {
                            dataAction.addCondition(otherCondition);
                        }
                    }

                    currComponent.addAction(dataAction);

                    Action callAction = new CustomAPICallAction(component, TreeViewEventEnum.onContextmenu);
                    callAction.addCondition(baseCondition);
                    if (otherConditions != null && !otherConditions.isEmpty()) {
                        for (Condition otherCondition : otherConditions) {
                            callAction.addCondition(otherCondition);
                        }
                    }
                    currComponent.addAction(callAction);
                    Event event = (Event) currComponent.getEvents().get(TreeViewEventEnum.onContextmenu);
                    event.setEventReturn("{false}");
                }
            }

        }
    }


    public void bindOptAction(Component currComponent, ModuleComponent moduleComponent, Set<ComponentType> bindTypes, List<Condition> otherConditions) throws JDSException {
        ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(this.getServiceClass().getName());

        if (apiClassConfig != null && apiClassConfig.getTreeEvent(CustomTreeEvent.LOADMENU) != null) {
            MethodConfig methodConfig = apiClassConfig.getTreeEvent(CustomTreeEvent.LOADMENU);
            APICallerComponent component = new APICallerComponent(methodConfig);// methodConfig.getApi();
            if (component != null) {
                component.setAlias(this.getId() + "_" + CustomTreeAction.LOADMENU.name());
                moduleComponent.addChildren(component);
                if (component != null) {
                    component.getProperties().addQuaryArgs("viewBarId", this.getId());
                    if (!this.isDyn()) {
                        moduleComponent.getCustomFunctions().put("checkOptMenu", "function(viewBarId,e,item){this.ViewMenuBar[viewBarId].setTagVar(item.tagVar);this.ViewMenuBar[viewBarId].pop(ood.Event.getPos(e));}");
                        Action action = new Action(TreeViewEventEnum.onContextmenu);
                        action.setDesc("判断缓存菜单");
                        action.setMethod(CustomGlobalMethod.call.getType());
                        action.setType(ActionTypeEnum.other);
                        action.setTarget("callback");
                        List<String> args = new ArrayList<>();
                        args.add("{page.checkOptMenu()}");
                        args.add(null);
                        args.add(null);
                        args.add(this.getId());
                        args.add("{args[2]}");
                        args.add("{args[1]}");
                        action.setArgs(args);
                        action.set_return(false);
                        Condition condition = new Condition("{page.ViewMenuBar}", SymbolType.objhaskey, this.getId());
                        action.addCondition(condition);
                        if (otherConditions != null && !otherConditions.isEmpty()) {
                            for (Condition otherCondition : otherConditions) {
                                action.addCondition(otherCondition);
                            }
                        }
                        currComponent.addAction(action);
                    }
                    Action selectAction = new Action(TreeViewEventEnum.onShowOptions);
                    selectAction.setDesc("选中当前节点");
                    selectAction.setMethod(CustomTreeMethod.setValue.getType());
                    selectAction.setType(ActionTypeEnum.control);
                    selectAction.setTarget(currComponent.getAlias());
                    List<String> args = new ArrayList<>();
                    args.add("{args[1].id}");
                    selectAction.setArgs(args);

                    if (otherConditions != null && !otherConditions.isEmpty()) {
                        for (Condition otherCondition : otherConditions) {
                            selectAction.addCondition(otherCondition);
                        }
                    }

                    currComponent.addAction(selectAction);
                    Condition baseCondition = new Condition("{args[1]}", SymbolType.nonempty, "");
                    Action setParamsAction = new SetTagVarQueryDataAction(component.getAlias(), "{args[1].tagVar}", TreeViewEventEnum.onShowOptions);

                    if (otherConditions != null && !otherConditions.isEmpty()) {
                        for (Condition otherCondition : otherConditions) {
                            setParamsAction.addCondition(otherCondition);
                        }
                    }

                    setParamsAction.addCondition(baseCondition);
                    currComponent.addAction(setParamsAction);
                    SetCustomQueryDataAction dataAction = new SetCustomQueryDataAction(component.getAlias(), "pos", "{args[2]}", TreeViewEventEnum.onShowOptions);
                    dataAction.addCondition(baseCondition);
                    if (otherConditions != null && !otherConditions.isEmpty()) {
                        for (Condition otherCondition : otherConditions) {
                            dataAction.addCondition(otherCondition);
                        }
                    }

                    currComponent.addAction(dataAction);

                    Action callAction = new CustomAPICallAction(component, TreeViewEventEnum.onShowOptions);
                    callAction.addCondition(baseCondition);
                    if (otherConditions != null && !otherConditions.isEmpty()) {
                        for (Condition otherCondition : otherConditions) {
                            callAction.addCondition(otherCondition);
                        }
                    }
                    currComponent.addAction(callAction);
                    Event event = (Event) currComponent.getEvents().get(TreeViewEventEnum.onShowOptions);
                    event.setEventReturn("{false}");
                }
            }

        }
    }


    @Override
    public void addChild(PopDynBar bars) {
        if (!bars.getId().equals(this.getId())) {
            TreeListItem menuItem = createListItem(bars.getId(), bars.getCaption(), bars.getImageClass());
            CustomContextBar<PopMenuProperties, PopMenuEventEnum> topMenu = (CustomContextBar) bars;
            List<TreeListItem> items = topMenu.getProperties().getItems();
            if (items != null) {
                for (TreeListItem childItem : items) {
                    menuItem.addChild(childItem);
                }
                this.getProperties().addItem(menuItem);
                Map<K, Event> event = ((CustomContextBar) bars).getEvents();
                Set<Map.Entry<K, Event>> entrySet = event.entrySet();
                for (Map.Entry<K, Event> entry : entrySet) {
                    List<Action> actions = entry.getValue().getActions();
                    for (Action action : actions) {
                        this.addAction(action);
                    }
                }
                List<APICallerComponent> barApis = bars.getApis();
                for (APICallerComponent barapi : barApis) {
                    if (!apis.contains(barapi)) {
                        this.apis.add(barapi);
                    }
                }
            }
        }
    }


    TreeListItem createListItem(String menuId, String caption, String imageClass) {
        TreeListItem menuItem = new TreeListItem(menuId, caption, imageClass);
        menuItem.setItemClass(itemClass);
        menuItem.setItemStyle(itemStyle);
        return menuItem;
    }

    public void addMenu(CustomMenu... types) {
        for (CustomMenu type : types) {
            if (!type.type().equals("")) {
                String menuId = type.type() + "_" + ComboInputType.button.name();
                TreeListItem menuItem = itemMap.get(menuId);
                if (menuItem == null) {
                    String caption = type.caption();
                    menuItem = createListItem(menuId, caption, type.imageClass());
                    this.getProperties().addItem(menuItem);
                    itemMap.put(menuId, menuItem);
                } else {
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


    public void addMenu(String className, APICallerComponent component) {
        String expression = component.getProperties().getExpression();
        if (expression == null || expression.equals("") || parExpression(expression)) {

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
                    menuItem = createListItem(menuId, caption, imageClass);
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
                ShowPageAction action = new ShowPageAction(PopMenuEventEnum.onMenuSelected);
                action.setTarget(className);
                Condition condition = new Condition("{args[1].id}", SymbolType.equal, menuId);
                List<Condition> conditions = new ArrayList<>();
                conditions.add(condition);
                action.setConditions(conditions);
                action.updateArgs("{args[0].tagVar}", 5);
                action.updateArgs("{args[0]}", 6);
                action.updateArgs("{args[1]}", 7);
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
            if (items != null && items.size() > 0) {
                for (CustomMenuItem item : items) {
                    this.addMenu(item.getMenu());
                }
                if (!apis.contains(component)) {
                    this.apis.add(component);
                }

            } else {
                String menuId = component.getAlias() + ComboInputType.button.name();
                TreeListItem menuItem = itemMap.get(menuId);
                if (menuItem == null) {
                    menuItem = createListItem(menuId, caption, imageClass);
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

                if (!apis.contains(component)) {
                    this.apis.add(component);
                    Set<Action> actions = component.getActions();
                    if (actions != null && actions.size() > 0) {
                        for (Action action : actions) {
                            action.setEventKey(PopMenuEventEnum.onMenuSelected);
                            List<Condition> conditions = new ArrayList<>();
                            Condition condition = new Condition("{args[1].id}", SymbolType.equal, menuId);
                            conditions.add(condition);
                            action.setDesc(action.getDesc().equals("") ? caption : action.getDesc());
                            action.setConditions(conditions);
                            action.set_return(false);
                            this.addAction(action, false);
                        }
                    } else {
                        Action setParamsAction = new Action(PopMenuEventEnum.onMenuSelected);
                        setParamsAction.setArgs(Arrays.asList(new String[]{"{page." + component.getAlias() + ".setQueryData()}", null, null, "{args[0].tagVar}", ""}));
                        setParamsAction.setType(ActionTypeEnum.control);
                        setParamsAction.setDesc("设置扩展参数");
                        setParamsAction.setTarget(component.getAlias());
                        setParamsAction.setMethod("setQueryData");
                        setParamsAction.setRedirection("other:callback:call");
                        setParamsAction.setId(component.getAlias() + "_" + "setQueryData");
                        this.addAction(setParamsAction, false);

                        Action action = new Action(PopMenuEventEnum.onMenuSelected);
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
                        action.setId(component.getAlias() + "_" + "invoke");
                        this.addAction(action, false);
                    }

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
                    CustomConditionAction action = new CustomConditionAction(actionType, type, PopMenuEventEnum.onMenuSelected);
                    this.addAction(action);
                }
            } catch (Throwable e) {
                e.printStackTrace();
                // log.error("expression err[" + actionType.getExpression() + "]");
            }
        }
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

    public Boolean getShowCaption() {
        return showCaption;
    }

    public void setShowCaption(Boolean showCaption) {
        this.showCaption = showCaption;
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
    public Boolean isDyn() {
        return dyn;
    }

    @Override
    public EventPos getPos() {
        return pos;
    }

    @Override
    public void setPos(EventPos pos) {
        this.pos = pos;
    }

    @Override
    public void addTag(String key, Object object) {
        tagVar.put(key, object);
    }


    public Map<String, Object> getTagVar() {
        return tagVar;
    }

    public void setTagVar(Map<String, Object> tagVar) {
        this.tagVar = tagVar;
    }

    public Class getServiceClass() {
        if (serviceClass == null || serviceClass.equals(Void.class)) {
            this.serviceClass = MenuActionService.class;
        }
        return serviceClass;
    }


    @Override
    public int compareTo(PopDynBar o) {
        if (index == null) {
            return -1;
        }

        if (index != null && o.getIndex() != null) {
            return this.index - o.getIndex();
        }

        return 1;
    }

}