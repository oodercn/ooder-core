package net.ooder.esd.custom.component.nav;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.action.CustomLoadClassAction;
import net.ooder.esd.annotation.event.ActionTypeEnum;
import net.ooder.esd.annotation.event.GalleryEventEnum;
import net.ooder.esd.annotation.event.ModuleEventEnum;
import net.ooder.esd.annotation.event.TabsEventEnum;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.CustomLayoutItemBean;
import net.ooder.esd.bean.view.CustomLayoutViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.CustomGalleryViewBean;
import net.ooder.esd.bean.view.NavGalleryComboViewBean;
import net.ooder.esd.custom.component.CustomModuleComponent;
import net.ooder.esd.custom.properties.NavTabsComponent;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.BlockComponent;
import net.ooder.esd.tool.component.GalleryComponent;
import net.ooder.esd.tool.component.LayoutComponent;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.LayoutProperties;
import net.ooder.esd.tool.properties.item.GalleryItem;
import net.ooder.esd.tool.properties.item.LayoutListItem;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CustomNavGalleryComponent extends CustomModuleComponent<GalleryComponent> {


    @JSONField(serialize = false)
    private GalleryComponent galleryComponent;
    @JSONField(serialize = false)
    private NavTabsComponent tabComponent;
    @JSONField(serialize = false)
    private LayoutComponent layoutComponent;


    public CustomNavGalleryComponent() {
        super();
    }


    public CustomNavGalleryComponent(EUModule module, MethodConfig methodConfig, Map<String, Object> valueMap) throws ClassNotFoundException {
        super(module, methodConfig, valueMap);
        NavGalleryComboViewBean navGalleryViewBean = (NavGalleryComboViewBean) methodConfig.getView();
        this.layoutComponent = getLayoutComponent(navGalleryViewBean);
        this.galleryComponent = new GalleryComponent(navGalleryViewBean.getGalleryViewBean());
        tabComponent = new NavTabsComponent(navGalleryViewBean.getTabsViewBean(), valueMap);
        tabComponent.getProperties().getItems().clear();
        tabComponent.setTarget(PosType.main.name());
        Action showAction = new Action(CustomLoadClassAction.tabShow, TabsEventEnum.onIniPanelView);
        showAction.updateArgs(tabComponent.getAlias(), 4);
        tabComponent.addAction(showAction);
        layoutComponent.addChildren(tabComponent);
        this.layoutComponent = getLayoutComponent(navGalleryViewBean);
        this.addChildNav(galleryComponent, navGalleryViewBean.getGalleryViewBean());
    }


    public void addChildNav(GalleryComponent currComponent, CustomGalleryViewBean navGalleryViewBean) {
        List<FieldModuleConfig> fieldModuleConfigList = navGalleryViewBean.getNavItems();
        if (currComponent.getProperties().getItems() != null && currComponent.getProperties().getItems().size() > 0) {
            Action clickItemAction = new Action(ModuleEventEnum.onRender);
            clickItemAction.setType(ActionTypeEnum.control);
            clickItemAction.setTarget(currComponent.getAlias());
            clickItemAction.setDesc("初始化");
            clickItemAction.setMethod("fireItemClickEvent");
            GalleryItem fristItem = currComponent.getProperties().getItems().get(0);
            clickItemAction.setArgs(Arrays.asList(new String[]{fristItem.getId()}));
            this.addAction(clickItemAction);
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
            moduleComponent.getModuleVar().putAll(methodConfig.getTagVar());
            contentComponent.addChildren(moduleComponent);

        }
        Action showAction = new Action(CustomLoadClassAction.GalleryShow, GalleryEventEnum.onItemSelected);
        showAction.updateArgs(layoutComponent.getAlias(), 4);
        currComponent.addAction(showAction);
        if (currComponent.getProperties().getItems() != null && currComponent.getProperties().getItems().size() > 0) {
            GalleryItem lastItem = currComponent.getProperties().getItems().get(currComponent.getProperties().getItems().size() - 1);
            currComponent.getProperties().setValue(lastItem.getId());
        }


        super.addChildLayoutNav(layoutComponent);

    }

    @JSONField(serialize = false)
    public LayoutComponent getLayoutComponent(NavGalleryComboViewBean viewbean) {
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
            layoutProperties.setType(LayoutType.vertical);
            layoutProperties.addItem(topItem);
            layoutProperties.addItem(mainItem);
        }
        layoutComponent.setProperties(layoutProperties);
        return layoutComponent;

    }

    public GalleryComponent getGalleryComponent() {
        return galleryComponent;
    }

    public void setGalleryComponent(GalleryComponent galleryComponent) {
        this.galleryComponent = galleryComponent;
    }

    public NavTabsComponent getTabComponent() {
        return tabComponent;
    }

    public void setTabComponent(NavTabsComponent tabComponent) {
        this.tabComponent = tabComponent;
    }

    public LayoutComponent getLayoutComponent() {
        return layoutComponent;
    }

    public void setLayoutComponent(LayoutComponent layoutComponent) {
        this.layoutComponent = layoutComponent;
    }
}
