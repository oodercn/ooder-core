package net.ooder.esd.custom.component.nav;

import net.ooder.common.JDSException;
import net.ooder.esd.annotation.CustomMenu;
import net.ooder.esd.annotation.action.CustomLoadClassAction;
import net.ooder.esd.annotation.event.CustomFormEvent;
import net.ooder.esd.annotation.event.CustomTabsEvent;
import net.ooder.esd.annotation.event.TabsEventEnum;
import net.ooder.esd.annotation.menu.CustomFormMenu;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.CustomLayoutItemBean;
import net.ooder.esd.bean.view.CustomLayoutViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.NavFoldingComboViewBean;
import net.ooder.esd.custom.component.CustomModuleComponent;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.BlockComponent;
import net.ooder.esd.tool.component.LayoutComponent;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.LayoutProperties;
import net.ooder.esd.tool.properties.item.LayoutListItem;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class FullNavFoldingTreeComponent extends CustomModuleComponent<NavFoldingTreeComponent> {
    public FullNavFoldingTreeComponent() {
        super();
    }


    public FullNavFoldingTreeComponent(EUModule module, MethodConfig methodConfig, Map<String, Object> valueMap) throws ClassNotFoundException, JDSException {
        super(module, methodConfig, valueMap);
        NavFoldingComboViewBean foldingTreeViewBean = (NavFoldingComboViewBean) methodConfig.getView();
        CustomLayoutViewBean layoutViewBean = foldingTreeViewBean.getLayoutViewBean();

        LayoutComponent layoutComponent = getLayoutComponent(methodConfig.getName() + ComponentType.LAYOUT.name(), foldingTreeViewBean);
        BlockComponent contentComponent = new BlockComponent(Dock.fill, euModule.getName() + "Content");
        contentComponent.getProperties().setBorderType(BorderType.none);
        contentComponent.setTarget(PosType.main.name());
        layoutComponent.addChildren(contentComponent);
        NavFoldingTreeComponent tabcomponent = new NavFoldingTreeComponent(methodConfig, layoutComponent, valueMap);
        tabcomponent.setTarget(PosType.before.name());
        layoutComponent.addChildren(tabcomponent);
        Action tabShowAction = new Action(CustomLoadClassAction.tabShow,CustomTabsEvent.TABEDITOR.getEventEnum());
        tabShowAction.updateArgs(tabcomponent.getAlias(), 4);
//        if (customTreeViewBean != null && customTreeViewBean.getAutoReload() != null && customTreeViewBean.getAutoReload()) {
//            tabcomponent.addAction(tabShowAction);
//        } else {
//            tabShowAction.setEventKey(TabsEventEnum.onIniPanelView);
//            tabcomponent.addAction( tabShowAction);
//        }
        tabShowAction.setEventKey(TabsEventEnum.onIniPanelView);
        tabcomponent.addAction( tabShowAction);
        // tabcomponent.addEvent(TabsEventEnum.onIniPanelView, tabShowAction);

        NavFoldingComboViewBean viewBean = (NavFoldingComboViewBean) methodConfig.getView();

        this.fillAction(viewBean);
        fillTreeAction(viewBean);
        this.addChildLayoutNav(layoutComponent);
    }

    protected void fillTreeAction(NavFoldingComboViewBean treeViewBean) {

        Set<CustomFormEvent> customFormEvents = treeViewBean.getEvent();

        List<CustomFormMenu> customFormMenus = treeViewBean.getCustomMenu();
        if (customFormMenus != null && customFormMenus.size() > 0) {
            this.getMenuBar(treeViewBean.getMenuBar()).addMenu(customFormMenus.toArray(new CustomMenu[]{}));
        }


        List<CustomFormMenu> customBottombar = treeViewBean.getBottombarMenu();
        if (customBottombar != null && customBottombar.size() > 0) {
            this.getBottomBar(treeViewBean.getBottomBar()).addMenu(customBottombar.toArray(new CustomFormMenu[]{}));
        }


    }


    public LayoutComponent getLayoutComponent(String alias, NavFoldingComboViewBean foldingTreeViewBean) {
        CustomLayoutViewBean layoutViewBean = foldingTreeViewBean.getLayoutViewBean();
        LayoutComponent layoutComponent = new LayoutComponent(Dock.fill, euModule.getName() + ComponentType.LAYOUT.name());
        LayoutProperties layoutProperties = layoutComponent.getProperties();


        LayoutListItem beforItem = new LayoutListItem(PosType.before);
        LayoutListItem mainItem = new LayoutListItem(PosType.main);

        List<CustomLayoutItemBean> itemBeanList = layoutViewBean.getLayoutItems();
        for (CustomLayoutItemBean layoutItemBean : itemBeanList) {
            if (layoutItemBean.getPos().equals(PosType.before)) {
                beforItem = new LayoutListItem(layoutItemBean);
            } else if (layoutItemBean.getPos().equals(PosType.main)) {
                mainItem = new LayoutListItem(layoutItemBean);
            }
        }

        if (layoutViewBean != null) {
            layoutProperties = new LayoutProperties(layoutViewBean);
            layoutProperties.getItems().clear();
            layoutProperties.addItem(beforItem);
            layoutProperties.addItem(mainItem);
        } else {
            layoutProperties.setBorderType(BorderType.none);
            layoutProperties.setType(LayoutType.horizontal);
            layoutProperties.addItem(beforItem);
            layoutProperties.addItem(mainItem);
        }
        layoutComponent.setProperties(layoutProperties);
        layoutComponent.setAlias(alias);
        return layoutComponent;

    }


}
