package net.ooder.esd.custom.component.index;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.esd.annotation.action.CustomLoadClassAction;
import net.ooder.esd.annotation.event.ActionTypeEnum;
import net.ooder.esd.annotation.event.CustomTreeEvent;
import net.ooder.esd.annotation.event.TabsEventEnum;
import net.ooder.esd.annotation.event.TreeViewEventEnum;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.annotation.ui.PosType;
import net.ooder.esd.annotation.ui.SymbolType;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.NavTreeComboViewBean;
import net.ooder.esd.bean.data.NavTreeDataBean;
import net.ooder.esd.custom.component.nav.CustomNavTreeComponent;
import net.ooder.esd.custom.properties.NavTabsProperties;
import net.ooder.esd.custom.properties.NavTreeProperties;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.*;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.Condition;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class IndexLeftTreeComponent extends CustomNavTreeComponent {

    @JSONField(serialize = false)
    private TabsComponent<NavTabsProperties> tabComponent;

    @JSONField(serialize = false)
    private LayoutComponent layoutComponent;


    public IndexLeftTreeComponent() {
        super();
    }

    public IndexLeftTreeComponent(EUModule module, MethodConfig methodConfig, LayoutComponent layoutComponent, Map<String, Object> valueMap) {
        super(module, methodConfig, valueMap);
        this.layoutComponent = layoutComponent;
        NavTreeComboViewBean navTreeViewBean = (NavTreeComboViewBean) methodConfig.getView();
        DivComponent leftComponent = new DivComponent("LeftDiv");
        leftComponent.getProperties().setDock(Dock.fill);
        NavTabsProperties tabsProperties = new NavTabsProperties(navTreeViewBean.getTabsViewBean());
        tabComponent = new TabsComponent(euModule.getName() + "Tab", tabsProperties);

        IndexLeftTopComponent leftTop = new IndexLeftTopComponent();
        leftComponent.addChildren(leftTop);
        leftComponent.addChildren(tabComponent);
        leftComponent.setTarget(PosType.main.name());

        Action showAction = new Action(CustomLoadClassAction.tabShow,TabsEventEnum.onIniPanelView);
        showAction.updateArgs(leftComponent.getAlias(), 4);
        tabComponent.addAction(showAction);
        layoutComponent.addChildren(tabComponent);
    }


    public void addChildNav(TreeViewComponent currComponent, NavTreeComboViewBean navTreeViewBean) {

        BlockComponent blockPanelComponent = new BlockComponent(Dock.fill, currComponent.getAlias() + "Panel");
        blockPanelComponent.setTarget(PosType.before.name());
        blockPanelComponent.addChildren(currComponent);
        Condition condition = new Condition("{args[1].euClassName}", SymbolType.nonempty, "");
        if (navTreeViewBean.getTabsViewBean().getAutoSave()!=null && navTreeViewBean.getTabsViewBean().getAutoSave()) {
            Action saveAction = new Action(TreeViewEventEnum.onItemSelected);
            saveAction.setDesc("保存表单");
            saveAction.setMethod("autoSave");
            saveAction.setType(ActionTypeEnum.control);
            saveAction.addCondition(condition);
            saveAction.setTarget(tabComponent.getAlias());
            currComponent.addAction(saveAction);
        }
        if (!navTreeViewBean.getTabsViewBean().getSingleOpen()) {
            Action removeAction = new Action(TreeViewEventEnum.onItemSelected);
            removeAction.setDesc("删除存在页");
            removeAction.setMethod("removeItems");
            removeAction.setType(ActionTypeEnum.control);
            removeAction.addCondition(condition);
            removeAction.setTarget(tabComponent.getAlias());
            removeAction.setArgs(Arrays.asList(new String[]{"{args[1].id}"}));
            currComponent.addAction(removeAction);
        } else {
            Action clearAction = new Action(TreeViewEventEnum.onItemSelected);
            clearAction.setDesc("清空");
            clearAction.setMethod("clearItems");
            clearAction.setType(ActionTypeEnum.control);
            clearAction.addCondition(condition);
            clearAction.setTarget(tabComponent.getAlias());
            currComponent.addAction(clearAction);
        }


        Action action = new Action(TreeViewEventEnum.onItemSelected);
        action.setDesc("添加TAB页");
        action.setMethod("insertItems2");
        action.setType(ActionTypeEnum.control);
        action.addCondition(condition);
        action.setTarget(tabComponent.getAlias());
        action.setArgs(Arrays.asList(new String[]{"{args[1]}"}));
        currComponent.addAction(action);
        Action tabclickItemAction = new Action(TreeViewEventEnum.onItemSelected);
        tabclickItemAction.setDesc("添加点击事件");
        tabclickItemAction.addCondition(condition);
        tabclickItemAction.setType(ActionTypeEnum.control);
        tabclickItemAction.setTarget(tabComponent.getAlias());
        tabclickItemAction.setMethod("fireItemClickEvent");
        tabclickItemAction.setArgs(Arrays.asList(new String[]{"{args[1].id}"}));

        currComponent.addAction(tabclickItemAction);

        NavTreeProperties properties = (NavTreeProperties) currComponent.getProperties();
        if (properties.getItems().size() > 0) {
            Action clickItemAction = new Action(TreeViewEventEnum.onRender);
            clickItemAction.setType(ActionTypeEnum.control);
            clickItemAction.setTarget(currComponent.getAlias());
            clickItemAction.setDesc("初始化");
            clickItemAction.setMethod("fireItemClickEvent");
            clickItemAction.setArgs(Arrays.asList(new String[]{properties.getFristId()}));
            currComponent.addAction( clickItemAction);
        }
        layoutComponent.addChildren(blockPanelComponent);
        super.addChildLayoutNav(layoutComponent);
        this.setCurrComponent(currComponent);

        try {
            fillViewAction(methodAPIBean);
            List<APICallerComponent> apiCallerComponents = genAPIComponent(navTreeViewBean.getTreeViewBean());
            this.addChildren(apiCallerComponents.toArray(new APICallerComponent[]{}));
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }


    protected void fillViewAction(MethodConfig methodConfig) {
        NavTreeDataBean dataBean = (NavTreeDataBean) methodConfig.getDataBean();

        if (dataBean != null) {
            if (dataBean.getDataUrl() != null && !dataBean.getDataUrl().equals("")) {
                this.dataUrl = dataBean.getDataUrl();
            }
            if (dataBean.getItemType() != null) {
                this.itemType = dataBean.getItemType();
            }

            if (dataBean.getLoadChildUrl() != null && !dataBean.getLoadChildUrl().equals("")) {
                this.loadChildUrl = dataBean.getLoadChildUrl();
            }

            if (dataUrl == null || dataUrl.equals("")) {
                MethodConfig methodAPIBean = methodConfig.getDataBean().getMethodEvent(CustomTreeEvent.TREERELOAD);
                if (methodAPIBean != null) {
                    dataUrl = methodAPIBean.getUrl();
                }

            }
            if (dataUrl == null || dataUrl.equals("")) {
                MethodConfig methodAPIBean = methodConfig.getDataBean().getMethodByItem(CustomMenuItem.RELOAD);
                if (methodAPIBean != null) {
                    dataUrl = methodAPIBean.getUrl();
                }

            }

            if (saveUrl == null || saveUrl.equals("")) {
                MethodConfig methodAPIBean = methodConfig.getDataBean().getMethodByItem(CustomMenuItem.SAVE);
                if (methodAPIBean != null) {
                    saveUrl = methodAPIBean.getUrl();
                }
            }

            if (loadChildUrl == null || loadChildUrl.equals("")) {
                MethodConfig methodAPIBean = methodConfig.getDataBean().getMethodEvent(CustomTreeEvent.RELOADCHILD);
                if (methodAPIBean != null) {
                    loadChildUrl = methodAPIBean.getUrl();
                }
            }

            if (editorPath == null || editorPath.equals("")) {
                MethodConfig methodAPIBean = methodConfig.getDataBean().getMethodEvent(CustomTreeEvent.TREENODEEDITOR);
                if (methodAPIBean != null) {
                    editorPath = methodAPIBean.getUrl();
                }
            }

            if (dataUrl == null || dataUrl.equals("")) {
                dataUrl = methodConfig.getUrl();
            }

        }


    }


}
