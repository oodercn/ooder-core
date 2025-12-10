package net.ooder.esd.custom.component.nav;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.action.CustomLoadClassAction;
import net.ooder.esd.annotation.event.ActionTypeEnum;
import net.ooder.esd.annotation.event.MenuEventEnum;
import net.ooder.esd.annotation.event.ModuleEventEnum;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.CustomLayoutItemBean;
import net.ooder.esd.bean.view.CustomLayoutViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.bean.view.NavMenuBarViewBean;
import net.ooder.esd.custom.component.CustomModuleComponent;
import net.ooder.esd.custom.component.form.field.CustomFieldMenubarComponent;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.BlockComponent;
import net.ooder.esd.tool.component.LayoutComponent;
import net.ooder.esd.tool.component.MenuBarComponent;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.LayoutProperties;
import net.ooder.esd.tool.properties.item.LayoutListItem;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FullNavMenuBarComponent extends CustomModuleComponent<MenuBarComponent> {
    public FullNavMenuBarComponent() {
        super();
    }

    @JSONField(serialize = false)
    private LayoutComponent layoutComponent;


    public FullNavMenuBarComponent(EUModule module, MethodConfig customMethodAPIBean, Map<String, Object> valueMap) {
        super(module, customMethodAPIBean, valueMap);
        NavMenuBarViewBean viewBean = (NavMenuBarViewBean) customMethodAPIBean.getView();
        CustomFieldMenubarComponent menubarComponent = new CustomFieldMenubarComponent(euModule, methodAPIBean, valueMap);
        this.addChildNav(menubarComponent, viewBean);

    }

    public void addChildNav(MenuBarComponent currComponent, NavMenuBarViewBean menuBarViewBean) {
        this.layoutComponent = getLayoutComponent(menuBarViewBean);
        super.addChildLayoutNav(layoutComponent);
        this.setCurrComponent(currComponent);

        List<FieldModuleConfig> fieldModuleConfigList = menuBarViewBean.getNavItems();
        if (currComponent.getProperties().getItems() != null && currComponent.getProperties().getItems().size() > 0) {
            Action clickItemAction = new Action(ModuleEventEnum.onRender);
            clickItemAction.setType(ActionTypeEnum.control);
            clickItemAction.setTarget(currComponent.getAlias());
            clickItemAction.setDesc("初始化");
            clickItemAction.setMethod("fireItemClickEvent");
            TreeListItem fristItem = (TreeListItem) currComponent.getProperties().getItems().get(0);
            clickItemAction.setArgs(Arrays.asList(new String[]{fristItem.getId()}));
            this.addAction( clickItemAction);
        }
        currComponent.setTarget(PosType.before.name());
        BlockComponent contentComponent = new BlockComponent(Dock.fill, euModule.getName() + "Content");
        contentComponent.getProperties().setBorderType(BorderType.none);
        contentComponent.setTarget(PosType.main.name());
        layoutComponent.addChildren(currComponent);
        layoutComponent.addChildren(contentComponent);
        if (fieldModuleConfigList.size() > 0) {
            FieldModuleConfig moduleInfo = fieldModuleConfigList.get(0);
            MethodConfig methodConfig = moduleInfo.getMethodConfig();
            ModuleComponent moduleComponent = new ModuleComponent();
            moduleComponent.setClassName(methodConfig.getEUClassName());
            moduleComponent.setAlias(methodConfig.getName());
            moduleComponent.setTarget(contentComponent.getTarget());
            moduleComponent.getProperties().setTarget(moduleComponent.getTarget());
            moduleComponent.getModuleVar().putAll(methodConfig.getTagVar());
            contentComponent.addChildren(moduleComponent);

        }


        Action showAction = new Action(CustomLoadClassAction.IndexShow,MenuEventEnum.onMenuBtnClick);
        showAction.updateArgs(layoutComponent.getAlias(), 4);
        currComponent.addAction(showAction);
        if (currComponent.getProperties().getItems() != null && currComponent.getProperties().getItems().size() > 0) {
            TreeListItem lastItem = (TreeListItem) currComponent.getProperties().getItems().get(currComponent.getProperties().getItems().size() - 1);
            currComponent.getProperties().setValue(lastItem.getId());
        }




    }

    @JSONField(serialize = false)
    public LayoutComponent getLayoutComponent(NavMenuBarViewBean viewbean) {
        CustomLayoutViewBean layoutViewBean = viewbean.getLayoutViewBean();
        LayoutComponent layoutComponent = new LayoutComponent(Dock.fill, euModule.getName() + ComponentType.LAYOUT.name());
        LayoutProperties layoutProperties = layoutComponent.getProperties();
        LayoutListItem topItem = new LayoutListItem(PosType.before);
        LayoutListItem mainItem = new LayoutListItem(PosType.main);
        List<CustomLayoutItemBean> itemBeanList = layoutViewBean.getLayoutItems();
        for (CustomLayoutItemBean layoutItemBean : itemBeanList) {
            if (layoutItemBean.getPos().equals(PosType.before)) {
                topItem = new LayoutListItem(layoutItemBean);
            } else if (layoutItemBean.getPos().equals(PosType.main)) {
                mainItem = new LayoutListItem(layoutItemBean);
            }
        }

        if (layoutViewBean != null) {
            layoutProperties = new LayoutProperties(layoutViewBean);
            layoutProperties.getItems().clear();
            layoutProperties.addItem(topItem);
            layoutProperties.addItem(mainItem);
        } else {
            layoutProperties.setBorderType(BorderType.none);
            layoutProperties.setLayoutType(LayoutType.vertical);
            layoutProperties.addItem(topItem);
            layoutProperties.addItem(mainItem);
        }
        layoutComponent.setProperties(layoutProperties);
        return layoutComponent;

    }

}
