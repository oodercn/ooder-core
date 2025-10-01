package net.ooder.esd.custom.component.nav;


import net.ooder.common.JDSException;
import net.ooder.esd.annotation.event.ActionTypeEnum;
import net.ooder.esd.annotation.event.TreeViewEventEnum;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.bean.view.NavFoldingComboViewBean;
import net.ooder.esd.custom.properties.NavFoldingTabsProperties;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.*;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class NavFoldingTabComponent extends FoldingTabsComponent {

    public NavFoldingTabComponent(NavFoldingComboViewBean viewBean, TabsComponent tabsComponent, Map<String, ?> valueMap) throws JDSException {
        this.setProperties( new NavFoldingTabsProperties(viewBean, valueMap));
        List<FieldModuleConfig> fieldModuleConfigList = viewBean.getNavItems();

        BlockComponent contentComponent = new BlockComponent(Dock.fill, viewBean.getName() + "Content");
        contentComponent.getProperties().setBorderType(BorderType.none);
        contentComponent.setTarget(PosType.main.name());

        for (FieldModuleConfig moduleInfo : fieldModuleConfigList) {
            EUModule<ModuleComponent> childModule = moduleInfo.getModule(valueMap);
            ModuleComponent treeComponent = childModule.getComponent();
            treeComponent.getProperties().setBorderType(BorderType.none);
            TreeViewComponent leftTreePanel = (TreeViewComponent) treeComponent.getCurrComponent();
            if (leftTreePanel == null) {
                leftTreePanel = (TreeViewComponent) treeComponent.findComponents(ComponentType.TREEVIEW, null).get(0);
            }
            Map<TreeViewEventEnum, Event> eventMap = leftTreePanel.getEvents();
            Event event = eventMap.get(TreeViewEventEnum.onItemSelected);
            if (event == null) {
                Condition condition = new Condition("{args[1].euClassName}", SymbolType.nonempty, "");
                if (!viewBean.getTabsViewBean().getSingleOpen()) {
                    Action removeAction = new Action(TreeViewEventEnum.onItemSelected);
                    removeAction.setDesc("删除存在页");
                    removeAction.setMethod("call");
                    removeAction.setType(ActionTypeEnum.other);
                    removeAction.addCondition(condition);
                    removeAction.setTarget("callback");
                    removeAction.setArgs(Arrays.asList(new String[]{"{page.parentModule." + tabsComponent.getAlias() + ".removeItems()}", null, null, "{args[1].id}"}));
                    leftTreePanel.addAction( removeAction);
                } else {
                    Action clearAction = new Action(TreeViewEventEnum.onItemSelected);
                    clearAction.setDesc("清空");
                    clearAction.setMethod("call");
                    clearAction.setType(ActionTypeEnum.other);
                    clearAction.addCondition(condition);
                    clearAction.setTarget("callback");
                    clearAction.setArgs(Arrays.asList(new String[]{"{page.parentModule." + tabsComponent.getAlias() + ".clearItems()}"}));

                    clearAction.addCondition(condition);
                    leftTreePanel.addAction( clearAction);
                }


                Action action = new Action(TreeViewEventEnum.onItemSelected);
                action.setDesc("添加TAB页");

                action.addCondition(condition);
                action.setMethod("call");
                action.setType(ActionTypeEnum.other);
                action.addCondition(condition);
                action.setTarget("callback");
                action.setArgs(Arrays.asList(new String[]{"{page.parentModule." + tabsComponent.getAlias() + ".insertItems2()}", null, null, "{args[1]}"}));


                leftTreePanel.addAction( action);
                Action tabclickItemAction = new Action(TreeViewEventEnum.onItemSelected);
                tabclickItemAction.setDesc("添加点击事件");


                tabclickItemAction.addCondition(condition);
                tabclickItemAction.setMethod("call");
                tabclickItemAction.setType(ActionTypeEnum.other);
                tabclickItemAction.addCondition(condition);
                tabclickItemAction.setTarget("callback");
                tabclickItemAction.setArgs(Arrays.asList(new String[]{"{page.parentModule." + tabsComponent.getAlias() + ".fireItemClickEvent()}", null, null, "{args[1].id}"}));

                leftTreePanel.addAction( tabclickItemAction);


                TreeViewProperties properties = leftTreePanel.getProperties();
                Component component = treeComponent.getTopComponentBox();
                if (component != null && component instanceof BlockComponent) {
                    ((BlockProperties) component.getProperties()).setBorderType(BorderType.none);
                }
                List<TreeListItem> items = properties.getItems();

                if (items != null && items.size() > 0) {
                    if (items.size() == 1 && items.get(0).getSub() != null && items.get(0).getSub().size() > 0) {
                        List<TreeListItem> subItems = items.get(0).getSub();
                        properties.setItems(subItems);
                    }
                    Action clickItemAction = new Action(TreeViewEventEnum.onRender);
                    clickItemAction.setType(ActionTypeEnum.control);
                    clickItemAction.setTarget(leftTreePanel.getAlias());
                    clickItemAction.setDesc("初始化");
                    clickItemAction.setMethod("fireItemClickEvent");
                    clickItemAction.setArgs(Arrays.asList(new String[]{properties.getFristId()}));
                    leftTreePanel.addAction( clickItemAction);
                }
                ESDFacrory.getAdminESDClient().saveModule(childModule,true);
            }

            ModuleComponent moduleComponent = new ModuleComponent();
            moduleComponent.setClassName(childModule.getClassName());
            moduleComponent.setAlias(childModule.getComponent().getAlias());
            moduleComponent.setTarget(childModule.getComponent().getTarget());
            moduleComponent.getModuleVar().putAll(moduleInfo.getTagVar());
            this.addChildren(moduleComponent);
        }
        this.setAlias(viewBean.getName());
    }


    public NavFoldingTabComponent(EUModule euModule, MethodConfig methodConfig, Map<String, ?> valueMap) throws JDSException {
        super();
    }


}
