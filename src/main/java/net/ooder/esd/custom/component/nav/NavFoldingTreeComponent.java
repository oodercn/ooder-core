package net.ooder.esd.custom.component.nav;


import net.ooder.common.JDSException;
import net.ooder.esd.annotation.action.CustomLoadClassAction;
import net.ooder.esd.annotation.event.ActionTypeEnum;
import net.ooder.esd.annotation.event.TreeViewEventEnum;
import net.ooder.esd.annotation.ui.BorderType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.annotation.ui.PosType;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.custom.properties.NavFoldingTabsProperties;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.*;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.BlockProperties;
import net.ooder.esd.tool.properties.Event;
import net.ooder.esd.tool.properties.TreeViewProperties;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class NavFoldingTreeComponent extends FoldingTabsComponent {

    public NavFoldingTreeComponent(MethodConfig methodConfig, LayoutComponent layoutComponent, Map<String, ?> valueMap) throws JDSException {
        this.setProperties(new NavFoldingTabsProperties(methodConfig, valueMap));
        List<FieldModuleConfig> fieldModuleConfigList = methodConfig.getView().getNavItems();

        BlockComponent contentComponent = new BlockComponent(Dock.fill, methodConfig.getName() + "Content");
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
                Action showAction = new Action(CustomLoadClassAction.show2, TreeViewEventEnum.onItemSelected);
                showAction.updateArgs(layoutComponent.getAlias(), 4);
                leftTreePanel.addAction(showAction);
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
                    leftTreePanel.addAction(clickItemAction);
                }
                ESDFacrory.getAdminESDClient().saveModule(childModule, true);
            }
            treeComponent.getModuleVar().putAll(moduleInfo.getTagVar());
            this.addChildren(treeComponent);
        }
        this.setAlias(methodConfig.getName());
    }


    public NavFoldingTreeComponent(EUModule euModule, MethodConfig methodConfig, Map<String, ?> valueMap) throws JDSException {
        super();


    }


}
