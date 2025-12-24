package net.ooder.esd.custom.properties;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.EventKey;
import net.ooder.common.JDSConstants;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.CustomMenu;
import net.ooder.esd.annotation.event.ActionTypeEnum;
import net.ooder.esd.annotation.event.GridEventEnum;
import net.ooder.esd.annotation.event.TreeViewEventEnum;
import net.ooder.esd.annotation.menu.GridRowMenu;
import net.ooder.esd.annotation.menu.TreeRowMenu;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.RowCmdBean;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.bean.TreeRowCmdBean;
import net.ooder.esd.bean.bar.DynBar;
import net.ooder.esd.bean.bar.RowCmdDynBar;
import net.ooder.esd.bean.grid.GridRowCmdBean;
import net.ooder.esd.custom.action.ShowPageAction;
import net.ooder.esd.custom.component.CustomMTreeComponent;
import net.ooder.esd.custom.component.CustomTreeComponent;
import net.ooder.esd.custom.component.grid.CustomGridComponent;
import net.ooder.esd.custom.component.grid.CustomMGridComponent;
import net.ooder.esd.custom.component.nav.CustomNavTreeComponent;
import net.ooder.esd.tool.component.*;
import net.ooder.esd.tool.properties.*;
import net.ooder.esd.tool.properties.item.CmdItem;
import net.ooder.jds.core.esb.EsbUtil;
import net.ooder.jds.core.esb.task.ExcuteObj;

import java.util.*;

public class CustomCmdBar<T extends Component> implements DynBar<RowCmdDynBar, CmdItem> {
    private static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, CustomCmdBar.class);

    @JSONField(serialize = false)
    private List<CmdItem> cmdItems;
    @JSONField(serialize = false)
    private T currComponent;

    @JSONField(serialize = false)
    private TreeListItem treeListItem;
    @JSONField(serialize = false)
    Map<String, CmdItem> itemMap = new HashMap<>();

    @JSONField(serialize = false)
    RowCmdBean rowCmdBean;

    TagCmdsAlign tagCmdsAlign = TagCmdsAlign.floatright;

    CmdButtonType buttonType = CmdButtonType.text;

    String itemStyle;

    CmdTPosType pos = CmdTPosType.row;

    String id;

    public CustomCmdBar(TreeRowCmdBean treeRowCmdBean, T currComponent, CustomTreeComponent module) {
        init(treeRowCmdBean, currComponent, module);
    }

    public CustomCmdBar(TreeRowCmdBean treeRowCmdBean, T currComponent, CustomNavTreeComponent module) {
        init(treeRowCmdBean, currComponent, module);
    }


    public CustomCmdBar(TreeRowCmdBean treeRowCmdBean, T currComponent, CustomMTreeComponent module) {
        init(treeRowCmdBean, currComponent, module);
    }

    public CustomCmdBar(GridRowCmdBean rowCmdBean, T currComponent, CustomGridComponent module) {
        init(rowCmdBean, currComponent, module);
    }

    public CustomCmdBar(GridRowCmdBean rowCmdBean, T currComponent, CustomMGridComponent module) {
        init(rowCmdBean, currComponent, module);
    }

    public CustomCmdBar(TreeRowCmdBean rowCmdBean, TreeListItem treeListItem, ModuleComponent module) {
        this.tagCmdsAlign = rowCmdBean.getTagCmdsAlign();
        this.buttonType = rowCmdBean.getButtonType();
        this.itemStyle = rowCmdBean.getItemStyle();
        this.id = rowCmdBean.getId();
        this.rowCmdBean = rowCmdBean;

        if (id == null || id.equals("")) {
            id = treeListItem.getId() + "CMD";
        }
        this.treeListItem = treeListItem;
        cmdItems = this.getTagCmds();
        if (cmdItems == null) {
            cmdItems = new ArrayList<>();
        }
        for (CmdItem item : cmdItems) {
            itemMap.put(item.getId(), item);
        }


        TreeRowCmdBean treeRowMenuBean = rowCmdBean;
        for (TreeRowMenu rowMenu : treeRowMenuBean.getRowMenu()) {
            addMenu(rowMenu);
        }


        if (module != null) {
            Set<ComponentType> bindTypes = rowCmdBean.getBindTypes();
            Class[] objs = rowCmdBean.getMenuClass();
            for (Class obj : objs) {
                try {
                    module.addBindService(obj, this, bindTypes.toArray(new ComponentType[]{}));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

    }

    void init(RowCmdBean rowCmdBean, T currComponent, ModuleComponent module) {
        this.tagCmdsAlign = rowCmdBean.getTagCmdsAlign();
        this.buttonType = rowCmdBean.getButtonType();
        this.itemStyle = rowCmdBean.getItemStyle();
        this.id = rowCmdBean.getId();
        this.rowCmdBean = rowCmdBean;

        if (id == null || id.equals("")) {
            id = currComponent.getAlias() + "CMD";
        }
        this.currComponent = currComponent;
        cmdItems = this.getTagCmds();
        if (cmdItems == null) {
            cmdItems = new ArrayList<>();
        }
        for (CmdItem item : cmdItems) {
            itemMap.put(item.getId(), item);
        }

        if (rowCmdBean instanceof GridRowCmdBean) {
            GridRowCmdBean gridRowMenuBean = (GridRowCmdBean) rowCmdBean;
            for (GridRowMenu rowMenu : gridRowMenuBean.getRowMenu()) {
                addMenu(rowMenu);
            }
            this.pos = gridRowMenuBean.getPos();
        } else if (rowCmdBean instanceof TreeRowCmdBean) {
            TreeRowCmdBean treeRowMenuBean = (TreeRowCmdBean) rowCmdBean;
            for (TreeRowMenu rowMenu : treeRowMenuBean.getRowMenu()) {
                addMenu(rowMenu);
            }
        }


        Set<ComponentType> bindTypes = rowCmdBean.getBindTypes();
        Class[] objs = rowCmdBean.getMenuClass();
        for (Class obj : objs) {
            try {
                module.addBindService(obj, this, bindTypes.toArray(new ComponentType[]{}));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public CustomCmdBar(T currComponent, TagCmdsAlign tagCmdsAlign) {
        this.currComponent = currComponent;
        if (tagCmdsAlign != null) {
            this.tagCmdsAlign = tagCmdsAlign;
        }
        cmdItems = this.getTagCmds();
        if (cmdItems == null) {
            cmdItems = new ArrayList<>();
        }
        for (CmdItem item : cmdItems) {
            itemMap.put(item.getId(), item);
        }
    }

    @Override
    public boolean initMenuClass(Class bindClazz) {

        return true;
    }


    private CmdItem createItem(APICallerComponent component) {
        String menuId = component.getAlias() + ComboInputType.button.name();
        CmdItem menuItem = itemMap.get(menuId);
        APICallerProperties properties = component.getProperties();
        if (menuItem == null) {
            menuItem = new CmdItem(menuId, properties.getDesc(), properties.getImageClass());
            menuItem.setTagCmdsAlign(tagCmdsAlign);
            if (properties.getImageClass() != null && !properties.getImageClass().equals("")) {
                menuItem.setImageClass(properties.getImageClass());
            }

            if (itemStyle != null && itemStyle.equals("")) {
                menuItem.setItemStyle(this.itemStyle);
            }
            if (rowCmdBean != null && !rowCmdBean.getShowCaption()) {
                menuItem.setCaption("");
            }
            menuItem.setTips(properties.getDesc());
            menuItem.setButtonType(buttonType);
            menuItem.setPos(pos);
            cmdItems.add(menuItem);
            itemMap.put(menuId, menuItem);
        } else {
            if (properties.getDesc() != null && !properties.getDesc().equals("")) {
                menuItem.setCaption(properties.getDesc());
            }
            if (properties.getExpression() != null && !properties.getExpression().equals("")) {
                menuItem.setExpression(properties.getExpression());
            }
            if (properties.getImageClass() != null && !properties.getImageClass().equals("")) {
                menuItem.setImageClass(properties.getImageClass());
            }
        }
        return menuItem;
    }


    @Override
    public void addSplit(String id) {
        CmdItem menuItem = new CmdItem(CmdButtonType.split, id);
        menuItem.setTagCmdsAlign(this.tagCmdsAlign);
        menuItem.setPos(pos);
        cmdItems.add(menuItem);
    }


    public List<CmdItem> addMenu(CustomMenu... types) {
        List<CmdItem> treeListItems = new ArrayList<>();
        for (CustomMenu type : types) {
            if (!type.type().equals("")) {
                String menuId = type.type() + ComboInputType.button.name();
                CmdItem menuItem = itemMap.get(menuId);
                if (menuItem == null) {
                    menuItem = new CmdItem(type);
                    menuItem.setId(menuId);
                    menuItem.setTagCmdsAlign(this.tagCmdsAlign);
                    if (type.imageClass() != null && !type.imageClass().equals("")) {
                        menuItem.setImageClass(type.imageClass());
                    }
                    if (rowCmdBean != null && !rowCmdBean.getShowCaption()) {
                        menuItem.setCaption("");
                    } else {
                        menuItem.setCaption(type.caption());
                    }
                    menuItem.setTips(type.caption());
                    menuItem.setButtonType(buttonType);
                    menuItem.setPos(pos);
                    cmdItems.add(menuItem);
                    itemMap.put(menuId, menuItem);
                } else {
                    if (!type.caption().equals("") && rowCmdBean != null && !rowCmdBean.getShowCaption()) {
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
            setTagCmds(cmdItems);
        }
        return treeListItems;
    }

    public RowCmdBean getRowCmdBean() {
        return rowCmdBean;
    }

    public void setRowCmdBean(RowCmdBean rowCmdBean) {
        this.rowCmdBean = rowCmdBean;
    }

    private List<Condition> createCondition(String menuId) {
        Condition condition = new Condition("{args[2]}", SymbolType.equal, menuId);
        List<Condition> conditions = new ArrayList<>();
        conditions.add(condition);
        return conditions;
    }

    private List<CmdItem> getTagCmds() {
        if (treeListItem != null) {
            return treeListItem.getTagCmds();
        } else if (currComponent instanceof TreeGridComponent) {
            return ((GridProperties) currComponent.getProperties()).getTagCmds();
        } else if (currComponent instanceof TreeViewComponent) {
            return ((TreeViewProperties) currComponent.getProperties()).getTagCmds();
        } else if (currComponent instanceof MTreeViewComponent) {
            return ((TreeViewProperties) currComponent.getProperties()).getTagCmds();
        } else if (currComponent instanceof TreeBarComponent) {
            return ((TreeBarProperties) currComponent.getProperties()).getTagCmds();
        }
        return new ArrayList<>();

    }

    private void setTagCmds(List<CmdItem> cmdItems) {
        if (treeListItem != null) {
            treeListItem.setTagCmds(cmdItems);
        } else if (currComponent instanceof TreeGridComponent) {
            ((GridProperties) currComponent.getProperties()).setTagCmds(cmdItems);
        } else if (currComponent instanceof TreeViewComponent) {
            ((TreeViewProperties) currComponent.getProperties()).setTagCmds(cmdItems);
        } else if (currComponent instanceof MTreeViewComponent) {
            ((TreeViewProperties) currComponent.getProperties()).setTagCmds(cmdItems);
        } else if (currComponent instanceof TreeBarComponent) {
            ((TreeBarProperties) currComponent.getProperties()).setTagCmds(cmdItems);
        }
    }

    private void addAction(Action action) {
        if (currComponent != null) {
            if (currComponent instanceof TreeGridComponent) {
                currComponent.addAction(action);
            } else if (currComponent instanceof TreeBarComponent) {
                currComponent.addAction(action);
            } else if (currComponent instanceof TreeViewComponent) {
                currComponent.addAction(action);
            } else if (currComponent instanceof MTreeViewComponent) {
                currComponent.addAction(action);
            }
        }

    }


    private EventKey getEventKey() {
        EventKey eventKey = TreeViewEventEnum.onCmd;
        if (currComponent != null) {
            if (currComponent instanceof TreeGridComponent) {
                eventKey = GridEventEnum.onCmd;
            } else if (currComponent instanceof TreeBarComponent) {
                eventKey = TreeViewEventEnum.onCmd;
            } else if (currComponent instanceof TreeViewComponent) {
                eventKey = TreeViewEventEnum.onCmd;
            } else if (currComponent instanceof MTreeViewComponent) {
                eventKey = TreeViewEventEnum.onCmd;
            }
        }
        return eventKey;
    }

    public void addMenu(String className, APICallerComponent component) {
        String expression = component.getProperties().getExpression();
        if (expression == null || expression.equals("") || EsbUtil.parExpression(expression, Boolean.class)) {
            CmdItem item = createItem(component);
            ShowPageAction action = new ShowPageAction(getEventKey());
            action.setDesc("点击" + component.getAlias());
            action.updateArgs("{args[1]}", 5);
            action.setTarget(className);
            action.setClassName(className);
            action.setRedirection("page:" + className + ":show2");
            action.setConditions(createCondition(item.getId()));
            action.set_return(false);
            this.addAction(action);

        }
        setTagCmds(cmdItems);
    }


    public void addMenu(APICallerComponent component) {
        String expression = component.getProperties().getExpression();
        if (expression == null || expression.equals("") || EsbUtil.parExpression(expression, Boolean.class)) {
            CmdItem item = createItem(component);
            Set<Action> actions = component.getActions();
            if (actions != null && actions.size() > 0) {
                for (Action action : actions) {
                    action.setDesc((action.getDesc() == null || action.getDesc().equals("")) ? component.getProperties().getDesc() : action.getDesc());
                    action.set_return(false);
                    action.setConditions(createCondition(item.getId()));
                    this.addAction(action);
                }
            } else {
                Action setParamsAction = new Action(getEventKey());
                setParamsAction.setArgs(Arrays.asList(new String[]{"{page." + component.getAlias() + ".setQueryData()}", null, null, "{args[1].tagVar}", ""}));
                setParamsAction.setType(ActionTypeEnum.control);
                setParamsAction.setTarget(component.getAlias());
                setParamsAction.setDesc(component.getProperties().getDesc());
                setParamsAction.setMethod("setQueryData");
                setParamsAction.setConditions(createCondition(item.getId()));
                setParamsAction.setRedirection("other:callback:call");
                //setParamsAction.set_return(true);
                this.addAction(setParamsAction);

                Action action = new Action(getEventKey());
                action.setArgs(Arrays.asList(new String[]{"{page." + component.getAlias() + ".invoke()}"}));
                action.setType(ActionTypeEnum.control);
                action.setTarget(component.getAlias());
                action.setDesc(component.getProperties().getDesc());
                action.setMethod("invoke");
                action.setConditions(createCondition(item.getId()));
                action.setRedirection("other:callback:call");
                action.set_return(false);
                this.addAction(action);
            }


        }
        setTagCmds(cmdItems);
    }

    public void fillActions(CustomMenu type) {
        CustomAction[] workListActionTypes = type.actions();
        for (CustomAction actionType : workListActionTypes) {
            try {
                if (type.expression() == null || type.expression().equals("") || EsbUtil.parExpression(type.expression(), Boolean.class)) {
                    String menuId = type.type() + ComboInputType.button.name();
                    Action action = new Action(getEventKey());
                    action.setArgs(Arrays.asList(new String[]{"{page." + type.type() + ".invoke()}"}));
                    action.setType(ActionTypeEnum.control);
                    action.setTarget(type.type());
                    action.setDesc(type.caption());
                    action.setMethod("invoke");
                    action.setRedirection("other:callback:call");
                    action.setConditions(createCondition(menuId));
                    action.set_return(false);
                    this.addAction(action);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public List<CmdItem> filter(Object rowData) {

        List<CmdItem> menuTypes = new ArrayList<CmdItem>();
        List<CmdItem> items = this.getCmdItems();
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

    public TreeListItem getTreeListItem() {
        return treeListItem;
    }

    public void setTreeListItem(TreeListItem treeListItem) {
        this.treeListItem = treeListItem;
    }

    public List<CmdItem> getCmdItems() {
        return cmdItems;
    }

    public void setCmdItems(List<CmdItem> cmdItems) {
        this.cmdItems = cmdItems;
    }

    public TagCmdsAlign getTagCmdsAlign() {
        return tagCmdsAlign;
    }

    public void setTagCmdsAlign(TagCmdsAlign tagCmdsAlign) {
        this.tagCmdsAlign = tagCmdsAlign;
    }

}
