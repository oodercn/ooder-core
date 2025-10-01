package net.ooder.esd.custom.component.nav;

import net.ooder.common.JDSException;
import net.ooder.esd.annotation.CustomMenu;
import net.ooder.esd.annotation.action.CustomLoadClassAction;
import net.ooder.esd.annotation.event.TabsEventEnum;
import net.ooder.esd.annotation.event.CustomFormEvent;
import net.ooder.esd.annotation.menu.CustomFormMenu;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.CustomLayoutItemBean;
import net.ooder.esd.bean.view.CustomLayoutViewBean;
import net.ooder.esd.bean.view.CustomTreeViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.NavFoldingComboViewBean;
import net.ooder.esd.bean.view.TabsViewBean;
import net.ooder.esd.custom.component.CustomModuleComponent;
import net.ooder.esd.custom.properties.NavTabsProperties;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.BlockComponent;
import net.ooder.esd.tool.component.LayoutComponent;
import net.ooder.esd.tool.component.TabsComponent;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.LayoutProperties;
import net.ooder.esd.tool.properties.item.LayoutListItem;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class FullNavFoldingComponent extends CustomModuleComponent<NavFoldingTreeComponent> {
    public FullNavFoldingComponent() {
        super();
    }


    public FullNavFoldingComponent(EUModule module, MethodConfig methodConfig, Map<String, Object> valueMap) throws JDSException {
        super(module, methodConfig, valueMap);
        NavFoldingComboViewBean foldingTreeViewBean = (NavFoldingComboViewBean) methodConfig.getView();
        CustomTreeViewBean customTreeViewBean = foldingTreeViewBean.getTreeViewBeans().get(0);
        NavFoldingComboViewBean viewBean = (NavFoldingComboViewBean) methodConfig.getView();

        LayoutComponent layoutComponent = getLayoutComponent(methodConfig.getName() + ComponentType.LAYOUT.name(), foldingTreeViewBean);
        BlockComponent contentComponent = new BlockComponent(Dock.fill, euModule.getName() + "Content");
        contentComponent.getProperties().setBorderType(BorderType.none);
        contentComponent.setTarget(PosType.main.name());


        NavTabsProperties tabsProperties = new NavTabsProperties(viewBean.getTabsViewBean());
        TabsComponent tabContentComponent = new TabsComponent(euModule.getName() + "Tab", tabsProperties);
        tabContentComponent.setTarget("main");
        Action showAction = new Action(CustomLoadClassAction.tabShow,TabsEventEnum.onIniPanelView);
        showAction.updateArgs(tabContentComponent.getAlias(), 4);
        tabContentComponent.addAction( showAction);
        contentComponent.addChildren(tabContentComponent);

        layoutComponent.addChildren(contentComponent);
        NavFoldingTabComponent tabcomponent = new NavFoldingTabComponent(viewBean, tabContentComponent, valueMap);


        tabcomponent.setTarget(PosType.before.name());
        layoutComponent.addChildren(tabcomponent);
        Action tabShowAction = new Action(CustomLoadClassAction.tabShow,TabsEventEnum.onItemSelected);
        tabShowAction.updateArgs(tabcomponent.getAlias(), 4);
        tabcomponent.addAction(tabShowAction);
        if (customTreeViewBean != null && customTreeViewBean.getAutoReload() != null && customTreeViewBean.getAutoReload()) {
            tabcomponent.addAction(tabShowAction);
        } else {
            tabShowAction.setEventKey(TabsEventEnum.onIniPanelView);
            tabcomponent.addAction(tabShowAction);
        }

        // tabcomponent.addEvent(TabsEventEnum.onIniPanelView, tabShowAction);


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
        CustomTreeViewBean treeViewBean = foldingTreeViewBean.getTreeViewBeans().get(0);
        TabsViewBean tabsViewBean = foldingTreeViewBean.getTabsViewBean();
        LayoutComponent layoutComponent = new LayoutComponent(Dock.fill, euModule.getName() + ComponentType.LAYOUT.name());
        LayoutProperties layoutProperties = layoutComponent.getProperties();
        CustomLayoutItemBean beforBean = null;
        if (treeViewBean.getMethodConfig() != null) {
            beforBean = treeViewBean.getMethodConfig().getLayoutItem();
        }
        LayoutListItem beforItem = null;
        if (beforBean != null) {
            beforItem = new LayoutListItem(beforBean);
        } else {
            beforItem = new LayoutListItem(PosType.before);
        }

        CustomLayoutItemBean mainBean = null;
        if (tabsViewBean.getMethodConfig() != null) {
            mainBean = tabsViewBean.getMethodConfig().getLayoutItem();
        }
        LayoutListItem mainItem = null;
        if (mainBean != null) {
            mainItem = new LayoutListItem(mainBean);
        } else {
            mainItem = new LayoutListItem(PosType.main);
        }

        if (layoutViewBean != null) {
            layoutProperties = new LayoutProperties(layoutViewBean);
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
